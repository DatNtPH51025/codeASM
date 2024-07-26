package com.example.asm_ph51025_and102;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmailRE, txtName, txtPassRE, txtFogotRE;
    private Button btnResig;
    private TextView txtLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUi();

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnResig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUi() {
        txtEmailRE = findViewById(R.id.txtEmailRE);
        txtName = findViewById(R.id.txtName);
        txtPassRE = findViewById(R.id.txtPassRE);
        txtFogotRE = findViewById(R.id.txtFogotRE);
        btnResig = findViewById(R.id.btnResig);
        txtLogin = findViewById(R.id.txtLogin);
    }

    private void registerUser() {
        String email = txtEmailRE.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String password = txtPassRE.getText().toString().trim();
        String confirmPassword = txtFogotRE.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            txtEmailRE.setError("Email không được để trống");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            txtName.setError("Họ tên không được để trống");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassRE.setError("Mật khẩu không được để trống");
            return;
        }

        if (!password.equals(confirmPassword)) {
            txtFogotRE.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        progressDialog.setMessage("Đang đăng ký...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build());
                            }
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

package com.example.asm_ph51025_and102;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.asm_ph51025_and102.DAO.NguoiDungDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPass = findViewById(R.id.txtPass);
        TextView txtFogot = findViewById(R.id.txtFogot);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnResig = findViewById(R.id.btnResig);

        btnLogin.setOnClickListener(view -> {
            String email = txtEmail.getText().toString().trim();
            String pass = txtPass.getText().toString().trim();

            // Kiểm tra đầu vào
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra đăng nhập
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc Activity hiện tại
                } else {
                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnResig.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        txtFogot.setOnClickListener(view -> ShowDialogForgotPass());
    }

    private void ShowDialogForgotPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_forgotpass, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText txtEmailSend = view.findViewById(R.id.txtEmailSend);
        Button btnGui = view.findViewById(R.id.btngui);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        btnHuy.setOnClickListener(view1 -> dialog.dismiss());

        btnGui.setOnClickListener(view1 -> {
            String email = txtEmailSend.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sử dụng Firebase để gửi liên kết khôi phục mật khẩu
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email đã được gửi", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

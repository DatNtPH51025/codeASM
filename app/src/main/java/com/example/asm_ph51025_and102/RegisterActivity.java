package com.example.asm_ph51025_and102;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText edtEmail = findViewById(R.id.txtEmailRE);
        EditText edtPass = findViewById(R.id.txtPassRE);
        EditText edtHoten = findViewById(R.id.txtName);
        Button btnRegister = findViewById(R.id.btnResig);

        btnRegister.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();
            String hoten = edtHoten.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty() || hoten.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lưu thông tin người dùng vào Firestore
                            String userId = mAuth.getCurrentUser().getUid();
                            User user = new User(email, hoten);

                            db.collection("NGUOIDUNG").document(userId)
                                    .set(user)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish(); // Kết thúc Activity hiện tại
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Lỗi lưu dữ liệu: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Lớp User để lưu vào Firestore
    public static class User {
        private String email;
        private String hoten;

        public User(String email, String hoten) {
            this.email = email;
            this.hoten = hoten;
        }

        public String getEmail() {
            return email;
        }

        public String getHoten() {
            return hoten;
        }
    }
}

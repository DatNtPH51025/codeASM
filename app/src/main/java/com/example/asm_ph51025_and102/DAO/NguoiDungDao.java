package com.example.asm_ph51025_and102.DAO;

import android.content.Context;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NguoiDungDao {
    private static FirebaseFirestore db;

    public NguoiDungDao(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    // Đăng nhập
    public void login(String email, String password, OnCompleteListener<Boolean> listener) {
        db.collection("NGUOIDUNG")
                .whereEqualTo("tendangnhap", email)
                .whereEqualTo("matkhau", password) // Cần mã hóa mật khẩu trong thực tế
                .get()
                .addOnCompleteListener(task -> {
                    boolean isLoginSuccessful = task.isSuccessful() && !task.getResult().isEmpty();
                    listener.onComplete(Tasks.forResult(isLoginSuccessful));
                });
    }

    // Đăng ký
    public void register(String username, String password, String hoten, OnCompleteListener<Boolean> listener) {
        Map<String, Object> user = new HashMap<>();
        user.put("tendangnhap", username);
        user.put("matkhau", password); // Cần mã hóa mật khẩu trong thực tế
        user.put("hoten", hoten);

        db.collection("NGUOIDUNG").document(username)
                .set(user)
                .addOnSuccessListener(aVoid -> listener.onComplete(Tasks.forResult(true)))
                .addOnFailureListener(e -> listener.onComplete(Tasks.forException(e)));
    }

    // Quên mật khẩu
    public static void forgotPassword(String email, OnCompleteListener<String> listener) {
        db.collection("NGUOIDUNG").document(email)
                .get()
                .addOnCompleteListener(task -> {
                    String password = task.isSuccessful() && task.getResult().exists()
                            ? task.getResult().getString("matkhau")
                            : null;
                    listener.onComplete(Tasks.forResult(password));
                });
    }
}

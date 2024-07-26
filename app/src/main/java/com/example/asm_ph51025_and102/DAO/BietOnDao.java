package com.example.asm_ph51025_and102.DAO;

import com.example.asm_ph51025_and102.DTO.BietOn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BietOnDao {
    private FirebaseFirestore db;
    private CollectionReference bietOnCollection;

    public BietOnDao() {
        db = FirebaseFirestore.getInstance();
        bietOnCollection = db.collection("BietOn"); // Đảm bảo tên collection đúng
    }

    // Lấy danh sách biết ơn
    public void getAllBietOn(final OnCompleteListener<ArrayList<BietOn>> listener) {
        bietOnCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<BietOn> list = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    BietOn bo = document.toObject(BietOn.class);
                    list.add(bo);
                }
                listener.onComplete(Tasks.forResult(list));
            } else {
                listener.onComplete(Tasks.forException(task.getException()));
            }
        });
    }

    // Thêm biết ơn
    public void ThemBietOn(BietOn bo, OnCompleteListener<Void> listener) {
        bietOnCollection.add(bo)
                .addOnSuccessListener(documentReference -> listener.onComplete(Tasks.forResult(null)))
                .addOnFailureListener(e -> listener.onComplete(Tasks.forException(e)));
    }

    // Sửa biết ơn
    public void chinhSuaBO(BietOn bo, OnCompleteListener<Void> listener) {
        DocumentReference docRef = bietOnCollection.document(String.valueOf(bo.getId()));
        Map<String, Object> updates = new HashMap<>();
        updates.put("tenTD", bo.getTenTD()); // Đảm bảo tên trường đúng
        updates.put("ND", bo.getND()); // Đảm bảo tên trường đúng

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> listener.onComplete(Tasks.forResult(null)))
                .addOnFailureListener(e -> listener.onComplete(Tasks.forException(e)));
    }

    // Xóa biết ơn
    public void XoaBietOn(int maBO, OnCompleteListener<Void> listener) {
        DocumentReference docRef = bietOnCollection.document(String.valueOf(maBO));
        docRef.delete()
                .addOnSuccessListener(aVoid -> listener.onComplete(Tasks.forResult(null)))
                .addOnFailureListener(e -> listener.onComplete(Tasks.forException(e)));
    }
}

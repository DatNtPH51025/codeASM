package com.example.asm_ph51025_and102.Fragment;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ph51025_and102.Adapter.BietOnAdapter;
import com.example.asm_ph51025_and102.DTO.BietOn;
import com.example.asm_ph51025_and102.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BOFragMent extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floaAdd;
    private FirebaseFirestore firestore;
    private CollectionReference bietOnCollection;
    private BietOnAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bo, container, false);

        // Ánh xạ
        recyclerView = view.findViewById(R.id.recyclerView);
        floaAdd = view.findViewById(R.id.floaAdd);

        // Khởi tạo Firestore
        firestore = FirebaseFirestore.getInstance();
        bietOnCollection = firestore.collection("BIETON");

        setupRecyclerView();

        floaAdd.setOnClickListener(v -> showDialogAdd(null));

        return view;
    }

    private void setupRecyclerView() {
        Query query = bietOnCollection.orderBy("tentieude");

        FirestoreRecyclerOptions<BietOn> options = new FirestoreRecyclerOptions.Builder<BietOn>()
                .setQuery(query, BietOn.class)
                .build();

        adapter = new BietOnAdapter(getContext(), options);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private void showDialogAdd(BietOn bo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogadd, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Xử lý chức năng
        EditText edtTD = view.findViewById(R.id.edtTD);
        EditText edtND = view.findViewById(R.id.edtND);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnDel = view.findViewById(R.id.btnDel);

        // Nếu có đối tượng `bo`, điền dữ liệu vào form
        if (bo != null) {
            edtTD.setText(bo.getTenTD());
            edtND.setText(bo.getND());
        }

        btnSave.setOnClickListener(v -> {
            String tenTD = edtTD.getText().toString();
            String ND = edtND.getText().toString();

            if (tenTD.equals("") || ND.equals("")) {
                Toast.makeText(getContext(), "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                BietOn updatedBo = new BietOn(tenTD, ND);
                // Cập nhật hoặc thêm dữ liệu ở đây
                if (bo != null) {
                    // Cập nhật dữ liệu
                    bietOnCollection.document(bo.getId()).set(updatedBo)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Thêm dữ liệu mới
                    bietOnCollection.add(updatedBo)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        btnDel.setOnClickListener(v -> dialog.dismiss());
    }
}


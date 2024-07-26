package com.example.asm_ph51025_and102.Adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ph51025_and102.DTO.BietOn;
import com.example.asm_ph51025_and102.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BietOnAdapter extends FirestoreRecyclerAdapter<BietOn, BietOnAdapter.ViewHolder> {

    private Context context;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public BietOnAdapter(Context context, @NonNull FirestoreRecyclerOptions<BietOn> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull BietOn model) {
        holder.txtBo.setText("Tiêu đề: " + model.getTenTD());
        holder.txtND.setText("Nội dung: " + model.getND());

        holder.txtEd.setOnClickListener(v -> {
            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            model.setId(snapshot.getId());  // Lấy ID từ snapshot và đặt vào model
            showDialogUpdate(model);
        });

        holder.txtDl.setOnClickListener(v -> {
            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            showDialogDelete(snapshot.getId(), model.getTenTD());
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_bieton, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBo, txtND, txtEd;
        Button txtDl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBo = itemView.findViewById(R.id.txtBo);
            txtND = itemView.findViewById(R.id.txtND);
            txtEd = itemView.findViewById(R.id.txtEd);
            txtDl = itemView.findViewById(R.id.btnDe);
        }
    }

    private void showDialogUpdate(BietOn bietOn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialogup, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText edtTDup = view.findViewById(R.id.edtTDup);
        EditText edtNDup = view.findViewById(R.id.edtNDup);
        Button btnSaveup = view.findViewById(R.id.btnSaveup);
        Button btnDelup = view.findViewById(R.id.btnDelup);

        edtTDup.setText(bietOn.getTenTD());
        edtNDup.setText(bietOn.getND());

        btnSaveup.setOnClickListener(v -> {
            String tenTD = edtTDup.getText().toString();
            String ND = edtNDup.getText().toString();
            if (tenTD.equals("") || ND.equals("")) {
                Toast.makeText(context, "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                bietOn.setTenTD(tenTD);
                bietOn.setND(ND);

                firestore.collection("BIETON").document(bietOn.getId()).set(bietOn)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnDelup.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void showDialogDelete(String id, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn xóa " + title + " không?")
                .setPositiveButton("Có", (dialog, which) -> firestore.collection("BIETON").document(id).delete()
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show()))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}

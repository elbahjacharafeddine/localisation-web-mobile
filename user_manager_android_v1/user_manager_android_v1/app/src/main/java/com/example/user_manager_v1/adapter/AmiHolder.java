package com.example.user_manager_v1.adapter;


import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_manager_v1.ChatActivity;
import com.example.user_manager_v1.R;
import com.example.user_manager_v1.retrofit.RetrofitService;
import com.example.user_manager_v1.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmiHolder extends RecyclerView.ViewHolder {
    TextView firstName,lastName,idUser,id_auh;
    public AmiHolder(@NonNull View itemView) {
        super(itemView);
        firstName = itemView.findViewById(R.id.first_name);
        lastName = itemView.findViewById(R.id.last_name);
        idUser = itemView.findViewById(R.id.id_user);
        id_auh = itemView.findViewById(R.id.id_ref);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                intent.putExtra("id_user",idUser.getText().toString());
            }
        });
    }
}

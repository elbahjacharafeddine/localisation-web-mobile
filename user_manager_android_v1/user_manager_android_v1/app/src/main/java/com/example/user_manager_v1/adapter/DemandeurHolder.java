package com.example.user_manager_v1.adapter;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_manager_v1.R;
import com.example.user_manager_v1.retrofit.RetrofitService;
import com.example.user_manager_v1.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemandeurHolder extends RecyclerView.ViewHolder {
    TextView firstName,lastName,idUser,id_auh;
    public DemandeurHolder(@NonNull View itemView) {
        super(itemView);
        firstName = itemView.findViewById(R.id.first_name);
        lastName = itemView.findViewById(R.id.last_name);
        idUser = itemView.findViewById(R.id.id_user);
        id_auh = itemView.findViewById(R.id.id_ref);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Acceptation !")
                        .setMessage("vous voulez accepter cette invitation !")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                RetrofitService retrofitService = new RetrofitService();
                                UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
                                userApi.acceptAmi(Integer.parseInt(id_auh.getText().toString()),Integer.parseInt(idUser.getText().toString()))
                                        .enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                Toast.makeText(itemView.getContext(),"Bien fait",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                //Toast.makeText(itemView.getContext(),"Invitation Annulee",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.star_on)
                        .show();
            }
        });
    }
}

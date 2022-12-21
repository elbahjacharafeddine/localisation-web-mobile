package com.example.user_manager_v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_manager_v1.R;
import com.example.user_manager_v1.model.User;

import java.util.List;

public class AmiAdapter extends RecyclerView.Adapter<AmiHolder> {
    private List<User> listUser;
    private int id_ref ;

    public AmiAdapter(List<User> listUser,int id_ref) {
        this.listUser = listUser;
        this.id_ref = id_ref;
    }

    @NonNull
    @Override
    public AmiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_ami_item,parent,false);
        return new AmiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmiHolder holder, int position) {
        User user = listUser.get(position);
        holder.idUser.setText(""+user.getId());
        holder.id_auh.setText(""+id_ref);
        holder.firstName.setText(user.getFirst_name());
        holder.lastName.setText(user.getLast_name());
    }



    @Override
    public int getItemCount() {
        if (listUser != null) {
            return listUser.size();
        }
        return 0;
    }
}


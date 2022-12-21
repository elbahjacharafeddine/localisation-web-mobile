package com.example.user_manager_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user_manager_v1.adapter.AmiAdapter;
import com.example.user_manager_v1.adapter.DemandeurAdapter;
import com.example.user_manager_v1.model.User;
import com.example.user_manager_v1.retrofit.RetrofitService;
import com.example.user_manager_v1.retrofit.UserApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAmi extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private TextView id_aut;
    int i =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ami);

        recyclerView = findViewById(R.id.listUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        id_aut = findViewById(R.id.id_auth);
        Intent intent = getIntent();
        id_aut.setText(intent.getStringExtra("id"));
        i = Integer.parseInt(intent.getStringExtra("id"));
        loadUser();

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if ("invitation".equals(item.getTitle())) {
                    Intent intent = new Intent(ListAmi.this, ListDemandeurInvit.class);
                    intent.putExtra("id", id_aut.getText().toString());
                    startActivity(intent);
                }
                else if("amis".equals(item.getTitle())){
                    //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }
                else if("configuration".equals(item.getTitle())){
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }
                else if("se deconnecter".equals(item.getTitle())){
                    signUserOut();
                }
                return true;
            }
        });
    }

    public void signUserOut(){


        // Return User Back To Home:
        Intent goToHome = new Intent(ListAmi.this, SignInActivity.class);
        startActivity(goToHome);
        finish();

    }

    private void loadUser() {

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        userApi.getAllAmis(Integer.parseInt(id_aut.getText().toString()))
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        populateListeView(response.body(), i);
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"impossible de charger la liste des utilisateurs",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void populateListeView(List<User> listUser,int id_auth) {
        AmiAdapter userAdapter = new AmiAdapter(listUser,id_auth);
        recyclerView.setAdapter(userAdapter);

    }
}

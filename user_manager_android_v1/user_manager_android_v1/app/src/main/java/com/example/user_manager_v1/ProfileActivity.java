package com.example.user_manager_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user_manager_v1.adapter.UserAdapter;
import com.example.user_manager_v1.model.User;
import com.example.user_manager_v1.retrofit.RetrofitService;
import com.example.user_manager_v1.retrofit.UserApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView tv_first_name, tv_last_name, tv_email,id_auth;
    Button sign_out_btn,chercherBtn,invite;
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    private static  final int REQUEST_LOCATION=1;


    TextView showLocationTxt;

    LocationManager locationManager;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if ("invitation".equals(item.getTitle())) {
                    Intent intent = new Intent(ProfileActivity.this, ListDemandeurInvit.class);
                    intent.putExtra("id", id_auth.getText().toString());
                    startActivity(intent);
                }
                else if("amis".equals(item.getTitle())){
                    Intent intent = new Intent(ProfileActivity.this,ListAmi.class);
                    intent.putExtra("id", id_auth.getText().toString());
                    startActivity(intent);
                }
                else if("configuration".equals(item.getTitle())){
                    //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }
                else if("se deconnecter".equals(item.getTitle())){
                   signUserOut();
                }
                return true;
            }
        });



        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check gps is enable or not

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps

            OnGPS();
        }
        else
        {
            //GPS is already On then

            getLocation();
        }

        int count = 100; //Declare as inatance variable

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        getLocation();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                getLocation();
                            }
                        }, 1000);

                    }
                });
            }
        }, 0, 10000);



        //la construction de la fonction
        recyclerView = findViewById(R.id.listUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadUser();

        // Hook Text View Objects:
        tv_first_name = findViewById(R.id.first_name);
        tv_last_name = findViewById(R.id.last_name);
        tv_email = findViewById(R.id.email);
        id_auth = findViewById(R.id.id);

        // Get Intent Extra Values:
        String first_name = getIntent().getStringExtra("first_name");
        String last_name = getIntent().getStringExtra("last_name");
        String email = getIntent().getStringExtra("email");
        int id = getIntent().getIntExtra("id",1);

        // Set Text View Profile Values:
        tv_first_name.setText(first_name);
        tv_last_name.setText(last_name);
        tv_email.setText(email);
        id_auth.setText(id+"");


        chercherBtn = findViewById(R.id.chercher);

        /*invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ListDemandeurInvit.class);
                intent.putExtra("id",id_auth.getText().toString());
                startActivity(intent);
            }
        });*/


        // Set On Click Listener:



        chercherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chercher();
            }
        });


    }


    private void chercher() {
        String id_ref = id_auth.getText().toString();

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        userApi.getUsers(Integer.parseInt(id_ref))
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        populateListeView(response.body(), Integer.parseInt(id_ref));
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"impossible de charger la liste des utilisateurs",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadUser() {

    }

    private void populateListeView(List<User> listUser,int id_auth) {
        UserAdapter userAdapter = new UserAdapter(listUser,id_auth);
        recyclerView.setAdapter(userAdapter);

    }


    public void signUserOut(){

        // Set Text View Profile Values:
        tv_first_name.setText(null);
        tv_last_name.setText(null);
        tv_email.setText(null);
        
        // Return User Back To Home:
        Intent goToHome = new Intent(ProfileActivity.this, SignInActivity.class);
        startActivity(goToHome);
        finish();

    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProfileActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();


                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                //Toast.makeText(getApplicationContext(),"Latitude= "+latitude+" "+"Longitude= "+longitude,Toast.LENGTH_SHORT).show();

                RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                // The URL Posting TO:
                String url = "http://192.168.0.104:8080/position/save";



                // String Request Object:
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("success")){
                            Toast.makeText(ProfileActivity.this, "Position saved Successful", Toast.LENGTH_LONG).show();
                        }
                        // End Of Response If Block.

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println(error.getMessage());
                        //Toast.makeText(ProfileActivity.this, "Position not saved", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("latitude", latitude);
                        params.put("longitude", longitude);
                        params.put("user_id", id_auth.getText().toString());
                        return params;
                    }
                };// End Of String Request Object.

                queue.add(stringRequest);

            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                
                RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                // The URL Posting TO:
                String url = "http://192.168.0.104:8080/position/save";



                // String Request Object:
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("success")){
                            Toast.makeText(ProfileActivity.this, "Position saved Successful", Toast.LENGTH_LONG).show();
                        }
                        // End Of Response If Block.

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println(error.getMessage());
                        //Toast.makeText(ProfileActivity.this, "Position not saved", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("latitude", latitude);
                        params.put("longitude", longitude);
                        params.put("user_id", id_auth.getText().toString());
                        return params;
                    }
                };// End Of String Request Object.

                queue.add(stringRequest);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                // The URL Posting TO:
                String url = "http://192.168.0.104:8080/position/save";



                // String Request Object:
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("success")){
                            Toast.makeText(ProfileActivity.this, "Position saved Successful", Toast.LENGTH_LONG).show();
                        }
                        // End Of Response If Block.

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println(error.getMessage());
                        //Toast.makeText(ProfileActivity.this, "Position not saved", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("latitude", latitude);
                        params.put("longitude", longitude);
                        params.put("user_id", id_auth.getText().toString());
                        return params;
                    }
                };// End Of String Request Object.

                queue.add(stringRequest);
                }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
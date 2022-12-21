package com.example.user_manager_v1.retrofit;

import com.example.user_manager_v1.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {


    @GET("/user/all")
    Call<List<User>> getAllUsers();

    @GET("/user/{user_id}/add/{ami_id}")
    Call<Void> addAmi(@Path("user_id")int user_id,@Path("ami_id") int ami_id);

    @GET("/user/all/{id}")
    Call<List<User>> getUsers(@Path("id") int id);

    @GET("/user/demandeur-invit/{id}")
    Call<List<User>> getDemandeurs(@Path("id") int id);

    @GET("/user/{user_id}/accept/{ami_id}")
    Call<Void> acceptAmi(@Path("user_id")int user_id,@Path("ami_id") int ami_id);

    @GET("/user/{id}/amis")
    Call<List<User>> getAllAmis(@Path("id") int id);



}

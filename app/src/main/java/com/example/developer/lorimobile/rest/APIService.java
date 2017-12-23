package com.example.developer.lorimobile.rest;

import com.example.developer.lorimobile.model.ProjectParticipant;
import com.example.developer.lorimobile.model.Task;
import com.example.developer.lorimobile.model.TimeEntry;
import com.example.developer.lorimobile.model.Token;
import com.example.developer.lorimobile.model.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded","Authorization: Basic Y2xpZW50OnNlY3JldA=="})
    @POST("/app/rest/v2/oauth/token")
    Call<Token> signIn(@Field("grant_type") String grantType, @Field("username") String login, @Field("password") String password);

    @GET("/app/rest/v2/userInfo")
    Call<User> getUser(@Header("Authorization") String token);

    @POST("/app/rest/v2/queries/ts$ProjectParticipant/getProjectsPart?view=projectParticipant-full")
    Call<List<ProjectParticipant>> getProjectList(@Body JsonObject body, @Header("Authorization") String token);

    @POST("/app/rest/v2/entities/ts$Task/search")
    Call<List<Task>> getTaskList(@Body JsonObject body, @Header("Authorization") String token);

    @POST("/app/rest/v2/entities/ts$TimeEntry")
    Call<TimeEntry> createTimeEntry(@Body TimeEntry timeEntry, @Header("Authorization") String token);

    @PUT("/app/rest/v2/entities/ts$TimeEntry/{id}")
    Call<TimeEntry> updateTimeEntry(@Path("id") String id, @Body TimeEntry timeEntry, @Header("Authorization") String token);

    @DELETE("/app/rest/v2/entities/ts$TimeEntry/{id}")
    Call<Void> deleteTimeEntry(@Path("id") String id, @Header("Authorization") String token);

    @POST("/app/rest/v2/queries/ts$TimeEntry/getTimeEntryOfDay?view=timeEntry-full")
    Call<List<TimeEntry>> getTimeEntryOfDay(@Body JsonObject body, @Header("Authorization") String token);

    @POST("/app/rest/v2/queries/ts$TimeEntry/findTimeEntry?view=timeEntry-full")
    Call<List<TimeEntry>> findTimeEntry(@Body JsonObject body, @Header("Authorization") String token);
}

package com.deitel.logginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.deitel.api.model.Login;
import com.deitel.api.model.User;
import com.deitel.api.service.UserClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnGetSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_Login);
        btnGetSecret = findViewById(R.id.btn_GetSecret);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login();
            }
        });
        btnGetSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getSecret();
            }
        });

    }

    //Implement Retrofit
    //Connection to the API
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://localhost:44395/api/User/login")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    private static String token;

    public void login(){
        Login login = new Login("david@contacto.net", "as");
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getToken(), Toast.LENGTH_LONG).show();
                    token = response.body().getToken();
                }else{
                    Toast.makeText(getApplicationContext(), "Loggin not correct :(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error :(", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getSecret(){
        Call<ResponseBody> call = userClient.getSecret(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "token is not correct :(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error :(", Toast.LENGTH_LONG).show();
            }
        });
    }

}
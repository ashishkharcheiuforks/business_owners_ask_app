package com.example.businessownersaskapptenk.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.businessownersaskapptenk.ApiService;
import com.example.businessownersaskapptenk.ApiServiceBuilder;
import com.example.businessownersaskapptenk.JsonModelObject.ResponseBasicLogin;
import com.example.businessownersaskapptenk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicLoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private EditText edit_email,edit_password;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_login);
        spinner=findViewById(R.id.sign_in_progress_bar);
        TextView register_text =findViewById(R.id.tv_dont_have_an_account);

        Bundle bundle=getIntent().getExtras();
        final String user_type=bundle.getString("user_type");
        register_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BasicRegisterActivity.class);
                intent.putExtra("user_type",user_type);
                startActivity(intent);
            }
        });

        edit_email=findViewById(R.id.sign_in_email);
        edit_password=findViewById(R.id.sign_in_password);

        Button signIn =findViewById(R.id.sign_in_btn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateInput())
                    return;
                String email = edit_email.getText().toString();
                String password=edit_password.getText().toString();
                spinner.setVisibility(View.VISIBLE);

                loginToServer(email,password,user_type);
            }
        });

        TextView forget_pass_btn=findViewById(R.id.sign_in_forgot_password);
        forget_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassResetialog();

            }
        });




    }

    private void loginToServer(final String email,String password,final String userType) {



        //buttonLogin.setText("LOADING...");
        //buttonLogin.setClickable(false);
        //buttonLogin.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

        ApiService service = ApiServiceBuilder.getService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);

            jsonObject.put("password", password);

            //jsonObject.put("token", facebookAccessToken);
            //  jsonObject.put("user_type", userType);

            Log.d("Token Object", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        Call<ResponseBasicLogin> call = service.basicLogin(requestBody);
        call.enqueue(new Callback<ResponseBasicLogin>() {
            @Override
            public void onResponse(Call<ResponseBasicLogin> call, Response<ResponseBasicLogin> response) {
                Log.d("Token Object", response.toString());
                spinner.setVisibility(View.GONE);

                ResponseBasicLogin responseBodyLogin = response.body();

                if(response.code()==500){

                    Toast.makeText(BasicLoginActivity.this, "Sorry!" + response.toString(), Toast.LENGTH_LONG).show();
                    return;
                }



                    if (response.code() == 400) {
                       // showResponseError(responseBodyLogin);
                        try{
                            Toast.makeText(BasicLoginActivity.this, "Sorry!" + response.errorBody().string(), Toast.LENGTH_LONG).show();

                        }catch (IOException e){}
                        return;

                    }
                    if (response.body() == null) {
                        Toast.makeText(BasicLoginActivity.this, "Sorry!" + response.toString(), Toast.LENGTH_LONG).show();

                        return;
                    }
                    Toast.makeText(BasicLoginActivity.this, "success!" + response.toString(), Toast.LENGTH_LONG).show();


                    sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    try {
                        Toast.makeText(BasicLoginActivity.this, "Success" + responseBodyLogin.getAccessToken(), Toast.LENGTH_LONG).show();
                        editor.putString("token", responseBodyLogin.getAccessToken());
                        editor.putString("name", responseBodyLogin.getFullName());
                        editor.putString("email", email);
                        editor.putString("avatar", responseBodyLogin.getAvatar());
                        editor.putString("login_method", "basic");

                        //Log.d("loghere", "onResponse: accesstoken from response body login " + responseBodyLogin.getAccessToken());

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    editor.commit();

                    if (userType.equals("customer")) {

                        Intent intent = new Intent(getApplicationContext(), CustomerMainActivity.class);
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(getApplicationContext(), DriverMainActivity.class);
                        startActivity(intent);
                    }
                }

            @Override
            public void onFailure(Call<ResponseBasicLogin> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(BasicLoginActivity.this, "Sorry! Somme Error Occured" + t.toString(), Toast.LENGTH_LONG).show();
            }
        });


        }


    private void passwordResetRequest(String reset_email){

        spinner.setVisibility(View.VISIBLE);
        ApiService service = ApiServiceBuilder.getService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", reset_email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        Call<ResponseBasicLogin> call = service.basicResetPassword(requestBody);
        call.enqueue(new Callback<ResponseBasicLogin>() {
            @Override
            public void onResponse(Call<ResponseBasicLogin> call, Response<ResponseBasicLogin> response) {
                //Log.d("Token Object", response.toString());
                //ResponseBasicLogin responseBodyLogin = response.body();
                //if(response.body()==null)
                  //  return;
                spinner.setVisibility(View.GONE);
                if(response.code()==400) {
                    Toast.makeText(BasicLoginActivity.this, "Sorry!" + response.toString(), Toast.LENGTH_LONG).show();
                    return;

                }
                Toast.makeText(BasicLoginActivity.this, "success!" +response.toString(), Toast.LENGTH_LONG).show();


                }
            @Override
            public void onFailure(Call<ResponseBasicLogin> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(BasicLoginActivity.this, "Sorry! Somme Error Occured" + t.toString(), Toast.LENGTH_LONG).show();
            }
        });




    }


    private void showPassResetialog(){


        final EditText txtUrl = new EditText(this);

// Set the default text to a link of the Queen
        txtUrl.setHint("Email");

        new AlertDialog.Builder(this)
                .setTitle("Reset Password:")
                .setMessage("Enter your email, we will send reset link.")
                .setView(txtUrl)
                .setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String input_email = txtUrl.getText().toString();
                        passwordResetRequest(input_email);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }


    private boolean validateInput(){

        boolean flag=true;


        if(isEmpty(edit_password)){
            edit_password.setText(null);
            edit_password.setHintTextColor(getResources().getColor(R.color.colorRed));
            edit_password.setHint("Password required");
            flag=false;

        }

        if(isEmpty(edit_email)){
            edit_email.setText(null);
            edit_email.setHintTextColor(getResources().getColor(R.color.colorRed));
            edit_email.setHint("Email required");
            flag=false;
        }
        return  flag;
    }


    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }





}


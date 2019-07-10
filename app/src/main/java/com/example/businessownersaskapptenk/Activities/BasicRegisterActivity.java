package com.example.businessownersaskapptenk.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class BasicRegisterActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private  EditText first_name_edit,last_name_edit,username_edit,email_edit,password1_edit,password2_edit;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_register);
        Button signUp = findViewById(R.id.sign_up_btn);
        username_edit=findViewById(R.id.sign_up_name);
        email_edit=findViewById(R.id.sign_up_email);
        password1_edit=findViewById(R.id.sign_up_password);
        password2_edit=findViewById(R.id.sign_up_confirm_password);
        first_name_edit=findViewById(R.id.sign_up_first_name);
        last_name_edit=findViewById(R.id.sign_up_last_name);
        spinner = findViewById(R.id.sign_up_progress_bar);
        Bundle bundle=getIntent().getExtras();
        final String user_type=bundle.getString("user_type");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////validate input///////////
                if(!validateInput())
                    return;



                Toast.makeText(BasicRegisterActivity.this, "Sign up clicked" , Toast.LENGTH_SHORT).show();
                String firstName=first_name_edit.getText().toString();
                String lastName=last_name_edit.getText().toString();
                String username= username_edit.getText().toString();
                String email = email_edit.getText().toString();
                String pass1=password1_edit.getText().toString();
                String pass2=password2_edit.getText().toString();



                spinner.setVisibility(View.VISIBLE);
                loginToServer(firstName,lastName,username,email,pass1,pass2,user_type);
            }
        });






    }



    private void loginToServer(String firstName,String lastName,String username,final String email,String password1,String password2,final String userType) {



        //buttonLogin.setText("LOADING...");
        //buttonLogin.setClickable(false);
        //buttonLogin.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

        ApiService service = ApiServiceBuilder.getService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("username", username);
            jsonObject.put("password1", password1);
            jsonObject.put("password2",password2);
            jsonObject.put("first_name",firstName);
            jsonObject.put("last_name",lastName);

            //jsonObject.put("token", facebookAccessToken);
          //  jsonObject.put("user_type", userType);

            Log.d("Token Object", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        Call<ResponseBasicLogin> call = service.basicRegister(requestBody);
        call.enqueue(new Callback<ResponseBasicLogin>() {
            @Override
            public void onResponse(Call<ResponseBasicLogin> call, Response<ResponseBasicLogin> response) {
                Log.d("Token Object", response.toString());
                spinner.setVisibility(View.GONE);

                ResponseBasicLogin responseBodyLogin = response.body();


                if(response.code()==500){
                    Toast.makeText(BasicRegisterActivity.this, "Sorry!" + response.toString(), Toast.LENGTH_LONG).show();
                    return;

                }
                if(response.code()==400) {
                    try {
                        Toast.makeText(BasicRegisterActivity.this, "Sorry!" + response.errorBody().string(), Toast.LENGTH_LONG).show();

                    }catch (IOException e){}

                    //showResponseError(response.body);

                    return;

                }

                if(response.body()==null) {
                    Toast.makeText(BasicRegisterActivity.this, "Sorry!" + response.toString(), Toast.LENGTH_LONG).show();

                    return;
                }

                Toast.makeText(BasicRegisterActivity.this, "success!" +response.toString(), Toast.LENGTH_LONG).show();



                sharedPref = getSharedPreferences("MY_KEY", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                try {
                    Toast.makeText(BasicRegisterActivity.this, "Success" +responseBodyLogin.getAccessToken(), Toast.LENGTH_LONG).show();
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

                Toast.makeText(BasicRegisterActivity.this, "Sorry! Somme Error Occured" + t.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private boolean validateInput(){

        boolean flag=true;

        if(!password1_edit.getText().toString().equals(password2_edit.getText().toString())) {
            password1_edit.setText(null);
            password2_edit.setText(null);
            password1_edit.setHint("Passwords not matched");
            password2_edit.setHint("Passwords not matched ");
            flag=false;
        }
        if(isEmpty(first_name_edit)){

            first_name_edit.setText(null);
            first_name_edit.setHintTextColor(getResources().getColor(R.color.red));
            first_name_edit.setHint("First Name required");
            flag=false;
        }

        if(isEmpty(last_name_edit)){

            last_name_edit.setText(null);
            last_name_edit.setHintTextColor(getResources().getColor(R.color.red));
            last_name_edit.setHint("Last Name required");
            flag=false;

        }

        if(isEmpty(username_edit)){

            username_edit.setText(null);
            username_edit.setHintTextColor(getResources().getColor(R.color.red));
            username_edit.setHint("Username required");
            flag=false;

        }
        if(isEmpty(password1_edit)){
            password1_edit.setText(null);
            password1_edit.setHintTextColor(getResources().getColor(R.color.red));
            password1_edit.setHint("Password required");
            flag=false;

        }
        if(isEmpty(password2_edit)){
            password2_edit.setText(null);
            password2_edit.setHintTextColor(getResources().getColor(R.color.red));
            password2_edit.setHint("Password required");
            flag=false;

        }

        if(isEmpty(email_edit)){
            email_edit.setText(null);
            email_edit.setHintTextColor(getResources().getColor(R.color.red));
            email_edit.setHint("Email required");
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

package com.example.carexpertsystem.Common.Login_Signup;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.carexpertsystem.HelperClasses.Constants;
import com.example.carexpertsystem.HelperClasses.RequestHandler;
import com.example.carexpertsystem.HelperClasses.SharedPrefManager;
import com.example.carexpertsystem.R;
import com.example.carexpertsystem.User.Dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmail, loginPassword;
    private Button loginBtn;
    private ProgressDialog progressDialog;
    private TextView loginSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        status bar gone using this line
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            return;
        }
        loginEmail=(EditText)findViewById(R.id.login_email);
        loginPassword=(EditText)findViewById(R.id.login_password);
        loginBtn=(Button)findViewById(R.id.login_btn);
        loginSignup=(TextView)findViewById(R.id.login_signup);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait ..");

        loginBtn.setOnClickListener(this);
        loginSignup.setOnClickListener(this);
    }
    private void userLogin(){
        final String useremail=loginEmail.getText().toString().trim();
        final String password=loginPassword.getText().toString().trim();

        progressDialog.show();
        StringRequest stringRequest=new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object=new JSONObject(response);
                            if(!object.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                object.getInt("LID"),
                                                object.getString("LUSERNAME")
                                        );
                                Toast.makeText(getApplicationContext(),"User Login Successfull",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("LUSERNAME",useremail);
                params.put("LPASSWORD",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void loginSignup(){
        startActivity(new Intent(getApplicationContext(),SignUpScreen.class));
    }

    @Override
    public void onClick(View view) {
        if(view==loginBtn){
            userLogin();
        }
        if(view==loginSignup){
            loginSignup();
        }
    }
}
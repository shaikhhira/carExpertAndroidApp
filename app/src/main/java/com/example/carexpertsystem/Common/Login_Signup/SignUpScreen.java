package com.example.carexpertsystem.Common.Login_Signup;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carexpertsystem.HelperClasses.Constants;
import com.example.carexpertsystem.HelperClasses.RequestHandler;
import com.example.carexpertsystem.HelperClasses.SharedPrefManager;
import com.example.carexpertsystem.R;
import com.example.carexpertsystem.User.Dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {

    private Button signupBtn;
    private EditText user_email,user_password;
    private ProgressDialog progressDialog;
    private TextView loginText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        status bar gone using this line
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_screen);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            return;
        }
        user_email=(EditText) findViewById(R.id.user_email);
        user_password=(EditText)findViewById((R.id.user_password));
        signupBtn=(Button)findViewById(R.id.signup_btn);
        loginText=(TextView)findViewById(R.id.already_login_btn);

        progressDialog=new ProgressDialog(this);

        signupBtn.setOnClickListener(this);
        loginText.setOnClickListener(this);
    }
    private void login(){
        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
        finish();
    }
    private void signupClick(){
        final String useremail=user_email.getText().toString().trim();
        final String password=user_password.getText().toString().trim();

        progressDialog.setMessage("Registring the user");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params=new HashMap<>();
                params.put("LUSERNAME",useremail);
                params.put("LPASSWORD",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view==signupBtn){
            signupClick();
        }
        if(view==loginText){
            login();
        }
    }
}
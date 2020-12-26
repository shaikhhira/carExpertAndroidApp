package com.example.carexpertsystem.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carexpertsystem.Common.Login_Signup.Welcome_Screen;
import com.example.carexpertsystem.HelperClasses.Constants;
import com.example.carexpertsystem.HelperClasses.SharedPrefManager;
import com.example.carexpertsystem.R;
import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener, AdapterView.OnItemSelectedListener {


    static final float END_SCALE=0.7f;
    private TextView textEmail;
    private Button logout_btn, map_btn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuDrawerIcon;
    private LinearLayout contentView;

    //search bar
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;

    //spinner for brands and models
    private Spinner brand_spinner,model_spinner;
    private ArrayList<String> brandList=new ArrayList<>();
    private ArrayList<String> modelList=new ArrayList<>();
    private ArrayAdapter<String> brandAdapter;
    private ArrayAdapter<String> modelAdapter;
    RequestQueue requestQueue;


    private TextView issue_text,solution_text;
    private ArrayList<String >issueList=new ArrayList<>();
    private ArrayList<String>solutionList=new ArrayList<>();
    private String selectedIssue;
    private Button yes_btn,no_btn,not_worked_btn,worked_btn;

    private boolean no_btn_text=false,yes_btn_text=false,worked_text=false,not_worked_text=false;


    private ListView listView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), Welcome_Screen.class));
            return;
        }

        logout_btn = (Button) findViewById(R.id.logout);
        map_btn = (Button) findViewById(R.id.map_btn);
        textEmail = (TextView) findViewById(R.id.textemail);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        menuDrawerIcon = (ImageView) findViewById(R.id.menu_drawer_icon);
        contentView=(LinearLayout)findViewById(R.id.contentView);


        brand_spinner=(Spinner)findViewById(R.id.brandSpinner);

        issue_text=(TextView)findViewById(R.id.issueText);
        solution_text=(TextView)findViewById(R.id.solutionText);
        yes_btn=(Button)findViewById(R.id.yesBtn);
        no_btn=(Button)findViewById(R.id.noBtn);
        worked_btn=(Button)findViewById(R.id.workedBtn);
        not_worked_btn=(Button)findViewById(R.id.notWorkerbtn);


        listView=(ListView)findViewById(R.id.listView);

        logout_btn.setOnClickListener(this);
        map_btn.setOnClickListener(this);


        navigationFunction();




        //search bar for problem start
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Search");
        searchBar.setSpeechMode(false);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
        //restore last queries from disk
//        lastSearches = loadSearchSuggestionFromDisk();
//        searchBar.setLastSuggestions(list);
//        //Inflate menu and setup OnMenuItemClickListener
//        searchBar.inflateMenu(R.menu.main);
//        searchBar.getMenu().setOnMenuItemClickListener(this);

        //search bar for problem end

        //spinner brand

        requestQueue= Volley.newRequestQueue(this);
        brand_spinner=(Spinner)findViewById(R.id.brandSpinner);
        model_spinner=(Spinner)findViewById(R.id.modelSpinner);



        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST,
                Constants.URL_BRAND,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("brands");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String brandName=jsonObject.optString("BRANDNAME");
                                brandList.add(brandName);
                                brandAdapter=new ArrayAdapter<>(Dashboard.this,
                                        android.R.layout.simple_spinner_item,brandList);
                                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                brand_spinner.setAdapter(brandAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        //set model according to brand
        brand_spinner.setOnItemSelectedListener(this);
    }

    //    when back button pressed only drawer will close
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private void navigationFunction() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id=item.getItemId();
                    if(id==R.id.nav_logout){
                        logout();
                    }
                    if (id==R.id.nav_mechanic){
                        startActivity(new Intent(getApplicationContext(),MechanicMap.class));
                    }
                    if(id==R.id.nav_rate_us){

                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });




        //get header view
        View headerView=navigationView.getHeaderView(0);
        TextView navUserEmail=(TextView) headerView.findViewById(R.id.menu_user_name);
        navUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());

        menuDrawerIcon.setOnClickListener(this);
        
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

//        to change backgroundc color
//        drawerLayout.setScrimColor(getResources().getColor(R.color.colorAccent));

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //scale the voew based on currebt slide offset
                final float diffScaledoffset=slideOffset*(1-END_SCALE);
                final  float offsetScale=1-diffScaledoffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //translate the voew accounting for the scaled width
                final float xOffset=drawerView.getWidth()*slideOffset;
                final float xOffsetDiff=contentView.getWidth()*diffScaledoffset/2;
                final float xTranslation=xOffset-xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void logout() {
        SharedPrefManager.getInstance(this).logout();
        startActivity(new Intent(getApplicationContext(), Welcome_Screen.class));
        finish();
    }

    private void map_call() {
        startActivity(new Intent(getApplicationContext(), MapActivity.class));
    }

    private void navigationDrawerCallingFunction() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == logout_btn) {
            logout();
        }
        if (view == map_btn) {
            map_call();
        }
        if (view == menuDrawerIcon) {
            navigationDrawerCallingFunction();
        }
        if(view==yes_btn){
            solutionCallingFunction();
        }
        if(view==no_btn){
            moreIssuesFunction();
        }
        if(view==not_worked_btn){
            not_worked_funtion();
        }
        if(view==worked_btn){
            worked_function();
        }

    }

    private void enabledFunction(){
        not_worked_btn.setEnabled(false);
        worked_btn.setEnabled(false);
        yes_btn.setEnabled(false);
        no_btn.setEnabled(false);
    }

    private void moreIssuesFunction() {
        issueList.remove(issueList.size()-1);
        if(issueList.size()>0){
            String res=issueList.get(issueList.size()-1);
            if(res==issue_text.toString()){
                issueList.remove(issueList.size()-1);
                res="";
                textEmail.setText(issue_text.toString());
            }else {
                issue_text.setText(res);
            }

        }else {
            issue_text.setText("you can connect with mechanic");
            enabledFunction();
        }

    }

    private void worked_function() {
        solution_text.setText("Thankyou");
        enabledFunction();
    }

    private void not_worked_funtion() {
        solutionList.remove(solutionList.size()-1);//remove previous one
        if(solutionList.size()>0){
            String res=solutionList.get(solutionList.size()-1);//add second last
            solution_text.setText(res);
        }else {
            solution_text.setText("Call Mechanic");
            enabledFunction();
        }
    }

    private void solutionCallingFunction() {

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST,
                Constants.URL_SOLUTION+issue_text.getText().toString(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("masterSolution");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                  String SolutionName=jsonObject.optString("SOLUTIONNAME");
                                solutionList.add(SolutionName);
                                solution_text.setText(SolutionName);

                                worked_btn.setVisibility(View.VISIBLE);
                                not_worked_btn.setVisibility(View.VISIBLE);

                                worked_btn.setOnClickListener(Dashboard.this);
                                not_worked_btn.setOnClickListener(Dashboard.this);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


    //search bar functions  start


    @Override
    protected void onDestroy() {
        //save last queries to disk
//        saveSearchSuggestionToDisk(searchBar.getLastSuggestions());
        lastSearches=searchBar.getLastSuggestions();

            super.onDestroy();
            

    }



    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        Toast.makeText(Dashboard.this, "Search " + s, Toast.LENGTH_SHORT).show();
    }

    //this function take search input value
    @Override
    public void onSearchConfirmed(CharSequence text) {
        if(text.toString()!=null){
            brand_spinner.setVisibility(View.VISIBLE);
            model_spinner.setVisibility(View.VISIBLE);
            startSearch(text.toString(), true, null, false);
            textEmail.setText(text.toString());//simple text to show the search value

            selectedIssue=text.toString();

            model_spinner.setOnItemSelectedListener(this);
        }

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
    //search bar functions end


    //for model selection
    @Override
    public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getId()==R.id.brandSpinner){
            modelList.clear();
            String selectedBrand=adapterView.getSelectedItem().toString();

            requestQueue=Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                    Constants.URL_MODEL+selectedBrand,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray=response.getJSONArray("models");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String modelName=jsonObject.optString("MODELNAME");
                                    modelList.add(modelName);
                                    modelAdapter=new ArrayAdapter<>(Dashboard.this,
                                            android.R.layout.simple_spinner_item,modelList);
                                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    model_spinner.setAdapter(modelAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
        }
        if(adapterView.getId()==R.id.modelSpinner){
            String selectedModel=adapterView.getSelectedItem().toString();
            requestQueue=Volley.newRequestQueue(this);


            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,
                    Constants.URL_ISSUE+selectedModel + Constants.URL_ISSUE_ISSUENAME+selectedIssue,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray=response.getJSONArray("masterIssue");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String IssueName=jsonObject.optString("ISSUENAME");

                                    issueList.add(IssueName);

                                    issue_text.setText(IssueName);

                                    if(IssueName==IssueName+1){
                                        issueList.remove(issueList.size()-1);
                                    }else {
                                        yes_btn.setVisibility(View.VISIBLE);
                                        no_btn.setVisibility(View.VISIBLE);

                                        yes_btn.setOnClickListener(Dashboard.this);
                                        no_btn.setOnClickListener(Dashboard.this);
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
        }

    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //model function end


}
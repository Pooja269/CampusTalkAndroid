package com.campustalk.developer.campustalk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Callback{

    ProgressDialog progressDialog;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        openConfigurationURL();

    }

    public void onLoginClick(View view){

        EditText etUsername = (EditText)findViewById(R.id.et_username);
        EditText etPassword = (EditText) findViewById(R.id.et_password);
        CheckBox rememberMe = (CheckBox)findViewById(R.id.chk_rememberme);
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if(username.equals("")){
            etUsername.setError("Enter Username");
        }
        else if(password.equals("")){
            etPassword.setError("Enter Password");
        }
        else {

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);

            String baseURL = sharedPreferences.getString("url", "");
            HashMap<String, String> parametersMap = new HashMap<>();
            parametersMap.put("username", username);
            parametersMap.put("password", password);
            parametersMap.put("operation", "login");

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Signing in...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            DownloadData data = new DownloadData(baseURL + getString(R.string.url), parametersMap, this);
            data.execute();
        }
    }

    void openConfigurationURL(){

        ConfigURLDialog dialog = new ConfigURLDialog();
        dialog.show(getSupportFragmentManager(),"CONFIG URL");
    }

    @Override
    public void getData(String JSONData) {

        try {

            JSONObject jsonObject = new JSONObject(JSONData);

            if(jsonObject.getString("login").equals("Success")) {
                String name = jsonObject.getString("name");
                String otherDetails = jsonObject.getString("otherDetails");
                String phone = jsonObject.getString("phone");
                String imagePath = jsonObject.getString("imagePath");
                String trainingDetails = jsonObject.getString("trainingDetails");
                String department = jsonObject.getString("department");
                String passingyear = jsonObject.getString("passingyear");
                String altphone = jsonObject.getString("altphone");
                String deptid = jsonObject.getString("deptid");
                String projectDetails = jsonObject.getString("projectDetails");
                String email = jsonObject.getString("email");
                String dob = jsonObject.getString("dob");
                String gender = jsonObject.getString("gender");
                String enrollment = jsonObject.getString("enrollment");
                String semester = jsonObject.getString("semester");


                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = sharedPreferences.edit();

                sharedEditor.putString("name", name);
                sharedEditor.putString("otherDetails",otherDetails);
                sharedEditor.putString("phone",phone);
                sharedEditor.putString("imagePath",imagePath);
                sharedEditor.putString("trainingDetails",trainingDetails);
                sharedEditor.putString("department",department);
                sharedEditor.putString("passingyear",passingyear);
                sharedEditor.putString("altphone",altphone);
                sharedEditor.putString("deptid",deptid);
                sharedEditor.putString("projectDetails",projectDetails);
                sharedEditor.putString("email",email);
                sharedEditor.putString("dob",dob);
                sharedEditor.putString("gender",gender);
                sharedEditor.putString("enrollment",enrollment);
                sharedEditor.putString("semester",semester);
                sharedEditor.putString("username",username);
                sharedEditor.putString("password",password);

                sharedEditor.commit();

                //open navigation drawer activity
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(intent);
                this.finish();
                System.out.println("finished");
            }else{

                ((EditText) findViewById(R.id.et_password)).setError("Enter valid Credentials");
            }

        }catch (Exception e){}
        finally {
            progressDialog.dismiss();
        }

    }
}


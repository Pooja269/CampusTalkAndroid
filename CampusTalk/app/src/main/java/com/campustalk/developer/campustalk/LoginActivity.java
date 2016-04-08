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

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE);
        String baseURL = sharedPreferences.getString("url", "");

        String username = ((EditText)findViewById(R.id.et_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.et_password)).getText().toString();

        HashMap<String,String> parametersMap = new HashMap<>();
        parametersMap.put("username",username);
        parametersMap.put("password",password);
        parametersMap.put("operation","login");

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadData data = new DownloadData(baseURL+getString(R.string.url),parametersMap,this);
        data.execute();

        Intent intent=new Intent(LoginActivity.this,NavigationActivity.class);
        startActivity(intent);
    }

    void openConfigurationURL(){

        ConfigURLDialog dialog = new ConfigURLDialog();
        dialog.show(getSupportFragmentManager(),"CONFIG URL");
    }

    @Override
    public void getData(String JSONData) {

        try {

            JSONObject jsonObject = new JSONObject(JSONData);
            String name = jsonObject.getString("name");

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
            SharedPreferences.Editor sharedEditor = sharedPreferences.edit();

            sharedEditor.putString("name",name);

            System.out.println(name);

            sharedEditor.commit();

        }catch (Exception e){}
        finally {
            progressDialog.dismiss();
        }

    }
}


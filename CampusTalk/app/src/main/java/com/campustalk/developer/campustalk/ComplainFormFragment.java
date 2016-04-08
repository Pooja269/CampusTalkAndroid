package com.campustalk.developer.campustalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by khushali on 30/03/2016.
 */
public class ComplainFormFragment extends Fragment implements Callback{

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complainform,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("name", "");
        String enrollment = sharedPreferences.getString("enrollment", "");

        ((TextView)view.findViewById(R.id.tv_name)).setText(name);
        ((TextView)view.findViewById(R.id.tv_enrollment)).setText(enrollment);


        ((Button) view.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
    }

    void validateAndSubmit(){

        String department = ((Spinner) view.findViewById(R.id.sp_dept)).getSelectedItem().toString();
        int semester = Integer.parseInt(((Spinner) view.findViewById(R.id.sp_sem)).getSelectedItem().toString());
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        TextView enrollment = (TextView) view.findViewById(R.id.tv_enrollment);
        EditText title=(EditText)view.findViewById(R.id.complain_title);
        EditText desc = (EditText)view.findViewById(R.id.complain_desc);
        if(title.getText().toString().equals(""))
            Toast.makeText(getActivity(),"Add Title",Toast.LENGTH_SHORT).show();
        else if(desc.getText().toString().equals(""))
            Toast.makeText(getActivity(), "Add Description", Toast.LENGTH_SHORT).show();
        else{
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
            String baseURL = sharedPreferences.getString("url", "");
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password","");

            HashMap<String,String> parametersMap = new HashMap<>();
            parametersMap.put("username",username);
            parametersMap.put("password",password);
            parametersMap.put("operation","complain");
            parametersMap.put("name",name.getText().toString());
            parametersMap.put("enrollment",enrollment.getText().toString());
            parametersMap.put("department",department);
            parametersMap.put("semester",semester+"");
            parametersMap.put("title", title.getText().toString());
            parametersMap.put("description", desc.getText().toString());

            DownloadData downloadData = new DownloadData(baseURL + getString(R.string.url), parametersMap, this);
            downloadData.execute();
        }

    }

    @Override
    public void getData(String JSONData) {

        try {
            EditText title=(EditText)view.findViewById(R.id.complain_title);
            EditText desc = (EditText)view.findViewById(R.id.complain_desc);
            JSONObject jsonObject = new JSONObject(JSONData);
            if((jsonObject.getString("login")).equals("Success")){
                Toast.makeText(getActivity(),"Complain submitted successfully",Toast.LENGTH_SHORT).show();
                title.setText("");
                desc.setText("");
            }
            else{
                Toast.makeText(getActivity(),"Complain submission failed",Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
                Toast.makeText(getActivity(),"Complain submission failed",Toast.LENGTH_SHORT).show();
        }

    }
}
package com.campustalk.developer.campustalk;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by PATEL POOJA on 30/03/2016.
 */
public class FeedbackFormFragment extends Fragment implements Callback {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_feedback1,container,false);
        return view;
    }

    @Override
    public void onStart()
    {
       super.onStart();
      ((Button)view.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onSubmit();
           }
       });


    }
    public void onSubmit(){
        String department=((Spinner)view.findViewById(R.id.sp_department)).getSelectedItem().toString();
        String semester=((Spinner)view.findViewById(R.id.sp_semester)).getSelectedItem().toString();
        String division=((EditText)view.findViewById(R.id.et_division)).getText().toString();
        String teacher=((EditText)view.findViewById(R.id.et_teacher)).getText().toString();
        String courseCode=((EditText)view.findViewById(R.id.et_courseCode)).getText().toString();
        String title=((EditText)view.findViewById(R.id.et_title)).getText().toString();
        String ans1=((Spinner)view.findViewById(R.id.sp_ans1)).getSelectedItem().toString();
        String ans2=((Spinner)view.findViewById(R.id.sp_ans2)).getSelectedItem().toString();
        String ans3=((Spinner)view.findViewById(R.id.sp_ans3)).getSelectedItem().toString();
        String ans4=((Spinner)view.findViewById(R.id.sp_ans4)).getSelectedItem().toString();
        String ans5=((Spinner)view.findViewById(R.id.sp_ans5)).getSelectedItem().toString();
        String ans6=((Spinner)view.findViewById(R.id.sp_ans6)).getSelectedItem().toString();
        String ans7=((Spinner)view.findViewById(R.id.sp_ans7)).getSelectedItem().toString();
        String ans8=((Spinner)view.findViewById(R.id.sp_ans8)).getSelectedItem().toString();
        String ans9=((Spinner)view.findViewById(R.id.sp_ans9)).getSelectedItem().toString();
        String ans10=((Spinner)view.findViewById(R.id.sp_ans10)).getSelectedItem().toString();
        String ans11=((Spinner)view.findViewById(R.id.sp_ans11)).getSelectedItem().toString();
        String ans12=((Spinner)view.findViewById(R.id.sp_ans12)).getSelectedItem().toString();
        String ans13=((Spinner)view.findViewById(R.id.sp_ans13)).getSelectedItem().toString();
        String comment=((EditText)view.findViewById(R.id.et_comment)).getText().toString();
        if(division.equals(""))
        {
            //Snackbar.make(view.findViewById(android.R.id.content),"Divison must be added",Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"Divison must be added",Toast.LENGTH_SHORT).show();
        }
        else if(teacher.equals(""))
        {
            //Snackbar.make(view.findViewById(android.R.id.content),"Please specify the faculty name",Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"Please specify the faculty name",Toast.LENGTH_SHORT).show();
        }
        else if(courseCode.equals(""))
        {
            //Snackbar.make(view.findViewById(android.R.id.content),"Course code should be added",Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"Course code should be added",Toast.LENGTH_SHORT).show();
        }
        else if(title.equals(""))
        {
            //Snackbar.make(view.findViewById(android.R.id.content),"Course title should be specified",Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"Course title should be specified",Toast.LENGTH_SHORT).show();
        }
        else {


            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.config_settings), Context.MODE_PRIVATE);
            String baseURL = sharedPreferences.getString("url", "");
            String username=sharedPreferences.getString("username","");
            String password=sharedPreferences.getString("password","");
            HashMap<String, String> parametersMap = new HashMap<>();
            parametersMap.put("username",username);
            parametersMap.put("password",password);
            parametersMap.put("operation", "feedback");
            parametersMap.put("department", department);
            parametersMap.put("division", division);
            parametersMap.put("semester", semester);
            parametersMap.put("coursecode", courseCode);
            parametersMap.put("coursetitle", title);
            parametersMap.put("teacher", teacher);
            parametersMap.put("que1", ans1);
            parametersMap.put("que2", ans2);
            parametersMap.put("que3", ans3);
            parametersMap.put("que4", ans4);
            parametersMap.put("que5", ans5);
            parametersMap.put("que6", ans6);
            parametersMap.put("que7", ans7);
            parametersMap.put("que8", ans8);
            parametersMap.put("que9", ans9);
            parametersMap.put("que10", ans10);
            parametersMap.put("que11", ans11);
            parametersMap.put("que12", ans12);
            parametersMap.put("que13", ans13);
            parametersMap.put("comment", comment);
            DownloadData data = new DownloadData(baseURL + getString(R.string.url), parametersMap, this);
            data.execute();
            System.out.println("hi");

        }
    }
    @Override
    public void getData(String JSONData){
        try{
            JSONObject jsonObject=new JSONObject(JSONData);
            if(jsonObject.getString("login").equals("Success"))
            {
                Toast.makeText(getActivity(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                ((EditText) view.findViewById(R.id.et_division)).setText("");
                ((EditText) view.findViewById(R.id.et_courseCode)).setText("");
                ((EditText) view.findViewById(R.id.et_teacher)).setText("");
                ((EditText) view.findViewById(R.id.et_title)).setText("");
                System.out.println("running");
            }
            else
            {
                Toast.makeText(getActivity(),"Feedback submission failed.Try again",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),"Feedback submission failed.Try again",Toast.LENGTH_SHORT).show();
            ((EditText) view.findViewById(R.id.et_division)).setText("");
            ((EditText) view.findViewById(R.id.et_courseCode)).setText("");
            ((EditText) view.findViewById(R.id.et_teacher)).setText("");
            ((EditText) view.findViewById(R.id.et_title)).setText("");
        }
    }
}

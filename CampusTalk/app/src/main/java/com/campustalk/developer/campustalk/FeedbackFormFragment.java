package com.campustalk.developer.campustalk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by PATEL POOJA on 30/03/2016.
 */
public class FeedbackFormFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_feedbackform,container,false);
        return view;
    }

    @Override
    public void onStart()
    {
       super.onStart();
        ((Button) view.findViewById(R.id.btnSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });

    }
    public void validateAndSubmit(){
        EditText div=(EditText)view.findViewById(R.id.div);
        EditText not=(EditText)view.findViewById(R.id.teacher);
        EditText course=(EditText)view.findViewById(R.id.courseCode);
        EditText title=(EditText)view.findViewById(R.id.courseTitle);
        if(div.getText().toString().equals(""))
        {
            Snackbar.make(view.findViewById(android.R.id.content),"Divison must be added",Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(),"submit",Toast.LENGTH_SHORT).show();
        }
        else if(not.getText().toString().equals(""))
        {
            Snackbar.make(view.findViewById(android.R.id.content),"Please specify the faculty name",Snackbar.LENGTH_SHORT).show();
        }
        else if(course.getText().toString().equals(""))
        {
            Snackbar.make(view.findViewById(android.R.id.content),"Course code should be added",Snackbar.LENGTH_SHORT).show();
        }
        else if(title.getText().toString().equals(""))
        {
            Snackbar.make(view.findViewById(android.R.id.content),"Course title should be specified",Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(),"Feedback submitted successfully",Toast.LENGTH_SHORT).show();
            div.setText("");
            course.setText("");
            not.setText("");
            title.setText("");
        }
    }
}

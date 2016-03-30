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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by khushali on 30/03/2016.
 */
public class ComplainFormFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedbackform,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Button) view.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
    }

    void validateAndSubmit(){

        EditText title=(EditText)view.findViewById(R.id.complain_title);
        EditText desc = (EditText)view.findViewById(R.id.complain_desc);
        if(title.getText().toString().equals(""))
            Snackbar.make(view.findViewById(android.R.id.content),"Title must be added",Snackbar.LENGTH_SHORT).show();
        else if(desc.getText().toString().equals(""))
            Snackbar.make(view.findViewById(android.R.id.content),"Description must be added", Snackbar.LENGTH_SHORT).show();
        else{
            Toast.makeText(getActivity(),"Complain submitted successfully",Toast.LENGTH_SHORT).show();
            title.setText("");
            desc.setText("");
        }

    }
}

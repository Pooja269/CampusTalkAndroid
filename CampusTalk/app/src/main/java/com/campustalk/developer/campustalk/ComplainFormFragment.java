package com.campustalk.developer.campustalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by khushali on 30/03/2016.
 */
public class ComplainFormFragment extends Fragment {

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
        ((Button) view.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
    }

    void validateAndSubmit(){

       // TextView name=(TextView)view.findViewById(R.id.)
       // if()
        Snackbar.make(view.findViewById(android.R.id.content),"Title must be added",Snackbar.LENGTH_SHORT).show();

    }
}

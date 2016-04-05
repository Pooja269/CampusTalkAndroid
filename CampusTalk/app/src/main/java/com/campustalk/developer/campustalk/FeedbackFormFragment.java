package com.campustalk.developer.campustalk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PATEL POOJA on 30/03/2016.
 */
public class FeedbackFormFragment extends Fragment {
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

    }
}

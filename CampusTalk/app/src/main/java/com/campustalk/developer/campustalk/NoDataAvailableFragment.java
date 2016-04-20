package com.campustalk.developer.campustalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by khushali on 12/04/2016.
 */
public class NoDataAvailableFragment extends Fragment {

    View view;

    static NoDataAvailableFragment setMessage(String mainMessage,String subMessage){
        NoDataAvailableFragment fragment = new NoDataAvailableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mainMessage",mainMessage);
        bundle.putString("subMessage",subMessage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_no_data_available,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String mainMessage = getArguments().getString("mainMessage");
        String subMessage = getArguments().getString("subMessage");

        ((TextView) view.findViewById(R.id.tv_main_message)).setText(mainMessage);
        ((TextView) view.findViewById(R.id.tv_sub_message)).setText(subMessage);
    }
}

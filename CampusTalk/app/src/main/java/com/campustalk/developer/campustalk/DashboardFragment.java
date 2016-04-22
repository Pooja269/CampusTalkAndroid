package com.campustalk.developer.campustalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by khushali on 03/04/2016.
 */
public class DashboardFragment extends Fragment {

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((LinearLayout) view.findViewById(R.id.ll_notice)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        ((LinearLayout) view.findViewById(R.id.ll_blog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),BlogActivity.class);
                startActivity(intent);
            }
        });

        ((LinearLayout)view.findViewById(R.id.ll_asknlearn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),QuestionActivity.class);
                startActivity(intent);
            }
        });

        ((LinearLayout)view.findViewById(R.id.ll_events)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EventActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.ll_studentProfiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ViewProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}

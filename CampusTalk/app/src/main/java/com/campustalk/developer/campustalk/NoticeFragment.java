package com.campustalk.developer.campustalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by khushali on 03/04/2016.
 */
public class NoticeFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice,container,false);
        return  view;
    }

    static NoticeFragment setNoticeType(String noticeType){
        NoticeFragment fragment = new NoticeFragment();
        Bundle args = new Bundle();
        args.putString("noticeType", noticeType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        String noticeType = getArguments().getString("noticeType","");

    }
}

package com.campustalk.developer.campustalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by khushali on 03/04/2016.
 */
public class NoticeFragment extends Fragment implements Callback{

    View view;
    boolean loaded = false;
    int pageNo = 1;
    int totalPages;
    RecyclerView recyclerView;
    String noticeType;
    String semester;
    List<Notice> noticeList;
    NoticeAdapter adapter;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        noticeType = getArguments().getString("noticeType", "");
        semester = getArguments().getString("semester","");

        Log.d("NoticeFragment","onCreateView"+noticeType);
        noticeList = new ArrayList<>();
        adapter = new NoticeAdapter(noticeList);
        view = inflater.inflate(R.layout.layout_recyclerview,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= totalPages) {
                    loadNotices(noticeType, semester, current_page);
                }
            }
        });
        recyclerView.removeAllViews();
        recyclerView.setAdapter(adapter);
        return  view;
    }

    static NoticeFragment setNoticeType(String noticeType,String semester){
        NoticeFragment fragment = new NoticeFragment();
        Bundle args = new Bundle();
        args.putString("noticeType", noticeType);
        args.putString("semester", semester);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.d("Notices","Visible hint"+noticeType);
                loadNotices(noticeType, semester, 1);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Notices", "start"+noticeType);
    }

    void loadNotices(String noticeType,String semester,int pageNo){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
        String url = sharedPreferences.getString("url","");
        url = url + getString(R.string.url);

        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");

        HashMap<String,String> parametersMap = new HashMap<>();
        parametersMap.put("username",username);
        parametersMap.put("password",password);
        parametersMap.put("noticeType",noticeType);
        parametersMap.put("semester",semester);
        parametersMap.put("pageNo",String.valueOf(pageNo));
        parametersMap.put("operation","notice");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Notices...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadData data = new DownloadData(url,parametersMap,this);
        data.execute();

    }

    @Override
    public void getData(String JSONData) {

        try{

            JSONObject jsonObject = new JSONObject(JSONData);
            totalPages = jsonObject.getInt("totalPages");
            JSONArray jsonArray = jsonObject.getJSONArray("notices");

            Log.d("ArrayLength",String.valueOf(jsonArray.length()));

            if(jsonArray.length()==0){
                Log.d("NoticeFragment","ArrayLength zero");
                Fragment fragment = NoDataAvailableFragment.setMessage("No Data Available !","Ooops ! No notices available");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frame,fragment,"NO DATA").commit();

            }else {

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject notice = jsonArray.getJSONObject(i);

                int noticeID = notice.getInt("id");
                String title = notice.getString("title");
                String desc = notice.getString("desc");
                String filePath = notice.getString("filePath");
                String date = notice.getString("date");
                String semester = notice.getString("semester");
                String enroll = notice.getString("enroll");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date noticeDate = sdf.parse(date);
                    sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
                    date = sdf.format(noticeDate);
                } catch (Exception e) {
                }

                Notice not = new Notice(noticeID, title, desc, date, filePath, semester, enroll);
                noticeList.add(not);
            }
                adapter.notifyDataSetChanged();
            }


        }catch(Exception e){

        }finally {
            progressDialog.dismiss();
        }
    }

    class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{

        List<Notice> notices;

        NoticeAdapter(List<Notice> notices){
            this.notices = notices;
        }


        @Override
        public void onBindViewHolder(NoticeViewHolder holder, int position) {

            final Notice notice = notices.get(position);
            holder.tvTitle.setText(notice.getTitle());
            holder.tvDescription.setText(notice.getDescription());
            holder.tvDate.setText(notice.getDate());

            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = notice.getFilePath();
                    if(filePath.equals("")){
                        Toast.makeText(getActivity(),"File Not Available",Toast.LENGTH_SHORT).show();
                    }else{
                        //code to download document
                    }
                }
            });
        }

        @Override
        public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cardview_notice, parent,false);
            NoticeViewHolder viewHolder = new NoticeViewHolder(view);
            return viewHolder;
        }

        @Override
        public int getItemCount() {
            return notices.size();
        }

        class NoticeViewHolder extends RecyclerView.ViewHolder{

            TextView tvTitle,tvDate,tvDescription,tvMore;
            Button btnDownload;

            NoticeViewHolder(View itemView){
                super(itemView);

                tvTitle = (TextView) itemView.findViewById(R.id.tv_notice_title);
                tvDate = (TextView) itemView.findViewById(R.id.tv_notice_date);
                tvDescription = (TextView) itemView.findViewById(R.id.tv_notice_desc);
                tvMore = (TextView) itemView.findViewById(R.id.tv_notice_more);

                btnDownload = (Button) itemView.findViewById(R.id.btn_notice_download);
            }


        }
    }
}

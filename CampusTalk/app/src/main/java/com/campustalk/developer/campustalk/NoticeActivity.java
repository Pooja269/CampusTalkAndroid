package com.campustalk.developer.campustalk;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends AppCompatActivity implements Callback{

    List<Notice> noticeList;
    String noticeType,semester;
    int totalPages;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    NoticeAdapter adapter;
    String username,password,url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NoticeAdapter(noticeList);

        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= totalPages) {
                    loadNotices(noticeType, semester, current_page);
                }

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
        url = sharedPreferences.getString("url","");
        url = url + getString(R.string.url);

        username = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Notices");

        Spinner spNoticeType = (Spinner) findViewById(R.id.sp_noticeType);

        spNoticeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                noticeList.clear();
                switch (position) {

                    case 0:

                        noticeType = "98521";
                        semester = "";
                        loadNotices(noticeType, semester, 1);
                        break;
                    case 1:
                        noticeType = "74563";
                        semester = "";
                        loadNotices(noticeType, semester, 1);
                        break;
                    case 2:
                        noticeType = "96541";
                        semester = "";
                        loadNotices(noticeType, semester, 1);
                        break;
                    case 3:
                        SharedPreferences sh = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
                        noticeType = sh.getString("deptid", "");
                        semester = sh.getString("semester", "");
                        loadNotices(noticeType, semester, 1);
                        break;
                    case 4:
                        noticeType = "69854";
                        semester = "";
                        loadNotices(noticeType, semester, 1);
                        break;
                    default:
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getData(String JSONData) {
        try{

            JSONObject jsonObject = new JSONObject(JSONData);
            totalPages = jsonObject.getInt("totalPages");
            JSONArray jsonArray = jsonObject.getJSONArray("notices");

            Log.d("ArrayLength", String.valueOf(jsonArray.length()));

            if(jsonArray.length()==0){
                recyclerView.setVisibility(View.GONE);

                Fragment fragment = NoDataAvailableFragment.setMessage("No Data Available !","Ooops ! No notices available");
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frame,fragment,"NO DATA").commit();

            }else {

                recyclerView.setVisibility(View.VISIBLE);

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


    void loadNotices(String noticeType,String semester,int pageNo){


        HashMap<String,String> parametersMap = new HashMap<>();
        parametersMap.put("username",username);
        parametersMap.put("password",password);
        parametersMap.put("noticeType",noticeType);
        parametersMap.put("semester",semester);
        parametersMap.put("pageNo",String.valueOf(pageNo));
        parametersMap.put("operation","notice");

        progressDialog = new ProgressDialog(NoticeActivity.this);
        progressDialog.setMessage("Loading Notices...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadData data = new DownloadData(url,parametersMap,this);
        data.execute();

    }

    class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{

        List<Notice> notices;

        NoticeAdapter(List<Notice> notices){
            this.notices = notices;
        }


        @Override
        public void onBindViewHolder(final NoticeViewHolder holder, int position) {

            final Notice notice = notices.get(position);
            holder.tvTitle.setText(notice.getTitle());
            holder.tvDescription.setText(notice.getDescription());
            holder.tvDate.setText(notice.getDate());

            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                boolean isExpanded = false;
                @Override
                public void onClick(View v) {


                    if (isExpanded) {
                        collapseTextView(holder.tvDescription,3);
                        isExpanded = false;
                        holder.tvMore.setText("more...");
                    }else {
                        expandTextView(holder.tvDescription);
                        isExpanded = true;
                        holder.tvMore.setText("less...");
                    }
                }
            });



            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = notice.getFilePath();
                    if(filePath.equals("")){
                        Toast.makeText(getBaseContext(), "File Not Available", Toast.LENGTH_SHORT).show();
                    }else{
                        //code to download document
                        String downloadUrl = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE).getString("url","") + getString(R.string.downloadDoc);

                        downloadUrl = downloadUrl + "?filePath=" + filePath;

                        DownloadDocument.downloadDoc(getBaseContext(),url,filePath);
                    }
                }
            });
        }

        @Override
        public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void expandTextView(final TextView tv) {
        tv.setEllipsize(null);
        tv.post(new Runnable() {
            @Override
            public void run() {
                Log.d("LINES", tv.getLineCount() + "");
                ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", tv.getLineCount());
                animation.setDuration(200).start();
            }
        });
    }

    /**
     * This method is used to shrink the given textview to given numLines lines and ellipsis
     *
     * @param tv
     * @param numLines
     */
    private void collapseTextView(TextView tv, int numLines) {
        tv.setEllipsize(TextUtils.TruncateAt.END);
        Log.d("LINES", numLines + "");
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", numLines);
        animation.setDuration(200).start();

    }

}

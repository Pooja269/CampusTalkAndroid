package com.campustalk.developer.campustalk;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shahdravya01 on 08-Apr-16.
 */
public class EventActivity extends AppCompatActivity implements Callback{

    List<Event> eventList;
    boolean loaded = false;
    ProgressDialog progressDialog;
    EventAdapter adapter;
    int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recyclerview);

    }

    @Override
    protected void onStart() {
        super.onStart();

        eventList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new EventAdapter();

        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= totalPages) {
                    loadEventData(current_page);
                }
            }
        });

        if(!loaded){

            loadEventData(1);

        }


    }

    void loadEventData(int pageNo){

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
        String url = sharedPreferences.getString("url", "");
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password","");

        url = url + getString(R.string.url);

        HashMap<String,String> parametersMap = new HashMap<>();
        parametersMap.put("username",username);
        parametersMap.put("password",password);
        parametersMap.put("operation", "event");
        parametersMap.put("pageNo", String.valueOf(pageNo));

        progressDialog = new ProgressDialog(EventActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Events...");
        progressDialog.show();

        DownloadData data = new DownloadData(url,parametersMap,this);
        data.execute();

    }

    @Override
    public void getData(String JSONData) {

        try {


            JSONObject jsonObject = new JSONObject(JSONData);
            JSONArray jsonArray = jsonObject.getJSONArray("events");
            totalPages = jsonObject.getInt("totalPages");

            if(jsonArray.length() == 0){

                Fragment fragment = NoDataAvailableFragment.setMessage("No Data Available !","Ooops ! No Events available");
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frame,fragment,"NO DATA").commit();

            }else{

                for (int i=0;i<jsonArray.length();i++){

                    JSONObject eventObject = jsonArray.getJSONObject(i);

                    String id = eventObject.getString("id");
                    String title = eventObject.getString("title");
                    String time = eventObject.getString("time");
                    String desc = eventObject.getString("desc");
                    String startdate = eventObject.getString("startdate");
                    String enddate = eventObject.getString("enddate");
                    String imagePath = eventObject.getString("imagePath");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date sdate = sdf.parse(startdate);
                    Date edate = sdf.parse(enddate);

                    sdf = new SimpleDateFormat("dd-MM-yyyy");
                    startdate = sdf.format(sdate);
                    enddate = sdf.format(edate);

                    Event event = new Event(id,title,time,startdate,desc,enddate,imagePath);

                    eventList.add(event);
                    adapter.notifyDataSetChanged();

                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
        }
    }

    class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cardview_events,null,false);
            EventViewHolder holder = new EventViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {

            Event event = eventList.get(position);

            holder.tvEventTime.setText(event.getEventTime());
            holder.tvEventName.setText(event.getEventTitle());
            holder.tvEventDesc.setText(event.getEventDescription());
            holder.tvEventDate.setText(event.getEventStartDate() + " To "+event.getEventEndDate());

        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        class EventViewHolder extends RecyclerView.ViewHolder{

            TextView tvEventName,tvEventDate,tvEventTime,tvEventDesc,tvMore;
            ImageView ivEventImage;

                EventViewHolder(View itemView){
                    super(itemView);

                    tvEventName = (TextView) itemView.findViewById(R.id.tvEvent_title);
                    tvEventDate = (TextView) itemView.findViewById(R.id.tvEvent_date);
                    tvEventDesc = (TextView) itemView.findViewById(R.id.tvEvent_desc);
                    tvEventTime = (TextView) itemView.findViewById(R.id.tvEvent_time);
                    tvMore = (TextView) itemView.findViewById(R.id.tvMore);

                    ivEventImage = (ImageView) itemView.findViewById(R.id.ivEvent_image);
                }

        }



    }
}

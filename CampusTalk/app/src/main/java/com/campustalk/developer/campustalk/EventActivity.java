package com.campustalk.developer.campustalk;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
public class EventActivity extends AppCompatActivity implements Callback {

    List<Event> eventList;
    boolean loaded = false;
    ProgressDialog progressDialog;
    EventAdapter adapter;
    int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recyclerview);
        eventList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

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

            if (!loaded) {

                loadEventData(1);
                loaded = true;
            }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void loadEventData(int pageNo) {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
        String url = sharedPreferences.getString("url", "");
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

        url = url + getString(R.string.url);

        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("username", username);
        parametersMap.put("password", password);
        parametersMap.put("operation", "event");
        parametersMap.put("pageNo", String.valueOf(pageNo));

        progressDialog = new ProgressDialog(EventActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Events...");
        progressDialog.show();

        DownloadData data = new DownloadData(url, parametersMap, this);
        data.execute();

    }

    @Override
    public void getData(String JSONData) {

        try {


            JSONObject jsonObject = new JSONObject(JSONData);
            JSONArray jsonArray = jsonObject.getJSONArray("events");
            totalPages = jsonObject.getInt("totalPages");

            if (jsonArray.length() == 0) {

                Fragment fragment = NoDataAvailableFragment.setMessage("No Data Available !", "Ooops ! No Events available");
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frame, fragment, "NO DATA").commit();

            } else {

                for (int i = 0; i < jsonArray.length(); i++) {

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

                    Event event = new Event(id, title, time, startdate, desc, enddate, imagePath);

                    eventList.add(event);


                }
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog.dismiss();
        }
    }

    class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        String url = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE).getString("url","") + getString(R.string.imageUrl);

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cardview_events, parent, false);
            EventViewHolder holder = new EventViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final EventViewHolder holder, int position) {

            final Event event = eventList.get(position);

            holder.tvEventTime.setText(event.getEventTime());
            holder.tvEventName.setText(event.getEventTitle());
            holder.tvEventDesc.setText(event.getEventDescription());
            holder.tvEventDate.setText(event.getEventStartDate() + " To " + event.getEventEndDate());

            String sample = "http://192.168.2.55:8090/CampusTalk/ImageServlet?imageID=E:\\Workspace-eclipse\\CampusTalk\\WebContent\\images\\Events\\spandanEvent1459484301826.jpg";

            Picasso.with(getBaseContext())
                    .load(url+"?imageID="+event.getEventImage())
                    .into(holder.ivEventImage);

            Log.d("imageUrl", url + "?imageID=" + event.getEventImage());

           /* holder.ibAddEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentResolver resolver = getContentResolver();
                    ContentValues values = new ContentValues();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Date sdate=null,edate=null;
                    try {
                        sdate = sdf.parse(event.getEventStartDate());
                        edate = sdf.parse(event.getEventEndDate());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    values.put(CalendarContract.Events.CALENDAR_ID, 1);
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, Time.getCurrentTimezone());
                    values.put(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getEventTime());
                    values.put(CalendarContract.Events.DESCRIPTION, event.getEventDescription());
                    values.put(CalendarContract.Events.TITLE, event.getEventTitle());
                    values.put(CalendarContract.Events.DTSTART, sdate.getTime());
                    values.put(CalendarContract.Events.DTEND, edate.getTime());

                    String permission = Manifest.permission.WRITE_CALENDAR;

                    if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                        Uri uri = resolver.insert(CalendarContract.Events.CONTENT_URI, values);
                    } else {
                        Toast.makeText(getBaseContext(), "You must give permission to access your calendar", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, 200);
                    }

                }
            });*/

            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                boolean isExpanded = false;

                @Override
                public void onClick(View v) {


                    if (isExpanded) {
                        collapseTextView(holder.tvEventDesc, 3);
                        isExpanded = false;
                        holder.tvMore.setText("more...");
                    } else {
                        expandTextView(holder.tvEventDesc);
                        isExpanded = true;
                        holder.tvMore.setText("less...");
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        class EventViewHolder extends RecyclerView.ViewHolder {

            TextView tvEventName, tvEventDate, tvEventTime, tvEventDesc, tvMore;
            ImageView ivEventImage;
            ImageButton ibAddEvent;

            EventViewHolder(View itemView) {
                super(itemView);

                tvEventName = (TextView) itemView.findViewById(R.id.tvEvent_title);
                tvEventDate = (TextView) itemView.findViewById(R.id.tvEvent_date);
                tvEventDesc = (TextView) itemView.findViewById(R.id.tvEvent_desc);
                tvEventTime = (TextView) itemView.findViewById(R.id.tvEvent_time);
                tvMore = (TextView) itemView.findViewById(R.id.tvMore);

                ivEventImage = (ImageView) itemView.findViewById(R.id.ivEvent_image);
                ibAddEvent = (ImageButton) itemView.findViewById(R.id.ib_add_calendar_event);
            }

        }


    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

    Toast.makeText(getBaseContext(),"Permission Granted ! Now click add button again to add event to your calendar",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(),"You must give permission to access your calendar",Toast.LENGTH_SHORT).show();
            }



    }*/

    /**
     * This method is used to expand the given tv to its full capacity
     *
     * @param tv
     */
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
package com.campustalk.developer.campustalk;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shahdravya01 on 20-Apr-16.
 */
public class BlogActivity extends AppCompatActivity implements Callback {

    BlogAdapter blogAdapter;
    boolean loaded;
    int totalPages;
    List<Blog> blogList;
    ProgressDialog progressDialog;
    Spinner category;
    String blogCategory;
    RecyclerView recyclerView;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_blog);
            System.out.println("blogsssss");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        blogList = new ArrayList<>();
        category = (Spinner) findViewById(R.id.sp_blogType);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blogList.clear();
                switch (position) {

                    case 0:
                        blogCategory = "Art";
                        loadBlogData(blogCategory, 1);
                        break;
                    case 1:
                        blogCategory = "Literature";
                        loadBlogData(blogCategory, 1);
                        break;
                    case 2:
                        blogCategory = "Photography";
                        loadBlogData(blogCategory, 1);
                        break;
                    case 3:
                        blogCategory = "Technical";
                        loadBlogData(blogCategory, 1);
                        break;
                    default:
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (!loaded){
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        blogAdapter = new BlogAdapter();

        recyclerView.setAdapter(blogAdapter);

        recyclerView.setOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= totalPages) {
                    loadBlogData(blogCategory, current_page);
                }

            }
        });
        loaded=true;
    }



    }

    public void loadBlogData(String category,int pageNo)
    {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
            String url = sharedPreferences.getString("url", "");
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password","");

            url = url + getString(R.string.url);

            HashMap<String,String> parametersMap = new HashMap<>();
            parametersMap.put("username",username);
            parametersMap.put("password",password);
            parametersMap.put("operation", "blog");
            parametersMap.put("category",category);
            parametersMap.put("pageNo", String.valueOf(pageNo));

        progressDialog = new ProgressDialog(BlogActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Blogs...");
        progressDialog.show();

        DownloadData data = new DownloadData(url,parametersMap,this);
        data.execute();
        }

    public void getData(String JSONData){

        try {
            JSONObject jsonObject = new JSONObject(JSONData);
            JSONArray jsonArray = jsonObject.getJSONArray("blogs");
            totalPages = jsonObject.getInt("totalPages");

            if (jsonArray.length() == 0) {

                recyclerView.setVisibility(View.GONE);
                Fragment fragment = NoDataAvailableFragment.setMessage("No Data Available !", "Ooops ! No Blogs available");
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.frame, fragment, "NO DATA").commit();

            }
            else {

                recyclerView.setVisibility(View.VISIBLE);
                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject blogObject = jsonArray.getJSONObject(i);

                    String id = blogObject.getString("id");
                    String name = blogObject.getString("name");
                    String date = blogObject.getString("date");
                    String enrollment = blogObject.getString("enroll");
                    String title = blogObject.getString("title");
                    String category = blogObject.getString("category");
                    String imagePath = blogObject.getString("imagePath");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date sdate = sdf.parse(date);

                    sdf = new SimpleDateFormat("EEE, dd-MMM-yyyy");
                    date = sdf.format(sdate);

                    Blog blog = new Blog(id, name, date, enrollment, title, category, imagePath);

                    blogList.add(blog);

                }
                System.out.println("blog size :"+ blogList.size());
                blogAdapter.notifyDataSetChanged();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
        }
        }

    class BlogAdapter extends  RecyclerView.Adapter<BlogAdapter.BlogViewHolder>{

        @Override
        public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.cardview_blog,parent,false);
            BlogViewHolder blogViewHolder = new BlogViewHolder(view);
            return blogViewHolder;
        }

        @Override
        public void onBindViewHolder(BlogViewHolder holder, int position) {
            final Blog blog = blogList.get(position);
            System.out.println("position"+position);

            holder.tvBlogTitle.setText(blog.getBlogTitle());
            holder.tvBlogName.setText(" "+blog.getBlogname());
            holder.tvBlogDate.setText(blog.getBlogDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BlogActivity.this,BlogDetailViewActivity.class);
                    Bundle b = new Bundle();
                    b.putString("blogId",blog.getBlogId());
                    b.putString("blogTitle",blog.getBlogTitle());
                    b.putString("name",blog.getBlogname());
                    b.putString("category",blog.getBlogCategory());
                    b.putString("enrollment",blog.getBlogEnrollment());
                    b.putString("imagePath",blog.getBlogImagePath());
                    b.putString("date",blog.getBlogDate());
                    intent.putExtra("bundle",b);
                    startActivity(intent);
                }
            });
        }

        public  int getItemCount(){
            System.out.println("Blog size"+blogList.size());
            return blogList.size();}

        class BlogViewHolder extends RecyclerView.ViewHolder {

            TextView tvBlogTitle, tvBlogName, tvBlogDate;

            BlogViewHolder(View itemView){
                super(itemView);

                tvBlogTitle = (TextView)itemView.findViewById(R.id.tv_blogtitle);
                tvBlogName = (TextView) itemView.findViewById(R.id.tv_blogname);
                tvBlogDate = (TextView) itemView.findViewById(R.id.tv_blogdate);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}



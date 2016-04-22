package com.campustalk.developer.campustalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by PATEL POOJA on 22/04/2016.
 */
public class BlogDetailViewActivity extends AppCompatActivity implements Callback {
    String blogId,title,name,date,blogImage;
    boolean loaded=false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogdetail_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        Bundle b =intent.getBundleExtra("bundle");
        blogId = b.getString("blogId");
        title = b.getString("blogTitle");
        blogImage = b.getString("imagePath");
        System.out.println(blogImage);
        date = b.getString("date");
        String category=b.getString("category");
        name =  b.getString("name");
        if(!loaded)
        {
            loadBlogData("1");
        }

    }

    private void loadBlogData(String Id) {
        SharedPreferences sharedPreferneces = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE);
        String url = sharedPreferneces.getString("url", "");
        String username =  sharedPreferneces.getString("username", "");
        String password = sharedPreferneces.getString("password","");
        HashMap<String,String> parametersmap = new HashMap<>();
        parametersmap.put("username",username);
        parametersmap.put("password",password);
        parametersmap.put("operation","blog");
        parametersmap.put("id",blogId);
        progressDialog = new ProgressDialog(BlogDetailViewActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Blog...");
        progressDialog.show();
        DownloadData data = new DownloadData(url+getString(R.string.url),parametersmap,this);
        data.execute();
    }

    @Override
    public void getData(String JSONData) {
        try {
            String url=getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE).getString("url","")+getString(R.string.imageUrl);
            JSONObject jsonObject = new JSONObject(JSONData);
            String id = jsonObject.getString("id");
            String description = jsonObject.getString("desc");
            ((TextView)findViewById(R.id.tv_blog_Title)).setText(title);
            ImageView image = (ImageView)findViewById(R.id.iv_blog_img);
             Picasso.with(getBaseContext())
                     .load(url+"?imageID="+blogImage).into(image);
                     ((TextView) findViewById(R.id.tv_blog_desc)).setText(description);
            Log.d("imageUrl", url + "?imageID=" + blogImage);
            ((TextView)findViewById(R.id.tv_name)).setText(" "+name);
            ((TextView)findViewById(R.id.tv_date)).setText(date);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
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

package com.campustalk.developer.campustalk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by PATEL POOJA on 05/04/2016.
 */
public class AnswerActivity extends AppCompatActivity implements Callback {
    List<Answer> answerList;
    AnswerAdapter adapter;
    String queId,question;
    boolean loaded = false;
    ProgressDialog progressDialog;
    Dialog dialog;
    View view;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Ask & Learn");
    }

    @Override
    protected void onStart() {

        super.onStart();
        Intent intent = getIntent();
        Bundle b=intent.getBundleExtra("bundle");
        queId = b.getString("questionId");
        question = b.getString("questionTitle");
        System.out.println(question);
        String date=b.getString("date");
        String name=b.getString("name");

        System.out.println(name+"  "+date);
        String department = b.getString("department");
        String semester = b.getString("semester");

        ((TextView) findViewById(R.id.text_asked)).setText(date);
        ((TextView)findViewById(R.id.question)).setText(question);
        ((TextView)findViewById(R.id.tv_name)).setText(" "+name+"\u002C");
        ((TextView)findViewById(R.id.tv_department)).setText(department);
        ((TextView)findViewById(R.id.tv_semester)).setText("sem-"+semester+"\u002C");
        answerList = new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.ansRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AnswerAdapter();
        recyclerView.setAdapter(adapter);
        if(!loaded)
        {
            loadAnswerData(queId);
        }
    }

    private void loadAnswerData(String queId) {
        SharedPreferences sharedPreferences= getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
        String url = sharedPreferences.getString("url", "");
        String userName = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        url=url+getString(R.string.url);
        HashMap<String,String> parametersMap = new HashMap<>();
        parametersMap.put("username",userName);
        parametersMap.put("password",password);
        parametersMap.put("operation","getanswer");
        parametersMap.put("id",queId);
        progressDialog = new ProgressDialog(AnswerActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading answers...");
        progressDialog.show();
        DownloadData data=new DownloadData(url,parametersMap,this);
        data.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
        return true;
    }

    @Override
    public void getData(String JSONData) {
        try{
            JSONObject jsonObject = new JSONObject(JSONData);
            JSONArray jsonArray=jsonObject.getJSONArray("answers");
            TextView tv =(TextView)findViewById(R.id.tv_noAnswer);
            if(jsonArray.length()==0)
            {
               recyclerView.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);

            }
            else
            {
                recyclerView.setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject answerObject = jsonArray.getJSONObject(i);
                    String id = answerObject.getString("id");
                    String answer =answerObject.getString("ans");
                    String name = answerObject.getString("name");
                    String semester = answerObject.getString("semester");
                    String department = answerObject.getString("department");
                    String date = answerObject.getString("date");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date1 = sdf.parse(date);
                    sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
                    date = sdf.format(date1);
                    System.out.println(date);
                    Answer answerObj=new Answer(id,answer,semester,department,name,date);
                    answerList.add(answerObj);
                    System.out.println("Answer object added "+i);
                }
                adapter.notifyDataSetChanged();
            }
            loaded = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
        }
    }

    class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>{
        @Override
        public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.raw_answer,parent,false);
            AnswerViewHolder holder =new AnswerViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(AnswerViewHolder holder, int position) {
            final Answer answer = answerList.get(position);
            holder.tvAnswerDate.setText(answer.getDate());
            System.out.println(answer.getDate());
            holder.tvAnswer.setText(answer.getAnswer());
            holder.tvName.setText(" "+answer.getName()+"\u002c");
            holder.tvSemester.setText("Sem-"+answer.getSemester()+"\u002c");
            holder.tvDepartment.setText(answer.getDepartment());

        }

        @Override
        public int getItemCount() {
            return answerList.size();
        }

        class AnswerViewHolder extends RecyclerView.ViewHolder{

            TextView tvAnswerDate,tvAnswer,tvName,tvDepartment,tvSemester;
            AnswerViewHolder(View itemView)
            {
                super(itemView);
                tvAnswerDate = (TextView)itemView.findViewById(R.id.tv_answer_date);
                tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvDepartment = (TextView) itemView.findViewById(R.id.tv_department);
                tvSemester = (TextView) itemView.findViewById(R.id.tv_semester);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_answer) {
            dialog = new Dialog(AnswerActivity.this);
            dialog.setTitle(question);
            view = getLayoutInflater().inflate(R.layout.dialog_fragment_answer, null);

            dialog.setContentView(view);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            Button btn = (Button) view.findViewById(R.id.btn_submit);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postAnswer();
                }
            });
            dialog.show();


        }
        else if(id == android.R.id.home)
        {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void postAnswer() {
        try{
            String answer = ((EditText)view.findViewById(R.id.et_answer)).getText().toString();
            if(answer.equals(""))
            {
                Toast.makeText(getBaseContext(),"Please give your answer",Toast.LENGTH_SHORT).show();

            }
            else
            {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE);
                String url =  sharedPreferences.getString("url", "");
                String userName = sharedPreferences.getString("username", "");
                String password = sharedPreferences.getString("password", "");
                String department = sharedPreferences.getString("department", "");
                String name =  sharedPreferences.getString("name","");
                String semester =  sharedPreferences.getString("semester","");
                HashMap<String,String> parametersMap =  new HashMap<>();
                parametersMap.put("username",userName);
                parametersMap.put("password",password);
                parametersMap.put("operation","postanswer");
                parametersMap.put("department",department);
                parametersMap.put("semester",semester);
                parametersMap.put("name",name);
                parametersMap.put("queid",queId);
                parametersMap.put("answer",answer);
                DownloadData data = new DownloadData(url+getString(R.string.url), parametersMap, new Callback() {
                    @Override
                    public void getData(String JSONData) {
                        try {
                            JSONObject jsonObject = new JSONObject(JSONData);
                            if (jsonObject.getString("login").equalsIgnoreCase("Success")) {
                                dialog.dismiss();
                                Toast.makeText(getBaseContext(), "Answer submitted successsfully", Toast.LENGTH_SHORT).show();
                                answerList.clear();
                                loadAnswerData(queId);

                            } else
                                Toast.makeText(getBaseContext(), "Answer submission failed.Try again later..", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getBaseContext(), "Answer submission failed.Try again later..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
                data.execute();
             }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}

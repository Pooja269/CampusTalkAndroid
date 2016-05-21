package com.campustalk.developer.campustalk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class QuestionActivity extends AppCompatActivity implements Callback {
    List<Question> questionList;
    boolean loaded = false;
    ProgressDialog progressDialog;
    QuestionAdapter adapter;
    int totalPages;
    View view;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ft_question);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionDialog();
            }
        });

        questionList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.queRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new QuestionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= totalPages) {
                    loadQuestionData(current_page);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        try

        {
            super.onStart();
            if (!loaded) {
                loadQuestionData(1);
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }


    void loadQuestionData(int pageNo) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), MODE_PRIVATE);
        String url = sharedPreferences.getString("url", "");
        String userName = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        url = url + getString(R.string.url);
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("username", userName);
        parametersMap.put("password", password);
        parametersMap.put("operation", "question");
        parametersMap.put("pageNo", String.valueOf(pageNo));
        progressDialog = new ProgressDialog(QuestionActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Questions...");
        progressDialog.show();
        DownloadData data = new DownloadData(url, parametersMap, this);
        data.execute();
    }

    @Override
    public void getData(String JSONData) {
        try {
            JSONObject jsonObject = new JSONObject(JSONData);
            JSONArray jsonArray = jsonObject.getJSONArray("questions");
            totalPages = jsonObject.getInt("totalPages");

            if (jsonArray.length() == 0) {

            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject questionObject = jsonArray.getJSONObject(i);
                    String id = questionObject.getString("id");
                    String department = questionObject.getString("department");
                    String name = questionObject.getString("name");
                    String que = questionObject.getString("que");
                    String date = questionObject.getString("date");
                    String semester = questionObject.getString("semester");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date1 = sdf.parse(date);
                    sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
                    date = sdf.format(date1);
                    Question question = new Question(id, que, name, date, department, semester);
                    questionList.add(question);
                    adapter.notifyDataSetChanged();

                }

            }
            loaded = true;


        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            progressDialog.dismiss();
        }
    }


    class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

        @Override
        public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.raw_question, parent, false);
            QuestionViewHolder holder = new QuestionViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(QuestionViewHolder holder, int position) {
            final Question question = questionList.get(position);

            holder.tvName.setText(" " + question.getname() + "\u002C");
            holder.tvQuestion.setText(question.getQuestionTitle());
            holder.tvDepartment.setText(question.getDepartment());
            holder.tvQuestionDate.setText(question.getDate());
            holder.tvSemester.setText("Sem-" + question.getSemester() + "\u002C");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(QuestionActivity.this,AnswerActivity.class);
                    Bundle b=new Bundle();
                    b.putString("questionId",question.getQuestionId());
                    b.putString("questionTitle",question.getQuestionTitle());
                    b.putString("name",question.getname());
                    b.putString("department",question.getDepartment());
                    b.putString("semester",question.getSemester());
                    b.putString("date",question.getDate());
                    intent.putExtra("bundle",b);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }


        class QuestionViewHolder extends RecyclerView.ViewHolder {
            TextView tvQuestionDate, tvSemester, tvName, tvDepartment, tvQuestion;

            QuestionViewHolder(View itemview) {
                super(itemview);
                tvQuestionDate = (TextView) itemview.findViewById(R.id.tv_question_date);
                tvDepartment = (TextView) itemview.findViewById(R.id.tv_department);
                tvName = (TextView) itemview.findViewById(R.id.tv_name);
                tvQuestion = (TextView) itemview.findViewById(R.id.tv_question);
                tvSemester = (TextView) itemview.findViewById(R.id.tv_semester);
            }
        }
    }

    public void questionDialog() {
        //System.out.println("Method called");

            dialog = new Dialog(QuestionActivity.this);
            dialog.setTitle("Post your question here...");

            view = getLayoutInflater().inflate(R.layout.dialog_fragment_question, null);
            dialog.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ((Button)view.findViewById(R.id.btn_post)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQuestion();
            }
        });
        dialog.show();
        }

    public void postQuestion() {
        try {
        String question = ((EditText) view.findViewById(R.id.et_question)).getText().toString();

        if (question.equals("")) {
            Toast.makeText(getBaseContext(), "Please enter a question", Toast.LENGTH_SHORT).show();
        } else {

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
            String baseUrl = sharedPreferences.getString("url", "");
            String userName = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");
            String semester = sharedPreferences.getString("semester", "");
            String department = sharedPreferences.getString("department", "");
            String name = sharedPreferences.getString("name","");
            HashMap<String, String> parameterMap = new HashMap<>();
            parameterMap.put("username", userName);
            parameterMap.put("enrollment",userName);
            parameterMap.put("password", password);
            parameterMap.put("operation", "postquestion");
            parameterMap.put("semester", semester);
            parameterMap.put("department", department);
            parameterMap.put("que", question);
            parameterMap.put("name",name);
            DownloadData data = new DownloadData(baseUrl + getString(R.string.url), parameterMap, new Callback() {
                @Override
                public void getData(String JSONData) {
                    try {
                        JSONObject jsonObject = new JSONObject(JSONData);
                        if (jsonObject.getString("login").equals("Success")) {
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), "Question posted successsfully", Toast.LENGTH_SHORT).show();
                            questionList.clear();
                            loadQuestionData(1);
                            ((EditText)view.findViewById(R.id.et_question)).setText("");
                        } else
                            Toast.makeText(getBaseContext(), "Question posting failed.Try again later..", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "question posting failed.Try again later..", Toast.LENGTH_SHORT).show();
                        ((EditText)view.findViewById(R.id.et_question)).setText("");
                    }
                }
            });
            data.execute();

        }
    }
        catch(Exception e)
        {
            e.printStackTrace();
        System.out.println(e);}
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
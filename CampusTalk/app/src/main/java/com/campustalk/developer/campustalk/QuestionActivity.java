package com.campustalk.developer.campustalk;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((TextView)findViewById(R.id.question_1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, AnswerActivity.class);
                startActivity(intent);
            }
        });
    }
    public void questionDialog()
    {
        System.out.println("Method called");
        AlertDialog.Builder dialog = new AlertDialog.Builder(QuestionActivity.this);
        dialog.setTitle("Post your question here...");
        View view=getLayoutInflater().inflate(R.layout.dialog_fragment_question,null);
        dialog.setView(view);
        Button btn=(Button) view.findViewById(R.id.btn_post);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.create();
        dialog.show();
    }
}

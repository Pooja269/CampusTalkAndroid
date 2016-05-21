package com.campustalk.developer.campustalk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by khushali on 22/04/2016.
 */
public class ViewProfileActivity extends AppCompatActivity implements Callback{

    RecyclerView recyclerView;
    List<Student> studentList;
    boolean loaded = false;
    String url , username , password ;
    ProgressDialog progressDialog;
    int totalPages;
    String department="",semester="",name="";
    StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Student Profiles");
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.ft_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ViewProfileActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_fragment_search_student,null,false);
                dialog.setContentView(view);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                Spinner spSem = ((Spinner) view.findViewById(R.id.sp_semester));
                String sem[]={"All Semester","1","2","3","4","5","6","7","8"};

                ArrayAdapter<String> a =new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_dropdown_item, sem);
                spSem.setAdapter(a);

                final String dept[] = {"All Department",
                        "Mechanical",
                "Electrical",
                "Civil",
                "Computer",
                "IT",
                "E&amp;C",
                "I&amp;C",
                "Environment",
                "Textile",
                "Plastic",
                "Rubber",
                "Chemical",
                "Bio Medical",
                "Applied Mechanics",
                "General"};

                Spinner spDept = (Spinner) view.findViewById(R.id.sp_department);

                ArrayAdapter<String> b =new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_dropdown_item, dept);
                spDept.setAdapter(b);


                dialog.show();

                view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name = ((EditText) view.findViewById(R.id.et_name)).getText().toString();
                        department = ((Spinner) view.findViewById(R.id.sp_department)).getSelectedItem().toString();
                        semester = ((Spinner) view.findViewById(R.id.sp_semester)).getSelectedItem().toString();

                        if(semester.startsWith("All"))
                            semester="";
                        if(department.startsWith("All"))
                            department="";

                        studentList.clear();
                        dialog.dismiss();
                        loadStudentDetails(1);
                    }
                });

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE);
        url = sharedPreferences.getString("url","") + getString(R.string.url);
        username = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");


        studentList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new StudentAdapter();

        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new InfiniteScrollListenerForGrid(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if(current_page<=totalPages){
                    loadStudentDetails(current_page);
                }
            }
        });

        if (!loaded){

            loadStudentDetails(1);

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

    void loadStudentDetails(int pageNo){

        HashMap<String,String> parametersMap = new HashMap<>();
        parametersMap.put("username",username);
        parametersMap.put("password",password);
        parametersMap.put("operation","students");
        parametersMap.put("pageNo",String.valueOf(pageNo));
        parametersMap.put("name",name);
        parametersMap.put("department",department);
        parametersMap.put("semester",semester);

        progressDialog = new ProgressDialog(ViewProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DownloadData data = new DownloadData(url,parametersMap,this);
        data.execute();

    }

    @Override
    public void getData(String JSONData) {
        try {

            JSONObject jsonObject = new JSONObject(JSONData);
            totalPages = jsonObject.getInt("totalPages");

            JSONArray jsonArray = jsonObject.getJSONArray("students");
            TextView tvNoData = (TextView) findViewById(R.id.tv_no_data_found);

            if(jsonArray.length() == 0){
                recyclerView.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }

            else {
                recyclerView.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject studentDetails = jsonArray.getJSONObject(i);

                    String otherDetails = studentDetails.getString("otherDetails");
                    String phone = studentDetails.getString("phone");
                    String imagePath = studentDetails.getString("imagePath");
                    String trainingDetails = studentDetails.getString("trainingDetails");
                    String studDepartment = studentDetails.getString("department");
                    String studSemester = studentDetails.getString("semester");
                    String passingyear = studentDetails.getString("passingyear");
                    String altphone = studentDetails.getString("altphone");
                    String projectDetails = studentDetails.getString("projectDetails");
                    String email = studentDetails.getString("email");
                    String dob = studentDetails.getString("dob");
                    String studName = studentDetails.getString("name");
                    String gender = studentDetails.getString("gender");
                    String enrollment = studentDetails.getString("enrollment");

                    Student student = new Student(studName, gender, dob, imagePath, enrollment, passingyear, phone, altphone, email, projectDetails, trainingDetails, otherDetails, studDepartment, studSemester);

                    studentList.add(student);

                }
                adapter.notifyDataSetChanged();
            }

        }catch (Exception e){}
        finally {
            progressDialog.dismiss();
        }
    }

    class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{


        @Override
        public void onBindViewHolder(StudentViewHolder holder, int position) {

            final Student student = studentList.get(position);

            holder.tvName.setText(student.getStudname());
            holder.tvSemester.setText(student.getStudsemester());
            holder.tvDepartment.setText(student.getStuddepartment());

            String url = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE).getString("url","") + getString(R.string.imageUrl);
            url = url + "?imageID=" + student.getImagePath();

            Picasso.with(getBaseContext()).load(url).error(R.drawable.profile).into(holder.ivProifleImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewProfileActivity.this,ViewStudentDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("student",student);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            });

        }

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cardview_viewprofile,parent,false);
            StudentViewHolder viewHolder = new StudentViewHolder(view);
            return viewHolder;
        }

        @Override
        public int getItemCount() {
            return studentList.size();
        }

        class StudentViewHolder extends RecyclerView.ViewHolder{

            CircularImageView ivProifleImage;
            TextView tvName , tvDepartment , tvSemester;

            StudentViewHolder(View itemView){

                super(itemView);

                ivProifleImage = (CircularImageView) itemView.findViewById(R.id.iv_profile_image);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvDepartment = (TextView) itemView.findViewById(R.id.tv_department);
                tvSemester = (TextView) itemView.findViewById(R.id.tv_semester);

            }

        }

    }
}

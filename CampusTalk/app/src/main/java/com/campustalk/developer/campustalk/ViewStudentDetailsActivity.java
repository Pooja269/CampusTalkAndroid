package com.campustalk.developer.campustalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by khushali on 22/04/2016.
 */
public class ViewStudentDetailsActivity extends AppCompatActivity{


    static Student student;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_myprofile);
        }

        @Override
        protected void onStart() {
            super.onStart();

            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("bundle");
            student = bundle.getParcelable("student");

            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            //set toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Student Profile");

            // Set up the ViewPager with the sections adapter.
            ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            ImageView ivProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE);

            String url = sharedPreferences.getString("url","") + getString(R.string.imageUrl);
            url = url + "?imageID=" + student.getImagePath();

            Picasso.with(getBaseContext()).load(url).error(R.drawable.profile).into(ivProfilePic);


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

        public static class PlaceholderFragment extends Fragment {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private static final String ARG_SECTION_NUMBER = "section_number";

            public PlaceholderFragment() {
            }

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            public static PlaceholderFragment newInstance(int sectionNumber) {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
                View rootView=null;
                System.out.println(getArguments().getInt(ARG_SECTION_NUMBER));
                if((getArguments().getInt(ARG_SECTION_NUMBER))==1) {
                    rootView = inflater.inflate(R.layout.fragment_generaldetail, container, false);
                    ((TextView) rootView.findViewById(R.id.tv_name)).setText(student.getStudname());
                    ((TextView) rootView.findViewById(R.id.tv_enrollment)).setText(student.getEnrollment());
                    ((TextView) rootView.findViewById(R.id.tv_DOB)).setText(student.getDob());
                    ((TextView) rootView.findViewById(R.id.tv_dept)).setText(student.getStuddepartment());
                    ((TextView)rootView.findViewById(R.id.tv_alternate_phone)).setText(student.getAltPhone());
                    ((TextView)rootView.findViewById(R.id.tv_email)).setText(student.getEmail());
                    ((TextView)rootView.findViewById(R.id.tv_gender)).setText(student.getGender());
                    ((TextView)rootView.findViewById(R.id.tv_passing_year)).setText(student.getPassingYear());
                    ((TextView)rootView.findViewById(R.id.tv_phone)).setText(student.getPhone());
                    ((TextView)rootView.findViewById(R.id.tv_sem)).setText(student.getStudsemester());

                }else{
                    rootView = inflater.inflate(R.layout.fragment_educationaldetail,container,false);
                    ((TextView)rootView.findViewById(R.id.tv_projectdetails)).setText(student.getProjectDetails());
                    ((TextView)rootView.findViewById(R.id.tv_trainingdetails)).setText(student.getTrainingDetails());
                    ((TextView)rootView.findViewById(R.id.tv_otherdetails)).setText(student.getOtherDetails());
                }
                return rootView;
            }
        }

        public class SectionsPagerAdapter extends FragmentPagerAdapter {

            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                // getItem is called to instantiate the fragment for the given page.
                // Return a PlaceholderFragment (defined as a static inner class below).
                return PlaceholderFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                // Show 2 total pages.
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Basic Details";
                    case 1:
                        return "Education Details";

                }
                return null;
            }
        }



}

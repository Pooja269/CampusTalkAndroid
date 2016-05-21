package com.campustalk.developer.campustalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by khushali on 12/04/2016.
 */
public class DocumentsDownloadFragment extends Fragment implements Callback{

    View view;
    boolean loaded = false;
    ProgressDialog progressDialog;
    HashMap<Integer,String> documentsMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_document,container,false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        documentsMap = new HashMap<>();

        Button btnPhoto = (Button) view.findViewById(R.id.btn_photo);
        setClickListenerOnButton(btnPhoto,1);

        Button btnSign = (Button) view.findViewById(R.id.btn_sign);
        setClickListenerOnButton(btnSign,2);

        Button btn10th = (Button) view.findViewById(R.id.btn_10th);
        setClickListenerOnButton(btn10th,3);

        Button btn12th = (Button) view.findViewById(R.id.btn_12th);
        setClickListenerOnButton(btn12th,4);

        Button btnSem1 = (Button) view.findViewById(R.id.btn_sem1);
        setClickListenerOnButton(btnSem1,5);

        Button btnSem2 = (Button) view.findViewById(R.id.btn_sem2);
        setClickListenerOnButton(btnSem2,6);

        Button btnSem3 = (Button) view.findViewById(R.id.btn_sem3);
        setClickListenerOnButton(btnSem3,7);

        Button btnSem4 = (Button) view.findViewById(R.id.btn_sem4);
        setClickListenerOnButton(btnSem4,8);

        Button btnSem5 = (Button) view.findViewById(R.id.btn_sem5);
        setClickListenerOnButton(btnSem5,9);

        Button btnSem6 = (Button) view.findViewById(R.id.btn_sem6);
        setClickListenerOnButton(btnSem6,10);

        Button btnSem7 = (Button) view.findViewById(R.id.btn_sem7);
        setClickListenerOnButton(btnSem7,11);

        Button btnSem8 = (Button) view.findViewById(R.id.btn_sem8);
        setClickListenerOnButton(btnSem8,12);

        Button btnLicense = (Button) view.findViewById(R.id.btn_driving);
        setClickListenerOnButton(btnLicense,13);

        Button btnClgid = (Button) view.findViewById(R.id.btn_clgid);
        setClickListenerOnButton(btnClgid,14);

        Button btnBirthCerti = (Button) view.findViewById(R.id.btn_birthCertificate);
        setClickListenerOnButton(btnBirthCerti,15);

        Button btnUniqueId = (Button) view.findViewById(R.id.btn_uniqueid);
        setClickListenerOnButton(btnUniqueId,16);

        Button btnOtherDoc = (Button) view.findViewById(R.id.btn_other);
        setClickListenerOnButton(btnOtherDoc,17);




        if(!loaded){
            loadData();
            loaded = true;
        }
    }

    void loadData(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
        String url = sharedPreferences.getString("url", "");
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password","");

        HashMap<String,String> parametersMap = new HashMap<>();
        parametersMap.put("username",username);
        parametersMap.put("password",password);
        parametersMap.put("operation","documents");
        parametersMap.put("enrollment",username);

        url = url + getString(R.string.url);

        DownloadData data = new DownloadData(url,parametersMap,this);
        data.execute();


    }

    @Override
    public void getData(String JSONData) {
        try{

            JSONObject jsonObject = new JSONObject(JSONData);
            String _10th = jsonObject.getString("10th");
            String _12th = jsonObject.getString("12th");
            String photo = jsonObject.getString("photo");
            String sign = jsonObject.getString("sign");
            String other = jsonObject.getString("ohterdoc");
            String uniqueId = jsonObject.getString("uniqueid");
            String sem1 = jsonObject.getString("sem1");
            String sem2 = jsonObject.getString("sem2");
            String sem3 = jsonObject.getString("sem3");
            String sem4 = jsonObject.getString("sem4");
            String sem5 = jsonObject.getString("sem5");
            String sem6 = jsonObject.getString("sem6");
            String sem7 = jsonObject.getString("sem7");
            String sem8 = jsonObject.getString("sem8");
            String license = jsonObject.getString("license");
            String clgid = jsonObject.getString("clgid");
            String birthCertificate = jsonObject.getString("birthcertificate");

            documentsMap.put(1,photo);
            documentsMap.put(2,sign);
            documentsMap.put(3,_10th);
            documentsMap.put(4,_12th);
            documentsMap.put(5,sem1);
            documentsMap.put(6,sem2);
            documentsMap.put(7,sem3);
            documentsMap.put(8,sem4);
            documentsMap.put(9,sem5);
            documentsMap.put(10,sem6);
            documentsMap.put(11,sem7);
            documentsMap.put(12,sem8);
            documentsMap.put(13,license);
            documentsMap.put(14,clgid);
            documentsMap.put(15,birthCertificate);
            documentsMap.put(16,uniqueId);
            documentsMap.put(17,other);

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            progressDialog.dismiss();
        }
    }

    void setClickListenerOnButton(Button button,final int i){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = documentsMap.get(i);
                String url = getActivity().getSharedPreferences(getString(R.string.config_settings),Context.MODE_PRIVATE).getString("url","") + getString(R.string.downloadDoc);

                if(path.equals("")){
                    Toast.makeText(getActivity(),"File Not Available!",Toast.LENGTH_SHORT).show();
                }else {
                    url = url + "?filePath="+ URLEncoder.encode(path);

                    DownloadDocument.downloadDoc(getActivity(),url,path);
                }
            }
        });

    }
}

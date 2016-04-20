package com.campustalk.developer.campustalk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by khushali on 08/04/2016.
 */
public class ConfigURLDialog extends DialogFragment {

    View view;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Configuration Settings");

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_config_url,null);

        dialog.setView(view);

        Button button = (Button) view.findViewById(R.id.btn_set);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetClick();
            }
        });

        return dialog.create();
    }

    void onSetClick(){
        //check whether edit text entry is empty or not
        String getURL = ((EditText)view.findViewById(R.id.et_url)).getText().toString();

        if(getURL.equals("")){
            ((EditText) view.findViewById(R.id.et_url)).setError("Enter URL");
        }else {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config_settings), Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("url", "http://"+getURL).commit();

            dismiss();
        }

    }
}

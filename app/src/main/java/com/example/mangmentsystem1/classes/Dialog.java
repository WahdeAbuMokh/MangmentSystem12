package com.example.mangmentsystem1.classes;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;

import com.example.mangmentsystem1.R;
import com.example.mangmentsystem1.ui.home.HomeFragment;

public class Dialog {
    private Activity activity;
    private Fragment fragment;
    private AlertDialog dialog;


    public Dialog(Activity myActivity) {
        activity = myActivity;
    }

    public Dialog(Fragment myfragment) {
        fragment=myfragment;
    }

    @SuppressLint("InflateParams")
    public void startLoadingdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());

        LayoutInflater inflater = fragment.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.lodaing, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissdialog() {
        dialog.dismiss();
    }

}
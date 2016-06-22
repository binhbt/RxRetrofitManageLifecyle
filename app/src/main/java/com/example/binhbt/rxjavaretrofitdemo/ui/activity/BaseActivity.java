package com.example.binhbt.rxjavaretrofitdemo.ui.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.binhbt.rxjavaretrofitdemo.net.RequestLoader;

/**
 * Created by binhbt on 6/8/2016.
 */
public class BaseActivity extends AppCompatActivity {
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }
    protected void onDestroy() {
        RequestLoader.getDefault().cancelAll(this);
        super.onDestroy();
    }
}

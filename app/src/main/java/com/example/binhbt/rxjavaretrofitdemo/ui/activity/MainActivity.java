package com.example.binhbt.rxjavaretrofitdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;


import com.example.binhbt.rxjavaretrofitdemo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_load)
    public void navigateToUserList() {
        startActivity(new Intent(this, UserListActivity.class));
    }
}

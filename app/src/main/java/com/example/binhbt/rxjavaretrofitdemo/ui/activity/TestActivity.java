package com.example.binhbt.rxjavaretrofitdemo.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.binhbt.rxjavaretrofitdemo.R;


/**
 * Created by binhbt on 6/10/2016.
 */
public class TestActivity extends MainActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button b = (Button)findViewById(R.id.btn_load);
        Log.e("aaa", b.getText().toString());
        Toast.makeText(this, b.getText().toString(), Toast.LENGTH_LONG).show();
    }
}

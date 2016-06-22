package com.example.binhbt.rxjavaretrofitdemo.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.binhbt.rxjavaretrofitdemo.net.ApiService;
import com.example.binhbt.rxjavaretrofitdemo.net.EndPoints;
import com.example.binhbt.rxjavaretrofitdemo.net.RequestLoader;


/**
 * Created by binhbt on 6/8/2016.
 */
public class BaseFragment extends Fragment {
    protected EndPoints api = new ApiService.Builder()
            .baseUrl(EndPoints.API_ENDPOINT)
            .logging(true)
            .build()
            .create(EndPoints.class);
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroyView() {
        RequestLoader.getDefault().cancelAll(this);
        super.onDestroyView();
    }
}

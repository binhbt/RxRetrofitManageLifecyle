package com.example.binhbt.rxjavaretrofitdemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.binhbt.rxjavaretrofitdemo.R;
import com.example.binhbt.rxjavaretrofitdemo.adapter.UsersAdapter;
import com.example.binhbt.rxjavaretrofitdemo.adapter.UsersLayoutManager;
import com.example.binhbt.rxjavaretrofitdemo.model.User;
import com.example.binhbt.rxjavaretrofitdemo.net.ApiService;
import com.example.binhbt.rxjavaretrofitdemo.net.EndPoints;
import com.example.binhbt.rxjavaretrofitdemo.net.RequestLoader;
import com.example.binhbt.rxjavaretrofitdemo.ui.activity.UserDetailActivity;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;

/**
 * Created by binhbt on 6/8/2016.
 */
public class UserListFragment extends BaseFragment {
    private UsersAdapter usersAdapter;// = new UsersAdapter(getActivity());
    @Bind(R.id.rv_users)
    RecyclerView rv_users;
    @Bind(R.id.rl_progress)
    RelativeLayout rl_progress;
    @Bind(R.id.rl_retry)
    RelativeLayout rl_retry;
    @Bind(R.id.bt_retry)
    Button bt_retry;

    public UserListFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupRecyclerView();
        return fragmentView;
    }

    private void startSubcriptions() {

        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        new RequestLoader.Builder()
                .api(interval)
                .callback(new RequestLoader.CallBack<Long>() {
                    @Override
                    public void onStart() {
                        Log.i("asdasd", "Start count");
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getActivity(), "got error " + t.toString(), Toast.LENGTH_LONG).show();
                        // Handle error
                        hideLoading();
                        showError("Request failed");
                        showRetry();
                    }

                    @Override
                    public void onFinish(Long result) {
                        Log.i("aassa", "Started in onResume(), running until in onDestroy(): " + result);
                    }
                })
                .container(this)
                .cancel(new RequestLoader.OnCancelRequest() {
                    @Override
                    public void onCancel() {
                        Log.i("asdasd", "Unsubscribing subscription from onResume()");
                    }
                })
                .container(this)
                .tag("interval")
                .build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestLoader.getDefault().cancelByTag("interval");
            }
        }, 10000);
        new RequestLoader.Builder()
                .api(api.userEntityList())
                .callback(new RequestLoader.CallBack<List<User>>() {
                    @Override
                    public void onStart() {
                        Toast.makeText(getActivity(), "onstart ", Toast.LENGTH_LONG).show();
                        hideRetry();
                        showLoading();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getActivity(), "got error " + t.toString(), Toast.LENGTH_LONG).show();
                        // Handle error
                        hideLoading();
                        showError("Request failed");
                        showRetry();
                    }

                    @Override
                    public void onFinish(List<User> result) {
                        //Log.e("Got result ", result.toString());
                        //Toast.makeText(getActivity(), "got error "+result.toString(), Toast.LENGTH_LONG).show();
                        // Update UI on the main thread
                        hideLoading();
                        renderUserList(result);
                        //Log.e("response", result.toString());
                    }
                })
                .cancel(new RequestLoader.OnCancelRequest() {
                    @Override
                    public void onCancel() {
                        Log.e("Cancel ", "cancelled ......");
                    }
                })
                .container(UserListFragment.this)
                .tag("get_list")
                .build();
        //RequestLoader.getDefault().cancelByTag("get_list");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            //this.loadUserList();
            startSubcriptions();
        }
    }

    private void setupRecyclerView() {
        usersAdapter = new UsersAdapter(getActivity());
        this.usersAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onUserItemClicked(User userModel) {
                Intent i = new Intent(getActivity(), UserDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", userModel);
                i.putExtras(b);
                startActivity(i);
            }
        });
        this.rv_users.setLayoutManager(new UsersLayoutManager(getActivity()));
        this.rv_users.setAdapter(usersAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rv_users.setAdapter(null);
        ButterKnife.unbind(this);
    }

    private void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    private void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    private void showError(String message) {
        this.showToastMessage(message);
    }

    private void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    private void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        UserListFragment.this.startSubcriptions();
    }

    private void renderUserList(Collection<User> userModelCollection) {
        if (userModelCollection != null) {
            this.usersAdapter.setUsersCollection(userModelCollection);
        }
    }

    private Subscription mGetPostSubscription;

    @Override
    public void onStop() {
        /*
        if (mGetPostSubscription != null) {
            mGetPostSubscription.unsubscribe();
        }
        */
        super.onStop();
    }
}
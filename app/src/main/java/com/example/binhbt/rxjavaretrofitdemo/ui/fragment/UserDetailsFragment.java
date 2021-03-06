package com.example.binhbt.rxjavaretrofitdemo.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.binhbt.rxjavaretrofitdemo.R;
import com.example.binhbt.rxjavaretrofitdemo.model.User;
import com.example.binhbt.rxjavaretrofitdemo.net.ApiService;
import com.example.binhbt.rxjavaretrofitdemo.net.EndPoints;
import com.example.binhbt.rxjavaretrofitdemo.net.RequestLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by binhbt on 6/8/2016.
 */
public class UserDetailsFragment extends BaseFragment {
    @Bind(R.id.iv_cover)
    ImageView iv_cover;
    @Bind(R.id.tv_fullname)
    TextView tv_fullname;
    @Bind(R.id.tv_email) TextView tv_email;
    @Bind(R.id.tv_followers) TextView tv_followers;
    @Bind(R.id.tv_description) TextView tv_description;
    @Bind(R.id.rl_progress)
    RelativeLayout rl_progress;
    @Bind(R.id.rl_retry) RelativeLayout rl_retry;
    @Bind(R.id.bt_retry)
    Button bt_retry;
    public UserDetailsFragment() {
        setRetainInstance(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            this.loadUserDetails();
        }
    }
    @Override public void onDestroyView() {
        super.onDestroyView();
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

    private void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    private void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    private void showError(String message) {
        this.showToastMessage(message);
    }
    private void renderUser(User user) {
        if (user != null) {
            Picasso.with(getActivity()).load(user.getCoverUrl()).into(this.iv_cover);
            this.tv_fullname.setText(user.getFullName());
            this.tv_email.setText(user.getEmail());
            this.tv_followers.setText(String.valueOf(user.getFollowers()));
            this.tv_description.setText(user.getDescription());
        }
    }
    private void loadUserDetails() {
        User user= (User)getArguments().getSerializable("user");
        new RequestLoader.Builder()
                .api(api.userEntityById(user.getUserId()))
                .callback(new RequestLoader.CallBack<User>() {
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
                    public void onFinish(User result) {
                        //Log.e("Got result ", result.toString());
                        //Toast.makeText(getActivity(), "got error "+result.toString(), Toast.LENGTH_LONG).show();
                        // Update UI on the main thread
                        hideLoading();
                        renderUser(result);
                        //Log.e("response", result.toString());
                    }
                })
                .cancel(new RequestLoader.OnCancelRequest() {
                    @Override
                    public void onCancel() {
                        Log.e("Cancel ", "cancelled ......");
                    }
                })
                .container(this)
                .tag("get_list")
                .build();
    }

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        UserDetailsFragment.this.loadUserDetails();
    }

    @Override
    public void onStop(){
        super.onStop();
    }
}

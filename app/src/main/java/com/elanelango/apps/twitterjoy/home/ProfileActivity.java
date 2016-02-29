package com.elanelango.apps.twitterjoy.home;

import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elanelango.apps.twitterjoy.R;
import com.elanelango.apps.twitterjoy.TwitterApplication;
import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.fragments.UserTweetsFragment;
import com.elanelango.apps.twitterjoy.models.User;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @Bind(R.id.tvScreenName)
    TextView tvScreenName;

    @Bind(R.id.tvDescription)
    TextView tvDescription;

    @Bind(R.id.tvFollowers)
    TextView tvFollowers;

    @Bind(R.id.tvFollowings)
    TextView tvFollowings;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        client = TwitterApplication.getRestClient();

        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        String screenName = null;
        if (user == null) {
            client.getUserInfo(new TwitterClient.UserInfoListener() {
                @Override
                public void onUserInfo(User user) {
                    loadUserInfo(user);
                }
            });
        } else {
            screenName = user.getScreenName();
            loadUserInfo(user);
        }

        if (savedInstanceState == null) {
            UserTweetsFragment userTweetsFragment = UserTweetsFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserTimeline, userTweetsFragment);
            ft.commit();
        }
    }

    private void loadUserInfo(User user) {
        getSupportActionBar().setTitle(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        tvDescription.setText(user.getDescription());

        String followers = Integer.toString(user.getFollowersCount());
        SpannableString styledFollowers = new SpannableString(followers + " followers");
        styledFollowers.setSpan(new StyleSpan(Typeface.BOLD), 0, followers.length(), 0);

        String followings = Integer.toString(user.getFriendsCount());
        SpannableString styledFollowings = new SpannableString(followings + " following");
        styledFollowings.setSpan(new StyleSpan(Typeface.BOLD), 0, followings.length(), 0);

        tvFollowers.setText(styledFollowers);
        tvFollowings.setText(styledFollowings);

        Glide.with(this)
                .load(user.getProfileImageUrl())
                .into(ivProfileImage);
    }
}

package com.elanelango.apps.twitterjoy.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.elanelango.apps.twitterjoy.R;
import com.elanelango.apps.twitterjoy.TwitterApplication;
import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.fragments.HomeTweetsFragment;
import com.elanelango.apps.twitterjoy.fragments.MentionsFragment;
import com.elanelango.apps.twitterjoy.models.Tweet;
import com.elanelango.apps.twitterjoy.models.User;
import com.elanelango.apps.twitterjoy.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.ComposeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private TwitterClient client;

    private TweetsPagerAdapter viewPagerAdapter;

    private HomeTweetsFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeDialog composeDialog = ComposeDialog.newInstance();
                composeDialog.show(fm, "compose_tweet");
            }
        });

        viewPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabStrip.setViewPager(viewPager);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.mnuProfile) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }
        return true;
    }

    @Override
    public void onFinishCompose(String tweet) {
        client.postTweet(tweet, new TwitterClient.PostTweetListener() {
            @Override
            public void onSuccess(Tweet postedTweet) {
                homeFragment.addOnTop(postedTweet);
            }
        });
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String pageTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    homeFragment = new HomeTweetsFragment();
                    return homeFragment;
                case 1:
                    return new MentionsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return pageTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles[position];
        }
    }
}

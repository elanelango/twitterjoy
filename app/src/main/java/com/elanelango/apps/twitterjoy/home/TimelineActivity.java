package com.elanelango.apps.twitterjoy.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.elanelango.apps.twitterjoy.R;
import com.elanelango.apps.twitterjoy.TwitterApplication;
import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.ComposeListener {

    private TwitterClient client;

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<Tweet> tweets;
    TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeDialog composeDialog = ComposeDialog.newInstance();
                composeDialog.show(fm, "compose_tweet");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTweets.setLayoutManager(layoutManager);

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(tweets);
        rvTweets.setAdapter(tweetsAdapter);

        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    private void populateTimeline() {
        client.getHomeTimeline(new TwitterClient.TweetsListener() {
            @Override
            public void onTweets(List<Tweet> tweets) {
                tweetsAdapter.addAll(tweets);
                Log.d("DEBUG", tweets.toString());
            }

            @Override
            public void onError(String errorText) {

            }
        });
    }

    @Override
    public void onFinishCompose(String tweet) {
        Toast.makeText(this, tweet, Toast.LENGTH_SHORT).show();
        client.postTweet(new TwitterClient.PostTweetListener() {
            @Override
            public void onSuccess() {

            }
        }, tweet);
    }
}

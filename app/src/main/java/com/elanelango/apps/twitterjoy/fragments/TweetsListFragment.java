package com.elanelango.apps.twitterjoy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elanelango.apps.twitterjoy.R;
import com.elanelango.apps.twitterjoy.TwitterClient;
import com.elanelango.apps.twitterjoy.home.TweetsAdapter;
import com.elanelango.apps.twitterjoy.models.Tweet;
import com.elanelango.apps.twitterjoy.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eelango on 2/27/16.
 */
public abstract class TweetsListFragment extends Fragment {

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    protected ArrayList<Tweet> tweets;
    protected TweetsAdapter tweetsAdapter;

    protected TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, v);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTweets.setLayoutManager(layoutManager);

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(tweets);
        rvTweets.setAdapter(tweetsAdapter);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getOlderTweets(new TwitterClient.TweetsListener() {
                    @Override
                    public void onTweets(List<Tweet> tweets) {
                        tweetsAdapter.addAll(tweets);
                    }

                    @Override
                    public void onError(String errorText) {

                    }
                });
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewerTweets(new TwitterClient.TweetsListener() {
                    @Override
                    public void onTweets(List<Tweet> tweets) {
                        tweetsAdapter.addAllToFront(tweets);
                        swipeContainer.setRefreshing(false);
                        rvTweets.scrollToPosition(0);
                    }

                    @Override
                    public void onError(String errorText) {

                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new TwitterClient(getContext());
        loadTweets();
    }

    private void loadTweets() {
        getInitialTweets(new TwitterClient.TweetsListener() {
            @Override
            public void onTweets(List<Tweet> tweets) {
                tweetsAdapter.addAll(tweets);
            }

            @Override
            public void onError(String errorText) {

            }
        });
    }
    protected abstract void getInitialTweets(TwitterClient.TweetsListener listener);
    protected abstract void getOlderTweets(TwitterClient.TweetsListener listener);
    protected abstract void getNewerTweets(TwitterClient.TweetsListener listener);


    public void addAll(List<Tweet> tweets) {
        tweetsAdapter.addAll(tweets);
    }

    public void addOnTop(Tweet tweet) {
        tweetsAdapter.add(0, tweet);
        rvTweets.scrollToPosition(0);
    }
}

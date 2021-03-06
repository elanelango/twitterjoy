package com.elanelango.apps.twitterjoy;

import org.apache.http.Header;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.elanelango.apps.twitterjoy.models.Tweet;
import com.elanelango.apps.twitterjoy.models.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public interface TweetsListener {
        public void onTweets(List<Tweet> tweets);
        public void onError(String errorText);
    }

    public interface PostTweetListener {
        public void onSuccess(Tweet postedTweet);
    }

    public interface UserInfoListener {
        public void onUserInfo(User user);
    }

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "IU3BFX8QtakfbaNdHle8RWrvV";
	public static final String REST_CONSUMER_SECRET = "SKGjpksV3dx3syC5K8MbMvSJ5ckG8t4dJqBR2lNwbBgt23TaQJ";
	public static final String REST_CALLBACK_URL = "oauth://twitterjoy"; // Change this (here and in manifest)

    Gson gson;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        gson = gsonBuilder.create();
	}

    private void getTimeline(String apiUrl, long since_id, long max_id, String screenName, final TweetsListener handler) {
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (since_id > 0)
            params.put("since_id", since_id);

        if (max_id > 0)
            params.put("max_id", max_id);

        if (screenName != null)
            params.put("screen_name", screenName);

        getClient().get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("ELANLOG", responseString);
                Type tweetList = new TypeToken<ArrayList<Tweet>>() {}.getType();
                ArrayList<Tweet> tweets = gson.fromJson(responseString, tweetList);
                handler.onTweets(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("ELANLOG", responseString);
            }
        });
    }

    public void getHomeTweets(long since_id, long max_id, final TweetsListener handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        getTimeline(apiUrl, since_id, max_id, null, handler);
    }

    public void getMentions(long since_id, long max_id, final TweetsListener handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        getTimeline(apiUrl, since_id, max_id, null, handler);
    }

    public void postTweet(String tweet, final PostTweetListener listener) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        getClient().post(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet postedTweet = gson.fromJson(responseString, Tweet.class);
                listener.onSuccess(postedTweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

        });
    }

    public void getUserTimeline(String screenName, long since_id, long max_id, TweetsListener handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        getTimeline(apiUrl, since_id, max_id, screenName, handler);
    }

    public void getUserInfo(final UserInfoListener handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                User user = gson.fromJson(responseString, User.class);
                handler.onUserInfo(user);
            }
        });
    }
}
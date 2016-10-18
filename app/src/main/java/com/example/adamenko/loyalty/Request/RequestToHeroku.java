package com.example.adamenko.loyalty.Request;

import android.util.Log;

import com.example.adamenko.loyalty.OnMyRequestListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Adamenko on 06.10.2016.
 */

public class RequestToHeroku {

    public RequestToHeroku() {

    }

    public void HerokuGet(HashMap<String, String> param, String page, final OnMyRequestListener mRequestListener) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams(param);
        client.get("https://simpletech-loyalty.herokuapp.com/api/" + page, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String value = new String(responseBody);
                    JSONObject valueTrue = new JSONObject(value);
                    mRequestListener.onSuccess(valueTrue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String value = "Нет соединения с интернетом";
                if (responseBody != null) {
                    value = new String(responseBody);
                } else
                    mRequestListener.onFailure(value);
                Log.e("Request", value);
            }

        });

    }

    public void HerokuPost(HashMap<String, String> param, String page, final OnMyRequestListener mRequestListener) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams(param);
        client.post("https://simpletech-loyalty.herokuapp.com/api/" + page, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String value = new String(responseBody);
                    JSONObject valueTrue = new JSONObject(value);
                    mRequestListener.onSuccess(valueTrue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String value = "Нет соединения с интернетом";
                if (responseBody != null) {
                    value = new String(responseBody);
                } else
                    mRequestListener.onFailure(value);
                Log.e("Request", value);
            }

        });

    }


}

package com.example.adamenko.loyalty.Fragments;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Adamenko on 29.09.2016.
 */

public class Subscribe {
    public Subscribe(String barCode,String id) {

        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("contact",barCode);
        param.put("topic",id);
        param.put("app", "AIzaSyB2zA4TL9napLFnR0cNI_I9gcdfg9qmZ6g");
        RequestParams params = new RequestParams(param);
        client.post("https://safe-forest-50436.herokuapp.com/api/subscribe", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String value = new String(responseBody);
                    JSONObject valueTrue = new JSONObject(value);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String value = new String(responseBody);
                    String code = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        });

    }
}

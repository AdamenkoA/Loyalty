package com.example.adamenko.loyalty.Request;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.example.adamenko.loyalty.Content.TopicContent;
import com.example.adamenko.loyalty.Decoration.DividerItemDecoration;
import com.example.adamenko.loyalty.Fragments.TopicFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Topic {

    public Topic(final RecyclerView recyclerView, final TopicFragment.OnListFragmentClickListener mListener, final Drawable dividerDrawable) {
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("app", "AIzaSyB2zA4TL9napLFnR0cNI_I9gcdfg9qmZ6g");
        RequestParams params = new RequestParams(param);
        client.get("https://simpletech-loyalty.herokuapp.com/api/topics", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String value = new String(responseBody);
                    JSONObject valueTrue = new JSONObject(value);

                    String title;
                    String description;
                    String id;

                    List<TopicContent> items = new ArrayList<TopicContent>();

                    JSONArray cast = valueTrue.getJSONArray("topics");
                    for (int i = 0; i < cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        id = actor.getString("id");
                        title = actor.getString("title");
                        description = actor.getString("description");
                        items.add(new TopicContent(Integer.parseInt(id), title, description, false));
                    }

                    //   ListViewAdapter lviewAdapter = new ListViewAdapter(context,title,description);
                   // recyclerView.setAdapter(new MyTopicRVA(items, mListener));
                    RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                    recyclerView.addItemDecoration(dividerItemDecoration);

                    //  listView.setAdapter(lviewAdapter);
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

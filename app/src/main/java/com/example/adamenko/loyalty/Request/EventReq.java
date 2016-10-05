package com.example.adamenko.loyalty.Request;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.example.adamenko.loyalty.Adapters.MyEventsRVA;
import com.example.adamenko.loyalty.Content.EventContent;
import com.example.adamenko.loyalty.Decoration.DividerItemDecoration;
import com.example.adamenko.loyalty.Fragments.EventsFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Adamenko on 29.09.2016.
 */

public class EventReq {
    public EventReq(final RecyclerView recyclerView, final EventsFragment.OnListFragmentInteractionListener mListener, final Drawable dividerDrawable) {
        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("app", "AIzaSyB2zA4TL9napLFnR0cNI_I9gcdfg9qmZ6g");
        RequestParams params = new RequestParams(param);
        client.get("https://simpletech-loyalty.herokuapp.com/api/events", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String value = new String(responseBody);
                    JSONObject valueTrue = new JSONObject(value);

                    String id;
                    String topicId;
                    String date;
                    String time;
                    String title;
                    Date dateF=new Date();
                    String description;
                    List<EventContent> items = new ArrayList<EventContent>();

                    JSONArray cast = valueTrue.getJSONArray("events");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        id= actor.getString("id");
                        topicId= actor.getString("topic_id");
                        title= actor.getString("title");

                        DateFormat df = new SimpleDateFormat("dd.MM");
                        String string = "January 2, 2010";
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                        try {
                            dateF= format.parse(actor.getString("date"));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        date  = df.format(dateF)+"";
                        time= actor.getString("time");
                        description= actor.getString("description");
                        items.add(new EventContent(id,topicId,date,time,title,description));
                    }

                    //   ListViewAdapter lviewAdapter = new ListViewAdapter(context,title,description);
                    recyclerView.setAdapter(new MyEventsRVA(items, mListener));
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

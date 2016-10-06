package com.example.adamenko.loyalty.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adamenko.loyalty.Adapters.MyEventsRVA;
import com.example.adamenko.loyalty.Content.EventContent;
import com.example.adamenko.loyalty.Decoration.DividerItemDecoration;
import com.example.adamenko.loyalty.OnMyRequestListener;
import com.example.adamenko.loyalty.R;
import com.example.adamenko.loyalty.Request.RequestFroAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventsFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    Drawable dividerDrawable;
    RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;

    public EventsFragment() {
    }

    @SuppressWarnings("unused")
    public static EventsFragment newInstance(int columnCount) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("app", "AIzaSyB2zA4TL9napLFnR0cNI_I9gcdfg9qmZ6g");
            new RequestFroAll(param, "events", new OnMyRequestListener() {
                @Override
                public void onSuccess(JSONObject valueTrue) {
                    String id;
                    String topicId;
                    String date;
                    String time;
                    String title;
                    Date dateF = new Date();
                    String description;
                    List<EventContent> items = new ArrayList<EventContent>();

                    JSONArray cast = null;
                    try {
                        cast = valueTrue.getJSONArray("events");
                        for (int i = 0; i < cast.length(); i++) {
                            JSONObject actor = cast.getJSONObject(i);
                            id = actor.getString("id");
                            topicId = actor.getString("topic_id");
                            title = actor.getString("title");

                            DateFormat df = new SimpleDateFormat("dd.MM");
                            String string = "January 2, 2010";
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                            try {
                                dateF = format.parse(actor.getString("date"));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            date = df.format(dateF) + "";
                            time = actor.getString("time");
                            description = actor.getString("description");
                            items.add(new EventContent(id, topicId, date, time, title, description));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    recyclerView.setAdapter(new MyEventsRVA(items, mListener));
                    RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                }

                @Override
                public void onFailure(String value) {

                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(EventContent item);
    }
}

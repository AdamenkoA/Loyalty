package com.example.adamenko.loyalty.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adamenko.loyalty.Adapters.MyEventsRVA;
import com.example.adamenko.loyalty.Content.EventContent;
import com.example.adamenko.loyalty.DataBase.MySQLiteHelper;
import com.example.adamenko.loyalty.Decoration.DividerItemDecoration;
import com.example.adamenko.loyalty.OnMyRequestListener;
import com.example.adamenko.loyalty.R;
import com.example.adamenko.loyalty.Request.RequestToHeroku;

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
    private MySQLiteHelper db;
    Context context;

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

        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider);
            HashMap<String, String> param = new HashMap<String, String>();
            RequestToHeroku rth = new RequestToHeroku();
            db = new MySQLiteHelper(this.getContext());
            param.put("app", db.getSettings("app"));
            if (isOnline()) {
                rth.HerokuGet(param, "events", new OnMyRequestListener() {
                    @Override
                    public void onSuccess(JSONObject valueTrue) {
                        List<EventContent> items = new ArrayList<>();
                        try {
                            JSONArray cast = valueTrue.getJSONArray("events");
                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String color = db.getSubscribe(Integer.parseInt(actor.getString("topic_id")), db.getWritableDatabase()) ? db.getTopic(actor.getString("topic_id")).getColor() : "#000000";
                                EventContent ec = new EventContent(actor.getString("id"), actor.getString("topic_id"),
                                        DateFormater(actor.getString("date")), actor.getString("time"), actor.getString("title"), actor.getString("description"), color);

                                db.addEvents(ec);

                                items.add(ec);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView.setAdapter(new MyEventsRVA(items, mListener, context));
                        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }

                    @Override
                    public void onFailure(String value) {
                        Toast.makeText(context,
                                value, Toast.LENGTH_SHORT).show();
                        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                        recyclerView.setAdapter(new MyEventsRVA(db.getEvents(), mListener, context));
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }
                });
            } else {
                Toast.makeText(context,
                        context.getText(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                recyclerView.setAdapter(new MyEventsRVA(db.getEvents(), mListener, context));
                recyclerView.addItemDecoration(dividerItemDecoration);
            }

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

    private String DateFormater(String date) {
        Date dateE = new Date();
        DateFormat df = new SimpleDateFormat("dd.MM");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            dateE = format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(dateE) + "";
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

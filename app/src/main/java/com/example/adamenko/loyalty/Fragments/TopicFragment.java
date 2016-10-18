package com.example.adamenko.loyalty.Fragments;

import android.app.Activity;
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

import com.example.adamenko.loyalty.Adapters.MyTopicRVA;
import com.example.adamenko.loyalty.Content.TopicContent;
import com.example.adamenko.loyalty.DataBase.MySQLiteHelper;
import com.example.adamenko.loyalty.Decoration.DividerItemDecoration;
import com.example.adamenko.loyalty.OnMyRequestListener;
import com.example.adamenko.loyalty.R;
import com.example.adamenko.loyalty.Request.RequestToHeroku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TopicFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentClickListener mListener;
    Context context;
    Activity ac;
    RecyclerView recyclerView;
    Drawable dividerDrawable;
    private MySQLiteHelper db;

    public TopicFragment() {
    }

    @SuppressWarnings("unused")
    public static TopicFragment newInstance(int columnCount) {
        TopicFragment fragment = new TopicFragment();
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
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider);
            HashMap<String, String> param = new HashMap<>();
            RequestToHeroku rth = new RequestToHeroku();
            db = new MySQLiteHelper(this.getContext());
            param.put("app", db.getSettings("app"));
            if (isOnline()) {
                rth.HerokuGet(param, "topics", new OnMyRequestListener() {
                    @Override
                    public void onSuccess(JSONObject valueTrue) {
                        List<TopicContent> items = new ArrayList<>();
                        Boolean isSubscribe = false;
                        String color = "";

                        try {
                            JSONArray cast = valueTrue.getJSONArray("topics");
                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                color = actor.getString("color").equals("") ? "#000000" : actor.getString("color");
                                isSubscribe = db.getSubscribe(Integer.parseInt(actor.getString("id")), db.getWritableDatabase());
                                TopicContent tc = new TopicContent(Integer.parseInt(actor.getString("id")), actor.getString("title"),
                                        actor.getString("description"), isSubscribe, color);
                                db.addTopics(tc);
                                items.add(tc);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                        recyclerView.setAdapter(new MyTopicRVA(items, mListener, context));
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }

                    @Override
                    public void onFailure(String value) {
                        Toast.makeText(context,
                                value, Toast.LENGTH_SHORT).show();
                        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                        recyclerView.setAdapter(new MyTopicRVA(db.getTopics(), mListener, context));
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }
                });
            } else {
                Toast.makeText(context,
                        context.getText(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                recyclerView.setAdapter(new MyTopicRVA(db.getTopics(), mListener, context));
                recyclerView.addItemDecoration(dividerItemDecoration);
            }

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ac = getActivity();
        if (context instanceof OnListFragmentClickListener) {
            mListener = (OnListFragmentClickListener) context;

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

    public interface OnListFragmentClickListener {
        void onListFragmentClickListener(TopicContent item);

        void onListFragmentLongClickListener(TopicContent item, Boolean holder, OnDialogClick mClickListener);
    }

    public interface OnDialogClick {
        void onDialogClick(Boolean mFlag, Context nContext, String color);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

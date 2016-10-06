package com.example.adamenko.loyalty.Fragments;

import android.app.Activity;
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

import com.example.adamenko.loyalty.Adapters.MyTopicRVA;
import com.example.adamenko.loyalty.Content.TopicContent;
import com.example.adamenko.loyalty.Decoration.DividerItemDecoration;
import com.example.adamenko.loyalty.OnMyRequestListener;
import com.example.adamenko.loyalty.R;
import com.example.adamenko.loyalty.Request.RequestFroAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TopicFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    OnMyRequestListener mRequestListener;
    Context context;
    Activity ac;
    RecyclerView recyclerView;
    Drawable dividerDrawable;

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
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("app", "AIzaSyB2zA4TL9napLFnR0cNI_I9gcdfg9qmZ6g");
            new RequestFroAll(param, "topics", new OnMyRequestListener() {
                @Override
                public void onSuccess(JSONObject valueTrue) {
                    String title;
                    String description;
                    String id;

                    JSONArray cast = null;
                    List<TopicContent> items = new ArrayList<TopicContent>();
                    try {
                        cast = valueTrue.getJSONArray("topics");
                        for (int i = 0; i < cast.length(); i++) {
                            JSONObject actor = cast.getJSONObject(i);
                            id = actor.getString("id");
                            title = actor.getString("title");
                            description = actor.getString("description");
                            items.add(new TopicContent(Integer.parseInt(id), title, description, false));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    recyclerView.setAdapter(new MyTopicRVA(items, mListener));
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
        ac = getActivity();
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
        void onListFragmentInteraction(TopicContent item);
    }

}

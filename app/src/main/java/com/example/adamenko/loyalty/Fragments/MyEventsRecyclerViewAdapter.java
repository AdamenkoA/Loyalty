package com.example.adamenko.loyalty.Fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adamenko.loyalty.Fragments.EventsFragment.OnListFragmentInteractionListener;
import com.example.adamenko.loyalty.Fragments.dummy.DummyContent.DummyItem;
import com.example.adamenko.loyalty.Fragments.dummy.EventContent;
import com.example.adamenko.loyalty.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEventsRecyclerViewAdapter extends RecyclerView.Adapter<MyEventsRecyclerViewAdapter.ViewHolder> {

    private final List<EventContent> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyEventsRecyclerViewAdapter(List<EventContent> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDate.setText(mValues.get(position).date);
        holder.mTime.setText(mValues.get(position).time);
        holder.mTitle.setText(mValues.get(position).title);
        holder.mDescription.setText(mValues.get(position).description);



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDate;
        public final TextView mTime;
        public final TextView mTitle;
        public final TextView mDescription;
        public EventContent mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDate = (TextView) view.findViewById(R.id.date_event);
            mTime = (TextView) view.findViewById(R.id.time_event);
            mTitle = (TextView) view.findViewById(R.id.title_event);
            mDescription = (TextView) view.findViewById(R.id.description_event);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}

package com.example.adamenko.loyalty.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adamenko.loyalty.Content.EventContent;
import com.example.adamenko.loyalty.Fragments.EventsFragment.OnListFragmentInteractionListener;
import com.example.adamenko.loyalty.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MyEventsRVA extends RecyclerView.Adapter<MyEventsRVA.ViewHolder> {

    private final List<EventContent> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;
    private Matcher matcher;
    private Pattern pattern;
    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    public MyEventsRVA(List<EventContent> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }
    private boolean validate(final String hexColorCode) {
        pattern = Pattern.compile(HEX_PATTERN);
        matcher = pattern.matcher(hexColorCode);
        return matcher.matches();

    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDate.setText(mValues.get(position).getDate());
        holder.mTime.setText(mValues.get(position).getTime());
        holder.mTitle.setText(mValues.get(position).getTitle());
        if (!holder.mItem.getColor().equals("#000000")&& validate(holder.mItem.getColor())) {
            holder.mTitle.setTextColor(Color.parseColor(holder.mItem.getColor()));
            holder.mDescription.setTextColor(Color.parseColor(holder.mItem.getColor()));
        }
        final ViewGroup.LayoutParams params = holder.mDescription.getLayoutParams();
        params.height = mContext.getResources().getDimensionPixelSize(R.dimen.text_view_height_min);
        holder.mDescription.setLayoutParams(params);
        holder.mDescription.setText(mValues.get(position).getDescription());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if (!holder.mFlag) {
                        params.height = mContext.getResources().getDimensionPixelSize(R.dimen.text_view_height);
                        holder.mDescription.setLayoutParams(params);
                        holder.mDescription.setVisibility(View.VISIBLE);
                        RotateAnimation rotate =
                                new RotateAnimation(180, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(300);
                        rotate.setFillAfter(true);
                        holder.mArrow.setAnimation(rotate);
                        holder.mFlag = true;
                        mListener.onListFragmentInteraction(holder.mItem);
                    } else {
                        params.height = 0;
                        holder.mDescription.setLayoutParams(params);
                        holder.mDescription.setVisibility(View.INVISIBLE);
                        RotateAnimation rotate =
                                new RotateAnimation(360, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(300);
                        rotate.setFillAfter(true);
                        holder.mFlag = false;
                        holder.mArrow.setAnimation(rotate);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mDate;
        final TextView mTime;
        final TextView mTitle;
        final TextView mDescription;
        EventContent mItem;
        ImageView mArrow;
        Boolean mFlag;
        Boolean mFlagL;


        ViewHolder(View view) {
            super(view);
            mView = view;
            mDate = (TextView) view.findViewById(R.id.date_event);
            mTime = (TextView) view.findViewById(R.id.time_event);
            mTitle = (TextView) view.findViewById(R.id.title_event);
            mDescription = (TextView) view.findViewById(R.id.description_event);
            mArrow = (ImageView) view.findViewById(R.id.ic_arrow_down_events);
            mFlag = false;
            mFlagL = false;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }

}

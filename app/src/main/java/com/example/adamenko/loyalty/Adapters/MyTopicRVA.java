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

import com.example.adamenko.loyalty.Content.TopicContent;
import com.example.adamenko.loyalty.Fragments.TopicFragment;
import com.example.adamenko.loyalty.R;

import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MyTopicRVA extends RecyclerView.Adapter<MyTopicRVA.ViewHolder> {

    private final List<TopicContent> mValues;
    private final TopicFragment.OnListFragmentClickListener mListener;

    private final Context mContext;

    public MyTopicRVA(List<TopicContent> items, TopicFragment.OnListFragmentClickListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getDescription());
        if (holder.mItem.getSubscribe()&& !holder.mItem.getColor().equals("")) {
            holder.mContentView.setTextColor(Color.parseColor(holder.mItem.getColor()));
            holder.mIdView.setTextColor(Color.parseColor(holder.mItem.getColor()));
        }
        final ViewGroup.LayoutParams params = holder.mContentView.getLayoutParams();
        params.height = 0;
        holder.mContentView.setLayoutParams(params);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if (!holder.mFlag) {
                        params.height = mContext.getResources().getDimensionPixelSize(R.dimen.text_view_height);
                        holder.mContentView.setLayoutParams(params);
                        holder.mContentView.setVisibility(View.VISIBLE);
                        RotateAnimation rotate =
                                new RotateAnimation(180, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(300);
                        rotate.setFillAfter(true);
                        holder.mArrow.setAnimation(rotate);
                        holder.mFlag = true;
                        mListener.onListFragmentClickListener(holder.mItem);
                    } else {
                        params.height = 0;
                        holder.mContentView.setLayoutParams(params);
                        holder.mContentView.setVisibility(View.INVISIBLE);
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
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null != mListener) {
                    holder.mFlagL = !holder.mFlagL;
                    mListener.onListFragmentLongClickListener(holder.mItem, holder.mFlagL, new TopicFragment.OnDialogClick() {
                        @Override
                        public void onDialogClick(Boolean mFlag, Context nContext, String mColor) {
                            if (!holder.mItem.getColor().equals("")) {
                                holder.mContentView.setTextColor(Color.parseColor(mColor));
                                holder.mIdView.setTextColor(Color.parseColor(mColor));
                            }
                        }
                    });
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        TopicContent mItem;
        ImageView mArrow;
        Boolean mFlag;
        Boolean mFlagL;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id_topic);
            mContentView = (TextView) view.findViewById(R.id.content_topic);
            mArrow = (ImageView) view.findViewById(R.id.ic_arrow_down);
            mFlag = false;
            mFlagL = false;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

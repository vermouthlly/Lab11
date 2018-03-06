package com.example.dell.lab11;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/12/15.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private OnRecyclerViewItemClickListener mItemClickListener = null;
    private List<Github> mUserList = new ArrayList<>();

    static public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, login, blog;
        ViewHolder(View view) {
            super(view);
            login = view.findViewById(R.id.user_name);
            id = view.findViewById(R.id.user_id);
            blog = view.findViewById(R.id.user_blog);
        }
    }

    public void appendItem(Github github) {
        mUserList.add(github);
        notifyDataSetChanged();
    }

    public Github getItem(int position) {
        return mUserList.get(position);
    }

    public void removeItem(int position) {
        mUserList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllItem() {
        mUserList.clear();
        notifyDataSetChanged();
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(mItemClickListener != null)
                    mItemClickListener.onClick(holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v)
            {
                if(mItemClickListener != null)
                    mItemClickListener.onLongClick(holder.getAdapterPosition());
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final CardAdapter.ViewHolder holder, int position) {
        Github github = mUserList.get(position);
        holder.login.setText(github.getLogin());
        holder.id.setText(String.valueOf(github.getId()));
        holder.blog.setText(github.getBlog());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }
}
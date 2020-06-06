package com.example.animesuggest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeCardAdapter extends RecyclerView.Adapter<AnimeCardAdapter.AnimeCardViewHolder> {
    public List<AnimeCard> list;
    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(int position, Object object);
    }


    public AnimeCardAdapter(List<AnimeCard> list, Context context, OnItemClickListener onItemClickListener) {
        this.list = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public static class AnimeCardViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        int id;
        ImageView coverImage;
        TextView title;

        public AnimeCardViewHolder(View view) {
            super(view);

            // item layout might not be right?
//            item = (LinearLayout) view.findViewById(R.id.animecard_layout);
            coverImage = (ImageView) view.findViewById(R.id.cardview_imgdisplay);
            title = (TextView) view.findViewById(R.id.cardview_txtdisplay);
        }
    }

    @Override
    public AnimeCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_anime, viewGroup, false);
        AnimeCardViewHolder viewHolder = new AnimeCardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AnimeCardViewHolder viewHolder, final int position) {
        // Update:
        if (list.get(position).getTitle() != null) {
            viewHolder.title.setText(list.get(position).getTitle());
        }
        else {
            viewHolder.title.setText(R.string.unavailabletitle);
        }

        Picasso.get()
                .load(list.get(position).getImageUrl())
                .into(viewHolder.coverImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(position, list.get(position));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

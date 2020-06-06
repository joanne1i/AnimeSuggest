package com.example.animesuggest;

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
            item = (LinearLayout) view.findViewById(R.id.animecard_layout);
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
        viewHolder.title.setText(list.get(position).getTitle());
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












//public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {
//
//    private List<AnimeCard> mAnimeCardList;
//    // not sure if this is the right listener
//    private RecyclerViewClickListener listener;
//
//    public AnimeAdapter(List<AnimeCard> mAnimeCardList, RecyclerViewClickListener listener) {
//        this.mAnimeCardList = mAnimeCardList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_anime, parent, false);
//        return new AnimeViewHolder(view, listener);
//    }
//
////    @Override
////    public void onBindViewHolder(@NonNull AnimeAdapter.AnimeViewHolder holder, int position) {
////        AnimeCard animeCard = mAnimeCardList.get(position);
////        holder.title.setText(animeCard.getTitle());
////        if(animeCard.getCoverImageURL() != null) {
////            Picasso.get()
////                    .load(animeCard.getCoverImageURL())
////                    .into(holder.coverImg);
////        }
////    }
//
//    @Override
//    public int getItemCount() {
//        return mAnimeCardList.size();
//    }
//
//    // ViewHolder ? made static
//    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
//
//        TextView title;
//        ImageView coverImg;
//
//        public AnimeViewHolder(@NonNull View itemView, final RecyclerViewClickListener listener) {
//            super(itemView);
//            title = itemView.findViewById(R.id.cardview_txtdisplay);
//            coverImg = itemView.findViewById(R.id.cardview_imgdisplay);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(listener != null) {
////                        listener.onRowClicked(getAdapterPosition());
//                        Log.d("123LISTENER", "listener works");
//                        listener.onRowClicked(getAdapterPosition());
//                    }
//
//                }
//            });
//
//        }
//    }
//}

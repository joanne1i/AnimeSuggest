package com.example.animesuggest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import com.example.SeasonalpagetwoQuery;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;

public class PopularThisSeasonFragment extends Fragment {

    private static List<AnimeCard> mlist = new ArrayList<>();


    String title;
    String imgurl;
    int id;
    int PAGEMAX = 50;
    AnimeCardAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popularseason, container, false);
        mlist.clear();

        // RecyclerView tings
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_id);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);

        Bundle bundle = new Bundle();
        Logger.addLogAdapter(new AndroidLogAdapter());
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("https://graphql.anilist.co")
                .okHttpClient(okHttpClient)
                .build();

        apolloClient.query(
                SeasonalpagetwoQuery.builder()
                        .page(2)
                        .build()
        ).enqueue(new ApolloCall.Callback<SeasonalpagetwoQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<SeasonalpagetwoQuery.Data> response) {
                int remaining = response.getData().Page().pageInfo().total();
                for (int i = 0; i < remaining-PAGEMAX; i++) {
                    id = response.getData().Page().media().get(i).id();
                    bundle.putInt("id", id);
                    title = response.getData().Page().media().get(i).title().romaji();
                    imgurl = response.getData().Page().media().get(i).coverImage().extraLarge();
                    mlist.add(new AnimeCard(id, title, imgurl));
                }
//                Collections.reverse(mlist);

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(e.getLocalizedMessage(), "error");
            }
        });

        apolloClient.query(
                SeasonalpagetwoQuery.builder()
                        .page(1)
                        .build()
        ).enqueue(new ApolloCall.Callback<SeasonalpagetwoQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<SeasonalpagetwoQuery.Data> response) {
                Intent intent = new Intent();
                for (int i = PAGEMAX-1; i >= 0; i--) {
                    id = response.getData().Page().media().get(i).id();
                    bundle.putInt("id", id);
                    title = response.getData().Page().media().get(i).title().romaji();
                    imgurl = response.getData().Page().media().get(i).coverImage().extraLarge();
                    mlist.add(new AnimeCard(id, title, imgurl));
                }
                Collections.reverse(mlist);
                // have to move the portion of the background task that updates the UI onto the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        adapter = new AnimeCardAdapter(mlist, getActivity(), new AnimeCardAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, Object object) {
                                Bundle bundle = new Bundle();
                                int newID = mlist.get(position).getId();
                                bundle.putInt("id", newID);
//                                Toast.makeText(view.getContext(),"ID: "+newID, Toast.LENGTH_SHORT).show();
                                AnimePageFragment animePageFragment = new AnimePageFragment();
                                animePageFragment.setArguments(bundle);
                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, animePageFragment)
                                        .addToBackStack("tag") // onbackpressed works!
                                        .commit();
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(e.getLocalizedMessage(), "error");
            }
        });

        return view;
    }
}


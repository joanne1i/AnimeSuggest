package com.example.animesuggest;

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
import com.example.AnimeDataQuery;
import com.example.SeasonalQuery;
import com.example.SeasonalQuery;
import com.example.SeasonalpagetwoQuery;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;

public class PopularThisSeasonFragment extends Fragment {

    private static List<AnimeCard> mlist = new ArrayList<>();
    String title;
    String imgurl;
    int id;
    final int PAGEMAX = 50;
    AnimeCardAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popularseason, container, false);

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
                            .page(1)
                            .build()
            ).enqueue(new ApolloCall.Callback<SeasonalpagetwoQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<SeasonalpagetwoQuery.Data> response) {
                    for (int i = 0; i < PAGEMAX; i++) {
                        id = response.getData().Page().media().get(i).id();
                        bundle.putInt("id", id);
                        title = response.getData().Page().media().get(i).title().romaji();
                        imgurl = response.getData().Page().media().get(i).coverImage().extraLarge();
                        mlist.add(new AnimeCard(id, title, imgurl));
                    }
                    Log.d("list_inside", mlist.toString());

                    // have to move the portion of the background task that updates the UI onto the main thread
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            adapter = new AnimeCardAdapter(mlist);
                            recyclerView.setAdapter(adapter);
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


//    public void loadAnime() {
//        Bundle bundle = new Bundle();
//        Logger.addLogAdapter(new AndroidLogAdapter());
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        ApolloClient apolloClient = ApolloClient.builder()
//                .serverUrl("https://graphql.anilist.co")
//                .okHttpClient(okHttpClient)
//                .build();
//        apolloClient.query(
//                SeasonalpagetwoQuery.builder()
//                        .page(1)
//                        .build()
//        ).enqueue(new ApolloCall.Callback<SeasonalpagetwoQuery.Data>() {
//            @Override
//            public void onResponse(@NotNull Response<SeasonalpagetwoQuery.Data> response) {
//                int random = (int) (Math.random() * 50 + 1);
//                id = response.getData().Page().media().get(random).id();
//                bundle.putInt("id", id);
//                title = response.getData().Page().media().get(random).title().romaji();
//                imgurl = response.getData().Page().media().get(random).coverImage().extraLarge();
//                mlist.add(new AnimeCard(id, title, imgurl));
//                Logger.d(response.getData().Page().media().get(random).title());
//            }
//            @Override
//            public void onFailure(@NotNull ApolloException e) {
//                Log.d(e.getLocalizedMessage(), "error");
//            }
//        });
//
//        // RecyclerView tings
//        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview_id);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        AnimeCardAdapter adapter = new AnimeCardAdapter(mlist);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(adapter);
//    }


//    List<AnimeCard> animeCardList = new ArrayList<>();
//    AnimeCard animeCard = new AnimeCard();
//    RecyclerViewClickListener listener;
//    Button button;
//    TextView textView;
//    ImageView imageView;
//    String temp;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.cardview_anime, container, false);
//
//        textView = view.findViewById(R.id.cardview_txtdisplay);
//        imageView = view.findViewById(R.id.cardview_imgdisplay);
////        button = view.findViewById(R.id.button_test);
//
//        Bundle bundle = new Bundle();
////        bundle.clear();
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//                Logger.addLogAdapter(new AndroidLogAdapter());
//                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//                ApolloClient apolloClient = ApolloClient.builder()
//                        .serverUrl("https://graphql.anilist.co")
//                        .okHttpClient(okHttpClient)
//                        .build();
//                apolloClient.query(
//                        SeasonalpagetwoQuery.builder()
//                                .page(1)
//                                .build()
//                ).enqueue(new ApolloCall.Callback<SeasonalpagetwoQuery.Data>() {
//                    @Override
//                    public void onResponse(@NotNull Response<SeasonalpagetwoQuery.Data> response) {
//                        int id = response.getData().Page().media().get(47).id();
//                        bundle.putInt("id", id);
//                        getActivity().runOnUiThread(new Runnable() {
//
//                            // have to move the portion of the background task that updates the UI onto the main thread
//                            @Override
//                            public void run() {
//
//                                // Stuff that updates the UI
//                                textView.setText(response.getData().Page().media().get(34).title().romaji());
//                                Picasso.get()
//                                        .load(response.getData().Page().media().get(34).coverImage().extraLarge())
//                                        .into(imageView);
//                            }
//                        });
//
//
////                        AnimePageFragment animePageFragment = new AnimePageFragment();
////                        animePageFragment.setArguments(bundle);
////                        getParentFragmentManager()
////                                .beginTransaction()
////                                .replace(R.id.fragment_container, animePageFragment)
////                                .commit();
//                        Logger.d(response.getData().Page().media().get(3).title());
//                    }
//                    @Override
//                    public void onFailure(@NotNull ApolloException e) {
//                        Log.d(e.getLocalizedMessage(), "error");
//                    }
//                });
////                Log.d("123TEST", animeCard.getAllCards().toString());
//
////            }
////        });
//
//
//        return view;
//    }


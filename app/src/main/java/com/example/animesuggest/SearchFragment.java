package com.example.animesuggest;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.RandomAnimeDataQuery;
import com.example.SearchgenreQuery;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;


public class SearchFragment extends Fragment {

    private AnimeCardAdapter adapter;
    private static List<AnimeCard> mlist = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    int id;
    String title;
    String imgurl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_search, container, false);
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_search_id);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);

        searchView = (SearchView) view.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // gets the word that is entered
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(view.getContext(),"Word: "+query, Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                Logger.addLogAdapter(new AndroidLogAdapter());
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                ApolloClient apolloClient = ApolloClient.builder()
                        .serverUrl("https://graphql.anilist.co")
                        .okHttpClient(okHttpClient)
                        .build();
                apolloClient.query(
                        SearchgenreQuery.builder()
                                .genre(query)
                                .build()
                ).enqueue(new ApolloCall.Callback<SearchgenreQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<SearchgenreQuery.Data> response) {
                        for (int i = 0; i < 50; i++) {
                            id = response.getData().Page().media().get(i).id();
                            bundle.putInt("id", id);
                            title = response.getData().Page().media().get(i).title().romaji();
                            imgurl = response.getData().Page().media().get(i).coverImage().extraLarge();
                            mlist.add(new AnimeCard(id, title, imgurl));
                        }
                        Log.d("list_inside", bundle.get("id").toString());

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
                return true;
            }

            @Override
            // tracks the typing in searchbar
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    SearchView searchView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        searchView = (SearchView) getView().findViewById(R.id.searchBar);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            // gets the word that is entered
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            // tracks the typing in searchbar
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
    }
}

package com.example.animesuggest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.SeasonalpagetwoQuery;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
// made into random fragment
public class FavoritesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_favorites, container, false);
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        Bundle bundle = new Bundle();
        int random = (int) (Math.random() * 50 + 1);
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
                int id = response.getData().Page().media().get(random).id();
                bundle.putInt("id", id);
                AnimePageFragment animePageFragment = new AnimePageFragment();
                animePageFragment.setArguments(bundle);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, animePageFragment)
                        .commit();
//                Logger.d(response.getData().Page().media().get(random).title());
            }
            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(e.getLocalizedMessage(), "error");
            }
        });
//                Log.d("123TEST", animeCard.getAllCards().toString());
        // get a lot of ids, store in list
        return view;
    }
}


package com.example.animesuggest;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;


public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_search, container, false);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    SearchView searchView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Logger.addLogAdapter(new AndroidLogAdapter());

        searchView = (SearchView) getView().findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // gets the word that is entered
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            // tracks the typing in searchbar
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        RelativeLayout relativeLayout = (RelativeLayout)getView().findViewById(R.id.relative_layout);
//        TextView text = new TextView(getActivity().getApplicationContext());
        Log.d("123QUERY", searchView.getQuery() + "");

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("http://192.168.1.7:8080/graphql")
                .build();
//        press = (Button) getView().findViewById(R.id.btn_press);
//        press.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("123test", "buttonpressed");
//            }
//        });
//                apolloClient.query(
//                        AnimeDataQuery.builder()
//                                .id(15125)
//                                .build()
//                ).enqueue(new ApolloCall.Callback<AnimeDataQuery.Data>() {
//                    @Override
//                    public void onResponse(@NotNull Response<AnimeDataQuery.Data> response) {
//                        Log.i("TAG", response.toString());
//                        Logger.d(response.getData().toString());
//                    }
//                    @Override
//                    public void onFailure(@NotNull ApolloException e) {
//                        Log.e("TAG", e.getMessage(), e);
//                        Logger.d(e.getLocalizedMessage(), "error");
//                    }
//                });
    }
}

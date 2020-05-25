package com.example.animesuggest;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    SearchView searchView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        Log.d("123QUERY", searchView.getQuery()+"");


    }
}

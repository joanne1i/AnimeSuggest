package com.example.animesuggest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends Activity {
    SearchView mySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        handleIntent(getIntent());

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mySearchView = (SearchView)findViewById(R.id.searchBar);
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // gets the word that is entered
            public boolean onQueryTextSubmit(String query) {
                Log.d("123text", query);

                return false;
            }

            @Override
            // tracks the typing in searchbar
            public boolean onQueryTextChange(String newText) {
                Log.d("123textchange", newText);
                return false;
            }

            public String getQuery(String query) {
                return query;
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        Log.d("doMySearch", query);
    }
}

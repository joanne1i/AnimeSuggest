package com.example.animesuggest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.AnimeDataQuery;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import okhttp3.OkHttpClient;

public class HomeFragment extends Fragment {
    TextView myTextView;
    ImageView myImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // show the current/popular shows
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Logger.addLogAdapter(new AndroidLogAdapter());

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("https://graphql.anilist.co")
                .okHttpClient(okHttpClient)
                .build();
        Button button = (Button)view.findViewById(R.id.btn_press);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("123test", "plz");
                apolloClient.query(
                        AnimeDataQuery.builder()
                                .id(98977)
                                .build()
                ).enqueue(new ApolloCall.Callback<AnimeDataQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<AnimeDataQuery.Data> response) {
                        String data = "";
                        myTextView = (TextView)getView().findViewById(R.id.text_box);
                        myImageView = (ImageView)getView().findViewById(R.id.animeImage);
                        data = response.getData().Media().title().romaji() +'\n' + response.getData().Media().title().native_();
                        myTextView.setText(data);
                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        uiHandler.post(new Runnable(){
                            @Override
                            public void run() {
                                Picasso.get()
                                        .load(response.getData().Media().coverImage().large())
                                        .into(myImageView);
                            }
                        });
                        Logger.d(response.getData().toString());
                    }
                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Logger.d(e.getLocalizedMessage(), "error");
                    }
                });
            }
        });
        return view;
    }
}

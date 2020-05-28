package com.example.animesuggest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.AnimeDataQuery;
import com.example.SeasonalQuery;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import okhttp3.OkHttpClient;
//./gradlew generateApolloSources

public class AnimePageFragment extends Fragment {
    ImageView coverImage;
    TextView romanji;
    TextView japanese;
    Button testbutton;
    TextView genres;
    TextView description;
    TextView startDate;
    TextView endDate;
    TextView score;
    TextView rank;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animepage, container, false);

        Logger.addLogAdapter(new AndroidLogAdapter());
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        GridLayout gridLayout = new GridLayout(getContext());
        TextView tv = new TextView(getContext());
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("https://graphql.anilist.co")
                .okHttpClient(okHttpClient)
                .build();
        apolloClient.query(
                AnimeDataQuery.builder()
                        .id(112641)
                        .build()
        ).enqueue(new ApolloCall.Callback<AnimeDataQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<AnimeDataQuery.Data> response) {
//                testbutton = (Button)getView().findViewById(R.id.button_test);
//                testbutton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
                        String data = "";
                        float avgscore = 0;
                        romanji = (TextView) getView().findViewById(R.id.displayromanji);
                        japanese = (TextView) getView().findViewById(R.id.displaynative);
                        description = (TextView) getView().findViewById(R.id.displaydescription);
                        genres = (TextView) getView().findViewById(R.id.genredisplay);
                        startDate = (TextView) getView().findViewById(R.id.airdatedisplay);
                        endDate = (TextView) getView().findViewById(R.id.enddatedisplay);
                        rank = (TextView) getView().findViewById(R.id.rankdisplay);
                        score = (TextView) getView().findViewById(R.id.scoredisplay);
                        coverImage = (ImageView)getView().findViewById(R.id.displaypic);

                        data = response.getData().Media().title().romaji();
                        romanji.setText(data);

                        data = response.getData().Media().title().native_();
                        japanese.setText(data);

                        data = response.getData().Media().description().replace("<i>", "");
                        data = data.replace("</i>", "");
                        description.setText(data);

                        data = response.getData().Media().genres().toString().replace("[","");
                        data = data.replace("]", "");
                        genres.setText(data);

                        // dates
                        if((response.getData().Media().startDate().month() == null) && (response.getData().Media().startDate().year()) == null) {
                            startDate.setText("-");
                        }
                        else {
                            data = "" + response.getData().Media().startDate().month() + "/" + response.getData().Media().startDate().year();
                            startDate.setText(data);
                        }
                        if((response.getData().Media().endDate().month() == null) && (response.getData().Media().endDate().year()) == null) {
                            endDate.setText("-");
                        }
                        else {
                            data = "" + response.getData().Media().endDate().month() + "/" + response.getData().Media().endDate().year();
                            endDate.setText(data);
                        }

                        // rank
                        data = "#" + response.getData().Media().rankings().get(0).rank();
                        rank.setText(data);

                        // average score
                        avgscore = (response.getData().Media().averageScore());
                        score.setText(avgscore+"");

                        // cover image
                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get()
                                        .load(response.getData().Media().coverImage().extraLarge())
                                        .into(coverImage);
                            }
                        });
                        Logger.d(response.getData().toString());

//                    }
//                });

//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(tv.getParent() != null) {
//                                        ((ViewGroup)tv.getParent()).removeView(tv); // <- fix
//                                    }
//                                    layout.addView(tv);
//                                }
//                            });


            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Logger.d(e.getLocalizedMessage(), "error");
            }
        });
        return view;

    }
}

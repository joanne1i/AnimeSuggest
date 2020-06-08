package com.example.animesuggest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.AnimeDataQuery;
import com.example.GetRecsQuery;
import com.example.GetUserRecsQuery;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
//./gradlew generateApolloSources

public class AnimePageFragment extends Fragment {
    ImageView coverImage;
    TextView romanji;
    TextView japanese;
    TextView genres;
    TextView description;
    TextView startDate;
    TextView endDate;
    TextView score;
    TextView rank;
    int id;

    // for recycler view recommendations
    private AnimeCardAdapter adapter;
    private static List<AnimeCard> mlist = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    String title;
    String imgurl;
    int rec_id;

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animepage, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_animefrag_id);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("id");
        }
        Logger.addLogAdapter(new AndroidLogAdapter());
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("https://graphql.anilist.co")
                .okHttpClient(okHttpClient)
                .build();

        // for the anime information
        apolloClient.query(
                AnimeDataQuery.builder()
                        .id(id)
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
                coverImage = (ImageView) getView().findViewById(R.id.displaypic);

                // romanji title
                data = response.getData().Media().title().romaji();
                romanji.setText(data);

                // japanese title
                data = response.getData().Media().title().native_();
                japanese.setText(data);

                // description
                if (response.getData().Media().description() != null) {
                    data = response.getData().Media().description().replace("<i>", "");
                    data = data.replace("</i>", "");
                    data = data.replace("<br>", "");
                    description.setText(data);
                } else {
                    description.setText("N/A");
                }

                // genres
                data = response.getData().Media().genres().toString().replace("[", "");
                data = data.replace("]", "");
                genres.setText(data);

                // dates
                if ((response.getData().Media().startDate().month() == null) && (response.getData().Media().startDate().year()) == null) {
                    startDate.setText("-");
                } else {
                    data = "" + response.getData().Media().startDate().month() + "/" + response.getData().Media().startDate().year();
                    startDate.setText(data);
                }
                if ((response.getData().Media().endDate().month() == null) && (response.getData().Media().endDate().year()) == null) {
                    endDate.setText("-");
                } else {
                    data = "" + response.getData().Media().endDate().month() + "/" + response.getData().Media().endDate().year();
                    endDate.setText(data);
                }

                // rank
                if ((response.getData().Media().rankings().isEmpty())) {
                    rank.setText("N/A");
                } else {
                    data = "#" + response.getData().Media().rankings().get(0).rank();
                    rank.setText(data);
                }

                // average score
                if (response.getData().Media().averageScore() != null) {
                    avgscore = (response.getData().Media().averageScore());
                    score.setText(avgscore + "");
                } else {
                    score.setText("N/A");
                }

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
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Logger.d(e.getLocalizedMessage(), "error");
            }
        });

        // for recycler view
        apolloClient.query(
                GetUserRecsQuery.builder()
                        .id(id)
                        .build()
        ).enqueue(new ApolloCall.Callback<GetUserRecsQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserRecsQuery.Data> response) {
                mlist.clear();
                int total = response.getData().Media().recommendations().pageInfo().total();
                if(total > 50) {
                    total = 50;
                }
                for (int i = 0; i < total; i++) {
                    rec_id = response.getData().Media().recommendations().edges().get(i).node().mediaRecommendation().id();
                    bundle.putInt("id", rec_id);
                    title = response.getData().Media().recommendations().edges().get(i).node().mediaRecommendation().title().romaji();
                    imgurl = response.getData().Media().recommendations().edges().get(i).node().mediaRecommendation().coverImage().extraLarge();
                    mlist.add(new AnimeCard(rec_id, title, imgurl));
                }

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
                Logger.d(e.getLocalizedMessage(), "error");
            }
        });
//        apolloClient.query(
//                GetRecsQuery.builder()
//                        .mediaRecommendationId(id)
//                        .build()
//        ).enqueue(new ApolloCall.Callback<GetRecsQuery.Data>() {
//            @Override
//            public void onResponse(@NotNull Response<GetRecsQuery.Data> response) {
//                mlist.clear();
//                int total = response.getData().Page().pageInfo().total();
//                if(total > 50) {
//                    total = total - 50;
//                }
//                for (int i = 0; i < total; i++) {
//                    rec_id = response.getData().Page().recommendations().get(i).media().id();
//                    bundle.putInt("id", rec_id);
//                    title = response.getData().Page().recommendations().get(i).media().title().romaji();
//                    imgurl = response.getData().Page().recommendations().get(i).media().coverImage().extraLarge();
//                    mlist.add(new AnimeCard(rec_id, title, imgurl));
//                }
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Stuff that updates the UI
//                        adapter = new AnimeCardAdapter(mlist, getActivity(), new AnimeCardAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClicked(int position, Object object) {
//                                Bundle bundle = new Bundle();
//                                int newID = mlist.get(position).getId();
//                                bundle.putInt("id", newID);
//                                AnimePageFragment animePageFragment = new AnimePageFragment();
//                                animePageFragment.setArguments(bundle);
//                                getParentFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.fragment_container, animePageFragment)
//                                        .addToBackStack("tag") // onbackpressed works!
//                                        .commit();
//                            }
//                        });
//                        recyclerView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                });
//
//            }
//
//            @Override
//            public void onFailure(@NotNull ApolloException e) {
//                Logger.d(e.getLocalizedMessage(), "error");
//            }
//        });
        return view;

    }
}

package com.example.animesuggest;

import android.media.Image;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.SeasonalQuery;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import okhttp3.OkHttpClient;

public class HomeFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // show the current/popular shows
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((ImageView) view.findViewById(R.id.kaguya_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.tog_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.gleipnir_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.rezero_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.comedylove_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.alicization_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.aot_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.sao_imgview)).setOnClickListener(this::onClick);

        ((ImageView) view.findViewById(R.id.deathnote_imgview)).setOnClickListener(this::onClick);

        ((TextView) view.findViewById(R.id.popular_viewALL)).setOnClickListener(this::onClick2);
//
//        ((TextView) view.findViewById(R.id.upcoming_viewALL)).setOnClickListener(this::onClick2);
//
//        ((TextView) view.findViewById(R.id.alltime_viewALL)).setOnClickListener(this::onClick2);

        return view;
    }

    // on click listener for home fragment anime cards
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch(view.getId()) {
            case R.id.kaguya_imgview:
                bundle.putInt("id", 112641);
                break;
            case R.id.tog_imgview:
                bundle.putInt("id",115230);
                break;
            case R.id.gleipnir_imgview:
                bundle.putInt("id", 108241);
                break;
            case R.id.rezero_imgview:
                bundle.putInt("id", 108632);
                break;
            case R.id.comedylove_imgview:
                bundle.putInt("id",108489);
                break;
            case R.id.alicization_imgview:
                bundle.putInt("id", 114308);
                break;
            case R.id.aot_imgview:
                bundle.putInt("id", 16498);
                break;
            case R.id.sao_imgview:
                bundle.putInt("id",11757);
                break;
            case R.id.deathnote_imgview:
                bundle.putInt("id", 1535);
                break;
        }
        AnimePageFragment animePageFragment = new AnimePageFragment();
        animePageFragment.setArguments(bundle);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, animePageFragment)
                .commit();
    }

    // onclick listener for popular this season page
    public void onClick2(View view) {
        Bundle bundle = new Bundle();
        switch(view.getId()) {
            case R.id.popular_viewALL:
                    Log.d("123MESS", "popularviewall");
                    break;
//                    case R.id.upcoming_viewALL:
//                    Log.d("123MESS", "upcomingviewall");
//                    break;
//                    case R.id.alltime_viewALL:
//                    Log.d("123MESS", "alltimeviewall");
//                    break;
        }
        PopularThisSeasonFragment popularPageFragment = new PopularThisSeasonFragment();
        popularPageFragment.setArguments(bundle);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, popularPageFragment)
                .commit();
    }

}
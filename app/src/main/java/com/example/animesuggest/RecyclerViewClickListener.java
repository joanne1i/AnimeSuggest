package com.example.animesuggest;

import android.view.View;

public interface RecyclerViewClickListener {
    void onRowClicked(int position);
    void onViewClicked(View v, int position);
}

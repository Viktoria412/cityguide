package com.example.cityguide.ui.attractions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cityguide.R;

public class AttractionListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(getContext());
        textView.setText("Список достопримечательностей");
        textView.setTextSize(24);
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }
}

package com.example.cityguide.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cityguide.R;
import com.example.cityguide.ui.attractions.AttractionListFragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnViewAttractions = root.findViewById(R.id.btnViewAttractions);
        if (btnViewAttractions != null) {
            btnViewAttractions.setOnClickListener(v ->
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new AttractionListFragment())
                            .addToBackStack(null)
                            .commit()
            );
        }

        return root;
    }
}

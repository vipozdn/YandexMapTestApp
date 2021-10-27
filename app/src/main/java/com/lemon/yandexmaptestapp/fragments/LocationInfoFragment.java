package com.lemon.yandexmaptestapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.lemon.yandexmaptestapp.R;

public class LocationInfoFragment extends Fragment {
    public LocationInfoFragment() {
        super(R.layout.fragment_location_info);
    }

    private TextView resultTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_info, container, false);
        resultTextView = view.findViewById(R.id.result_textview);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getParentFragmentManager().setFragmentResultListener("requestKey",
                this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String[] resultArray = bundle.getStringArray("bundleKey");

                if (resultArray[0].equals("")) {
                    resultArray[0] = getResources().getString(R.string.need_internet);
                }

                String output = getResources().getString(R.string.location_result, resultArray[0],
                        resultArray[1], resultArray[2]);

                resultTextView.setText(output);
            }
        });

    }


}

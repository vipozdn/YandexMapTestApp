package com.lemon.yandexmaptestapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lemon.yandexmaptestapp.R;

public class ClickFragment extends Fragment {
    public ClickFragment() {
        super(R.layout.fragment_click);
    }

    private FragmentViewListener fragmentViewListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentViewListener) {
            fragmentViewListener = (FragmentViewListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentViewListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_click, container, false);
        Button clickButton = (Button) view.findViewById(R.id.fragment_click_button);

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentViewListener.onFragmentSomeViewClicked(clickButton.getId());
            }
        });

        return view;
    }
}

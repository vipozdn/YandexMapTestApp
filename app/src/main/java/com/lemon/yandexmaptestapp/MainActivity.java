package com.lemon.yandexmaptestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.lemon.yandexmaptestapp.fragments.ClickFragment;
import com.lemon.yandexmaptestapp.fragments.FragmentViewListener;
import com.lemon.yandexmaptestapp.fragments.LocationInfoFragment;
import com.lemon.yandexmaptestapp.fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements FragmentViewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ClickFragment.class, null)
                    .commit();
        }
    }

    @Override
    public void onFragmentSomeViewClicked(int viewId) {
        if (viewId == R.id.fragment_click_button) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack("map")
                    .replace(R.id.fragment_container_view, MapFragment.class, null)
                    .commit();
        } else if (viewId == R.id.fragment_map_button) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.fragment_container_view, LocationInfoFragment.class, null)
                    .commit();
        }
    }

    @Override
    public void passFragmentResult(String result) {
        Bundle resultBundle = new Bundle();
        resultBundle.putString("bundleKey", result);
        getSupportFragmentManager().setFragmentResult("requestKey", resultBundle);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
package com.lemon.yandexmaptestapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.lemon.yandexmaptestapp.fragments.ClickFragment;
import com.lemon.yandexmaptestapp.fragments.FragmentViewListener;
import com.lemon.yandexmaptestapp.fragments.LocationInfoFragment;
import com.lemon.yandexmaptestapp.fragments.MapFragment;
import com.yandex.mapkit.MapKitFactory;

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
            MapKitFactory.setApiKey(Constants.YANDEX_MAPS_API_KEY);
            MapKitFactory.initialize(this);
        }
    }

    @Override
    public void onFragmentSomeViewClicked(int viewId) {
        if (viewId == R.id.fragment_click_button) {

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                openMapFragment();
            }

        } else if (viewId == R.id.fragment_map_button) {
            openLocationInfoFragment();
        }
    }


    @Override
    public void passFragmentResult(String[] result) {
        Bundle resultBundle = new Bundle();
        resultBundle.putStringArray("bundleKey", result);
        getSupportFragmentManager().setFragmentResult("requestKey", resultBundle);
    }

    private void openMapFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack("map")
                .replace(R.id.fragment_container_view, MapFragment.class, null)
                .commit();
    }

    private void openLocationInfoFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, LocationInfoFragment.class, null)
                .commit();
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

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, getResources().getString(R.string.location_permission_granted),
                            Toast.LENGTH_SHORT).show();
                    openMapFragment();
                } else {
                    Toast.makeText(this,
                            getResources().getString(R.string.location_permission_denied),
                            Toast.LENGTH_SHORT).show();
                }
            });
}
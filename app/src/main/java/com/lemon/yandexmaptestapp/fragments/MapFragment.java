package com.lemon.yandexmaptestapp.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.lemon.yandexmaptestapp.R;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {
    public MapFragment() {
        super(R.layout.fragment_map);
    }

    private FragmentViewListener fragmentViewListener;
    private MapView mapView;
    private TextView mapTextView;
    private Context context;
    private Point currentPoint = null;

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
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Button mapButton = (Button) view.findViewById(R.id.fragment_map_button);
        mapTextView = (TextView) view.findViewById(R.id.map_text_view);

        context = getActivity();
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPoint == null) {
                    Toast.makeText(context, getResources().getString(R.string.click_to_set_marker),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String locationAddress = getLocationAddress();
                fragmentViewListener.onFragmentSomeViewClicked(mapButton.getId());
                fragmentViewListener.passFragmentResult(new String[]{locationAddress,
                        String.valueOf(currentPoint.getLatitude()), String.valueOf(currentPoint.getLongitude())});
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();

        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.createLocationManager().requestSingleUpdate(new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                mapView.getMap().move(
                        new CameraPosition(location.getPosition(), 14.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null);

            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });

        mapView.getMap().addInputListener(new InputListener() {
            @Override
            public void onMapTap(@NonNull Map map, @NonNull Point point) {
                map.getMapObjects().clear();
                map.getMapObjects().addPlacemark(point, ImageProvider.fromBitmap(getMarkerBitmap()));
                currentPoint = point;

                String points = point.getLatitude() + ", " + point.getLongitude();
                mapTextView.setText(points);
            }

            @Override
            public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

            }
        });
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }


    private Bitmap getMarkerBitmap() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_location, null);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private String getLocationAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String result = "";
        try {
            addresses = geocoder.getFromLocation(currentPoint.getLatitude(), currentPoint.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            result += (address + " " + city + " " + state + " " + country);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

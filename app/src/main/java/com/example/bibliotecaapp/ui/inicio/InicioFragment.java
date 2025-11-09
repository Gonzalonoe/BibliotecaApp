package com.example.bibliotecaapp.ui.inicio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bibliotecaapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InicioFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private InicioViewModel vm;
    private FloatingActionButton fabAbrirMaps;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        // Mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fabAbrirMaps = root.findViewById(R.id.fabAbrirMaps);
        fabAbrirMaps.setOnClickListener(v -> abrirEnGoogleMaps());

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        vm.getUbicacionInicial().observe(getViewLifecycleOwner(), latLng -> {
            if (mMap != null) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("📚 Biblioteca Central")
                        .snippet("    Biblioteca del Congreso de Estados Unidos")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                try {
                    boolean success = mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    requireContext(), R.raw.map_style_biblioteca));
                    if (!success) Log.e("MAPA", "No se aplicó el estilo.");
                } catch (Exception e) {
                    Log.e("MAPA", "Error cargando estilo.", e);
                }
            }
        });
    }

    private void abrirEnGoogleMaps() {
        String geoUri = "geo:38.8887,-77.0047?q=Library+of+Congress,+Washington+D.C.";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

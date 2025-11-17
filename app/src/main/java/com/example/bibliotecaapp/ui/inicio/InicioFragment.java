package com.example.bibliotecaapp.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.bibliotecaapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class InicioFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private InicioViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        root.findViewById(R.id.fabAbrirMaps).setOnClickListener(v ->
                vm.onAbrirGoogleMapsClick()
        );

        vm.getIntentAbrirMaps().observe(getViewLifecycleOwner(), intent -> {
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        vm.getAccionConfigMapa().observe(getViewLifecycleOwner(), accion -> {
            accion.configurar(mMap, requireContext());
        });

        vm.inicializarMapa();
    }
}

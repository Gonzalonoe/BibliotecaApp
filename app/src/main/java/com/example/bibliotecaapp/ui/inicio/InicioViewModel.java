package com.example.bibliotecaapp.ui.inicio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bibliotecaapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioViewModel extends ViewModel {

    public interface AccionMapa {
        void configurar(GoogleMap map, Context ctx);
    }

    private final MutableLiveData<AccionMapa> accionConfigMapa = new MutableLiveData<>();

    public LiveData<AccionMapa> getAccionConfigMapa() {
        return accionConfigMapa;
    }

    private final MutableLiveData<Intent> intentAbrirMaps = new MutableLiveData<>();

    public LiveData<Intent> getIntentAbrirMaps() {
        return intentAbrirMaps;
    }

    private final LatLng posicionBiblioteca =
            new LatLng(38.88874242852299, -77.00474046182204);

    public void inicializarMapa() {
        accionConfigMapa.setValue((map, ctx) -> {
            map.clear();

            map.addMarker(new MarkerOptions()
                    .position(posicionBiblioteca)
                    .title("📚 Biblioteca Central")
                    .snippet("Biblioteca del Congreso de Estados Unidos")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            );

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionBiblioteca, 15f));

            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    ctx, R.raw.map_style_biblioteca
            ));
        });
    }

    public void onAbrirGoogleMapsClick() {
        String geoUri = "geo:38.8887,-77.0047?q=Library+of+Congress,+Washington+D.C.";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        intent.setPackage("com.google.android.apps.maps");

        intentAbrirMaps.setValue(intent);
    }
}

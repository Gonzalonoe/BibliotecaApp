package com.example.bibliotecaapp.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<LatLng> ubicacionInicial = new MutableLiveData<>();

    public InicioViewModel() {
        ubicacionInicial.setValue(new LatLng(38.88874242852299, -77.00474046182204));
    }

    public LiveData<LatLng> getUbicacionInicial() {
        return ubicacionInicial;
    }
}

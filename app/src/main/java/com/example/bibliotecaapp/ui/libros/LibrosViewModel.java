package com.example.bibliotecaapp.ui.libros;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Libro;
import com.example.bibliotecaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibrosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Libro>> libros;

    public LibrosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Libro>> getLibros() {
        if (libros == null) {
            libros = new MutableLiveData<>();
        }
        return libros;
    }

    public void cargarLibros() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<List<Libro>> call = api.obtenerLibros("Bearer " + token);

        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if (response.isSuccessful()) {
                    libros.postValue(response.body());
                } else {
                    Log.e("Libros", "Error al cargar libros");
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Log.e("Libros", "Error de conexión: " + t.getMessage());
            }
        });
    }
}

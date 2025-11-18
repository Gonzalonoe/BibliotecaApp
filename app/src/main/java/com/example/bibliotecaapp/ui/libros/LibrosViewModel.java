package com.example.bibliotecaapp.ui.libros;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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

    private MutableLiveData<List<Libro>> libros = new MutableLiveData<>();
    private MutableLiveData<Boolean> esAdminLive = new MutableLiveData<>();

    public LibrosViewModel(@NonNull Application application) {
        super(application);
        verificarRol();
    }

    public LiveData<List<Libro>> getLibros() {
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

    public LiveData<Boolean> getEsAdmin() {
        return esAdminLive;
    }

    private void verificarRol() {
        SharedPreferences sp =
                getApplication().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String rol = sp.getString("rol", "").trim();

        boolean esAdmin = rol.equalsIgnoreCase("Admin");

        Log.d("ROL_DEBUG", "ROL EN SharedPreferences = " + rol);

        esAdminLive.setValue(esAdmin);
    }
}


package com.example.bibliotecaapp.ui.reportes;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Reporte;
import com.example.bibliotecaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaReportesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Reporte>> reportesLive = new MutableLiveData<>();

    public ListaReportesViewModel(@NonNull Application application) {
        super(application);
        cargarTodosLosReportes();
    }

    public LiveData<List<Reporte>> getReportes() {
        return reportesLive;
    }

    private void cargarTodosLosReportes() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<List<Reporte>> call = api.getReportes("Bearer " + token);
        call.enqueue(new Callback<List<Reporte>>() {
            @Override
            public void onResponse(Call<List<Reporte>> call, Response<List<Reporte>> response) {
                if (response.isSuccessful()) {
                    reportesLive.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al cargar reportes (admin)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reporte>> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

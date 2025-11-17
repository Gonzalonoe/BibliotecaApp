package com.example.bibliotecaapp.ui.reportes;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Reporte;
import com.example.bibliotecaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleReporteViewModel extends AndroidViewModel {

    private MutableLiveData<Reporte> reporteLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensaje = new MutableLiveData<>();

    public DetalleReporteViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Reporte> getReporte() {
        return reporteLiveData;
    }

    public MutableLiveData<String> getMensaje() {
        return mensaje;
    }

    public void cargarReporte(int id) {
        String token = ApiClient.leerToken(getApplication());

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        Call<Reporte> call = api.obtenerReportePorId("Bearer " + token, id);

        call.enqueue(new Callback<Reporte>() {
            @Override
            public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                if (response.isSuccessful()) {
                    reporteLiveData.postValue(response.body());
                } else {
                    mensaje.postValue("❗ No se pudo cargar el reporte (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Reporte> call, Throwable t) {
                mensaje.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void aceptarReporte(int id) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        api.aceptarReporte("Bearer " + token, id)
                .enqueue(new Callback<Reporte>() {
                    @Override
                    public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                        if (response.isSuccessful()) {
                            reporteLiveData.postValue(response.body());
                            mensaje.postValue("Reporte ACEPTADO ✔");
                        } else {
                            mensaje.postValue("Error al aceptar (" + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<Reporte> call, Throwable t) {
                        mensaje.postValue("Error: " + t.getMessage());
                    }
                });
    }

    public void cancelarReporte(int id) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        api.cancelarReporte("Bearer " + token, id)
                .enqueue(new Callback<Reporte>() {
                    @Override
                    public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                        if (response.isSuccessful()) {
                            reporteLiveData.postValue(response.body());
                            mensaje.postValue("Reporte CANCELADO ❌");
                        } else {
                            mensaje.postValue("Error al cancelar (" + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<Reporte> call, Throwable t) {
                        mensaje.postValue("Error: " + t.getMessage());
                    }
                });
    }
}

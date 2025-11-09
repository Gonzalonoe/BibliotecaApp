package com.example.bibliotecaapp.ui.reportes;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Reporte;
import com.example.bibliotecaapp.request.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Reporte>> reportesLive = new MutableLiveData<>();

    public ReportesViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Reporte>> getReportes() {
        return reportesLive;
    }

    public void cargarReportes() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<List<Reporte>> call = api.getReportes("Bearer " + token);
        call.enqueue(new Callback<List<Reporte>>() {
            @Override
            public void onResponse(Call<List<Reporte>> call, Response<List<Reporte>> response) {
                if (response.isSuccessful()) {
                    reportesLive.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al cargar reportes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reporte>> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void enviarReporte(String titulo, String sinopsis, Uri imagenUri) {
        try {
            String token = ApiClient.leerToken(getApplication());
            ApiClient.InmoServicio api = ApiClient.getInmoServicio();

            RequestBody tituloBody = RequestBody.create(MediaType.parse("text/plain"), titulo);
            RequestBody sinopsisBody = RequestBody.create(MediaType.parse("text/plain"), sinopsis);

            MultipartBody.Part imagenPart = null;
            if (imagenUri != null) {
                File file = uriToFile(imagenUri);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                imagenPart = MultipartBody.Part.createFormData("ImagenPortada", file.getName(), requestFile);
            }

            Call<Reporte> call = api.crearReporte("Bearer " + token, tituloBody, sinopsisBody, imagenPart);

            call.enqueue(new Callback<Reporte>() {
                @Override
                public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Reporte enviado con éxito", Toast.LENGTH_SHORT).show();
                        cargarReportes();
                    } else {
                        Toast.makeText(getApplication(), "Error al enviar reporte: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e("API_REPORTE", "Error HTTP: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Reporte> call, Throwable t) {
                    Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_REPORTE", "Fallo conexión", t);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplication(), "Error al preparar reporte: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private File uriToFile(Uri uri) throws Exception {
        InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("portada_", ".jpg", getApplication().getCacheDir());
        OutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
        return tempFile;
    }
}

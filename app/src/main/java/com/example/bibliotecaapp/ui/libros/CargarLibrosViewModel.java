package com.example.bibliotecaapp.ui.libros;

import android.app.Application;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Libro;
import com.example.bibliotecaapp.request.ApiClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CargarLibrosViewModel extends AndroidViewModel {

    private final ApiClient.InmoServicio api;

    private final MutableLiveData<Integer> fabVisibility = new MutableLiveData<>();

    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    public CargarLibrosViewModel(@NonNull Application application) {
        super(application);
        api = ApiClient.getInmoServicio();
    }


    public void onPantallaCargarLibrosMostrada() {
        fabVisibility.setValue(View.GONE);
    }

    public void onPantallaCargarLibrosCerrada() {
        fabVisibility.setValue(View.VISIBLE);
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public void onPortadaSeleccionada(Uri uri) {
        mensaje.setValue("📷 Portada seleccionada correctamente");
    }

    public void onGuardarClick(String titulo, String autor, String anio, String stock, String descripcion, File portadaFile) {
        if (titulo.isEmpty() || autor.isEmpty() || anio.isEmpty() || stock.isEmpty() || descripcion.isEmpty() || portadaFile == null) {
            mensaje.setValue("⚠️ Complete todos los campos y seleccione una portada");
            return;
        }

        guardarLibro(titulo, autor, anio, stock, descripcion, portadaFile);
    }

    private void guardarLibro(String titulo, String autor, String anio, String stock, String descripcion, File portadaFile) {
        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("Token no encontrado, inicie sesión nuevamente");
            return;
        }

        RequestBody tituloBody = RequestBody.create(MediaType.parse("text/plain"), titulo);
        RequestBody autorBody = RequestBody.create(MediaType.parse("text/plain"), autor);
        RequestBody anioBody = RequestBody.create(MediaType.parse("text/plain"), anio);
        RequestBody stockBody = RequestBody.create(MediaType.parse("text/plain"), stock);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), descripcion);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), portadaFile);
        MultipartBody.Part portadaPart = MultipartBody.Part.createFormData("portada", portadaFile.getName(), requestFile);

        Call<Libro> call = api.crearLibro(
                "Bearer " + token,
                tituloBody,
                autorBody,
                anioBody,
                stockBody,
                descBody,
                portadaPart
        );

        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()) {
                    mensaje.setValue("✅ Libro agregado correctamente");
                } else {
                    mensaje.setValue("Error al crear libro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensaje.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}



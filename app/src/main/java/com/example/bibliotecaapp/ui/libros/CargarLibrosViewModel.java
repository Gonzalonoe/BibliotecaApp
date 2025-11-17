package com.example.bibliotecaapp.ui.libros;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Libro;
import com.example.bibliotecaapp.request.ApiClient;
import com.example.bibliotecaapp.utils.PathUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CargarLibrosViewModel extends AndroidViewModel {

    private final ApiClient.InmoServicio api;

    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    public LiveData<String> getMensaje() { return mensaje; }

    private final MutableLiveData<Uri> portadaPreviewUri = new MutableLiveData<>();
    public LiveData<Uri> getPortadaPreviewUri() { return portadaPreviewUri; }

    private final MutableLiveData<Boolean> limpiarCampos = new MutableLiveData<>();
    public LiveData<Boolean> getLimpiarCampos() { return limpiarCampos; }

    private File portadaFile;

    public CargarLibrosViewModel(@NonNull Application application) {
        super(application);
        api = ApiClient.getInmoServicio();
    }


    public void onPortadaSeleccionada(Uri uri) {

        if (uri == null) {
            mensaje.setValue("⚠ No se seleccionó ninguna imagen");
            return;
        }

        portadaPreviewUri.setValue(uri);

        String path = PathUtil.getPath(getApplication(), uri);
        if (path != null) {
            portadaFile = new File(path);
            mensaje.setValue("📷 Portada seleccionada");
        } else {
            mensaje.setValue("⚠ Error al obtener la ruta de la imagen");
        }
    }


    public void onGuardarClick(String titulo, String autor, String anio, String stock, String descripcion) {

        if (titulo.isEmpty() || autor.isEmpty() || anio.isEmpty() ||
                stock.isEmpty() || descripcion.isEmpty() || portadaFile == null) {

            mensaje.setValue("⚠ Complete todos los campos y seleccione imagen");
            return;
        }

        guardarLibro(titulo, autor, anio, stock, descripcion);
    }

    private void guardarLibro(String titulo, String autor, String anio, String stock, String descripcion) {

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("⚠ Token no encontrado. Inicie sesión nuevamente.");
            return;
        }

        RequestBody tituloBody = RequestBody.create(MediaType.parse("text/plain"), titulo);
        RequestBody autorBody = RequestBody.create(MediaType.parse("text/plain"), autor);
        RequestBody anioBody = RequestBody.create(MediaType.parse("text/plain"), anio);
        RequestBody stockBody = RequestBody.create(MediaType.parse("text/plain"), stock);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), descripcion);

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), portadaFile);
        MultipartBody.Part portadaPart =
                MultipartBody.Part.createFormData("portada", portadaFile.getName(), fileBody);

        api.crearLibro(
                "Bearer " + token,
                tituloBody,
                autorBody,
                anioBody,
                stockBody,
                descBody,
                portadaPart
        ).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()) {
                    mensaje.setValue("📚 Libro agregado correctamente");
                    limpiarCampos.setValue(true);
                } else {
                    mensaje.setValue("⚠ Error al crear libro (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensaje.setValue("🚫 Error de conexión: " + t.getMessage());
            }
        });
    }
}

package com.example.bibliotecaapp.ui.libros;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Libro;
import com.example.bibliotecaapp.models.LibroUpdateRequest;
import com.example.bibliotecaapp.models.Pedido;
import com.example.bibliotecaapp.models.PedidoRequest;
import com.example.bibliotecaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleLibroViewModel extends AndroidViewModel {

    private final MutableLiveData<Libro> libroSeleccionado = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    private final MutableLiveData<Boolean> modoEdicion = new MutableLiveData<>(false);
    private final MutableLiveData<String> textoBotonEditar = new MutableLiveData<>("✏️ Editar");

    private final ApiClient.InmoServicio api;

    public DetalleLibroViewModel(@NonNull Application application) {
        super(application);
        api = ApiClient.getInmoServicio();
    }

    public void setLibro(Libro libro) {
        libroSeleccionado.setValue(libro);
    }

    public LiveData<Libro> getLibro() {
        return libroSeleccionado;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<Boolean> getModoEdicion() {
        return modoEdicion;
    }

    public LiveData<String> getTextoBotonEditar() {
        return textoBotonEditar;
    }

    public void alternarModoEdicion() {
        Boolean editando = modoEdicion.getValue();
        if (editando == null) editando = false;

        modoEdicion.setValue(!editando);
        textoBotonEditar.setValue(!editando ? "💾 Guardar" : "✏️ Editar");
    }

    public void guardarCambios(String titulo, String autor, String anio, String stock, String descripcion) {
        Libro libro = libroSeleccionado.getValue();

        if (libro == null) {
            mensaje.setValue("Error: no se encontró el libro para editar.");
            return;
        }

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("⚠️ Sesión expirada. Inicie sesión nuevamente.");
            return;
        }

        LibroUpdateRequest req = new LibroUpdateRequest(
                titulo,
                autor,
                Integer.parseInt(anio),
                Integer.parseInt(stock),
                descripcion
        );

        Call<Libro> call = api.actualizarLibro("Bearer " + token, libro.getId(), req);

        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()) {
                    Libro actualizado = response.body();
                    libroSeleccionado.setValue(actualizado);
                    mensaje.setValue("✅ Libro actualizado correctamente.");
                    Toast.makeText(getApplication(), "✅ Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    mensaje.setValue("⚠️ Error al actualizar: código " + response.code());
                    Toast.makeText(getApplication(), "Error al actualizar (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensaje.setValue("🚫 Error de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        modoEdicion.setValue(false);
        textoBotonEditar.setValue("✏️ Editar");
    }

    public void eliminarLibro() {
        Libro libro = libroSeleccionado.getValue();
        if (libro == null) {
            mensaje.setValue("⚠️ No se encontró el libro para eliminar.");
            return;
        }

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            Toast.makeText(getApplication(), "Token no encontrado. Inicie sesión nuevamente", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = api.eliminarLibro("Bearer " + token, libro.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mensaje.setValue("🗑️ Libro eliminado correctamente");
                    Toast.makeText(getApplication(), "🗑️ Libro eliminado correctamente", Toast.LENGTH_LONG).show();
                } else {
                    mensaje.setValue("⚠️ Error al eliminar: código " + response.code());
                    Toast.makeText(getApplication(), "Error al eliminar (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mensaje.setValue("🚫 Error de conexión al eliminar: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reservarLibro() {
        Libro libro = libroSeleccionado.getValue();
        if (libro == null) {
            mensaje.setValue("No se encontró el libro para reservar");
            return;
        }

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("⚠️ Sesión expirada. Inicie sesión nuevamente.");
            return;
        }

        PedidoRequest request = new PedidoRequest(libro.getId(), libro.getTitulo());

        Call<Pedido> call = api.crearPedido("Bearer " + token, request);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()) {
                    mensaje.setValue("📘 Pedido creado con éxito.");
                    Toast.makeText(getApplication(), "📘 Pedido creado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    mensaje.setValue("⚠️ Error al crear el pedido (" + response.code() + ")");
                    Toast.makeText(getApplication(), "Error al crear el pedido (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                mensaje.setValue("🚫 Error de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


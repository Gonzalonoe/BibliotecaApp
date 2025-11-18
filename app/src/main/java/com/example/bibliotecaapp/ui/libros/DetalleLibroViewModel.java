package com.example.bibliotecaapp.ui.libros;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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

    public MutableLiveData<Libro> libro = new MutableLiveData<>();
    public MutableLiveData<String> toast = new MutableLiveData<>();

    public MutableLiveData<Integer> visibleEditar = new MutableLiveData<>(android.view.View.GONE);
    public MutableLiveData<Integer> visibleEliminar = new MutableLiveData<>(android.view.View.GONE);

    public MutableLiveData<Boolean> editable = new MutableLiveData<>(false);
    public MutableLiveData<String> textoBotonEditar = new MutableLiveData<>("✏️ Editar");

    public MutableLiveData<Void> mostrarDialogoEliminar = new MutableLiveData<>();
    public MutableLiveData<Void> volverAtras = new MutableLiveData<>();

    private final ApiClient.InmoServicio api;

    public DetalleLibroViewModel(@NonNull Application app) {
        super(app);
        api = ApiClient.getInmoServicio();
        verificarRol();
    }

    public void cargarLibroDesdeArgs(Bundle args) {
        if (args == null) return;
        Libro l = (Libro) args.getSerializable("libro");
        libro.setValue(l);
    }

    private void verificarRol() {
        SharedPreferences sp = getApplication()
                .getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String rol = sp.getString("rol", "0");
        boolean admin = rol.equalsIgnoreCase("admin") || rol.equals("1");

        visibleEditar.setValue(admin ? android.view.View.VISIBLE : android.view.View.GONE);
        visibleEliminar.setValue(admin ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    public void onEditarClick(String t, String a, String anio, String stock, String desc) {

        boolean modo = editable.getValue();

        if (!modo) {
            editable.setValue(true);
            textoBotonEditar.setValue("💾 Guardar");
            return;
        }

        Libro l = libro.getValue();

        LibroUpdateRequest req = new LibroUpdateRequest(
                t,
                a,
                Integer.parseInt(anio),
                Integer.parseInt(stock),
                desc
        );

        String token = ApiClient.leerToken(getApplication());

        api.actualizarLibro("Bearer " + token, l.getId(), req)
                .enqueue(new Callback<Libro>() {
                    @Override
                    public void onResponse(Call<Libro> call, Response<Libro> response) {
                        if (response.isSuccessful()) {
                            libro.setValue(response.body());
                            toast.setValue("Libro actualizado correctamente");
                        } else {
                            toast.setValue("Error al actualizar: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Libro> call, Throwable t) {
                        toast.setValue("Error de conexión: " + t.getMessage());
                    }
                });

        editable.setValue(false);
        textoBotonEditar.setValue("✏️ Editar");
    }

    public void reservarLibro() {
        Libro l = libro.getValue();
        String token = ApiClient.leerToken(getApplication());

        PedidoRequest req = new PedidoRequest(l.getId(), l.getTitulo());

        api.crearPedido("Bearer " + token, req)
                .enqueue(new Callback<Pedido>() {
                    @Override
                    public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                        if (response.isSuccessful()) {
                            toast.setValue("Pedido creado con éxito");
                        } else {
                            toast.setValue("Error al crear el pedido");
                        }
                    }

                    @Override
                    public void onFailure(Call<Pedido> call, Throwable t) {
                        toast.setValue("Error: " + t.getMessage());
                    }
                });
    }

    public void solicitarEliminar() {
        mostrarDialogoEliminar.setValue(null);
    }

    public void confirmarEliminar() {
        Libro l = libro.getValue();
        String token = ApiClient.leerToken(getApplication());

        api.eliminarLibro("Bearer " + token, l.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            toast.setValue("Libro dado de baja");
                            volverAtras.setValue(null);
                        } else {
                            toast.setValue("Error al dar de baja");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        toast.setValue("Error de conexión");
                    }
                });
    }
}

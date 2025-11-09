package com.example.bibliotecaapp.ui.pedidos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Pedido;
import com.example.bibliotecaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePedidoViewModel extends AndroidViewModel {

    private final MutableLiveData<Pedido> pedidoLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    public DetallePedidoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Pedido> getPedido() {
        return pedidoLiveData;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // 🔹 Cargar pedido actualizado desde API
    public void cargarPedidoPorId(int pedidoId) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<Pedido> call = api.obtenerPedidoPorId("Bearer " + token, pedidoId);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pedidoLiveData.postValue(response.body());
                } else {
                    mensaje.postValue("⚠️ No se pudo cargar el pedido (código " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                mensaje.postValue("🚫 Error de conexión: " + t.getMessage());
            }
        });
    }

    // 🔹 Asigna un pedido localmente (por argumento desde el fragment)
    public void setPedido(Pedido pedido) {
        pedidoLiveData.setValue(pedido);
    }

    // 🟢 Cambiar el estado del pedido (Pendiente, Prestado, Devuelto, Cancelado)
    public void cambiarEstado(int pedidoId, int nuevoEstado) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<Pedido> call = api.cambiarEstadoPedido("Bearer " + token, pedidoId, nuevoEstado);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pedidoLiveData.postValue(response.body());
                    mensaje.postValue("📦 Estado actualizado a: " + getNombreEstado(nuevoEstado));
                } else {
                    mensaje.postValue("⚠️ Error al cambiar estado (código " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                mensaje.postValue("🚫 Error de conexión: " + t.getMessage());
            }
        });
    }

    // 🟩 Marcar pedido como devuelto (usa endpoint /devolver)
    public void devolverPedido(int pedidoId) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<Pedido> call = api.devolverPedido("Bearer " + token, pedidoId);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pedidoLiveData.postValue(response.body());
                    mensaje.postValue("📗 Libro devuelto correctamente. Stock actualizado.");
                } else {
                    mensaje.postValue("⚠️ Solo se pueden devolver pedidos que estén en estado Prestado.");
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                mensaje.postValue("🚫 Error de conexión: " + t.getMessage());
            }
        });
    }

    // 🔸 Traducción del estado a texto amigable
    private String getNombreEstado(int estado) {
        switch (estado) {
            case 0: return "Pendiente";
            case 1: return "Aprobado";
            case 2: return "Prestado";
            case 3: return "Devuelto";
            case 4: return "Vencido";
            case 5: return "Cancelado";
            default: return "Desconocido";
        }
    }
}

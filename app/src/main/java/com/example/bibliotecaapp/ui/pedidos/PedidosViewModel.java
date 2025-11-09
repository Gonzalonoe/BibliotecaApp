package com.example.bibliotecaapp.ui.pedidos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Pedido;
import com.example.bibliotecaapp.models.PedidoRequest;
import com.example.bibliotecaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pedido>> pedidosLiveData = new MutableLiveData<>();

    public PedidosViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Pedido>> getPedidosLiveData() {
        return pedidosLiveData;
    }

    // 🔹 Cargar los pedidos del usuario actual
    public void cargarPedidos() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<List<Pedido>> call = api.obtenerMisPedidos("Bearer " + token);
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful()) {
                    pedidosLiveData.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
                Log.e("API_PEDIDOS", "Error al cargar pedidos", t);
            }
        });
    }

    // 🔹 Crear un nuevo pedido
    public void crearPedido(int libroId, String titulo) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        PedidoRequest request = new PedidoRequest(libroId, titulo);

        Call<Pedido> call = api.crearPedido("Bearer " + token, request);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Pedido realizado con éxito", Toast.LENGTH_SHORT).show();
                    cargarPedidos();
                } else {
                    Toast.makeText(getApplication(), "Error al crear pedido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_PEDIDOS", "Error al crear pedido", t);
            }
        });
    }

    // 🔹 Cancelar un pedido (solo si está pendiente)
    public void cancelarPedido(int pedidoId) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<Void> call = api.cancelarPedido("Bearer " + token, pedidoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Pedido cancelado", Toast.LENGTH_SHORT).show();
                    cargarPedidos();
                } else {
                    Toast.makeText(getApplication(), "Error al cancelar pedido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_PEDIDOS", "Error al cancelar pedido", t);
            }
        });
    }
}

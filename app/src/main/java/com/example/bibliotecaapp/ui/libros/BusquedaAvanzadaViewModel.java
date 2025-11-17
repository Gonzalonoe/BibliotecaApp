package com.example.bibliotecaapp.ui.libros;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Libro;
import com.example.bibliotecaapp.request.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusquedaAvanzadaViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Libro>> resultados = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Libro>> getResultados() { return resultados; }

    private final MutableLiveData<Integer> paginaActual = new MutableLiveData<>(1);
    private final MutableLiveData<Integer> totalPaginas = new MutableLiveData<>(1);

    private final MutableLiveData<String> textoPagina = new MutableLiveData<>("Página 1 / 1");
    public LiveData<String> getTextoPagina() { return textoPagina; }

    private final MutableLiveData<Boolean> botonAnteriorEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> botonSiguienteEnabled = new MutableLiveData<>(false);
    public LiveData<Boolean> getAnteriorEnabled() { return botonAnteriorEnabled; }
    public LiveData<Boolean> getSiguienteEnabled() { return botonSiguienteEnabled; }

    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    public LiveData<String> getMensaje() { return mensaje; }

    private String titulo, autor, descripcion;
    private Integer anio, stock;

    public BusquedaAvanzadaViewModel(@NonNull Application app) {
        super(app);
    }

    public void buscar(String t, String a, String anioStr, String stockStr, String desc) {
        titulo = t;
        autor = a;
        descripcion = desc;

        anio = anioStr.isEmpty() ? null : Integer.parseInt(anioStr);
        stock = stockStr.isEmpty() ? null : Integer.parseInt(stockStr);

        paginaActual.setValue(1);
        buscarPagina(1);
    }

    private void buscarPagina(int page) {

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("⚠️ Debe iniciar sesión nuevamente.");
            return;
        }

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Call<Map<String, Object>> call = api.buscarLibrosPaginados(
                "Bearer " + token, titulo, autor, anio, stock, descripcion, page, 5
        );

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> res) {

                if (!res.isSuccessful() || res.body() == null) {
                    mensaje.setValue("Error (" + res.code() + ")");
                    return;
                }

                Map<String, Object> data = res.body();

                int paginas = ((Double) data.get("totalPaginas")).intValue();
                int pagina = ((Double) data.get("page")).intValue();

                totalPaginas.setValue(paginas);
                paginaActual.setValue(pagina);
                actualizarTextoPagina(pagina, paginas);
                actualizarHabilitacionBotones(pagina, paginas);

                List<Map<String, Object>> lista = (List<Map<String, Object>>) data.get("resultados");
                List<Libro> libros = new ArrayList<>();

                if (lista != null) {
                    for (Map<String, Object> item : lista) {
                        Libro l = new Libro();
                        l.setId(((Double) item.get("id")).intValue());
                        l.setTitulo((String) item.get("titulo"));
                        l.setAutor((String) item.get("autor"));
                        l.setAnio(item.get("anio") != null ? ((Double) item.get("anio")).intValue() : null);
                        l.setStock(item.get("stock") != null ? ((Double) item.get("stock")).intValue() : 0);
                        l.setDescripcion((String) item.get("descripcion"));
                        l.setPortada((String) item.get("portada"));
                        libros.add(l);
                    }
                }

                resultados.setValue(libros);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                mensaje.setValue("🚫 Error: " + t.getMessage());
            }
        });
    }

    public void paginaSiguiente() {
        Integer actual = paginaActual.getValue();
        Integer total = totalPaginas.getValue();

        if (actual != null && total != null && actual < total) {
            buscarPagina(actual + 1);
        }
    }

    public void paginaAnterior() {
        Integer actual = paginaActual.getValue();

        if (actual != null && actual > 1) {
            buscarPagina(actual - 1);
        }
    }

    private void actualizarTextoPagina(int actual, int total) {
        textoPagina.setValue("Página " + actual + " / " + total);
    }

    private void actualizarHabilitacionBotones(int actual, int total) {
        botonAnteriorEnabled.setValue(actual > 1);
        botonSiguienteEnabled.setValue(actual < total);
    }
}

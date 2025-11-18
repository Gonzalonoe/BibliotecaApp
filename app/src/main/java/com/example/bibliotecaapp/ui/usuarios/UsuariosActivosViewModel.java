package com.example.bibliotecaapp.ui.usuarios;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Usuario;
import com.example.bibliotecaapp.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosActivosViewModel extends AndroidViewModel {

    public MutableLiveData<List<Usuario>> usuarios = new MutableLiveData<>();

    public UsuariosActivosViewModel(@NonNull Application app) {
        super(app);
    }

    public void cargarUsuarios() {
        String token = ApiClient.leerToken(getApplication());

        ApiClient.getInmoServicio()
                .obtenerUsuariosActivos("Bearer " + token)
                .enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                        if (response.isSuccessful())
                            usuarios.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Usuario>> call, Throwable t) {
                    }
                });
    }

    public void darDeBaja(int id, Runnable onDone) {
        String token = ApiClient.leerToken(getApplication());

        ApiClient.getInmoServicio()
                .darDeBajaUsuario("Bearer " + token, id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        cargarUsuarios();
                        onDone.run();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
    }
}

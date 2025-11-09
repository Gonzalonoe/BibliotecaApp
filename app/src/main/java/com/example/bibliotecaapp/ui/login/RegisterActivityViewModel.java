package com.example.bibliotecaapp.ui.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Usuario;
import com.example.bibliotecaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje;

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getMensaje() {
        if (mensaje == null) {
            mensaje = new MutableLiveData<>();
        }
        return mensaje;
    }

    public void registrarUsuario(String nombre, String email, String password) {
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        Usuario usuario = new Usuario(nombre, email, password);

        Call<Usuario> call = api.registrarUsuario(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    mensaje.postValue("Registro exitoso");
                } else {
                    mensaje.postValue("Error al registrar usuario");
                    Log.e("API", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                mensaje.postValue("Error de conexión: " + t.getMessage());
                Log.e("API", "Falla en la llamada: " + t.getMessage());
            }
        });
    }
}


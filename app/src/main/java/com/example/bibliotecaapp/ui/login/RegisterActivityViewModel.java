package com.example.bibliotecaapp.ui.login;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.models.Usuario;
import com.example.bibliotecaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje = new MutableLiveData<>();
    private MutableLiveData<Void> navegarLogin = new MutableLiveData<>();

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getMensaje() {
        return mensaje;
    }

    public MutableLiveData<Void> getNavegarLogin() {
        return navegarLogin;
    }

    public void onClickRegistrar(String nombre, String email, String password) {

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            mensaje.setValue("Todos los campos son obligatorios");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mensaje.setValue("Ingrese un email válido");
            return;
        }

        registrarUsuario(nombre, email, password);
    }

    public void onClickInicio() {
        navegarLogin.setValue(null);
    }

    private void registrarUsuario(String nombre, String email, String password) {

        Usuario usuario = new Usuario(nombre, email, password);
        ApiClient.InmoServicio api = ApiClient.getInmoServicio();

        api.registrarUsuario(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (!response.isSuccessful()) {
                    mensaje.postValue("Error al registrar usuario");
                    return;
                }

                mensaje.postValue("Registro exitoso");
                navegarLogin.postValue(null);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                mensaje.postValue("Error de conexión: " + t.getMessage());
                Log.e("API", t.getMessage());
            }
        });
    }
}

package com.example.bibliotecaapp.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bibliotecaapp.MainActivity;
import com.example.bibliotecaapp.models.LoginRequest;
import com.example.bibliotecaapp.models.LoginResponse;
import com.example.bibliotecaapp.models.Usuario;
import com.example.bibliotecaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private final Context context;
    private MutableLiveData<String> mMensaje;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = getApplication();
    }

    public LiveData<String> getMMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public void logueo(String usuario, String contrasenia) {
        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mMensaje.setValue("Error, campos vacíos");
            return;
        }

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        LoginRequest request = new LoginRequest(usuario, contrasenia);
        Call<LoginResponse> call = api.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    ApiClient.guardarToken(getApplication(), token);

                    ApiClient.InmoServicio api2 = ApiClient.getInmoServicio();
                    Call<Usuario> perfilCall = api2.obtenerPerfil("Bearer " + token);

                    perfilCall.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Usuario user = response.body();
                                ApiClient.guardarUsuario(getApplication(), user);

                                SharedPreferences sp = getApplication()
                                        .getSharedPreferences("usuario", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("nombre", user.getNombre());
                                editor.putString("email", user.getEmail());
                                editor.putString("token", token);
                                editor.apply();

                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                getApplication().startActivity(intent);
                            } else {
                                mMensaje.postValue("Error al obtener el perfil del usuario");
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            mMensaje.postValue("Error de conexión al obtener perfil");
                            Log.e("Perfil", "Error: " + t.getMessage());
                        }
                    });

                } else {
                    mMensaje.postValue("Usuario o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                mMensaje.postValue("Error de conexión con el servidor");
                Log.e("Login", throwable.getMessage());
            }
        });
    }
}



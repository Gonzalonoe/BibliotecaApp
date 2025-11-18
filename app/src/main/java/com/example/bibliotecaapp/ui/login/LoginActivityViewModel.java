package com.example.bibliotecaapp.ui.login;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

public class LoginActivityViewModel extends AndroidViewModel implements SensorEventListener {

    private MutableLiveData<String> mMensaje = new MutableLiveData<>();
    private MutableLiveData<Void> shakeEvent = new MutableLiveData<>();
    private MutableLiveData<Void> solicitarPermisoLlamada = new MutableLiveData<>();

    private SensorManager sensorManager;
    private Sensor acelerometro;

    private static final int SHAKE_THRESHOLD = 800;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        sensorManager = (SensorManager) application.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }


    public LiveData<String> getMMensaje() { return mMensaje; }

    public LiveData<Void> getShakeEvent() { return shakeEvent; }

    public LiveData<Void> getSolicitarPermisoLlamada() { return solicitarPermisoLlamada; }


    public void onLoginClicked(String usuario, String contrasenia) {

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mMensaje.setValue("Error, campos vacíos");
            return;
        }

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        LoginRequest request = new LoginRequest(usuario, contrasenia);

        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    mMensaje.postValue("Usuario o contraseña incorrectos");
                    return;
                }

                String token = response.body().getToken();
                ApiClient.guardarToken(getApplication(), token);

                obtenerPerfil(token);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                mMensaje.postValue("Error de conexión con el servidor");
            }
        });
    }

    private void obtenerPerfil(String token) {

        Call<Usuario> call = ApiClient.getInmoServicio().obtenerPerfil("Bearer " + token);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    mMensaje.postValue("Error al obtener perfil");
                    return;
                }

                Usuario u = response.body();

                SharedPreferences sp = getApplication()
                        .getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);

                sp.edit()
                        .putString("id", u.getId())
                        .putString("nombre", u.getNombre())
                        .putString("email", u.getEmail())
                        .putString("rol", u.getRol())
                        .putString("token", token)
                        .apply();

                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplication().startActivity(intent);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                mMensaje.postValue("Error de conexión al obtener perfil");
            }
        });
    }



    public void onRegisterClicked(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public void iniciarAcelerometro() {
        if (acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void detenerAcelerometro() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) <= 100) return;

        long diffTime = curTime - lastUpdate;
        lastUpdate = curTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float speed = Math.abs(x + y + z - last_x - last_y - last_z)
                / diffTime * 10000;

        last_x = x;
        last_y = y;
        last_z = z;

        if (speed > SHAKE_THRESHOLD) {
            shakeEvent.postValue(null);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }


    public void solicitarLlamada(Context context) {

        boolean permiso = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED;

        if (!permiso) {
            solicitarPermisoLlamada.postValue(null);
            return;
        }

        ejecutarLlamada(context);
    }

    public void resultadoPermisoLlamada(boolean concedido) {
        if (concedido) {
            ejecutarLlamada(getApplication());
        } else {
            mMensaje.postValue("Permiso de llamada denegado");
        }
    }

    private void ejecutarLlamada(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:1122334455"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

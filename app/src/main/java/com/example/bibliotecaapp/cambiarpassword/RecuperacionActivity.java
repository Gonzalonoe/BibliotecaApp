package com.example.bibliotecaapp.cambiarpassword;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.models.CambiarPasswordRequest;
import com.example.bibliotecaapp.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperacionActivity extends AppCompatActivity {

    private EditText etEmail, etPasswordActual, etPasswordNueva;
    private Button btnRestablecer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion);

        etEmail = findViewById(R.id.etRecEmail);
        etPasswordActual = findViewById(R.id.etPasswordActual);
        etPasswordNueva = findViewById(R.id.etPasswordNueva);
        btnRestablecer = findViewById(R.id.btnRestablecerPassword);

        btnRestablecer.setOnClickListener(v -> cambiarPassword());
    }

    private void cambiarPassword() {
        String email = etEmail.getText().toString().trim();
        String actual = etPasswordActual.getText().toString();
        String nueva = etPasswordNueva.getText().toString();

        if (email.isEmpty() || actual.isEmpty() || nueva.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        CambiarPasswordRequest req = new CambiarPasswordRequest(email, actual, nueva);

        ApiClient.InmoServicio api = ApiClient.getInmoServicio();
        api.cambiarPassword(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RecuperacionActivity.this,
                            "Contraseña actualizada correctamente",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RecuperacionActivity.this,
                            "Email o contraseña actual incorrectos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RecuperacionActivity.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}


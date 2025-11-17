package com.example.bibliotecaapp.ui.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.bibliotecaapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel vm;
    private ActivityLoginBinding binding;

    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        vm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())
                .create(LoginActivityViewModel.class);

        setContentView(binding.getRoot());

        vm.getMMensaje().observe(this, msg ->
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show()
        );

        vm.getShakeEvent().observe(this, unused -> {
            vm.solicitarLlamada(LoginActivity.this);
        });

        binding.btLogin.setOnClickListener(v -> {
            vm.onLoginClicked(
                    binding.etEmail.getText().toString(),
                    binding.etPassword.getText().toString()
            );
        });

        binding.btRegister.setOnClickListener(v -> vm.onRegisterClicked(this));

        vm.getSolicitarPermisoLlamada().observe(this, unused -> {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PERMISSION
            );
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vm.iniciarAcelerometro();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vm.detenerAcelerometro();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            boolean concedido = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
            vm.resultadoPermisoLlamada(concedido);
        }
    }
}

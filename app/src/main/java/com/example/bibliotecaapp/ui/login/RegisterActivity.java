package com.example.bibliotecaapp.ui.login;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bibliotecaapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private RegisterActivityViewModel vm;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(RegisterActivityViewModel.class);

        vm.getMensaje().observe(this, mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        );

        vm.getNavegarLogin().observe(this, unused -> {
            finish(); // el ViewModel decide CUÁNDO volver
        });

        binding.btnRegistrar.setOnClickListener(v ->
                vm.onClickRegistrar(
                        binding.etNombre.getText().toString(),
                        binding.etEmail.getText().toString(),
                        binding.etPassword.getText().toString()
                )
        );

        binding.btnInicio.setOnClickListener(v ->
                vm.onClickInicio()
        );
    }
}

package com.example.bibliotecaapp.ui.logout;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bibliotecaapp.databinding.FragmentLogoutBinding;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private LogoutViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(LogoutViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle("Cerrar Sesión");

        vm.getMostrarDialogo().observe(getViewLifecycleOwner(), unused -> {
            mostrarDialogoSalida();
        });

        vm.getNavegarLogin().observe(getViewLifecycleOwner(), intent -> {
            requireActivity().startActivity(intent);
            requireActivity().finish();
        });

        binding.btnLogout.setOnClickListener(v -> {
            animarBoton(v);
            vm.onClickLogout();
        });
    }

    private void animarBoton(View v) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v, "scaleX", 0.9f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v, "scaleY", 0.9f);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);
        scaleDownX.start();
        scaleDownY.start();

        v.postDelayed(() -> {
            ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(v, "scaleX", 1f);
            ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(v, "scaleY", 1f);
            scaleUpX.setDuration(100);
            scaleUpY.setDuration(100);
            scaleUpX.start();
            scaleUpY.start();
        }, 120);
    }

    private void mostrarDialogoSalida() {

        vm.construirDialogo(
                requireContext(),
                requireActivity(),
                getLayoutInflater(),
                requireActivity().getResources()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

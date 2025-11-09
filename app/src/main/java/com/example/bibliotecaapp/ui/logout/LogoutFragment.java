package com.example.bibliotecaapp.ui.logout;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.databinding.FragmentLogoutBinding;
import com.example.bibliotecaapp.ui.login.LoginActivity;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private LogoutViewModel logoutViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(600)
                .setStartDelay(100)
                .start();

        requireActivity().setTitle("Cerrar Sesión");


        binding.btnLogout.setOnClickListener(v -> {
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

                mostrarDialogoDeSalida();
            }, 120);
        });
    }

    private void mostrarDialogoDeSalida() {
        if (getContext() == null) return;

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas cerrar sesión y volver al inicio de sesión?")
                .setPositiveButton("Sí", (d, w) -> {

                    requireActivity().getSharedPreferences("prefs", getContext().MODE_PRIVATE)
                            .edit()
                            .remove("token")
                            .apply();

                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("No", (d, w) -> d.dismiss())
                .setIcon(android.R.drawable.ic_lock_power_off)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.teal_200));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.colorError));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.example.bibliotecaapp.ui.logout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bibliotecaapp.R;
import com.example.bibliotecaapp.ui.login.LoginActivity;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<Void> mostrarDialogo = new MutableLiveData<>();
    private MutableLiveData<Intent> navegarLogin = new MutableLiveData<>();

    public MutableLiveData<Void> getMostrarDialogo() {
        return mostrarDialogo;
    }

    public MutableLiveData<Intent> getNavegarLogin() {
        return navegarLogin;
    }

    public void onClickLogout() {
        mostrarDialogo.setValue(null);
    }

    public void construirDialogo(
            Context context,
            Activity activity,
            LayoutInflater inflater,
            Resources r
    ) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas cerrar sesión y volver al inicio?")
                .setPositiveButton("Sí", (d, w) -> cerrarSesion(context, activity))
                .setNegativeButton("No", (d, w) -> d.dismiss())
                .setIcon(android.R.drawable.ic_lock_power_off)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(r.getColor(R.color.teal_200));

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(r.getColor(R.color.colorError));
    }

    private void cerrarSesion(Context context, Activity activity) {

        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .remove("token")
                .apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        navegarLogin.setValue(intent);
    }
}

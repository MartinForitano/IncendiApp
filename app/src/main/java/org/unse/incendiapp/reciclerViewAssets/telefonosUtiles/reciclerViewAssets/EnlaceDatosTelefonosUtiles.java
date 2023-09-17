package org.unse.incendiapp.reciclerViewAssets.telefonosUtiles.reciclerViewAssets;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unse.incendiapp.R;

public class EnlaceDatosTelefonosUtiles extends RecyclerView.ViewHolder {

    TextView tvNumeroTelefono, tvDescripcionTelefono, rvNroLlamada;

    public EnlaceDatosTelefonosUtiles(@NonNull View itemView) {
        super(itemView);
        tvNumeroTelefono = itemView.findViewById(R.id.tv_numeroTelefono);
        tvDescripcionTelefono = itemView.findViewById(R.id.tv_descripcionTelefono);
        rvNroLlamada = itemView.findViewById(R.id.tv_nroLlamada);
    }
}

package org.unse.incendiapp.reciclerViewAssets;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unse.incendiapp.R;

public class EnlaceDatos extends RecyclerView.ViewHolder {

    TextView tvTipo, tvUbicacion, tvId, tvFechas;
    ImageView ivTipoEvento;

    public EnlaceDatos(@NonNull View itemView) {
        super(itemView);
        tvTipo = itemView.findViewById(R.id.tv_tipoEvento);
        tvUbicacion = itemView.findViewById(R.id.tv_ubicacionEvento);
        tvId = itemView.findViewById(R.id.tv_idEvento);
        ivTipoEvento = itemView.findViewById(R.id.iv_TipoEvento);
        tvFechas = itemView.findViewById(R.id.tv_item_fechas);
    }
}

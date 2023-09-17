package org.unse.incendiapp.reciclerViewAssets.telefonosUtiles.reciclerViewAssets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unse.incendiapp.R;

import java.util.ArrayList;

public class AdaptadorDatosTelefonosUtiles extends RecyclerView.Adapter<EnlaceDatosTelefonosUtiles> implements View.OnClickListener{

    Context context;
    ArrayList<item_telefonos_utiles> items;

    private View.OnClickListener listener;

    public AdaptadorDatosTelefonosUtiles(Context context, ArrayList<item_telefonos_utiles> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public EnlaceDatosTelefonosUtiles onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_telefonos_utiles, parent, false);
        v.setOnClickListener(this.listener);
        return new EnlaceDatosTelefonosUtiles(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EnlaceDatosTelefonosUtiles holder, int position) {
        holder.tvNumeroTelefono.setText(items.get(position).getNroTelefono());
        holder.tvDescripcionTelefono.setText(items.get(position).getDescripcionTelefono());
        holder.rvNroLlamada.setText(String.valueOf(items.get(position).getNumeroLlamada()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View view) {

    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}

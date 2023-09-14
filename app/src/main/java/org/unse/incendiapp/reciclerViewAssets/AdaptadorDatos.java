package org.unse.incendiapp.reciclerViewAssets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unse.incendiapp.R;

import java.util.ArrayList;

public class AdaptadorDatos extends RecyclerView.Adapter<EnlaceDatos> implements View.OnClickListener{

    Context context;
    ArrayList<item_evento> items;

    private View.OnClickListener listener;

    public AdaptadorDatos(Context context, ArrayList<item_evento> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public EnlaceDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        v.setOnClickListener(this.listener);
        return new EnlaceDatos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EnlaceDatos holder, int position) {
        holder.tvTipo.setText(items.get(position).getTipoEvento());
        holder.tvUbicacion.setText(items.get(position).getUbicacionEvento());
        holder.tvId.setText(String.valueOf(items.get(position).getIdEvento()));
        switch (items.get(position).getTipoEvento()){
            case "Incendio":
                holder.ivTipoEvento.setImageResource(R.drawable.fuego);
                break;
            case "Accidente":
                holder.ivTipoEvento.setImageResource(R.drawable.accidente);
                break;
            case "Calzada en mal estado":
                holder.ivTipoEvento.setImageResource(R.drawable.calzadamalestado);
                break;
            case "Derrumbe":
                holder.ivTipoEvento.setImageResource(R.drawable.colapsocamino);
                break;
            case "Neblina":
                holder.ivTipoEvento.setImageResource(R.drawable.fog);
                break;
            case "Estructura colapsada":
                holder.ivTipoEvento.setImageResource(R.drawable.colapsoestructural);
                break;
            case "Incendio estructural":
                holder.ivTipoEvento.setImageResource(R.drawable.building);
                break;
            case "Incendio forestal":
                holder.ivTipoEvento.setImageResource(R.drawable.fire);
                break;
            case "Catastrofe":
                holder.ivTipoEvento.setImageResource(R.drawable.desastre);
                break;
            case "Boton antipanico":
                holder.ivTipoEvento.setImageResource(R.drawable.botonantipanico);
                break;
            case "Otro":
                holder.ivTipoEvento.setImageResource(R.drawable.otro);
                break;
        }

        if(items.get(position).getEsVerificado()){
            holder.ivEsVerificado.setVisibility(View.VISIBLE);
        }else{
            holder.ivEsVerificado.setVisibility(View.INVISIBLE);
        }


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

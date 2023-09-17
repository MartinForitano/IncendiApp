package org.unse.incendiapp.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.unse.incendiapp.R;
import org.unse.incendiapp.io.entidadesApi.DTOEventoResponse;
import org.unse.incendiapp.reciclerViewAssets.AdaptadorDatos;
import org.unse.incendiapp.reciclerViewAssets.item_evento;
import org.unse.incendiapp.reciclerViewAssets.telefonosUtiles.reciclerViewAssets.AdaptadorDatosTelefonosUtiles;
import org.unse.incendiapp.reciclerViewAssets.telefonosUtiles.reciclerViewAssets.item_telefonos_utiles;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link telefonos_utiles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class telefonos_utiles extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public telefonos_utiles() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment telefonos_utiles.
     */
    // TODO: Rename and change types and number of parameters
    public static telefonos_utiles newInstance(String param1, String param2) {
        telefonos_utiles fragment = new telefonos_utiles();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_telefonos_utiles, container, false);
    }

    public void onResume(){
        super.onResume();
        cargarInterfazTelefonosUtiles();
    }

    private void cargarInterfazTelefonosUtiles() {
        configurarRecyclerListaTelefonos(cargarListaTelefonos());
    }

    private ArrayList<item_telefonos_utiles> cargarListaTelefonos() {
        ArrayList<item_telefonos_utiles> listaRetorno = new ArrayList<>();
        listaRetorno.add(new item_telefonos_utiles(100, "100", "Bomberos"));
        listaRetorno.add(new item_telefonos_utiles(101, "101", "Policía - Comando Radioeléctrico"));
        listaRetorno.add(new item_telefonos_utiles(102, "102", "Ayuda al Niño"));
        listaRetorno.add(new item_telefonos_utiles(6007819, "6007819", "Defensa Civil"));
        listaRetorno.add(new item_telefonos_utiles(105, "105", "Emergencia Ambiental"));
        listaRetorno.add(new item_telefonos_utiles(106, "106", "Emergencia Náutica"));
        listaRetorno.add(new item_telefonos_utiles(107, "107", "Emergencia Médica"));
        listaRetorno.add(new item_telefonos_utiles(4212600, "4212600", "Servicio Social Municipal"));
        return listaRetorno;
    }

    private void configurarRecyclerListaTelefonos(ArrayList<item_telefonos_utiles> listaItems) {
        RecyclerView recyclerView = getActivity().findViewById(R.id.rv_telefonos_utiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        AdaptadorDatosTelefonosUtiles adaptadorDatos = new AdaptadorDatosTelefonosUtiles(getActivity(), listaItems);
        adaptadorDatos.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog;
                AlertDialog.Builder builderAlert;

                builderAlert = new AlertDialog.Builder(getContext());
                builderAlert.setTitle("¿Confirmar la llamada a " + listaItems.get(recyclerView.getChildLayoutPosition(view)).getDescripcionTelefono()+ "?");
                builderAlert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        realizarLlamada(listaItems.get(recyclerView.getChildLayoutPosition(view)).getNumeroLlamada());
                    }
                });
                builderAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //No hacer nada
                    }
                });
                dialog = builderAlert.create();
                dialog.show();
            }
        });
        recyclerView.setAdapter(adaptadorDatos);
    }

    private void realizarLlamada(Integer numeroTelefono){
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + numeroTelefono));
            startActivity(i);
        }else{
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 255);
        }
    }



}
package org.unse.incendiapp.UI;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unse.incendiapp.DB.AdminSQLiteOpenHelper;
import org.unse.incendiapp.Entidades.Usuario;
import org.unse.incendiapp.MainActivity;
import org.unse.incendiapp.R;
import org.unse.incendiapp.io.ApiMethods;
import org.unse.incendiapp.io.entidadesApi.DTOEventoResponse;
import org.unse.incendiapp.io.entidadesApi.DTOListadoGeneral;
import org.unse.incendiapp.io.entidadesApi.UsuarioApi;
import org.unse.incendiapp.reciclerViewAssets.AdaptadorDatos;
import org.unse.incendiapp.reciclerViewAssets.item_evento;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link administracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class administracion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String ipApi = "192.168.0.14:8080";

    private Long idEventoModifica;

    public administracion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment administracion.
     */
    // TODO: Rename and change types and number of parameters
    public static administracion newInstance(String param1, String param2) {
        administracion fragment = new administracion();
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
        return inflater.inflate(R.layout.fragment_administracion, container, false);
    }

    public void onResume(){
        cargarInterfazAdministracion();
        super.onResume();
    }

    public void onPause(){
        super.onPause();
    }


    public void iraCrearEvento(View view){

    }

    private void cargarInterfazAdministracion(){
        ConstraintLayout layAdmin, layAviso;
        FloatingActionButton favAgregar;
        layAdmin = getActivity().findViewById(R.id.lay_administracion);
        layAviso = getActivity().findViewById(R.id.lay_aviso_login);
        favAgregar = getActivity().findViewById(R.id.fav_btn_agregarEvento);
        if(obtenerUsuario()!=null) {
            ((MainActivity) getActivity()).cargarListaAdministracion();
            layAdmin.setVisibility(View.VISIBLE);
            layAviso.setVisibility(View.INVISIBLE);
            favAgregar.setClickable(true);
            configurarControlesFiltro();
        }else{
            layAdmin.setVisibility(View.INVISIBLE);
            layAviso.setVisibility(View.VISIBLE);
            favAgregar.setClickable(false);
        }
    }

    private Usuario obtenerUsuario() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(), "adminUsuarios", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase(); //abrir el flujo para escribir en la db
        Cursor registro = DB.rawQuery("SELECT * FROM usuarios where idUsuario = 1", null);
        Usuario u = null;
        if(registro.moveToFirst()){
            u = new Usuario(null, registro.getString(0), registro.getString(1), registro.getString(2), registro.getInt(3));
        }
        return u;
    }

    private void configurarControlesFiltro() {
        CheckBox cbTodos, cbVerificados, cbEnCurso;
        cbTodos = getActivity().findViewById(R.id.cb_todos);
        cbVerificados = getActivity().findViewById(R.id.cb_solo_verificados);
        cbEnCurso = getActivity().findViewById(R.id.cb_solo_en_curso);
        cbTodos.setChecked(true);
        cbEnCurso.setChecked(false);
        cbVerificados.setChecked(false);
        cbTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbTodos.isChecked()) {
                    cbVerificados.setChecked(false);
                    cbEnCurso.setChecked(false);
                }
            }
        });

        cbVerificados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbVerificados.isChecked()){
                    cbTodos.setChecked(false);
                }
            }
        });

        cbEnCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbEnCurso.isChecked()){
                    cbTodos.setChecked(false);
                }
            }
        });
    }

    public void cargarListaAdministracionFiltrada(View view) {

    }

}
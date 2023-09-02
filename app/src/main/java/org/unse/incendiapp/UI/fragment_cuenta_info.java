package org.unse.incendiapp.UI;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.unse.incendiapp.DB.AdminSQLiteOpenHelper;
import org.unse.incendiapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_cuenta_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_cuenta_info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    public fragment_cuenta_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_cuenta_info.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_cuenta_info newInstance(String param1, String param2) {
        fragment_cuenta_info fragment = new fragment_cuenta_info();
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
        v = inflater.inflate(R.layout.fragment_cuenta_info, container, false);
        // Inflate the layout for this fragment
        configurarVistaInformacion();
        return v;
    }

    @Override
    public void onResume() {
        configurarVistaInformacion();
        super.onResume();
    }

    public void configurarVistaInformacion(){
        TextView tvCuenta = v.findViewById(R.id.tv_nombreCuenta);
        AdminSQLiteOpenHelper admin;
        admin = new AdminSQLiteOpenHelper(this.getContext(), "adminUsuarios", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase();
        Cursor registro = DB.rawQuery("SELECT * FROM usuarios WHERE idUsuario = 1", null);
        if(registro.moveToFirst()){
            tvCuenta.setText(registro.getString(1));
            DB.close();
        };
        DB.close();
    }

}
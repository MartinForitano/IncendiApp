package org.unse.incendiapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.unse.incendiapp.MainActivity;
import org.unse.incendiapp.R;

public class fragment_ubicacion_evento_entrada_modificacion extends Fragment {


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if(((MainActivity) getActivity()).getLatitudIngreso()!=null && ((MainActivity) getActivity()).getLongitudIngreso()!=null) {
                if (((MainActivity) getActivity()).getLatitudIngreso() != 0 && ((MainActivity) getActivity()).getLongitudIngreso() != 0) {
                    LatLng ubicacion = new LatLng(((MainActivity) getActivity()).getLatitudIngreso(), ((MainActivity) getActivity()).getLongitudIngreso());
                    googleMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicacion del evento"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
                }
            }else{
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    googleMap.clear();
                    LatLng nuevaUbicacion = new LatLng(latLng.latitude, latLng.longitude);
                    googleMap.addMarker(new MarkerOptions().position(nuevaUbicacion).title("Ubicacion de evento"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(nuevaUbicacion));
                    ((MainActivity) getActivity()).setLatitudIngreso(latLng.latitude);
                    ((MainActivity) getActivity()).setLongitudIngreso(latLng.longitude);
                    ((MainActivity) getActivity()).setLatitudIngresoModifica(latLng.latitude);
                    ((MainActivity) getActivity()).setLongitudIngresoModifica(latLng.longitude);
                    AlertDialog dialog;
                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("¿Elegir ubicacion?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getFragmentManager().popBackStack();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // no hacer nada
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }

            });


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ubicacion_evento_entrada_modificacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
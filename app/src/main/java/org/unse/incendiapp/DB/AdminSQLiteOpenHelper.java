package org.unse.incendiapp.DB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
// clase administradora de la DB

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase adminUsuarios) {
        adminUsuarios.execSQL("CREATE TABLE usuarios(idUsuario INTEGER primary key, nombre text, pass text, token text, tipousuario INTEGER )");
        //adminUsuarios.execSQL("CREATE TABLE eventos(idEvento INTEGER primary key, tipo text, cantVictimas INTEGER, autoridades text, areaInfluencia text, ubicacionEvento text, tiempoInicio INTEGER, tiempoFin INTEGER DEFAULT NULL, ubiLatitud text, ubiLongitud text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

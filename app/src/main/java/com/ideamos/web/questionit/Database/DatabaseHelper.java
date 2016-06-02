package com.ideamos.web.questionit.Database;

import android.content.Context;
import java.sql.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Creado por Ideamosweb on 31/05/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "questionit.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    private Dao<User, Integer> userDao = null;
    private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;

    /*Funcion que permite crear la base de datos cuando inicia la aplicacion
    * Usa como parametros;
    * @param sqLiteDatabase -> extension de la base de datos para sqlite
    * @param source -> variable para la conexion a los recursos de sqlite
    */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        try {
            TableUtils.createTable(source, User.class);
        } catch (SQLException sqlEx) {
            Log.e("DatabaseHelper(onCreate)", "Error: " + sqlEx.getMessage());
            throw new RuntimeException(sqlEx);
        }
    }

    /*Funcion que permite actualizar la base de datos cuando sea necesario
    * Usa como parametros;
    * @param db -> extension de la base de datos
    * @param source -> variable para la conexion a la base de datos
    * @param oldVersion -> numero de version actual de la base de datos
    * @param newVersion -> numero de la nueva version de la base de datos
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(source, User.class, true);
            onCreate(db, source);
        } catch (SQLException sqlEx) {
            Log.e("DatabaseHelper(onUpgrade)", "Error: " + sqlEx.getMessage());
            Log.e(DatabaseHelper.class.getSimpleName(), "Imposible eliminar la base de datos", sqlEx);
        }
    }

    /*Funcion que permite resetear la base de datos cuando se cierra la sesión del usuario
    * Todos los datos de la aplicación son borrados para evitar información basura.
    * No recibe ningun parametro para funcionar.
    */
    public void onResetDataBase(){
        try {
            ConnectionSource source = this.getConnectionSource();
            //Se eliminan las tablas existentes
            TableUtils.dropTable(source, User.class, true);
            //Recreacion de las tablas
            TableUtils.createTable(source, User.class);
        }catch (SQLException sqlEx){
            Log.i("DatabaseHelper(onResetDataBase)", "Error: " + sqlEx.getMessage());
            throw new RuntimeException(sqlEx);
        }
    }

    public void close(){
        super.close();
        userDao = null;
        userRuntimeDao = null;
    }

    public Dao<User, Integer> getUserDao() throws SQLException {
        if(userDao == null) userDao = getDao(User.class);
        return userDao;
    }
    public RuntimeExceptionDao<User, Integer> getUsuarioRuntimeDao() {
        if(userRuntimeDao == null) userRuntimeDao = getRuntimeExceptionDao(User.class);
        return userRuntimeDao;
    }

}

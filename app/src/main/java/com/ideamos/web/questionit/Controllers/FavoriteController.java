package com.ideamos.web.questionit.Controllers;

import android.content.Context;
import android.util.Log;
import com.ideamos.web.questionit.Database.DatabaseHelper;
import com.ideamos.web.questionit.Models.Favorite;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import java.util.List;

/**
 * Creado por Deimer, fecha: 6/07/2016.
 */
public class FavoriteController {

    private DatabaseHelper helper;
    private Context context;

    public void favoriteController(){}

    public FavoriteController(Context context){
        this.context = context;
        favoriteController();
    }

    //Funcion que permite la creacion de un favorito
    public boolean create(Favorite favorite){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
            favoriteDao.createOrUpdate(favorite);
        } catch (Exception ex) {
            res = false;
            Log.e("FavoriteController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un favorito
    public boolean update(Favorite favorite){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
            favoriteDao.update(favorite);
        } catch (Exception ex) {
            res = false;
            Log.e("FavoriteController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion de un favorito
    public Favorite show(int id){
        Favorite favorite;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
            favorite = favoriteDao.queryForId(id);
        } catch (Exception ex) {
            favorite = null;
            Log.e("FavoriteController(show)", "Error: " + ex.getMessage());
        }
        return favorite;
    }

    //Funcion que permite mostrar toda la informacion de un favorito
    public Favorite search(int post_id){
        Favorite favorite;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
            favorite = favoriteDao.queryBuilder().where().eq("post_id", post_id).query().get(0);
        } catch (Exception ex) {
            favorite = null;
            Log.e("FavoriteController(search)", "Error: " + ex.getMessage());
        }
        return favorite;
    }

    //Funcion que permite mostrar toda la informacion de un favorito
    public boolean thereFavorites(int post_id){
        boolean isFavorite = false;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
            List<Favorite> list = favoriteDao.queryBuilder().where().eq("post_id", post_id).query();
            if (list.size() > 0){
                isFavorite = true;
            }
        } catch (Exception ex) {
            isFavorite = false;
            Log.e("FavoriteController(show)", "Error: " + ex.getMessage());
        }
        return isFavorite;
    }

    public boolean isFavorite(int post_id){
        boolean isFavorite = false;
        try {
            if(thereFavorites(post_id)){
                helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
                RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
                Favorite favorite = favoriteDao.queryBuilder().where().eq("post_id", post_id).query().get(0);
                isFavorite = favorite.isActive();
            }
        } catch (Exception ex) {
            isFavorite = false;
            Log.e("FavoriteController(show)", "Error: " + ex.getMessage());
        }
        return isFavorite;
    }

    //Funcion para listar la lista de favoritos de un usuario
    public List<Favorite> list(){
        List<Favorite> list;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
            list = favoriteDao.queryForAll();
        } catch (Exception ex) {
            list = null;
            Log.e("FavoriteController(list)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite eliminar un favorito de la base de datos
    public boolean delete(Favorite favorite){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Favorite, Integer> favoriteDao = helper.getFavoriteRuntimeDao();
            favoriteDao.delete(favorite);
        } catch (Exception ex) {
            res = false;
            Log.e("FavoriteController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

}

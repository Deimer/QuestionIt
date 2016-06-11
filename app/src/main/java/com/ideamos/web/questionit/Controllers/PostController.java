package com.ideamos.web.questionit.Controllers;

import android.content.Context;
import android.util.Log;

import com.ideamos.web.questionit.Database.DatabaseHelper;
import com.ideamos.web.questionit.Models.Post;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * Creado por Ideamosweb on 9/06/2016.
 */
public class PostController {

    private DatabaseHelper helper;
    private Context contexto;

    public void postController(){}

    public PostController(Context contexto){
        this.contexto = contexto;
        postController();
    }

    //Funcion que permite la creacion de un post nuevo
    public boolean create(Post post){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Post, Integer> postDao = helper.getPostRuntimeDao();
            postDao.createOrUpdate(post);
        } catch (Exception ex) {
            res = false;
            Log.e("PostController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un post
    public boolean update(Post post){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Post, Integer> postDao = helper.getPostRuntimeDao();
            postDao.update(post);
        } catch (Exception ex) {
            res = false;
            Log.e("PostController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion de un post del usuario
    public Post show(Context context, int id){
        Post post;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Post, Integer> postDao = helper.getPostRuntimeDao();
            post = postDao.queryForId(id);
        } catch (Exception ex) {
            post = null;
            Log.e("PostController(show)", "Error: " + ex.getMessage());
        }
        return post;
    }

    public List<Post> list(Context context){
        List<Post> list;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Post, Integer> postDao = helper.getPostRuntimeDao();
            list = postDao.queryForAll();
        } catch (Exception ex) {
            list = null;
            Log.e("PostController(list)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite eliminar un post de la base de datos
    public boolean delete(Post post){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Post, Integer> postDao = helper.getPostRuntimeDao();
            postDao.delete(post);
        } catch (Exception ex) {
            res = false;
            Log.e("PostController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

}

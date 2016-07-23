package com.ideamos.web.questionit.Controllers;

import android.content.Context;
import android.util.Log;
import com.ideamos.web.questionit.Database.DatabaseHelper;
import com.ideamos.web.questionit.Models.Reaction;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import java.util.List;

/**
 * Creado por Deimer, fecha: 14/07/2016.
 */
public class ReactionController {

    private DatabaseHelper helper;
    private Context context;

    public void reactionController(){}

    public ReactionController(Context context){
        this.context = context;
        reactionController();
    }

    //Funcion que permite la creacion de una reaccion
    public boolean create(Reaction reaction){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
            reactionDao.createOrUpdate(reaction);
        } catch (Exception ex) {
            res = false;
            Log.e("ReactionController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de una reaccion
    public boolean update(Reaction reaction){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
            reactionDao.update(reaction);
        } catch (Exception ex) {
            res = false;
            Log.e("ReactionController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion de una reaccion
    public Reaction show(int id){
        Reaction reaction;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
            reaction = reactionDao.queryForId(id);
        } catch (Exception ex) {
            reaction = null;
            Log.e("ReactionController(show)", "Error: " + ex.getMessage());
        }
        return reaction;
    }

    //Funcion que permite mostrar toda la informacion de una reaccion
    public Reaction search(int post_id){
        Reaction reaction;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
            reaction = reactionDao.queryBuilder().where().eq("post_id", post_id).queryForFirst();
        } catch (Exception ex) {
            reaction = null;
            Log.e("ReactionController(search)", "Error: " + ex.getMessage());
        }
        return reaction;
    }

    //Funcion que permite mostrar toda la informacion de una reaccion
    public boolean hasReaction(int post_id){
        boolean hasReaction = false;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
            List<Reaction> list = reactionDao.queryBuilder().where().eq("post_id", post_id).query();
            if (list.size() > 0){
                hasReaction = true;
            }
        } catch (Exception ex) {
            hasReaction = false;
            Log.e("ReactionController(show)", "Error: " + ex.getMessage());
        }
        return hasReaction;
    }

    public Reaction isReaction(int post_id){
        Reaction reaction = null;
        try {
            if(hasReaction(post_id)){
                helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
                RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
                reaction = reactionDao.queryBuilder().where().eq("post_id", post_id).query().get(0);
            }
        } catch (Exception ex) {
            reaction = null;
            Log.e("ReactionController(show)", "Error: " + ex.getMessage());
        }
        return reaction;
    }

    //Funcion para listar la lista de reacciones de un usuario
    public List<Reaction> list(){
        List<Reaction> list;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
            list = reactionDao.queryForAll();
        } catch (Exception ex) {
            list = null;
            Log.e("ReactionController(list)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite eliminar una reaccion de la base de datos
    public boolean delete(Reaction reaction){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Reaction, Integer> reactionDao = helper.getReactionRuntimeDao();
            reactionDao.delete(reaction);
        } catch (Exception ex) {
            res = false;
            Log.e("ReactionController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

}

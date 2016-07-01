package com.ideamos.web.questionit.Controllers;

import android.content.Context;
import android.util.Log;
import com.ideamos.web.questionit.Database.DatabaseHelper;
import com.ideamos.web.questionit.Models.UserAnswer;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import java.util.List;

/**
 * Creado por Deimer, fecha: 30/06/2016.
 */
public class UserAnswerController {

    private DatabaseHelper helper;
    private Context context;

    public void userAnswerController(){}

    public UserAnswerController(Context context){
        this.context = context;
        userAnswerController();
    }

    //Funcion que permite la creacion de una nueva respuesta
    public boolean create(UserAnswer answer){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            RuntimeExceptionDao<UserAnswer, Integer> answerDao = helper.getUserAnswerRuntimeDao();
            answerDao.createOrUpdate(answer);
        } catch (Exception ex) {
            res = false;
            Log.e("UserAnswerController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de una respuesta
    public boolean update(UserAnswer answer){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<UserAnswer, Integer> answerDao = helper.getUserAnswerRuntimeDao();
            answerDao.update(answer);
        } catch (Exception ex) {
            res = false;
            Log.e("UserAnswerController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion de un tipo de respuesta
    public UserAnswer show(int id){
        UserAnswer answer;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<UserAnswer, Integer> answerDao = helper.getUserAnswerRuntimeDao();
            answer = answerDao.queryForId(id);
        } catch (Exception ex) {
            answer = null;
            Log.e("UserAnswerController(show)", "Error: " + ex.getMessage());
        }
        return answer;
    }

    public List<UserAnswer> list(){
        List<UserAnswer> list;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<UserAnswer, Integer> answerDao = helper.getUserAnswerRuntimeDao();
            list = answerDao.queryForAll();
        } catch (Exception ex) {
            list = null;
            Log.e("UserAnswerController(list)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite eliminar un tipo de respuesta de la base de datos
    public boolean delete(UserAnswer answer){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<UserAnswer, Integer> answerDao = helper.getUserAnswerRuntimeDao();
            answerDao.delete(answer);
        } catch (Exception ex) {
            res = false;
            Log.e("UserAnswerController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

}

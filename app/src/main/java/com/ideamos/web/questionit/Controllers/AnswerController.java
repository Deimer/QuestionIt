package com.ideamos.web.questionit.Controllers;

import android.content.Context;
import android.util.Log;
import com.ideamos.web.questionit.Database.DatabaseHelper;
import com.ideamos.web.questionit.Models.Answer;
import com.ideamos.web.questionit.Models.AnswerType;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import java.util.List;

/**
 * Creado por Deimer, fecha: 22/06/2016.
 */
public class AnswerController {

    private DatabaseHelper helper;
    private Context context;

    public void answerController(){}

    public AnswerController(Context context){
        this.context = context;
        answerController();
    }

//region Seccion de Answer

    //Funcion que permite la creacion de una nueva respuesta
    public boolean create(Answer answer){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            RuntimeExceptionDao<Answer, Integer> answerDao = helper.getAnswerRuntimeDao();
            answerDao.createOrUpdate(answer);
        } catch (Exception ex) {
            res = false;
            Log.e("AnswerController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de una respuesta
    public boolean update(Answer answer){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Answer, Integer> answerDao = helper.getAnswerRuntimeDao();
            answerDao.update(answer);
        } catch (Exception ex) {
            res = false;
            Log.e("AnswerController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion de un tipo de respuesta
    public Answer view(int id){
        Answer answer;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Answer, Integer> answerDao = helper.getAnswerRuntimeDao();
            answer = answerDao.queryForId(id);
        } catch (Exception ex) {
            answer = null;
            Log.e("AnswerController(show)", "Error: " + ex.getMessage());
        }
        return answer;
    }

    public List<Answer> toList(){
        List<Answer> list;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Answer, Integer> answerDao = helper.getAnswerRuntimeDao();
            list = answerDao.queryForAll();
        } catch (Exception ex) {
            list = null;
            Log.e("AnswerController(list)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite eliminar un tipo de respuesta de la base de datos
    public boolean delete(Answer answer){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Answer, Integer> answerDao = helper.getAnswerRuntimeDao();
            answerDao.delete(answer);
        } catch (Exception ex) {
            res = false;
            Log.e("AnswerController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

//endregion

//region Seccion de AnswerType

    //Funcion que permite la creacion de un nuevo tipo de respuesta
    public boolean create(AnswerType answerType){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            RuntimeExceptionDao<AnswerType, Integer> answerTypeDao = helper.getAnswerTypeRuntimeDao();
            answerTypeDao.createOrUpdate(answerType);
        } catch (Exception ex) {
            res = false;
            Log.e("AnswerController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un tipo de respuesta
    public boolean update(AnswerType answerType){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<AnswerType, Integer> answerTypeDao = helper.getAnswerTypeRuntimeDao();
            answerTypeDao.update(answerType);
        } catch (Exception ex) {
            res = false;
            Log.e("AnswerController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion de un tipo de respuesta
    public AnswerType show(int id){
        AnswerType answerType;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<AnswerType, Integer> answerTypeDao = helper.getAnswerTypeRuntimeDao();
            answerType = answerTypeDao.queryForId(id);
        } catch (Exception ex) {
            answerType = null;
            Log.e("AnswerController(show)", "Error: " + ex.getMessage());
        }
        return answerType;
    }

    public List<AnswerType> list(){
        List<AnswerType> list;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<AnswerType, Integer> answerTypeDao = helper.getAnswerTypeRuntimeDao();
            list = answerTypeDao.queryForAll();
        } catch (Exception ex) {
            list = null;
            Log.e("AnswerController(list)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite eliminar un tipo de respuesta de la base de datos
    public boolean delete(AnswerType answerType){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<AnswerType, Integer> answerTypeDao = helper.getAnswerTypeRuntimeDao();
            answerTypeDao.delete(answerType);
        } catch (Exception ex) {
            res = false;
            Log.e("AnswerController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

//endregion

}

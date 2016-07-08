package com.ideamos.web.questionit.Controllers;

import android.content.Context;
import android.util.Log;
import com.ideamos.web.questionit.Database.DatabaseHelper;
import com.ideamos.web.questionit.Models.Category;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import java.util.List;

/**
 * Creado por Ideamosweb on 14/06/2016.
 */
public class CategoryController {

    private DatabaseHelper helper;
    private Context context;

    public void categoryController(){}

    public CategoryController(Context context){
        this.context = context;
        categoryController();
    }

    //Funcion que permite la creacion de una nueva categoria
    public boolean create(Category category){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            categoryDao.createOrUpdate(category);
        } catch (Exception ex) {
            res = false;
            Log.e("CategoryController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de una categoria
    public boolean update(Category category){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            categoryDao.update(category);
        } catch (Exception ex) {
            res = false;
            Log.e("CategoryController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion de una categoria
    public Category show(int id){
        Category category;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            category = categoryDao.queryForId(id);
        } catch (Exception ex) {
            category = null;
            Log.e("CategoryController(show)", "Error: " + ex.getMessage());
        }
        return category;
    }

    public List<Category> list(){
        List<Category> list;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            list = categoryDao.queryForAll();
        } catch (Exception ex) {
            list = null;
            Log.e("CategoryController(list)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite eliminar una categoria de la base de datos
    public boolean delete(Category category){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            categoryDao.delete(category);
        } catch (Exception ex) {
            res = false;
            Log.e("CategoryController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

}

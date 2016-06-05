package com.ideamos.web.questionit.views;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.gregacucnik.EditTextView;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Update extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{

    //Variables de entorno
    private Context context;
    private UserController userController;
    private Validate validate;
    private SweetDialog dialog;

    //Elementos
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.img_user)ImageView img_user;
    @Bind(R.id.cv_photo_profile)CircleImageView cv_profile;
    @Bind(R.id.txt_first_name)EditTextView txt_first_name;
    @Bind(R.id.txt_last_name)EditTextView txt_last_name;
    @Bind(R.id.txt_birth_date)EditTextView txt_birth_date;
    @Bind(R.id.layout_date)LinearLayout layout_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        validate = new Validate(context);
        dialog = new SweetDialog(context);
        setupToolbar();
        setupDatePicker();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setupDatePicker(){
        layout_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Update.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.dismissOnPause(true);
                dpd.showYearPickerFirst(true);
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setTitle("Escoja su fecha");
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                dpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"-"+(++monthOfYear)+"-"+year;
        txt_birth_date.setText(date);
    }

    @OnClick(R.id.fab_update)
    public void clickUpdate(){
        String fname = txt_first_name.getText();
        String lname = txt_last_name.getText();
        String bdate = txt_birth_date.getText();
        if (fname.equalsIgnoreCase("") || lname.equalsIgnoreCase("")) {
            dialog.dialogWarning("", "Antes de continuar, debes proporcionar tus credenciales.");
        } else {
            if(bdate.equalsIgnoreCase("")){
                dialog.dialogWarning("", "Debes proporcionar tu fecha de nacimiento.");
            }else{
                boolean hayConexion = validate.connect();
                if (hayConexion) {
                    update(fname, lname, bdate);
                } else {
                    dialog.dialogWarning("Error de conexión", "No se pudo detectar una conexión estable a internet.");
                }
            }
        }
    }

    public void update(String fname, String lname, String bdate){
        dialog.dialogProgress("Iniciando sesión...");
        final String url = getString(R.string.url_con);
        int user_id = userController.show(context).getUser_id();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.updateUser(user_id, fname, lname, bdate, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    User user = userController.show(context);
                    user.setState(2);
                    if(userController.update(user)){
                        dialog.cancelarProgress();
                        //next();
                    }
                } else {
                    String message = jsonObject.get("message").getAsString();
                    dialog.cancelarProgress();
                    dialog.dialogWarning("", message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Login(login)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(login)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

}

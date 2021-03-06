package com.ideamos.web.questionit.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.gregacucnik.EditTextView;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Update extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener{

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
    @Bind(R.id.txt_birth_date)TextView lbl_birth_date;

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
        loadImageProfile();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setupDatePicker(){
        lbl_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDate();
            }
        });
    }

    public void showDialogDate(){
        Calendar now = Calendar.getInstance();
        Date d = new Date();
        Calendar before = Calendar.getInstance();
        before.setTime(d);
        before.add(Calendar.YEAR, -12);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Update.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.dismissOnPause(true);
        dpd.vibrate(false);
        dpd.setMaxDate(before);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"-"+(++monthOfYear)+"-"+year;
        lbl_birth_date.setText(date);
    }

    @OnClick(R.id.fab_update)
    public void clickUpdate(){
        String fname = txt_first_name.getText().trim();
        String lname = txt_last_name.getText().trim();
        String bdate = lbl_birth_date.getText().toString();
        if (fname.equalsIgnoreCase("") || lname.equalsIgnoreCase("")) {
            dialog.dialogWarning("", "Antes de continuar, debes proporcionar tus credenciales.");
        } else {
            if(bdate.equalsIgnoreCase("")){
                dialog.dialogWarning("", "Debes proporcionar tu fecha de nacimiento.");
            }else{
                boolean connection = validate.connect();
                if (connection) {
                    update(fname, lname, bdate);
                } else {
                    dialog.dialogWarning("Error de conexión", "No se pudo detectar una conexión estable a internet.");
                }
            }
        }
    }

    public void update(final String fname, final String lname, final String bdate){
        dialog.dialogProgress("Actualizando información...");
        final String url = getString(R.string.url_con);
        int user_id = userController.show().getUser_id();
        String token = userController.show().getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.updateUser(token, user_id, fname, lname, bdate, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    User user = userController.show();
                    user.setFirst_name(fname);
                    user.setLast_name(lname);
                    user.setBirth_date(bdate);
                    user.setState(1);
                    if(userController.update(user)){
                        String token = jsonObject.get("new_token").getAsString();
                        if(userController.changeToken(token)) {
                            dialog.cancelarProgress();
                            next();
                        }
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
                    Log.e("Update(update)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Update(update)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void next(){
        Intent timeLine = new Intent(Update.this, Timeline.class);
        startActivity(timeLine);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void loadImageProfile(){
        Picasso.with(context)
                .load(R.drawable.background_collapsing)
                .transform(new BlurTransformation(context, 25, 1))
                .centerCrop()
                .fit()
                .into(img_user);
    }

}

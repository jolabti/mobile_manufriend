package xyz.jncode.manufriend;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.jncode.manufriend.Share.Api;
import xyz.jncode.manufriend.Share.Function;
import xyz.jncode.manufriend.Util.SessionManager;
import xyz.jncode.manufriend.Util.Tools;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

public class EditProfilActivity extends AppCompatActivity {
    EditText edNama, edEmail, edPassword, edConfirmPassword, edNoTelp, edAlamat, edTanggalLahir;
    Button submit;
    RadioButton rbPria, rbWanita;
    Calendar myCalendar;
    SessionManager sessionManager;
    String gender;

    boolean clearall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        sessionManager = new SessionManager(this);
        declareComponents();
        starRequestEditProfilDatas();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        edTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfilActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        rbPria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbWanita.isChecked()) {
                    rbWanita.setChecked(false);
                }
            }
        });

        rbWanita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbPria.isChecked()) {
                    rbPria.setChecked(false);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditProfile();
            }
        });
    }

    public void declareComponents() {
        myCalendar = Calendar.getInstance();
        edNama = findViewById(R.id.name);
        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        edConfirmPassword = findViewById(R.id.konfirmPassword);
        edNoTelp = findViewById(R.id.telepon);
        edAlamat = findViewById(R.id.alamat);
        edTanggalLahir = findViewById(R.id.dateBorn);
        rbPria = findViewById(R.id.rbLaki);
        rbWanita = findViewById(R.id.rbPerempuan);
        submit = findViewById(R.id.btn_save_edit);


    }


    public void updateLabel() {


        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edTanggalLahir.setText(sdf.format(myCalendar.getTime()));

    }

    public boolean checkRulesComponent(){

        if(edNama.getText().toString().length()<1){
            edNama.setError("Nama belum diisi");
            clearall =false;
        }
        else if(!Tools.validateEmail(edEmail.getText().toString())){
            edEmail.setError("Email belum diisi ");
            clearall =false;
        }

        else if(!edPassword.getText().toString().equals(edConfirmPassword.getText().toString())){
            edPassword.setError("Password tidak sesuai");
            edConfirmPassword.setError("Password tidak sesuai");
            clearall =false;
        }
        else if(edNoTelp.getText().toString().length()<1){
            clearall =false;

            edNoTelp.setError("Nomor telepon belum diisi");
        }
        else if(edAlamat.getText().toString().length()<1){
            edAlamat.setError("Alamat belum diisi");
            clearall =false;

        }
        else if(edTanggalLahir.getText().toString().length()<1){
            clearall =false;

            edTanggalLahir.setError("Alamat belum diisi");

        }
        else{
            clearall=true;
        }

        return clearall;


    }


    public void starRequestEditProfilDatas() {

        //sessionManager.getUserDetails().get(Api.PARAM_ID_USER);

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.GET_USER_BY_ID + sessionManager.getUserDetails().get(Api.PARAM_ID_USER)) //iduser
                .tag("GETUSER")
                .addHeader("application/json", "charset=utf-8")
                //.addHeader("Content-Type","application/x-www-form-urlencoded")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        BaseOkHttpClient.cancelRequest("GETUSER");
        BaseOkHttpClient.getInstance(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jobRoot = new JSONObject(response.body().string());
                                Log.d("trace_jobroot", jobRoot.toString());
//                                bindingSettingTv(
//                                        jobRoot.getString("nama_user"),
//                                        jobRoot.getString("gender_user"),
//                                        jobRoot.getString("email_user"),
//                                        jobRoot.getString("alamat_user"),
//                                        jobRoot.getString("telepon_user"),
//                                        jobRoot.getString("ttl_user"));

                                edNama.setText(jobRoot.getString("nama_user"));
                                edEmail.setText(jobRoot.getString("email_user"));
                                edPassword.setText(jobRoot.getString("password_user"));
                                edConfirmPassword.setText(jobRoot.getString("password_user"));
                                edNoTelp.setText(jobRoot.getString("telepon_user"));
                                edAlamat.setText(jobRoot.getString("alamat_user"));
                                edTanggalLahir.setText(jobRoot.getString("ttl_user"));

                                if (jobRoot.getString("gender_user").equals("pria")) {
                                    rbPria.setChecked(true);
                                    rbWanita.setChecked(false);
                                 } else {
                                    rbWanita.setChecked(true);
                                    rbPria.setChecked(false);
                                 }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }

            }
        });


    }

    public void saveEditProfile() {

        if (rbPria.isChecked()) {
            gender = "pria";
        } else if (rbWanita.isChecked()) {
            gender = "wanita";
        }

        final RequestBody requestBody = new FormBody.Builder()
                .add("nama_user", edNama.getText().toString())
                .add("email_user", edEmail.getText().toString())
                .add("password_user", edConfirmPassword.getText().toString())
                .add("telepon_user", edNoTelp.getText().toString())
                .add("alamat_user", edAlamat.getText().toString())
                .add("gender_user", gender)
                .build();

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.POST_UPDATE_USER+ sessionManager.getUserDetails().get(Api.PARAM_ID_USER))
                .tag("TAG_SAVE_EDIT")
                .post(requestBody)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        //Jika terjadi sesuatu dengan server, maka akan lebih mudah penanganannya
        BaseOkHttpClient.cancelRequest("TAG_SAVE_EDIT");
        BaseOkHttpClient.getInstance(getApplicationContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Berhasil Diubah", Toast.LENGTH_LONG).show();
                            Function.prog_dialog(EditProfilActivity.this, "Mengubah data...", response.code());
                            starRequestEditProfilDatas();

                        }
                    });
                }

            }
        });

    }


}

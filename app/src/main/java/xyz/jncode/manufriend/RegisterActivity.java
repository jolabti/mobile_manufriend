package xyz.jncode.manufriend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import xyz.jncode.manufriend.Util.Tools;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

public class RegisterActivity extends AppCompatActivity {
    EditText edNama,edEmail, edPassword, edConfirmPassword,edNoTelp,edAlamat, edTanggalLahir;
    RadioButton rbPria, rbWanita;
    Button submit;

    Calendar myCalendar;
    String gender;

    boolean clearall;

    public String TAG_REGISTER = "register";
    DatePickerDialog dialogDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        declareComponents();

        /*final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };*/

       /* edTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/

            dialogDate = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Todo your work here

                edTanggalLahir.setText(Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(day));
            }
        }, 94, 8, 24);


            edTanggalLahir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialogDate.show();
                }
            });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkRulesComponent()==true){
                    startRequestRegister();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Belum dapat diproses", Toast.LENGTH_LONG).show();
                }
            }
        });

        rbPria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbWanita.isChecked()){
                    rbWanita.setChecked(false);
                }
            }
        });

        rbWanita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbPria.isChecked()){
                    rbPria.setChecked(false);
                }
            }
        });
    }

    public void declareComponents(){
        myCalendar = Calendar.getInstance();
        edNama = findViewById(R.id.name);
        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        edConfirmPassword = findViewById(R.id.konfirmPassword);
        edNoTelp = findViewById(R.id.telepon);
        edAlamat= findViewById(R.id.name);
        edTanggalLahir = findViewById(R.id.dateBornRegister);
        rbPria = findViewById(R.id.rbLaki);
        rbWanita = findViewById(R.id.rbPerempuan);
        submit = findViewById(R.id.btn_register);


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
            edTanggalLahir.setError("Tanggal lahir belum diisi");
        }
        else{
            clearall=true;
        }

        return clearall;


    }


    /*public void updateLabel() {


        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edTanggalLahir.setText(sdf.format(myCalendar.getTime()));

    }*/

    public void startRequestRegister(){


        if(rbPria.isChecked()){
            gender="pria";
        }
        else if(rbWanita.isChecked()){
            gender="wanita";
        }


        Log.d("trace_ttl", edTanggalLahir.getText().toString());
        final RequestBody requestBody = new FormBody.Builder()
                .add("nama_user", edNama.getText().toString())
                .add("email_user", edEmail.getText().toString())
                .add("password_user", edConfirmPassword.getText().toString())
                .add("telepon_user", edNoTelp.getText().toString())
                .add("alamat_user", edAlamat.getText().toString())
                .add("ttl_user", edTanggalLahir.getText().toString())
                .add("gender_user", gender)
                .build();

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.POST_REGISTER)
                .tag(TAG_REGISTER)
                .post(requestBody)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        //Jika terjadi sesuatu dengan server, maka akan lebih mudah penanganannya
        BaseOkHttpClient.cancelRequest(TAG_REGISTER);
        BaseOkHttpClient.getInstance(getApplicationContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if(response.isSuccessful()){

                    Log.d("trace_reg_suc", response.body().string());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                                Toast.makeText(getApplicationContext(),"Berhasil Terdaftar, Silakan Login", Toast.LENGTH_LONG).show();
                                Intent gotoLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(gotoLogin);
                        }
                    });
                }
            }
        });
    }
}

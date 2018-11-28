package xyz.jncode.manufriend;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.jncode.manufriend.Share.Api;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

public class DetailRiwayatActivity extends AppCompatActivity {
    String tampungID, tampungLabel;
    String tempHarga, tempWaktu, tempTanggal, tempDurasi, tempCatatan, tempNamaService;


    String idService;
    int hitungTotalHarga;


    TextView tvDurasi, tvHargaService, tvLabelPrice, tvLabelService;

    EditText ed_tanggal, ed_notes, ed_pukul;
    Calendar myCalendar;
    int crementor, totalPrice;
    Button btnPlus, btnMinus, btnPesan;
    TextWatcher priceWatcher;
    Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        extras = getIntent().getExtras();

        ed_tanggal = findViewById(R.id.date_detail);
        ed_notes = findViewById(R.id.notes_detail);
        ed_pukul = findViewById(R.id.time_detail);

        btnPlus = findViewById(R.id.plusButton);
        btnMinus = findViewById(R.id.minusButton);
        btnPesan = findViewById(R.id.btnPesan);

        tvDurasi = findViewById(R.id.quantity_textview);
        tvHargaService = findViewById(R.id.labelHarga);
        tvLabelPrice = findViewById(R.id.id_label_price);
        tvLabelService = findViewById(R.id.labelService);

        myCalendar = Calendar.getInstance();

        ed_pukul.setCursorVisible(false);
        ed_pukul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DetailRiwayatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed_pukul.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        TextWatcher twDurasiCounter = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() != 0) {

                    Log.d("hasilTW", s.toString());
                    hitungTotalHarga = Integer.parseInt(tempHarga) * Integer.parseInt(s.toString());

                    if (hitungTotalHarga == 0) {
                        tvLabelPrice.setTextColor(getResources().getColor(R.color.manu_red));
                        tvLabelPrice.setText("Lama durasi tidak boleh kosong");
                    } else {
                        tvLabelPrice.setTextColor(getResources().getColor(R.color.manu_black));
                        tvLabelPrice.setText("Rp" + " " + Integer.toString(hitungTotalHarga));

                    }

                }


            }
        };


        tvDurasi.addTextChangedListener(twDurasiCounter);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        ed_tanggal.setCursorVisible(false);
        ed_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(DetailRiwayatActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crementor++;

                if (crementor < 0) {
                    crementor = 0;
                }
                tvDurasi.setText(Integer.toString(crementor));


            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crementor--;

                if (crementor < 0) {
                    crementor = 0;
                }

                tvDurasi.setText(Integer.toString(crementor));

            }
        });


        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectandIntentData();
            }
        });


        //===================================================================

        if (extras == null) {
            tampungID = null;
        } else {
            tampungID = extras.getString("idservice");
            tampungLabel = extras.getString("labelsProduct");
            tvLabelService.setText(tampungLabel);
            Log.d("cek_tampungID", tampungID);
            startRequesrPrice(tampungID);
        }


//        totalPrice = Integer.parseInt(tempHarga) * crementor;


    }

    public void updateLabel() {


        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_tanggal.setText(sdf.format(myCalendar.getTime()));

    }


    public void startRequesrPrice(String myIDService) {

        idService  = myIDService;

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.GET_PRICE_SERVICE + myIDService)
                .tag("SERVICEHARGA")
                .addHeader("application/json", "charset=utf-8")
                //.addHeader("Content-Type","application/x-www-form-urlencoded")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        BaseOkHttpClient.cancelRequest("SERVICEHARGA");
        BaseOkHttpClient.getInstance(getApplicationContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("statusres", "Gagal server");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()) {


                    Log.d("statusres", "Berhasil");
                    //Log.d("statusjson", response.body().string()); //hanya boleh dipakai 1 x

                    try {
                        JSONObject jobRoot = new JSONObject(response.body().string());

                        JSONObject jobData = jobRoot.getJSONObject("data");

                        Log.d("id_service_job", jobData.getString("id_service"));

                        Log.d("nama_service_job", jobData.getString("nama_service"));
                        Log.d("harga_service_job", jobData.getString("harga_service"));

                        tempNamaService = jobData.getString("nama_service");
                        tempHarga = jobData.getString("harga_service");


                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                tvHargaService.setText("Rp. " + tempHarga + "/jam");

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } else if (!response.isSuccessful()) {

                    Log.d("statusres", "Gagal");
                }

            }
        });
    }


    public void collectandIntentData() {

        tempCatatan = ed_notes.getText().toString();
        tempDurasi = tvDurasi.getText().toString();
        tempTanggal = ed_tanggal.getText().toString();
        tempWaktu = ed_pukul.getText().toString();


        Log.d("showtCatatan", tempCatatan);
        Log.d("showtDurasi", tempDurasi);
        Log.d("showtTanggal", tempTanggal);
        Log.d("showtWaktu", tempWaktu);

        Intent gotoRincian = new Intent(DetailRiwayatActivity.this, RincianPemesananActivity.class);
        gotoRincian.putExtra("showIDService", idService);
        gotoRincian.putExtra("showtCatatan", tempCatatan);
        gotoRincian.putExtra("showtDurasi", tempDurasi);
        gotoRincian.putExtra("showtTanggal", tempTanggal);
        gotoRincian.putExtra("showtHarga", String.valueOf(hitungTotalHarga));
        gotoRincian.putExtra("showwWaktu", tempWaktu);
        gotoRincian.putExtra("showLabelProduct", extras.getString("labelsProduct"));
        gotoRincian.putExtra("showDefaultStatus", "Request");
        gotoRincian.putExtra("showDefaultNamaService", tempNamaService);

        startActivity(gotoRincian);


    }
}

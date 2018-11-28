package xyz.jncode.manufriend;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.jncode.manufriend.Fragment.FragmentRiwayat;
import xyz.jncode.manufriend.Share.Api;

import xyz.jncode.manufriend.Share.Function;
import xyz.jncode.manufriend.Util.SessionManager;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

public class RincianPemesananActivity extends AppCompatActivity {

    LinearLayout llRincian;
    TextView tvStatus, tvService, tvTanggal, tvDurasi, tvAlamat, tvAgen, tvTotal, tvPembayaran;
    String status, service, tanggal, durasi, alamat, agen, total, pembayaran;
    SessionManager sessionManager;

    ProgressDialog progressDialog;

    Button btnSelesai;

    Bundle extras ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat);
        sessionManager = new SessionManager(RincianPemesananActivity.this);
        llRincian = findViewById(R.id.id_all_rincian);
        progressDialog = new ProgressDialog(RincianPemesananActivity.this);



        extras = getIntent().getExtras();

        btnSelesai = findViewById(R.id.btn_selesai_rincian);

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequestPostTransaction();
            }
        });

        tvStatus = findViewById(R.id.status_tv);
        tvService = findViewById(R.id.service_tv);
        tvTanggal = findViewById(R.id.date_tv);
        tvDurasi = findViewById(R.id.duration_tv);
        tvAlamat = findViewById(R.id.address_tv);
        tvAgen = findViewById(R.id.agen_tv);
        tvTotal = findViewById(R.id.total_tv);
        tvPembayaran = findViewById(R.id.pembayaran_tv);

        durasi = extras.getString("showtDurasi");
        tanggal = extras.getString("showtTanggal");

        /*
           gotoRincian.putExtra("showtCatatan", tempCatatan);
        gotoRincian.putExtra("showtDurasi", tempDurasi);
        gotoRincian.putExtra("showtTanggal", tempTanggal);
        gotoRincian.putExtra("showwWaktu", tempWaktu);
        gotoRincian.putExtra("showLabelProduct", extras.getString("labelsProduct"));
        gotoRincian.putExtra("showDefaultStatus", "Request");


            $dataParsing = array(
          "id_user"=>"",

          "id_user"=>$this->post('id_user'),
          "id_service"=>$this->post('id_service'),
          "id_status"=>$this->post('id_status'),
          "tanggal_order"=>$this->post('tanggal_order'),
          "pukul_trx"=>$this->post('pukul_trx'),
          "durasi"=>$this->post('durasi'),
          "total_harga"=>$this->post('total_harga'),
          "notes"=>$this->post('notes')


         */

        Log.d("track_durasi", durasi);
        Log.d("track_tanggal", tanggal);
        Log.d("track_session_email", sessionManager.getUserDetails().get("email"));


        tvDurasi.setText(durasi);
        tvTanggal.setText(tanggal);
        tvStatus.setText(extras.getString("showDefaultStatus"));
        //tvService.setText(extras.getString("showLabelProduct"));
        tvTotal.setText( extras.getString("showtHarga"));
        tvService.setText( extras.getString("showDefaultNamaService"));


        //startRequesrPrice(tampungID);


    }

    private void startRequestPostTransaction(){

        //Function.prog_dialog(RincianPemesananActivity.this,"Memuat transaksi...");

        final RequestBody requestBody = new FormBody.Builder()
                .add("id_user",  sessionManager.getUserDetails().get(Api.PARAM_ID_USER))
                .add("id_service", extras.getString("showIDService"))
                .add("id_status", "1")
                .add("pukul_trx", extras.getString("showwWaktu"))
                .add("durasi", extras.getString("showtDurasi"))
                .add("tanggal_order", extras.getString("showtTanggal"))
                .add("total_harga", extras.getString("showtHarga"))
                .add("notes", extras.getString("showtCatatan"))
                .build();

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.POST_TRX)
                .tag("trx_post")
                .post(requestBody)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        //Jika terjadi sesuatu dengan server, maka akan lebih mudah penanganannya
        BaseOkHttpClient.cancelRequest("trx_post");
        BaseOkHttpClient.getInstance(getApplicationContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Log.d("trace_response_trx", response.body().string());

                if(response.isSuccessful()){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //progressDialog.dismiss();
                            //progressDialog.cancel();



                            Function.prog_dialog(RincianPemesananActivity.this,"Memuat transaksi...",response.code());


                            final Fragment fragment = FragmentRiwayat.newInstance();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.layout_main, fragment).commit();
                            setupChangeViewFragment(fragment);
                        }
                    });
                }
            }
        });


    }

    public void setupChangeViewFragment(Fragment frm) {

        llRincian.setVisibility(View.GONE);

        FragmentTransaction transaction = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.layout_main, frm);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    @Override
//    public void onBackPressed() {
//
//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }
//
//    }


}

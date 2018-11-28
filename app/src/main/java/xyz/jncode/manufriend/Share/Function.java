package xyz.jncode.manufriend.Share;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.jncode.manufriend.Fragment.FragmentRiwayat;
import xyz.jncode.manufriend.LoginActivity;
import xyz.jncode.manufriend.MainActivity;
import xyz.jncode.manufriend.R;
import xyz.jncode.manufriend.RegisterActivity;
import xyz.jncode.manufriend.RincianPemesananActivity;
import xyz.jncode.manufriend.Util.SessionManager;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

//import android.support.v4.app.FragmentManager;

public class Function {


    public static void processAction(Activity activity, String target, String passIdSurat) {

        if (target.equals(Api.PARAM_TARGET_RIWAYAT)) {



               Fragment riwayatFragment= FragmentRiwayat.newInstance();
                trx_fragment(activity,riwayatFragment);



        }

    }


    public static void trx_fragment(Activity activity, Fragment frm){

        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.layout_main, frm);
       // transaction.addToBackStack(null);
        transaction.commit();

    }

    public static void prog_dialog(Activity activity, String message, int responseCode){

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(message);
        //progressDialog.setTitle("ProgressDialog bar example");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        if(responseCode==200){
            progressDialog.dismiss();
        }
        else{
            progressDialog.show();
        }
    }

    public static void ctm_dialog_riwayat(Activity activity, String titleDialog){
        final Dialog dlCtm = new Dialog(activity);
        dlCtm.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        dlCtm.setCancelable(false);
        dlCtm.setTitle(titleDialog);
        dlCtm.setContentView(R.layout.custom_dialog_riwayat);
        dlCtm.setCanceledOnTouchOutside(true);
    }


    public static void ctm_dialog(final Activity activity, String idRecordTrx, final String titleDialog){


        final SessionManager sessionManager;
        sessionManager= new SessionManager(activity);



        if(activity!=null){

            final okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(Api.GET_ITEM_RIWAYAT + idRecordTrx) //iduser
                    .tag("trx")
                    .addHeader("application/json", "charset=utf-8")
                    //.addHeader("Content-Type","application/x-www-form-urlencoded")
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();

            BaseOkHttpClient.cancelRequest("trx");
            BaseOkHttpClient.getInstance(activity).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    if(e.getMessage()!=""){
                        sessionManager.logoutUser();
                    }
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                      if(response.isSuccessful()){

                          try {
                              JSONObject jsonObject = new JSONObject(response.body().string());
                              final String harga = jsonObject.getString("total_harga");
                              final String service = jsonObject.getString("nama_service");
                              final String waktu = jsonObject.getString("tanggal_trx");

                              new Handler(Looper.getMainLooper()).post(new Runnable() {
                                  @Override
                                  public void run() {

                                      //TextView tv_ctm_service, tv_ctm_harga,tv_ctm_waktu;

                                      final Dialog dlCtm = new Dialog(activity);
                                      dlCtm.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                                      dlCtm.setCancelable(false);
                                      dlCtm.setTitle(titleDialog);
                                      dlCtm.setContentView(R.layout.custom_dialog_riwayat);
                                      dlCtm.setCanceledOnTouchOutside(true);

                                      final Button  btnClose  = dlCtm.findViewById(R.id.ctm_dialog_button_close);
                                      final TextView tv_ctm_service =   dlCtm.findViewById(R.id.ctm_dialog_service);
                                      final TextView tv_ctm_waktu = dlCtm.findViewById(R.id.ctm_dialog_waktu);
                                      final TextView tv_ctm_harga= dlCtm.findViewById(R.id.ctm_dialog_harga);

                                      tv_ctm_service.setText(service);
                                      tv_ctm_waktu.setText(waktu);
                                      tv_ctm_harga.setText(harga);

                                      dlCtm.show();

                                      btnClose.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              dlCtm.dismiss();
                                          }
                                      });
                                  }
                              });




                          } catch (Exception e) {
                              e.printStackTrace();
                          }

                      }
                      else{

                          new Handler(Looper.getMainLooper()).post(new Runnable() {
                              @Override
                              public void run() {

                                  Toast.makeText(activity,"Gagal mengambil detail riwayat", Toast.LENGTH_LONG).show();

                              }
                          });
                      }


//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            //TextView tv_ctm_service, tv_ctm_harga,tv_ctm_waktu;
//
//                            final Dialog dlCtm = new Dialog(activity);
//                            dlCtm.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//                            dlCtm.setCancelable(false);
//                            dlCtm.setTitle(titleDialog);
//                            dlCtm.setContentView(R.layout.custom_dialog_riwayat);
//                            dlCtm.setCanceledOnTouchOutside(false);
//
//                            final Button  btnClose  = dlCtm.findViewById(R.id.ctm_dialog_button_close);
//                            final TextView tv_ctm_service =   dlCtm.findViewById(R.id.ctm_dialog_service);
//                            final TextView tv_ctm_waktu = dlCtm.findViewById(R.id.ctm_dialog_waktu);
//                            final TextView tv_ctm_harga= dlCtm.findViewById(R.id.ctm_dialog_harga);
//                        }
//                    });

                }
            });
        }



    }
}

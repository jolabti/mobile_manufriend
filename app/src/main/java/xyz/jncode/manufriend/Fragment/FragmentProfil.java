package xyz.jncode.manufriend.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.jncode.manufriend.BantuanActivity;
import xyz.jncode.manufriend.EditProfilActivity;
import xyz.jncode.manufriend.R;
import xyz.jncode.manufriend.Share.Api;
import xyz.jncode.manufriend.Util.SessionManager;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

public class FragmentProfil extends Fragment {
    Button btnEdit, btnKeluar, btnBantuan;
    TextView tvNamauser,tvGender, tvEmail, tvAlamat,tvTelepon,tvBirthdate;
    SessionManager sessionManager;

    public static FragmentProfil newInstance() {
        FragmentProfil fragment = new FragmentProfil();
        // Fragment fragment = PostingFragment.newInstance();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        tvNamauser = rootView.findViewById(R.id.profile_nama_user);
        tvGender = rootView.findViewById(R.id.profile_gender);
        tvEmail = rootView.findViewById(R.id.profile_email);
        tvAlamat= rootView.findViewById(R.id.profile_alamat);
        tvTelepon = rootView.findViewById(R.id.profile_telepon);
        tvBirthdate= rootView.findViewById(R.id.profile_bornday);


        btnKeluar = rootView.findViewById(R.id.keluar_btn);
        btnEdit = rootView.findViewById(R.id.edit_btn);
        btnBantuan = rootView.findViewById(R.id.bantuan_btn);

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("Apakah Anda yakin keluar?");
//                builder.setCancelable(true);
//                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        getActivity().finish();
//                        System.exit(0);
//                    }
//                });
//                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });

                sessionManager.logoutUser();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoEditProfil = new Intent(getActivity(), EditProfilActivity.class);
                startActivity(gotoEditProfil);
            }
        });

        btnBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoBantuan = new Intent(getActivity(), BantuanActivity.class);
                startActivity(gotoBantuan);
            }
        });


        startRequestProfile();

        return rootView;
    }

    //BELUM JALAN
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void startRequestProfile(){

        //sessionManager.getUserDetails().get(Api.PARAM_ID_USER);

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.GET_USER_BY_ID + sessionManager.getUserDetails().get(Api.PARAM_ID_USER)) //iduser
                .tag("GETUSER")
                .addHeader("application/json", "charset=utf-8")
                //.addHeader("Content-Type","application/x-www-form-urlencoded")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        BaseOkHttpClient.cancelRequest("GETUSER");
        BaseOkHttpClient.getInstance(getActivity()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(response.isSuccessful()){


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jobRoot = new JSONObject(response.body().string());

                                bindingSettingTv(
                                        jobRoot.getString("nama_user"),
                                        jobRoot.getString("gender_user"),
                                        jobRoot.getString("email_user"),
                                        jobRoot.getString("alamat_user"),
                                        jobRoot.getString("telepon_user"),
                                        jobRoot.getString("ttl_user"));

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

    public void bindingSettingTv(String namaUser,String gender, String email, String alamat, String telepon, String tanggalLahir){

        tvNamauser.setText(namaUser);
        tvGender.setText(gender);
        tvEmail.setText(email);
        tvAlamat.setText(alamat);
        tvTelepon.setText(telepon);
        tvBirthdate.setText(tanggalLahir);
    }
}

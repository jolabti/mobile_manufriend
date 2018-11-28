package xyz.jncode.manufriend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import GSON.GSONLogin;
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

public class LoginActivity extends AppCompatActivity {
    public String varMessage;
    public SessionManager sessionManager;
    Button btnLogin, btnRegister;
    AutoCompleteTextView ac_email, ac_password;
    String TAG_LOGIN = "thislogin";
    String myEmail, myPassword;
    GSONLogin gson;

    ProgressDialog progressDialog;


    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(getApplicationContext());

        if(sessionManager.isLoggedIn()){
            Intent gotoMain = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(gotoMain);
        }

        btnLogin = findViewById(R.id.lg_btn_login);
        btnRegister = findViewById(R.id.lg_btn_register);
        ac_email = findViewById(R.id.userEmail);
        ac_password = findViewById(R.id.userPassword);


        progressDialog = new ProgressDialog(LoginActivity.this);

        progressDialog.setMessage("Authenticating ...");
        //progressDialog.setTitle("ProgressDialog bar example");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myEmail = ac_email.getText().toString();
                myPassword = ac_password.getText().toString();

                if (myEmail.length() < 1 || myPassword.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Form autentikasi ada yang kosong", Toast.LENGTH_SHORT).show();
                } else if (!Tools.validateEmail(myEmail)) {
                    ac_email.setError("format email tidak sesuai");
                } else if (myPassword.length() < 6) {
                    ac_password.setError("password kurang dari ketentuan");
                } else {
                    Log.d("test", "akan diisi start request api login");
                    startRequestApiLogin(myEmail.trim(), myPassword.trim());
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(gotoRegister);
            }
        });

    }

    public void startRequestApiLogin(String email, String password) {

        //progressDialog.show();

        final RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.GET_LOGIN)
                .tag(TAG_LOGIN)
                .post(requestBody)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        //Jika terjadi sesuatu dengan server, maka akan lebih mudah penanganannya
        BaseOkHttpClient.cancelRequest(TAG_LOGIN);
        BaseOkHttpClient.getInstance(getApplicationContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("cek_koneksi", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {

                    //sessionManager.logoutUser();
                    backgroundThreadShortToast(getApplicationContext(), "Gagal login");
                    //Toast.makeText(getApplicationContext(), "Gagal authentikasi", Toast.LENGTH_SHORT).show();


                } else {

                    //Log.d("responlogin", response.body().string());
                    //progressDialog.dismiss();
                    try {
                        JSONObject jobRoot = new JSONObject(response.body().string());

                        if (jobRoot.has("email")) {

                            if (jobRoot.getString("email") != "") {


                                sessionManager.createLoginSession(jobRoot.getString("name"), jobRoot.getString("email"),jobRoot.getString("id_user"));
                                backgroundThreadShortToast(getApplicationContext(), "Berhasil Login");


                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Function.prog_dialog(LoginActivity.this,"Authenticating...",response.code());
                                        Intent gotoMain = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(gotoMain);

                                    }
                                });





                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}

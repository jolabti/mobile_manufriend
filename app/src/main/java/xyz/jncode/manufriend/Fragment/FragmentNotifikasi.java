package xyz.jncode.manufriend.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.NotifikasiAdapter;
import Adapter.RiwayatAdapter;
import Model.Notifikasimodel;
import Model.Riwayatmodel;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.jncode.manufriend.R;
import xyz.jncode.manufriend.Share.Api;
import xyz.jncode.manufriend.Util.SessionManager;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

public class FragmentNotifikasi extends Fragment {


    RecyclerView rvNotifikasi;
    CardView cvNotifikasi;
    SessionManager sessionManager;

    List<Notifikasimodel> notifList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    public NotifikasiAdapter adapter;

    public static FragmentNotifikasi newInstance() {
        FragmentNotifikasi fragment = new FragmentNotifikasi();
        // Fragment fragment = PostingFragment.newInstance();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
        notifList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifikasi, container, false);
        rvNotifikasi = rootView.findViewById(R.id.rv_notifikasi);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startRequestApiNotifikasi();


    }

    public void startRequestApiNotifikasi(){

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.GET_NOTIFIKASI + sessionManager.getUserDetails().get(Api.PARAM_ID_USER))
                .tag(Api.PARAM_ID_USER)
                .addHeader("application/json", "charset=utf-8")
                //.addHeader("Content-Type","application/x-www-form-urlencoded")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        BaseOkHttpClient.cancelRequest(Api.PARAM_ID_USER);
        BaseOkHttpClient.getInstance(getActivity()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e.getMessage()!=""){
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //Log.d("trace_notifikasi", response.body().string());



                try {
                    JSONArray jarRoot = new JSONArray(response.body().string());

                    notifList.clear();


                    for(int i=0; i< jarRoot.length();i++){

                        JSONObject job = new JSONObject(jarRoot.get(i).toString());

                        Log.d("trace_job_names", job.getString("nama_service"));
                        Log.d("trace_job_tgl", job.getString("tanggal_trx"));

                        Notifikasimodel  rmnotif =  new Notifikasimodel(job.getString("tanggal_trx"),job.getString("nama_service"),"Telah disetujui");
                        notifList.add(rmnotif);


                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                            rvNotifikasi.setHasFixedSize(true);
                            rvNotifikasi.setLayoutManager(linearLayoutManager);

                            adapter = new NotifikasiAdapter(getActivity(),notifList);
                            rvNotifikasi.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            rvNotifikasi.notify();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


}

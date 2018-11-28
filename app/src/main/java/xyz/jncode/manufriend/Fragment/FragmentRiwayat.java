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
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.RiwayatAdapter;
import Model.Riwayatmodel;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.jncode.manufriend.R;
import xyz.jncode.manufriend.Share.Api;
import xyz.jncode.manufriend.Share.Function;
import xyz.jncode.manufriend.Util.SessionManager;
import xyz.jncode.manufriend.base.BaseOkHttpClient;

public class FragmentRiwayat extends Fragment {
    SessionManager sessionManager;
    String resultStatus;

    List<Riwayatmodel> riwayatList;

    RecyclerView mList;
    CardView cvRiwayat;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    public RiwayatAdapter adapter;


    public static FragmentRiwayat newInstance() {
        FragmentRiwayat fragment = new FragmentRiwayat();
        // Fragment fragment = PostingFragment.newInstance();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());

        riwayatList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_riwayat, container, false);

        //dividerItemDecoration = new DividerItemDecoration(mList.getAct(), linearLayoutManager.getOrientation());
        mList = rootView.findViewById(R.id.rv_riwayat);
        cvRiwayat = rootView.findViewById(R.id.cv_riwayat);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startRequestApiRiwayat();





    }

    private void startRequestApiRiwayat(){

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Api.GET_RIWAYAT + sessionManager.getUserDetails().get(Api.PARAM_ID_USER))
                .tag(Api.PARAM_ID_USER)
                .addHeader("application/json", "charset=utf-8")
                //.addHeader("Content-Type","application/x-www-form-urlencoded")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        BaseOkHttpClient.cancelRequest(Api.PARAM_ID_USER);
        BaseOkHttpClient.getInstance(getActivity()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (e.getMessage()!=""){
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                    if (response.isSuccessful()){

                        //Log.d("trace_riwayat", response.body().string());
                        try {

                            JSONArray jarRoot = new JSONArray(response.body().string());
                            Log.d("trace_jrootlen", String.valueOf(jarRoot.length()));

                            riwayatList.clear();

                            for (int i=0;i<jarRoot.length();i++){

                                JSONObject job1 = new JSONObject(jarRoot.get(i).toString());
                                Log.d("trace_jobemail", job1.getString("email_user"));
                                Log.d("trace_namaservice", job1.getString("nama_service"));
                                Log.d("trace_notes", job1.getString("notes"));



                                Riwayatmodel rmodel = new Riwayatmodel(job1.getString("nama_service"),job1.getString("tanggal_trx"),job1.getString("notes"),job1.getString("id_status"),job1.getString("id_trx"));

                                riwayatList.add(rmodel);



                            }

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    Function.prog_dialog(getActivity(),"Memuat riwayat ...", response.code());

                                    linearLayoutManager = new LinearLayoutManager(getActivity());
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                                    mList.setHasFixedSize(true);
                                    mList.setLayoutManager(linearLayoutManager);
                                    //mList.addItemDecoration(dividerItemDecoration);

                                    adapter = new RiwayatAdapter(getActivity(),riwayatList);

                                    mList.setAdapter(adapter);

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{

                        Log.d("trace_riwayat", String.valueOf(response.code()));

                    }
            }
        });


    }


}

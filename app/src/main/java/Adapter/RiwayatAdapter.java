package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Model.Riwayatmodel;
import xyz.jncode.manufriend.R;
import xyz.jncode.manufriend.Share.Function;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    @NonNull

    private Context context;
    private List<Riwayatmodel> list;
    String resultStatus;
    int colors ;
    public RiwayatAdapter(Context context, List<Riwayatmodel> list) {
        this.context = context;
        this.list = list;
    }


    public RiwayatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_riwayat, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.ViewHolder holder, int position) {

        final Riwayatmodel rwmodel = list.get(position);
        Log.d("trace_status",rwmodel.getStatusRequest());
        switch (rwmodel.getStatusRequest()){

            case "1":
                resultStatus="Sedang diproses";
                //Log.d("trace_rstatus", resultStatus);
                colors = R.color.manu_grey;


                break;
            case "2":
                resultStatus="Disetujui";
                //Log.d("trace_rstatus", resultStatus);
                colors = R.color.manu_blue;
                //holder.cvRiwayat.setCardBackgroundColor(R.color.manu_blue);


                break;
            case "3":
                resultStatus="Gagal dipenuhi";
                //Log.d("trace_rstatus", resultStatus);
                colors = R.color.manu_red;
                //holder.cvRiwayat.setCardBackgroundColor(R.color.manu_red);

                break;


        }


        holder.textService.setText(rwmodel.getNamaService());
        holder.textStatus.setText(resultStatus);
        holder.textTanggal.setText(rwmodel.getWaktuService());
        holder.textNotes.setText(rwmodel.getNoteService());




        holder.cvRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("trace_id_record", rwmodel.getIdRecordServices());
                Function.ctm_dialog((Activity) context, rwmodel.getIdRecordServices(),"");
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textService, textStatus, textTanggal, textNotes;
        public CardView cvRiwayat;

        public ViewHolder(View view) {
            super(view);

            textService = view.findViewById(R.id.custom_riwayat_service);
            textStatus = view.findViewById(R.id.custom_riwayat_status);
            textTanggal = view.findViewById(R.id.custom_riwayat_tanggal);
            textNotes = view.findViewById(R.id.custom_riwayat_notes);

            cvRiwayat = view.findViewById(R.id.cv_riwayat);

        }
    }
}

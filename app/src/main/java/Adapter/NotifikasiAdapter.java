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

import Model.Notifikasimodel;
import Model.Riwayatmodel;
import xyz.jncode.manufriend.R;
import xyz.jncode.manufriend.Share.Function;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {
    @NonNull

    private Context context;
    private List<Notifikasimodel> list;
    String resultStatus;
    int colors ;
    public NotifikasiAdapter(Context context, List<Notifikasimodel> list) {
        this.context = context;
        this.list = list;
    }


    public NotifikasiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_notifikasi, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NotifikasiAdapter.ViewHolder holder, int position) {

        final Notifikasimodel nm_model = list.get(position);
        holder.textService.setText(nm_model.getNamaService());
        holder.textTanggal.setText(nm_model.getTglTrx());
        holder.textStatus.setText("Berhasil diproses ....");


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

            textService = view.findViewById(R.id.custom_notifikasi_service);
            textStatus = view.findViewById(R.id.custom_notifikasi_status);
            textTanggal = view.findViewById(R.id.custom_notifikasi_tanggal);


            cvRiwayat = view.findViewById(R.id.cv_riwayat);

        }
    }
}

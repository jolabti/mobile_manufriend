package xyz.jncode.manufriend.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jncode.manufriend.DetailRiwayatActivity;
import xyz.jncode.manufriend.R;

public class FragmentHome extends Fragment {

    CardView cardViewJalan, cardViewBelanja, cardViewNgobrol, cardViewOlahraga, cardViewPesta, cardViewLainLain;

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        // Fragment fragment = PostingFragment.newInstance();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        cardViewJalan = rootView.findViewById(R.id.cv_jalan_jalan);
        cardViewBelanja = rootView.findViewById(R.id.cv_belanja);
        cardViewNgobrol = rootView.findViewById(R.id.cv_ngobrol);
        cardViewOlahraga = rootView.findViewById(R.id.cv_olahraga);
        cardViewPesta = rootView.findViewById(R.id.cv_pesta);
        cardViewLainLain = rootView.findViewById(R.id.cv_lainlain);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardViewJalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoDetail = new Intent(getActivity(), DetailRiwayatActivity.class);
                gotoDetail.putExtra("idservice", "1");
                gotoDetail.putExtra("labelsProduct", "Jalan-jalan");
                getActivity().startActivity(gotoDetail);
            }
        });

        cardViewBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoDetail = new Intent(getActivity(), DetailRiwayatActivity.class);
                gotoDetail.putExtra("idservice", "2");
                gotoDetail.putExtra("labelsProduct", "Belanja");
                getActivity().startActivity(gotoDetail);
            }
        });

        cardViewNgobrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoDetail = new Intent(getActivity(), DetailRiwayatActivity.class);
                gotoDetail.putExtra("idservice", "3");
                gotoDetail.putExtra("labelsProduct", "Ngobrol");
                getActivity().startActivity(gotoDetail);
            }
        });

        cardViewOlahraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoDetail = new Intent(getActivity(), DetailRiwayatActivity.class);
                gotoDetail.putExtra("idservice", "4");
                gotoDetail.putExtra("labelsProduct", "Olahraga");
                getActivity().startActivity(gotoDetail);
            }
        });

        cardViewPesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoDetail = new Intent(getActivity(), DetailRiwayatActivity.class);
                gotoDetail.putExtra("idservice", "5");
                gotoDetail.putExtra("labelsProduct", "Mendatangi Pesta");
                getActivity().startActivity(gotoDetail);
            }
        });

        cardViewLainLain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoDetail = new Intent(getActivity(), DetailRiwayatActivity.class);
                gotoDetail.putExtra("idservice", "6");
                gotoDetail.putExtra("labelsProduct", "Lain-lain");
                getActivity().startActivity(gotoDetail);
            }
        });
    }
}

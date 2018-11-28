package xyz.jncode.manufriend;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import xyz.jncode.manufriend.Fragment.FragmentHome;
import xyz.jncode.manufriend.Fragment.FragmentNotifikasi;
import xyz.jncode.manufriend.Fragment.FragmentProfil;
import xyz.jncode.manufriend.Fragment.FragmentRiwayat;


public class MainActivity extends AppCompatActivity {

    Fragment fragmentHome;
    Fragment fragmentRiwayat;
    Fragment fragmentNotifikasi;
    Fragment fragmentProfil;
    LinearLayout llRiwayat, llHome, llNotifikasi, llProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentHome = FragmentHome.newInstance();
        fragmentRiwayat = FragmentRiwayat.newInstance();
        fragmentNotifikasi = FragmentNotifikasi.newInstance();
        fragmentProfil = FragmentProfil.newInstance();
        llRiwayat = findViewById(R.id.menu_riwayat);
        llHome = findViewById(R.id.menu_home);
        llNotifikasi = findViewById(R.id.menu_notifikasi);
        llProfil = findViewById(R.id.menu_profil);

        setupChangeViewFragment(fragmentHome);

        llRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupChangeViewFragment(fragmentRiwayat);
            }
        });

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupChangeViewFragment(fragmentHome);
            }
        });

        llNotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupChangeViewFragment(fragmentNotifikasi);
            }
        });
        llProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupChangeViewFragment(fragmentProfil);
            }
        });
    }

    public void setupChangeViewFragment(Fragment frm) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.layout_main, frm);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}

package xyz.jncode.manufriend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ProfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profil);
        Bundle extras = getIntent().getExtras();
    }
}

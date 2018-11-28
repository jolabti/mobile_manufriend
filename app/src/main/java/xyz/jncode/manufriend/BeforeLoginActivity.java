package xyz.jncode.manufriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class BeforeLoginActivity extends AppCompatActivity {
    Button nextLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);

        nextLogin = findViewById(R.id.btn_before_login);

        nextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoLogin = new Intent(BeforeLoginActivity.this, LoginActivity.class);
                startActivity(gotoLogin);
            }
        });
    }
}

package com.example.cd.workreminder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RememberMe extends AppCompatActivity {

    private EditText name; //Added on 7 - 5 - 2019
    private EditText password; //Added on 7 - 5- 2019
    private Button save; //Added on 7 - 5- 2019
    private SharedPreferences pref; //Added on 7 - 5- 2019
    private SharedPreferences.Editor editPref; //Added on 7 - 5 - 2019

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_me);
        this.pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rememberName = name.getText().toString();
                String rememberPassword = password.getText().toString();
                editPref = pref.edit();
                editPref.putString("NAME", rememberName);
                editPref.putString("PASSWORD", "'" + rememberPassword + "'");
                editPref.putBoolean("SAVE_PASSWORD", true);
                editPref.apply();
                finish();
            }
        });
    }
}//end class

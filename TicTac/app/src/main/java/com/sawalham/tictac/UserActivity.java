package com.sawalham.tictac;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by mohammad on 6/30/2015.
 */
public class UserActivity extends Activity {

    EditText name;
    Button button;
    String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        button = (Button) findViewById(R.id.btndone);
        name = (EditText) findViewById(R.id.txtname);
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sp.getBoolean("KEY", false) == true) {
            name.setText(sp.getString("NAME", " "));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = name.getText().toString();
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("KEY", true);
                edit.putString("NAME", n);
                edit.commit();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }
}

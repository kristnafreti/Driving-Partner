package com.example.kris.drivingpartner;

/**
 * Created by kris on 8/25/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MenuActivity extends Activity {

    Context context = this;
    SharedPreferences sp;

    private EditText PhoneNumber;
    private Button setNumber;
    private Button toAlcohol;
    private TextView konfirmasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sp = PreferenceManager.getDefaultSharedPreferences(context);

        PhoneNumber = (EditText)findViewById(R.id.Phone);
        setNumber = (Button)findViewById(R.id.button1);
        toAlcohol = (Button)findViewById(R.id.button2);
        konfirmasi  = (TextView)findViewById(R.id.textView1);

        konfirmasi.setText("Nomor Designated Driver anda: " +(sp.getString("phone", "n/a")));

        setNumber.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String number = PhoneNumber.getText().toString();
                Editor edit = sp.edit();
                edit.putString("phone", number);
                edit.commit();
                konfirmasi.setText("Nomor Designated Driver anda: " +(sp.getString("phone", "n/a")));
            }

        });

        toAlcohol.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, SensorActivity.class);
                intent.putExtra("drunkReport", false);
                startActivity(intent);
            }

        });
    }

}

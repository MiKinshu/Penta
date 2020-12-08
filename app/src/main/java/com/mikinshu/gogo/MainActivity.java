package com.mikinshu.gogo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import chtgupta.qrutils.qractivity.QRScanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    public static String ORG = "NULL";
    private final int QR_SCAN_REQUEST_CODE = 123;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ORG.equals("NULL")) {
            button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            new QRScanner(getBaseContext())
                                    .setFullScreen(false)
                                    .setAutoFocusInterval(2000)
                                    .setFocusOnTouchEnabled(true)
                                    .build(), QR_SCAN_REQUEST_CODE
                    );
                }
            });
            final String[] orgArray = {"IIITA", "China Museum", "Taj Mahal", "Haveli"};
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.activity_listview, orgArray);

            ListView listView = (ListView) findViewById(R.id.orgList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ORG = orgArray[position];
                    Toast.makeText(getApplicationContext(),  orgArray[position], Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, SahayakTheAssistant.class);
                    startActivity(intent);
                }
            });
        }
        else {
            Intent intent = new Intent(MainActivity.this, SahayakTheAssistant.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCAN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String ORG = data.getStringExtra("qrData");
                Intent intent = new Intent(MainActivity.this, SahayakTheAssistant.class);
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {
                String error = data.getStringExtra("error");
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
package com.mikinshu.gogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable ;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import chtgupta.qrutils.qractivity.QRScanner;

public class MainActivity extends AppCompatActivity {
    public static String ORG = "NULL";
    private final int QR_SCAN_REQUEST_CODE = 123;
    private static final int RC_SIGN_IN = 1;
    public static String TAG = "MyLogs";
    Button button;

    //User details
    public static String mUsername, mEmail;

    //Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    void setUpApplication() {
        if (ORG.equals("NULL")) {
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
            ListView listView = findViewById(R.id.orgList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ORG = orgArray[position];
                    Toast.makeText(getApplicationContext(), orgArray[position], Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, SahayakTheAssistant.class);
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(MainActivity.this, SahayakTheAssistant.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    mUsername = user.getDisplayName();
                    mEmail = user.getEmail();
                    setUpApplication();
                } else {
                    // user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
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
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                setUpApplication();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cannot work, until you sign in.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
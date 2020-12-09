package com.mikinshu.gogo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import chtgupta.qrutils.qractivity.QRScanner;

public class MainActivity extends AppCompatActivity {
    public static Integer ORG = -1;
    public static String Name = "NONE";
    private final int QR_SCAN_REQUEST_CODE = 123;
    private static final int RC_SIGN_IN = 1;
    public static String TAG = "MyLogs";

    ProgressDialog dialog;
    Button button;

    //User details
    public static String mUsername, mEmail;

    //Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //Listview
    public ArrayList<Integer> ID;
    public ArrayList<String> Names;

    void setUpApplication() {
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

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if(ID == null || Names == null)
            new RetriveDetails().execute();
    }

    class RetriveDetails extends AsyncTask<Void,Void,String> {
        @SuppressLint("WrongThread")

        public RetriveDetails(){
            ID = new ArrayList<>();
            Names = new ArrayList<>();
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.setTitle("Fetching data");
            dialog.setMessage("Getting Organisations");
            dialog.show();
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL audiodb = new URL(getString(R.string.server) + "/channel/all");
                HttpURLConnection myConnection =(HttpURLConnection) audiodb.openConnection();
                InputStream stream = new BufferedInputStream(myConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                JSONArray topLevel = new JSONArray(builder.toString());
                for(int i = 0; i < topLevel.length(); i++) {
                    JSONObject o = topLevel.getJSONObject(i);
                    ID.add(o.getInt("id"));
                    Names.add(o.getString("name"));
                }
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            finally {
                dialog.dismiss();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final String s) {//sharing details
            super.onPostExecute(s);
            dialog.dismiss();
            final String[] orgArray = new String[Names.size()];
            for(int i = 0; i < Names.size(); i++) {
                orgArray[i] = Names.get(i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.activity_listview, orgArray);
            ListView listView = findViewById(R.id.orgList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ORG = ID.get(position);
                    Name = Names.get(position);
                    Intent intent = new Intent(MainActivity.this, SahayakTheAssistant.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        Objects.requireNonNull(this.getSupportActionBar()).hide();
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
                String rec = Objects.requireNonNull(data).getStringExtra("qrData");
                String[] split = Objects.requireNonNull(rec).split("~");
                ORG = Integer.parseInt(split[0]);
                Name = split[1];
                Log.d(TAG, "onActivityResult: " + split[0]);
                Log.d(TAG, "onActivityResult: " + split[1]);
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
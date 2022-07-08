package com.andyra.tugas7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static EditText eduser,edpass;

    SharedPreferences msharedpreferences;
    public static final String MyPREFERENCES = "MayPrefs" ;
    public static final String user = "MayUser";
    public static final String pass = "MayPass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eduser = (EditText) findViewById(R.id.ed_user);
        edpass = (EditText) findViewById(R.id.ed_pass);
        Button btnlogin = (Button) findViewById(R.id.btn_login);
        Button btnregis = (Button) findViewById(R.id.btn_regis);
        Button btnexit = (Button) findViewById(R.id.btn_klr);


        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String username = eduser.getText().toString();
                String password = edpass.getText().toString();
                try {
                    DatabaseAdapter dbadapter = new DatabaseAdapter(MainActivity.this);
                    dbadapter.open();

                    if (dbadapter.Register(username, password)) {
                        Toast.makeText(MainActivity.this, "Create Data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "User "+username +" Telah terdaftar"
                                , Toast.LENGTH_LONG).show();
                    }

                    dbadapter.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = eduser.getText().toString();
                String password = edpass.getText().toString();
                SharedPreferences.Editor meditor = msharedpreferences.edit();

                try {
                    if (username.length() > 0 && password.length() > 0) {
                        DatabaseAdapter dbAdapter = new DatabaseAdapter(MainActivity.this);
                        dbAdapter.open();

                        if (dbAdapter.Login(username, password)) {
                            Toast.makeText(MainActivity.this, "Login Sukses"
                                    , Toast.LENGTH_LONG).show();
                            meditor.putString(user, username);
                            meditor.putString(pass, password);
                            meditor.commit();
                            Intent pindah = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(pindah);
                            MainActivity.this.finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Username/Password salah"
                                    , Toast.LENGTH_LONG).show();
                        }

                        dbAdapter.close();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage()
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        msharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (msharedpreferences.contains(user))
        {
            if(msharedpreferences.contains(pass)){
                Intent pindah = new Intent(this, Dashboard.class);
                startActivity(pindah);
                MainActivity.this.finish();
            }
        }
        super.onResume();
    }
}
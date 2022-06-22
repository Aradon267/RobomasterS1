package com.example.robomaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * This is the first screen of the application that contains the login functionality
 * and the register ability.
 * If the input of the user matches the data
 * in the database he will be moved to the next screen.
 * If the user clicks on the register button he will be moved to the register page
 **/

public class LoginActivity extends AppCompatActivity {

    private Button btnReg;
    private Button btnLogin;
    private Button btnFetch;
    private CheckBox cbRem;
    private EditText etName;
    private EditText etPass;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    /**
     * The function will create and run the current screen(login) with the data from the saved state
     * @param savedInstanceState data saved to help activities start/restart themselves
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeLoinScreen();

        SharedPreferences prefs = getSharedPreferences("USER_DATA_FILE", MODE_PRIVATE);
        String userSaved = prefs.getString("Username", null);
        String passSaved = prefs.getString("Password", null);

        if (userSaved != null && passSaved != null) {
            btnFetch.setText("Use: " + userSaved);
        }
        else{
            btnFetch.setText("No data found");
        }


        /**
         * Go to signup activity
         */
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screen = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(screen);
            }
        });

        /**
         * Use saved data as login information
         */
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSaved != null && passSaved != null) {
                    logSaved(userSaved);
                }
                else{
                    Toast.makeText(LoginActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * Go to next activity if the user given was found in the database
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bad = false;
                if(isEmpty(etPass))
                {
                    bad = true;
                    Toast.makeText(LoginActivity.this, "You did not enter a password", Toast.LENGTH_SHORT).show();
                }
                if(isEmpty(etName))
                {
                    bad = true;
                    Toast.makeText(LoginActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                }
                if(bad == false) {
                    Query q = myRef.child("allUsers").orderByKey();

                    final String name = etName.getText().toString();
                    final String pass = etPass.getText().toString();
                    final boolean save = cbRem.isChecked();

                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean correct = false;
                            for(DataSnapshot dst : dataSnapshot.getChildren()) {
                                User u = dst.getValue(User.class);
                                if(u.username.equals(name) && u.password.equals(pass)){
                                    correct = true;

                                }
                            }
                            if(!correct){
                                Toast.makeText(LoginActivity.this, "The username or password entered were incorrect!", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                if(save){
                                    SharedPreferences preferences = getSharedPreferences("USER_DATA_FILE", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("Username",name);
                                    editor.putString("Password",pass);
                                    editor.apply();
                                    editor.commit();
                                }
                                Intent screen = new Intent(LoginActivity.this, RobotLinkActivity.class);
                                screen.putExtra("user",name);
                                startActivity(screen);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }

    /**
     * Initialize the components and enables dark mode
     */
    private void initializeLoinScreen() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        btnReg = findViewById(R.id.btnSign);
        btnLogin = findViewById(R.id.btnLog);
        btnFetch = findViewById(R.id.btnFetch);
        etName = findViewById(R.id.etName);
        etPass = findViewById(R.id.etPass);
        cbRem = findViewById(R.id.cbRemUser);
    }

    /**
     * The function will check if the edittext given is empty
     * @param etText the edittext we check
     * @return true if the edittext is empty, false otherwise
     */
    public boolean isEmpty(EditText etText) {
        if (etText.getText().toString().length() > 0)
            return false;
        return true;
    }

    /**
     * Moves the saved user to the next screen
     * @param userSaved the name of the user we connect as
     */
    public void logSaved(String userSaved){
        Intent screen = new Intent(LoginActivity.this, RobotLinkActivity.class);
        screen.putExtra("user",userSaved);
        startActivity(screen);
    }
    
}

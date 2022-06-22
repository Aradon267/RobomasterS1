package com.example.robomaster;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * This is the register screen of the application that contains the register functionality
 * If the input of the user does not appear in our database he will be added
 * and moved to the login page once again.
 * If the user clicks on the back button he will be moved to the login page
 **/

public class SignupActivity extends AppCompatActivity {

    private Button btnBack;
    private Button btnSignin;

    private EditText etName;
    private EditText etPass;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    /**
     * The function will create and run the current screen(register) with the data from the saved state
     * @param savedInstanceState data saved to help activities start/restart themselves
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeComponents();

        /**
         * Go to Login activity
         */
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * Adds new user to the database if another user with the same name wasn't found
         */
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bad = false;
                if(isEmpty(etPass))
                {
                    bad = true;
                    Toast.makeText(SignupActivity.this, "You did not enter a password", Toast.LENGTH_SHORT).show();
                }
                if(isEmpty(etName))
                {
                    bad = true;
                    Toast.makeText(SignupActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                }
                if(bad == false) {
                    Query q = myRef.child("allUsers").orderByKey();
                    final String name = etName.getText().toString();
                    final String pass = etPass.getText().toString();
                    final User Unew = new User(name,pass);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean exist = false;
                            for(DataSnapshot dst : dataSnapshot.getChildren()) {
                                User u = dst.getValue(User.class);
                                if(u.username.equals(name)){
                                    exist = true;

                                }
                            }
                            if(!exist){
                                myRef.child("allUsers").push().setValue(Unew);
                                Toast.makeText(SignupActivity.this, "User added!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Toast.makeText(SignupActivity.this, "Username already exist!", Toast.LENGTH_SHORT).show();
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
     * Initialize the components in one function
     */
    private void initializeComponents() {
        btnBack = findViewById(R.id.btnBack);
        btnSignin = findViewById(R.id.btnSign);
        etName = findViewById(R.id.etName);
        etPass = findViewById(R.id.etPass);
    }

    /**
     * The function will check if the EditText that was given is empty
     * @param etText the EditText we want to check if empty or not
     * @return true if the EditText is empty false if not empty
     */
    public boolean isEmpty(EditText etText) {
        if (etText.getText().toString().length() > 0)
            return false;
        return true;
    }
}

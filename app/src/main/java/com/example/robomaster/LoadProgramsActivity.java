package com.example.robomaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This is the load programs screen of the application that contains the loading of saved programs functionality.
 * The user gets a list of all the programs he saved and he can click on one to use it on the commands activity.
 * The user can go back to the commands activity without choosing a program.
 */

public class LoadProgramsActivity extends AppCompatActivity {

    private String name;

    private ListView lvFunctions;
    private Button btnFinish;

    private ArrayList<Program> programs = new ArrayList<Program>();


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    /**
     * The function will create and run the current screen(tutorial) with the data from the saved state
     * @param savedInstanceState data saved to help activities start/restart themselves
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_programs);

        initializeComponents();
        Intent i = getIntent();
        name = i.getStringExtra("user");
        initPrograms();


        /**
         * Go to command activity
         */
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("program","placeholder");
                intent.putExtra("programDisplay","placeholder");
                setResult(200,intent);
                finish();
            }
        });

        /**
         * Listens to clicks on the listview and loads the selected program
         */
        lvFunctions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Dialog progDialog = new Dialog(LoadProgramsActivity.this);
                progDialog.setContentView(R.layout.dialog_delete);

                Button btnLoadProg = progDialog.findViewById(R.id.btnLoad);
                Button btnDelProg = progDialog.findViewById(R.id.btnDelete);

                /**
                 * Load the program
                 */
                btnLoadProg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String prog = programs.get(i).toStringProgram();
                        String progDis = programs.get(i).toStringProgramDisplay();
                        Intent intent = new Intent();
                        intent.putExtra("program",prog);
                        intent.putExtra("programDisplay",progDis);
                        setResult(200,intent);
                        progDialog.dismiss();
                        finish();
                    }
                });

                /**
                 * delete the program
                 */
                btnDelProg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Program rep = programs.get(i);
                        programs.remove(programs.get(i));
                        refresh_lv();
                        myRef.child("allPrograms").child(rep.getprogid()).removeValue();
                        progDialog.dismiss();
                    }
                });

                progDialog.show();

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                progDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);
            }
        });
    }




    /**
     * Initialize the components in one function
     */
    private void initializeComponents() {
        lvFunctions = findViewById(R.id.lvPrograms);
        btnFinish = findViewById(R.id.btnBackProg);
    }

    /**
     * Initialize the programs array with programs from firebase according to the username
     */
    private void initPrograms(){
        Query q = myRef.child("allPrograms").orderByValue();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                programs.clear();
                for(DataSnapshot dst : dataSnapshot.getChildren()) {
                    Program p = dst.getValue(Program.class);
                    if(p.getUsername().equals(name)){
                        programs.add(p);
                    }
                }
                refresh_lv();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * refreshes the listview
     */
    public void refresh_lv(){
        ProgramsAdapter adp = new ProgramsAdapter(this,R.layout.programs_row,programs);
        lvFunctions.setAdapter(adp);
    }
}
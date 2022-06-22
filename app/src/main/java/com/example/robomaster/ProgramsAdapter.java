package com.example.robomaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The class will handle the custom array adapter we use at LoadProgramsActivity.
 * functions - the ArrayList of programs we want to show.
 * context - contains the activity we want to put the programs at.
 */

public class ProgramsAdapter extends ArrayAdapter {
    ArrayList<Program> functions;
    Context context;


    /**
     * A regular constructor for the adapter
     * @param context contains the activity we want to put the objects at
     * @param resource the xml we want to use to display the objects with
     * @param objects the ArrayList of objects with the data we want to show
     */
    public ProgramsAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);

        this.functions = objects;
        this.context = context;

    }



    /**
     * Get a View that displays the data in a given xml layout at the specified position in the data set
     * @param position index to show
     * @param convertView the basic building block for user interface components
     * @param parent a special view that can contain other views
     * @return a View that displays the data
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.programs_row, null);

        TextView tvNameColor = (TextView) v.findViewById(R.id.tvNameProg);
        TextView tvR = (TextView) v.findViewById(R.id.tvDescProg);
        tvNameColor.setText(functions.get(position).getProgramName() + " - " + functions.get(position).getDate());
        tvR.setText(functions.get(position).getProgramDescription());

        return v;
    }
}

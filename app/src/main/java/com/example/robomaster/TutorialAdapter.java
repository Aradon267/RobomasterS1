package com.example.robomaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * The class will handle the custom array adapter we use at TutorialActivity.
 * functions - the ArrayList of functions we want to show.
 * context - contains the activity we want to put the functions at.
 */
public class TutorialAdapter extends ArrayAdapter {

    ArrayList<RobotFunctionTutorial> functions;
    Context context;

    /**
     * A regular constructor for the adapter
     * @param context contains the activity we want to put the objects at
     * @param resource the xml we want to use to display the objects with
     * @param objects the ArrayList of objects with the data we want to show
     */
    public TutorialAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
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
        View v = inflater.inflate(R.layout.tutorial_row, null);

        TextView tvNameColor = (TextView) v.findViewById(R.id.tvNameFunc);
        TextView tvR = (TextView) v.findViewById(R.id.tvDescFunc);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        if(functions.get(position).getFuncDescRest().equals("")){
            tvNameColor.setText(functions.get(position).getFuncName());
            tvR.setText(functions.get(position).getFuncDesc());
            Picasso.get().load(functions.get(position).getImg()).into(imageView);
        }
        else{
            tvNameColor.setText(functions.get(position).getFuncName());
            tvR.setText(functions.get(position).getFuncDesc()+"...");
            Picasso.get().load(functions.get(position).getImg()).into(imageView);
        }
        return v;
    }
}

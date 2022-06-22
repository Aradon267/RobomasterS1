package com.example.robomaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This is the tutorial screen of the application that contains information about the robots functions
 * The information will be displayed in a listview.
 * If the user clicks on an item in the listview a dialog with extended explanation
 * will appear.
 **/

public class TutorialActivity extends AppCompatActivity {
    private ArrayList<RobotFunctionTutorial> functions = new ArrayList<RobotFunctionTutorial>();

    private ListView lvFunctions;
    private Button btnFinish;

    /**
     * The function will create and run the current screen(tutorial) with the data from the saved state
     * @param savedInstanceState data saved to help activities start/restart themselves
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        initializeComponents();
        initFunctions();
        refresh_lv();

        /**
         * Go to command activity
         */
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * Listens to clicks on the listview and opens a dialog with information based on the clicked item
         */
        lvFunctions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Dialog tutorialDialog = new Dialog(TutorialActivity.this);
                tutorialDialog.setContentView(R.layout.dialog_tutorial);

                Button btnClose = tutorialDialog.findViewById(R.id.btnDialogClose);
                TextView tvDiaName = tutorialDialog.findViewById(R.id.tvDialogName);
                TextView tvDiaDesc = tutorialDialog.findViewById(R.id.tvDialogDesc);
                TextView tvDiaPara = tutorialDialog.findViewById(R.id.tvDialogPara);
                ImageView imgDia = tutorialDialog.findViewById(R.id.imgDialog);

                tvDiaName.setText(functions.get(i).getFuncName());
                tvDiaDesc.setText(functions.get(i).getFuncDesc() + functions.get(i).getFuncDescRest());
                tvDiaPara.setText(functions.get(i).getFuncPara());
                Picasso.get().load(functions.get(i).getImg()).into(imgDia);

                /**
                 * Closes the dialog
                 */
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tutorialDialog.dismiss();
                    }
                });
                tutorialDialog.show();

            }
        });
    }

    /**
     * refreshes the listview
     */
    public void refresh_lv(){
        TutorialAdapter adp = new TutorialAdapter(this,R.layout.tutorial_row,functions);
        lvFunctions.setAdapter(adp);
    }


    /**
     * Initialize the components in one function
     */
    private void initializeComponents() {
        lvFunctions = findViewById(R.id.lvFunctions);
        btnFinish = findViewById(R.id.btnBackTutorial);
    }

    /**
     * Initializes the array
     */
    public void initFunctions(){
        functions.add(new RobotFunctionTutorial("MOVE","Commands the robot to move"," for a given amount of time with given speed for each wheel.", "\nThe function gets 5 parameters: FL wheel speed(-100 to 100) FR wheel speed(-100 to 100) BL wheel speed(-100 to 100) BR wheel speed(-100 to 100) all in rounds per minute to set the speed of each wheel and time in seconds(1-100) to set the amount of time.","https://images.everydayhealth.com/images/pain-management/back-pain/cs-back-pain-driving-1440x810.jpg"));
        functions.add(new RobotFunctionTutorial("BLINK","Makes the LEDs of the robot blink"," to the color you enter for a given amount of time.","\nThe function gets 4 parameters: R(0-255) B(0-255) G(0-255) to indicate the color and time in seconds(1-100) to set the amount of time.","https://www.cnet.com/a/img/1lJNHSz9rBUpIIeNpyIhcULHQtA=/1200x675/2021/04/05/0461e12f-d099-4621-86f9-9a6ba66111ce/gosund.jpg"));
        functions.add(new RobotFunctionTutorial("SHOOT","Commands the robot to shoot once.","","\nThe function gets no parameters.","https://cdn.britannica.com/19/65719-050-B3CCD2AF/Cannon-Antietam-National-Battlefield-Maryland.jpg"));
        functions.add(new RobotFunctionTutorial("CUSTOM","Executes a custom command.","","\nThe function gets the the custom command as a parameter.","https://t3.ftcdn.net/jpg/03/67/25/32/360_F_367253290_6TxOfCSQstFoHzbU9HyNoc7AVJzgqQem.jpg"));
        functions.add(new RobotFunctionTutorial("PERMANENT COLOR","Changes the LEDs of the robot when it's executing"," commands based on your choice from the more options activity.","\nThe function gets the R(0-255) B(0-255) G(0-255) from the edittext or from what you will click in the picture. You can also take a picture to use instead.","https://d3mvlb3hz2g78.cloudfront.net/wp-content/uploads/2012/08/thumb_720_450_LED_shutterstock_62486296.jpg"));
        functions.add(new RobotFunctionTutorial("SAVE PROGRAM","Saving the program the user wrote"," to the firebase.","\nThe function gets no parameters.","https://www.downloadclipart.net/medium/save-button-png-transparent-photo.png"));
        functions.add(new RobotFunctionTutorial("LOAD PROGRAM","Loads the programs the user wrote"," from the firebase and allowing the user to keep working on it.","\nThe function gets no parameters.","https://w7.pngwing.com/pngs/702/417/png-transparent-input-load-file-document-data-icon-loading-symbol-downloading-thumbnail.png"));
        functions.add(new RobotFunctionTutorial("TEST MODE","Enters the command activity in test mode"," to check UI, input acceptance, etc.","\nClick 3 times on the \"connect to a robot\" textview in robotlink activity.","https://ckhconsulting.com/wp-content/uploads/2021/07/pencil-test.jpeg"));
    }
}


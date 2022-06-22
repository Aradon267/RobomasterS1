package com.example.robomaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.regex.Pattern;

/**
 * This is the linking screen of the application that contains the link functionality.
 * If the input of the user can be connected to a server he will be moved to the next page.
 * The user can ask to logout and he will have to confirm this action in a dialog.
 */

public class RobotLinkActivity extends AppCompatActivity {
    public static RobomasterClass robomaster = null;

    private EditText etServerIP, etServerPort;
    private Button btnLogout;
    private int counter = 0;
    private String name;


    /**
     * The function will create and run the current screen(robotLink) with the data from the saved state
     * @param savedInstanceState data saved to help activities start/restart themselves
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.robot_link_activity);

        initializeComponents();

        Intent i = getIntent();
        name = i.getStringExtra("user");

        /**
         * Prompts logout confirmation dialog
         */
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog logoutDialog = new Dialog(RobotLinkActivity.this);
                logoutDialog.setContentView(R.layout.dialog_confirm_logout);

                Button btnConfirm = logoutDialog.findViewById(R.id.btnConfirmLogout);
                Button btnNoConfirm = logoutDialog.findViewById(R.id.btnNoLogout);
                ImageView imgAlert = logoutDialog.findViewById(R.id.imgAlertPic);
                imgAlert.setBackgroundResource(android.R.drawable.ic_dialog_alert);

                /**
                 * Closes the dialog without logging out
                 */
                btnNoConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                    }
                });

                /**
                 * Closes dialog, logging out and returns to login screen
                 */
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            robomaster.disconnect();
                        }
                        catch (Exception e){

                        }
                        logoutDialog.dismiss();
                        finish();
                    }
                });
                logoutDialog.show();
            }
        });
    }

    /**
     * The function is activated when clicked on a button and will make the connection to the robot
     * using the RobomasterClass and using the user's input.
     * If the connection was successful the user will be moved to the next screen.
     * If the connection didn't work an error will be displayed.
     * @param v the view that received the client's click event
     * **/
    public void handleEstablishConnection(View v) {
        boolean bad = false;
        if(isEmpty(etServerIP)){
            bad = true;
            Toast.makeText(RobotLinkActivity.this, "The IP is missing", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etServerPort)){
            bad = true;
            Toast.makeText(RobotLinkActivity.this, "The port is missing", Toast.LENGTH_SHORT).show();
        }
        if(!bad){
            Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
            String ChosenServerIP = etServerIP.getText().toString();
            int ChosenServerPort = Integer.parseInt(etServerPort.getText().toString());
            if(PATTERN.matcher(ChosenServerIP).matches()){
                robomaster = new RobomasterClass(ChosenServerIP, ChosenServerPort);
                robomaster.establishConnection(RobotLinkActivity.this, CommandActivity.class, name);
            }
            else{
                Toast.makeText(RobotLinkActivity.this, "IP must be [0-255].[0-255].[0-255].[0-255]", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Enters test mode(check UI and input boundaries) after 3 clicks on a textview
     * @param v the view that received the client's click event
     */
    public void handleQuickConnect(View v){
        counter++;
        if(counter>2){
            Toast.makeText(RobotLinkActivity.this, "Entering test mode...", Toast.LENGTH_SHORT).show();
            robomaster = new RobomasterClass("1.7.6.5", 2);
            robomaster.establishQuickConnection();
            Intent i = new Intent(RobotLinkActivity.this, CommandActivity.class);
            i.putExtra("user",name);
            startActivity(i);
        }
        else{
            Toast.makeText(RobotLinkActivity.this, "Click " + (3-counter) + " more times for test mode", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize the components in one function
     */
    private void initializeComponents() {
        etServerIP = findViewById(R.id.etServerIP);
        etServerPort = findViewById(R.id.etServerPort);
        btnLogout = findViewById(R.id.btnAskLogout);
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
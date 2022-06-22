package com.example.robomaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * This is the command screen of the application.
 * From here, the user can send premade or self-made commands to the robot.
 * To send premade commands, the user can put variables such as wheels speed, driving time in seconds and more.
 * All of the commands that we want to execute will be displayed on the listview.
 * The execute button will send the contents of the listview to the robot.
 **/

public class CommandActivity extends AppCompatActivity {

    private EditText etScript;
    private Button btnExecute;
    private Button btnCustom;

    private ListView lvCommands;

    private EditText etMoveFR, etMoveFL, etMoveBR, etMoveBL;
    private EditText etBlinkR, etBlinkG, etBlinkB;
    private EditText etMoveDuration;
    private EditText etBlinkDuration;

    private String permaRes;
    private String programRes;
    private String programDisRes;
    private ArrayList<String> commandsArr = new ArrayList<>();

    private RobomasterClass robomaster = RobotLinkActivity.robomaster;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    private String name;

    /**
     * A class that will hold the request and reply codes for intents
     */
    public static class IntentCodes{
        public static final int COLOR_REQUEST = 123;
        public static final int PROGRAM_REQUEST = 456;
        public static final int RESULT_SUCCESS = 200;
    }

    /**
     * The function will create and run the current screen(command) with the data from the saved state
     * @param savedInstanceState data saved to help activities start/restart themselves
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        initializeComponents();

        Intent i = getIntent();
        name = i.getStringExtra("user");

        /**
         * Listener that adds custom script to the queue
         */
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomScript();
            }
        });

        /**
         * Listener that executes the script
         */
        btnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!robomaster.getQ().isEmpty()){
                    executeScript(v);
                }
            }
        });
    }

    /**
     * Initialize the components in one function
     */
    private void initializeComponents() {
        etScript = findViewById(R.id.etScript);
        btnCustom = findViewById(R.id.btnCustom);
        btnExecute = findViewById(R.id.btnExecute);
        etMoveFL = findViewById(R.id.etMoveFL);
        etMoveFR = findViewById(R.id.etMoveFR);
        etMoveBL = findViewById(R.id.etMoveBL);
        etMoveBR = findViewById(R.id.etMoveBR);

        etBlinkR = findViewById(R.id.etBlinkR);
        etBlinkG = findViewById(R.id.etBlinkG);
        etBlinkB = findViewById(R.id.etBlinkB);

        etMoveDuration = findViewById(R.id.etMoveDuration);
        etBlinkDuration = findViewById(R.id.etBlinkDuration);

        lvCommands = findViewById(R.id.lvCommands);
    }

    /**
     * Add custom scripts to the queue of commands
     */
    public void addCustomScript() {
        if(!isEmpty(etScript)){
            final String chosenScript = etScript.getText().toString();
            robomaster.addToMessagesQueue(chosenScript);
            commandsArr.add(chosenScript);
            refresh_lv();
        }
        else{
            Toast.makeText(CommandActivity.this, "You did not enter a custom command", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The function will handle what needs to happen when the screen is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (robomaster != null) robomaster.disconnect();
    }

    /**
     * The function will handle creating an options menu
     * @param menu the menu
     * @return true for the menu to be displayed, false for not displaying it
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * The function will handle what happens when we click on an item in the menu
     * @param item the item we click on
     * @return false to allow normal menu processing, true to stop it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.itmDisconnect:
                robomaster.disconnect();
                finish();
            break;
            case R.id.itmOptions:
                Intent screen = new Intent(CommandActivity.this, OptionsActivity.class);
                startActivityForResult(screen,IntentCodes.COLOR_REQUEST);
            break;
            case R.id.itmTutorial:
                Intent screenTutorial = new Intent(CommandActivity.this, TutorialActivity.class);
                startActivity(screenTutorial);
            break;
            case R.id.itmSave:
                final Dialog saveDialog = new Dialog(CommandActivity.this);
                saveDialog.setContentView(R.layout.dialog_save_program);

                Button btnConfirm = saveDialog.findViewById(R.id.btnConfirmSave);
                Button btnNoConfirm = saveDialog.findViewById(R.id.btnNoSave);
                EditText etName = saveDialog.findViewById(R.id.etName);
                EditText etDescription = saveDialog.findViewById(R.id.etDesc);

                /**
                 * Closes the dialog without saving
                 */
                btnNoConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveDialog.dismiss();
                    }
                });

                /**
                 * Closes dialog and saves program
                 */
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean badEntry = false;
                        if(isEmpty(etName)){
                            badEntry = true;
                            Toast.makeText(CommandActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                        }
                        if(isEmpty(etDescription)){
                            badEntry = true;
                            Toast.makeText(CommandActivity.this, "You did not enter a description", Toast.LENGTH_SHORT).show();
                        }
                        if(robomaster.getQ().isEmpty()){
                            badEntry = true;
                            Toast.makeText(CommandActivity.this, "Queue is empty", Toast.LENGTH_SHORT).show();
                        }
                        if(!badEntry){
                            final String progName = etName.getText().toString();
                            final String progDesc = etDescription.getText().toString();
                            String creationDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            List l = new ArrayList(robomaster.getQ());
                            Program prog = new Program(l, name, progName, progDesc, creationDate, commandsArr, myRef.child("allPrograms").push().getKey());
                            myRef.child("allPrograms").child(prog.getprogid()).setValue(prog);
                        }
                        saveDialog.dismiss();
                    }
                });
                saveDialog.show();
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                saveDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);
            break;
            case R.id.itmLoad:
                Intent screenLoad = new Intent(CommandActivity.this, LoadProgramsActivity.class);
                screenLoad.putExtra("user",name);
                startActivityForResult(screenLoad,IntentCodes.PROGRAM_REQUEST);
            break;
            case R.id.itmClear:
                commandsArr.clear();
                robomaster.clearQueue();
                refresh_lv();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The function will be called when the user returns from activities started with "startActivityForResult"
     * @param requestCode the code we used when we started the activity to identify how to use the response
     * @param resultCode the code that came with the result to indicate failure and success
     * @param data the data sent as the result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == IntentCodes.RESULT_SUCCESS){
            switch(requestCode){
                case IntentCodes.COLOR_REQUEST:
                    permaRes = data.getStringExtra("color");
                    if(!permaRes.equals("placeholder")){
                        permaLed(permaRes);
                    }
                break;
                case IntentCodes.PROGRAM_REQUEST:
                    programRes = data.getStringExtra("program");
                    if(!programRes.equals("placeholder")){
                        programDisRes = data.getStringExtra("programDisplay");
                        Toast.makeText(CommandActivity.this, programDisRes, Toast.LENGTH_SHORT).show();
                        String[] progCommands = programRes.split("\n");
                        String[] progCommandsDis = programDisRes.split("\n");
                        loadSavedProgram(progCommands, progCommandsDis);
                    }
                break;
            }
        }
    }

    /**
     * Adds the moving command to the queue of commands if all parameters are in boundaries
     * @param v the view that received the client's click event
     */
    public void handleMove(final View v) {
        boolean badEntry = false;
        if(isEmpty(etMoveFL)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter a speed on the FL wheel!", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etMoveFR)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter a speed on the FR wheel!", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etMoveBL)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter a speed on the BL wheel!", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etMoveBR)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter a speed on the BR wheel!", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etMoveDuration)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter an amount of time!", Toast.LENGTH_SHORT).show();
        }
        if(!badEntry){
            boolean badRange = false;
            final String flSpeed = etMoveFL.getText().toString();
            final String frSpeed = etMoveFR.getText().toString();
            final String blSpeed = etMoveBL.getText().toString();
            final String brSpeed = etMoveBR.getText().toString();
            final String MoveDuration = etMoveDuration.getText().toString();
            if(!checkRange(-101,101,flSpeed)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Speed of FL is out of bounds, must be -100 to 100", Toast.LENGTH_SHORT).show();
            }
            if(!checkRange(-101,101,frSpeed)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Speed of FR is out of bounds, must be -100 to 100", Toast.LENGTH_SHORT).show();
            }
            if(!checkRange(-101,101,blSpeed)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Speed of BL is out of bounds, must be -100 to 100", Toast.LENGTH_SHORT).show();
            }
            if(!checkRange(-101,101,brSpeed)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Speed of BR is out of bounds, must be -100 to 100", Toast.LENGTH_SHORT).show();
            }
            if(!checkRange(0,101,MoveDuration)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Amount of time is out of bounds, must be 1 to 100", Toast.LENGTH_SHORT).show();
            }
            if(!badRange){
                final String MoveFuncName = "chassis_ctrl.set_wheel_speed";
                final String EnableAccelerationFuncName = "chassis_ctrl.enable_stick_overlay";
                robomaster.addToMessagesQueue(MoveFuncName + "(" +
                        flSpeed + "," + frSpeed + "," + blSpeed+","+brSpeed
                        + ")");
                robomaster.addToMessagesQueue(EnableAccelerationFuncName + "()");
                robomaster.addToMessagesQueue("time.sleep" + "(" + MoveDuration + ")");
                robomaster.addToMessagesQueue("chassis_ctrl.stop()");
                commandsArr.add("Drive" + "(" + flSpeed + "," + frSpeed + "," + blSpeed+","+brSpeed + "," + MoveDuration + ")");
                Toast.makeText(CommandActivity.this, "Added move to queue", Toast.LENGTH_LONG).show();
                refresh_lv();
            }
        }
    }

    /**
     * Adds the blinking command to the queue of commands if all parameters are in boundaries
     * @param v the view that received the client's click event
     */
    public void handleBlink(final View v){
        boolean badEntry = false;
        if(isEmpty(etBlinkR)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter a color on the R", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etBlinkG)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter a color on the G", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etBlinkB)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter a color on the B", Toast.LENGTH_SHORT).show();
        }
        if(isEmpty(etBlinkDuration)){
            badEntry = true;
            Toast.makeText(CommandActivity.this, "You did not enter an amount of time!", Toast.LENGTH_SHORT).show();
        }
        if(!badEntry){
            boolean badRange = false;
            final String R = etBlinkR.getText().toString();
            final String G = etBlinkG.getText().toString();
            final String B = etBlinkB.getText().toString();
            final String BlinkDuration = etBlinkDuration.getText().toString();
            if(!checkRange(-1,256,R)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Value of R, must be 0 to 255", Toast.LENGTH_SHORT).show();
            }
            if(!checkRange(-1,256,G)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Value of G, must be 0 to 255", Toast.LENGTH_SHORT).show();
            }
            if(!checkRange(-1,256,B)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Value of B, must be 0 to 255", Toast.LENGTH_SHORT).show();
            }
            if(!checkRange(0,101,BlinkDuration)){
                badRange = true;
                Toast.makeText(CommandActivity.this, "Amount of time is out of bounds, must be 1 to 100", Toast.LENGTH_SHORT).show();
            }
            if(!badRange){
                final String blinkBottom = "led_ctrl.set_bottom_led(rm_define.armor_bottom_all, " + R + ", " + G + ", " + B + ", rm_define.effect_breath)";
                final String blinkTop = "led_ctrl.set_top_led(rm_define.armor_top_all, " + R + ", " + G + ", " + B + ", rm_define.effect_breath)";
                robomaster.addToMessagesQueue(blinkBottom);
                robomaster.addToMessagesQueue(blinkTop);
                robomaster.addToMessagesQueue("time.sleep" + "(" + BlinkDuration + ")");
                commandsArr.add("Blink" + "(" + R + "," + G + "," + B+","+BlinkDuration + ")");
                if(permaRes!=null && !permaRes.equals("placeholder")){
                    final String blinkBottomPerma = "led_ctrl.set_bottom_led(rm_define.armor_bottom_all, " + permaRes + ", rm_define.effect_breath)";
                    final String blinkTopPerma = "led_ctrl.set_top_led(rm_define.armor_top_all, " + permaRes + ", rm_define.effect_breath)";
                    robomaster.addToMessagesQueue(blinkBottomPerma);
                    robomaster.addToMessagesQueue(blinkTopPerma);
                }
                Toast.makeText(CommandActivity.this, "Added blink to queue", Toast.LENGTH_LONG).show();
                refresh_lv();
            }
        }
    }

    /**
     * Adds the shooting command to the queue of commands
     * @param v the view that received the client's click event
     */
    public void handleShoot(final View v) {
        final String ShootFuncName = "gun_ctrl.set_fire_count";

        robomaster.addToMessagesQueue(ShootFuncName + "(1)");
        robomaster.addToMessagesQueue("gun_ctrl.fire_once" + "()");
        commandsArr.add("Shoot");
        Toast.makeText(CommandActivity.this, "Added shooting to queue", Toast.LENGTH_LONG).show();
        refresh_lv();
    }

    /**
     * Adds the permanent color of the LEDs to the queue
     * @param RGB the values of the LED
     */
    public void permaLed(String RGB){
        final String blinkBottom = "led_ctrl.set_bottom_led(rm_define.armor_bottom_all, " + RGB + ", rm_define.effect_breath)";
        final String blinkTop = "led_ctrl.set_top_led(rm_define.armor_top_all, " + RGB + ", rm_define.effect_breath)";
        Queue<String> tempLEDQueue = new LinkedList<String>();
        if(commandsArr.isEmpty()){
            robomaster.addToMessagesQueue(blinkBottom);
            robomaster.addToMessagesQueue(blinkTop);
            commandsArr.add(0,"Permanent(" + RGB + ")");
        }
        else if(commandsArr.get(0).contains("Permanent")){
            tempLEDQueue.add(blinkBottom);
            tempLEDQueue.add(blinkTop);
            resetQueueContain(tempLEDQueue);
            commandsArr.remove(0);
            commandsArr.add(0,"Permanent(" + RGB + ")");
        }
        else{
            tempLEDQueue.add(blinkBottom);
            tempLEDQueue.add(blinkTop);
            resetQueue(tempLEDQueue);
            commandsArr.add(0,"Permanent(" + RGB + ")");
        }
        refresh_lv();
    }

    /**
     * The function will handle using saved programs
     * @param program the commands to be sent to the robot
     * @param programDis the commands to be displayed to the user
     */
    public void loadSavedProgram(String[] program, String[] programDis){
        robomaster.clearQueue();
        for (String s : program) {
            robomaster.addToMessagesQueue(s);
        }
        commandsArr.clear();
        for (String s : programDis) {
            commandsArr.add(s);
        }
        refresh_lv();
    }

    /**
     * Executes all commands in the queue
     * @param v the view that received the client's click event
     */
    public void executeScript(final View v) {
        robomaster.sendMessagesQueue();
        commandsArr.clear();
        if(permaRes!=null && !permaRes.equals("placeholder")){
            commandsArr.add(0,"Permanent(" + permaRes + ")");
            final String blinkBottom = "led_ctrl.set_bottom_led(rm_define.armor_bottom_all, " + permaRes + ", rm_define.effect_breath)";
            final String blinkTop = "led_ctrl.set_top_led(rm_define.armor_top_all, " + permaRes + ", rm_define.effect_breath)";
            robomaster.addToMessagesQueue(blinkBottom);
            robomaster.addToMessagesQueue(blinkTop);
        }
        refresh_lv();
    }

    /**
     * Refresh the listview
     */
    public void refresh_lv(){
        ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,commandsArr);
        lvCommands.setAdapter(adp);
    }

    /**
     * The function will check if the given value is in the given range
     * @param min the minimum value possible
     * @param max the maximum value possible
     * @param val the value we check
     * @return true if the value is in the given range, false otherwise
     */
    public boolean checkRange(int min, int max, String val){
        String regex = "-?\\d+(\\.\\d+)?";
        if(val.matches(regex)){
            return min < Integer.parseInt(val) && max > Integer.parseInt(val);
        }
        return false;
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
     * The function will reset the commands queue to include the permanent color if a permanent color was already
     * picked
     * @param temp a queue with the permanent colors
     */
    public void resetQueueContain(Queue<String> temp){
        Queue<String> tempEventQueue = robomaster.getQ();
        tempEventQueue.remove();
        tempEventQueue.remove();
        while (!tempEventQueue.isEmpty()) {
            String currentMessage = tempEventQueue.remove();
            temp.add(currentMessage);
        }
        robomaster.clearQueue();
        while(!temp.isEmpty()){
            String curr = temp.remove();
            robomaster.addToMessagesQueue(curr);
        }
    }

    /**
     * The function will reset the commands queue to include the permanent color if a permanent color wasn't already
     * picked and if the commands queue is not empty
     * @param temp a queue with the permanent colors
     */
    public void resetQueue(Queue<String> temp){
        Queue<String> tempEventQueue = robomaster.getQ();
        while (!tempEventQueue.isEmpty()) {
            String currentMessage = tempEventQueue.remove();
            temp.add(currentMessage);
        }
        robomaster.clearQueue();
        while(!temp.isEmpty()){
            String curr = temp.remove();
            robomaster.addToMessagesQueue(curr);
        }
    }
}



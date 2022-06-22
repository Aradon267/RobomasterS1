package com.example.robomaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This is the options screen of the application that contains the ability to choose a main color for the robot.
 * If the user clicks on the back button he will be moved to the command page.
 * If the user clicks on a color from the image, the robot's LEDs will be changed accordingly on the next execute.
 * If the user writes a valid color, the robot's LEDs will be changed accordingly on the next execute.
 */

public class OptionsActivity extends AppCompatActivity {
    private ImageView imgCircle;
    private View colorDis;
    private Bitmap bitmap;
    private EditText etBlinkR, etBlinkG, etBlinkB;

    /**
     * The function will create and run the current screen(options) with the data from the saved state
     * @param savedInstanceState data saved to help activities start/restart themselves
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        initializeComponents();

        imgCircle.setDrawingCacheEnabled(true);
        imgCircle.buildDrawingCache(true);

        /**
         * Listen to clicks on the picture and changes the values in the edittext and of the bitmap accordingly.
         */
        imgCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = Bitmap.createBitmap(imgCircle.getDrawingCache());
                    if((int)motionEvent.getX() >= 0 && (int)motionEvent.getY() >= 0 && (int)motionEvent.getX() < (bitmap.getWidth() - 1) && (int)motionEvent.getY() < (bitmap.getHeight() - 1)){
                        int pixels = bitmap.getPixel((int)motionEvent.getX(),(int)motionEvent.getY());
                        int r, g, b;
                        r = Color.red(pixels);
                        g = Color.green(pixels);
                        b = Color.blue(pixels);

                        colorDis.setBackgroundColor(Color.rgb(r, g, b));
                        etBlinkR.setText(String.valueOf(r));
                        etBlinkG.setText(String.valueOf(g));
                        etBlinkB.setText(String.valueOf(b));
                    }
                }
                return true;
            }
        });


    }

    /**
     * Initialize the components in one function
     */
    private void initializeComponents() {
        imgCircle = findViewById(R.id.colorCircle);
        colorDis = findViewById(R.id.dis);
        etBlinkR = findViewById(R.id.etPermaBlinkR);
        etBlinkG = findViewById(R.id.etPermaBlinkG);
        etBlinkB = findViewById(R.id.etPermaBlinkB);

    }

    /**
     * The function will check if the given value is in the given range
     * @param min the minimum value possible
     * @param max the maximum value possible
     * @param val the value we check
     * @return true if the value is in the given range, false otherwise
     */
    public boolean checkRange(int min, int max, String val){
        String regex = "[0-9]+";
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
     * The function will handle creating an options menu
     * @param menu the menu
     * @return true for the menu to be displayed, false for not displaying it
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.color_options_menu,menu);
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
            case R.id.itmCheck:
                boolean badEntry = false;
                if(isEmpty(etBlinkR)){
                    badEntry = true;
                    Toast.makeText(OptionsActivity.this, "You did not enter a color on the R", Toast.LENGTH_SHORT).show();
                }
                if(isEmpty(etBlinkG)){
                    badEntry = true;
                    Toast.makeText(OptionsActivity.this, "You did not enter a color on the G", Toast.LENGTH_SHORT).show();
                }
                if(isEmpty(etBlinkB)){
                    badEntry = true;
                    Toast.makeText(OptionsActivity.this, "You did not enter a color on the B", Toast.LENGTH_SHORT).show();
                }
                if(!badEntry){

                    boolean badRange = false;
                    final String R = etBlinkR.getText().toString();
                    final String G = etBlinkG.getText().toString();
                    final String B = etBlinkB.getText().toString();
                    if(!checkRange(-1,256,R)){
                        badRange = true;
                        Toast.makeText(OptionsActivity.this, "Value of R, must be 0 to 255", Toast.LENGTH_SHORT).show();
                    }
                    if(!checkRange(-1,256,G)){
                        badRange = true;
                        Toast.makeText(OptionsActivity.this, "Value of G, must be 0 to 255", Toast.LENGTH_SHORT).show();
                    }
                    if(!checkRange(-1,256,B)){
                        badRange = true;
                        Toast.makeText(OptionsActivity.this, "Value of B, must be 0 to 255", Toast.LENGTH_SHORT).show();
                    }
                    if(!badRange){
                        colorDis.setBackgroundColor(Color.rgb(Integer.parseInt(R),Integer.parseInt(G),Integer.parseInt(B)));
                    }
                }
                break;
            case R.id.itmApply:
                boolean badEntryApp = false;
                if(isEmpty(etBlinkR)){
                    badEntryApp = true;
                    Toast.makeText(OptionsActivity.this, "You did not enter a color on the R", Toast.LENGTH_SHORT).show();
                }
                if(isEmpty(etBlinkG)){
                    badEntryApp = true;
                    Toast.makeText(OptionsActivity.this, "You did not enter a color on the G", Toast.LENGTH_SHORT).show();
                }
                if(isEmpty(etBlinkB)){
                    badEntryApp = true;
                    Toast.makeText(OptionsActivity.this, "You did not enter a color on the B", Toast.LENGTH_SHORT).show();
                }
                if(!badEntryApp){

                    boolean badRange = false;
                    final String R = etBlinkR.getText().toString();
                    final String G = etBlinkG.getText().toString();
                    final String B = etBlinkB.getText().toString();
                    if(!checkRange(-1,256,R)){
                        badRange = true;
                        Toast.makeText(OptionsActivity.this, "Value of R, must be 0 to 255", Toast.LENGTH_SHORT).show();
                    }
                    if(!checkRange(-1,256,G)){
                        badRange = true;
                        Toast.makeText(OptionsActivity.this, "Value of G, must be 0 to 255", Toast.LENGTH_SHORT).show();
                    }
                    if(!checkRange(-1,256,B)){
                        badRange = true;
                        Toast.makeText(OptionsActivity.this, "Value of B, must be 0 to 255", Toast.LENGTH_SHORT).show();
                    }
                    if(!badRange){

                        String color = R + ", " + G + ", " + B;
                        Intent intent = new Intent();
                        intent.putExtra("color",color);
                        setResult(200,intent);
                        finish();
                    }
                }
                break;
            case R.id.itmBack:
                Intent intent = new Intent();
                intent.putExtra("color","placeholder");
                setResult(200,intent);
                finish();
                break;
            case R.id.itmCam:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 789);
                break;
            case R.id.itmReset:
                imgCircle.destroyDrawingCache();
                imgCircle.setImageResource(R.drawable.colorwheel);
                imgCircle.setDrawingCacheEnabled(true);
                imgCircle.buildDrawingCache(true);
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

        if (requestCode == 789 && resultCode == RESULT_OK) {
            switchToCamera(data);
        }
    }

    /**
     * The function will switch the picture to the picture from the camera
     * @param data the intent that hold the picture
     */
    public void switchToCamera(Intent data){
        imgCircle.destroyDrawingCache();
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        imgCircle.setImageBitmap(imageBitmap);
        imgCircle.setDrawingCacheEnabled(true);
        imgCircle.buildDrawingCache(true);
    }
}
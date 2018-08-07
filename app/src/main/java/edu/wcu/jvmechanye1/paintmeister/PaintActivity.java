package edu.wcu.jvmechanye1.paintmeister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The PaintActivity class handles the interaction between fragments, text fields, and the
 * paint space for a finger painting app.
 *
 * @Author Justin Mechanye
 * @Date 4/27/18
 */
public class PaintActivity extends AppCompatActivity implements
        ColorPickFrag.OnFragmentInteractionListener, SaveFragment.OnFragmentInteractionListener{

    /**
     * tv1 is the text view that shows the current color, and tv2 shows the current width.
     */
    private TextView tv1, tv2;

    /**
     * Gains access to the Paint view in the activity.
     */
    private PaintView pv;

    /**
     * A boolean to check if we are using the SaveFragment or not.
     */
    private boolean savePressed;


    /**
     * Creates defaults for the PaintActivity.
     * @param savedInstanceState current saved preferences.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        //Set up the paint view.
        pv= findViewById(R.id.view1);

        //If there is a savedInstance state load it, otherwise load preferences.
        if(savedInstanceState!=null){
            pv.setWidth(Float.parseFloat(savedInstanceState.getString("Width")));
            pv.setColors(savedInstanceState.getString("BrushColor"));
        }else {
            loadPref(null);
        }

        //Set up default text fields.
        tv1= findViewById(R.id.current_color);
        tv2= findViewById(R.id.current_width);
        tv1.setText(pv.cString());
        tv2.setText(pv.widthValString());

        //Check if this view is a load and load if it is.
        runLoad();

        savePressed=false;
    }

    /**
     * This method checks if an extra is sent to the activity from the load list activity.
     * Should their be an extra we collect the file name and run load(String filename)
     */
    public void runLoad(){
        Bundle extra= getIntent().getExtras();
        String filename;
        if(extra != null){
            filename=extra.getString("image");
            load(filename);
        }
        //Do nothing if there isn't an extra.
    }




    /**
     * Builds the paint action bar with options such as: save, load, pick width.
     * @param menu The menu to be inflated.
     *
     * @return true to show the options menu false to turn off the options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.paint_menu, menu);
        return true;
    }

    /**
     * Event handler for any option selected by the above menu.
     * @param item the item pressed on the menu.
     * @return true to enact changes based on option select.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.load:
                loadButton();
                break;
            case R.id.save:
                saveButton();
                break;
            case R.id.width:
                width();
                break;
        }
        return true;
    }

    /**
     * When the save option his pressed in the Action bar this method is called.
     * if savePressed is true it brings up the ColorPickerFrag, and if savedPressed is false
     * it opens up the SaveFragment.
     */
    public void saveButton(){
        //Set up fragment Transaction
        FragmentManager fm= this.getSupportFragmentManager();
        FragmentTransaction fmt= fm.beginTransaction();
        Fragment switcher= null;
        //Check if the save button is pressed on click if it is change to color picker
        if(savePressed){
            savePressed=false;
            switcher= new ColorPickFrag();
        }else{
            savePressed=true;
            switcher= new SaveFragment();
        }

        //Replaces current fragment with the next fragment for use.
        fmt.replace(R.id.listFrag,switcher);
        fmt.commit();
    }
    /**
     * Saves each stroke in the painting area to a text file.
     * @param filename name of the file that will hold this painting.
     */
    public void save(String filename){
        String [] fileSplit= filename.split("\\s+");
        filename=this.generateFileName(fileSplit);
        //Set file name to filename.txt
        filename= filename+".txt";
        //create file if it doesn't exist.
        File file = new File(this.getFilesDir(), filename);
        //Collect each stroke to be put into the file.
        String string= pv.toString();
        //Save stroke information to file.
        FileOutputStream fos;
        try{
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        }catch(FileNotFoundException fne){
            Toast.makeText(this,R.string.saveNF,Toast.LENGTH_LONG).show();
            fne.printStackTrace();
        }catch(IOException ioe){
            Toast.makeText(this,R.string.saveIO,Toast.LENGTH_LONG).show();
            ioe.printStackTrace();
        }
        //Announce successful save.
        Toast.makeText(this, R.string.sComplete, Toast.LENGTH_SHORT).show();
    }

    /**
     * Starts load list activity from the paint activity.
     */
    public void loadButton(){
        Intent i= new Intent(this,FileList.class);
        startActivity(i);
    }


    /**
     * Load loads a file then updates the drawing view.
     * @param filename  The name of the file to load.
     */
    public void load(String filename){
        String line;
        PaintView.Line l;

        ArrayList<PaintView.Line> linesLoad;
        File loadIn = new File(this.getFilesDir(),  filename);

        try{
            BufferedReader bf= new BufferedReader(new FileReader(loadIn));

            while((line=bf.readLine())!= null){
                linesLoad=new ArrayList<>();
                //Parse the first line into segments.
                String[] contents = line.split(":");
                Log.v("size",""+contents.length);

                //Collect the color to be added to each line.
                String color= contents[0];
                float lineWidth= Float.parseFloat(contents[1]);
                //Starting at location 2 get coordinates at 2-5 and make a line.
                for(int i=2; i+3<contents.length;i+=4){
                    float x1 = Float.parseFloat(contents[i]);
                    float y1 = Float.parseFloat(contents[i+1]);
                    float x2 = Float.parseFloat(contents[i+2]);
                    float y2 = Float.parseFloat(contents[i+3]);
                    l=new PaintView.Line(x1,y1,x2,y2,lineWidth,color);
                    linesLoad.add(l);
                }
                //Put the arraylist of lines into PaintView.
                pv.setLines(linesLoad);
            }
            //Paint loaded file.
            pv.invalidate();
        }catch(FileNotFoundException fe){
            Toast.makeText(this,R.string.loadFail,Toast.LENGTH_LONG).show();
            Log.v("Error",fe.getMessage());
        }catch(IOException ioe){
            Toast.makeText(this,R.string.loadIO,Toast.LENGTH_LONG).show();
            Log.v("Error",ioe.getMessage());
        }

    }

    /**
     * This method sets the paint stroke width to a maximum of 18, if the user
     * presses the button to change the width past 18 it is set back to 4.
     */
    public void width(){
        if(pv.getWidthVal() < 18f) {
            pv.setWidth(pv.getWidthVal() + 2f);
            tv2.setText(pv.widthValString());
        }else {
            pv.setWidth(4f);
            tv2.setText(pv.widthValString());
        }
    }


    /**
     * Gets the selected color and tells the Paint View what color the paint should become.
     * @param color
     */
    @Override
    public void onFragmentInteraction(String color) {
        tv1.setText(color);
        pv.setColors(color);
    }

    /**
     * Handles the save button being pressed.
     * KNOWN BUG: When the edit text is blank it save a file "".txt
     * @param b  The string returned from the edit text in the fragment to be "FileName.txt"
     */
    @Override
    public void onButtonPress(String b) {
        if(b==null){
            Toast.makeText(this,R.string.emptyText,Toast.LENGTH_LONG).show();
        }else {
            String fileName= b;
            save(fileName);
        }
    }

    /**
     * This destroys the activity and saves preferences.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        savePref(null);
    }

    /**
     * Does a save with shared preferences.
     * @param v
     */
    private void savePref(View v){
        SharedPreferences sp= getSharedPreferences("SCPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putFloat("Width",pv.getWidthVal());
        editor.putString("BrushColor", pv.cString());

        editor.commit();
    }

    /**
     * Loads from shared Preferences (Default values.
     * @param v
     */
    private void loadPref(View v){
        SharedPreferences sp= getSharedPreferences("SCPref",Context.MODE_PRIVATE);
        pv.setColors(sp.getString("BrushColor","black"));
        pv.setWidth(sp.getFloat("Width", 4));
    }

    /**
     * Saves instance state.
     * @param savedInstanceState
     */
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("BrushColor",pv.cString());
        savedInstanceState.putString("Width",""+pv.getWidthVal());
    }

    /**
     * This method ensures that there are no white spaces in the fileName for saving.
     * @param fileParts the filename split by white space.
     * @return r the return string that is the fileName with no whitespace.
     */
    public String generateFileName(String[] fileParts){
        String r= fileParts[0];
        if(fileParts.length>1){
            for(int i =1; i<fileParts.length;i++){
                r= r+ fileParts[i];
            }
        }
        return r;
    }

}

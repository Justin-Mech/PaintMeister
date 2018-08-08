package edu.wcu.jvmechanye1.paintmeister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

/**
 * This is the Main Menu for the application with two buttons new, and load.
 *
 * @author Justin Mechanye
 * @version 4/27/18
 */
public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    /**
     * The button that loads a new PaintActivity.
     */
    private Button newB;
    /**
     * The button that loads the FileList Activity.
     */
    private Button loadB;

    /**
     * Creates the home page and sets up the buttons.
     * @param savedInstanceState used if preferences are saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //On create set buttons.
        newB= findViewById(R.id.main_button1);
        loadB= findViewById(R.id.main_button2);

    }

    /**
     * When a button is clicked this checks to see which button then changes activity from
     * the main menu to either the FileList, or the PaintActivity.
     * @param view the view being clicked.
     */
    @Override
    public void onClick(View view) {
        Intent i= null;
        if(view == newB){
            i= new Intent(this, PaintActivity.class);
        }else if(view== loadB){
            i= new Intent(this, FileList.class);
        }

        if(i != null) {
            startActivity(i);
        }
    }
}

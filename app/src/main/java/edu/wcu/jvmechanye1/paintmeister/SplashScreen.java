package edu.wcu.jvmechanye1.paintmeister;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * This activity acts as a splash screen for the application that lasts for
 * around five seconds.
 *
 * @author Jusitn Mechanye
 * @date 4/27/18
 */
public class SplashScreen extends AppCompatActivity {
    /**
     * The splash screen stays up for 5 seconds.
     */
    private final int WAIT=5000;

    /**
     * Create the splash screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    /**
     * Builds a Handler to allow splash screen to remain for five seconds.
     */
    @Override
    protected void onStart(){
        super.onStart();

        Handler h= new Handler();
        h.postDelayed(runner,WAIT);
    }

    /**
     * Builds the intent to go from the splash screen to the Main menu.
     */
    protected void next(){
        Intent next= new Intent(this,MainMenu.class);
        this.startActivity(next);
        this.finish(); //Make it so user can't get back to splash screen.
    }

    /**
     * Implementation of Runnable that runs the goToMain() method taking us from the splash
     * screen to the Main Menu.
     */
    final Runnable runner= new Runnable(){
        public void run(){
            next();
        }
    };
}

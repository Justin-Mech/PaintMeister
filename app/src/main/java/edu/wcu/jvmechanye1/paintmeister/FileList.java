package edu.wcu.jvmechanye1.paintmeister;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * This Activity lists all saved files for this application and allows you to select a file
 * to load into PaintActivity.
 *
 * @author Jusitn Mechanye
 * @version 4/27/18
 */
public class FileList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    /**
     * An arraylist of the files in the files internal directory.
     */
    private ArrayList<String> files;

    /**
     * The list view that can be interacted with.
     */
    private ListView lv;

    /**
     * Sets up the list view and adapter on creation of this activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        files= new ArrayList<>();
        listFiles(this.getFilesDir());
        lv= findViewById(R.id.file_list);
        
        ArrayAdapter<String> ad= new ArrayAdapter<String>(this,R.layout.filelistitem,files);
        lv.setAdapter(ad);
        
        lv.setOnItemClickListener(this);
    }

    /**
     * This program takes a file directory and builds and array list of file names.
     * @param dir the directory you would like to search for text files in.
     */
    public void listFiles(File dir){
        File[] list= dir.listFiles();

        for(File f: list){
            String [] fCheck= f.getName().split("\\.");
            if(fCheck.length>1) {
                if (fCheck[1].equals("txt")) {
                    files.add(f.getName());
                }
            }
        }
    }

    /**
     * If any of the items in this list are clicked this sends an extra with the file name
     * to PaintActivity.
     * @param adapterView the array adapter being used.
     * @param view the item being clicked
     * @param i  the item's id
     * @param l  the item's position.
     * @param l  the item's position.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent in= new Intent(this, PaintActivity.class);
        in.putExtra("image",((TextView)view).getText().toString());
        this.startActivity(in);
    }
}

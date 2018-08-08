package edu.wcu.jvmechanye1.paintmeister;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;


/**
 * The PaintView class is an implementation of the OnTouchListener that is used to read
 * Motion events and draw lines to the view of various colors and widths.
 *
 * @author  Justin Mechanye
 * @version 4/27/18
 */
public class PaintView extends View implements View.OnTouchListener {

    /**
     * p1 will be used to change the paint color and stroke width.
     */
    private Paint p1;

    /**
     * This float contains the stroke width
     */
    private float width;

    /**
     * This holds the name of a color
     */
    private String color;

    /**
     * This holds and ArrayList of lines to represent a single stroke on the screen.
     */
    public ArrayList<ArrayList<Line>> strokes;

    /**
     * Contains multiple lines for a drawing.
     */
    public ArrayList<Line> lines;

    /**
     * These both store the last (X,Y) use to draw a line from (X1,Y1),(X2,Y2)
     */
    private float lastX,lastY;

    /**
     * A constructor for the paint view.
     * @param context what activity is this view in
     * @param attrs a sett of attributes for this view.
     */
    public PaintView(Context context, AttributeSet attrs) {
        super(context,attrs);

        this.setOnTouchListener(this);
        p1= new Paint();

        this.strokes= new ArrayList<>();
    }

    /**
     * Used to set the stroke width.
     * @param w the new width of a paint stroke.
     */
    public void setWidth(float w){
        this.width=w;
    }

    /**
     * Gets the current width value.
     * @return float width.
     */
    public float getWidthVal(){
        return this.width;
    }

    /**
     * Sends the current value of the width as a string for the UI.
     * @return String value of the width
     */
    public String widthValString(){
        return " "+this.width;
    }

    /**
     * This sets the color of p1
     * @param color the integer value of a color
     */
    public void setPaintColor(int color){
        this.p1.setColor(color);
    }

    /**
     * Set the string of the current color
     * @param c  A string color value.
     */
    public void setColors(String c){
        color=c;
    }

    /**
     * Gets the string value of the current color.
     * @return String color
     */
    public String cString(){
        return color;
    }

    /**
     * This is used to add lines to strokes for loading in a file.
     * @param l a list of lines = 1 stroke.
     */
    public void setLines(ArrayList<Line> l){
        lines= l;
        strokes.add(lines);
    }


    /**
     * This method draws all strokes stored in the strokes arraylist to the view.
     * @param canvas the view to be drawn on.
     */
    @Override
    protected void onDraw(Canvas canvas){
        if(strokes != null){
            for(ArrayList<Line> al: strokes) {

                for (Line l : al) {
                    this.setUpPaintColor(l.getColorName());
                    p1.setStrokeWidth(l.getLineWidth());
                    canvas.drawLine(l.getX1(), l.getY1(), l.getX2(), l.getY2(), p1);
                }
            }
        }
    }

    /**
     * This detects if the view is touched and is used to collect coordinates to draw.
     * @param view  The view being touched
     * @param event  A motion event (ACTION_DOWN, ACTION_MOVE,ACTION_UP)
     * @return  true to draw.
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(lines == null) {
            lines = new ArrayList<Line>();
        }
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:


                lastX= x;
                lastY= y;
                break;

            case MotionEvent.ACTION_MOVE:
                Line line= new Line(x,y,lastX,lastY,width,color);
                lines.add(line);

                lastX=x;
                lastY=y;
                break;

            case MotionEvent.ACTION_UP:
                strokes.add(lines);
                Log.v("Size",""+strokes.size());
                lines = new ArrayList<>();
                break;

        }//end switch
        this.invalidate();
        return true;
    }

    /**
     * This method takes a string and sets the paint color based on the string.
     * @param color
     */
    private void setUpPaintColor(String color){
        switch (color){
            case "black":
                this.setPaintColor(Color.BLACK);
                break;
            case "white":
                this.setPaintColor(Color.WHITE);
                break;
            case "blue":
                this.setPaintColor(Color.BLUE);
                break;
            case "green":
                this.setPaintColor(Color.GREEN);
                break;
            case "yellow":
                this.setPaintColor(Color.YELLOW);
                break;
            case "magenta":
                this.setPaintColor(Color.MAGENTA);
                break;
            case "red":
                this.setPaintColor(Color.RED);
                break;
            case "cyan":
                this.setPaintColor(Color.CYAN);
                break;
        }
    }

    /**
     * The toString method takes each stroke of paint and appends each of their colors widths
     * and coordinates for each stroke into a single line to be saved.
     * @return  String- each stroke on an individual line.
     */
    public String toString(){
        StringBuilder returnVal= new StringBuilder();

        for(ArrayList<Line> al: strokes){
            Log.v("Amount of Lines",""+al.size());
            String color = al.get(0).getColorName();
            returnVal.append(color);
            returnVal.append(":");
            returnVal.append(al.get(0).getLineWidth());
            returnVal.append(":");
            for(Line l: al){
                returnVal.append(l.getX1());
                returnVal.append(":");
                returnVal.append(l.getY1());
                returnVal.append(":");
                returnVal.append(l.getX2());
                returnVal.append(":");
                returnVal.append(l.getY2());
                returnVal.append(":");
            }
            returnVal.append("\n");
        }

        return returnVal.toString();
    }
//##################################################################################################
//      End of PaintView.class
//##################################################################################################


    /**
     * Line is a class that holds the information of a single line to be drawn.
     * Many lines make up a single stroke.
     *
     * @author Justin Mechanye
     * @date 4/27/18
     */
    public static class Line{
        /**
         * The starting x coordinate
         */
        public float x1;
        /**
         * The starting y coordinate.
         */
        public float y1;
        /**
         * The next x coordinate used to form a line.
         */
        public float x2;
        /**
         * The next y coordinate used to form a line.
         */
        public float y2;
        /**
         * The width of this line.
         */
        public float lineWidth;

        /**
         * The color of this line.
         */
        public  String colorName;

        /**
         * A simple constructor for a line.
         * @param x1    The first x coordinate of the line.
         * @param y1    The first y coordinate of the line.
         * @param x2    the second x coordinate of the line
         * @param y2    The second y coordinate of the line
         * @param lineWidth     Width of this line.
         * @param colorName     Color of this line.
         */
        public Line(float x1, float y1, float x2, float y2, float lineWidth,String colorName){
            this.x1=x1;
            this.x2=x2;
            this.y1=y1;
            this.y2=y2;
            this.lineWidth=lineWidth;
            this.colorName=colorName;
        }

        /**
         * A getter method for the first x coordinate in a line.
         * @return x1
         */
        public float getX1() {
            return x1;
        }


        /**
         * A getter for the first y coordinate in a line.
         * @return y1
         */
        public float getY1() {
            return y1;
        }


        /**
         * A getter for the second x coordinate of a line.
         * @return x2
         */
        public float getX2() {
            return x2;
        }


        /**
         * A getter for the second y coordinate of a line.
         * @return  y2
         */
        public float getY2() {
            return y2;
        }

        /**
         * A getter for the width of a line.
         * @return lineWidth
         */
        public float getLineWidth() {
            return lineWidth;
        }

        /**
         * A getter for the color of a line.
         * @return colorName.
         */
        public String getColorName() {
            return colorName;
        }

    }//End of Line.class
}//EOF

/**
 * Name: Jennifer Vannier
 *
 * Program: ShiftDoorBlack.java
 *
 * Purpose: This serves as the image that is the goal of the level.
 */

import java.awt.Image;
import javax.swing.ImageIcon;

public class ShiftDoorBlack {
	
	private int x;
	private int y;
	private Image image;
	private int level = 0;
	
    public ShiftDoorBlack() {
    	ImageIcon temp = new ImageIcon(this.getClass().getResource("door.png"));
    	image = temp.getImage();
    	x = 0;
    	y = 0;
    	if (level == 0){
    		x = 348;
    		y = 137;
    	}
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    
    public Image getImage(){
		return image;
	}
	
	public void nextLevel(){
		level++;
	}
}
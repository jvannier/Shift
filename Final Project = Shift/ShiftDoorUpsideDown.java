/**
 * Name: Jennifer Vannier
 *
 * Program: ShiftDoorUpsideDown.java
 *
 * Purpose: This serves as the image that is the goal of the level.
 */

import java.awt.Image;
import javax.swing.ImageIcon;

public class ShiftDoorUpsideDown {
	
	private int x;
	private int y;
	private Image image;
	private int level = 0;
	
    public ShiftDoorUpsideDown() {
    	ImageIcon temp = new ImageIcon(this.getClass().getResource("doorUpsideDown.png"));
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
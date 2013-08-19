/**
 * Name: Jennifer Vannier
 *
 * Program: ShiftBlack.java
 *
 * Purpose: This is the player in black.
 */

import java.awt.Image;

import javax.swing.ImageIcon;

public class ShiftBlack extends ShiftCharacters{

	private Image image;
    
    public ShiftBlack(int xTemp, int yTemp, int x1Temp, int y1Temp){
    	super(xTemp, yTemp, x1Temp, y1Temp);    
    			
    	ImageIcon temp = new ImageIcon(this.getClass().getResource("shift.gif"));
    	image = temp.getImage();
    }
    
    public Image getImage(){
		return image;
	}

}
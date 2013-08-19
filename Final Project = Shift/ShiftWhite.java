/**
 * Name: Jennifer Vannier
 *
 * Program: ShiftWhite.java
 *
 * Purpose: This is the player in white.
 */

import java.awt.Image;

import javax.swing.ImageIcon;

public class ShiftWhite extends ShiftCharacters{

	private Image image;
    
    public ShiftWhite(int xTemp, int yTemp, int x1Temp, int y1Temp){
    	super(xTemp, yTemp, x1Temp, y1Temp);
    	
    	ImageIcon temp = new ImageIcon(this.getClass().getResource("shiftWhite.gif"));
    	image = temp.getImage();
    } 

	public Image getImage(){
		return image;
	}
}
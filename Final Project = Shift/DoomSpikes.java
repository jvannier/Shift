/**
 * Name: Jennifer Vannier
 *
 * Program: DoomSpikes.java
 *
 * Purpose: Spikes that will kill the player if the player has died more than five times.
 */
 
import java.awt.Image;
import javax.swing.ImageIcon;

import java.awt.event.KeyEvent;

public class DoomSpikes {
	
	private int x1;
	private int x;
	private int y;
	private Image img;
	
    public DoomSpikes() {
    	x = -900;
    	y = 25;
    	x1 = 1;
    	
    	ImageIcon spikes = new ImageIcon(this.getClass().getResource("DoomSpikes.png"));
    	img = spikes.getImage();
    }
    
    public void move(){
    	x+=x1;
    }
    
    public int getX(){
    	return x;
    }

	public int getY(){
		return y;
	}
	
	public Image getImage(){
		return img;
	}
}
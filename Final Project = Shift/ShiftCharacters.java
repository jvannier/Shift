/**
 * Name: Jennifer Vannier
 *
 * Program: ShiftCharacters.java
 *
 * Purpose: This generalizes common methods for the player characters for 
 *				easy editing.
 */

import java.awt.event.KeyEvent;

public class ShiftCharacters {
	
	private int x1;
	private int y1;
	private int x;
	private int y;
	
    public ShiftCharacters(int xTemp, int yTemp, int x1Temp, int y1Temp) {
    	x = xTemp;
    	y = yTemp;
    	x1 = x1Temp;
    	y1 = y1Temp;
    }
    
    public void move(){
    	x+=x1;
    	y+=y1;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getX1(){
    	return x1;
    }

	public int getY(){
		return y;
	}
	
	public int getY1(){
		return y1;
	}
	
	public void setX (int xTemp){
		x = xTemp;
	}
	
	public void setX1(int x1Temp){
		x1 = x1Temp;
	}
	
	public void setY (int yTemp){
		y = yTemp;
	}
	
	public void setY1 (int y1Temp){
		y1 = y1Temp;
	}
	
	
	//KeyListener
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT){
			x1 = -1;
		}
		
		if (key == KeyEvent.VK_RIGHT){
			x1 = 1;
		}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT){
			x1 = 0;
		}
		
		if (key == KeyEvent.VK_RIGHT){
			x1= 0;
		}
		
		if (key == KeyEvent.VK_UP){
			y1= 0;
		}
	}
}
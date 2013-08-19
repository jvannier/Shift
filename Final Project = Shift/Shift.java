/**
 * Name: Jennifer Vannier
 *
 * Program: Shift.java
 *
 * Purpose: This is the actual coding for all the intricacies of the player
 *				having several levels, being able to shift, not running into
 *				walls, jumping, falling, dying, and so on and so forth.
 */
 
import java.awt.*; 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;

import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.ImageIcon;

import java.awt.image.BufferStrategy;

public class Shift extends JFrame implements ActionListener{
	
	private Timer timer;
	
	private int level = 0;
	
	//for checking. n = null, r = right, l = left, j = jump
	private char keyDirection = 'n';
	
	//characters involved	
	private ShiftBlack guyB = new ShiftBlack(10, 137, 0, 0);
	private ShiftWhite guyW = new ShiftWhite(342, 137, 0, 0);
	private ShiftDoorBlack doorB = new ShiftDoorBlack();
	private ShiftDoorUpsideDown doorU = new ShiftDoorUpsideDown();
	private ShiftDoorWhite doorW = new ShiftDoorWhite();
	private ShiftDoorWhiteUpsideDown doorWU = new ShiftDoorWhiteUpsideDown();
	private DoomSpikes doom = new DoomSpikes();
	
	//needed global variables for jumping and gravity
	private boolean jump = false;
	private int tempYB = guyB.getY();
	private int tempYW = guyW.getY();
	private int steps = 0;
	private int direction = 0; //0 = right, 1 = left
	
	//used for Shifting
	//		true = black; false = white;
	private boolean shiftColor = true;
	private int changeColor = 0;
	//level6 shifting
	private int shifts = 0;	
	boolean canShift = true;
	
	//for sliders/keys
	private boolean keyFound = false;
	
	//for printing the number of deaths and choosing the ending
	private int deaths = 0;
	private boolean dead = false;
	
    public Shift() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(400, 400);
    	setLocationRelativeTo(null);
    	setTitle("Shift");
    	setResizable(false);
    	setVisible(true);
    	
    	addKeyListener(new TAdapter());
    	
    	createBufferStrategy(110);
    	  
    	guyB = new ShiftBlack(10, 136, 0, 0);
    	guyW = new ShiftWhite(342, 137, 0, 0);
    		
    	timer = new Timer(5, this);
    	timer.start(); 
    	
    	while (true){
    		long start = System.currentTimeMillis();
    		loop();
    		while (System.currentTimeMillis() - start < 5){
    			//nothing
    		}
    	}   	 		  	
    }
    
    //loops it so for things such as jumping and gravity it can have
    //		an animated action    
    private void loop(){
    	ShiftCharacters guy;
    	int tempY;
    	if(shiftColor){
    		guy = guyB;
    		tempY = tempYB;
    	}else{
    		guy = guyW;
    		tempY = tempYW;
    	}
		
		//for changing between checking the white ground and the black ground
		boolean flag = false;
		
    	//right
    	if (direction != 1){
		    //jumping
		    if(jump){
		    	//checks if hitting a wall [if it is, fall to ground]
				if (!(check(0))){
					guy.setX1(0);
					flag = false;
					if(shiftColor){
						flag = checkGroundBlack();
					}else{
						flag = checkGroundWhite();
					}
					
					if(!flag){			
						guy.setY1(1);
					}else{
						guy.setY1(0);
						
						//waits for it to finish falling before it takes further
						//		input from the user	
						jump = false;						
					}
				}else{//else can jump   
					//up
			    	if((guy.getY() > tempY - 40) && (steps == 0)){
			    		guy.setX1(1);
			    		guy.setY1(-1);
			    		
			    		if(checkTop()){
			    			tempY = tempY + 40;
			    		}
			    	}
			    	//down
			    	if ((guy.getY() <= tempY - 40) || (steps > 0)){
			    		steps++;
			    		guy.setY1(1);
			    	
				   		//if hit ground stop dropping
				   		flag = false;
						if(shiftColor){
							flag = checkGroundBlack();
						}else{
							flag = checkGroundWhite();
						}
				   		if(flag){
				   			guy.setY1(0);
				   			guy.setX1(0);
				   			jump = false;
				   			steps = 0;
				   		}
			    	}
				}	
		    }else{//not jumping
		    	tempY = guy.getY();
					
				//if not jumping check gravity
				//if not on ground, fall
				flag = false;
				if(shiftColor){
					flag = checkGroundBlack();
				}else{
					flag = checkGroundWhite();
				}
				
				if(!flag){
					guy.setY1(1);
				}else{
					//else stay put
					guy.setY1(0);
				}

				//checks if hitting a wall to the right
				while ((!(check(0))) && (keyDirection == 'r')){
					//check gravity --> if not on ground, fall
					//[doing again because it needs to be checked in the
					//		while loop as well as if it's not hitting
					//		a wall]
					if(shiftColor){
						flag = checkGroundBlack();
					}else{
						flag = checkGroundWhite();
					}
				
					if(!flag){
						guy.setY1(1);
					}else{
						//else stay put
						guy.setY1(0);
					}
					guy.setX1(0);
		    	}		
		    }
    	}else{//left
		    //jumping
		    if(jump){
		    	//checks if hitting a wall [if it is, fall to ground]
				if (!(check(1))){
					guy.setX1(0);
					
					//checks for gravity
					flag = false;
					if(shiftColor){
						flag = checkGroundBlack();
					}else{
						flag = checkGroundWhite();
					}

					if(!flag){
						guy.setY1(1);
					}else{
						guy.setY1(0);
						
						//doesn't take input until finished falling
						jump = false;
					}
				}else{//can can jump
					//up
			    	if((guy.getY() > tempY - 40) && (steps == 0)){
			    		guy.setX1(-1);
			    		guy.setY1(-1);
			    		
			    		if(checkTop()){
			    			tempY = tempY + 40 + guy.getY();
			    		}
			    	}
			    	//down
			    	if ((guy.getY() <= tempY - 40) || (steps > 0)){
			    		steps++;
			    		guy.setY1(1);

				   		//if hit ground stop dropping
				   		flag = false;
						if(shiftColor){
							flag = checkGroundBlack();
						}else{
							flag = checkGroundWhite();
						}
				   		if(flag){
				   			guy.setY1(0);
				   			guy.setX1(0);
				   			jump = false;
				   		}
			    	}
				}
		    }else{//not jumping
		    	tempY = guy.getY();
		    	steps = 0;

				//if not jumping check gravity
				//if not on ground, fall
				flag = false;
				if(shiftColor){
					flag = checkGroundBlack();
				}else{
					flag = checkGroundWhite();
				}
				//check for if it isn't hitting a wall
				if(!flag){
					guy.setY1(1);
				}else{
					//else stay put
					guy.setY1(0);
				}
				
				//checks if it hits the left side of the window
				while ((!(check(1))) && (keyDirection == 'l')){
					//if not jumping check gravity
					//if not on ground, fall
					//[doing again because it needs to be checked in the
					//		while loop as well as if it's not hitting
					//		a wall]
					if(shiftColor){
						flag = checkGroundBlack();
					}else{
						flag = checkGroundWhite();
					}
					if(!flag){
						guy.setY1(1);	
					}else{
						//else stay put
						guy.setY1(0);
					}
					guy.setX1(0);
		    	}
		    }
    	}
    }
    
    //Welcome + Controls page
    public void level0(Graphics g){
    	Graphics2D temp = (Graphics2D) g;
    	
    	//white background
    	temp.setColor(Color.white);
		temp.fillRect(3, 25, 394, 370);
		
		//text
		temp.setColor(Color.black);
		temp.setFont(new Font("Vivaldi", Font.BOLD, 25));
		temp.drawString("Welcome to Shift", 115, 75);
		temp.setFont(new Font("Arial", Font.PLAIN, 12));
		temp.drawString("As programmed by Jennifer Vannier", 107, 110);
		temp.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		temp.drawString("Reach the Door. Good Luck.", 109, 140);
		temp.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		temp.drawString("Controls:", 30, 165);
		temp.drawString("Shift             :        Shifts ;)", 110, 190);
		temp.drawString("Left Arrow     :        Moves Left", 110, 220); 
		temp.drawString("Right Arrow    :        Moves Right", 110, 250);
		temp.drawString("Space Bar       :        Jump", 110, 280);
		temp.drawString("R                  :        Restarts the level", 110, 310);
		temp.drawString("Q                  :        Quit", 110, 340);
		temp.setFont(new Font("Times New Roman", Font.BOLD, 16));
		temp.drawString("Press Enter to Start", 130, 380);
    }

    public void level1 (Graphics g){
    	Graphics2D temp = (Graphics2D) g;
    	
    	//true = black
	   	if (shiftColor){
		   	//background white
		   	temp.setColor(Color.white);
		   	temp.fillRect(3, 25, 394, 370);	
	    		
	    	temp.setColor(Color.black);
	   		temp.fillRect(3, 185, 394, 212);
	    	
	    	//text
	    	temp.drawString("OH LOOK; A DOOR! GO GO GO!", 100, 50);
	    		
	   		//door
		   	temp.drawImage(doorB.getImage(), doorB.getX(), doorB.getY(), this);
		   	//guy
		   	temp.drawImage(guyB.getImage(), guyB.getX(), guyB.getY(), this);
		    	
		   	if(changeColor == 2){
		   		guyB.setX(370 - guyW.getX());
		   	}
		   	changeColor = 1;  	 		
	   	}else{//white
	   		if(changeColor == 1){
	   			guyW.setX(370-guyB.getX());
	   		}
	    		
	   		//background black	    		
	   		temp.setColor(Color.black);
	    	temp.fillRect(3, 25, 394, 370);
	    		
	    	temp.setColor(Color.white);
	    	temp.fillRect(3, 185, 394, 212);
	    	
	    	//text
	    	temp.setColor(Color.white);
	    	temp.drawString("OH LOOK; A DOOR! GO GO GO!", 100, 50);
	    	
	   		//door
	   		temp.drawImage(doorU.getImage(), 352 - doorB.getX(), doorU.getY() + 48, this);
	   		//guy
	   		temp.drawImage(guyW.getImage(), guyW.getX(), guyW.getY(), this);
	   		changeColor = 2;
	   	}
    }   
    	
    public void level2 (Graphics g){
   		Graphics2D temp = (Graphics2D) g;	
     	 		
	 	//black
	 	if(shiftColor){
			//background white
		   	temp.setColor(Color.white);
		   	temp.fillRect(3, 25, 394, 370);	
		    		
	 		//spikes
	 		ImageIcon spikes = new ImageIcon(this.getClass().getResource("spikesBlack.png"));
	 		Image imageSpikes = spikes.getImage();
	 		temp.drawImage(imageSpikes, 99, 347, this);
	 		temp.drawImage(imageSpikes, 246, 347, this); 		    		
		    		
		   	//ground
    		temp.setColor(Color.black);
		   	temp.fillRect(3, 185, 97, 212);
		   	temp.fillRect(149, 185, 97, 212);
		   	temp.fillRect(296, 185, 101, 212);    	  				
   			
   			//text
	    	temp.drawString("Those spikes look kind of dangerous...", 130, 45);
	    	temp.drawString("You might want to  SPACE  over them.", 155, 100);
   				
    		//door
		   	temp.drawImage(doorB.getImage(), doorB.getX(), doorB.getY(), this);
		   	//guy
		   	temp.drawImage(guyB.getImage(), guyB.getX(), guyB.getY(), this);
		    	
		   	if(changeColor == 2){
		   		guyB.setX(370 - guyW.getX());
		   		guyB.setY(326 - guyW.getY());
		   	}
		   	changeColor = 1;
	 	}else{//white
	 		if(changeColor == 1){
	   			guyW.setX(370-guyB.getX());
	   			guyW.setY(326-guyB.getY());
	    	}
	    		
	 		//background black
	 		temp.setColor(Color.black);
	 		temp.fillRect(3, 25, 394, 370);		
		 				
	 		//ground	    	
		   	temp.setColor(Color.white);
		   	temp.fillRect(3, 237, 394, 160);
		   	temp.fillRect(104, 25, 50, 212);
		   	temp.fillRect(251, 25, 50, 212);		    	

	 		//spikes
	 		ImageIcon spikes = new ImageIcon(this.getClass().getResource("spikesWhite.png"));
	 		Image imageSpikes = spikes.getImage();
	 		temp.drawImage(imageSpikes, 104, 25, this);
	 		temp.drawImage(imageSpikes, 251, 25, this); 
	 		
	 		//text
	 		temp.setColor(Color.white);
	    	temp.drawString("Those spikes look kind of dangerous...", 130, 45);
	    	temp.drawString("You might want to  SPACE  over them.", 155, 100);
	 		
	 		//door
	 		temp.drawImage(doorU.getImage(), 352 - doorB.getX(), doorU.getY() + 100, this);
	 		//guy
	 		temp.drawImage(guyW.getImage(), guyW.getX(), guyW.getY(), this);
	 		changeColor = 2;
	 	}
    }

    public void level3 (Graphics g){
   		Graphics2D temp = (Graphics2D) g; 
   			
   		//black
	 	if(shiftColor){
			//background white
		   	temp.setColor(Color.white);
		   	temp.fillRect(3, 25, 394, 370);	
		    		
	 		//spikes
	 		ImageIcon spikes = new ImageIcon(this.getClass().getResource("spikesBlackLarge.png"));
	 		Image imageSpikes = spikes.getImage();
	 		temp.drawImage(imageSpikes, 99, 347, this);
	 		temp.drawImage(imageSpikes, 224, 347, this); 		    		
		    		
		   	//ground
    		temp.setColor(Color.black);
		   	temp.fillRect(3, 185, 97, 212);
		   	temp.fillRect(174, 185, 50, 212);
		   	temp.fillRect(299, 185, 100, 212);
		   	temp.fillRect(360, 155, 37, 30);				
   			
    		//door
		   	temp.drawImage(doorB.getImage(), 237, 285, this);  
		   		 
			//slider image
   			ImageIcon slider = new ImageIcon(this.getClass().getResource("SliderBlack.png"));
   			Image imageSlider = slider.getImage();
   				
   			if ((!checkKey()) && (!keyFound)){
   				//rectangle around door
   				temp.drawRect(237, 285, 48, 48);
   				//key
   				ImageIcon key = new ImageIcon(this.getClass().getResource("KeyBlack.gif"));
   				Image imageKey = key.getImage();
   				temp.drawImage(imageKey, 364, 125, this);
				//slider
   				temp.drawImage(imageSlider, 154, 333, this);
   			}else{
   				keyFound = true;
   				//slider
   				temp.drawImage(imageSlider, 224, 333, this);
   			}
   			
   			//text
   			temp.drawString("A key....okaayyy.", 120, 45);
   			
		   	//guy
		   	temp.drawImage(guyB.getImage(), guyB.getX(), guyB.getY(), this);
		   	
		   	if(changeColor == 2){
		   		guyB.setX(370 - guyW.getX());
		   		guyB.setY(326 - guyW.getY());
		   	}
		   	changeColor = 1;
	 	}else{//white
	 		if(changeColor == 1){
	   			guyW.setX(370-guyB.getX());
	   			guyW.setY(326-guyB.getY());
	   		}
	    		
	 		//background black
	 		temp.setColor(Color.black);
	 		temp.fillRect(3, 25, 394, 370);		
		    		    			 				 			
	 		//ground
	 		temp.setColor(Color.white);	    	
			temp.fillRect(3, 277, 394, 150);
			temp.fillRect(40, 247, 357, 30);
			temp.fillRect(101, 25, 75, 222);
			temp.fillRect(226, 25, 75, 222);
			
	 		//spikes
	 		ImageIcon spikes = new ImageIcon(this.getClass().getResource("spikesWhiteLarge.png"));
	 		Image imageSpikes = spikes.getImage();
	 		temp.drawImage(imageSpikes, 101, 25, this);
	 		temp.drawImage(imageSpikes, 226, 25, this); 
	 					
			//text
			temp.setColor(Color.white);
   			temp.drawString("A key....okaayyy.", 120, 45);
   			
	 		//door
	 		temp.drawImage(doorU.getImage(), 113, 87, this);
	 		
   			//slider image
   			ImageIcon slider = new ImageIcon(this.getClass().getResource("SliderWhite.png"));
   			Image imageSlider = slider.getImage();
   			
   			if (!keyFound){
   				//rectangle around door
   				temp.setColor(Color.black);
   				temp.drawRect(113, 87, 48, 48);
   				//key
   				ImageIcon key = new ImageIcon(this.getClass().getResource("KeyUpsideDown.gif"));
   				Image imageKey = key.getImage();
   				temp.drawImage(imageKey, 6, 277, this);
				//slider
   				temp.drawImage(imageSlider, 176, 75, this);
   			}else{
   				keyFound = true;
   				//slider
   				temp.drawImage(imageSlider, 106, 75, this);
   			}

	 		//guy
	 		temp.drawImage(guyW.getImage(), guyW.getX(), guyW.getY(), this);
	 		changeColor = 2;
	 	}	 			
    }
   	
   	public void level4 (Graphics g){
   		Graphics2D temp = (Graphics2D) g;
   			 		
   		//black
	 	if(shiftColor){
			//background white
		   	temp.setColor(Color.white);
		   	temp.fillRect(3, 25, 394, 370);		    		
		    		
		   	//ground
    		temp.setColor(Color.black);
			temp.fillRect(3, 215, 60, 212);
			temp.fillRect(63, 245, 60, 212);
			temp.fillRect(123, 275, 60, 212);
			temp.fillRect(183, 305, 60, 212);
			temp.fillRect(243, 335, 80, 212);
			temp.fillRect(323, 215, 74, 212);
			temp.fillRect(243, 215, 50, 30);			
   			
   			//text
   			temp.drawString("When all else fails, SHIFT your life around.", 50, 55);
   			
    		//door
		   	temp.drawImage(doorB.getImage(), 244, 167, this);  

		   	//guy
		   	temp.drawImage(guyB.getImage(), guyB.getX(), guyB.getY(), this);
		   	
		   	if(changeColor == 2){
		   		guyB.setX(370 - guyW.getX());
		   		guyB.setY(326 - guyW.getY());	   		
		   	}
		   	changeColor = 1;
	 	}else{//white
	 		if(changeColor == 1){
	   			guyW.setX(370-guyB.getX());
	   			guyW.setY(326-guyB.getY());
	   		}
	    		
	 		//background black
	 		temp.setColor(Color.black);
	 		temp.fillRect(3, 25, 394, 370);
		    		    			 				 			
	 		//ground
	 		temp.setColor(Color.white);	    	
		    temp.fillRect(3, 207, 74, 190);
		    temp.fillRect(77, 87, 80, 90);
		    temp.fillRect(77, 177, 30, 30);
		    temp.fillRect(77, 207, 80, 220);
		    temp.fillRect(157, 117, 60, 280);
		    temp.fillRect(217, 147, 60, 250);
		    temp.fillRect(277, 177, 60, 220);
		    temp.fillRect(337, 207, 60, 190);
	 		
	 		//text
   			temp.setColor(Color.white);
   			temp.drawString("When all else fails, SHIFT your life around.", 50, 55);
	 		temp.drawString("Exactly. :D", 300, 105);
	 		
	 		//door
	 		temp.drawImage(doorU.getImage(), 108, 207, this); 		
			
			temp.drawImage(guyW.getImage(), guyW.getX(), guyW.getY(), this);
	 		changeColor = 2;
	 	}	 			
   	} 
   			 
   	public void level5 (Graphics g){
   		Graphics2D temp = (Graphics2D) g;	
   		
   		//black
	 	if(shiftColor){
			//background white
		   	temp.setColor(Color.white);
		   	temp.fillRect(3, 25, 394, 370);		    		
		    		
		   	//ground
    		temp.setColor(Color.black);			
   			temp.fillRect(3, 85, 50, 60);
   			temp.fillRect(3, 235, 50, 162);
   			temp.fillRect(53, 335, 60, 62);
   			temp.fillRect(113, 85, 75, 312);
   			temp.fillRect(138, 75, 50, 10);
   			temp.fillRect(93, 85, 20, 60);
   			temp.fillRect(188, 250, 34, 147);
   			temp.fillRect(222, 285, 75, 112);
   			temp.fillRect(297, 278, 50, 122);
   			temp.fillRect(347, 75, 50, 322);
   			temp.fillRect(222, 90, 75, 50);
   			
   			//spikes
	 		//small
	 		ImageIcon spikesS = new ImageIcon(this.getClass().getResource("spikesBlack.png"));
	 		Image imageSpikesS = spikesS.getImage();
	 		temp.drawImage(imageSpikesS, 138, 25, this);
	 		//large
	 		ImageIcon spikesL = new ImageIcon(this.getClass().getResource("spikesBlackLarge.png"));
	 		Image imageSpikesL = spikesL.getImage();
	 		temp.drawImage(imageSpikesL, 222, 250, this);
   			
   			//slider image
   			ImageIcon slider = new ImageIcon(this.getClass().getResource("SliderBlack.png"));
   			Image imageSlider = slider.getImage();
   				
   			if ((!checkKey()) && (!keyFound)){
   				//key
   				ImageIcon key = new ImageIcon(this.getClass().getResource("KeyWhite.gif"));
   				Image imageKey = key.getImage();
   				temp.drawImage(imageKey, 244, 110, this);
				//slider
   				temp.drawImage(imageSlider, 118, 239, this);
   			}else{
   				keyFound = true;
   				//slider
   				temp.drawImage(imageSlider, 187, 239, this);
   			}
   			
    		//door
		   	temp.drawImage(doorWU.getImage(), 3, 85, this);  

		   	//guy
		   	temp.drawImage(guyB.getImage(), guyB.getX(), guyB.getY(), this);
		   	
		   	if(changeColor == 2){
		   		guyB.setX(370 - guyW.getX());
		   		guyB.setY(326 - guyW.getY());	   		
		   	}
		   	changeColor = 1;
	 	}else{//white
	 		if(changeColor == 1){
	   			guyW.setX(370-guyB.getX());
	   			guyW.setY(326-guyB.getY());
	   		}
	    		
	 		//background black
	 		temp.setColor(Color.black);
	 		temp.fillRect(3, 25, 394, 370);
		    		    			 				 			
	 		//ground
	 		temp.setColor(Color.white);	    	
	 		temp.fillRect(3, 347, 209, 50);
	 		temp.fillRect(53, 144, 50, 203);
	 		temp.fillRect(103, 172, 109, 110);
	 		temp.fillRect(103, 332, 109, 15);
	 		temp.fillRect(178, 282, 34, 50);
	 		temp.fillRect(287, 87, 60, 190);
	 		temp.fillRect(307, 277, 40, 60);
	 		temp.fillRect(262, 337, 135, 60);
	 		temp.fillRect(347, 187, 50, 90);
	 		
	 		//spikes
	 		//small
	 		ImageIcon spikesS = new ImageIcon(this.getClass().getResource("spikesWhite.png"));
	 		Image imageSpikesS = spikesS.getImage();
	 		temp.drawImage(imageSpikesS, 212, 347, this);
	 		//large
	 		ImageIcon spikesL = new ImageIcon(this.getClass().getResource("spikesWhiteLarge.png"));
	 		Image imageSpikesL = spikesL.getImage();
	 		temp.drawImage(imageSpikesL, 103, 122, this);
	 		
	 		//slider image
   			ImageIcon slider = new ImageIcon(this.getClass().getResource("SliderWhite.png"));
   			Image imageSlider = slider.getImage();
   			
   			if ((!checkKey()) && (!keyFound)){
   				//key
   				ImageIcon key = new ImageIcon(this.getClass().getResource("KeyWhiteUpsideDown.gif"));
   				Image imageKey = key.getImage();
   				temp.drawImage(imageKey, 126, 282, this);
				//slider
   				temp.drawImage(imageSlider, 212, 171, this);
   			}else{
   				keyFound = true;
   				//slider
   				temp.drawImage(imageSlider, 143, 171, this);
   			}
	 		   		
   			temp.setColor(Color.white);
   			temp.drawString("Hi! Remember R restarts the level.", 75, 60);
   		
	 		//door
	 		temp.drawImage(doorW.getImage(), 349, 289, this); 		
			
			temp.drawImage(guyW.getImage(), guyW.getX(), guyW.getY(), this);
	 		changeColor = 2;
	 	}
   	}
   	
   	public void level6(Graphics g){
   		Graphics2D temp = (Graphics2D) g;
	 		
   		//black
	 	if(shiftColor){
			//background white
		   	temp.setColor(Color.white);
		   	temp.fillRect(3, 25, 394, 371);		    		
		    		
		   	//ground
    		temp.setColor(Color.black);
   			temp.fillRect(3, 25, 50, 274);
   			temp.fillRect(53, 299, 244, 50);
   			temp.fillRect(103, 125, 50, 100);
   			temp.fillRect(153, 75, 144, 50);
   			temp.fillRect(153, 175, 94, 50);
   			temp.fillRect(247, 125, 100, 50);
   			temp.fillRect(297, 175, 50, 174);
   			
   			//slider image
   			ImageIcon slider = new ImageIcon(this.getClass().getResource("SliderBlack.png"));
   			Image imageSlider = slider.getImage();
			
   			if ((!checkKey()) && (!keyFound)){
   				//key				
   				if(shifts >= 4){
   					ImageIcon key = new ImageIcon(this.getClass().getResource("KeyWhiteUpsideDown.gif"));
   					Image imageKey = key.getImage();
   					temp.drawImage(imageKey, 113, 125, this);
   					
   					//text
   					temp.drawString("Key wuz here.", 155, 145);				
   					temp.setFont(new Font("Vivaldi", Font.PLAIN, 18));					
					temp.drawString("Moved", 190, 165);
   				}else{
   					ImageIcon key = new ImageIcon(this.getClass().getResource("KeyBlack.gif"));
   					Image imageKey = key.getImage();
   					temp.drawImage(imageKey, 184, 145, this);
   				}

				//slider
   				temp.drawImage(imageSlider, 346, 125, this);
   			}else{
   				keyFound = true;
   				//no slider
   			}	
   			
    		//door
		   	temp.drawImage(doorB.getImage(), 3, 349, this);  

		   	//guy
		   	temp.drawImage(guyB.getImage(), guyB.getX(), guyB.getY(), this);
		   	
		   	if(changeColor == 2){
		   		guyB.setX(370 - guyW.getX());
		   		guyB.setY(326 - guyW.getY());	   		
		   	}
		   	changeColor = 1;
	 	}else{//white
	 		if(changeColor == 1){
	   			guyW.setX(370-guyB.getX());
	   			guyW.setY(326-guyB.getY());
	   		}
	    		
	 		//background black
	 		temp.setColor(Color.black);
	 		temp.fillRect(3, 25, 394, 372);
		   		    			 				 			
	 		//ground
	 		temp.setColor(Color.white);	    	
	 		temp.fillRect(3, 25, 394, 48);
	 		temp.fillRect(3, 73, 50, 330);
	 		temp.fillRect(347, 73, 50, 50);
	 		temp.fillRect(53, 297, 50, 100);
	 		temp.fillRect(103, 347, 244, 50);
	 		temp.fillRect(247, 297, 100, 50);
	 		temp.fillRect(153, 247, 94, 50);
	 		temp.fillRect(297, 123, 50, 175);
	 		temp.fillRect(103, 123, 200, 74);
	 		temp.fillRect(103, 197, 50, 50);

	 		
	 		//slider image
   			ImageIcon slider = new ImageIcon(this.getClass().getResource("SliderWhite.png"));
   			Image imageSlider = slider.getImage();
   			
   			if ((!checkKey()) && (!keyFound)){
   				//key 				
   				if(shifts >= 4){
   					ImageIcon key = new ImageIcon(this.getClass().getResource("KeyWhite.gif"));
   					Image imageKey = key.getImage();
   					temp.drawImage(imageKey, 257, 267, this);	 		
   						
   					//text
	 				ImageIcon text = new ImageIcon(this.getClass().getResource("lvl6 text.png"));
	 				Image textImg = text.getImage();
	 				temp.drawImage(textImg, 153, 247, this);
   				}else{
   				   	ImageIcon key = new ImageIcon(this.getClass().getResource("KeyUpsideDown.gif"));
   					Image imageKey = key.getImage();
   					temp.drawImage(imageKey, 186, 247, this);
   				}
   				
				//slider
   				temp.drawImage(imageSlider, -16, 285, this);
   			}else{
   				keyFound = true;
   				//no slider
   			}
	 		
	 		//door
	 		temp.drawImage(doorU.getImage(), 349, 25, this); 		
			
			//guy
			temp.drawImage(guyW.getImage(), guyW.getX(), guyW.getY(), this);
	 		changeColor = 2;
	 	}
   	}
   	
   	//death screen
   	public void drawDead(Graphics g){
   		Graphics2D temp = (Graphics2D) g;
   		
   		//background black
		temp.setColor(Color.black);
		temp.fillRect(3, 25, 394, 370);
		
		//text
		temp.setColor(Color.white);
		temp.setFont(new Font("Century Gothic", Font.BOLD, 25));
		temp.drawString("FATALITY.", 145, 90);
		temp.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		temp.drawString("Press R to restart this level or Q to quit.", 90, 150);
   	}
   	
   	//kill level
   	public void level7 (Graphics g){
   		Graphics2D temp = (Graphics2D) g;
   		
   		//background white
		temp.setColor(Color.white);
		temp.fillRect(3, 25, 394, 371);	
   		
   		//guy
		temp.drawImage(guyB.getImage(), guyB.getX(), guyB.getY(), this);

		temp.setColor(Color.black);
				
		//trophy
		ImageIcon trophy = new ImageIcon(this.getClass().getResource("trophy.png"));
		Image img = trophy.getImage();
		temp.drawImage(img, 355, 378, this);		
		if(deaths >= 5){
			//box around trophy	
			temp.drawRect(354, 377, 22, 21);
			
			//sliding spikes of DOOM.
			temp.drawImage(doom.getImage(), doom.getX(), doom.getY(), this);
			
			//text
			temp.setFont(new Font("Times New Roman", Font.ITALIC, 17));
			temp.drawString("The Reaper is not forgiving....", 25, 60);
			temp.drawString("You shall be SHIFTed out of this dimension.", 50, 100);
		}else{
			//other text
			temp.setFont(new Font("Times New Roman", Font.ITALIC, 17));
			temp.drawString("You survived. Take this trophy.", 80, 60);
			temp.drawString("AND GO.", 155, 100);
		}

   	}
   	
   	public void levelEnd (Graphics g){
   		Graphics2D temp = (Graphics2D) g;
   		
    	//white background
    	temp.setColor(Color.white);
		temp.fillRect(3, 25, 394, 370);
		
		//text
		temp.setColor(Color.black);
		temp.setFont(new Font("Century Gothic", Font.BOLD, 25));
		if (deaths < 5){
			temp.drawString("You Won!", 135, 75);
		}else{
			temp.drawString("You Died!", 135, 75);
		}
		temp.drawString("Congratulations!", 95, 110);
		temp.setFont(new Font("Times New Roman", Font.ITALIC, 11));
		if(deaths < 5){
			temp.drawString("DEATHS: " + deaths, 169, 250);
		}else{
			temp.drawString("DEATHS:", 157, 250);
			temp.setFont(new Font("Times New Roman", Font.BOLD, 12));
			temp.drawString(" DEAD.", 203, 250);
		}
		temp.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		temp.drawString("~The End~", 160, 200);
		temp.setFont(new Font("Lucida Handwriting", Font.PLAIN, 14));
		temp.drawString("Hope you enjoyed it! :)", 180, 340);
		
		//thanks
		temp.setFont(new Font("Arial", Font.PLAIN, 14));
		temp.drawString("Special Thanks to my sister, Mickie, for the", 20, 370);
		temp.drawString("animated character, trophy, and beta-ing.", 70, 385);
   	}
   		 
    //paints, in order, the door, the ground, and the shift character.
    public void paint (Graphics g){
    	super.paint(g);
    	
    	Graphics2D temp = (Graphics2D) g;   	
		
		//if dead, draws death screen, else continues the game	
		if(dead){
			drawDead(temp);
		}else{
		
			//Welcome + Instructions Page
			if (level == 0){
				level0(temp);
				shiftColor = true;
			}
			
		 	//Level1
		 	if(level == 1){
		    	level1(temp);
		 	}
		 	
		 	//Level2
		 	if(level == 2){
				level2(temp);
		 	}
	   				
		 	//Level3
		 	if(level == 3){		    	
		 		level3(temp);
		 	}
		 	
		 	//level4
		 	if(level == 4){
		 		level4(temp);
		 	}
		 	
		 	//level5
		 	if(level == 5){
		 		level5(temp);
		 	}
		 	
		 	//level6
		 	if(level == 6){
		 		level6(temp);
		 	}
		 	
		 	//level7
		 	if(level == 7){
		 		level7(temp);
		 	}
		 	
		 	//end of game
		 	if(level == 8){
		 		levelEnd(temp);
		 	}
		 	
		}
			 		   	
    	Toolkit.getDefaultToolkit().sync();
    	g.dispose();
    }     
    
    //check if the guy found the key
    //returns true if the key's been found
    public boolean checkKey(){
    	Rectangle guy = new Rectangle(guyB.getX(), guyB.getY(), 30, 48);
    	
    	Rectangle key = new Rectangle (0, 0, 0, 0);
    	
    	if (level == 3){
    		key = new Rectangle(364, 125, 30, 30);
    	}
    	
    	if (level == 5){
    		guy = new Rectangle(guyW.getX(), guyW.getY(), 30, 48);
    		key = new Rectangle(126, 282, 30, 30);
    	}
    	
    	if (level == 6){
    		if(!shiftColor){
    			guy = new Rectangle(guyW.getX(), guyW.getY(), 30, 48);
    		   	if(shifts >= 4){
   					key = new Rectangle(257, 267, 30, 30);
   				}	
    		}
    	}
    	
    	if (guy.intersects(key)){
    		return true;
    	}
    	
    	return false;
    }    					
    		
    //checking if it hits ground [black ground]
    //returns true if hit ground
    public boolean checkGroundBlack(){
    	canShift = true;
    	
    	Rectangle guy = new Rectangle (guyB.getX(), guyB.getY()-1, 30, 48);
    	
    	if (level == 1){
    		Rectangle ground = new Rectangle (3, 183, 393, 211);
    		if (guy.intersects(ground)){
    			return true;
    		}
    	}
    	
    	Rectangle block1;
    	Rectangle block2;
    	Rectangle block3;
    	
    	if(level == 2){
    		block1 = new Rectangle(3, 183, 97, 211);
		    block2 = new Rectangle(149, 183, 97, 211);
		    block3 = new Rectangle(296, 183, 101, 211);    
		    if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))){
		    	return true;
		    }
    	}
    	
    	Rectangle block4;
    	Rectangle block5;
    	Rectangle block6;
    	 
    	if(level == 3){
    		block1 = new Rectangle(3, 183, 97, 211);
		   	block2 = new Rectangle(174, 183, 50, 211);
		   	block3 = new Rectangle(299, 183, 100, 211);
		   	block4 = new Rectangle(360, 153, 37, 30); 
    		if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4))){
    				return true;
    		}
    		    		
    		if(!keyFound){
    			//box around door
    			block5 = new Rectangle(236, 283, 50, 48);
    			//slider
    			block6 = new Rectangle(153, 331, 72, 12);
    			if((guy.intersects(block5)) || (guy.intersects(block6))){
    				canShift = false;
    				return true;
    			}
    		}else{
    			//slider
    			block6 = new Rectangle(224, 331, 70, 11);
    			if(guy.intersects(block6)){
    				canShift = false;
    				return true;
    			}
    		}
    	}
    	
    	Rectangle block7;

    	if (level == 4){
    		block1 = new Rectangle(3, 213, 60, 211);
			block2 = new Rectangle(63, 243, 60, 211);
			block3 = new Rectangle(123, 273, 60, 211);
			block4 = new Rectangle(183, 303, 60, 211);
			block5 = new Rectangle(243, 333, 80, 211);
			block6 = new Rectangle(323, 213, 74, 211);
			block7 = new Rectangle(243, 213, 50, 1);
    		if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4)) || (guy.intersects(block5))
    			|| (guy.intersects(block6)) || (guy.intersects(block7))){
    				return true;
    		}
    	}
		
		Rectangle block8;
		Rectangle block9;
		Rectangle block10;
		Rectangle block11;
		
    	if (level == 5){
    		block1 = new Rectangle(4, 83, 48, 59);
    		block2 = new Rectangle(4, 233, 48, 161);
    		block3 = new Rectangle(54, 333, 58, 61);
    		block4 = new Rectangle(114, 83, 73, 311);
    		block5 = new Rectangle(139, 23, 48, 9);
    		block6 = new Rectangle(94, 83, 18, 59);
    		block7 = new Rectangle(189, 248, 32, 146);
    		block8 = new Rectangle(223, 283, 73, 111);
    		block9 = new Rectangle(298, 276, 48, 121);
    		block10 = new Rectangle(348, 73, 48, 321);
    		block11 = new Rectangle(223, 88, 73, 49);
    	    if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4)) || (guy.intersects(block5))
    			|| (guy.intersects(block6)) || (guy.intersects(block7))
    			|| (guy.intersects(block8)) || (guy.intersects(block9))
    			|| (guy.intersects(block10)) || (guy.intersects(block11))){
    				return true;
    		}
    		
    		if(!keyFound){
    			//slider
    			block6 = new Rectangle(118, 237, 70, 11);
    			if(guy.intersects(block6)){
    				canShift = false;
    				return true;
    			}
    		}else{
    			//slider
    			block6 = new Rectangle(187, 237, 70, 11);
    			if(guy.intersects(block6)){
    				canShift = false;
    				return true;
    			}
    		}
    	}

    	if (level == 6){
    		block1 = new Rectangle(3, 23, 50, 273);
    		block2 = new Rectangle(53, 297, 244, 49);
    		block3 = new Rectangle(103, 123, 50, 99);
    		block4 = new Rectangle(153, 73, 144, 49);
    		block5 = new Rectangle(153, 173, 94, 49);
    		block6 = new Rectangle(247, 123, 100, 49);
    		block7 = new Rectangle(297, 173, 50, 173);
    		if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4)) || (guy.intersects(block5))
    			|| (guy.intersects(block6)) || (guy.intersects(block7))){
    				return true;
    		}
    		
    		if(!keyFound){
	    		//slider
	    		block8 = new Rectangle(346, 123, 75, 12);
	    		if(guy.intersects(block8)){
	    			canShift = false;
	    			return true;
	    		}
    		}
    	}
    	
    	if (level == 7){
    		block1 = new Rectangle(354, 375, 22, 20);
    		if(guy.intersects(block1) && (deaths >= 5)){
    			canShift = false;
    			return true;
    		}
    	}
    	
    	//Bottom
    	Rectangle bottom = new Rectangle (3, 395, 393, 1);    	
    	
    	if (guy.intersects(bottom)){
    		canShift = false;
    		return true;
    	}
    	
    	return false;
    }
    
    //checking if it hits ground [white ground]
    //returns true if it hit groud
    public boolean checkGroundWhite(){
    	canShift = true;
    	
    	Rectangle guy = new Rectangle (guyW.getX(), guyW.getY()-1, 30, 48);
    	
    	if(level == 1){
    		Rectangle ground = new Rectangle (3, 183, 393, 211);
    		if(guy.intersects(ground)){
    			return true;
    		}
    	}
    	
    	Rectangle block1;
    	Rectangle block2;
    	Rectangle block3;
    	
    	if(level == 2){
    		block1 = new Rectangle(3, 235, 394, 161);
		    block2 = new Rectangle(104, 25, 50, 213);
		    block3 = new Rectangle(251, 25, 50, 213);	
		    if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))){
		    	return true;
		    }
    	}
    	
    	Rectangle block4;

    	if(level == 3){    		
    		block1 = new Rectangle(3, 275, 394, 151);
    		block2 = new Rectangle(40, 245, 357, 31);
    		block3 = new Rectangle(101, 23, 75, 223);
    		block4 = new Rectangle(226, 23, 75, 223);
    		if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4))){
    				return true;
    		}
    	}
    	
    	Rectangle block5;
    	Rectangle block6;
    	Rectangle block7;
    	Rectangle block8;

    	if(level == 4){
    		block1 = new Rectangle(3, 205, 74, 191);
		    block2 = new Rectangle(77, 85, 80, 91);
		    block3 = new Rectangle(77, 175, 30, 31);
		    block4 = new Rectangle(77, 205, 80, 221);
		    block5 = new Rectangle(157, 115, 60, 281);
		    block6 = new Rectangle(217, 145, 60, 251);
		    block7 = new Rectangle(277, 175, 60, 221);
		    block8 = new Rectangle(337, 205, 60, 191);
		    if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4)) || (guy.intersects(block5))
    			|| (guy.intersects(block6)) || (guy.intersects(block7))
    			|| (guy.intersects(block8))){
    				return true;
    		}
    	}
    	
    	Rectangle block9;

	 	if(level == 5){
	 		block1 = new Rectangle(3, 345, 209, 51);
	 		block2 = new Rectangle(53, 142, 50, 204);
	 		block3 = new Rectangle(103, 170, 109, 111);
	 		block4 = new Rectangle(103, 330, 109, 16);
	 		block5 = new Rectangle(178, 280, 34, 51);
	 		block6 = new Rectangle(287, 85, 60, 191);
	 		block7 = new Rectangle(307, 275, 40, 61);
	 		block8 = new Rectangle(262, 335, 135, 61);
	 		block9 = new Rectangle(347, 185, 50, 91);
	 		if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4)) || (guy.intersects(block5))
    			|| (guy.intersects(block6)) || (guy.intersects(block7))
    			|| (guy.intersects(block8)) || (guy.intersects(block9))){
    				return true;
    		}
   				
    		if(!keyFound){
    			//slider
    			block6 = new Rectangle(212, 169, 70, 11);
    			if(guy.intersects(block6)){
    				canShift = false;
    				return true;
    			}
    		}else{
    			//slider
    			block6 = new Rectangle(143, 169, 70, 11);
    			if(guy.intersects(block6)){
    				canShift = false;
    				return true;
    			}
    		}
	 	}	
		
		Rectangle block10;
		
	 	if(level == 6){
	 		block1 = new Rectangle(3, 23, 394, 49);
	 		block2 = new Rectangle(3, 71, 50, 331);
	 		block3 = new Rectangle(347, 71, 50, 51);
	 		block4 = new Rectangle(53, 295, 50, 101);
	 		block5 = new Rectangle(103, 345, 244, 51);
	 		block6 = new Rectangle(247, 295, 100, 51);
	 		block7 = new Rectangle(153, 245, 94, 51);
	 		block8 = new Rectangle(297, 121, 50, 176);
	 		block9 = new Rectangle(103, 121, 200, 75);
	 		block10 = new Rectangle(103, 195, 50, 51);
	 		if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4)) || (guy.intersects(block5))
    			|| (guy.intersects(block6)) || (guy.intersects(block7))
    			|| (guy.intersects(block8)) || (guy.intersects(block9))
    			|| (guy.intersects(block10))){
    				return true;
    		}
	 	}
    	
    	//Bottom
    	Rectangle bottom = new Rectangle (3, 395, 393, 1);    	
    	
    	if (guy.intersects(bottom)){
    		canShift = false;
    		return true;
    	}
    	return false;
    }
 	
 	//checks if it hits the top of certain blocks [level 4, 5, and 6]
 	//returns true if it hit the top
 	public boolean checkTop(){
 		Rectangle guy = new Rectangle (guyB.getX(), guyB.getY(), 30, 48);
 		
    	Rectangle block1;
    		
    	if(level == 4){
    		block1 = new Rectangle (243, 216, 50, 30);
    		if(guy.intersects(block1) && shiftColor){
   				guyB.setY1(0);
    			return true;
    		}
    	}		   	
    		
    	Rectangle block2;
    	Rectangle block3;
    	Rectangle block4;
    	Rectangle block5;	
    	Rectangle block6;
    	Rectangle block7;
		Rectangle block8;
		Rectangle block9;
		Rectangle block10;		
		Rectangle block11;		    		    			 				 			

    	if(level == 5){
    		if(shiftColor){
    			block1 = new Rectangle(3, 87, 50, 60);
    			block2 = new Rectangle(3, 237, 50, 162);
    			block3 = new Rectangle(53, 337, 60, 62);
    			block4 = new Rectangle(113, 87, 75, 312);
    			block5 = new Rectangle(138, 77, 50, 10);
    			block6 = new Rectangle(93, 87, 20, 60);
    			block7 = new Rectangle(188, 252, 34, 147);
    			block8 = new Rectangle(222, 287, 75, 112);
    			block9 = new Rectangle(297, 280, 50, 122);
    			block10 = new Rectangle(347, 77, 50, 322);
    			block11 = new Rectangle(222, 92, 75, 50);
    			
    			if ((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    				|| (guy.intersects(block4)) || (guy.intersects(block5))
    				|| (guy.intersects(block6)) || (guy.intersects(block7))
    				|| (guy.intersects(block8)) || (guy.intersects(block9))
    				|| (guy.intersects(block10) || (guy.intersects(block11)))){
    					guyB.setY1(0);
    					return true;
    			}
    		}else{
    			guy = new Rectangle (guyW.getX(), guyW.getY(), 30, 48);
    			block1 = new Rectangle(3, 349, 209, 50);
    			block2 = new Rectangle(53, 146, 50, 203);
    			block3 = new Rectangle(103, 174, 109, 110);
    			block4 = new Rectangle(103, 334, 109, 15);
    			block5 = new Rectangle(178, 284, 34, 50);
    			block6 = new Rectangle(287, 89, 60, 190);
    			block7 = new Rectangle(307, 279, 40, 60);
    			block8 = new Rectangle(262, 339, 135, 60);
    			block9 = new Rectangle(347, 189, 50, 90);
    			
    			if ((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    				|| (guy.intersects(block4)) || (guy.intersects(block5))
    				|| (guy.intersects(block6)) || (guy.intersects(block7))
    				|| (guy.intersects(block8)) || (guy.intersects(block9))){
    					guyW.setY1(0);
    					return true;
    			}
    		}
    	}
    		
    	if(level == 6){
    		if(shiftColor){
    			block1 = new Rectangle(3, 26, 50, 274);
    			block2 = new Rectangle(53, 300, 244, 50);
    			block3 = new Rectangle(103, 126, 50, 100);
    			block4 = new Rectangle(153, 76, 144, 50);
    			block5 = new Rectangle(153, 176, 94, 50);
    			block6 = new Rectangle(247, 126, 100, 50);
    			block7 = new Rectangle(297, 176, 50, 174);
    			
    			if ((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    				|| (guy.intersects(block4)) || (guy.intersects(block5))
    				|| (guy.intersects(block6)) || (guy.intersects(block7))){
    					guyB.setY1(0);
    					return true;
    			}
    		}else{
    			guy = new Rectangle (guyW.getX(), guyW.getY(), 30, 48);
    			block1 = new Rectangle(3, 26, 394, 48);
    			block2 = new Rectangle(3, 74, 50, 330);
    			block3 = new Rectangle(347, 74, 50, 50);
    			block4 = new Rectangle(53, 298, 50, 100);
    			block5 = new Rectangle(103, 348, 244, 50);
    			block6 = new Rectangle(247, 298, 100, 50);
    			block7 = new Rectangle(153, 248, 94, 50);
    			block8 = new Rectangle(297, 124, 50, 175);
    			block9 = new Rectangle(103, 124, 200, 74);
    			block10 = new Rectangle(103, 198, 50, 50);
    			if ((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    				|| (guy.intersects(block4)) || (guy.intersects(block5))
    				|| (guy.intersects(block6)) || (guy.intersects(block7))
    				|| (guy.intersects(block8)) || (guy.intersects(block9))
    				|| (guy.intersects(block10))){
    					guyW.setY1(0);
    					return true;
    			}
    		}
    	}
    	
    	return false;
 	}
 	
    //checks if it can move right or left based on input
    //returns true if it can [0 = right, 1 = left]
    public boolean check(int dir){	
    	ShiftCharacters guyCharacter;
    	if (shiftColor){
    		guyCharacter = guyB;
    	}else{
    		guyCharacter = guyW;
    	}
    	
    	Rectangle guy;
    	if (dir == 0){//right   		
    		guy= new Rectangle(guyCharacter.getX() + 28, guyCharacter.getY()-1, 2, 47);
    	}else{//left   		
    		guy = new Rectangle (guyCharacter.getX(), guyCharacter.getY()-1, 1, 47);
    	}
    	
    	Rectangle block1;
		Rectangle block2;
		Rectangle block3;    	
    	
    	if (level == 2){
		    if(shiftColor){
		    	block1 = new Rectangle(2, 183, 99, 212);
		    	block2 = new Rectangle(148, 183, 99, 212);
		    	block3 = new Rectangle(295, 183, 103, 212);
		    }else{
		    	block1 = new Rectangle(2, 235, 396, 160);
		    	block2 = new Rectangle(103, 23, 52, 212);
		    	block3 = new Rectangle(250, 23, 52, 212);
		    }
		    if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))){
		    	return false;
		    }
    	}
    	
    	Rectangle block4;

    	if(level == 3){
    		if(shiftColor){
    			block1 = new Rectangle(2, 183, 99, 212);
		    	block2 = new Rectangle(173, 183, 52, 212);
		    	block3 = new Rectangle(298, 183, 102, 212);
		    	block4 = new Rectangle(359, 153, 39, 30); 
    		}else{
    			block1 = new Rectangle(2, 275, 396, 212);
    			block2 = new Rectangle(39, 245, 359, 212);
    			block3 = new Rectangle(100, 23, 77, 212);
    			block4 = new Rectangle(225, 23, 77, 212);
    		}
    		if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
    			|| (guy.intersects(block4))){
    				return false;
    		}
    	}

    	Rectangle block5;
    	Rectangle block6;
    	Rectangle block7;
    	Rectangle block8;

    	if(level == 4){
    		if(shiftColor){
	    		block1 = new Rectangle(2, 213, 62, 212);
				block2 = new Rectangle(62, 243, 62, 212);
				block3 = new Rectangle(122, 273, 62, 212);
				block4 = new Rectangle(182, 303, 62, 212);
				block5 = new Rectangle(242, 333, 82, 212);
				block6 = new Rectangle(322, 213, 76, 212);
				block7 = new Rectangle(242, 213, 52, 30);
				if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
	    			|| (guy.intersects(block4)) || (guy.intersects(block5))
	    			|| (guy.intersects(block6)) || (guy.intersects(block7))){
	    				return false;
	    		}
    		}else{
    			block1 = new Rectangle(2, 205, 76, 190);
		    	block2 = new Rectangle(76, 85, 82, 90);
		    	block3 = new Rectangle(76, 175, 32, 30);
		    	block4 = new Rectangle(76, 205, 82, 220);
		    	block5 = new Rectangle(156, 115, 62, 280);
		    	block6 = new Rectangle(216, 145, 62, 250);
		    	block7 = new Rectangle(276, 175, 62, 220);
		    	block8 = new Rectangle(336, 205, 62, 190);	
    			if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
	    			|| (guy.intersects(block4)) || (guy.intersects(block5))
	    			|| (guy.intersects(block6)) || (guy.intersects(block7))
	    			|| (guy.intersects(block8))){
	    				return false;
	    		}
    		}
    	}
    	
    	Rectangle block9;
    	Rectangle block10;
    	Rectangle block11;
    	Rectangle block12;

    	if(level == 5){
    		if(shiftColor){
    			block1 = new Rectangle(2, 83, 52, 60);
    			block2 = new Rectangle(2, 233, 52, 162);
    			block3 = new Rectangle(52, 333, 62, 62);
    			block4 = new Rectangle(112, 83, 77, 312);
    			block5 = new Rectangle(137, 23, 52, 62);
    			block6 = new Rectangle(92, 83, 22, 60);
    			block7 = new Rectangle(187, 248, 36, 147);
    			block8 = new Rectangle(221, 283, 77, 112);
    			block9 = new Rectangle(296, 276, 52, 122);
    			block10 = new Rectangle(346, 73, 52, 322);
    			block11 = new Rectangle(221, 88, 77, 50);
	    	    //side of spikes
	    	    block12 = new Rectangle(297, 255, 1, 35);    			
    			if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
	    			|| (guy.intersects(block4)) || (guy.intersects(block5))
	    			|| (guy.intersects(block6)) || (guy.intersects(block7))
	    			|| (guy.intersects(block8)) || (guy.intersects(block9))
	    			|| (guy.intersects(block10)) || (guy.intersects(block11))
	    			|| (guy.intersects(block12))){
	    				return false;
	    		}
    		}else{
    			block1 = new Rectangle(2, 345, 211, 50);
    			block2 = new Rectangle(52, 142, 52, 203);
    			block3 = new Rectangle(102, 170, 111, 110);
    			block4 = new Rectangle(102, 330, 111, 15);
    			block5 = new Rectangle(177, 280, 36, 50);
    			block6 = new Rectangle(286, 85, 62, 190);
    			block7 = new Rectangle(306, 275, 42, 60);
    			block8 = new Rectangle(261, 335, 137, 60);
    			block9 = new Rectangle(346, 185, 52, 90);
    			//side of spikes
    			block10 = new Rectangle(178, 143, 1, 30);
    			if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
	    			|| (guy.intersects(block4)) || (guy.intersects(block5))
	    			|| (guy.intersects(block6)) || (guy.intersects(block7))
	    			|| (guy.intersects(block8)) || (guy.intersects(block9))
	    			|| (guy.intersects(block10))){
	    				return false;
	    		}
    		}
    	}
		
    	if(level == 6){
    		if(shiftColor){
    			block1 = new Rectangle(2, 23, 53, 274);		
    			block2 = new Rectangle(52, 297, 246, 50);
    			block3 = new Rectangle(102, 123, 54, 100);
    			block4 = new Rectangle(152, 73, 146, 50);
    			block5 = new Rectangle(152, 173, 96, 50);
    			block6 = new Rectangle(246, 123, 102, 50);
    			block7 = new Rectangle(296, 173, 52, 174);
    			if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
	    			|| (guy.intersects(block4)) || (guy.intersects(block5))
	    			|| (guy.intersects(block6)) || (guy.intersects(block7))){
	    				return false;
	    		}
    		}else{
    			block1 = new Rectangle(2, 23, 396, 48);
    			block2 = new Rectangle(2, 71, 52, 330);
    			block3 = new Rectangle(346, 71, 52, 50);
    			block4 = new Rectangle(52, 295, 52, 100);
    			block5 = new Rectangle(102, 345, 246, 50);
    			block6 = new Rectangle(246, 295, 102, 50);
    			block7 = new Rectangle(152, 245, 96, 50);
    			block8 = new Rectangle(296, 121, 52, 175);
    			block9 = new Rectangle(102, 121, 202, 74);
    			block10 = new Rectangle(102, 195, 52, 50);
    			if((guy.intersects(block1)) || (guy.intersects(block2)) || (guy.intersects(block3))
	    			|| (guy.intersects(block4)) || (guy.intersects(block5))
	    			|| (guy.intersects(block6)) || (guy.intersects(block7))
	    			|| (guy.intersects(block8)) || (guy.intersects(block9))
	    			|| (guy.intersects(block10))){
	    				return false;
	    		}
    		}
    	}
    	
    	if(level == 7){
    		block1 = new Rectangle(353, 375, 24, 21);
    		if(guy.intersects(block1) && (deaths >= 5)){
    			return false;
    		}
    	}
    	
    	if (dir == 0){
    		//checks if hits Right wall of window
    		Rectangle right = new Rectangle(396, 25, 200, 372);
    		if(guy.intersects(right)){
    			return false;
    		}
    	}else{
    		//checks if it hits Left wall of window
    		Rectangle left = new Rectangle(3, 0, 1, 397);
    		if(guy.intersects(left)){
    			return false;
    		}
    	}
    	return true;
    }
    
    //checks to see if dead, if dead restard level
    public void checkDead(){
    	Rectangle guy = new Rectangle(guyB.getX(), guyB.getY(), 30, 48);
    	
    	Rectangle spikes1;
    	Rectangle spikes2;
    	
    	//level2 dead possiblities
    	if(level == 2){
    		spikes1 = new Rectangle (99, 347, 50, 50);
	 		spikes2 = new Rectangle (246, 347, 50, 50);
	 		if((guy.intersects(spikes1)) ||	(guy.intersects(spikes2))){
	 			dead = true;
	 			deaths++;
	 		}
    	}
    	
    	//level3's spikes
    	if(level == 3){
    		spikes1 = new Rectangle (99, 347, 75, 50);
	 		spikes2 = new Rectangle (224, 347, 75, 50);
    		if((guy.intersects(spikes1)) || (guy.intersects(spikes2))){
    			dead = true;
    			deaths++;
    		}
    	}
    	
    	//nothing for level4
    	
    	//level5's spikes
    	if(level == 5){
    		if(shiftColor){
	 			spikes1 = new Rectangle(222, 250, 74, 30);
    			
    			if(guy.intersects(spikes1)){
    				dead = true;
    				deaths++;
    			}
    		}else{
    			guy = new Rectangle(guyW.getX(), guyW.getY(), 30, 48);
    		
    			spikes1 = new Rectangle(213, 369, 48, 30);
	 			spikes2 = new Rectangle(104, 144, 73, 30);
	 			
	 			if((guy.intersects(spikes1)) || (guy.intersects(spikes2))){
					dead = true;
	 				deaths++;
	 				
	 			}
    		}
		}
		
		//nothing for level6
		
		//level7's spike wall
		if(level == 7){
			spikes1 = new Rectangle(doom.getX(), doom.getY(), 30, 371);
			if(guy.intersects(spikes1)){
				level++;
			}		
		}
    }
    
    //Checks if the level and eventually game has  been won
    public void checkWin(){
    	Rectangle guy = new Rectangle (guyB.getX(), guyB.getY(), 30, 48);
    	
    	Rectangle door = new Rectangle (-100, -100, 0, 0);
    	
    	if ((level != 0) && (level <= 2)){
    		door = new Rectangle (doorB.getX(), doorB.getY(), 48, 48);
    	}
    	
    	if (level == 3){
    		door = new Rectangle(237, 285, 48, 48);
    	}
    	
    	if (level == 4){
    		door = new Rectangle(244, 167, 48, 48);
    	}
    	
    	if (level == 5){
    		guy = new Rectangle (guyW.getX(), guyW.getY(), 30, 48);
    		door = new Rectangle(349, 289, 50, 50);
    	}
    	
    	if (level == 6){
    		door = new Rectangle(3, 349, 48, 48);
    	}
    	
    	if (level == 7){
    		door = new Rectangle(355, 378, 20, 19);
    	}
    	
    	if (door.intersects(guy)){
			level++;
			shifts = 0;
			jump = false;
								
			guyB.setX(10);
		    guyB.setY(137);
		    
		    //make sure they don't have residual acceleration
			guyB.setX1(0);
			guyW.setX1(0);
		    
		    if (level == 3){
		    	keyFound = false;
		    }	
		    		    
		    if (level == 5){
			   	guyB = new ShiftBlack (guyB.getX(), guyB.getY() + 15, 0, 0);
			   	keyFound = false;
			}
			
			if (level == 6){
				shiftColor = !shiftColor;
				guyW = new ShiftWhite (guyW.getX() - 50, guyW.getY() - 200, 0, 0);
				keyFound = false;
			}			
    	}
    }     
    
    //Checks if an action has been performed and acts accordingly
    public void actionPerformed(ActionEvent e){
    	if(shiftColor){
    		guyB.move();
    	}else{
    		guyW.move();
    	}    

    	//makes sure it doesnt fall too far
    	if((!check(0)) && (!check(1))){
    		guyB.setY(guyB.getY() - 2);
    		guyW.setY(guyW.getY() - 2);
    	}
    	
    	//spikes of doom
		if(level == 7){
			doom.move();
		}
		
    	checkDead();
    	checkWin();
    	
    	if((level != 0) && (!dead) && (level != 8)){
    		repaint();
    	}
    }
    
    //KEYLISTENER
    private class TAdapter extends KeyAdapter{
    	public void keyReleased(KeyEvent e){		
    		if(shiftColor){
    			guyB.keyReleased(e);
    		}else{
    			guyW.keyReleased(e);
    		}
    	}
    	
    	public void keyPressed(KeyEvent e){
    		
			int key = e.getKeyCode();
			
			//space causes a jump in the direction moved the step
			//		before, with a default direction of right
			if (key == KeyEvent.VK_SPACE){
				keyDirection = 'j';
				jump = true;
			}  
			
			//If you aren't jumping, you're moving
			if (jump == false){
				//resets variables for next jump
				tempYB = guyB.getY();
				tempYW = guyW.getY();
				
				if (key == KeyEvent.VK_RIGHT){
					//for checking for boundries
					keyDirection = 'r';
					//for jumping
					direction = 0;
				}
				
				if (key == KeyEvent.VK_LEFT){
					//for checking for boundries
					keyDirection = 'l';
					//for jumping
					direction = 1;
				}			
								
				if(shiftColor){
					guyB.keyPressed(e);
				}else{
					guyW.keyPressed(e);
				}				
			
				//if you hit shift the colors swap and the board turns 180 degrees
				if(key == KeyEvent.VK_SHIFT){
					//checks if on a slider, if so, canShift is made false
					if(shiftColor){
						checkGroundBlack();
					}else{
						checkGroundWhite();
					}
					
					if (canShift){
						//if falling can't shift
						ShiftCharacters guy;
						if(shiftColor){
							guy = guyB;
						}else{
							guy = guyW;
						}
						if(guy.getY1() != 0){
						}else{
							shiftColor = !shiftColor;
							shifts++;
							
							//make sure they don't have residual acceleration
							guyB.setX1(0);
							guyW.setX1(0);
						}
					}
				}
			}
			
			//Quits the game ==> restarts at first level
			if(key == KeyEvent.VK_Q){
				level = 0;
				shifts = 0;
				deaths = 0;
				dead = false;
					
				//in case its white, make it black
				if(!shiftColor){
					shiftColor = true;
				}					
				
				jump = false;
				
				//make sure they don't have residual acceleration
				guyB.setX1(0);
				guyW.setX1(0);
				
				guyB.setX(10);
			   	guyB.setY(137);
			   	guyW.setX(10);
			   	guyW.setY(137);
			 	   	
			   	if(level == 3){
			   		keyFound = false;
			   	}
			   	
			   	if (level == 5){
		    		guyB = new ShiftBlack (guyB.getX(), guyB.getY() + 15, 0, 0);
		    		keyFound = false;
		    	}
		    	
		    	if (level == 6){
					guyB = new ShiftBlack (guyB.getX() + 50, guyB.getY() + 50, 0, 0);
					keyFound = false;
				}
				
				if (level == 7){
					doom = new DoomSpikes();
				}
			}
				
			//Restarts level
			if(key == KeyEvent.VK_R){
				//if it's white, make it black
				if(!shiftColor){
					shiftColor = true;
				}
				
				shifts = 0;
				dead = false;
				jump = false;
				
				//make sure they don't have residual acceleration
				guyB.setX1(0);
				guyW.setX1(0);
					
				guyB.setX(10);
			 	guyB.setY(137);
			  	guyW.setX(360);
			 	guyW.setY(134);
			 	   		
			   	if(level == 3){
			   		keyFound = false;
			  	}
		   		
		   		if (level == 5){
					guyB.setX(10);
		    		guyB.setY(157);
		    		
		    		guyW.setX(360);
		    		guyW.setY(157);
		    		keyFound = false;
		    	}
		    	
		    	if (level == 6){
					guyB = new ShiftBlack (guyB.getX() + 50, guyB.getY() + 50, 0, 0);
					guyW = new ShiftWhite (guyW.getX() - 50, guyW.getY() - 50, 0, 0);
					keyFound = false;
				}
				
				if (level == 7){
					doom = new DoomSpikes();
				}
			}				
				
			//Cheat key to let someone skip the level
			if(key == KeyEvent.VK_ENTER){
				if (level < 8){
					level++;
					shifts = 0;
					dead = false;
					
					//in case its white, make it black
					shiftColor = true;	
			
					jump = false;                                 
					
					//make sure they don't have residual acceleration
					guyB.setX1(0);
					guyW.setX1(0);
						
					guyB.setX(10);
				   	guyB.setY(134);
				   	guyW.setX(360);
				   	guyW.setY(134);
													
					if(level == 3){
				   		keyFound = false;
				   	}	
				   					   	
				 	if (level == 5){
		    			guyB = new ShiftBlack (guyB.getX(), guyB.getY() + 15, 0, 0);
		    			guyW = new ShiftWhite (guyW.getX(), guyW.getY() - 15, 0, 0);
		    			keyFound = false;
				    }
					
					if (level == 6){
						guyB = new ShiftBlack (guyB.getX() + 50, guyB.getY() + 50, 0, 0);
						guyW = new ShiftWhite (guyW.getX() - 50, guyW.getY() - 50, 0, 0);
						keyFound = false;
					}
					
					if (level == 7){
						doom = new DoomSpikes();
					}
				}
			}
			
			//makes the key found for easy editing and acts as a cheat
			if (key == KeyEvent.VK_K){
				keyFound = !keyFound;
			}
    	}
    }	       
        
    //draws frames so can use acceleration, gravity, etc.
    public void drawFrame(){
   		BufferStrategy bf = this.getBufferStrategy();
   		Graphics g = null;
   				
   		try{
   			g = bf.getDrawGraphics();
   			paint(g);
   		} finally {
   			g.dispose();
   		}
   		bf.show();
   		Toolkit.getDefaultToolkit().sync();
    }
} 
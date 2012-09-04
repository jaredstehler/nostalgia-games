
/*
	Project Orange

	Developed by Cycle Storm Games
	for JE Bikes
	
	12.24.99-12.26.99

	Version 1.10.250-R	->   Release version  (03.18.2000)
*/

import java.applet.*;
import java.awt.*;
import java.awt.image.*;

public class PunishThePoser extends Applet implements Runnable {

	int current_level, ammo_amt, throw_pos, direction, numhits;

	int ammoX, ammoY;		int bikeX, bikeY, speed;
	int ammo_image;			int handX, handY, hand_image, hit_image;

	Image Bike[] = new Image[14];  // 4 for each direction, 3 for
								   // each getting hit	

	Image hand[] = new Image[4];  // hand holding ammo, throwing it..
	Image ammo[] = new Image[4];  

	Image bg[] = new Image[3];  // 1 bg for each level
	Image you_win[] = new Image[5];
	Image title, you_lose;

	Image bikes_left;
	
	AudioClip hit, stopithurts, youlose;
	AudioClip ouches[] = new AudioClip[4];

	Font f = new Font("TimesRoman", Font.PLAIN, 14);
	Font credits_font_big = new Font("TimesRoman", Font.BOLD, 18);
	Font credits_font_small = new Font("TimesRoman", Font.PLAIN, 12);

	FontMetrics fm_big = getFontMetrics(credits_font_big);
	FontMetrics fm_small = getFontMetrics(credits_font_small);
	
	String credits[] = new String[20];
								 
	boolean GO = false;
	
	boolean START_GAME = false, DONE = false, FINISHED = false;
	boolean AMMO_AWAY = false;

	private MediaTracker tracker;

	Image offimg, offimg2, tempimg;
	Graphics g2, g3;

	Thread runner;

	public void init(){
		offimg = createImage(420, 300);
		offimg2 = createImage(420, 300);

		g2 = offimg.getGraphics();
		g3 = offimg2.getGraphics();
	}


	public void start(){
		if(runner == null){
			runner = new Thread(this);
			runner.start();
		}
	}

	
	public void stop(){
		if(runner != null){
			runner.stop();
			runner = null;
		}
	}


	public void run(){

		getAppletContext().showStatus("Copyright © 2000 JE Bikes.");

		int trackernum = 0, bike_image = 0, drawX;

		g3.setFont(f);
		g3.setColor(Color.white);
		g3.fillRect(0, 0, 420, 300);
		g3.setColor(Color.black);
		g3.drawString("Loading Game Images.....", 125, 150);
		g3.drawRect(140, 170, 100, 8);

		tempimg = offimg2;
		repaint();
		
		hit = getAudioClip(getCodeBase(), "punishtheposer/rock_hit.au");
		ouches[1] = getAudioClip(getCodeBase(), "punishtheposer/ow.au");
		ouches[2] = getAudioClip(getCodeBase(), "punishtheposer/ooohhh.au");
		ouches[3] = getAudioClip(getCodeBase(), "punishtheposer/oof.au");
		youlose = getAudioClip(getCodeBase(), "punishtheposer/hahayoulost.au");
		stopithurts = getAudioClip(getCodeBase(), "punishtheposer/stopithurts.au");

		Bike[0] = getImage(getCodeBase(), "punishtheposer/bike0.gif");
		Bike[1] = getImage(getCodeBase(), "punishtheposer/bike1.gif");
		Bike[2] = getImage(getCodeBase(), "punishtheposer/bike2.gif");
		Bike[3] = getImage(getCodeBase(), "punishtheposer/bike3.gif");
		Bike[4] = getImage(getCodeBase(), "punishtheposer/bike4.gif");
		Bike[5] = getImage(getCodeBase(), "punishtheposer/bike5.gif");
		Bike[6] = getImage(getCodeBase(), "punishtheposer/bike6.gif");
		Bike[7] = getImage(getCodeBase(), "punishtheposer/bike7.gif");

		Bike[8] = getImage(getCodeBase(), "punishtheposer/bike8.gif");
		Bike[9] = getImage(getCodeBase(), "punishtheposer/bike9.gif");
		Bike[10] = getImage(getCodeBase(), "punishtheposer/bike10.gif");
		Bike[11] = getImage(getCodeBase(), "punishtheposer/bike11.gif");
		Bike[12] = getImage(getCodeBase(), "punishtheposer/bike12.gif");
		Bike[13] = getImage(getCodeBase(), "punishtheposer/bike13.gif");

		title = getImage(getCodeBase(), "punishtheposer/title.jpg");
		you_lose = getImage(getCodeBase(), "punishtheposer/you_lose.jpg");

		you_win[0] = getImage(getCodeBase(), "punishtheposer/win_1.jpg");
		you_win[1] = getImage(getCodeBase(), "punishtheposer/win_2.jpg");
		you_win[2] = getImage(getCodeBase(), "punishtheposer/win_3.jpg");
		you_win[3] = getImage(getCodeBase(), "punishtheposer/win_4.jpg");
		you_win[4] = getImage(getCodeBase(), "punishtheposer/win_5.jpg");
		
		bikes_left = getImage(getCodeBase(), "punishtheposer/bikes_left.jpg");

		bg[0] = getImage(getCodeBase(), "punishtheposer/bg0.jpg");
		bg[1] = getImage(getCodeBase(), "punishtheposer/bg1.jpg");
		bg[2] = getImage(getCodeBase(), "punishtheposer/bg2.jpg");

		hand[0] = getImage(getCodeBase(), "punishtheposer/hand0.gif");
		hand[1] = getImage(getCodeBase(), "punishtheposer/hand1.gif");

		ammo[0] = getImage(getCodeBase(), "punishtheposer/ammo0.gif");
		ammo[1] = getImage(getCodeBase(), "punishtheposer/ammo1.gif");
		ammo[2] = getImage(getCodeBase(), "punishtheposer/ammo2.gif");

		tracker = new MediaTracker(this);		
		
		tracker.addImage(Bike[0], 0);
		tracker.addImage(Bike[1], 1);
		tracker.addImage(Bike[2], 2);
		tracker.addImage(Bike[3], 3);
		tracker.addImage(Bike[4], 4);
		tracker.addImage(Bike[5], 5);
		tracker.addImage(Bike[6], 6);
		tracker.addImage(Bike[7], 7);
		tracker.addImage(Bike[8], 8);
		tracker.addImage(Bike[9], 9);
		tracker.addImage(Bike[10], 10);
		tracker.addImage(Bike[11], 11);
		tracker.addImage(Bike[12], 12);
		tracker.addImage(Bike[13], 13);
	
		tracker.addImage(title, 14);
		tracker.addImage(you_lose, 15);
		
		tracker.addImage(you_win[0], 16);
		tracker.addImage(you_win[1], 17);
		tracker.addImage(you_win[2], 18);
		tracker.addImage(you_win[3], 19);
		tracker.addImage(you_win[4], 20);

		tracker.addImage(hand[0], 21);
		tracker.addImage(hand[1], 22);

		tracker.addImage(ammo[0], 23);
		tracker.addImage(ammo[1], 24);
		tracker.addImage(ammo[2], 25);

		tracker.addImage(bg[0], 26);
		tracker.addImage(bg[1], 27);
		tracker.addImage(bg[2], 28);

		tracker.addImage(bikes_left, 29);

		int percent_complete = 0, img_num = 0;
		int increment = (int)(100 / 29);
		
		while(percent_complete < 100){
			try{
				tracker.waitForID(img_num);
			}
			catch(InterruptedException e)
			{
				return;
			}

			percent_complete += increment;

			g3.setColor(Color.black);
			g3.drawRect(140, 170, 100, 8);
			g3.fillRect(140, 170, percent_complete, 8);

			tempimg = offimg2;
			repaint();

			img_num++;
		}

		
/*		try{
			tracker.waitForID(0);
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }
*/
		
		while(true){

			GO = true;

			getAppletContext().showStatus("http://www.jebikes.com/");

			do{
				g3.drawImage(title, 0, 0, this);
				repaint();
				pause(50);
			}while(!START_GAME);

			current_level = 1;
			direction = 0;
			speed = 7;
			handY = 200;
			hand_image = ammo_image = 0;
			AMMO_AWAY = false;

			while(!FINISHED){

				g2.drawImage(bg[current_level - 1], 0, 0, this);

				DONE = false;

				bikeX = direction = 0;
				bikeY = 50;

				ammoY = 210;
				
				ammo_amt = 6;
				
				if(current_level > 1)
					ammo_amt--;
				if(current_level > 2)
					ammo_amt--;
				
				throw_pos = bike_image = numhits = 0;
				hit_image = 1;

				while(!DONE){
					if(direction == 0)
						bikeX += speed;
					else
						bikeX -= speed;

					if(bikeX >= 425 || bikeX <= -125)
						direction = 1 - direction;

					trackernum++;

					if(trackernum >= 5){
						bike_image++;
						trackernum = 0;
					}

					if(bike_image > 2 && direction == 0)
						bike_image = 0;
					if(bike_image > 6 && direction == 1)
						bike_image = 4;

					
					if(AMMO_AWAY){
						throw_pos++;

						if(throw_pos < 12){
								ammo_image = 0;
								ammoY -= 10;
						}
						else if(throw_pos >= 12 && throw_pos < 24){
								ammo_image = 1;
								ammoY -= 5;
						}
						else{
								ammo_image = 2;
								ammoY += 6;
						}
				
						if(throw_pos >= 36){
							if(checkCollision()){
								if(hit != null)
									hit.play();
								numhits++;
							
								if(hit_image >= 3){
									bikeY = 100;
									hit_image = 3;
								}
								
								if(ouches[hit_image] != null)
									ouches[hit_image].play();

								if(direction == 0)
									bike_image = hit_image + 7;
								else
									bike_image = hit_image + 10;
								
								hit_image++;
	
							if(numhits >= 3){
								current_level++;
								speed += 5;

								if(current_level > 3){
									//win game screen
									showScn(1);
									showCredits();
									DONE = true;
									FINISHED = true;
								}

								else{
									DONE = true;
								}
							}
						}

						AMMO_AWAY = false;
						ammoY = 210;
						throw_pos = ammo_image = hand_image = 0;
						handY += 40;

						if(ammo_amt <= 0 && !DONE && !FINISHED){
							DONE = true;
							FINISHED = true;
							//  losing screen
							showScn(0);
						}


						}
					}


					// Screen redraw
					g3.drawImage(offimg, 0, 0, this);
					g3.drawImage(Bike[bike_image], bikeX, bikeY, this);
					g3.drawImage(ammo[ammo_image], ammoX, ammoY, this);
					g3.drawImage(hand[hand_image], handX, handY, this);

					g3.setColor(Color.black);
					g3.fillRect(0,250,420,50);

					drawX = 25;

					for(int f = 1; f <= ammo_amt; f++){
						g3.drawImage(ammo[2], drawX, 270, this);
						drawX += 28;
					}

					drawX = 300;

					for(int f = 1; f <= (3-numhits); f++){
						g3.drawImage(bikes_left, drawX, 265, this);
						drawX += 38;
					}

//					g3.setColor(Color.white);
//					g3.drawString("Ammo remaining: "+ammo_amt+"    Hits Needed:"+ (3-numhits) +"     Level "+current_level, 25, 275);

					tempimg = offimg2;
					repaint();

					if(bike_image >= 8){
						pause(1000);
						bikeY = 50;
					}
					
					pause(30);
				}
			}

			START_GAME = false;
			FINISHED = DONE = false;
		}
	}

	public boolean mouseDown(Event evt, int x, int y){
		// START_GAME = true;  <OR>  fires ammo unless
		// ammo already in air

		if(!START_GAME && GO)
			START_GAME = true;

		if(!AMMO_AWAY){
			AMMO_AWAY = true;
			hand_image = 1;
			handY -= 40;
			ammo_amt--;
			ammoX = x - 37;
		}

		return true;
	}

	public boolean mouseMove(Event evt, int x, int y){
		// moves the hand
		handX = x - 37;

		if(!AMMO_AWAY)
			ammoX = x - 37;

		return true;
	}

	public boolean checkCollision(){
		// Check for collision between ammo and bike
		boolean collision = false;

		if(ammoX > bikeX && ammoX < bikeX + 120)
			collision = true;

		return(collision);
	}

	public void showScn(int scn_num){
		switch(scn_num){
		case 0:
			if(youlose != null)
				youlose.play();
			g3.drawImage(you_lose, 0, 0, this);
			break;
		case 1:
			//  Win animations!!!!
	
			if(stopithurts != null)
				stopithurts.play();
			
			for(int i = 0; i <= 4; i++){
				g3.drawImage(you_win[i], 0, 0, this);
				tempimg = offimg2;
				repaint();
				pause(600);
			}
			
			break;
		}

		tempimg = offimg2;
		repaint();

		START_GAME = false;
		
		do{
			pause(50);
		}while(!START_GAME);
		
	}
	
	public void showCredits(){
		int fontX, fontY;
		
		credits[0] = "Steve C";
		credits[1] = "the poser";
		credits[2] = "Jared Stehler";
		credits[3] = "game program";
		credits[4] = "Eric Miller";
		credits[5] = "image work";
		credits[6] = "Punish the Poser";
		credits[7] = "Copyright © 2000 JE Bikes";

		for(int i = 0; i <= 7; i += 2){
			g3.setColor(Color.black);
			g3.fillRect(0, 0, 420, 300);	
			g3.setColor(Color.white);
		
			g3.setFont(credits_font_big);
			
			fontX = (420 - fm_big.stringWidth(credits[i])) / 2;
			fontY = 150;
			
			g3.drawString(credits[i], fontX, fontY);
			
			g3.setFont(credits_font_small);
			
			fontX = (420 - fm_small.stringWidth(credits[i+1])) / 2;
			fontY = 170;
			
			g3.drawString(credits[i+1], fontX, fontY);
			
			repaint();
			pause(2000);
		}
		pause(2000);
	}

	public void pause(int time){
		try{ Thread.sleep(time); }
			catch(InterruptedException e) {}
	}

	public void update(Graphics g){
		g.clipRect(0, 0, 420, 300);
		paint(g);
	}

	public void paint(Graphics g){
		if(tempimg != null)
			g.drawImage(tempimg, 0, 0, this);
	}
}

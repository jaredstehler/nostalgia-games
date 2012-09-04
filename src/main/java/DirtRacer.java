/*
	Project Monkshood
	Copyright 1999 J&E Bicycles
	All Rights Reserved.
	4.29.99

	v1.0
*/

import java.awt.image.*;
import java.awt.*;
//import java.net.*;

public class DirtRacer extends java.applet.Applet implements Runnable{

	Thread runner;

	final int width = 280, height = 300;
	final int sector_size = 60;

	final int num_levels = 10;

	final int num_obstacles = 23;
	final int num_crashables = 10;
	final int num_slowdn = 10;

	Font f = new Font("TimesRoman", Font.PLAIN, 14);
	Font dispFont = new Font("TimesRoman", Font.BOLD, 20);
	
	Image Title[] = new Image[4];
	Image Street[] = new Image[5];
	Image Sidewalk[] = new Image[2];
	Image Obstacles[] = new Image[num_obstacles];
	Image Bike[] = new Image[11];

	Image Animations[] = new Image[11];
	Image count[] = new Image[4];

	Image CurrentSidewalk[] = new Image[2];
	Image CurrentStreet[] = new Image[2];

	Image left_side, right_side;

	Image tempimg, CurrentBikeImage, drawPercent, START, loadingScreen, levelComplete, displayimage;

	Image count1, count2, count3, count_go, highScoresBG, youWin, game_over, win_scn;

	Image offimg, offimg2;
	Graphics g2, g3;

	long start_time, end_time, total_time;

	double time[] = new double[num_levels+1];

	double time_to_beat;

	int ObstacleLocation[][] = new int[207][7];
	
	int current_level, current_sector, current_lane, current_score, current_screen, current_speed, current_damage;

	int percent_done, lvl_num_sectors, panIncrement, speed, speed_tracker;

	boolean DONE, LEVEL_SUCCESS, GAME_DONE, CRASHED, SLOW_SPEED, FAST_SPEED;
	boolean CRASHING = false;

	int x = 1, y = 1, panX, bikex, bikey;

	private MediaTracker tracker;


	public void init(){
		offimg = createImage(280, 360);
		offimg2 = createImage(280, 405);

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
		int loop;

		g3.setColor(Color.white);
		g3.fillRect(0, 0, 280, 300);

		tempimg = offimg2;
		y = 0;
		repaint();

//	Load all necessary images

		Title[0] = getImage(getCodeBase(), "dirtracer/title.jpg");

		loadingScreen = getImage(getCodeBase(), "dirtracer/loading_screen.jpg");
		levelComplete = getImage(getCodeBase(), "dirtracer/level_complete.jpg");
//		youWin = getImage(getCodeBase(), "dirtracer/you_win.jpg");
//		win_scn = getImage(getCodeBase(), "dirtracer/win_screen_1.jpg");
//		game_over = getImage(getCodeBase(), "dirtracer/game_over.jpg");
//		displayimage = getImage(getCodeBase(), "dirtracer/display_scn.jpg");

		count[1] = getImage(getCodeBase(), "dirtracer/1.gif");
		count[2] = getImage(getCodeBase(), "dirtracer/2.gif");
		count[3] = getImage(getCodeBase(), "dirtracer/3.gif");
		count[0] = getImage(getCodeBase(), "dirtracer/go.gif");

//		shadow = getImage(getCodeBase(), "dirtracer/shadow.gif");

//		highScoresBG = getImage(getCodeBase(), "dirtracer/clouds.jpg");

		Animations[0] = getImage(getCodeBase(), "dirtracer/lose_1.gif");
		Animations[1] = getImage(getCodeBase(), "dirtracer/lose_2.gif");
		Animations[2] = getImage(getCodeBase(), "dirtracer/lose_3.gif");
		Animations[3] = getImage(getCodeBase(), "dirtracer/lose_4.gif");
		Animations[4] = getImage(getCodeBase(), "dirtracer/lose_5.gif");

		Animations[5] = getImage(getCodeBase(), "dirtracer/win_1.gif");
		Animations[6] = getImage(getCodeBase(), "dirtracer/win_2.gif");
		Animations[7] = getImage(getCodeBase(), "dirtracer/win_3.gif");
		Animations[8] = getImage(getCodeBase(), "dirtracer/win_4.gif");
		Animations[9] = getImage(getCodeBase(), "dirtracer/win_5.gif");

		Animations[10] = getImage(getCodeBase(), "dirtracer/dirt_jump.jpg");

		left_side = getImage(getCodeBase(), "dirtracer/trees_left.jpg");
		right_side = getImage(getCodeBase(), "dirtracer/trees_right.jpg");

		Sidewalk[1] = getImage(getCodeBase(), "dirtracer/dirt_left.jpg");
		Street[0] = getImage(getCodeBase(), "dirtracer/dirt.jpg");
		Street[1] = getImage(getCodeBase(), "dirtracer/dirt.jpg");
		Sidewalk[0] = getImage(getCodeBase(), "dirtracer/dirt_right.jpg");

		Bike[0] = getImage(getCodeBase(), "dirtracer/bike_1.gif");
		Bike[1] = getImage(getCodeBase(), "dirtracer/bike_2.gif");
		Bike[2] = getImage(getCodeBase(), "dirtracer/bike_3.gif");
		Bike[3] = getImage(getCodeBase(), "dirtracer/bike_4.gif");
		Bike[4] = getImage(getCodeBase(), "dirtracer/bike_5.gif");
		Bike[5] = getImage(getCodeBase(), "dirtracer/bike_6.gif");
		Bike[6] = getImage(getCodeBase(), "dirtracer/bike_7.gif");
		Bike[7] = getImage(getCodeBase(), "dirtracer/bike_8.gif");
		Bike[8] = getImage(getCodeBase(), "dirtracer/bike_9.gif");
		Bike[9] = getImage(getCodeBase(), "dirtracer/bike_10.gif");
		Bike[10] = getImage(getCodeBase(), "dirtracer/bike_11.gif");

		Obstacles[0] = getImage(getCodeBase(), "dirtracer/creek_1.jpg");
		Obstacles[1] = getImage(getCodeBase(), "dirtracer/creek_2.jpg");
		Obstacles[2] = getImage(getCodeBase(), "dirtracer/creek_3.jpg");
		Obstacles[3] = getImage(getCodeBase(), "dirtracer/creek_4.jpg");
		Obstacles[4] = getImage(getCodeBase(), "dirtracer/log_one.gif");
		Obstacles[5] = getImage(getCodeBase(), "dirtracer/log_two.gif");
		Obstacles[6] = getImage(getCodeBase(), "dirtracer/log_three.gif");
		Obstacles[7] = getImage(getCodeBase(), "dirtracer/log_four.gif");
		Obstacles[8] = getImage(getCodeBase(), "dirtracer/rock.gif");
		Obstacles[9] = getImage(getCodeBase(), "dirtracer/stump.gif");

		Obstacles[10] = getImage(getCodeBase(), "dirtracer/bush.gif");
		Obstacles[11] = getImage(getCodeBase(), "dirtracer/ditch.gif");
		Obstacles[12] = getImage(getCodeBase(), "dirtracer/puddle.gif");
		Obstacles[13] = getImage(getCodeBase(), "dirtracer/washboard.jpg");

		Obstacles[14] = getImage(getCodeBase(), "dirtracer/cut_right.jpg");
		Obstacles[15] = getImage(getCodeBase(), "dirtracer/cut_left.jpg");
		Obstacles[16] = getImage(getCodeBase(), "dirtracer/fade_right.jpg");
		Obstacles[17] = getImage(getCodeBase(), "dirtracer/fade_left.jpg");
		Obstacles[18] = getImage(getCodeBase(), "dirtracer/full_grass.jpg");

		Obstacles[19] = getImage(getCodeBase(), "dirtracer/zipper.gif");

		Obstacles[20] = getImage(getCodeBase(), "dirtracer/bridge.jpg");
		Obstacles[21] = getImage(getCodeBase(), "dirtracer/finish_line.gif");
		Obstacles[22] = getImage(getCodeBase(), "dirtracer/start_line.gif");

		tracker = new MediaTracker(this);		
		
		tracker.addImage(Title[0], 0);

//		tracker.addImage(displayimage, 0);
		tracker.addImage(loadingScreen, 0);
		tracker.addImage(levelComplete, 1);
//		tracker.addImage(youWin, 1);
//		tracker.addImage(win_scn, 1);
//		tracker.addImage(game_over, 1);
		tracker.addImage(count[0], 0);
		tracker.addImage(count[1], 0);
		tracker.addImage(count[2], 0);
		tracker.addImage(count[3], 0);

		tracker.addImage(right_side, 0);
		tracker.addImage(left_side, 0);
		tracker.addImage(Street[0], 0);
		tracker.addImage(Street[1], 0);
//		tracker.addImage(Street[2], 5);
//		tracker.addImage(Street[3], 5);
//		tracker.addImage(Street[4], 5);
		tracker.addImage(Sidewalk[0], 0);
		tracker.addImage(Sidewalk[1], 0);

//		tracker.addImage(highScoresBG, 0);

		tracker.addImage(Animations[0], 0);
		tracker.addImage(Animations[1], 1);
		tracker.addImage(Animations[2], 1);
		tracker.addImage(Animations[3], 0);
		tracker.addImage(Animations[4], 0);
		tracker.addImage(Animations[5], 0);
		tracker.addImage(Animations[6], 1);
		tracker.addImage(Animations[7], 1);
		tracker.addImage(Animations[8], 0);
		tracker.addImage(Animations[9], 0);
		tracker.addImage(Animations[10], 0);

		tracker.addImage(Obstacles[0], 2);
		tracker.addImage(Obstacles[1], 2);
		tracker.addImage(Obstacles[2], 2);
		tracker.addImage(Obstacles[3], 2);
		tracker.addImage(Obstacles[4], 2);
		tracker.addImage(Obstacles[5], 2);
		tracker.addImage(Obstacles[6], 2);
		tracker.addImage(Obstacles[7], 2);
		tracker.addImage(Obstacles[8], 2);
		tracker.addImage(Obstacles[9], 2);
		tracker.addImage(Obstacles[10], 2);
		tracker.addImage(Obstacles[11], 2);
		tracker.addImage(Obstacles[12], 2);
		tracker.addImage(Obstacles[13], 2);
		tracker.addImage(Obstacles[14], 2);
		tracker.addImage(Obstacles[15], 2);
		tracker.addImage(Obstacles[16], 2);
		tracker.addImage(Obstacles[17], 2);
		tracker.addImage(Obstacles[18], 2);
		tracker.addImage(Obstacles[19], 2);
		tracker.addImage(Obstacles[20], 2);
		tracker.addImage(Obstacles[21], 2);
		tracker.addImage(Obstacles[22], 2);

		tracker.addImage(Bike[0], 1);
		tracker.addImage(Bike[1], 1);
		tracker.addImage(Bike[2], 1);
		tracker.addImage(Bike[3], 1);
		tracker.addImage(Bike[4], 1);
		tracker.addImage(Bike[5], 1);
		tracker.addImage(Bike[6], 1);
		tracker.addImage(Bike[7], 1);
		tracker.addImage(Bike[8], 1);
		tracker.addImage(Bike[9], 1);
		tracker.addImage(Bike[10], 1);

		try{
			tracker.waitForID(0);
			g3.setColor(Color.white);
			g3.fillRect(0, 0, 280, 300);
			g3.setColor(Color.black);
			g3.drawString("Loading Game Images.....", 40, 150);
			tempimg = offimg2;
			repaint();
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }

		if((tracker.statusID(0, true) & MediaTracker.ERRORED) != 0) {
			System.out.println("Error loading images!");

			g3.setFont(f);
			g3.setColor(Color.red);
			g3.fillRect(0, 0, 300, 280);
			g3.setColor(Color.black);
			g3.drawString("Error loading images!", 120, 140);
			tempimg = offimg2;
			x = 0;
			repaint();

//			stop();
		}

		try{
			tracker.waitForID(1);
			g3.setColor(Color.white);
			g3.fillRect(0, 0, 280, 300);
			g3.setColor(Color.black);
			g3.drawString("Loading Bike Images.....", 40, 150);
			tempimg = offimg2;
			repaint();
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }

		try{
			tracker.waitForID(2);
			g3.setColor(Color.white);
			g3.fillRect(0, 0, 280, 300);
			g3.setColor(Color.black);
			g3.drawString("Loading Obstacle Images.....", 40, 150);
			
			tempimg = offimg2;
			repaint();

		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }

		drawPercent = loadingScreen;

		do{
		}while(drawPercent == null);

		//Display title screen 
		//wait for START = true
		//...
	do{	
		int imagenum = 0;
		x = y = 0;

		do{
			g2.drawImage(Title[imagenum], 0, 0, this);
			tempimg = offimg;
			repaint();

			pause(800);
		
		}while(START == null);

		showLoading();
		clearOffscn();

		getAppletContext().showStatus("Copyright 1999 J&E Bicycles.");

		clearLevel();

		try{
			tracker.waitForID(1);
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }

		pause(400);

///////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////
		current_level = 1;
///////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////
		current_sector = 2;
		current_lane = 3;
		current_screen = current_score = current_damage = 0;

		loadLevel(current_level);

		redrawOffscn(0);
		redrawOffscn(1);

//		BUNNYHOP = false;
//		NO_LANE_CHANGE = false;
		CRASHED = SLOW_SPEED = FAST_SPEED = false;

		x = y = panX = /*lvl_pct_done = hopHeight = hopLength =*/ 0;
		
		int trackernum = 0, bikeimagenum = 0;

		CurrentBikeImage = Bike[0];

//		current_speed = 150;  // 90 = 16.40 fps

		panIncrement = speed;

		pause(1000);

		getAppletContext().showStatus("http://www.jebikes.com/");

	//Main Game Loop

	do{

		//Level Loop

		g3.drawImage(offimg, 0, -60, this);
		g3.drawImage(CurrentBikeImage, 130, 180, this);
		g3.setColor(Color.yellow);
		g3.setFont(dispFont);			

		g3.drawString("Time needed to advance:", 40, 40);
		g3.drawString(" "+time_to_beat+" seconds", 80, 60);

		g3.drawString("Level: "+current_level+"    Damage: "+current_damage+"%   "+current_sector, 20, 270);
		g3.drawString("Time: "+time_to_beat, 80, 290);
		g3.setColor(Color.black);

//		redrawDisplay();

		tempimg = offimg2;
		x = 0;
		y = 0;

		repaint();

		loop = 3;

		do{
			g3.drawImage(count[loop], 85, 96, this);
			tempimg = offimg2;
			x = 0;
			y = 0;
			repaint();
		
			pause(1000);
		
			loop--;
		}while(loop >= 0);

		start_time = (long)(System.currentTimeMillis() + (time_to_beat * 1000));
//	 	start_time = System.currentTimeMillis();

		do{
			panScreen();		

			trackernum++;

			if(ObstacleLocation[current_sector][current_lane] > 0){
				if(ObstacleLocation[current_sector][current_lane] <= 10){
					SLOW_SPEED = FAST_SPEED = false;

					CRASHED = true;
					crash();
				}
				
				else if(ObstacleLocation[current_sector][current_lane] > 10 && ObstacleLocation[current_sector][current_lane] < 20){
					FAST_SPEED = false;					

					SLOW_SPEED = true;
					panIncrement = speed - 2;
				}
			
				else if(ObstacleLocation[current_sector][current_lane] == 20){
					SLOW_SPEED = false;
							
					FAST_SPEED = true;
					speed_tracker = 1;
				}
			}

			else if(SLOW_SPEED == true){
				SLOW_SPEED = false;
				panIncrement = speed;
			}							

			if(trackernum >= 7){
				trackernum = 0;
				++bikeimagenum;
				if(bikeimagenum > 3)
					bikeimagenum = 0;
				if(panIncrement != 0)
					CurrentBikeImage = Bike[bikeimagenum];
			}

			if(FAST_SPEED == true){
				speed_tracker++;

				if(speed_tracker > 75){
					FAST_SPEED = false;
					panIncrement = speed;
				}

//				else if(speed_tracker < 8 || speed_tracker > 37)
//					panIncrement = speed + 1;
				
				else
					panIncrement = speed + 3;
			}

			total_time = start_time - System.currentTimeMillis();

//			end_time = System.currentTimeMillis();
//			total_time = end_time - start_time;

			time[current_level] = total_time / 1000.0000;
		
//			if(time[current_level] > time_to_beat){
			
			if(time[current_level] <= 0){
				DONE = true;
				LEVEL_SUCCESS = false;
				g3.setFont(dispFont);
				g3.setColor(Color.yellow);

				g3.drawString("Out of Time!", 75, 75+panX);
				tempimg = offimg2;
				repaint();
				pause(2000);
			}

			g3.drawImage(offimg, 0, 0, this);
			drawBike();
			bikey = 240 - panX + 15;
			g3.drawImage(CurrentBikeImage, bikex, bikey, this);
			redrawDisplay();

			tempimg = offimg2;
			y = panX - 60;
			repaint();

			pause(25);

		}while(DONE == false); //End Level Loop

		if(LEVEL_SUCCESS == false){
			GAME_DONE = true;
		
			clearLevel();

			loserAnimation();
/*
			g3.drawImage(game_over, 0, 0, this);
			x = 0;
			repaint();
*/
//			pause(5000);

			break;
		}

		showLevelComplete();
//		pause(2000);

		time[current_level] = time_to_beat - time[current_level];

		g3.setColor(Color.white);
		g3.setFont(f);			

		g3.drawString("End Level Bonus: "+(current_level*100), 35, 120);
		tempimg = offimg2;
		repaint();
		pause(1000);
		current_score += current_level * 100;

		g3.setColor(Color.yellow);
		g3.drawString("Time Bonus ("+(int)(time_to_beat - time[current_level])+" sec): "+(int)((time_to_beat - time[current_level])*25), 35, 140);
		tempimg = offimg2;
		repaint();
		pause(1000);
		current_score += (int)((time_to_beat - time[current_level]) * 25);
		g3.setColor(Color.white);

		g3.drawString("Level Time: "+time[current_level], 35, 220);
		tempimg = offimg2;
		repaint();
		pause(1000);

		if(CRASHED == false){
			g3.setColor(Color.yellow);
			g3.drawString("Crashless bonus: 50", 35, 160);
			tempimg = offimg2;
			repaint();
			pause(1000);
			g3.setColor(Color.white);
			current_score += 100;
		}

		if(current_damage == 0 && current_level == 10){
			g3.setColor(Color.yellow);
			g3.drawString("Super Crashless bonus: 500", 35, 180);
			tempimg = offimg2;
			repaint();
			pause(1000);
			g3.setColor(Color.white);
			current_score += 1000;
		}

		g3.drawString("New Total: "+current_score, 35, 200);
		tempimg = offimg2;
		repaint();
		pause(2000);

		if(current_level == num_levels){
			winGame();
			GAME_DONE = true;
			clearLevel();
			break;
		}			

		g3.drawString("Loading Level "+(current_level+1)+"...", 100, 250);
		tempimg = offimg2;
		repaint();
		pause(1000);

//		showLoading();
		clearOffscn();

//		BUNNYHOP = false;
		CRASHED = SLOW_SPEED = FAST_SPEED = false;

		current_level++;

		current_sector = 2;
		current_screen = 0;
		current_lane = 3;

		loadLevel(current_level);

		redrawOffscn(0);
//		redrawOffscn(1);

		x = y = panX = 0;
		
		panIncrement = speed;

		trackernum = bikeimagenum = 0;

		CurrentBikeImage = Bike[0];

		current_speed = 150;

		pause(1000);

		DONE = false;
		
	}while(GAME_DONE == false);

		START = null;
		GAME_DONE = false;
		DONE = false;
		panIncrement = 0;
		SLOW_SPEED = FAST_SPEED = false;

	}while(drawPercent != null);

	stop();
	}


	public boolean keyDown(Event evt, int key){
		switch(key){
			case Event.DOWN:  
					break;
			case Event.UP:  
					break;
			case Event.LEFT:  
					if(panIncrement != 0 && !CRASHING){
						--current_lane;

						if(current_lane < 1)
							current_lane = 1;
					}
					break;
			case Event.RIGHT:
					if(panIncrement != 0 && !CRASHING){
						++current_lane;
							
						if(current_lane > 5)
							current_lane = 5;
					}
					break;
			default:
				break;
		}

		return true;
	}

	public boolean keyUp(Event evt, int key){
		if(START != null)
			START = Bike[0];

		switch(key){
			case Event.DOWN:
					SLOW_SPEED = false;
					FAST_SPEED = false;
					panIncrement = 0;
					break;
			case Event.UP:
					if(panIncrement == 0)
						panIncrement = speed;
					break;
			case Event.LEFT:
					break;
			case Event.RIGHT:
					break;
			default:
				if(key == 83 || key == 115){
					START = drawPercent;
					speed = 6;
				}
				break;
		}

//  D		
		if(key == 68)
			current_damage += 10;

//  T
		else if(key == 84)
			start_time -= 1000;

//  U
		else if(key == 85){
			DONE = true;
			LEVEL_SUCCESS = true;
		}

//  W
		else if(key == 87){
			current_level = 10;
			DONE = true;
			LEVEL_SUCCESS = true;
		}

		return true;
	}

	public int drawBike(){
		switch(current_lane){
			case 1:  bikex = 40+10;
				break;
			case 2:  bikex = 80+10;
				break;
			case 3:  bikex = 120+10;
				break;
			case 4:  bikex = 160+10;
				break;
			case 5:  bikex = 200+10;
				break;
		}

		return(bikex);
		
	}
		
	
	public int panScreen(){
		panX+=panIncrement;

		if(panX >= sector_size){
			++current_sector;

			if(current_sector >= lvl_num_sectors){
				DONE = true;
				LEVEL_SUCCESS = true;
			}

			panX = 0;
			redrawOffscn(current_screen);
//			current_screen = toggleOffScreenImage();
		}
		return panX;
	}
			

	public void redrawOffscn(int screen){
		if(screen == 0){
			draw(current_sector - 1, 5);
			draw(current_sector, 4);
			draw(current_sector + 1, 3);
			draw(current_sector + 2, 2);
			draw(current_sector + 3, 1);
			draw(current_sector + 4, 0);
		}

		else{
			draw(current_sector - 1, 5);
			draw(current_sector, 4);
			draw(current_sector + 1, 3);
			draw(current_sector + 2, 2);
			draw(current_sector + 3, 1);
			draw(current_sector + 4, 0);
		}
	}

	
	public void draw(int sectornum, int rownum){
		int drawy = rownum * 60;

		g2.drawImage(left_side, 0, drawy, this);
	
		g2.drawImage(CurrentSidewalk[1], 40, drawy, this);

		g2.drawImage(CurrentStreet[1], 80, drawy, this);

		g2.drawImage(CurrentStreet[0], 120, drawy, this);

		g2.drawImage(CurrentStreet[1], 160, drawy, this);

		g2.drawImage(CurrentSidewalk[0], 200, drawy, this);

		g2.drawImage(right_side, 240, drawy, this);

			if(ObstacleLocation[sectornum][1] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][1]-1], 40, drawy, this);

			if(ObstacleLocation[sectornum][2] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][2]-1], 80, drawy, this);

			if(ObstacleLocation[sectornum][3] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][3]-1], 120, drawy, this);

			if(ObstacleLocation[sectornum][4] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][4]-1], 160, drawy, this);

			if(ObstacleLocation[sectornum][5] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][5]-1], 200, drawy, this);
	}

	public void crash(){
		int tempx;

		CRASHING = true;

		if(panX != 0)
			tempx = 0 - panX;

		else
			tempx = 0;

		panIncrement = 0;
		
		FAST_SPEED = SLOW_SPEED = false;
		
		current_damage += 10;
		if(current_damage >= 100){
			LEVEL_SUCCESS = false;
			DONE = true;
		}

		int animnum = 4;

		do{
			y = tempx;

			g3.drawImage(offimg, 0, 0, this);
			g3.drawImage(Bike[animnum], bikex, bikey, this);

			g3.setColor(Color.yellow);
			g3.setFont(dispFont);			

			g3.drawString("Level: "+current_level+"    Damage: "+current_damage+"%   "+current_sector, 20, 270+panX);
			g3.drawString("Time: "+time[current_level], 80, 290+panX);
			g3.setColor(Color.black);

//			redrawDisplay();

			tempimg = offimg2;

			repaint();
	
			pause(75);
			animnum++;
		}while(animnum <= 6);

		pause(1500);

/*		++current_lane;
		
		if(current_lane > 4)
			current_lane-=2;
*/

		current_sector++;
		redrawOffscn(current_screen);

		if(ObstacleLocation[current_sector][current_lane] != 0){

		animnum = 0;

		do{
			animnum++;
			current_lane++;
			if(current_lane > 5)
				current_lane = 1;
			if(animnum >= 5)
				break;
		}while(ObstacleLocation[current_sector][current_lane] != 0);

		if(ObstacleLocation[current_sector][current_lane] != 0){
			current_sector++;
			panX = 0;
			redrawOffscn(current_screen);
			animnum = 0;

			do{
				animnum++;
				current_lane++;
				if(current_lane > 5)
					current_lane = 1;
				if(animnum >= 5)
					break;
			}while(ObstacleLocation[current_sector][current_lane] != 0);
		}
	
		}

//		int loopnum = 0;

		current_score -= 100;

		if(current_score <= 0)
			current_score = 0;

		CRASHING = false;
	}

	public void loserAnimation(){
		int showx = drawBike(), showy = 240, imgnum = 0;
		x = 0;

		do{
			g3.drawImage(offimg, 0, 0, this);
			redrawDisplay();
			g3.drawImage(Animations[imgnum], showx, showy, this);
			tempimg = offimg2;
			repaint();
		
			pause(75);
			imgnum++;
		}while(imgnum <= 3);

		g3.drawImage(Animations[4], showx-20, showy-10, this);
		tempimg = offimg2;
		repaint();

		pause(1000);
	}

	public void winGame(){
		int winloop = 1, spriteX, spriteY = 240, num;

		do{
			if(winloop < 45 || winloop >= 115){
				spriteX = 130;
				num = 5;
			}
			else{
				spriteX = 110;
				
				if(winloop < 60)
					num = 6;
				else if(winloop < 75)
					num = 7;
				else if(winloop < 90)
					num = 8;
				else if(winloop < 105)
					num = 9;
				else
					num = 6;
			}

			g3.drawImage(offimg, 0, 0, this);
			g3.drawImage(Animations[10], 120, 150, this);
			g3.drawImage(Animations[num], spriteX, spriteY, this);
			tempimg = offimg2;
			repaint();

			pause(25);

			winloop++;
			spriteY -= 2;
		}while(winloop < 140);	
		

		//Any winning animation goes here!
/*
		g3.drawImage(win_scn, 0, 0, this);
		tempimg = offimg2;
		x = 0;
		repaint();
		pause(3000);
*/

//////////////////////////////////////////////////////
////////          CREDITS                   //////////
//////////////////////////////////////////////////////

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 280, 300);
		g3.setColor(Color.white);
		g3.drawString("Credits", 113, 130);
		tempimg = offimg2;
		repaint();			
		pause(2000);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 280, 300);
		g3.setColor(Color.white);
		g3.drawString("produced by", 100, 115);
		g3.drawString("J&E Bicycles", 97, 130);
		tempimg = offimg2;
		repaint();			
		pause(2000);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 280, 300);
		g3.setColor(Color.white);
		g3.drawString("game program", 95, 115);
		g3.drawString("Jared Stehler", 93, 130);
		tempimg = offimg2;
		repaint();			
		pause(2000);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 280, 300);
		g3.setColor(Color.white);
		g3.drawString("artwork, level design", 84, 115);
		g3.drawString("Eric Miller", 113, 130);
		tempimg = offimg2;
		repaint();			
		pause(2000);
/*
		g3.setColor(Color.black);
		g3.fillRect(0, 0, 280, 300);
		g3.setColor(Color.white);
		g3.drawString("beta testers", 110, 110);
		g3.drawString("Dean Bourque", 113, 130);
		g3.drawString("Dave Maier", 113, 145);
		g3.drawString("Brian Peer", 113, 160);
		g3.drawString("Evan Pierce", 113, 175);
		tempimg = offimg2;
		repaint();			
		pause(3000);
*/
		g3.setColor(Color.black);
		g3.fillRect(0, 0, 280, 300);
		g3.setColor(Color.white);
		g3.drawString("Dirt Racer v1.0", 74, 110);
		g3.drawString("Copyright 1999 J&E Bicycles", 60, 125);
		g3.drawString("All Rights Reserved", 74, 140);
		tempimg = offimg2;
		repaint();			
		pause(3000);

		clearOffscn();
		
//		TextField tf = new TextField("Enter your e-mail address:", 30);
		
//		do{}while(emailAddress == null);
		
//		scores.addScore(emailAddress, current_score);		

//		scores.showHighScores(g3);
	}
	

	public void loadLevel(int levelnum){

		switch(levelnum){
			case 1:lvl_num_sectors = 102;	
				time_to_beat = 60;
				break;

			case 2:lvl_num_sectors = 76;
				time_to_beat = 45;
				break;

			case 3:lvl_num_sectors = 75;
				time_to_beat = 40;
				break;

			case 4:lvl_num_sectors = 75;
				time_to_beat = 38;
				break;

			case 5:lvl_num_sectors = 76;
				time_to_beat = 45;
				break;

			case 6:lvl_num_sectors = 73;
				time_to_beat = 48;
				break;

			case 7:lvl_num_sectors = 100;
				time_to_beat = 55;
				break;

			case 8:lvl_num_sectors = 81;
				time_to_beat = 40;
				break;

			case 9:lvl_num_sectors = 75;
				time_to_beat = 40;
				break;

			case 10:lvl_num_sectors = 120;
				time_to_beat = 72;
				break;
		}

		int loopvar = 1, loopvar2 = 1, picnum = 0;

		CurrentSidewalk[0] = Sidewalk[0];
		CurrentSidewalk[1] = Sidewalk[1];
		CurrentStreet[0] = Street[0];
		CurrentStreet[1] = Street[1];

		switch(levelnum){
			case 1:	
ObstacleLocation[2][1]=23;
ObstacleLocation[7][1]=11;
ObstacleLocation[8][3]=12;
ObstacleLocation[9][3]=12;
ObstacleLocation[10][3]=12;
ObstacleLocation[11][3]=12;
ObstacleLocation[11][1]=20;
ObstacleLocation[11][5]=20;
ObstacleLocation[14][4]=11;
ObstacleLocation[16][5]=12;
ObstacleLocation[18][5]=12;
ObstacleLocation[17][5]=12;
ObstacleLocation[18][2]=11;
ObstacleLocation[19][2]=20;
ObstacleLocation[22][1]=1;
ObstacleLocation[22][2]=2;
ObstacleLocation[22][3]=21;
ObstacleLocation[22][4]=3;
ObstacleLocation[22][5]=4;
ObstacleLocation[26][4]=11;
ObstacleLocation[28][2]=11;
ObstacleLocation[33][1]=5;
ObstacleLocation[33][2]=6;
ObstacleLocation[33][4]=7;
ObstacleLocation[33][5]=8;
ObstacleLocation[35][1]=20;
ObstacleLocation[35][3]=11;
ObstacleLocation[38][4]=11;
ObstacleLocation[40][2]=11;
ObstacleLocation[42][5]=11;
ObstacleLocation[44][1]=11;
ObstacleLocation[48][5]=11;
ObstacleLocation[47][4]=11;
ObstacleLocation[47][3]=11;
ObstacleLocation[46][2]=11;
ObstacleLocation[50][1]=1;
ObstacleLocation[50][2]=2;
ObstacleLocation[50][3]=3;
ObstacleLocation[50][4]=21;
ObstacleLocation[50][5]=4;
ObstacleLocation[51][5]=20;
ObstacleLocation[53][5]=11;
ObstacleLocation[55][4]=12;
ObstacleLocation[56][4]=12;
ObstacleLocation[57][4]=12;
ObstacleLocation[58][4]=12;
ObstacleLocation[59][4]=12;
ObstacleLocation[57][1]=20;
ObstacleLocation[58][2]=11;
ObstacleLocation[64][1]=1;
ObstacleLocation[64][2]=21;
ObstacleLocation[64][3]=2;
ObstacleLocation[64][4]=3;
ObstacleLocation[64][5]=4;
ObstacleLocation[67][3]=11;
ObstacleLocation[68][2]=11;
ObstacleLocation[73][1]=5;
ObstacleLocation[73][3]=7;
ObstacleLocation[73][4]=6;
ObstacleLocation[73][5]=8;
ObstacleLocation[76][2]=20;
ObstacleLocation[77][4]=11;
ObstacleLocation[78][2]=12;
ObstacleLocation[79][2]=12;
ObstacleLocation[80][2]=12;
ObstacleLocation[79][5]=12;
ObstacleLocation[80][5]=12;
ObstacleLocation[81][5]=12;
ObstacleLocation[82][5]=12;
ObstacleLocation[82][1]=11;
ObstacleLocation[83][3]=12;
ObstacleLocation[84][3]=12;
ObstacleLocation[85][3]=12;
ObstacleLocation[87][1]=5;
ObstacleLocation[87][2]=7;
ObstacleLocation[87][3]=6;
ObstacleLocation[87][4]=8;
ObstacleLocation[90][1]=20;
ObstacleLocation[92][1]=15;
ObstacleLocation[93][1]=17;
ObstacleLocation[93][2]=15;
ObstacleLocation[93][4]=11;
ObstacleLocation[94][2]=19;
ObstacleLocation[95][2]=17;
ObstacleLocation[95][3]=15;
ObstacleLocation[96][1]=11;
ObstacleLocation[96][2]=16;
ObstacleLocation[96][3]=19;
ObstacleLocation[97][1]=19;
ObstacleLocation[97][2]=19;
ObstacleLocation[97][3]=19;
ObstacleLocation[98][1]=19;
ObstacleLocation[98][2]=19;
ObstacleLocation[98][3]=19;
ObstacleLocation[99][1]=19;
ObstacleLocation[99][2]=19;
ObstacleLocation[99][3]=19;
ObstacleLocation[100][1]=19;
ObstacleLocation[100][2]=19;
ObstacleLocation[100][3]=19;
ObstacleLocation[96][5]=12;
ObstacleLocation[97][5]=12;
ObstacleLocation[98][5]=12;
ObstacleLocation[101][1]=22;
				break;

			case 2:
				clearLevel();
ObstacleLocation[2][1]=23;
ObstacleLocation[4][1]=15;
ObstacleLocation[5][1]=19;
ObstacleLocation[6][1]=18;
ObstacleLocation[4][5]=16;
ObstacleLocation[5][5]=19;
ObstacleLocation[6][5]=17;
ObstacleLocation[9][5]=16;
ObstacleLocation[10][1]=19;
ObstacleLocation[11][1]=19;
ObstacleLocation[12][1]=19;
ObstacleLocation[13][1]=19;
ObstacleLocation[14][1]=19;
ObstacleLocation[15][1]=19;
ObstacleLocation[16][1]=19;
ObstacleLocation[17][1]=19;
ObstacleLocation[18][1]=19;
ObstacleLocation[19][1]=19;
ObstacleLocation[20][1]=19;
ObstacleLocation[21][1]=19;
ObstacleLocation[22][1]=19;
ObstacleLocation[23][1]=19;
ObstacleLocation[24][1]=19;
ObstacleLocation[25][1]=18;
ObstacleLocation[11][2]=15;
ObstacleLocation[12][2]=19;
ObstacleLocation[13][2]=19;
ObstacleLocation[14][2]=19;
ObstacleLocation[15][2]=19;
ObstacleLocation[16][2]=19;
ObstacleLocation[17][2]=19;
ObstacleLocation[18][2]=19;
ObstacleLocation[19][2]=19;
ObstacleLocation[20][2]=19;
ObstacleLocation[21][2]=19;
ObstacleLocation[22][2]=19;
ObstacleLocation[23][2]=19;
ObstacleLocation[24][2]=18;
ObstacleLocation[15][3]=15;
ObstacleLocation[16][3]=19;
ObstacleLocation[17][3]=18;
ObstacleLocation[10][4]=16;
ObstacleLocation[11][4]=19;
ObstacleLocation[12][4]=19;
ObstacleLocation[13][4]=17;
ObstacleLocation[19][4]=16;
ObstacleLocation[20][4]=19;
ObstacleLocation[21][4]=19;
ObstacleLocation[22][4]=19;
ObstacleLocation[23][4]=17;
ObstacleLocation[9][5]=16;
ObstacleLocation[10][5]=19;
ObstacleLocation[11][5]=19;
ObstacleLocation[12][5]=19;
ObstacleLocation[13][5]=19;
ObstacleLocation[14][5]=19;
ObstacleLocation[15][5]=19;
ObstacleLocation[16][5]=19;
ObstacleLocation[17][5]=19;
ObstacleLocation[18][5]=19;
ObstacleLocation[19][5]=19;
ObstacleLocation[20][5]=19;
ObstacleLocation[21][5]=19;
ObstacleLocation[22][5]=19;
ObstacleLocation[23][5]=19;
ObstacleLocation[24][5]=17;
ObstacleLocation[32][1]=16;
ObstacleLocation[33][1]=19;
ObstacleLocation[34][1]=19;
ObstacleLocation[35][1]=19;
ObstacleLocation[36][1]=19;

ObstacleLocation[38][1]=16;
ObstacleLocation[39][1]=18;
ObstacleLocation[29][2]=16;
ObstacleLocation[30][2]=19;
ObstacleLocation[31][2]=19;
ObstacleLocation[32][2]=19;
ObstacleLocation[33][2]=19;
ObstacleLocation[34][2]=19;
ObstacleLocation[35][2]=19;
ObstacleLocation[36][2]=17;
ObstacleLocation[37][2]=16;
ObstacleLocation[38][2]=18;
ObstacleLocation[29][3]=19;
ObstacleLocation[30][3]=19;
ObstacleLocation[32][3]=20;
ObstacleLocation[35][3]=15;
ObstacleLocation[36][3]=19;
ObstacleLocation[37][3]=18;
ObstacleLocation[29][4]=15;
ObstacleLocation[30][4]=19;
ObstacleLocation[31][4]=19;
ObstacleLocation[32][4]=19;
ObstacleLocation[33][4]=17;
ObstacleLocation[32][5]=15;
ObstacleLocation[33][5]=19;
ObstacleLocation[34][5]=19;
ObstacleLocation[35][5]=19;
ObstacleLocation[36][5]=17;
ObstacleLocation[43][2]=16;
ObstacleLocation[44][2]=19;
ObstacleLocation[45][2]=19;
ObstacleLocation[46][2]=17;
ObstacleLocation[40][5]=16;
ObstacleLocation[41][5]=19;
ObstacleLocation[42][5]=19;
ObstacleLocation[43][5]=19;
ObstacleLocation[44][5]=19;
ObstacleLocation[45][5]=19;
ObstacleLocation[46][5]=19;
ObstacleLocation[47][5]=19;
ObstacleLocation[48][5]=19;
ObstacleLocation[49][5]=17;
ObstacleLocation[41][4]=16;
ObstacleLocation[42][4]=19;
ObstacleLocation[43][4]=19;
ObstacleLocation[44][4]=19;
ObstacleLocation[45][4]=19;
ObstacleLocation[46][4]=19;
ObstacleLocation[47][4]=19;
ObstacleLocation[48][4]=17;
ObstacleLocation[42][3]=16;
ObstacleLocation[43][3]=19;
ObstacleLocation[44][3]=19;
ObstacleLocation[45][3]=19;
ObstacleLocation[46][3]=19;
ObstacleLocation[47][3]=17;
ObstacleLocation[48][1]=15;
ObstacleLocation[49][1]=19;
ObstacleLocation[50][1]=19;
ObstacleLocation[51][1]=19;
ObstacleLocation[52][1]=19;
ObstacleLocation[53][1]=19;
ObstacleLocation[54][1]=19;
ObstacleLocation[55][1]=19;
ObstacleLocation[56][1]=19;
ObstacleLocation[57][1]=18;
ObstacleLocation[49][2]=15;
ObstacleLocation[50][2]=19;
ObstacleLocation[51][2]=19;
ObstacleLocation[52][2]=19;
ObstacleLocation[53][2]=19;
ObstacleLocation[54][2]=19;
ObstacleLocation[55][2]=19;
ObstacleLocation[56][2]=18;
ObstacleLocation[50][3]=15;
ObstacleLocation[51][3]=19;
ObstacleLocation[52][3]=19;
ObstacleLocation[53][3]=19;
ObstacleLocation[54][3]=19;
ObstacleLocation[55][3]=18;
ObstacleLocation[51][4]=15;
ObstacleLocation[52][4]=19;
ObstacleLocation[53][4]=19;
ObstacleLocation[54][4]=18;
ObstacleLocation[66][1]=15;
ObstacleLocation[67][1]=19;
ObstacleLocation[68][1]=19;
ObstacleLocation[69][1]=19;
ObstacleLocation[70][1]=19;
ObstacleLocation[71][1]=18;
ObstacleLocation[62][2]=16;
ObstacleLocation[63][2]=19;
ObstacleLocation[64][2]=17;
ObstacleLocation[66][2]=20;
ObstacleLocation[67][2]=15;
ObstacleLocation[68][2]=19;
ObstacleLocation[69][2]=19;
ObstacleLocation[70][2]=18;
ObstacleLocation[62][3]=19;
ObstacleLocation[63][3]=19;
ObstacleLocation[64][3]=19;
ObstacleLocation[65][3]=17;
ObstacleLocation[72][3]=16;
ObstacleLocation[73][3]=19;
ObstacleLocation[74][3]=19;
ObstacleLocation[75][3]=19;
ObstacleLocation[62][4]=15;
ObstacleLocation[63][4]=19;
ObstacleLocation[64][4]=19;
ObstacleLocation[65][4]=19;
ObstacleLocation[66][4]=19;
ObstacleLocation[67][4]=19;
ObstacleLocation[68][4]=19;
ObstacleLocation[71][4]=16;
ObstacleLocation[72][4]=19;
ObstacleLocation[73][4]=19;
ObstacleLocation[74][4]=19;
ObstacleLocation[75][4]=19;
ObstacleLocation[58][5]=16;
ObstacleLocation[59][5]=19;
ObstacleLocation[60][5]=17;
ObstacleLocation[66][5]=20;
ObstacleLocation[70][5]=16;
ObstacleLocation[71][5]=19;
ObstacleLocation[72][5]=19;
ObstacleLocation[73][5]=19;
ObstacleLocation[74][5]=19;
ObstacleLocation[75][5]=19;
ObstacleLocation[76][1]=22;
	
				break;
			case 3:
				clearLevel();
ObstacleLocation[2][1]=23;
ObstacleLocation[6][1]=5;
ObstacleLocation[6][2]=6;
ObstacleLocation[6][3]=20;
ObstacleLocation[6][4]=7;
ObstacleLocation[6][5]=8;
ObstacleLocation[11][1]=5;
ObstacleLocation[11][2]=6;
ObstacleLocation[11][3]=7;
ObstacleLocation[11][4]=8;
ObstacleLocation[12][3]=20;
ObstacleLocation[14][1]=5;
ObstacleLocation[14][2]=8;
ObstacleLocation[14][4]=5;
ObstacleLocation[14][5]=8;
ObstacleLocation[16][5]=20;
ObstacleLocation[18][1]=15;
ObstacleLocation[19][1]=19;
ObstacleLocation[20][1]=19;
ObstacleLocation[21][1]=19;
ObstacleLocation[22][1]=19;
ObstacleLocation[23][1]=19;
ObstacleLocation[24][1]=19;
ObstacleLocation[25][1]=19;
ObstacleLocation[26][1]=19;
ObstacleLocation[27][1]=19;
ObstacleLocation[28][1]=19;
ObstacleLocation[29][1]=19;
ObstacleLocation[30][1]=19;
ObstacleLocation[31][1]=19;
ObstacleLocation[32][1]=19;
ObstacleLocation[33][1]=19;
ObstacleLocation[34][1]=19;
ObstacleLocation[35][1]=19;
ObstacleLocation[36][1]=19;
ObstacleLocation[37][1]=19;
ObstacleLocation[38][1]=19;
ObstacleLocation[39][1]=19;
ObstacleLocation[40][1]=19;
ObstacleLocation[41][1]=19;
ObstacleLocation[42][1]=19;
ObstacleLocation[43][1]=19;
ObstacleLocation[44][1]=19;
ObstacleLocation[45][1]=19;
ObstacleLocation[46][1]=19;
ObstacleLocation[47][1]=19;
ObstacleLocation[48][1]=19;
ObstacleLocation[49][1]=19;
ObstacleLocation[50][1]=19;
ObstacleLocation[51][1]=19;
ObstacleLocation[52][1]=19;
ObstacleLocation[53][1]=19;
ObstacleLocation[54][1]=19;
ObstacleLocation[55][1]=19;
ObstacleLocation[56][1]=19;
ObstacleLocation[57][1]=19;
ObstacleLocation[58][1]=19;
ObstacleLocation[59][1]=19;
ObstacleLocation[60][1]=19;
ObstacleLocation[61][1]=19;
ObstacleLocation[62][1]=19;
ObstacleLocation[63][1]=19;
ObstacleLocation[64][1]=19;
ObstacleLocation[65][1]=19;
ObstacleLocation[66][1]=19;
ObstacleLocation[67][1]=19;
ObstacleLocation[68][1]=19;
ObstacleLocation[69][1]=19;
ObstacleLocation[70][1]=19;
ObstacleLocation[71][1]=19;
ObstacleLocation[72][1]=19;
ObstacleLocation[73][1]=19;
ObstacleLocation[74][1]=18;
ObstacleLocation[20][2]=5;
ObstacleLocation[20][3]=6;
ObstacleLocation[22][5]=8;
ObstacleLocation[23][2]=5;
ObstacleLocation[23][3]=8;
ObstacleLocation[25][4]=6;
ObstacleLocation[25][5]=8;
ObstacleLocation[28][2]=5;
ObstacleLocation[28][3]=7;
ObstacleLocation[29][2]=15;
ObstacleLocation[30][2]=17;
ObstacleLocation[30][3]=15;
ObstacleLocation[31][2]=20;
ObstacleLocation[31][3]=17;
ObstacleLocation[31][4]=15;
ObstacleLocation[32][4]=17;
ObstacleLocation[32][5]=19;
ObstacleLocation[33][5]=19;
ObstacleLocation[34][5]=19;
ObstacleLocation[35][5]=19;
ObstacleLocation[36][5]=19;
ObstacleLocation[37][5]=19;
ObstacleLocation[38][5]=19;
ObstacleLocation[39][5]=19;
ObstacleLocation[40][5]=19;
ObstacleLocation[41][5]=19;
ObstacleLocation[42][5]=17;

ObstacleLocation[44][5]=16;
ObstacleLocation[45][5]=19;
ObstacleLocation[46][5]=19;
ObstacleLocation[47][5]=19;
ObstacleLocation[48][5]=19;
ObstacleLocation[49][5]=19;
ObstacleLocation[50][5]=19;
ObstacleLocation[51][5]=19;
ObstacleLocation[52][5]=19;
ObstacleLocation[53][5]=19;
ObstacleLocation[54][5]=19;
ObstacleLocation[55][5]=19;
ObstacleLocation[56][5]=19;
ObstacleLocation[57][5]=19;
ObstacleLocation[58][5]=19;
ObstacleLocation[59][5]=19;
ObstacleLocation[60][5]=19;
ObstacleLocation[61][5]=19;
ObstacleLocation[62][5]=19;
ObstacleLocation[63][5]=19;
ObstacleLocation[34][2]=10;
ObstacleLocation[37][3]=10;
ObstacleLocation[38][4]=10;
ObstacleLocation[40][2]=10;
ObstacleLocation[43][2]=10;
ObstacleLocation[43][3]=10;
ObstacleLocation[43][4]=10;
ObstacleLocation[44][4]=20;
ObstacleLocation[47][3]=10;
ObstacleLocation[50][2]=10;
ObstacleLocation[50][4]=10;
ObstacleLocation[52][2]=10;
ObstacleLocation[53][4]=10;
ObstacleLocation[56][2]=10;
ObstacleLocation[56][4]=10;
ObstacleLocation[59][3]=10;
ObstacleLocation[61][2]=10;
ObstacleLocation[65][2]=16;
ObstacleLocation[66][2]=18;
ObstacleLocation[64][3]=16;
ObstacleLocation[65][3]=19;
ObstacleLocation[66][3]=17;
ObstacleLocation[63][4]=16;
ObstacleLocation[64][4]=18;
ObstacleLocation[66][4]=15;
ObstacleLocation[67][4]=17;
ObstacleLocation[65][5]=20;
ObstacleLocation[67][5]=15;
ObstacleLocation[68][5]=17;
ObstacleLocation[70][2]=20;
ObstacleLocation[74][1]=22;


				break;
			case 4:
				clearLevel();
ObstacleLocation[2][1]=23;
ObstacleLocation[9][1]=20;
ObstacleLocation[9][2]=20;
ObstacleLocation[9][3]=20;
ObstacleLocation[9][4]=20;
ObstacleLocation[9][5]=20;
ObstacleLocation[16][3]=20;

ObstacleLocation[12][2]=16;
ObstacleLocation[12][3]=19;
ObstacleLocation[12][4]=15;
ObstacleLocation[13][2]=19;
ObstacleLocation[13][3]=19;
ObstacleLocation[13][4]=19;
ObstacleLocation[14][2]=17;
ObstacleLocation[14][3]=19;
ObstacleLocation[14][4]=18;

ObstacleLocation[18][2]=16;
ObstacleLocation[18][3]=19;
ObstacleLocation[18][4]=15;
ObstacleLocation[19][2]=19;
ObstacleLocation[19][3]=19;
ObstacleLocation[19][4]=19;
ObstacleLocation[20][2]=17;
ObstacleLocation[20][3]=19;
ObstacleLocation[20][4]=18;
ObstacleLocation[22][3]=20;

ObstacleLocation[24][1]=16;
ObstacleLocation[24][2]=19;
ObstacleLocation[24][3]=15;
ObstacleLocation[25][1]=19;
ObstacleLocation[25][2]=19;
ObstacleLocation[25][3]=19;
ObstacleLocation[26][1]=17;
ObstacleLocation[26][2]=19;
ObstacleLocation[26][3]=18;

ObstacleLocation[30][1]=5;
ObstacleLocation[30][2]=6;
ObstacleLocation[30][4]=7;
ObstacleLocation[30][5]=8;

ObstacleLocation[34][1]=20;
ObstacleLocation[34][2]=20;
ObstacleLocation[34][3]=20;
ObstacleLocation[34][4]=20;
ObstacleLocation[34][5]=20;
ObstacleLocation[37][1]=1;
ObstacleLocation[37][2]=2;
ObstacleLocation[37][3]=3;
ObstacleLocation[37][4]=21;
ObstacleLocation[37][5]=4;
ObstacleLocation[40][1]=1;
ObstacleLocation[40][2]=21;
ObstacleLocation[40][3]=2;
ObstacleLocation[40][4]=3;
ObstacleLocation[40][5]=4;
ObstacleLocation[44][1]=1;
ObstacleLocation[44][4]=21;
ObstacleLocation[44][2]=2;
ObstacleLocation[44][3]=3;
ObstacleLocation[44][5]=4;
ObstacleLocation[47][2]=20;

ObstacleLocation[48][1]=16;
ObstacleLocation[48][2]=19;
ObstacleLocation[48][3]=15;
ObstacleLocation[49][1]=19;
ObstacleLocation[49][2]=19;
ObstacleLocation[49][3]=19;
ObstacleLocation[50][1]=17;
ObstacleLocation[50][2]=19;
ObstacleLocation[50][3]=18;

ObstacleLocation[55][2]=16;
ObstacleLocation[55][3]=19;
ObstacleLocation[55][4]=19;
ObstacleLocation[56][2]=19;
ObstacleLocation[56][3]=19;
ObstacleLocation[56][4]=19;
ObstacleLocation[57][2]=17;
ObstacleLocation[57][3]=19;
ObstacleLocation[57][4]=19;
ObstacleLocation[55][5]=15;
ObstacleLocation[56][5]=19;
ObstacleLocation[57][5]=18;
ObstacleLocation[58][3]=20;

ObstacleLocation[60][1]=19;
ObstacleLocation[60][2]=19;
ObstacleLocation[60][3]=19;
ObstacleLocation[60][4]=15;
ObstacleLocation[61][1]=19;
ObstacleLocation[61][2]=19;
ObstacleLocation[61][3]=19;
ObstacleLocation[61][4]=19;
ObstacleLocation[62][1]=19;
ObstacleLocation[62][2]=19;
ObstacleLocation[62][3]=19;
ObstacleLocation[62][4]=18;

ObstacleLocation[63][3]=20;
ObstacleLocation[64][3]=13;
ObstacleLocation[65][4]=13;
ObstacleLocation[68][2]=14;
ObstacleLocation[69][2]=14;
ObstacleLocation[69][3]=14;

ObstacleLocation[72][4]=13;
ObstacleLocation[71][5]=20;
ObstacleLocation[72][5]=13;
ObstacleLocation[73][3]=13;

ObstacleLocation[74][1]=22;

				break;
			case 5:
				clearLevel();
ObstacleLocation[2][1]=23;
ObstacleLocation[5][2]=14;
ObstacleLocation[5][3]=14;
ObstacleLocation[5][4]=14;
ObstacleLocation[7][1]=13;
ObstacleLocation[7][2]=10;
ObstacleLocation[7][5]=13;
ObstacleLocation[8][3]=9;
ObstacleLocation[11][1]=1;
ObstacleLocation[11][2]=21;
ObstacleLocation[11][3]=2;
ObstacleLocation[11][4]=3;
ObstacleLocation[11][5]=4;
ObstacleLocation[13][2]=9;
ObstacleLocation[13][3]=13;
ObstacleLocation[15][3]=21;
ObstacleLocation[16][3]=21;
ObstacleLocation[17][3]=21;
ObstacleLocation[18][3]=21;
ObstacleLocation[19][3]=21;
ObstacleLocation[20][3]=21;
ObstacleLocation[16][1]=1;
ObstacleLocation[16][2]=2;
ObstacleLocation[16][4]=3;
ObstacleLocation[16][5]=4;
ObstacleLocation[18][1]=1;
ObstacleLocation[18][2]=2;
ObstacleLocation[18][4]=3;
ObstacleLocation[18][5]=4;

ObstacleLocation[23][2]=21;
ObstacleLocation[24][2]=21;
ObstacleLocation[25][2]=21;
ObstacleLocation[26][2]=21;
ObstacleLocation[27][2]=21;
ObstacleLocation[23][4]=21;
ObstacleLocation[24][4]=21;
ObstacleLocation[25][4]=21;
ObstacleLocation[26][4]=21;
ObstacleLocation[27][4]=21;
ObstacleLocation[23][3]=12;
ObstacleLocation[24][3]=12;
ObstacleLocation[25][3]=20;
ObstacleLocation[26][3]=12;
ObstacleLocation[27][3]=12;

ObstacleLocation[28][2]=9;
ObstacleLocation[28][4]=9;
ObstacleLocation[30][2]=11;
ObstacleLocation[30][3]=11;
ObstacleLocation[32][1]=11;
ObstacleLocation[32][2]=13;
ObstacleLocation[32][3]=11;
ObstacleLocation[32][5]=13;
ObstacleLocation[33][5]=20;
ObstacleLocation[34][5]=14;
ObstacleLocation[34][4]=14;
ObstacleLocation[35][4]=14;
ObstacleLocation[35][5]=14;
ObstacleLocation[36][3]=14;
ObstacleLocation[36][4]=14;
ObstacleLocation[36][5]=14;

ObstacleLocation[40][2]=21;
ObstacleLocation[41][2]=21;
ObstacleLocation[42][2]=21;
ObstacleLocation[42][3]=21;
ObstacleLocation[43][3]=21;
ObstacleLocation[44][3]=21;
ObstacleLocation[45][3]=21;
ObstacleLocation[45][2]=21;
ObstacleLocation[46][2]=21;
ObstacleLocation[44][1]=1;
ObstacleLocation[44][2]=2;
ObstacleLocation[44][4]=3;
ObstacleLocation[44][5]=4;
ObstacleLocation[40][1]=12;
ObstacleLocation[41][1]=12;
ObstacleLocation[42][1]=12;
ObstacleLocation[43][1]=12;
ObstacleLocation[45][1]=12;
ObstacleLocation[46][1]=12;
ObstacleLocation[43][2]=12;
ObstacleLocation[41][3]=12;
ObstacleLocation[41][4]=12;
ObstacleLocation[42][4]=12;
ObstacleLocation[43][4]=12;
ObstacleLocation[45][4]=12;
ObstacleLocation[46][3]=12;
ObstacleLocation[46][4]=12;
ObstacleLocation[42][5]=12;
ObstacleLocation[43][5]=12;

ObstacleLocation[50][1]=14;
ObstacleLocation[50][5]=14;
ObstacleLocation[51][1]=14;
ObstacleLocation[51][2]=14;
ObstacleLocation[51][4]=14;
ObstacleLocation[51][5]=14;
ObstacleLocation[52][4]=14;
ObstacleLocation[52][5]=14;
ObstacleLocation[52][1]=10;
ObstacleLocation[53][3]=14;
ObstacleLocation[53][4]=14;
ObstacleLocation[53][5]=14;
ObstacleLocation[55][1]=14;
ObstacleLocation[55][2]=14;
ObstacleLocation[55][3]=14;
ObstacleLocation[55][5]=10;

ObstacleLocation[59][1]=19;
ObstacleLocation[59][2]=9;
ObstacleLocation[59][5]=19;
ObstacleLocation[60][1]=20;
ObstacleLocation[60][2]=9;
ObstacleLocation[61][2]=9;
ObstacleLocation[62][2]=10;
ObstacleLocation[63][2]=5;
ObstacleLocation[63][4]=7;
ObstacleLocation[63][5]=8;
ObstacleLocation[64][2]=10;
ObstacleLocation[65][2]=9;
ObstacleLocation[66][2]=5;
ObstacleLocation[66][3]=7;
ObstacleLocation[66][5]=8;
ObstacleLocation[67][2]=10;
ObstacleLocation[68][2]=9;
ObstacleLocation[69][2]=9;
ObstacleLocation[71][2]=10;
ObstacleLocation[72][2]=9;
ObstacleLocation[74][1]=1;
ObstacleLocation[74][2]=2;
ObstacleLocation[74][3]=21;
ObstacleLocation[74][4]=3;
ObstacleLocation[74][5]=4;
ObstacleLocation[75][1]=22;

				break;
			case 6:
				clearLevel();

ObstacleLocation[2][1]=23;

ObstacleLocation[5][1]=1;
ObstacleLocation[5][2]=2;
ObstacleLocation[5][3]=21;
ObstacleLocation[5][4]=3;
ObstacleLocation[5][5]=4;
ObstacleLocation[6][1]=20;
ObstacleLocation[6][5]=20;
ObstacleLocation[7][1]=9;
ObstacleLocation[7][5]=9;
ObstacleLocation[9][2]=7;
ObstacleLocation[9][3]=6;
ObstacleLocation[9][4]=7;
ObstacleLocation[9][5]=8;
ObstacleLocation[10][5]=20;

ObstacleLocation[14][2]=1;
ObstacleLocation[14][3]=2;
ObstacleLocation[14][4]=3;
ObstacleLocation[14][5]=4;
ObstacleLocation[14][1]=21;

ObstacleLocation[16][1]=5;
ObstacleLocation[16][2]=6;
ObstacleLocation[16][4]=7;
ObstacleLocation[16][5]=8;

ObstacleLocation[17][3]=20;

ObstacleLocation[18][1]=1;
ObstacleLocation[18][3]=2;
ObstacleLocation[18][4]=3;
ObstacleLocation[18][5]=4;
ObstacleLocation[18][2]=21;

ObstacleLocation[20][1]=1;
ObstacleLocation[20][2]=2;
ObstacleLocation[20][4]=3;
ObstacleLocation[20][5]=4;
ObstacleLocation[20][3]=21;

ObstacleLocation[24][2]=5;
ObstacleLocation[24][3]=6;
ObstacleLocation[24][4]=7;
ObstacleLocation[24][5]=8;

ObstacleLocation[25][3]=20;

ObstacleLocation[27][1]=1;
ObstacleLocation[27][2]=2;
ObstacleLocation[27][4]=3;
ObstacleLocation[27][5]=4;
ObstacleLocation[27][3]=21;

ObstacleLocation[29][2]=5;
ObstacleLocation[29][3]=6;
ObstacleLocation[29][4]=7;
ObstacleLocation[29][5]=8;

ObstacleLocation[32][1]=5;
ObstacleLocation[32][2]=6;
ObstacleLocation[32][3]=7;
ObstacleLocation[32][4]=8;

ObstacleLocation[34][1]=1;
ObstacleLocation[34][2]=2;
ObstacleLocation[34][3]=3;
ObstacleLocation[34][5]=4;
ObstacleLocation[34][4]=21;

ObstacleLocation[35][3]=20;

ObstacleLocation[36][1]=5;
ObstacleLocation[36][3]=6;
ObstacleLocation[36][4]=7;
ObstacleLocation[36][5]=8;

ObstacleLocation[38][1]=1;
ObstacleLocation[38][2]=2;
ObstacleLocation[38][3]=3;
ObstacleLocation[38][4]=4;
ObstacleLocation[38][5]=21;

ObstacleLocation[40][1]=1;
ObstacleLocation[40][3]=2;
ObstacleLocation[40][4]=3;
ObstacleLocation[40][5]=4;
ObstacleLocation[40][2]=21;

ObstacleLocation[41][3]=20;

ObstacleLocation[42][2]=1;
ObstacleLocation[42][3]=2;
ObstacleLocation[42][4]=3;
ObstacleLocation[42][5]=4;
ObstacleLocation[42][1]=21;

ObstacleLocation[45][1]=1;
ObstacleLocation[45][2]=2;
ObstacleLocation[45][3]=21;
ObstacleLocation[45][4]=3;
ObstacleLocation[45][5]=4;
ObstacleLocation[46][1]=20;
ObstacleLocation[46][5]=20;
ObstacleLocation[47][1]=9;
ObstacleLocation[47][5]=9;
ObstacleLocation[49][2]=7;
ObstacleLocation[49][3]=6;
ObstacleLocation[49][4]=7;
ObstacleLocation[49][5]=8;
ObstacleLocation[50][5]=20;

ObstacleLocation[52][1]=19;
ObstacleLocation[52][5]=19;
ObstacleLocation[53][1]=19;
ObstacleLocation[53][5]=19;
ObstacleLocation[54][1]=19;
ObstacleLocation[54][5]=19;
ObstacleLocation[55][1]=19;
ObstacleLocation[55][5]=19;
ObstacleLocation[56][1]=19;
ObstacleLocation[56][5]=19;
ObstacleLocation[57][1]=19;
ObstacleLocation[57][5]=19;
ObstacleLocation[58][1]=19;
ObstacleLocation[58][5]=19;
ObstacleLocation[59][1]=19;
ObstacleLocation[59][5]=19;
ObstacleLocation[60][1]=19;
ObstacleLocation[60][5]=19;
ObstacleLocation[61][1]=19;
ObstacleLocation[61][5]=19;
ObstacleLocation[62][1]=19;
ObstacleLocation[62][5]=19;

ObstacleLocation[53][4]=16;
ObstacleLocation[54][4]=18;
ObstacleLocation[54][3]=16;
ObstacleLocation[55][3]=18;

ObstacleLocation[56][3]=16;
ObstacleLocation[57][3]=18;
ObstacleLocation[57][2]=16;
ObstacleLocation[58][2]=18;

ObstacleLocation[58][3]=16;
ObstacleLocation[59][3]=18;
ObstacleLocation[59][2]=16;
ObstacleLocation[60][2]=18;

ObstacleLocation[60][4]=16;
ObstacleLocation[61][4]=18;
ObstacleLocation[61][3]=16;
ObstacleLocation[62][3]=18;

ObstacleLocation[65][1]=1;
ObstacleLocation[65][2]=2;
ObstacleLocation[65][5]=21;
ObstacleLocation[65][3]=3;
ObstacleLocation[65][4]=4;
ObstacleLocation[66][1]=20;
ObstacleLocation[66][4]=20;
ObstacleLocation[67][1]=9;
ObstacleLocation[67][4]=9;
ObstacleLocation[69][1]=7;
ObstacleLocation[69][2]=6;
ObstacleLocation[69][3]=7;
ObstacleLocation[69][4]=8;
ObstacleLocation[70][1]=20;

ObstacleLocation[72][1]=22;

				break;
			case 7:
				clearLevel();
ObstacleLocation[2][1] =23;

ObstacleLocation[5][1] =11 ;
ObstacleLocation[7][1] =11 ;
ObstacleLocation[6][1] =11 ;
ObstacleLocation[8][2] =11 ;
ObstacleLocation[9][2] =11 ;
ObstacleLocation[7][2] =11 ;
ObstacleLocation[6][3] =11 ;
ObstacleLocation[9][3] =11 ;
ObstacleLocation[10][3] =11 ;
ObstacleLocation[12][4] =11 ;
ObstacleLocation[5][4] =11 ;
ObstacleLocation[11][4] =11 ;
ObstacleLocation[11][5] =11 ;
ObstacleLocation[10][5] =11 ;
ObstacleLocation[8][5] =11 ;
ObstacleLocation[6][2] =20 ;
ObstacleLocation[9][1] =20 ;

ObstacleLocation[16][1] =9 ;
ObstacleLocation[17][3] =10 ;
ObstacleLocation[17][5] =9 ;

ObstacleLocation[21][1] =1 ;
ObstacleLocation[21][2] =2 ;
ObstacleLocation[21][3] =21 ;
ObstacleLocation[21][4] = 3;
ObstacleLocation[21][5] = 4;

ObstacleLocation[24][1] =21 ;

ObstacleLocation[24][2] = 2;
ObstacleLocation[24][3] = 3;
ObstacleLocation[24][4] = 2;
ObstacleLocation[24][5] = 4;

ObstacleLocation[26][1] =1 ;
ObstacleLocation[26][2] =2 ;
ObstacleLocation[26][3] =3 ;
ObstacleLocation[26][4] =3 ;
ObstacleLocation[26][5] =21 ;

ObstacleLocation[29][1] =1 ;
ObstacleLocation[29][2] =2 ;
ObstacleLocation[29][3] =3 ;
ObstacleLocation[29][4] =2 ;
ObstacleLocation[29][5] =21 ;
                           
ObstacleLocation[31][1] =1 ;
ObstacleLocation[31][2] =21 ;
ObstacleLocation[31][3] = 2;
ObstacleLocation[31][4] = 3;
ObstacleLocation[31][5] = 4;

ObstacleLocation[30][1] = 20;

ObstacleLocation[35][2] =11;
ObstacleLocation[35][4] =11;
ObstacleLocation[36][1] =11;
ObstacleLocation[36][4] =11;
ObstacleLocation[37][1] =20;
ObstacleLocation[37][2] =20;
ObstacleLocation[37][3] =20;
ObstacleLocation[37][4] =20;
ObstacleLocation[37][5] =20;

ObstacleLocation[39][2] =11;
ObstacleLocation[39][4] =11;
ObstacleLocation[41][1] =11;
ObstacleLocation[41][3] =11;
ObstacleLocation[41][5] =11;
ObstacleLocation[44][1] =11;
ObstacleLocation[44][3] =11;
ObstacleLocation[44][4] =11;
ObstacleLocation[44][5] =11;

ObstacleLocation[47][1] =11;
ObstacleLocation[47][2] =11;
ObstacleLocation[47][4] =13;
ObstacleLocation[48][2] =11;
ObstacleLocation[48][5] =11;
ObstacleLocation[49][3] =13;
ObstacleLocation[51][3] =13;
ObstacleLocation[51][4] =11;
ObstacleLocation[51][5] =13;
ObstacleLocation[52][3] =11;
ObstacleLocation[52][4] =13;
ObstacleLocation[53][1] =13;
ObstacleLocation[53][5] =11;
ObstacleLocation[54][2] =13;

ObstacleLocation[55][1] =21 ;
ObstacleLocation[56][1] =13 ;
ObstacleLocation[57][1] =21 ;
ObstacleLocation[58][1] =1 ;
ObstacleLocation[59][1] =21 ;
ObstacleLocation[60][1] =1 ;

ObstacleLocation[56][2] =21 ;
ObstacleLocation[57][2] =12 ;
ObstacleLocation[58][2] =21 ;
ObstacleLocation[59][2] =13 ;
ObstacleLocation[60][2] =21 ;

ObstacleLocation[55][3] =21 ;
ObstacleLocation[56][3] =21 ;
ObstacleLocation[57][3] =21 ;
ObstacleLocation[58][3] =2 ;
ObstacleLocation[59][3] =21 ;
ObstacleLocation[60][3] =3 ;

ObstacleLocation[55][4] =21 ;
ObstacleLocation[56][4] =13 ;
ObstacleLocation[57][4] =21 ;
ObstacleLocation[58][4] =21 ;
ObstacleLocation[59][4] =21 ;
ObstacleLocation[60][4] =21 ;

ObstacleLocation[55][5] =12 ;
ObstacleLocation[56][5] =12 ;
ObstacleLocation[57][5] =21 ;
ObstacleLocation[58][5] =4 ;
ObstacleLocation[59][5] =20 ;
ObstacleLocation[60][5] =4 ;

ObstacleLocation[64][1] = 21;
ObstacleLocation[64][2] = 2;
ObstacleLocation[64][3] = 3;
ObstacleLocation[64][4] = 2;
ObstacleLocation[64][5] = 4;
ObstacleLocation[66][1] =1 ;
ObstacleLocation[66][2] =2 ;
ObstacleLocation[66][3] =3 ;
ObstacleLocation[66][4] =3 ;
ObstacleLocation[66][5] =21 ;
ObstacleLocation[67][1] = 20;

ObstacleLocation[69][1] =1 ;
ObstacleLocation[69][2] =2 ;
ObstacleLocation[69][3] =3 ;
ObstacleLocation[69][4] =2 ;
ObstacleLocation[69][5] =21 ;
                           
ObstacleLocation[71][1] =1 ;
ObstacleLocation[71][2] =21 ;
ObstacleLocation[71][3] = 2;
ObstacleLocation[71][4] = 3;
ObstacleLocation[71][5] = 4;
                 
ObstacleLocation[70][1] = 20;

ObstacleLocation[75][2] =11;
ObstacleLocation[75][4] =11;
ObstacleLocation[76][1] =11;
ObstacleLocation[76][4] =11;
ObstacleLocation[77][1] =20;
ObstacleLocation[77][2] =20;
ObstacleLocation[77][3] =20;
ObstacleLocation[77][4] =20;
ObstacleLocation[77][5] =20;

ObstacleLocation[79][2] =11;
ObstacleLocation[79][4] =11;
ObstacleLocation[81][1] =11;
ObstacleLocation[81][3] =11;
ObstacleLocation[81][5] =11;
ObstacleLocation[84][1] =11;
ObstacleLocation[84][3] =11;
ObstacleLocation[84][4] =11;
ObstacleLocation[84][5] =11;

ObstacleLocation[87][1] =11;
ObstacleLocation[87][2] =11;
ObstacleLocation[87][4] =13;
ObstacleLocation[88][2] =11;
ObstacleLocation[88][5] =11;
ObstacleLocation[89][3] =13;
ObstacleLocation[90][5] = 20;
ObstacleLocation[91][3] =13;
ObstacleLocation[91][4] =11;
ObstacleLocation[91][5] =13;
ObstacleLocation[92][3] =11;
ObstacleLocation[92][4] =13;
ObstacleLocation[93][1] =13;
ObstacleLocation[93][5] =11;
ObstacleLocation[94][2] =13;

ObstacleLocation[99][1] =22;
ObstacleLocation[24][3] = 3;
ObstacleLocation[24][4] = 2;
ObstacleLocation[24][5] = 4;

ObstacleLocation[26][1] =1 ;
ObstacleLocation[26][2] =2 ;
ObstacleLocation[26][3] =3 ;
ObstacleLocation[26][4] =3 ;
ObstacleLocation[26][5] =21 ;

ObstacleLocation[29][1] =1 ;
ObstacleLocation[29][2] =2 ;
ObstacleLocation[29][3] =3 ;
ObstacleLocation[29][4] =2 ;
ObstacleLocation[29][5] =21 ;
                           
ObstacleLocation[31][1] =1 ;
ObstacleLocation[31][2] =21 ;
ObstacleLocation[31][3] = 2;
ObstacleLocation[31][4] = 3;
ObstacleLocation[31][5] = 4;

ObstacleLocation[30][1] = 20;

ObstacleLocation[35][2] =11;
ObstacleLocation[35][4] =11;
ObstacleLocation[36][1] =11;
ObstacleLocation[36][4] =11;
ObstacleLocation[37][1] =20;
ObstacleLocation[37][2] =20;
ObstacleLocation[37][3] =20;
ObstacleLocation[37][4] =20;
ObstacleLocation[37][5] =20;

ObstacleLocation[39][2] =11;
ObstacleLocation[39][4] =11;
ObstacleLocation[41][1] =11;
ObstacleLocation[41][3] =11;
ObstacleLocation[41][5] =11;
ObstacleLocation[44][1] =11;
ObstacleLocation[44][3] =11;
ObstacleLocation[44][4] =11;
ObstacleLocation[44][5] =11;

ObstacleLocation[47][1] =11;
ObstacleLocation[47][2] =11;
ObstacleLocation[47][4] =13;
ObstacleLocation[48][2] =11;
ObstacleLocation[48][5] =11;
ObstacleLocation[49][3] =13;
ObstacleLocation[51][3] =13;
ObstacleLocation[51][4] =11;
ObstacleLocation[51][5] =13;
ObstacleLocation[52][3] =11;
ObstacleLocation[52][4] =13;
ObstacleLocation[53][1] =13;
ObstacleLocation[53][5] =11;
ObstacleLocation[54][2] =13;

				break;
			case 8:
				clearLevel();
ObstacleLocation[2][1]=23;

ObstacleLocation[5][1]=20;
ObstacleLocation[5][2]=10;
ObstacleLocation[5][3]=20;
ObstacleLocation[6][1]=9;
ObstacleLocation[7][1]=5;
ObstacleLocation[7][3]=6;
ObstacleLocation[7][4]=7;
ObstacleLocation[7][5]=8;
ObstacleLocation[8][5]=20;
ObstacleLocation[9][1]=1;
ObstacleLocation[9][2]=2;
ObstacleLocation[9][3]=3;
ObstacleLocation[9][4]=4;
ObstacleLocation[9][5]=21;
ObstacleLocation[10][1]=10;
ObstacleLocation[10][4]=10;
ObstacleLocation[10][2]=20;
ObstacleLocation[11][3]=10;
ObstacleLocation[12][4]=20;
ObstacleLocation[12][1]=9;
ObstacleLocation[12][2]=9;
ObstacleLocation[14][1]=20;
ObstacleLocation[14][5]=10;
ObstacleLocation[14][3]=20;
ObstacleLocation[16][2]=5;
ObstacleLocation[16][3]=6;
ObstacleLocation[16][4]=7;
ObstacleLocation[16][5]=8;
ObstacleLocation[17][2]=20;
ObstacleLocation[18][3]=9;
ObstacleLocation[19][5]=20;
ObstacleLocation[20][2]=10;
ObstacleLocation[20][4]=10;
ObstacleLocation[20][5]=10;
ObstacleLocation[21][1]=20;
ObstacleLocation[22][1]=1;
ObstacleLocation[22][2]=2;
ObstacleLocation[22][4]=3;
ObstacleLocation[22][5]=4;
ObstacleLocation[22][3]=21;
ObstacleLocation[23][3]=20;
ObstacleLocation[24][3]=9;
ObstacleLocation[25][2]=20;
ObstacleLocation[25][3]=10;
ObstacleLocation[28][4]=20;
ObstacleLocation[28][2]=9;
ObstacleLocation[29][3]=10;
ObstacleLocation[29][1]=20;
ObstacleLocation[30][1]=9;
ObstacleLocation[30][2]=9;
ObstacleLocation[30][3]=9;
ObstacleLocation[30][5]=9;
ObstacleLocation[31][4]=20;
ObstacleLocation[33][1]=5;
ObstacleLocation[33][3]=6;
ObstacleLocation[33][4]=7;
ObstacleLocation[33][5]=8;
ObstacleLocation[34][1]=20;
ObstacleLocation[35][5]=20;
ObstacleLocation[37][1]=1;
ObstacleLocation[37][3]=2;
ObstacleLocation[37][4]=3;
ObstacleLocation[37][5]=4;
ObstacleLocation[37][2]=21;
ObstacleLocation[38][1]=9;
ObstacleLocation[38][5]=9;
ObstacleLocation[39][2]=20;
ObstacleLocation[40][1]=10;
ObstacleLocation[43][1]=5;
ObstacleLocation[43][2]=6;
ObstacleLocation[43][4]=7;
ObstacleLocation[43][5]=8;
ObstacleLocation[45][1]=20;
ObstacleLocation[47][1]=1;
ObstacleLocation[47][2]=2;
ObstacleLocation[47][3]=3;
ObstacleLocation[47][5]=4;
ObstacleLocation[47][4]=21;
ObstacleLocation[48][2]=9;
ObstacleLocation[49][4]=9;
ObstacleLocation[49][3]=20;
ObstacleLocation[53][3]=10;
ObstacleLocation[54][4]=20;
ObstacleLocation[56][4]=3;
ObstacleLocation[56][2]=2;
ObstacleLocation[56][3]=3;
ObstacleLocation[56][5]=4;
ObstacleLocation[56][1]=21;
ObstacleLocation[58][1]=5;
ObstacleLocation[58][2]=6;
ObstacleLocation[58][3]=7;
ObstacleLocation[58][5]=8;
ObstacleLocation[59][2]=20;
ObstacleLocation[60][3]=9;
ObstacleLocation[62][1]=5;
ObstacleLocation[62][2]=6;
ObstacleLocation[62][3]=7;
ObstacleLocation[62][4]=8;
ObstacleLocation[64][5]=20;
ObstacleLocation[65][5]=9;
ObstacleLocation[67][2]=5;
ObstacleLocation[67][3]=6;
ObstacleLocation[67][4]=8;
ObstacleLocation[69][1]=20;
ObstacleLocation[71][1]=5;
ObstacleLocation[71][3]=7;
ObstacleLocation[71][4]=8;
ObstacleLocation[73][5]=9;
ObstacleLocation[74][3]=20;
ObstacleLocation[76][1]=1;
ObstacleLocation[76][2]=2;
ObstacleLocation[76][3]=3;
ObstacleLocation[76][4]=4;
ObstacleLocation[76][5]=21;

ObstacleLocation[80][1]=22;
				break;
			case 9:
				clearLevel();
ObstacleLocation[2][1]=23;

ObstacleLocation[20][5]=19;
ObstacleLocation[21][5]=19;
ObstacleLocation[21][4]=16;
ObstacleLocation[22][4]=18;
ObstacleLocation[22][3]=16;
ObstacleLocation[23][3]=18;

ObstacleLocation[24][1]=19;
ObstacleLocation[25][1]=19;
ObstacleLocation[25][2]=15;
ObstacleLocation[26][2]=17;
ObstacleLocation[26][3]=16;
ObstacleLocation[27][3]=17;

ObstacleLocation[30][3]=16;
ObstacleLocation[30][4]=15;
ObstacleLocation[31][3]=17;
ObstacleLocation[31][4]=18;

ObstacleLocation[31][5]=13;
ObstacleLocation[32][1]=13;
ObstacleLocation[32][2]=20;

ObstacleLocation[35][1]=11;
ObstacleLocation[35][2]=11;
ObstacleLocation[35][3]=11;
ObstacleLocation[35][4]=11;

ObstacleLocation[37][1]=11;
ObstacleLocation[37][2]=20;
ObstacleLocation[37][3]=11;
ObstacleLocation[37][4]=11;
ObstacleLocation[37][5]=11;

ObstacleLocation[40][1]=11;
ObstacleLocation[40][2]=11;
ObstacleLocation[40][3]=11;
ObstacleLocation[40][4]=11;
ObstacleLocation[40][5]=20;

ObstacleLocation[42][1]=11;
ObstacleLocation[42][2]=14;
ObstacleLocation[42][3]=11;
ObstacleLocation[42][4]=11;

ObstacleLocation[46][2]=19;
ObstacleLocation[46][3]=19;
ObstacleLocation[46][4]=19;
ObstacleLocation[46][5]=19;

ObstacleLocation[50][1]=20;
ObstacleLocation[50][2]=19;
ObstacleLocation[50][3]=19;
ObstacleLocation[50][4]=19;
ObstacleLocation[50][5]=19;

ObstacleLocation[53][1]=19;
ObstacleLocation[53][3]=19;
ObstacleLocation[53][4]=19;

ObstacleLocation[55][2]=19;
ObstacleLocation[55][3]=19;
ObstacleLocation[55][4]=19;

ObstacleLocation[57][1]=20;
ObstacleLocation[57][2]=20;
ObstacleLocation[57][3]=20;
ObstacleLocation[57][4]=20;

ObstacleLocation[61][1]=5;
ObstacleLocation[61][2]=6;
ObstacleLocation[61][3]=7;
ObstacleLocation[61][4]=8;
ObstacleLocation[61][5]=20;

ObstacleLocation[65][5]=19;
ObstacleLocation[66][5]=19;
ObstacleLocation[66][4]=16;
ObstacleLocation[67][4]=18;
ObstacleLocation[67][3]=16;
ObstacleLocation[68][3]=18;

ObstacleLocation[68][1]=19;
ObstacleLocation[69][1]=19;
ObstacleLocation[69][2]=15;
ObstacleLocation[70][2]=17;
ObstacleLocation[70][3]=16;
ObstacleLocation[71][3]=17;

ObstacleLocation[74][1]=22;

				break;
			case 10:
				clearLevel();

ObstacleLocation[2][1] =23;

ObstacleLocation[4][1] = 5;
ObstacleLocation[4][2] = 6;
ObstacleLocation[4][3] = 7;
ObstacleLocation[4][4] = 8;
ObstacleLocation[7][2] = 5;
ObstacleLocation[7][3] = 6;
ObstacleLocation[7][4] = 7;
ObstacleLocation[7][5] = 8;
ObstacleLocation[10][1] = 5;
ObstacleLocation[10][2] = 6;
ObstacleLocation[10][3] = 7;
ObstacleLocation[10][4] = 8;
ObstacleLocation[12][1] = 5;
ObstacleLocation[12][2] = 6;
ObstacleLocation[12][3] = 7;
ObstacleLocation[12][5] = 8;
ObstacleLocation[14][1] =21;
ObstacleLocation[14][2] =2;
ObstacleLocation[14][3] =3;
ObstacleLocation[14][4] =21;
ObstacleLocation[14][5] =4;
ObstacleLocation[16][1] = 6;
ObstacleLocation[16][2] = 7;
ObstacleLocation[16][3] = 8;
ObstacleLocation[16][4] = 5;
ObstacleLocation[17][1] =9 ;
ObstacleLocation[17][3] =10 ;
ObstacleLocation[18][5] =9 ;
ObstacleLocation[19][2] = 9;
ObstacleLocation[19][4] = 10;
ObstacleLocation[21][1] = 9;
ObstacleLocation[21][3] = 9;
ObstacleLocation[21][5] = 10;
ObstacleLocation[23][1] =10 ;
ObstacleLocation[23][3] =10 ;
ObstacleLocation[23][5] =10 ;
ObstacleLocation[24][2] =10 ;
ObstacleLocation[25][3] = 10;
ObstacleLocation[27][1] = 20;
ObstacleLocation[27][2] = 20;
ObstacleLocation[27][3] = 20;
ObstacleLocation[27][4] = 20;
ObstacleLocation[27][5] = 20;

ObstacleLocation[29][1] =1;
ObstacleLocation[29][2] =2;
ObstacleLocation[29][3] =21;
ObstacleLocation[29][4] =3;
ObstacleLocation[29][5] =4;

ObstacleLocation[31][1] =9;
ObstacleLocation[31][2] =10;
ObstacleLocation[31][3] =10;
ObstacleLocation[33][4] =9;
ObstacleLocation[33][5] =10;

ObstacleLocation[35][1] = 20;
ObstacleLocation[35][2] = 20;
ObstacleLocation[35][3] = 20;
ObstacleLocation[35][4] = 20;
ObstacleLocation[35][5] = 20;

ObstacleLocation[36][1] =5;
ObstacleLocation[36][2] =6;
ObstacleLocation[36][3] =7;
ObstacleLocation[38][4] =9;
ObstacleLocation[37][5] =11;

ObstacleLocation[41][1] = 20;
ObstacleLocation[41][2] = 20;
ObstacleLocation[41][3] = 20;
ObstacleLocation[41][4] = 20;
ObstacleLocation[41][5] = 20;

ObstacleLocation[45][1] =9;
ObstacleLocation[45][2] =9;
ObstacleLocation[45][4] =10;
ObstacleLocation[45][5] =9;

ObstacleLocation[46][1] =10;
ObstacleLocation[46][4] =9;
ObstacleLocation[46][5] =9;

ObstacleLocation[47][5] =10;

ObstacleLocation[48][2] =10;
ObstacleLocation[48][3] =10;
ObstacleLocation[48][5] =10;

ObstacleLocation[49][2] =10;
ObstacleLocation[49][3] =9;

ObstacleLocation[50][4] =9;
ObstacleLocation[50][1] =20;
ObstacleLocation[50][3] =20;
ObstacleLocation[50][5] =20;

ObstacleLocation[51][1] =9;
ObstacleLocation[51][2] =10;
ObstacleLocation[51][4] =10;

ObstacleLocation[52][1] =10;
ObstacleLocation[52][4] =9;

ObstacleLocation[53][1] =20;

ObstacleLocation[54][2] =9;
ObstacleLocation[54][3] =9;
ObstacleLocation[54][4] =9;
ObstacleLocation[54][5] =9;

ObstacleLocation[55][3] =9;
ObstacleLocation[55][4] =10;
ObstacleLocation[55][5] =10;

ObstacleLocation[56][1] =9;
ObstacleLocation[56][3] =10;
ObstacleLocation[56][4] =10;
ObstacleLocation[56][5] =10;

ObstacleLocation[57][1] =10;
ObstacleLocation[57][5] =9;

ObstacleLocation[58][1] =10;
ObstacleLocation[58][2] =9;
ObstacleLocation[58][3] =9;
ObstacleLocation[58][5] =9;

ObstacleLocation[59][3] =20;

ObstacleLocation[60][1] =1;
ObstacleLocation[60][2] =2;
ObstacleLocation[60][3] =21;
ObstacleLocation[60][4] =3;
ObstacleLocation[60][5] =4;
ObstacleLocation[62][2] =11;
ObstacleLocation[62][3] =11;
ObstacleLocation[62][4] =11;
ObstacleLocation[64][1] =10;
ObstacleLocation[64][5] =10;
ObstacleLocation[65][1] =5;
ObstacleLocation[65][3] =6;
ObstacleLocation[65][4] =7;
ObstacleLocation[65][5] =8;

ObstacleLocation[70][1] =9;
ObstacleLocation[71][1] =9;
ObstacleLocation[72][1] =9;
ObstacleLocation[73][1] =9;
ObstacleLocation[74][1] =9;
ObstacleLocation[75][1] =9;
ObstacleLocation[76][1] =9;
ObstacleLocation[77][1] =9;
ObstacleLocation[78][1] =9;
ObstacleLocation[79][1] =9;
ObstacleLocation[80][1] =10;
ObstacleLocation[81][1] =10;
ObstacleLocation[82][1] =10;
ObstacleLocation[83][1] =10;
ObstacleLocation[84][1] =10;
ObstacleLocation[85][1] =10;
ObstacleLocation[86][1] =10;
ObstacleLocation[87][1] =10;
ObstacleLocation[88][1] =10;
ObstacleLocation[89][1] =10;
ObstacleLocation[90][1] =9;
ObstacleLocation[91][1] =9;
ObstacleLocation[92][1] =9;
ObstacleLocation[93][1] =9;
ObstacleLocation[94][1] =9;
ObstacleLocation[96][1] =20;
ObstacleLocation[101][1] =9;
ObstacleLocation[102][1] =9;
ObstacleLocation[103][1] =9;
ObstacleLocation[104][1] =9;
ObstacleLocation[111][1] =9;
ObstacleLocation[112][1] =9;
ObstacleLocation[113][1] =9;
ObstacleLocation[114][1] =9;
ObstacleLocation[115][1] =9;
ObstacleLocation[116][1] =9;
ObstacleLocation[117][1] =9;
ObstacleLocation[118][1] =10;
ObstacleLocation[70][2] =10;
ObstacleLocation[71][2] =10;
ObstacleLocation[72][2] =10;
ObstacleLocation[73][2] =10;
ObstacleLocation[74][2] =10;
ObstacleLocation[75][2] =10;
ObstacleLocation[76][2] =10;
ObstacleLocation[77][2] =10;
ObstacleLocation[78][2] =10;
ObstacleLocation[79][2] =9;
ObstacleLocation[80][2] =9;
ObstacleLocation[85][2] =9;
ObstacleLocation[86][2] =9;
ObstacleLocation[87][2] =9;
ObstacleLocation[88][2] =9;
ObstacleLocation[89][2] =9;
ObstacleLocation[90][2] =9;
ObstacleLocation[91][2] =9;
ObstacleLocation[92][2] =9;
ObstacleLocation[93][2] =9;
ObstacleLocation[96][2] =9;
ObstacleLocation[97][2] =9;
ObstacleLocation[98][2] =9;
ObstacleLocation[99][2] =9;
ObstacleLocation[101][2] =9;
ObstacleLocation[102][2] =9;
ObstacleLocation[103][2] =9;
ObstacleLocation[107][2] =10;
ObstacleLocation[108][2] =10;
ObstacleLocation[109][2] =10;
ObstacleLocation[110][2] =10;
ObstacleLocation[70][3] =9;
ObstacleLocation[76][3] =9;
ObstacleLocation[77][3] =9;
ObstacleLocation[78][3] =9;
ObstacleLocation[79][3] =9;
ObstacleLocation[80][3] =9;
ObstacleLocation[82][3] =20;
ObstacleLocation[85][3] =9;
ObstacleLocation[90][3] =9;
ObstacleLocation[95][3] =9;
ObstacleLocation[96][3] =10;
ObstacleLocation[97][3] =10;
ObstacleLocation[98][3] =10;
ObstacleLocation[99][3] =10;
ObstacleLocation[105][3] =10;
ObstacleLocation[106][3] =10;
ObstacleLocation[107][3] =10;
ObstacleLocation[108][3] =10;
ObstacleLocation[109][3] =10;
ObstacleLocation[110][3] =10;
ObstacleLocation[111][3] =9;
ObstacleLocation[112][3] =9;
ObstacleLocation[113][3] =9;
ObstacleLocation[114][3] =9;
ObstacleLocation[115][3] =9;
ObstacleLocation[116][3] =9;
ObstacleLocation[117][3] =9;
ObstacleLocation[118][3] =9;
ObstacleLocation[73][4] =10;
ObstacleLocation[74][4] =10;
ObstacleLocation[76][4] =10;
ObstacleLocation[77][4] =10;
ObstacleLocation[83][4] =10;
ObstacleLocation[87][4] =10;
ObstacleLocation[88][4] =9;
ObstacleLocation[90][4] =9;
ObstacleLocation[92][4] =9;
ObstacleLocation[93][4] =9;
ObstacleLocation[94][4] =9;
ObstacleLocation[95][4] =9;
ObstacleLocation[96][4] =9;
ObstacleLocation[97][4] =9;
ObstacleLocation[98][4] =9;
ObstacleLocation[99][4] =9;
ObstacleLocation[100][4] =9;
ObstacleLocation[101][4] =9;
ObstacleLocation[102][4] =9;
ObstacleLocation[103][4] =9;
ObstacleLocation[107][4] =9;
ObstacleLocation[108][4] =9;
ObstacleLocation[109][4] =9;
ObstacleLocation[110][4] =9;
ObstacleLocation[114][4] =10;
ObstacleLocation[118][4] =10;
ObstacleLocation[70][5] =9;
ObstacleLocation[71][5] =9;
ObstacleLocation[72][5] =9;
ObstacleLocation[73][5] =9;
ObstacleLocation[80][5] =9;
ObstacleLocation[81][5] =9;
ObstacleLocation[82][5] =9;
ObstacleLocation[83][5] =9;
ObstacleLocation[85][5] =9;
ObstacleLocation[86][5] =9;
ObstacleLocation[87][5] =10;
ObstacleLocation[88][5] =10;
ObstacleLocation[92][5] =10;
ObstacleLocation[93][5] =10;
ObstacleLocation[94][5] =10;
ObstacleLocation[95][5] =10;
ObstacleLocation[96][5] =10;
ObstacleLocation[97][5] =10;
ObstacleLocation[98][5] =10;
ObstacleLocation[99][5] =10;
ObstacleLocation[100][5] =9;
ObstacleLocation[101][5] =9;
ObstacleLocation[102][5] =9;
ObstacleLocation[103][5] =9;
ObstacleLocation[104][5] =9;
ObstacleLocation[105][5] =9;
ObstacleLocation[112][5] =9;
ObstacleLocation[116][5] =9;

ObstacleLocation[120][1] =22;
				break;
		}
	}

	public void destroy(){
		g2.dispose();
		g3.dispose();
	}

	public void clearLevel(){
		int a = 1, b = 1;

		do{
			do{
				ObstacleLocation[a][b] = 0;
				b++;
			}while(b < 6);
			
			b = 1;
			a++;
		}while(a < 201);
	}
	
	public void showLevelComplete(){
//		if(current_level < 10){
			g3.setColor(Color.black);
			g3.fillRect(0, 0, 280, 300);
			g3.drawImage(levelComplete, 0, 0, this);
			tempimg = offimg2;
			x = y = 0;
			repaint();
//		}
/*		else{
			g3.drawImage(youWin, 0, 0, this);
			tempimg = offimg2;
			x = y = 0;
			repaint();
		}
*/
	}

	public void redrawDisplay(){
		g3.setColor(Color.yellow);
		g3.setFont(dispFont);			

		g3.drawString("Level: "+current_level+"    Damage: "+current_damage+"%   "+current_sector, 20, 330 - panX);
		g3.drawString("Time: "+time[current_level], 80, 350 - panX);
	}

	public void showLoading(){
		tempimg = loadingScreen;
		x = y = 0;
		repaint();
	}

	public void clearOffscn(){
		g2.setColor(Color.black);
		g3.setColor(Color.white);

		g2.fillRect(0, 0, 280, 360);
		g3.fillRect(0, 0, 280, 360);
	}

	public int toggleOffScreenImage(){
		current_screen = 1 - current_screen;
		return current_screen;
	}

	public void pause(int time){
		try{ Thread.sleep(time); }
			catch(InterruptedException e) {}
	}

	public void update(Graphics g){
		g.clipRect(0, 0, 280, 300);
		paint(g);
	}

	public void paint(Graphics g){
		if(tempimg != null)
			g.drawImage(tempimg, 0, y, this);
	
/*		if(drawPercent == null){
			g.setColor(Color.white);
			g.fillRect(0, 0, 300, 280);
			g.setColor(Color.black);

			Font f = new Font("TimesRoman", Font.PLAIN, 12);
			FontMetrics fm = getFontMetrics(f);
			g.setFont(f);

			String s = "Loading Images...";
			int xstart = (size().width - fm.stringWidth(s)) / 2;
			int ystart = size().height / 2;
	
			g.drawString(s, xstart, ystart);
		}
*/	
	}

}
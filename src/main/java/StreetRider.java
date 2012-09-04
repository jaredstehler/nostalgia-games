

import java.awt.image.*;
import java.awt.*;
import java.net.*;

public class StreetRider extends java.applet.Applet implements Runnable{

	Thread runner;

	final int width = 300, height = 280;
	final int sector_size = 60;

	final int num_levels = 10;

	final int num_bldgs = 43;
	final int num_obstacles = 44;

	final int bikeX = 120;

	final int lane1Y = 75;
	final int lane2Y = 115;	
	final int lane3Y = 155;	
	final int lane4Y = 195;

	Font f = new Font("TimesRoman", Font.PLAIN, 14);
	
	Image Title[] = new Image[4];
	Image Street[] = new Image[5];
	Image Sidewalk[] = new Image[2];
	Image Buildings[] = new Image[num_bldgs];
	Image Obstacles[] = new Image[num_obstacles];
	Image Ramps[] = new Image[4];
	Image CurrentRamps[] = new Image[2];
	Image Bike[] = new Image[11];

	Image lose[] = new Image[7];  Image win[] = new Image[2];
	
	Image CurrentSidewalk[] = new Image[2];
	Image CurrentStreet[] = new Image[2];

	Image tempimg, CurrentBikeImage, drawPercent, START, loadingScreen, levelComplete, dirt, displayimage;

	Image count[] = new Image[4];
	
	Image count_go, shadow, highScoresBG, youWin, game_over, win_scn;

	Image offimg, offimg2;
	Graphics g2, g3;

	int ObstacleLocation[][] = new int[207][6];
	
	int RampLocation[][] = new int[207][6];
	
	int lvl_bldgs[] = new int[207];

	int current_level, current_sector, current_lane, current_score, current_screen, current_speed, current_damage, hopHeight, hopLength;

	int percent_done, lvl_num_sectors, lvl_pct_done, panIncrement = 0, hopSize, speed;

	boolean DONE, LEVEL_SUCCESS, GAME_DONE, BUNNYHOP, NO_LANE_CHANGE, CRASHED;

	int x = 1, y = 1, panX, bikex, bikey, shadowX, shadowY;

	String emailAddress;
	
	private MediaTracker tracker;


	public void init(){
		offimg = createImage(360, 280);
		offimg2 = createImage(360, 280);

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

		getAppletContext().showStatus("Street Rider v1.5");

		g3.drawString("Loading Images, Please Wait...", 70, 140);
		g3.setColor(Color.black);
		g3.drawRect(85, 165, 129, 8);

		tempimg = offimg2;
		repaint();

//	Load all necessary images

		Title[0] = getImage(getCodeBase(), "streetrider/title_1.jpg");

		loadingScreen = getImage(getCodeBase(), "streetrider/loading_screen.jpg");
		levelComplete = getImage(getCodeBase(), "streetrider/level_complete.jpg");
		youWin = getImage(getCodeBase(), "streetrider/you_win.jpg");
		win_scn = getImage(getCodeBase(), "streetrider/win_screen_1.jpg");
		game_over = getImage(getCodeBase(), "streetrider/game_over.jpg");
		displayimage = getImage(getCodeBase(), "streetrider/display_scn.jpg");

		count[1] = getImage(getCodeBase(), "streetrider/1.gif");
		count[2] = getImage(getCodeBase(), "streetrider/2.gif");
		count[3] = getImage(getCodeBase(), "streetrider/3.gif");
		count_go = getImage(getCodeBase(), "streetrider/go.gif");

		shadow = getImage(getCodeBase(), "streetrider/shadow.gif");
		dirt = getImage(getCodeBase(), "streetrider/dirt.jpg");

		highScoresBG = getImage(getCodeBase(), "streetrider/clouds.jpg");

		Street[0] = getImage(getCodeBase(), "streetrider/street_1.jpg");
		Street[1] = getImage(getCodeBase(), "streetrider/street_2.jpg");
		Sidewalk[0] = getImage(getCodeBase(), "streetrider/sidewalk_1.jpg");
		Sidewalk[1] = getImage(getCodeBase(), "streetrider/sidewalk_2.jpg");

		Street[2] = getImage(getCodeBase(), "streetrider/roof_top.jpg");
		Street[3] = getImage(getCodeBase(), "streetrider/roof_bottom.jpg");
		Street[4] = getImage(getCodeBase(), "streetrider/roof_middle.jpg");

		Bike[0] = getImage(getCodeBase(), "streetrider/bike_1.gif");
		Bike[1] = getImage(getCodeBase(), "streetrider/bike_2.gif");
		Bike[2] = getImage(getCodeBase(), "streetrider/bike_3.gif");
		Bike[3] = getImage(getCodeBase(), "streetrider/bike_4.gif");
		Bike[4] = getImage(getCodeBase(), "streetrider/bike_5.gif");
		Bike[5] = getImage(getCodeBase(), "streetrider/bike_6.gif");
		Bike[6] = getImage(getCodeBase(), "streetrider/bike_7.gif");
		Bike[7] = getImage(getCodeBase(), "streetrider/bike_8.gif");
		Bike[8] = getImage(getCodeBase(), "streetrider/bike_9.gif");
		Bike[9] = getImage(getCodeBase(), "streetrider/bike_10.gif");
		Bike[10] = getImage(getCodeBase(), "streetrider/bike_11.gif");
		
		lose[0] = getImage(getCodeBase(), "streetrider/crushedbike.gif");
		lose[1] = getImage(getCodeBase(), "streetrider/lose_1.gif");
		lose[2] = getImage(getCodeBase(), "streetrider/lose_2.gif");
		lose[3] = getImage(getCodeBase(), "streetrider/lose_3.gif");
		lose[4] = getImage(getCodeBase(), "streetrider/lose_4.gif");
		lose[5] = getImage(getCodeBase(), "streetrider/lose_5.gif");
		lose[6] = getImage(getCodeBase(), "streetrider/lose_6.gif");

		win[0] = getImage(getCodeBase(), "streetrider/win_1.gif");
		win[1] = getImage(getCodeBase(), "streetrider/win_2.gif");
		
	
		Buildings[0] = getImage(getCodeBase(), "streetrider/city_1.jpg");	
		Buildings[1] = getImage(getCodeBase(), "streetrider/city_2.jpg");	
		Buildings[2] = getImage(getCodeBase(), "streetrider/city_3.jpg");	
		Buildings[3] = getImage(getCodeBase(), "streetrider/city_4.jpg");	
		Buildings[4] = getImage(getCodeBase(), "streetrider/city_5.jpg");	
		Buildings[5] = getImage(getCodeBase(), "streetrider/city_6.jpg");	
		Buildings[6] = getImage(getCodeBase(), "streetrider/city_7.jpg");	
		Buildings[7] = getImage(getCodeBase(), "streetrider/city_8.jpg");	
		Buildings[8] = getImage(getCodeBase(), "streetrider/city_9.jpg");	
		Buildings[9] = getImage(getCodeBase(), "streetrider/city_10.jpg");	
	
		Buildings[10] = getImage(getCodeBase(), "streetrider/school_1.jpg");	
		Buildings[11] = getImage(getCodeBase(), "streetrider/school_2.jpg");	
		Buildings[12] = getImage(getCodeBase(), "streetrider/school_3.jpg");	
		Buildings[13] = getImage(getCodeBase(), "streetrider/school_4.jpg");	
		Buildings[14] = getImage(getCodeBase(), "streetrider/school_5.jpg");	
		Buildings[15] = getImage(getCodeBase(), "streetrider/school_6.jpg");	
		Buildings[16] = getImage(getCodeBase(), "streetrider/school_7.jpg");	
		Buildings[17] = getImage(getCodeBase(), "streetrider/school_8.jpg");	
		Buildings[18] = getImage(getCodeBase(), "streetrider/school_9.jpg");	
		Buildings[19] = getImage(getCodeBase(), "streetrider/school_10.jpg");	
		
		Buildings[20] = getImage(getCodeBase(), "streetrider/park_1.jpg");	
		Buildings[21] = getImage(getCodeBase(), "streetrider/park_2.jpg");	
		Buildings[22] = getImage(getCodeBase(), "streetrider/park_3.jpg");	
		Buildings[23] = getImage(getCodeBase(), "streetrider/park_4.jpg");	
		Buildings[24] = getImage(getCodeBase(), "streetrider/park_5.jpg");	
		Buildings[25] = getImage(getCodeBase(), "streetrider/park_6.jpg");	
		Buildings[26] = getImage(getCodeBase(), "streetrider/park_7.jpg");	
		Buildings[27] = getImage(getCodeBase(), "streetrider/park_8.jpg");	
		Buildings[28] = getImage(getCodeBase(), "streetrider/park_9.jpg");	
		Buildings[29] = getImage(getCodeBase(), "streetrider/park_10.jpg");	

		Buildings[30] = getImage(getCodeBase(), "streetrider/forest_1.jpg");

		Buildings[31] = getImage(getCodeBase(), "streetrider/powerlines1.jpg");
		Buildings[32] = getImage(getCodeBase(), "streetrider/powerlines2.jpg");
		Buildings[33] = getImage(getCodeBase(), "streetrider/powerlines3.jpg");
		Buildings[34] = getImage(getCodeBase(), "streetrider/powerlines4.jpg");

		Buildings[35] = getImage(getCodeBase(), "streetrider/Clouds_1.jpg");
		Buildings[36] = getImage(getCodeBase(), "streetrider/Clouds_2.jpg");
		Buildings[37] = getImage(getCodeBase(), "streetrider/Clouds_3.jpg");
		Buildings[38] = getImage(getCodeBase(), "streetrider/Clouds_4.jpg");
		Buildings[39] = getImage(getCodeBase(), "streetrider/Clouds_5.jpg");
		Buildings[40] = getImage(getCodeBase(), "streetrider/Clouds_6.jpg");
		Buildings[41] = getImage(getCodeBase(), "streetrider/Clouds_7.jpg");
		Buildings[42] = getImage(getCodeBase(), "streetrider/Clouds_8.jpg");

		Ramps[0] = getImage(getCodeBase(), "streetrider/jump_small.gif");
		Ramps[1] = getImage(getCodeBase(), "streetrider/jump_big.gif");
		Ramps[2] = getImage(getCodeBase(), "streetrider/dirt_small.gif");
		Ramps[3] = getImage(getCodeBase(), "streetrider/dirt_big.gif");

		Obstacles[0] = getImage(getCodeBase(), "streetrider/dirt_big_catch.gif");
		Obstacles[1] = getImage(getCodeBase(), "streetrider/trash_1.gif");
		Obstacles[2] = getImage(getCodeBase(), "streetrider/rock_1.gif");
		Obstacles[3] = getImage(getCodeBase(), "streetrider/open_manhole.gif");
		Obstacles[4] = getImage(getCodeBase(), "streetrider/log_1.gif");
		Obstacles[5] = getImage(getCodeBase(), "streetrider/log_2.gif");
		Obstacles[6] = getImage(getCodeBase(), "streetrider/log_3.gif");
		Obstacles[7] = getImage(getCodeBase(), "streetrider/log_4.gif");
		Obstacles[8] = getImage(getCodeBase(), "streetrider/stick_1.gif");
		Obstacles[9] = getImage(getCodeBase(), "streetrider/stick_2.gif");
		Obstacles[10] = getImage(getCodeBase(), "streetrider/bat.gif");
		Obstacles[11] = getImage(getCodeBase(), "streetrider/bucket.gif");
		Obstacles[12] = getImage(getCodeBase(), "streetrider/box.gif");
		Obstacles[13] = getImage(getCodeBase(), "streetrider/cinder_block.gif");
		Obstacles[14] = getImage(getCodeBase(), "streetrider/cone.gif");
		Obstacles[15] = getImage(getCodeBase(), "streetrider/glass.gif");
		Obstacles[16] = getImage(getCodeBase(), "streetrider/paper.gif");
		Obstacles[17] = getImage(getCodeBase(), "streetrider/plastic.gif");
		Obstacles[18] = getImage(getCodeBase(), "streetrider/pot_hole.gif");
		Obstacles[19] = getImage(getCodeBase(), "streetrider/object_20.gif");
		Obstacles[20] = getImage(getCodeBase(), "streetrider/shrubbery.gif");
		Obstacles[21] = getImage(getCodeBase(), "streetrider/sign.gif");
		Obstacles[22] = getImage(getCodeBase(), "streetrider/road_construction.gif");
		Obstacles[23] = getImage(getCodeBase(), "streetrider/cat.gif");
		Obstacles[24] = getImage(getCodeBase(), "streetrider/wakeupbumps.jpg");
		Obstacles[25] = getImage(getCodeBase(), "streetrider/badramp.gif");

		Obstacles[26] = getImage(getCodeBase(), "streetrider/grate.gif");
		Obstacles[27] = getImage(getCodeBase(), "streetrider/indent1.jpg");
		Obstacles[28] = getImage(getCodeBase(), "streetrider/indent2.jpg");
		Obstacles[29] = getImage(getCodeBase(), "streetrider/pipes.gif");
		Obstacles[30] = getImage(getCodeBase(), "streetrider/smokestack.gif");
		Obstacles[31] = getImage(getCodeBase(), "streetrider/vent.gif");
		Obstacles[32] = getImage(getCodeBase(), "streetrider/airvent.gif");
		Obstacles[33] = getImage(getCodeBase(), "streetrider/spinnything.gif");
		Obstacles[34] = getImage(getCodeBase(), "streetrider/stairs.gif");
		Obstacles[35] = getImage(getCodeBase(), "streetrider/dish.gif");

		Obstacles[36] = getImage(getCodeBase(), "streetrider/split_roof_1.jpg");
		Obstacles[37] = getImage(getCodeBase(), "streetrider/split_roof_4.jpg");
		Obstacles[38] = getImage(getCodeBase(), "streetrider/split_roof_5.jpg");
		Obstacles[39] = getImage(getCodeBase(), "streetrider/split_roof_8.jpg");

		Obstacles[40] = getImage(getCodeBase(), "streetrider/split_roof_2.jpg");
		Obstacles[41] = getImage(getCodeBase(), "streetrider/split_roof_3.jpg");
		Obstacles[42] = getImage(getCodeBase(), "streetrider/split_roof_6.jpg");
		Obstacles[43] = getImage(getCodeBase(), "streetrider/split_roof_7.jpg");


		tracker = new MediaTracker(this);		
		

		tracker.addImage(Title[0], 0);

		tracker.addImage(displayimage, 1);
		tracker.addImage(loadingScreen, 2);
		tracker.addImage(levelComplete, 3);
		tracker.addImage(youWin, 4);
		tracker.addImage(win_scn, 5);
		tracker.addImage(game_over, 6);
		tracker.addImage(count[1], 7);
		tracker.addImage(count[2], 8);
		tracker.addImage(count[3], 9);
		tracker.addImage(count_go, 10);

		tracker.addImage(Street[0], 11);
		tracker.addImage(Street[1], 12);
		tracker.addImage(Street[2], 13);
		tracker.addImage(Street[3], 14);
		tracker.addImage(Street[4], 15);
		tracker.addImage(Sidewalk[0], 16);
		tracker.addImage(Sidewalk[1], 17);

		tracker.addImage(dirt, 18);
		tracker.addImage(shadow, 19);

		tracker.addImage(highScoresBG, 20);

		tracker.addImage(Ramps[0], 20);
		tracker.addImage(Ramps[1], 21);
		tracker.addImage(Ramps[2], 22);
		tracker.addImage(Ramps[3], 23);

		tracker.addImage(Buildings[0], 24);
		tracker.addImage(Buildings[1], 25);
		tracker.addImage(Buildings[2], 26);
		tracker.addImage(Buildings[3], 27);
		tracker.addImage(Buildings[4], 28);
		tracker.addImage(Buildings[5], 29);
		tracker.addImage(Buildings[6], 30);
		tracker.addImage(Buildings[7], 31);
		tracker.addImage(Buildings[8], 32);
		tracker.addImage(Buildings[9], 33);

		tracker.addImage(Buildings[10], 34);
		tracker.addImage(Buildings[11], 35);
		tracker.addImage(Buildings[12], 36);
		tracker.addImage(Buildings[13], 37);
		tracker.addImage(Buildings[14], 38);
		tracker.addImage(Buildings[15], 39);
		tracker.addImage(Buildings[16], 40);
		tracker.addImage(Buildings[17], 41);
		tracker.addImage(Buildings[18], 42);
		tracker.addImage(Buildings[19], 43);

		tracker.addImage(Buildings[20], 44);
		tracker.addImage(Buildings[21], 45);
		tracker.addImage(Buildings[22], 46);
		tracker.addImage(Buildings[23], 47);
		tracker.addImage(Buildings[24], 48);
		tracker.addImage(Buildings[25], 49);
		tracker.addImage(Buildings[26], 50);
		tracker.addImage(Buildings[27], 51);
		tracker.addImage(Buildings[28], 52);
		tracker.addImage(Buildings[29], 53);
		tracker.addImage(Buildings[30], 54);
		tracker.addImage(Buildings[31], 55);
		tracker.addImage(Buildings[32], 56);
		tracker.addImage(Buildings[33], 57);
		tracker.addImage(Buildings[34], 58);
		tracker.addImage(Buildings[35], 59);
		tracker.addImage(Buildings[36], 60);
		tracker.addImage(Buildings[37], 61);
		tracker.addImage(Buildings[38], 62);
		tracker.addImage(Buildings[39], 63);
		tracker.addImage(Buildings[40], 64);
		tracker.addImage(Buildings[41], 65);
		tracker.addImage(Buildings[42], 66);

		tracker.addImage(Obstacles[0], 67);
		tracker.addImage(Obstacles[1], 68);
		tracker.addImage(Obstacles[2], 69);
		tracker.addImage(Obstacles[3], 70);
		tracker.addImage(Obstacles[4], 71);
		tracker.addImage(Obstacles[5], 72);
		tracker.addImage(Obstacles[6], 73);
		tracker.addImage(Obstacles[7], 74);
		tracker.addImage(Obstacles[8], 75);
		tracker.addImage(Obstacles[9], 76);
		tracker.addImage(Obstacles[10],77);
		tracker.addImage(Obstacles[11],78);
		tracker.addImage(Obstacles[12],79);
		tracker.addImage(Obstacles[13],80);
		tracker.addImage(Obstacles[14], 81);
		tracker.addImage(Obstacles[15], 82);
		tracker.addImage(Obstacles[16], 83);
		tracker.addImage(Obstacles[17], 84);
		tracker.addImage(Obstacles[18], 85);
		tracker.addImage(Obstacles[19], 86);
		tracker.addImage(Obstacles[20], 87);
		tracker.addImage(Obstacles[21], 88);
		tracker.addImage(Obstacles[22], 89);
		tracker.addImage(Obstacles[23], 90);

		tracker.addImage(Obstacles[24], 91);
		tracker.addImage(Obstacles[25], 92);
		tracker.addImage(Obstacles[26], 93);
		tracker.addImage(Obstacles[27], 94);
		tracker.addImage(Obstacles[28], 95);
		tracker.addImage(Obstacles[29], 96);
		tracker.addImage(Obstacles[30], 97);
		tracker.addImage(Obstacles[31], 98);
		tracker.addImage(Obstacles[32], 99);
		tracker.addImage(Obstacles[33], 100);
		tracker.addImage(Obstacles[34], 101);
		tracker.addImage(Obstacles[35], 102);
		tracker.addImage(Obstacles[36], 103);
		tracker.addImage(Obstacles[37], 104);
		tracker.addImage(Obstacles[38], 105);
		tracker.addImage(Obstacles[39], 106);
		tracker.addImage(Obstacles[40], 107);
		tracker.addImage(Obstacles[41], 108);
		tracker.addImage(Obstacles[42], 109);
		tracker.addImage(Obstacles[43], 110);

		tracker.addImage(Bike[0], 111);
		tracker.addImage(Bike[1], 112);
		tracker.addImage(Bike[2], 113);
		tracker.addImage(Bike[3], 114);
		tracker.addImage(Bike[4], 115);
		tracker.addImage(Bike[5], 116);
		tracker.addImage(Bike[6], 117);
		tracker.addImage(Bike[7], 118);
		tracker.addImage(Bike[8], 119);
		tracker.addImage(Bike[9], 120);
		tracker.addImage(Bike[10], 121);

		tracker.addImage(lose[0], 121);
		tracker.addImage(lose[1], 122);
		tracker.addImage(lose[2], 123);
		tracker.addImage(lose[3], 124);
		tracker.addImage(lose[4], 125);
		tracker.addImage(lose[5], 126);
		tracker.addImage(lose[6], 127);
		
		tracker.addImage(win[0], 128);
		tracker.addImage(win[1], 129);
		
		int percent_complete = 0, img_num = 0;
		int increment = 1;
		
		while(img_num <= 129){
			try{
				tracker.waitForID(img_num);
			}
			catch(InterruptedException e)
			{
				return;
			}

			percent_complete += increment;

			g3.setColor(Color.black);
			g3.drawRect(85, 165, 129, 8);
			g3.fillRect(85, 165, percent_complete, 8);

			tempimg = offimg2;
			repaint();

			img_num++;
		}
/*
		try{
			tracker.waitForID(0);
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }
*/
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

			stop();
		}

//		drawPercent = Buildings[0];

//		do{
//		}while(drawPercent == null);

		repaint();

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

			pause(200);
		
		}while(START == null);

		showLoading();
		clearOffscn();

		getAppletContext().showStatus("Copyright 1999 J&E Bicycles.");

		clearLevel(1);

		try{
			tracker.waitForID(1);
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }

//		pause(600);

///////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////
		current_level = 1;
///////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////
		current_sector = current_lane = 2;
		current_screen = current_score = current_damage = 0;
		
		loadLevel(current_level);

		redrawOffscn(0);
		redrawOffscn(1);

		BUNNYHOP = false;
		NO_LANE_CHANGE = false;
		CRASHED = false;

		x = y = panX = lvl_pct_done = hopHeight = hopLength = 0;
		
		int trackernum = 0, bikeimagenum = 0;

		CurrentBikeImage = Bike[0];

		current_speed = 150;  // 90 = 16.40 fps

//		pause(700);

		getAppletContext().showStatus("http://www.jebikes.com/");

	//Main Game Loop

	do{

		//Level Loop
		g3.drawImage(offimg, 0, 0, this);
		g3.drawImage(shadow, 60, 85+40, this);
		g3.drawImage(CurrentBikeImage, 60, 85, this);
		redrawDisplay();

		tempimg = offimg2;
		x = 0;
		y = 0;

		repaint();

		for(int k = 3; k >= 1; k--){
			g3.drawImage(count[k], 96, 94, this);
			tempimg = offimg2;
			x = 0;
			y = 0;
			repaint();

			pause(1000);
		}
		
/*
		g3.drawImage(count2, 96, 94, this);
		tempimg = offimg2;
		x = 0;
		y = 0;
		repaint();
		
		pause(1000);

		g3.drawImage(count1, 96, 94, this);
		tempimg = offimg2;
		x = 0;
		y = 0;
		repaint();
		
		pause(1000);
*/
		g3.drawImage(count_go, 96, 94, this);
		tempimg = offimg2;
		x = 0;
		y = 0;
		repaint();
		
		pause(1000);

		do{
			panScreen();		

			trackernum++;

			if(BUNNYHOP == true){
				if(hopLength >= hopSize){
					hopLength = hopHeight = 0;
					BUNNYHOP = false;
					NO_LANE_CHANGE = false;
				}
				
				else{
					if(hopLength < 12){
						hopHeight-=2;
						CurrentBikeImage = Bike[4];
					}					
					else if(hopLength > 48){
						hopHeight+=2;	
						CurrentBikeImage = Bike[6];
					}
					else
						CurrentBikeImage = Bike[5];

					hopLength++;
				}

				if(ObstacleLocation[current_sector][current_lane] != 0){
					current_score += 10;
				}
			}

			else{
				if(RampLocation[current_sector][current_lane] != 0){
					BUNNYHOP = true;

					if(RampLocation[current_sector][current_lane] == 1)
						hopSize = 60 / panIncrement * 2;
					else
						hopSize = 60 / panIncrement * 4;
				}		

				else if(ObstacleLocation[current_sector][current_lane] > 0 && ObstacleLocation[current_sector][current_lane] < 41){
					CRASHED = true;
					crash();
				}
		
				if(trackernum >= 7){
					trackernum = 0;
					++bikeimagenum;
					if(bikeimagenum > 3)
						bikeimagenum = 0;
					if(panIncrement != 0)
						CurrentBikeImage = Bike[bikeimagenum];
				}
			}

			g3.drawImage(offimg, 0, 0, this);
			drawBike();
			bikex = 60 + panX;
			g3.drawImage(shadow, bikex, shadowY, this);
			g3.drawImage(CurrentBikeImage, bikex, bikey, this);
			redrawDisplay();

			tempimg = offimg2;
			x = 0 - panX;

			repaint();

			pause(25);

		}while(DONE == false); //End Level Loop

		if(LEVEL_SUCCESS == false){
			GAME_DONE = true;
		
			loop = 1;

			do{
				clearLevel(loop);
				loop++;
			}while(loop <= 10);

			loseAnimation();
			
			g3.drawImage(game_over, 0, 0, this);
			x = 0;
			repaint();

			pause(3000);

			break;
		}

		g3.setColor(Color.white);
		g3.setFont(f);			

		showLevelComplete();
		pause(2000);

		g3.drawString("Current Score: "+current_score, 35, 100);
		tempimg = offimg2;
		repaint();
		pause(1000);

		g3.drawString("End Level Bonus: "+(current_level*100), 35, 120);
		tempimg = offimg2;
		repaint();
		pause(1000);
		current_score += current_level * 100;

		if(CRASHED == false){
			g3.setColor(Color.yellow);
			g3.drawString("Crashless bonus: 100", 35, 140);
			tempimg = offimg2;
			repaint();
			pause(1000);
			g3.setColor(Color.white);
			current_score += 100;
		}

		if(current_damage == 0 && current_level == 10){
			g3.setColor(Color.yellow);
			g3.drawString("Super Crashless bonus: 1000", 35, 160);
			tempimg = offimg2;
			repaint();
			pause(1000);
			g3.setColor(Color.white);
			current_score += 1000;
		}

		g3.drawString("New Total: "+current_score, 35, 190);
		tempimg = offimg2;
		repaint();
		pause(2000);

		if(current_level == num_levels){
			winGame();
			GAME_DONE = true;
			clearLevel(current_level);
			break;
		}			

		g3.drawString("Loading Level "+(current_level+1)+"...", 100, 250);
		tempimg = offimg2;
		repaint();
		pause(1000);

//		showLoading();
		clearOffscn();

		BUNNYHOP = false;
		CRASHED = false;

		current_level++;

		current_sector = current_lane = 2;
		current_screen = 0;

		loadLevel(current_level);

		redrawOffscn(0);
//		redrawOffscn(1);

		x = y = panX = hopHeight = hopLength = panIncrement = 0;
		
		trackernum = lvl_pct_done = bikeimagenum = 0;

		CurrentBikeImage = Bike[0];

		current_speed = 150;

		pause(1000);

		DONE = false;
		
	}while(GAME_DONE == false);

		START = null;
		GAME_DONE = false;
		DONE = false;
		panIncrement = 0;

	}while(true);

//	stop();
	}


	public boolean keyDown(Event evt, int key){
		switch(key){
			case Event.DOWN:  
					if(NO_LANE_CHANGE == false){
						if(panIncrement != 0){
							++current_lane;
							
							if(BUNNYHOP == true)
								NO_LANE_CHANGE = true;

							if(current_lane > 4)
								current_lane = 4;
						}
					}
						break;
			case Event.UP:  
					if(NO_LANE_CHANGE == false){
						if(panIncrement != 0){
							--current_lane;

							if(BUNNYHOP == true)
								NO_LANE_CHANGE = true;
	
							if(current_lane < 1)
								current_lane = 1;
						}
					}
					break;
			case Event.LEFT:  
					break;
			case Event.RIGHT:
					break;
			default:
	   			char key_press = (char)key;
				break;
		}

		//Process key_press if key != above

		return true;
	}

	public boolean keyUp(Event evt, int key){
		if(START != null)
			START = Bike[0];

		switch(key){
			case Event.DOWN:
					break;
			case Event.UP:
					break;
/*			case Event.LEFT:
					panIncrement = 0;
					break;
*/
			case Event.RIGHT:
					if(panIncrement == 0)
						panIncrement = speed;
					break;
			case 32: // Space Bar
					if(panIncrement != 0){
						BUNNYHOP = true;
						hopSize = 60 / panIncrement * 2;
					}
					break;
			default:
				if(key == 83 || key == 115){
					START = Buildings[0];
					speed = 6;
				}
				break;
		}

		if(key == 70 || key == 102){
					START = Buildings[0];
					speed = 4;
		}
		
		//Process key_unpress if key != above
		return true;
	}

	public int drawBike(){
		switch(current_lane){
			case 1:  bikey = 75 - 30;
				break;
			case 2:  bikey = 115 - 30;
				break;
			case 3:  bikey = 155 - 30;
				break;
			case 4:  bikey = 195 - 30;
				break;
		}
//		bikex = 60 + panX;
		
		shadowY = bikey + 42;
		bikey += hopHeight;

		return(bikey);
		
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
			draw(current_sector - 1, 0);
			draw(current_sector, 1);
			draw(current_sector + 1, 2);
			draw(current_sector + 2, 3);
			draw(current_sector + 3, 4);
			draw(current_sector + 4, 5);
		}

		else{
			draw(current_sector - 1, 0);
			draw(current_sector, 1);
			draw(current_sector + 1, 2);
			draw(current_sector + 2, 3);
			draw(current_sector + 3, 4);
			draw(current_sector + 4, 5);
		}
	}

	
	public void draw(int sectornum, int rownum){
		int drawx = rownum * 60;
	
		g2.drawImage(Buildings[lvl_bldgs[sectornum]], drawx, 0, this);

		g2.drawImage(CurrentSidewalk[1], drawx, 195, this);

		g2.drawImage(CurrentStreet[1], drawx, 155, this);

		g2.drawImage(CurrentStreet[0], drawx, 115, this);

		g2.drawImage(CurrentSidewalk[0], drawx, 75, this);

			if(RampLocation[sectornum][4] != 0)
				g2.drawImage(CurrentRamps[RampLocation[sectornum][4]-1], drawx, 195-5, this);
			if(ObstacleLocation[sectornum][4] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][4]-1], drawx, 195-10, this);

			if(RampLocation[sectornum][3] != 0)
				g2.drawImage(CurrentRamps[RampLocation[sectornum][3]-1], drawx, 155-5, this);
			if(ObstacleLocation[sectornum][3] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][3]-1], drawx, 155-10, this);

			if(RampLocation[sectornum][2] != 0)
				g2.drawImage(CurrentRamps[RampLocation[sectornum][2]-1], drawx, 115-5, this);
			if(ObstacleLocation[sectornum][2] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][2]-1], drawx, 115-10, this);

			if(RampLocation[sectornum][1] != 0)
				g2.drawImage(CurrentRamps[RampLocation[sectornum][1]-1], drawx, 75 - 5, this);
			if(ObstacleLocation[sectornum][1] != 0)
				g2.drawImage(Obstacles[ObstacleLocation[sectornum][1]-1], drawx, 75-10, this);

	}

	public void crash(){
		int tempx;

		if(panX != 0)
			tempx = 0 - panX;

		else
			tempx = 0;

		panIncrement = 0;
		
		current_damage += 10;
		if(current_damage >= 100){
			LEVEL_SUCCESS = false;
			DONE = true;
		}

		int animnum = 6;

		do{
			x = tempx;

			g3.drawImage(offimg, 0, 0, this);
//			drawBike();
//			bikex = 60 + panX;
			g3.drawImage(shadow, bikex, shadowY, this);
			g3.drawImage(Bike[animnum], bikex, bikey, this);
			redrawDisplay();

			tempimg = offimg2;

			repaint();
	
			pause(75);
			animnum++;
		}while(animnum <= 10);

		pause(1500);

/*		++current_lane;
		
		if(current_lane > 4)
			current_lane-=2;
*/

		current_sector++;
		redrawOffscn(current_screen);

		if(ObstacleLocation[current_sector][current_lane] != 0 || RampLocation[current_sector][current_lane] != 0){

		animnum = 0;

		do{
			animnum++;
			current_lane++;
			if(current_lane > 4)
				current_lane = 1;
			if(animnum >= 4)
				break;
		}while(ObstacleLocation[current_sector][current_lane] != 0 || RampLocation[current_sector][current_lane] != 0);

		if(ObstacleLocation[current_sector][current_lane] != 0 || RampLocation[current_sector][current_lane] != 0){
			current_sector++;
			panX = 0;
			redrawOffscn(current_screen);
			animnum = 0;

			do{
				animnum++;
				current_lane++;
				if(current_lane > 4)
					current_lane = 1;
				if(animnum >= 4)
					break;
			}while(ObstacleLocation[current_sector][current_lane] != 0 || RampLocation[current_sector][current_lane] != 0);
		}
	
		}

//		int loopnum = 0;

		current_score -= 100;

		if(current_score <= 0)
			current_score = 0;

	}

	public void loseAnimation(){
		int img = 1, bx = bikex, by = bikey + 40;
			
		g3.drawImage(offimg, 0, 0, this);
		g3.drawImage(lose[1], bikex, bikey, this);
		g3.drawImage(lose[0], bx, by, this);

		tempimg = offimg2;
		repaint();
		
		pause(1000);
		
		img = 2;
		
		for(int i = 1; i <= 7; i++){
			g3.drawImage(offimg, 0, 0, this);
			g3.drawImage(lose[0], bx, by, this);
			g3.drawImage(lose[img], bikex, bikey, this);
			
			tempimg = offimg2;
			repaint();
			
			pause(166);
			
			img = 5 - img;
		}
		
		for(int i = 4; i <= 6; i++){
			g3.drawImage(offimg, 0, 0, this);
			g3.drawImage(lose[0], bx, by, this);
			g3.drawImage(lose[i], bikex, bikey, this);
			
			tempimg = offimg2;
			repaint();
			
			pause(150);
		}
		
		for(int i = 1; i <= 60; i++){
			g3.drawImage(offimg, 0, 0, this);
			g3.drawImage(lose[0], bx, by, this);
			g3.drawImage(lose[6], bikex, bikey, this);
			
			tempimg = offimg2;
			repaint();
			
			pause(35);
			
			if(i < 20){
				bx += 3;  by-=3;
			}
			else if(i < 40){
				bx+=3;
			}
			else{
				bx+=3;  by+=3;
			}
				
		}
		
		pause(500);
		
	}
	
	public void winGame(){
		int posy, posx, img = 0;
		
		for(posy = 40; posy <= 120; posy += 40){
			for(posx = 0; posx < 301; posx += 4){
				g3.drawImage(offimg, 0-panX, 0, this);
				g3.drawImage(win[img], posx, posy, this);
				
				tempimg = offimg2;
				repaint();
				
				pause(30);
				
				if(posx % 32 == 0)
					img = 1 - img;
			}
		}
		
		g3.drawImage(win_scn, 0, 0, this);
		tempimg = offimg2;
		x = 0;
		repaint();
		pause(3000);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 300, 280);
		g3.setColor(Color.white);
		g3.drawString("Credits", 113, 130);
		tempimg = offimg2;
		repaint();			
		pause(1500);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 300, 280);
		g3.setColor(Color.white);
		g3.drawString("produced by", 100, 110);
		g3.drawString("J&E Bicycles", 97, 120);
		tempimg = offimg2;
		repaint();			
		pause(2000);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 300, 280);
		g3.setColor(Color.white);
		g3.drawString("game program", 95, 100);
		g3.drawString("Jared Stehler", 93, 110);
		tempimg = offimg2;
		repaint();			
		pause(2000);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 300, 280);
		g3.setColor(Color.white);
		g3.drawString("artwork, level design", 64, 100);
		g3.drawString("Eric Miller", 113, 110);
		tempimg = offimg2;
		repaint();			
		pause(2000);

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 300, 280);
		g3.setColor(Color.white);
		g3.drawString("Street Rider v1.5", 64, 100);
		g3.drawString("Copyright 1999 J&E Bicycles", 50, 110);
		g3.drawString("All Rights Reserved", 64, 120);
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
			case 1:lvl_num_sectors = 101;
				break;

			case 2:lvl_num_sectors = 100;
				break;

			case 3:lvl_num_sectors = 90;
				break;

			case 4:lvl_num_sectors = 150;
				break;

			case 5:lvl_num_sectors = 100;
				break;

			case 6:lvl_num_sectors = 100;
				break;

			case 7:lvl_num_sectors = 80;
				break;

			case 8:lvl_num_sectors = 100;
				break;

			case 9:lvl_num_sectors = 100;
				break;

			case 10:lvl_num_sectors = 200;
				break;
		}

		int loopvar = 1, loopvar2 = 1, picnum = 0;

		CurrentSidewalk[0] = Sidewalk[0];
		CurrentSidewalk[1] = Sidewalk[1];
		CurrentStreet[0] = Street[0];
		CurrentStreet[1] = Street[1];

		CurrentRamps[0] = Ramps[0];
		CurrentRamps[1] = Ramps[1];

		do{
			lvl_bldgs[loopvar] = picnum;
			loopvar++;
			picnum++;
			if(picnum > 29)
				picnum = 0;
		}while(loopvar <= lvl_num_sectors);

		switch(levelnum){
			case 1:	
				RampLocation[10][2] = 2;
				ObstacleLocation[11][2] = 2;
				ObstacleLocation[12][2] = 2;
				ObstacleLocation[13][2] = 2;
				
				ObstacleLocation[6][3] = 4;
	
				RampLocation[15][1] = 1;

				ObstacleLocation[17][1] = 3;
				ObstacleLocation[18][4] = 3;
				
				RampLocation[20][4] = 1;
                                RampLocation[21][4] = 2;

				ObstacleLocation[22][4] = 2;
				ObstacleLocation[23][4] = 2;
				ObstacleLocation[24][4] = 2;

				ObstacleLocation[22][3] = 2;
				ObstacleLocation[27][1] = 26;

				ObstacleLocation[35][1] = 18;
				ObstacleLocation[36][1] = 2;
				ObstacleLocation[35][2] = 4;
				ObstacleLocation[36][2] = 24;
				ObstacleLocation[35][4] = 2;
				ObstacleLocation[36][4] = 17;
				ObstacleLocation[40][1] = 26;

				ObstacleLocation[43][3] = 2;
				ObstacleLocation[44][3] = 6;
				ObstacleLocation[47][1] = 26;
	
				ObstacleLocation[54][1] = 11;
				ObstacleLocation[54][3] = 2;
				ObstacleLocation[54][4] = 13;
				ObstacleLocation[57][1] = 26;

				ObstacleLocation[61][2] = 3;
				ObstacleLocation[62][3] = 4;
				ObstacleLocation[64][1] = 9;
				ObstacleLocation[69][4] = 10;

				RampLocation[65][1] = 1;
				ObstacleLocation[66][1] = 4;
				

				RampLocation[70][1] = 2;
				RampLocation[71][2] = 2;
				RampLocation[72][3] = 2;
				RampLocation[71][4] = 2;

				ObstacleLocation[71][1] = 2;
				ObstacleLocation[72][1] = 2;
				ObstacleLocation[73][1] = 2;
				ObstacleLocation[74][1] = 2;

				ObstacleLocation[72][2] = 2;
				ObstacleLocation[73][2] = 2;
				ObstacleLocation[74][2] = 2;


				ObstacleLocation[74][4] = 3;	
				RampLocation[75][4] = 1;	

				RampLocation[87][1] = 1;
				RampLocation[88][2] = 2;
				RampLocation[89][3] = 1;
				RampLocation[87][4] = 2;

				ObstacleLocation[91][1] = 5;	
				ObstacleLocation[91][2] = 6;	
				ObstacleLocation[91][3] = 7;	
				ObstacleLocation[91][4] = 8;	

				ObstacleLocation[94][1] = 26;
				ObstacleLocation[99][1] = 2;	
				ObstacleLocation[99][2] = 2;	
				ObstacleLocation[99][3] = 2;	
				ObstacleLocation[99][4] = 2;					

				break;


			case 2:
				clearLevel(1);

				ObstacleLocation[10][1]=17;
				ObstacleLocation[11][1]=2;
				ObstacleLocation[12][1]=18; 
				ObstacleLocation[12][2]=13;

				ObstacleLocation[12][4]=22;
				ObstacleLocation[14][2]=12;
				ObstacleLocation[15][2]=16;

				RampLocation[15][3]=2;

				ObstacleLocation[15][4]=17;
				ObstacleLocation[16][4]=2;
				ObstacleLocation[17][4]=18;
				ObstacleLocation[19][3]=19;

				ObstacleLocation[19][1]=11;
				ObstacleLocation[21][1]=14;
				ObstacleLocation[21][2]=24;

				RampLocation[21][3]=2;
				ObstacleLocation[22][3]=15;
				ObstacleLocation[23][3]=19;
				ObstacleLocation[24][3]=15;
				ObstacleLocation[23][4]=15;

				ObstacleLocation[25][4]=4;
				ObstacleLocation[25][1]=26;
				ObstacleLocation[26][3]=24;
				ObstacleLocation[27][4]=13;

				ObstacleLocation[31][2]=23;
				ObstacleLocation[30][2]=15;

				ObstacleLocation[34][1] = 18;
				RampLocation[35][2]=1;
				ObstacleLocation[36][1] = 2;
				ObstacleLocation[36][2] = 4;
				ObstacleLocation[37][2] = 24;
				ObstacleLocation[34][4] = 2;
				ObstacleLocation[37][4] = 17;

				ObstacleLocation[40][1]=26;
				ObstacleLocation[43][1]=17;
				ObstacleLocation[44][1]=2;
				ObstacleLocation[45][1]=18; 
				ObstacleLocation[44][2]=13;

				ObstacleLocation[44][4]=22;
				ObstacleLocation[44][2]=12;
				ObstacleLocation[45][2]=16;

				RampLocation[43][3]=2;

				ObstacleLocation[48][4]=17;
				ObstacleLocation[46][4]=2;
				ObstacleLocation[49][4]=18;
				ObstacleLocation[50][3]=19;
			
				RampLocation[50][2]=1;
				ObstacleLocation[51][2]=13;
				ObstacleLocation[52][2]=24;
				ObstacleLocation[51][1]=22;
				ObstacleLocation[54][3]=19;
				ObstacleLocation[56][4]=14;
				ObstacleLocation[59][3]=10;
	                  ObstacleLocation[55][1]=26;

				RampLocation[60][1]=2;
				RampLocation[60][4]=1;
				ObstacleLocation[61][1]=26;
				ObstacleLocation[62][1]=17;
				ObstacleLocation[63][1]=2;
				ObstacleLocation[64][1]=18; 
				ObstacleLocation[64][2]=13;
				ObstacleLocation[65][2]=14;
				ObstacleLocation[66][2]=24;
				ObstacleLocation[67][1]=22;
				ObstacleLocation[71][3]=19;
				ObstacleLocation[72][4]=14;
				ObstacleLocation[72][3]=10;
	                  ObstacleLocation[70][1]=26;

				ObstacleLocation[75][4]=4;
				ObstacleLocation[75][1]=26;
				ObstacleLocation[76][3]=24;
				ObstacleLocation[77][4]=13;

				ObstacleLocation[81][2]=23;
				ObstacleLocation[80][2]=15;

				ObstacleLocation[84][1] = 18;
				RampLocation[85][2]=1;
				ObstacleLocation[85][1]=26;
				ObstacleLocation[86][1] = 2;
				ObstacleLocation[86][2] = 4;
				ObstacleLocation[87][2] = 24;
				ObstacleLocation[84][4] = 2;
				ObstacleLocation[87][4] = 17;

				ObstacleLocation[90][1]=26;
				ObstacleLocation[93][1]=17;
				ObstacleLocation[94][1]=2;
				ObstacleLocation[95][1]=18; 
				ObstacleLocation[94][3]=13;

				RampLocation[60][1]=2;
				RampLocation[60][4]=1;
				RampLocation[68][4]=2;
				RampLocation[75][3]=1;
				RampLocation[82][1]=1;
				RampLocation[84][3]=2;
				RampLocation[90][2]=1;
				RampLocation[93][4]=2;
				RampLocation[94][2]=2;

				ObstacleLocation[99][1]=26;
	
				break;
			case 3:
				clearLevel(2);
				loopvar = 1;
				loopvar2 = 31;

				do{
					lvl_bldgs[loopvar] = loopvar2;
					loopvar++;
					loopvar2++;
					if(loopvar2 > 34)
						loopvar2 = 31;
				}while(loopvar <= lvl_num_sectors);

				CurrentSidewalk[0] = Street[0];
				CurrentSidewalk[1] = Street[1];
				CurrentStreet[0] = Street[0];
				CurrentStreet[1] = Street[0];

				RampLocation[7][1] = 2;
				RampLocation[9][3] = 2;
				RampLocation[15][2] = 2;
				RampLocation[15][4] = 2;
				RampLocation[19][1] = 2;
				RampLocation[19][3] = 2;
				RampLocation[38][1] = 1;
				RampLocation[38][4] = 1;
				RampLocation[39][4] = 2;
				RampLocation[43][3] = 2;
				RampLocation[45][1] = 2;
				RampLocation[45][2] = 2;
				RampLocation[45][3] = 2;
				RampLocation[45][4] = 2;
				RampLocation[55][1] = 2;
				RampLocation[55][2] = 2;
				RampLocation[55][4] = 2;
				RampLocation[59][1] = 2;
				RampLocation[59][2] = 2;
				RampLocation[59][3] = 2;
				RampLocation[59][4] = 2;
				RampLocation[63][1] = 2;
				RampLocation[63][2] = 2;
				RampLocation[63][3] = 2;
				RampLocation[67][2] = 2;
				RampLocation[82][1] = 2;
				RampLocation[82][2] = 2;
				RampLocation[82][3] = 2;
				RampLocation[82][4] = 2;

				ObstacleLocation[5][2] = 20;
				ObstacleLocation[7][4] = 13;
				ObstacleLocation[11][1] = 3;
				ObstacleLocation[13][1] = 20;
				ObstacleLocation[13][2] = 20;
				ObstacleLocation[13][3] = 20;
				ObstacleLocation[13][4] = 20;
				ObstacleLocation[14][1] = 20;
				ObstacleLocation[14][3] = 20;
				ObstacleLocation[16][3] = 11;
				ObstacleLocation[19][2] = 15;
				ObstacleLocation[19][4] = 15;
				ObstacleLocation[21][3] = 23;
				ObstacleLocation[23][1] = 15;
				ObstacleLocation[23][2] = 15;
				ObstacleLocation[23][4] = 3;
				ObstacleLocation[26][3] = 24;
				ObstacleLocation[28][1] = 11;
				ObstacleLocation[29][4] = 15;
				ObstacleLocation[30][3] = 15;
				ObstacleLocation[30][4] = 23;
				ObstacleLocation[31][3] = 15;
				ObstacleLocation[31][4] = 23;
				ObstacleLocation[32][4] = 15;
				ObstacleLocation[33][1] = 12;
				ObstacleLocation[35][3] = 13;
				ObstacleLocation[37][4] = 11;
				ObstacleLocation[40][1] = 13;
				ObstacleLocation[40][4] = 3;
				ObstacleLocation[43][4] = 3;
				ObstacleLocation[44][3] = 3;
				ObstacleLocation[44][4] = 3;
				ObstacleLocation[46][4] = 20;
				ObstacleLocation[47][3] = 3;
				ObstacleLocation[47][4] = 20;
				ObstacleLocation[48][4] = 20;
				ObstacleLocation[50][4] = 13;
				ObstacleLocation[52][4] = 13;
				ObstacleLocation[54][4] = 15;
				ObstacleLocation[55][4] = 15;
				ObstacleLocation[64][1] = 14;
				ObstacleLocation[64][4] = 3;
				ObstacleLocation[67][1] = 12;
				ObstacleLocation[67][3] = 12;
				ObstacleLocation[67][4] = 11;
				ObstacleLocation[69][1] = 23;
				ObstacleLocation[69][2] = 14;
				ObstacleLocation[69][3] = 13;
				ObstacleLocation[69][4] = 12;
				ObstacleLocation[72][1] = 15;
				ObstacleLocation[72][3] = 15;
				ObstacleLocation[72][4] = 15;
				ObstacleLocation[73][1] = 15;
				ObstacleLocation[73][3] = 15;
				ObstacleLocation[73][4] = 15;
				ObstacleLocation[74][1] = 15;
				ObstacleLocation[74][4] = 15;
				ObstacleLocation[75][1] = 15;
				ObstacleLocation[75][2] = 15;
				ObstacleLocation[75][4] = 15;
				ObstacleLocation[76][1] = 15;
				ObstacleLocation[76][2] = 15;
				ObstacleLocation[76][4] = 15;
				ObstacleLocation[77][1] = 15;
				ObstacleLocation[77][4] = 15;
				ObstacleLocation[78][1] = 15;
				ObstacleLocation[78][3] = 15;
				ObstacleLocation[78][4] = 15;
				ObstacleLocation[79][1] = 15;
				ObstacleLocation[79][2] = 15;
				ObstacleLocation[79][3] = 15;
				ObstacleLocation[79][4] = 15;
				ObstacleLocation[80][1] = 15;
				ObstacleLocation[80][3] = 15;
				ObstacleLocation[83][1] = 14;
				ObstacleLocation[83][2] = 14;
				ObstacleLocation[83][3] = 14;
				ObstacleLocation[83][4] = 14;
				ObstacleLocation[84][1] = 3;
				ObstacleLocation[84][4] = 3;
				ObstacleLocation[85][1] = 14;
				ObstacleLocation[85][2] = 14;
				ObstacleLocation[85][3] = 14;
				ObstacleLocation[85][4] = 14;
				ObstacleLocation[88][1] = 24;

				break;
			case 4:
				clearLevel(3);	
				loopvar = 1;

				do{
					lvl_bldgs[loopvar] = 30;
					loopvar++;
				}while(loopvar <= lvl_num_sectors);

				CurrentSidewalk[0] = dirt;
				CurrentSidewalk[1] = dirt;
				CurrentStreet[0] = dirt;
				CurrentStreet[1] = dirt;

				CurrentRamps[0] = Ramps[2];
				CurrentRamps[1] = Ramps[3];

				RampLocation[9][1] = 2;
				RampLocation[15][3] = 1;
				RampLocation[15][4] = 2;
				RampLocation[27][1] = 2;
				RampLocation[27][2] = 2;
				RampLocation[27][3] = 2;
				RampLocation[27][4] = 2;
				RampLocation[31][3] = 2;
				RampLocation[35][2] = 2;
				RampLocation[35][3] = 2;
				RampLocation[39][1] = 2;
				RampLocation[39][3] = 2;
				RampLocation[41][4] = 2;
				RampLocation[43][1] = 2;
				RampLocation[43][2] = 2;
				RampLocation[45][3] = 2;
				RampLocation[45][4] = 2;
				RampLocation[47][1] = 2;
				RampLocation[47][2] = 2;
				RampLocation[59][1] = 2;
				RampLocation[58][4] = 1;
				RampLocation[59][4] = 2;
				RampLocation[66][1] = 2;
				RampLocation[66][2] = 2;
				RampLocation[66][3] = 1;
				RampLocation[66][4] = 2;
				RampLocation[72][3] = 2;
				RampLocation[73][4] = 1;
				RampLocation[87][4] = 2;
				RampLocation[115][1] = 2;
				RampLocation[115][2] = 2;
				RampLocation[115][3] = 2;
				RampLocation[115][4] = 2;
				RampLocation[135][1] = 2;
				RampLocation[135][3] = 2;
				RampLocation[136][2] = 2;
				RampLocation[136][4] = 2;

				ObstacleLocation[7][3] = 3;
				ObstacleLocation[10][2] = 12;
				ObstacleLocation[11][4] = 21;
				ObstacleLocation[12][1] = 1;
				ObstacleLocation[12][3] = 20;
				ObstacleLocation[14][2] = 14;
				ObstacleLocation[17][3] = 3;
				ObstacleLocation[18][2] = 13;
				ObstacleLocation[19][1] = 5;
				ObstacleLocation[19][2] = 6;
				ObstacleLocation[19][3] = 7;
				ObstacleLocation[19][4] = 8;
				ObstacleLocation[22][2] = 3;
				ObstacleLocation[23][4] = 10;
				ObstacleLocation[24][1] = 21;
				ObstacleLocation[24][3] = 9;
				ObstacleLocation[28][1] = 3;
				ObstacleLocation[28][2] = 3;
				ObstacleLocation[28][3] = 3;
				ObstacleLocation[28][4] = 3;
				ObstacleLocation[29][3] = 9;
				ObstacleLocation[30][1] = 13;
				ObstacleLocation[30][3] = 10;
				ObstacleLocation[31][1] = 17;
				ObstacleLocation[31][2] = 3;
				ObstacleLocation[31][4] = 11;
				ObstacleLocation[32][2] = 21;
				ObstacleLocation[35][4] = 10;
				ObstacleLocation[39][2] = 8;
				ObstacleLocation[40][4] = 8;
				ObstacleLocation[42][3] = 9;
				ObstacleLocation[44][2] = 10;
				ObstacleLocation[46][3] = 3;
				ObstacleLocation[47][4] = 3;
				ObstacleLocation[48][3] = 21;
				ObstacleLocation[49][1] = 21;
				ObstacleLocation[50][2] = 21;
				ObstacleLocation[50][4] = 21;
				ObstacleLocation[52][2] = 3;
				ObstacleLocation[52][3] = 3;
				ObstacleLocation[54][1] = 5;
				ObstacleLocation[54][2] = 6;
				ObstacleLocation[54][3] = 7;
				ObstacleLocation[54][4] = 8;
				ObstacleLocation[58][1] = 8;
				ObstacleLocation[60][1] = 3;
				ObstacleLocation[60][2] = 9;
				ObstacleLocation[60][3] = 14;
				ObstacleLocation[60][4] = 3;
				ObstacleLocation[61][1] = 3;
				ObstacleLocation[61][4] = 3;
				ObstacleLocation[62][1] = 3;
				ObstacleLocation[62][3] = 13;
				ObstacleLocation[62][4] = 3;

				ObstacleLocation[68][3] = 10;
				ObstacleLocation[69][1] = 1;
				ObstacleLocation[69][4] = 14;
				ObstacleLocation[70][2] = 1;
				ObstacleLocation[73][1] = 12;
				ObstacleLocation[74][2] = 9;
				ObstacleLocation[74][4] = 3;
				ObstacleLocation[76][3] = 13;
				ObstacleLocation[78][1] = 5;
				ObstacleLocation[78][2] = 6;
				ObstacleLocation[78][3] = 7;
				ObstacleLocation[78][4] = 8;
				ObstacleLocation[81][1] = 21;
				ObstacleLocation[81][3] = 9;
				ObstacleLocation[82][4] = 17;
				ObstacleLocation[83][2] = 10;
				ObstacleLocation[83][4] = 18;
				ObstacleLocation[85][1] = 21;
				ObstacleLocation[86][3] = 20;
				ObstacleLocation[88][1] = 12;
				ObstacleLocation[89][2] = 9;
				ObstacleLocation[88][4] = 3;
				ObstacleLocation[89][4] = 3;
				ObstacleLocation[90][4] = 3;
				ObstacleLocation[91][1] = 9;
				ObstacleLocation[91][3] = 11;
				ObstacleLocation[93][4] = 14;
				ObstacleLocation[94][2] = 13;
				ObstacleLocation[96][1] = 21;
				ObstacleLocation[96][4] = 17;
				ObstacleLocation[97][3] = 10;
				ObstacleLocation[97][4] = 18;
				ObstacleLocation[100][1] = 5;
				ObstacleLocation[100][2] = 6;
				ObstacleLocation[100][3] = 7;
				ObstacleLocation[100][4] = 8;
				ObstacleLocation[101][1] = 3;
				ObstacleLocation[101][3] = 3;
				ObstacleLocation[101][4] = 3;
				ObstacleLocation[102][1] = 3;
				ObstacleLocation[102][4] = 3;
				ObstacleLocation[103][1] = 3;
				ObstacleLocation[103][2] = 3;
				ObstacleLocation[103][4] = 3;
				ObstacleLocation[104][1] = 3;
				ObstacleLocation[104][2] = 3;
				ObstacleLocation[104][4] = 3;
				ObstacleLocation[105][1] = 3;
				ObstacleLocation[105][4] = 3;
				ObstacleLocation[106][3] = 3;
				ObstacleLocation[106][4] = 3;
				ObstacleLocation[107][2] = 3;
				ObstacleLocation[107][3] = 3;
				ObstacleLocation[107][4] = 3;
				ObstacleLocation[108][2] = 3;
				ObstacleLocation[108][3] = 3;
				ObstacleLocation[108][4] = 3;
				ObstacleLocation[109][3] = 3;
				ObstacleLocation[110][1] = 3;
				ObstacleLocation[110][3] = 3;
				ObstacleLocation[111][1] = 3;
				ObstacleLocation[112][1] = 3;
				ObstacleLocation[112][2] = 3;
				ObstacleLocation[112][3] = 3;
				ObstacleLocation[112][4] = 3;
				ObstacleLocation[113][2] = 9;
				ObstacleLocation[113][4] = 3;
				ObstacleLocation[116][1] = 13;
				ObstacleLocation[116][2] = 13;
				ObstacleLocation[116][3] = 13;
				ObstacleLocation[116][4] = 13;
				ObstacleLocation[118][1] = 13;
				ObstacleLocation[118][3] = 13;
				ObstacleLocation[119][2] = 10;
				ObstacleLocation[119][4] = 9;
				ObstacleLocation[121][1] = 21;
				ObstacleLocation[121][3] = 17;
				ObstacleLocation[122][4] = 18;
				ObstacleLocation[124][1] = 5;
				ObstacleLocation[124][2] = 6;
				ObstacleLocation[124][3] = 7;
				ObstacleLocation[124][4] = 8;
				ObstacleLocation[127][1] = 5;
				ObstacleLocation[127][2] = 6;
				ObstacleLocation[127][3] = 7;
				ObstacleLocation[127][4] = 8;
				ObstacleLocation[128][3] = 10;
				ObstacleLocation[130][1] = 5;
				ObstacleLocation[130][2] = 6;
				ObstacleLocation[130][3] = 7;
				ObstacleLocation[130][4] = 8;
				ObstacleLocation[134][1] = 8;
				ObstacleLocation[134][3] = 8;
				ObstacleLocation[135][2] = 8;
				ObstacleLocation[135][4] = 8;
				ObstacleLocation[136][1] = 3;
				ObstacleLocation[137][1] = 3;
				ObstacleLocation[137][2] = 3;
				ObstacleLocation[137][3] = 3;
				ObstacleLocation[137][4] = 3;
				ObstacleLocation[138][1] = 3;
				ObstacleLocation[138][2] = 3;
				ObstacleLocation[138][3] = 3;
				ObstacleLocation[138][4] = 3;
				ObstacleLocation[142][1] = 5;
				ObstacleLocation[142][2] = 6;
				ObstacleLocation[142][3] = 7;
				ObstacleLocation[142][4] = 8;
				ObstacleLocation[144][2] = 9;
				ObstacleLocation[147][1] = 21;
				ObstacleLocation[147][4] = 21;
				ObstacleLocation[148][2] = 3;
				ObstacleLocation[148][3] = 3;
				ObstacleLocation[149][1] = 13;
				ObstacleLocation[149][4] = 14;

				break;
			case 5:
				clearLevel(4);
				loopvar = 1;
				loopvar2 = 31;

				do{
					lvl_bldgs[loopvar] = loopvar2;
					loopvar++;
					loopvar2++;
					if(loopvar2 > 34)
						loopvar2 = 31;
				}while(loopvar <= lvl_num_sectors);

				CurrentSidewalk[0] = Street[0];
				CurrentSidewalk[1] = Street[1];
				CurrentStreet[0] = Street[0];
				CurrentStreet[1] = Street[0];

ObstacleLocation[7][1]=3;
ObstacleLocation[7][2]=19;
ObstacleLocation[8][3]=14;
RampLocation[8][2]=2;
ObstacleLocation[9][2]=2;
ObstacleLocation[9][3]=12;
ObstacleLocation[10][1]=4;
ObstacleLocation[11][3]=15;
ObstacleLocation[11][4]=24;
RampLocation[12][2]=2;
ObstacleLocation[12][1]=16;
ObstacleLocation[13][2]=13;
ObstacleLocation[13][4]=15;
ObstacleLocation[14][3]=4;
ObstacleLocation[14][1]=17;
RampLocation[14][2]=2;
ObstacleLocation[15][3]=15;
ObstacleLocation[16][4]=19;
RampLocation[16][1]=2;
ObstacleLocation[17][2]=17;
ObstacleLocation[17][3]=20;
ObstacleLocation[18][4]=14;
ObstacleLocation[19][2]=3;
ObstacleLocation[19][1]=12;
RampLocation[20][2]=2;
ObstacleLocation[20][3]=2;
ObstacleLocation[21][4]=4;
ObstacleLocation[22][2]=15;
RampLocation[22][3]=2;
ObstacleLocation[23][1]=15;
ObstacleLocation[23][2]=19;
RampLocation[23][3]=2;
ObstacleLocation[25][2]=20;
RampLocation[25][1]=2;
ObstacleLocation[26][4]=15;
ObstacleLocation[27][3]=2;
ObstacleLocation[27][2]=15;
ObstacleLocation[27][1]=24;
ObstacleLocation[28][4]=11;
ObstacleLocation[28][2]=18;
ObstacleLocation[28][1]=14;
RampLocation[29][2]=2;
ObstacleLocation[30][4]=4;
ObstacleLocation[31][3]=11;
ObstacleLocation[31][2]=20;
ObstacleLocation[32][3]=15;
ObstacleLocation[33][2]=14;
RampLocation[34][4]=2;
ObstacleLocation[34][1]=12;
ObstacleLocation[35][3]=19;
RampLocation[35][2]=2;
ObstacleLocation[36][4]=15;
ObstacleLocation[37][2]=14;
ObstacleLocation[37][3]=13;
ObstacleLocation[37][1]=3;
ObstacleLocation[38][4]=20;
ObstacleLocation[38][2]=2;
RampLocation[39][1]=2;
ObstacleLocation[39][4]=15;
ObstacleLocation[40][3]=14;
ObstacleLocation[40][1]=17;
ObstacleLocation[42][3]=15;
RampLocation[42][2]=2;
ObstacleLocation[42][4]=4;
ObstacleLocation[43][3]=12;
RampLocation[43][2]=2;
ObstacleLocation[44][1]=20;
ObstacleLocation[44][3]=16;
ObstacleLocation[45][2]=20;
ObstacleLocation[45][4]=15;
RampLocation[46][3]=2;
ObstacleLocation[46][4]=23;
ObstacleLocation[47][1]=17;
ObstacleLocation[47][2]=23;
RampLocation[48][3]=2;
ObstacleLocation[48][2]=20;
ObstacleLocation[49][1]=3;
ObstacleLocation[49][3]=20;
ObstacleLocation[50][4]=19;
ObstacleLocation[50][2]=4;
ObstacleLocation[51][2]=20;
RampLocation[51][4]=2;
ObstacleLocation[51][1]=15;
ObstacleLocation[52][3]=20;
ObstacleLocation[52][2]=19;
ObstacleLocation[53][2]=24;
RampLocation[54][4]=2;
ObstacleLocation[54][1]=4;
ObstacleLocation[56][3]=20;
ObstacleLocation[56][2]=12;
ObstacleLocation[57][3]=19;
ObstacleLocation[57][2]=2;
RampLocation[57][1]=2;
ObstacleLocation[58][4]=3;
ObstacleLocation[58][3]=14;
ObstacleLocation[59][4]=4;
ObstacleLocation[59][2]=20;
ObstacleLocation[60][1]=20;
RampLocation[60][2]=2;
RampLocation[61][1]=2;
ObstacleLocation[61][3]=19;
ObstacleLocation[62][2]=3;
ObstacleLocation[62][4]=4;
ObstacleLocation[63][2]=2;
ObstacleLocation[63][1]=18;
RampLocation[64][4]=2;
ObstacleLocation[64][3]=18;
ObstacleLocation[65][2]=15;
ObstacleLocation[65][1]=20;
RampLocation[65][3]=2;
ObstacleLocation[66][4]=12;
ObstacleLocation[66][3]=11;
RampLocation[66][2]=2;
ObstacleLocation[67][1]=19;
RampLocation[68][1]=2;
ObstacleLocation[68][3]=20;
ObstacleLocation[69][3]=24;
ObstacleLocation[70][2]=4;
ObstacleLocation[70][1]=13;
RampLocation[71][2]=2;
ObstacleLocation[72][4]=3;
ObstacleLocation[72][1]=12;
ObstacleLocation[73][2]=15;
RampLocation[74][4]=2;
ObstacleLocation[75][3]=19;
ObstacleLocation[75][2]=13;
ObstacleLocation[76][1]=14;
RampLocation[76][3]=2;
ObstacleLocation[77][2]=20;
ObstacleLocation[78][1]=19;
RampLocation[78][2]=2;
ObstacleLocation[79][3]=20;
ObstacleLocation[79][4]=20;
ObstacleLocation[80][2]=15;
RampLocation[80][1]=2;
ObstacleLocation[81][2]=20;
ObstacleLocation[81][3]=20;
ObstacleLocation[82][4]=4;
ObstacleLocation[82][3]=20;
ObstacleLocation[82][2]=20;
ObstacleLocation[83][1]=17;
RampLocation[83][2]=2;
ObstacleLocation[84][3]=15;
ObstacleLocation[86][4]=24;
ObstacleLocation[87][3]=16;
ObstacleLocation[87][2]=20;
RampLocation[88][1]=2;
ObstacleLocation[90][3]=13;
ObstacleLocation[91][2]=24;
ObstacleLocation[93][4]=19;
RampLocation[93][3]=2;
ObstacleLocation[93][2]=15;
ObstacleLocation[93][1]=20;
RampLocation[94][3]=2;
ObstacleLocation[94][2]=3;
ObstacleLocation[94][1]=20;
ObstacleLocation[95][4]=2;
RampLocation[97][2]=2;

				break;
			case 6:
				CurrentSidewalk[1] = Obstacles[24];
				clearLevel(5);

ObstacleLocation[6][2]=4;
ObstacleLocation[7][3]=19;
ObstacleLocation[8][1]=17;
ObstacleLocation[9][1]=18;
ObstacleLocation[9][2]=20;
ObstacleLocation[10][1]=2;
ObstacleLocation[11][3]=26;
ObstacleLocation[14][1]=22;
ObstacleLocation[14][3]=20;
ObstacleLocation[16][2]=19;
ObstacleLocation[18][3]=26;
ObstacleLocation[18][2]=26;
ObstacleLocation[18][1]=26;
ObstacleLocation[22][1]=17;
ObstacleLocation[22][2]=20;
ObstacleLocation[22][3]=4;
ObstacleLocation[23][1]=18;
ObstacleLocation[24][1]=2;
ObstacleLocation[24][2]=20;
ObstacleLocation[26][2]=4;
ObstacleLocation[26][3]=19;
ObstacleLocation[28][1]=22;
ObstacleLocation[29][3]=20;
ObstacleLocation[30][3]=26;
ObstacleLocation[31][2]=12;
ObstacleLocation[32][2]=16;
ObstacleLocation[34][1]=18;
ObstacleLocation[34][2]=19;
ObstacleLocation[35][1]=2;
ObstacleLocation[36][1]=17;
ObstacleLocation[36][3]=26;
ObstacleLocation[38][2]=4;
ObstacleLocation[39][3]=20;
ObstacleLocation[40][1]=22;
ObstacleLocation[41][3]=19;
ObstacleLocation[42][1]=26;
ObstacleLocation[42][2]=26;
ObstacleLocation[42][3]=26;
ObstacleLocation[45][1]=2;
ObstacleLocation[46][1]=2;
ObstacleLocation[46][2]=15;
ObstacleLocation[47][1]=18;
ObstacleLocation[47][2]=19;
ObstacleLocation[48][1]=17;
ObstacleLocation[48][3]=4;
ObstacleLocation[50][1]=20;
ObstacleLocation[51][2]=15;
ObstacleLocation[52][2]=4;
ObstacleLocation[54][3]=19;
ObstacleLocation[56][1]=17;
ObstacleLocation[57][1]=2;
ObstacleLocation[58][1]=18;
ObstacleLocation[58][3]=12;
ObstacleLocation[61][1]=20;
ObstacleLocation[61][2]=19;
ObstacleLocation[64][1]=2;
ObstacleLocation[65][1]=18;
ObstacleLocation[65][2]=20;
ObstacleLocation[66][1]=17;
ObstacleLocation[66][2]=20;
ObstacleLocation[67][1]=2;
ObstacleLocation[67][2]=20;
ObstacleLocation[68][1]=20;
ObstacleLocation[68][3]=20;
ObstacleLocation[69][1]=20;
ObstacleLocation[69][3]=19;
ObstacleLocation[70][1]=12;
ObstacleLocation[71][1]=16;
ObstacleLocation[71][3]=26;
ObstacleLocation[72][2]=26;
ObstacleLocation[72][3]=14;
ObstacleLocation[73][2]=13;
ObstacleLocation[73][3]=15;
ObstacleLocation[74][3]=15;
ObstacleLocation[75][1]=18;
ObstacleLocation[75][3]=15;
ObstacleLocation[76][1]=2;
ObstacleLocation[76][2]=19;
ObstacleLocation[76][3]=14;
ObstacleLocation[77][1]=17;
ObstacleLocation[77][3]=15;
ObstacleLocation[78][1]=13;
ObstacleLocation[78][3]=12;
ObstacleLocation[79][1]=14;
ObstacleLocation[79][2]=14;
ObstacleLocation[80][1]=15;
ObstacleLocation[80][2]=15;
ObstacleLocation[81][3]=15;
ObstacleLocation[82][1]=22;
ObstacleLocation[84][3]=19;
ObstacleLocation[85][1]=2;
ObstacleLocation[85][2]=4;
ObstacleLocation[86][1]=17;
ObstacleLocation[87][3]=26;
ObstacleLocation[88][1]=18;
ObstacleLocation[90][1]=2;
ObstacleLocation[90][3]=26;
ObstacleLocation[91][1]=17;
ObstacleLocation[91][2]=19;
ObstacleLocation[93][1]=18;
ObstacleLocation[95][1]=26;
ObstacleLocation[95][3]=26;
ObstacleLocation[97][3]=19;

ObstacleLocation[1][4]=25;
ObstacleLocation[2][4]=25;
ObstacleLocation[3][4]=25;
ObstacleLocation[4][4]=25;
ObstacleLocation[5][4]=25;
ObstacleLocation[6][4]=25;
ObstacleLocation[7][4]=25;
ObstacleLocation[8][4]=25;
ObstacleLocation[9][4]=25;
ObstacleLocation[10][4]=25;
ObstacleLocation[11][4]=25;
ObstacleLocation[12][4]=25;
ObstacleLocation[13][4]=25;
ObstacleLocation[14][4]=25;
ObstacleLocation[15][4]=25;
ObstacleLocation[16][4]=25;
ObstacleLocation[17][4]=25;
ObstacleLocation[18][4]=25;
ObstacleLocation[19][4]=25;
ObstacleLocation[20][4]=25;
ObstacleLocation[21][4]=25;
ObstacleLocation[22][4]=25;
ObstacleLocation[23][4]=25;
ObstacleLocation[24][4]=25;
ObstacleLocation[25][4]=25;
ObstacleLocation[26][4]=25;
ObstacleLocation[27][4]=25;
ObstacleLocation[28][4]=25;
ObstacleLocation[29][4]=25;
ObstacleLocation[30][4]=25;
ObstacleLocation[31][4]=25;
ObstacleLocation[32][4]=25;
ObstacleLocation[33][4]=25;
ObstacleLocation[34][4]=25;
ObstacleLocation[35][4]=25;
ObstacleLocation[36][4]=25;
ObstacleLocation[37][4]=25;
ObstacleLocation[38][4]=25;
ObstacleLocation[39][4]=25;
ObstacleLocation[40][4]=25;
ObstacleLocation[41][4]=25;
ObstacleLocation[42][4]=25;
ObstacleLocation[43][4]=25;
ObstacleLocation[44][4]=25;
ObstacleLocation[45][4]=25;
ObstacleLocation[46][4]=25;
ObstacleLocation[47][4]=25;
ObstacleLocation[48][4]=25;
ObstacleLocation[49][4]=25;
ObstacleLocation[50][4]=25;
ObstacleLocation[51][4]=25;
ObstacleLocation[52][4]=25;
ObstacleLocation[53][4]=25;
ObstacleLocation[54][4]=25;
ObstacleLocation[55][4]=25;
ObstacleLocation[56][4]=25;
ObstacleLocation[57][4]=25;
ObstacleLocation[58][4]=25;
ObstacleLocation[59][4]=25;
ObstacleLocation[60][4]=25;
ObstacleLocation[61][4]=25;
ObstacleLocation[62][4]=25;
ObstacleLocation[63][4]=25;
ObstacleLocation[64][4]=25;
ObstacleLocation[65][4]=25;
ObstacleLocation[66][4]=25;
ObstacleLocation[67][4]=25;
ObstacleLocation[68][4]=25;
ObstacleLocation[69][4]=25;
ObstacleLocation[70][4]=25;
ObstacleLocation[71][4]=25;
ObstacleLocation[72][4]=25;
ObstacleLocation[73][4]=25;
ObstacleLocation[74][4]=25;
ObstacleLocation[75][4]=25;
ObstacleLocation[76][4]=25;
ObstacleLocation[77][4]=25;
ObstacleLocation[78][4]=25;
ObstacleLocation[79][4]=25;
ObstacleLocation[80][4]=25;
ObstacleLocation[81][4]=25;
ObstacleLocation[82][4]=25;
ObstacleLocation[83][4]=25;
ObstacleLocation[84][4]=25;
ObstacleLocation[85][4]=25;
ObstacleLocation[86][4]=25;
ObstacleLocation[87][4]=25;
ObstacleLocation[88][4]=25;
ObstacleLocation[89][4]=25;
ObstacleLocation[90][4]=25;
ObstacleLocation[91][4]=25;
ObstacleLocation[92][4]=25;
ObstacleLocation[93][4]=25;
ObstacleLocation[94][4]=25;
ObstacleLocation[95][4]=25;
ObstacleLocation[96][4]=25;
ObstacleLocation[97][4]=25;
ObstacleLocation[98][4]=25;
ObstacleLocation[99][4]=25;
ObstacleLocation[100][4]=25;

				break;
			case 7:
				clearLevel(6);
				loopvar = 1;

				do{
					lvl_bldgs[loopvar] = 30;
					loopvar++;
				}while(loopvar <= lvl_num_sectors);

				CurrentSidewalk[0] = dirt;
				CurrentSidewalk[1] = dirt;
				CurrentStreet[0] = dirt;
				CurrentStreet[1] = dirt;

				CurrentRamps[0] = Ramps[2];
				CurrentRamps[1] = Ramps[3];

				ObstacleLocation[16][1]=5;
				ObstacleLocation[17][1]=21;
				ObstacleLocation[18][1]=1;
				ObstacleLocation[19][1]=8;
				ObstacleLocation[23][1]=1;
				ObstacleLocation[35][1]=21;
				ObstacleLocation[39][1]=21;
				ObstacleLocation[43][1]=6;
				ObstacleLocation[47][1]=21;
				ObstacleLocation[55][1]=8;
				ObstacleLocation[61][1]=7;
				ObstacleLocation[69][1]=21;
				ObstacleLocation[73][1]=21;
				ObstacleLocation[77][1]=5;
	
				ObstacleLocation[16][2]=7;
				ObstacleLocation[27][2]=21;
				ObstacleLocation[31][2]=1;
				ObstacleLocation[43][2]=8;
				ObstacleLocation[56][2]=8;
				ObstacleLocation[61][2]=8;
				ObstacleLocation[73][2]=21;
				ObstacleLocation[77][2]=8;
				
				ObstacleLocation[16][3]=6;
				ObstacleLocation[20][3]=9;
				ObstacleLocation[21][3]=2;	
				ObstacleLocation[23][3]=7;
				ObstacleLocation[31][3]=1;
				ObstacleLocation[39][3]=5;
				ObstacleLocation[47][3]=6;
				ObstacleLocation[55][3]=8;
				ObstacleLocation[59][3]=17;
				ObstacleLocation[65][3]=1;
				ObstacleLocation[67][3]=21;
				ObstacleLocation[69][3]=14;
				ObstacleLocation[75][3]=13;
				
				ObstacleLocation[16][4]=8;
				ObstacleLocation[17][4]=12;
				ObstacleLocation[18][4]=16;
				ObstacleLocation[19][4]=3;
				ObstacleLocation[23][4]=8;
				ObstacleLocation[35][4]=1;
				ObstacleLocation[39][4]=8;
				ObstacleLocation[43][4]=3;
				ObstacleLocation[47][4]=8;
				ObstacleLocation[63][4]=3;
				ObstacleLocation[65][4]=3;
				ObstacleLocation[67][4]=1;
				ObstacleLocation[75][4]=21;
				ObstacleLocation[77][4]=3;
				
				RampLocation[15][1]=2;
				RampLocation[27][1]=2;
				RampLocation[31][1]=2;
				RampLocation[51][1]=2;
				RampLocation[71][1]=2;
				RampLocation[78][1]=2;
		
				RampLocation[15][2]=2;
				RampLocation[19][2]=2;
				RampLocation[23][2]=2;
				RampLocation[35][2]=2;
				RampLocation[39][2]=2;
				RampLocation[47][2]=2;
				RampLocation[51][2]=2;
				RampLocation[57][2]=2;
				RampLocation[65][2]=2;
				RampLocation[67][2]=1;
				RampLocation[69][2]=2;
				RampLocation[75][2]=1;
				
				RampLocation[15][3]=2;
				RampLocation[18][3]=2;
				RampLocation[27][3]=2;
				RampLocation[35][3]=2;
				RampLocation[43][3]=2;
				RampLocation[51][3]=2;
				RampLocation[61][3]=2;
				RampLocation[63][3]=2;
				RampLocation[73][3]=2;
		
				RampLocation[15][4]=2;
				RampLocation[31][4]=2;
				RampLocation[55][4]=2;
				RampLocation[59][4]=2;
			
				break;
			case 8:
				clearLevel(7);

ObstacleLocation[4][1]=20;
ObstacleLocation[6][1]=20;
ObstacleLocation[12][1]=20;
ObstacleLocation[13][1]=20;
ObstacleLocation[16][1]=20;
ObstacleLocation[19][1]=20;
ObstacleLocation[22][1]=20;
ObstacleLocation[25][1]=20;
ObstacleLocation[28][1]=20;
ObstacleLocation[29][1]=20;
ObstacleLocation[30][1]=20;
ObstacleLocation[35][1]=20;
ObstacleLocation[36][1]=20;
ObstacleLocation[37][1]=20;
ObstacleLocation[39][1]=20;
ObstacleLocation[40][1]=20;
ObstacleLocation[41][1]=20;
ObstacleLocation[43][1]=20;
ObstacleLocation[44][1]=20;
ObstacleLocation[45][1]=20;
ObstacleLocation[47][1]=20;
ObstacleLocation[48][1]=20;
ObstacleLocation[49][1]=20;
ObstacleLocation[50][1]=20;
ObstacleLocation[54][1]=20;
ObstacleLocation[56][1]=20;
ObstacleLocation[62][1]=20;
ObstacleLocation[63][1]=20;
ObstacleLocation[66][1]=20;
ObstacleLocation[69][1]=20;
ObstacleLocation[72][1]=20;
ObstacleLocation[75][1]=20;
ObstacleLocation[78][1]=20;
ObstacleLocation[79][1]=20;
ObstacleLocation[80][1]=20;
ObstacleLocation[85][1]=20;
ObstacleLocation[87][1]=20;
ObstacleLocation[89][1]=20;
ObstacleLocation[90][1]=20;
ObstacleLocation[91][1]=20;
ObstacleLocation[93][1]=20;
ObstacleLocation[94][1]=20;
ObstacleLocation[95][1]=20;
ObstacleLocation[97][1]=20;
ObstacleLocation[98][1]=20;
ObstacleLocation[99][1]=20;
ObstacleLocation[100][1]=20;
ObstacleLocation[5][2]=20;
ObstacleLocation[8][2]=20;
ObstacleLocation[10][2]=20;
ObstacleLocation[15][2]=20;
ObstacleLocation[16][2]=20;
ObstacleLocation[21][2]=20;
ObstacleLocation[23][2]=20;
ObstacleLocation[26][2]=20;
ObstacleLocation[29][2]=20;
ObstacleLocation[30][2]=20;
ObstacleLocation[32][2]=20;
ObstacleLocation[33][2]=20;
ObstacleLocation[34][2]=20;
ObstacleLocation[35][2]=20;
ObstacleLocation[37][2]=20;
ObstacleLocation[38][2]=20;
ObstacleLocation[40][2]=20;
ObstacleLocation[41][2]=20;
ObstacleLocation[42][2]=20;
ObstacleLocation[45][2]=20;
ObstacleLocation[46][2]=20;
ObstacleLocation[55][2]=20;
ObstacleLocation[58][2]=20;
ObstacleLocation[60][2]=20;
ObstacleLocation[65][2]=20;
ObstacleLocation[66][2]=20;
ObstacleLocation[71][2]=20;
ObstacleLocation[73][2]=20;
ObstacleLocation[76][2]=20;
ObstacleLocation[79][2]=20;
ObstacleLocation[80][2]=20;
ObstacleLocation[82][2]=20;
ObstacleLocation[83][2]=20;
ObstacleLocation[84][2]=20;
ObstacleLocation[85][2]=20;
ObstacleLocation[87][2]=20;
ObstacleLocation[88][2]=20;
ObstacleLocation[90][2]=20;
ObstacleLocation[92][2]=20;
ObstacleLocation[95][2]=20;
ObstacleLocation[96][2]=20;
ObstacleLocation[6][3]=20;
ObstacleLocation[9][3]=20;
ObstacleLocation[12][3]=20;
ObstacleLocation[13][3]=20;
ObstacleLocation[16][3]=20;
ObstacleLocation[20][3]=20;
ObstacleLocation[23][3]=20;
ObstacleLocation[26][3]=20;
ObstacleLocation[27][3]=20;
ObstacleLocation[36][3]=20;
ObstacleLocation[40][3]=20;
ObstacleLocation[44][3]=20;
ObstacleLocation[48][3]=20;
ObstacleLocation[56][3]=20;
ObstacleLocation[59][3]=20;
ObstacleLocation[62][3]=20;
ObstacleLocation[63][3]=20;
ObstacleLocation[66][3]=20;
ObstacleLocation[70][3]=20;
ObstacleLocation[73][3]=20;
ObstacleLocation[76][3]=20;
ObstacleLocation[77][3]=20;
ObstacleLocation[86][3]=20;
ObstacleLocation[90][3]=20;
ObstacleLocation[94][3]=20;
ObstacleLocation[98][3]=20;
ObstacleLocation[4][4]=20;
ObstacleLocation[8][4]=20;
ObstacleLocation[10][4]=20;
ObstacleLocation[13][4]=20;
ObstacleLocation[16][4]=20;
ObstacleLocation[17][4]=20;
ObstacleLocation[22][4]=20;
ObstacleLocation[25][4]=20;
ObstacleLocation[29][4]=20;
ObstacleLocation[38][4]=20;
ObstacleLocation[42][4]=20;
ObstacleLocation[46][4]=20;
ObstacleLocation[50][4]=20;
ObstacleLocation[54][4]=20;
ObstacleLocation[58][4]=20;
ObstacleLocation[60][4]=20;
ObstacleLocation[63][4]=20;
ObstacleLocation[66][4]=20;
ObstacleLocation[67][4]=20;
ObstacleLocation[72][4]=20;
ObstacleLocation[75][4]=20;
ObstacleLocation[79][4]=20;
ObstacleLocation[88][4]=20;
ObstacleLocation[92][4]=20;
ObstacleLocation[96][4]=20;
ObstacleLocation[100][4]=20;
RampLocation[34][1]=2;
RampLocation[38][1]=2;
RampLocation[42][1]=2;
RampLocation[46][1]=2;
RampLocation[84][1]=2;
RampLocation[88][1]=2;
RampLocation[92][1]=2;
RampLocation[96][1]=2;
RampLocation[50][2]=2;
RampLocation[100][2]=2;

				break;
			case 9:
				clearLevel(8);

				loopvar = 1;
				loopvar2 = 35;

				do{
					lvl_bldgs[loopvar] = loopvar2;
					loopvar++;
					loopvar2++;
					if(loopvar2 > 42)
						loopvar2 = 35;
				}while(loopvar <= lvl_num_sectors);

				CurrentSidewalk[0] = Street[2];
				CurrentSidewalk[1] = Street[3];
				CurrentStreet[0] = Street[4];
				CurrentStreet[1] = Street[4];

				RampLocation[7][1] = 2;
				RampLocation[7][2] = 1;
				RampLocation[34][4] = 2;
				RampLocation[48][1] = 2;
				RampLocation[55][2] = 1;
				RampLocation[85][1] = 2;
				RampLocation[86][2] = 2;
				RampLocation[87][3] = 2;
				RampLocation[88][4] = 2;

				ObstacleLocation[5][3] = 34;
				ObstacleLocation[8][2] = 3;
				ObstacleLocation[10][3] = 42;
				ObstacleLocation[10][4] = 37;
				ObstacleLocation[11][1] = 44;
				ObstacleLocation[11][2] = 39;
				ObstacleLocation[11][3] = 38;
				ObstacleLocation[11][4] = 41;
				ObstacleLocation[12][1] = 40;
				ObstacleLocation[12][2] = 43;
				ObstacleLocation[15][4] = 31;
				ObstacleLocation[17][2] = 33;
				ObstacleLocation[18][1] = 28;
				ObstacleLocation[19][1] = 29;
				ObstacleLocation[19][3] = 35;
				ObstacleLocation[22][3] = 42;
				ObstacleLocation[22][4] = 37;
				ObstacleLocation[23][1] = 44;
				ObstacleLocation[23][2] = 39;
				ObstacleLocation[23][3] = 38;
				ObstacleLocation[23][4] = 41;
				ObstacleLocation[24][1] = 40;
				ObstacleLocation[24][2] = 43;
				ObstacleLocation[27][1] = 16;
				ObstacleLocation[27][4] = 36;
				ObstacleLocation[28][3] = 27;
				ObstacleLocation[30][2] = 14;
				ObstacleLocation[32][3] = 9;
				ObstacleLocation[33][1] = 32;
				ObstacleLocation[34][2] = 30;
				ObstacleLocation[36][3] = 42;
				ObstacleLocation[36][4] = 37;
				ObstacleLocation[37][1] = 44;
				ObstacleLocation[37][2] = 39;
				ObstacleLocation[37][3] = 38;
				ObstacleLocation[37][4] = 41;
				ObstacleLocation[38][1] = 40;
				ObstacleLocation[38][2] = 43;
				ObstacleLocation[38][4] = 13;
				ObstacleLocation[41][2] = 20;
				ObstacleLocation[43][1] = 28;
				ObstacleLocation[44][1] = 29;
				ObstacleLocation[44][3] = 20;
				ObstacleLocation[47][3] = 35;
				ObstacleLocation[49][1] = 28;
				ObstacleLocation[50][1] = 29;
				ObstacleLocation[51][1] = 3;
				ObstacleLocation[55][3] = 36;
				ObstacleLocation[56][2] = 14;
				ObstacleLocation[57][4] = 30;
				ObstacleLocation[59][1] = 28;
				ObstacleLocation[60][1] = 29;
				ObstacleLocation[60][3] = 31;
				ObstacleLocation[62][4] = 34;
				ObstacleLocation[63][2] = 34;
				ObstacleLocation[66][4] = 33;
				ObstacleLocation[67][2] = 32;
				ObstacleLocation[69][3] = 42;
				ObstacleLocation[69][4] = 37;
				ObstacleLocation[70][1] = 44;
				ObstacleLocation[70][2] = 39;
				ObstacleLocation[70][3] = 38;
				ObstacleLocation[70][4] = 41;
				ObstacleLocation[71][1] = 40;
				ObstacleLocation[71][2] = 43;
				ObstacleLocation[73][4] = 30;
				ObstacleLocation[74][1] = 28;
				ObstacleLocation[74][3] = 31;
				ObstacleLocation[75][1] = 29;
				ObstacleLocation[76][2] = 11;
				ObstacleLocation[77][4] = 33;
				ObstacleLocation[79][3] = 35;
				ObstacleLocation[81][1] = 36;
				ObstacleLocation[81][4] = 33;
				ObstacleLocation[82][2] = 27;
				ObstacleLocation[83][3] = 34;
				ObstacleLocation[83][4] = 26;
				ObstacleLocation[86][1] = 20;
				ObstacleLocation[87][1] = 20;
				ObstacleLocation[87][2] = 20;
				ObstacleLocation[88][1] = 20;
				ObstacleLocation[88][2] = 20;
				ObstacleLocation[88][3] = 20;
				ObstacleLocation[89][1] = 20;
				ObstacleLocation[89][2] = 20;
				ObstacleLocation[89][3] = 20;
				ObstacleLocation[89][4] = 20;
				ObstacleLocation[90][1] = 20;
				ObstacleLocation[90][2] = 20;
				ObstacleLocation[90][3] = 20;
				ObstacleLocation[90][4] = 20;
				ObstacleLocation[91][1] = 20;
				ObstacleLocation[91][2] = 20;
				ObstacleLocation[91][3] = 20;
				ObstacleLocation[91][4] = 20;
				ObstacleLocation[94][3] = 42;
				ObstacleLocation[94][4] = 37;
				ObstacleLocation[95][1] = 44;
				ObstacleLocation[95][2] = 39;
				ObstacleLocation[95][3] = 38;
				ObstacleLocation[95][4] = 41;
				ObstacleLocation[96][1] = 40;
				ObstacleLocation[96][2] = 43;
				ObstacleLocation[98][3] = 35;
				ObstacleLocation[99][1] = 32;

				break;
			case 10:
				clearLevel(9);

				loopvar = 1;
				loopvar2 = 35;

				do{
					lvl_bldgs[loopvar] = loopvar2;
					loopvar++;
					loopvar2++;
					if(loopvar2 > 42)
						loopvar2 = 35;
				}while(loopvar <= lvl_num_sectors);

				CurrentSidewalk[0] = Street[2];
				CurrentSidewalk[1] = Street[3];
				CurrentStreet[0] = Street[4];
				CurrentStreet[1] = Street[4];

ObstacleLocation[3][1]=31;
ObstacleLocation[4][3]=32;
ObstacleLocation[6][1]=28;
ObstacleLocation[6][4]=30;
ObstacleLocation[7][1]=29;
ObstacleLocation[7][2]=32;

ObstacleLocation[10][3]=42;
ObstacleLocation[10][4]=37;
ObstacleLocation[11][1]=44;
ObstacleLocation[11][2]=39;
ObstacleLocation[11][3]=38;
ObstacleLocation[11][4]=41;
ObstacleLocation[12][1]=40;
ObstacleLocation[12][2]=43;


ObstacleLocation[14][1]=28;
ObstacleLocation[14][4]=36;
ObstacleLocation[15][1]=29;
ObstacleLocation[15][3]=35;
ObstacleLocation[17][1]=33;
ObstacleLocation[17][2]=36;
ObstacleLocation[17][4]=33;

ObstacleLocation[20][3]=42;
ObstacleLocation[20][4]=37;
ObstacleLocation[21][1]=44;
ObstacleLocation[21][2]=39;
ObstacleLocation[21][3]=38;
ObstacleLocation[21][4]=41;
ObstacleLocation[22][1]=40;
ObstacleLocation[22][2]=43;


ObstacleLocation[24][2]=32;
ObstacleLocation[25][3]=35;
ObstacleLocation[26][1]=36;
ObstacleLocation[26][4]=30;
ObstacleLocation[27][2]=32;
ObstacleLocation[27][3]=27;

ObstacleLocation[39][3]=42;
ObstacleLocation[39][4]=37;
ObstacleLocation[40][1]=44;
ObstacleLocation[40][2]=39;
ObstacleLocation[40][3]=38;
ObstacleLocation[40][4]=41;
ObstacleLocation[41][1]=40;
ObstacleLocation[41][2]=43;


ObstacleLocation[33][4]=31;
ObstacleLocation[34][2]=31;
ObstacleLocation[34][3]=36;
ObstacleLocation[35][1]=28;
ObstacleLocation[36][1]=29;
ObstacleLocation[36][4]=31;
ObstacleLocation[37][3]=35;
ObstacleLocation[38][2]=31;
ObstacleLocation[40][1]=31;
ObstacleLocation[41][4]=27;

ObstacleLocation[44][3]=42;
ObstacleLocation[44][4]=37;
ObstacleLocation[45][1]=44;
ObstacleLocation[45][2]=39;
ObstacleLocation[45][3]=38;
ObstacleLocation[45][4]=41;
ObstacleLocation[46][1]=40;
ObstacleLocation[46][2]=43;


ObstacleLocation[43][1]=34;
ObstacleLocation[44][1]=34;
ObstacleLocation[46][3]=35;
ObstacleLocation[47][2]=36;
ObstacleLocation[47][4]=34;
ObstacleLocation[48][1]=28;
ObstacleLocation[48][4]=34;
ObstacleLocation[49][1]=29;
ObstacleLocation[49][2]=32;
ObstacleLocation[49][3]=32;

ObstacleLocation[54][3]=42;
ObstacleLocation[54][4]=37;
ObstacleLocation[55][1]=44;
ObstacleLocation[55][2]=39;
ObstacleLocation[55][3]=38;
ObstacleLocation[55][4]=41;
ObstacleLocation[56][1]=40;
ObstacleLocation[56][2]=43;


ObstacleLocation[58][1]=36;
ObstacleLocation[58][4]=30;
ObstacleLocation[59][3]=35;
ObstacleLocation[59][4]=30;
ObstacleLocation[61][3]=30;
ObstacleLocation[62][1]=28;
ObstacleLocation[62][2]=30;
ObstacleLocation[63][1]=29;

ObstacleLocation[64][3]=42;
ObstacleLocation[64][4]=37;
ObstacleLocation[65][1]=44;
ObstacleLocation[65][2]=39;
ObstacleLocation[65][3]=38;
ObstacleLocation[65][4]=41;
ObstacleLocation[66][1]=40;
ObstacleLocation[66][2]=43;


ObstacleLocation[68][4]=36;
ObstacleLocation[69][1]=31;
ObstacleLocation[69][2]=35;
ObstacleLocation[70][4]=32;
ObstacleLocation[71][1]=32;
ObstacleLocation[71][3]=32;
ObstacleLocation[72][2]=32;
ObstacleLocation[73][3]=35;
ObstacleLocation[73][4]=31;
ObstacleLocation[74][1]=36;

ObstacleLocation[76][3]=42;
ObstacleLocation[76][4]=37;
ObstacleLocation[77][1]=44;
ObstacleLocation[77][2]=39;
ObstacleLocation[77][3]=38;
ObstacleLocation[77][4]=41;
ObstacleLocation[78][1]=40;
ObstacleLocation[78][2]=43;


ObstacleLocation[79][3]=31;
ObstacleLocation[80][1]=28;
ObstacleLocation[80][4]=31;
ObstacleLocation[81][1]=29;
ObstacleLocation[82][3]=35;
ObstacleLocation[83][1]=32;
ObstacleLocation[84][2]=32;
ObstacleLocation[84][4]=36;
ObstacleLocation[86][1]=31;
ObstacleLocation[86][3]=32;
ObstacleLocation[87][2]=35;
ObstacleLocation[88][4]=32;
ObstacleLocation[89][1]=28;
ObstacleLocation[89][3]=32;
ObstacleLocation[90][1]=29;

ObstacleLocation[91][3]=42;
ObstacleLocation[91][4]=37;
ObstacleLocation[92][1]=44;
ObstacleLocation[92][2]=39;
ObstacleLocation[92][3]=38;
ObstacleLocation[92][4]=41;
ObstacleLocation[93][1]=40;
ObstacleLocation[93][2]=43;


ObstacleLocation[95][1]=30;
ObstacleLocation[95][3]=35;
ObstacleLocation[95][4]=30;
ObstacleLocation[97][1]=31;
ObstacleLocation[97][2]=36;
ObstacleLocation[97][4]=30;
ObstacleLocation[98][1]=31;
ObstacleLocation[99][4]=30;
ObstacleLocation[101][1]=31;

ObstacleLocation[103][3]=42;
ObstacleLocation[103][4]=37;
ObstacleLocation[104][1]=44;
ObstacleLocation[104][2]=39;
ObstacleLocation[104][3]=38;
ObstacleLocation[104][4]=41;
ObstacleLocation[105][1]=40;
ObstacleLocation[105][2]=43;


ObstacleLocation[106][4]=33;
ObstacleLocation[108][1]=28;
ObstacleLocation[108][4]=33;
ObstacleLocation[109][1]=29;
ObstacleLocation[109][2]=36;
ObstacleLocation[109][3]=35;
ObstacleLocation[111][1]=31;
ObstacleLocation[111][4]=33;
ObstacleLocation[112][2]=33;

ObstacleLocation[114][3]=42;
ObstacleLocation[114][4]=37;
ObstacleLocation[115][1]=44;
ObstacleLocation[115][2]=39;
ObstacleLocation[115][3]=38;
ObstacleLocation[115][4]=41;
ObstacleLocation[116][1]=40;
ObstacleLocation[116][2]=43;


ObstacleLocation[117][4]=34;
ObstacleLocation[118][1]=34;
ObstacleLocation[118][4]=34;
ObstacleLocation[119][1]=34;
ObstacleLocation[120][2]=35;
ObstacleLocation[121][3]=36;
ObstacleLocation[122][1]=34;

ObstacleLocation[124][3]=42;
ObstacleLocation[124][4]=37;
ObstacleLocation[125][1]=44;
ObstacleLocation[125][2]=39;
ObstacleLocation[125][3]=38;
ObstacleLocation[125][4]=41;
ObstacleLocation[126][1]=40;
ObstacleLocation[126][2]=43;


ObstacleLocation[128][2]=30;
ObstacleLocation[129][1]=28;
ObstacleLocation[129][3]=35;
ObstacleLocation[130][1]=29;
ObstacleLocation[130][2]=30;
ObstacleLocation[131][2]=30;
ObstacleLocation[131][4]=34;
ObstacleLocation[132][1]=30;

ObstacleLocation[134][3]=42;
ObstacleLocation[134][4]=37;
ObstacleLocation[135][1]=44;
ObstacleLocation[135][2]=39;
ObstacleLocation[135][3]=38;
ObstacleLocation[135][4]=41;
ObstacleLocation[136][1]=40;
ObstacleLocation[136][2]=43;


ObstacleLocation[137][4]=34;
ObstacleLocation[138][2]=32;
ObstacleLocation[139][3]=35;
ObstacleLocation[140][1]=32;
ObstacleLocation[141][4]=32;
ObstacleLocation[142][3]=32;
ObstacleLocation[143][1]=34;

ObstacleLocation[144][3]=42;
ObstacleLocation[144][4]=37;
ObstacleLocation[145][1]=44;
ObstacleLocation[145][2]=39;
ObstacleLocation[145][3]=38;
ObstacleLocation[145][4]=41;
ObstacleLocation[146][1]=40;
ObstacleLocation[146][2]=43;


ObstacleLocation[148][1]=36;
ObstacleLocation[148][4]=34;
ObstacleLocation[149][2]=31;
ObstacleLocation[149][3]=31;
ObstacleLocation[152][1]=31;

ObstacleLocation[153][3]=42;
ObstacleLocation[153][4]=37;
ObstacleLocation[154][1]=44;
ObstacleLocation[154][2]=39;
ObstacleLocation[154][3]=38;
ObstacleLocation[154][4]=41;
ObstacleLocation[155][1]=40;
ObstacleLocation[155][2]=43;


ObstacleLocation[156][4]=33;
ObstacleLocation[157][1]=33;
ObstacleLocation[158][3]=35;
ObstacleLocation[159][1]=28;
ObstacleLocation[160][1]=29;
ObstacleLocation[160][4]=33;
ObstacleLocation[161][2]=33;

ObstacleLocation[163][3]=42;
ObstacleLocation[163][4]=37;
ObstacleLocation[164][1]=44;
ObstacleLocation[164][2]=39;
ObstacleLocation[164][3]=38;
ObstacleLocation[164][4]=41;
ObstacleLocation[165][1]=40;
ObstacleLocation[165][2]=43;


ObstacleLocation[166][4]=30;
ObstacleLocation[167][1]=36;
ObstacleLocation[168][2]=34;
ObstacleLocation[169][3]=30;
ObstacleLocation[169][1]=35;
ObstacleLocation[170][2]=34;
ObstacleLocation[171][4]=30;
ObstacleLocation[172][1]=28;
ObstacleLocation[172][2]=31;
ObstacleLocation[172][3]=31;
ObstacleLocation[173][1]=29;


ObstacleLocation[174][3]=42;
ObstacleLocation[174][4]=37;
ObstacleLocation[175][1]=44;
ObstacleLocation[175][2]=39;
ObstacleLocation[175][3]=38;
ObstacleLocation[175][4]=41;
ObstacleLocation[176][1]=40;
ObstacleLocation[176][2]=43;


ObstacleLocation[178][1]=34;
ObstacleLocation[178][2]=34;
ObstacleLocation[178][4]=36;
ObstacleLocation[180][1]=28;
ObstacleLocation[181][1]=29;
ObstacleLocation[181][2]=35;
ObstacleLocation[182][3]=31;
ObstacleLocation[183][1]=34;
ObstacleLocation[183][2]=34;

ObstacleLocation[185][3]=42;
ObstacleLocation[185][4]=37;
ObstacleLocation[186][1]=44;
ObstacleLocation[186][2]=39;
ObstacleLocation[186][3]=38;
ObstacleLocation[186][4]=41;
ObstacleLocation[187][1]=40;
ObstacleLocation[187][2]=43;


ObstacleLocation[189][2]=33;
ObstacleLocation[190][1]=28;
ObstacleLocation[190][4]=36;
ObstacleLocation[191][1]=29;
ObstacleLocation[191][2]=33;

ObstacleLocation[193][3]=42;
ObstacleLocation[193][4]=37;
ObstacleLocation[194][1]=44;
ObstacleLocation[194][2]=39;
ObstacleLocation[194][3]=38;
ObstacleLocation[194][4]=41;
ObstacleLocation[195][1]=40;
ObstacleLocation[195][2]=43;


ObstacleLocation[198][3]=42;
ObstacleLocation[198][4]=37;
ObstacleLocation[199][1]=44;
ObstacleLocation[199][2]=39;
ObstacleLocation[199][3]=38;
ObstacleLocation[199][4]=41;
ObstacleLocation[200][1]=40;
ObstacleLocation[200][2]=43;

				break;
		}
	}

	public void destroy(){
		g2.dispose();
		g3.dispose();
	}

	public void clearLevel(int levelnum){
		int a = 1, b = 1;

		do{
			do{
				ObstacleLocation[a][b] = 0;
				RampLocation[a][b] = 0;
				b++;
			}while(b < 6);
			
			b = 1;
			a++;
		}while(a < 201);
		
/*		switch(levelnum){
			case 1:
				RampLocation[10][2] = 0;
				ObstacleLocation[11][2] = 0;
				ObstacleLocation[12][2] = 0;
				ObstacleLocation[13][2] = 0;
				
				ObstacleLocation[6][3] = 0;
	
				RampLocation[15][1] = 0;

				ObstacleLocation[17][1] = 0;
				ObstacleLocation[18][4] = 0;
				
				RampLocation[20][4] = 0;
                                RampLocation[21][4] = 0;

				ObstacleLocation[27][1] = 0;
				ObstacleLocation[40][1] = 0;
				ObstacleLocation[47][1] = 0;
				ObstacleLocation[57][1] = 0;
				ObstacleLocation[94][1] = 0;
	
				ObstacleLocation[22][4] = 0;
				ObstacleLocation[23][4] = 0;
				ObstacleLocation[24][4] = 0;

				ObstacleLocation[22][3] = 0;

				ObstacleLocation[35][1] = 0;
				ObstacleLocation[36][1] = 0;
				ObstacleLocation[35][2] = 0;
				ObstacleLocation[36][2] = 0;
				ObstacleLocation[35][4] = 0;
				ObstacleLocation[36][4] = 0;

				ObstacleLocation[43][3] = 0;
				ObstacleLocation[44][3] = 0;

				ObstacleLocation[54][1] = 0;
				ObstacleLocation[54][3] = 0;
				ObstacleLocation[54][4] = 0;
				
				ObstacleLocation[61][2] = 0;
				ObstacleLocation[62][3] = 0;
				ObstacleLocation[64][1] = 0;
				ObstacleLocation[69][4] = 0;

				RampLocation[65][1] = 0;
				ObstacleLocation[66][1] = 0;
				

				RampLocation[70][1] = 0;
				RampLocation[71][2] = 0;
				RampLocation[72][3] = 0;
				RampLocation[71][4] = 0;

				ObstacleLocation[71][1] = 0;
				ObstacleLocation[72][1] = 0;
				ObstacleLocation[73][1] = 0;
				ObstacleLocation[74][1] = 0;

				ObstacleLocation[72][2] = 0;
				ObstacleLocation[73][2] = 0;
				ObstacleLocation[74][2] = 0;


				ObstacleLocation[74][4] = 0;	
				RampLocation[75][4] = 0;	

				RampLocation[87][1] = 0;
				RampLocation[88][2] = 0;
				RampLocation[89][3] = 0;
				RampLocation[87][4] = 0;

				ObstacleLocation[91][1] = 0;	
				ObstacleLocation[91][2] = 0;	
				ObstacleLocation[91][3] = 0;	
				ObstacleLocation[91][4] = 0;	


				ObstacleLocation[99][1] = 0;	
				ObstacleLocation[99][2] = 0;	
				ObstacleLocation[99][3] = 0;	
				ObstacleLocation[99][4] = 0;					
				break;

			case 2:
				ObstacleLocation[10][1]=0;
				ObstacleLocation[11][1]=0;
				ObstacleLocation[12][1]=0; 
				ObstacleLocation[12][2]=0;

				ObstacleLocation[12][4]=0;
				ObstacleLocation[14][2]=0;
				ObstacleLocation[15][2]=0;

				RampLocation[15][3]=0;

				ObstacleLocation[15][4]=0;
				ObstacleLocation[16][4]=0;
				ObstacleLocation[17][4]=0;
				ObstacleLocation[19][3]=0;

				ObstacleLocation[19][1]=0;
				ObstacleLocation[21][1]=0;
				ObstacleLocation[21][2]=0;
				RampLocation[21][3]=0;
				ObstacleLocation[22][3]=0;
				ObstacleLocation[23][3]=0;
				ObstacleLocation[24][3]=0;
				ObstacleLocation[23][4]=0;

				ObstacleLocation[25][4]=0;
				ObstacleLocation[25][1]=0;
				ObstacleLocation[26][3]=0;
				ObstacleLocation[27][4]=0;

				ObstacleLocation[31][2]=0;
				ObstacleLocation[30][2]=0;

				ObstacleLocation[34][1] = 0;
				RampLocation[35][2]=0;
				ObstacleLocation[36][1] = 0;
				ObstacleLocation[36][2] = 0;
				ObstacleLocation[37][2] = 0;
				ObstacleLocation[34][4] = 0;
				ObstacleLocation[37][4] = 0;

				ObstacleLocation[40][1]=0;
				ObstacleLocation[43][1]=0;
				ObstacleLocation[44][1]=0;
				ObstacleLocation[45][1]=0; 
				ObstacleLocation[44][2]=0;

				ObstacleLocation[44][4]=0;
				ObstacleLocation[44][2]=0;
				ObstacleLocation[45][2]=0;

				RampLocation[43][3]=0;

				ObstacleLocation[48][4]=0;
				ObstacleLocation[46][4]=0;
				ObstacleLocation[49][4]=0;
				ObstacleLocation[50][3]=0;
			
				RampLocation[50][2]=0;
				ObstacleLocation[51][2]=0;
				ObstacleLocation[52][2]=0;
				ObstacleLocation[51][1]=0;
				ObstacleLocation[54][3]=0;
				ObstacleLocation[56][4]=0;
				ObstacleLocation[59][3]=0;
	                  ObstacleLocation[55][1]=0;

				RampLocation[60][1]=0;
				RampLocation[60][4]=0;
				ObstacleLocation[61][1]=0;
				ObstacleLocation[62][1]=0;
				ObstacleLocation[63][1]=0;
				ObstacleLocation[64][1]=0; 
				ObstacleLocation[64][2]=0;
				ObstacleLocation[65][2]=0;
				ObstacleLocation[66][2]=0;
				ObstacleLocation[67][1]=0;
				ObstacleLocation[71][3]=0;
				ObstacleLocation[72][4]=0;
				ObstacleLocation[72][3]=0;
	                  ObstacleLocation[70][1]=0;

				ObstacleLocation[75][4]=0;
				ObstacleLocation[75][1]=0;
				ObstacleLocation[76][3]=0;
				ObstacleLocation[77][4]=0;

				ObstacleLocation[81][2]=0;
				ObstacleLocation[80][2]=0;

				ObstacleLocation[84][1] = 0;
				RampLocation[85][2]=0;
				ObstacleLocation[85][1]=0;
				ObstacleLocation[86][1] = 0;
				ObstacleLocation[86][2] = 0;
				ObstacleLocation[87][2] = 0;
				ObstacleLocation[84][4] = 0;
				ObstacleLocation[87][4] = 0;

				ObstacleLocation[90][1]=0;
				ObstacleLocation[93][1]=0;
				ObstacleLocation[94][1]=0;
				ObstacleLocation[95][1]=0; 
				ObstacleLocation[94][3]=0;

				RampLocation[60][1]=0;
				RampLocation[60][4]=0;
				RampLocation[68][4]=0;
				RampLocation[75][3]=0;
				RampLocation[82][1]=0;
				RampLocation[84][3]=0;
				RampLocation[90][2]=0;
				RampLocation[93][4]=0;
				RampLocation[94][2]=0;

				ObstacleLocation[99][1]=0;

				break;
			case 3:

				RampLocation[7][1] = 0;
				RampLocation[9][3] = 0;
				RampLocation[15][2] =0;
				RampLocation[15][4] = 0;
				RampLocation[19][1] = 0;
				RampLocation[19][3] = 0;
				RampLocation[38][1] = 0;
				RampLocation[38][4] = 0;
				RampLocation[39][4] = 0;
				RampLocation[43][3] = 0;
				RampLocation[45][1] = 0;
				RampLocation[45][2] = 0;
				RampLocation[45][3] = 0;
				RampLocation[45][4] = 0;
				RampLocation[55][1] = 0;
				RampLocation[55][2] = 0;
				RampLocation[55][4] = 0;
				RampLocation[59][1] = 0;
				RampLocation[59][2] = 0;
				RampLocation[59][3] = 0;
				RampLocation[59][4] = 0;
				RampLocation[63][1] = 0;
				RampLocation[63][2] = 0;
				RampLocation[63][3] = 0;
				RampLocation[67][2] = 0;
				RampLocation[82][1] = 0;
				RampLocation[82][2] = 0;
				RampLocation[82][3] = 0;
				RampLocation[82][4] = 0;
				ObstacleLocation[5][2] = 0;
				ObstacleLocation[7][4] = 0;
				ObstacleLocation[11][1] = 0;
				ObstacleLocation[13][1] = 0;
				ObstacleLocation[13][2] = 0;
				ObstacleLocation[13][3] = 0;
				ObstacleLocation[13][4] = 0;
				ObstacleLocation[14][1] = 0;
				ObstacleLocation[14][3] = 0;
				ObstacleLocation[16][3] = 0;
				ObstacleLocation[19][2] = 0;
				ObstacleLocation[19][4] = 0;
				ObstacleLocation[21][3] = 0;
				ObstacleLocation[23][1] = 0;
				ObstacleLocation[23][2] = 0;
				ObstacleLocation[23][4] = 0;
				ObstacleLocation[26][3] = 0;
				ObstacleLocation[28][1] = 0;
				ObstacleLocation[29][4] = 0;
				ObstacleLocation[30][3] = 0;
				ObstacleLocation[30][4] = 0;
				ObstacleLocation[31][3] = 0;
				ObstacleLocation[31][4] = 0;
				ObstacleLocation[32][4] = 0;
				ObstacleLocation[33][1] = 0;
				ObstacleLocation[35][3] = 0;
				ObstacleLocation[37][4] = 0;
				ObstacleLocation[40][1] = 0;
				ObstacleLocation[40][4] = 0;
				ObstacleLocation[43][4] = 0;
				ObstacleLocation[44][3] = 0;
				ObstacleLocation[44][4] = 0;
				ObstacleLocation[46][4] = 0;
				ObstacleLocation[47][3] = 0;
				ObstacleLocation[47][4] = 0;
				ObstacleLocation[48][4] = 0;
				ObstacleLocation[50][4] = 0;
				ObstacleLocation[52][4] = 0;
				ObstacleLocation[54][4] = 0;
				ObstacleLocation[55][4] = 0;
				ObstacleLocation[64][1] = 0;
				ObstacleLocation[64][4] = 0;
				ObstacleLocation[67][1] = 0;
				ObstacleLocation[67][3] = 0;
				ObstacleLocation[67][4] = 0;
				ObstacleLocation[69][1] = 0;
				ObstacleLocation[69][2] = 0;
				ObstacleLocation[69][3] = 0;
				ObstacleLocation[69][4] = 0;
				ObstacleLocation[72][1] = 0;
				ObstacleLocation[72][3] = 0;
				ObstacleLocation[72][4] = 0;
				ObstacleLocation[73][1] = 0;
				ObstacleLocation[73][3] = 0;
				ObstacleLocation[73][4] = 0;
				ObstacleLocation[74][1] = 0;
				ObstacleLocation[74][4] = 0;
				ObstacleLocation[75][1] = 0;
				ObstacleLocation[75][2] = 0;
				ObstacleLocation[75][4] = 0;
				ObstacleLocation[76][1] = 0;
				ObstacleLocation[76][2] = 0;
				ObstacleLocation[76][4] = 0;
				ObstacleLocation[77][1] = 0;
				ObstacleLocation[77][4] = 0;
				ObstacleLocation[78][1] = 0;
				ObstacleLocation[78][3] = 0;
				ObstacleLocation[78][4] = 0;
				ObstacleLocation[79][1] = 0;
				ObstacleLocation[79][2] = 0;
				ObstacleLocation[79][3] = 0;
				ObstacleLocation[79][4] = 0;
				ObstacleLocation[80][1] = 0;
				ObstacleLocation[80][2] = 0;
				ObstacleLocation[80][3] = 0;
				ObstacleLocation[80][4] = 0;
				ObstacleLocation[83][1] = 0;
				ObstacleLocation[83][2] = 0;
				ObstacleLocation[83][3] = 0;
				ObstacleLocation[83][4] = 0;
				ObstacleLocation[84][1] = 0;
				ObstacleLocation[84][4] = 0;
				ObstacleLocation[85][1] = 0;
				ObstacleLocation[85][2] = 0;
				ObstacleLocation[85][3] = 0;
				ObstacleLocation[85][4] = 0;
				ObstacleLocation[88][1] = 0;

				break;
			case 4:
				RampLocation[9][1] = 0;
				RampLocation[15][3] = 0;
				RampLocation[15][4] = 0;
				RampLocation[27][1] = 0;
				RampLocation[27][2] = 0;
				RampLocation[27][3] = 0;
				RampLocation[27][4] = 0;
				RampLocation[31][3] = 0;
				RampLocation[35][2] = 0;
				RampLocation[35][3] = 0;
				RampLocation[39][1] = 0;
				RampLocation[39][3] = 0;
				RampLocation[41][4] = 0;
				RampLocation[43][1] = 0;
				RampLocation[43][2] = 0;
				RampLocation[45][3] = 0;
				RampLocation[45][4] = 0;
				RampLocation[47][1] = 0;
				RampLocation[47][2] = 0;
				RampLocation[59][1] = 0;
				RampLocation[58][4] = 0;
				RampLocation[59][4] = 0;
				RampLocation[66][1] = 0;
				RampLocation[66][2] = 0;
				RampLocation[66][3] = 0;
				RampLocation[66][4] = 0;
				RampLocation[72][3] = 0;
				RampLocation[73][4] = 0;
				RampLocation[87][4] = 0;
				RampLocation[115][1] = 0;
				RampLocation[115][2] = 0;
				RampLocation[115][3] = 0;
				RampLocation[115][4] = 0;
				RampLocation[135][1] = 0;
				RampLocation[135][3] = 0;
				RampLocation[136][2] = 0;
				RampLocation[136][4] = 0;
				ObstacleLocation[7][3] = 0;
				ObstacleLocation[10][2]= 0;
				ObstacleLocation[11][4] = 0;
				ObstacleLocation[12][1] = 0;
				ObstacleLocation[12][3] = 0;
				ObstacleLocation[14][2] = 0;
				ObstacleLocation[17][3] = 0;
				ObstacleLocation[18][2] = 0;
				ObstacleLocation[19][1] = 0;
				ObstacleLocation[19][2] = 0;
				ObstacleLocation[19][3] = 0;
				ObstacleLocation[19][4] = 0;
				ObstacleLocation[22][2] = 0;
				ObstacleLocation[23][4] = 0;
				ObstacleLocation[24][1] = 0;
				ObstacleLocation[24][3] = 0;
				ObstacleLocation[28][1] = 0;
				ObstacleLocation[28][2] = 0;
				ObstacleLocation[28][3] = 0;
				ObstacleLocation[28][4] = 0;
				ObstacleLocation[29][3] = 0;
				ObstacleLocation[30][1] = 0;
				ObstacleLocation[30][3] = 0;
				ObstacleLocation[31][1] = 0;
				ObstacleLocation[31][2] = 0;
				ObstacleLocation[31][4] = 0;
				ObstacleLocation[32][2] = 0;
				ObstacleLocation[35][4] = 0;
				ObstacleLocation[39][2] = 0;
				ObstacleLocation[40][4] = 0;
				ObstacleLocation[42][3] = 0;
				ObstacleLocation[44][2] = 0;
				ObstacleLocation[46][3] = 0;
				ObstacleLocation[47][4] = 0;
				ObstacleLocation[48][3] = 0;
				ObstacleLocation[49][1] = 0;
				ObstacleLocation[50][2] = 0;
				ObstacleLocation[50][4] = 0;
				ObstacleLocation[52][2] = 0;
				ObstacleLocation[52][3] = 0;
				ObstacleLocation[54][1] = 0;
				ObstacleLocation[54][2] = 0;
				ObstacleLocation[54][3] = 0;
				ObstacleLocation[54][4] = 0;
				ObstacleLocation[58][1] = 0;
				ObstacleLocation[60][1] = 0;
				ObstacleLocation[60][2] = 0;
				ObstacleLocation[60][3] = 0;
				ObstacleLocation[60][4] = 0;
				ObstacleLocation[61][1] = 0;
				ObstacleLocation[61][4] = 0;
				ObstacleLocation[62][1] = 0;
				ObstacleLocation[62][3] = 0;
				ObstacleLocation[62][4] = 0;
				ObstacleLocation[68][3] = 0;
				ObstacleLocation[69][1] = 0;
				ObstacleLocation[69][4] = 0;
				ObstacleLocation[70][2] = 0;
				ObstacleLocation[73][1] = 0;
				ObstacleLocation[74][2] = 0;
				ObstacleLocation[74][4] = 0;
				ObstacleLocation[76][3] = 0;
				ObstacleLocation[78][1] = 0;
				ObstacleLocation[78][2] = 0;
				ObstacleLocation[78][3] = 0;
				ObstacleLocation[78][4] = 0;
				ObstacleLocation[81][1] = 0;
				ObstacleLocation[81][3] = 0;
				ObstacleLocation[82][4] = 0;
				ObstacleLocation[83][2] = 0;
				ObstacleLocation[83][4] = 0;
				ObstacleLocation[85][1] = 0;
				ObstacleLocation[86][3] = 0;
				ObstacleLocation[88][1] = 0;
				ObstacleLocation[89][2] = 0;
				ObstacleLocation[88][4] = 0;
				ObstacleLocation[89][4] = 0;
				ObstacleLocation[90][4] = 0;
				ObstacleLocation[91][1] = 0;
				ObstacleLocation[91][3] = 0;
				ObstacleLocation[93][4] = 0;
				ObstacleLocation[94][2] = 0;
				ObstacleLocation[96][1] = 0;
				ObstacleLocation[96][4] = 0;
				ObstacleLocation[97][3] = 0;
				ObstacleLocation[97][4] = 0;
				ObstacleLocation[100][1] = 0;
				ObstacleLocation[100][2] = 0;
				ObstacleLocation[100][3] = 0;
				ObstacleLocation[100][4] = 0;
				ObstacleLocation[101][1] = 0;
				ObstacleLocation[101][3] = 0;
				ObstacleLocation[101][4] = 0;
				ObstacleLocation[102][1] = 0;
				ObstacleLocation[102][4] = 0;
				ObstacleLocation[103][1] = 0;
				ObstacleLocation[103][2] = 0;
				ObstacleLocation[103][4] = 0;
				ObstacleLocation[104][1] = 0;
				ObstacleLocation[104][2] = 0;
				ObstacleLocation[104][4] = 0;
				ObstacleLocation[105][1] = 0;
				ObstacleLocation[105][4] = 0;
				ObstacleLocation[106][3] = 0;
				ObstacleLocation[106][4] = 0;
				ObstacleLocation[107][2] = 0;
				ObstacleLocation[107][3] = 0;
				ObstacleLocation[107][4] = 0;
				ObstacleLocation[108][2] = 0;
				ObstacleLocation[108][3] = 0;
				ObstacleLocation[108][4] = 0;
				ObstacleLocation[109][3] = 0;
				ObstacleLocation[110][1] = 0;
				ObstacleLocation[110][3] = 0;
				ObstacleLocation[111][1] = 0;
				ObstacleLocation[112][1] = 0;
				ObstacleLocation[112][2] = 0;
				ObstacleLocation[112][3] = 0;
				ObstacleLocation[112][4] = 0;
				ObstacleLocation[113][2] = 0;
				ObstacleLocation[113][4] = 0;
				ObstacleLocation[116][1] = 0;
				ObstacleLocation[116][2] = 0;
				ObstacleLocation[116][3] = 0;
				ObstacleLocation[116][4] = 0;
				ObstacleLocation[118][1] = 0;
				ObstacleLocation[118][3] = 0;
				ObstacleLocation[119][2] = 0;
				ObstacleLocation[119][4] = 0;
				ObstacleLocation[121][1] = 0;
				ObstacleLocation[121][3] = 0;
				ObstacleLocation[122][4] = 0;
				ObstacleLocation[124][1] = 0;
				ObstacleLocation[124][2] = 0;
				ObstacleLocation[124][3] = 0;
				ObstacleLocation[124][4] = 0;
				ObstacleLocation[127][1] = 0;
				ObstacleLocation[127][2] = 0;
				ObstacleLocation[127][3] = 0;
				ObstacleLocation[127][4] = 0;
				ObstacleLocation[128][3] = 0;
				ObstacleLocation[130][1] = 0;
				ObstacleLocation[130][2] = 0;
				ObstacleLocation[130][3] = 0;
				ObstacleLocation[130][4] = 0;
				ObstacleLocation[134][1] = 0;
				ObstacleLocation[134][3] = 0;
				ObstacleLocation[135][2] = 0;
				ObstacleLocation[135][4] = 0;
				ObstacleLocation[136][1] = 0;
				ObstacleLocation[137][1] = 0;
				ObstacleLocation[137][2] = 0;
				ObstacleLocation[137][3] = 0;
				ObstacleLocation[137][4] = 0;
				ObstacleLocation[138][1] = 0;
				ObstacleLocation[138][2] = 0;
				ObstacleLocation[138][3] = 0;
				ObstacleLocation[138][4] = 0;
				ObstacleLocation[142][1] = 0;
				ObstacleLocation[142][2] = 0;
				ObstacleLocation[142][3] = 0;
				ObstacleLocation[142][4] = 0;
				ObstacleLocation[144][2] = 0;
				ObstacleLocation[147][1] = 0;
				ObstacleLocation[147][4] = 0;
				ObstacleLocation[148][2] = 0;
				ObstacleLocation[148][3] = 0;
				ObstacleLocation[149][1] = 0;
				ObstacleLocation[149][4] = 0;

				break;
			case 5:

ObstacleLocation[7][1]=0;
ObstacleLocation[7][2]=0;
ObstacleLocation[8][3]=0;
RampLocation[8][2]=0;
ObstacleLocation[9][2]=0;
ObstacleLocation[9][3]=0;
ObstacleLocation[10][1]=0;
ObstacleLocation[11][3]=0;
ObstacleLocation[11][4]=0;
RampLocation[12][2]=0;
ObstacleLocation[12][1]=0;
ObstacleLocation[13][2]=0;
ObstacleLocation[13][4]=0;
ObstacleLocation[14][3]=0;
ObstacleLocation[14][1]=0;
RampLocation[14][2]=0;
ObstacleLocation[15][3]=0;
ObstacleLocation[16][4]=0;
RampLocation[16][1]=0;
ObstacleLocation[17][2]=0;
ObstacleLocation[17][3]=0;
ObstacleLocation[18][4]=0;
ObstacleLocation[19][2]=0;
ObstacleLocation[19][1]=0;
RampLocation[20][2]=0;
ObstacleLocation[20][3]=0;
ObstacleLocation[21][4]=0;
ObstacleLocation[22][2]=0;
RampLocation[22][3]=0;
ObstacleLocation[23][1]=0;
ObstacleLocation[23][2]=0;
RampLocation[23][3]=0;
ObstacleLocation[25][2]=0;
RampLocation[25][1]=0;
ObstacleLocation[26][4]=0;
ObstacleLocation[27][3]=0;
ObstacleLocation[27][2]=0;
ObstacleLocation[27][1]=0;
ObstacleLocation[28][4]=0;
ObstacleLocation[28][2]=0;
ObstacleLocation[28][1]=0;
RampLocation[29][2]=0;
ObstacleLocation[30][4]=0;
ObstacleLocation[31][3]=0;
ObstacleLocation[31][2]=0;
ObstacleLocation[32][3]=0;
ObstacleLocation[33][2]=0;
RampLocation[34][4]=0;
ObstacleLocation[34][1]=0;
ObstacleLocation[35][3]=0;
RampLocation[35][2]=0;
ObstacleLocation[36][4]=0;
ObstacleLocation[37][2]=0;
ObstacleLocation[37][3]=0;
ObstacleLocation[37][1]=0;
ObstacleLocation[38][4]=0;
ObstacleLocation[38][2]=0;
RampLocation[39][1]=0;
ObstacleLocation[39][4]=0;
ObstacleLocation[40][3]=0;
ObstacleLocation[40][1]=0;
ObstacleLocation[42][3]=0;
RampLocation[42][2]=0;
ObstacleLocation[42][4]=0;
ObstacleLocation[43][3]=0;
RampLocation[43][2]=0;
ObstacleLocation[44][1]=0;
ObstacleLocation[44][3]=0;
ObstacleLocation[45][2]=0;
ObstacleLocation[45][4]=0;
RampLocation[46][3]=0;
ObstacleLocation[46][4]=0;
ObstacleLocation[47][1]=0;
ObstacleLocation[47][2]=0;
RampLocation[48][3]=0;
ObstacleLocation[48][2]=0;
ObstacleLocation[49][1]=0;
ObstacleLocation[49][3]=0;
ObstacleLocation[50][4]=0;
ObstacleLocation[50][2]=0;
ObstacleLocation[51][2]=0;
RampLocation[51][4]=0;
ObstacleLocation[51][1]=0;
ObstacleLocation[52][3]=0;
ObstacleLocation[52][2]=0;
ObstacleLocation[53][2]=0;
RampLocation[54][4]=0;
ObstacleLocation[54][1]=0;
ObstacleLocation[56][3]=0;
ObstacleLocation[56][2]=0;
ObstacleLocation[57][3]=0;
ObstacleLocation[57][2]=0;
RampLocation[57][1]=0;
ObstacleLocation[58][4]=0;
ObstacleLocation[58][3]=0;
ObstacleLocation[59][4]=0;
ObstacleLocation[59][2]=0;
ObstacleLocation[60][1]=0;
RampLocation[60][2]=0;
RampLocation[61][1]=0;
ObstacleLocation[61][3]=0;
ObstacleLocation[62][2]=0;
ObstacleLocation[62][4]=0;
ObstacleLocation[63][2]=0;
ObstacleLocation[63][1]=0;
RampLocation[64][4]=0;
ObstacleLocation[64][3]=0;
ObstacleLocation[65][2]=0;
ObstacleLocation[65][1]=0;
RampLocation[65][3]=0;
ObstacleLocation[65][2]=0;
ObstacleLocation[66][4]=0;
ObstacleLocation[66][3]=0;
RampLocation[66][2]=0;
ObstacleLocation[67][1]=0;
RampLocation[68][1]=0;
ObstacleLocation[68][3]=0;
ObstacleLocation[69][3]=0;
ObstacleLocation[70][2]=0;
ObstacleLocation[70][1]=0;
RampLocation[71][2]=0;
ObstacleLocation[72][4]=0;
ObstacleLocation[72][1]=0;
ObstacleLocation[73][2]=0;
RampLocation[74][4]=0;
ObstacleLocation[75][3]=0;
ObstacleLocation[75][2]=0;
ObstacleLocation[76][1]=0;
RampLocation[76][3]=0;
ObstacleLocation[77][2]=0;
ObstacleLocation[78][1]=0;
RampLocation[78][2]=0;
ObstacleLocation[79][3]=0;
ObstacleLocation[79][4]=0;
ObstacleLocation[80][2]=0;
RampLocation[80][1]=0;
ObstacleLocation[81][2]=0;
ObstacleLocation[81][3]=0;
ObstacleLocation[82][4]=0;
ObstacleLocation[82][3]=0;
ObstacleLocation[82][2]=0;
ObstacleLocation[83][1]=0;
RampLocation[83][2]=0;
ObstacleLocation[84][3]=0;
ObstacleLocation[86][4]=0;
ObstacleLocation[87][3]=0;
ObstacleLocation[87][2]=0;
RampLocation[88][1]=0;
ObstacleLocation[90][3]=0;
ObstacleLocation[91][2]=0;
ObstacleLocation[93][4]=0;
RampLocation[93][3]=0;
ObstacleLocation[93][2]=0;
ObstacleLocation[93][1]=0;
RampLocation[94][3]=0;
ObstacleLocation[94][2]=0;
ObstacleLocation[94][1]=0;
ObstacleLocation[95][4]=0;
RampLocation[97][2]=0;
				break;
			case 6:


ObstacleLocation[6][2]=0;
ObstacleLocation[7][3]=0;
ObstacleLocation[8][1]=0;
ObstacleLocation[9][1]=0;
ObstacleLocation[9][2]=0;
ObstacleLocation[10][1]=0;
ObstacleLocation[11][3]=0;
ObstacleLocation[14][1]=0;
ObstacleLocation[14][3]=0;
ObstacleLocation[16][2]=0;
ObstacleLocation[18][3]=0;
ObstacleLocation[18][2]=0;
ObstacleLocation[18][1]=0;
ObstacleLocation[22][1]=0;
ObstacleLocation[22][2]=0;
ObstacleLocation[22][3]=0;
ObstacleLocation[23][1]=0;
ObstacleLocation[24][1]=0;
ObstacleLocation[24][2]=0;
ObstacleLocation[26][2]=0;
ObstacleLocation[26][3]=0;
ObstacleLocation[28][1]=0;
ObstacleLocation[29][3]=0;
ObstacleLocation[30][3]=0;
ObstacleLocation[31][2]=0;
ObstacleLocation[32][2]=0;
ObstacleLocation[34][1]=0;
ObstacleLocation[34][2]=0;
ObstacleLocation[35][1]=0;
ObstacleLocation[36][1]=0;
ObstacleLocation[36][3]=0;
ObstacleLocation[38][2]=0;
ObstacleLocation[39][3]=0;
ObstacleLocation[40][1]=0;
ObstacleLocation[41][3]=0;
ObstacleLocation[42][1]=0;
ObstacleLocation[42][2]=0;
ObstacleLocation[42][3]=0;
ObstacleLocation[45][1]=0;
ObstacleLocation[46][1]=0;
ObstacleLocation[46][2]=0;
ObstacleLocation[47][1]=0;
ObstacleLocation[47][2]=0;
ObstacleLocation[48][1]=0;
ObstacleLocation[48][3]=0;
ObstacleLocation[50][1]=0;
ObstacleLocation[51][2]=0;
ObstacleLocation[52][2]=0;
ObstacleLocation[54][3]=0;
ObstacleLocation[56][1]=0;
ObstacleLocation[57][1]=0;
ObstacleLocation[58][1]=0;
ObstacleLocation[58][3]=0;
ObstacleLocation[61][1]=0;
ObstacleLocation[61][2]=0;
ObstacleLocation[64][1]=0;
ObstacleLocation[65][1]=0;
ObstacleLocation[65][2]=0;
ObstacleLocation[66][1]=0;
ObstacleLocation[66][2]=0;
ObstacleLocation[67][1]=0;
ObstacleLocation[67][2]=0;
ObstacleLocation[68][1]=0;
ObstacleLocation[68][3]=0;
ObstacleLocation[69][1]=0;
ObstacleLocation[69][3]=0;
ObstacleLocation[70][1]=0;
ObstacleLocation[71][1]=0;
ObstacleLocation[71][3]=0;
ObstacleLocation[72][2]=0;
ObstacleLocation[72][3]=0;
ObstacleLocation[73][2]=0;
ObstacleLocation[73][3]=0;
ObstacleLocation[74][3]=0;
ObstacleLocation[75][1]=0;
ObstacleLocation[75][3]=0;
ObstacleLocation[76][1]=0;
ObstacleLocation[76][2]=0;
ObstacleLocation[76][3]=0;
ObstacleLocation[77][1]=0;
ObstacleLocation[77][3]=0;
ObstacleLocation[78][1]=0;
ObstacleLocation[78][3]=0;
ObstacleLocation[79][1]=0;
ObstacleLocation[79][2]=0;
ObstacleLocation[80][1]=0;
ObstacleLocation[80][2]=0;
ObstacleLocation[81][3]=0;
ObstacleLocation[82][1]=0;
ObstacleLocation[84][3]=0;
ObstacleLocation[85][1]=0;
ObstacleLocation[85][2]=0;
ObstacleLocation[86][1]=0;
ObstacleLocation[87][3]=0;
ObstacleLocation[88][1]=0;
ObstacleLocation[90][1]=0;
ObstacleLocation[90][3]=0;
ObstacleLocation[91][1]=0;
ObstacleLocation[91][2]=0;
ObstacleLocation[93][1]=0;
ObstacleLocation[95][1]=0;
ObstacleLocation[95][3]=0;
ObstacleLocation[97][3]=0;

ObstacleLocation[1][4]=0;
ObstacleLocation[2][4]=0;
ObstacleLocation[3][4]=0;
ObstacleLocation[4][4]=0;
ObstacleLocation[5][4]=0;
ObstacleLocation[6][4]=0;
ObstacleLocation[7][4]=0;
ObstacleLocation[8][4]=0;
ObstacleLocation[9][4]=0;
ObstacleLocation[10][4]=0;
ObstacleLocation[11][4]=0;
ObstacleLocation[12][4]=0;
ObstacleLocation[13][4]=0;
ObstacleLocation[14][4]=0;
ObstacleLocation[15][4]=0;
ObstacleLocation[16][4]=0;
ObstacleLocation[17][4]=0;
ObstacleLocation[18][4]=0;
ObstacleLocation[19][4]=0;
ObstacleLocation[20][4]=0;
ObstacleLocation[21][4]=0;
ObstacleLocation[22][4]=0;
ObstacleLocation[23][4]=0;
ObstacleLocation[24][4]=0;
ObstacleLocation[25][4]=0;
ObstacleLocation[26][4]=0;
ObstacleLocation[27][4]=0;
ObstacleLocation[28][4]=0;
ObstacleLocation[29][4]=0;
ObstacleLocation[30][4]=0;
ObstacleLocation[31][4]=0;
ObstacleLocation[32][4]=0;
ObstacleLocation[33][4]=0;
ObstacleLocation[34][4]=0;
ObstacleLocation[35][4]=0;
ObstacleLocation[36][4]=0;
ObstacleLocation[37][4]=0;
ObstacleLocation[38][4]=0;
ObstacleLocation[39][4]=0;
ObstacleLocation[40][4]=0;
ObstacleLocation[41][4]=0;
ObstacleLocation[42][4]=0;
ObstacleLocation[43][4]=0;
ObstacleLocation[44][4]=0;
ObstacleLocation[45][4]=0;
ObstacleLocation[46][4]=0;
ObstacleLocation[47][4]=0;
ObstacleLocation[48][4]=0;
ObstacleLocation[49][4]=0;
ObstacleLocation[50][4]=0;
ObstacleLocation[51][4]=0;
ObstacleLocation[52][4]=0;
ObstacleLocation[53][4]=0;
ObstacleLocation[54][4]=0;
ObstacleLocation[55][4]=0;
ObstacleLocation[56][4]=0;
ObstacleLocation[57][4]=0;
ObstacleLocation[58][4]=0;
ObstacleLocation[59][4]=0;
ObstacleLocation[60][4]=0;
ObstacleLocation[61][4]=0;
ObstacleLocation[62][4]=0;
ObstacleLocation[63][4]=0;
ObstacleLocation[64][4]=0;
ObstacleLocation[65][4]=0;
ObstacleLocation[66][4]=0;
ObstacleLocation[67][4]=0;
ObstacleLocation[68][4]=0;
ObstacleLocation[69][4]=0;
ObstacleLocation[70][4]=0;
ObstacleLocation[71][4]=0;
ObstacleLocation[72][4]=0;
ObstacleLocation[73][4]=0;
ObstacleLocation[74][4]=0;
ObstacleLocation[75][4]=0;
ObstacleLocation[76][4]=0;
ObstacleLocation[77][4]=0;
ObstacleLocation[78][4]=0;
ObstacleLocation[79][4]=0;
ObstacleLocation[80][4]=0;
ObstacleLocation[81][4]=0;
ObstacleLocation[82][4]=0;
ObstacleLocation[83][4]=0;
ObstacleLocation[84][4]=0;
ObstacleLocation[85][4]=0;
ObstacleLocation[86][4]=0;
ObstacleLocation[87][4]=0;
ObstacleLocation[88][4]=0;
ObstacleLocation[89][4]=0;
ObstacleLocation[90][4]=0;
ObstacleLocation[91][4]=0;
ObstacleLocation[92][4]=0;
ObstacleLocation[93][4]=0;
ObstacleLocation[94][4]=0;
ObstacleLocation[95][4]=0;
ObstacleLocation[96][4]=0;
ObstacleLocation[97][4]=0;
ObstacleLocation[98][4]=0;
ObstacleLocation[99][4]=0;
ObstacleLocation[100][4]=0;


				break;
			case 7:
				ObstacleLocation[16][1]=0;
				ObstacleLocation[17][1]=0;
				ObstacleLocation[18][1]=0;
				ObstacleLocation[19][1]=0;
				ObstacleLocation[23][1]=0;
				ObstacleLocation[35][1]=0;
				ObstacleLocation[39][1]=0;
				ObstacleLocation[43][1]=0;
				ObstacleLocation[47][1]=0;
				ObstacleLocation[55][1]=0;
				ObstacleLocation[61][1]=0;
				ObstacleLocation[69][1]=0;
				ObstacleLocation[73][1]=0;
				ObstacleLocation[77][1]=0;
				ObstacleLocation[16][2]=0;
				ObstacleLocation[27][2]=0;
				ObstacleLocation[31][2]=0;
				ObstacleLocation[43][2]=0;
				ObstacleLocation[56][2]=0;
				ObstacleLocation[61][2]=0;
				ObstacleLocation[73][2]=0;
				ObstacleLocation[77][2]=0;
				ObstacleLocation[16][3]=0;
				ObstacleLocation[20][3]=0;
				ObstacleLocation[21][3]=0;	
				ObstacleLocation[23][3]=0;
				ObstacleLocation[31][3]=0;
				ObstacleLocation[39][3]=0;
				ObstacleLocation[47][3]=0;
				ObstacleLocation[55][3]=0;
				ObstacleLocation[59][3]=0;
				ObstacleLocation[65][3]=0;
				ObstacleLocation[67][3]=0;
				ObstacleLocation[69][3]=0;
				ObstacleLocation[75][3]=0;
				ObstacleLocation[16][4]=0;
				ObstacleLocation[17][4]=0;
				ObstacleLocation[18][4]=0;
				ObstacleLocation[19][4]=0;
				ObstacleLocation[23][4]=0;
				ObstacleLocation[35][4]=0;
				ObstacleLocation[39][4]=0;
				ObstacleLocation[43][4]=0;
				ObstacleLocation[47][4]=0;
				ObstacleLocation[63][4]=0;
				ObstacleLocation[65][4]=0;
				ObstacleLocation[67][4]=0;
				ObstacleLocation[75][4]=0;
				ObstacleLocation[77][4]=0;
				RampLocation[15][1]=0;
				RampLocation[27][1]=0;
				RampLocation[31][1]=0;
				RampLocation[51][1]=0;
				RampLocation[71][1]=0;
				RampLocation[78][1]=0;
				RampLocation[15][2]=0;
				RampLocation[19][2]=0;
				RampLocation[23][2]=0;
				RampLocation[35][2]=0;
				RampLocation[39][2]=0;
				RampLocation[47][2]=0;
				RampLocation[51][2]=0;
				RampLocation[57][2]=0;
				RampLocation[65][2]=0;
				RampLocation[67][2]=0;
				RampLocation[69][2]=0;
				RampLocation[75][2]=0;
				RampLocation[15][3]=0;
				RampLocation[18][3]=0;
				RampLocation[27][3]=0;
				RampLocation[35][3]=0;
				RampLocation[43][3]=0;
				RampLocation[51][3]=0;
				RampLocation[61][3]=0;
				RampLocation[63][3]=0;
				RampLocation[73][3]=0;
				RampLocation[15][4]=0;
				RampLocation[31][4]=0;
				RampLocation[55][4]=0;
				RampLocation[59][4]=0;
				break;
			case 8:
ObstacleLocation[4][1]=0;
ObstacleLocation[6][1]=0;
ObstacleLocation[12][1]=0;
ObstacleLocation[13][1]=0;
ObstacleLocation[16][1]=0;
ObstacleLocation[19][1]=0;
ObstacleLocation[22][1]=0;
ObstacleLocation[25][1]=0;
ObstacleLocation[28][1]=0;
ObstacleLocation[29][1]=0;
ObstacleLocation[30][1]=0;
ObstacleLocation[35][1]=0;
ObstacleLocation[36][1]=0;
ObstacleLocation[37][1]=0;
ObstacleLocation[39][1]=0;
ObstacleLocation[40][1]=0;
ObstacleLocation[41][1]=0;
ObstacleLocation[43][1]=0;
ObstacleLocation[44][1]=0;
ObstacleLocation[45][1]=0;
ObstacleLocation[47][1]=0;
ObstacleLocation[48][1]=0;
ObstacleLocation[49][1]=0;
ObstacleLocation[50][1]=0;
ObstacleLocation[54][1]=0;
ObstacleLocation[56][1]=0;
ObstacleLocation[62][1]=0;
ObstacleLocation[63][1]=0;
ObstacleLocation[66][1]=0;
ObstacleLocation[69][1]=0;
ObstacleLocation[72][1]=0;
ObstacleLocation[75][1]=0;
ObstacleLocation[78][1]=0;
ObstacleLocation[79][1]=0;
ObstacleLocation[80][1]=0;
ObstacleLocation[85][1]=0;
ObstacleLocation[87][1]=0;
ObstacleLocation[89][1]=0;
ObstacleLocation[90][1]=0;
ObstacleLocation[91][1]=0;
ObstacleLocation[93][1]=0;
ObstacleLocation[94][1]=0;
ObstacleLocation[95][1]=0;
ObstacleLocation[97][1]=0;
ObstacleLocation[98][1]=0;
ObstacleLocation[99][1]=0;
ObstacleLocation[100][1]=0;
ObstacleLocation[5][2]=0;
ObstacleLocation[8][2]=0;
ObstacleLocation[10][2]=0;
ObstacleLocation[15][2]=0;
ObstacleLocation[16][2]=0;
ObstacleLocation[21][2]=0;
ObstacleLocation[23][2]=0;
ObstacleLocation[26][2]=0;
ObstacleLocation[29][2]=0;
ObstacleLocation[30][2]=0;
ObstacleLocation[32][2]=0;
ObstacleLocation[33][2]=0;
ObstacleLocation[34][2]=0;
ObstacleLocation[35][2]=0;
ObstacleLocation[37][2]=0;
ObstacleLocation[38][2]=0;
ObstacleLocation[40][2]=0;
ObstacleLocation[41][2]=0;
ObstacleLocation[42][2]=0;
ObstacleLocation[45][2]=0;
ObstacleLocation[46][2]=0;
ObstacleLocation[55][2]=0;
ObstacleLocation[58][2]=0;
ObstacleLocation[60][2]=0;
ObstacleLocation[65][2]=0;
ObstacleLocation[66][2]=0;
ObstacleLocation[71][2]=0;
ObstacleLocation[73][2]=0;
ObstacleLocation[76][2]=0;
ObstacleLocation[79][2]=0;
ObstacleLocation[80][2]=0;
ObstacleLocation[82][2]=0;
ObstacleLocation[83][2]=0;
ObstacleLocation[84][2]=0;
ObstacleLocation[85][2]=0;
ObstacleLocation[87][2]=0;
ObstacleLocation[88][2]=0;
ObstacleLocation[90][2]=0;
ObstacleLocation[92][2]=0;
ObstacleLocation[95][2]=0;
ObstacleLocation[96][2]=0;
ObstacleLocation[6][3]=0;
ObstacleLocation[9][3]=0;
ObstacleLocation[12][3]=0;
ObstacleLocation[13][3]=0;
ObstacleLocation[16][3]=0;
ObstacleLocation[20][3]=0;
ObstacleLocation[23][3]=0;
ObstacleLocation[26][3]=0;
ObstacleLocation[27][3]=0;
ObstacleLocation[36][3]=0;
ObstacleLocation[40][3]=0;
ObstacleLocation[44][3]=0;
ObstacleLocation[48][3]=0;
ObstacleLocation[56][3]=0;
ObstacleLocation[59][3]=0;
ObstacleLocation[62][3]=0;
ObstacleLocation[63][3]=0;
ObstacleLocation[66][3]=0;
ObstacleLocation[70][3]=0;
ObstacleLocation[73][3]=0;
ObstacleLocation[76][3]=0;
ObstacleLocation[77][3]=0;
ObstacleLocation[86][3]=0;
ObstacleLocation[90][3]=0;
ObstacleLocation[94][3]=0;
ObstacleLocation[98][3]=0;
ObstacleLocation[4][4]=0;
ObstacleLocation[8][4]=0;
ObstacleLocation[10][4]=0;
ObstacleLocation[13][4]=0;
ObstacleLocation[16][4]=0;
ObstacleLocation[17][4]=0;
ObstacleLocation[22][4]=0;
ObstacleLocation[25][4]=0;
ObstacleLocation[29][4]=0;
ObstacleLocation[38][4]=0;
ObstacleLocation[42][4]=0;
ObstacleLocation[46][4]=0;
ObstacleLocation[50][4]=0;
ObstacleLocation[54][4]=0;
ObstacleLocation[58][4]=0;
ObstacleLocation[60][4]=0;
ObstacleLocation[63][4]=0;
ObstacleLocation[66][4]=0;
ObstacleLocation[67][4]=0;
ObstacleLocation[72][4]=0;
ObstacleLocation[75][4]=0;
ObstacleLocation[79][4]=0;
ObstacleLocation[88][4]=0;
ObstacleLocation[92][4]=0;
ObstacleLocation[96][4]=0;
ObstacleLocation[100][4]=0;
RampLocation[34][1]=0;
RampLocation[38][1]=0;
RampLocation[42][1]=0;
RampLocation[46][1]=0;
RampLocation[84][1]=0;
RampLocation[88][1]=0;
RampLocation[92][1]=0;
RampLocation[96][1]=0;
RampLocation[50][2]=0;
RampLocation[100][2]=0;
				break;
			case 9:
				RampLocation[7][1] = 0;
				RampLocation[7][2] = 0;
				RampLocation[34][4] = 0;
				RampLocation[48][1] = 0;
				RampLocation[55][2] = 0;
				RampLocation[85][1] = 0;
				RampLocation[86][2] = 0;
				RampLocation[87][3] = 0;
				RampLocation[88][4] = 0;

				ObstacleLocation[5][3] = 0;
				ObstacleLocation[8][2] = 0;
				ObstacleLocation[10][3] = 0;
				ObstacleLocation[10][4] = 0;
				ObstacleLocation[11][1] = 0;
				ObstacleLocation[11][2] = 0;
				ObstacleLocation[11][3] = 0;
				ObstacleLocation[11][4] = 0;
				ObstacleLocation[12][1] = 0;
				ObstacleLocation[12][2] = 0;
				ObstacleLocation[15][4] = 0;
				ObstacleLocation[17][2] = 0;
				ObstacleLocation[18][1] = 0;
				ObstacleLocation[19][1] = 0;
				ObstacleLocation[19][3] = 0;
				ObstacleLocation[22][3] = 0;
				ObstacleLocation[22][4] = 0;
				ObstacleLocation[23][1] = 0;
				ObstacleLocation[23][2] = 0;
				ObstacleLocation[23][3] = 0;
				ObstacleLocation[23][4] = 0;
				ObstacleLocation[24][1] = 0;
				ObstacleLocation[24][2] = 0;
				ObstacleLocation[27][1] = 0;
				ObstacleLocation[27][4] = 0;
				ObstacleLocation[28][3] = 0;
				ObstacleLocation[30][2] = 0;
				ObstacleLocation[32][3] = 0;
				ObstacleLocation[33][1] = 0;
				ObstacleLocation[34][2] = 0;
				ObstacleLocation[36][3] = 0;
				ObstacleLocation[36][4] = 0;
				ObstacleLocation[37][1] = 0;
				ObstacleLocation[37][2] = 0;
				ObstacleLocation[37][3] = 0;
				ObstacleLocation[37][4] = 0;
				ObstacleLocation[38][1] = 0;
				ObstacleLocation[38][2] = 0;
				ObstacleLocation[38][4] = 0;
				ObstacleLocation[41][2] = 0;
				ObstacleLocation[43][1] = 0;
				ObstacleLocation[44][1] = 0;
				ObstacleLocation[44][3] = 0;
				ObstacleLocation[47][3] = 0;
				ObstacleLocation[49][1] = 0;
				ObstacleLocation[50][1] = 0;
				ObstacleLocation[51][1] = 0;
				ObstacleLocation[55][3] = 0;
				ObstacleLocation[56][2] = 0;
				ObstacleLocation[57][4] = 0;
				ObstacleLocation[59][1] = 0;
				ObstacleLocation[60][1] = 0;
				ObstacleLocation[60][3] = 0;
				ObstacleLocation[62][4] = 0;
				ObstacleLocation[63][2] = 0;
				ObstacleLocation[66][4] = 0;
				ObstacleLocation[67][2] = 0;
				ObstacleLocation[69][3] = 0;
				ObstacleLocation[69][4] = 0;
				ObstacleLocation[70][1] = 0;
				ObstacleLocation[70][2] = 0;
				ObstacleLocation[70][3] = 0;
				ObstacleLocation[70][4] = 0;
				ObstacleLocation[71][1] = 0;
				ObstacleLocation[71][2] = 0;
				ObstacleLocation[73][4] = 0;
				ObstacleLocation[74][1] = 0;
				ObstacleLocation[74][3] = 0;
				ObstacleLocation[75][1] = 0;
				ObstacleLocation[76][2] = 0;
				ObstacleLocation[77][4] = 0;
				ObstacleLocation[79][3] = 0;
				ObstacleLocation[81][1] = 0;
				ObstacleLocation[81][4] = 0;
				ObstacleLocation[82][2] = 0;
				ObstacleLocation[83][3] = 0;
				ObstacleLocation[83][4] = 0;
				ObstacleLocation[86][1] = 0;
				ObstacleLocation[87][1] = 0;
				ObstacleLocation[87][2] = 0;
				ObstacleLocation[88][1] = 0;
				ObstacleLocation[88][2] = 0;
				ObstacleLocation[88][3] = 0;
				ObstacleLocation[89][1] = 0;
				ObstacleLocation[89][2] = 0;
				ObstacleLocation[89][3] = 0;
				ObstacleLocation[89][4] = 0;
				ObstacleLocation[90][1] = 0;
				ObstacleLocation[90][2] = 0;
				ObstacleLocation[90][3] = 0;
				ObstacleLocation[90][4] = 0;
				ObstacleLocation[91][1] = 0;
				ObstacleLocation[91][2] = 0;
				ObstacleLocation[91][3] = 0;
				ObstacleLocation[91][4] = 0;
				ObstacleLocation[94][3] = 0;
				ObstacleLocation[94][4] = 0;
				ObstacleLocation[95][1] = 0;
				ObstacleLocation[95][2] = 0;
				ObstacleLocation[95][3] = 0;
				ObstacleLocation[95][4] = 0;
				ObstacleLocation[96][1] = 0;
				ObstacleLocation[96][2] = 0;
				ObstacleLocation[98][3] = 0;
				ObstacleLocation[99][1] = 0;

				break;
			case 10:

ObstacleLocation[3][1]=0;
ObstacleLocation[4][3]=0;
ObstacleLocation[6][3]=0;
ObstacleLocation[6][4]=0;
ObstacleLocation[7][1]=0;
ObstacleLocation[7][2]=0;
ObstacleLocation[10][3]=0;
ObstacleLocation[10][4]=0;
ObstacleLocation[11][1]=0;
ObstacleLocation[11][2]=0;
ObstacleLocation[11][3]=0;
ObstacleLocation[11][4]=0;
ObstacleLocation[12][1]=0;
ObstacleLocation[12][2]=0;
ObstacleLocation[14][1]=0;
ObstacleLocation[14][4]=0;
ObstacleLocation[15][1]=0;
ObstacleLocation[15][3]=0;
ObstacleLocation[17][1]=0;
ObstacleLocation[17][2]=0;
ObstacleLocation[17][4]=0;
ObstacleLocation[20][3]=0;
ObstacleLocation[20][4]=0;
ObstacleLocation[21][1]=0;
ObstacleLocation[21][2]=0;
ObstacleLocation[21][3]=0;
ObstacleLocation[21][4]=0;
ObstacleLocation[22][1]=0;
ObstacleLocation[22][2]=0;
ObstacleLocation[24][2]=0;
ObstacleLocation[25][3]=0;
ObstacleLocation[26][1]=0;
ObstacleLocation[26][4]=0;
ObstacleLocation[27][2]=0;
ObstacleLocation[27][3]=0;
ObstacleLocation[39][3]=0;
ObstacleLocation[39][4]=0;
ObstacleLocation[40][1]=0;
ObstacleLocation[40][2]=0;
ObstacleLocation[40][3]=0;
ObstacleLocation[40][4]=0;
ObstacleLocation[41][1]=0;
ObstacleLocation[41][2]=0;
ObstacleLocation[33][4]=0;
ObstacleLocation[34][2]=0;
ObstacleLocation[34][3]=0;
ObstacleLocation[35][1]=0;
ObstacleLocation[36][1]=0;
ObstacleLocation[36][4]=0;
ObstacleLocation[37][3]=0;
ObstacleLocation[38][2]=0;
ObstacleLocation[40][1]=0;
ObstacleLocation[40][3]=0;
ObstacleLocation[44][3]=0;
ObstacleLocation[44][4]=0;
ObstacleLocation[45][1]=0;
ObstacleLocation[45][2]=0;
ObstacleLocation[45][3]=0;
ObstacleLocation[45][4]=0;
ObstacleLocation[46][1]=0;
ObstacleLocation[46][2]=0;
ObstacleLocation[45][1]=0;
ObstacleLocation[46][1]=0;
ObstacleLocation[46][3]=0;
ObstacleLocation[47][2]=0;
ObstacleLocation[47][4]=0;
ObstacleLocation[48][1]=0;
ObstacleLocation[48][4]=0;
ObstacleLocation[49][1]=0;
ObstacleLocation[49][2]=0;
ObstacleLocation[49][3]=0;
ObstacleLocation[54][3]=0;
ObstacleLocation[54][4]=0;
ObstacleLocation[55][1]=0;
ObstacleLocation[55][2]=0;
ObstacleLocation[55][3]=0;
ObstacleLocation[55][4]=0;
ObstacleLocation[56][1]=0;
ObstacleLocation[56][2]=0;
ObstacleLocation[58][1]=0;
ObstacleLocation[58][4]=0;
ObstacleLocation[59][3]=0;
ObstacleLocation[59][4]=0;
ObstacleLocation[61][3]=0;
ObstacleLocation[62][1]=0;
ObstacleLocation[62][2]=0;
ObstacleLocation[63][1]=0;
ObstacleLocation[64][3]=0;
ObstacleLocation[64][4]=0;
ObstacleLocation[65][1]=0;
ObstacleLocation[65][2]=0;
ObstacleLocation[65][3]=0;
ObstacleLocation[65][4]=0;
ObstacleLocation[66][1]=0;
ObstacleLocation[66][2]=0;
ObstacleLocation[68][4]=0;
ObstacleLocation[69][1]=0;
ObstacleLocation[69][2]=0;
ObstacleLocation[70][4]=0;
ObstacleLocation[71][1]=0;
ObstacleLocation[71][3]=0;
ObstacleLocation[72][2]=0;
ObstacleLocation[73][3]=0;
ObstacleLocation[73][4]=0;
ObstacleLocation[74][1]=0;
ObstacleLocation[76][3]=0;
ObstacleLocation[76][4]=0;
ObstacleLocation[77][1]=0;
ObstacleLocation[77][2]=0;
ObstacleLocation[77][3]=0;
ObstacleLocation[77][4]=0;
ObstacleLocation[78][1]=0;
ObstacleLocation[78][2]=0;
ObstacleLocation[79][3]=0;
ObstacleLocation[80][1]=0;
ObstacleLocation[80][4]=0;
ObstacleLocation[81][4]=0;
ObstacleLocation[82][3]=0;
ObstacleLocation[83][1]=0;
ObstacleLocation[84][2]=0;
ObstacleLocation[84][4]=0;
ObstacleLocation[86][1]=0;
ObstacleLocation[86][3]=0;
ObstacleLocation[87][2]=0;
ObstacleLocation[88][4]=0;
ObstacleLocation[89][1]=0;
ObstacleLocation[89][3]=0;
ObstacleLocation[90][1]=0;
ObstacleLocation[91][3]=0;
ObstacleLocation[91][4]=0;
ObstacleLocation[92][1]=0;
ObstacleLocation[92][2]=0;
ObstacleLocation[92][3]=0;
ObstacleLocation[92][4]=0;
ObstacleLocation[93][1]=0;
ObstacleLocation[93][2]=0;
ObstacleLocation[95][1]=0;
ObstacleLocation[95][3]=0;
ObstacleLocation[95][4]=0;
ObstacleLocation[97][1]=0;
ObstacleLocation[97][2]=0;
ObstacleLocation[97][4]=0;
ObstacleLocation[98][1]=0;
ObstacleLocation[99][4]=0;
ObstacleLocation[101][1]=0;
ObstacleLocation[103][3]=0;
ObstacleLocation[103][4]=0;
ObstacleLocation[104][1]=0;
ObstacleLocation[104][2]=0;
ObstacleLocation[104][3]=0;
ObstacleLocation[104][4]=0;
ObstacleLocation[105][1]=0;
ObstacleLocation[105][2]=0;
ObstacleLocation[106][4]=0;
ObstacleLocation[108][1]=0;
ObstacleLocation[108][4]=0;
ObstacleLocation[109][1]=0;
ObstacleLocation[109][2]=0;
ObstacleLocation[109][3]=0;
ObstacleLocation[111][1]=0;
ObstacleLocation[111][4]=0;
ObstacleLocation[112][2]=0;
ObstacleLocation[114][3]=0;
ObstacleLocation[114][4]=0;
ObstacleLocation[115][1]=0;
ObstacleLocation[115][2]=0;
ObstacleLocation[115][3]=0;
ObstacleLocation[115][4]=0;
ObstacleLocation[116][1]=0;
ObstacleLocation[116][2]=0;
ObstacleLocation[117][4]=0;
ObstacleLocation[118][1]=0;
ObstacleLocation[118][4]=0;
ObstacleLocation[119][1]=0;
ObstacleLocation[120][2]=0;
ObstacleLocation[121][3]=0;
ObstacleLocation[122][1]=0;
ObstacleLocation[124][3]=0;
ObstacleLocation[124][4]=0;
ObstacleLocation[125][1]=0;
ObstacleLocation[125][2]=0;
ObstacleLocation[125][3]=0;
ObstacleLocation[125][4]=0;
ObstacleLocation[126][1]=0;
ObstacleLocation[126][2]=0;
ObstacleLocation[128][2]=0;
ObstacleLocation[129][1]=0;
ObstacleLocation[129][3]=0;
ObstacleLocation[130][1]=0;
ObstacleLocation[130][2]=0;
ObstacleLocation[131][2]=0;
ObstacleLocation[131][4]=0;
ObstacleLocation[132][1]=0;
ObstacleLocation[134][3]=0;
ObstacleLocation[134][4]=0;
ObstacleLocation[135][1]=0;
ObstacleLocation[135][2]=0;
ObstacleLocation[135][3]=0;
ObstacleLocation[135][4]=0;
ObstacleLocation[136][1]=0;
ObstacleLocation[136][2]=0;
ObstacleLocation[137][4]=0;
ObstacleLocation[138][2]=0;
ObstacleLocation[139][3]=0;
ObstacleLocation[140][1]=0;
ObstacleLocation[141][4]=0;
ObstacleLocation[142][3]=0;
ObstacleLocation[143][1]=0;
ObstacleLocation[144][3]=0;
ObstacleLocation[144][4]=0;
ObstacleLocation[145][1]=0;
ObstacleLocation[145][2]=0;
ObstacleLocation[145][3]=0;
ObstacleLocation[145][4]=0;
ObstacleLocation[146][1]=0;
ObstacleLocation[146][2]=0;
ObstacleLocation[148][1]=0;
ObstacleLocation[148][4]=0;
ObstacleLocation[149][2]=0;
ObstacleLocation[149][3]=0;
ObstacleLocation[152][1]=0;
ObstacleLocation[153][3]=0;
ObstacleLocation[153][4]=0;
ObstacleLocation[154][1]=0;
ObstacleLocation[154][2]=0;
ObstacleLocation[154][3]=0;
ObstacleLocation[154][4]=0;
ObstacleLocation[155][1]=0;
ObstacleLocation[155][2]=0;
ObstacleLocation[156][4]=0;
ObstacleLocation[157][1]=0;
ObstacleLocation[158][3]=0;
ObstacleLocation[159][1]=0;
ObstacleLocation[160][1]=0;
ObstacleLocation[160][4]=0;
ObstacleLocation[161][2]=0;
ObstacleLocation[163][3]=0;
ObstacleLocation[163][4]=0;
ObstacleLocation[164][1]=0;
ObstacleLocation[164][2]=0;
ObstacleLocation[164][3]=0;
ObstacleLocation[164][4]=0;
ObstacleLocation[165][1]=0;
ObstacleLocation[165][2]=0;
ObstacleLocation[166][4]=0;
ObstacleLocation[167][1]=0;
ObstacleLocation[168][2]=0;
ObstacleLocation[169][3]=0;
ObstacleLocation[169][1]=0;
ObstacleLocation[170][2]=0;
ObstacleLocation[171][4]=0;
ObstacleLocation[172][1]=0;
ObstacleLocation[172][2]=0;
ObstacleLocation[172][3]=0;
ObstacleLocation[173][1]=0;
ObstacleLocation[174][3]=0;
ObstacleLocation[174][4]=0;
ObstacleLocation[175][1]=0;
ObstacleLocation[175][2]=0;
ObstacleLocation[175][3]=0;
ObstacleLocation[175][4]=0;
ObstacleLocation[176][1]=0;
ObstacleLocation[176][2]=0;
ObstacleLocation[178][1]=0;
ObstacleLocation[178][2]=0;
ObstacleLocation[178][4]=0;
ObstacleLocation[180][1]=0;
ObstacleLocation[181][1]=0;
ObstacleLocation[181][1]=0;
ObstacleLocation[182][3]=0;
ObstacleLocation[183][1]=0;
ObstacleLocation[183][2]=0;
ObstacleLocation[185][3]=0;
ObstacleLocation[185][4]=0;
ObstacleLocation[186][1]=0;
ObstacleLocation[186][2]=0;
ObstacleLocation[186][3]=0;
ObstacleLocation[186][4]=0;
ObstacleLocation[187][1]=0;
ObstacleLocation[187][2]=0;
ObstacleLocation[189][2]=0;
ObstacleLocation[190][1]=0;
ObstacleLocation[190][4]=0;
ObstacleLocation[191][1]=0;
ObstacleLocation[191][2]=0;
ObstacleLocation[193][3]=0;
ObstacleLocation[193][4]=0;
ObstacleLocation[194][1]=0;
ObstacleLocation[194][2]=0;
ObstacleLocation[194][3]=0;
ObstacleLocation[194][4]=0;
ObstacleLocation[195][1]=0;
ObstacleLocation[195][2]=0;
ObstacleLocation[198][3]=0;
ObstacleLocation[198][4]=0;
ObstacleLocation[199][1]=0;
ObstacleLocation[199][2]=0;
ObstacleLocation[199][3]=0;
ObstacleLocation[199][4]=0;
ObstacleLocation[200][1]=0;
ObstacleLocation[200][2]=0;
				break;
		}
		*/
	}
	
	public void showLevelComplete(){
		if(current_level < 10){
			g3.drawImage(levelComplete, 0, 0, this);
			tempimg = offimg2;
			x = y = 0;
			repaint();
		}
		else{
			g3.drawImage(youWin, 0, 0, this);
			tempimg = offimg2;
			x = y = 0;
			repaint();
		}
	}

	public void redrawDisplay(){
		g3.drawImage(displayimage, 0+panX, 241, this);
		g3.setColor(Color.white);
		g3.setFont(f);			

		g3.drawString("Level: "+current_level+"    Damage: "+current_damage+"%    Score: "+current_score, 30 + panX, 265);
	}

	public void showLoading(){
		tempimg = loadingScreen;
		x = y = 0;
		repaint();
	}

	public void clearOffscn(){
		g2.setColor(Color.white);
		g3.setColor(Color.white);

		g2.fillRect(0, 0, 360, 300);
		g3.fillRect(0, 0, 360, 300);
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
		g.clipRect(0, 0, 300, 280);
		paint(g);
	}

	public void paint(Graphics g){
		if(tempimg != null)
			g.drawImage(tempimg, x, 0, this);
	
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

	public String getAppletInfo(){
		return "Street Rider v1.5 Copyright 1999 J&E Bicycles  Author: Jared Stehler";
	}
}
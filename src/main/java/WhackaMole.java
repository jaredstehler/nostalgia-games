/*
	Project Blue
	Copyright © 2000 JE Bikes.

	Version: 0.80.950A -> Alpha Version
*/

import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.util.Random;

public class WhackaMole extends Applet implements Runnable{
	final int GAME_TIME = 30000;

	boolean DONE = false, START_GAME = false;

	int score, num_misses;

	boolean IS_UP[] = new boolean[10];
	int		num_up  = 0;

	int shovelX = 0, shovelY = 0, shovelimg = 6;

	boolean CLICK = false, CHANGED = false, SHOVEL_CHANGED = false;
	int clicked_sect = 0;
	int WAITING = 0;

	Image images[] = new Image[8];
	int imgnum[] = new int[10];

	Image title;

	AudioClip sndHit;
	
	int trackernum[] = new int[10];

	long game_start;
	long start_time[] = new long[10];
	long end_time, total_time;

	Random rnd = new Random();

	Font smlF = new Font("TimesRoman", Font.PLAIN, 14);
	Font f = new Font("TimesRoman", Font.BOLD, 18);
	Font bigF = new Font("TimesRoman", Font.BOLD, 28);

	Image offimg, offimg2, tempimg;
	Graphics g2, g3;

	private MediaTracker tracker;

	Thread runner;

	public void init(){
		offimg = createImage(300, 300);
		offimg2 = createImage(300, 330);

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


		int rndnum, tempnum = 0;

		long prev_time = 0;

		g3.setFont(smlF);
		g3.setColor(Color.white);
		g3.fillRect(0, 0, 300, 330);
		g3.setColor(Color.black);
		g3.drawString("Loading Game Images.....", 65, 150);
		tempimg = offimg2;
		repaint();

		g3.setFont(f);
		
		sndHit = getAudioClip(getCodeBase(), "whackamole/rat_hit.au");

		title = getImage(getCodeBase(), "whackamole/title.jpg");
		images[6] = getImage(getCodeBase(), "whackamole/shovel.gif");
		images[7] = getImage(getCodeBase(), "whackamole/shovel_hit.gif");

		images[4] = getImage(getCodeBase(), "whackamole/getready.gif");
		images[5] = getImage(getCodeBase(), "whackamole/go.gif");
		
		images[0] = getImage(getCodeBase(), "whackamole/mole_bg.jpg");
		images[1] = getImage(getCodeBase(), "whackamole/mole_out_dirt.jpg");
		images[2] = getImage(getCodeBase(), "whackamole/mole_out_hit.jpg");

		tracker = new MediaTracker(this);	
		
		tracker.addImage(title, 0);
		tracker.addImage(images[6], 1);
		tracker.addImage(images[7], 2);

		tracker.addImage(images[4], 3);
		tracker.addImage(images[5], 4);
		
		tracker.addImage(images[0], 5);
		tracker.addImage(images[1], 6);
		tracker.addImage(images[2], 7);

		int percent_complete = 0, img_num = 0;
		int increment = (int)(100 / 8);
		
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
			g3.drawRect(75, 170, 100, 8);
			g3.fillRect(75, 170, percent_complete, 8);

			tempimg = offimg2;
			repaint();

			img_num++;
		}

		// Infinite loop

		do{
			//...
			getAppletContext().showStatus("http://www.jebikes.com");

			for(int i = 0; i < 10; i++){
				imgnum[i] = 3;
				trackernum[i] = 0;
				IS_UP[i] = false;
			}

			START_GAME = DONE = CLICK = false;
			CHANGED = true;
			score = num_misses = num_up = 0;

		do{
			g3.drawImage(title, 0, 0, this);
			tempimg = offimg2;
			repaint();
		}while(!START_GAME);

/////////// Main Game Loop //////////////////////////
	
		g2.drawImage(images[0], 0, 0, this);

		g3.drawImage(offimg, 0, 0, this);
		g3.drawImage(images[4], 65, 137, this);
		refreshDisp(GAME_TIME / 1000);

		tempimg = offimg2;
		repaint();

		pause(1000);

		g3.drawImage(offimg, 0, 0, this);
		g3.drawImage(images[5], 114, 133, this);
		refreshDisp(GAME_TIME / 1000);

		tempimg = offimg2;
		repaint();

		pause(1000);


		game_start = System.currentTimeMillis() + GAME_TIME;

		do{
			//...
			if(num_up < 4 && WAITING == 0){
				rndnum = (int)(rnd.nextDouble() * 10);
			
				if(rndnum < 7){
					for(int i = 1; i < (rndnum / 3); i++){
						tempnum = getNewSect();

						IS_UP[tempnum] = true;
						imgnum[tempnum] = 1;
						start_time[tempnum] = System.currentTimeMillis();
						num_up++;
					}
					WAITING++;
					refresh();
				}
			}

			if(WAITING > 0){
				WAITING++;
			
				if(WAITING > 10)
					WAITING = 0;
			}
			
			end_time = System.currentTimeMillis();

			for(int i = 1; i < 10; i++){
				if(end_time - start_time[i] > 1000 && IS_UP[i]){
					IS_UP[i] = false;
					imgnum[tempnum] = 3;
					num_up--;
					num_misses++;

					g3.drawImage(offimg, 0, 0, this);
					refresh();
				}
			}

			if(CLICK && IS_UP[clicked_sect]){
				sndHit.play();
				
				IS_UP[clicked_sect] = CLICK = false;
				num_up--;
				imgnum[clicked_sect] = 2;

				score += 1;

				g3.drawImage(offimg, 0, 0, this);
				refresh();
//				g3.drawImage(images[shovelimg], shovelX, shovelY, this);
				refreshDisp((int)(total_time/1000));
			}
			else
				CLICK = false;

			for(int i = 1; i < 10; i++){
				if(imgnum[i] == 2){
					trackernum[i]++;

					if(trackernum[i] > 14){
						trackernum[i] = 0;
						imgnum[i] = 3;
						refresh();
					}
				}
			}

			total_time = game_start - System.currentTimeMillis();

			if(total_time <= 0)
				DONE = true;

			if((int)(total_time/1000) != (int)(prev_time/1000)){
				refreshDisp((int)(total_time / 1000));
				prev_time = total_time;
				refresh();
			}

//			refresh();

			if(CHANGED){
				tempimg = offimg2;
				repaint();
				CHANGED = false;
			}

/*			if(SHOVEL_CHANGED){
				g3.drawImage(offimg, 0, 0, this);
				refresh();
				refreshDisp((int)(total_time / 1000));
				g3.drawImage(images[shovelimg], shovelX, shovelY, this);

//				tempimg = offimg2;
//				repaint();

				SHOVEL_CHANGED = false;
			}
*/
		}while(!DONE);

		// debriefing screen
		endScreen();

//////// End Main Game Loop /////////////////////////////////////

		}while(true);

	}


	public boolean mouseDown(Event evt, int x, int y){
		if(!START_GAME)
			START_GAME = true;
		else{

		CLICK = true;

		SHOVEL_CHANGED = true;

		shovelimg = 7;
		
		if(x >= 0 && x < 100){
			if(y >= 0 && y < 100){
				clicked_sect = 1;
			}
			else if(y >= 100 && y < 200){
				clicked_sect = 4;
			}
			else {
				clicked_sect = 7;
			}
		}
		else if(x >= 100 && x < 200){
			if(y >= 0 && y < 100){
				clicked_sect = 2;
			}
			else if(y >= 100 && y < 200){
				clicked_sect = 5;
			}
			else {
				clicked_sect = 8;
			}
		}
		else {
			if(y >= 0 && y < 100){
				clicked_sect = 3;
			}
			else if(y >= 100 && y < 200){
				clicked_sect = 6;
			}
			else {
				clicked_sect = 9;
			}
		}

		}

		return true;
	}

	public boolean mouseMove(Event evt, int x, int y){
		shovelX = x;
		shovelY = y;

		shovelimg = 6;

		SHOVEL_CHANGED = true;

		return true;
	}

	public int getNewSect(){
		int sect = (int)(rnd.nextDouble() * 10);

		if(sect < 1 || sect > 9)
			sect = 1;

		return(sect);
	}

	public void refreshDisp(int time_left){
		g3.setColor(Color.black);
		g3.fillRect(0, 300, 300, 30);
		g3.setColor(Color.yellow);
		g3.drawString("Moles Hit: "+score, 15, 322);
		g3.drawString("Time Left: "+time_left, 180, 322);
	}

	public void endScreen(){
		double accuracy;

		START_GAME = false;

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 300, 330);

		g3.setColor(Color.yellow);
		g3.setFont(bigF);
		g3.drawString("Out of Time!", 70, 65);
		tempimg = offimg2;
		repaint();
		pause(1000);

		g3.setFont(f);
		g3.drawString("Moles Hit: "+score, 25, 155);
		tempimg = offimg2;
		repaint();
		pause(1000);

		g3.drawString("Misses: "+num_misses, 25, 175);
		tempimg = offimg2;
		repaint();
		pause(1000);


		do{
			g3.drawString("Click to continue...", 65, 270);
			tempimg = offimg2;
			repaint();
		}while(!START_GAME);

		START_GAME = false;
	}



	public int getX(int sect){
		switch(sect){
		case 1:
		case 4:
		case 7:
			return(10);
		case 2:
		case 5:
		case 8:
			return(110);
		case 3:
		case 6:
		case 9:
			return(210);
		}

		return(0);
	}

	public int getY(int sect){
		switch(sect){
		case 1:
		case 2:
		case 3:
			return(10);
		case 4:
		case 5:
		case 6:
			return(110);
		case 7:
		case 8:
		case 9:
			return(210);
		}

		return(0);
	}


	public void refresh(){
		for(int i = 1; i < 10; i++){

			int x = getX(i);
			int y = getY(i);

			draw(imgnum[i], x, y);

		}

		CHANGED = true;
	}

	public void draw(int img_num, int x, int y){
		if(images[img_num] != null)
			g3.drawImage(images[img_num], x, y, this);
	}

	public void pause(int time){
		try{ Thread.sleep(time); }
			catch(InterruptedException e) {}
	}

	public void update(Graphics g){
		g.clipRect(0, 0, 300, 330);
		paint(g);
	}

	public void paint(Graphics g){
		if(tempimg != null)
			g.drawImage(tempimg, 0, 0, this);
	}
}


			

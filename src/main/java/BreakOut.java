/*
	Breakout v1.00.000
	Copyright © 2001 ImBoredOnline.com
	written for ImBoredOnline.com by Jared Stehler
	04.2001
*/
	



import java.awt.*;
import java.applet.*;
//import java.util.Random;

public class BreakOut extends Applet implements Runnable
{

	final int BLOCKHEIGHT = 10;
	final int BLOCKWIDTH  = 60;
	final int MAX_BLOCKS = 61;
	
	int blocknum = 0;	
	int ballmoveX, ballmoveY, ballX, ballY;
	
	int BallMove[][] = new int[6][2];
	int ballmovenum = 3;
	
	int paddleX;
	
	Image Block[] = new Image[6];
	int BlockPos[][] = new int[MAX_BLOCKS][2];
	boolean BlockVal[] = new boolean[MAX_BLOCKS];
	
	int num_blocks;
	
	Color bgcolor, dispColor;
	
	int current_level, num_balls, current_score;
	boolean DONE = false, GAME_OVER = false, START = false;
	boolean REDRAWBLOCKS = true;
	
	
	Image Ball, Paddle;
	Image Title, GameOver, YouWin;
	
	
	Font dispFont = new Font("TimesRoman", Font.BOLD, 18);
	Font creditsFont = new Font("TimesRoman", Font.PLAIN, 12);
	
	
	Thread runner;

	Graphics g2;
	Image offimg;
	
	private MediaTracker tracker;

	
	public void init(){
		offimg = createImage(360, 380);

		g2 = offimg.getGraphics();
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
		
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 360, 380);
		g2.setColor(Color.black);
		g2.drawString("Loading...", 160, 190);
		repaint();
		
		
		BallMove[0][0] = 2;		BallMove[0][1] = 4;
		BallMove[1][0] = 5;		BallMove[1][1] = 3;
		BallMove[2][0] = 3;		BallMove[2][1] = 5;
		BallMove[3][0] = 4;		BallMove[3][1] = 4;
		BallMove[4][0] = 2;		BallMove[4][1] = 5;
		BallMove[5][0] = 3;		BallMove[5][1] = 4;
		
	
		Title = getImage(getCodeBase(), "breakout/title.jpg");
		GameOver = getImage(getCodeBase(), "breakout/game_over.jpg");
		YouWin = getImage(getCodeBase(), "breakout/you_win.jpg");
		
		Ball = getImage(getCodeBase(), "breakout/ball.gif");
		Paddle = getImage(getCodeBase(), "breakout/paddle.gif");
		
		Block[0] = getImage(getCodeBase(), "breakout/block_red.gif");
		Block[1] = getImage(getCodeBase(), "breakout/block_blue.gif");
		Block[2] = getImage(getCodeBase(), "breakout/block_yellow.gif");
		Block[3] = getImage(getCodeBase(), "breakout/block_purple.gif");
		Block[4] = getImage(getCodeBase(), "breakout/block_green.gif");
		Block[5] = getImage(getCodeBase(), "breakout/block_brown.gif");


		tracker = new MediaTracker(this);		
		
		tracker.addImage(Title, 0);
		tracker.addImage(GameOver, 0);
		tracker.addImage(YouWin, 0);

		tracker.addImage(Ball, 0);
		tracker.addImage(Paddle, 0);
		
		tracker.addImage(Block[0], 0);
		tracker.addImage(Block[1], 0);
		tracker.addImage(Block[2], 0);
		tracker.addImage(Block[3], 0);
		tracker.addImage(Block[4], 0);
		tracker.addImage(Block[5], 0);

	
			try{
				tracker.waitForID(0);
			}
			catch(InterruptedException e)
			{
				return;
			}

			
			do{
			// game loops forever in this loop

				//show title screen
				//wait for START = true
				do{
					g2.drawImage(Title, 0, 0, this);
					repaint();
					pause(100);
				}while(!START);

			current_level = 1;
			current_score = 0;
			num_balls = 6;

			g2.setFont(dispFont);
			
			
			// the main game loop with all the levels etc
			do{
			
		paddleX = 20;

		//randomize these values
		
		ballX = 150;
		ballY = 150;
		
		
		loadLevel(current_level);

			g2.setColor(bgcolor);
			redrawScreen();
			repaint();
			
			waitForClick();		
			
			
		do{
			if(REDRAWBLOCKS){
			blocknum=0;
			for(int i = 0; i < num_blocks; i++){
				if(BlockVal[i]){
					g2.drawImage(Block[blocknum], BlockPos[i][0], BlockPos[i][1], this);
				}
					blocknum++;
					if(blocknum > 5)
						blocknum = 1;
				
			}
			REDRAWBLOCKS = false;
			}
						
			g2.fillRect(ballX, ballY, 10, 10);
			g2.fillRect(0, 330, 360, 15);
			
			ballX += ballmoveX;
			ballY += ballmoveY;
			
			checkCollisions();
			

			g2.drawImage(Ball, ballX, ballY, this);
			
			g2.drawImage(Paddle, paddleX, 330, this);
			
		g2.setColor(dispColor);
		g2.drawString("Level: "+current_level, 10, 360);
		g2.drawString("Score: "+current_score, 130, 360);
		g2.drawString("Balls Left: "+num_balls, 260, 360);
		g2.setColor(bgcolor);
			
			pause(23);
				
			repaint();
		}while(!DONE);		
		//end game loop
		
		DONE = false;
		
		if(!GAME_OVER){//successful completion of the level
			//increment level
		}
		
			}while(!GAME_OVER);
			
			if(current_level <= 10){
				//show game over screen
				g2.drawImage(GameOver, 0, 0, this);
				repaint();
				pause(3000);
				
				GAME_OVER = false;
				START = false;
				DONE = false;
			}
			else{
				//show victory screen
				g2.drawImage(YouWin, 0, 0, this);
				repaint();
				pause(1000);
				
				g2.setFont(creditsFont);
				g2.setColor(Color.yellow);
				g2.drawString("written by Jared Stehler", 117, 325);
				g2.drawString("for ImBoredOnline.com", 120, 340);
				g2.drawString("Copyright © 2001 ImBoredOnline.com", 80, 365);

				repaint();
				pause(3000);
				
				g2.drawString("Click to Continue...", 130, 230);
				
			START = false;
			
			do{
				repaint();
				pause(60);
			}while(!START);
			
			START = false;

			}
			
			}while(true);
		
		
	}
	
	public void redrawScreen(){
			g2.setColor(bgcolor);
			g2.fillRect(0, 0, 360, 380);

			
			blocknum=0;
			for(int i = 0; i < num_blocks; i++){
				if(BlockVal[i]){
					g2.drawImage(Block[blocknum], BlockPos[i][0], BlockPos[i][1], this);
				}
					blocknum++;
					if(blocknum > 5)
						blocknum = 1;
				
			}
						
			g2.drawImage(Ball, ballX, ballY, this);
			
			g2.drawImage(Paddle, paddleX, 330, this);
			
		g2.setColor(dispColor);
		g2.drawString("Level: "+current_level, 10, 360);
		g2.drawString("Score: "+current_score, 130, 360);
		g2.drawString("Balls Left: "+num_balls, 260, 360);
		g2.setColor(bgcolor);
			
			pause(23);
		
	}
				
	
	public void checkCollisions(){
		if(ballY < 140)
			REDRAWBLOCKS = true;
		
		if((ballX > paddleX && ballX < (paddleX + 60)) && (ballY >= 320)){
			//paddle collision
			
			ballmoveY = 0 - ballmoveY;
			
			
		}
		
		else if(ballY <= 0){ //horiz side collision
			ballmoveY = 0 - ballmoveY;
			

			
		}
		else if(ballX <= 0 || ballX >= (360-10)){ //vert side collision
			ballmoveX = 0 - ballmoveX;

			
		}
		
		if(ballY >= (360 - 10)){
			ballX = 180;	ballY = 180;
			
			// decrement num balls left, if all gone then end level with a loss
			num_balls--;
			if(num_balls < 0){
				DONE = true;
				GAME_OVER = true;
			}
			else{
				redrawScreen();
				waitForClick();
				
				// set ball velocity
				ballmoveX = BallMove[ballmovenum][0];
				ballmoveY = BallMove[ballmovenum][1];
		
				ballmovenum++;
				if(ballmovenum > 5)
					ballmovenum = 0;
				
			}
			
		}
		
		else{
			for(int i = 0; i < num_blocks; i++){
				if(BlockVal[i]){
					if((ballX > BlockPos[i][0]) && (ballX < (BlockPos[i][0] + BLOCKWIDTH))){
						if((ballY > BlockPos[i][1]) && (ballY < (BlockPos[i][1] + BLOCKHEIGHT))){
							// we have a hit!!  get rid of it
							BlockVal[i] = false;
							// increment the score here
							current_score += 1000;
							
							// change direction of the ball here
							// assuming horiz side collision... will pay for it later on...
							ballmoveY = 0 - ballmoveY;
							redrawScreen();
				
							checkWin();
							

						}
					}
				}
			}
		}
	}
	
	public void checkWin(){
		boolean no_win = false;
		
		for(int i = 0; i < num_blocks; i++){
			if(BlockVal[i])
				no_win = true;
		}
		
		if(!no_win){
			//levelDone();
//			System.out.println("LevelComplete!");
			current_level++;
			if(current_level > 10)
				GAME_OVER = true;
			DONE = true;
			
		}
	}
	
	public void waitForClick(){
			g2.setColor(dispColor);
			g2.drawString("Click To Start", 140, 160);
			g2.setColor(bgcolor);
			
			START = false;
			
			do{
				repaint();
				pause(60);
			}while(!START);

			
			redrawScreen();
			pause(23);
			repaint();
	
	}
	
	public boolean mouseMove(Event evt, int x, int y){
		paddleX = x - 30;
		
		if(paddleX < 0)
			paddleX = 0;
		else if(paddleX > 300)
			paddleX = 300;
		
		
		return true;
	}

	public boolean mouseDown(Event evt, int x, int y){
		
		if(!START)
			START = true;

		return true;
	}
	
	public void loadLevel(int level){

		switch(level){
		case 1:
		// level 1
		num_blocks = 20;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		//position the blocks
		BlockPos[0][0] = 30;	BlockPos[0][1] = 38;
		BlockPos[1][0] = 91;	BlockPos[1][1] = 38;
		BlockPos[2][0] = 152;	BlockPos[2][1] = 38;
		BlockPos[3][0] = 213;	BlockPos[3][1] = 38;
		BlockPos[4][0] = 274;	BlockPos[4][1] = 38;

		BlockPos[5][0] = 30;	BlockPos[5][1] = 49;
		BlockPos[6][0] = 91;	BlockPos[6][1] = 49;
		BlockPos[7][0] = 152;	BlockPos[7][1] = 49;
		BlockPos[8][0] = 213;	BlockPos[8][1] = 49;
		BlockPos[9][0] = 274;	BlockPos[9][1] = 49;

		BlockPos[10][0] = 30;	BlockPos[10][1] = 60;
		BlockPos[11][0] = 91;	BlockPos[11][1] = 60;
		BlockPos[12][0] = 152;	BlockPos[12][1] = 60;
		BlockPos[13][0] = 213;	BlockPos[13][1] = 60;
		BlockPos[14][0] = 274;	BlockPos[14][1] = 60;

		BlockPos[15][0] = 30;	BlockPos[15][1] = 71;
		BlockPos[16][0] = 91;	BlockPos[16][1] = 71;
		BlockPos[17][0] = 152;	BlockPos[17][1] = 71;
		BlockPos[18][0] = 213;	BlockPos[18][1] = 71;
		BlockPos[19][0] = 274;	BlockPos[19][1] = 71;

		BlockPos[20][0] = 30;	BlockPos[0][1] = 82;
		BlockPos[21][0] = 91;	BlockPos[1][1] = 82;
		BlockPos[22][0] = 152;	BlockPos[2][1] = 82;
		BlockPos[23][0] = 213;	BlockPos[3][1] = 82;
		BlockPos[24][0] = 274;	BlockPos[4][1] = 82;
		
		break;
		case 2:
		num_blocks = 27;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 0;		BlockPos[0][1] = 27;
		BlockPos[1][0] = 61;	BlockPos[1][1] = 27;
		BlockPos[2][0] = 122;	BlockPos[2][1] = 27;
		BlockPos[3][0] = 183;	BlockPos[3][1] = 27;
		BlockPos[4][0] = 244;	BlockPos[4][1] = 27;
		BlockPos[5][0] = 305;	BlockPos[5][1] = 27;
		
		BlockPos[6][0] = 0;		BlockPos[6][1] = 38;
		BlockPos[7][0] = 61;	BlockPos[7][1] = 38;
		BlockPos[8][0] = 122;	BlockPos[8][1] = 38;
		BlockPos[9][0] = 183;	BlockPos[9][1] = 38;
		BlockPos[10][0] = 244;	BlockPos[10][1] = 38;
		BlockPos[11][0] = 305;	BlockPos[11][1] = 38;

		BlockPos[12][0] = 30;	BlockPos[12][1] = 49;
		BlockPos[13][0] = 91;	BlockPos[13][1] = 49;
		BlockPos[14][0] = 152;	BlockPos[14][1] = 49;
		BlockPos[15][0] = 213;	BlockPos[15][1] = 49;
		BlockPos[16][0] = 274;	BlockPos[16][1] = 49;

		BlockPos[17][0] = 60;	BlockPos[17][1] = 60;
		BlockPos[18][0] = 121;	BlockPos[18][1] = 60;
		BlockPos[19][0] = 182;	BlockPos[19][1] = 60;
		BlockPos[20][0] = 243;	BlockPos[20][1] = 60;
		
		BlockPos[21][0] = 90;	BlockPos[21][1] = 71;
		BlockPos[22][0] = 151;	BlockPos[22][1] = 71;
		BlockPos[23][0] = 212;	BlockPos[23][1] = 71;
		
		BlockPos[24][0] = 120;	BlockPos[24][1] = 82;
		BlockPos[25][0] = 181;	BlockPos[25][1] = 82;
		
		BlockPos[26][0] = 150;	BlockPos[26][1] = 93;
		
		break;

		case 3:
		num_blocks = 21;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		//position the blocks
		BlockPos[0][0] = 0;		BlockPos[0][1] = 27;
		BlockPos[1][0] = 301;	BlockPos[1][1] = 27;

		BlockPos[2][0] = 30;	BlockPos[2][1] = 38;
		BlockPos[3][0] = 271;	BlockPos[3][1] = 38;
		
		BlockPos[4][0] = 60;	BlockPos[4][1] = 49;
		BlockPos[5][0] = 241;	BlockPos[5][1] = 49;
		
		BlockPos[6][0] = 90;	BlockPos[6][1] = 60;
		BlockPos[7][0] = 211;	BlockPos[7][1] = 60;

		BlockPos[8][0] = 120;	BlockPos[8][1] = 71;
		BlockPos[9][0] = 181;	BlockPos[9][1] = 71;
		
		BlockPos[10][0] = 150;	BlockPos[10][1] = 82;

		BlockPos[11][0] = 120;	BlockPos[11][1] = 93;
		BlockPos[12][0] = 181;	BlockPos[12][1] = 93;

		BlockPos[13][0] = 90;	BlockPos[13][1] = 104;
		BlockPos[14][0] = 211;	BlockPos[14][1] = 104;

		BlockPos[15][0] = 60;	BlockPos[15][1] = 115;
		BlockPos[16][0] = 241;	BlockPos[16][1] = 115;
		
		BlockPos[17][0] = 30;	BlockPos[17][1] = 126;
		BlockPos[18][0] = 271;	BlockPos[18][1] = 126;
		
		BlockPos[19][0] = 0;	BlockPos[19][1] = 137;
		BlockPos[20][0] = 301;	BlockPos[20][1] = 137;

		
		break;
		
		case 4:
		num_blocks = 27;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 150;	BlockPos[0][1] = 27;

		BlockPos[1][0] = 120;	BlockPos[1][1] = 38;
		BlockPos[2][0] = 181;	BlockPos[2][1] = 38;

		BlockPos[3][0] = 90;	BlockPos[3][1] = 49;
		BlockPos[4][0] = 151;	BlockPos[4][1] = 49;
		BlockPos[5][0] = 212;	BlockPos[5][1] = 49;

		BlockPos[6][0] = 60;	BlockPos[6][1] = 60;
		BlockPos[7][0] = 121;	BlockPos[7][1] = 60;
		BlockPos[8][0] = 182;	BlockPos[8][1] = 60;
		BlockPos[9][0] = 243;	BlockPos[9][1] = 60;

		BlockPos[10][0] = 30;	BlockPos[10][1] = 71;
		BlockPos[11][0] = 91;	BlockPos[11][1] = 71;
		BlockPos[12][0] = 152;	BlockPos[12][1] = 71;
		BlockPos[13][0] = 213;	BlockPos[13][1] = 71;
		BlockPos[14][0] = 274;	BlockPos[14][1] = 71;

		BlockPos[15][0] = 0;	BlockPos[15][1] = 82;
		BlockPos[16][0] = 61;	BlockPos[16][1] = 82;
		BlockPos[17][0] = 122;	BlockPos[17][1] = 82;
		BlockPos[18][0] = 183;	BlockPos[18][1] = 82;
		BlockPos[19][0] = 244;	BlockPos[19][1] = 82;
		BlockPos[20][0] = 305;	BlockPos[20][1] = 82;
		
		BlockPos[21][0] = 0;	BlockPos[21][1] = 93;
		BlockPos[22][0] = 61;	BlockPos[22][1] = 93;
		BlockPos[23][0] = 122;	BlockPos[23][1] = 93;
		BlockPos[24][0] = 183;	BlockPos[24][1] = 93;
		BlockPos[25][0] = 244;	BlockPos[25][1] = 93;
		BlockPos[26][0] = 305;	BlockPos[26][1] = 93;

		break;
		
		case 5:
		num_blocks = 36;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 0;		BlockPos[0][1] = 5;
		BlockPos[1][0] = 61;	BlockPos[1][1] = 5;
		BlockPos[2][0] = 122;	BlockPos[2][1] = 5;
		BlockPos[3][0] = 183;	BlockPos[3][1] = 5;
		BlockPos[4][0] = 244;	BlockPos[4][1] = 5;
		BlockPos[5][0] = 305;	BlockPos[5][1] = 5;

		BlockPos[6][0] = 0;		BlockPos[6][1] = 16;
		BlockPos[7][0] = 122;	BlockPos[7][1] = 16;
		BlockPos[8][0] = 244;	BlockPos[8][1] = 16;

		BlockPos[9][0] = 61;	BlockPos[9][1] = 27;
		BlockPos[10][0] = 183;	BlockPos[10][1] = 27;
		BlockPos[11][0] = 305;	BlockPos[11][1] = 27;
		
		BlockPos[12][0] = 0;	BlockPos[12][1] = 38;
		BlockPos[13][0] = 122;	BlockPos[13][1] = 38;
		BlockPos[14][0] = 244;	BlockPos[14][1] = 38;
		
		BlockPos[15][0] = 61;	BlockPos[15][1] = 49;
		BlockPos[16][0] = 183;	BlockPos[16][1] = 49;
		BlockPos[17][0] = 305;	BlockPos[17][1] = 49;

		BlockPos[18][0] = 0;	BlockPos[18][1] = 60;
		BlockPos[19][0] = 122;	BlockPos[19][1] = 60;
		BlockPos[20][0] = 244;	BlockPos[20][1] = 60;

		BlockPos[21][0] = 61;	BlockPos[21][1] = 71;
		BlockPos[22][0] = 183;	BlockPos[22][1] = 71;
		BlockPos[23][0] = 305;	BlockPos[23][1] = 71;
		
		BlockPos[24][0] = 0;	BlockPos[24][1] = 82;
		BlockPos[25][0] = 122;	BlockPos[25][1] = 82;
		BlockPos[26][0] = 244;	BlockPos[26][1] = 82;

		BlockPos[27][0] = 61;	BlockPos[27][1] = 93;
		BlockPos[28][0] = 183;	BlockPos[28][1] = 93;
		BlockPos[29][0] = 305;	BlockPos[29][1] = 93;

		BlockPos[30][0] = 0;	BlockPos[30][1] = 104;
		BlockPos[31][0] = 61;	BlockPos[31][1] = 104;
		BlockPos[32][0] = 122;	BlockPos[32][1] = 104;
		BlockPos[33][0] = 183;	BlockPos[33][1] = 104;
		BlockPos[34][0] = 244;	BlockPos[34][1] = 104;
		BlockPos[35][0] = 305;	BlockPos[35][1] = 104;
		break;

		case 6:
		num_blocks = 36;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 150;	BlockPos[0][1] = 5;

		BlockPos[1][0] = 120;	BlockPos[1][1] = 16;
		BlockPos[2][0] = 181;	BlockPos[2][1] = 16;

		BlockPos[3][0] = 90;	BlockPos[3][1] = 27;
		BlockPos[4][0] = 151;	BlockPos[4][1] = 27;
		BlockPos[5][0] = 212;	BlockPos[5][1] = 27;

		BlockPos[6][0] = 60;	BlockPos[6][1] = 38;
		BlockPos[7][0] = 121;	BlockPos[7][1] = 38;
		BlockPos[8][0] = 182;	BlockPos[8][1] = 38;
		BlockPos[9][0] = 243;	BlockPos[9][1] = 38;

		BlockPos[10][0] = 30;	BlockPos[10][1] = 49;
		BlockPos[11][0] = 91;	BlockPos[11][1] = 49;
		BlockPos[12][0] = 152;	BlockPos[12][1] = 49;
		BlockPos[13][0] = 213;	BlockPos[13][1] = 49;
		BlockPos[14][0] = 274;	BlockPos[14][1] = 49;

		BlockPos[15][0] = 0;	BlockPos[15][1] = 60;
		BlockPos[16][0] = 61;	BlockPos[16][1] = 60;
		BlockPos[17][0] = 122;	BlockPos[17][1] = 60;
		BlockPos[18][0] = 183;	BlockPos[18][1] = 60;
		BlockPos[19][0] = 244;	BlockPos[19][1] = 60;
		BlockPos[20][0] = 305;	BlockPos[20][1] = 60;		
		
		BlockPos[21][0] = 30;	BlockPos[21][1] = 71;
		BlockPos[22][0] = 91;	BlockPos[22][1] = 71;
		BlockPos[23][0] = 152;	BlockPos[23][1] = 71;
		BlockPos[24][0] = 213;	BlockPos[24][1] = 71;
		BlockPos[25][0] = 274;	BlockPos[25][1] = 71;

		BlockPos[26][0] = 60;	BlockPos[26][1] = 82;
		BlockPos[27][0] = 121;	BlockPos[27][1] = 82;
		BlockPos[28][0] = 182;	BlockPos[28][1] = 82;
		BlockPos[29][0] = 243;	BlockPos[29][1] = 82;

		BlockPos[30][0] = 90;	BlockPos[30][1] = 93;
		BlockPos[31][0] = 151;	BlockPos[31][1] = 93;
		BlockPos[32][0] = 212;	BlockPos[32][1] = 93;

		BlockPos[33][0] = 120;	BlockPos[33][1] = 104;
		BlockPos[34][0] = 181;	BlockPos[34][1] = 104;

		BlockPos[35][0] = 150;	BlockPos[35][1] = 115;
		
		break;
		
		case 7:
		num_blocks = 46;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 0;		BlockPos[0][1] = 5;
		BlockPos[1][0] = 61;	BlockPos[1][1] = 5;
		BlockPos[2][0] = 122;	BlockPos[2][1] = 5;
		BlockPos[3][0] = 183;	BlockPos[3][1] = 5;
		BlockPos[4][0] = 244;	BlockPos[4][1] = 5;
		BlockPos[5][0] = 305;	BlockPos[5][1] = 5;		

		
		BlockPos[6][0] = 0;		BlockPos[6][1] = 16;
		BlockPos[7][0] = 305;	BlockPos[7][1] = 16;		

		BlockPos[8][0] = 0;		BlockPos[8][1] = 27;
		BlockPos[9][0] = 122;	BlockPos[9][1] = 27;
		BlockPos[10][0] = 183;	BlockPos[10][1] = 27;
		BlockPos[11][0] = 305;	BlockPos[11][1] = 27;		

		BlockPos[12][0] = 0;	BlockPos[12][1] = 38;
		BlockPos[13][0] = 122;	BlockPos[13][1] = 38;
		BlockPos[14][0] = 183;	BlockPos[14][1] = 38;
		BlockPos[15][0] = 305;	BlockPos[15][1] = 38;		
		
		BlockPos[16][0] = 0;	BlockPos[16][1] = 49;
		BlockPos[17][0] = 61;	BlockPos[17][1] = 49;
		BlockPos[18][0] = 122;	BlockPos[18][1] = 49;
		BlockPos[19][0] = 244;	BlockPos[19][1] = 49;
		BlockPos[20][0] = 305;	BlockPos[20][1] = 49;		
		
		BlockPos[21][0] = 0;	BlockPos[21][1] = 60;
		BlockPos[22][0] = 61;	BlockPos[22][1] = 60;
		BlockPos[23][0] = 244;	BlockPos[23][1] = 60;
		BlockPos[24][0] = 305;	BlockPos[24][1] = 60;		
		
		BlockPos[25][0] = 0;	BlockPos[25][1] = 71;
		BlockPos[26][0] = 61;	BlockPos[26][1] = 71;
		BlockPos[27][0] = 183;	BlockPos[27][1] = 71;
		BlockPos[28][0] = 244;	BlockPos[28][1] = 71;
		BlockPos[29][0] = 305;	BlockPos[29][1] = 71;		
		
		BlockPos[30][0] = 0;	BlockPos[30][1] = 82;
		BlockPos[31][0] = 122;	BlockPos[31][1] = 82;
		BlockPos[32][0] = 183;	BlockPos[32][1] = 82;
		BlockPos[33][0] = 244;	BlockPos[33][1] = 82;
		BlockPos[34][0] = 305;	BlockPos[34][1] = 82;		
		
		BlockPos[35][0] = 0;	BlockPos[35][1] = 93;
		BlockPos[36][0] = 122;	BlockPos[36][1] = 93;
		BlockPos[37][0] = 183;	BlockPos[37][1] = 93;
		BlockPos[38][0] = 244;	BlockPos[38][1] = 93;
		BlockPos[39][0] = 305;	BlockPos[39][1] = 93;		
		
		BlockPos[40][0] = 0;	BlockPos[40][1] = 104;
		BlockPos[41][0] = 61;	BlockPos[41][1] = 104;
		BlockPos[42][0] = 122;	BlockPos[42][1] = 104;
		BlockPos[43][0] = 183;	BlockPos[43][1] = 104;
		BlockPos[44][0] = 244;	BlockPos[44][1] = 104;
		BlockPos[45][0] = 305;	BlockPos[45][1] = 104;		
		
		break;
		
		case 8:
		num_blocks = 38;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 0;		BlockPos[0][1] = 5;
		BlockPos[1][0] = 61;	BlockPos[1][1] = 5;
		BlockPos[2][0] = 122;	BlockPos[2][1] = 5;
		BlockPos[3][0] = 183;	BlockPos[3][1] = 5;
		BlockPos[4][0] = 244;	BlockPos[4][1] = 5;
		BlockPos[5][0] = 305;	BlockPos[5][1] = 5;		

		BlockPos[6][0] = 0;		BlockPos[6][1] = 16;
		BlockPos[7][0] = 305;	BlockPos[7][1] = 16;		

		BlockPos[8][0] = 0;		BlockPos[8][1] = 27;
		BlockPos[9][0] = 152;	BlockPos[9][1] = 27;
		BlockPos[10][0] = 305;	BlockPos[10][1] = 27;		

		BlockPos[11][0] = 0;	BlockPos[11][1] = 38;
		BlockPos[12][0] = 91;	BlockPos[12][1] = 38;
		BlockPos[13][0] = 213;	BlockPos[13][1] = 38;
		BlockPos[14][0] = 305;	BlockPos[14][1] = 38;		

		BlockPos[15][0] = 0;	BlockPos[15][1] = 49;
		BlockPos[16][0] = 151;	BlockPos[16][1] = 49;
		BlockPos[17][0] = 305;	BlockPos[17][1] = 49;		

		BlockPos[18][0] = 0;	BlockPos[18][1] = 60;
		BlockPos[19][0] = 91;	BlockPos[19][1] = 60;
		BlockPos[20][0] = 213;	BlockPos[20][1] = 60;
		BlockPos[21][0] = 305;	BlockPos[21][1] = 60;		

		BlockPos[22][0] = 0;	BlockPos[22][1] = 71;
		BlockPos[23][0] = 91;	BlockPos[23][1] = 71;
		BlockPos[24][0] = 152;	BlockPos[24][1] = 71;
		BlockPos[25][0] = 213;	BlockPos[25][1] = 71;
		BlockPos[26][0] = 305;	BlockPos[26][1] = 71;		

		BlockPos[27][0] = 0;	BlockPos[27][1] = 82;
		BlockPos[28][0] = 152;	BlockPos[28][1] = 82;
		BlockPos[29][0] = 305;	BlockPos[29][1] = 82;		

		BlockPos[30][0] = 0;	BlockPos[30][1] = 93;
		BlockPos[31][0] = 305;	BlockPos[31][1] = 93;		

		BlockPos[32][0] = 0;	BlockPos[32][1] = 104;
		BlockPos[33][0] = 61;	BlockPos[33][1] = 104;
		BlockPos[34][0] = 122;	BlockPos[34][1] = 104;
		BlockPos[35][0] = 183;	BlockPos[35][1] = 104;
		BlockPos[36][0] = 244;	BlockPos[36][1] = 104;
		BlockPos[37][0] = 305;	BlockPos[37][1] = 104;		

		break;
		
		case 9:
		num_blocks = 30;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 142;		BlockPos[0][1] = 5;

		BlockPos[1][0] = 81;		BlockPos[1][1] = 16;
		BlockPos[2][0] = 142;		BlockPos[2][1] = 16;
		BlockPos[3][0] = 204;		BlockPos[3][1] = 16;

		BlockPos[4][0] = 59;		BlockPos[4][1] = 27;
		BlockPos[5][0] = 227;		BlockPos[5][1] = 27;

		BlockPos[6][0] = 48;		BlockPos[6][1] = 38;
		BlockPos[7][0] = 243;		BlockPos[7][1] = 38;

		BlockPos[8][0] = 37;		BlockPos[8][1] = 49;
		BlockPos[9][0] = 142;		BlockPos[9][1] = 49;
		BlockPos[10][0] = 256;		BlockPos[10][1] = 49;

		BlockPos[11][0] = 31;		BlockPos[11][1] = 60;
		BlockPos[12][0] = 114;		BlockPos[12][1] = 60;
		BlockPos[13][0] = 175;		BlockPos[13][1] = 60;
		BlockPos[14][0] = 263;		BlockPos[14][1] = 60;

		BlockPos[15][0] = 31;		BlockPos[15][1] = 71;
		BlockPos[16][0] = 114;		BlockPos[16][1] = 71;
		BlockPos[17][0] = 175;		BlockPos[17][1] = 71;
		BlockPos[18][0] = 263;		BlockPos[18][1] = 71;
		
		BlockPos[19][0] = 37;		BlockPos[19][1] = 82;
		BlockPos[20][0] = 142;		BlockPos[20][1] = 82;
		BlockPos[21][0] = 256;		BlockPos[21][1] = 82;

		BlockPos[22][0] = 48;		BlockPos[22][1] = 93;
		BlockPos[23][0] = 243;		BlockPos[23][1] = 93;

		BlockPos[24][0] = 59;		BlockPos[24][1] = 104;
		BlockPos[25][0] = 227;		BlockPos[25][1] = 104;

		BlockPos[26][0] = 81;		BlockPos[26][1] = 115;
		BlockPos[27][0] = 142;		BlockPos[27][1] = 115;
		BlockPos[28][0] = 204;		BlockPos[28][1] = 115;

		BlockPos[29][0] = 142;		BlockPos[29][1] = 126;

		break;
		
		case 10:
		num_blocks = 27;
		
		//bgcolor
		bgcolor = Color.white;
		dispColor = Color.black;
		
		
		//position the blocks
		BlockPos[0][0] = 152;		BlockPos[0][1] = 5;
		
		BlockPos[1][0] = 152;		BlockPos[1][1] = 16;

		BlockPos[2][0] = 152;		BlockPos[2][1] = 27;

		BlockPos[3][0] = 30;		BlockPos[3][1] = 38;
		BlockPos[4][0] = 91;		BlockPos[4][1] = 38;
		BlockPos[5][0] = 152;		BlockPos[5][1] = 38;
		BlockPos[6][0] = 213;		BlockPos[6][1] = 38;
		BlockPos[7][0] = 274;		BlockPos[7][1] = 38;

		BlockPos[8][0] = 91;		BlockPos[8][1] = 49;
		BlockPos[9][0] = 152;		BlockPos[9][1] = 49;
		BlockPos[10][0] = 213;		BlockPos[10][1] = 49;

		BlockPos[11][0] = 152;		BlockPos[11][1] = 60;

		BlockPos[12][0] = 152;		BlockPos[12][1] = 71;

		BlockPos[13][0] = 95;		BlockPos[13][1] = 82;
		BlockPos[14][0] = 210;		BlockPos[14][1] = 82;
		
		BlockPos[15][0] = 80;		BlockPos[15][1] = 93;
		BlockPos[16][0] = 225;		BlockPos[16][1] = 93;
		
		BlockPos[17][0] = 65;		BlockPos[17][1] = 104;
		BlockPos[18][0] = 240;		BlockPos[18][1] = 104;
		
		BlockPos[19][0] = 50;		BlockPos[19][1] = 115;
		BlockPos[20][0] = 255;		BlockPos[20][1] = 115;

		BlockPos[21][0] = 0;		BlockPos[21][1] = 137;
		BlockPos[22][0] = 61;		BlockPos[22][1] = 137;
		BlockPos[23][0] = 122;		BlockPos[23][1] = 137;
		BlockPos[24][0] = 183;		BlockPos[24][1] = 137;
		BlockPos[25][0] = 244;		BlockPos[25][1] = 137;
		BlockPos[26][0] = 305;		BlockPos[26][1] = 137;		

		break;
		
		
		default:
		break;
		}
		
		//activate all blocks
		for(int i = 0; i < num_blocks; i++){
			BlockVal[i] = true;
		}
		
		// set ball velocity
		ballmoveX = BallMove[ballmovenum][0];
		ballmoveY = BallMove[ballmovenum][1];
		
		ballmovenum++;
		if(ballmovenum > 5)
			ballmovenum = 0;
		
	}		
	
	public void pause(int time){
		try{ Thread.sleep(time); }
			catch(InterruptedException e) {}
	}
		
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void paint(Graphics g){
		
		g.drawImage(offimg, 0, 0, this);
		
	}
	
}

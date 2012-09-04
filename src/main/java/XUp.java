/*
	Project Canidae
	Copyright 2000 JE Bicycles
	Release Version
	12.31.01

	Mods 10.xx.01
*/

//import java.awt.image.*;
import java.awt.*;

public final class XUp extends java.applet.Applet implements Runnable{

	int x = 0, panX = 0, panIncrement, current_sector, current_level = 0, current_comp;


	final int NUM_LEVELS = 11;
	final int NUM_TRICKS = 26;
	final int MAX_TRICK_ANIM = 15;
	final int MAX_SECTORS = 102;






	final String VERSION_BUILD = "1.00.100";




		
		
	final int sector_size = 60;

	final int SPACE_BAR = 32;
	final int RETURN = 10;
	final int ESCAPE = 27;

	final int NUM_IMAGES = 46;

	Thread runner;

	private MediaTracker tracker;
	
	Graphics g2, g3;
	
	Image tempimg, offimg, offimg2;

//	Image Menu_Selector;

	Image jump_meter, jump_meter_box, Title, menu_img;
	Image Options_Screen, Options_Selector, PauseScreen, LoadingScreen;

	Image Background;			
	
	Image currenttrick_points;		int currenttrickpoints;
									int trickpointsdecrement;
									
	Image combobox;					int combo = 0;
									int ComboPoints[] = new int[10];
									boolean SHOW_COMBOBOX = false;

	Image MainMenu[] = new Image[4];	int MenuPos[] = new int[11];
	Image OptionsMenu[] = new Image[4]; int OptionsPos[] = new int[4];

	Image trophy;

	Image Victory[] = new Image[4];

	Image HelpScreens[] = new Image[5];	int helpimgnum = 0;
	boolean REDRAW_HELP = false;	boolean SHOWING_HELP = false;



	Image level[][] = new Image[NUM_LEVELS][MAX_SECTORS + 6];
		int LevelNumSectors[] = new int[NUM_LEVELS + 1];
		int levelnumsectors;
		
		String CompType;
		String CompDesc[] = new String[NUM_LEVELS];
		String CurrentRun[][] = new String[3][50];
		int current_run_trick_num;
		String CompHistory[] = new String[NUM_LEVELS + 1];
		
		int LevelPlacementScores[] = new int[4];  // Scores for each placement
		int lowestplace;  // Lowest place needed to advance in a comp

	Image Bike[] = new Image[10];		Image Crash[][] = new Image[3][8];

	Image TrickAnim[][] = new Image[NUM_TRICKS+1][MAX_TRICK_ANIM];
		int trick_image_num = 0;	int TrickAnimNumImages[] = new int[NUM_TRICKS+1];
		int trick_wait_timer = 0;   int TrickWaitLength[] = new int[NUM_TRICKS+1];
									int trick_wait_length = 0;

	int PowerMeter[] = new int[10];
	int JumpLocation[] = new int[MAX_SECTORS+1];


	double Sin[] = new double[701];
	double Eqn[] = new double[701];

	String TrickListInfo[] = new String[NUM_TRICKS+1];
	String TrickHowTo[] = new String[NUM_TRICKS+1];
	String KeyCombos[] = new String[NUM_TRICKS+1];
	int    TrickPoints[] = new int[NUM_TRICKS+1]; 
	
	int SpeedSetting = 6;
	
	int pete = 0;      // pete is the integer for the riding anim

	int firstrun_score = 0, best_run = 0, MainGameLoop;
	int current_score = currenttrickpoints = current_comp = 0;
	int jump_length = 0, meter_position = 0, jump_factor = 0, ramplength = 0;
	int bikeY_on_ramp;
	int power, ability = 1, skill_level = 5, old_skill, previous_speed;

	int menu_position;		Color MenuTextColor[] = new Color[12];
							String MainMenuText[] = new String[12];

	int first_trick_shown, listY;

	String current_trick = "";	int trick = 0;
	String last_trick = "";

	double ups = 1.4;

	long start_time, end_time, total_time;

	Font f = new Font("TimesRoman", Font.PLAIN, 14);
	Font menuFont = new Font("SansSerif", Font.PLAIN, 12);
//	Font trick_list_font = new Font("TimesRoman", Font.BOLD, 14);
	Font trick_how_to_font = new Font("TimesRoman", Font.PLAIN, 14);
	
	Font dispFont = new Font("TimesRoman", Font.BOLD, 18);
	
	Font fontBig = new Font("SansSerif", Font.BOLD, 24);

//	Font credits_font_big = new Font("TimesRoman", Font.BOLD, 18);
	Font credits_font_small = new Font("TimesRoman", Font.BOLD, 12);

	FontMetrics fm_big = getFontMetrics(fontBig);
	FontMetrics fm_small = getFontMetrics(credits_font_small);
	FontMetrics fm_disp = getFontMetrics(dispFont);

	boolean SHOWING_CREDITS = false;
	
	boolean PROGTEMP = false;
	boolean GAME_IN_PROGRESS = false, WAITING_FOR_SPACEBAR = false;
	boolean WAITING_FOR_OPTION = false, SKIP_MENU = false;
	
	boolean DONE = false, GAME_PAUSED, LOADING = false;
	boolean IN_THE_AIR = false, EXECUTING_TRICK = false, FINISHING_TRICK = false, LANDING = false;
	boolean POINT_TRANSFER = false, BACKWARDS = false;

	boolean MAIN_MENU = false, START_GAME = false, QUIT = false;
	boolean REDRAW_MENU = false, SHOWING_TRICK_LIST = false, TRICK_LIST = false;
	boolean REDRAW_LIST = false;
	boolean OPTIONS_MENU = false, REDRAW_OPTIONS = false;

	int PRACTICE_MODE = 0;		boolean FREESTYLE_MODE = false;

	public void init(){
		offimg = createImage(480, 400);
		offimg2 = createImage(480, 400);

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
		int bikeY, selectorY, temp_selection, i, I;
		
		getAppletContext().showStatus("Copyright © 2001 JEBikes.com");

		/*
			Load all images, display loading screen(s)
			Title Screen Loop, Main Game Loop
		*/
		
		
/*			double x1 = 1, y1;
			int p = 150;

			g3.setColor(Color.black);
			
			do{
//				y1 = 0 - (80 * (Math.sin(.01 * (x1+180))));

				y1 = (((x1 - 210)*(x1 - 210)) / p) + 200;
				
				System.out.println("x = "+x1);

				g3.drawLine((int)x1, (int)y1, (int)x1, (int)y1);
				tempimg = offimg2;
				repaint();

				x1+=1;

			}while(x1 < 360);

			stop();
*/

		
		g3.setFont(f);
		g3.setColor(Color.white);
		g3.fillRect(0, 0, 420, 400);
		g3.setColor(Color.black);
		g3.drawString("Project: Canidae Build v"+VERSION_BUILD, 10, 390);
		g3.drawString("Loading Game Images...", 135, 210);
		tempimg = offimg2;
		repaint();
		
		ComboPoints[0] = 0;			ComboPoints[1] = 0;
		ComboPoints[2] = 500;			ComboPoints[3] = 2000;
		ComboPoints[4] = 3000;			ComboPoints[5] = 4000;
		ComboPoints[6] = 5000;			ComboPoints[7] = 6000;
		ComboPoints[8] = 8000;			ComboPoints[9] = 100000;
		
		
		LevelNumSectors[0] = 39;		LevelNumSectors[6] = 57;
		LevelNumSectors[1] = 39;		LevelNumSectors[7] = 57;
		LevelNumSectors[2] = 39;		LevelNumSectors[8] = 39;
		LevelNumSectors[3] = 39;		LevelNumSectors[9] = 57;
		LevelNumSectors[4] = 48;		LevelNumSectors[10] = 84;
		LevelNumSectors[5] = 48;		LevelNumSectors[11] = 102;

		
		PowerMeter[0] = PowerMeter[1] = PowerMeter[7] = PowerMeter[8] = PowerMeter[9] = 0;
		PowerMeter[2] = PowerMeter[6] = 1;
		PowerMeter[3] = PowerMeter[5] = 2;
		PowerMeter[4] = 4;


		MenuPos[0] = 156;		OptionsPos[0] = 129;	MenuPos[6] = 156;
		MenuPos[1] = 156;		OptionsPos[1] = 100;	MenuPos[7] = 156;
		MenuPos[2] = 156;		OptionsPos[2] = 168;	MenuPos[8] = 156;
		MenuPos[3] = 156;       OptionsPos[3] = 134;	MenuPos[9] = 156;
		MenuPos[4] = 156;								MenuPos[10]= 156;
		MenuPos[5] = 156;
		

		MainMenuText[0] = "[ start a new game ]";	MainMenuText[6] = "[ trick list ]";
		MainMenuText[1] = "[ resume game ]";	MainMenuText[7] = "[ how to play ]";
		MainMenuText[2] = "[ freestyle mode ]";	MainMenuText[8] = "[ credits ]";
		MainMenuText[3] = "[ help/options ]";	MainMenuText[9] = "[ toggle speed ]";
		MainMenuText[4] = "[ quit ]";			MainMenuText[10]= "[ back to main ]";

		for(int k = 0; k <= 6; k++){
			MenuTextColor[k] = Color.blue;
		}

		int m = 0;	//int p = 0;
 
		do{
			Sin[m] = Math.sin(.01 * m);
//			Eqn[k] = k * k * .5;
			m++;
		}while(m <= 700);


//		do{
//			Eqn[p] = k * k * .5;
//			p++;	k++;
//		}while(p <= 700);

		initTrickStrings();

		Title = getImage(getCodeBase(), "xup/images/Title_screen.jpg");

		menu_img = getImage(getCodeBase(), "xup/images/main_menu.gif");
		Image Menu_Selector = getImage(getCodeBase(), "xup/images/menu_selector.gif");
		Options_Screen = getImage(getCodeBase(), "xup/images/options_menu.jpg");
		Options_Selector = getImage(getCodeBase(), "xup/images/options_selector.gif");
		PauseScreen = getImage(getCodeBase(), "xup/images/Pause_screen.gif");
		LoadingScreen = getImage(getCodeBase(), "xup/images/Loading_screen.jpg");
		
		
		trophy = getImage(getCodeBase(), "xup/images/trophy.jpg");
		
		Victory[0] = getImage(getCodeBase(), "xup/images/newspaper_1.jpg");
		Victory[1] = getImage(getCodeBase(), "xup/images/newspaper_2.jpg");
		Victory[2] = getImage(getCodeBase(), "xup/images/newspaper_3.jpg");
		
		HelpScreens[0] = getImage(getCodeBase(), "xup/images/help_screen_1.jpg");
		HelpScreens[1] = getImage(getCodeBase(), "xup/images/help_screen_2.jpg");
		HelpScreens[2] = getImage(getCodeBase(), "xup/images/help_screen_3.jpg");
		HelpScreens[3] = getImage(getCodeBase(), "xup/images/help_screen_4.jpg");
		HelpScreens[4] = getImage(getCodeBase(), "xup/images/help_screen_5.jpg");


		MainMenu[0] = getImage(getCodeBase(), "xup/images/compete.gif");
		MainMenu[1] = getImage(getCodeBase(), "xup/images/practice.gif");
		MainMenu[2] = getImage(getCodeBase(), "xup/images/options.gif");
		MainMenu[3] = getImage(getCodeBase(), "xup/images/quit.gif");

		OptionsMenu[0] = getImage(getCodeBase(), "xup/images/tricklist.gif");
		OptionsMenu[1] = getImage(getCodeBase(), "xup/images/resetgame.gif");
		OptionsMenu[2] = getImage(getCodeBase(), "xup/images/help.gif");
		OptionsMenu[3] = getImage(getCodeBase(), "xup/images/goback.gif");
		
		jump_meter = getImage(getCodeBase(), "xup/images/Powermeter.gif");
		jump_meter_box = getImage(getCodeBase(), "xup/images/Jump_meter_box.gif");

		currenttrick_points = getImage(getCodeBase(), "xup/images/trick_points.gif");
		combobox = getImage(getCodeBase(), "xup/images/combobonus.gif");

		Bike[0] = getImage(getCodeBase(), "xup/images/Riding_1.gif");
		Bike[1] = getImage(getCodeBase(), "xup/images/Riding_2.gif");
		
		Bike[2] = getImage(getCodeBase(), "xup/images/in_air.gif");
		
		Bike[3] = getImage(getCodeBase(), "xup/images/on_dropin2.gif");
		
		Bike[4] = getImage(getCodeBase(), "xup/images/landing.gif");
		
		Bike[5] = getImage(getCodeBase(), "xup/images/on_dropin.gif");

		Bike[6] = getImage(getCodeBase(), "xup/images/on_jump_1.gif");
		Bike[7] = getImage(getCodeBase(), "xup/images/on_jump_2.gif");
		Bike[8] = getImage(getCodeBase(), "xup/images/on_jump_3.gif");


		Crash[0][0] = getImage(getCodeBase(), "xup/images/crash00.gif");

		Crash[1][0] = getImage(getCodeBase(), "xup/images/fall_1.gif");
		Crash[1][1] = getImage(getCodeBase(), "xup/images/fall_2.gif");
		Crash[1][2] = getImage(getCodeBase(), "xup/images/fall_3.gif");
		Crash[1][3] = getImage(getCodeBase(), "xup/images/fall_4.gif");
		Crash[1][4] = getImage(getCodeBase(), "xup/images/fall_5.gif");
		Crash[1][5] = getImage(getCodeBase(), "xup/images/fall_6.gif");
		Crash[1][6] = getImage(getCodeBase(), "xup/images/fall_7.gif");
		Crash[1][7] = getImage(getCodeBase(), "xup/images/fall_8.gif");


		Background = getImage(getCodeBase(), "xup/images/bg.jpg");


		TrickAnim[0][0] = getImage(getCodeBase(), "xup/images/No_hndr_1.gif");
		TrickAnim[0][1] = getImage(getCodeBase(), "xup/images/No_hndr_2.gif");
		TrickAnim[0][2] = getImage(getCodeBase(), "xup/images/No_hndr_3.gif");
		TrickAnim[0][3] = getImage(getCodeBase(), "xup/images/No_hndr_4.gif");
		TrickAnim[0][4] = getImage(getCodeBase(), "xup/images/No_hndr_5.gif");
		
		TrickAnim[1][0] = getImage(getCodeBase(), "xup/images/Noft_1.gif");
		TrickAnim[1][1] = getImage(getCodeBase(), "xup/images/Noft_2.gif");
		TrickAnim[1][2] = getImage(getCodeBase(), "xup/images/Noft_3.gif");

		TrickAnim[2][0] = getImage(getCodeBase(), "xup/images/Xup_1.gif");
		TrickAnim[2][1] = getImage(getCodeBase(), "xup/images/Xup_2.gif");
		TrickAnim[2][2] = getImage(getCodeBase(), "xup/images/Xup_3.gif");
		
		TrickAnim[3][0] = getImage(getCodeBase(), "xup/images/Can_1.gif");
		TrickAnim[3][1] = getImage(getCodeBase(), "xup/images/Can_2.gif");
		TrickAnim[3][2] = getImage(getCodeBase(), "xup/images/Can_3.gif");
		
		TrickAnim[4][0] = getImage(getCodeBase(), "xup/images/Tobbogan_1.gif");
		TrickAnim[4][1] = getImage(getCodeBase(), "xup/images/Tobbogan_2.gif");
		TrickAnim[4][2] = getImage(getCodeBase(), "xup/images/Tobbogan_3.gif");
		TrickAnim[4][3] = getImage(getCodeBase(), "xup/images/Tobbogan_4.gif");
		
		TrickAnim[5][0] = getImage(getCodeBase(), "xup/images/360_1.gif");
		TrickAnim[5][1] = getImage(getCodeBase(), "xup/images/360_2.gif");
		TrickAnim[5][2] = getImage(getCodeBase(), "xup/images/360_3.gif");
		TrickAnim[5][3] = getImage(getCodeBase(), "xup/images/360_4.gif");
		TrickAnim[5][4] = getImage(getCodeBase(), "xup/images/360_5.gif");
		TrickAnim[5][5] = getImage(getCodeBase(), "xup/images/360_6.gif");

		TrickAnim[6][0] = getImage(getCodeBase(), "xup/images/Nfcan_1.gif");
		TrickAnim[6][1] = getImage(getCodeBase(), "xup/images/Nfcan_2.gif");
		TrickAnim[6][2] = getImage(getCodeBase(), "xup/images/Nfcan_3.gif");
		
		TrickAnim[7][0] = getImage(getCodeBase(), "xup/images/brspin_1.gif");
		TrickAnim[7][1] = getImage(getCodeBase(), "xup/images/brspin_2.gif");
		TrickAnim[7][2] = getImage(getCodeBase(), "xup/images/brspin_3.gif");
		TrickAnim[7][3] = getImage(getCodeBase(), "xup/images/brspin_4.gif");
		TrickAnim[7][4] = getImage(getCodeBase(), "xup/images/brspin_5.gif");

		TrickAnim[8][0] = getImage(getCodeBase(), "xup/images/Noth_1.gif");
		TrickAnim[8][1] = getImage(getCodeBase(), "xup/images/Noth_2.gif");
		TrickAnim[8][2] = getImage(getCodeBase(), "xup/images/Noth_3.gif");

		TrickAnim[9][0] = getImage(getCodeBase(), "xup/images/table_1.gif");
		TrickAnim[9][1] = getImage(getCodeBase(), "xup/images/table_2.gif");
		TrickAnim[9][2] = getImage(getCodeBase(), "xup/images/table_3.gif");
		
		TrickAnim[10][0] = getImage(getCodeBase(), "xup/images/nftxup_1.gif");
		TrickAnim[10][1] = getImage(getCodeBase(), "xup/images/nftxup_2.gif");
		TrickAnim[10][2] = getImage(getCodeBase(), "xup/images/nftxup_3.gif");
		
		TrickAnim[11][0] = getImage(getCodeBase(), "xup/images/Super_1.gif");
		TrickAnim[11][1] = getImage(getCodeBase(), "xup/images/Super_2.gif");
		TrickAnim[11][2] = getImage(getCodeBase(), "xup/images/Super_3.gif");
		TrickAnim[11][3] = getImage(getCodeBase(), "xup/images/Super_4.gif");
		TrickAnim[11][4] = getImage(getCodeBase(), "xup/images/Super_5.gif");
		
		TrickAnim[12][0] = getImage(getCodeBase(), "xup/images/turndwn_1.gif");
		TrickAnim[12][1] = getImage(getCodeBase(), "xup/images/turndwn_2.gif");
		TrickAnim[12][2] = getImage(getCodeBase(), "xup/images/turndwn_3.gif");
		
		TrickAnim[13][0] = getImage(getCodeBase(), "xup/images/tlwhip_1.gif");
		TrickAnim[13][1] = getImage(getCodeBase(), "xup/images/tlwhip_2.gif");
		TrickAnim[13][2] = getImage(getCodeBase(), "xup/images/tlwhip_3.gif");
		TrickAnim[13][3] = getImage(getCodeBase(), "xup/images/tlwhip_4.gif");
		TrickAnim[13][4] = getImage(getCodeBase(), "xup/images/tlwhip_5.gif");
		
		TrickAnim[14][0] = getImage(getCodeBase(), "xup/images/superseatgrb_1.gif");
		TrickAnim[14][1] = getImage(getCodeBase(), "xup/images/superseatgrb_2.gif");
		TrickAnim[14][2] = getImage(getCodeBase(), "xup/images/superseatgrb_3.gif");
		TrickAnim[14][3] = getImage(getCodeBase(), "xup/images/superseatgrb_4.gif");
		TrickAnim[14][4] = getImage(getCodeBase(), "xup/images/superseatgrb_5.gif");
		
		TrickAnim[15][0] = getImage(getCodeBase(), "xup/images/360_1.gif");
		TrickAnim[15][1] = getImage(getCodeBase(), "xup/images/360_2.gif");
		TrickAnim[15][2] = getImage(getCodeBase(), "xup/images/360_3.gif");
		TrickAnim[15][3] = getImage(getCodeBase(), "xup/images/360_4.gif");
		TrickAnim[15][4] = getImage(getCodeBase(), "xup/images/360_5.gif");
		TrickAnim[15][5] = getImage(getCodeBase(), "xup/images/360_6.gif");
		TrickAnim[15][6] = getImage(getCodeBase(), "xup/images/360_1.gif");
		TrickAnim[15][7] = getImage(getCodeBase(), "xup/images/360_2.gif");
		TrickAnim[15][8] = getImage(getCodeBase(), "xup/images/360_3.gif");
		TrickAnim[15][9] = getImage(getCodeBase(), "xup/images/360_4.gif");
		TrickAnim[15][10] = getImage(getCodeBase(), "xup/images/360_5.gif");
		TrickAnim[15][11] = getImage(getCodeBase(), "xup/images/360_6.gif");
		
		TrickAnim[16][0] = getImage(getCodeBase(), "xup/images/backflip_1.gif");
		TrickAnim[16][1] = getImage(getCodeBase(), "xup/images/backflip_2.gif");
		TrickAnim[16][2] = getImage(getCodeBase(), "xup/images/backflip_3.gif");
		TrickAnim[16][3] = getImage(getCodeBase(), "xup/images/backflip_4.gif");
		TrickAnim[16][4] = getImage(getCodeBase(), "xup/images/backflip_5.gif");
		TrickAnim[16][5] = getImage(getCodeBase(), "xup/images/backflip_6.gif");
		TrickAnim[16][6] = getImage(getCodeBase(), "xup/images/backflip_7.gif");

		
		TrickAnim[17][0] = getImage(getCodeBase(), "xup/images/backflip_1.gif");
		TrickAnim[17][1] = getImage(getCodeBase(), "xup/images/backflip_2.gif");
		TrickAnim[17][2] = getImage(getCodeBase(), "xup/images/backflip_3.gif");
		TrickAnim[17][3] = getImage(getCodeBase(), "xup/images/backflip_4.gif");
		TrickAnim[17][4] = getImage(getCodeBase(), "xup/images/backflip_5.gif");
		TrickAnim[17][5] = getImage(getCodeBase(), "xup/images/backflip_6.gif");
		TrickAnim[17][6] = getImage(getCodeBase(), "xup/images/backflip_7.gif");
		TrickAnim[17][7] = getImage(getCodeBase(), "xup/images/backflip_1.gif");
		TrickAnim[17][8] = getImage(getCodeBase(), "xup/images/backflip_2.gif");
		TrickAnim[17][9] = getImage(getCodeBase(), "xup/images/backflip_3.gif");
		TrickAnim[17][10] = getImage(getCodeBase(), "xup/images/backflip_4.gif");
		TrickAnim[17][11] = getImage(getCodeBase(), "xup/images/backflip_5.gif");
		TrickAnim[17][12] = getImage(getCodeBase(), "xup/images/backflip_6.gif");
		TrickAnim[17][13] = getImage(getCodeBase(), "xup/images/backflip_7.gif");

		TrickAnim[18][0] = getImage(getCodeBase(), "xup/images/360_1.gif");
		TrickAnim[18][1] = getImage(getCodeBase(), "xup/images/360_2.gif");
		TrickAnim[18][2] = getImage(getCodeBase(), "xup/images/360_3.gif");
		TrickAnim[18][3] = getImage(getCodeBase(), "xup/images/180.gif");



		level[0][0] = getImage(getCodeBase(), "xup/images/Lvl1_01.jpg");
		level[0][1] = getImage(getCodeBase(), "xup/images/Lvl1_02.jpg");
		level[0][2] = getImage(getCodeBase(), "xup/images/Lvl1_03.jpg");
		level[0][3] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][4] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][5] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][6] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][7] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][8] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][9] = getImage(getCodeBase(), "xup/images/Lvl1_2.jpg");
		level[0][10] = getImage(getCodeBase(), "xup/images/Lvl1_3.jpg");
		level[0][11] = getImage(getCodeBase(), "xup/images/Lvl1_4.jpg");
		level[0][12] = getImage(getCodeBase(), "xup/images/Lvl1_5.jpg");
		level[0][13] = getImage(getCodeBase(), "xup/images/Lvl1_5.jpg");
		level[0][14] = getImage(getCodeBase(), "xup/images/Lvl1_6.jpg");
		level[0][15] = getImage(getCodeBase(), "xup/images/Lvl1_7.jpg");
		level[0][16] = getImage(getCodeBase(), "xup/images/Lvl1_8.jpg");
		level[0][17] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
//		level[0][18] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
//		level[0][19] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][18] = getImage(getCodeBase(), "xup/images/Lvl1_2.jpg");
		level[0][19] = getImage(getCodeBase(), "xup/images/Lvl1_3.jpg");
		level[0][20] = getImage(getCodeBase(), "xup/images/Lvl1_4.jpg");
		level[0][21] = getImage(getCodeBase(), "xup/images/Lvl1_5.jpg");
		level[0][22] = getImage(getCodeBase(), "xup/images/Lvl1_5.jpg");
		level[0][23] = getImage(getCodeBase(), "xup/images/Lvl1_6.jpg");
		level[0][24] = getImage(getCodeBase(), "xup/images/Lvl1_7.jpg");
		level[0][25] = getImage(getCodeBase(), "xup/images/Lvl1_8.jpg");
		level[0][26] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
//		level[0][28] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
//		level[0][30] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][27] = getImage(getCodeBase(), "xup/images/Lvl1_2.jpg");
		level[0][28] = getImage(getCodeBase(), "xup/images/Lvl1_3.jpg");
		level[0][29] = getImage(getCodeBase(), "xup/images/Lvl1_4.jpg");
		level[0][30] = getImage(getCodeBase(), "xup/images/Lvl1_5.jpg");
		level[0][31] = getImage(getCodeBase(), "xup/images/Lvl1_5.jpg");
		level[0][32] = getImage(getCodeBase(), "xup/images/Lvl1_6.jpg");
		level[0][33] = getImage(getCodeBase(), "xup/images/Lvl1_7.jpg");
		level[0][34] = getImage(getCodeBase(), "xup/images/Lvl1_8.jpg");
		level[0][35] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][36] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][37] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][38] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][39] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][40] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][41] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][42] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");
		level[0][43] = getImage(getCodeBase(), "xup/images/Lvl1_1.jpg");

	//  Don't wait for level images until they are needed

		tracker = new MediaTracker(this);		

		tracker.addImage(LoadingScreen, 0);

		tracker.addImage(Title, 1);

		tracker.addImage(menu_img, 2);

		tracker.addImage(Menu_Selector, 3);
		tracker.addImage(Options_Screen, 4);
		tracker.addImage(Options_Selector, 5);
		tracker.addImage(PauseScreen, 6);
		
		tracker.addImage(trophy, 6);
		
		tracker.addImage(Victory[0], 6);
		tracker.addImage(Victory[1], 6);
		tracker.addImage(Victory[2], 6);

		tracker.addImage(HelpScreens[0], 7);
		tracker.addImage(HelpScreens[1], 8);
		tracker.addImage(HelpScreens[2], 9);
		tracker.addImage(HelpScreens[3], 10);
		tracker.addImage(HelpScreens[4], 11);

/*		tracker.addImage(MainMenu[0], 7);
		tracker.addImage(MainMenu[1], 8);
		tracker.addImage(MainMenu[2], 9);
		tracker.addImage(MainMenu[3], 10);
*/
		tracker.addImage(OptionsMenu[0], 11);
		tracker.addImage(OptionsMenu[1], 12);
		tracker.addImage(OptionsMenu[2], 13);
		tracker.addImage(OptionsMenu[3], 14);

		tracker.addImage(jump_meter, 15);
		tracker.addImage(jump_meter_box, 16);

		tracker.addImage(currenttrick_points, 16);
		tracker.addImage(combobox, 16);

		tracker.addImage(Bike[0], 17);
		tracker.addImage(Bike[1], 17);
		
		tracker.addImage(Bike[2], 17);

		tracker.addImage(Bike[3], 17);
		
		tracker.addImage(Bike[4], 18);
		
		tracker.addImage(Bike[5], 18);

		tracker.addImage(Bike[6], 18);
		tracker.addImage(Bike[7], 18);
		tracker.addImage(Bike[8], 18);


		tracker.addImage(Crash[0][0], 18);

		tracker.addImage(Crash[1][0], 18);
		tracker.addImage(Crash[1][1], 18);
		tracker.addImage(Crash[1][2], 18);
		tracker.addImage(Crash[1][3], 18);
		tracker.addImage(Crash[1][4], 18);
		tracker.addImage(Crash[1][5], 18);
		tracker.addImage(Crash[1][6], 18);
		tracker.addImage(Crash[1][7], 18);


		tracker.addImage(Background, 18);

		tracker.addImage(TrickAnim[0][0], 19);
		tracker.addImage(TrickAnim[0][1], 19);
		tracker.addImage(TrickAnim[0][2], 19);
		tracker.addImage(TrickAnim[0][3], 19);
		tracker.addImage(TrickAnim[0][4], 19);

		
		tracker.addImage(TrickAnim[1][0], 20);
		tracker.addImage(TrickAnim[1][1], 20);
		tracker.addImage(TrickAnim[1][2], 20);

		tracker.addImage(TrickAnim[2][0], 21);
		tracker.addImage(TrickAnim[2][1], 21);
		tracker.addImage(TrickAnim[2][2], 21);
		
		tracker.addImage(TrickAnim[3][0], 22);
		tracker.addImage(TrickAnim[3][1], 22);
		tracker.addImage(TrickAnim[3][2], 22);
		
		tracker.addImage(TrickAnim[4][0], 23);
		tracker.addImage(TrickAnim[4][1], 23);
		tracker.addImage(TrickAnim[4][2], 23);
		tracker.addImage(TrickAnim[4][3], 23);
		
		tracker.addImage(TrickAnim[5][0], 24);
		tracker.addImage(TrickAnim[5][1], 24);
		tracker.addImage(TrickAnim[5][2], 24);
		tracker.addImage(TrickAnim[5][3], 24);
		tracker.addImage(TrickAnim[5][4], 24);
		tracker.addImage(TrickAnim[5][5], 24);
		
		tracker.addImage(TrickAnim[6][0], 25);
		tracker.addImage(TrickAnim[6][1], 25);
		tracker.addImage(TrickAnim[6][2], 25);
		
		tracker.addImage(TrickAnim[7][0], 26);
		tracker.addImage(TrickAnim[7][1], 26);
		tracker.addImage(TrickAnim[7][2], 26);
		tracker.addImage(TrickAnim[7][3], 26);
		tracker.addImage(TrickAnim[7][4], 26);

		tracker.addImage(TrickAnim[8][0], 27);
		tracker.addImage(TrickAnim[8][1], 27);
		tracker.addImage(TrickAnim[8][2], 27);

		tracker.addImage(TrickAnim[9][0], 28);
		tracker.addImage(TrickAnim[9][1], 28);
		tracker.addImage(TrickAnim[9][2], 28);
		
		tracker.addImage(TrickAnim[10][0], 29);
		tracker.addImage(TrickAnim[10][1], 29);
		tracker.addImage(TrickAnim[10][2], 29);
		
		tracker.addImage(TrickAnim[11][0], 30);
		tracker.addImage(TrickAnim[11][1], 30);
		tracker.addImage(TrickAnim[11][2], 30);
		tracker.addImage(TrickAnim[11][3], 30);
		tracker.addImage(TrickAnim[11][4], 30);
		
		tracker.addImage(TrickAnim[12][0], 31);
		tracker.addImage(TrickAnim[12][1], 31);
		tracker.addImage(TrickAnim[12][2], 31);

		tracker.addImage(TrickAnim[13][0], 32);
		tracker.addImage(TrickAnim[13][1], 32);
		tracker.addImage(TrickAnim[13][2], 32);
		tracker.addImage(TrickAnim[13][3], 32);
		tracker.addImage(TrickAnim[13][4], 32);
		
		tracker.addImage(TrickAnim[14][0], 33);
		tracker.addImage(TrickAnim[14][1], 33);
		tracker.addImage(TrickAnim[14][2], 33);
		tracker.addImage(TrickAnim[14][3], 33);
		tracker.addImage(TrickAnim[14][4], 33);

		tracker.addImage(TrickAnim[15][0], 34);
		tracker.addImage(TrickAnim[15][1], 34);
		tracker.addImage(TrickAnim[15][2], 34);
		tracker.addImage(TrickAnim[15][3], 34);
		tracker.addImage(TrickAnim[15][4], 34);
		tracker.addImage(TrickAnim[15][5], 34);
		tracker.addImage(TrickAnim[15][6], 34);
		tracker.addImage(TrickAnim[15][7], 34);
		tracker.addImage(TrickAnim[15][8], 34);
		tracker.addImage(TrickAnim[15][9], 34);
		tracker.addImage(TrickAnim[15][10], 34);
		tracker.addImage(TrickAnim[15][11], 34);
		
		tracker.addImage(TrickAnim[16][0], 35);
		tracker.addImage(TrickAnim[16][1], 35);
		tracker.addImage(TrickAnim[16][2], 35);
		tracker.addImage(TrickAnim[16][3], 35);
		tracker.addImage(TrickAnim[16][4], 35);
		tracker.addImage(TrickAnim[16][5], 35);
		tracker.addImage(TrickAnim[16][6], 35);

		tracker.addImage(TrickAnim[17][0], 36);

		tracker.addImage(TrickAnim[18][0], 37);
		tracker.addImage(TrickAnim[18][1], 37);
		tracker.addImage(TrickAnim[18][2], 37);
		tracker.addImage(TrickAnim[18][3], 37);


		tracker.addImage(level[0][0], 38);
		tracker.addImage(level[0][1], 38);
		tracker.addImage(level[0][2], 38);
		tracker.addImage(level[0][8], 38);
		tracker.addImage(level[0][9], 39);
		tracker.addImage(level[0][10], 40);
		tracker.addImage(level[0][11], 41);
		tracker.addImage(level[0][12], 42);
		tracker.addImage(level[0][13], 43);
		tracker.addImage(level[0][14], 44);
		tracker.addImage(level[0][15], 45);
		tracker.addImage(level[0][16], 46);

		try{
			tracker.waitForID(0);
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }

		if((tracker.statusID(0, true) & MediaTracker.ERRORED) != 0) {
			System.out.println("Error loading images!");

			g3.setFont(f);
			g3.setColor(Color.red);
			g3.fillRect(0, 0, 360, 300);
			g3.setColor(Color.black);
			g3.drawString("Error loading images!", 120, 140);
			tempimg = offimg2;
			repaint();

			stop();
		}

//		showLoading();
		{
		int percent_complete = 0, img_num = NUM_IMAGES;
		int increment = (int)(100 / NUM_IMAGES);
		
		g3.drawImage(LoadingScreen, 0, 0, this);

		while(percent_complete < 100){
			try{
				tracker.waitForID(img_num);
			}
			catch(InterruptedException e)
			{
				return;
			}

			percent_complete += increment;

			g3.setColor(Color.yellow);
			g3.drawRect(135, 350, 100, 8);
			g3.fillRect(135, 350, percent_complete, 8);

			tempimg = offimg2;
			repaint();

			img_num--;
		}
		}
/*
		try{
			tracker.waitForID(1);
		}
	
		catch (InterruptedException e) {
			System.out.println("InterruptedException encountered!");
			return;
		 }

		if((tracker.statusID(1, true) & MediaTracker.ERRORED) != 0) {
			System.out.println("Error loading images!");
/*
			g3.setFont(f);
			g3.setColor(Color.red);
			g3.fillRect(0, 0, 360, 300);
			g3.setColor(Color.black);
			g3.drawString("Error loading images!", 120, 140);
			tempimg = offimg2;
			repaint();

			stop();


		}
*/
//		System.out.println("TOTAL MEMORY: " + Runtime.getRuntime().totalMemory());
//		System.out.println("FREE MEMORY : " + Runtime.getRuntime().freeMemory());

		Runtime.getRuntime().gc();
		pause(25);

//		System.out.println("FREE MEMORY : " + Runtime.getRuntime().freeMemory());

		//		The whole game is infinitely looping in here:
		do{
		
//      Title Screen, High Scores List Displayed Here             

			g2.drawImage(Title, 0, 0, this);
			tempimg = offimg;
			repaint();

		do{
			
			int k = 0;
			if(k >= 10){
				g2.drawImage(Title, 0, 0, this);
				tempimg = offimg;
				repaint();
				
				k = 0;
			}
			
			k++;
			pause(20);

//			doHighScoresList();
//			pause(500);
		}while(!START_GAME);

//      Game Loop (Menu, Main game, back to Menu)
		do{

//		Main Menu Loop 
//	doMainMenu();
//		menu_position = 1;

			if(!SKIP_MENU){
			
		g2.drawImage(menu_img, 129, 50, this);
		pause(26);

		g3.drawImage(offimg, 0, 0, this);
		pause(26);

		tempimg = offimg2;
		repaint();

//		g2.drawImage(Title, 0, 0, this);

//		g3.drawImage(offimg, 0, 0, this);
/*
//Draw the Main Menu

		g3.setFont(menuFont);

		g3.setColor(Color.black);
		g3.fillRoundRect(130, 50, 160, 300, 15, 15);

		g3.setColor(Color.white);
		g3.drawRoundRect(130, 50, 160, 300, 15, 15);
		g3.drawRoundRect(135, 100, 150, 200, 15, 15);

		g3.drawString("X-Up", 200, 70);
		g3.drawString("< main menu >", 170, 80);


//		g3.drawImage(MainMenu[menu_position - 1], MenuPos[menu_position - 1], 100, this);

		int tempy = 160;

		for(int k = 0; k <= 4; k++){
			g3.setColor(MenuTextColor[k]);
			g3.drawString(MainMenuText[k], MenuPos[k], tempy);

			tempy += 20;
		}

		pause(26);

		tempimg = offimg2;
		repaint();
*/
//		stop();
/* */		
		menu_position = temp_selection = 1;

/*		do{
			if(REDRAW_MENU){

				selectorY = 40 * menu_position + 60;
	
				g3.drawImage(offimg, 0, 0, this);
				g3.drawImage(Menu_Selector, 114, selectorY, this);

				pause(26);

				tempimg = offimg2;
				repaint();

				REDRAW_MENU = false;

			}

			if(SHOWING_TRICK_LIST){
				MAIN_MENU = false;

				first_trick_shown = 1;
				listY = 125;

				REDRAW_LIST = true;

				do{
					if(REDRAW_LIST){
						listY = 145;

						g3.setColor(Color.black);
						g3.fillRect(15, 80, 390, 210);
						g3.setColor(Color.black);
						g3.fillRect(30, 127, 350, 100);

						g3.setColor(Color.white);
						g3.drawRect(15, 80, 390, 210);
						g3.drawRect(25, 127, 370, 110);

						g3.setColor(Color.yellow);
						g3.setFont(f);

						g3.drawString("Trick List", 190, 100);
						g3.drawString("Your skill level is " + skill_level, 160, 120);
						g3.drawString("Up and Down: Scroll the list", 140, 257);
						g3.drawString("Enter: Return to the Main Menu", 130, 274);

						i = first_trick_shown;

						g3.setFont(trick_how_to_font);

						do{
							if(i <= skill_level){

//								g3.setFont(trick_list_font);
								g3.drawString(TrickListInfo[i]+"  "+TrickHowTo[i], 32, listY);

//								g3.setFont(trick_how_to_font);
//								g3.drawString(TrickHowTo[i], 180, listY);
							}

							listY += 17;

							i++;
						}while(i < first_trick_shown + 6);
	
						tempimg = offimg2;
						repaint();

						REDRAW_LIST = false;
						}

					System.out.println(first_trick_shown);

				}while(SHOWING_TRICK_LIST);

				MAIN_MENU = true;
				REDRAW_MENU = true;
			}

		}while(MAIN_MENU);
*/

		MAIN_MENU = true;

		doMainMenu();
			}// end if(!SKIP_MENU)
			
			
			
		redrawOffscn();

////////////////////////////
//		panIncrement = 6;
		panIncrement = SpeedSetting;
////////////////////////////

		current_sector = 0;
		combo = 0;

//		loadLevel(0);

		g3.setFont(dispFont);

		current_score = 0;
		
		clearOffscn();
//		jump_factor = 120;
		

		if(!QUIT){

			// For main game: show next comp screen here, with like
			// a 'Press Spacebar To Continue' prompt or something
			if(GAME_IN_PROGRESS)
				showNextComp();
			
			
		// For The 2 run competition mode \\
		for(MainGameLoop = 1; MainGameLoop <= 2; MainGameLoop++){
			
			
//--------------------------------
// Drop - in ramp code goes here
			
				panX = 0;
			
				int RX = 5, RY = 184, addend = 0;

				redrawOffscn();
				
				for(int k = 1; k <= 20; k++){
					
					g3.drawImage(offimg, 0, 0, this);

					g3.setColor(Color.black);	
					g3.fillRect(panX, 362, 420, 40);
					g3.drawImage(jump_meter, 220+panX, 373, this);
					g3.drawImage(jump_meter_box, (meter_position*16+panX+230), 367, this);

					g3.setColor(Color.white);
					g3.drawString("Score: " + current_score, 20+panX, 387);
					
					if(PRACTICE_MODE == 1){
						g3.setColor(Color.black);
						g3.drawString("Freestyle Mode -- Press \'Q\' to quit.", 90 + panX, 15);
						g3.drawString("Press \'P\' to pause/unpause.", 120 + panX, 35);
						g3.drawString("Press \'T\' to see the trick list.", 115 + panX, 55);
					}
					else{
						g3.setColor(Color.black);
						if(MainGameLoop == 1)
							g3.drawString(CompType+" - First Run", 110 + panX, 20);
						else
							g3.drawString(CompType+" - Second Run", 110 + panX, 20);

						g3.drawString("Press \'T\' to see the trick list.", 100 + panX, 35);
					}
					
					
					g3.drawImage(Bike[5], RX, RY, this);
					
					if(k <= 2)
						addend = 1;
					else if(k <= 5)
						addend = 2;
					else if(k <= 9)
						addend = 3;
					else if(k <= 14)
						addend = 4;
					else
						addend = 5;
					
					RX += addend;
					RY += addend;
					
					tempimg=offimg2;
					repaint();
					pause(25);
				}
				
				RY -= 10;	RX += 5;
				
				for(int k = 0; k <= 1; k++){
					
					g3.drawImage(offimg, 0, 0, this);

					g3.setColor(Color.black);	
					g3.fillRect(panX, 362, 420, 40);
					g3.drawImage(jump_meter, 220+panX, 373, this);
					g3.drawImage(jump_meter_box, (meter_position*16+panX+230), 367, this);

					g3.setColor(Color.white);
					g3.drawString("Score: " + current_score, 20+panX, 387);
					
					if(PRACTICE_MODE == 1){
						g3.setColor(Color.black);
						g3.drawString("Freestyle Mode -- Press \'Q\' to quit.", 90 + panX, 15);
						g3.drawString("Press \'P\' to pause/unpause.", 120 + panX, 35);
						g3.drawString("Press \'T\' to see the trick list.", 115 + panX, 55);
					}
					else{
						g3.setColor(Color.black);
						if(MainGameLoop == 1)
							g3.drawString(CompType+" - First Run", 110 + panX, 20);
						else
							g3.drawString(CompType+" - Second Run", 110 + panX, 20);

						g3.drawString("Press \'T\' to see the trick list.", 100 + panX, 35);
					}
					
					g3.drawImage(Bike[3], RX, RY, this);

					RX += 6;
					RY += 1;
					
					tempimg=offimg2;
					repaint();
					pause(25);
				}
				
			
//--------------------------------

			
//		current_sector = 3;		

		
		current_run_trick_num = 0;
		
		showLoading();
//			pause(1000);
		
		DONE = false;

		
//      Start Main Level Loop
		do{
//			if(!SHOWING_TRICK_LIST){

			panScreen(); 

			g3.drawImage(offimg, 0, 0, this);

			g3.setColor(Color.black);

			if(EXECUTING_TRICK){
				g3.drawImage(currenttrick_points, 159 + panX, 324, this);
				g3.drawString(currenttrickpoints+" ", 190 + panX, 355);
			}

			else if(POINT_TRANSFER){
				currenttrickpoints -= trickpointsdecrement;
				current_score += trickpointsdecrement;

				g3.drawImage(currenttrick_points, 159 + panX, 324, this);
				g3.drawString(currenttrickpoints+" ", 190 + panX, 355);

				if(currenttrickpoints <= 0){
					currenttrickpoints = 0;
					POINT_TRANSFER = false;
				}

			}
			
			g3.fillRect(panX, 362, 420, 40);
			g3.drawImage(jump_meter, 220+panX, 373, this);
			g3.drawImage(jump_meter_box, (meter_position*16+panX+230), 367, this);


			g3.setColor(Color.white);
			
			if(SHOW_COMBOBOX){
				g3.drawImage(combobox, 140+panX, 50, this);
				g3.drawString(ComboPoints[combo]+" ", 200+panX, 100);
			}

			g3.setFont(credits_font_small);
			g3.drawString("Last Trick: " + last_trick, 20+panX, 395);
			g3.setFont(dispFont);
			
			g3.drawString("Score: " + current_score, 20+panX, 380);
			
			bikeY = drawBike();

//			g3.drawImage(menu_img, 129, 50, this);

			if(PRACTICE_MODE == 1){
				if(current_sector + 6 >= 44)
					current_sector = 1;

				g3.setColor(Color.black);
				g3.drawString("Freestyle Mode -- Press \'Q\' to quit.", 90 + panX, 15);
				g3.drawString("Press \'P\' to pause/unpause.", 120 + panX, 35);
				g3.drawString("Press \'T\' to see the trick list.", 115 + panX, 55);

			}
			else if(PRACTICE_MODE == 2){
				panX = x = 0;
				MAIN_MENU = true;
				IN_THE_AIR = LANDING = false;
					jump_length = 1;
					ramplength = 0;

//					panIncrement = 6;
					panIncrement = SpeedSetting;

					meter_position = 0;
					jump_factor = 0;
					current_trick = "";
					
					DONE = true;
//				break;
			}
			else{
				g3.setColor(Color.black);
				if(MainGameLoop == 1)
					g3.drawString(CompType+" - First Run", 110 + panX, 20);
				else
					g3.drawString(CompType+" - Second Run", 110 + panX, 20);

				g3.drawString("Press \'T\' to see the trick list.", 100 + panX, 35);
			}
			
			if(GAME_PAUSED){
				g3.drawImage(PauseScreen, 140+panX, 150, this);

				if(TRICK_LIST){
//					System.out.println("first_trick_shown"+first_trick_shown);

						listY = 145;

						g3.setColor(Color.black);
						g3.fillRect(15+panX, 80, 390, 210);
						g3.setColor(Color.black);
						g3.fillRect(30+panX, 127, 350, 100);

						g3.setColor(Color.white);
						g3.drawRect(15+panX, 80, 390, 210);
						g3.drawRect(25+panX, 127, 370, 110);

						g3.setColor(Color.yellow);
						g3.setFont(f);

						g3.drawString("Trick List", 190+panX, 100);
						g3.drawString("Your skill level is " + skill_level, 160+panX, 120);
						g3.drawString("Up and Down: Scroll the list", 115+panX, 257);
						g3.drawString("Press \'T\' to return to the game", 115+panX, 274);

						I = first_trick_shown;

						g3.setFont(trick_how_to_font);

						do{
							if(I <= skill_level){

//								g3.setFont(trick_list_font);
								g3.drawString(TrickListInfo[I]+"  "+TrickHowTo[I], 32+panX, listY);

//								g3.setFont(trick_how_to_font);
//								g3.drawString(TrickHowTo[I], 180+panX, listY);
							}
							listY += 17;

							I++;
						}while(I < first_trick_shown + 6);

				}
			}


			tempimg = offimg2;
			x = 0 - panX;
			repaint();

			pause(25);

			if(JumpLocation[current_sector + 3] > 0){
				if(jump_factor == 0)
					meter_position++;

				if(meter_position > 9)
					meter_position = 9;

				ramplength += panIncrement + 5;
			}
			else if(JumpLocation[current_sector + 3] < 0){
				if(jump_factor == 0){
					meter_position = 0;
					ramplength = 0;

//					crash(1);
					IN_THE_AIR = true;					
//					System.out.println("You crashed!!!");
				}
				else{
					IN_THE_AIR = true;
//					System.out.println("JumpFactor: "+jump_factor);
				}
			}

			if(IN_THE_AIR && !GAME_PAUSED){
//				panIncrement = 8;
				panIncrement = SpeedSetting + 2;

				if(power == 4)
//					panIncrement = 6;
					panIncrement = SpeedSetting;
				else if(power == 2)
//					panIncrement = 7;
					panIncrement = SpeedSetting + 1;
				else if(power == 0)
//					panIncrement = 8;
					panIncrement = SpeedSetting + 2;

				jump_length+=panIncrement;

				if(EXECUTING_TRICK && !FINISHING_TRICK)
					startTrick();

				else if(EXECUTING_TRICK && FINISHING_TRICK)
					finishTrick();

// Execute tricks
				else if(!EXECUTING_TRICK && !LANDING && power > 0){
					end_time = System.currentTimeMillis();
					if((end_time - start_time) >= 350 && current_trick != ""){

//						i = 1;
						trick = 0;

//							do{
						for(i = 1; i <= 25; i++){ 
								if(current_trick.equals(KeyCombos[i]) && i <= skill_level)
									trick = i;
						}
//								i++;
//							}while(i <= 25);

						if(trick > 0){
							trick_wait_length = TrickWaitLength[trick - 1];
							startTrick();
						}
								
						current_trick = "";
					}
				}

				if(jump_length >= 335 && !LANDING){

					if(EXECUTING_TRICK)
						crash();

					else{
						LANDING = true;
						bikeY_on_ramp = bikeY + 20;
						
						if(combo > 1){
							SHOW_COMBOBOX = true;
						}
					}
				}


/*				if(jump_length >= 360 && bikeY < 190){
					bikeY = 190;
				}
*/
// Successful landing of jump
				if(bikeY >= 250){
					IN_THE_AIR = false;
					LANDING = false;
					SHOW_COMBOBOX = false;
					current_score += ComboPoints[combo];
					combo = 0;
					
					jump_length = 1;
					ramplength = meter_position = jump_factor = 0;

//					panIncrement = 6;
					panIncrement = SpeedSetting;

					current_trick = "";
					
//					ramplength = 0 - 60;
				}
			}



//			System.out.println("Jump Length: "+jump_length);
//			}
		}while(!DONE);
//		End Main Level Loop

		DONE = false;
		FREESTYLE_MODE = false;
		panX = x = 0;
		IN_THE_AIR = LANDING = false;
		jump_length = 1;
		ramplength = 0;

//		panIncrement = 6;
		panIncrement = SpeedSetting;

		meter_position = 0;
		jump_factor = 0;
		current_trick = "";
		
		if(PRACTICE_MODE == 2){
			PRACTICE_MODE = 0;
			MainGameLoop += 2;
		}
		else{
	
			// Bike exits stage left
		for(int e = 120; e <= 420; e+=(panIncrement + 2)){
					g3.drawImage(offimg, 0, 0, this);

					g3.setColor(Color.black);	
					g3.fillRect(panX, 362, 420, 40);
					g3.drawImage(jump_meter, 220+panX, 373, this);
					g3.drawImage(jump_meter_box, (meter_position*16+panX+230), 367, this);

					g3.setColor(Color.white);
					g3.drawString("Score: " + current_score, 20+panX, 387);

						g3.setColor(Color.black);
						if(MainGameLoop == 1)
							g3.drawString(CompType+" - First Run", 110 + panX, 20);
						else
							g3.drawString(CompType+" - Second Run", 110 + panX, 20);
					
						g3.drawImage(Bike[pete], e, 255, this);
						pete = 1 - pete;
						
						tempimg = offimg2;
						repaint();
						pause(25);
		}
						
				
			// If second run, Determine which run's score will be kept
			if(MainGameLoop == 2){
				if(firstrun_score > current_score){
					current_score = firstrun_score;
					best_run = 1;
				}
				else
					best_run = 2;
			}
			// Else Set firstrun_score = current_score
			else{
				firstrun_score = current_score;
				current_score = 0;
			}
		}
		

		
		
		if(MainGameLoop == 1){
						
			g3.drawString("Press space to start your second run.", ((420 - fm_disp.stringWidth("Press space to start your second run.")) / 2), 200);
			tempimg = offimg2;
			repaint();
			
			WAITING_FOR_SPACEBAR = true;
			while(WAITING_FOR_SPACEBAR){
				pause(25);
			}
			current_sector = 0;
		}

		
		} // end of for loop for the 2 run comp mode

		if(GAME_IN_PROGRESS){
///////////////////////////////////////////////////////////////////////////////
		// Main Game:  Run Debriefing goes in here, with advancement and 
		// Incrementation of skill points if applicable
		
		// At this point, important variables are:
		//  current_score, LevelPlacementScores, lowestplace, skill_level
		
		showDebriefing();
		
		
		// If ranking is not enough to advance, show last trophy won
		// (e.g. 2nd Place Comp. 4 Trophy)
		
		// Skill Level Stays at 5 for first 2 (local) comps... after completion
		//  of second local comp, skill level goes to 10
		// Skill Point increases from then on are based on rankings...
		// 1st place - Move up 6 levels
		// 2nd place - Move up 3 levels
		// 3rd place - Move up 2 levels
		
		// Update CompHistory[current_comp] with the results
		// Show history screen, with brief summary
		// Of rank / score acheived at each comp participated in
		
		
		// From here it will be possible to return to main menu
		// To consult with help or trick list or other menu items,
		// Then return to their game in progress by using the resume option
		// In the main menu.

		showHistory();
		
		// Clear out CurrentRun[]
		for(int q = 0; q < 50; q++){
			CurrentRun[0][q] = "";
			CurrentRun[1][q] = "";
			CurrentRun[2][q] = "";
		}
			
			
///////////////////////////////////////////////////////////////////////////////
		}
		
		if(PROGTEMP){
			GAME_IN_PROGRESS = true;
			PROGTEMP = false;
		}
		
		}// End if(!QUIT){

		
		}while(!QUIT);

		QUIT = START_GAME = MAIN_MENU = false;

		}while(true);

	}


	private int drawBike(){
		int bikeX = 120+panX, bikeY = 0;

		if(!IN_THE_AIR && !LANDING){
			bikeY = 255 - ramplength;
			
			if(!GAME_PAUSED)
				pete = 1 - pete;

			if(ramplength > 0){
				if(ramplength < 20)
					g3.drawImage(Bike[6], bikeX, bikeY, this);
				else if(ramplength < 40)
					g3.drawImage(Bike[7], bikeX, bikeY, this);
				else
					g3.drawImage(Bike[8], bikeX, bikeY, this);
			}
			else
				g3.drawImage(Bike[pete], bikeX, bikeY, this);
		}

		if(IN_THE_AIR){
		if(!LANDING && !EXECUTING_TRICK){
			bikeY = (int)(0 - (jump_factor * (Sin[jump_length])) + 165); 
//            bikeY = (int)((jump_factor * Eqn[jump_length]) + 145);

			g3.drawImage(Bike[2], bikeX, bikeY, this);

		}

		else if(EXECUTING_TRICK){
			bikeY = (int)(0 - (jump_factor * (Sin[jump_length])) + 165); 

			if(trick_image_num != 0)
				g3.drawImage(TrickAnim[trick - 1][trick_image_num - 1], bikeX, bikeY, this);
		}
		
		else /* if(LANDING) */{
			if(!GAME_PAUSED){
				bikeY_on_ramp += 4;

				if(power == 1)
					bikeY_on_ramp+=3;
				else if(power == 2)
					bikeY_on_ramp+=2;
				else if(power == 0)
					bikeY_on_ramp+=4;
			}

			bikeY = bikeY_on_ramp;

			g3.drawImage(Bike[4], bikeX, bikeY, this);
		}
		}

		return(bikeY);
	}

	
	public boolean mouseDown(Event evt, int x, int y){
			if(!START_GAME){
				START_GAME = true;
				MAIN_MENU = true;
				QUIT = false;
			}
				if(MAIN_MENU){
					switch(menu_position){
					case 1:
						// Start New Game
						// If game in progress, prompt with confirmation window:
						if(GAME_IN_PROGRESS){
							System.out.println("Erase current game in progress (Y/N)?");
							
							//if y then {
							//skill_level = 5;
							//current_level = current_comp = 1;
							//GAME_IN_PROGRESS = true;
							
							// Erase CompHistory[]
							for(int q = 0; q <= NUM_LEVELS; q++)
								CompHistory[q] = "-";
							
							//MAIN_MENU = false;
							//}
						}
							
						// Otherwise, proceed to level 1, and first comp
						
						skill_level = 5;
						current_level = current_comp = 1;
						loadLevel(current_level);
						GAME_IN_PROGRESS = true;
						DONE = false;
						
						MAIN_MENU = false;
						
						
						break;
					case 2:
						// Resume Current Game
						// First, check to see if current game exists
						if(GAME_IN_PROGRESS){
							current_level = current_comp;
							loadLevel(current_level);
							
							//showHistory();
	
							MAIN_MENU = false;
						}
						else{
						// If not, prompt with window "No game in progress!! 
						// Please go to 'start new' to start a new game"
						}
						
						break;
					case 3:
						// Freestyle Mode

						// Setting skill level the fullest so you can practice all the tricks
						old_skill = skill_level;
						skill_level = 19;

						// Set level to 0 for freestyle mode
						current_level = 0;
						loadLevel(0);
						
						PROGTEMP = GAME_IN_PROGRESS;
						GAME_IN_PROGRESS = false;
						
						PRACTICE_MODE = 1;
						FREESTYLE_MODE = true;
						MAIN_MENU = false;
						break;
					case 4:
						// Help
						OPTIONS_MENU = true;
						REDRAW_OPTIONS = true;
						break;
					case 5:
						// Save and Quit
						QUIT = true;
						MAIN_MENU = false;
						break;
					}
				}
				else if(OPTIONS_MENU){
					switch(menu_position){
					case 1: // Trick List
						SHOWING_TRICK_LIST = true;
						OPTIONS_MENU = false;
//                      doTrickList();
						break;
					case 2: // How To Play
						SHOWING_HELP = true;
						REDRAW_HELP = true;
						OPTIONS_MENU = false;
						break;
					case 3: // Credits
						SHOWING_CREDITS = true;

						REDRAW_OPTIONS = true;
						break;
					case 4: // Toggle Game Speed
						toggleGameSpeed();
						
						REDRAW_OPTIONS = true;
						break;
					case 5: // back to main
						OPTIONS_MENU = false;
						break;
					}
				}
				else if(SHOWING_HELP){
					helpimgnum++;

					if(helpimgnum > 4)
						helpimgnum = 0;

					REDRAW_HELP = true;
				}


			return true;
	}		

	public boolean mouseMove(Event evt, int x, int y){

//		System.out.println("mouseMove()");

		if(MAIN_MENU){
			if(menu_position != 0)
				MenuTextColor[menu_position - 1] = Color.blue;

			if(x > 130 && x < 290 && y > 130){
				if(y < 150){
					menu_position = 1;
					MenuTextColor[0] = Color.white;
				}
				else if(y < 170){
					menu_position = 2;
					MenuTextColor[1] = Color.white;
				}
				else if(y < 190){
					menu_position = 3;
					MenuTextColor[2] = Color.white;
				}
				else if(y < 210){
					menu_position = 4;
					MenuTextColor[3] = Color.white;
				}
				else if(y < 230){
					menu_position = 5;
					MenuTextColor[4] = Color.white;
				}
				else if(y < 250){
					menu_position = 6;
					MenuTextColor[5] = Color.white;
				}

				REDRAW_MENU = true;

//				System.out.println("MenuPosition: "+menu_position);

			}		

		}

		else if(OPTIONS_MENU){
			if(menu_position != 0)
				MenuTextColor[menu_position + 5] = Color.blue;

			if(x > 130 && x < 290 && y > 130){
				if(y < 150){
					menu_position = 1;
					MenuTextColor[6] = Color.white;
				}
				else if(y < 170){
					menu_position = 2;
					MenuTextColor[7] = Color.white;
				}
				else if(y < 190){
					menu_position = 3;
					MenuTextColor[8] = Color.white;
				}
				else if(y < 210){
					menu_position = 4;
					MenuTextColor[9] = Color.white;
				}
				else if(y < 230){
					menu_position = 5;
					MenuTextColor[10] = Color.white;
				}


				REDRAW_OPTIONS = true;

//				System.out.println("OptionsPosition: "+menu_position);

			}					
		}
		return true;
	}
	
	public boolean keyDown(Event evt, int key){

		if(!GAME_PAUSED){

		switch(key){
			case Event.DOWN:  
				if(OPTIONS_MENU){
					if(menu_position >= 4)
						menu_position = 1;
					else
						menu_position++;

					REDRAW_OPTIONS = true;
				}

				else if(SHOWING_TRICK_LIST){
					if(first_trick_shown < skill_level - 5){
						first_trick_shown++;
						REDRAW_LIST = true;
					}
				}

				else if(IN_THE_AIR && !EXECUTING_TRICK){
					current_trick += "D";
					start_time = System.currentTimeMillis();
				}
						break;
			case Event.UP:  
				if(OPTIONS_MENU){
					if(menu_position <= 1)
						menu_position = 4;
					else
						menu_position--;

					REDRAW_OPTIONS = true;
				}

				else if(SHOWING_TRICK_LIST){
					if(first_trick_shown > 1){
						first_trick_shown--;
						REDRAW_LIST = true;
					}
				}

				else if(IN_THE_AIR && !EXECUTING_TRICK){
					current_trick += "U";
					start_time = System.currentTimeMillis();
				}

					break;
			case Event.LEFT:  
				if(IN_THE_AIR && !EXECUTING_TRICK){
					current_trick += "L";
					start_time = System.currentTimeMillis();
				}
					break;
			case Event.RIGHT:
				if(IN_THE_AIR && !EXECUTING_TRICK){
					current_trick += "R";
					start_time = System.currentTimeMillis();
				}
					break;
			case SPACE_BAR:
					if(WAITING_FOR_SPACEBAR || WAITING_FOR_OPTION){
					WAITING_FOR_SPACEBAR = false;
					if(WAITING_FOR_OPTION)
						SKIP_MENU = true;
					WAITING_FOR_OPTION = false;
					}
				if(IN_THE_AIR && EXECUTING_TRICK){
					finishTrick();
					FINISHING_TRICK = true;
				}

				else{
//				if(JumpLocation[current_sector + 3] != 0){
					power = PowerMeter[meter_position];
					jump_factor = (int)(power * ups * 30);
//				}

				}

				break;
			case RETURN:
//				if(SHOWING_TRICK_LIST)
//					SHOWING_TRICK_LIST = false;

				break;
			case ESCAPE:
				if(SHOWING_TRICK_LIST)
					SHOWING_TRICK_LIST = false;
				else if(OPTIONS_MENU)
					OPTIONS_MENU = false;
				else if(SHOWING_HELP)
					SHOWING_HELP = false;
				break;
			default:
				break;
		}

//		X
		if(key == 88 || key == 120){
			if(IN_THE_AIR && !EXECUTING_TRICK){
				current_trick += "X";
				start_time = System.currentTimeMillis();
			}
		}

//		C
		if(key == 67 || key == 99){
			if(IN_THE_AIR && !EXECUTING_TRICK){
				current_trick += "C";
				start_time = System.currentTimeMillis();
			}
		}

		}

		return true;
	}

	public boolean keyUp(Event evt, int key){

		if(!GAME_PAUSED){


//		S
		if(key == 83 || key == 115){
			if(!START_GAME){
				START_GAME = true;
				MAIN_MENU = true;
				QUIT = false;
			}
		}
//		Q
		else if(key == 81 || key == 113){
			if(PRACTICE_MODE == 1){
				PRACTICE_MODE = 2;
				skill_level = old_skill;
			}
		}

		}

//		P
		if(key == 80 || key == 112){
			if(PRACTICE_MODE == 1){
				if(GAME_PAUSED){
					GAME_PAUSED = false;
					panIncrement = previous_speed;
				}
				else{
					previous_speed = panIncrement;
					panIncrement = 0;
					GAME_PAUSED = true;
//					pauseGame();
				}
			}
		}
		
		// M -- For Go To Menu Function from showHistory()
		if(key == 77 || key == 109){
			if(WAITING_FOR_OPTION){
				WAITING_FOR_OPTION = false;
				SKIP_MENU = false;
				MAIN_MENU = true;
			}
		}
				

		// T - supposed to bring up trick list during practice
		if(key == 84 || key == 116){
			if(TRICK_LIST){
				GAME_PAUSED = false;
				TRICK_LIST = false;
				panIncrement = previous_speed;
			}
			else{
				previous_speed = panIncrement;
				panIncrement = 0;
				first_trick_shown = 1;
				GAME_PAUSED = true;
				TRICK_LIST = true;
			}
			
		}


		else{
			switch(key){
				case Event.DOWN:
					if(TRICK_LIST){
						if(first_trick_shown < skill_level - 5){
							first_trick_shown++;
						}
					}
						break;
				case Event.UP:
					if(TRICK_LIST){
						if(first_trick_shown > 1){
							first_trick_shown--;
						}
					}
						break;
				case Event.LEFT:
					break;
				case Event.RIGHT:
					break;
				default:
					break;
			}
		}


		return true;
	}

	private final int panScreen(){
		panX+=panIncrement;

		if(panX >= sector_size){
			++current_sector;

			if(current_sector >= levelnumsectors - 4){
				if(PRACTICE_MODE == 1)
					current_sector = 3;
				else
					DONE = true;
			}

			panX = 0;
			redrawOffscn();
		}

		return panX;
	}

	
	private final void redrawOffscn(){
		draw(current_sector, 0);
		draw(current_sector + 1, 1);
		draw(current_sector + 2, 2);
		draw(current_sector + 3, 3);
		draw(current_sector + 4, 4);
		draw(current_sector + 5, 5);
		draw(current_sector + 6, 6);
		draw(current_sector + 7, 7);
	}


	private final void draw(int sector, int col){
//		if(level[current_level][sector] != null)
			g2.drawImage(level[current_level][sector], col * 60, 0, this);

	}

	private final void startTrick(){
//		System.out.println("startTrick():"+trick_image_num+","+trick);

		EXECUTING_TRICK = true;

//		current_score += TrickPoints[trick - 1];

		currenttrickpoints += TrickPoints[trick - 1];

	
		trick_wait_timer++;

		if(trick_wait_timer >= trick_wait_length){
			trick_wait_timer = 0;
			trick_image_num++;
		}


		if(trick_image_num > TrickAnimNumImages[trick - 1]){
			switch(trick){
			case 6:
			case 8:
			case 14:
			case 16:
			case 17:
			case 18:
				EXECUTING_TRICK = FINISHING_TRICK = false;

				POINT_TRANSFER = true;
				
				combo++;

				trickpointsdecrement = currenttrickpoints / 5;

//				trick_wait_length = 2;
				trick_image_num = 0;
				last_trick = TrickListInfo[trick];
				
				if(GAME_IN_PROGRESS){
					CurrentRun[MainGameLoop][current_run_trick_num] = last_trick;
					current_run_trick_num++;
				}
				break;
			default:
//				trick_wait_length = 1;
				trick_image_num = TrickAnimNumImages[trick - 1];
				break;
			}
		}
	}

	private final void finishTrick(){
//		System.out.println("finishTrick():"+trick_image_num+","+trick);

//		currenttrickpoints = 0;

		trick_wait_timer++;
	
		if(trick_wait_timer >= trick_wait_length){
			trick_wait_timer = 0;
			trick_image_num--;
		}

		if(trick_image_num < 0){
			EXECUTING_TRICK = FINISHING_TRICK = false;
			POINT_TRANSFER = true;
			
			combo++;

			trickpointsdecrement = currenttrickpoints / 5;

			trick_image_num = trick_wait_timer = 0;
			
			last_trick = TrickListInfo[trick];
			
			if(GAME_IN_PROGRESS){
				CurrentRun[MainGameLoop][current_run_trick_num] = last_trick;
				current_run_trick_num++;
			}
		}
	}


	private void crash(/*int num*/){
		int crashX, crashY, crashimgnum = 0;

		currenttrickpoints = 0;

//		switch(num){
//		case 1:
			//		Crash Anim for rolling into pit from lip
			//System.out.println("Lip Crash");
//			crashX = 165;	crashY = 140;

//			for(int i = 0; i <= 13; i++){
//				System.out.println("Lip Crash Anim: At loop count "+i);

//				g3.drawImage(offimg, 0, 0, this);
//				g3.drawImage(Crash[0][0] /*Crash[0][crashimgnum]*/, crashX, crashY, this);

//				crashX += 2;	crashY += 2 * i;
				
//				if(i == 11)
//					crashY -= 4 * i;

//				crashimgnum++;
//				if(crashimgnum > numofimagesinthisanim)
//					crashimgnum = numofimagesinthisanim;

//				tempimg = offimg2;
//				repaint(60, 110, 250, 250);
//				pause(46);  // ~21 fps
//			}

//			pause(1500);

//			break;

//		case 2:
			//		Crash Anim for crashing into the landing ramp
//			System.out.println("Landing Ramp Crash");

			crashX = 105;	crashY = 175;

			for(int i = 0; i <= 14 /*num of images in this anim*/; i++){
//				System.out.println("Landing Ramp Crash Anim: At loop count "+i);

				g3.drawImage(offimg, -40, 0, this);
				g3.drawImage(Crash[1][crashimgnum], crashX, crashY, this);
				
				crashX += 3;	crashY += 8;

				if(i % 2 == 0)  crashimgnum++;
				if(crashimgnum > 7)
					crashimgnum = 7;

				tempimg = offimg2;
				repaint(60, 110, 360, 250);
				pause(100);
			}
			
			crashY += 4;
			
			g3.drawImage(offimg, -40, 0, this);
			g3.drawImage(Crash[1][crashimgnum], crashX, crashY, this);
			tempimg = offimg2;
			repaint(60, 110, 350, 250);

			pause(1500);
//////////////////////////////////////////////////////////////////////////////////
			EXECUTING_TRICK = false;
			FINISHING_TRICK = false;
			LANDING = false;
			IN_THE_AIR = false;
			
			combo = 0;

			jump_length = 1;
			ramplength = 0;

//			panIncrement = 6;
			panIncrement = SpeedSetting;

			meter_position = 0;
			jump_factor = 0;
			current_trick = "";
			
			if(GAME_IN_PROGRESS){
				CurrentRun[MainGameLoop][current_run_trick_num] = "Crash!";
				current_run_trick_num = 0;
			}
					
			trick = trick_image_num = trick_wait_timer = 0;
//////////////////////////////////////////////////////////////////////////////////
//			break;
//		}


		if(FREESTYLE_MODE){
			current_sector = 1;
		
			redrawOffscn();
			g3.drawImage(offimg, 0, 0, this);
			g3.drawImage(Bike[0], 120+panX, 255, this);

			g3.setColor(Color.black);
			g3.fillRect(panX, 362, 420, 40);
			g3.drawImage(jump_meter, 220+panX, 373, this);
			g3.drawImage(jump_meter_box, (meter_position*16+panX+230), 367, this);

			g3.setColor(Color.white);
			g3.drawString("Score: " + current_score, 20+panX, 387);

			g3.setColor(Color.black);
			g3.drawString("Freestyle Mode -- Press \'Q\' to quit.", 90 + panX, 15);
			g3.drawString("Press \'P\' to pause/unpause.", 120 + panX, 35);

			tempimg = offimg2;
			repaint();
		}
		else{
			/// end the level
			DONE = true;
			// Display popup message like 'Oh, that's too bad!' or something
		}

		pause(1000);
	}

	private void doHighScoresList(){
	}

	private void doMainMenu(){
		int selectorY, i, tempy;

		if(MAIN_MENU){

//Draw the Main Menu

			for(tempy = 0; tempy <= 10; tempy++)
				MenuTextColor[tempy] = Color.blue;

		g3.setFont(menuFont);
		g3.setColor(Color.white);
/*
		g3.setColor(Color.black);
		g3.fillRoundRect(130, 50, 160, 300, 15, 15);

		g3.setColor(Color.white);
		g3.drawRoundRect(130, 50, 160, 300, 15, 15);
		g3.drawRoundRect(135, 100, 150, 200, 15, 15);
*/

		g2.drawImage(menu_img, 129, 50, this);
		pause(26);

		g3.drawImage(offimg, 0, 0, this);

		g3.drawString("X-Up", 200, 70);
		g3.drawString("< main menu >", 170, 80);

//		g3.drawImage(MainMenu[menu_position - 1], MenuPos[menu_position - 1], 100, this);

		 tempy = 150;

		for(int k = 0; k <= 4; k++){
			g3.setColor(MenuTextColor[k]);
			g3.drawString(MainMenuText[k], MenuPos[k], tempy);

			tempy += 20;
		}

		pause(26);

		tempimg = offimg2;
		repaint();
		}


		do{
			if(REDRAW_MENU){

//				selectorY = 40 * menu_position + 63;
	
//				g3.drawImage(offimg, 0, 0, this);
//				g3.drawImage(MainMenu[menu_position - 1], MenuPos[menu_position - 1], selectorY, this);

//				g3.drawString(MainMenuText[menu_position - 1], MenuPos[menu_position - 1], selectorY);

				 tempy = 150;

				for(int k = 0; k <= 4; k++){
					g3.setColor(MenuTextColor[k]);
					g3.drawString(MainMenuText[k], MenuPos[k], tempy);

					tempy += 20;
				}


//				pause(26);

				tempimg = offimg2;
				repaint(135, 140, 150, 120);

				REDRAW_MENU = false;

			}

			
			if(SHOWING_HELP){
				MAIN_MENU = false;

				do{
					if(REDRAW_HELP){
						g3.drawImage(HelpScreens[helpimgnum], 0, 0, this);
	
						tempimg = offimg2;
						repaint();
					}
				}while(SHOWING_HELP);

				helpimgnum = 0;

				OPTIONS_MENU = true;
				REDRAW_OPTIONS = true;
			}

			if(SHOWING_TRICK_LIST){
				MAIN_MENU = false;

				first_trick_shown = 1;
				listY = 125;

				REDRAW_LIST = true;

				do{
					if(REDRAW_LIST){
						listY = 145;


						g3.setColor(Color.black);
						g3.fillRect(15, 80, 390, 210);
						g3.setColor(Color.black);
						g3.fillRect(30, 127, 350, 100);

						g3.setColor(Color.white);
						g3.drawRect(15, 80, 390, 210);
						g3.drawRect(25, 127, 370, 110);

//						g3.setColor(Color.yellow);
						g3.setFont(f);

						g3.drawString("Trick List", 190, 100);
						g3.drawString("Your skill level is " + skill_level, 160, 120);
						g3.drawString("Up and Down keys: Scroll the list", 115, 257);
						g3.drawString("Escape: Return to the Main Menu", 115, 274);

						i = first_trick_shown;

						g3.setFont(trick_how_to_font);


						do{
							if(i <= skill_level){

//								g3.setFont(trick_list_font);

								g3.drawString(TrickListInfo[i]+"  "+TrickHowTo[i], 32, listY);

//								g3.setFont(trick_how_to_font);
//								g3.drawString(TrickHowTo[i], 180, listY);
							}

							listY += 17;

							i++;
						}while(i < first_trick_shown + 6);
	
						tempimg = offimg2;
						repaint();

						REDRAW_LIST = false;
						}

//					System.out.println(first_trick_shown);
					pause(25);

				}while(SHOWING_TRICK_LIST);

				if(PRACTICE_MODE == 1)
					MAIN_MENU = false;

				else{
					OPTIONS_MENU = true;
					REDRAW_OPTIONS = true;
				}
			}
			
			
			if(OPTIONS_MENU){
				MAIN_MENU = false;
				REDRAW_MENU = true;

				menu_position = 1;
	
				g3.setFont(menuFont);
				g3.setColor(Color.white);

				g2.drawImage(menu_img, 129, 50, this);
				pause(26);

				g3.drawImage(offimg, 0, 0, this);
	
				g3.drawString("X-Up", 200, 70);
				g3.drawString("< help menu >", 170, 80);


				 tempy = 150;
	
				for(int k = 6; k <= 10; k++){
					g3.setColor(MenuTextColor[k]);
					g3.drawString(MainMenuText[k], MenuPos[k], tempy);
	
					tempy += 20;
				}
	
				pause(26);
	
				tempimg = offimg2;
				repaint();


				do{
					if(REDRAW_OPTIONS){

/*						selectorY = 35 * menu_position + 77;
	
						g3.drawImage(Options_Screen, 0, 0, this);
						g3.drawImage(OptionsMenu[menu_position - 1], OptionsPos[menu_position - 1], selectorY, this);

//						pause(26);

						tempimg = offimg2;
						repaint();
*/
						 tempy = 150;

						for(int k = 6; k <= 10; k++){
							g3.setColor(MenuTextColor[k]);
							g3.drawString(MainMenuText[k], MenuPos[k], tempy);

							tempy += 20;
						}


						tempimg = offimg2;
						repaint(135, 140, 150, 120);

						REDRAW_MENU = false;
						REDRAW_OPTIONS = false;
					}
					else if(SHOWING_CREDITS){
						showCredits();
						SHOWING_CREDITS = false;

						g3.setFont(menuFont);
						g3.setColor(Color.white);

						g2.drawImage(menu_img, 129, 50, this);
						pause(26);

						g3.drawImage(offimg, 0, 0, this);
		
						g3.drawString("X-Up", 200, 70);
						g3.drawString("< help menu >", 170, 80);

						tempimg = offimg2;
						repaint();

					}
				}while(OPTIONS_MENU);

				MAIN_MENU = true;
				REDRAW_MENU = true;

				menu_position = 1;

//Draw the Main Menu

		g2.drawImage(menu_img, 129, 50, this);
		pause(26);

		g3.drawImage(offimg, 0, 0, this);

			for(tempy = 0; tempy <= 10; tempy++)
				MenuTextColor[tempy] = Color.blue;

		g3.setColor(Color.white);
		g3.setFont(menuFont);
/*
		g3.setColor(Color.black);
		g3.fillRoundRect(130, 50, 160, 300, 15, 15);

		g3.setColor(Color.white);
		g3.drawRoundRect(130, 50, 160, 300, 15, 15);
		g3.drawRoundRect(135, 100, 150, 200, 15, 15);
*/
		g3.drawString("X-Up", 200, 70);
		g3.drawString("< main menu >", 170, 80);


//		g3.drawImage(MainMenu[menu_position - 1], MenuPos[menu_position - 1], 100, this);

		 tempy = 150;

		for(int k = 0; k <= 4; k++){
			g3.setColor(MenuTextColor[k]);
			g3.drawString(MainMenuText[k], MenuPos[k], tempy);

			tempy += 20;
		}

		pause(26);

		tempimg = offimg2;
		repaint();
		
			}

		}while(MAIN_MENU);

	}

	private int checkForTrick(String current_trick){
		int trick = 0, i = 1;

		do{
			if(current_trick == KeyCombos[i])
				trick = i;
			i++;
		}while(i <= skill_level);

		return(trick);
	}


	private void doTrickList(){
		int i;

			if(SHOWING_TRICK_LIST){
				MAIN_MENU = false;

				first_trick_shown = 1;
				listY = 125;

				REDRAW_LIST = true;

				do{
					if(REDRAW_LIST){
						listY = 145;

						g3.setColor(Color.black);
						g3.fillRect(15, 80, 390, 210);
						g3.setColor(Color.black);
						g3.fillRect(30, 127, 350, 100);

						g3.setColor(Color.white);
						g3.drawRect(15, 80, 390, 210);
						g3.drawRect(25, 127, 370, 110);

						g3.setColor(Color.yellow);
						g3.setFont(f);

						g3.drawString("Trick List", 190, 100);
						g3.drawString("Your skill level is " + skill_level, 160, 120);
						g3.drawString("Up and Down: Scroll the list", 115, 257);
						g3.drawString("Enter or Escape: Return to the Main Menu", 115, 274);

						i = first_trick_shown;

						g3.setFont(trick_how_to_font);

						do{
							if(i <= skill_level){

//								g3.setFont(trick_list_font);
								g3.drawString(TrickListInfo[i]+"  "+TrickHowTo[i], 32, listY);

//								g3.setFont(trick_how_to_font);
//								g3.drawString(TrickHowTo[i], 180, listY);
							}

							listY += 17;

							i++;
						}while(i < first_trick_shown + 6);
	
						tempimg = offimg2;
						repaint();

						REDRAW_LIST = false;
						}

					System.out.println(first_trick_shown);

				}while(SHOWING_TRICK_LIST);

				if(PRACTICE_MODE == 1)
					MAIN_MENU = false;

				else{
					OPTIONS_MENU = true;
					REDRAW_OPTIONS = true;
				}
			}
	}


	private void showLoading(){
		g3.drawImage(LoadingScreen, 0, 0, this);

/*		int percent_complete = 0, img_num = 1;
		int increment = (int)(100 / NUM_IMAGES);
		
		g3.drawImage(LoadingScreen, 0, 0, this);

		while(percent_complete < 100){
			try{
				tracker.waitForID(img_num);
			}
			catch(InterruptedException e){}

			percent_complete += increment;

			g3.setColor(Color.green);
			g3.drawRect(260, 340, 360, 350);
			g3.fillRect(260, 340, 260 + percent_complete, 350);

			tempimg = offimg2;
			repaint();

			System.out.println(percent_complete);

			pause(500);
		}
	*/
	}

	private void initTrickStrings(){
		TrickListInfo[1] = "No Hander";
		TrickListInfo[2] = "No Footer";
		TrickListInfo[3] = "X-Up";
		TrickListInfo[4] = "Can-Can";
		TrickListInfo[5] = "Toboggan";
		TrickListInfo[6] = "360";
		TrickListInfo[7] = "No footed can-can";
		TrickListInfo[8] = "Barspin";
		TrickListInfo[9] = "Nothing";
		TrickListInfo[10] = "Tabletop";
		TrickListInfo[11] = "No footed x-up";
		TrickListInfo[12] = "Superman";
		TrickListInfo[13] = "Turndown";
		TrickListInfo[14] = "Tailwhip";
		TrickListInfo[15] = "Superman seat grab";
		TrickListInfo[16] = "720";
		TrickListInfo[17] = "Backflip";
		TrickListInfo[18] = "Double backflip";
		TrickListInfo[19] = "180";

		
		TrickHowTo[1] = "- C";
		TrickHowTo[2] = "- X";
		TrickHowTo[3] = "- Left, Up";
		TrickHowTo[4] = "- X, Up";
		TrickHowTo[5] = "- C, Up, Left";
		TrickHowTo[6] = "- Left, Down, Right, Up, Left";
		TrickHowTo[7] = "- X, X, Up";
		TrickHowTo[8] = "- C, Up, Down";
		TrickHowTo[9] = "- X, C";
		TrickHowTo[10] = "- X, Right, Right";
		TrickHowTo[11] = "- X, Left, Up";
		TrickHowTo[12] = "- X, Left, Left";
		TrickHowTo[13] = "- Down, Up, Left, Up";
		TrickHowTo[14] = "- X, Down, Left, Up";
		TrickHowTo[15] = "- X, Left, C, Left";
		TrickHowTo[16] = "- Left, Down, Right, Up, Left, Down, Right, Up, Left";
		TrickHowTo[17] = "- Right, Left, Left, Right, Left, Left";
		TrickHowTo[18] = "- Right, Right, Left, Left, Left, Left";
		TrickHowTo[19] = "- Up, Left";

		
		KeyCombos[1] = "C";
		KeyCombos[2] = "X";
		KeyCombos[3] = "LU";
		KeyCombos[4] = "XU";
		KeyCombos[5] = "CUL";
		KeyCombos[6] = "LDRUL";
		KeyCombos[7] = "XXU";
		KeyCombos[8] = "CUD";
		KeyCombos[9] = "XC";
		KeyCombos[10] = "XRR";
		KeyCombos[11] = "XLU";
		KeyCombos[12] = "XLL";
		KeyCombos[13] = "DULU";
		KeyCombos[14] = "XDLU";
		KeyCombos[15] = "XLCL";
		KeyCombos[16] = "LDRULDRUL";
		KeyCombos[17] = "RLLRLL";
		KeyCombos[18] = "RRLLLL";
		KeyCombos[19] = "UL";

		
		TrickAnimNumImages[0] = 5;
		TrickAnimNumImages[1] = 3;
		TrickAnimNumImages[2] = 3;
		TrickAnimNumImages[3] = 3;
		TrickAnimNumImages[4] = 4;
		TrickAnimNumImages[5] = 6;
		TrickAnimNumImages[6] = 3;
		TrickAnimNumImages[7] = 5;
		TrickAnimNumImages[8] = 3;
		TrickAnimNumImages[9] = 3;
		TrickAnimNumImages[10] = 3;
		TrickAnimNumImages[11] = 5;
		TrickAnimNumImages[12] = 3;
		TrickAnimNumImages[13] = 5;
		TrickAnimNumImages[14] = 5;
		TrickAnimNumImages[15] = 12;
		TrickAnimNumImages[16] = 7;
		TrickAnimNumImages[17] = 14;
		TrickAnimNumImages[18] = 4;

		
		TrickWaitLength[0] = 1;
		TrickWaitLength[1] = 1;
		TrickWaitLength[2] = 1;
		TrickWaitLength[3] = 1;
		TrickWaitLength[4] = 1;
		TrickWaitLength[5] = 1;
		TrickWaitLength[6] = 2;
		TrickWaitLength[7] = 1;
		TrickWaitLength[8] = 1;
		TrickWaitLength[9] = 1;
		TrickWaitLength[10] = 1;

		TrickWaitLength[11] = 1;
		TrickWaitLength[12] = 1;
		TrickWaitLength[13] = 2;
		TrickWaitLength[14] = 2;
		TrickWaitLength[15] = 1;
		TrickWaitLength[16] = 2;

		TrickWaitLength[17] = 2;
		TrickWaitLength[18] = 1;

		
		TrickPoints[0] = 50;
		TrickPoints[1] = 50;
		TrickPoints[2] = 50;
		TrickPoints[3] = 60;
		TrickPoints[4] = 70;
		TrickPoints[5] = 430;
		TrickPoints[6] = 100;
		TrickPoints[7] = 450;
		TrickPoints[8] = 100;
		TrickPoints[9] = 90;
		TrickPoints[10] = 100;

		TrickPoints[11] = 110;
		TrickPoints[12] = 110;
		TrickPoints[13] = 300;
		TrickPoints[14] = 120;
		TrickPoints[15] = 300;
		TrickPoints[16] = 250;

		TrickPoints[17] = 140;
		TrickPoints[18] = 110;


	}

	private void showNextComp(){
		int fontx, fonty, k;
		String lowest;
		
		switch(lowestplace){
		case 1: lowest = "1st";
				break;
		case 2: lowest = "2nd";
				break;
		case 3: lowest = "3rd";
				break;
		default: lowest = "NotGiven";
				 break;
		}
		
		g3.setColor(Color.black);
		g3.fillRect(0, 0, 420, 400);
		g3.setFont(dispFont);
		g3.setColor(Color.yellow);
		
		g3.drawString("Next Comp...", 150, 25);
		
		g3.setFont(fontBig);
		fontx = (420 - fm_big.stringWidth(CompType)) / 2;
		g3.drawString(CompType, fontx, 50);
		
		g3.drawImage(trophy, 147, 70, this);
		
		fonty = 220;	fontx -= 15;	k = 0;
		g3.setFont(credits_font_small);
		
		while(CompDesc[k] != null){
			fontx = (420 - fm_small.stringWidth(CompDesc[k])) / 2;
			g3.drawString(CompDesc[k], fontx, fonty); 
			fonty += 12;
			k++;
		}
		
		fontx = (420 - fm_small.stringWidth("To place in this comp, you need:")) / 2;
		fonty += 20;
		
		g3.drawString("To place in this comp, you need:", fontx, fonty);
			fonty += 15;
		g3.drawString("First Place: "+LevelPlacementScores[1]+" points", fontx, fonty);
			fonty += 15;
		g3.drawString("Second Place: "+LevelPlacementScores[2]+" points", fontx, fonty);
			fonty += 15;
		g3.drawString("Third Place: "+LevelPlacementScores[3]+" points", fontx, fonty);
			fonty += 25;
			fontx -= 40;
		
		g3.drawString("You need to reach at least "+lowest+" place to advance.", fontx, fonty);
		
		g3.setFont(dispFont);
		g3.drawString("Press Space To Start...", 115, 390);	
		
		tempimg = offimg2;
		repaint();
		
		WAITING_FOR_SPACEBAR = true;
		while(WAITING_FOR_SPACEBAR){
			pause(25);
		}
		
	}
		
	private void showDebriefing(){
		// Draw The Debriefing screen, then return it to run to add to it
		int fontx, fonty, k, skill_addend = 0, place = 0;
		String finish = "";	boolean ADVANCE = false;

		g3.setColor(Color.black);
		g3.fillRect(0, 0, 420, 400);
		g3.setFont(dispFont);
		g3.setColor(Color.yellow);
		
		g3.setFont(fontBig);
		fontx = (420 - fm_big.stringWidth(CompType)) / 2;
		g3.drawString(CompType, fontx, 25);
		
		g3.setFont(credits_font_small);
		
		k = 0;	fonty = 60;
		
		fontx = (420 - fm_small.stringWidth("Best Run("+best_run+"):")) / 2;
		g3.drawString("Best Run("+best_run+"):", fontx, fonty);
		
		fontx -= 70;
		fonty = 80;
		
		tempimg = offimg2;
		repaint();
		
		for(int q = 0; q < 50; q++){
			if(CurrentRun[best_run][q] != null){
				g3.drawString(CurrentRun[best_run][q], fontx, fonty);
				fonty+= 15;
				k++;
			
				if(k % 5 == 0 && k != 0){
					fonty = 80;
					fontx += 70;
				}
			}
//			k++;
		}

		g3.setFont(dispFont);
		
		fontx = (420 - fm_disp.stringWidth("Your Best Score: "+current_score)) / 2;
		fonty = 182;
//		g3.setFont(credits_font_small);
		g3.drawString("Your Best Score: "+current_score, fontx, fonty);
		fonty += 30;

		if(current_score >= LevelPlacementScores[3]){
			finish = "You finished in 3rd Place!";
			CompHistory[current_comp] = "3rd place in "+CompType;
			place = 3;
		}
		if(current_score >= LevelPlacementScores[2]){
			finish = "You finished in 2nd Place!";
			CompHistory[current_comp] = "2nd place in "+CompType;
			place = 2;
		}
		if(current_score >= LevelPlacementScores[1]){
			finish = "You finished in 1st Place!!";
			CompHistory[current_comp] = "1st place in "+CompType;
			place = 1;
		}
		
		if(current_score >= LevelPlacementScores[lowestplace])
			ADVANCE = true;
		
		fontx = (420 - fm_disp.stringWidth(finish)) / 2;
		g3.drawString(finish, fontx, fonty);
		
		fonty += 45;
		
		if(ADVANCE){
			fontx = (420 - fm_disp.stringWidth("This is good enough to advance to the next comp!")) / 2;
			g3.drawString("This is good enough to advance to the next comp!", fontx, fonty);
			fonty += 25;
			
			current_comp++;
			if(current_comp > 10){
				victory();
			}
		}
		else{
			fontx = (420 - fm_disp.stringWidth("Sorry, but this isn't good enough to advance!")) / 2;
			g3.drawString("Sorry, but this isn't good enough to advance!", fontx, fonty);
			fonty += 25;
		}

		// Skill Points additions here
		if(current_comp == 3)
			skill_addend = 5;
		else if(current_comp > 3){
			if(place == 1)
				skill_addend = 2;
			else if(place == 2)
				skill_addend = 1;
			else if(place == 3)
				skill_addend = 0;
			else
				skill_addend = 0;
		}
		
		
		fontx += 25;
		g3.drawString("This placement earned you "+skill_addend+" skill points,", fontx, fonty);


		if((skill_addend > 0) && (skill_level < 19)){
			fonty += 14;
			g3.drawString("You earned the following tricks:", fontx, fonty);
			fonty += 10;

			for(int j = skill_level; j <= (skill_level + skill_addend); j += 2){
				fonty += 14;
				g3.drawString(TrickListInfo[j], fontx, fonty);
				if((j + 1) <= (skill_level + skill_addend))
					g3.drawString(TrickListInfo[j + 1], (fontx + 170), fonty);
			}
		}


		skill_level += skill_addend;
		
		if(skill_level > 19)
			skill_level = 19;


		fonty += 18;	fontx += 48;
		g3.drawString(" bringing your total to "+skill_level, fontx, fonty);
		
//		g3.setFont(dispFont);
		g3.drawString("Press Space To Continue...", 115, 390);	
		
		tempimg = offimg2;
		repaint();
		
		current_level = current_comp;
		loadLevel(current_level);
		DONE = false;
		
		WAITING_FOR_SPACEBAR = true;
		while(WAITING_FOR_SPACEBAR){
			pause(25);
		}
	}

	private void showHistory(){
		// Show contents of String CompHistory[]
		// Give option of going on or returning to main menu
		int fontx, fonty, k;
		
		g3.setColor(Color.black);
		g3.fillRect(0, 0, 420, 400);
		g3.setFont(fontBig);
		g3.setColor(Color.yellow);

		fonty = 40;
		fontx = (420 - fm_big.stringWidth("Comp History")) / 2;
		g3.drawString("Comp History", fontx, fonty);
		fonty += 35;
		
		g3.setFont(credits_font_small);
		
		for(int i = 1; i <= NUM_LEVELS; i++){
			if(CompHistory[i] != null){
				fontx = (420 - fm_small.stringWidth(CompHistory[i])) / 2;
				g3.drawString(CompHistory[i], fontx, fonty);
			}
			else{
				fontx = 210;
				g3.drawString("-", fontx, fonty);
			}
			fonty += 25;
		}
		
		g3.setFont(dispFont);
		g3.drawString("Press \'M\' to go to the main menu, or", 70, 370);
		g3.drawString("Press Space To Continue...", 95, 390);	
		
		tempimg = offimg2;
		repaint();
		
		// Include option for returning to the main menu here
		WAITING_FOR_OPTION = true;
		while(WAITING_FOR_OPTION){
			pause(25);
		}

	}
	
	private void victory(){
		// Newspapers flying sequence
		
		int imagenum = 0;
		
		for(int pos = -420; pos <= 420; pos += 3){
			g3.setColor(Color.black);
			g3.fillRect(0, 0, 420, 400);
			g3.drawImage(Victory[imagenum], pos, 0, this);
			
			tempimg = offimg2;
			repaint();
			pause(26);
		}
		
		imagenum = 1;
		
		for(int pos = 420; pos >= -420; pos -= 3){
			g3.setColor(Color.black);
			g3.fillRect(0, 0, 420, 400);
			g3.drawImage(Victory[imagenum], pos, 0, this);
			
			tempimg = offimg2;
			repaint();
			pause(26);
		}
		
		imagenum = 2;
		
		for(int pos = -400; pos <= 0; pos += 3){
			g3.setColor(Color.black);
			g3.fillRect(0, 0, 420, 400);
			g3.drawImage(Victory[imagenum], 0, pos, this);
			
			tempimg = offimg2;
			repaint();
			pause(26);
		}
		
		pause(4000);
		
		// Followed by showCredits()
		showCredits();
		
		GAME_IN_PROGRESS = false;
	}
	
	private void showCredits(){
/*		Image creditsOffscn;
		Graphics creds;

		creditsOffscn = createImage(400, 1000);
		creds = creditsOffscn.getGraphics();

		creds.fillRect(0, 0, 400, 480);
*/
		int fontX, fontY;
		int num_creds = 52;
		
		String credits[] = new String[num_creds];
		
		credits[0] = "Credits";
		credits[1] = "";
		credits[2] = "";
		credits[3] = "starring";
		credits[4] = "Charlie Band-aid";
		credits[5] = "Riding Champ";
		credits[6] = "Poser Steve";
		credits[7] = "bruised onlooker";
		credits[8] = "";
		credits[9] = "";
		credits[10] = "Eric";
		credits[11] = "stunt coordinator"; 
		credits[12] = "Bri";
		credits[13] = "stuntman";
		credits[14] = "Joe";
		credits[15] = "stuntboy";
		credits[16] = "Jill";
		credits[17] = "stunted";
		credits[18] = "";
		credits[19] = "";
		credits[20] = "Eric";
		credits[21] = "brought food";
		credits[22] = "Jared";
		credits[23] = "ate food";
		credits[24] = "Game Program";
		credits[25] = "Jared";
		credits[26] = "Design";
		credits[27] = "JE Bikes";
		credits[28] = "";
		credits[29] = "";
		credits[30] = "special thanks to:";
		credits[31] = "Mario's Tennis";
		credits[32] = "Ramen Noodles";
		credits[33] = "The Letter E";
		credits[34] = "Finger Bikes";
		credits[35] = "GI-Joe";
		credits[36] = "";
		credits[37] = "";
		credits[38] = "";
		credits[39] = "";
		credits[40] = "Filmed in Amazing Techni-Color®";
		credits[41] = "Soundtrack available nowhere";
		credits[42] = "";
		credits[43] = "";
		credits[44] = "No animals were harmed during";
		credits[45] = "production of this product.";
		credits[46] = "";
		credits[47] = "";
		credits[48] = "";
		credits[49] = "";
		credits[50] = "X - Up";
		credits[51] = "Copyright © 2000 JE Bikes";


		g3.setColor(Color.black);
		g3.fillRect(0, 0, 420, 400);
		tempimg = offimg2;
		repaint();

		for(int k = 400; k > -1800; k -= 2){
			fontY = k + (0 * 40);

			g3.setColor(Color.black);
			g3.fillRect(125, fontY - 10, 200, 2400);	

		for(int i = 0; i <= num_creds - 2; i += 2){
			fontY = k + (i * 40);

			g3.setColor(Color.white);
		
			g3.setFont(credits_font_small);
			
//			fontX = (420 - fm_big.stringWidth(credits[i])) / 2;
			fontX = 125;
			
			g3.drawString(credits[i], fontX, fontY);
			
//			g3.setFont(credits_font_small);
			
//			fontX = (420 - fm_small.stringWidth(credits[i+1])) / 2;
			fontX = 125;
			fontY += 15;
			
			g3.drawString(credits[i+1], fontX, fontY);

			if(i == 50){
				fontY += 15;
				g3.drawString("All Rights Reserved.", fontX, fontY);
			}

		}
			tempimg = offimg2;
			x = 0;
			repaint(125, 0, 200, 400);
			pause(26);
		}
		pause(3000);
	}

	private void pauseGame(){
		GAME_PAUSED = true;

		panIncrement = 0;

/*		while(GAME_PAUSED){
			System.out.println(GAME_PAUSED);
			g3.drawImage(PauseScreen, 150, 180, this);
			tempimg = offimg2;
			repaint();

			pause(20);
		};
*/
	}

	private void loadLevel(int levelnum){
		
		// Key:
		// 0     - Freestyle Mode Level
		// 1 - 3 - Comps 1 thru 3 (3 jumps)
		// 4 - 5 - Comps 4 thru 5 (4 jumps)
		// 6 - 7 - Comps 6 thru 7 (5 jumps)
		// 8     - Comp 8 (3 jumps)
		// 9     - Comp 9 (5 jumps)
		// 10    - Comp 10(8 jumps)
		// 11    - Secret Comp 11 (10 Jumps) (Only upon 1st place completion of all other comps)
		// Can group: {1, 2, 3, 8} {6, 7, 9} {4, 5} {10}
		
		

		clearLevel(levelnum);
		
		
		level[current_level][0] = level[0][0];
		level[current_level][1] = level[0][1];
		level[current_level][2] = level[0][2];
		
			JumpLocation[9] = 1;
			JumpLocation[10] = 0-1;
			JumpLocation[11] = 0-1;
			JumpLocation[12] = 0-1;
			JumpLocation[13] = 0-1;
			JumpLocation[14] = 0-1;
			JumpLocation[18] = 1;
			JumpLocation[19] = 0-1;
			JumpLocation[20] = 0-1;
			JumpLocation[21] = 0-1;
			JumpLocation[22] = 0-1;
			JumpLocation[23] = 0-1;
			JumpLocation[27] = 1;
			JumpLocation[28] = 0-1;
			JumpLocation[29] = 0-1;
			JumpLocation[30] = 0-1;
			JumpLocation[31] = 0-1;
			JumpLocation[32] = 0-1;
			
			level[levelnum][9] = level[0][9];
			level[levelnum][10] = level[0][10];
			level[levelnum][11] = level[0][11];
			level[levelnum][12] = level[0][12];
			level[levelnum][13] = level[0][13];
			level[levelnum][14] = level[0][14];
			level[levelnum][15] = level[0][15];
			level[levelnum][16] = level[0][16];
			
			level[levelnum][18] = level[0][9];
			level[levelnum][19] = level[0][10];
			level[levelnum][20] = level[0][11];
			level[levelnum][21] = level[0][12];
			level[levelnum][22] = level[0][13];
			level[levelnum][23] = level[0][14];
			level[levelnum][24] = level[0][15];
			level[levelnum][25] = level[0][16];

			level[levelnum][27] = level[0][9];
			level[levelnum][28] = level[0][10];
			level[levelnum][29] = level[0][11];
			level[levelnum][30] = level[0][12];
			level[levelnum][31] = level[0][13];
			level[levelnum][32] = level[0][14];
			level[levelnum][33] = level[0][15];
			level[levelnum][34] = level[0][16];

		switch(levelnum){
		case 4:
		case 5:
			JumpLocation[36] = 1;
			JumpLocation[37] = 0-1;
			JumpLocation[38] = 0-1;
			JumpLocation[39] = 0-1;
			JumpLocation[40] = 0-1;
			JumpLocation[41] = 0-1;
			
			level[levelnum][36] = level[0][9];
			level[levelnum][37] = level[0][10];
			level[levelnum][38] = level[0][11];
			level[levelnum][39] = level[0][12];
			level[levelnum][40] = level[0][13];
			level[levelnum][41] = level[0][14];
			level[levelnum][42] = level[0][15];
			level[levelnum][43] = level[0][16];

			break;
		case 6:
		case 7:
		case 9:
			JumpLocation[36] = 1;
			JumpLocation[37] = 0-1;
			JumpLocation[38] = 0-1;
			JumpLocation[39] = 0-1;
			JumpLocation[40] = 0-1;
			JumpLocation[41] = 0-1;
			JumpLocation[45] = 1;
			JumpLocation[46] = 0-1;
			JumpLocation[47] = 0-1;
			JumpLocation[48] = 0-1;
			JumpLocation[49] = 0-1;
			JumpLocation[50] = 0-1;
			
			level[levelnum][36] = level[0][9];
			level[levelnum][37] = level[0][10];
			level[levelnum][38] = level[0][11];
			level[levelnum][39] = level[0][12];
			level[levelnum][40] = level[0][13];
			level[levelnum][41] = level[0][14];
			level[levelnum][42] = level[0][15];
			level[levelnum][43] = level[0][16];
			
			level[levelnum][45] = level[0][9];
			level[levelnum][46] = level[0][10];
			level[levelnum][47] = level[0][11];
			level[levelnum][48] = level[0][12];
			level[levelnum][49] = level[0][13];
			level[levelnum][50] = level[0][14];
			level[levelnum][51] = level[0][15];
			level[levelnum][52] = level[0][16];

			break;
		case 10:
			
			JumpLocation[36] = 1;
			JumpLocation[37] = 0-1;
			JumpLocation[38] = 0-1;
			JumpLocation[39] = 0-1;
			JumpLocation[40] = 0-1;
			JumpLocation[41] = 0-1;
			JumpLocation[45] = 1;
			JumpLocation[46] = 0-1;
			JumpLocation[47] = 0-1;
			JumpLocation[48] = 0-1;
			JumpLocation[49] = 0-1;
			JumpLocation[50] = 0-1;
			JumpLocation[54] = 1;
			JumpLocation[55] = 0-1;
			JumpLocation[56] = 0-1;
			JumpLocation[57] = 0-1;
			JumpLocation[58] = 0-1;
			JumpLocation[59] = 0-1;
			JumpLocation[63] = 1;
			JumpLocation[64] = 0-1;
			JumpLocation[65] = 0-1;
			JumpLocation[66] = 0-1;
			JumpLocation[67] = 0-1;
			JumpLocation[68] = 0-1;
			JumpLocation[72] = 1;
			JumpLocation[73] = 0-1;
			JumpLocation[74] = 0-1;
			JumpLocation[75] = 0-1;
			JumpLocation[76] = 0-1;
			JumpLocation[77] = 0-1;
			
			level[levelnum][36] = level[0][9];
			level[levelnum][37] = level[0][10];
			level[levelnum][38] = level[0][11];
			level[levelnum][39] = level[0][12];
			level[levelnum][40] = level[0][13];
			level[levelnum][41] = level[0][14];
			level[levelnum][42] = level[0][15];
			level[levelnum][43] = level[0][16];
			
			level[levelnum][45] = level[0][9];
			level[levelnum][46] = level[0][10];
			level[levelnum][47] = level[0][11];
			level[levelnum][48] = level[0][12];
			level[levelnum][49] = level[0][13];
			level[levelnum][50] = level[0][14];
			level[levelnum][51] = level[0][15];
			level[levelnum][52] = level[0][16];
			
			level[levelnum][54] = level[0][9];
			level[levelnum][55] = level[0][10];
			level[levelnum][56] = level[0][11];
			level[levelnum][57] = level[0][12];
			level[levelnum][58] = level[0][13];
			level[levelnum][59] = level[0][14];
			level[levelnum][60] = level[0][15];
			level[levelnum][61] = level[0][16];
			
			level[levelnum][63] = level[0][9];
			level[levelnum][64] = level[0][10];
			level[levelnum][65] = level[0][11];
			level[levelnum][66] = level[0][12];
			level[levelnum][67] = level[0][13];
			level[levelnum][68] = level[0][14];
			level[levelnum][69] = level[0][15];
			level[levelnum][70] = level[0][16];
			
			level[levelnum][72] = level[0][9];
			level[levelnum][73] = level[0][10];
			level[levelnum][74] = level[0][11];
			level[levelnum][75] = level[0][12];
			level[levelnum][76] = level[0][13];
			level[levelnum][77] = level[0][14];
			level[levelnum][78] = level[0][15];
			level[levelnum][79] = level[0][16];

			
			break;
		case 11:
			
						
			JumpLocation[36] = 1;
			JumpLocation[37] = 0-1;
			JumpLocation[38] = 0-1;
			JumpLocation[39] = 0-1;
			JumpLocation[40] = 0-1;
			JumpLocation[41] = 0-1;
			JumpLocation[45] = 1;
			JumpLocation[46] = 0-1;
			JumpLocation[47] = 0-1;
			JumpLocation[48] = 0-1;
			JumpLocation[49] = 0-1;
			JumpLocation[50] = 0-1;
			JumpLocation[54] = 1;
			JumpLocation[55] = 0-1;
			JumpLocation[56] = 0-1;
			JumpLocation[57] = 0-1;
			JumpLocation[58] = 0-1;
			JumpLocation[59] = 0-1;
			JumpLocation[63] = 1;
			JumpLocation[64] = 0-1;
			JumpLocation[65] = 0-1;
			JumpLocation[66] = 0-1;
			JumpLocation[67] = 0-1;
			JumpLocation[68] = 0-1;
			JumpLocation[72] = 1;
			JumpLocation[73] = 0-1;
			JumpLocation[74] = 0-1;
			JumpLocation[75] = 0-1;
			JumpLocation[76] = 0-1;
			JumpLocation[77] = 0-1;
			JumpLocation[81] = 1;
			JumpLocation[82] = 0-1;
			JumpLocation[83] = 0-1;
			JumpLocation[84] = 0-1;
			JumpLocation[85] = 0-1;
			JumpLocation[86] = 0-1;
			JumpLocation[90] = 1;
			JumpLocation[91] = 0-1;
			JumpLocation[92] = 0-1;
			JumpLocation[93] = 0-1;
			JumpLocation[94] = 0-1;
			JumpLocation[95] = 0-1;
			
			
			level[levelnum][36] = level[0][9];
			level[levelnum][37] = level[0][10];
			level[levelnum][38] = level[0][11];
			level[levelnum][39] = level[0][12];
			level[levelnum][40] = level[0][13];
			level[levelnum][41] = level[0][14];
			level[levelnum][42] = level[0][15];
			level[levelnum][43] = level[0][16];
			
			level[levelnum][45] = level[0][9];
			level[levelnum][46] = level[0][10];
			level[levelnum][47] = level[0][11];
			level[levelnum][48] = level[0][12];
			level[levelnum][49] = level[0][13];
			level[levelnum][50] = level[0][14];
			level[levelnum][51] = level[0][15];
			level[levelnum][52] = level[0][16];
			
			level[levelnum][54] = level[0][9];
			level[levelnum][55] = level[0][10];
			level[levelnum][56] = level[0][11];
			level[levelnum][57] = level[0][12];
			level[levelnum][58] = level[0][13];
			level[levelnum][59] = level[0][14];
			level[levelnum][60] = level[0][15];
			level[levelnum][61] = level[0][16];
			
			level[levelnum][63] = level[0][9];
			level[levelnum][64] = level[0][10];
			level[levelnum][65] = level[0][11];
			level[levelnum][66] = level[0][12];
			level[levelnum][67] = level[0][13];
			level[levelnum][68] = level[0][14];
			level[levelnum][69] = level[0][15];
			level[levelnum][70] = level[0][16];
			
			level[levelnum][72] = level[0][9];
			level[levelnum][73] = level[0][10];
			level[levelnum][74] = level[0][11];
			level[levelnum][75] = level[0][12];
			level[levelnum][76] = level[0][13];
			level[levelnum][77] = level[0][14];
			level[levelnum][78] = level[0][15];
			level[levelnum][79] = level[0][16];
			
			level[levelnum][81] = level[0][9];
			level[levelnum][82] = level[0][10];
			level[levelnum][83] = level[0][11];
			level[levelnum][84] = level[0][12];
			level[levelnum][85] = level[0][13];
			level[levelnum][86] = level[0][14];
			level[levelnum][87] = level[0][15];
			level[levelnum][88] = level[0][16];
			
			level[levelnum][90] = level[0][9];
			level[levelnum][91] = level[0][10];
			level[levelnum][92] = level[0][11];
			level[levelnum][93] = level[0][12];
			level[levelnum][94] = level[0][13];
			level[levelnum][95] = level[0][14];
			level[levelnum][96] = level[0][15];
			level[levelnum][97] = level[0][16];

			
			break;
		}	
		
		// Now set the needed scores for advancement
		// Variable: LevelPlacementScores[1, 2, 3]
		// Variable: LowestPlace  -  set to whatever is the lowest 
		//  place that can be reached to still advance to next comp
		// Variable: LevelNumSectors
		// Variable: CompType[] - String Title for this comp
		// Variable: CompDesc[] - String description of this comp
		
		levelnumsectors = LevelNumSectors[levelnum];
		
		switch(levelnum){
		case 0:
			break;
		case 1:
			LevelPlacementScores[1] = 4000;
			LevelPlacementScores[2] = 3000;
			LevelPlacementScores[3] = 2000;
			
			lowestplace = 2;
			
			CompType = "Local Comp. 1";
			CompDesc[0] = "The first local comp that you will face";
			CompDesc[1] = "is relatively easy, having only 3 jumps";
			CompDesc[2] = "and a low score needed to advance...";
			CompDesc[3] = "So get out there and make a name for";
			CompDesc[4] = "yourself!";
			
			break;
		case 2:
			LevelPlacementScores[1] = 5000;
			LevelPlacementScores[2] = 4000;
			LevelPlacementScores[3] = 3500;
			
			lowestplace = 1;

			CompType = "Local Comp. 2";
			CompDesc[0] = "This is the last of the local comps.";
			CompDesc[1] = "If you place good enough here, you'll";
			CompDesc[2] = "be able to finally move out of this small";
			CompDesc[3] = "town and start competing in some amateur comps...";
			CompDesc[4] = "There are 3 jumps in this comp.";

			break;
		case 3:
			LevelPlacementScores[1] = 6000;
			LevelPlacementScores[2] = 5000;
			LevelPlacementScores[3] = 4000;
			
			lowestplace = 3;

			CompType = "Amateur Comp. 1";
			CompDesc[0] = "Well, You made it!  While this isn't exactly the";
			CompDesc[1] = "big leagues, you're one step closer...";
			CompDesc[2] = "The stakes are higher, the tricks are harder...";
			CompDesc[3] = "There are 3 jumps in this comp.";
			CompDesc[4] = "Good Luck!";
			
			break;
		case 4:
			LevelPlacementScores[1] = 6500;
			LevelPlacementScores[2] = 5500;
			LevelPlacementScores[3] = 4500;
			
			lowestplace = 2;

			CompType = "Amateur Comp. 2";
			CompDesc[0] = "Another day, another competition...";
			CompDesc[1] = "";
			CompDesc[2] = "You're doing great so far, lets hope";
			CompDesc[3] = "that the winning doesn't stop here...";
			CompDesc[4] = "There are 4 jumps in this comp.";
			break;
		case 5:
			LevelPlacementScores[1] = 9000;
			LevelPlacementScores[2] = 8000;
			LevelPlacementScores[3] = 7000;
			
			lowestplace = 1;

			CompType = "Amateur Comp. 3";
			CompDesc[0] = "You've made it to the last of the Amateur";
			CompDesc[1] = "comps!  If successful here, you will be";
			CompDesc[2] = "able to turn pro and start competing at";
			CompDesc[3] = "the pro level!  Don't let all that pressure get to you...";
			CompDesc[4] = "There are 4 jumps in this comp.";
			
			break;
		case 6:
			LevelPlacementScores[1] = 10000;
			LevelPlacementScores[2] = 9000;
			LevelPlacementScores[3] = 8000;
			
			lowestplace = 3;

			CompType = "Pro Comp. 1";
			CompDesc[0] = "This is it -- the Pro Level.";
			CompDesc[1] = "It's time to pull out all the big tricks";
			CompDesc[2] = "and show the world what you're made of!";
			CompDesc[3] = "See you in the winner's circle!";
			CompDesc[4] = "There are 5 jumps in this comp.";
			break;
		case 7:
			LevelPlacementScores[1] = 11500;
			LevelPlacementScores[2] = 10500;
			LevelPlacementScores[3] = 9500;
			
			lowestplace = 2;

			CompType = "Pro Comp. 2";
			CompDesc[0] = "Another day, another dollar...";
			CompDesc[1] = "Place well in just a few more of these and";
			CompDesc[2] = "you'll be bigger than Dave Mirra!";
			CompDesc[3] = "There are 5 jumps in this comp.";
			CompDesc[4] = "";
			break;
		case 8:
			LevelPlacementScores[1] = 13000;
			LevelPlacementScores[2] = 12500;
			LevelPlacementScores[3] = 10000;
			
			lowestplace = 2;

			CompType = "Pro Comp. 3";
			CompDesc[0] = "Keep doing well... the rewards will follow...";
			CompDesc[1] = "";
			CompDesc[2] = "";
			CompDesc[3] = "";
			CompDesc[4] = "There are 3 jumps in this comp.";
			break;
		case 9:
			LevelPlacementScores[1] = 14500;
			LevelPlacementScores[2] = 13000;
			LevelPlacementScores[3] = 12500;
			
			lowestplace = 1;

			CompType = "Pro Comp. 4";
			CompDesc[0] = "You're getting pretty damn good, aren't you?";
			CompDesc[1] = "";
			CompDesc[2] = "";
			CompDesc[3] = "";
			CompDesc[4] = "There are 5 jumps in this comp.";
			break;
		case 10:
			LevelPlacementScores[1] = 18000;
			LevelPlacementScores[2] = 17500;
			LevelPlacementScores[3] = 16000;
			
			lowestplace = 1;

			CompType = "Pro Comp. 5";
			CompDesc[0] = "This is it:  The last Pro Comp.";
			CompDesc[1] = "Finish first here and little kids";
			CompDesc[2] = "everywhere will be buying your action";
			CompDesc[3] = "figures and hanging your posters on their walls...";
			CompDesc[4] = "There are a total of 8 jumps in this comp.";
			break;
		case 11:
			LevelPlacementScores[1] = 20500;
			LevelPlacementScores[2] = 19000;
			LevelPlacementScores[3] = 18500;
			
			lowestplace = 1;
			CompType = "Secret Comp";
			CompDesc[0] = "If you've made it here, that means that you";
			CompDesc[1] = "are the best of the best... You have taken";
			CompDesc[2] = "First Place at every comp, and are now ready";
			CompDesc[3] = "for the Secret Bonus Comp!";
			CompDesc[4] = "There are 10 jumps in this comp.";

			break;
		}

	}

	private void clearLevel(int levelnum){
		int q = 3;

		do{
			JumpLocation[q] = 0;
			if(levelnum != 0)
				level[levelnum][q] = level[0][3];
			q++;
		}while(q <= MAX_SECTORS);
	}


	public void destroy(){
		g2.dispose();
		g3.dispose();
	}

	
	private void toggleGameSpeed(){
		String speed;	int tempy;
		
		SpeedSetting = 10 - SpeedSetting;
		
		if(SpeedSetting == 6){
			speed = "Fast Mode Setting";
		}
		else{
			speed = "Slow Mode Setting";
		}
		

		g3.setColor(Color.black);
		g3.setFont(dispFont);
		
				g2.drawImage(menu_img, 129, 50, this);
				pause(20);

				g3.drawImage(offimg, 0, 0, this);
	
		g3.drawString(speed, 130, 20);

				g3.setFont(menuFont);
				g3.setColor(Color.white);

				g3.drawString("X-Up", 200, 70);
				g3.drawString("< help menu >", 170, 80);


				 tempy = 150;
	
				for(int k = 6; k <= 10; k++){
					g3.setColor(MenuTextColor[k]);
					g3.drawString(MainMenuText[k], MenuPos[k], tempy);
	
					tempy += 20;
				}
	
				pause(26);
	
		tempimg = offimg2;
		repaint();
		
//		pause(1000);
	}
	
	
	private void redrawDisplay(){
	}

	private void clearOffscn(){
		g2.setColor(Color.white);
		g3.setColor(Color.white);

		g2.fillRect(0, 0, 480, 400);
		g3.fillRect(0, 0, 480, 400);
	}

	
	private void pause(int time){
		try{ Thread.sleep(time); }
			catch(InterruptedException e) {}
	}


	public void update(Graphics g){
		g.clipRect(0, 0, 416, 400);

		if(tempimg != null)
			g.drawImage(tempimg, x, 0, this);

//		paint(g);
	}

	public void paint(Graphics g){

		update(g);

//		if(tempimg != null)
//			g.drawImage(tempimg, x, 0, this);
	}

}  // End of class Canidae 

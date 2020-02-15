package com.bn.ball.jiemian;


public class Constant {
	
	public static float SCREEN_WIDTH=1024;
	public static float SCREEN_HEIGHT=720;
	
	public static ScreenScaleResult screenScaleResult=ScreenScaleUtil.calScale(SCREEN_WIDTH, SCREEN_HEIGHT);
	public static float sx=screenScaleResult.lucX;
    public static float sy=screenScaleResult.lucY;
    public static float ratio=1;
    
    public static int i=0;
    public static int j=0;
    
    public static boolean gameMusic_flag=true;
    
    public static boolean button1=true;
    public static boolean button2=true;
    
	public static float Menu_Start_Left;
	public static float Menu_Start_Right;
	public static float Menu_Start_Top;
	public static float Menu_Start_Bottom;
	
	public static float Menu_Set_Left;
	public static float Menu_Set_Right;
	public static float Menu_Set_Top;
	public static float Menu_Set_Bottom;
	
	public static float Menu_Help_Left;
	public static float Menu_Help_Right;
	public static float Menu_Help_Top;
	public static float Menu_Help_Bottom;
	
	public static float Menu_History_Left;
	public static float Menu_History_Right;
	public static float Menu_History_Top;
	public static float Menu_History_Bottom;
	
	public static float Menu_Exit_Left;
	public static float Menu_Exit_Right;
	public static float Menu_Exit_Top;
	public static float Menu_Exit_Bottom;
	
	public static float Sound_bgMusic_Left;
	public static float Sound_bgMusic_Right;
	public static float Sound_bgMusic_Top;
	public static float Sound_bgMusic_Buttom;
	
	public static float Sound_gSound_Left;
	public static float Sound_gSound_Right;
	public static float Sound_gSound_Top;
	public static float Sound_gSound_Buttom;
	
	public static float Sound_Back_Left;
	public static float Sound_Back_Right;
	public static float Sound_Back_Top;
	public static float Sound_Back_Buttom;
	
	public static float History_Time_Left;
	public static float History_Time_Right;
	public static float History_Time_Top;
	public static float History_Time_Buttom;
	
	public static float History_Grade_Left;
	public static float History_Grade_Right;
	public static float History_Grade_Top;
	public static float History_Grade_Buttom;
	
	public static float BZ_LB_Left;
	public static float BZ_LB_Right;
	public static float BZ_LB_Top;
	public static float BZ_LB_Buttom;
	
	public static float BZ_RB_Left;
	public static float BZ_RB_Right;
	public static float BZ_RB_Top;
	public static float BZ_RB_Buttom;
	
	public static float Game_LB_Left;
	public static float Game_LB_Right;
	public static float Game_LB_Top;
	public static float Game_LB_Buttom;
	
	public static float Game_RB_Left;
	public static float Game_RB_Right;
	public static float Game_RB_Top;
	public static float Game_RB_Buttom;
	
	public static float Game_Jump_Left;
	public static float Game_Jump_Right;
	public static float Game_Jump_Top;
	public static float Game_Jump_Buttom;
	public static void ScaleSR()
    {
    	screenScaleResult=ScreenScaleUtil.calScale(SCREEN_WIDTH, SCREEN_HEIGHT);
    	sx=screenScaleResult.lucX;
    	sy=screenScaleResult.lucY;
    	ratio=screenScaleResult.ratio;
    	
    	Menu_Start_Left=(sx+20)*ratio;//開始
     	Menu_Start_Right=(sx+320)*ratio;
     	Menu_Start_Top=(sy+100)*ratio;
     	Menu_Start_Bottom=(sy+190)*ratio;
     	
    	Menu_Set_Left=(sx+20)*ratio;//開始
     	Menu_Set_Right=(sx+300)*ratio;
     	Menu_Set_Top=(sy+210)*ratio;
     	Menu_Set_Bottom=(sy+300)*ratio;
     	
    	Menu_Help_Left=(sx+20)*ratio;//開始
     	Menu_Help_Right=(sx+280)*ratio;
     	Menu_Help_Top=(sy+320)*ratio;
     	Menu_Help_Bottom=(sy+410)*ratio;
     	
    	Menu_History_Left=(sx+20)*ratio;//開始
     	Menu_History_Right=(sx+260)*ratio;
     	Menu_History_Top=(sy+430)*ratio;
     	Menu_History_Bottom=(sy+520)*ratio;
     	
    	Menu_Exit_Left=(sx+20)*ratio;//開始
     	Menu_Exit_Right=(sx+240)*ratio;
     	Menu_Exit_Top=(sy+540)*ratio;
     	Menu_Exit_Bottom=(sy+630)*ratio;
     	
     	Sound_bgMusic_Left=(sx+750)*ratio;
     	Sound_bgMusic_Right=(sx+930)*ratio;
     	Sound_bgMusic_Top=(sy+150)*ratio;
     	Sound_bgMusic_Buttom=(sy+330)*ratio;
     	
     	Sound_gSound_Left=(sx+750)*ratio;
     	Sound_gSound_Right=(sx+930)*ratio;
     	Sound_gSound_Top=(sy+370)*ratio;
     	Sound_gSound_Buttom=(sy+550)*ratio;
     	
     	Sound_Back_Left=(sx+30)*ratio;
     	Sound_Back_Right=(sx+250)*ratio;
     	Sound_Back_Top=(sy+550)*ratio;
     	Sound_Back_Buttom=(sy+700)*ratio;
     	
       	//歷史界面 
    	History_Time_Left=(sx+250)*ratio;
    	History_Time_Right=(sx+470)*ratio;
    	History_Time_Top=(sy+20)*ratio;
    	History_Time_Buttom=(sy+160)*ratio;
    	
    	History_Grade_Left=(sx+620)*ratio;
    	History_Grade_Right=(sx+850)*ratio;
    	History_Grade_Top=(sy+20)*ratio;
    	History_Grade_Buttom=(sy+160)*ratio;
    	
    	//幫助界面
    	BZ_LB_Left=(sx+10)*ratio;
    	BZ_LB_Right=(sx+230)*ratio;
    	BZ_LB_Top=(sy+600)*ratio;
    	BZ_LB_Buttom=(sy+720)*ratio;
    	
    	BZ_RB_Left=(sx+790)*ratio;
    	BZ_RB_Right=(sx+1010)*ratio;
    	BZ_RB_Top=(sy+600)*ratio;
    	BZ_RB_Buttom=(sy+720)*ratio;
    	
    	
    	Game_LB_Left=(sx+210)*ratio;
    	Game_LB_Right=(sx+490)*ratio;
    	Game_LB_Top=(sy+335)*ratio;
    	Game_LB_Buttom=(sy+450)*ratio;
    	
    	Game_RB_Left=(sx+500)*ratio;
    	Game_RB_Right=(sx+770)*ratio;
    	Game_RB_Top=(sy+335)*ratio;
    	Game_RB_Buttom=(sy+450)*ratio;
    	
    	Game_Jump_Left=(sx+780)*ratio;
    	Game_Jump_Right=(sx+945)*ratio;
    	Game_Jump_Top=(sy+500)*ratio;
    	Game_Jump_Buttom=(sy+670)*ratio;
    }

}

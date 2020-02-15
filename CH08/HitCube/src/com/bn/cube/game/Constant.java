package com.bn.cube.game;

import com.bn.cube.view.ScreenScaleResult;
import com.bn.cube.view.ScreenScaleUtil;

public class Constant {
	
    public static float screenWidth;//螢幕的寬和高
    public static float screenHigth;
    public static ScreenScaleResult screenScaleResult=ScreenScaleUtil.calScale(screenWidth, screenHigth);
    public static float sxgame=screenScaleResult.lucX;
    public static float sygame=screenScaleResult.lucY;
    public static float ratio=1;
	public static float length=20;
	public static float angleSpan=20f;
	public static float UnitSize=5.53f;
	
	public static float cube_Width=0.5f;  
	public static float cube_Height=0.25f;
	public static float cube_Depth=0.25f;
	
	public static boolean cube_flag=false;
	public static boolean guan_flag=false;
	public static boolean wall_flag=false;
	public static boolean pieces_flag=false;
	public static boolean peng_flag=false;
	public static boolean button_flag=false;
	public static boolean pause_flag=false;
	public static boolean mySur_flag=true;
	public static boolean ball_flag=false;
	public static boolean star_flag=false;
	public static boolean isLoadedOk=false;
	public static int threadTime=15;
	public static boolean  shengliFlag=false;
	
	public static float piecesY=0;
	public static float piecesZ=0;
	
	public static int cube_length=1;
	public static int level=1;
	public static int scoreNumberbase=0;
	public static int levelNumber=0;
	public static int lifeNum=3;
	public static int pictureId=2;
	public static float second=90;
	public static float totalTime=0;
	public static int sumBoardScore=0;//游戲界面總分	
	public static int sumTotalScore=0;	
	public static int sumTimeScore=0;
	public static int sunLivesScore=0;
	
	public static float currZL=10.0f;
	public static float UNIT_SIZE=1.0f;
	
	public static float i=0;
	public static float j=0;
	public static float k=-0.5f;	
	
	public static float ball_X=0;
	public static float ball_Y=0f;
	public static float ball_Z=-8f;
	public static float R=0.25f;
	
	public static float ball_Span=15;
	public static float circleZ=ball_Z;
	
	public static float curr_X=0;//觸控位置
	public static float curr_Y=0;
	
	public static boolean move_flag=false;
	
	public static float game_Pause_Left=900;
	public static float game_Pause_Right=1024;
	public static float game_Pause_Top=600;
	public static float game_Pause_Bottom=700;
	
	//=======================================
	public static float game_Shalou_Left=0;
	public static float game_Shalou_Right=102;
	public static float game_Shalou_Top=600;
	public static float game_Shalou_Bottom=700;
	
	
	public static float Pause_Exit_Left=430;
	public static float Pause_Exit_Right=555;
	public static float Pause_Exit_Top=140;
	public static float Pause_Exit_Buttom=245;
	
	public static float Pause_Go_Left=550;
	public static float Pause_Go_Right=720;
	public static float Pause_Go_Top=320;
	public static float Pause_Go_Buttom=465;
	
	public static float Pause_Quit_Left=260;
	public static float Pause_Quit_Right=425;
	public static float Pause_Quit_Top=320;
	public static float Pause_Quit_Buttom=465;
	
	public static float Game_Board_Left=0;
	public static float Game_Board_Right=1024;
	public static float Game_Board_Top=500;
	public static float Game_Board_Buttom=700;
	
	public static float Win_Quit_Left=250;
	public static float Win_Quit_Right=350;
	public static float Win_Quit_Top=485;
	public static float Win_Quit_Buttom=580;
	
	public static float Win_Next_Left=630;
	public static float Win_Next_Right=800;
	public static float Win_Next_Top=420;
	public static float Win_Next_Buttom=560;
	
	public static float Lose_Quit_Left=590;
	public static float Lose_Quit_Right=750;
	public static float Lose_Quit_Top=475;
	public static float Lose_Quit_Buttom=610;
	
	public static float dangbanCenterx=512;
	public static float dangbanCentery=360;
	public static float scale=92.6f;
	
	public static float sx=0;
	public static float sy=0;
	public static float sz=0;
	
	public static boolean cubePieceInitFlag=false;
	public static boolean cubePieceDrawFlag=false;
	public static boolean cubePieceThreadFlag=false;
	public static boolean dangban_flag=false;
	public static boolean AndScore_flag=false;
	public static boolean win_flag=false;
	public static boolean losebutton_frag=false;
	public static boolean lightFlag=false;
	public static boolean baiTiaoThreadFlag=false;
	public static boolean baiTiaoDrawFlag=false;
	
	public static boolean dangBanBigFlag=false;
	
	public static float pieceX=0;
	public static float pieceY=0;
	public static float pieceZ=0;
	public static float starY=0;
	 
	public static float lightCount=10;
	public static float lightX=-1;
	public static float lightY=-1;
	public static float lightZ=-1;	
	public static float baiTiaoX=0;
	public static float baiTiaoY=0;
	public static float baiTiaoZ=0;
	
	public static float baiTiaoAngleId=0;
	public static float uA=0;
	public static float uAId=0;
	
	public static int deFenId=0;
	public static boolean deFenJianFlag=false;
	public static boolean deFenJiaFlag=false;
	public static boolean deFenDrawFlag=false;
	public static boolean deFenThreadFlag=false;
	public static float denfenAlpha=0;
	
//-===================================
	public static float shalouCount=0;
	public static boolean shalouFlag=false;
	public static float shalouAngle=0;
	public static float shalouKaiId=0;
	public static int daojuId=-1;
	public static boolean drawdaojuFlag=false;
	public static boolean daojuThreadFlag=false;
	
	public static float daojuX=0;
	public static float daojuY=0;
	public static float daojuZ=0;
	public static float colorId=0;
	public static float daojuACount=0;
	public static float daojuAlevel=0;
	public static boolean counter_flag=false;
    public static void ScaleSRGame()
    {
    	screenScaleResult=ScreenScaleUtil.calScale(screenWidth, screenHigth);
    	sxgame=screenScaleResult.lucX;
    	sygame=screenScaleResult.lucY;
    	ratio=screenScaleResult.ratio;
    	game_Pause_Left=(sxgame+880)*ratio;
    	game_Pause_Right=(sxgame+1024)*ratio;
    	game_Pause_Top=(sygame+580)*ratio;
    	game_Pause_Bottom=(sygame+720)*ratio;
    	
    	//=======================================
    	game_Shalou_Left=(sxgame+0)*ratio;
    	game_Shalou_Right=(sxgame+140)*ratio;
    	game_Shalou_Top=(sygame+560)*ratio;
    	game_Shalou_Bottom=(sygame+720)*ratio;
    	
    	
    	Pause_Exit_Left=(sxgame+390)*ratio;
    	Pause_Exit_Right=(sxgame+585)*ratio;
    	Pause_Exit_Top=(sygame+100)*ratio;
    	Pause_Exit_Buttom=(sygame+280)*ratio;
    	
    	Pause_Go_Left=(sxgame+510)*ratio;
    	Pause_Go_Right=(sxgame+720)*ratio;
    	Pause_Go_Top=(sygame+280)*ratio;
    	Pause_Go_Buttom=(sygame+500)*ratio;
    	
    	Pause_Quit_Left=(sxgame+220)*ratio;
    	Pause_Quit_Right=(sxgame+460)*ratio;
    	Pause_Quit_Top=(sygame+280)*ratio;
    	Pause_Quit_Buttom=(sygame+500)*ratio;
    	
    	Game_Board_Left=(sxgame+0)*ratio;
    	Game_Board_Right=(sxgame+1024)*ratio;
    	Game_Board_Top=(sygame+500)*ratio;
    	Game_Board_Buttom=(sygame+700)*ratio;
    	
    	Win_Quit_Left=(sxgame+210)*ratio;
    	Win_Quit_Right=(sxgame+390)*ratio;
    	Win_Quit_Top=(sygame+450)*ratio;
    	Win_Quit_Buttom=(sygame+620)*ratio;
    	
    	Win_Next_Left=(sxgame+590)*ratio;
    	Win_Next_Right=(sxgame+840)*ratio;
    	Win_Next_Top=(sygame+380)*ratio;
    	Win_Next_Buttom=(sygame+600)*ratio;
    	
    	Lose_Quit_Left=(sxgame+550)*ratio;
    	Lose_Quit_Right=(sxgame+790)*ratio;
    	Lose_Quit_Top=(sygame+435)*ratio;
    	Lose_Quit_Buttom=(sygame+650)*ratio;
    	
    	dangbanCenterx=(sxgame+512)*ratio;
    	dangbanCentery=(sygame+360)*ratio;
    	scale=92.6f;
    }	
}

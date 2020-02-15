package com.bn.cube.view;

public class Constant {
	
    public static float screenWidth;//¿Ã¹õªº¼e©M°ª
    public static float screenHigth;
    public static ScreenScaleResult screenScaleResult=ScreenScaleUtil.calScale(screenWidth, screenHigth);
    public static float sx=screenScaleResult.lucX;
    public static float sy=screenScaleResult.lucY;
    public static float ratio=1;
	public static float length=20;
	public static float angleSpan=20f;
	public static float color_green;
	public static float cube_Width=0.5f;  
	public static float cube_Height=0.25f;
	
	public static boolean menu_flag=true;
	public static boolean cube_flag=false;
	public static boolean guan_flag=false;
	public static boolean button_flag=false;
	public static boolean line_frag=false;
	public static boolean contbutton_flag=false;
	public static boolean soundbutton_flag=false;
	public static boolean star_flag=false;
	public static boolean water_flag=false;
	public static boolean bgNusic_flag=false;
	public static boolean gameMusic_flag=false;
	public static boolean boss_flag=false;
	public static int cube_length=1;
	public static int level=1;
	public static int maxLevel=1;
	public static float wx=0;
	public static int bn;
	
	
	public static float starY=0;
	public static float currZL=10.0f;
	public static float UNIT_SIZE=1.0f;
	
	public static float ball_Span=15;
	public static float circleZ=-8; 
	
	public static float Menu_EX_Left=60;
	public static float Menu_EX_Right=235;
	public static float Menu_EX_Top=470;
	public static float Menu_EX_Buttom=615;
	
	public static float Menu_NEW_Left=150;
	public static float Menu_NEW_Right=450;
	public static float Menu_NEW_Top=220;
	public static float Menu_NEW_Buttom=475;
	
	public static float Menu_CONT_Left=525;
	public static float Menu_CONT_Right=825;
	public static float Menu_CONT_Top=220;
	public static float Menu_CONT_Buttom=475;
	
	public static float Menu_Set_Left=750;
	public static float Menu_Set_Right=915;
	public static float Menu_Set_Top=75;
	public static float Menu_Set_Buttom=225;
	
	public static float Menu_Help_Left=750;
	public static float Menu_Help_Right=915;
	public static float Menu_Help_Top=495;
	public static float Menu_Help_Buttom=645;
	
	public static float Cont_Level1_Left=305;
	public static float Cont_Level1_Right=430;
	public static float Cont_Level1_Top=125;
	public static float Cont_Level1_Buttom=265;	
	
	public static float Cont_Level2_Left=430;
	public static float Cont_Level2_Right=545;
	public static float Cont_Level2_Top=200;
	public static float Cont_Level2_Buttom=330;	
	
	public static float Cont_Level3_Left=545;
	public static float Cont_Level3_Right=668;
	public static float Cont_Level3_Top=125;
	public static float Cont_Level3_Buttom=265;
	
	public static float Cont_Level4_Left=668;
	public static float Cont_Level4_Right=788;
	public static float Cont_Level4_Top=200;
	public static float Cont_Level4_Buttom=330;
	
	public static float Cont_Level5_Left=668;
	public static float Cont_Level5_Right=788;
	public static float Cont_Level5_Top=330;
	public static float Cont_Level5_Buttom=465;
	
	public static float Cont_Level6_Left=545;
	public static float Cont_Level6_Right=668;
	public static float Cont_Level6_Top=400;
	public static float Cont_Level6_Buttom=540;
	
	public static float Cont_Boss_Left=275;
	public static float Cont_Boss_Right=500;
	public static float Cont_Boss_Top=350;
	public static float Cont_Boss_Buttom=590;
	
	public static float Cont_Back_Left=100;
	public static float Cont_Back_Right=250;
	public static float Cont_Back_Top=500;
	public static float Cont_Back_Buttom=625;
	
	public static float Sound_Back_Left=75;
	public static float Sound_Back_Right=240;
	public static float Sound_Back_Top=530;
	public static float Sound_Back_Buttom=670;
	
	public static float Sound_Music_Left=275;
	public static float Sound_Music_Right=480;
	public static float Sound_Music_Top=130;
	public static float Sound_Music_Buttom=300;
	
	public static float Sound_Sound_Left=275;
	public static float Sound_Sound_Right=480;
	public static float Sound_Sound_Top=385;
	public static float Sound_Sound_Buttom=560;
	
	public static float Sound_MButton_Left=710;
	public static float Sound_MButton_Right=860;
	public static float Sound_MButton_Top=150;
	public static float Sound_MButton_Buttom=280;
	
	public static float Sound_SButton_Left=710;
	public static float Sound_SButton_Right=860;
	public static float Sound_SButton_Top=405;
	public static float Sound_SButton_Buttom=510;
	
	public static float Help_For_Left=70;
	public static float Help_For_Right=225;
	public static float Help_For_Top=550;
	public static float Help_For_Buttom=720;
	
	public static float Help_Nex_Left=760;
	public static float Help_Nex_Right=915;
	public static float Help_Nex_Top=550;
	public static float Help_Nex_Buttom=720;
					
	
    public static void ScaleSR()
    {
    	screenScaleResult=ScreenScaleUtil.calScale(screenWidth, screenHigth);
    	sx=screenScaleResult.lucX;
    	sy=screenScaleResult.lucY;
    	ratio=screenScaleResult.ratio;
    	//===================MenuView==================
    	
    	Menu_EX_Left=(sx+30)*ratio;
    	Menu_EX_Right=(sx+270)*ratio;
    	Menu_EX_Top=(sy+430)*ratio;
    	Menu_EX_Buttom=(sy+650)*ratio;
    	
    	Menu_NEW_Left=(sx+130)*ratio;
    	Menu_NEW_Right=(sx+460)*ratio;
    	Menu_NEW_Top=(sy+200)*ratio;
    	Menu_NEW_Buttom=(sy+490)*ratio;
    	
    	Menu_CONT_Left=(sx+525)*ratio;
    	Menu_CONT_Right=(sx+850)*ratio;
    	Menu_CONT_Top=(sy+200)*ratio;
    	Menu_CONT_Buttom=(sy+490)*ratio;    	    	
    	
    	Menu_Set_Left=(sx+720)*ratio;
    	Menu_Set_Right=(sx+950)*ratio;
    	Menu_Set_Top=(sy+50)*ratio;
    	Menu_Set_Buttom=(sy+250)*ratio;
    	
    	Menu_Help_Left=(sx+720)*ratio;
    	Menu_Help_Right=(sx+950)*ratio; 
    	Menu_Help_Top=(sy+470)*ratio;
    	Menu_Help_Buttom=(sy+670)*ratio;
    	//===================ContView=========================
    	Cont_Level1_Left=(sx+305)*ratio;
    	Cont_Level1_Right=(sx+430)*ratio;
    	Cont_Level1_Top=(sy+125)*ratio;
    	Cont_Level1_Buttom=(sy+265)*ratio;	
    	
    	Cont_Level2_Left=(sx+430)*ratio;
    	Cont_Level2_Right=(sx+545)*ratio;
    	Cont_Level2_Top=(sy+200)*ratio;
    	Cont_Level2_Buttom=(sy+330)*ratio;	
    	
    	Cont_Level3_Left=(sx+545)*ratio;
    	Cont_Level3_Right=(sx+668)*ratio;
    	Cont_Level3_Top=(sy+125)*ratio;
    	Cont_Level3_Buttom=(sy+265)*ratio;
    	
    	Cont_Level4_Left=(sx+668)*ratio;
    	Cont_Level4_Right=(sx+788)*ratio;
    	Cont_Level4_Top=(sy+200)*ratio;
    	Cont_Level4_Buttom=(sy+330)*ratio;
    	
    	Cont_Level5_Left=(sx+668)*ratio;
    	Cont_Level5_Right=(sx+788)*ratio;
    	Cont_Level5_Top=(sy+330)*ratio;
    	Cont_Level5_Buttom=(sy+465)*ratio;
    	
    	Cont_Level6_Left=(sx+545)*ratio;
    	Cont_Level6_Right=(sx+668)*ratio;
    	Cont_Level6_Top=(sy+400)*ratio;
    	Cont_Level6_Buttom=(sy+540)*ratio;
    	
    	Cont_Boss_Left=(sx+275)*ratio;
    	Cont_Boss_Right=(sx+500)*ratio;
    	Cont_Boss_Top=(sy+350)*ratio;
    	Cont_Boss_Buttom=(sy+590)*ratio;
    	
    	Cont_Back_Left=(sx+100)*ratio;
    	Cont_Back_Right=(sx+250)*ratio;
    	Cont_Back_Top=(sy+500)*ratio;
    	Cont_Back_Buttom=(sy+625)*ratio;
    	
    	//===================SoundView=======================
    	Sound_Back_Left=(sx+40)*ratio;
    	Sound_Back_Right=(sx+270)*ratio;
    	Sound_Back_Top=(sy+500)*ratio;
    	Sound_Back_Buttom=(sy+700)*ratio;
    	    	
    	Sound_MButton_Left=(sx+660)*ratio;
    	Sound_MButton_Right=(sx+910)*ratio;
    	Sound_MButton_Top=(sy+110)*ratio;
    	Sound_MButton_Buttom=(sy+320)*ratio;
    	
    	Sound_SButton_Left=(sx+660)*ratio;
    	Sound_SButton_Right=(sx+910)*ratio;
    	Sound_SButton_Top=(sy+390)*ratio;
    	Sound_SButton_Buttom=(sy+560)*ratio;
    	
    	Help_For_Left=(sx+50)*ratio;
    	Help_For_Right=(sx+250)*ratio;
    	Help_For_Top=(sy+520)*ratio;
    	Help_For_Buttom=(sy+720)*ratio;
    	
    	Help_Nex_Left=(sx+730)*ratio;
    	Help_Nex_Right=(sx+940)*ratio;
    	Help_Nex_Top=(sy+520)*ratio;
    	Help_Nex_Buttom=(sy+720)*ratio;
    	
    }
	public static float[]button=new float[]
	{
		-0.4f, 0.5f, -10,
		0f, 0.266f, -10,
		0.4f, 0.5f, -10,
		0.8f, 0.266f, -10,
		0.8f, -0.19f, -10,
		0.4f, -0.424f, -10,		
	};	
}

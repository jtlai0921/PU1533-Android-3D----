package com.bn.txz;

import com.bn.txz.manager.ScreenScaleResult;
import com.bn.txz.manager.ScreenScaleUtil;



//�Ω�޲z���@�`�ƪ��`�����O
public class Constant 
{
	
	public static float boxV=0.05f;  
	public static float xOffset=-9f;
	public static boolean boxFlag=false;//�D��椤�c�l�h���ЧӦ�
	public static float robotXstar=-15.5f;
	public static float robotYstar=0;
	public static float robotZstar=-8.5f;

	public static WhichView currView; 
	
	public static int status=0;
	
	public static final float UNIT_SIZE=15.0f;//�Ѳy�b�|
	
	public static boolean MENU_IS_WHILE=true;//�O�_�O�`���u�@
	public static boolean SET_IS_WHILE=true;//�O�_�O�`���u�@
	public static boolean SELECT_IS_WHILE=true;//�O�_�O�`���u�@
	
	public static Object initLock=new Object();
	
	public static float screenWidthStandard=960;//�ù��e��
	public static float screenHeightStandard=540;//�ù�����
	
	public static float screenWidth;//�ù��e��
	public static float screenHeight;//�ù�����
	public static float ratio;//�e��/����  
	
	public static final float bottom=-1;
	public static final float top=1;
	public static final float near=2f;
	public static final float far=400f;
    
    
    //handler�`�ƽs��========================================
    //���D������ɭ�
    public static final int COMMAND_GOTO_GAME_VIEW=0;
    //��ܴ���Toast
    public static final int COMMAND_TOAST_MSG=1;
    //���D����ɭ�
    public static final int COMMAND_GOTO_MENU_VIEW=2;
    //������ɭ�
    public static final int COMMAND_GOTO_GUAN_VIEW=3;
    //���D������ɭ�
    public static final int COMMAND_GOTO_ABOUT_VIEW=4;
    //���D��]�w�ɭ�
    public static final int COMMAND_GOTO_SET_VIEW=6;
       
    public static final int COMMAND_GOTO_FINISH=4;
    
    public static boolean IS_BEIJINGYINYUE=true;//�O�_�ϥέI������
    public static boolean IS_YINXIAO=true;//�O�_�ϥδ�������
    public static boolean IS_DRAW_WIN=false;
    
    public static ScreenScaleResult screenScaleResult;
    public static void ScaleSR()
    {
    	screenScaleResult=ScreenScaleUtil.calScale(screenWidth, screenHeight);
    }
    
	public static int CURR_DIRECTION=0;//�ثe���񪺰ʵe�د���
	public static final int POSITIVE_MOVETO_Z=0;//z�b����V
	public static final int POSITIVE_MOVETO_X=90;//x�b����V
	public static final int NEGATIVE_MOVETO_Z=180;//z�b�t��V
	public static final int NEGATIVE_MOVETO_X=270;//x�b�t��V
	
	public static final float Game_Left_l=667;
	public static final float Game_Left_r=751;
	public static final float Game_Left_u=334;
	public static final float Game_Left_d=442;
	
	public static final float Game_Right_l=826;
	public static final float Game_Right_r=931;
	public static final float Game_Right_u=334;
	public static final float Game_Right_d=442;
	
	public static final float Game_Up_l=738;
	public static final float Game_Up_r=852;
	public static final float Game_Up_u=270;
	public static final float Game_Up_d=366;
	
	public static final float Game_Down_l=738;
	public static final float Game_Down_r=852;
	public static final float Game_Down_u=433;
	public static final float Game_Down_d=536;
	
	public static final float Game_View_l=817;
	public static final float Game_View_r=951;
	public static final float Game_View_u=6;
	public static final float Game_View_d=130;
	
	public static final float Game_Win_First_l=295;
	public static final float Game_Win_First_r=446;
	public static final float Game_Win_First_u=288;
	public static final float Game_Win_First_d=340;
	
	public static final float Game_Win_Two_l=519;
	public static final float Game_Win_Two_r=686;
	public static final float Game_Win_Two_u=288;
	public static final float Game_Win_Two_d=340;
	
	public static final float Menu_Start_l=723;
	public static final float Menu_Start_r=930;
	public static final float Menu_Start_u=11;
	public static final float Menu_Start_d=64;
	
	public static final float Menu_Set_l=723;
	public static final float Menu_Set_r=930;
	public static final float Menu_Set_u=115;
	public static final float Menu_Set_d=159;
	
	public static final float Menu_Guan_l=723;
	public static final float Menu_Guan_r=930;
	public static final float Menu_Guan_u=205;
	public static final float Menu_Guan_d=237;
	
	public static final float Menu_About_l=723;
	public static final float Menu_About_r=930;
	public static final float Menu_About_u=289;
	public static final float Menu_About_d=325;
	
	public static final float Menu_Help_l=723;
	public static final float Menu_Help_r=930;
	public static final float Menu_Help_u=335;
	public static final float Menu_Help_d=421;
	
	public static final float Menu_Quit_l=723;
	public static final float Menu_Quit_r=930;
	public static final float Menu_Quit_u=463;
	public static final float Menu_Quit_d=528;
	
	public static final float Select_1_l=485;
	public static final float Select_1_r=570;
	
	public static final float Select_2_l=649;
	public static final float Select_2_r=736;
	
	public static final float Select_3_l=810;
	public static final float Select_3_r=890;
	
	public static final float Select_1_u=62;
	public static final float Select_1_d=117;
	
	public static final float Select_2_u=176;
	public static final float Select_2_d=231;
	
	public static final float Select_3_u=283;
	public static final float Select_3_d=363;
	
	public static final float Select_back_l=693;
	public static final float Select_back_r=876;
	public static final float Select_back_u=409;
	public static final float Select_back_d=485;
	
	
	public static final float Set_kai_1_l=803;
	public static final float Set_kai_1_r=903;
	public static final float Set_kai_1_u=72;
	public static final float Set_kai_1_d=137;
	
	public static final float Set_kai_2_l=803;
	public static final float Set_kai_2_r=903;
	public static final float Set_kai_2_u=192;
	public static final float Set_kai_2_d=250;
	
	public static final float Set_back_l=683;
	public static final float Set_back_r=909;
	public static final float Set_back_u=363;
	public static final float Set_back_d=453;
	
	public static final float Help_pre_l=26.086956f;
	public static final float Help_pre_r=195.13043f;
	public static final float Help_pre_t=388.29077f;
	public static final float Help_pre_b=493.85068f;
	
	public static final float Help_next_l=729.3913f;
	public static final float Help_next_r=904.6956f;
	public static final float Help_next_t=387.7603f;
	public static final float Help_next_b=493.85068f;
	
	public static int idKey=0;
	
	public static boolean menu_flag=true;
	public static boolean set_flag=true;
	public static boolean about_flag=true;
	public static boolean select_flag=true;
	
	//�n��
	public static final float YAOGAN_UNIT_SIZE=0.35f;//�j�p
	
//	public static  float YAOGAN_WAI_LEFT=763.6f;//Ĳ����t��m
//	public static  float YAOGAN_WAI_RIGHT=938.2f;
//	public static  float YAOGAN_WAI_TOP=518.4f;
//	public static  float YAOGAN_WAI_BOTTOM=334.8f;
//	
	public static float YAOGAN_CENTER_X=0;//����
	public static float YAOGAN_CENTER_Y=0;
	public static float YAOGAN_R=91.8f;//�b�|
	
}

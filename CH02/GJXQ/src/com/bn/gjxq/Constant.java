package com.bn.gjxq;

import com.bn.gjxq.manager.ScreenScaleResult;
import com.bn.gjxq.manager.ScreenScaleUtil;

//用於管理公共常數的常數類別
public class Constant 
{
	public static WhichView currView;  
	
	public static Object initLock=new Object();
	
	public static float screenWidth;//螢幕寬度
	public static float screenHeight;//螢幕高度
	public static float ratio;//寬度/高度  
	
	public static final float bottom=-1;
	public static final float top=1;
	public static final float near=2f;
	public static final float far=400f;
    
	//設定時選取的玩家棋子彩色，0---黑色  1---白色
	public static int SELECTED_COLOR=1;
    
    //handler常數編號========================================
    //跳躍到游戲界面
    public static final int COMMAND_GOTO_GAME_VIEW=0;
    //顯示提示Toast
    public static final int COMMAND_TOAST_MSG=1;
    //跳躍到選單界面
    public static final int COMMAND_GOTO_MENU_VIEW=2;
    //顯示提示的dialog
    public static final int COMMAND_DIALOG_MSG=3;
    
    //是否使用音效
    public static boolean IS_YINXIAO=true;
    
    public static ScreenScaleResult screenScaleResult;
    public static float sx;
    public static float sy;
    public static void ScaleSR()
    {
    	screenScaleResult=ScreenScaleUtil.calScale(screenWidth, screenHeight);
    	sx=screenScaleResult.lucX;
    	sy=screenScaleResult.lucY;
    }
}

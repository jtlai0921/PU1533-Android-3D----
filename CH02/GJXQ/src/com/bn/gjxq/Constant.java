package com.bn.gjxq;

import com.bn.gjxq.manager.ScreenScaleResult;
import com.bn.gjxq.manager.ScreenScaleUtil;

//�Ω�޲z���@�`�ƪ��`�����O
public class Constant 
{
	public static WhichView currView;  
	
	public static Object initLock=new Object();
	
	public static float screenWidth;//�ù��e��
	public static float screenHeight;//�ù�����
	public static float ratio;//�e��/����  
	
	public static final float bottom=-1;
	public static final float top=1;
	public static final float near=2f;
	public static final float far=400f;
    
	//�]�w�ɿ�������a�Ѥl�m��A0---�¦�  1---�զ�
	public static int SELECTED_COLOR=1;
    
    //handler�`�ƽs��========================================
    //���D������ɭ�
    public static final int COMMAND_GOTO_GAME_VIEW=0;
    //��ܴ���Toast
    public static final int COMMAND_TOAST_MSG=1;
    //���D����ɭ�
    public static final int COMMAND_GOTO_MENU_VIEW=2;
    //��ܴ��ܪ�dialog
    public static final int COMMAND_DIALOG_MSG=3;
    
    //�O�_�ϥέ���
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

package com.bn.util;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.bn.box.ChiLun_Picture;
import com.bn.box.MuTong_Picture;
import com.bn.box.Rec_Picture;
import com.bn.box.Room;
import com.bn.box.TextureRectangular;
import com.bn.box.TextureRectangular_shuiping;
import com.bn.box.Texture_MuTong;
import com.bn.box.Texture_Rain;
import com.bn.box.Texture_Yuanzhu;
import com.bn.box.TxingPicture;
import com.bn.zxl.GameView;
import com.bn.zxl.Texture_Level_Change;

public class Constant 
{
	public static float SCREEN_WIDTH;
	public static float SCREEN_HEIGHT;
	public static  Vec2 GRAVITYTEMP=new Vec2(0.0f,10.0f);//�{�ɭ��O�ѦҶq�A�ѭ��O�P�������L������
	public static float Width=960;//�Э�ù��e��
	public static float Height=540;//�Э�ù�����
	public static float RATIO=Width/Height;
	public static float Z_R=0.166f;//�Э�`��
	public static float R=20f;//�Э�`��
	public static float RATE=10f;//�Э�`��
	
	public static final int BUTTON_SOUND=0;//����id	
	
	private static boolean EFFECT_SOUND_STATUS;//���Ī��A
	private static boolean BACKGROUND_MUSIC_STATUS;//�I�����֪��A
	private static int PASS_NUM;//�z�L������
	
	public static boolean DRAW_THREAD_FLAG=true;//�@��ø�e
	public static boolean Change_Thread_Flag=false;//�}�l�h����
	public static boolean Add_Speed_Flag=false;
	public static boolean Touch_Flag=false;//���U
	public static boolean Add_Flag=false;//�[�J�@�ӭ���A��_�mtrue
	public static boolean Array_Null_Flag=true;//�x�U�������
	public static boolean Level_Fail_Flag=false;//�L���ЧӦ�
	public static boolean RESTART=false;//���s�}�l
	public static boolean Load_Finish=false;
	public static boolean LOAD_ACTIVITY=false;
	
	public static float TIME_STEP=4f/60f;
	public static int ITERATIONS=10;
	
	public static Room room;
	public static Texture_Yuanzhu ChiLun_Cebi;
	public static TextureRectangular ChiLun;
	public static TextureRectangular Cloud;
	public static TextureRectangular ObjectArrayTexture;
	public static Texture_MuTong Mutong_cebi;
	public static TextureRectangular_shuiping MuTong_di;
	public static TextureRectangular_shuiping MuTong_di_1;
	public static Texture_Rain Rain;
	public static Rec_Picture recPicture;
	public static MuTong_Picture mutongPicture;
	public static ChiLun_Picture chilunPicture;
	public static Texture_Level_Change levelexit;
	public static Texture_Level_Change backGround;
	public static TxingPicture tXing;
	
	public static float Cloud_Position=100;
	public static float Cloud_Current_Position=100;
	public static float Touch_X=400;
	public static float Touch_Y=200;
	public static float BoxPosition_x=0;
	public static float BoxPosition_y=0;
	public static int LastLevel=0;
	public static int CurrentLevel=0;
	
	public static  Bitmap[] TP_ARRAY;//�Ϥ��}�C
	public static String[] PicNum=
	{
		"box.png",//��c���z
		"bucket.png",//�K���z
		"biggear.png",//�������z
		"clouds.png",//���m
		"cask.png",//����z
		"Level_close.png",//�L������X�ɭ�
		"background1.png",//�I�����z
		"sidemenu1.png",//���V�U�@��
		"sidemenu2.png",//���^����
		"stone1.png",//�Q�r�[
		"stone2.png",
		"stone5.png",//�B�I
		"stone9.png",
		"wall.png",
		"wall_heng.png",//���j��
		"zhadan.png",
		"diban.png",
		"ding.png"
	};
	public static String[] pic2D=
		{
				//welcomeview
				//"androidheli.png",  //logo�Ϥ�
				//mainmenuview
				"mainmenu.png",
				"play_up.png",
				"play_down.png",
				"bg_music_open.png",
				"bg_music_close.png",
				"e_music_open.png",
				"e_music_close.png",
				"help_button.png",
				//selectview
				"selectViewBackground.png",//�I����
				"back.png", //�Ǧ^���s��
				"room_1_open.png",
				"room_2_open.png",
				"room_3_open.png",
				"room_4_open.png",
				"room_5_open.png",
				"room_6_open.png",
				"room_2_close.png",
				"room_3_close.png",
				"room_4_close.png",
				"room_5_close.png",
				"room_6_close.png",
				//helpview
				"help.png"
		};
	public static HashMap<String, Bitmap> pic2DHashMap;
	public static void init2DPic(Resources resources)
	{
		pic2DHashMap=new HashMap<String, Bitmap>();
		for(String picname:pic2D)
		{
			pic2DHashMap.put(picname, PicLoadUtil.loadBM(resources, picname));
		}
	}
	public static String [][] ObjectList=
	{
		{
			"Rec","MuTong"
		},
		{
			"Rec"
		},
		{
			"Rec","MuTong"
		},
		{
			"ChiLun","ChiLun","ChiLun"
		},
		{
			"Rec","ChiLun"
		},
		{
			"Rec","Rec","ChiLun"
		}
	};
	
	
	public static void initObjectTexture(GameView gameview)//�_�l�Ư��z����
	{
		ChiLun_Cebi=new Texture_Yuanzhu(gameview,40,30);
		ChiLun=new TextureRectangular(80,80);
		Mutong_cebi=new Texture_MuTong(gameview,27,70);
		MuTong_di=new TextureRectangular_shuiping(54,76);
		MuTong_di_1=new TextureRectangular_shuiping(54,-76);
		Cloud=new TextureRectangular(200,100);
		Rain=new Texture_Rain(20,20);
		ObjectArrayTexture=new TextureRectangular(60,60);
		recPicture =new Rec_Picture(gameview,106,76);
		mutongPicture=new MuTong_Picture(gameview);
		chilunPicture=new ChiLun_Picture(30f,gameview);
//		levelexit=new Texture_Level_Change(500,400,1);
		levelexit=new Texture_Level_Change(500,500,1);
//		backGround=new Texture_Level_Change(960,540,-1);
		backGround=new Texture_Level_Change(1160,700,-1);
		room=new Room(gameview,1160,700,1);
		tXing=new TxingPicture(24,8);
	}
	public static void initBitmap(Resources resources)
	{
		TP_ARRAY=null;
		TP_ARRAY=new Bitmap[PicNum.length];
		for(int i=0;i<PicNum.length;i++)//�N�Ϥ����J��Bitmap
		{
			TP_ARRAY[i]=PicLoadUtil.loadBM(resources, PicNum[i]);
		}
	}
	public static ScreenScaleResult ssr;
	
	public static boolean bitmapHitTest(float touch_x,float touch_y,float left,float top,Bitmap bitmap) 
	{
		if(bitmap!=null)
		{
			if(
					touch_x>=(left+ssr.lucX)*ssr.ratio&&
					touch_x<=(left+bitmap.getWidth()+ssr.lucX)*ssr.ratio&&
					touch_y>=(top+ssr.lucY)*ssr.ratio&&
					touch_y<=(top+bitmap.getHeight()+ssr.lucY)*ssr.ratio
			   )
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean getEffectSoundStatus() 
	{
		return EFFECT_SOUND_STATUS;
	}
	
	public static boolean getBackgroundMusicStatus() 
	{
		return BACKGROUND_MUSIC_STATUS;
	}
	
	public static int getPassNum() 
	{
		return PASS_NUM;
	}
	
	public static void setEffectSoundStatus(SharedPreferencesUtil spu,boolean value)
	{
		EFFECT_SOUND_STATUS=value;
		spu.putEffectSoundStatus(value);
	}
	
	public static void setBackgroundMusicStatu(SharedPreferencesUtil spu,boolean value)
	{
		BACKGROUND_MUSIC_STATUS=value;
		spu.putBackgroundMusicStatus(value);
	}
	
	public static void setPassNum(SharedPreferencesUtil spu,int value)
	{
		PASS_NUM=value;
		if (value>=0&&value<=6) 
		{
			spu.putPassNum(value);
		}
	}
	
	public static void initPlayerPrefers(SharedPreferencesUtil spu) 
    {
		EFFECT_SOUND_STATUS=spu.getEffectSoundStatus();
		BACKGROUND_MUSIC_STATUS=spu.getBackgroundMusicStatus();
		PASS_NUM=spu.getPassNum();
	}
	
}

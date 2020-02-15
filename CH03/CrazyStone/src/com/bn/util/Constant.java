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
	public static  Vec2 GRAVITYTEMP=new Vec2(0.0f,10.0f);//臨時重力參考量，由重力感知器給他給予值
	public static float Width=960;//標准螢幕寬度
	public static float Height=540;//標准螢幕高度
	public static float RATIO=Width/Height;
	public static float Z_R=0.166f;//標准深度
	public static float R=20f;//標准深度
	public static float RATE=10f;//標准深度
	
	public static final int BUTTON_SOUND=0;//音效id	
	
	private static boolean EFFECT_SOUND_STATUS;//音效狀態
	private static boolean BACKGROUND_MUSIC_STATUS;//背景音樂狀態
	private static int PASS_NUM;//透過的關數
	
	public static boolean DRAW_THREAD_FLAG=true;//一直繪畫
	public static boolean Change_Thread_Flag=false;//開始搬移雲
	public static boolean Add_Speed_Flag=false;
	public static boolean Touch_Flag=false;//按下
	public static boolean Add_Flag=false;//加入一個剛體，抬起置true
	public static boolean Array_Null_Flag=true;//灑下木塊完畢
	public static boolean Level_Fail_Flag=false;//過關標志位
	public static boolean RESTART=false;//重新開始
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
	
	public static  Bitmap[] TP_ARRAY;//圖片陣列
	public static String[] PicNum=
	{
		"box.png",//木箱紋理
		"bucket.png",//鐵桶紋理
		"biggear.png",//齒輪紋理
		"clouds.png",//雲彩
		"cask.png",//木桶紋理
		"Level_close.png",//過關後跳出界面
		"background1.png",//背景紋理
		"sidemenu1.png",//跳向下一關
		"sidemenu2.png",//跳回本關
		"stone1.png",//十字架
		"stone2.png",
		"stone5.png",//雨點
		"stone9.png",
		"wall.png",
		"wall_heng.png",//紅磚牆
		"zhadan.png",
		"diban.png",
		"ding.png"
	};
	public static String[] pic2D=
		{
				//welcomeview
				//"androidheli.png",  //logo圖片
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
				"selectViewBackground.png",//背景圖
				"back.png", //傳回按鈕圖
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
	
	
	public static void initObjectTexture(GameView gameview)//起始化紋理物件
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
		for(int i=0;i<PicNum.length;i++)//將圖片載入成Bitmap
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

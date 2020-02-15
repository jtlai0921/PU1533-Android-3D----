package com.f.pingpong;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;  
import java.util.Map;  

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.f.util.From2DTo3DUtil;
import com.f.util.LoadObjData;
import com.f.util.ScreenScaleResult;
import com.f.util.ScreenScaleUtil;
import com.zyz.data.ObjData;

public final class Constant {

	/** UNIT_SIZE */
	public static final float UNIT_SIZE = 2.0f;
	
	//第一個參數為執行震動方法後多長時間開始震動
	//第二個參數為震動持續時間，兩個參數必須為long型態
	public static long[] COLLISION_SOUND_PATTERN={0l,30l};
	
	/** 球台面的長長寬*/
	public static final float TABLE_LENGTH = 2.74f  * UNIT_SIZE;//長    //1.6f 
	public static final float TABLE_WIDHT  = 1.525f * UNIT_SIZE;//寬
	public static final float TABLE_HEIGHT = 0.05f  * UNIT_SIZE;//高

	/** 球拍的長和寬*/
	public static final float RACKETS_LENGTH = 0.376f * UNIT_SIZE /1.5f;//0.188f * UNIT_SIZE;
	public static final float RACKETS_WIDTH = 0.585f * UNIT_SIZE /1.5f;//0.182f * UNIT_SIZE;
	
	/** 球的半徑*/
	public static final float BALL_R = 0.02f * UNIT_SIZE;
	/** 主界面球的半徑*/
	public static final float MAINBALL_R = 0.03f * UNIT_SIZE;
	//球的半徑與主界面球的半徑比值
	public static final float BALL_MBALL = BALL_R / MAINBALL_R;
	/** 正方體天空盒  長長寬相等**/
	public static final float ROOM_WIDTH = 16 * UNIT_SIZE;
	
	/** 網的寬和高*/
	public static final float NET_WIDTH = 1.83f * UNIT_SIZE;
	public static final float NET_HEIGHT = 0.1525f * UNIT_SIZE;//0.1525f
	public static final float WIDTH_SPAN=3.3f;//2.8f;//水平長度總跨度
	
	//
	public static final int SIDE_AI = 1;
	public static final int SIDE_HAND = 0;
	
	//選取國家
	public static int WHICH_COUNTRY = -1;
	
	//迭代頻率
	public static final float TIME_UNIT = 0.05f;
	
	//飄揚的旗幟
	public static final boolean FLAG_FLY_THREAD = true;

	//是否處於
	public static boolean IS_SHOOTING     = true;
	
	//是否是人發球以後第一次沾到球台
	public static boolean IS_SHOOT_BALL_2 = true;
	
	//預設人工發球
	public static boolean IS_SHOOT_MAN    = true ;
	
	//游戲是否結束
	public static boolean IS_GAME_OVER    = false;
	
	//發球延時
	public static boolean CHECKING_POINTS = false;
	
	//是否暫停
	public static boolean PAUSE           = false;
	
	//幫助暫停
	public static boolean HELP_PAUSE      = false;
	
	//是否是扇球 
	public static boolean IS_PADDLE       = false;
	
	//是否是第一次載入
	public static boolean IS_FIRSTLOADING_MAIN = true;
	
	public static boolean IS_FIRSTLOADING_GAME = true;
	
	public static boolean IS_PLAY_VIDEO = false;
	
	public static Object videoLock = new Object();
	public static Object soundTaskLock = new Object();
	
	public static int COUNTRYME = 0;
	public static int COUNTRYAI = 0;
	
	public static boolean IS_HELP = false;
	
	
	public static void resetFlags()
	{
		//是否處於
		IS_SHOOTING     = true;
		
		//是否是人發球以後第一次沾到球台
		IS_SHOOT_BALL_2 = true;
		
		//預設人工發球
		IS_SHOOT_MAN    = true ;
		
		//發球延時
		CHECKING_POINTS = false;
		
		//是否暫停
		PAUSE           = false;
	}
	
	//難度控制常數
	public static  class DifficultyContorl
	{
		public static int DIFFICULTY = -1;
		//三個難度控制量
		public static float BALL_SPEED_Y = 0.65f;
		public static float RACKETS_STEP = 0.03f;
		public static float RANDOM_ZOOM  = 0.0f;
		
		//AI失球機率
		public static float LOST_CHANCE = 0.5f;
		
		//三個難度等級
		private static final float [] BALL_SPEED_Y_LEVEL = { 0.75f, 0.60f, 0.50f, 0.45f, 0.30f, 0.25f};
		private static final float [] RACKETS_STEP_LEVEL = { 0.03f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f}; 
		private static final float [] RANDOM_ZOOM_LEVEL  = { 0.20f, 0.30f, 0.40f, 0.50f, 0.60f, 0.70f};
		//AI失球機率等級
		private static final float [] LOST_CHANCE_LEVEL  = { 0.40f, 0.35f, 0.25f, 0.20f, 0.15f, 0.10f};
		
		public static void selectCountries(int country)
		{
			BALL_SPEED_Y = countries[country][0];
			RACKETS_STEP = countries[country][1];
			RANDOM_ZOOM  = countries[country][2];
			LOST_CHANCE  = countries[country][3];
		}
		private static final float [][]  countries = 
		{
			{BALL_SPEED_Y_LEVEL[0],RACKETS_STEP_LEVEL[0],RANDOM_ZOOM_LEVEL[3],LOST_CHANCE_LEVEL[2]},
			{BALL_SPEED_Y_LEVEL[1],RACKETS_STEP_LEVEL[1],RANDOM_ZOOM_LEVEL[1],LOST_CHANCE_LEVEL[1]},
			{BALL_SPEED_Y_LEVEL[2],RACKETS_STEP_LEVEL[3],RANDOM_ZOOM_LEVEL[3],LOST_CHANCE_LEVEL[2]},
			{BALL_SPEED_Y_LEVEL[4],RACKETS_STEP_LEVEL[3],RANDOM_ZOOM_LEVEL[2],LOST_CHANCE_LEVEL[3]},
			{BALL_SPEED_Y_LEVEL[3],RACKETS_STEP_LEVEL[4],RANDOM_ZOOM_LEVEL[4],LOST_CHANCE_LEVEL[4]},
			{BALL_SPEED_Y_LEVEL[5],RACKETS_STEP_LEVEL[5],RANDOM_ZOOM_LEVEL[5],LOST_CHANCE_LEVEL[4]},
		}; 
		
	}
	
	public static float SCREEN_WIDTH_STANDARD=800;  //螢幕標准寬度	
	public static float SCREEN_HEIGHT_STANDARD=480;  //螢幕標准高度
	public static float SCREEN_WIDTH;//螢幕寬度
	public static float SCREEN_HEIGHT;//螢幕高度
	public static float RATIO=SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//螢幕長寬比
	
	//自適應螢幕工具類別物件
	public static ScreenScaleResult ssr;
	public static boolean XSFLAG=false;//執行scaleCL()標志位\
	public static float[][] MAINVIEW_TOUCH_AREA;
	public static float[][] GAMEVIEW_TOUCH_AREA;
	
	public static String[] MAINVIEW_PIC_ID = new String[]  
  	{
  		"title.png",					//0
  		"set.png",						//1 
  		"play.png",						//2
  		"help.png",						//3
  		"flag1.png",					//4
  		"flag2.png",					//5
  		"flag3.png",					//6
  		"flag4.png",					//7
  		"flag5.png",					//8
  		"flag6.png",					//9
  		"select_opponent.png",			//10
  		"back.png",						//11  
  		"sound.png",					//12
  		"vibrate.png",					//13
  		"select.png",					//14
  		"select_null.png",				//15
  		"forward.png",					//16
  		"select_country.png"			//17
  	};
	
	public static String[] OBJ_PIC_ID = new String[]
	{
		"bat.pkm",						//0
		"qiuan.pkm",					//1  
		"floor_main1.pkm",				//2
		"floor_main2.pkm",				//3
		"shadow.pkm",					//4
		"ball.pkm",						//5
		"spring.pkm",					//6
		"surface1.pkm",					//7
		"surface2.pkm"					//8
	};
	
	private static String [] LOCK_FlAG =
	{
		"flag2lock.png",		//1
  		"flag3lock.png",		//2
  		"flag4lock.png",		//3
  		"flag5lock.png",		//4
  		"flag6lock.png" 		//5  
	};
	
	public static String[] WALL_PAPER = 
	{
		"flag1wall.png",
		"flag2wall.png",
		"flag3wall.png",
		"flag4wall.png",
		"flag5wall.png",
		"flag6wall.png"
	};
	
	private static String [] BATS =
	{
		"bat_hum0.png",
		"bat_hum1.png",
		"bat_hum2.png",
		"bat_hum3.png",
		"net1.png",
		"helphand.png"
	};
	
	public static String[] GAMEVIEW_PIC_ID = new String[]
	{
		"num_0.png",					//0
		"num_1.png",					//1
		"num_2.png",					//2
		"num_3.png",					//3
		"num_4.png",					//4
		"num_5.png",					//5
		"num_6.png",					//6
		"num_7.png",					//7
		"num_8.png",					//8
		"num_9.png",					//9
		"player.png",					//10
		"flag1.png",					//11
		"flag2.png",					//12
		"flag3.png",					//13
		"flag4.png",					//14
		"flag5.png",					//15
		"flag6.png",					//16
		"win.png",						//17
		"lose.png",						//18
		"pause.png",					//19
		"replay.png",					//20
		"play.png",						//21
		"mainmenu.png",					//22
		"next.png",						//23
		"select.png",					//24
		"not.png",						//25
		"playback.png",					//26
		"helpsm.png"					//27
	};
	
	public static byte[][] MAINMENU_PIC_BYTE;
	public static byte[][] LOCK_FlAG_BYTE;
	public static byte[][] OBJ_PIC_BYTE;
	
	public static void loadMainPicByByte(Resources res)
	{
		MAINMENU_PIC_BYTE = new byte[MAINVIEW_PIC_ID.length][];
		LOCK_FlAG_BYTE = new byte[LOCK_FlAG.length][];
		OBJ_PIC_BYTE = new byte[OBJ_PIC_ID.length][];
		for(int i = 0;i < MAINVIEW_PIC_ID.length;i++)
		{
			MAINMENU_PIC_BYTE[i] = getAssets(res, MAINVIEW_PIC_ID[i]);
		}
		for(int i = 0;i < LOCK_FlAG.length;i++)
		{
			LOCK_FlAG_BYTE[i] = getAssets(res, LOCK_FlAG[i]);
		}
		for(int i = 0;i < OBJ_PIC_ID.length;i++)
		{
			OBJ_PIC_BYTE[i] = getAssets(res, OBJ_PIC_ID[i]);
		}
	}
	
	public static byte[][] GAMEVIEW_PIC_BYTE;
	public static byte[][] BATS_BYTE;
	public static byte[][] WALL_PAPER_BYTE;
	
	public static void loadGamePicByByte(Resources res)
	{
		GAMEVIEW_PIC_BYTE = new byte[GAMEVIEW_PIC_ID.length][];
		BATS_BYTE = new byte[BATS.length][];
		WALL_PAPER_BYTE = new byte[WALL_PAPER.length][];
		for(int i = 0;i < GAMEVIEW_PIC_ID.length;i++)
		{
			GAMEVIEW_PIC_BYTE[i] = getAssets(res, GAMEVIEW_PIC_ID[i]);
		}
		for(int i = 0;i < BATS.length;i++)
		{
			BATS_BYTE[i] = getAssets(res, BATS[i]);
		}
		for(int i = 0;i < WALL_PAPER.length;i++)
		{
			WALL_PAPER_BYTE[i] = getAssets(res, WALL_PAPER[i]);
		}
	}
	
	public static byte[] getAssets(Resources res, String fname) 
   {
		byte[] data = null;
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new BufferedInputStream(res.getAssets().open(fname));
			out = new ByteArrayOutputStream(1024);
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			data = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	
	public static float[][] MAINMENU_PIC_LS = new float[][]
  	{
  		{280,50,500,80},	//0標題
  		{250,250,100,100},	//1設定按鈕
  		{400,250,100,100},	//2開始按鈕
  		{550,250,100,100},	//3幫助按鈕
  		{224,212,128,64},	//4國旗1
  		{400,212,128,64},	//5國旗2
  		{576,212,128,64},	//6國旗3
  		{224,312,128,64},	//7國旗4
  		{400,312,128,64},	//8國旗5
  		{576,312,128,64},	//9國旗6
  		{400,132,512,64},	//10選取對手
  		{80,410,100,100},	//11傳回
  		{280,210,240,80},	//12音效
  		{540,210,80,80},	//13音效開關
  		{280,330,240,80},	//14震動
  		{540,330,80,80},	//15震動開關
  		{720,410,100,100},	//16前進
  		{400,132,512,64}	//17選取國家
  	};
	
	public static int[][] GAMEVIEW_PIC_LS = new int[][]
	{
		{74,52,128,64},		//0玩家旗幟
		{726,52,128,64},	//1對手國旗
		{163,52,32,64},		//2玩家得分十位
		{195,52,32,64},		//3玩家得分個位
		{605,52,32,64},		//4對手得分個位
		{637,52,32,64},		//5對手得分十位
		{400,240,512,128},	//6勝利失敗
		{740,410,80,80},	//7暫停按鈕
		{250,250,100,100},	//8重玩
		{400,250,100,100},	//9繼續
		{550,250,100,100},	//10主選單
		{280,250,100,100},	//11失敗重玩
		{520,250,100,100},	//12失敗主選單
		{280,250,100,100},	//13播放影片
		{520,250,100,100},	//14不播放影片
		{400,150,512,64},	//15是否重播影片
		{80,680,128,512}	//16幫助說明
	};
	
	public static void scaleCL()
	{
		if(XSFLAG) return;        
        ssr=ScreenScaleUtil.calScale(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        MAINVIEW_TOUCH_AREA = new float[MAINVIEW_PIC_ID.length][4];
        for(int i = 0;i < MAINVIEW_PIC_ID.length;i++)
        {
        	MAINVIEW_TOUCH_AREA[i][0] = ((MAINMENU_PIC_LS[i][0] - MAINMENU_PIC_LS[i][2]/2) + ssr.lucX) * ssr.ratio;
        	MAINVIEW_TOUCH_AREA[i][2] = MAINMENU_PIC_LS[i][2] * ssr.ratio;
        	MAINVIEW_TOUCH_AREA[i][1] = ((MAINMENU_PIC_LS[i][1] - MAINMENU_PIC_LS[i][3]/2) + ssr.lucY) * ssr.ratio;
        	MAINVIEW_TOUCH_AREA[i][3] = MAINMENU_PIC_LS[i][3] * ssr.ratio;
        }
        GAMEVIEW_TOUCH_AREA = new float[GAMEVIEW_PIC_LS.length - 7][4];
        for(int i = 0;i < GAMEVIEW_PIC_LS.length - 7;i++)
        {
        	GAMEVIEW_TOUCH_AREA[i][0] = ((GAMEVIEW_PIC_LS[i+7][0] - GAMEVIEW_PIC_LS[i+7][2]/2) + ssr.lucX) * ssr.ratio;
        	GAMEVIEW_TOUCH_AREA[i][2] = GAMEVIEW_PIC_LS[i+7][2] * ssr.ratio;
        	GAMEVIEW_TOUCH_AREA[i][1] = ((GAMEVIEW_PIC_LS[i+7][1] - GAMEVIEW_PIC_LS[i+7][3]/2) + ssr.lucY) * ssr.ratio;
        	GAMEVIEW_TOUCH_AREA[i][3] = GAMEVIEW_PIC_LS[i+7][3] * ssr.ratio;
        }
        
        XSFLAG=true;
	}
	
	//主界面紋理ID陣列
	public static int[] MAINVIEW_TEXTURE_ID;
	public static int[] GAMEVIEW_TEXTURE_ID;
	public static int[] LOCK_FlAG_TEXTURE_ID;
	public static int[] BATS_TEXTURE_ID;
	public static int[] WALL_TEXTURE_ID;
	public static int[] OBJ_TEXTURE_ID;

	//載入紋理ID
	public static void loadMainTextureId()
	{
		MAINVIEW_TEXTURE_ID = new int[MAINVIEW_PIC_ID.length];
		for(int i = 0;i < MAINVIEW_PIC_ID.length;i++)
		{
			MAINVIEW_TEXTURE_ID[i] = initTextureByByte(MAINMENU_PIC_BYTE[i]);
		}
		OBJ_TEXTURE_ID = new int[OBJ_PIC_ID.length];
		for(int i = 0;i < OBJ_PIC_ID.length - 3;i++)
		{
			OBJ_TEXTURE_ID[i] = initOBJTextureByByte(OBJ_PIC_BYTE[i]);
		}
	}
	public static void loadLockFlagTextureId()
	{
		LOCK_FlAG_TEXTURE_ID = new int[LOCK_FlAG.length];
		for(int i = 0 ;i < LOCK_FlAG.length ; i++)
		{
			LOCK_FlAG_TEXTURE_ID[i] = initTextureByByte(LOCK_FlAG_BYTE[i]);
		}
		
	}
	//載入紋理ID
	public static void loadGameTextureId()
	{
		GAMEVIEW_TEXTURE_ID = new int[GAMEVIEW_PIC_ID.length];
		for(int i = 0;i < GAMEVIEW_PIC_ID.length;i++)
		{
			GAMEVIEW_TEXTURE_ID[i] = initTextureByByte(GAMEVIEW_PIC_BYTE[i]);
		}
		OBJ_TEXTURE_ID[0] = initOBJTextureByByte(OBJ_PIC_BYTE[0]);
		OBJ_TEXTURE_ID[2] = initOBJTextureByByte(OBJ_PIC_BYTE[2]);
		OBJ_TEXTURE_ID[4] = initOBJTextureByByte(OBJ_PIC_BYTE[4]);
		OBJ_TEXTURE_ID[5] = initOBJTextureByByte(OBJ_PIC_BYTE[5]); 
		OBJ_TEXTURE_ID[6] = initOBJTextureByByte(OBJ_PIC_BYTE[6]);
		OBJ_TEXTURE_ID[7] = initOBJTextureByByte(OBJ_PIC_BYTE[7]);
		OBJ_TEXTURE_ID[8] = initOBJTextureByByte(OBJ_PIC_BYTE[8]);
	}
	public static void loadBatsTextureId()
	{
		BATS_TEXTURE_ID = new int[BATS.length];
		for(int i = 0 ; i < BATS.length ; i++)
		{
			BATS_TEXTURE_ID[i] = initTextureByByte(BATS_BYTE[i]);
		}
	}
	
	public static void loadWallTextureId()
	{
		WALL_TEXTURE_ID = new int[WALL_PAPER.length];
		for(int i = 0 ; i < WALL_PAPER.length ; i++)
		{
			WALL_TEXTURE_ID[i] = initTextureByByte(WALL_PAPER_BYTE[i]);
		}
	}
	
	//打到第幾個國家
	public static int PASS_COUNTRY ;

	public static Map<String,Boolean> parsePassCountry(int passCountry)
	{
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		for(int i=0;i < passCountry; i++){
			map.put("country"+i, true);
		}
		for(int i = passCountry;i<6; i++){
			map.put("country"+i, false);
		}
		return map;
	}
	
	public static int initTextureByByte(byte[] data)
	{
		Bitmap bitmapTmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures,0);
		int textureId = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
		bitmapTmp.recycle();
		return textureId;
	}
	
	public static int initOBJTextureByByte(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		int textureId = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		try {
			ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D, 0, 0, GLES20.GL_RGB,	GLES20.GL_UNSIGNED_BYTE, in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return textureId;
	}
	
	public static int initTexture(Resources res, String fname)// textureId
	{
		// 產生紋理ID
		int[] textures = new int[1];
		GLES20.glGenTextures(1, // 產生的紋理id的數量
				textures, // 紋理id的陣列
				0 // 偏移量
		);
		int textureId = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		// 透過輸入流載入圖片===============begin===================
		InputStream is = null;
		try {
			is = res.getAssets().open(fname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bitmap bitmapTmp = null;
		try {
			bitmapTmp = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 透過輸入流載入圖片===============end=====================
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
		bitmapTmp.recycle(); // 紋理載入成功後釋放圖片
		return textureId;
	}
	
	public static int [][] indexlength = new int [2][2];
	public static int [][] vCount = new int [2][10];
	public static FloatBuffer [][] vertexBuffer  = new FloatBuffer [2][15];
	public static FloatBuffer [][] texCoorBuffer = new FloatBuffer [2][10];
	public static FloatBuffer [][] normalBuffer  = new FloatBuffer [2][10];
	public static ShortBuffer [][] indexBuffer   = new ShortBuffer [2][10];
	
	public static void loadMainBuffer(Context context)
	{
		float[] vertices = null;
		float[] textureCoors = null;
		float[] normal = null;
		short[] index = null;
		ByteBuffer vbb = null;
		ByteBuffer tbb = null;
		ByteBuffer nbb = null;
		ByteBuffer ibb = null;
		
		//mr1頂點緩沖
		vertices = From2DTo3DUtil.Vertices(MAINMENU_PIC_LS[0][2],MAINMENU_PIC_LS[0][3]);
		vCount[0][0] = vertices.length/3;//MainRect頂點數量
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[0][0] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[0][0].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[0][0].position(0);//設定緩沖區起始位置
		//mr2頂點緩沖
		vertices = From2DTo3DUtil.Vertices(MAINMENU_PIC_LS[1][2],MAINMENU_PIC_LS[1][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[0][1] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[0][1].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[0][1].position(0);//設定緩沖區起始位置
		//mr3頂點緩沖
		vertices = From2DTo3DUtil.Vertices(MAINMENU_PIC_LS[4][2],MAINMENU_PIC_LS[4][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[0][2] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[0][2].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[0][2].position(0);//設定緩沖區起始位置
		//mr4頂點緩沖
		vertices = From2DTo3DUtil.Vertices(MAINMENU_PIC_LS[10][2],MAINMENU_PIC_LS[10][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[0][3] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[0][3].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[0][3].position(0);//設定緩沖區起始位置
		//mr5頂點緩沖
		vertices = From2DTo3DUtil.Vertices(MAINMENU_PIC_LS[12][2],MAINMENU_PIC_LS[12][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[0][4] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[0][4].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[0][4].position(0);//設定緩沖區起始位置
		//mr6頂點緩沖
		vertices = From2DTo3DUtil.Vertices(MAINMENU_PIC_LS[13][2],MAINMENU_PIC_LS[13][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[0][5] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[0][5].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[0][5].position(0);//設定緩沖區起始位置
		//MainRect紋理緩沖
		textureCoors = new float[]
		{
			0.0f, 0.0f,
			0.0f, 1.0f, 
			1.0f, 0.0f,
			
			1.0f, 0.0f, 
			0.0f, 1.0f, 
			1.0f, 1.0f
		};
		tbb = ByteBuffer.allocateDirect(textureCoors.length*4);//建立頂點紋理資料緩沖
        tbb.order(ByteOrder.nativeOrder());//設定位元組順序
        texCoorBuffer[0][0] = tbb.asFloatBuffer();//轉為Float型緩沖
        texCoorBuffer[0][0].put(textureCoors);//向緩沖區中放入頂點著色資料
        texCoorBuffer[0][0].position(0);//設定緩沖區起始位置
        
        //MainBall頂點緩沖
        ArrayList<Float> alVertex = new ArrayList<Float>();
		final int angleSpan = 10;
		a:for(int vAngle = -90;vAngle<=90;vAngle+=angleSpan)
		{
			for(int hAngle = 0;hAngle <=360 ;hAngle+=angleSpan)
			{
				//1
				float x = (float) (MAINBALL_R*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle)));
				float y = (float) (MAINBALL_R*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle)));
				float z = (float) (MAINBALL_R*Math.sin(Math.toRadians(vAngle)));
				alVertex.add(x);
				alVertex.add(y);
				alVertex.add(z);
				//2
				if(vAngle+angleSpan>90)
				{
					break a;
				}
				float sx = (float) (MAINBALL_R*Math.cos(Math.toRadians(vAngle+angleSpan))*Math.cos(Math.toRadians(hAngle)));
				float sy = (float) (MAINBALL_R*Math.cos(Math.toRadians(vAngle+angleSpan))*Math.sin(Math.toRadians(hAngle)));
				float sz = (float) (MAINBALL_R*Math.sin(Math.toRadians(vAngle+angleSpan)));
				alVertex.add(sx);
				alVertex.add(sy);
				alVertex.add(sz);
			}
		}
		vCount[0][1] = alVertex.size()/3;//MainBall頂點數量
		vertices = new float[vCount[0][1] * 3];
		for (int i = 0; i < alVertex.size(); i++) 
		{
			vertices[i] = alVertex.get(i);
		}
		vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[0][6] = vbb.asFloatBuffer();
		vertexBuffer[0][6].put(vertices);
		vertexBuffer[0][6].position(0);
		//MainBall紋理座標緩沖
		textureCoors = generateBallTexCoor(angleSpan);
        tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());
        texCoorBuffer[0][1] = tbb.asFloatBuffer();
        texCoorBuffer[0][1].put(textureCoors);
        texCoorBuffer[0][1].position(0);
        //MainBall法向量緩沖
        nbb = ByteBuffer.allocateDirect(vertices.length*4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer[0][0] =nbb.asFloatBuffer();
        normalBuffer[0][0].put(vertices);
        normalBuffer[0][0].position(0);
        
        //球拍資料緩沖
        LoadObjData  loadObjData=new LoadObjData(context,"mainbat.data");
        ObjData batData = loadObjData.getObjData();
        //球拍頂點緩沖
        vertices = batData.getVertices();
        vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[0][7] = vbb.asFloatBuffer();
		vertexBuffer[0][7].put(vertices);
		vertexBuffer[0][7].position(0);
		//球拍紋理緩沖
		textureCoors = batData.getTextures();
		tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());
        texCoorBuffer[0][2] = tbb.asFloatBuffer();
        texCoorBuffer[0][2].put(textureCoors);
        texCoorBuffer[0][2].position(0);
        //球拍法向量緩沖
        normal = batData.getNormals();
        nbb = ByteBuffer.allocateDirect(normal.length*4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer[0][1] = nbb.asFloatBuffer();
        normalBuffer[0][1].put(normal);
        normalBuffer[0][1].position(0);
        //球拍索引緩沖
        index = batData.getIndices();
        indexlength[0][0] = index.length;
        ibb = ByteBuffer.allocateDirect(index.length*2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer[0][0] = ibb.asShortBuffer();
        indexBuffer[0][0].put(index);
        indexBuffer[0][0].position(0);
        
        //球桌資料緩沖
        loadObjData=new LoadObjData(context,"maintable.data");
        ObjData tableData=loadObjData.getObjData();
        //球桌頂點緩沖
        vertices = tableData.getVertices();
        vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[0][8] = vbb.asFloatBuffer();
		vertexBuffer[0][8].put(vertices);
		vertexBuffer[0][8].position(0);
		//球桌紋理緩沖
		textureCoors = tableData.getTextures();
		tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());
        texCoorBuffer[0][3]=tbb.asFloatBuffer();
        texCoorBuffer[0][3].put(textureCoors);
        texCoorBuffer[0][3].position(0);
        //球桌法向量緩沖
        normal = tableData.getNormals();
        nbb = ByteBuffer.allocateDirect(normal.length*4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer[0][2] = nbb.asFloatBuffer();
        normalBuffer[0][2].put(normal);
        normalBuffer[0][2].position(0);
        //球桌索引緩沖
        index = tableData.getIndices();
        indexlength[0][1] = index.length;
        ibb = ByteBuffer.allocateDirect(index.length*2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer[0][1] = ibb.asShortBuffer();
        indexBuffer[0][1].put(index);
        indexBuffer[0][1].position(0);
        
        //地板頂點緩沖
        vertices=new float[]
   		{
   			-UNIT_SIZE * 8/2,0,-UNIT_SIZE * 8/2,
   			-UNIT_SIZE * 8/2,0,UNIT_SIZE * 8/2,
   			UNIT_SIZE * 8/2,0,-UNIT_SIZE * 8/2,
   			
   			UNIT_SIZE * 8/2,0,-UNIT_SIZE * 8/2,
   			-UNIT_SIZE * 8/2,0,UNIT_SIZE * 8/2,
   			UNIT_SIZE * 8/2,0,UNIT_SIZE * 8/2
   		};
        vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[0][9] = vbb.asFloatBuffer();
		vertexBuffer[0][9].put(vertices);
		vertexBuffer[0][9].position(0);
		//地板法向量緩沖
		normal = new float[]
		{
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0
		};
		nbb = ByteBuffer.allocateDirect(normal.length*4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer[0][3] = nbb.asFloatBuffer();
        normalBuffer[0][3].put(normal);
        normalBuffer[0][3].position(0);
        
        //飄揚的旗幟緩沖
        final int cols=12;//列數
    	final int rows=cols/2;//行數
    	final float UNIT_SIZE=WIDTH_SPAN/cols;//每格的單位長度
    	vCount[0][2] = cols*rows*6;//FlyFlag頂點數量
    	//飄揚的旗幟頂點緩沖
        vertices = new float[vCount[0][2]*3];
        int count=0;//頂點計數器
        for(int j=0;j<rows;j++)
        {
        	for(int i=0;i<cols;i++)
        	{        		
        		//計算目前格子左上側點座標 
        		float zsx=-UNIT_SIZE*cols/2+i*UNIT_SIZE;
        		float zsy=UNIT_SIZE*rows/2-j*UNIT_SIZE;
        		float zsz=0;
       
        		vertices[count++]=zsx;
        		vertices[count++]=zsy;
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx;
        		vertices[count++]=zsy-UNIT_SIZE;
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=zsy;
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=zsy;
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx;
        		vertices[count++]=zsy-UNIT_SIZE;
        		vertices[count++]=zsz;
        		        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=zsy-UNIT_SIZE;
        		vertices[count++]=zsz; 
        	}
        }
        vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[0][10] = vbb.asFloatBuffer();
		vertexBuffer[0][10].put(vertices);
		vertexBuffer[0][10].position(0);
		//飄揚的旗幟紋理緩沖
		textureCoors = generateFlyFlagTexCoor(cols,rows);     
		tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
		tbb.order(ByteOrder.nativeOrder());//設定位元組順序
		texCoorBuffer[0][4] = tbb.asFloatBuffer();//轉為Float型緩沖
		texCoorBuffer[0][4].put(textureCoors);//向緩沖區中放入頂點著色資料
		texCoorBuffer[0][4].position(0);//設定緩沖區起始位置
		
		
		
		//**************************************************************************//
		//  GameView裡面的選單按鈕紋理矩形，
		//  其紋理資料全部是texCoorBuffer[0][0],頂點數量全部是vCount[0][0]
		//	gr1 --- GAMEVIEW_PIC_LS[ 0][2], GAMEVIEW_PIC_LS[ 0][3]
		//  gr2 --- GAMEVIEW_PIC_LS[ 2][2], GAMEVIEW_PIC_LS[ 2][3]
		//  gr3 --- GAMEVIEW_PIC_LS[ 7][2], GAMEVIEW_PIC_LS[ 7][3]
		//  gr4 --- GAMEVIEW_PIC_LS[ 8][2], GAMEVIEW_PIC_LS[ 8][3]
		//  gr5 --- GAMEVIEW_PIC_LS[ 6][2], GAMEVIEW_PIC_LS[ 6][3]
		//  gr6 --- GAMEVIEW_PIC_LS[15][2], GAMEVIEW_PIC_LS[15][3]
		//  gr7 --- GAMEVIEW_PIC_LS[16][2], GAMEVIEW_PIC_LS[16][3]
		//*****************************************************************************//
		//gr1
		vertices = From2DTo3DUtil.Vertices(GAMEVIEW_PIC_LS[0][2],GAMEVIEW_PIC_LS[0][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[1][8] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[1][8].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[1][8].position(0);//設定緩沖區起始位置
		//gr2
		vertices = From2DTo3DUtil.Vertices(GAMEVIEW_PIC_LS[2][2],GAMEVIEW_PIC_LS[2][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[1][9] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[1][9].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[1][9].position(0);//設定緩沖區起始位置
		//gr3
		vertices = From2DTo3DUtil.Vertices(GAMEVIEW_PIC_LS[7][2],GAMEVIEW_PIC_LS[7][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[1][10] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[1][10].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[1][10].position(0);//設定緩沖區起始位置
		//gr4
		vertices = From2DTo3DUtil.Vertices(GAMEVIEW_PIC_LS[8][2],GAMEVIEW_PIC_LS[8][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[1][11] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[1][11].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[1][11].position(0);//設定緩沖區起始位置
		//gr5
		vertices = From2DTo3DUtil.Vertices(GAMEVIEW_PIC_LS[6][2],GAMEVIEW_PIC_LS[6][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[1][12] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[1][12].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[1][12].position(0);//設定緩沖區起始位置
		//gr6
		vertices = From2DTo3DUtil.Vertices(GAMEVIEW_PIC_LS[15][2],GAMEVIEW_PIC_LS[15][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[1][13] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[1][13].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[1][13].position(0);//設定緩沖區起始位置
		//gr7
		vertices = From2DTo3DUtil.Vertices(GAMEVIEW_PIC_LS[16][2],GAMEVIEW_PIC_LS[16][3]);
		vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點資料緩沖
		vbb.order(ByteOrder.nativeOrder());//設定位元組順序
		vertexBuffer[1][14] = vbb.asFloatBuffer();//轉為int型緩沖
		vertexBuffer[1][14].put(vertices);//向緩沖區中放入頂點座標資料
		vertexBuffer[1][14].position(0);//設定緩沖區起始位置

		loadGameBuffer();
	 }
	 private static void loadGameBuffer()
     {
		// RACKETS頂點資料
		float vertexs[] = t(RACKETS_LENGTH, RACKETS_WIDTH);
		vCount[1][0] = vertexs.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[1][0] = vbb.asFloatBuffer();
		vertexBuffer[1][0].put(vertexs);
		vertexBuffer[1][0].position(0);
		// RACKETS紋理資料
		float texCoor[] = new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
				1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoor.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texCoorBuffer[1][0] = tbb.asFloatBuffer();
		texCoorBuffer[1][0].put(texCoor);
		texCoorBuffer[1][0].position(0);
		// RACKETS法向量資料
		float normal[] = new float[] { 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
				0, 1, 0, 0, 1 };
		ByteBuffer nbb = ByteBuffer.allocateDirect(normal.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer[1][0] = nbb.asFloatBuffer();
		normalBuffer[1][0].put(normal);
		normalBuffer[1][0].position(0);

		// NET頂點資料，紋理資料和法向量資料同 gTexCoorBuffer[0]， gNormalBuffer[0]
		vertexs = t(NET_WIDTH, NET_HEIGHT);
		vCount[1][1] = vertexs.length / 3;
		vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[1][1] = vbb.asFloatBuffer();
		vertexBuffer[1][1].put(vertexs);
		vertexBuffer[1][1].position(0);

		// loading 頂點資料，紋理資料和法向量資料同 gTexCoorBuffer[0]， gNormalBuffer[0]
		vertexs = t(2.0f, 2.1f);
		vCount[1][2] = vertexs.length / 3;
		vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[1][2] = vbb.asFloatBuffer();
		vertexBuffer[1][2].put(vertexs);
		vertexBuffer[1][2].position(0);

		// process 頂點資料，紋理資料和法向量資料同 gTexCoorBuffer[0]， gNormalBuffer[0]
		vertexs = t(5, 0.1f);
		vCount[1][3] = vertexs.length / 3;
		vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[1][3] =vbb.asFloatBuffer();
		vertexBuffer[1][3].put(vertexs);
		vertexBuffer[1][3].position(0);

		// 一共8個點 上面4個 編號 按從左至右 從遠到近的順序是 0 1 2 3
		// 下面四個 編號 按從左至右 從遠到近的順序是 4 5 6 7
		float x0 = -TABLE_WIDHT / 2, y0 = TABLE_HEIGHT / 2, z0 = -TABLE_LENGTH / 2;
		float x1 = TABLE_WIDHT / 2, y1 = TABLE_HEIGHT / 2, z1 = -TABLE_LENGTH / 2;
		float x2 = -TABLE_WIDHT / 2, y2 = TABLE_HEIGHT / 2, z2 = TABLE_LENGTH / 2;
		float x3 = TABLE_WIDHT / 2, y3 = TABLE_HEIGHT / 2, z3 = TABLE_LENGTH / 2;

		float x4 = -TABLE_WIDHT / 2, y4 = -TABLE_HEIGHT / 2, z4 = -TABLE_LENGTH / 2;
		float x5 = TABLE_WIDHT / 2, y5 = -TABLE_HEIGHT / 2, z5 = -TABLE_LENGTH / 2;
		float x6 = -TABLE_WIDHT / 2, y6 = -TABLE_HEIGHT / 2, z6 = TABLE_LENGTH / 2;
		float x7 = TABLE_WIDHT / 2, y7 = -TABLE_HEIGHT / 2, z7 = TABLE_LENGTH / 2;

		float[] vertex = new float[] 
	    {
				// 遠面 014 415
			x0, y0, z0, x1, y1, z1, x4, y4, z4,
			x4, y4, z4, x1, y1, z1, x5, y5, z5,
			// 左面 042 246
			x0, y0, z0, x4, y4, z4, x2, y2, z2,
			x2, y2, z2, x4, y4, z4, x6, y6, z6,
			// 上面 021 123
			x0, y0, z0, x2, y2, z2, x1, y1, z1,
			x1, y1, z1, x2, y2, z2, x3, y3, z3,
			// 右面 371 175
			x3, y3, z3, x7, y7, z7, x1, y1, z1,
			x1, y1, z1, x7, y7, z7, x5, y5, z5,
			// 下面 574 476
			x5, y5, z5, x7, y7, z7, x4, y4, z4,
			x4, y4, z4, x7, y7, z7, x6, y6, z6,
			// 近面 263 367
			x2, y2, z2, x6, y6, z6, x3, y3, z3,
			x3, y3, z3, x6, y6, z6, x7, y7, z7 
	    };

		vCount[1][4] = vertex.length / 3;

		vbb = ByteBuffer.allocateDirect(vertex.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[1][4] = vbb.asFloatBuffer();
		vertexBuffer[1][4].put(vertex);
		vertexBuffer[1][4].position(0);

		texCoor = new float[] 
		{
			// 遠
			0.0f, 0.0f, 0.006f, 0.0f, 0.006f, 0.0f, 0.006f,	0.0f,0.006f,0.0f,0.006f,0.006f,
			// 左
			0.0f, 0.0f, 0.006f, 0.0f, 0.006f, 0.0f, 0.006f, 0.0f,0.006f,0.0f,0.006f,0.006f,
			// 上
			0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,1.0f,1.0f,	1.0f,
			// 右
			0.0f, 0.0f, 0.006f, 0.0f, 0.006f, 0.0f, 0.006f, 0.0f, 0.006f,0.0f, 0.006f,0.006f,
			// 下
			0.2f, 0.1f, 0.2f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.2f,	0.1f, 0.2f,
			// 近
			0.0f, 0.0f, 0.006f, 0.0f, 0.006f, 0.0f, 0.006f, 0.0f, 0.006f,0.0f, 0.006f, 0.006f
		};

		tbb = ByteBuffer.allocateDirect(texCoor.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texCoorBuffer[1][1] = tbb.asFloatBuffer();
		texCoorBuffer[1][1].put(texCoor);
		texCoorBuffer[1][1].position(0);

		// 014 415 042 246 021 123 371 175 574 476 263 367
		normal = new float[] 
		{
			// 014 415
			0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
			// 042 246
			-1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
			// 021 123
			0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
			// 371 175
			1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
			// 574 476
			0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
			// 263 367
			0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 
		};
		nbb = ByteBuffer.allocateDirect(normal.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer[1][1] = nbb.asFloatBuffer();
		normalBuffer[1][1].put(normal);
		normalBuffer[1][1].position(0);
		
		////////////////////////////////////////////////////////////
		//Spring(mv, 2.25f, 2.35f, 0.0f, 0.5f, 10, 30);
		float rBig = 2.25f;
		float rSmall = 2.35f;// 螺旋管外徑、螺旋管內徑
		float h = 0.0f;
		float nCirclef = 0.5f; // 螺旋管高度，螺旋管圈數
		int nCol = 10;
		int nRow = 30; // 小圓周和大圓周切分的份數
		float angdegTotal = nCirclef * 360.0f;// 大圓周總度數
		float angdegColSpan = 360.0f / nCol;// 小圓周每份的角度跨度
		float angdegRowSpan = angdegTotal / nRow;// 大圓周每份的角度跨度
		float A = (rBig - rSmall) / 2;// 用於旋轉的小圓半徑
		float D = rSmall + A;// 旋轉軌跡形成的大圓周半徑
		vCount[1][5] = 3 * nCol * nRow * 2;// 頂點個數，共有nColumn*nRow*2個三角形，每個三角形都有三個頂點
		// 座標資料起始化
		ArrayList<Float> alVertix = new ArrayList<Float>();// 原頂點清單（未卷冊繞）
		ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();// 群組織成面的頂點的索引值清單（按逆時針卷冊繞）
		// 頂點
		for (float angdegCol = 0; Math.ceil(angdegCol) < 360 + angdegColSpan; angdegCol += angdegColSpan) {
			double a = Math.toRadians(angdegCol);// 目前小圓周弧度
			for (float angdegRow = 0; Math.ceil(angdegRow) < angdegTotal
					+ angdegRowSpan; angdegRow += angdegRowSpan)// 重復了一列頂點，方便了索引的計算
			{
				float yVec = (angdegRow / angdegTotal) * h;// 根據旋轉角度增加y的值
				double u = Math.toRadians(angdegRow);// 目前大圓周弧度
				float y = (float) (A * Math.cos(a));
				float x = (float) ((D + A * Math.sin(a)) * Math.sin(u));
				float z = (float) ((D + A * Math.sin(a)) * Math.cos(u));
				// 將計算出來的XYZ座標加入存放頂點座標的ArrayList
				alVertix.add(x);
				alVertix.add(y + yVec);
				alVertix.add(z);
			}
		}
		// 索引
		for (int i = 0; i < nCol; i++) {
			for (int j = 0; j < nRow; j++) {
				int index = i * (nRow + 1) + j;// 目前索引
				// 卷冊繞索引
				alFaceIndex.add(index + 1);// 下一列---1
				alFaceIndex.add(index + nRow + 1);// 下一列---2
				alFaceIndex.add(index + nRow + 2);// 下一行下一列---3

				alFaceIndex.add(index + 1);// 下一列---1
				alFaceIndex.add(index);// 目前---0
				alFaceIndex.add(index + nRow + 1);// 下一列---2
			}
		}
		// 計算卷冊繞頂點和平均法向量
		float[] vertices = new float[vCount[1][5] * 3];
		cullVertex(alVertix, alFaceIndex, vertices);

		// 紋理
		ArrayList<Float> alST = new ArrayList<Float>();// 原紋理座標清單（未卷冊繞）
		for (float angdegCol = 0; Math.ceil(angdegCol) < 360 + angdegColSpan; angdegCol += angdegColSpan) {
			float t = angdegCol / 360;// t座標
			for (float angdegRow = 0; Math.ceil(angdegRow) < angdegTotal
					+ angdegRowSpan; angdegRow += angdegRowSpan)// 重復了一列紋理座標，以索引的計算
			{
				float s = angdegRow / angdegTotal;// s座標
				// 將計算出來的ST座標加入存放頂點座標的ArrayList
				alST.add(s);
				alST.add(t);
			}
		}
		// 計算卷冊繞後紋理座標
		float[] textures = cullTexCoor(alST, alFaceIndex);
		vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer[1][5] = vbb.asFloatBuffer();
		vertexBuffer[1][5].put(vertices);
		vertexBuffer[1][5].position(0);

		tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texCoorBuffer[1][2] = tbb.asFloatBuffer();
		texCoorBuffer[1][2].put(textures);
		texCoorBuffer[1][2].position(0);
			
	    ////***********************************
		// floor
		float width = ROOM_WIDTH;
		float height = ROOM_WIDTH;
		vertices = new float[] 
		{ 
			-width / 2, 0, -height / 2, -width / 2, 0,	height / 2, width / 2, 0, -height / 2,
			width / 2, 0, -height / 2, -width / 2, 0, height / 2,width / 2, 0, height / 2 
		};
		vCount[1][6] = vertices.length / 3;
		float[] texture = new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,	1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
		float[] normals = new float[] { 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, };
					
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer[1][6] = byteBuf.asFloatBuffer();
		vertexBuffer[1][6].put(vertices);
		vertexBuffer[1][6].position(0);
		
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		texCoorBuffer[1][3] = byteBuf.asFloatBuffer();
		texCoorBuffer[1][3].put(texture);
		texCoorBuffer[1][3].position(0);
		
		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer[1][2] = byteBuf.asFloatBuffer();
		normalBuffer[1][2].put(normals);
		normalBuffer[1][2].position(0);
		// -------------------------------------------
		// wall 8, 4
		width = 8;
		height = 4;
		vertices = new float[] 
	    {
			-width / 2, 0, -height / 2, -width / 2, 0,	height / 2, width / 2, 0, 
			-height / 2, width / 2, 0, -height / 2, -width / 2, 0, height / 2,
			width / 2, 0, height / 2 
		};
		vCount[1][7] = vertices.length / 3;
		byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer[1][7] = byteBuf.asFloatBuffer();
		vertexBuffer[1][7].put(vertices);
		vertexBuffer[1][7].position(0);
	}
	
	private static float[] t(float length, float width)
	{
		//x-o-y平面
		float vertexs[] = new float[] 
		{
			// 013  312
			-length/2, width/2,0,
			-length/2,-width/2,0,
			 length/2, width/2,0,

			 length/2,width/2,0,
			-length/2,-width/2,0,
			 length/2,-width/2,0,
		};
		return vertexs;
	}
	// 透過原頂點和面的索引值，得到用頂點卷冊繞的陣列
	private static void cullVertex(ArrayList<Float> alv,// 原頂點清單（未卷冊繞）
			ArrayList<Integer> alFaceIndex,// 群組織成面的頂點的索引值清單（按逆時針卷冊繞）
			float[] vertices// 用頂點卷冊繞的陣列（頂點結果放入該陣列中，陣列長度應等於索引清單長度的3倍）
	) {
		// 產生頂點的陣列
		int vCount = 0;
		for (int i : alFaceIndex) {
			vertices[vCount++] = alv.get(3 * i);
			vertices[vCount++] = alv.get(3 * i + 1);
			vertices[vCount++] = alv.get(3 * i + 2);
		}
	}

	// 根據原紋理座標和索引，計算卷冊繞後的紋理的方法
	private static float[] cullTexCoor(ArrayList<Float> alST,// 原紋理座標清單（未卷冊繞）
			ArrayList<Integer> alTexIndex// 群組織成面的紋理座標的索引值清單（按逆時針卷冊繞）
	) {
		float[] textures = new float[alTexIndex.size() * 2];
		// 產生頂點的陣列
		int stCount = 0;
		for (int i : alTexIndex) {
			textures[stCount++] = alST.get(2 * i);
			textures[stCount++] = alST.get(2 * i + 1);
		}
		return textures;
	}
	
	//自動切分紋理產生紋理陣列的方法
    private static float[] generateFlyFlagTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//列數
    	float sizeh=1.0f/bh;//行數
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//每行列一個矩形，由兩個三角形構成，共六個點，12個紋理座標
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
	private static float[] generateBallTexCoor(int angleSpan) 
	{
		final int bh =180/angleSpan;
		final int bw =360/angleSpan;
		final float sizew=1.0f/bw;//列數
		final float sizeh=1.0f/bh;//行數
		final List<Float> list=new ArrayList<Float>(); 
    	a:for(int i=0;i<=bh;i++)
    	{
    		for(int j=0;j<=bw;j++)
    		{
    			//1
    			float s=j*sizew;
    			float t=i*sizeh;
    			list.add(s);
    			list.add(t);
    			//2
    			if(i+1>bh)
    			{
    				break a;
    			}
    			float ct=(i+1)*sizeh;
    			float cs=(j)*sizew;
    			list.add(cs);
    			list.add(ct);
    		}
    	}
    	float result[] = new float[list.size()];
    	for(int i=0;i<list.size();i++)
    	{
    		result[i] = list.get(i);
    	}
    	return result;
	}
	
	/**攝影機搬移距離*/
	public static Vector2f CAMERAMOVE = new Vector2f();
	/** 空間加速度A*/
	public  static final Vector3f A = new Vector3f(0.0f,-0.8f,0.0f);
	/** 主界面球空間加速度MA*/
	public  static final Vector3f MA = new Vector3f(0.0f,-0.2f,0.0f);
	
	/** 球碰撞反彈後能量損失系數 */
	public static final float BALL_ENERGY_LOST = 0.866f;//0.766f;
	
	public static final float BALL_ENERGY_LOST_SHOOT = 0.999f;//
	/** 球台面位置  高度y座標  */
	public static final float TABLE_Y     =  TABLE_HEIGHT;

	/** 球台面位置  x最大值 */
	public static final float TABLE_X_MAX =  TABLE_WIDHT / 2.0f;
	
	/** 球台面位置  x最小值 */
	public static final float TABLE_X_MIN =  -TABLE_WIDHT / 2.0f;
	
	/** 球台面位置  z最大值 */
	public static final float TABLE_Z_MAX =  TABLE_LENGTH / 2.0f;
	
	/** 球台面位置  z最小值 */
	public static final float TABLE_Z_MIN =  -TABLE_LENGTH / 2.0f;
	
	/** 網 高度的 y值*/
	public static final float NET_Y = NET_HEIGHT + TABLE_HEIGHT / 2.0f - 0.1f;
	
	/** 網 的寬度 z正方向*/
	public static final float NET_Z_MAX =  0.025f;
	
	/** 網 的寬度 z負方向*/
	public static final float NET_Z_MIN = -0.025f;
	
	/**TABLE_Z_MIN 這個值 必須與 MySurfaceView中racketsAI傳進去的z值一樣*/
	public static final float AI_Z = -(TABLE_Z_MAX+1f);
}

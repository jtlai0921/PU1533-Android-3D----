package com.f.view;


import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.f.dynamic.MainBallForContorl;
import com.f.gamebody.GameFlyFlag;
import com.f.mainbody.MainBat;
import com.f.mainbody.MainFloor;
import com.f.mainbody.MainBall;
import com.f.mainbody.MainMenuRect;
import com.f.mainbody.MainTable;
import com.f.pingpong.MainActivity;
import com.f.pingpong.Constant.DifficultyContorl;
import com.f.util.From2DTo3DUtil;
import com.f.util.MatrixState;
import com.f.video.FrameData;
import com.f.video.VideoUtil;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.Constant.DifficultyContorl.DIFFICULTY;
import static com.f.pingpong.ShaderManager.*;

public class MainMenuSurfaceView extends GLSurfaceView
{
	private MainActivity ma;
	private Context context;
	private SceneRenderer mRenderer;//場景著色器
    private int texTableId;//系統分配的紋理id
    private int texLightId;
    private int texShadowId;
    private int texBatId;
    private int texMainBallId;
    private MainBallForContorl mbfc;
    private GameFlyFlag countryFlyFlag = null;
	private int selectingCountry = -1;
	//載入中
	private boolean LOAD_OVER = false;
	private int load_step = 0;//載入資源的步數
	private int textureid_loading = 0;
	
    public static int state = 0;
    public static boolean soundflag = true;
	public static boolean vibrateflag = true;
	
	public MainMenuSurfaceView(Context context)
	{
		super(context);
		this.context = context;
		this.ma = (MainActivity)context;
		setEGLContextClientVersion(2); 
		mRenderer = new SceneRenderer();//建立場景著色器
		setRenderer(mRenderer);				  
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		private float yAngle;//繞Y軸旋轉的角度
		private MainTable table;
		private MainFloor floor;
		private MainBat bat;
		private MainBall mb;
		private MainMenuRect mr1;
		private MainMenuRect mr2;
		private MainMenuRect mr3;
		private MainMenuRect mr4;
		private MainMenuRect mr5;
		private MainMenuRect mr6;
		private MainMenuRect mr7;
		
		private boolean isFirstFrame = true;
		private Resources res = MainMenuSurfaceView.this.getResources();
		
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			//清除深度緩沖與彩色緩沖
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            if(IS_FIRSTLOADING_MAIN)
            {
            	if(!LOAD_OVER)
    			{
    				drawOrthLoadingView();
    			}
    			else
    			{
    				drawPerspective();
    				drawMenu();
    			}
            }
            else
            {
            	drawPerspective();
				drawMenu();
            }
			
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) 
		{
			//設定視窗大小及位置 
			GLES20.glViewport(ssr.lucX,ssr.lucY, (int)(SCREEN_WIDTH_STANDARD*ssr.ratio), (int)(SCREEN_HEIGHT_STANDARD*ssr.ratio));
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) 
		{
			//設定螢幕背景色RGBA
            GLES20.glClearColor(0f,0f,0f,1.0f);    
            //開啟深度檢驗
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //開啟背面剪裁   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //起始化變換矩陣
            MatrixState.setInitStack();
            //起始化光源位置
            MatrixState.setLightLocation(0, 8, 0);
            
            if(!IS_FIRSTLOADING_MAIN)
            {
            	loadMainTextureId();
	            loadLockFlagTextureId();
	            compileMainShaderReal();
            	
            	texBatId = OBJ_TEXTURE_ID[0];
	            texTableId = OBJ_TEXTURE_ID[1];
	            texLightId = (Math.random() < 0.6 ?OBJ_TEXTURE_ID[2] : OBJ_TEXTURE_ID[3]);
	            texShadowId = OBJ_TEXTURE_ID[4];
	            texMainBallId = OBJ_TEXTURE_ID[5];
	            
	            table=new MainTable();
				floor=new MainFloor();
	            bat=new MainBat();
	            mb=new MainBall();
	            countryFlyFlag = new GameFlyFlag(0);
	            mr1=new MainMenuRect(vertexBuffer[0][0],texCoorBuffer[0][0]);
	            mr2=new MainMenuRect(vertexBuffer[0][1],texCoorBuffer[0][0]);
	            mr3=new MainMenuRect(vertexBuffer[0][2],texCoorBuffer[0][0]);
	            mr4=new MainMenuRect(vertexBuffer[0][3],texCoorBuffer[0][0]);
	            mr5=new MainMenuRect(vertexBuffer[0][4],texCoorBuffer[0][0]);
	            mr6=new MainMenuRect(vertexBuffer[0][5],texCoorBuffer[0][0]);
	            
	            mbfc=new MainBallForContorl(mb);   
	            mbfc.start();
	            state = 1;
	            for(int i = 0; i < FrameData.playFrameDataList.size();i++)
	    		{
	        		FrameData.playFrameDataList.get(i).isTaskDone=false;   
	    		}
	            for(int i = 0; i < FrameData.helpFrameDataList.size();i++)
	    		{
	        		FrameData.helpFrameDataList.get(i).isTaskDone=false;    
	    		}
				ma.soundandshakeutil.palyBgSound();
            }
            else
            {
            	textureid_loading = initTexture(res,"mainloading.png");
                mr7=new MainMenuRect(res,400,200);
            }
            
            DifficultyContorl.DIFFICULTY = -1;
    		WHICH_COUNTRY = -1;
		}
		
		private void drawOrthLoadingView() 
		{
			if (isFirstFrame) // 若果是第一框
			{ 
				MatrixState.pushMatrix();
				MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);// 設定正交投影
				MatrixState.setCamera(0, 0, 1, 0, 0, -1, 0, 1, 0);// 設定攝影機
				mr7.drawSelf(From2DTo3DUtil.point3D(new float[]{400, 240}), textureid_loading);
				MatrixState.popMatrix();
				isFirstFrame = false;
			}
			else
			{
				MatrixState.pushMatrix();
				MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);// 設定正交投影
				MatrixState.setCamera(0, 0, 1, 0, 0, -1, 0, 1, 0);// 設定攝影機
				mr7.drawSelf(From2DTo3DUtil.point3D(new float[]{400, 240}), textureid_loading);
				MatrixState.popMatrix();
				loadResource();// 載入資源的方法
				return;
			}
		}
		
		private void loadResource() 
		{
			switch (load_step) 
			{
			case 0:
				init_All_Texture(load_step);
				load_step++;
				break;
			case 1:
				init_All_Texture(load_step);
				load_step++;
				break;
			case 2:
				init_All_Texture(load_step);
				load_step++;
				break;
			case 3:
				init_All_Object(load_step);
				load_step++;
				break;
			case 4:
				init_All_Object(load_step);
				load_step++;
				break;
			case 5:
				init_All_Object(load_step);
				LOAD_OVER = true;
				mbfc=new MainBallForContorl(mb); 
	            mbfc.start();
	            state = 1;
	            for(int i = 0; i < FrameData.playFrameDataList.size();i++)
	    		{
	        		FrameData.playFrameDataList.get(i).isTaskDone=false;   
	    		}
	            for(int i = 0; i < FrameData.helpFrameDataList.size();i++)
	    		{
	        		FrameData.helpFrameDataList.get(i).isTaskDone=false;   
	    		}
	            FrameData.helpFrameDataList = VideoUtil.getFrameData(getResources(), "help.pp");
				ma.soundandshakeutil.palyBgSound();
				IS_FIRSTLOADING_MAIN = false;
				break;
			}
		}
		
		private void init_All_Texture(int step) 
		{
			switch (step) 
			{
			case 0:
				//載入紋理
	            loadMainPicByByte(res);
				break;
			case 1:
				//載入紋理ID
	            loadMainTextureId();
	            texBatId = OBJ_TEXTURE_ID[0];
	            texTableId = OBJ_TEXTURE_ID[1];
	            texLightId = (Math.random() < 0.6 ?OBJ_TEXTURE_ID[2] : OBJ_TEXTURE_ID[3]);
	            texShadowId = OBJ_TEXTURE_ID[4];
	            texMainBallId = OBJ_TEXTURE_ID[5];
				break;
			case 2:
				loadLockFlagTextureId();
				loadShaderString(res);
				compileMainShaderReal();
				break;
			}
		}
		
		private void init_All_Object(int step) 
		{
			switch (step) 
			{
			case 3:
				loadMainBuffer(context);
				break;
			case 4:
	            table=new MainTable();
				floor=new MainFloor();
	            bat=new MainBat();
				break;
			case 5:
				mb=new MainBall();
	            countryFlyFlag = new GameFlyFlag(0);//飄揚的旗幟
	            mr1=new MainMenuRect(vertexBuffer[0][0],texCoorBuffer[0][0]);
	            mr2=new MainMenuRect(vertexBuffer[0][1],texCoorBuffer[0][0]);
	            mr3=new MainMenuRect(vertexBuffer[0][2],texCoorBuffer[0][0]);
	            mr4=new MainMenuRect(vertexBuffer[0][3],texCoorBuffer[0][0]);
	            mr5=new MainMenuRect(vertexBuffer[0][4],texCoorBuffer[0][0]);
	            mr6=new MainMenuRect(vertexBuffer[0][5],texCoorBuffer[0][0]);
				break;
			} 
		}
		
		private void drawPerspective()
		{ 
        	//計算GLSurfaceView的長寬比
            float ratio = RATIO;
            //呼叫此方法計算產生透視投影矩陣
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 300);
            
            MatrixState.pushMatrix();
            
            //呼叫此方法產生攝影機9參數位置矩陣
            MatrixState.setCamera(0f,3f,5.5f,0f,2f,0f,0f,1.0f,0.0f);
			
            //繪制地面
            MatrixState.pushMatrix();
            MatrixState.translate(0f, 0f, 0f);
            MatrixState.rotate(yAngle, 0, 1, 0);
            floor.drawSelf(texLightId, texShadowId);
            MatrixState.popMatrix();
            
            //繪制球案
            MatrixState.pushMatrix();
            MatrixState.translate(0f, 0f, 0f);
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.scale(0.4f, 0.4f, 0.4f);
            table.drawSelf(texTableId,0);
            //開啟混合
            GLES20.glEnable(GLES20.GL_BLEND);
            //設定混合參數
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            table.drawSelf(texTableId,1);
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
            
            //繪制球拍1
            MatrixState.pushMatrix();
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.translate(0f, 2.0f, 2.0f);
            MatrixState.scale(0.03f, 0.03f, 0.03f);
            bat.drawSelf(texBatId);
            MatrixState.popMatrix();
            
            //繪制球拍2
            MatrixState.pushMatrix();
            MatrixState.rotate(180, 0, 1, 0);
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.translate(0f, 2.0f, 2.0f);
            MatrixState.scale(0.03f, 0.03f, 0.03f);
            bat.drawSelf(texBatId);
            MatrixState.popMatrix();
            
            //繪制球
            MatrixState.pushMatrix();
            MatrixState.rotate(yAngle, 0, 1, 0);
            mb.drawSelf(texMainBallId,0);
            //開啟混合
            GLES20.glEnable(GLES20.GL_BLEND);
            //設定混合參數
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            mb.drawSelf(texMainBallId,1);
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
            
            yAngle+=0.2;//自動轉動
            
            MatrixState.popMatrix();
		}
		
		private void drawMenu()
		{
			//計算GLSurfaceView的長寬比
			float ratio = RATIO;
			//設定目前矩陣為投影矩陣
			MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
			
			MatrixState.pushMatrix();
			
			//呼叫此方法產生攝影機9參數位置矩陣
			MatrixState.setCamera(0, 0, 0, 0, 0, -1, 0, 1, 0);
			
            //開啟混合
            GLES20.glEnable(GLES20.GL_BLEND);
            //設定混合參數
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	        
            mr1.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[0][0], MAINMENU_PIC_LS[0][1]}), MAINVIEW_TEXTURE_ID[0]);
            
			switch(state)
			{
			case 1:
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[1][0], MAINMENU_PIC_LS[1][1]}), MAINVIEW_TEXTURE_ID[1]);
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[2][0], MAINMENU_PIC_LS[2][1]}), MAINVIEW_TEXTURE_ID[2]);
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[3][0], MAINMENU_PIC_LS[3][1]}), MAINVIEW_TEXTURE_ID[3]);
				break;
			case 2:
				selectingCountry = WHICH_COUNTRY;
				if(selectingCountry == 0)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[4][0], MAINMENU_PIC_LS[4][1]}), MAINVIEW_TEXTURE_ID[4]);
				}
				else
				{
					mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[4][0], MAINMENU_PIC_LS[4][1]}), MAINVIEW_TEXTURE_ID[4]);
				}
				if(selectingCountry == 1)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[5][0], MAINMENU_PIC_LS[5][1]}), MAINVIEW_TEXTURE_ID[5]);
				}
				else
				{
					mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[5][0], MAINMENU_PIC_LS[5][1]}), MAINVIEW_TEXTURE_ID[5]);
				}
				if(selectingCountry == 2)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[6][0], MAINMENU_PIC_LS[6][1]}), MAINVIEW_TEXTURE_ID[6]);
				}
				else
				{
					mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[6][0], MAINMENU_PIC_LS[6][1]}), MAINVIEW_TEXTURE_ID[6]);
				}
				if(selectingCountry == 3)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[7][0], MAINMENU_PIC_LS[7][1]}), MAINVIEW_TEXTURE_ID[7]);
				}
				else
				{
					mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[7][0], MAINMENU_PIC_LS[7][1]}), MAINVIEW_TEXTURE_ID[7]);
				}
				if(selectingCountry == 4)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[8][0], MAINMENU_PIC_LS[8][1]}), MAINVIEW_TEXTURE_ID[8]);
				}
				else
				{
					mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[8][0], MAINMENU_PIC_LS[8][1]}), MAINVIEW_TEXTURE_ID[8]);
				}
				if(selectingCountry == 5)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[9][0], MAINMENU_PIC_LS[9][1]}), MAINVIEW_TEXTURE_ID[9]);
				}
				else
				{
					mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[9][0], MAINMENU_PIC_LS[9][1]}), MAINVIEW_TEXTURE_ID[9]);
				}
				mr4.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[17][0], MAINMENU_PIC_LS[17][1]}), MAINVIEW_TEXTURE_ID[17]);
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[11][0], MAINMENU_PIC_LS[11][1]}), MAINVIEW_TEXTURE_ID[11]);
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[16][0], MAINMENU_PIC_LS[16][1]}), MAINVIEW_TEXTURE_ID[16]);
				break;
			case 3:
				mr5.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[12][0], MAINMENU_PIC_LS[12][1]}), MAINVIEW_TEXTURE_ID[12]);
				mr6.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[13][0], MAINMENU_PIC_LS[13][1]}), MAINVIEW_TEXTURE_ID[soundflag?14:15]);
				mr5.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[14][0], MAINMENU_PIC_LS[14][1]}), MAINVIEW_TEXTURE_ID[13]);
				mr6.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[15][0], MAINMENU_PIC_LS[15][1]}), MAINVIEW_TEXTURE_ID[vibrateflag?14:15]);
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[11][0], MAINMENU_PIC_LS[11][1]}), MAINVIEW_TEXTURE_ID[11]);
				break;
			case 4:
				selectingCountry = DIFFICULTY;
				Map<String,Boolean>  passCountry = parsePassCountry(PASS_COUNTRY);
		
				if(selectingCountry == 0)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[4][0], MAINMENU_PIC_LS[4][1]}), MAINVIEW_TEXTURE_ID[4]);
				}
				else
				{
					mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[4][0], MAINMENU_PIC_LS[4][1]}), MAINVIEW_TEXTURE_ID[4]);
				}
				if(selectingCountry == 1)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[5][0], MAINMENU_PIC_LS[5][1]}), MAINVIEW_TEXTURE_ID[5]);
				}
				else
				{
					if(passCountry.get("country1")){
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[5][0], MAINMENU_PIC_LS[5][1]}), MAINVIEW_TEXTURE_ID[5]);
					}else{
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[5][0], MAINMENU_PIC_LS[5][1]}), LOCK_FlAG_TEXTURE_ID[0]);
					}
				}
				if(selectingCountry == 2)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[6][0], MAINMENU_PIC_LS[6][1]}), MAINVIEW_TEXTURE_ID[6]);
				}
				else
				{
					if(passCountry.get("country2")){
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[6][0], MAINMENU_PIC_LS[6][1]}), MAINVIEW_TEXTURE_ID[6]);
					}else{
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[6][0], MAINMENU_PIC_LS[6][1]}), LOCK_FlAG_TEXTURE_ID[1]);
					}
				}
				if(selectingCountry == 3)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[7][0], MAINMENU_PIC_LS[7][1]}), MAINVIEW_TEXTURE_ID[7]);
				}
				else
				{
					if(passCountry.get("country3")){
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[7][0], MAINMENU_PIC_LS[7][1]}), MAINVIEW_TEXTURE_ID[7]);
					}else{
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[7][0], MAINMENU_PIC_LS[7][1]}), LOCK_FlAG_TEXTURE_ID[2]);
					}
				}
				if(selectingCountry == 4)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[8][0], MAINMENU_PIC_LS[8][1]}), MAINVIEW_TEXTURE_ID[8]);
				}
				else
				{
					if(passCountry.get("country4")){
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[8][0], MAINMENU_PIC_LS[8][1]}), MAINVIEW_TEXTURE_ID[8]);
					}else{
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[8][0], MAINMENU_PIC_LS[8][1]}), LOCK_FlAG_TEXTURE_ID[3]);
					}
				}
				if(selectingCountry == 5)
				{
					drawFlyFlag(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[9][0], MAINMENU_PIC_LS[9][1]}), MAINVIEW_TEXTURE_ID[9]);
					
				}
				else
				{
					if(passCountry.get("country5")){
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[9][0], MAINMENU_PIC_LS[9][1]}), MAINVIEW_TEXTURE_ID[9]);
					}else{
						mr3.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[9][0], MAINMENU_PIC_LS[9][1]}), LOCK_FlAG_TEXTURE_ID[4]);
					}
				}
				passCountry = null;
				mr4.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[10][0], MAINMENU_PIC_LS[10][1]}), MAINVIEW_TEXTURE_ID[10]);
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[11][0], MAINMENU_PIC_LS[11][1]}), MAINVIEW_TEXTURE_ID[11]);
				mr2.drawSelf(From2DTo3DUtil.point3D(new float[]{MAINMENU_PIC_LS[16][0], MAINMENU_PIC_LS[16][1]}), MAINVIEW_TEXTURE_ID[16]);
				break;
			}
			GLES20.glDisable(GLES20.GL_BLEND);
			
			MatrixState.popMatrix();
		}
		
		private void drawFlyFlag(float []f,int texid)
		{
			if(selectingCountry == -1) return;
			
			MatrixState.pushMatrix();
            MatrixState.rotate(20, 1, 0, 0);
			MatrixState.scale(0.17f, 0.17f, 0.17f);
            MatrixState.translate(f[0], f[1], -10f);
            switch(selectingCountry){
	            case 0:MatrixState.translate(-3.5f, -3.0f, 0.0f);break;
	            case 1:MatrixState.translate( 0.0f, -3.0f, 0.0f);break;
	            case 2:MatrixState.translate( 3.5f, -3.0f, 0.0f);break;
	            case 3:MatrixState.translate(-3.5f, -5.2f, 0.0f);break;
	            case 4:MatrixState.translate( 0.0f, -5.2f, 0.0f);break;
	            case 5:MatrixState.translate( 3.5f, -5.2f, 0.0f);break;
            }
            countryFlyFlag.drawSelf(texid);
            MatrixState.popMatrix();
            
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		int currentNum=e.getAction();
		float x=e.getX();
		float y=e.getY();
		switch(currentNum)
		{
		case MotionEvent.ACTION_DOWN:
			switch(state)
			{
			
			case 1:
				//按下設定按鈕
				if(x > MAINVIEW_TOUCH_AREA[1][0] && x < MAINVIEW_TOUCH_AREA[1][0] + MAINVIEW_TOUCH_AREA[1][2]
				&& y > MAINVIEW_TOUCH_AREA[1][1] && y < MAINVIEW_TOUCH_AREA[1][1] + MAINVIEW_TOUCH_AREA[1][3])
				{
					System.out.println("按下的是設定！");
					state = 3;
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下開始按鈕
				else if(x > MAINVIEW_TOUCH_AREA[2][0] && x < MAINVIEW_TOUCH_AREA[2][0] + MAINVIEW_TOUCH_AREA[2][2]
				     && y > MAINVIEW_TOUCH_AREA[2][1] && y < MAINVIEW_TOUCH_AREA[2][1] + MAINVIEW_TOUCH_AREA[2][3])
				{
					System.out.println("按下的是開始！");
					state = 2;
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下幫助按鈕
				else if(x > MAINVIEW_TOUCH_AREA[3][0] && x < MAINVIEW_TOUCH_AREA[3][0] + MAINVIEW_TOUCH_AREA[3][2]
				     && y > MAINVIEW_TOUCH_AREA[3][1] && y < MAINVIEW_TOUCH_AREA[3][1] + MAINVIEW_TOUCH_AREA[3][3])
   				{
   					System.out.println("按下的是幫助！");
   					IS_HELP = true;
   					ma.soundandshakeutil.playSoundL(5, 0);
   					ma.hd.sendEmptyMessage(2);
   					return true;
   				}
				break;
			case 2:
				//按下國家1
				if(x > MAINVIEW_TOUCH_AREA[4][0] && x < MAINVIEW_TOUCH_AREA[4][0] + MAINVIEW_TOUCH_AREA[4][2]
				&& y > MAINVIEW_TOUCH_AREA[4][1] && y < MAINVIEW_TOUCH_AREA[4][1] + MAINVIEW_TOUCH_AREA[4][3])
				{
					System.out.println("按下的是國家1！");
					WHICH_COUNTRY = 0;
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下國家2
				else if(x > MAINVIEW_TOUCH_AREA[5][0] && x < MAINVIEW_TOUCH_AREA[5][0] + MAINVIEW_TOUCH_AREA[5][2]
				     && y > MAINVIEW_TOUCH_AREA[5][1] && y < MAINVIEW_TOUCH_AREA[5][1] + MAINVIEW_TOUCH_AREA[5][3])
				{
					System.out.println("按下的是國家2！");
					WHICH_COUNTRY = 1;
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下國家3
				else if(x > MAINVIEW_TOUCH_AREA[6][0] && x < MAINVIEW_TOUCH_AREA[6][0] + MAINVIEW_TOUCH_AREA[6][2]
				     && y > MAINVIEW_TOUCH_AREA[6][1] && y < MAINVIEW_TOUCH_AREA[6][1] + MAINVIEW_TOUCH_AREA[6][3])
   				{
   					System.out.println("按下的是國家3！");
   					WHICH_COUNTRY = 2;
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				//按下國家4
				else if(x > MAINVIEW_TOUCH_AREA[7][0] && x < MAINVIEW_TOUCH_AREA[7][0] + MAINVIEW_TOUCH_AREA[7][2]
				     && y > MAINVIEW_TOUCH_AREA[7][1] && y < MAINVIEW_TOUCH_AREA[7][1] + MAINVIEW_TOUCH_AREA[7][3])
				{
					System.out.println("按下的是國家4！");
					WHICH_COUNTRY = 3;
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下國家5
				else if(x > MAINVIEW_TOUCH_AREA[8][0] && x < MAINVIEW_TOUCH_AREA[8][0] + MAINVIEW_TOUCH_AREA[8][2]
				     && y > MAINVIEW_TOUCH_AREA[8][1] && y < MAINVIEW_TOUCH_AREA[8][1] + MAINVIEW_TOUCH_AREA[8][3])
   				{
   					System.out.println("按下的是國家5！");
   					WHICH_COUNTRY = 4;
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				//按下國家6
				else if(x > MAINVIEW_TOUCH_AREA[9][0] && x < MAINVIEW_TOUCH_AREA[9][0] + MAINVIEW_TOUCH_AREA[9][2]
				     && y > MAINVIEW_TOUCH_AREA[9][1] && y < MAINVIEW_TOUCH_AREA[9][1] + MAINVIEW_TOUCH_AREA[9][3])
   				{
   					System.out.println("按下的是國家6！");
   					WHICH_COUNTRY = 5;
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				//按下傳回按鈕
				else if(x > MAINVIEW_TOUCH_AREA[11][0] && x < MAINVIEW_TOUCH_AREA[11][0] + MAINVIEW_TOUCH_AREA[11][2]
				     && y > MAINVIEW_TOUCH_AREA[11][1] && y < MAINVIEW_TOUCH_AREA[11][1] + MAINVIEW_TOUCH_AREA[11][3])
   				{
   					System.out.println("按下的是傳回！");
   					state = 1;
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				//按下前進按鈕
				else if(x > MAINVIEW_TOUCH_AREA[16][0] && x < MAINVIEW_TOUCH_AREA[16][0] + MAINVIEW_TOUCH_AREA[16][2]
				     && y > MAINVIEW_TOUCH_AREA[16][1] && y < MAINVIEW_TOUCH_AREA[16][1] + MAINVIEW_TOUCH_AREA[16][3])
   				{
   					System.out.println("按下的是前進！");
   					if(WHICH_COUNTRY != -1)
   					{
   						state = 4;
   					}
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				break;
			case 3:
				//按下音效開關
				if(x > MAINVIEW_TOUCH_AREA[13][0] && x < MAINVIEW_TOUCH_AREA[13][0] + MAINVIEW_TOUCH_AREA[13][2]
				&& y > MAINVIEW_TOUCH_AREA[13][1] && y < MAINVIEW_TOUCH_AREA[13][1] + MAINVIEW_TOUCH_AREA[13][3])
				{
					System.out.println("按下的是音效開關！");
					soundflag = !soundflag;
					if(soundflag)
					{
						ma.soundandshakeutil.palyBgSound();
					}
					else
					{
						ma.soundandshakeutil.stopBgSound();
					}
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下震動開關
				else if(x > MAINVIEW_TOUCH_AREA[15][0] && x < MAINVIEW_TOUCH_AREA[15][0] + MAINVIEW_TOUCH_AREA[15][2]
				     && y > MAINVIEW_TOUCH_AREA[15][1] && y < MAINVIEW_TOUCH_AREA[15][1] + MAINVIEW_TOUCH_AREA[15][3])
				{
					System.out.println("按下的是震動開關！");
					vibrateflag = !vibrateflag;
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下傳回按鈕
				else if(x > MAINVIEW_TOUCH_AREA[11][0] && x < MAINVIEW_TOUCH_AREA[11][0] + MAINVIEW_TOUCH_AREA[11][2]
				     && y > MAINVIEW_TOUCH_AREA[11][1] && y < MAINVIEW_TOUCH_AREA[11][1] + MAINVIEW_TOUCH_AREA[11][3])
   				{
   					System.out.println("按下的是傳回！");
   					state = 1;
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				break;
			case 4:
				Map<String,Boolean>  passCountry = parsePassCountry(PASS_COUNTRY);
				
				//按下國家1
				if(x > MAINVIEW_TOUCH_AREA[4][0] && x < MAINVIEW_TOUCH_AREA[4][0] + MAINVIEW_TOUCH_AREA[4][2]
				&& y > MAINVIEW_TOUCH_AREA[4][1] && y < MAINVIEW_TOUCH_AREA[4][1] + MAINVIEW_TOUCH_AREA[4][3])
				{
					System.out.println("按下的是國家1！");
					DIFFICULTY = 0;
					ma.soundandshakeutil.playSoundL(5, 0);
					return true;
				}
				//按下國家2
				else if(x > MAINVIEW_TOUCH_AREA[5][0] && x < MAINVIEW_TOUCH_AREA[5][0] + MAINVIEW_TOUCH_AREA[5][2]
				     && y > MAINVIEW_TOUCH_AREA[5][1] && y < MAINVIEW_TOUCH_AREA[5][1] + MAINVIEW_TOUCH_AREA[5][3])
				{
					if(passCountry.get("country1"))
					{
						System.out.println("按下的是國家2！");
						DIFFICULTY = 1;
						ma.soundandshakeutil.playSoundL(5, 0);
						return true;
					}
				}
				//按下國家3
				else if(x > MAINVIEW_TOUCH_AREA[6][0] && x < MAINVIEW_TOUCH_AREA[6][0] + MAINVIEW_TOUCH_AREA[6][2]
				     && y > MAINVIEW_TOUCH_AREA[6][1] && y < MAINVIEW_TOUCH_AREA[6][1] + MAINVIEW_TOUCH_AREA[6][3])
   				{
					if(passCountry.get("country2"))
					{
	   					System.out.println("按下的是國家3！");
	   					DIFFICULTY = 2;
	   					ma.soundandshakeutil.playSoundL(5, 0);
	   					return true;
					}
   				}
				//按下國家4
				else if(x > MAINVIEW_TOUCH_AREA[7][0] && x < MAINVIEW_TOUCH_AREA[7][0] + MAINVIEW_TOUCH_AREA[7][2]
				     && y > MAINVIEW_TOUCH_AREA[7][1] && y < MAINVIEW_TOUCH_AREA[7][1] + MAINVIEW_TOUCH_AREA[7][3])
				{
					if(passCountry.get("country3"))
					{
						System.out.println("按下的是國家4！");
						DIFFICULTY = 3;
						ma.soundandshakeutil.playSoundL(5, 0);
						return true;
					}
				}
				//按下國家5
				else if(x > MAINVIEW_TOUCH_AREA[8][0] && x < MAINVIEW_TOUCH_AREA[8][0] + MAINVIEW_TOUCH_AREA[8][2]
				     && y > MAINVIEW_TOUCH_AREA[8][1] && y < MAINVIEW_TOUCH_AREA[8][1] + MAINVIEW_TOUCH_AREA[8][3])
   				{
					if(passCountry.get("country4"))
					{
	   					System.out.println("按下的是國家5！");
	   					DIFFICULTY = 4;
	   					ma.soundandshakeutil.playSoundL(5, 0);
	   					return true;
					}
   				}
				//按下國家6
				else if(x > MAINVIEW_TOUCH_AREA[9][0] && x < MAINVIEW_TOUCH_AREA[9][0] + MAINVIEW_TOUCH_AREA[9][2]
				     && y > MAINVIEW_TOUCH_AREA[9][1] && y < MAINVIEW_TOUCH_AREA[9][1] + MAINVIEW_TOUCH_AREA[9][3])
   				{
					if(passCountry.get("country5"))
					{
						System.out.println("按下的是國家6！");
						DIFFICULTY = 5;
						ma.soundandshakeutil.playSoundL(5, 0);
						return true;
					}
   				}
				//按下傳回按鈕
				else if(x > MAINVIEW_TOUCH_AREA[11][0] && x < MAINVIEW_TOUCH_AREA[11][0] + MAINVIEW_TOUCH_AREA[11][2]
				     && y > MAINVIEW_TOUCH_AREA[11][1] && y < MAINVIEW_TOUCH_AREA[11][1] + MAINVIEW_TOUCH_AREA[11][3])
   				{
   					System.out.println("按下的是傳回！");
   					state = 2;
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				//按下前進按鈕
				else if(x > MAINVIEW_TOUCH_AREA[16][0] && x < MAINVIEW_TOUCH_AREA[16][0] + MAINVIEW_TOUCH_AREA[16][2]
				     && y > MAINVIEW_TOUCH_AREA[16][1] && y < MAINVIEW_TOUCH_AREA[16][1] + MAINVIEW_TOUCH_AREA[16][3])
   				{
   					System.out.println("按下的是前進！");
   					if(DIFFICULTY != -1)
   					{
   						ma.soundandshakeutil.stopBgSound();
   						ma.hd.sendEmptyMessage(0);
   					}
   					ma.soundandshakeutil.playSoundL(5, 0);
   					return true;
   				}
				break;
			}
			break;
		}
		
		return false;
	}
}

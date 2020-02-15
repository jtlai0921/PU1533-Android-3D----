package com.bn.txz;

import static com.bn.txz.Constant.bottom;
import static com.bn.txz.Constant.far;
import static com.bn.txz.Constant.near;
import static com.bn.txz.Constant.ratio;
import static com.bn.txz.Constant.top;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.game.Robot;
import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.VertexDataManager;
import com.bn.txz.manager.VertexTexture3DObjectForDraw;
import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.bn.txz.game.GameData;

public class TXZMenuView extends GLSurfaceView{
	
	TXZActivity activity;		//Activity參考
	TXZMenuView menu;
	private SceneRenderer mRenderer;//場景著色器      
	
    public GameData gdMain=new GameData();
    GameData gdDraw=new GameData();
    GameData gdTemp=new GameData();
    boolean flagn=true;
    float anglet=0;
	float anglex=25;
	boolean flagx=false;
	boolean color=false;
	 
	public TXZMenuView(Context context) {
		super(context);
		this.activity=(TXZActivity)context;
		Constant.MENU_IS_WHILE=true;
		mRenderer = new SceneRenderer();	//建立場景著色器
	    setRenderer(mRenderer);				//設定著色器		        
	    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色   
	}

	public boolean onTouchEvent(MotionEvent e)
	{
		int x=(int)e.getX();
		int y=(int)e.getY();
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(x>=(Constant.Menu_Start_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Start_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Start_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Start_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//按下開始游戲按鈕
			{
				activity.gotoGameView();
				Constant.menu_flag=true;
				Constant.boxFlag=false;
				Constant.MENU_IS_WHILE=false;
				
				
			}
			else if(x>=(Constant.Menu_Set_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Set_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Set_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Set_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//按下設定按鈕
			{
				activity.gotoSetView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
				Constant.boxFlag=false;
			}
			else if(x>=(Constant.Menu_Guan_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Guan_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Guan_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Guan_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//按下選關按鈕
			{
				activity.gotoSelectView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
				
			}
			else if(x>=(Constant.Menu_About_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_About_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_About_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_About_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//按下關於按鈕
			{
				activity.gotoAboutView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
			}
			else if(x>=(Constant.Menu_Help_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Help_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Help_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Help_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//按下幫助按鈕
			{
				activity.gotoHelpView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
			}
			else if(x>=(Constant.Menu_Quit_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Quit_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Quit_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Quit_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//按下離開游戲按鈕
			{
				activity.stopBeiJingYinYue();
				Constant.menu_flag=false;
				Constant.boxFlag=false;
				Constant.MENU_IS_WHILE=false;
		    	System.exit(0);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}
	private class SceneRenderer implements GLSurfaceView.Renderer {
		VertexTexture3DObjectForDraw button;//按鈕
		VertexTexture3DObjectForDraw box;
		VertexTexture3DObjectForDraw bigCelestial;//星空
		VertexTexture3DObjectForDraw smallCelestial;//星空
		float yAngle=0;
		
		int boxId;
		
    	int headTexId;//頁首紋理ID
    	int armTexId;//其他部位紋理ID
    	int start;
    	int set;
    	int chance;
    	int about;
    	int exit;
    	int help;
    	
    	int startl;
    	int setl;
    	int chancel;
    	int aboutl;
    	int exitl;
    	int helpl;
    	
    	//從obj檔案中載入的3D物體的參考
    	VertexTextureNormal3DObjectForDraw[] lovntArray=new VertexTextureNormal3DObjectForDraw[6];	
		Robot robot;
		MenuDoActionThread dat;
		
		public void onDrawFrame(GL10 gl) {
			//清楚深度快取與彩色快取
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_PROJECTION);//設定目前矩陣為投影矩陣
			gl.glLoadIdentity();//設定目前矩陣為單位矩陣
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);  //呼叫此方法計算產生透視投影矩陣
			gl.glMatrixMode(GL10.GL_MODELVIEW);//設定目前矩陣為模式矩陣
			gl.glLoadIdentity();//設定目前矩陣為單位矩陣	
			
			GLU.gluLookAt//可能變形的角度——大角度
            (
            		gl, 
            		0f,   //人眼位置的X
            		5f, 	//人眼位置的Y
            		5f,   //人眼位置的Z
            		0, 	//人眼光看的點X
            		5f,   //人眼光看的點Y
            		0,   //人眼光看的點Z
            		0, 
            		1, 
            		0
            );   
			
            //將繪制資料覆制進臨時資料
    		synchronized(gdDraw.dataLock)
    		{
    			gdDraw.copyTo(gdTemp);
    		} 
    		
    		gl.glPushMatrix();
    		gl.glRotatef(yAngle, 0, 1, 0);
    		bigCelestial.drawSelf(gl);//繪制星空
			smallCelestial.drawSelf(gl);
    		gl.glPopMatrix();
    		
    		gl.glPushMatrix();
    		gl.glTranslatef(Constant.robotXstar, Constant.robotYstar, Constant.robotZstar);
            //繪制物體
            robot.drawSelfAnother(gl);  
            gl.glPopMatrix();
            
            gl.glPushMatrix();
            gl.glTranslatef(Constant.xOffset, 2, -8.5f);
            gl.glScalef(4, 4, 4);
            box.drawSelf(gl, boxId);
            gl.glPopMatrix();
            
            gl.glMatrixMode(GL10.GL_PROJECTION);//設定投影矩陣
			gl.glLoadIdentity();//設定目前矩陣為單位矩陣				
			gl.glOrthof(-ratio, ratio, bottom, top, near, far);//呼叫此方法計算產生正交投影矩陣
			 
			 GLU.gluLookAt//設定攝影機    
		     ( 
					gl, 
					0,0,10,
					0,0,0,
					0,1,0  
			 );				
			
			gl.glMatrixMode(GL10.GL_MODELVIEW);//設定模式矩陣
	        gl.glLoadIdentity(); //設定目前矩陣為單位矩陣	
	        
	        
	        if(color)
	        {
	        	gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.8f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, start);//開始游戲
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.475f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, set);//設定
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.15f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,chance);//選關
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.175f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, about);//關於
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.5f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, help);//幫助
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.825f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,exit);//離開
				gl.glPopMatrix();
	        }
	        else
	        {
	        	gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.8f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, startl);//開始游戲
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.475f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, setl);//設定
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.15f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,chancel);//選關
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.175f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, aboutl);//關於
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.5f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, helpl);//幫助
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.825f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,exitl);//離開
				gl.glPopMatrix();
	        }
            
			
		}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(
					Constant.screenScaleResult.lucX, Constant.screenScaleResult.lucY, 
					(int)(Constant.screenWidthStandard*Constant.screenScaleResult.ratio), 
					(int)(Constant.screenHeightStandard*Constant.screenScaleResult.ratio)
			);
			
			Constant.ratio=Constant.screenWidthStandard/Constant.screenHeightStandard;  
			
    		gl.glEnable(GL10.GL_CULL_FACE);//設定為開啟背面剪裁
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);  		//關閉抗抖動 
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);//設定特定Hint專案的模式，這裡為設定為使用快速模式
			gl.glClearColor(0, 0, 0, 0);		//設定螢幕背景色黑色RGBA
			gl.glEnable(GL10.GL_DEPTH_TEST);	//開啟深度檢驗
			gl.glDisable(GL10.GL_CULL_FACE);	//設定為開啟背面剪裁
			gl.glShadeModel(GL10.GL_SMOOTH);	//設定著色模型為平順著色   
			
			Constant.menu_flag=true;
			Constant.boxFlag=false;
			Constant.xOffset=-9f;
			Constant.robotXstar=-15.5f;
			Constant.robotYstar=0;
			Constant.robotZstar=-8.5f;
            
            //紋理的起始化
            armTexId=initTexture(gl, PicDataManager.picDataArray[4]);//機器人其他部分紋理id
			lovntArray[0]=new VertexTextureNormal3DObjectForDraw//身體
			(
					VertexDataManager.vertexPositionArray[17],
					VertexDataManager.vertexTextrueArray[17],
					VertexDataManager.vertexNormalArray[17],
					VertexDataManager.vCount[17],
					armTexId
			);
			headTexId=initTexture(gl, PicDataManager.picDataArray[3]);//機器人頭紋理id
			lovntArray[1]=new VertexTextureNormal3DObjectForDraw//頭
			(
					VertexDataManager.vertexPositionArray[16],
					VertexDataManager.vertexTextrueArray[16],
					VertexDataManager.vertexNormalArray[16],
					VertexDataManager.vCount[16],
					headTexId
			);
			lovntArray[2]=new VertexTextureNormal3DObjectForDraw//左臂上
			(
					VertexDataManager.vertexPositionArray[18],
					VertexDataManager.vertexTextrueArray[18],
					VertexDataManager.vertexNormalArray[18],
					VertexDataManager.vCount[18],
					armTexId
			);
			
			lovntArray[3]=new VertexTextureNormal3DObjectForDraw//左臂下
			(
					VertexDataManager.vertexPositionArray[19],
					VertexDataManager.vertexTextrueArray[19],
					VertexDataManager.vertexNormalArray[19],
					VertexDataManager.vCount[19],
					armTexId
			);
			
			lovntArray[4]=new VertexTextureNormal3DObjectForDraw//右臂上
			(
					VertexDataManager.vertexPositionArray[20],
					VertexDataManager.vertexTextrueArray[20],
					VertexDataManager.vertexNormalArray[20],
					VertexDataManager.vCount[20],
					armTexId
			);
			
			lovntArray[5]=new VertexTextureNormal3DObjectForDraw//右臂下
			(
					VertexDataManager.vertexPositionArray[21],
					VertexDataManager.vertexTextrueArray[21],
					VertexDataManager.vertexNormalArray[21],
					VertexDataManager.vCount[21],  
					armTexId
			);
			robot=new Robot(lovntArray,TXZMenuView.this);  
            dat=new MenuDoActionThread(robot,TXZMenuView.this);    
            MenuDoActionThread.currActionIndex=0;
            MenuDoActionThread.currStep=0;
            dat.start();
            
            boxId=initTexture(gl,PicDataManager.picDataArray[11]);
            start=initTexture(gl, PicDataManager.picDataArray[30]);//開始游戲紋理id
            set=initTexture(gl, PicDataManager.picDataArray[31]);//設定紋理id
            chance=initTexture(gl, PicDataManager.picDataArray[32]);//選關紋理id
            about=initTexture(gl, PicDataManager.picDataArray[34]);//關於紋理id
            exit=initTexture(gl, PicDataManager.picDataArray[33]);//離開紋理id
            help=initTexture(gl, PicDataManager.picDataArray[40]);//離開紋理id
            
            startl=initTexture(gl, PicDataManager.picDataArray[44]);//開始游戲紋理id
            setl=initTexture(gl, PicDataManager.picDataArray[45]);//設定紋理id
            chancel=initTexture(gl, PicDataManager.picDataArray[46]);//選關紋理id
            aboutl=initTexture(gl, PicDataManager.picDataArray[48]);//關於紋理id
            exitl=initTexture(gl, PicDataManager.picDataArray[47]);//離開紋理id
            helpl=initTexture(gl, PicDataManager.picDataArray[49]);//離開紋理id
            button=new VertexTexture3DObjectForDraw//按鈕
			(
					VertexDataManager.vertexPositionArray[15],//頂點座標資料
					VertexDataManager.vertexTextrueArray[15], //紋理座標
					VertexDataManager.vCount[15] //頂點數
			);
            
            box=new VertexTexture3DObjectForDraw//按鈕
			(
					VertexDataManager.vertexPositionArray[9],//頂點座標資料
					VertexDataManager.vertexTextrueArray[9], //紋理座標
					VertexDataManager.vCount[9] //頂點數
			);  
            
            
            new Thread() 
            {
            	public void run() 
            	{
            		while(Constant.menu_flag)
            		{
            			if(flagn)
            			{            				
            				anglet+=1f;
            				if(anglet>=25)
            				{	
            					flagn=false; 
            				}
            			}else{
            				anglet-=1f;
            				if(anglet<=-25)
            				{
            					flagn=true;
            				}            				
            			}
            			if(flagx)
            			{
            				anglex+=0.5f;
            				if(anglex>=12.5f)
            				{
            					flagx=false;
            				}	
            			}else {
            				anglex-=0.5f;
            				if(anglex<-12.5f)
            				{
            					flagx=true;
            				}
            			}
            			
            			try {
							Thread.sleep(90);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
            	}
            }.start();
            
            new Thread()
            {
            	public void run()
            	{
            		while(Constant.menu_flag)
            		{
            			color=!color;
            			try
            			{
            				Thread.sleep(500);
            			}
            			catch(Exception e)
            			{
            				e.printStackTrace();
            			}
            		}
            	}
            }.start();
            
            bigCelestial=new VertexTexture3DObjectForDraw
            (
            		0,0,3,
            		VertexDataManager.vertexPositionArray[24],
            		VertexDataManager.vertexColorArray[24],
            		VertexDataManager.vCount[24]
            );
            smallCelestial=new VertexTexture3DObjectForDraw
            (
            		0,0,1.5f,
            		VertexDataManager.vertexPositionArray[25],
            		VertexDataManager.vertexColorArray[25],
            		VertexDataManager.vCount[25]
            );
            new Thread()
            {
            	public void run()
            	{
            		while(Constant.menu_flag)
            		{
            			yAngle+=0.5f;
            			if(yAngle>=360)
            			{
            				yAngle=0;
            			}
            			try
            			{
            				Thread.sleep(100);
            			}
            			catch(InterruptedException e)
            			{
            				e.printStackTrace();
            			}
            		}
            	}
            }.start();
		}
	}
	
	public int initTexture(GL10 gl, byte[] data) {//起始化紋理
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		int currTextureId = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);//綁定目前紋理
		
		//使用MipMap紋理過濾
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);//最近點取樣
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);//線性紋理過濾
		 //設定紋理伸展模式為重復  
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
		Bitmap bitmapTmp=BitmapFactory.decodeByteArray(data, 0, data.length);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
		bitmapTmp.recycle();
		return currTextureId;
	}

}	


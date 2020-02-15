package com.bn.cube.view;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.cube.core.MatrixState;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class SoundView extends MyGLSurfaceView{

	HitCubeActivity activity;		//Activity參考
	MyGLSurfaceView mv;
	 private SceneRenderer mRenderer;//場景著色器      	 

	int bg_MusicId;
	int game_MusicId;
	int onId;
	int offId;
	int backId;
	ButtonGraph bgBack;
	ButtonGraph bgMusic;
	ButtonGraph bgSound;
	ButtonGraph bgMusicButton;
	ButtonGraph bgSoundButton;
	ButtonLine buttonLine[]=new ButtonLine[3];
	
	TextureRectNo whichSound;//按鈕
	TextureRectNo button;
	TextureRectNo back; 
	
	Star star;
	StarThread starThread;
	SoundButtonThread sbThread;
	LineThread lineThread;
	
    boolean fhPress; //離開按鈕標志
    boolean Soundflag=true;
  
    int i=0;
    int j=0;
    float angle;
	public SoundView(Context context) {
		super(context);
		this.activity=(HitCubeActivity)context;
	    this.setEGLContextClientVersion(2); //設定使用OPENGL ES2.0
	    mRenderer = new SceneRenderer();	//建立場景著色器
	    setRenderer(mRenderer);				//設定著色器		        
	    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設定著色模式為主動著色   
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        
        case MotionEvent.ACTION_DOWN:
        {
        	if(x>Constant.Sound_Back_Left&&x<Constant.Sound_Back_Right&&y>Constant.Sound_Back_Top&&y<Constant.Sound_Back_Buttom)
        	{
        		activity.hd.sendEmptyMessage(0);
        	}else if(x>Constant.Sound_MButton_Left&&x<Constant.Sound_MButton_Right&&y>Constant.Sound_MButton_Top&&y<Constant.Sound_MButton_Buttom)
        	{
        		if(i==0)
        		{
        			Constant.bgNusic_flag=true;
        		}
        		else
        		{
        			Constant.bgNusic_flag=false;
        		}	
        	}else if(x>Constant.Sound_SButton_Left&&x<Constant.Sound_SButton_Right&&y>Constant.Sound_SButton_Top&&y<Constant.Sound_SButton_Buttom)
        	{        		
        		if(j==0)
        		{
        			Constant.gameMusic_flag=true;
        			
        		}
        		else
        		{
        			Constant.gameMusic_flag=false;
        		}
        	}
        	break;
        }
        case MotionEvent.ACTION_UP:
        {
        	if(x>Constant.Sound_MButton_Left&&x<Constant.Sound_MButton_Right&&y>Constant.Sound_MButton_Top&&y<Constant.Sound_MButton_Buttom)
        	{
        		i=(i+1)%2;
        		if(activity.beijingyinyue!=null)
        	    {
        			activity.beijingyinyue.stop();
        			activity.beijingyinyue=null;
        	    }else
        	    {
        	    	activity.playBeiJingYinYue();
        	    }
        		
        		
        	}
        	if(x>Constant.Sound_SButton_Left&&x<Constant.Sound_SButton_Right&&y>Constant.Sound_SButton_Top&&y<Constant.Sound_SButton_Buttom)
        	{
        		j=(j+1)%2;
        		SharedPreferences.Editor editor=activity.sp.edit();
        		editor.putBoolean("youxiyinxiao",!Constant.gameMusic_flag);
        		editor.commit();
        	}
        	break;        	
        }
        	
        }

        return true;
	}
	private class SceneRenderer implements GLSurfaceView.Renderer {		

		public void onDrawFrame(GL10 gl) {
			//清除深度緩沖與彩色緩沖
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);            
            
            MatrixState.pushMatrix();
            MatrixState.translate(-0.3f, 0.35f, -8);
            bgMusic.drawSelf();
            buttonLine[1].drawSelf();
            MatrixState.popMatrix();
            

            
            MatrixState.pushMatrix();
            MatrixState.translate(-0.3f, -0.35f, -8);
            bgSound.drawSelf();
            buttonLine[1].drawSelf();
            MatrixState.popMatrix();           
            
            MatrixState.pushMatrix();
            MatrixState.translate(0.8f, 0.35f, -8);
            bgMusicButton.drawSelf();
            buttonLine[2].drawSelf();
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(0.8f, -0.35f, -8);
            bgSoundButton.drawSelf();
            buttonLine[2].drawSelf();
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(-0.9f, -0.7f, -8);
            bgBack.drawSelf();
            buttonLine[0].drawSelf();
            MatrixState.popMatrix();                                 
            
            MatrixState.pushMatrix();
            MatrixState.translate(0, Constant.starY, -8);
            star.drawSelf();
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(-0.26f,0.3f, -7);
            whichSound.drawSelf(bg_MusicId);
            MatrixState.popMatrix();
          
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(-0.26f,-0.31f, -7);
            whichSound.drawSelf(game_MusicId);
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(0.7f, 0.31f, -7);
            if(Constant.bgNusic_flag)
            {
            	button.drawSelf(offId);
            }else
            {
         	   	
         	   button.drawSelf(onId);
            }           
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(0.7f, -0.3f, -7);
            if(Constant.gameMusic_flag)
            {
            	button.drawSelf(offId);
            }else
            {
         	   	button.drawSelf(onId);
            } 
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(-0.79f,-0.62f, -7);
            back.drawSelf(backId);
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			
//            //設定視窗大小及位置 
         	gl.glViewport((int)Constant.sx, (int)Constant.sy, (int)(1024*Constant.ratio), (int)(720*Constant.ratio));
         	//計算GLSurfaceView的長寬比
              float ratio = (float) 1024 / 720;
            MatrixState.setProjectFrustum(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 6f, 100f);
          //呼叫此方法產生攝影機9參數位置矩陣
            MatrixState.setCamera(0,0f,0f,0f,0.0f,-2f,0f,1.0f,0.0f);  
            new Thread()
            {
            	public void run()
            	{
            		while(Soundflag)
            		{
            			angle=angle-1.5f;
            			
            			try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
            	}
            }.start();
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			 //設定螢幕背景色RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);  
           
            //開啟深度檢驗
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //開啟背面剪裁   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //起始化變換矩陣
            MatrixState.setInitStack();
            whichSound=new TextureRectNo(0.16f,0.16f,SoundView.this);
            button=new TextureRectNo(0.12f,0.12f,SoundView.this);
            back=new TextureRectNo(0.15f,0.12f,SoundView.this);
            
            Constant.star_flag=true;
            Constant.soundbutton_flag=true;

            
            bgBack=new ButtonGraph(SoundView.this,0.2f,new float[]{0,1,0,0});
            bgMusic=new ButtonGraph(SoundView.this,0.25f,new float[]{1,1,0,0});
            bgSound=new ButtonGraph(SoundView.this,0.25f,new float[]{1,1,0,0});
            bgMusicButton=new ButtonGraph(SoundView.this,0.18f,new float[]{0,1,1,0});
            bgSoundButton=new ButtonGraph(SoundView.this,0.18f,new float[]{0,1,1,0});
            buttonLine[0]=new ButtonLine(SoundView.this,0.2f,0.22f,0f,new float[]{0,0,1,0}); 
            buttonLine[1]=new ButtonLine(SoundView.this,0.25f,0.27f,0f,new float[]{0,0,1,0});
            buttonLine[2]=new ButtonLine(SoundView.this,0.18f,0.2f,0f,new float[]{0,0,1,0});
        	bg_MusicId=initTexture("music.png");
        	game_MusicId=initTexture("sound.png");
        	onId=initTexture("on.png");
        	offId=initTexture("off.png");
        	backId=initTexture("back.png"); 
            star=new Star(2,2,SoundView.this);
            sbThread=new SoundButtonThread(SoundView.this);
            starThread=new StarThread(star);
            Constant.line_frag=true;
            lineThread=new LineThread(buttonLine);
            starThread.start();
            sbThread.start(); 
            lineThread.start();
		}
		
	}
	public int initTexture(String fname)
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,          //產生的紋理id的數量
				textures,   //紋理id的陣列
				0           //偏移量
		);    
		int textureId=textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		
		InputStream is=null;
		try {
			is=this.getResources().getAssets().open(fname);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Bitmap bitmapTemp;
		try
		{
			bitmapTemp=BitmapFactory.decodeStream(is);
					
		}finally
		{
			try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
		}
		GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //紋理型態，在OpenGL ES中必須為GL10.GL_TEXTURE_2D
        		0, 					  //紋理的階層，0表示基本圖形層，可以瞭解為直接貼圖
        		bitmapTemp, 			  //紋理圖形
        		0					  //紋理邊框尺寸
        );
        bitmapTemp.recycle(); 		  //紋理載入成功後釋放圖片
        
        return textureId;
	}

}

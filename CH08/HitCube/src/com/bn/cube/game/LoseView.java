package com.bn.cube.game;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.cube.core.MatrixState;
import com.bn.cube.view.ButtonGraph;
import com.bn.cube.view.ButtonLine;
import com.bn.cube.view.HitCubeActivity;
import com.bn.cube.view.LineThread;
import com.bn.cube.view.MyGLSurfaceView;
import com.bn.cube.view.TextureRectNo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class LoseView extends MyGLSurfaceView{

	MyGLSurfaceView mv;
	HitCubeActivity activity;
	float ratio;
	int numberId[]=new int[10];//數字陣列
	int maohaoId;
	int finalScoreId;
	int totalTimeId;
	int livesLostId;
	int quitId;
	int boardId;
	int finalScoreNum[]=new int[9];
	int totalTimeNum[]=new int[7];
	int livesLostNum[]=new int[9];
	ButtonGraph bgQuit;
	ButtonLine blQuit[]=new ButtonLine[1];

	TextureRect board;
	
	LineThread lineThread;
	LoseButtonThread loseButtonThread;
	Star star;
	StarThread starThread;
	private SceneRenderer mRenderer;//場景著色器      	 
	TextureRectNo quit;
	TextureRectNo textureRect;
	TextureRectNo scorenum;
	public LoseView(Context context) {
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
        	if(x>Constant.Lose_Quit_Left&&x<Constant.Lose_Quit_Right&&y>Constant.Lose_Quit_Top&&y<Constant.Lose_Quit_Buttom)
        	{
        		activity.hd.sendEmptyMessage(0);
        	}
        	break;
        }
        case MotionEvent.ACTION_UP: 
        {
        	break;
        }        	
        }
        return true;
	}
	private class SceneRenderer implements GLSurfaceView.Renderer {

		public void onDrawFrame(GL10 gl) {
			
			//清除深度緩沖與彩色緩沖
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //呼叫此方法計算產生透視投影矩陣
            MatrixState.setProjectFrustum(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 6f, 100f);
            //呼叫此方法產生攝影機9參數位置矩陣
            MatrixState.setCamera(0,0,2f,0,0,-2f,0,1,0); 

            MatrixState.pushMatrix();
            MatrixState.translate(0.7f, -0.8f, -10f);
            bgQuit.drawSelf();
            blQuit[0].drawSelf();
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(0.47f, -0.55f, -6f);
            quit.drawSelf(quitId);
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(0, Constant.starY, -8);
            star.drawSelf();
            MatrixState.popMatrix();
            
            drawScore();
            
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			//設定視口大小
			gl.glViewport((int)Constant.sxgame,(int)Constant.sygame,(int)(1024*Constant.ratio), (int)(720*Constant.ratio));
			ratio=(float)1024/720;
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
            maohaoId=initTexture("maohao.png");
            finalScoreId=initTexture("finalscore.png");
            totalTimeId=initTexture("totaltime.png");
            livesLostId=initTexture("liveslost.png");
            boardId=initTexture("board.png");
            quitId=initTexture("quit.png");
            board=new TextureRect(0.7f,0.7f,LoseView.this);
            scorenum=new TextureRectNo(0.025f,0.05f,LoseView.this);
            quit=new TextureRectNo(0.14f,0.11f,LoseView.this);
            textureRect=new TextureRectNo(0.35f,0.07f,LoseView.this);
            bgQuit=new ButtonGraph(LoseView.this,0.3f,new float[]{1f,0,0f,0});
            blQuit[0]=new ButtonLine(LoseView.this,0.3f,0.315f,0,new float[]{1f,0,0f,0});
            com.bn.cube.view.Constant.line_frag=true;
            Constant.losebutton_frag=true;
            Constant.star_flag=true;
            lineThread=new LineThread(blQuit);
            loseButtonThread=new LoseButtonThread(LoseView.this);
            star=new Star(2,2,LoseView.this);
            starThread=new StarThread(star);
            lineThread.start();
            loseButtonThread.start();
            starThread.start();
            numberId[0]=initTexture("n0.png"); 
            numberId[1]=initTexture("n1.png"); 
            numberId[2]=initTexture("n2.png");
            numberId[3]=initTexture("n3.png");
            numberId[4]=initTexture("n4.png");
            numberId[5]=initTexture("n5.png");
            numberId[6]=initTexture("n6.png");
            numberId[7]=initTexture("n7.png");
            numberId[8]=initTexture("n8.png");
            numberId[9]=initTexture("n9.png");
		}	
	}

	public void drawScore()
	{
   	 	MatrixState.setProjectOrtho(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 1f, 100f);
   	 	MatrixState.setCamera(0f,0f,2f,0f,0.0f,-2f,0f,1.0f,0.0f);
   	 	
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(0, 0, -55f);
        board.drawSelf(boardId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
   	 	
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.35f, 0.35f, 0f);
        textureRect.drawSelf(finalScoreId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        finalScoreNum[8]=(Constant.sumBoardScore+Constant.sumTotalScore)/10000000;
        finalScoreNum[7]=(Constant.sumBoardScore+Constant.sumTotalScore)%10000000/1000000;
        finalScoreNum[6]=(Constant.sumBoardScore+Constant.sumTotalScore)%1000000/100000;
        finalScoreNum[5]=(Constant.sumBoardScore+Constant.sumTotalScore)%100000/10000;
        finalScoreNum[4]=(Constant.sumBoardScore+Constant.sumTotalScore)%10000/1000;
        finalScoreNum[3]=(Constant.sumBoardScore+Constant.sumTotalScore)%1000/100;         
        for(int i=1;i<=8;i++)
        {
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate((float)(-0.25f-0.05*(i-1)),0.2f, 0.3f);
            scorenum.drawSelf(numberId[finalScoreNum[i]]);                 
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
        }                       
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.38f, 0.05f, 0f);
        textureRect.drawSelf(totalTimeId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        totalTimeNum[1]=(int)Constant.totalTime%10;
        totalTimeNum[2]=(int)Constant.totalTime%60/10;
        totalTimeNum[3]=(int)Constant.totalTime/60%10;
        totalTimeNum[4]=(int)Constant.totalTime/60/10;
        totalTimeNum[5]=(int)Constant.totalTime/60/60%10;
        totalTimeNum[6]=(int)Constant.totalTime/60/60/10;
        //時間
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.25f),-0.1f, 0.3f);
        scorenum.drawSelf(numberId[totalTimeNum[1]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.3f),-0.1f, 0.3f);
        scorenum.drawSelf(numberId[totalTimeNum[2]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.35f),-0.1f, 0.3f);
        scorenum.drawSelf(maohaoId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.4f),-0.1f, 0.3f);
        scorenum.drawSelf(numberId[totalTimeNum[3]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.45f),-0.1f, 0.3f);
        scorenum.drawSelf(numberId[totalTimeNum[4]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.5f),-0.1f, 0.3f);
        scorenum.drawSelf(maohaoId);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.55f),-0.1f, 0.3f);
        scorenum.drawSelf(numberId[totalTimeNum[5]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate((float)(-0.6f),-0.1f, 0.3f);
        scorenum.drawSelf(numberId[totalTimeNum[6]]);                 
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        //生命
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(-0.38f, -0.25f, 0f);
        textureRect.drawSelf(livesLostId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        livesLostNum[1]=6;
        for(int i=1;i<=8;i++)
        {
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate((float)(-0.25f-0.05*(i-1)),-0.4f, 0.3f);
            scorenum.drawSelf(numberId[livesLostNum[i]]);                 
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
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

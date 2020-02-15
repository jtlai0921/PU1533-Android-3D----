package com.bn.cube.view;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.cube.core.MatrixState;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class ContView extends MyGLSurfaceView{

	MyGLSurfaceView mv;
	HitCubeActivity activity;
	int contId;
	int backId;
	int bossId;
	float UNIT_SIZE=0.05f;
	float ratio;
	int numberId[]=new int[7];//�Ʀr�}�C
	ButtonGraph bgCont;
	ButtonGraph bgBack;
	ButtonGraph bgLevel;
	ButtonGraph bgBoss;
	ButtonLine buttonLine[]=new ButtonLine[4];
	Star star;
	ContButtonThread cbt;
	LineThread ld;
	StarThread st;
	private SceneRenderer mRenderer;//�����ۦ⾹      	 
	TextureRectNo level;
	TextureRectNo texture;
	TextureRectNo bossTexture;
	TextureRectNo exit;
	public ContView(Context context) {
		super(context);
		this.activity=(HitCubeActivity)context;
	    this.setEGLContextClientVersion(2); //�]�w�ϥ�OPENGL ES2.0
	    mRenderer = new SceneRenderer();	//�إ߳����ۦ⾹
	    setRenderer(mRenderer);				//�]�w�ۦ⾹		        
	    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//�]�w�ۦ�Ҧ����D�ʵۦ�   
	}
	 
	@Override
	public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
        {
        	if(x>Constant.Cont_Level1_Left&&x<Constant.Cont_Level1_Right&&y>Constant.Cont_Level1_Top&&y<Constant.Cont_Level1_Buttom)
        	{
        		com.bn.cube.game.Constant.level=1;
        		activity.hd.sendEmptyMessage(3);
        	}else if(Constant.maxLevel>=2&&x>Constant.Cont_Level2_Left&&x<Constant.Cont_Level2_Right&&y>Constant.Cont_Level2_Top&&y<Constant.Cont_Level2_Buttom)
        	{
        		com.bn.cube.game.Constant.level=2;
        		activity.hd.sendEmptyMessage(3);
        	}else if(Constant.maxLevel>=3&&x>Constant.Cont_Level3_Left&&x<Constant.Cont_Level3_Right&&y>Constant.Cont_Level3_Top&&y<Constant.Cont_Level3_Buttom)
        	{
        		com.bn.cube.game.Constant.level=3;
        		activity.hd.sendEmptyMessage(3);
        	}else if(Constant.maxLevel>=4&&x>Constant.Cont_Level4_Left&&x<Constant.Cont_Level4_Right&&y>Constant.Cont_Level4_Top&&y<Constant.Cont_Level4_Buttom)
        	{
        		com.bn.cube.game.Constant.level=4;
        		activity.hd.sendEmptyMessage(3);
        	}else if(Constant.maxLevel>=5&&x>Constant.Cont_Level5_Left&&x<Constant.Cont_Level5_Right&&y>Constant.Cont_Level5_Top&&y<Constant.Cont_Level5_Buttom)
        	{
        		com.bn.cube.game.Constant.level=5;
        		activity.hd.sendEmptyMessage(3);
        	}else if(Constant.maxLevel>=6&&x>Constant.Cont_Level6_Left&&x<Constant.Cont_Level6_Right&&y>Constant.Cont_Level6_Top&&y<Constant.Cont_Level6_Buttom)
        	{
        		com.bn.cube.game.Constant.level=6;
        		activity.hd.sendEmptyMessage(3);
        	}else if(Constant.boss_flag==true&&x>Constant.Cont_Boss_Left&&x<Constant.Cont_Boss_Right&&y>Constant.Cont_Boss_Top&&y<Constant.Cont_Boss_Buttom)
        	{
        		com.bn.cube.game.Constant.level=7;
        		activity.hd.sendEmptyMessage(3);
        	}else if(x>Constant.Cont_Back_Left&&x<Constant.Cont_Back_Right&&y>Constant.Cont_Back_Top&&y<Constant.Cont_Back_Buttom)
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
			
			//�M���`�׽w�R�P�m��w�R
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //�I�s����k�p�ⲣ�ͳz����v�x�}
            MatrixState.setProjectFrustum(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 6f, 100f);
            //�I�s����k������v��9�ѼƦ�m�x�}
            MatrixState.setCamera(0,0,0f,0,0,-2f,0,1,0); 
                                   
            MatrixState.pushMatrix();
            MatrixState.translate(-1.05f, -0.75f, -10);
            bgBack.drawSelf();
            buttonLine[0].drawSelf();
            MatrixState.popMatrix();                        
         
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate(-0.84f,-0.6f, -8);
            texture.drawSelf(backId);
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(0, Constant.starY, -8);
            star.drawSelf();
            MatrixState.popMatrix();
            
            drawLevel();
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			
            //�]�w�����j�p�Φ�m 
         	gl.glViewport((int)Constant.sx, (int)Constant.sy, (int)(1024*Constant.ratio), (int)(720*Constant.ratio));
         	//�p��GLSurfaceView�����e��
              ratio = (float) 1024 / 720;
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			 //�]�w�ù��I����RGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);  
           
            //�}�Ҳ`������
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //�}�ҭI���ŵ�   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //�_�l���ܴ��x�}
            MatrixState.setInitStack();
            bgBack=new ButtonGraph(ContView.this,0.24f,new float[]{0,1,0,0});
            buttonLine[0]=new ButtonLine(ContView.this,0.24f,0.26f,0.1f,new float[]{0,0,1,0});           
            bgCont=new ButtonGraph(ContView.this,0.24f,new float[]{1,1f,0,0});
            buttonLine[1]=new ButtonLine(ContView.this,0.24f,0.26f,0.3f,new float[]{0,0,1,0});
            bgBoss=new ButtonGraph(ContView.this,0.45f,new float[]{0,0,1,0});
            buttonLine[3]=new ButtonLine(ContView.this,0.45f,0.48f,0.1f,new float[]{0,0,1,0});
            bgLevel=new ButtonGraph(ContView.this,0.25f,new float[]{0,0,1,0});
            buttonLine[2]=new ButtonLine(ContView.this,0.25f,0.27f,0.1f,new float[]{0,0,1,0});
            texture=new TextureRectNo(0.16f,0.12f,ContView.this);
            bossTexture=new TextureRectNo(0.23f,0.2f,ContView.this);
            level=new TextureRectNo(0.16f,0.16f,ContView.this);
            contId=initTexture("cont.png");
            backId=initTexture("back.png");
            bossId=initTexture("boss.png");
            star=new Star(2,2,ContView.this);    
            Constant.contbutton_flag=true;
            cbt=new ContButtonThread(ContView.this);
            cbt.start();
            Constant.line_frag=true;
            ld=new LineThread(buttonLine);
            ld.start();
            Constant.star_flag=true;
            st=new StarThread(star);
            st.start();
            numberId[1]=initTexture("level1.png");             
            numberId[2]=initTexture("level2.png");
            numberId[3]=initTexture("level3.png"); 
            numberId[4]=initTexture("level4.png");
            numberId[5]=initTexture("level5.png");
            numberId[6]=initTexture("level6.png");
		}	
		public void drawLevel()
		{

			for(int i=0;i<Constant.button.length/3;i++)
			{
				MatrixState.pushMatrix();
				MatrixState.translate(Constant.button[3*i], Constant.button[3*i+1],Constant.button[3*i+2]);
				buttonLine[2].drawSelf();
				MatrixState.popMatrix();
			}		
			for(int i=0;i<Constant.maxLevel;i++)
			{
				MatrixState.pushMatrix();
				MatrixState.translate(Constant.button[3*i], Constant.button[3*i+1],Constant.button[3*i+2]);
				bgLevel.drawSelf();
				MatrixState.popMatrix();
			}
			for(int i=0;i<Constant.button.length/3;i++)			
			{
	            GLES20.glEnable(GLES20.GL_BLEND);
	            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
				MatrixState.pushMatrix();
				MatrixState.translate(Constant.button[3*i]*0.8f, Constant.button[3*i+1]*0.8f,Constant.button[3*i+2]*0.8f);
				level.drawSelf(numberId[i+1]);
				GLES20.glDisable(GLES20.GL_BLEND);
				MatrixState.popMatrix();
			}
			MatrixState.pushMatrix();
			MatrixState.translate(-0.34f, -0.424f, -10);
			buttonLine[3].drawSelf();
			MatrixState.popMatrix();
			
			if(Constant.boss_flag)
			{
			MatrixState.pushMatrix();
			MatrixState.translate(-0.34f, -0.424f, -10);
			bgBoss.drawSelf();
			MatrixState.popMatrix();
			}

			MatrixState.pushMatrix();
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			MatrixState.translate(-0.27f, -0.34f, -8);  
			bossTexture.drawSelf(bossId);
			GLES20.glDisable(GLES20.GL_BLEND);
			MatrixState.popMatrix();
		}
	}

	
	public int initTexture(String fname)
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,          //���ͪ����zid���ƶq
				textures,   //���zid���}�C
				0           //�����q
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
        		GLES20.GL_TEXTURE_2D,   //���z���A�A�bOpenGL ES��������GL10.GL_TEXTURE_2D
        		0, 					  //���z�����h�A0��ܰ򥻹ϧμh�A�i�H�A�Ѭ������K��
        		bitmapTemp, 			  //���z�ϧ�
        		0					  //���z��ؤؤo
        );
        bitmapTemp.recycle(); 		  //���z���J���\������Ϥ�
        
        return textureId;
	}

}

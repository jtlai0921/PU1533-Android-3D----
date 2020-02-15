package com.bn.cube.view;
import com.bn.cube.core.MatrixState;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class MenuView extends MyGLSurfaceView{

	MyGLSurfaceView mv;
	HitCubeActivity activity;
	int contId;
	int newId;
	int exitId;
	int setId;
	int helpId;
	boolean flag=true;
	boolean flagn=true;
	boolean flagx=false;
	float angle=0;
	float anglet=0;
	float anglex=25;
	ButtonLine buttonLine[]=new ButtonLine[2];
	float upx=0;
	float upy=1f;
	float upz=0;
	float UNIT_SIZE=0.05f;
	float ratio;
	ButtonGraph bgNew;
	ButtonGraph bgCont;
	ButtonGraph bgExit;
	ButtonGraph bgSet;
	ButtonGraph bgHelp;
	LineThread lt;
	MenuButtonThread bt;
	StarThread st;
	Star star;
	Water water;
	Water water1;
	int waterId;
	WaterThread wt;
	private SceneRenderer mRenderer;//�����ۦ⾹      	 

	TextureRectNo texture;
	TextureRectNo exit;
	TextureRectNo set;
	TextureRectNo help;
	public MenuView(Context context) {
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
        	if(x>Constant.Menu_EX_Left&&x<Constant.Menu_EX_Right&&y>Constant.Menu_EX_Top&&y<Constant.Menu_EX_Buttom)
        	{
        		activity.hd.sendEmptyMessage(5);
        	}else if(x>Constant.Menu_NEW_Left&&x<Constant.Menu_NEW_Right&&y>Constant.Menu_NEW_Top&&y<Constant.Menu_NEW_Buttom)
        	{
        		activity.hd.sendEmptyMessage(3);
        	}else if(x>Constant.Menu_CONT_Left&&x<Constant.Menu_CONT_Right&&y>Constant.Menu_CONT_Top&&y<Constant.Menu_CONT_Buttom)
        	{
        		activity.hd.sendEmptyMessage(1);
        	}else if(x>Constant.Menu_Set_Left&&x<Constant.Menu_Set_Right&&y>Constant.Menu_Set_Top&&y<Constant.Menu_Set_Buttom)
        	{
        		activity.hd.sendEmptyMessage(2);
        	}else if(x>Constant.Menu_Help_Left&&x<Constant.Menu_Help_Right&&y>Constant.Menu_Help_Top&&y<Constant.Menu_Help_Buttom)
        	{
        		activity.hd.sendEmptyMessage(6);
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
            drawExit();
            drawWater();
            //�I�s����k�p�ⲣ�ͳz����v�x�}
            MatrixState.setProjectFrustum(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 1f, 100f);
            //�I�s����k������v��9�ѼƦ�m�x�}
            MatrixState.setCamera(0,0,0f,0,0,-2f,upx,upy,upz); 
            
            
            MatrixState.pushMatrix();
            MatrixState.translate((float)(2.4f*Math.cos(Math.toRadians(anglet))), 0, (float)(-4+1.2f*Math.sin(Math.toRadians(anglet))-4));
            MatrixState.rotate(-anglet, 0, 1, 0);
            MatrixState.rotate(-anglex, 1, 0, 0);
            bgCont.drawSelf();
            buttonLine[1].drawSelf();  
            MatrixState.popMatrix();
            
            
            MatrixState.pushMatrix();
            MatrixState.translate((float)(-2.4f*Math.cos(Math.toRadians(anglet))), 0, (float)(-4-1.2f*Math.sin(Math.toRadians(anglet))-4));
            MatrixState.rotate(-anglet, 0, 1, 0);
            MatrixState.rotate(-anglex, 1, 0, 0);
            bgCont.drawSelf();
            buttonLine[1].drawSelf(); 
            MatrixState.popMatrix();
                       
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//            MatrixState.translate((float)(-0.9f*Math.cos(Math.toRadians(anglet))), 0, (float)(-2-0.6*Math.sin(Math.toRadians(anglet))-1f));
            MatrixState.translate((float)(-0.9f), 0, (float)(-2-0.6*Math.sin(Math.toRadians(anglet))-1f));
            MatrixState.rotate(-anglet, 0, 1, 0);
            MatrixState.rotate(-anglex, 1, 0, 0);
            texture.drawSelf(newId);  
            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            MatrixState.translate((float)(0.9f), 0, (float)(-2+0.6*Math.sin(Math.toRadians(anglet))-1f));
            MatrixState.rotate(-anglet, 0, 1, 0);
            MatrixState.rotate(-anglex, 1, 0, 0);
            texture.drawSelf(contId);
            GLES20.glDisable(GLES20.GL_BLEND); 
            MatrixState.popMatrix(); 
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

            bgNew=new ButtonGraph(MenuView.this,1.6f,new float[]{0.97f,0.105f,0.04f,0});
            buttonLine[1]=new ButtonLine(MenuView.this,1.6f,1.7f,0.0f,new float[]{0,1f,0,0});
            bgCont=new ButtonGraph(MenuView.this,1.6f,new float[]{1,1f,0,0});
            
            bgExit=new ButtonGraph(MenuView.this,0.16f,new float[]{1,0,0,0});
            bgSet=new ButtonGraph(MenuView.this,0.16f,new float[]{1,0,0,0});
            bgHelp=new ButtonGraph(MenuView.this,0.16f,new float[]{1,0,0,0});
            buttonLine[0]=new ButtonLine(MenuView.this,0.16f,0.172f,0f,new float[]{0,0,1,0});
            texture=new TextureRectNo(0.48f,0.36f,MenuView.this);
            exit=new TextureRectNo(0.12f,0.16f,MenuView.this); 
            set=new TextureRectNo(0.14f,0.12f,MenuView.this);
            help=new TextureRectNo(0.12f,0.10f,MenuView.this);
            contId=initTexture("cont.png");      
            newId=initTexture("newb.png"); 
            exitId=initTexture("exit.png"); 
            setId=initTexture("setmusic.png");
            helpId=initTexture("help.png");
            star=new Star(2,2,MenuView.this);
            water=new Water(MenuView.this,30,1);
            water1=new Water(MenuView.this,30,1);
            waterId=initTexture("water.png");                          
            Constant.button_flag=true; 
            bt=new MenuButtonThread(MenuView.this);                                       
            bt.start();
            Constant.star_flag=true;
            st=new StarThread(star); 
            st.start();
            Constant.water_flag=true;
            wt=new WaterThread();
            wt.start();
            Constant.line_frag=true; 
            lt=new LineThread(buttonLine); 
            lt.start();
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
		}		
	}
	public void drawExit()
	{
		//�I�s����k�p�ⲣ�ͳz����v�x�}
		MatrixState.setProjectOrtho(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 6f, 100f);

		//�I�s����k������v��9�ѼƦ�m�x�}
		MatrixState.setCamera(0,0,3f,0,0,0f,0,1,0);
		
        MatrixState.pushMatrix();
        MatrixState.translate(-0.7f, -0.4f, -9);
        bgExit.drawSelf();
        buttonLine[0].drawSelf();
        MatrixState.popMatrix();
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.pushMatrix();
        MatrixState.translate(-0.7f, -0.4f, -8);
        exit.drawSelf(exitId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(0.7f, 0.4f, -9);
        bgSet.drawSelf();
        buttonLine[0].drawSelf();
        MatrixState.popMatrix();
		
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.pushMatrix();
        MatrixState.translate(0.7f, 0.4f, -8);
        set.drawSelf(setId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(0.7f, -0.4f, -9);
        bgHelp.drawSelf();
        buttonLine[0].drawSelf();
        MatrixState.popMatrix();
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.pushMatrix();
        MatrixState.translate(0.7f, -0.4f, -8);
        help.drawSelf(helpId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(0, Constant.starY, -8);
        star.drawSelf();
        MatrixState.popMatrix();
        


	}
	public void drawWater()
	{
		MatrixState.setProjectOrtho(-0.7f, 0.7f, -1f*0.7f, 1f*0.7f, 1f, 100f);
		//�I�s����k������v��9�ѼƦ�m�x�}
		MatrixState.setCamera(0,0,3f,0,0,0f,0,1,0);
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(Constant.wx, -0.64f, -5);
        MatrixState.scale(0.024f, 0.06f, 1);
        water.drawSelf(waterId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.translate(Constant.wx+2f, -0.64f, -5);
        MatrixState.scale(0.024f, 0.06f, 1);
        water.drawSelf(waterId);
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix(); 
	}
	public void leave()  
	{
		flag=false;		
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

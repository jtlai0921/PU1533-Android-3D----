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

public class HelpView extends MyGLSurfaceView{
	private int load_step=0;
	int forId;
	int nexId;
	int backId;
	int tex_loadingviewId;
	int tex_processId;
	int loadId;
	int processBeijing;
	
	int page_index=0;//�ثe���U���������ޭ�
    TextureRectNo loadingView;//3D�������J�ɭ�
    TextureRectNo processBar;//���J�ɭ������i�׫��ܾ�
    TextureRectNo loading;
	boolean isLoadedOk=false;
	boolean isFirstFrame=false;
	MyGLSurfaceView mv;
	HitCubeActivity activity;
	ButtonLine blButton[]=new ButtonLine[1];
	ButtonGraph bgButton;
	float ratio;
	LineThread lt;
	HelpButtonThread hbt;
	private SceneRenderer mRenderer;//�����ۦ⾹      	 
	int picturesId[]=new int[8];
	
	TextureRectNo texture;
	TextureRectNo textureBJ;
	public HelpView(Context context) {
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
			  if(x>Constant.Help_For_Left&&x<Constant.Help_For_Right&&y>Constant.Help_For_Top&&y<Constant.Help_For_Buttom)
			  {
				  if(page_index==0)
				  {
					  activity.hd.sendEmptyMessage(0);
				  }
				  if(page_index>0)
				  {
					  page_index--;
					  page_index=page_index%picturesId.length;
					 
				  }
				 
				  
			  }else if(x>Constant.Help_Nex_Left&&x<Constant.Help_Nex_Right&&y>Constant.Help_Nex_Top&&y<Constant.Help_Nex_Buttom)
			  {
				  if(page_index==picturesId.length-1)
				  {
					  activity.hd.sendEmptyMessage(0);
				  }
				  if( page_index<picturesId.length-1)
				  {
					  page_index++;
					  page_index=page_index%picturesId.length;
				  } 
					  
				 
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

            if(!isLoadedOk)//�p�L�S�����J����
            {
            	 drawOrthLoadingView();            	 
            }
            else
            {
            	drawGameView();
            }
            
            

            
           
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
            
            
            isFirstFrame=true;
            isLoadedOk=false;
            load_step=0;//���J�귽���B��
            //�إ߸��J�ɭ������z�x��
            loadingView=new TextureRectNo(1, 1f, HelpView.this);
            tex_loadingviewId=initTexture("beijing.png");
            processBar=new TextureRectNo(1f, 0.1f,HelpView.this);
            tex_processId=initTexture("process.png"); 
            loading=new TextureRectNo(0.8f,0.1f,HelpView.this);
            loadId=initTexture("load.png");
            processBeijing=initTexture("processbeijing.png");
            
            
            

		}	
	    //�����vø����J�ɭ�
	    public void drawOrthLoadingView(){
	        if(isFirstFrame){ //�Y�G�O�Ĥ@��
	        	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//�]�w�����v
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//�]�w��v��
	            loadingView.drawSelf(tex_loadingviewId);
	            MatrixState.popMatrix();
	        	isFirstFrame=false;
	        } else {//�o�̶i��귽�����J
	        	MatrixState.pushMatrix();
	            MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);//�]�w�����v
	            MatrixState.setCamera(0, 0, 1, 0, 0,-1, 0, 1, 0);//�]�w��v��
	            
	            loadingView.drawSelf(tex_loadingviewId);//ø��I����
	            
	            MatrixState.pushMatrix();  //�]�w�i�׫��ܾ�
	            
	             MatrixState.pushMatrix();
	             GLES20.glEnable(GLES20.GL_BLEND);
	             GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
	             GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	             MatrixState.translate(0, -1+0.08f, 0f);
	             processBar.drawSelf(processBeijing);
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST);  
	             MatrixState.popMatrix();
	            
	             GLES20.glEnable(GLES20.GL_BLEND);
	             GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
	             GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	             MatrixState.translate(-2+2*load_step/(float)8, -1+0.09f, 0f);
	             processBar.drawSelf(tex_processId);
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
	             MatrixState.popMatrix();
	             
	             MatrixState.pushMatrix();
	             GLES20.glEnable(GLES20.GL_BLEND);
	             GLES20.glDisable(GLES20.GL_DEPTH_TEST); 
	             GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	             MatrixState.translate(0.2f, 0.5f, 0);
	             loading.drawSelf(loadId);
	             GLES20.glDisable(GLES20.GL_BLEND); 
	             GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
	             MatrixState.popMatrix();
	             
	            MatrixState.popMatrix();
	            loadResource();//���J�귽����k
	            return;    
	        } 
	        }
	}
	//���J�Ҧ����귽
	public void loadResource()
	{
		switch(load_step)
		{
		case 0:
//			init_Shader();
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
			init_All_Texture(load_step);
			load_step++;
			break;
		case 4:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 5:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 6:
			init_All_Texture(load_step);
			load_step++;
			break;
		case 7:
			init_All_Texture(load_step);
			load_step++;
			break;

		case 8:
			init_All_Texture(load_step);
			isLoadedOk=true;//������@�ſ��
//			isMenuLevel=1;//������@�ſ��
			loadingView=null;//�P��
			processBar=null;//�P��
			break;
		}
	}
	public void init_All_Texture(int index)
	{
		switch(index)
		{
		case 1:
            picturesId[1]=initTexture("jiemian.jpg");
            picturesId[0]=initTexture("xuanguan.jpg");
            break;
		case 2:    
            picturesId[2]=initTexture("jiafen.jpg");
            picturesId[3]=initTexture("jianfen.jpg");
            break;
		case 3:			
            picturesId[4]=initTexture("jiansu.jpg");
            picturesId[5]=initTexture("jiasu.jpg");
            break;
		case 4:    
            picturesId[6]=initTexture("wudi.jpg");
            picturesId[7]=initTexture("zanting.jpg");
            break;
		case 5:    
            bgButton=new ButtonGraph(HelpView.this,0.12f,new float[]{0,1,1,0});
            blButton[0]=new ButtonLine(HelpView.this,0.12f,0.126f,0f,new float[]{0,0,1,0});
            break;
		case 6:    
            nexId=initTexture("xiayiye.png");
            forId=initTexture("shangyiye.png");
            backId=initTexture("back.png");
            break;
		case 7:    
            texture=new TextureRectNo(0.1f,0.09f,HelpView.this);
            textureBJ=new TextureRectNo(1f,0.7f,HelpView.this);
            break;
		case 8:    
            Constant.line_frag=true;
            Constant.button_flag=true;
            lt=new LineThread(blButton);
            hbt=new HelpButtonThread(HelpView.this);
            lt.start();
            hbt.start();
            break;
		}	
	}	
	public void drawGameView()
	{
        //�I�s����k�p�ⲣ�ͳz����v�x�}
        MatrixState.setProjectOrtho(-ratio*0.7f, ratio*0.7f, -1f*0.7f, 1f*0.7f, 1f, 100f);
        //�I�s����k������v��9�ѼƦ�m�x�}
        MatrixState.setCamera(0,0,0f,0,0,-2f,0,1,0);
        MatrixState.pushMatrix();
        MatrixState.translate(0f, 0f, -10);
        textureBJ.drawSelf(picturesId[page_index]);
        MatrixState.popMatrix();        
        
        MatrixState.pushMatrix();
        MatrixState.translate(-0.7f, -0.56f, -9);
        bgButton.drawSelf();
        blButton[0].drawSelf();
        MatrixState.popMatrix();
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.pushMatrix();
        MatrixState.translate(-0.7f, -0.56f, -8);
        if(page_index==0)
        {
        	texture.drawSelf(backId);
        }else
        {
            texture.drawSelf(forId);
        }
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
        
        MatrixState.pushMatrix();
        MatrixState.translate(0.7f, -0.56f, -9);
        bgButton.drawSelf();
        blButton[0].drawSelf();
        MatrixState.popMatrix();
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        MatrixState.pushMatrix();
        MatrixState.translate(0.7f, -0.56f, -8);
        if(page_index==picturesId.length-1)
        {
        	texture.drawSelf(backId);
        }else
        {
            texture.drawSelf(nexId);
        }
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState.popMatrix();
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


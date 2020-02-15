package com.bn.txz;

import static com.bn.txz.Constant.bottom;
import static com.bn.txz.Constant.far;
import static com.bn.txz.Constant.near;
import static com.bn.txz.Constant.ratio;
import static com.bn.txz.Constant.top;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.game.GameData;
import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.VertexDataManager;
import com.bn.txz.manager.VertexTexture3DObjectForDraw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class TXTAboutView extends GLSurfaceView{

	TXZActivity activity;		//Activity�Ѧ�
	private SceneRenderer mRenderer;//�����ۦ⾹      
	 public GameData gdMain=new GameData();
	 GameData gdDraw=new GameData();
	 GameData gdTemp=new GameData();
	 boolean color=false;
	 boolean flagn=true;
	 float anglet=0;
     float anglex=25;
	 boolean flagx=false;
	
	public TXTAboutView(Context context) {
		super(context);
		this.activity=(TXZActivity)context;
		mRenderer = new SceneRenderer();	//�إ߳����ۦ⾹
	    setRenderer(mRenderer);				//�]�w�ۦ⾹		        
	    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//�]�w�ۦ�Ҧ����D�ʵۦ�   
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
		int x=(int)e.getX();
		int y=(int)e.getY();
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(x>(700+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<(1000+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y<(700+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y>(580+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{
				activity.gotoMenuView();		
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
		VertexTexture3DObjectForDraw word;
		int about;
		int aboutl;
		
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_PROJECTION);//�]�w�ثe�x�}����v�x�}
			gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
			gl.glOrthof(-ratio, ratio, bottom, top, near, far);//�I�s����k�p�ⲣ�ͥ����v�x�}
			gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
			gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
			
			 GLU.gluLookAt//�]�w��v��    
		     ( 
					gl, 
					0,0,10,
					0,0,0,
					0,1,0  
			 );		
			 
			 
			 if(color)
			 {
				 gl.glPushMatrix();
				 gl.glRotatef(-anglet, 0, 1, 0);
		    	 gl.glRotatef(-anglex, 1, 0, 0);
				 word.drawSelf(gl, about);
				 gl.glPopMatrix();
			 }
			 else
			 {
				 gl.glPushMatrix();
				 gl.glRotatef(-anglet, 0, 1, 0);
		    	 gl.glRotatef(-anglex, 1, 0, 0);
				 word.drawSelf(gl, aboutl);
				 gl.glPopMatrix();
			 }
				 
	}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(
					Constant.screenScaleResult.lucX, Constant.screenScaleResult.lucY, 
					(int)(Constant.screenWidthStandard*Constant.screenScaleResult.ratio), 
					(int)(Constant.screenHeightStandard+Constant.screenScaleResult.ratio)
			);
			
			Constant.ratio=Constant.screenWidthStandard/Constant.screenHeightStandard;  
			//�]�w���}�ҭI���ŵ�
    		gl.glEnable(GL10.GL_CULL_FACE);
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			//�]�w�I���m��
            gl.glClearColor(0,0,0,0);    
            //�ҥβ`�״���
            gl.glEnable(GL10.GL_DEPTH_TEST);
            //�}�ҭI���ŵ�   
            gl.glEnable(GL10.GL_CULL_FACE);
            
            Constant.about_flag=true;
            
            word=new VertexTexture3DObjectForDraw//���s
			(
					VertexDataManager.vertexPositionArray[27],//���I�y�и��
					VertexDataManager.vertexTextrueArray[27], //���z�y��
					VertexDataManager.vCount[27] //���I��
			);
            
    		about=initTexture(gl, PicDataManager.picDataArray[56]);//���󯾲z
    		
    		aboutl=initTexture(gl, PicDataManager.picDataArray[55]);//���󯾲z
    		
    		new Thread() 
            {
            	public void run() 
            	{
            		while(Constant.about_flag)
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
            		while(Constant.about_flag)
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
            
		}
	}
	public int initTexture(GL10 gl, byte[] data) {//�_�l�Ư��z
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		int currTextureId = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);//�j�w�ثe���z
		
		//�ϥ�MipMap���z�L�o
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);//�̪��I����
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);//�u�ʯ��z�L�o
		 //�]�w���z���i�Ҧ������_  
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
		Bitmap bitmapTmp=BitmapFactory.decodeByteArray(data, 0, data.length);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
		bitmapTmp.recycle();
		return currTextureId;
	}
}

package com.bn.txz;

import static com.bn.txz.Constant.bottom;
import static com.bn.txz.Constant.far;
import static com.bn.txz.Constant.near;
import static com.bn.txz.Constant.ratio;
import static com.bn.txz.Constant.top;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.game.GameData;
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

public class TXZSetView extends GLSurfaceView{

	TXZActivity activity;		//Activity�Ѧ�
	private SceneRenderer mRenderer;//�����ۦ⾹      
	
	public GameData gdMain=new GameData();
    GameData gdDraw=new GameData();
    GameData gdTemp=new GameData();  
    
    boolean flagn=true;
    float anglet=0;
	float anglex=25;
	boolean flagx=false;
	boolean color=false;
    
	public TXZSetView(Context context) {
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
			if(x>=(Constant.Set_kai_1_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Set_kai_1_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Set_kai_1_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Set_kai_1_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{
				Constant.IS_BEIJINGYINYUE=!Constant.IS_BEIJINGYINYUE;
				if(Constant.IS_BEIJINGYINYUE)
				{
					activity.playBeiJingYinYue();
				}
				else
				{
					activity.stopBeiJingYinYue();
				}
			}
			else if(x>=(Constant.Set_kai_2_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Set_kai_2_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Set_kai_2_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Set_kai_2_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{
				Constant.IS_YINXIAO=!Constant.IS_YINXIAO;
			}
			else if(x>=(Constant.Set_back_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Set_back_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Set_back_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Set_back_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{
				activity.gotoMenuView();
				Constant.SET_IS_WHILE=false;
				Constant.set_flag=false;
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
	VertexTexture3DObjectForDraw button;//���s
	int headTexId;//�������zID
	int armTexId;//��L���쯾�zID
	int bgmusic;
	int bgyouxi;
	int on;
	int off;
	int back;
	
	int bgmusicl;
	int bgyouxil;
	int onl;
	int offl;
	int backl;
	
	float yAngle=0;
	
	//�qobj�ɮפ����J��3D���骺�Ѧ�
	VertexTextureNormal3DObjectForDraw[] lovntArray=new VertexTextureNormal3DObjectForDraw[6];	
	VertexTexture3DObjectForDraw bigCelestial;//�P��
	VertexTexture3DObjectForDraw smallCelestial;//�P��
	Robot robot;
	SetDoActionThread dat;	
	
	public void onDrawFrame(GL10 gl) {
		//�M���`�ק֨��P�m��֨�
		gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);//�]�w�ثe�x�}����v�x�}
		gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);  //�I�s����k�p�ⲣ�ͳz����v�x�}
		gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
		gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}	
		
		GLU.gluLookAt//�i���ܧΪ����סX�X�j����
        (
        		gl, 
        		0f,   //�H����m��X
        		5f, 	//�H����m��Y
        		5f,   //�H����m��Z
        		0, 	//�H�����ݪ��IX
        		5f,   //�H�����ݪ��IY
        		0,   //�H�����ݪ��IZ
        		0, 
        		1, 
        		0
        );   
		
        //�Nø�����Ш�i�{�ɸ��
		synchronized(gdDraw.dataLock)
		{
			gdDraw.copyTo(gdTemp);
		} 
		
		gl.glPushMatrix();
		gl.glRotatef(yAngle, 0, 1, 0);
		bigCelestial.drawSelf(gl);//ø��P��
		smallCelestial.drawSelf(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-5f, 0, -2.5f);
        //ø���
        robot.drawSelfSet(gl);
        gl.glPopMatrix();
        
        gl.glMatrixMode(GL10.GL_PROJECTION);//�]�w��v�x�}
		gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}				
		gl.glOrthof(-ratio, ratio, bottom, top, near, far);//�I�s����k�p�ⲣ�ͥ����v�x�}
		 
		 GLU.gluLookAt//�]�w��v��
	     ( 
				gl, 
				0,0,10,
				0,0,0,
				0,1,0  
		 );	
		 
		 gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�Ҧ��x�}
	     gl.glLoadIdentity(); //�]�w�ثe�x�}�����x�}	
	        
         if(color)
         {
        	 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.6f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
     		 gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgmusic);
    		 gl.glPopMatrix();
    		
    		 if(Constant.IS_BEIJINGYINYUE)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, off);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, on);
    			 gl.glPopMatrix();
    		 }
    		 
    		 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.2f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgyouxi);
    		 gl.glPopMatrix();
    		 
    		 if(Constant.IS_YINXIAO)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, off);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, on);
    			 gl.glPopMatrix();
    		 }
    		 
    		
    		
    		 gl.glPushMatrix();
    		 gl.glTranslatef(1.2f, -0.5f, 0.1f);
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, back);
    		 gl.glPopMatrix();
         }
         else
         {
        	 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.6f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
     		 gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgmusicl);
    		 gl.glPopMatrix();
    		
    		 if(Constant.IS_BEIJINGYINYUE)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, offl);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.6f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, onl);
    			 gl.glPopMatrix();
    		 }
    		 
    		 gl.glPushMatrix();
    		 gl.glTranslatef(0.6f, 0.2f, 0.1f);	
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, bgyouxil);
    		 gl.glPopMatrix();
    		 
    		 if(Constant.IS_YINXIAO)
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, offl);
    			 gl.glPopMatrix();
    		 }
    		 else
    		 {
    			 gl.glPushMatrix();
    			 gl.glTranslatef(1.4f, 0.2f, 0.1f);
    			 gl.glRotatef(-anglet, 0, 1, 0);
    		 		gl.glRotatef(-anglex, 1, 0, 0);
    			 gl.glScalef(0.5f, 1f, 0.5f);
    			 button.drawSelf(gl, onl);
    			 gl.glPopMatrix();
    		 }
    		 
    		
    		
    		 gl.glPushMatrix();
    		 gl.glTranslatef(1.2f, -0.5f, 0.1f);
    		 gl.glRotatef(-anglet, 0, 1, 0);
    	 		gl.glRotatef(-anglex, 1, 0, 0);
    		 button.drawSelf(gl, backl);
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
			//�]�w���}�ҭI���ŵ�
    		gl.glEnable(GL10.GL_CULL_FACE);
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);  		//�����ܧݰ� 
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);//�]�w�S�wHint�M�ת��Ҧ��A�o�̬��]�w���ϥΧֳt�Ҧ�
			gl.glClearColor(0, 0, 0, 0);		//�]�w�ù��I����¦�RGBA
			gl.glEnable(GL10.GL_DEPTH_TEST);	//�}�Ҳ`������
			gl.glDisable(GL10.GL_CULL_FACE);	//�]�w���}�ҭI���ŵ�
			gl.glShadeModel(GL10.GL_SMOOTH);	//�]�w�ۦ�ҫ��������ۦ�   
			
			Constant.set_flag=true;
			Constant.SET_IS_WHILE=true;
            
            //���z���_�l��
            armTexId=initTexture(gl, PicDataManager.picDataArray[4]);//�����H��L�������zid
			lovntArray[0]=new VertexTextureNormal3DObjectForDraw//����
			(
					VertexDataManager.vertexPositionArray[17],
					VertexDataManager.vertexTextrueArray[17],
					VertexDataManager.vertexNormalArray[17],
					VertexDataManager.vCount[17],
					armTexId
			);
			headTexId=initTexture(gl, PicDataManager.picDataArray[3]);//�����H�Y���zid
			lovntArray[1]=new VertexTextureNormal3DObjectForDraw//�Y
			(
					VertexDataManager.vertexPositionArray[16],
					VertexDataManager.vertexTextrueArray[16],
					VertexDataManager.vertexNormalArray[16],
					VertexDataManager.vCount[16],
					headTexId
			);
			lovntArray[2]=new VertexTextureNormal3DObjectForDraw//���u�W
			(
					VertexDataManager.vertexPositionArray[18],
					VertexDataManager.vertexTextrueArray[18],
					VertexDataManager.vertexNormalArray[18],
					VertexDataManager.vCount[18],
					armTexId
			);
			
			lovntArray[3]=new VertexTextureNormal3DObjectForDraw//���u�U
			(
					VertexDataManager.vertexPositionArray[19],
					VertexDataManager.vertexTextrueArray[19],
					VertexDataManager.vertexNormalArray[19],
					VertexDataManager.vCount[19],
					armTexId
			);
			
			lovntArray[4]=new VertexTextureNormal3DObjectForDraw//�k�u�W
			(
					VertexDataManager.vertexPositionArray[20],
					VertexDataManager.vertexTextrueArray[20],
					VertexDataManager.vertexNormalArray[20],
					VertexDataManager.vCount[20],
					armTexId
			);
			
			lovntArray[5]=new VertexTextureNormal3DObjectForDraw//�k�u�U
			(
					VertexDataManager.vertexPositionArray[21],
					VertexDataManager.vertexTextrueArray[21],
					VertexDataManager.vertexNormalArray[21],
					VertexDataManager.vCount[21],  
					armTexId
			);
			robot=new Robot(lovntArray,TXZSetView.this);  
            dat=new SetDoActionThread(robot,TXZSetView.this);       
            dat.start();
            
            back=initTexture(gl,PicDataManager.picDataArray[29]);
            bgyouxi=initTexture(gl,PicDataManager.picDataArray[38]);//�}�l�������zid
            bgmusic=initTexture(gl,PicDataManager.picDataArray[37]);//�}�l�������zid
            on=initTexture(gl,PicDataManager.picDataArray[39]);//�]�w���zid
            off=initTexture(gl, PicDataManager.picDataArray[43]);//���J�ɭ��I�����zid
            backl=initTexture(gl,PicDataManager.picDataArray[54]);
            bgyouxil=initTexture(gl,PicDataManager.picDataArray[51]);//�}�l�������zid
            bgmusicl=initTexture(gl,PicDataManager.picDataArray[50]);//�}�l�������zid
            onl=initTexture(gl,PicDataManager.picDataArray[52]);//�]�w���zid
            offl=initTexture(gl, PicDataManager.picDataArray[53]);//���J�ɭ��I�����zid
            button=new VertexTexture3DObjectForDraw//��Ĺ�ɭ����s
			(
					VertexDataManager.vertexPositionArray[15],//�Фl�����I�y�и��
					VertexDataManager.vertexTextrueArray[15], //�ж����z�y��
					VertexDataManager.vCount[15] //���I��
			);
            
            new Thread() 
            {
            	public void run() 
            	{
            		while(Constant.set_flag)
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
            		while(Constant.set_flag)
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
            		while(Constant.set_flag)
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

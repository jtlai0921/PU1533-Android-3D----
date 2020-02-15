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
	
	TXZActivity activity;		//Activity�Ѧ�
	TXZMenuView menu;
	private SceneRenderer mRenderer;//�����ۦ⾹      
	
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
			if(x>=(Constant.Menu_Start_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Start_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Start_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Start_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�}�l�������s
			{
				activity.gotoGameView();
				Constant.menu_flag=true;
				Constant.boxFlag=false;
				Constant.MENU_IS_WHILE=false;
				
				
			}
			else if(x>=(Constant.Menu_Set_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Set_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Set_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Set_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�]�w���s
			{
				activity.gotoSetView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
				Constant.boxFlag=false;
			}
			else if(x>=(Constant.Menu_Guan_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Guan_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Guan_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Guan_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�������s
			{
				activity.gotoSelectView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
				
			}
			else if(x>=(Constant.Menu_About_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_About_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_About_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_About_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U������s
			{
				activity.gotoAboutView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
			}
			else if(x>=(Constant.Menu_Help_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Help_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Help_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Help_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U���U���s
			{
				activity.gotoHelpView();
				Constant.menu_flag=false;
				Constant.MENU_IS_WHILE=false;
			}
			else if(x>=(Constant.Menu_Quit_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					x<=(Constant.Menu_Quit_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
					y>=(Constant.Menu_Quit_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
					y<=(Constant.Menu_Quit_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U���}�������s
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
		VertexTexture3DObjectForDraw button;//���s
		VertexTexture3DObjectForDraw box;
		VertexTexture3DObjectForDraw bigCelestial;//�P��
		VertexTexture3DObjectForDraw smallCelestial;//�P��
		float yAngle=0;
		
		int boxId;
		
    	int headTexId;//�������zID
    	int armTexId;//��L���쯾�zID
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
    	
    	//�qobj�ɮפ����J��3D���骺�Ѧ�
    	VertexTextureNormal3DObjectForDraw[] lovntArray=new VertexTextureNormal3DObjectForDraw[6];	
		Robot robot;
		MenuDoActionThread dat;
		
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
    		gl.glTranslatef(Constant.robotXstar, Constant.robotYstar, Constant.robotZstar);
            //ø���
            robot.drawSelfAnother(gl);  
            gl.glPopMatrix();
            
            gl.glPushMatrix();
            gl.glTranslatef(Constant.xOffset, 2, -8.5f);
            gl.glScalef(4, 4, 4);
            box.drawSelf(gl, boxId);
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
				gl.glTranslatef(1.3f, 0.8f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, start);//�}�l����
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.475f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, set);//�]�w
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.15f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,chance);//����
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.175f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, about);//����
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.5f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, help);//���U
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.825f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,exit);//���}
				gl.glPopMatrix();
	        }
	        else
	        {
	        	gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.8f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, startl);//�}�l����
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.475f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, setl);//�]�w
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, 0.15f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,chancel);//����
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.175f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, aboutl);//����
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.5f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl, helpl);//���U
				gl.glPopMatrix();
				
				gl.glPushMatrix();
				gl.glTranslatef(1.3f, -0.825f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				button.drawSelf(gl,exitl);//���}
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
			
    		gl.glEnable(GL10.GL_CULL_FACE);//�]�w���}�ҭI���ŵ�
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);  		//�����ܧݰ� 
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);//�]�w�S�wHint�M�ת��Ҧ��A�o�̬��]�w���ϥΧֳt�Ҧ�
			gl.glClearColor(0, 0, 0, 0);		//�]�w�ù��I����¦�RGBA
			gl.glEnable(GL10.GL_DEPTH_TEST);	//�}�Ҳ`������
			gl.glDisable(GL10.GL_CULL_FACE);	//�]�w���}�ҭI���ŵ�
			gl.glShadeModel(GL10.GL_SMOOTH);	//�]�w�ۦ�ҫ��������ۦ�   
			
			Constant.menu_flag=true;
			Constant.boxFlag=false;
			Constant.xOffset=-9f;
			Constant.robotXstar=-15.5f;
			Constant.robotYstar=0;
			Constant.robotZstar=-8.5f;
            
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
			robot=new Robot(lovntArray,TXZMenuView.this);  
            dat=new MenuDoActionThread(robot,TXZMenuView.this);    
            MenuDoActionThread.currActionIndex=0;
            MenuDoActionThread.currStep=0;
            dat.start();
            
            boxId=initTexture(gl,PicDataManager.picDataArray[11]);
            start=initTexture(gl, PicDataManager.picDataArray[30]);//�}�l�������zid
            set=initTexture(gl, PicDataManager.picDataArray[31]);//�]�w���zid
            chance=initTexture(gl, PicDataManager.picDataArray[32]);//�������zid
            about=initTexture(gl, PicDataManager.picDataArray[34]);//���󯾲zid
            exit=initTexture(gl, PicDataManager.picDataArray[33]);//���}���zid
            help=initTexture(gl, PicDataManager.picDataArray[40]);//���}���zid
            
            startl=initTexture(gl, PicDataManager.picDataArray[44]);//�}�l�������zid
            setl=initTexture(gl, PicDataManager.picDataArray[45]);//�]�w���zid
            chancel=initTexture(gl, PicDataManager.picDataArray[46]);//�������zid
            aboutl=initTexture(gl, PicDataManager.picDataArray[48]);//���󯾲zid
            exitl=initTexture(gl, PicDataManager.picDataArray[47]);//���}���zid
            helpl=initTexture(gl, PicDataManager.picDataArray[49]);//���}���zid
            button=new VertexTexture3DObjectForDraw//���s
			(
					VertexDataManager.vertexPositionArray[15],//���I�y�и��
					VertexDataManager.vertexTextrueArray[15], //���z�y��
					VertexDataManager.vCount[15] //���I��
			);
            
            box=new VertexTexture3DObjectForDraw//���s
			(
					VertexDataManager.vertexPositionArray[9],//���I�y�и��
					VertexDataManager.vertexTextrueArray[9], //���z�y��
					VertexDataManager.vCount[9] //���I��
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


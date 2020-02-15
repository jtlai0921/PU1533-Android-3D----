package com.bn.txz;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.txz.game.GameData;
import com.bn.txz.game.Robot;
import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.VertexDataManager;
import com.bn.txz.manager.VertexTexture3DObjectForDraw;
import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import static com.bn.txz.Constant.*;

public class TXZSelectView extends GLSurfaceView{
	
	public SceneRenderer mRenderer; 
	
	float x,y;					//Ĳ���I��X�PY�y��
	
	boolean flagn=true;
    float anglet=0;
	float anglex=25;
	boolean flagx=false;
	boolean color=false;
	
	public GameData gdMain=new GameData();
    GameData gdDraw=new GameData();
    GameData gdTemp=new GameData();  
	
	
	int count=0;
	TXZActivity activity;
	public TXZSelectView(TXZActivity activity) {
		super(activity);
		this.activity=activity;
		Constant.SET_IS_WHILE=true;
		this.count=activity.sharedUtil.getPassNum();
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);		//�]�w�ۦ⾹
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);	//�]�w�ۦ�Ҧ����D�ʵۦ�
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(count>=1&&x>=(Constant.Select_1_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_1_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_1_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
						&&y<=(Constant.Select_1_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�Ĥ@��
				{
					com.bn.txz.game.GameData.level=1;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=2&&x>=(Constant.Select_2_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_2_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_1_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_1_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�ĤG��
				{
					com.bn.txz.game.GameData.level=2;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=3&&x>=(Constant.Select_3_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_3_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_1_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_1_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�ĤT��
				{
					com.bn.txz.game.GameData.level=3;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=4&&x>=(Constant.Select_1_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_1_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_2_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_2_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�ĥ|��
				{
					com.bn.txz.game.GameData.level=4;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=5&&x>=(Constant.Select_2_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_2_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_2_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_2_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�Ĥ���
				{
					com.bn.txz.game.GameData.level=5;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=6&&x>=(Constant.Select_3_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_3_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_2_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_2_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�Ĥ���
				{
					com.bn.txz.game.GameData.level=6;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=7&&x>=(Constant.Select_1_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_1_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_3_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_3_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�ĤC��
				{
					com.bn.txz.game.GameData.level=7;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=8&&x>=(Constant.Select_2_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_2_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_3_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_3_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�ĤK��
				{
					com.bn.txz.game.GameData.level=8;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(count>=9&&x>=(Constant.Select_3_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_3_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_3_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_3_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�ĤE��
				{
					com.bn.txz.game.GameData.level=9;
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
					activity.gotoGameView();
				}
				else if(x>=(Constant.Select_back_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						x<=(Constant.Select_back_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
						y>=(Constant.Select_back_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
						y<=(Constant.Select_back_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�Ǧ^���s
				{
					Constant.SELECT_IS_WHILE=false;
					Constant.select_flag=false;
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

	private class SceneRenderer implements GLSurfaceView.Renderer
	{
		VertexTexture3DObjectForDraw bgButton;//���s
		VertexTexture3DObjectForDraw bigCelestial;//�P��
		VertexTexture3DObjectForDraw smallCelestial;//�P��
		Robot robot;
		//�qobj�ɮפ����J��3D���骺�Ѧ�
		VertexTextureNormal3DObjectForDraw[] lovntArray=new VertexTextureNormal3DObjectForDraw[6];	
		SelectDoActionThread dat;
		float yAngle=0;
		
		int bgButtonId[]=new int[9];//���d���s���zid
		int bgButtonBack;//�Ǧ^���s���zid
		int bgButtonBackl;
		int suoId;
		int headTexId;//�������zID
		int armTexId;//��L���쯾�zID
		
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			
			//�M���`�ק֨��P�m��֨�
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_PROJECTION);//�]�w�ثe�x�}����v�x�}
			gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
			gl.glFrustumf(-ratio, ratio, bottom, top, near, far);  //�I�s����k�p�ⲣ�ͳz����v�x�}
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
    		gl.glTranslatef(-4f, 0, -5.5f);
            //ø���
            robot.drawSelfSelect(gl);
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
	        
	        gl.glEnable(GL10.GL_BLEND);//�}�ҲV�X
			 //�]�w���V�X�]�l�P�ت��V�X�]�l
		     gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	        
	        for(int i=0;i<9;i++)
	        {
	        	if(i<count)
	        	{
	        		if(i==0)
		        	{
		        		gl.glPushMatrix();
		     			gl.glTranslatef(0.12f, 0.6f, 0.1f);
		     			bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==1)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.76f, 0.6f, 0.1f);
		     			bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==2)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(1.4f, 0.6f, 0.1f);
		     			bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==3)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.12f, 0.2f, 0.1f);
		     			bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==4)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.76f, 0.2f, 0.1f);
		     			bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==5)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(1.4f, 0.2f, 0.1f);
		        		bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==6)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.12f, -0.2f, 0.1f);
		        		bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==7)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.76f, -0.2f, 0.1f);
		        		bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==8)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(1.4f, -0.2f, 0.1f);
		        		bgButton.drawSelf(gl,bgButtonId[i] );
		     			gl.glPopMatrix();
		        	}
	        	}
	        	else
	        	{
	        		if(i==0)
		        	{
		        		gl.glPushMatrix();
		     			gl.glTranslatef(0.12f, 0.6f, 0.1f);
		     			bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==1)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.76f, 0.6f, 0.1f);
		     			bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==2)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(1.4f, 0.6f, 0.1f);
		     			bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==3)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.12f, 0.2f, 0.1f);
		     			bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==4)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.76f, 0.2f, 0.1f);
		     			bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==5)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(1.4f, 0.2f, 0.1f);
		        		bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==6)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.12f, -0.2f, 0.1f);
		        		bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==7)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(0.76f, -0.2f, 0.1f);
		        		bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
		        	else if(i==8)
		        	{
		        		gl.glPushMatrix();
		        		gl.glTranslatef(1.4f, -0.2f, 0.1f);
		        		bgButton.drawSelf(gl,suoId );
		     			gl.glPopMatrix();
		        	}
	        	}
	        	
	        }
	        gl.glDisable(GL10.GL_BLEND);//�}�ҲV�X
	        
	        if(color)
	        {
	    		gl.glPushMatrix();
				gl.glTranslatef(1.2f, -0.65f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				bgButton.drawSelf(gl, bgButtonBack);
				gl.glPopMatrix();
	        }
	        else
	        {
	    		gl.glPushMatrix();
				gl.glTranslatef(1.2f, -0.65f, 0.1f);
				gl.glRotatef(-anglet, 0, 1, 0);
	    		gl.glRotatef(-anglex, 1, 0, 0);
				bgButton.drawSelf(gl, bgButtonBackl);
				gl.glPopMatrix();
	        }
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) 
		{
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
			
			
			initTexId(gl);//�_�l�Ư��z
			Constant.select_flag=true;
			Constant.SELECT_IS_WHILE=true;
			
			
			bgButton=new VertexTexture3DObjectForDraw//��Ĺ�ɭ����s
			(
					VertexDataManager.vertexPositionArray[15],//�Фl�����I�y�и��
					VertexDataManager.vertexTextrueArray[15], //�ж����z�y��
					VertexDataManager.vCount[15] //���I��
			);
			
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
				robot=new Robot(lovntArray,TXZSelectView.this);  
	            dat=new SelectDoActionThread(robot,TXZSelectView.this);       
	            dat.start();
	            
	            new Thread() 
	            {
	            	public void run() 
	            	{
	            		while(Constant.select_flag)
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
	            		while(Constant.select_flag)
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
	            
	            new Thread()
	            {
	            	public void run()
	            	{
	            		while(Constant.select_flag)
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
		
		public void initTexId(GL10 gl) {//�_�l�Ư��zid
			bgButtonId[0]=initTexture(gl, PicDataManager.picDataArray[21]);//���d1���s���zid
			bgButtonId[1]=initTexture(gl, PicDataManager.picDataArray[22]);//���d2���s���zid
			bgButtonId[2]=initTexture(gl, PicDataManager.picDataArray[23]);//���d3���s���zid
			bgButtonId[3]=initTexture(gl, PicDataManager.picDataArray[24]);//���d4���s���zid
			bgButtonId[4]=initTexture(gl, PicDataManager.picDataArray[25]);//���d5���s���zid
			bgButtonId[5]=initTexture(gl, PicDataManager.picDataArray[26]);//���d6���s���zid
			bgButtonId[6]=initTexture(gl, PicDataManager.picDataArray[27]);//���d7���s���zid
			bgButtonId[7]=initTexture(gl, PicDataManager.picDataArray[28]);//���d8���s���zid
			bgButtonId[8]=initTexture(gl, PicDataManager.picDataArray[35]);//���d8���s���zid
			bgButtonBack=initTexture(gl, PicDataManager.picDataArray[29]);//�Ǧ^���s���zid
			bgButtonBackl=initTexture(gl, PicDataManager.picDataArray[54]);//�Ǧ^���s���zid
			suoId=initTexture(gl, PicDataManager.picDataArray[36]);//�Ǧ^���s���zid
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
}

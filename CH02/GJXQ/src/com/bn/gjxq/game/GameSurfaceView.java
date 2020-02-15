package com.bn.gjxq.game;

import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.gjxq.Constant;
import com.bn.gjxq.GJXQActivity;
import com.bn.gjxq.manager.PicDataManager;
import com.bn.gjxq.manager.VertexDataManager;
import com.bn.gjxq.manager.VertexTexture3DObjectForDraw;
import com.bn.gjxq.manager.VertexTextureNormal3DObjectForDraw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import static com.bn.gjxq.Constant.*;

public class GameSurfaceView extends GLSurfaceView{
	
	GJXQActivity activity;
	
	float x,y;					//Ĳ���I��X�PY�y��
	
	boolean isDrawSmallQP=true;  //�O�_ø��p�ѺмЧӦ�
	float mPreviousX,mPreviousY;
	float startX,startY;
	boolean isMove=false;
	final float MOVE_THOLD=30;	//Ĳ���ɧP�_�O�_�Omove�ʧ@

	public SceneRenderer mRenderer; 
	
	public Object aqLock=new Object();	//�ʧ@��C��
	public Queue<Action> aq=new LinkedList<Action>();//�ʧ@��C
	DoActionThread dat;					//����ʧ@������Ѧ�
	
	//�D���
	GameData gdMain=new GameData();
	//ø����
	GameData gdDraw=new GameData();
	
	VertexTexture3DObjectForDraw room;		//�ж�
	VertexTexture3DObjectForDraw block;		//�ѺФ��
	VertexTextureNormal3DObjectForDraw [] qizi=new VertexTextureNormal3DObjectForDraw [6];	//�Ѥl
	VertexTexture3DObjectForDraw smallBoard;//�p�Ѻ�
	VertexTexture3DObjectForDraw smallBoardBlock;//�p�Ѻиs�զ����
	VertexTexture3DObjectForDraw smallChess;//�p�Ѥl
	VertexTexture3DObjectForDraw smallJianTou;//�p�b�Y
	
	public GameSurfaceView(GJXQActivity activity) 
	{		
		super(activity);
		this.activity=activity;
		this.gdMain.humanColor=Constant.SELECTED_COLOR;
		this.gdMain.currColor=this.gdMain.humanColor;//�ثe�m�⬰�H���m��
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);		//�]�w�ۦ⾹
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);	//�]�w�ۦ�Ҧ����D�ʵۦ�
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		//�Y�ثe���O�H���ѡAĲ���L��
		synchronized(gdMain.dataLock)
		{
			if(gdMain.currColor!=gdMain.humanColor)
			{
				return false;
			}
		}
		
		x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
               startX=x;
               startY=y;
               isMove=false;
               if(x>=(800+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(900+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
            		   &&y>=(20+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
            		   &&y<=(60+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//�Y���U�p�b�Y
               {
            	   isDrawSmallQP=!isDrawSmallQP;//�N�O�_ø��p�ѺмЧӦ�m��
               }
			break;
			case MotionEvent.ACTION_MOVE:
				float dxStart=Math.abs(x-startX);//X��V�������q
				float dyStart=Math.abs(y-startY);//Y��V�������q
				
				//�Y�Gx�Py�h�����d��j��
				if(dxStart>MOVE_THOLD||dyStart>MOVE_THOLD)
				{
					isMove=true;
				}
				if(isMove)//�h���ЧӦ쬰true
				{
					float dx=x-mPreviousX;//X��V�h��������
					float dy=y-mPreviousY;//Y��V�h��������
					Action acTemp=new Action		//������v�����ʧ@
					(
						ActionType.CHANGE_CAMERA,	//�ʧ@���A
						new float[]{dx,dy}			//�ʧ@���
					);
					synchronized(aqLock)			//��W�ʧ@��C
					{
						aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
					}
				}
			break;
			case MotionEvent.ACTION_UP:
				if(!isMove)//�Y�G���O�h��
				{
					Action acTemp=new Action//�B���ʧ@
					(
						ActionType.SELECT_3D,//�ʧ@���A
						new float[]{x,y}//�ʧ@���
					);
					synchronized(aqLock)//��W�ʧ@��C
					{
						aq.offer(acTemp);//�V�ʧ@��C����C���[�J�ʧ@
					}
				}
			break;
		}
		mPreviousX=x;//��s�W�@������m
		mPreviousY=y;
		return true;
	}
	
	private class SceneRenderer implements GLSurfaceView.Renderer
	{
		int roomId;	//�ж����zid
		
		//��l���z�ϥ|�T
		//�Ĥ@������        0---��    1----��
		//�ĤG��    0---���Ŀ�     1----�Ŀ�
		int[][] blockTexId=new int[2][2];
		
		//�Ѥl���z�Ϩ�T�A�զ�Ѥl���z�P�µ��Ѥl�����z
		int[] qzTexId=new int[2];
		
		//�p�ѺЯ��z��id
		int samllBoardId;
		
		//�p�Ѻиs�զ�������z�Ϩ�T
		int[] smallBoardBlockId=new int[2];
		int[] smallChessID=new int[12];
		
		//�p�b�Y���z
		int smallJianTouId;
		
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			//�Nø�����Ш�i�{���ܼ�
			int[][] qiwzTemp=new int[8][8];	//�Ѥl��m
			int[][] gzxzTemp=new int[8][8]; //��l�Ŀ� 
			int[][] qzxzTemp=new int[8][8]; //�Ѥl�Ŀ�
			float[] camera=new float[9];	//�۾�
			synchronized(gdDraw.dataLock)	//��Wø����
			{
				for(int i=0;i<8;i++)
				{
					for(int j=0;j<8;j++)//�Nø���ƴѤl��m�B��l�Ŀ�B�Ѥl�Ŀ�copy�i�{���ܼ�
					{
						qiwzTemp[i][j]=gdDraw.qzwz[i][j];
						gzxzTemp[i][j]=gdDraw.gzxz[i][j];
						qzxzTemp[i][j]=gdDraw.qzxz[i][j];
					}
				}
				//�N��v��9�Ѽ�copy�i�{���ܼ�
				camera[0]=gdDraw.cx;//��v����m
				camera[1]=gdDraw.cy;
				camera[2]=gdDraw.cz;
				camera[3]=gdDraw.tx;//�[���I��m
				camera[4]=gdDraw.ty;
				camera[5]=gdDraw.tz;
				camera[6]=gdDraw.ux;//up�V�q
				camera[7]=gdDraw.uy;
				camera[8]=gdDraw.uz;
			}
			
			//�M���D�r�֨��P�m��֨�
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_PROJECTION);//�]�w�ثe�x�}����v�x�}
			gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
			gl.glFrustumf(-ratio, ratio, bottom, top, near, far);//�I�s����k�p�ⲣ�ͳz����v�x�}
			gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
			gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}			

			GLU.gluLookAt//�]�w��v��
			(
				gl, 
				camera[0],camera[1],camera[2],//��v����m
				camera[3],camera[4],camera[5],//�[���I��m
				camera[6],camera[7],camera[8]//UP�V�q
			);

			
			//ø��ж�
			room.drawSelf(gl, roomId);   
			
			//ø��ѺФ��
			for(int i=0;i<8;i++)
			{
				for(int j=0;j<8;j++)
				{
					int status=gzxzTemp[i][j];			//�Ŀ窱�A
					int hb=GameStaticData.gzhb[i][j];	//��l�s��
					float xOffset=GameStaticData.XOffset[i][j];//��l�bX�b��V�������q
					float zOffset=GameStaticData.ZOffset[i][j];//��l�bZ�b��V�������q
					int texIdTemp=blockTexId[hb][status];	   //�ѺФ�������zid
					gl.glPushMatrix();
					gl.glTranslatef(xOffset, 0, zOffset);
					block.drawSelf(gl, texIdTemp);
					gl.glPopMatrix();
				}
			}
			
			//ø��Ѥl
			//�e�\����       
	        gl.glEnable(GL10.GL_LIGHTING);	        
			for(int i=0;i<8;i++)
			{
				for(int j=0;j<8;j++)
				{
					int qzbh=qiwzTemp[i][j];	//�Ѥl�s��
					int qzxzzt=qzxzTemp[i][j];  //�Ѥl�Ŀ窱�A
					if(qzbh==-1)continue;		//�Y�G�Ѥl�s����-1�Y�S���Ѥl
					float xOffset=GameStaticData.XOffsetQZ[i][j];//�Ѥl�bX�b��V�������q
					float zOffset=GameStaticData.ZOffsetQZ[i][j];//�Ѥl�bY�b��V�������q
					int hbqkTemp=(qzbh>5)?1:0;					 //�лx�Ѥl�O�¤l�٬O�դl
					int texIdTemp=qzTexId[hbqkTemp];//�Ѥl���zid
					gl.glPushMatrix();
					gl.glTranslatef(xOffset, 0.6f+((qzxzzt==1)?0.6f:0.0f), zOffset);
					gl.glRotatef(GameStaticData.ANGLE[hbqkTemp], 0, 1, 0);
					qizi[qzbh%6].drawSelf(gl, texIdTemp);//ø��Ѥl
					gl.glPopMatrix();
				}
			}
			//�T�����       
	        gl.glDisable(GL10.GL_LIGHTING);

			 //�}�ҲV�X
			 gl.glEnable(GL10.GL_BLEND);
			 //�]�w���V�X�]�l�P�ت��V�X�]�l
		     gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
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
	         
	         //ø��p�b�Y
             gl.glPushMatrix();     			 
 			 gl.glTranslatef(1.2f, 0.91f, -2);
 			 smallJianTou.drawSelf(gl, smallJianTouId);
 			 gl.glPopMatrix();
 			 
 			if(isDrawSmallQP)//�Yø��p�Ѻ�
			{
	 			 //ø��p�Ѻ�
			     gl.glPushMatrix();				 
				 gl.glTranslatef(1.2f, 0.3f, -1);
				 smallBoard.drawSelf(gl, samllBoardId);
				 gl.glPopMatrix();
				 
				 //ø��p�ѺФ��
				 for(int i=0;i<8;i++)
				 {
					for(int j=0;j<8;j++)
					{
						int hb=GameStaticData.gzhb[i][j];	//��l�s��
						int tempId=smallBoardBlockId[hb];
						gl.glPushMatrix();
						gl.glTranslatef(0.8f+j*0.115f, 0.695f-i*0.115f, -0.5f);
						smallBoardBlock.drawSelf(gl, tempId);
						gl.glPopMatrix();
					}
				 }
				 
				 for(int i=0;i<8;i++)
				 {
					for(int j=0;j<8;j++)
					{
						int qzbh=qiwzTemp[i][j];	//�Ѥl�s���A�O�b���Ӯ�lø��ӴѤl
						int hb=GameStaticData.gzhb[i][j];	//��l�s���A�ΨӬ�ø���m�⪺��l
						int tempId=smallBoardBlockId[hb];
						if(qzbh==0)
						{
							tempId=smallChessID[10];
						}
						if(qzbh==1)
						{
							tempId=smallChessID[11];
						}
						if(qzbh==2)
						{
							tempId=smallChessID[9];
						}
						if(qzbh==3)
						{
							tempId=smallChessID[7];
						}
						if(qzbh==4)
						{
							tempId=smallChessID[8];
						}
						if(qzbh==5)
						{
							tempId=smallChessID[6];
						}
						if(qzbh==6)
						{
							tempId=smallChessID[4];
						}
						if(qzbh==7)
						{
							tempId=smallChessID[5];
						}
						if(qzbh==8)
						{
							tempId=smallChessID[3];
						}
						if(qzbh==9)
						{
							tempId=smallChessID[1];
						}
						if(qzbh==10)
						{
							tempId=smallChessID[3];
						}
						if(qzbh==11)
						{
							tempId=smallChessID[0];
						}
						if(qzbh==-1)
						{
							tempId=smallBoardBlockId[hb];
						}
						gl.glPushMatrix();
						gl.glTranslatef(0.8f+j*0.115f, 0.7f-i*0.115f, 0f);
						smallChess.drawSelf(gl, tempId);//ø��p�Ѥl
						gl.glPopMatrix();
						
					}
				 }
			 }		     
	         gl.glDisable(GL10.GL_BLEND);//�����V�X
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) 
		{
			gl.glViewport(Constant.screenScaleResult.lucX, Constant.screenScaleResult.lucY, 
					(int)(GJXQActivity.screenWidthStandard*Constant.screenScaleResult.ratio), 
					(int)(GJXQActivity.screenHeightStandard*Constant.screenScaleResult.ratio));
			
			ratio=GJXQActivity.screenWidthStandard/GJXQActivity.screenHeightStandard;  
			//�]�w���}�ҭI���ŵ�
    		gl.glEnable(GL10.GL_CULL_FACE);
    		
    		dat=new DoActionThread(GameSurfaceView.this);//�إ߰���ʧ@�����
    		dat.start();//�Ұʰ����
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
			
			room=new VertexTexture3DObjectForDraw
			(
				VertexDataManager.vertexPositionArray[0],//�Фl�����I�y�и��
				VertexDataManager.vertexTextrueArray[0], //�ж����z�y��
				VertexDataManager.vCount[0], //���I��
				VertexDataManager.AABBData[0]//�]��
			);
			block=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[1],//�Ѥl��������I�y�и��
					VertexDataManager.vertexTextrueArray[1], //�Ѥl��������z�y�и��
					VertexDataManager.vCount[1], //���I��
					VertexDataManager.AABBData[1]//�]��
			);
			qizi[0]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[2],
					VertexDataManager.vertexTextrueArray[2],
					VertexDataManager.vertexNormalArray[2],
					VertexDataManager.vCount[2],
					VertexDataManager.AABBData[2]
			);
			qizi[1]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[3],
					VertexDataManager.vertexTextrueArray[3],
					VertexDataManager.vertexNormalArray[3],
					VertexDataManager.vCount[3],
					VertexDataManager.AABBData[3]
			);
			qizi[2]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[4],
					VertexDataManager.vertexTextrueArray[4],
					VertexDataManager.vertexNormalArray[4],
					VertexDataManager.vCount[4],
					VertexDataManager.AABBData[4]
			);
			qizi[3]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[5],
					VertexDataManager.vertexTextrueArray[5],
					VertexDataManager.vertexNormalArray[5],
					VertexDataManager.vCount[5],
					VertexDataManager.AABBData[5]
			);
			qizi[4]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[6],
					VertexDataManager.vertexTextrueArray[6],
					VertexDataManager.vertexNormalArray[6],
					VertexDataManager.vCount[6],
					VertexDataManager.AABBData[6]
			);
			qizi[5]=new VertexTextureNormal3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[7],
					VertexDataManager.vertexTextrueArray[7],
					VertexDataManager.vertexNormalArray[7],
					VertexDataManager.vCount[7],
					VertexDataManager.AABBData[7]
			);
			
			//�p�Ѻ�
			smallBoard=new VertexTexture3DObjectForDraw  
			(
					VertexDataManager.vertexPositionArray[8],
					VertexDataManager.vertexTextrueArray[8],
					VertexDataManager.vCount[8],
					VertexDataManager.AABBData[8]
			);
			//�p�Ѻиs�զ����
			smallBoardBlock=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[9],
					VertexDataManager.vertexTextrueArray[9],
					VertexDataManager.vCount[9],
					VertexDataManager.AABBData[9]
			);
			//�p�Ѥl
			smallChess=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[10],
					VertexDataManager.vertexTextrueArray[10],
					VertexDataManager.vCount[10],
					VertexDataManager.AABBData[10]
			);
			//�p�b�Y
			smallJianTou=new VertexTexture3DObjectForDraw
			(
					VertexDataManager.vertexPositionArray[11],
					VertexDataManager.vertexTextrueArray[11],
					VertexDataManager.vCount[11],
					VertexDataManager.AABBData[11]
			);
			
			initLight(gl);
			initMaterial(gl);
		}
		
		public void initTexId(GL10 gl) {//�_�l�Ư��zid
			roomId = initTexture(gl, PicDataManager.picDataArray[3]);		//�ж����zid
			blockTexId[0][0]=initTexture(gl,PicDataManager.picDataArray[4]);//�ѺФ�����zid
			blockTexId[0][1]=initTexture(gl,PicDataManager.picDataArray[6]);
			blockTexId[1][0]=initTexture(gl,PicDataManager.picDataArray[0]);
			blockTexId[1][1]=initTexture(gl,PicDataManager.picDataArray[2]);
			qzTexId[0]=initTexture(gl,PicDataManager.picDataArray[5]);		//�Ѥl���zid
			qzTexId[1]=initTexture(gl,PicDataManager.picDataArray[1]);
			samllBoardId=initTexture(gl,PicDataManager.picDataArray[7]);//�p�ѺЯ��zid
			smallBoardBlockId[0]=initTexture(gl,PicDataManager.picDataArray[8]);//�p�Ѻиs�զ�������zid
			smallBoardBlockId[1]=initTexture(gl,PicDataManager.picDataArray[9]);//�p�Ѻиs�զ�������zid
			//�p�Ѥl�����zid
			smallChessID[0]=initTexture(gl,PicDataManager.picDataArray[10]);//�§L
			smallChessID[1]=initTexture(gl,PicDataManager.picDataArray[11]);//�«�
			smallChessID[2]=initTexture(gl,PicDataManager.picDataArray[12]);//�¤�
			smallChessID[3]=initTexture(gl,PicDataManager.picDataArray[13]);//�¶H
			smallChessID[4]=initTexture(gl,PicDataManager.picDataArray[14]);//�¨�
			smallChessID[5]=initTexture(gl,PicDataManager.picDataArray[15]);//�°�
			smallChessID[6]=initTexture(gl,PicDataManager.picDataArray[16]);//�էL	
			smallChessID[7]=initTexture(gl,PicDataManager.picDataArray[17]);//�ի�	
			smallChessID[8]=initTexture(gl,PicDataManager.picDataArray[18]);//�դ�	
			smallChessID[9]=initTexture(gl,PicDataManager.picDataArray[19]);//�նH	
			smallChessID[10]=initTexture(gl,PicDataManager.picDataArray[20]);//�ը�	
			smallChessID[11]=initTexture(gl,PicDataManager.picDataArray[21]);//�հ�	
			
			smallJianTouId=initTexture(gl,PicDataManager.picDataArray[22]);//�p�b�Y���zid
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
	
	private void initLight(GL10 gl)
	{
        gl.glEnable(GL10.GL_LIGHT0);//�}��0���O  
        
        //���ҥ��]�w
        float[] ambientParams={0.4f,0.4f,0.4f,1.0f};//���Ѽ� RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams,0);            

        //���g���]�w
        float[] diffuseParams={1f,1f,1f,1.0f};//���Ѽ� RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams,0); 
        
        //�Ϯg���]�w
        float[] specularParams={0.15f,0.15f,0.15f,1.0f};//���Ѽ� RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams,0);
        
        //�]�w������m
        float[] positionParams={10,20,20,1};//�̫᪺1��ܬO�w���
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParams,0); 
	}
	
	private void initMaterial(GL10 gl)
	{//���謰�զ�ɤ���m�⪺�����b�W���N�N��{�X����m��
        //���ҥ����զ����
        float ambientMaterial[] = {0.4f, 0.4f, 0.4f, 1.0f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial,0);
        //���g�����զ����
        float diffuseMaterial[] = {1f, 1f, 1f, 1.0f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial,0);
        //�������謰�զ�
        float specularMaterial[] = {0.5f, 0.5f, 0.5f, 1.0f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial,0);
        //�����Ϯg�ϰ�,�ƶV�j�ϥհϰ�V�p�V�t
        float shininessMaterial[] = {0.5f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shininessMaterial,0);
	}
}

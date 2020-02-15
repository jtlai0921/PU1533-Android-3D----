package com.bn.txz;

import static com.bn.txz.Constant.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.VertexDataManager;
import com.bn.txz.manager.VertexTexture3DObjectForDraw;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class TXZHelpView extends GLSurfaceView
{
	TXZActivity activity;		//Activity�Ѧ�
	private SceneRenderer mRenderer;//�����ۦ⾹   
//	static int idKey=0;//�I��id��key
	boolean isLoadedOk=false;//�O�_���J�����ЧӦ�
	boolean inLoadView=true;//�O�_�b���J�ɭ��ЧӦ�
	private int load_step=0;//�i�׫��ܾ��B��
	VertexTexture3DObjectForDraw laodBack;//���J�ɭ��I����
	VertexTexture3DObjectForDraw processBar;//���J �ɭ������i�׫��ܾ��x��
	VertexTexture3DObjectForDraw loading;//���J�ɭ�����r�x��
	public TXZHelpView(TXZActivity activity) 
	{
		super(activity);
		this.activity=activity;
		mRenderer = new SceneRenderer();	//�إ߳����ۦ⾹
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//�]�w�ۦ�Ҧ����D�ʵۦ�   
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(x<(Constant.Help_pre_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&x>(Constant.Help_pre_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&y<(Constant.Help_pre_b+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
					&&y>(Constant.Help_pre_t+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{//���U������s
				if(idKey==0)
				{
					activity.gotoMenuView();
					idKey=0;
				}
				else{
					idKey=idKey-1;
				}
			}
			if(x<(Constant.Help_next_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&x>(Constant.Help_next_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio
					&&y<(Constant.Help_next_b+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
					&&y>(Constant.Help_next_t+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)
			{//���U�k����s
				if(idKey==9)
				{
					activity.gotoMenuView();
					idKey=0;
				}
				else{
					idKey=idKey+1;
				}
			}
			break;
		}
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		VertexTexture3DObjectForDraw helpback;
		VertexTexture3DObjectForDraw button;
		
		int helpId[]=new int[10];
		int prepageId;
		int nextpageId;
		
		int loadBackId;//���J�ɭ��I���x�ί��z
		int processBeijing;//���J�ɭ��i�׫��ܾ��x�έI��
		int tex_processId;//�i�׫��ܾ�
		int loadId;
		
		private boolean isFirstFrame=true;
		@Override
		public void onDrawFrame(GL10 gl) {
			//�M���`�ק֨��P�m��֨�
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			if(!isLoadedOk)
			{
				inLoadView=true;
				drawLoadingView(gl);
			}
			else
			{
				inLoadView=false;
				drawHelpView(gl);
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
			
			loadBackId=initTexture(gl, PicDataManager.picDataArray[12]);//���J�ɭ��I�����zid
			processBeijing=initTexture(gl, PicDataManager.picDataArray[13]);//���J�ɭ��i�׫��ܾ��I�����zid
			tex_processId=initTexture(gl, PicDataManager.picDataArray[14]);//���J�ɭ��i�׫��ܾ����zid
			loadId=initTexture(gl, PicDataManager.picDataArray[15]);//���J�ɭ��I�����zid
			laodBack=new VertexTexture3DObjectForDraw//���J�ɭ��I���x��
			(
					VertexDataManager.vertexPositionArray[22],//���J�ɭ��I���x�Ϊ����I�y�и��
					VertexDataManager.vertexTextrueArray[22], //���J�ɭ��I���x�ί��z�y��
					VertexDataManager.vCount[22] //���I��
			);
			
			processBar=new VertexTexture3DObjectForDraw//���J�ɭ��I���x��
			(
					VertexDataManager.vertexPositionArray[11],//���J�ɭ��I���x�Ϊ����I�y�и��
					VertexDataManager.vertexTextrueArray[11], //���J�ɭ��I���x�ί��z�y��
					VertexDataManager.vCount[11] //���I��
			);
			
			loading=new VertexTexture3DObjectForDraw//���J�ɭ���r�x��
			(
					VertexDataManager.vertexPositionArray[12],//���J�ɭ���r�x�Ϊ����I�y�и��
					VertexDataManager.vertexTextrueArray[12], //���J�ɭ���r�x�ί��z�y��
					VertexDataManager.vCount[12] //���I��
			);
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

		public void drawLoadingView(GL10 gl)
		{
			if(isFirstFrame)
			{
				gl.glPushMatrix();
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
			     laodBack.drawSelf(gl, loadBackId);//ø����J�ɭ��I��
			     gl.glPopMatrix();
			     isFirstFrame=false;
			}
			else
			{
				gl.glPushMatrix();
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
			     laodBack.drawSelf(gl, loadBackId);//ø����J�ɭ��I��
			     
			     gl.glPushMatrix();
			     
			     gl.glPushMatrix();
			     gl.glEnable(GL10.GL_BLEND);
			     gl.glDisable(GL10.GL_DEPTH_TEST); 
			     gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			     gl.glTranslatef(0, -1+0.08f, 0f);
			     processBar.drawSelf(gl, processBeijing);
	             gl.glDisable(GL10.GL_BLEND); 
	             gl.glEnable(GL10.GL_DEPTH_TEST);  
	             gl.glPopMatrix();
	            
	             gl.glEnable(GL10.GL_BLEND);
	             gl.glDisable(GL10.GL_DEPTH_TEST); 
	             gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	             gl.glTranslatef(-3.5f+3.5f*load_step/(float)8, -1+0.09f, 0f);
	             processBar.drawSelf(gl, tex_processId);
	             gl.glDisable(GL10.GL_BLEND); 
	             gl.glEnable(GL10.GL_DEPTH_TEST); 
	             gl.glPopMatrix();
	             
	             gl.glPushMatrix();
	             gl.glEnable(GL10.GL_BLEND);
	             gl.glDisable(GL10.GL_DEPTH_TEST); 
	             gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	             gl.glTranslatef(0.2f, 0.5f, 0);
	             loading.drawSelf(gl, loadId);
	             gl.glDisable(GL10.GL_BLEND); 
	             gl.glEnable(GL10.GL_DEPTH_TEST); 
	             gl.glPopMatrix();

	            gl.glPopMatrix();
	            loadResource(gl);//���J�귽����k
	            return;
			}
		}
		//�إ����U�ɭ��ݭn�����z�M�x��
		public void loadResource(GL10 gl)
		{
			switch(load_step)
			{
			case 0:
				load_step++;
				break;
			case 1:
				//���zid
				helpId[0]=initTexture(gl,PicDataManager.picDataArray[5]);
				helpId[1]=initTexture(gl,PicDataManager.picDataArray[6]);
				load_step++;
				break;
			case 2:
				helpId[2]=initTexture(gl,PicDataManager.picDataArray[7]);
				helpId[3]=initTexture(gl,PicDataManager.picDataArray[8]);
				load_step++;
				break;
			case 3:
				helpId[4]=initTexture(gl,PicDataManager.picDataArray[41]);
				helpId[5]=initTexture(gl,PicDataManager.picDataArray[42]);
				load_step++;
				break;
			case 4:
				helpId[6]=initTexture(gl,PicDataManager.picDataArray[59]);
				helpId[7]=initTexture(gl,PicDataManager.picDataArray[60]);
				load_step++;
				break;
			case 5:
				helpId[8]=initTexture(gl,PicDataManager.picDataArray[61]);
				helpId[9]=initTexture(gl,PicDataManager.picDataArray[62]);
				load_step++;
				break;
			case 6:
				prepageId=initTexture(gl,PicDataManager.picDataArray[63]);
				nextpageId=initTexture(gl,PicDataManager.picDataArray[64]);
				load_step++;
				break;
			case 7:
				helpback=new VertexTexture3DObjectForDraw//��Ĺ�ɭ����s
				(
						VertexDataManager.vertexPositionArray[22],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[22], //�ж����z�y��
						VertexDataManager.vCount[22] //���I��
				);
				load_step++;
				break;
			case 8:
				button=new VertexTexture3DObjectForDraw//��Ĺ�ɭ����s
				(
						VertexDataManager.vertexPositionArray[31],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[31], //�ж����z�y��
						VertexDataManager.vCount[31] //���I��
				);
				isLoadedOk=true;//������@�ſ��
				laodBack=null;//�P��
				processBar=null;//�P��
				break;
			}
		}
		public void drawHelpView(GL10 gl)
		{
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
	        
	        helpback.drawSelf(gl, helpId[idKey]);
			gl.glEnable(GL10.GL_BLEND);//�}�ҲV�X
			 //�]�w���V�X�]�l�P�ت��V�X�]�l
		    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		    
			gl.glPushMatrix();
			gl.glTranslatef(-1.3f, -0.7f, 0.1f);
			button.drawSelf(gl, prepageId);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslatef(1.3f, -0.7f, 0.1f);
			button.drawSelf(gl, nextpageId);
			gl.glPopMatrix();
			gl.glDisable(GL10.GL_BLEND);//�����V�X
		}
	}
}

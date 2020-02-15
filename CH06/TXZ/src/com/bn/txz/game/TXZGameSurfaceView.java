package com.bn.txz.game;

import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static com.bn.txz.Constant.*;
import com.bn.txz.Constant;
import com.bn.txz.TXZActivity;
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

public class TXZGameSurfaceView extends GLSurfaceView{
	
	public SceneRenderer mRenderer; 
	
	//�D���
	GameData gdMain=new GameData();
	//ø����
	GameData gdDraw=new GameData();
	//�{�ɸ��
	GameData gdTemp=new GameData();
	//���d���O���D�Bø��M�{�ɸ��
	GuanQiaData gqdMain=new GuanQiaData();
	GuanQiaData gqdDraw=new GuanQiaData();
	GuanQiaData gqdTemp=new GuanQiaData();
	
	float mPreviousX,mPreviousY;
	float startX,startY;
	boolean isMove=false;
	final float MOVE_THOLD=30;	//Ĳ���ɧP�_�O�_�Omove�ʧ@
	float x,y;					//Ĳ���I��X�PY�y��
	
	public Object aqLock=new Object();	//�ʧ@��C��
	public Queue<Action> aq=new LinkedList<Action>();//�ʧ@��C
	TXZDoActionThread dat;					//����ʧ@������Ѧ�
	
	public Object aqygLock=new Object();
	public Queue<Action> aqyg=new LinkedList<Action>();//�n��ʧ@��C
	YGDoActionThread ygdat;
	
	VertexTexture3DObjectForDraw room;		//�ж�
	VertexTexture3DObjectForDraw sky;		//�Ѫ�
	VertexTextureNormal3DObjectForDraw wall;//��
	VertexTexture3DObjectForDraw water;		//��
	VertexTextureNormal3DObjectForDraw[] lovntArray=new VertexTextureNormal3DObjectForDraw[6];//�����H�U����
	Robot robot;
	VertexTexture3DObjectForDraw left;//�V�����������s
	
	VertexTexture3DObjectForDraw yaogan1;//�n��
	VertexTexture3DObjectForDraw yaogan2;
	
	VertexTexture3DObjectForDraw laodBack;//���J�ɭ��I����
	VertexTexture3DObjectForDraw processBar;//���J �ɭ������i�׫��ܾ��x��
	VertexTexture3DObjectForDraw loading;//���J�ɭ�����r�x��
	
	VertexTexture3DObjectForDraw bground;//Ĺ�ɭ��I��
	VertexTexture3DObjectForDraw bgbutton;//Ĺ�ɭ����h�U�@�����s
	
	int num=16;
	VertexTexture3DObjectForDraw texRect[]=new VertexTexture3DObjectForDraw[num];//���z�x��
	
	boolean waterflag=true;
	private int load_step=0;//�i�׫��ܾ��B��
	
	public static float currDegree=0;
	public static float currDegreeView=0;
	public static float currX;//�����H�Ҧb����m
	public static float currY;
	public static float currZ;
	public boolean isFirst=false;//�O�_�O�Ĥ@���ഫ���ת��Ч�
	
	boolean viewFlag=false;//��v�����׼ЧӦ�
	 
	boolean isLoadedOk=false;//�O�_���J�����ЧӦ�
	boolean inLoadView=true;//�O�_�b���J�ɭ��ЧӦ�
	public boolean isInAnimation=false;//�O�_�b����ʵe�ЧӦ�
	private boolean isWinOrLose=false;//�O�_�b��Ĺ�ЧӦ�
	public boolean isDrawWinOrLose=false;
	
	
	public boolean temp=true;
	
	static float offsetx=0;//�n��h����x�b�������q
	static float offsety=0;//�n��h����y�b�������q
	boolean isYaogan=false;//�Ĥ@�����U���I�O�_�b�n�줺
	static float vAngle=100;//�n�쪺���ਤ��
	boolean isGo=false;//�O�_�[�J�e�i�ʧ@���C���Ч�
	boolean isGoFlag;//��sisGo��������O�_�u�@���ЧӦ�
	
	float skyAngle=0;
	public static boolean isSkyAngle=false;
	
	
	
	TXZActivity activity;
	public TXZGameSurfaceView(TXZActivity activity) {
		super(activity);
		this.activity=activity;
		mRenderer = new SceneRenderer();
		setRenderer(mRenderer);		//�]�w�ۦ⾹
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);	//�]�w�ۦ�Ҧ����D�ʵۦ�
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if(inLoadView)
		{
			return false;
		}
		
		float YAOGAN_WAI_LEFT=(763.6f+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio;//Ĳ����t��m
		float YAOGAN_WAI_RIGHT=(938.2f+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio;
		float YAOGAN_WAI_TOP=(518.4f+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio;
		float YAOGAN_WAI_BOTTOM=(334.8f+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio;
		
		Constant.YAOGAN_CENTER_X=YAOGAN_WAI_LEFT+(YAOGAN_WAI_RIGHT-YAOGAN_WAI_LEFT)/2;//����
		Constant.YAOGAN_CENTER_Y=YAOGAN_WAI_BOTTOM+(YAOGAN_WAI_TOP-YAOGAN_WAI_BOTTOM)/2;
		Constant.YAOGAN_R=91.8f*(screenWidth-Constant.screenScaleResult.lucX*Constant.screenScaleResult.ratio*2)/Constant.screenWidthStandard;
		
		x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(isInAnimation)
				{
					return false;
				}
               startX=x;
               startY=y;
               isMove=false;
               currDegreeView=currDegree;
               isYaogan=false;
               if(Math.sqrt((x-Constant.YAOGAN_CENTER_X)*(x-Constant.YAOGAN_CENTER_X)+
            		   (y-Constant.YAOGAN_CENTER_Y)*(y-Constant.YAOGAN_CENTER_Y))<Constant.YAOGAN_R)
               {//Ĳ���I�b�n�줺
            	   if(isWinOrLose)
            	   {
            		   return false;
            	   }
            	   isYaogan=true;
               }
               if(x>=(Constant.Game_View_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(Constant.Game_View_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   y>=(Constant.Game_View_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
            		   y<=(Constant.Game_View_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//���U�ഫ���׵������s
               {
            	   if(isWinOrLose)
            	   {
            		   return false;
            	   }
            	   Action acTemp=new Action			//�ഫ���ת��ʧ@
				   (
						ActionType.CONVERT			//�ʧ@���A
				   );
					synchronized(aqLock)			//��W�ʧ@��C
					{
						aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
					}
					isFirst=!isFirst;
               }
               else if(x>=(Constant.Game_Win_First_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(Constant.Game_Win_First_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   y>=(Constant.Game_Win_First_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
            		   y<=(Constant.Game_Win_First_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio
            		   &&isWinOrLose)//�b��Ĺ�ɭ����U�Ĥ@�ӫ��s
               {
        		   //�Ǧ^����ɭ�
            	   if(!isWinOrLose)
            	   {
            		   return false;
            	   }
        		   activity.handler.sendEmptyMessage(Constant.COMMAND_GOTO_MENU_VIEW);
        		   isWinOrLose=false;
        		   isDrawWinOrLose=false;
               }
               else if(x>=(Constant.Game_Win_Two_l+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   x<=(Constant.Game_Win_Two_r+Constant.screenScaleResult.lucX)*Constant.screenScaleResult.ratio&&
            		   y>=(Constant.Game_Win_Two_u+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio&&
            		   y<=(Constant.Game_Win_Two_d+Constant.screenScaleResult.lucY)*Constant.screenScaleResult.ratio)//�b��Ĺ�ɭ����U�ĤG�ӫ��s
               {
            	   if(!isWinOrLose)
            	   {
            		   return false;
            	   }
            	   if(gdMain.winFlag)//Ĺ�ɭ����U�U�@�����s
            	   {
            		   //��U�@���h
            		   if(GameData.level==9)
            		   {
            			   GameData.level=0;
            		   }
            		   gdMain.loseFlag=false;
            		   gdMain.winFlag=false;
            		   isWinOrLose=false;
            		   GameData.level=GameData.level+1;
            		   isDrawWinOrLose=false;
            		   activity.handler.sendEmptyMessage(Constant.COMMAND_GOTO_GAME_VIEW);
            	   }
            	   else if(gdMain.loseFlag)//��ɭ����U�������s
            	   {
            		   gdMain.loseFlag=false;
            		   gdMain.winFlag=false;
            		   isWinOrLose=false;
            		   isDrawWinOrLose=false;
            		   activity.handler.sendEmptyMessage(Constant.COMMAND_GOTO_GAME_VIEW);
            	   }
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
				if(!viewFlag&&isMove&&!isYaogan)	//�h���ЧӦ쬰true,�åB�_�l���U���I���b�n�줺
				{
					float dx=x-mPreviousX;			//X��V�h��������
					float dy=y-mPreviousY;			//Y��V�h��������
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
				if(isMove&&isYaogan)				//�h���ЧӦ쬰true,�åB�_�l���U���I�b�n�줺
				{
					Action acTemp=new Action		//���ܷn�쪺�ʧ@
					(
						ActionType.YAOGAN_MOVE,		//�ʧ@���A
						new float[]{x,y}			//�ʧ@���
					);
					synchronized(aqygLock)			//��W�ʧ@��C
					{
						aqyg.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
					}
					if(vAngle>=-45&&vAngle<45&&isGo)
					{//�e�i
						Action acTempl=new Action		//�����H�e�i���ʧ@
					   (
							ActionType.ROBOT_UP			//�ʧ@���A
					   );
						synchronized(aqLock)			//��W�ʧ@��C
						{
							aq.offer(acTempl);			//�N�ʧ@��C����C���[�J�ʧ@
						}
						isGo=false;
					}
				}
			break;
			case MotionEvent.ACTION_UP:
				Action actemp=new Action
				(
					ActionType.ACTION_UP	
				);
				synchronized(aqygLock)			//��W�ʧ@��C
				{
					aqyg.offer(actemp);			//�N�ʧ@��C����C���[�J�ʧ@
				}
				if(isYaogan)
				{
					if(vAngle>=-135&&vAngle<-45)
					{//�k��
						Action acTemp=new Action		//�����H�V�k�઺�ʧ@
					   (
							ActionType.ROBOT_RIGHT	//�ʧ@���A
					   );
						synchronized(aqLock)			//��W�ʧ@��C
						{
							aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
						}
					}
					if((vAngle>=45&&vAngle<90)||(vAngle>=-270&&vAngle<-225))
					{//����
						Action acTemp=new Action		//�����H�V���઺�ʧ@
	   				   (
	   						ActionType.ROBOT_LEFT	//�ʧ@���A
	   				   );
	   					synchronized(aqLock)			//��W�ʧ@��C
	   					{
	   						aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
	   					}
					}
					if(vAngle>=-225&&vAngle<-135)
					{//����
						Action acTemp=new Action		//�����H�V���઺�ʧ@
					   (
							ActionType.ROBOT_DOWN	//�ʧ@���A
					   );
						synchronized(aqLock)			//��W�ʧ@��C
						{
							aq.offer(acTemp);			//�N�ʧ@��C����C���[�J�ʧ@
						}
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
		int currentFlagindex=0;//�ثe�ؽs��
		private boolean isFirstFrame=true;
		int roomId;	//�ж����zid
		int headId;//�Yid
		int leftId;//���uid
		int wallId;//�����zid
		int waterId;//�������zid
		int convertId;//�����ഫ�������s���zid
		int boxId;//�c�l���zid
		int targetId;//�ت��a���zid
		int loadBackId;//���J�ɭ��I���x�ί��z
		int processBeijing;//���J�ɭ��i�׫��ܾ��x�έI��
		int tex_processId;//�i�׫��ܾ�
		int loadId;
		int loseId;//Ĺ�ɭ���r�x�ί��zid
		int winId;//Ĺ�ɭ��I�����zid
		int returnId;//Ĺ�ɭ��U�@�����s���zid
		int retryId;//Ĺ�ɭ����Ǧ^���s���zid
		int next_GuanId;//�U�@�����s���zid
		int yaogan1Id;//�n�쪺���zid
		int yaogan2Id;
		
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			
			//�M���`�ק֨��P�m��֨�
			gl.glClear(GL10.GL_DEPTH_BUFFER_BIT|GL10.GL_COLOR_BUFFER_BIT);
			
			  if(!isLoadedOk)//�p�L�S�����J����
	          {
				     inLoadView=true;
	            	 drawOrthLoadingView(gl);  //ø����J�i�׫��ܾ��ɭ�          	 
	          }
			  else
			  {
				  inLoadView=false;
				  drawGameView(gl);
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
			Constant.CURR_DIRECTION=POSITIVE_MOVETO_Z;//�_�l�¦V��z�b����V
			
			Robot.RobotFlag=true;
			currDegree=0;
			isWinOrLose=false;
			Constant.IS_DRAW_WIN=false;
			isSkyAngle=true;
			
			initTexId(gl);//�_�l�Ư��z
			
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
			
			//�Ұʤ@�Ӱ�����ʺA������
            new Thread()
            {
            	@Override
            	public void run()
            	{
            		while(true)
            		{
            			currentFlagindex=(currentFlagindex+1)%texRect.length;            		
	            		try {
							Thread.sleep(100);
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
            		while(isSkyAngle)
            		{
            			skyAngle=(skyAngle+0.2f)%360;
            			try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
            	}
            }.start();
        	initLight(gl);
			initMaterial(gl);
            
		}
		
		public void initTexId(GL10 gl) {//�_�l�Ư��zid
			loadBackId=initTexture(gl, PicDataManager.picDataArray[12]);//���J�ɭ��I�����zid
			processBeijing=initTexture(gl, PicDataManager.picDataArray[13]);//���J�ɭ��i�׫��ܾ��I�����zid
			tex_processId=initTexture(gl, PicDataManager.picDataArray[14]);//���J�ɭ��i�׫��ܾ����zid
			loadId=initTexture(gl, PicDataManager.picDataArray[15]);//���J�ɭ��I�����zid

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
	
	
		//ø��i�׫��ܾ����J�ɭ�
		public void drawOrthLoadingView(GL10 gl)
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
		
		//���J�Ҧ����귽
		public void loadResource(GL10 gl)
		{
			switch(load_step)
			{
			case 0:
				load_step++;
				break;
			case 1:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 2:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 3:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 4:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 5:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 6:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 7:
				init_All_Resource(gl,load_step);
				load_step++;
				break;
			case 8:
				init_All_Resource(gl,load_step);
				isLoadedOk=true;//������@�ſ��
				laodBack=null;//�P��
				processBar=null;//�P��
				break;
			}
		}
		
		//���J���z����k
		public void init_All_Resource(GL10 gl,int index)
		{
			switch(index)
			{
			case 1:  
				PicDataManager.loadPicData(TXZGameSurfaceView.this.getContext(), index);
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
	          break;
			case 2:
				PicDataManager.loadPicData(TXZGameSurfaceView.this.getContext(), index);
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 3:	
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 4:  
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 5:
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				break;
			case 6:
				VertexDataManager.initVertexData(TXZGameSurfaceView.this.getResources(), index);
				 //�إ߯��z�x�ι磌�� 
				 //�إ߯��z�x�ι磌�� 
				double dAngle=Math.PI/12/(num/4);//�C����15��
	            for(int i=0;i<num;i++)
	            {
	            	if(i<num/4)//�Ĥ@�B����
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*i);
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            	if(i<num/2&&i>=num/4)//�ĤG�B�k��^�з�
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*(num/4-(i-num/4)));
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            	if(i<num*3/4&&i>=num/2)//�ĤT�B�k��
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*(num/4-(i-num/4)));
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            	if(i<num&&i>=num*3/4)//�ĥ|������^�з�
	            	{
	            		VertexDataManager.initSoftBoxVertexData(dAngle*((i-num/4)-num*3/4));
	            		texRect[i]=new VertexTexture3DObjectForDraw
	            		(
	            				VertexDataManager.vertexPositionArray[26],
	            				VertexDataManager.vertexTextrueArray[26],
	            				VertexDataManager.vCount[26]
	            		);
	            	}
	            }
				break;
			case 7:	
				left=new VertexTexture3DObjectForDraw//�V�����������s
				(
						VertexDataManager.vertexPositionArray[8],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[8], //�ж����z�y��
						VertexDataManager.vCount[8] //���I��
				);
				
				yaogan1=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[28],
						VertexDataManager.vertexTextrueArray[28],
						VertexDataManager.vCount[28]
				);
				yaogan2=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[29],
						VertexDataManager.vertexTextrueArray[29],
						VertexDataManager.vCount[29]
				);
				yaogan1Id=initTexture(gl, PicDataManager.picDataArray[57]);
				yaogan2Id=initTexture(gl, PicDataManager.picDataArray[58]);
				
				
				boxId=initTexture(gl, PicDataManager.picDataArray[10]);//�c�l���zid
				
		
				
				convertId=initTexture(gl, PicDataManager.picDataArray[9]);//�����ഫ�������s���zid
				
				roomId = initTexture(gl, PicDataManager.picDataArray[0]);		//�ж����zid
				room=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[0],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[0], //�ж����z�y��
						VertexDataManager.vCount[0] //���I��
				);
				
				sky=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[32],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[32], //�ж����z�y��
						VertexDataManager.vCount[32] //���I��
				);
				
				wallId=initTexture(gl, PicDataManager.picDataArray[1]);		//�s�զ������zid
				wall=new VertexTextureNormal3DObjectForDraw//��
				(
	                VertexDataManager.vertexPositionArray[9],
					VertexDataManager.vertexTextrueArray[9],
					VertexDataManager.vertexNormalArray[9],
					VertexDataManager.vCount[9]
				);
				
				waterId = initTexture(gl, PicDataManager.picDataArray[2]);		//�������zid
				water=new VertexTexture3DObjectForDraw
				(
						VertexDataManager.vertexPositionArray[7],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[7], //�ж����z�y��
						VertexDataManager.vCount[7] //���I��
				);
				
				loseId=initTexture(gl, PicDataManager.picDataArray[16]);		//��I�����zid
				winId=initTexture(gl, PicDataManager.picDataArray[17]);		//Ĺ�I�����zid
				returnId=initTexture(gl, PicDataManager.picDataArray[19]);		//�������zid
				retryId=initTexture(gl, PicDataManager.picDataArray[18]);		//�Ǧ^���zid
				next_GuanId=initTexture(gl, PicDataManager.picDataArray[20]);		//�U�@�����zid
				
				bground=new VertexTexture3DObjectForDraw//��Ĺ�ɭ��I��
				(
						VertexDataManager.vertexPositionArray[14],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[14], //�ж����z�y��
						VertexDataManager.vCount[14] //���I��
				);
				
				bgbutton=new VertexTexture3DObjectForDraw//��Ĺ�ɭ����s
				(
						VertexDataManager.vertexPositionArray[15],//�Фl�����I�y�и��
						VertexDataManager.vertexTextrueArray[15], //�ж����z�y��
						VertexDataManager.vCount[15] //���I��
				);
				
				break;
			case 8: 
				targetId=initTexture(gl, PicDataManager.picDataArray[11]);//�ت��a���zid
				
				
				leftId=initTexture(gl, PicDataManager.picDataArray[4]);//�����H��L�������zid
				lovntArray[0]=new VertexTextureNormal3DObjectForDraw//����
				(
						VertexDataManager.vertexPositionArray[2],
						VertexDataManager.vertexTextrueArray[2],
						VertexDataManager.vertexNormalArray[2],
						VertexDataManager.vCount[2],
						leftId
				);
				headId=initTexture(gl, PicDataManager.picDataArray[3]);//�����H�Y���zid
				lovntArray[1]=new VertexTextureNormal3DObjectForDraw//�Y
				(
						VertexDataManager.vertexPositionArray[1],
						VertexDataManager.vertexTextrueArray[1],
						VertexDataManager.vertexNormalArray[1],
						VertexDataManager.vCount[1],
						headId
				);
				lovntArray[2]=new VertexTextureNormal3DObjectForDraw//���u�W
				(
						VertexDataManager.vertexPositionArray[3],
						VertexDataManager.vertexTextrueArray[3],
						VertexDataManager.vertexNormalArray[3],
						VertexDataManager.vCount[3],
						leftId
				);
				
				lovntArray[3]=new VertexTextureNormal3DObjectForDraw//���u�U
				(
						VertexDataManager.vertexPositionArray[4],
						VertexDataManager.vertexTextrueArray[4],
						VertexDataManager.vertexNormalArray[4],
						VertexDataManager.vCount[4],
						leftId
				);
				
				lovntArray[4]=new VertexTextureNormal3DObjectForDraw//�k�u�W
				(
						VertexDataManager.vertexPositionArray[5],
						VertexDataManager.vertexTextrueArray[5],
						VertexDataManager.vertexNormalArray[5],
						VertexDataManager.vCount[5],
						leftId
				);
				
				lovntArray[5]=new VertexTextureNormal3DObjectForDraw//�k�u�U
				(
						VertexDataManager.vertexPositionArray[6],
						VertexDataManager.vertexTextrueArray[6],
						VertexDataManager.vertexNormalArray[6],
						VertexDataManager.vCount[6],  
						leftId
				);
				robot=new Robot(lovntArray,TXZGameSurfaceView.this);  
	    		dat=new TXZDoActionThread(TXZGameSurfaceView.this);//�إ߰���ʧ@�����
	    		dat.start();//�Ұʰ����
	    		ygdat=new YGDoActionThread(TXZGameSurfaceView.this);
	    		ygdat.start();
	    		new Thread()
	    		{
	    			public void run()
	    			{
	    				isGoFlag=true;
	    				while(isGoFlag)
	    				{
	    					isGo=true;
	    					try 
	    		    		{
	    						Thread.sleep(700);
	    					} catch (InterruptedException e) 
	    					{
	    						e.printStackTrace();
	    					}
	    				}
	    			}
	    		}.start();
				break;
			} 
		}
		
		//ø����� �ɭ�
		public void drawGameView(GL10 gl)
		{
			gl.glMatrixMode(GL10.GL_PROJECTION);//�]�w�ثe�x�}����v�x�}
			gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
			visualAngle(gl,ratio);//�I�s����k�p�ⲣ�ͳz����v�x�}
			gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
			gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}	
			
			cameraPosition(gl);//��v���]�w
			//�Nø�����Ш�i�{���ܼ�
			synchronized(gdDraw.dataLock)	//��Wø����
			{
				gdDraw.copyTo(gdTemp);
			}
			
			synchronized(gqdDraw.gqdataLock)	//��Wø����
			{
				gqdTemp.boxCount=gqdDraw.boxCount;
				for(int i=0;i<gqdTemp.boxCount;i++)
				{
					gqdTemp.cdArray[i].row=gqdDraw.cdArray[i].row;
					gqdTemp.cdArray[i].col=gqdDraw.cdArray[i].col;
				}
				for(int i=0;i<gqdDraw.MAP[GameData.level-1].length;i++)
				{
					for(int j=0;j<gqdDraw.MAP[GameData.level-1][0].length;j++)
					{
						gqdTemp.MAP[GameData.level-1][i][j]=gqdMain.MAP[GameData.level-1][i][j];
					}
				}
				
			}

			
			//================ø��˼v=============================
			gl.glDisable(GL10.GL_CULL_FACE);//�����I���ŵ�
			
			for(int i=0;i<gqdMain.MAP[GameData.level-1].length;i++)
			{
				for(int j=0;j<gqdMain.MAP[GameData.level-1][0].length;j++)
            	{
        			float xOffset=GuanQiaData.XOffset[i][j];//��l�bX�b��V�������q
					float zOffset=GuanQiaData.ZOffset[i][j];//��l�bZ�b��V�������q
            		if(gqdTemp.MAP[GameData.level-1][i][j]==1||gqdTemp.MAP[GameData.level-1][i][j]==2||
            				gqdTemp.MAP[GameData.level-1][i][j]==4)//�Y�G�a�Ϥ������νc�l�ξ����H�A�hø���
            		{
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, -0.1f, zOffset);
            			wall.drawSelf(gl,wallId);//ø������˼v
            			gl.glPopMatrix();
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==3||gqdTemp.MAP[GameData.level-1][i][j]==6)//�Y�G�a�Ϥ����ت��a�ΤH�b�ت��a�A�hø��ت��a
            		{
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, -0.1f, zOffset);
            			wall.drawSelf(gl,targetId);//�ت��a�˼v
            			gl.glPopMatrix();
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==2)//�Y�G�a�Ϥ����c�l�A�hø��c�l
            		{
            			gl.glEnable(GL10.GL_BLEND);
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, 0.9f, zOffset);
            			if(i==GuanQiaData.move_row&&j==GuanQiaData.move_col)
            			{
            				gl.glTranslatef(GuanQiaData.xoffset, 0, GuanQiaData.zoffset);
            			}
            			texRect[currentFlagindex].drawSelf(gl,boxId);//ø��ثe��
            			gl.glPopMatrix();
            			gl.glDisable(GL10.GL_BLEND);
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==5)//�Y�G�O���n���c�l�A�ت��a�]�nø��
            		{
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, -0.1f, zOffset);
            			wall.drawSelf(gl,targetId);//�ت��a�˼v
            			gl.glPopMatrix();
            			gl.glPushMatrix();
            			gl.glScalef(1, -1, 1);
            			gl.glTranslatef(xOffset, 0.9f, zOffset);
            			wall.drawSelf(gl,targetId);//ø����n���c�l���˼v
            			gl.glPopMatrix();
            		}
            	}
            }
			
            gl.glPushMatrix();
			gl.glScalef(1, -1, 1);
			gl.glTranslatef(0, 0.1f, 0);
    		robot.drawSelf(gl);//ø������H
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glScalef(1, -1, 1);
			gl.glTranslatef(0, -0.4f, 0);
			room.drawSelf(gl, roomId);//ø��ж����˼v
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glScalef(1, -1, 1);
			gl.glTranslatef(0, -0.4f, 0);
			gl.glRotatef(skyAngle, 0, 1, 0);
			sky.drawSelf(gl, roomId);//ø��ѪŪ��˼v
			gl.glPopMatrix();
			
			gl.glEnable(GL10.GL_CULL_FACE);//�}�ҭI���ŵ�
			
            gl.glEnable(GL10.GL_BLEND); //�}�ҲV�X   
            //�]�w���V�X�]�l�P�ت��V�X�]�l
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			water.drawSelf(gl, waterId);
			gl.glDisable(GL10.GL_BLEND);
			//================ø��˼v=============================
			
			//===============ø�����===============================
			gl.glPushMatrix();
			//�e�\����       
	        gl.glEnable(GL10.GL_LIGHTING);
    		gl.glTranslatef(0, GameStaticData.FLOOR_Y+0.8f, 0);
    		robot.drawSelf(gl);//ø������H
    		//�T�����       
	        gl.glDisable(GL10.GL_LIGHTING);
            gl.glPopMatrix();
	        
            
			room.drawSelf(gl, roomId);//ø��ж�
			
			gl.glPushMatrix();
			gl.glRotatef(skyAngle, 0, 1, 0);//ø��Ѫ�
			sky.drawSelf(gl, roomId);
			gl.glPopMatrix();
			
			
			for(int i=0;i<gqdMain.MAP[GameData.level-1].length;i++)
			{
				for(int j=0;j<gqdMain.MAP[GameData.level-1][0].length;j++)
            	{
        			float xOffset=GuanQiaData.XOffset[i][j];//��l�bX�b��V�������q
					float zOffset=GuanQiaData.ZOffset[i][j];//��l�bZ�b��V�������q
            		if(gqdTemp.MAP[GameData.level-1][i][j]==1||gqdTemp.MAP[GameData.level-1][i][j]==2||
            				gqdTemp.MAP[GameData.level-1][i][j]==4)
            		{//�Y�G�a�Ϥ������νc�l�ξ����H�A�hø���
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y, zOffset);
            			wall.drawSelf(gl,wallId);//ø���
            			gl.glPopMatrix();
            		}
            		if(gqdTemp.MAP[GameData.level-1][i][j]==3||gqdTemp.MAP[GameData.level-1][i][j]==6)//�Y�G�a�Ϥ����ت��a�ΤH�b���ت��a�A�hø��ت��a
            		{
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y, zOffset);
            			wall.drawSelf(gl,targetId);//�ت��a
            			gl.glPopMatrix();
            		}
            		
            		if(gqdTemp.MAP[GameData.level-1][i][j]==5)//�Y�G�O���n���c�l�A�ت��a�]�nø��
            		{
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y, zOffset);
            			wall.drawSelf(gl,targetId);//�ت��a�˼v
            			gl.glPopMatrix();
            			gl.glPushMatrix();
            			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y+1f, zOffset);
            			wall.drawSelf(gl,targetId);//ø����n���c�l���˼v
            			gl.glPopMatrix();
            		}
            	}
            }
			
			for(int i=0;i<gqdTemp.boxCount;i++)//ø��c�l
			{
				float xOffset=GuanQiaData.XOffset[gqdTemp.cdArray[i].row][gqdTemp.cdArray[i].col];//��l�bX�b��V�������q
				float zOffset=GuanQiaData.ZOffset[gqdTemp.cdArray[i].row][gqdTemp.cdArray[i].col];//��l�bZ�b��V�������q
				gl.glEnable(GL10.GL_BLEND);
    			gl.glPushMatrix();
    			gl.glTranslatef(xOffset, GameStaticData.FLOOR_Y+1f, zOffset);
    			if(gqdTemp.cdArray[i].row==GuanQiaData.move_row&&gqdTemp.cdArray[i].col==GuanQiaData.move_col)
    			{
    				gl.glTranslatef(GuanQiaData.xoffset, 0, GuanQiaData.zoffset);
    			}
    			if(gqdTemp.cdArray[i].row!=0&&gqdTemp.cdArray[i].col!=0)
    			{
    				texRect[currentFlagindex].drawSelf(gl,boxId);//ø��ثe��
    			}
    			
    			gl.glPopMatrix();
    			gl.glDisable(GL10.GL_BLEND);
			}
			//===============ø�����===============================
			
			//===============ø��������s=============================
			 gl.glEnable(GL10.GL_BLEND);//�}�ҲV�X
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
	         
	         //������
	         gl.glPushMatrix();
	         gl.glTranslatef(1.35f, -0.6f, -0.1f);
	         gl.glTranslatef(offsetx*0.17f, offsety*0.17f, 0f);
	         yaogan2.drawSelf(gl,yaogan2Id);
	         gl.glPopMatrix();
	         
	         //�n��I��
	         gl.glPushMatrix();
	         gl.glTranslatef(1.35f, -0.6f, 0f);
	         yaogan1.drawSelf(gl, yaogan1Id);
	         gl.glPopMatrix();
	         
	         gl.glPushMatrix();
	         gl.glTranslatef(1.64f, 0.86f, 0f);
	         left.drawSelf(gl, convertId);//ø����ഫ���������s
	         gl.glPopMatrix();
	         
	         gl.glDisable(GL10.GL_BLEND);//�����V�X
	       //===============ø��������s=============================
	         
	       //=====================�P�_�����O�_����================
	            judgeGoToLastViewOrGoToNext(gl);
	       //=====================�P�_�����O�_����================
		}
		
		//���������A�P�_�_�O�i�J�U�@������
		public void judgeGoToLastViewOrGoToNext(GL10 gl)
		{
			int row=0;
	        int col=0;
	        int targetCount=0;
	        int boxWaterleft=0;
	        int boxWaterright=0;
	        int boxWatertop=0;
	        int boxWaterdown=0;
	        int boxWaterCount=0;
	        row=gqdTemp.MAP[GameData.level-1].length;
			col=gqdTemp.MAP[GameData.level-1][0].length;
			for(int i=0;i<row;i++)
			{
				for(int j=0;j<col;j++)
				{
					if(gqdTemp.MAP[GameData.level-1][i][j]==2)//�S�����n���c�l�ƶq
					{
						targetCount++;
					}
				}
			}
			if(targetCount==0)//��������,�Y�ت��I���ƶq��0
			{
				int currLevel=0;
				gdMain.winFlag=true;
				gdMain.loseFlag=false;
				gdMain.gameFinish=false;
				isWinOrLose=true;
				currLevel=activity.sharedUtil.getPassNum();
	          	if(GameData.level>=currLevel&&temp)
	          	{
	          		activity.sharedUtil.putPassNum(currLevel+1);
	          		temp=false;
	          	} 
				dat.workFlag=false;
				ygdat.workFlag=false;
				isGoFlag=false;
				if(isDrawWinOrLose&&!isInAnimation)
				{
					drawWinOrLose(gl);
				}
			}
			else if(targetCount!=0)//�O�_�S�����X�檺�c�l������ʡA�Y������ʫh���a��F��������
			{
				for(int i=0;i<row;i++)
				{
					for(int j=0;j<col;j++)
					{
						if(gqdTemp.MAP[GameData.level-1][i][j]==2)//�ت��ƶq
						{
							if(gqdTemp.MAP[GameData.level-1][i-1][j]==0)
							{
								boxWatertop=1;
							}
							if(gqdTemp.MAP[GameData.level-1][i+1][j]==0)
							{
								boxWaterdown=1;
							}
							if(gqdTemp.MAP[GameData.level-1][i][j-1]==0)
							{
								boxWaterleft=1;
							}
							if(gqdTemp.MAP[GameData.level-1][i][j+1]==0)
							{
								boxWaterright=1;
							}
							
							if((boxWatertop==1&&boxWaterleft==1)||(boxWaterleft==1&&boxWaterdown==1)||(boxWaterdown==1&&boxWaterright==1)||
									(boxWaterright==1&&boxWatertop==1))
							{
								boxWaterCount++;
								boxWatertop=0;
								boxWaterdown=0;
								boxWaterleft=0;
								boxWaterright=0;
								
							}
						}
					}
				}
				
//				for(int i=0;i<targetCount;i++)
//				{
//					if((boxWatertop==1&&boxWaterleft==1)||(boxWaterleft==1&&boxWaterdown==1)||(boxWaterdown==1&&boxWaterright==1)||
//							(boxWaterright==1&&boxWatertop==1))
//					{
//						boxWaterCount++;
//					}
//				}
				if(boxWaterCount==targetCount)
				{
					System.out.println(boxWaterCount+"  ========  "+targetCount);
					gdMain.winFlag=false;
					gdMain.loseFlag=true;
					gdMain.gameFinish=true;
					isWinOrLose=true;
					dat.workFlag=false;
					ygdat.workFlag=false;
					isGoFlag=false;
					Constant.IS_DRAW_WIN=true;
					if(isDrawWinOrLose&&!isInAnimation)
					{
						drawWinOrLose(gl);
					}
				}
				
			}
		}
		
		public void drawWinOrLose(GL10 gl)
		{
			
			 if(Constant.IS_DRAW_WIN)
			 {
				 gl.glEnable(GL10.GL_BLEND);//�����V�X
				 if(gdMain.loseFlag&&gdMain.gameFinish)
		         {
		             	gl.glPushMatrix();
		         		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		         		gl.glTranslatef(0f,0f, 0.1f);
		             	bground.drawSelf(gl,loseId);
		             	gl.glPopMatrix();
		         }
		         if(gdMain.winFlag)
		         {
		             	gl.glPushMatrix();
		         		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		         		gl.glTranslatef(0f,0f, 0.1f);
		             	bground.drawSelf(gl,winId);
		             	gl.glPopMatrix();
		         }
		         if(gdMain.loseFlag)
		         {
		             	gl.glPushMatrix();
		             	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		             	gl.glTranslatef(-0.45f,-0.2f, 0.15f);
		             	bgbutton.drawSelf(gl,returnId);//�Ǧ^���s
		             	gl.glPopMatrix();
		             	
		             	gl.glPushMatrix();
		             	gl.glTranslatef(0.45f,-0.2f, 0.15f);
		             	bgbutton.drawSelf(gl,retryId);//�������s
		             	gl.glPopMatrix();
		         }
		         if(gdMain.winFlag)
		         {
		        	 gl.glPushMatrix();
		             gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		             gl.glTranslatef(-0.45f,-0.2f, 0.15f);
		             bgbutton.drawSelf(gl,returnId);//�Ǧ^���s
		             gl.glPopMatrix();
		             
		             gl.glPushMatrix();
		             gl.glTranslatef(0.45f,-0.2f, 0.15f);
		             bgbutton.drawSelf(gl,next_GuanId);//�U�@�����s
		             gl.glPopMatrix();
		         }
				 gl.glDisable(GL10.GL_BLEND);//�����V�X
			 }
		}
		
		//�����ܤƫ�A�Ĥ@���שM�ĤT���ת����פj�p
		public void visualAngle(GL10 gl, float ratio)
		{
			if(viewFlag)//�Ĥ@����
	    	{
				gl.glFrustumf(-ratio*0.3f, ratio*0.3f, bottom*0.3f, top*0.3f, near*0.3f, far*0.3f);
	        	
	    	}else//�ĤT����
	    	{
	    		//�I�s����k�p�ⲣ�ͳz����v�x�}
	            gl.glFrustumf(-ratio, ratio, bottom, top, near, far);//�I�s����k�p�ⲣ�ͳz����v�x�}
	    	}
		}
		
		//�����ܤƫ�A�Ĥ@���שM�ĤT���סA��v������m
		public void cameraPosition(GL10 gl)
		{
			if(viewFlag)//�Ĥ@���סA�û���b�����H����W��
	        {
				if(isFirst)
				{
					currX=robot.positionx;
					currZ=robot.positionz;
					isFirst=!isFirst;
				}
				
	        	  GLU.gluLookAt
		          ( 
		        		  gl,
		                  (float) (currX+1f*Math.sin(Math.toRadians(currDegreeView+180))),//��v������m
		                   1.5f+2.7f,
		                  (float) (currZ+1f*Math.cos(Math.toRadians(currDegreeView+180))), //�����H�����
		                  
		                  (float) (currX+0.5f*Math.sin(Math.toRadians(currDegreeView+180))),//��v���ݦV����m
		                  1.5f+2f, 
		                  (float) (currZ+0.5f*Math.cos(Math.toRadians(currDegreeView+180))),
		                  
		                  0,1,0
		           );
	        	  synchronized(gqdDraw.gqdataLock)//��ø��
		           {
					   gqdDraw.ComparableByDis(
		        			  (float) (currX+1f*Math.sin(Math.toRadians(currDegreeView+180))), 
		        			  1.5f+2.7f, 
		        			  (float) (currZ+1f*Math.cos(Math.toRadians(currDegreeView+180))),
		        			  gqdMain.MAP[GameData.level-1]);//�Ƨ�
		           }
	        	
	        }else//�ĤT����
	        {	
	        	
	        	//�Nø�����Ш�i�{���ܼ�
				synchronized(gdDraw.dataLock)	//��Wø����
				{
					//�N��v��9�Ѽ�copy�i�{���ܼ�
					gdTemp.cx=gdDraw.cx;//��v����m
					gdTemp.cy=gdDraw.cy;
					gdTemp.cz=gdDraw.cz;
					gdTemp.tx=gdDraw.tx;//�[���I��m
					gdTemp.ty=gdDraw.ty;
					gdTemp.tz=gdDraw.tz;
					gdTemp.ux=gdDraw.ux;//up�V�q
					gdTemp.uy=gdDraw.uy;
					gdTemp.uz=gdDraw.uz;
				}
	        	GLU.gluLookAt//�`��v���]�w
				(
					gl, 
					gdTemp.cx,gdTemp.cy,gdTemp.cz,//��v����m
					gdTemp.tx,gdTemp.ty,gdTemp.tz,//�[���I��m
					gdTemp.ux,gdTemp.uy,gdTemp.uz//UP�V�q
				);
	        	synchronized(gqdDraw.gqdataLock)//��D��
		        {
					   gqdDraw.ComparableByDis(
							   gdMain.cx, 
							   gdMain.cy, 
							   gdMain.cz,
							   gqdMain.MAP[GameData.level-1]);//�Ƨ�
		        }
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
}

package com.f.view; 

import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;
import static com.f.pingpong.Constant.DifficultyContorl.DIFFICULTY;
import static com.f.pingpong.Constant.DifficultyContorl.selectCountries;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.f.dynamic.BallForContorl;
import com.f.gamebody.GameBall;
import com.f.gamebody.GameBatContorl;
import com.f.gamebody.GameFlyFlag;
import com.f.gamebody.GameRoom;
import com.f.gamebody.GameTable;
import com.f.gamebody.base.BatNetRect;
import com.f.mainbody.MainMenuRect;
import com.f.pingpong.Constant;
import com.f.pingpong.MainActivity;
import com.f.util.From2DTo3DUtil;
import com.f.util.MatrixState;
import com.f.video.FrameData;
import com.f.video.PlayVideoThread;

public class GameSurfaceView extends GLSurfaceView{

	private int textureid_ball;
	private int textureid_wall;
	private int textureid_floor;
	private int textureid_floor_hadow;
	private int textureid_racketAi;
	private int textureid_spring;
	private int textureid_surface;
	private int textureid_loading;
	private int textureid_progress;
	
	private int textureid_net;
	private int textureid_racket;
	
	private MainActivity ma          = null;

	private GameRoom room 		     = null;
	private GameBall ball 			 = null;
	private GameTable tabletennis 	 = null;
	private GameFlyFlag flyflag 	 = null;
	private GameBatContorl rackets 	 = null;
	private GameBatContorl racketsAi = null;
	
	private BatNetRect loading  = null;
	private BatNetRect process  = null;

	public BallForContorl  bfc  = null;
	public PlayVideoThread pvt  = null;

	
	
	public Vector2f mPreviPoint = new Vector2f();   //x,y��V�W�����y��
	public Vector2f mTouchPoint = new Vector2f();	//x,y��VĲ�����y��

    //����O���A
	public static int state     = 0;
	//���J�귽���B��
	private int load_step       = 0;
	//���J��
	private boolean LOAD_OVER   = false;
	//��Ĺ�ЧӦ�
	private boolean win_flag 	= false;
	private boolean lose_flag	= false;
	//�}�l�ɶ��W
	private long startTime      = 0L;
	
	private Resources res       = null;
	
	public GameSurfaceView(Context context) {
		super(context);
		
		ma = (MainActivity)context;
		res = ma.getResources();
		setEGLContextClientVersion(2); 
		setRenderer(render);				  
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		selectCountries(DIFFICULTY);
        
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		switch (e.getAction()) 
		{
			case MotionEvent.ACTION_DOWN:
				switch(state)
				{
				case 1://��l��
					if(x > GAMEVIEW_TOUCH_AREA[0][0] && x < GAMEVIEW_TOUCH_AREA[0][0] + GAMEVIEW_TOUCH_AREA[0][2]
					&& y > GAMEVIEW_TOUCH_AREA[0][1] && y < GAMEVIEW_TOUCH_AREA[0][1] + GAMEVIEW_TOUCH_AREA[0][3])
					{
						System.out.println("���U���O�Ȱ���");
						PAUSE = true;
						state = 2;
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					break;
				case 2://�Ȱ����
					if(x > GAMEVIEW_TOUCH_AREA[1][0] && x < GAMEVIEW_TOUCH_AREA[1][0] + GAMEVIEW_TOUCH_AREA[1][2]
					&& y > GAMEVIEW_TOUCH_AREA[1][1] && y < GAMEVIEW_TOUCH_AREA[1][1] + GAMEVIEW_TOUCH_AREA[1][3])
					{
						System.out.println("���U���O������");
						IS_GAME_OVER = true;
						ma.hd.sendEmptyMessage(0);
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					else if(x > GAMEVIEW_TOUCH_AREA[2][0] && x < GAMEVIEW_TOUCH_AREA[2][0] + GAMEVIEW_TOUCH_AREA[2][2]
					     && y > GAMEVIEW_TOUCH_AREA[2][1] && y < GAMEVIEW_TOUCH_AREA[2][1] + GAMEVIEW_TOUCH_AREA[2][3])
					{
						System.out.println("���U���O�~����");
						PAUSE = false;
						state = 1;
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					else if(x > GAMEVIEW_TOUCH_AREA[3][0] && x < GAMEVIEW_TOUCH_AREA[3][0] + GAMEVIEW_TOUCH_AREA[3][2]
					     && y > GAMEVIEW_TOUCH_AREA[3][1] && y < GAMEVIEW_TOUCH_AREA[3][1] + GAMEVIEW_TOUCH_AREA[3][3])
					{
						System.out.println("���U���O�D�����");
						IS_GAME_OVER = true;
						ma.hd.sendEmptyMessage(1);
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					break;
				case 5:
					if(x > GAMEVIEW_TOUCH_AREA[1][0] && x < GAMEVIEW_TOUCH_AREA[1][0] + GAMEVIEW_TOUCH_AREA[1][2]
   					&& y > GAMEVIEW_TOUCH_AREA[1][1] && y < GAMEVIEW_TOUCH_AREA[1][1] + GAMEVIEW_TOUCH_AREA[1][3])
   					{
   						System.out.println("���U���O������");
   						IS_GAME_OVER = true;
   						ma.hd.sendEmptyMessage(0);
   						ma.soundandshakeutil.playSoundL(5, 0);
   					}
   					else if(x > GAMEVIEW_TOUCH_AREA[2][0] && x < GAMEVIEW_TOUCH_AREA[2][0] + GAMEVIEW_TOUCH_AREA[2][2]
   					     && y > GAMEVIEW_TOUCH_AREA[2][1] && y < GAMEVIEW_TOUCH_AREA[2][1] + GAMEVIEW_TOUCH_AREA[2][3])
   					{
   						System.out.println("���U���O�U�@����");
   						IS_GAME_OVER = true;
   						DIFFICULTY = DIFFICULTY + 1;
   						ma.hd.sendEmptyMessage(0);
   						ma.soundandshakeutil.playSoundL(5, 0);
   					}
   					else if(x > GAMEVIEW_TOUCH_AREA[3][0] && x < GAMEVIEW_TOUCH_AREA[3][0] + GAMEVIEW_TOUCH_AREA[3][2]
   					     && y > GAMEVIEW_TOUCH_AREA[3][1] && y < GAMEVIEW_TOUCH_AREA[3][1] + GAMEVIEW_TOUCH_AREA[3][3])
   					{
   						System.out.println("���U���O�D�����");
   						IS_GAME_OVER = true;
   						ma.hd.sendEmptyMessage(1);
   						ma.soundandshakeutil.playSoundL(5, 0);
   					}
					break;
				case 6:
					if(x > GAMEVIEW_TOUCH_AREA[4][0] && x < GAMEVIEW_TOUCH_AREA[4][0] + GAMEVIEW_TOUCH_AREA[4][2]
  					&& y > GAMEVIEW_TOUCH_AREA[4][1] && y < GAMEVIEW_TOUCH_AREA[4][1] + GAMEVIEW_TOUCH_AREA[4][3])
  					{
  						System.out.println("���U���O������");
  						IS_GAME_OVER = true;
  						ma.hd.sendEmptyMessage(0);
  						ma.soundandshakeutil.playSoundL(5, 0);
  					}
  					else if(x > GAMEVIEW_TOUCH_AREA[5][0] && x < GAMEVIEW_TOUCH_AREA[5][0] + GAMEVIEW_TOUCH_AREA[5][2]
  					     && y > GAMEVIEW_TOUCH_AREA[5][1] && y < GAMEVIEW_TOUCH_AREA[5][1] + GAMEVIEW_TOUCH_AREA[5][3])
  					{
  						System.out.println("���U���O�D�����");
  						IS_GAME_OVER = true;
   						ma.hd.sendEmptyMessage(1);
   						ma.soundandshakeutil.playSoundL(5, 0);
  					}
					break;
				case 7:
					if(x > GAMEVIEW_TOUCH_AREA[4][0] && x < GAMEVIEW_TOUCH_AREA[4][0] + GAMEVIEW_TOUCH_AREA[4][2]
					&& y > GAMEVIEW_TOUCH_AREA[4][1] && y < GAMEVIEW_TOUCH_AREA[4][1] + GAMEVIEW_TOUCH_AREA[4][3])
					{
						System.out.println("���U���O������");
						IS_HELP = false;
						ma.hd.sendEmptyMessage(2);
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					else if(x > GAMEVIEW_TOUCH_AREA[5][0] && x < GAMEVIEW_TOUCH_AREA[5][0] + GAMEVIEW_TOUCH_AREA[5][2]
					     && y > GAMEVIEW_TOUCH_AREA[5][1] && y < GAMEVIEW_TOUCH_AREA[5][1] + GAMEVIEW_TOUCH_AREA[5][3])
					{
						System.out.println("���U���O������");
						FrameData.playFrameDataList.clear();
						state = rackets.points > racketsAi.points ? 5 : 6;
						if(DIFFICULTY == 5)
						{
							state = 6;
						}
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					break;
				case 8://����v�����Ȱ�
					if(x > GAMEVIEW_TOUCH_AREA[0][0] && x < GAMEVIEW_TOUCH_AREA[0][0] + GAMEVIEW_TOUCH_AREA[0][2]
					&& y > GAMEVIEW_TOUCH_AREA[0][1] && y < GAMEVIEW_TOUCH_AREA[0][1] + GAMEVIEW_TOUCH_AREA[0][3])
					{
						System.out.println("���U���O����v���Ȱ���");
						pvt.pauseTime = System.nanoTime();  
						HELP_PAUSE = true;
						state = IS_HELP ? 9 : 10;
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					break; 
				case 9://���U���Ȱ����  
					if(x > GAMEVIEW_TOUCH_AREA[4][0] && x < GAMEVIEW_TOUCH_AREA[4][0] + GAMEVIEW_TOUCH_AREA[4][2]
					&& y > GAMEVIEW_TOUCH_AREA[4][1] && y < GAMEVIEW_TOUCH_AREA[4][1] + GAMEVIEW_TOUCH_AREA[4][3])
					{
						System.out.println("���U���O�~����");
						pvt.startTime += System.nanoTime() - pvt.pauseTime;
						HELP_PAUSE = false;
						state = 8;
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					else if(x > GAMEVIEW_TOUCH_AREA[5][0] && x < GAMEVIEW_TOUCH_AREA[5][0] + GAMEVIEW_TOUCH_AREA[5][2]
					     && y > GAMEVIEW_TOUCH_AREA[5][1] && y < GAMEVIEW_TOUCH_AREA[5][1] + GAMEVIEW_TOUCH_AREA[5][3])
					{
						System.out.println("���U���O�D�����");
						HELP_PAUSE = false;
						pvt.wFlag = false;
						ma.hd.sendEmptyMessage(1);
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					break;
				case 10://�v�������Ȱ����
					if(x > GAMEVIEW_TOUCH_AREA[1][0] && x < GAMEVIEW_TOUCH_AREA[1][0] + GAMEVIEW_TOUCH_AREA[1][2]
					&& y > GAMEVIEW_TOUCH_AREA[1][1] && y < GAMEVIEW_TOUCH_AREA[1][1] + GAMEVIEW_TOUCH_AREA[1][3])
					{
						System.out.println("���U���O������");
						HELP_PAUSE = false;
						pvt.wFlag = false;
						ma.hd.sendEmptyMessage(2);
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					else if(x > GAMEVIEW_TOUCH_AREA[2][0] && x < GAMEVIEW_TOUCH_AREA[2][0] + GAMEVIEW_TOUCH_AREA[2][2]
					     && y > GAMEVIEW_TOUCH_AREA[2][1] && y < GAMEVIEW_TOUCH_AREA[2][1] + GAMEVIEW_TOUCH_AREA[2][3])
					{
						System.out.println("���U���O�~����");
						pvt.startTime += System.nanoTime() - pvt.pauseTime;
						HELP_PAUSE = false;
						state = 8;
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					else if(x > GAMEVIEW_TOUCH_AREA[3][0] && x < GAMEVIEW_TOUCH_AREA[3][0] + GAMEVIEW_TOUCH_AREA[3][2]
					     && y > GAMEVIEW_TOUCH_AREA[3][1] && y < GAMEVIEW_TOUCH_AREA[3][1] + GAMEVIEW_TOUCH_AREA[3][3])
					{
						System.out.println("���U���O�D�����");
						HELP_PAUSE = false;
						pvt.wFlag = false;
						ma.hd.sendEmptyMessage(1);
						ma.soundandshakeutil.playSoundL(5, 0);
					}
					break;
				case 11:
					if(x > GAMEVIEW_TOUCH_AREA[4][0] && x < GAMEVIEW_TOUCH_AREA[4][0] + GAMEVIEW_TOUCH_AREA[4][2]
  					&& y > GAMEVIEW_TOUCH_AREA[4][1] && y < GAMEVIEW_TOUCH_AREA[4][1] + GAMEVIEW_TOUCH_AREA[4][3])
  					{
  						System.out.println("���U���O������");
  						HELP_PAUSE = false;
						pvt.wFlag = false;
						ma.hd.sendEmptyMessage(2);
  						ma.soundandshakeutil.playSoundL(5, 0);
  					}
  					else if(x > GAMEVIEW_TOUCH_AREA[5][0] && x < GAMEVIEW_TOUCH_AREA[5][0] + GAMEVIEW_TOUCH_AREA[5][2]
  					     && y > GAMEVIEW_TOUCH_AREA[5][1] && y < GAMEVIEW_TOUCH_AREA[5][1] + GAMEVIEW_TOUCH_AREA[5][3])
  					{
  						System.out.println("���U���O�D�����");
  						HELP_PAUSE = false;
						pvt.wFlag = false;
   						ma.hd.sendEmptyMessage(1);
   						ma.soundandshakeutil.playSoundL(5, 0);
  					}
					break;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(state == 1)//�Y�G�b��l�ɭ�
				{
					//�Y�G����v���A�h���B�zĲ��
					if(IS_PLAY_VIDEO){ 
						return false;
					}else{
						synchronized (Constant.videoLock) {
							mTouchPoint.set(x, y);
						}
						bfc.setNewPoint(mTouchPoint);
					}
				}
				break;
		}
		mPreviPoint.set(x, y);
		return true;
	}

	private GLSurfaceView.Renderer render = new Renderer() 
	{
		private MainMenuRect gr1;
		private MainMenuRect gr2;
		private MainMenuRect gr3;
		private MainMenuRect gr4;
		private MainMenuRect gr5;
		private MainMenuRect gr6;
		private MainMenuRect gr7;
		
		private int textureid_helphand;
		private boolean isFirstFrame = true;
		private MainMenuRect helphandrec;
		
		private float helpsmY = GAMEVIEW_PIC_LS[16][1];
	
		
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) 
		{
			GLES20.glClearColor(0.0f,0.0f,0.0f, 1.0f);  
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            MatrixState.setInitStack();  
            
        	textureid_loading = initTexture(res,"loading.png");
        	textureid_progress = initTexture(res,"process.png"); 
        	
        	loading = new BatNetRect(res,vCount[1][2],vertexBuffer[1][2],texCoorBuffer[1][0] ,normalBuffer[1][0]);
        	process = new BatNetRect(res,vCount[1][3],vertexBuffer[1][3],texCoorBuffer[1][0] ,normalBuffer[1][0]);
            
            IS_GAME_OVER = false;
            
		}
		
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) 
		{
			//�]�w�����j�p�Φ�m 
			GLES20.glViewport(ssr.lucX,ssr.lucY, (int)(SCREEN_WIDTH_STANDARD*ssr.ratio), (int)(SCREEN_HEIGHT_STANDARD*ssr.ratio));
		}
		
		@Override
		public void onDrawFrame(GL10 gl)
		{
			  //�M���`�׽w�R�P�m��w�R
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			if(!LOAD_OVER)
			{
				drawOrthLoadingView();
			}
			else
			{
				drawPerspective();
				drawMenu();
			}
		}
		
		private void drawOrthLoadingView() 
		{
			if (isFirstFrame) { // �Y�G�O�Ĥ@��
				MatrixState.pushMatrix();
				MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);// �]�w�����v
				MatrixState.setCamera(0, 0, 1, 0, 0, -1, 0, 1, 0);// �]�w��v��
				loading.drawSelf(textureid_loading, 0);
				MatrixState.popMatrix();
				isFirstFrame = false;
			} else {// �o�̶i��귽�����J
				MatrixState.pushMatrix();
				MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);// �]�w�����v
				MatrixState.setCamera(0, 0, 1, 0, 0, -1, 0, 1, 0);// �]�w��v��
				MatrixState.pushMatrix(); // �]�w�i�׫��ܾ�
				MatrixState.translate(-3 + 2 * load_step / (float) 6, -1f, 0f);
				process.drawSelf(textureid_progress, 0);
				MatrixState.popMatrix();
				loading.drawSelf(textureid_loading, 0);//ø��I����
				MatrixState.popMatrix();
				loadResource();// ���J�귽����k
				return;
			}
		}

		private void loadResource() {
			switch (load_step) {
			case 0:
				init_All_Texture(load_step);
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
				init_All_Object(load_step);
				load_step++;
				break;

			case 4:
				init_All_Object(load_step);
				load_step++;
				break;

			case 5:
				init_All_Object(load_step);
				load_step++;
				break;

			case 6:
				init_All_Object(load_step);
				loading = null;
				process = null;
				startTime = System.nanoTime();
				//�з����}�ҹ�������
				if(!IS_PLAY_VIDEO){
					FrameData.playFrameDataList.clear();
					bfc = new BallForContorl(ma, ball,rackets,racketsAi);
					bfc.start();
					state=1;
				}else{
				//�Y�G����v���}��������
					pvt = new PlayVideoThread();
					pvt.start(); 
					state = 8; 
					FrameData.currIndex = 0;
				}
				LOAD_OVER = true;
				IS_FIRSTLOADING_GAME = false;
				break;
			}
		}

		private void init_All_Texture(int step)
		{
			switch (step) {
			case 0:
				if(IS_FIRSTLOADING_GAME)
	            {
					loadGamePicByByte(res);
	            }
	            break;
			case 1:
				loadGameTextureId();
				loadBatsTextureId();
				loadWallTextureId();
				break;
			case 2:
				scompileGameShaderReal();
				textureid_net = BATS_TEXTURE_ID[4];
				textureid_ball = OBJ_TEXTURE_ID[5];
		        textureid_surface = (Math.random() > 0.40) ? OBJ_TEXTURE_ID[7] : OBJ_TEXTURE_ID[8];
		        textureid_spring = OBJ_TEXTURE_ID[6];
		        textureid_racketAi = OBJ_TEXTURE_ID[0]; 
		        textureid_wall = WALL_TEXTURE_ID[DIFFICULTY];
		        textureid_helphand = BATS_TEXTURE_ID[5];
	            textureid_floor = OBJ_TEXTURE_ID[2];
	            textureid_floor_hadow = OBJ_TEXTURE_ID[4];
				break;
			}
		}

		private void init_All_Object(int step) {
			switch (step) {
			case 3:
				room = new GameRoom();
	            ball = new GameBall();
				rackets = new GameBatContorl(new Vector3f(0.0f,0.6f, TABLE_Z_MAX - 0.1f), 0);// 0�N��H�u
				break;
			case 4:
				flyflag = new GameFlyFlag(1);//�ƴ����X�m
				racketsAi = new GameBatContorl(new Vector3f(0.0f, 0.6f, AI_Z), 1);// 1�N��AI
				tabletennis = new GameTable();
				helphandrec = new MainMenuRect(res, 128, 128);
				break;
			case 5:
				gr1 = new MainMenuRect(vertexBuffer[1][8],texCoorBuffer[0][0]);
				gr2 = new MainMenuRect(vertexBuffer[1][9],texCoorBuffer[0][0]);
				gr3 = new MainMenuRect(vertexBuffer[1][10],texCoorBuffer[0][0]);
				break;
			case 6: 
				gr4 = new MainMenuRect(vertexBuffer[1][11],texCoorBuffer[0][0]);
				gr5 = new MainMenuRect(vertexBuffer[1][12],texCoorBuffer[0][0]);
				gr6 = new MainMenuRect(vertexBuffer[1][13],texCoorBuffer[0][0]);
				gr7 = new MainMenuRect(vertexBuffer[1][14],texCoorBuffer[0][0]);
				break;
			}
		}
		
		//----------------�v�����-------------------------------------//
		float cxCurr,cyCurr;
	    Vector3f positionPlayerCurr=new Vector3f();
	    Vector3f positionAiCurr=new Vector3f();
	    Vector3f positionBallCurr=new Vector3f();
	    int[] task;
	    float zAngleCurr;//�y����ਤ��
	    float zAngleCurrAI;//���z�y����ਤ��
	    List<Vector3f> lListCurr = new ArrayList<Vector3f>();//��m(Location)�M��
	    boolean isPaddleCurr;
	    int pointsPlayerCurr;
	    int pointsAiCurr;
	    float txCurr = 400 ,tyCurr = 200;
	    boolean isshootman;
	    //----------------�v�����-------------------------------------//
	    private void drawPerspective()
		{		   
			synchronized(Constant.videoLock)
			{
				if(!Constant.IS_PLAY_VIDEO)
				{
					cxCurr=CAMERAMOVE.x;
					cyCurr=CAMERAMOVE.y;
					positionPlayerCurr.set(rackets.position.x, rackets.position.y, rackets.position.z);
					positionAiCurr.set(racketsAi.position.x, racketsAi.position.y, racketsAi.position.z);
					positionBallCurr.set(ball.position.x, ball.position.y, ball.position.z);
					task=ma.soundandshakeutil.getTask();
					zAngleCurr = rackets.zAngle;
					zAngleCurrAI = racketsAi.zAngle;
					lListCurr = new ArrayList<Vector3f>();
					FrameData.copyVecList(lListCurr, ball.lList);
					isPaddleCurr = IS_PADDLE;
					pointsPlayerCurr = rackets.points;
					pointsAiCurr = racketsAi.points;
					txCurr = mTouchPoint.x; 
					tyCurr = mTouchPoint.y;
					isshootman = IS_SHOOT_MAN;
				}
				else
				{
					FrameData cfd;
					if(IS_HELP)
					{
						cfd=FrameData.helpFrameDataList.get(FrameData.currIndex);
					}
					else
					{
						cfd=FrameData.playFrameDataList.get(FrameData.currIndex);
					}
					cxCurr=cfd.cx;
					cyCurr=cfd.cy;
					positionPlayerCurr.set(cfd.positionPlayer.x,cfd.positionPlayer.y,cfd.positionPlayer.z);
					positionAiCurr.set(cfd.positionAI.x,cfd.positionAI.y,cfd.positionAI.z);
					positionBallCurr.set(cfd.positionBall.x,cfd.positionBall.y,cfd.positionBall.z);
					if(!cfd.isTaskDone)
					{
						task=cfd.task;
						cfd.isTaskDone=true;
					}
					else
					{
						task=new int[0];
					}
					
					zAngleCurr = cfd.zAngle;
					zAngleCurrAI = cfd.zAngleAI;
					lListCurr = cfd.lList;
					isPaddleCurr = cfd.is_paddle;
					pointsPlayerCurr = cfd.pointsplayer;
					pointsAiCurr = cfd.pointAi;
					txCurr = cfd.tx; 
					tyCurr = cfd.ty;
					isshootman = cfd.ishsootman;
				}
			}
			
			ma.soundandshakeutil.doTask(task);  
			//�Y�G���O����v���åB�����S�������A�N�v��
			if(!Constant.IS_PLAY_VIDEO&&!Constant.IS_GAME_OVER)
			{
				FrameData fd= FrameData.LoadData
				(
					cxCurr, 
					cyCurr, 
					positionPlayerCurr, 
					positionAiCurr, 
					positionBallCurr,
					task,
					zAngleCurr,
					zAngleCurrAI,
					lListCurr,
					isPaddleCurr,
					pointsPlayerCurr,
					pointsAiCurr,
					txCurr,
					tyCurr,
					System.nanoTime() - startTime,
					isshootman
				);
				FrameData.playFrameDataList.add(fd);
			}
			
			float ratio = RATIO;
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 3.0f, 200);
			
            MatrixState.setLightLocation(0,5,0);
            
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			MatrixState.pushMatrix();
			
			MatrixState.setCamera
            (
            	-0.025125364f + cxCurr,2.3820834f,6.761625f+cyCurr,
        		0.0f + cxCurr,0.0f,0.0f + cyCurr,
        		0.0f,0.82984775f,-0.5579899f
            ); 
			
			//ø��ж�
			MatrixState.pushMatrix();
			room.drawSelf(textureid_floor,textureid_floor_hadow,textureid_wall);
			MatrixState.popMatrix();
			
			//ø��y�x����
			MatrixState.pushMatrix();
			tabletennis.drawSelf(textureid_surface, textureid_net, textureid_spring); 
			MatrixState.popMatrix();
			//�y
			MatrixState.pushMatrix();
		    ball.drawSelf(textureid_ball,0,positionBallCurr,0);//ø��y
			MatrixState.popMatrix();
			
			//ø��ۼv
		    if(isPaddleCurr)
			{
		    	GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
				for(int i=lListCurr.size()-1;i>=0;i--)
				{					
			        if(i>lListCurr.size()-1)break;//�ѨM��������B���D
					ball.drawSelf(textureid_ball,0,lListCurr.get(i),0.2f+i*0.1f);//ø��ۼv
				}
				GLES20.glDisable(GLES20.GL_BLEND);
		        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			}
		    

	        //�}�ҲV�X
	        GLES20.glEnable(GLES20.GL_BLEND);
	        //�]�w�V�X�Ѽ�
	        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
	        ball.drawSelf(textureid_ball,1,positionBallCurr,0);//ø��v�l
	        GLES20.glDisable(GLES20.GL_BLEND);
	        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			
			//�y��
			MatrixState.pushMatrix();
			setBatTextureId();
			rackets.position.set(positionPlayerCurr.x,positionPlayerCurr.y,positionPlayerCurr.z);
			rackets.zAngle = zAngleCurr;
			rackets.drawSelf(textureid_racket,positionPlayerCurr);
			MatrixState.popMatrix();

			//�y��
			MatrixState.pushMatrix();
			racketsAi.position.set(positionAiCurr.x,positionAiCurr.y,positionAiCurr.z);
			racketsAi.zAngle = zAngleCurrAI;
			racketsAi.drawSelf(textureid_racketAi,positionAiCurr);
			MatrixState.popMatrix();
			
			MatrixState.popMatrix();
		}
		private void drawHelpHand(float txCurr, float tyCurr) 
		{
			helphandrec.drawSelf(From2DTo3DUtil.point3D(new float[]{txCurr, tyCurr}), textureid_helphand);
		}
		private void setBatTextureId()
		{
			switch(DIFFICULTY){
			case 0:
				textureid_racket = BATS_TEXTURE_ID[0];
				break;
			case 1:
			case 2:
				textureid_racket = BATS_TEXTURE_ID[1];
				break;
			case 3:
			case 4:
				textureid_racket = BATS_TEXTURE_ID[2];
				break;
			case 5:
				textureid_racket = BATS_TEXTURE_ID[3];
				break;
			}
		}
		private void drawMenu()
		{
			float ratio = RATIO;
			//�]�w�ثe�x�}����v�x�}
			MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
			
			MatrixState.pushMatrix();
			//�I�s����k������v��9�ѼƦ�m�x�}
			MatrixState.setCamera(0, 0, 0, 0, 0, -1, 0, 1, 0);
			
			//�}�ҲV�X
            GLES20.glEnable(GLES20.GL_BLEND);
            //�]�w�V�X�Ѽ�
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            
            //ø���X�H�αo��
            if(isshootman)
            {
            	drawFlyFlag(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[0][0], GAMEVIEW_PIC_LS[0][1]}), GAMEVIEW_TEXTURE_ID[WHICH_COUNTRY+11]);
            	gr1.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[1][0], GAMEVIEW_PIC_LS[1][1]}), GAMEVIEW_TEXTURE_ID[DIFFICULTY+11]);
            }
            else
            {
            	gr1.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[0][0], GAMEVIEW_PIC_LS[0][1]}), GAMEVIEW_TEXTURE_ID[WHICH_COUNTRY+11]);
            	drawFlyFlag(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[1][0], GAMEVIEW_PIC_LS[1][1]}), GAMEVIEW_TEXTURE_ID[DIFFICULTY+11]);
            }
            gr2.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[2][0], GAMEVIEW_PIC_LS[2][1]}), GAMEVIEW_TEXTURE_ID[pointsPlayerCurr/10]);
            gr2.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[3][0], GAMEVIEW_PIC_LS[3][1]}), GAMEVIEW_TEXTURE_ID[pointsPlayerCurr%10]);
            gr2.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[4][0], GAMEVIEW_PIC_LS[4][1]}), GAMEVIEW_TEXTURE_ID[pointsAiCurr/10]);
            gr2.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[5][0], GAMEVIEW_PIC_LS[5][1]}), GAMEVIEW_TEXTURE_ID[pointsAiCurr%10]); 
            switch(state)
            {
            case 1://��l�ɭ�
            	gr3.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[7][0], GAMEVIEW_PIC_LS[7][1]}), GAMEVIEW_TEXTURE_ID[19]);
            	break;
            case 2://�Ȱ��ɭ�
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[8][0], GAMEVIEW_PIC_LS[8][1]}), GAMEVIEW_TEXTURE_ID[20]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[9][0], GAMEVIEW_PIC_LS[9][1]}), GAMEVIEW_TEXTURE_ID[21]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[10][0], GAMEVIEW_PIC_LS[10][1]}), GAMEVIEW_TEXTURE_ID[22]);
            	break;
            case 3://�ӧQ
            	gr5.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[6][0], GAMEVIEW_PIC_LS[6][1]}), GAMEVIEW_TEXTURE_ID[17]);
            	if(!win_flag)
            	{
            		new Thread()
                	{
                		public void run()
                		{
                			try 
                			{
    							Thread.sleep(2000);
    						} 
                			catch (InterruptedException e) 
                			{
    							e.printStackTrace();
    						}
                			state = 7;
                		}
                	}.start();
                	win_flag = true;
            	}
            	break;
            case 4://����
            	gr5.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[6][0], GAMEVIEW_PIC_LS[6][1]}), GAMEVIEW_TEXTURE_ID[18]);
            	if(!lose_flag)
            	{
		        	new Thread()
		        	{
		        		public void run()
		        		{
		        			try 
		        			{
								Thread.sleep(2000);
							} 
		        			catch (InterruptedException e) 
		        			{
								e.printStackTrace();
							}
		        			state = 7;
		        		}
		        	}.start();
		        	lose_flag = true;
            	}
            	break;
            case 5://�ӧQ���
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[8][0], GAMEVIEW_PIC_LS[8][1]}), GAMEVIEW_TEXTURE_ID[20]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[9][0], GAMEVIEW_PIC_LS[9][1]}), GAMEVIEW_TEXTURE_ID[23]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[10][0], GAMEVIEW_PIC_LS[10][1]}), GAMEVIEW_TEXTURE_ID[22]);
            	break;
            case 6://���ѿ��
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[11][0], GAMEVIEW_PIC_LS[11][1]}), GAMEVIEW_TEXTURE_ID[20]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[12][0], GAMEVIEW_PIC_LS[12][1]}), GAMEVIEW_TEXTURE_ID[22]);
            	break;
            case 7://�O�_�˵�����
            	gr6.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[15][0], GAMEVIEW_PIC_LS[15][1]}), GAMEVIEW_TEXTURE_ID[26]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[11][0], GAMEVIEW_PIC_LS[11][1]}), GAMEVIEW_TEXTURE_ID[24]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[12][0], GAMEVIEW_PIC_LS[12][1]}), GAMEVIEW_TEXTURE_ID[25]);
            	break;
            case 8://�v��������l
            	if(IS_HELP)
    			{
            		gr3.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[7][0], GAMEVIEW_PIC_LS[7][1]}), GAMEVIEW_TEXTURE_ID[19]);
            		gr7.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[16][0], helpsmY}), GAMEVIEW_TEXTURE_ID[27]);
            		drawHelpHand(txCurr,tyCurr);
    			}
            	else
            	{
            		gr3.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[7][0], GAMEVIEW_PIC_LS[7][1]}), GAMEVIEW_TEXTURE_ID[19]);
            		if(pvt != null && !pvt.wFlag)
            		{
            			state = 11; 
            		}
            	}
            	break;
            case 9://���U�Ȱ����
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[11][0], GAMEVIEW_PIC_LS[11][1]}), GAMEVIEW_TEXTURE_ID[21]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[12][0], GAMEVIEW_PIC_LS[12][1]}), GAMEVIEW_TEXTURE_ID[22]);
            	break; 
            case 10://�v�������Ȱ����
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[8][0], GAMEVIEW_PIC_LS[8][1]}), GAMEVIEW_TEXTURE_ID[20]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[9][0], GAMEVIEW_PIC_LS[9][1]}), GAMEVIEW_TEXTURE_ID[21]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[10][0], GAMEVIEW_PIC_LS[10][1]}), GAMEVIEW_TEXTURE_ID[22]);
            	break;
            case 11://�v������������
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[11][0], GAMEVIEW_PIC_LS[11][1]}), GAMEVIEW_TEXTURE_ID[20]);
            	gr4.drawSelf(From2DTo3DUtil.point3D(new float[]{GAMEVIEW_PIC_LS[12][0], GAMEVIEW_PIC_LS[12][1]}), GAMEVIEW_TEXTURE_ID[22]);
            	break;
            }
            GLES20.glDisable(GLES20.GL_BLEND);//�����V�X
			
			MatrixState.popMatrix();
			
			helpsmY -= 0.5f;
			if(helpsmY <= -100)
			{
				helpsmY = GAMEVIEW_PIC_LS[16][1]; 
			}
		}
	
		private void drawFlyFlag(float []f,int texid)
		{
			MatrixState.pushMatrix();
            MatrixState.rotate(20, 1, 0, 0);
			MatrixState.scale(0.17f, 0.17f, 0.17f);
            MatrixState.translate(f[0], f[1], -10f);
            if(isshootman)
            {
            	MatrixState.translate(-6.7f, 0.5f, 0.0f);
            }
            else
            {
            	MatrixState.translate(6.7f, 0.5f, 0.0f);
            }
            flyflag.drawSelf(texid);
            MatrixState.popMatrix();
		}
	};
	
}


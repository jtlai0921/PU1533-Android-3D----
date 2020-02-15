package tstc.fxq.main;
import static tstc.fxq.constants.Constant.BALL_SCALE;
import static tstc.fxq.constants.Constant.CUE_DH_FLAG;
import static tstc.fxq.constants.Constant.CUE_Y_ADJUST;
import static tstc.fxq.constants.Constant.MINI_MAP_TRASLATE;
import static tstc.fxq.constants.Constant.MiniMapScale;
import static tstc.fxq.constants.Constant.cueFlag;
import static tstc.fxq.constants.Constant.mode;
import static tstc.fxq.constants.Constant.pauseFlag;
import static tstc.fxq.constants.Constant.threadWork;
import static tstc.fxq.constants.Constant.vBall;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.GameMode;
import tstc.fxq.constants.Sight;
import tstc.fxq.constants.WhatMessage;
import tstc.fxq.parts.BallKongZhi;
import tstc.fxq.parts.BallDingDian;
import tstc.fxq.parts.Cue;
import tstc.fxq.parts.MiniLine;
import tstc.fxq.parts.MiniMap;
import tstc.fxq.parts.PoolMap;
import tstc.fxq.parts.ZhuoZi;
import tstc.fxq.parts.NearPoint;
import tstc.fxq.parts.Percentage;
import tstc.fxq.parts.ProgressBar;
import tstc.fxq.parts.TextureRect;
import tstc.fxq.parts.Qiang;
import tstc.fxq.threads.BallGoThread;
import tstc.fxq.threads.MoveCameraThread;
import tstc.fxq.threads.RegulationTimeThread;
import tstc.fxq.threads.ThreadKey;
import tstc.fxq.threads.TimeRunningThread;
import tstc.fxq.utils.Board;
import tstc.fxq.utils.InstrumentBoardFactory;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.RainbowBar;
import tstc.fxq.utils.Score;
import tstc.fxq.utils.ShaderManager;
import tstc.fxq.utils.Timer;
import tstc.fxq.utils.VirtualButton;
import tstc.fxq.utils.VirtualButtonChangePic;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView{
	private float TOUCH_SCALE_FACTOR_X;	//X��V�Y����
	private float TOUCH_SCALE_FACTOR_Y; //y��V�Y����
	
	float mPreviousX;
	float mPreviousY;
	public MyActivity activity=(MyActivity)this.getContext();
	
	//��v������m
	float cx=0;//��v��x��m
	float cy=50;//��v��y��m
	float cz=50;//��v��z��m
	
	float tx=0;//�ت��Ix��m
	float ty=0;//�ت��Iy��m
	float tz=0;//�ت��Iz��m
	
	//�z����v���Y��]�l
    float factor = 0.38f;
    
    //���b�ղy����m����v���ɪ����p��
	public float currSightDis=Constant.FIRST_SIGHT_DIS;//��v���M�ت����Z��
	float angdegElevation=10;//����
	public float angdegAzimuth=0;//��쨤
	//�ثe�̤p����
	float currMinElevation = Constant.FIRST_MIN_ELEVATION;//�}�l���Ĥ@�H�٪��̤p����
	//���׼Ҧ�
	public Sight currSight = Sight.first;//�}�l���Ĥ@�H�٨���
	
	//����D�ʪ��ܼ�
    float yAngleV=0;//�`���������ܤƳt��
    boolean isMove=false;//Ĳ�N�ƥ�O�_���h�����ЧӦ�
    long previousTime;//�W�����U���ɶ�
    
	// ��v����m�l�ܥ��y����k
	public void setCameraPostion(float xOffset, float zOffset) {
		/*
		 * �N�ղy��m�]���ت�
		 * 
		 * �b�]�p���ɭԡA�y���_�l��m�M�ୱ�t90��
		 * �Gø��y�ɾ���uy�b����F-90��
		 * �y�Ф]�n�����ഫ������᪺
		 */
		//�Y���Ĥ@�H�٨��סA�ت��I�O���y�A��v�����Z������
		if(currSight == Sight.first){
			tx = -zOffset;
			ty = Constant.BALL_Y;
			tz = xOffset;
		}
		//�Y���ۥѨ��סA�ت��I�O�y�x�����I�A��v�����Z������
		else if(currSight == Sight.free){
			tx = 0;
			ty = Constant.BALL_Y;
			tz = 0;
		}
		//�p����v������m
		double angradElevation = Math.toRadians(angdegElevation);// �����]���ס^
		double angradAzimuth = Math.toRadians(angdegAzimuth);// ��쨤
		cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.cos(angradAzimuth));
		cy = (float) (ty + currSightDis * Math.sin(angradElevation));
		cz = (float) (tz + currSightDis * Math.cos(angradElevation) * Math.sin(angradAzimuth));
	}
	
    private SceneRenderer mRenderer;//�����ۦ⾹
    
    //�����������A��
    int size;//�O���y���Ӽ�    
    public static boolean miniBall=true;//����ø��g�A���y
    static boolean mFlag=true;//����O�_�}�Ҥp�a��
    public static int state = -1;//�O�����U���������s
    static boolean moveFlag=false;//�Y�G�b�I��������ɡA��h���F,����אּfalse�A�w�����OACTION_MOVE�ƥ�C
    public static List<Integer> alBallMiniId;//�g�A��y���zID�M��
    public static List<Integer> alBallTexId;//��y���zID
    static boolean downFlag=false;//�Y�G�bmove�ɡAdown�ƥ󤣯�ϥΡC
    
    
    
    int currProgress;//�i�׫��ܾ����i��
    //���zid�ŧi
    int wel_8;	//���J�ɭ����I���Ϥ� 
    int wel_9;	//���J�ɭ����I���Ϥ�
    int process_top; //�i�׫��ܾ����W�h�Ϥ�
    int process_bottom;	//�i�׫��ܾ����U�h�Ϥ�
    int num[];		//11�i�Ʀr�Ϥ��]���t�ʤ����^
    
    
    //���zid�ŧi
    int cueId;
	int ballTexId;
	public int wallId;//�𭱩M�a�OId
	int tableBottomId;//�ੳ���zID		
	int tableAreaId;//�ୱ���zID
	int tableAreaHelpShadowId;//���y���v�l�A�Ȫ��ୱ���z
	int tableRoundUDId;//��l�W�U��tID
	int tableRoundLRId;//��l���k��tID
	int circleId;//�}ID	
	public int iconId;//���Ъ�Id
	public int numbersId;//����Id
	int ballMiniId;//�g�A��y���zID
	int cueMiniId;//�g�A�y�쯾�zID
	int leftId;//�������䥪��ʯ��zID
	int rightId;//��������k��ʯ��zID
	int mId;//������M��ID
	int goId;//���������y��ID
	int miniTableId;//�g�A�y�௾�zID	
	int poolPartOneId;//�y�x���}�������zID
	int poolPartTwoId;//�y�x���}�������zID	
	int poolPartTwoJiaoId;//�y�x��}�������zID
	int poolPartOneMId;//�y�x���}�������zID	
	int greenId;//�y�x���}��ⳡ�����zID	
	int barId;//�O�D���I��
	int sightFreeId;//�ۥѨ��ׯ��zid
	int sightFirstId;//�Ĥ@�H�٨��ׯ��zid
	int nearId;//�Ԫ�Ϥ����zid
	int farId;//�Ի��Ϥ����zid
	int breakId;//���j��id
	
	
	//********************************�i�׫��ܾ�begin*****************
    TextureRect wel;//���J�ɭ����I���Ϥ�
	ProgressBar process;//�i�׫��ܾ�
	Percentage perc;//�i�ת��ʤ���
	//********************************�i�׫��ܾ�end*****************
	
	//�����D���������󪺰Ѧ�
    public static ArrayList<BallKongZhi> ballAl;//��y���M��    
	public Cue cue;//�y��	
	MiniMap ballTexture;//�g�A�a�ϤW���y
	MiniMap cueMini;//�g�A�y��
	MiniLine line;//�u 
	PoolMap poolMap;
	Qiang wall;//�𭱩M�a�O
	BallDingDian btbv;//�Ω�ø��y    
	ZhuoZi drawTable;//��l
	
    //�۾A���̪��Ҧ�����O������
	VirtualButton goBtn;//go���s
	VirtualButton rightBtn;//�k���s
	VirtualButton leftBtn;//�����s
	VirtualButton mBtn;//m���s
	Board iconBoard;//�y����O
	RainbowBar rainbowBar;//�m�i��
	Board barBoard;//�O�D���I��
	public Score score;//�i�y�Ӽ�
	VirtualButtonChangePic sightBtn;//�����ഫ���s
	VirtualButton nearBtn;//�ո`��v���M�ت��I�Z�������s
	VirtualButton farBtn;//�ո`��v���M�ت��I�Z�������s
	public Timer timer;//�˭p�ɾ�
	//��������󪺰Ѧ�
    public BallGoThread bgt;//��y���B�ʰ����
    static ThreadKey key;//������U�����䪺�����
    TimeRunningThread timeRunningThread;//�˭p�ɰ����
    //++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xbegin++++++++++++++++++++++++++++++++++
    RegulationTimeThread regTimeThread;	//�C�@��P�_�@���O�_�O�ɪ������
    //++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xend++++++++++++++++++++++++++++++++++
    
    boolean hasLoadOk=false;//�O�_�w�g���J����
	public MySurfaceView(Context context) {
        super(context);
        
        this.setKeepScreenOn(true);        
        
        this.setEGLContextClientVersion(2); //�]�w�ϥ�OPENGL ES2.0
        mRenderer = new SceneRenderer();	//�إ߳����ۦ⾹
        setRenderer(mRenderer);				//�]�w�ۦ⾹		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//�]�w�ۦ�Ҧ����D�ʵۦ�
        
        ballAl=new ArrayList<BallKongZhi>();//��y���M��
        alBallMiniId=new ArrayList<Integer>();//�g�A��y���zID�M��
        alBallTexId=new ArrayList<Integer>();//��y���zID
        
        //++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xbegin++++++++++++++++++++++++++++++++++
        regTimeThread = new RegulationTimeThread(this,ballAl);
        //++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xend++++++++++++++++++++++++++++++++++
        
        //�C���_�l�ƫ�A�٭��R�A�`�ƪ���
        miniBall=true;//����ø��g�A���y
        mFlag=true;//����O�_�}�Ҥp�a��
        state = -1;//�O�����U���������s
        moveFlag=false;//�Y�G�b�I��������ɡA��h���F,����אּfalse�A�w�����OACTION_MOVE�ƥ�C
        alBallTexId=new ArrayList<Integer>();//��y���zID
        downFlag=false;//�Y�G�bmove�ɡAdown�ƥ󤣯�ϥΡC
        Constant.cueFlag=true;//ø��y��ЧӦ�  
        
        //�Ұʤ@�Ӱ�����ھڥثe�����t�ױ������
        Constant.threadWork=true;
        new Thread()
        {
        	public void run()
        	{
        		while(threadWork)
        		{ 
        			//�Y�G�y��b��ܤ�
        			if(cueFlag){
            			//�ھڨ��t�׭p��s���������ਤ��
            			angdegAzimuth-=yAngleV;
            			//�N���׳W��ƨ�0��360����
            			angdegAzimuth=(angdegAzimuth+360)%360;
            			
            			if(ballAl != null && ballAl.size() != 0){
        	            	//���s�]�w��v������m
        	 	            setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
            			}
    	 	            
            			//�Y�ثe����w�g��_�h���t�װI��
            			if(!isMove)
            			{
            				yAngleV=yAngleV*0.8f;
            			}
            			//�Y���t�פp��]�w�ȫh�k0
            			if(Math.abs(yAngleV)<0.05)
            			{
            				yAngleV=0;
            			}
        			}
        			
        			try 
        			{
						Thread.sleep(50);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
        		}
        	}
        }.start();
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent e) {
		
		//�Y�G���b���J�A�Ǧ^true
		if(!hasLoadOk){
			return true;
		}
		
		float y = e.getY();
        float x = e.getX();

        float dx = x - mPreviousX;//�p��Ĳ����Y�첾
    	float dy = y - mPreviousY;//�p��Ĳ����Y�첾

        long currTime=System.currentTimeMillis();//���o�ثe�ɶ��W
        long timeSpan=(currTime-previousTime)/10;//�p��⦸Ĳ���ƥ󤧶����ɶ��t
        
        switch (e.getAction()) 
        {
        case MotionEvent.ACTION_DOWN: 
        	
        	isMove=false;
        	
        	if(CUE_DH_FLAG==true)
    		{//�y�������ʵe���񤤡A���T���ƥ�
    			break;
    		} 
        	//�Y�GĲ���ƥ�o�ͦb�m�i�W�A�h���ܤO�D
        	if(rainbowBar.isActionOnRainbowBar(x, y)){
        		rainbowBar.changePower(x, y);
        		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
        	}
        	//�Y�G�b�ۥѨ��פ��I���ϥܡA�|�������������
        	else if(iconBoard.isActionOnBoard(x, y) && currSight == Sight.free){
        		angdegElevation = 89.99f;
        		angdegAzimuth = 90;
        		//�令�̦X�A�������Z��
    			currSightDis = Constant.FREE_SIGHT_DIS;
        		//���s�]�w��v������m
	            setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
	            moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
        	}
    		if(downFlag==true)
        	{
	        	//�Y�GĲ���ƥ�o�ͦb�k���s�W
	        	if(rightBtn.isActionOnButton(x, y))
	        	{
	        		rightBtn.pressDown();//���U���s
	        		if(Constant.cueFlag)	//��������A�åB�b���y��ɤ~����ʲy��
	        		{
	        			state=ThreadKey.rightRotate;//��ܫ��U�k��ʫ��s
	            		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
	        		}
	        	}
	        	//�Y�GĲ���ƥ�o�ͦb�����s�W
	        	else if(leftBtn.isActionOnButton(x, y))
	        	{
	        		leftBtn.pressDown();//���U���s
	        		if(cueFlag)	//��������A�åB�b���y��ɤ~����ʲy��
	        		{
	        			state=ThreadKey.leftRotate;//��ܫ��U����ʫ��s
	            		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
	        		}
	        	}
	        	//�Y�GĲ���ƥ�o�ͦbm���s�W
	        	else if(mBtn.isActionOnButton(x, y))
	        	{
	        		mBtn.pressDown();//���U���s
	        		mFlag=!mFlag;//���UM��
	        		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
	        	}
	        	//�Y�GĲ���ƥ�o�ͩԪ���s�W�A�åB�ثe�y�w�g����
	        	else if(nearBtn.isActionOnButton(x, y))
	        	{
	        		if(cueFlag){
		        		nearBtn.pressDown();//���U���s
		        		state=ThreadKey.nearer;//��ܫ��U�Ԫ���s
	        		}
	        		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
	        	}
	        	//�Y�GĲ���ƥ�o�ͦb�Ի����s�W�A�åB�ثe�y�w�g����
	        	else if(farBtn.isActionOnButton(x, y))
	        	{
	        		if(cueFlag){
		        		farBtn.pressDown();//���U���s
		        		state=ThreadKey.farther;//��ܫ��U�Ի����s
		        	}
	        		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
	        	}
	        	//�Y�GĲ���ƥ�o�ͦb�������׫��s�W
	        	else if(sightBtn.isActionOnButton(x, y))
	        	{
	        		sightBtn.pressDown();//���U���s
	        		//�������׼Ҧ�
	        		if(!sightBtn.isBtnPressedDown()){
	        			//���̤ܳp����
	        			currMinElevation = Constant.FIRST_MIN_ELEVATION;
	        			angdegElevation = 10;
	        			//������Ĥ@�H�٨��׫e�A�O�����y�e���y����m�A����v���q���B�}�l�B��
						MoveCameraThread.xFrom = ballAl.get(0).xOffset;
						MoveCameraThread.zFrom = ballAl.get(0).zOffset;

		        		//�N�ثe�Z������b�Ĥ@���׶Z�����d��
		        		currSightDis = Math.min(currSightDis, Constant.FIRST_SIGHT_DIS_MAX);
		        		
	        			//��o�����e���y����ਤ��
	        			float tempAngle = cue.getAngle();
	        			//���ܥثe����
	        			currSight = Sight.first;
	        			//�y�쪺���ਤ�׳]�������e������
	        			cue.setAngle(tempAngle);
	        		}
	        		else{
	        			//���̤ܳp����
	        			currMinElevation = Constant.FREE_MIN_ELEVATION;
	        			//�Y�G�����e�ثe���פp��̤p���סA�N�ثe���׳]���̤p����
	        			if(angdegElevation > currMinElevation){
	        				angdegElevation = currMinElevation;
	        			}
	        			//�N�ثe�Z������b�ۥѨ��׶Z�����d��
		        		currSightDis = Math.max(currSightDis, Constant.FREE_SIGHT_DIS_MIN);
		        		
	        			//��o�����e���y����ਤ��
	        			float tempAngle = cue.getAngle();
	        			//���ܥثe����
	        			currSight = Sight.free;
	        			//�y�쪺���ਤ�׳]�������e������
	        			cue.setAngle(tempAngle);
	        		}
	        		//���s�]�w��v������m
	        		setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
	        		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
	        	}
	        	//�Y�GĲ���ƥ�o�ͦbgo���s�W
	        	else if(goBtn.isActionOnButton(x, y))
	        	{
	        		goBtn.pressDown();//���U���s
	        		//���y�������s
		        	if(cueFlag)	
		        	{
		        		CUE_DH_FLAG=true;
		        		new Thread()
						{
							public void run()
							{
								for(int i=0;i<5;i++)
								{
									CUE_Y_ADJUST+=0.25f;
									try 
									{
										Thread.sleep(100);
									} 
									catch (InterruptedException e) 
									{
										e.printStackTrace();
									}
								}
								for(int i=0;i<5;i++)
								{
									CUE_Y_ADJUST-=0.25f;
									try 
									{
										Thread.sleep(100);
									} 
									catch (InterruptedException e) 
									{
										e.printStackTrace();
									}
								}
								CUE_DH_FLAG=false;
								
								//�O�����y�e���y����m
								MoveCameraThread.xFrom = ballAl.get(0).xOffset;
								MoveCameraThread.zFrom = ballAl.get(0).zOffset;
								//�N�O�D�����ثe�O�D�����ȵ��y���t��
								vBall=MySurfaceView.this.rainbowBar.getCurrStrength();
    			        		ballAl.get(0).vx=(float)(-1*vBall*Math.sin(Math.toRadians(cue.getAngle())));//���ѥ��y�t��
    							ballAl.get(0).vz=(float)(-1*vBall*Math.cos(Math.toRadians(cue.getAngle())));
    							cueFlag=false;//���y�ЧӦ�]��false//���U���y��

    							if(activity.isSoundOn()){
        							activity.playSound(1, 0, 1);//����}�y����    	
    							}    							
							}
						}.start();
		        	}
		        	
		        	
		        	//++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xbegin++++++++++++++++++++++++++++++++++
		        	Constant.START_TIME=System.currentTimeMillis();//�����y���}�l�ɶ�
		        	//++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xend++++++++++++++++++++++++++++++++++
		        	
		        	//++++++++++++++++++++�S������ت��y���ǳW�ƥ�{���Xbegin++++++++++++++++++
		        	Constant.IS_FIGHT=false;	//�b�@�����y�ƥ󤤡A�O�_�P��L����y�o�͸I�����ЧӦ�
		        	//++++++++++++++++++++�S������ت��y���ǳW�ƥ�{���Xend++++++++++++++++++++
		        	
		        	Constant.MAINBALL_FIRST_FIGHT = false;
		        	
		        	
		        	
             		moveFlag=false;//�b�������s��m���U�A��wmove�ƥ�
	        	}
        	}
        break;
        case MotionEvent.ACTION_MOVE:
        	if(CUE_DH_FLAG==true)
    		{//�y�������ʵe���񤤡A���T���ƥ�
    			break;
    		}
        	//�Y�G�h����Ĳ���ƥ�S���o�ͦbgo���s�W
        	if(!goBtn.isActionOnButton(x, y)){
        		goBtn.releaseUp();//������s
        	}
        	//�Y�G�h����Ĳ���ƥ�S���o�ͦb�k���s�W
        	if(!rightBtn.isActionOnButton(x, y)){
        		rightBtn.releaseUp();//������s
        	}
        	//�Y�G�h����Ĳ���ƥ�S���o�ͦb�����s�W
        	if(!leftBtn.isActionOnButton(x, y)){
        		leftBtn.releaseUp();//������s
        	}
        	//�Y�G�h����Ĳ���ƥ�S���o�ͦbm���s�W
        	if(!mBtn.isActionOnButton(x, y)){
        		mBtn.releaseUp();//������s
        	}
        	//�Y�GĲ���ƥ�o�ͩԪ���s�W
        	if(!nearBtn.isActionOnButton(x, y)){
        		nearBtn.releaseUp();//������s
        	}
        	//�Y�GĲ���ƥ�o�ͦb�Ի����s�W
        	if(!farBtn.isActionOnButton(x, y)){
        		farBtn.releaseUp();//������s
        	}
        	if(rainbowBar.isActionOnRainbowBar(x, y)){
        		rainbowBar.changePower(x, y);
        	}
        	//�Y�GĲ���ƥ�o�ͦb���פ������s�W�A����]����
        	else if(sightBtn.isActionOnButton(x, y)){
        		//����]����
        	}
        	//�Y�G�b�ۥѨ��פ����U�ϥܡA����]����
        	else if(iconBoard.isActionOnBoard(x, y) && currSight == Sight.free){
        		//����]����
        	}
        	//���U���s�ɡA�NmoveFlag�]��false�A�o�˱q���s�W�h���X�Ӥ]���פ]���|��
        	else if(moveFlag==true)
        	{
        		downFlag=false;//����down�ƥ�i���i��
	            
	            float tmpAngdegAzimuth = -dx * TOUCH_SCALE_FACTOR_X;//�]�w�uy�b���ਤ��
	            float tmpAngdegElevation = -dy * TOUCH_SCALE_FACTOR_Y;//�]�w�ux�b���ਤ��

	            //�Y�G���Ĥ@�H�٨��סA�B�y�b�B�ʤ��A���e�\���k�٫�
	            if(currSight == Sight.first && cueFlag == false){
	            	break;
	            }
	            
	            //�O�_�����v�����ЧӦ�G0����ʡA1���k��ʡA2�W�U���
	            int rotateDirection = 0;
	            //�Y�G���L�V�h���h�A�åB�h�������h
	            if(Math.abs(dx)<=Math.abs(dy) && Math.abs(tmpAngdegElevation) > 0.34f){	            	
	            	rotateDirection = 2;
	            }
	            //�Y�G�����h���h�A�åB�h�������h
	            else if(Math.abs(dx)>Math.abs(dy) && Math.abs(tmpAngdegAzimuth) > 3f){
		            rotateDirection = 1;
		            //Ĳ���첾�j��]�w�ȫh�i�J�h�����A
	            	isMove=true; 	            	
	            }
	            else{
	            	isMove = false;
	            	yAngleV = 0;
	            }
	            //�������
	            if(rotateDirection == 1){
	 	            if(isMove)
	 	            {//�Y�b�h�����A�h�p�⨤���ܤƳt��
	 	            	if(timeSpan!=0)
	 	            	{//���������0���͵L�a�j���t��
	 	            		yAngleV=dx * TOUCH_SCALE_FACTOR_X/timeSpan;     
	 	            	}
	 	            } 
	 	            
	            }//���L���
	            else if(rotateDirection == 2){
		            angdegElevation += tmpAngdegElevation;//�]�w�ux�b���ਤ��		            
		            //�Y�G����90���٭n������v�����Y�¦V�A�G����90��
		            if(angdegElevation>89.99f){
		            	angdegElevation=89.99f;
		            }
		            //����ثe��������p��̤p����
		            else if(angdegElevation<currMinElevation){
		            	angdegElevation = currMinElevation;
		            }
		            //���s�]�w��v������m
		            setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);
	            }
        	}
			break;
        case MotionEvent.ACTION_UP:
        	//������s
        	goBtn.releaseUp();
        	rightBtn.releaseUp();
        	leftBtn.releaseUp();
        	mBtn.releaseUp();
        	nearBtn.releaseUp();
        	farBtn.releaseUp();
        	
    		moveFlag=true; 
        	downFlag=true;//����
        	state=-1;//-1--��ܤ����
        	isMove=false;
        break;
        }

		mPreviousY = y;//�O��Ĳ������m
	    mPreviousX = x;//�O��Ĳ������m
        previousTime=currTime;//�O�������ɶ��W
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		//�`�ѱ��������ը���W�v���{���X
//		long olds;
//    	long currs;
		boolean isFirstFrame=true;//�O�_�Oø��Ĥ@��
		
        public void onDrawFrame(GL10 gl) {
        	//�`�ѱ��������ը���W�v���{���X
//        	currs=System.nanoTime();			
//			System.out.println(1000000000.0/(currs-olds)+"FPS");
//			olds=currs;
        	if(!hasLoadOk)
        	{
            	//�M���m��֨���`�ק֨�
            	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            	
            	//ø���w��ɭ�
            	MatrixState.pushMatrix();
                MatrixState.setProjectOrtho(-1, 1, -1, 1, 1, 10);
                MatrixState.setCamera(0, 0, 4, 0, 0,3, 0, 1, 0); 
                
                //�Y�G�O15�y�Ҧ�
                if(Constant.POS_INDEX==0)
                {
                    MatrixState.pushMatrix();
                    wel.drawSelf(wel_8);//ø���w��ɭ�
                    MatrixState.popMatrix();
                	
                }//�Y�G�O9�y�Ҧ�
                else
                {
                    MatrixState.pushMatrix();
                    wel.drawSelf(wel_9);//ø���w��ɭ�
                    MatrixState.popMatrix();
                }
                MatrixState.popMatrix();
                
                //�Y�G�O�Ĥ@��
                if(isFirstFrame)
                {
                	isFirstFrame=false;
                }//�Y�G�O�ĤG��
                else
                { 	
                	
                	if(currProgress==0)
                	{
                		//���J�i�׫��ܾ����I�}�ϸ귽
                		initProgressBarBitmap();
                		//���J�i�׫��ܾ���shader
                		initProgressBarShader();
                		//�إ߶i�׫��ܾ�������
                		initProgressBarObject();
                		
                	}
                	
                    //ø��i�׫��ܾ�
                    drawProgressBar();
                	
                	currProgress++;//�i�׫��ܾ����i�׫���[
                	
                	if(currProgress==20)
                	{
                		//���J�Ψ쪺�Ҧ��I�}�ϸ귽
                		initBitmap();
                	}
                	
                	if(currProgress==40)
                	{
    					//���J�Ҧ�Shader
    					ShaderManager.loadShadersFromFile(MySurfaceView.this);
    					//�sĶ�Ҧ���shader
    					ShaderManager.compileShaders();
                	}
                	
                	if(currProgress==60)
                	{
    					//��������إߪ���
    					initLoading();
                	}
                	
                	if(currProgress==80)
                	{
                        //�Ұʮ�y���B�ʰ����
                        bgt=new BallGoThread(ballAl,MySurfaceView.this);
                        bgt.start();

            			moveFlag=true;//���������ù��iĲ��
            			downFlag=true;//��������������iĲ��
            			//���������A�]�����Ĥ@����
            			setCameraPostion(ballAl.get(0).xOffset, ballAl.get(0).zOffset);

                        key=new ThreadKey(MySurfaceView.this);
                        key.start();//�Ұ�����������䪺�����    
                        
                		if(Constant.mode == GameMode.countDown){
                			timeRunningThread=new TimeRunningThread(MySurfaceView.this);//�إ߭p�ɰ����
                			timeRunningThread.start();
                		}
                        //�C���i�J�����ɭ��ɡA�N��v�����B�ʪ�������M��
                        MoveCameraThread.currThread = null;
                        
                        
                        //++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xbegin++++++++++++++++++++++++++++++++++
                        regTimeThread.start();
                      //++++++++++++++++++++++++++++++�O�ɥǳW�Ϊ��{���Xend++++++++++++++++++++++++++++++++++
                       
        	            
                	}
    	            if(currProgress>=100)
    	            {
            			if(activity.isBackGroundMusicOn())//�}�ҭI������
            			{
	            	    	activity.initBackGroundSound();
            				activity.mMediaPlayer.start();
            			}
    	            	hasLoadOk=true;	//���J�����A�ЧӦ�]��true
    	            }
                }
	
        	}else
        	{
        		//�M���m��֨���`�ק֨�
            	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            	
                //�I�s����k�p�ⲣ�ͳz����v�x�}
                MatrixState.setProjectFrustum(-Constant.RATIO*factor, Constant.RATIO*factor, -1*factor, 1*factor, 4, 250); 
            	
            	//�b������v���e�A��w��v�������ਤ�סA�ϲy��M��v�������ਤ�׵�ı�W�O���@�P
            	cue.lockAngdegAzimuth(angdegAzimuth);
                //�]�wcamera��m
            	MatrixState.setCamera
                (
                		cx,   //�H����m��X
                		cy, 	//�H����m��Y
                		cz,   //�H����m��Z
                		tx, 	//�H�����ݪ��IX
                		ty,   //�H�����ݪ��IY
                		tz,   //�H�����ݪ��IZ
                		0, 	//up��m
                		1, 
                		0
                );
            	
            	
            	
                //ø�����
            	MatrixState.pushMatrix();
            	MatrixState.translate(0, -5, 0);
                wall.drawSelf(wallId);//ø���𭱩M�a�O
                MatrixState.popMatrix();
                
                //ø���l
                MatrixState.pushMatrix();
                drawTable.drawSelf
                (
                		tableBottomId,tableAreaId,tableRoundUDId,
                		tableRoundLRId,circleId,poolPartOneId,poolPartTwoId,
                		poolPartTwoJiaoId,poolPartOneMId,
                		greenId
                );
                MatrixState.popMatrix();
                
                //ø��y
                MatrixState.pushMatrix();//�_�l�x�}�J���|
                MatrixState.rotate(-90, 0, 1, 0); 
                size=ballAl.size();//��o�y���Ӽ�
                
            	try 
                {
            		//�ƻs�y���z�M�y���M��  
            		List<Integer> alBallTexIdTmp=new ArrayList<Integer>(alBallTexId);//��y���zID
            		ArrayList<BallKongZhi> ballAlTmp=new ArrayList<BallKongZhi>(ballAl);;//��y���M��
            		
            		//�}�ҲV�X
                    GLES20.glEnable(GLES20.GL_BLEND);  
                    //�]�w�V�X�]�l
                    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);            		
            		//ø��Ҧ��y���v
            		for(int i=0;i<size;i++)
                    {
            			ballAlTmp.get(i).lockState();
                    	ballAlTmp.get(i).drawSelf(alBallTexIdTmp.get(i), tableAreaHelpShadowId, 1);
                    }
                    //�����V�X
                    GLES20.glDisable(GLES20.GL_BLEND);
                    
            		//ø��Ҧ��y
                	for(int i=0;i<size;i++)
                    {
                    	ballAlTmp.get(i).drawSelf(alBallTexIdTmp.get(i), tableAreaHelpShadowId, 0);
                    }
                	
                }catch(Exception e)
                {
                	e.printStackTrace();
                }
            	MatrixState.popMatrix();//�٭�_�l�x�}
            	
            	
            	// ø��y��
    			if (cueFlag && ballAl != null && ballAl.size() != 0) {
    				// �T�β`�״���
    				GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    				MatrixState.pushMatrix();
    				cue.drawSelf(ballAl.get(0), cueId, cueId, tableRoundUDId);
    				MatrixState.popMatrix();
    				GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    			}
                
                //**********************�}�lø��p�a��******************************************
    			MatrixState.setProjectFrustum(-Constant.RATIO, Constant.RATIO, -1, 1, 4, 250);
                MatrixState.setCamera(0,0,0,0,0,-1,0,1,0);//��v���٭����I
                
                //�T�β`�״���
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                GLES20.glEnable(GLES20.GL_BLEND);//�}�ҲV�X   
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);//�]�w���V�X�]�l�P�ت��V�X�]�l
                                
                if(mFlag==true)//����O�_�}�Ҥp�a��
                {
    	            MatrixState.pushMatrix();
    	            MatrixState.translate(MINI_MAP_TRASLATE[MyActivity.screenId][0], MINI_MAP_TRASLATE[MyActivity.screenId][1], MINI_MAP_TRASLATE[MyActivity.screenId][2]);
    	            
    	            MatrixState.pushMatrix();
    	            poolMap.drawSelf(miniTableId);//ø��g�A�a��
    	            MatrixState.popMatrix();
    	            
    	            if(ballAl.size() != 0)//�Y�M�椤�y���ƶq��0�A�h��ø��y
    	            {
    		            try
    		            {
    		            	if(miniBall)//����O�_ø����y
    		            	{
    		            		MatrixState.pushMatrix();
    		            		ballTexture.drawSelf
    				            (
    				            		-ballAl.get(0).zOffset*MiniMapScale,
    				            		-ballAl.get(0).xOffset*MiniMapScale,
    				            		0.1f,
    				            		alBallMiniId.get(0)
    				            );//ø��زy
    		            		MatrixState.popMatrix();
    		            	}
    		            	
    		            	for(int i=1;i<ballAl.size();i++)
    		            	{
    		            		MatrixState.pushMatrix();//ø��g�A�a�ϤW���y
    				            ballTexture.drawSelf
    				            (
    				            		-ballAl.get(i).zOffset*MiniMapScale,
    				            		-ballAl.get(i).xOffset*MiniMapScale,
    				            		0.1f,
    				            		alBallMiniId.get(i)
    				            );//ø��䥦�y
    				            MatrixState.popMatrix();
    		            	}
    			            if(cueFlag)
    			            {
    			            	MatrixState.pushMatrix();
    			            	MatrixState.translate(-ballAl.get(0).zOffset*MiniMapScale, -ballAl.get(0).xOffset*MiniMapScale, 0);
    			            	MatrixState.rotate(cue.getAngle(), 0, 0, 1);
    			            	
    			            	MatrixState.pushMatrix();
    			            	cueMini.drawSelf//ø��g�A�y��
    			            	(
    			            			-0.15f, 
    			            			0, 
    			            			0.1f, 
    			            			cueMiniId
    			            	);
    			            	MatrixState.popMatrix(); 
    			            	MatrixState.popMatrix();
    			            	
    			            	MatrixState.pushMatrix();
    			            	float[] cuePoint=NearPoint.getCuePoint//���o�y��y��
    			            	(-ballAl.get(0).zOffset*MiniMapScale, -ballAl.get(0).xOffset*MiniMapScale, -cue.getAngle());
    			            	float[] tempPoint=NearPoint.GetJiaoDian//���o�ت��I�y��
            					(
            							-ballAl.get(0).zOffset*MiniMapScale,-ballAl.get(0).xOffset*MiniMapScale,
            							cuePoint[0],cuePoint[1]
            					);
    			            	line.drawSelf//ø��u
    			            	(
    			            			new float[]
    	            			          {
    			            					-ballAl.get(0).zOffset*MiniMapScale, -ballAl.get(0).xOffset*MiniMapScale, 0.1f,
    			            					tempPoint[0],tempPoint[1],0.1f
    	            			          }
    			            			
    			            	);
    			            	
    			            	MatrixState.popMatrix();
    			            }
    		            }catch(Exception e)
    		            {
    		            	e.printStackTrace();
    		            }
    	            }
    	            MatrixState.popMatrix();
                }
                //�����V�X
                GLES20.glDisable(GLES20.GL_BLEND);
                //�ҥβ`�״���
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                //**********************����ø��p�a��******************************************
                
                //================ø��۾A���̪�����O======= begin ========
                //�]�w�����v�x�}
                MatrixState.setProjectOrtho(-Constant.RATIO, Constant.RATIO, -1, 1, 1, 2);
                //�I�s����k������v��9�ѼƦ�m�x�}
                MatrixState.setCamera(0,0,1f,0f,0f,0f,0f,1.0f,0.0f);
                //�}�ҲV�X
                GLES20.glEnable(GLES20.GL_BLEND);
                //�]�w�V�X�]�l
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                //�T�β`�״���
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                
                //go���s
                MatrixState.pushMatrix();
                goBtn.drawSelf();          
                MatrixState.popMatrix(); 
                //�k���s
                MatrixState.pushMatrix();
                rightBtn.drawSelf();          
                MatrixState.popMatrix(); 
                //�����s
                MatrixState.pushMatrix();
                leftBtn.drawSelf();          
                MatrixState.popMatrix(); 
                //m���s
                MatrixState.pushMatrix();
                mBtn.drawSelf();          
                MatrixState.popMatrix();             
                //icon����O
                MatrixState.pushMatrix();
                iconBoard.drawSelf();          
                MatrixState.popMatrix();            
                //�O�D���I��
                MatrixState.pushMatrix();
                barBoard.drawSelf();          
                MatrixState.popMatrix();
                //�m�i��
                MatrixState.pushMatrix();
                rainbowBar.drawSelf();          
                MatrixState.popMatrix();
                //�o��
                MatrixState.pushMatrix();
                score.drawSelf();          
                MatrixState.popMatrix();
                //�������׫��s
                MatrixState.pushMatrix();
                sightBtn.drawSelf();          
                MatrixState.popMatrix();
                //�Ԫ���s
                MatrixState.pushMatrix();
                nearBtn.drawSelf();          
                MatrixState.popMatrix();
                //�Ի����s
                MatrixState.pushMatrix();
                farBtn.drawSelf();          
                MatrixState.popMatrix();
                //�˭p�ɫ��s
                if(Constant.mode == GameMode.countDown){
                    MatrixState.pushMatrix();
                    timer.drawSelf();          
                    MatrixState.popMatrix();            	
                }
                
                
                //�����V�X
                GLES20.glDisable(GLES20.GL_BLEND);
                //�ҥβ`�״���
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                //================ø��۾A���̪�����O======= end ==========  
                
                //�٭��z����v�x�}
                MatrixState.setProjectFrustum(-Constant.RATIO*factor, Constant.RATIO*factor, -1*factor, 1*factor, 4, 250);

        	}

        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
        	
        	//�ù������e������
            Constant.SCREEN_WIDTH = width;
        	Constant.SCREEN_HEIGHT = height;        	 
        	//�p��GLSurfaceView�����e��
            Constant.RATIO = (float) width / height;
            
        	//�_�l��XY��V�Y����
        	TOUCH_SCALE_FACTOR_X = 360f/width;
        	TOUCH_SCALE_FACTOR_Y = 90f/height;
            //�]�w�����j�p�Φ�m 
        	GLES20.glViewport(0, 0, width, height);           
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //�]�w�ù��I����¦�RGBA
        	GLES20.glClearColor(0,0,0,1);            
            //�ҥβ`�״���
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    		//�]�w���}�ҭI���ŵ�
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //�_�l���ܴ��x�}
            MatrixState.setInitStack();
            //�_�l�ƥ�����m
            MatrixState.setLightLocation(0, 10, 0);

           
            //���J�I�����I�}�ϸ귽
            wel_8=initTexture(R.drawable.wel_8);//15�y�����J�ɭ����I��
            wel_9=initTexture(R.drawable.wel_9);//9�y�����J�ɭ����I��
            //���J�A���J�ɭ���shader
            ShaderManager.loadWelcomeSurfaceShaderFromFile(MySurfaceView.this);
            ShaderManager.compileWelcomeSurfaceShader();
            //���J�ɭ����x�Ϋإߪ���
    		wel = new TextureRect(2f, 2f, 0.781f, 0.936f); 
        }
    }
	
	//���J�A�i�׫��ܾ��ɭ����I�}�ϸ귽
	public void initProgressBarBitmap()
	{
		//�_�l�ƹϤ�id
        process_top=initTexture(R.drawable.process_top);
        process_bottom=initTexture(R.drawable.process_bottom);
        //�_�l�ƼƦr�Ϥ��귽
        num = new int[]{
        	initTexture(R.drawable.num0),
        	initTexture(R.drawable.num1),
        	initTexture(R.drawable.num2),
        	initTexture(R.drawable.num3),
        	initTexture(R.drawable.num4),
        	initTexture(R.drawable.num5),
        	initTexture(R.drawable.num6),
        	initTexture(R.drawable.num7),
        	initTexture(R.drawable.num8),
        	initTexture(R.drawable.num9),
        	initTexture(R.drawable.num10)
        };
	}
	//���J�i�׫��ܾ��ɭ���shader
	public void initProgressBarShader()
	{
        //���J�A���J�ɭ���shader
        ShaderManager.loadProgressBarShaderFromFile(MySurfaceView.this);
        ShaderManager.compileProgressBarShader();
	}
	//���i�׫��ܾ��إߪ���
	public void initProgressBarObject()
	{
        //�إߵ������s���� 
        process=new ProgressBar(
        		MySurfaceView.this,
        		0.06f,0.6f,
        		0.1f, 		//����  
        		1f, 1f //sEnd�BtEnd
        		);
        //�Ʀr�i��		
        perc = new Percentage(
        		MySurfaceView.this,
        		0.4f,0.61f,
        		0.04f, 0.08f, 		//�e�סA����  
        		1f, 1f //sEnd�BtEnd
        );  
	}
	
	public void drawProgressBar()
	{
		//�}�ҲV�X
        GLES20.glEnable(GLES20.GL_BLEND);  
        //�]�w�V�X�]�l
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
        //ø������Ϥ�
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0f, 2.0f);
        process.drawSelf(process_bottom,100);         
        MatrixState.popMatrix(); 
        //ø��W�����Ϥ�
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0f, 2.1f);
        process.drawSelf(process_top,currProgress);          
        MatrixState.popMatrix();
        
		//ø��i�׫��ܾ��W�誺�Ʀr
		String tempStr=currProgress+"";
		for(int i=0;i<tempStr.length();i++)   
		{   
			MatrixState.pushMatrix();
			MatrixState.translate((2*perc.width)*(i+1), 0f, 2.2f);
			perc.drawSelf(num[tempStr.charAt(i)-'0']); 
			MatrixState.popMatrix(); 
		}
		
		//ø��ʤ���
		MatrixState.pushMatrix();
		MatrixState.translate((tempStr.length()+1)*2*perc.width, 0f, 2.2f);
		perc.drawSelf(num[10]); 
		MatrixState.popMatrix(); 
		
        //�����V�X
        GLES20.glDisable(GLES20.GL_BLEND);
	}
	
	//�_�l�ƩҦ����Ϥ��귽
	public void initBitmap()
	{
        cueId=initTexture(R.drawable.cue);//�_�l�Ʋy��ID            
        //�_�l�Ư��z    ,�N�Ҧ��y�����zID��J�@�ӲM�椤        
        ballTexId=initTexture(R.drawable.snooker0);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker1);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker2);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker3);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker4);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker5);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker6);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker7);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker8);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker9);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker10);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker11);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker12);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker13);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker14);
        alBallTexId.add(ballTexId);
        ballTexId=initTexture(R.drawable.snooker15);
        alBallTexId.add(ballTexId);
        //�_�l�Ʈ�l �ж������zID
		wallId=initTexture(R.drawable.wall);
        tableBottomId=initTexture(R.drawable.bottom);//�ੳ���zID
        tableAreaId=initTexture(R.drawable.area);//�ୱ���zID
        tableAreaHelpShadowId=initTexture(R.drawable.area_help_shadow);//���y���v�l�A�Ȫ��ୱ���z
        tableRoundUDId=initTexture(R.drawable.round);//��l�W�U��tID
        tableRoundLRId=initTexture(R.drawable.roundlr);//��l���k��tID
        circleId=initTexture(R.drawable.circle);//�y�}ID
		iconId=initTexture(R.drawable.icon0);
		numbersId=initTexture(R.drawable.numbers);
		////////////////////////////////////////////////
		//�_�l�ưg�A�a�ϯ��zID
		ballMiniId=initTexture(R.drawable.minimap0);//���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap1);//1���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap2);//2���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap3);//3���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap4);//4���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap5);//5���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap6);//6���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap7);//7���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap8);//8���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap9);//9���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap10);//10���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap11);//11���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap12);//12���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap13);//13���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap14);//14���y
		alBallMiniId.add(ballMiniId);
		ballMiniId=initTexture(R.drawable.minimap15);//15���y
		alBallMiniId.add(ballMiniId);
		
		cueMiniId=initTexture(R.drawable.cue);
		miniTableId=initTexture(R.drawable.minitable);//�_�l�Ʋy�௾�z
		////////////////////////�_�l�Ƶ������s���z
		leftId=initTexture(R.drawable.left);
		rightId=initTexture(R.drawable.right);
		mId=initTexture(R.drawable.m);
		goId=initTexture(R.drawable.space);
		poolPartOneId=initTexture(R.drawable.texture4);
		poolPartTwoId=initTexture(R.drawable.round);
		poolPartTwoJiaoId=initTexture(R.drawable.d1);
		poolPartOneMId=initTexture(R.drawable.d2);
		greenId = initTexture(R.drawable.green);
		barId=initTexture(R.drawable.bar);
		sightFirstId = initTexture(R.drawable.sight_first);
		sightFreeId = initTexture(R.drawable.sight_free);
		nearId = initTexture(R.drawable.near);
		farId = initTexture(R.drawable.far);
		breakId = initTexture(R.drawable.breaker);
		/////////////////////////////////////////////////////
	}
	//�_�l�Ư��z
	public int initTexture(int drawableId)
	{
		//���ͯ��zID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //���ͪ����zid���ƶq
				textures,   //���zid���}�C
				0           //�����q
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		//�DMipmap���z���˹L�o�Ѽ�	
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		
		//ST��V���z���i�Ҧ�
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);		
        
        //�z�L��J�y���J�Ϥ�===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);        	
        }
        finally 
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
        //��ڸ��J���z,�����o�Ӥ�k��A�Y�G�Ϥ��榡�����D�A�|�ߥX�Ϥ��榡�ҥ~�A���A�|�~��ܨ�L�ҥ~
	   	GLUtils.texImage2D
	    (
	    		GLES20.GL_TEXTURE_2D, //���z���A
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp, //���z�ϧ�
	     		GLUtils.getType(bitmapTmp), 
	     		0 //���z��ؤؤo
	     );   
        //�۰ʲ���Mipmap���z
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //���񯾲z��
        bitmapTmp.recycle();
        //�Ǧ^���zID
        return textureId;
	}	
	
	//�إߴ������󪫥�
	public void initLoading()
	{
		cue=new Cue(MySurfaceView.this,1.0f);
		
		poolMap=new PoolMap(this);//�g�A��x
		
        ballTexture=new MiniMap//ø��g�A�a�ϤW���y
 		(MySurfaceView.this,
 			1.0f*MiniMapScale,1.0f*MiniMapScale,
 			new float[]
		          {
	    				0,0,0,1,1,1,
	    	        	1,1,1,0,0,0
		          }
 		);
        
        cueMini=new MiniMap//ø��g�A�y��
        (MySurfaceView.this,
         		4.0f*MiniMapScale,0.25f*MiniMapScale,
     			new float[]
 		          {
 	    				0,0,0,1,1,1,
 	    	        	1,1,1,0,0,0
 		          }	
        );
        
        line=new MiniLine(this);//ø��u
        
	    drawTable=new ZhuoZi(this);//�إ߮�l����
	    wall=new Qiang(this);//�إ���M����
	    //�إߥΩ�ø��y
	    btbv=new BallDingDian(MySurfaceView.this,BALL_SCALE);
	    
	    //�إߥΩ󱱨�y�A�å[�J�M��
		for(int i=0;i<BallKongZhi.AllBallsPos[Constant.POS_INDEX].length;i++)
		{
			ballAl.add(
					new BallKongZhi(
							btbv,
							BallKongZhi.AllBallsPos[Constant.POS_INDEX][i][0],
							BallKongZhi.AllBallsPos[Constant.POS_INDEX][i][1],
							this,
							i
							)
					);
		}
		
		//�إߦ۾A���̪�����O
        initInstrumentBoards();
	}
	//�_�l�ƻ���O����k
	void initInstrumentBoards(){
        //go���s 
        goBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		0f, 430f,
        		50f, 50f,
        		30,30,
        		1f, 1f, //sEnd�BtEnd
        		goId, //upTexId�BdownTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //�k���s
        rightBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		470f, 430f,//x2=800-x1-r
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd�BtEnd
        		rightId, //upTexId�BdownTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //�����s
        leftBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		280f, 430f,
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd�BtEnd
        		leftId, //upTexId�BdownTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );        
        //m���s
        mBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		600f, 430f,
        		50f, 50f,
        		40,40,
        		1f, 1f, //sEnd�BtEnd
        		mId, //upTexId�BdownTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );   
        //�إ�icon����O 
        iconBoard=InstrumentBoardFactory.newBoardInstance(
        		MySurfaceView.this,
        		10f, 12f,
        		30f, 30f,
        		40, 40,
        		1f, 1f, //sEnd�BtEnd
        		iconId, //upTexId�BdownTexId,
        		0,//���z��
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //�إ߱m�i��
        rainbowBar=InstrumentBoardFactory.newRainbowBarInstance(
        		MySurfaceView.this,
        		8f, 145f,
        		22f, 190f,
        		20, 30,
        		0.114678899f,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //�O�D���I��
        barBoard=InstrumentBoardFactory.newBoardInstance(
        		MySurfaceView.this,
        		4f, 140f,
        		30f, 200f,
        		0, 0,
        		0.448f, 1f, //sEnd�BtEnd
        		barId, //upTexId�BdownTexId,
        		1,//�z��
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //�إ߱o������
        score=InstrumentBoardFactory.newScoreInstance(
        		MySurfaceView.this,
        		50f, 13f,
        		22f, 27f,
        		1f, 1f, //sEnd�BtEnd
        		numbersId, //numbersId
        		true,
        		InstrumentBoardFactory.Gravity.Side
        ); 
        //�������ת����s
        sightBtn=InstrumentBoardFactory.newButtonChangePicInstance(
        		MySurfaceView.this,
        		740f, 430f,
        		50f, 50f,
        		40f, 40f,
        		1f, 1f, //sEnd�BtEnd
        		sightFirstId,sightFreeId, //upTexId�BdownTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //�Ԫ���s
        nearBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		200f, 430f,
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd�BtEnd
        		nearId, //upTexId�BdownTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );  
        //�Ի����s
        farBtn=InstrumentBoardFactory.newButtonInstance(
        		MySurfaceView.this,
        		130f, 430f,
        		50f, 50f,
        		20,20,
        		1f, 1f, //sEnd�BtEnd
        		farId, //upTexId�BdownTexId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        );
        //�إ߱o������
        timer=InstrumentBoardFactory.newTimerInstance(
        		MySurfaceView.this,
        		350f, 13f,
        		22f, 27f,
        		1f, 1f, //sEnd�BtEnd
        		numbersId, //numbersId
        		breakId,
        		true,
        		InstrumentBoardFactory.Gravity.Side
        ); 
	}
	@Override
    public void onResume()
    {
    	super.onResume();//�I�s�����O��k
    	pauseFlag = false;  
    	Constant.cueFlag=true;//ø��y��ЧӦ�
    	//���s�إߨñҰʩҦ������
    	recreateAllThreads();
    }
	@Override
	public void onPause() {
		super.onPause();
		pauseFlag = true;
		//����Ҧ������
		stopAllThreads();
	}

	//����Ҧ����������k
	void stopAllThreads()
	{
		bgt.flag = false;   
		key.stateFlag = false;
		if(timeRunningThread != null){
			timeRunningThread.setFlag(false);
		}
		regTimeThread.regTimeFlag = false;
		threadWork = false;
	}
	void recreateAllThreads(){
		bgt.flag = true;   
		key.stateFlag = true;
		if(timeRunningThread != null){
			timeRunningThread.setFlag(true);
		}
		regTimeThread.regTimeFlag = true;
		
		//�Ұʮ�y���B�ʰ����
        bgt=new BallGoThread(ballAl,MySurfaceView.this);
        bgt.start();
        //�Ұ�����������䪺�����   
        key=new ThreadKey(MySurfaceView.this);
        key.start();
        //�إ߭p�ɰ����
		if(Constant.mode == GameMode.countDown){
			timeRunningThread=new TimeRunningThread(MySurfaceView.this);
			timeRunningThread.start();
		}
		regTimeThread.start();
	}
	//���������A�ø��D������ɭ�����k�C�Ӥ�k�u��b���������A�ݭn�O���ɩI�s
	public void overGame(){
		stopAllThreads();
		//����I������
        if(activity.mMediaPlayer.isPlaying()){
        	activity.mMediaPlayer.stop();
        }
        
        //�Y�G�O�˭p�ɼҦ�
        if(mode == GameMode.countDown){
        	//�N�`�o�������ȵ�activity����score
        	activity.currScore = timer.getLeftSecond();
    		activity.sendMessage(WhatMessage.OVER_GAME);
        } 
        //�Y�G�O�m�߼Ҧ�
        else{
			//���Ĺ�o��������͵���
			this.bgt.sendHandlerMessage(Constant.EXIT_SYSTEM_WIN);
        }
	}
}


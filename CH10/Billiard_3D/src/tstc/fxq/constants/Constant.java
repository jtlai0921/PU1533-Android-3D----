package tstc.fxq.constants;

import java.util.ArrayList;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.parts.BallKongZhi;
import tstc.fxq.threads.MoveCameraThread;
import tstc.fxq.utils.PZJCUtil;

public class Constant
{
	public static int SCREEN_WIDTH;//�ù����e��
	public static int SCREEN_HEIGHT;//�ù�������
	public static float RATIO;//�ù����e��
	
	public static boolean pauseFlag=false;//�O�_�Ȱ��{��
 
	public final static float TABLE_UNIT_SIZE=0.5f;//�ੳ������
	public final static float TABLE_UNIT_HIGHT=0.5f;//�ੳ��찪��
	
	public static boolean MQJDSYYJBF=false;//���y�i�}���Ĥw�g����

	public final static float BOTTOM_HIGHT=2.0f;//��l������
	public final static float BOTTOM_LENGTH=36.0f;//��l������
	public final static float BOTTOM_WIDE=24.0f;//��l���e��
	
	public static final float ANGLE_SPAN=12f;//�N�y�i�������������
	public static final float UNIT_SIZE=0.8f;//�y���ؤo
	//�y�h���C�@�B������ɶ����j
	public static final float TIME_SPAN=0.04f; 
	public static final float BALL_SCALE=0.8f;//�y�Y���
	public static final float BALL_R=UNIT_SIZE*BALL_SCALE;//�y���b�|
	public static final float BALL_R_POWER_TWO=BALL_R*BALL_R;//�y�b�|������
	public static final float V_TENUATION=0.987f;//�t�װI��t��
	public static final float V_THRESHOLD=0.10f;//�t�׳]�w�ȡA�p�󦹳]�w�Ȫ��t�׻{����0
	public static final int   THREAD_SLEEP=20;//�������v�ɶ�
	
	public static final float MIDDLE=4.42f*BALL_R*0.9f;//���}�Z��
	public static final float EDGE_SMALL=2.5f*BALL_R;//���}���a�Z���]�p�϶��^
	public static final float EDGE_BIG=3*BALL_R;//�a�}���a�Z���]�j�϶��^
	public static final float EDGE=EDGE_SMALL+EDGE_BIG;//�a�}�a��Z��,�j�϶�+�p�϶�
	
	public static final float UP_DOWN_LENGTH=BOTTOM_LENGTH/2-EDGE-MIDDLE/2;//�W�U��t���סC
	public static final float LEFT_RIGHT_LENGTH=BOTTOM_WIDE-2*EDGE;//���k��t����
	public static final float UP_DOWN_HIGHT=2.0f;//��t���סC
	public static final float TABLE_AREA_LENGTH=BOTTOM_LENGTH-2*EDGE_BIG;//�ୱ����
	public static final float TABLE_AREA_WIDTH=BOTTOM_WIDE-2*EDGE_BIG;//�ୱ�e�� 
	public static final float CIRCLE_R=3.0f*BALL_R;//�y�}���j�p
	public static float vBall;//�O�����y���O�D�j�p,��V
	public static boolean cueFlag=true;//��ø�y�쪺�ЧӦ�
	public static boolean overFlag=false;//���y�������ЧӦ�
	public static boolean hitFlag=false;//����O�_���y  
	public static boolean hitSound=false;//����y�y�I��������
	  
	public static int score=0;//�O���o��	  
	
	public static final float GOT_SCORE_DISTANCE=EDGE_BIG-BALL_R*0.05f;//�y�߻P�~��t���Z���A�Ω�P�_�O�_�i�y
	public static final float CUE_ROTATE_DEGREE_SPAN=0.25f;//�y��C����ʪ�����
	
	public static final float TEXTURE_RECT_Y=-2;//�ୱY�y��
	public static final float DELTA=-1f+0.5f;//�p���Z
	public static final float DELTA_BALL=0.5f;//�y�p���Z
	public static final float WALL_SIZE=70.0f*TABLE_UNIT_SIZE;//�𭱤j�p
	public static final float BALL_Y_ADJUST=0.20f;//�ѩ�v�l���M�y�x�����X�A�Ӧb�y�x���W��A�G�y�n���@��y��V����m�վ��
	public static final float BALL_Y=TEXTURE_RECT_Y+DELTA_BALL+BALL_Y_ADJUST;//�_�l�y��y��m
	public static float CUE_Y_ADJUST=0.0f;//�y��ʵe�վ��
	public static boolean CUE_DH_FLAG=false;//�y��ʵe���񤤼ЧӦ�
	public static final float V_BALL_MAX = 20;//�y���̤j�t��
	
	public static final float BOTTOM_Y=-4;//�y��a��Y��m
	
	public static final float MiniMapScale=0.05f;//�y��Ի���ҭ�
	
	public static final float screenRatio800x480=1.667f;
	public static final float screenRatio854x480=1.779f;
	
	//�����y�_�l��m���`��
	public static final float MAIN_BALL_INIT_ZOFFSET=8.6f;//���y�_�l��Z��m
	public static final float DIS_WITH_MAIN_BALL=14.0f;//�@���y�M���y�����Z��
	public static float GAP_BETWEEN_BALLS=0.12f;//�y�P�y�������_�l����
	
	//���󨤫ת��`��
    public static final float FREE_SIGHT_DIS = 130;//�ۥѨ��׮���v���M�ت��I���Z��
    public static final float FIRST_SIGHT_DIS = 60;//�Ĥ@�H�٨��׮���v���M�ت��I���Z��
    
    public static final float FREE_SIGHT_DIS_MIN = 30;//�ۥѨ��׮���v���M�ت��I���̪�Z��
    public static final float FIRST_SIGHT_DIS_MIN = 20;//�Ĥ@�H�٨��׮���v���M�ت��I���̪�Z��
    
    public static final float FREE_SIGHT_DIS_MAX = 130;//�ۥѨ��׮���v���M�ت��I���̻��Z��
    public static final float FIRST_SIGHT_DIS_MAX = 80;//�Ĥ@�H�٨��׮���v���M�ت��I���̻��Z��
    
    public static final float FREE_SIGHT_DIS_SPAN = 3;//�ۥѨ��׮���v���M�ت��I���̪�Z��
    public static final float FIRST_SIGHT_DIS_SPAN = 2;//�Ĥ@�H�٨��׮���v���M�ت��I���̪�Z��
    
    public static final float FREE_MIN_ELEVATION = 10;//�ۥѨ��׮ɳ̤p����
    public static final float FIRST_MIN_ELEVATION = 2;///�Ĥ@�H�ٮɳ̤p����
    
	public static float[][] MINI_MAP_TRASLATE=//�g�A�Ϫ���m
	{
		{//480*320
			2.3f,1.6f,-9,
		},
		{//480*854
			3.0f,1.5f,-9,
		},
		{//800*480
			2.7f,1.6f,-9,
		}
	};
	
	//2D�ɭ��۾A���̮ɪ��`��
	public static final int screenWidthTest=800;//���վ��ù��e��
	public static final int screenHeightTest=480;//���վ��ù�����

	public static float wRatio;//�A�����ù����Y����
	public static float hRatio;
	
	//�_�l�Ʊ`�ƪ�������k
	public static boolean isInitFlag = false;//��k�O�_�Q�I�s�L���ЧӦ�	
	public static void initConst(int screenWidth,int screenHeight)
	{
		//�Y�G��k�w�g����L�A�h���A����
		if(isInitFlag == true){
			return;
		}
		
		SCREEN_WIDTH=screenWidth;//�ù����ؤo
		SCREEN_HEIGHT=screenHeight;
		//�A�����ù����Y����
		wRatio=screenWidth/(float)screenWidthTest;
		hRatio=screenHeight/(float)screenHeightTest;
		
		//�Хܤ�k�w�Q����L
		isInitFlag = true;
	}
	
	//�������k�A0�N��8�y�Ҧ��A1�N��9�y�Ҧ�
	public static int POS_INDEX = 0;
	//�����Ҧ�
	public static GameMode mode = GameMode.countDown;
	
	//�D�ʰ�����u�@�ЧӦ�   
	public static boolean threadWork;
	//����Ѱ��ߺ��ʰ��D���`��
	public static final int MAX_COLLISION_NUM = 3;//�̤j�s��I��������
	public static int collisionCount;//�ثe�s��I��������
	public static int preCollisionIdA = -1;//�W���I����A�y��id
	public static int preCollisionIdB = -1;//�W���I����B�y��id
	//##########################�H�U�O�W�h���Ψ쪺�{���X##################################################

	
	public static long START_TIME = 0;		//�W�@�����y�������ɶ�(�����y���}�l�ɶ�)
	//�X�{�ǳWToast���s��
	public static final int OVERTIMETOAST = 0;	//�����y�W�L60��ɡA�X�{�ǳW��Toast
	public static final int REMINDPLAYER = 1;	//�����y�W�L45��ɡA�������a
	
	public static final int MAINBALL_FLOP = 2;	//�D�y���}Toast
	public static final int NO_FIGHT = 3;		//�S���P����y�o�͸I���ƥ�ǳW
	public static final int NO_FIGHT_AND_FLOP = 4;		//�S���P����y�o�o�͸I���ƥ�åB�D�y���}
	public static final int NO_FIGHT_TARGET = 5;
	public static final int MAINBALLFLOP_AND_NOTFIGHTTARGET = 6;//�D�y�i�}�A�������ت��y
	
	public static final int EXIT_SYSTEM_LOSE9 = 7;//���}�~�{����͵���
	public static final int EXIT_SYSTEM_LOSE8 = 8;//���}�~�{����͵���
	public static final int EXIT_SYSTEM_WIN = 9;//���}�~�{����͵���
	public static final int TIME_UP = 10;//�ɶ���
	public static final int BREAK_RECORD = 11;//�ɶ���
	
	
	public static boolean IS_FIGHT=false;	//�b�@�����y�L�{���A�O�_�P����y�o�͸I�����ЧӦ�
	public static boolean MAINBALL_FLOPFLAG=false;	//�D�y�O�_�i�}���ЧӦ�A�Y���I���L�y�A�D�y�i�}��A�h����ܡ����P��L�y�I����Toast��
	public static boolean MAINBALL_FIRST_FIGHT = false;//�b�@�����y�L�{���A�D�y�P��L�y�Ĥ@���I�����ЧӦ�
	public static boolean FIRST_FIGHT_ERROR = false; //�Ĥ@����D�y�I�����y���O�̤p�y���ЧӦ�
	
	
	
	//�N�ղy�٭��_�l��m����k
	public static void recoverWhiteBall(
			MySurfaceView mv,
			ArrayList<BallKongZhi> ballAl 	//�Ҧ���y���M��
			)
	{
		ballAl.get(0).zOffset=MAIN_BALL_INIT_ZOFFSET;
		if(ballAl.size()>=2)
		{
			//�ʺA�j�M�i�H��X��m
			for(int ik=0;ik<11;ik++)
			{									
				boolean zjTemp=false;
				ballAl.get(0).xOffset=ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}										
				if(zjTemp==false)
				{
					break;
				}
				
				zjTemp=false;
				ballAl.get(0).xOffset=-ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}	
				if(zjTemp==false)
				{
					break;
				}
			}
		}
		ballAl.get(0).yOffset=1;
		MQJDSYYJBF=false;
		mv.cue.setAngle(0);
		//��ø���y
		MySurfaceView.miniBall=true;
		
		//*******************������v���B�ʪ��ʵe�ĪG************************
		
		
		//�O�����y��ղy����m
		MoveCameraThread.xTo = ballAl.get(0).xOffset;
		MoveCameraThread.zTo = ballAl.get(0).zOffset;
		
		if(mv.currSight == Sight.first){
			//�Y�G�ثe��v����������šA�άO������w�g���`�A�A�}�ҷh����v���������
			if(
					MoveCameraThread.currThread == null || 
					!MoveCameraThread.currThread.isAlive()
				)
			{
				//�Ҧ��y�����A�}�ҷh����v���������
				MoveCameraThread.currThread = new MoveCameraThread(mv);	
				MoveCameraThread.currThread.start();
			}
		}					//�Y�G�O�ۥѨ��סA������ܲy��
		else{
			//��ܲy�쪺�ЧӦ�	
			
		}

	}
	//�N�ղy�٭��_�l��m����k
	public static void recoverWhiteBallNoCam(
			MySurfaceView mv,
			ArrayList<BallKongZhi> ballAl 	//�Ҧ���y���M��
			)
	{
		ballAl.get(0).zOffset=MAIN_BALL_INIT_ZOFFSET;
		if(ballAl.size()>=2)
		{
			//�ʺA�j�M�i�H��X��m
			for(int ik=0;ik<11;ik++)
			{									
				boolean zjTemp=false;
				ballAl.get(0).xOffset=ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}										
				if(zjTemp==false)
				{
					break;
				}
				
				zjTemp=false;
				ballAl.get(0).xOffset=-ik*BALL_R;										
				for(int jk=1;jk<ballAl.size();jk++)
				{
					zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
					if(zjTemp==true)
					{
						break;
					}
				}	
				if(zjTemp==false)
				{
					break;
				}
			}
		}
		ballAl.get(0).yOffset=1;
		MQJDSYYJBF=false;
		mv.cue.setAngle(0);
		//��ø���y
		MySurfaceView.miniBall=true;

	}
	//***************************�O�ɥǳWend**************************************
}
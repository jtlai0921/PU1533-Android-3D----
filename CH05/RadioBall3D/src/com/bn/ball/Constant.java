package com.bn.ball;

public class Constant 
{	
	
	public static final float MAX_S_QHC=1.0f;//�C�Ს�̤jS���z�y��
	public static final float MAX_T_QHC=0.746f;//�C�Ს�̤jT���z�y��
	   
	public static final float MAX_S_GHXP=1.0f;//��e�p�~�̤jS���z�y��
	public static final float MAX_T_GHXP=1.0f;//��e�p�~�̤jT���z�y��
	
	public static final float UNIT_SIZE=0.8f;//�y���ؤo
	public static final float BALL_SCALE=0.35f;//�y�Y���
	public static final float BALL_R=UNIT_SIZE*BALL_SCALE;//�y���b�|
	public static float H=16;
	public static float angleSpan=20; //�޽u����������
	public static float BALL_Z=-1.5f;//�y���_�lZ�y��
	public static float BALL_X=0;	//�y���_�lX�y��
	public static float BALL_Y=-1.2f; //�y���_�lY�y��
	public static float GUANDAO_L=20;//�C�Ӻ޽u������
	public static final float GUANDAO_LENGTH=2000;//�޽u�`����
	public static float GROUND_L=20;//�C�Ӧa��������
	public static final float GROUND_LENGTH=2000;
	public static final float DISTANCE_CAMERA_BALL=2.0f;//��v���Z���|������Z��	
	public static final float MOVE_SPAN=0.2f;//��v���C���h�����첾		
	public static float cUpX=0;
	public static float cUpY=0;
	public static float cUpZ=0;	
	public static final float ANGLE_SPAN=5;//�N�y�i�������������	
	public static float angleCurr;//�ثe��y�����ਤ��
	public static float currAxisX;//�ثe��y����b�V�q��X���q
	public static float currAxisY;//�ثe��y����b�V�q��Y���q�]�]���y�b�ୱ�W�B�ʱ���b�����ୱ�A�]���S��Y���q�^
	public static float currAxisZ;	//�ثe��y����b�V�q��Z���q
	public static float vx=0f;
	public static float vz=-0.2f;	//�p�y�bZ�b���t��
	public static float TIME_SPAN=1.5f;//�y�h���C�@�B������ɶ����j�]���ȶV�p����ĪG�V�u��A���p��q�W�j�A�]���n�X�z�]�w���Ȥj�p�^
	public static boolean flag=false;
	
	public static int num=0;
	
	public static boolean daojuFlag=false;
	
	public static float yuanZhu_Length=0.5f;//��W�������e���@�b
	public static float yuanZhu_Width=0.5f;
	public static float yuanZhu_Height=0.6f;
	
	public static float liFangTi_Length=0.8f;//�ߤ��骺�����e���@�b
	public static float liFangTi_Width=0.8f;
	public static float liFangTi_Height=0.8f;
	
	public static float container_Length=1.0f;//�����骺�����e���@�b
	public static float container_Width=1.5f;
	public static float container_Height=1.0f;
	
	public static float cube_Length=1.5f;//�ߤ��骺�����e���@�b
	public static float cube_Width=1.5f;
	public static float cube_Height=1.5f;
		
	public static float boLi_Length=2.4f;//�����������e���@�b
	public static float boLi_Height=0.9f;
	
	public static boolean ballYFlag=false;
	public static boolean bolipzFlag=false;
	public static boolean loseFlag=true;
	public static boolean winFlag=false;
	public static boolean daojuZFlag=false;
	
	public static float ballVY=0.18f;
	
	public static final float V_THRESHOLD=0.032f;//�t�׳]�w�ȡA�p�󦹳]�w�Ȫ��t�׻{����0
	public static final float V_MC=0.98f;
	public static float upValue=-0.02f;
	public static float downValue=-0.04f;
	
	public static float pengCount=1.2f;
	public static float vk=1;
	public static int sumBoardScore=0;
	public static int sumLoadScore=0;
	public static int sumEfectScore=0;
	public static float daoJuZ=-5.5f;
	public static float change=0;
	
	public static boolean gameOver=true;
}

package com.bn.gjxq.game;
import android.opengl.Matrix;

public class GameData 
{
//////////////////////////////////////////////////////////////////
	public final static int no_qz=-1;//�L�Ѥl
	public final static int bche = 0;//�ը�
	public final static int bma = 1;//�հ�
	public final static int bxiang = 2;//�նH
	public final static int bhou = 3;//�ի�
	public final static int bwang = 4;//�դ�
	public final static int bbing = 5;//�էL
	public final static int hche = 6;//�¨�
	public final static int hma = 7;//�°�
	public final static int hxiang = 8;//�¶H
	public final static int hhou = 9;//�«�
	public final static int hwang = 10;//�¤�
	public final static int hbing = 11;//�§L
	///////////////////////////////////////////////////////////////////
	
	//���Ū�H�Χ�s����
	public Object dataLock=new Object();
	
    //���a�ζ¤l�٬O�դl
    public int humanColor=1; //0---�¦�  1---�զ�
    //�ثe���Ӫ��a��
	public int currColor=1;//0---�¦�  1---�զ�
	
	//�ը�--0
	//�հ�--1
	//�լ�--2
	//�ի�--3
	//�դ�--4
	//�էL--5
	//�¨�--6
	//�°�--7
	//�¬�--8
	//�«�--9
	//�¤�--10
	//�§L--11	
	public int[][] qzwz=//�Ѥl��m
    {
    		{hche,hma,hxiang,hhou,hwang,hxiang,hma,hche},
    		{hbing,hbing,hbing,hbing,hbing,hbing,hbing,hbing},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{bbing,bbing,bbing,bbing,bbing,bbing,bbing,bbing},
    		{bche,bma,bxiang,bhou,bwang,bxiang,bma,bche}
    		
    };
    
    //�Ѥl�Ŀ�
    //���Ŀ�---0
    //�Ŀ�-----1
    public int[][] qzxz=//�Ѥl�Ŀ�
    {
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0}
    };
    
    
    //���Ŀ�---0
    //�Ŀ�-----1
    public int[][] gzxz=//�Ѻж��Ŀ�
    {
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0}
    };
    
    //�ثe���A�s��
    public int status=0;//0--�L�Ѥl�Ŀ�  1-���Ѥl�Ŀ�
    
    public float direction = 0;		//��쨤�A���Ƚd��0--360
    public float angle =60;			//�����A���Ƚd��0--90�A90�׬������A0�׬�����
    public float distance = 12f; 	//��v���ت��I�Z��
    public final float TOUCH_SCALE_FACTOR = 180.0f/400;	//�����Y����
	//��v������m
    public float cx=0; 
    public float cy=0; 
    public float cz=0; 

	//�[���I����m
    public float tx=0; 
    public float ty=0; 
    public float tz=0; 
    
    //up�V�q
    public float ux=0; 
    public float uy=0; 
    public float uz=0; 
	
	public void calculCamare(float dx,float dy) //�p����v������m�P���ਤ��
	{
		direction += dx * TOUCH_SCALE_FACTOR;	//��쨤
		angle+=dy * TOUCH_SCALE_FACTOR;			//��V��
		if(angle<=0)
		{
			angle=0;
		}
		else if(angle>=60)
		{
			angle=60;
		}
        
		//�����V�q
	   	 double angleHD=Math.toRadians(direction);
	   	 float[] cup={-(float) Math.sin(angleHD),0,(float) Math.cos(angleHD),1};
	   	 float[] cLocation={0,distance,0,1};
	   	 
	   	 //�p����b�V�q    	 
	   	 float[] zhou={-cup[2],0,cup[0]};   
	   	
	   	 //�p����v���Ѽ�
	   	 float[] helpM=new float[16];
	   	 Matrix.setIdentityM(helpM, 0);
	   	 Matrix.rotateM(helpM, 0, angle, zhou[0],zhou[1],zhou[2]);
	   	 float[] cupr=new float[4];
	   	 float[] cLocationr=new float[4];
	   	 Matrix.multiplyMV(cupr, 0, helpM, 0, cup, 0);
	   	 Matrix.multiplyMV(cLocationr, 0, helpM, 0, cLocation, 0);
	   	 
	   	 cx=cLocationr[0];//��v����m
	   	 cy=cLocationr[1];
	   	 cz=cLocationr[2];
	   	 tx=0f;ty=0f;tz=0f;//�[���I��m
	   	 ux=cupr[0];uy=cupr[1];uz=cupr[2];//up�V�q
	}
	
	{
		calculCamare(0,0);
	}
	
	public void updateCameraData(GameData gdIn)//�N�ǿ�i�Ӫ�������Ƶ����ȵ���v������m
	{
		this.cx=gdIn.cx;//��v����m
		this.cy=gdIn.cy;
		this.cz=gdIn.cz;
		this.tx=gdIn.tx;//�[���I��m
		this.ty=gdIn.tx;
		this.tz=gdIn.tx;
		this.ux=gdIn.ux;//up�V�q
		this.uy=gdIn.uy;
		this.uz=gdIn.uz;
	}
	
	//���o���w��m�Ѥl���m��  0---�¦�  1---�զ�
	public int getColor(int i,int j)
	{
		int result=0;
		int qzbh=qzwz[i][j];
		if(qzbh>=0&&qzbh<=5)//�Y�Ѥl�s���b0--5�����h�O�զ�Ѥl
		{
			result=1;
		}
		return result;
	}

}

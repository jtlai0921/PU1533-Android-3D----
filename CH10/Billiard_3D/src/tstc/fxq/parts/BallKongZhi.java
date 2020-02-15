package tstc.fxq.parts;
import java.util.ArrayList;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.PZJCUtil;
import tstc.fxq.utils.MatrixState;
import android.opengl.Matrix;
import static tstc.fxq.constants.Constant.*;
//用於控制的桌球
public class BallKongZhi 
{	
	public int id; //用於標示每一個球的id
	int minId = 0;	//編號最小的球的id
	
	MySurfaceView mSurfaceView;//建立MySurfaceView的參考 
	BallDingDian btv;//用於繪制的桌球	
	
	public float xOffset;//桌球的X位置
	public float zOffset;//桌球的Z位置
	public float yOffset=1;//桌球的y位置
	//桌球的速度（桌球是一直在桌面上運動，因此沒有Y向速度）
	public float vx;
	public float vz;
	//桌球卷動過程中的各種擾動值
	float angleTemp;//角度擾動值
	float axisX;//旋轉軸向量的X分量
	float axisZ;//旋轉軸向量的Z分量
	float axisY=0;//旋轉軸向量的Y分量（因為球在桌面上運動旋轉軸平行於桌面，因此沒有Y分量）
	float distance;//此輪桌球搬移的距離	
	float angleX;//繞X軸轉動的角度
	float angleY;//繞Y軸轉動的角度
	float angleZ;//繞Z軸轉動的角度
	
	float[] selfRotateMatrix;//附帶旋轉矩陣
	
	//鎖定本框姿態
	float xOffsetLock;//桌球的X位置
	float zOffsetLock;//桌球的Z位置
	public void lockState()
	{
		xOffsetLock=xOffset;
		zOffsetLock=zOffset;
	} 
	//關於球台上球起始位置的常數
	static final float xBall1=0f;//一號球的位置
	static final float zBall1=Constant.MAIN_BALL_INIT_ZOFFSET - Constant.DIS_WITH_MAIN_BALL;
	static final float xBallDis=(float) ((Constant.BALL_R*2+Constant.GAP_BETWEEN_BALLS)*Math.cos(Math.PI/3));//斜著的球與球之間x方向距離
	static final float zBallDis=(float) ((Constant.BALL_R*2+Constant.GAP_BETWEEN_BALLS)*Math.sin(Math.PI/3));//斜著的球與球之間y方向距離
	static final float xDis=Constant.BALL_R*2+Constant.GAP_BETWEEN_BALLS;//每一列球的距離
	//所有球的位置，0為母球，以後依次從左到右，從下到上
	public static final float[][][] AllBallsPos=
	{
		{//8球模式時，各球的位置
			new float[]{xBall1, Constant.MAIN_BALL_INIT_ZOFFSET},//母球位置
			new float[]{xBall1, zBall1},//1列
			new float[]{xBall1+xBallDis, zBall1-zBallDis},//2列
			new float[]{xBall1+xBallDis-xDis, zBall1-zBallDis},
			new float[]{xBall1+xBallDis*2, zBall1-zBallDis*2},//3列
			new float[]{xBall1+xBallDis*2-xDis, zBall1-zBallDis*2},
			new float[]{xBall1+xBallDis*2-xDis*2, zBall1-zBallDis*2},
			
			new float[]{xBall1+xBallDis*3, zBall1-zBallDis*3},//4列
			new float[]{xBall1+xBallDis*3-xDis, zBall1-zBallDis*3},
			new float[]{xBall1+xBallDis*3-xDis*2, zBall1-zBallDis*3},
			new float[]{xBall1+xBallDis*3-xDis*3, zBall1-zBallDis*3},
			new float[]{xBall1+xBallDis*4, zBall1-zBallDis*4},//5列
			new float[]{xBall1+xBallDis*4-xDis, zBall1-zBallDis*4},
			new float[]{xBall1+xBallDis*4-xDis*2, zBall1-zBallDis*4},
			new float[]{xBall1+xBallDis*4-xDis*3, zBall1-zBallDis*4},
			new float[]{xBall1+xBallDis*4-xDis*4, zBall1-zBallDis*4},
		},
		{//9球模式時，各球的位置
			new float[]{xBall1, Constant.MAIN_BALL_INIT_ZOFFSET},//母球位置
			new float[]{xBall1, zBall1},//1列
			new float[]{xBall1+xBallDis, zBall1-zBallDis},//2列
			new float[]{xBall1+xBallDis-xDis, zBall1-zBallDis},
			new float[]{xBall1+xBallDis*2, zBall1-zBallDis*2},//3列
			new float[]{xBall1+xBallDis*2-xDis, zBall1-zBallDis*2},
			new float[]{xBall1+xBallDis*2-xDis*2, zBall1-zBallDis*2},
	
			new float[]{xBall1+xBallDis, zBall1-zBallDis*3},//4列
			new float[]{xBall1+xBallDis-xDis, zBall1-zBallDis*3},
			new float[]{xBall1, zBall1-zBallDis*4},//5列
		}
	};
	
	public BallKongZhi(BallDingDian btv,float xOffset,float zOffset,MySurfaceView mSurfaceView,int id)
	{
		this.id=id;
		
		this.btv=btv;
		this.xOffset=xOffset;
		this.zOffset=zOffset;
		this.mSurfaceView=mSurfaceView;
		//起始時為鎖定位置給予值
		xOffsetLock=xOffset;
		zOffsetLock=zOffset;
		//起始化附帶旋轉矩陣
		selfRotateMatrix=new float[16];
		//起始繞X軸旋轉-90度是為了讓桌球號碼朝向攝影機
		Matrix.setRotateM(selfRotateMatrix, 0, -90, 1, 0, 0);
	}	
	public void drawSelf(int texId, int tableTexId, int isShadow)
	{
		//繪制物體自己	
		MatrixState.pushMatrix();
		//搬移到指定位置
		MatrixState.translate(xOffsetLock, BALL_Y*yOffset, zOffsetLock);
		//加上附帶旋轉矩陣
		MatrixState.matrix(selfRotateMatrix);

		//繪制球或影子
		btv.drawSelf(texId, tableTexId, isShadow);
		MatrixState.popMatrix();
	}	
	//球按照目前速度向前運動一步的方法
	public void go(ArrayList<BallKongZhi> ballAl)
	{	
		//計算總速度
		float vTotal=(float)Math.sqrt(vx*vx+vz*vz);			
		//速度真為0不需要go
		if(vTotal==0)
		{
			return;
		}
		
		//記錄舊位置
		float tempX=xOffset;
		float tempZ=zOffset;
		
		//若總速度不小於設定值，則計算球下一步的位置
		xOffset=xOffset+vx*TIME_SPAN;
		zOffset=zOffset+vz*TIME_SPAN;
		
		boolean syFlag=false;//臨時音效播放標示
		
		boolean flag=false;//是否與別的球碰撞  false-未碰撞		
		boolean isCollideWithTableFlag = false;//是否和球台碰撞的標志位
		
		//計算下一步的位置後判斷是否與別的球碰撞
		for(int i=0;i<ballAl.size();i++)
		{			
			BallKongZhi bfcL=ballAl.get(i);
			
			if(i == 1)
			{
				minId = bfcL.id;	//取得編號最小的球的id
			}
			
			if(bfcL!=this)
			{
				boolean tempFlag=PZJCUtil.jiSuanPengZhuang(this, bfcL);
				
				//若果在某一次擊球過程中發生了碰撞，並且是主球撞擊其他球的第一次碰撞
				if(tempFlag && (this.id == 0)&&(Constant.FIRST_FIGHT_ERROR == false)
						&& (minId!=0)
				)
				{
					if(bfcL.id == minId)
					{
						Constant.MAINBALL_FIRST_FIGHT = true;
					}

					if(bfcL.id>minId&&(Constant.MAINBALL_FIRST_FIGHT==false))
					{	
						Constant.FIRST_FIGHT_ERROR = true;
						
					}
				}
				
				if(tempFlag)
				{
					flag=true;	
					//********************未擊中目的犯規begin*********************
					//在一次擊球過程中是否與其他球發生碰撞的標志位
					Constant.IS_FIGHT=true;
					//********************未擊中目的犯規end*********************
				}
			}	
		}
		
		//播放碰撞音效
		if(!syFlag&&flag)
		{
			syFlag=true;
			if(mSurfaceView.activity.isSoundOn()){//播放球碰撞的音效
				mSurfaceView.activity.playSound(2, 0, vTotal/Constant.V_BALL_MAX);//球球相撞的音效
			}
		}	
		
		//撞a角
		float aX=-TABLE_AREA_WIDTH/2;
		float aZ=TABLE_AREA_LENGTH/2-EDGE_SMALL;     //設定A點座標   
		float disSquare=(this.xOffset-aX)*(this.xOffset-aX)+(this.zOffset-aZ)*(this.zOffset-aZ);//計算距離A的距離
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;//速度設反
			flag=true;//碰撞標志為設為true
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞b角
		float bX=-TABLE_AREA_WIDTH/2;
		float bZ=MIDDLE/2;        
		disSquare=(this.xOffset-bX)*(this.xOffset-bX)+(this.zOffset-bZ)*(this.zOffset-bZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞c角
		float cX=-TABLE_AREA_WIDTH/2;
		float cZ=-MIDDLE/2;        
		disSquare=(this.xOffset-cX)*(this.xOffset-cX)+(this.zOffset-cZ)*(this.zOffset-cZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞d角
		float dX=-TABLE_AREA_WIDTH/2;
		float dZ=-TABLE_AREA_LENGTH/2+EDGE_SMALL;        
		disSquare=(this.xOffset-dX)*(this.xOffset-dX)+(this.zOffset-dZ)*(this.zOffset-dZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞e角
		float eX=-TABLE_AREA_WIDTH/2+EDGE_SMALL;
		float eZ=-TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-eX)*(this.xOffset-eX)+(this.zOffset-eZ)*(this.zOffset-eZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞f角
		float fX=TABLE_AREA_WIDTH/2-EDGE_SMALL;
		float fZ=-TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-fX)*(this.xOffset-fX)+(this.zOffset-fZ)*(this.zOffset-fZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞g角
		float gX=TABLE_AREA_WIDTH/2;
		float gZ=-TABLE_AREA_LENGTH/2+EDGE_SMALL;        
		disSquare=(this.xOffset-gX)*(this.xOffset-gX)+(this.zOffset-gZ)*(this.zOffset-gZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞h角
		float hX=TABLE_AREA_WIDTH/2;
		float hZ=-MIDDLE/2;        
		disSquare=(this.xOffset-hX)*(this.xOffset-hX)+(this.zOffset-hZ)*(this.zOffset-hZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞i角
		float iX=TABLE_AREA_WIDTH/2;
		float iZ=MIDDLE/2;        
		disSquare=(this.xOffset-iX)*(this.xOffset-iX)+(this.zOffset-iZ)*(this.zOffset-iZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞j角
		float jX=TABLE_AREA_WIDTH/2;
		float jZ=TABLE_AREA_LENGTH/2-EDGE_SMALL;        
		disSquare=(this.xOffset-jX)*(this.xOffset-jX)+(this.zOffset-jZ)*(this.zOffset-jZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞k角
		float kX=TABLE_AREA_WIDTH/2-EDGE_SMALL;
		float kZ=TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-kX)*(this.xOffset-kX)+(this.zOffset-kZ)*(this.zOffset-kZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//撞l角
		float lX=-TABLE_AREA_WIDTH/2+EDGE_SMALL;
		float lZ=TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-lX)*(this.xOffset-lX)+(this.zOffset-lZ)*(this.zOffset-lZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//與球台碰撞標志設為true
		}
		
		//若果了角就不考慮撞邊
		if(flag==false)
		{
			//判斷是否與四個球台壁碰撞
			if(this.zOffset<-BOTTOM_LENGTH/2f+BALL_R||this.zOffset>BOTTOM_LENGTH/2f-BALL_R)//外圍
			{
				//碰左擋板或右擋板，Z向速度置反
				this.vz=-this.vz;
				flag=true;
				isCollideWithTableFlag = true;//與球台碰撞標志設為true
			}
			if(this.xOffset<-BOTTOM_WIDE/2f+BALL_R||this.xOffset>BOTTOM_WIDE/2f-BALL_R)//外圍
			{
				//碰前擋板或後擋板，X向速度置反
				this.vx=-this.vx;
				flag=true;
				isCollideWithTableFlag = true;//與球台碰撞標志設為true
			}
			
			if(this.zOffset>MIDDLE/2&&this.zOffset<BOTTOM_LENGTH/2-EDGE)//內圍左側
			{
				if(this.xOffset>TABLE_AREA_WIDTH/2-BALL_R||this.xOffset<-TABLE_AREA_WIDTH/2+BALL_R)//上下碰撞檢驗
				{
					this.vx=-this.vx;				
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
			}
			if(this.zOffset<-MIDDLE/2&&this.zOffset>-BOTTOM_LENGTH/2+EDGE)//內側右側
			{
				if(this.xOffset>TABLE_AREA_WIDTH/2-BALL_R||this.xOffset<-TABLE_AREA_WIDTH/2+BALL_R)//上下碰撞檢驗
				{
					this.vx=-this.vx;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
			}
			if(this.xOffset>-BOTTOM_WIDE/2+EDGE&&this.xOffset<BOTTOM_WIDE/2-EDGE)//內側 左右碰撞檢驗
			{
				if(this.zOffset>TABLE_AREA_LENGTH/2-BALL_R||this.zOffset<-TABLE_AREA_LENGTH/2+BALL_R)
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
			}
			
			//洞口線ab、cd的碰撞檢驗
			if(this.xOffset>-BOTTOM_WIDE/2&&this.xOffset<-TABLE_AREA_WIDTH/2)
			{
				//ab線，其中在ab之間設有一虛線，其實際作用為避免發生檢驗錯誤（現象為球為走到碰撞點就發生碰撞，相傳鬧鬼事件）
				if((this.zOffset>MIDDLE/2-BALL_R&&this.zOffset<TABLE_AREA_LENGTH/4)||
						(this.zOffset<BOTTOM_LENGTH/2-EDGE+BALL_R&&this.zOffset>TABLE_AREA_LENGTH/4))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
				//cd線，其中在cd之間設有一虛線，其實際作用為避免發生檢驗錯誤（現象為球為走到碰撞點就發生碰撞，相傳鬧鬼事件）
				if((this.zOffset<-MIDDLE/2+BALL_R&&this.zOffset>-TABLE_AREA_LENGTH/4)||
					(this.zOffset>-BOTTOM_LENGTH/2+EDGE-BALL_R&&this.zOffset<-TABLE_AREA_LENGTH/4))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
			}			
			
			//洞口線GH、IJ的碰撞檢驗
			if(this.xOffset>TABLE_AREA_WIDTH/2&&this.xOffset<BOTTOM_WIDE/2)
			{
				//gh線，其中在ab之間設有一虛線，其實際作用為避免發生檢驗錯誤（現象為球為走到碰撞點就發生碰撞，相傳鬧鬼事件）
				if((this.zOffset<-MIDDLE/2+BALL_R&&this.zOffset>-TABLE_AREA_LENGTH/4)||
						(this.zOffset<-TABLE_AREA_LENGTH/4&&this.zOffset>-BOTTOM_LENGTH/2+EDGE-BALL_R))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
				//ij線，其中在ij之間設有一虛線，其實際作用為避免發生檢驗錯誤（現象為球為走到碰撞點就發生碰撞，相傳鬧鬼事件）
				if((this.zOffset>MIDDLE/2-BALL_R&&this.zOffset<TABLE_AREA_LENGTH/4)||
						(this.zOffset<BOTTOM_LENGTH/2-EDGE+BALL_R&&this.zOffset>TABLE_AREA_LENGTH/4))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
				
			}
			//洞口線EF的碰撞檢驗
			if(this.zOffset>-BOTTOM_LENGTH/2&&this.zOffset<-TABLE_AREA_LENGTH/2)
			{
				//其實這兩條線不影響碰撞檢驗的正確性，因為球不可能從外面撞擊左右邊緣的豎線，但是為了避免麻煩，將其加上。
				//ef線，其中在ef之間設有一虛線，其實際作用為避免發生檢驗錯誤（現象為球為走到碰撞點就發生碰撞，相傳鬧鬼事件）
				if((this.xOffset<0&&this.xOffset>-BOTTOM_WIDE/2+EDGE-BALL_R)||
						(this.xOffset>0&&this.xOffset<BOTTOM_WIDE/2-EDGE+BALL_R))
				{
					this.vx=-this.vx;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
			}
			//洞口線KL的碰撞檢驗
			if(this.zOffset>TABLE_AREA_LENGTH/2&&this.zOffset<BOTTOM_LENGTH/2)
			{
				//其實這兩條線不影響碰撞檢驗的正確性，因為球不可能從外面撞擊左右邊緣的豎線，但是為了避免麻煩，將其加上。
				//ef線，其中在ef之間設有一虛線，其實際作用為避免發生檢驗錯誤（現象為球為走到碰撞點就發生碰撞，相傳鬧鬼事件）
				if((this.xOffset>-BOTTOM_WIDE/2+EDGE-BALL_R&&this.xOffset<0)||
						this.xOffset>0&&this.xOffset<BOTTOM_WIDE/2-EDGE+BALL_R)
				{
					this.vx=-this.vx;
					flag=true;
					isCollideWithTableFlag = true;//與球台碰撞標志設為true
				}
			}
		}
		if(flag==false)
		{//沒有碰撞
			//計算球此步運動的距離
			distance=(float)vTotal*TIME_SPAN;
			//根據運動的距離計算出球需要卷動的角度
			angleTemp=(float)Math.toDegrees(distance/(BALL_R));		
			//計算卷動時繞著轉的軸的向量
			axisX=vz;
			axisZ=-vx;
			//每搬移一步後速度需要衰減
			vx=vx*V_TENUATION;
			vz=vz*V_TENUATION;
			
			//繞旋轉軸旋轉（旋轉軸垂直與運動方向，平行於桌面）
			//旋轉時要求角度不為0且軸不能全為0
			if(Math.abs(angleTemp)!=0&&(Math.abs(axisX)!=0||Math.abs(axisY)!=0||Math.abs(axisZ)!=0))
			{
				float[] newMatrix=new float[16];
				Matrix.setRotateM(newMatrix, 0, angleTemp, axisX, axisY, axisZ);
				float[] resultMatrix=new float[16];
				Matrix.multiplyMM(resultMatrix, 0, newMatrix, 0, selfRotateMatrix,0);
				selfRotateMatrix=resultMatrix;
			}
		}
		else
		{//碰撞了則不能搬移
			xOffset=tempX;
			zOffset=tempZ;
			//若果和球台碰撞，目前碰撞次數歸零
			if(isCollideWithTableFlag){
				Constant.collisionCount = 0;
			}
		}	 
		
		//當總速度小於設定值時認為球停止了
		if(vTotal<V_THRESHOLD)
		{
			distance=0;
			vx=0;
			vz=0;
			return;
		} 
	}
}


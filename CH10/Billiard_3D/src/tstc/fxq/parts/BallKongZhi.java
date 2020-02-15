package tstc.fxq.parts;
import java.util.ArrayList;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.PZJCUtil;
import tstc.fxq.utils.MatrixState;
import android.opengl.Matrix;
import static tstc.fxq.constants.Constant.*;
//�Ω󱱨��y
public class BallKongZhi 
{	
	public int id; //�Ω�ХܨC�@�Ӳy��id
	int minId = 0;	//�s���̤p���y��id
	
	MySurfaceView mSurfaceView;//�إ�MySurfaceView���Ѧ� 
	BallDingDian btv;//�Ω�ø���y	
	
	public float xOffset;//��y��X��m
	public float zOffset;//��y��Z��m
	public float yOffset=1;//��y��y��m
	//��y���t�ס]��y�O�@���b�ୱ�W�B�ʡA�]���S��Y�V�t�ס^
	public float vx;
	public float vz;
	//��y���ʹL�{�����U���Z�ʭ�
	float angleTemp;//�����Z�ʭ�
	float axisX;//����b�V�q��X���q
	float axisZ;//����b�V�q��Z���q
	float axisY=0;//����b�V�q��Y���q�]�]���y�b�ୱ�W�B�ʱ���b�����ୱ�A�]���S��Y���q�^
	float distance;//������y�h�����Z��	
	float angleX;//¶X�b��ʪ�����
	float angleY;//¶Y�b��ʪ�����
	float angleZ;//¶Z�b��ʪ�����
	
	float[] selfRotateMatrix;//���a����x�}
	
	//��w���ث��A
	float xOffsetLock;//��y��X��m
	float zOffsetLock;//��y��Z��m
	public void lockState()
	{
		xOffsetLock=xOffset;
		zOffsetLock=zOffset;
	} 
	//����y�x�W�y�_�l��m���`��
	static final float xBall1=0f;//�@���y����m
	static final float zBall1=Constant.MAIN_BALL_INIT_ZOFFSET - Constant.DIS_WITH_MAIN_BALL;
	static final float xBallDis=(float) ((Constant.BALL_R*2+Constant.GAP_BETWEEN_BALLS)*Math.cos(Math.PI/3));//�׵۪��y�P�y����x��V�Z��
	static final float zBallDis=(float) ((Constant.BALL_R*2+Constant.GAP_BETWEEN_BALLS)*Math.sin(Math.PI/3));//�׵۪��y�P�y����y��V�Z��
	static final float xDis=Constant.BALL_R*2+Constant.GAP_BETWEEN_BALLS;//�C�@�C�y���Z��
	//�Ҧ��y����m�A0�����y�A�H��̦��q����k�A�q�U��W
	public static final float[][][] AllBallsPos=
	{
		{//8�y�Ҧ��ɡA�U�y����m
			new float[]{xBall1, Constant.MAIN_BALL_INIT_ZOFFSET},//���y��m
			new float[]{xBall1, zBall1},//1�C
			new float[]{xBall1+xBallDis, zBall1-zBallDis},//2�C
			new float[]{xBall1+xBallDis-xDis, zBall1-zBallDis},
			new float[]{xBall1+xBallDis*2, zBall1-zBallDis*2},//3�C
			new float[]{xBall1+xBallDis*2-xDis, zBall1-zBallDis*2},
			new float[]{xBall1+xBallDis*2-xDis*2, zBall1-zBallDis*2},
			
			new float[]{xBall1+xBallDis*3, zBall1-zBallDis*3},//4�C
			new float[]{xBall1+xBallDis*3-xDis, zBall1-zBallDis*3},
			new float[]{xBall1+xBallDis*3-xDis*2, zBall1-zBallDis*3},
			new float[]{xBall1+xBallDis*3-xDis*3, zBall1-zBallDis*3},
			new float[]{xBall1+xBallDis*4, zBall1-zBallDis*4},//5�C
			new float[]{xBall1+xBallDis*4-xDis, zBall1-zBallDis*4},
			new float[]{xBall1+xBallDis*4-xDis*2, zBall1-zBallDis*4},
			new float[]{xBall1+xBallDis*4-xDis*3, zBall1-zBallDis*4},
			new float[]{xBall1+xBallDis*4-xDis*4, zBall1-zBallDis*4},
		},
		{//9�y�Ҧ��ɡA�U�y����m
			new float[]{xBall1, Constant.MAIN_BALL_INIT_ZOFFSET},//���y��m
			new float[]{xBall1, zBall1},//1�C
			new float[]{xBall1+xBallDis, zBall1-zBallDis},//2�C
			new float[]{xBall1+xBallDis-xDis, zBall1-zBallDis},
			new float[]{xBall1+xBallDis*2, zBall1-zBallDis*2},//3�C
			new float[]{xBall1+xBallDis*2-xDis, zBall1-zBallDis*2},
			new float[]{xBall1+xBallDis*2-xDis*2, zBall1-zBallDis*2},
	
			new float[]{xBall1+xBallDis, zBall1-zBallDis*3},//4�C
			new float[]{xBall1+xBallDis-xDis, zBall1-zBallDis*3},
			new float[]{xBall1, zBall1-zBallDis*4},//5�C
		}
	};
	
	public BallKongZhi(BallDingDian btv,float xOffset,float zOffset,MySurfaceView mSurfaceView,int id)
	{
		this.id=id;
		
		this.btv=btv;
		this.xOffset=xOffset;
		this.zOffset=zOffset;
		this.mSurfaceView=mSurfaceView;
		//�_�l�ɬ���w��m������
		xOffsetLock=xOffset;
		zOffsetLock=zOffset;
		//�_�l�ƪ��a����x�}
		selfRotateMatrix=new float[16];
		//�_�l¶X�b����-90�׬O���F����y���X�¦V��v��
		Matrix.setRotateM(selfRotateMatrix, 0, -90, 1, 0, 0);
	}	
	public void drawSelf(int texId, int tableTexId, int isShadow)
	{
		//ø���ۤv	
		MatrixState.pushMatrix();
		//�h������w��m
		MatrixState.translate(xOffsetLock, BALL_Y*yOffset, zOffsetLock);
		//�[�W���a����x�}
		MatrixState.matrix(selfRotateMatrix);

		//ø��y�μv�l
		btv.drawSelf(texId, tableTexId, isShadow);
		MatrixState.popMatrix();
	}	
	//�y���ӥثe�t�צV�e�B�ʤ@�B����k
	public void go(ArrayList<BallKongZhi> ballAl)
	{	
		//�p���`�t��
		float vTotal=(float)Math.sqrt(vx*vx+vz*vz);			
		//�t�ׯu��0���ݭngo
		if(vTotal==0)
		{
			return;
		}
		
		//�O���¦�m
		float tempX=xOffset;
		float tempZ=zOffset;
		
		//�Y�`�t�פ��p��]�w�ȡA�h�p��y�U�@�B����m
		xOffset=xOffset+vx*TIME_SPAN;
		zOffset=zOffset+vz*TIME_SPAN;
		
		boolean syFlag=false;//�{�ɭ��ļ���Х�
		
		boolean flag=false;//�O�_�P�O���y�I��  false-���I��		
		boolean isCollideWithTableFlag = false;//�O�_�M�y�x�I�����ЧӦ�
		
		//�p��U�@�B����m��P�_�O�_�P�O���y�I��
		for(int i=0;i<ballAl.size();i++)
		{			
			BallKongZhi bfcL=ballAl.get(i);
			
			if(i == 1)
			{
				minId = bfcL.id;	//���o�s���̤p���y��id
			}
			
			if(bfcL!=this)
			{
				boolean tempFlag=PZJCUtil.jiSuanPengZhuang(this, bfcL);
				
				//�Y�G�b�Y�@�����y�L�{���o�ͤF�I���A�åB�O�D�y������L�y���Ĥ@���I��
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
					//********************�������ت��ǳWbegin*********************
					//�b�@�����y�L�{���O�_�P��L�y�o�͸I�����ЧӦ�
					Constant.IS_FIGHT=true;
					//********************�������ت��ǳWend*********************
				}
			}	
		}
		
		//����I������
		if(!syFlag&&flag)
		{
			syFlag=true;
			if(mSurfaceView.activity.isSoundOn()){//����y�I��������
				mSurfaceView.activity.playSound(2, 0, vTotal/Constant.V_BALL_MAX);//�y�y�ۼ�������
			}
		}	
		
		//��a��
		float aX=-TABLE_AREA_WIDTH/2;
		float aZ=TABLE_AREA_LENGTH/2-EDGE_SMALL;     //�]�wA�I�y��   
		float disSquare=(this.xOffset-aX)*(this.xOffset-aX)+(this.zOffset-aZ)*(this.zOffset-aZ);//�p��Z��A���Z��
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;//�t�׳]��
			flag=true;//�I���ЧӬ��]��true
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��b��
		float bX=-TABLE_AREA_WIDTH/2;
		float bZ=MIDDLE/2;        
		disSquare=(this.xOffset-bX)*(this.xOffset-bX)+(this.zOffset-bZ)*(this.zOffset-bZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��c��
		float cX=-TABLE_AREA_WIDTH/2;
		float cZ=-MIDDLE/2;        
		disSquare=(this.xOffset-cX)*(this.xOffset-cX)+(this.zOffset-cZ)*(this.zOffset-cZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��d��
		float dX=-TABLE_AREA_WIDTH/2;
		float dZ=-TABLE_AREA_LENGTH/2+EDGE_SMALL;        
		disSquare=(this.xOffset-dX)*(this.xOffset-dX)+(this.zOffset-dZ)*(this.zOffset-dZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��e��
		float eX=-TABLE_AREA_WIDTH/2+EDGE_SMALL;
		float eZ=-TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-eX)*(this.xOffset-eX)+(this.zOffset-eZ)*(this.zOffset-eZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��f��
		float fX=TABLE_AREA_WIDTH/2-EDGE_SMALL;
		float fZ=-TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-fX)*(this.xOffset-fX)+(this.zOffset-fZ)*(this.zOffset-fZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��g��
		float gX=TABLE_AREA_WIDTH/2;
		float gZ=-TABLE_AREA_LENGTH/2+EDGE_SMALL;        
		disSquare=(this.xOffset-gX)*(this.xOffset-gX)+(this.zOffset-gZ)*(this.zOffset-gZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��h��
		float hX=TABLE_AREA_WIDTH/2;
		float hZ=-MIDDLE/2;        
		disSquare=(this.xOffset-hX)*(this.xOffset-hX)+(this.zOffset-hZ)*(this.zOffset-hZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��i��
		float iX=TABLE_AREA_WIDTH/2;
		float iZ=MIDDLE/2;        
		disSquare=(this.xOffset-iX)*(this.xOffset-iX)+(this.zOffset-iZ)*(this.zOffset-iZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��j��
		float jX=TABLE_AREA_WIDTH/2;
		float jZ=TABLE_AREA_LENGTH/2-EDGE_SMALL;        
		disSquare=(this.xOffset-jX)*(this.xOffset-jX)+(this.zOffset-jZ)*(this.zOffset-jZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��k��
		float kX=TABLE_AREA_WIDTH/2-EDGE_SMALL;
		float kZ=TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-kX)*(this.xOffset-kX)+(this.zOffset-kZ)*(this.zOffset-kZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//��l��
		float lX=-TABLE_AREA_WIDTH/2+EDGE_SMALL;
		float lZ=TABLE_AREA_LENGTH/2;        
		disSquare=(this.xOffset-lX)*(this.xOffset-lX)+(this.zOffset-lZ)*(this.zOffset-lZ);
		if(disSquare<BALL_R_POWER_TWO)
		{
			this.vz=-this.vz;
			this.vx=-this.vx;
			flag=true;
			isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
		}
		
		//�Y�G�F���N���Ҽ{����
		if(flag==false)
		{
			//�P�_�O�_�P�|�Ӳy�x���I��
			if(this.zOffset<-BOTTOM_LENGTH/2f+BALL_R||this.zOffset>BOTTOM_LENGTH/2f-BALL_R)//�~��
			{
				//�I���תO�Υk�תO�AZ�V�t�׸m��
				this.vz=-this.vz;
				flag=true;
				isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
			}
			if(this.xOffset<-BOTTOM_WIDE/2f+BALL_R||this.xOffset>BOTTOM_WIDE/2f-BALL_R)//�~��
			{
				//�I�e�תO�Ϋ�תO�AX�V�t�׸m��
				this.vx=-this.vx;
				flag=true;
				isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
			}
			
			if(this.zOffset>MIDDLE/2&&this.zOffset<BOTTOM_LENGTH/2-EDGE)//���򥪰�
			{
				if(this.xOffset>TABLE_AREA_WIDTH/2-BALL_R||this.xOffset<-TABLE_AREA_WIDTH/2+BALL_R)//�W�U�I������
				{
					this.vx=-this.vx;				
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
			}
			if(this.zOffset<-MIDDLE/2&&this.zOffset>-BOTTOM_LENGTH/2+EDGE)//�����k��
			{
				if(this.xOffset>TABLE_AREA_WIDTH/2-BALL_R||this.xOffset<-TABLE_AREA_WIDTH/2+BALL_R)//�W�U�I������
				{
					this.vx=-this.vx;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
			}
			if(this.xOffset>-BOTTOM_WIDE/2+EDGE&&this.xOffset<BOTTOM_WIDE/2-EDGE)//���� ���k�I������
			{
				if(this.zOffset>TABLE_AREA_LENGTH/2-BALL_R||this.zOffset<-TABLE_AREA_LENGTH/2+BALL_R)
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
			}
			
			//�}�f�uab�Bcd���I������
			if(this.xOffset>-BOTTOM_WIDE/2&&this.xOffset<-TABLE_AREA_WIDTH/2)
			{
				//ab�u�A�䤤�bab�����]���@��u�A���ڧ@�ά��קK�o��������~�]�{�H���y������I���I�N�o�͸I���A�۶Ǿx���ƥ�^
				if((this.zOffset>MIDDLE/2-BALL_R&&this.zOffset<TABLE_AREA_LENGTH/4)||
						(this.zOffset<BOTTOM_LENGTH/2-EDGE+BALL_R&&this.zOffset>TABLE_AREA_LENGTH/4))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
				//cd�u�A�䤤�bcd�����]���@��u�A���ڧ@�ά��קK�o��������~�]�{�H���y������I���I�N�o�͸I���A�۶Ǿx���ƥ�^
				if((this.zOffset<-MIDDLE/2+BALL_R&&this.zOffset>-TABLE_AREA_LENGTH/4)||
					(this.zOffset>-BOTTOM_LENGTH/2+EDGE-BALL_R&&this.zOffset<-TABLE_AREA_LENGTH/4))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
			}			
			
			//�}�f�uGH�BIJ���I������
			if(this.xOffset>TABLE_AREA_WIDTH/2&&this.xOffset<BOTTOM_WIDE/2)
			{
				//gh�u�A�䤤�bab�����]���@��u�A���ڧ@�ά��קK�o��������~�]�{�H���y������I���I�N�o�͸I���A�۶Ǿx���ƥ�^
				if((this.zOffset<-MIDDLE/2+BALL_R&&this.zOffset>-TABLE_AREA_LENGTH/4)||
						(this.zOffset<-TABLE_AREA_LENGTH/4&&this.zOffset>-BOTTOM_LENGTH/2+EDGE-BALL_R))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
				//ij�u�A�䤤�bij�����]���@��u�A���ڧ@�ά��קK�o��������~�]�{�H���y������I���I�N�o�͸I���A�۶Ǿx���ƥ�^
				if((this.zOffset>MIDDLE/2-BALL_R&&this.zOffset<TABLE_AREA_LENGTH/4)||
						(this.zOffset<BOTTOM_LENGTH/2-EDGE+BALL_R&&this.zOffset>TABLE_AREA_LENGTH/4))
				{
					this.vz=-this.vz;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
				
			}
			//�}�f�uEF���I������
			if(this.zOffset>-BOTTOM_LENGTH/2&&this.zOffset<-TABLE_AREA_LENGTH/2)
			{
				//���o����u���v�T�I�����窺���T�ʡA�]���y���i��q�~���������k��t���ݽu�A���O���F�קK�·СA�N��[�W�C
				//ef�u�A�䤤�bef�����]���@��u�A���ڧ@�ά��קK�o��������~�]�{�H���y������I���I�N�o�͸I���A�۶Ǿx���ƥ�^
				if((this.xOffset<0&&this.xOffset>-BOTTOM_WIDE/2+EDGE-BALL_R)||
						(this.xOffset>0&&this.xOffset<BOTTOM_WIDE/2-EDGE+BALL_R))
				{
					this.vx=-this.vx;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
			}
			//�}�f�uKL���I������
			if(this.zOffset>TABLE_AREA_LENGTH/2&&this.zOffset<BOTTOM_LENGTH/2)
			{
				//���o����u���v�T�I�����窺���T�ʡA�]���y���i��q�~���������k��t���ݽu�A���O���F�קK�·СA�N��[�W�C
				//ef�u�A�䤤�bef�����]���@��u�A���ڧ@�ά��קK�o��������~�]�{�H���y������I���I�N�o�͸I���A�۶Ǿx���ƥ�^
				if((this.xOffset>-BOTTOM_WIDE/2+EDGE-BALL_R&&this.xOffset<0)||
						this.xOffset>0&&this.xOffset<BOTTOM_WIDE/2-EDGE+BALL_R)
				{
					this.vx=-this.vx;
					flag=true;
					isCollideWithTableFlag = true;//�P�y�x�I���Чӳ]��true
				}
			}
		}
		if(flag==false)
		{//�S���I��
			//�p��y���B�B�ʪ��Z��
			distance=(float)vTotal*TIME_SPAN;
			//�ھڹB�ʪ��Z���p��X�y�ݭn���ʪ�����
			angleTemp=(float)Math.toDegrees(distance/(BALL_R));		
			//�p����ʮ�¶���઺�b���V�q
			axisX=vz;
			axisZ=-vx;
			//�C�h���@�B��t�׻ݭn�I��
			vx=vx*V_TENUATION;
			vz=vz*V_TENUATION;
			
			//¶����b����]����b�����P�B�ʤ�V�A�����ୱ�^
			//����ɭn�D���פ���0�B�b�������0
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
		{//�I���F�h����h��
			xOffset=tempX;
			zOffset=tempZ;
			//�Y�G�M�y�x�I���A�ثe�I�������k�s
			if(isCollideWithTableFlag){
				Constant.collisionCount = 0;
			}
		}	 
		
		//���`�t�פp��]�w�Ȯɻ{���y����F
		if(vTotal<V_THRESHOLD)
		{
			distance=0;
			vx=0;
			vz=0;
			return;
		} 
	}
}


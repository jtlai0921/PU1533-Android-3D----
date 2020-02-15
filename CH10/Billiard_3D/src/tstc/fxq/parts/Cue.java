package tstc.fxq.parts;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.Sight;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import static tstc.fxq.constants.Constant.*;
/*
 * �y��A�Ѩ�Ӷ�P�@�Ӥ@�Y�j�@�Y�p����W�s�զ�
 */

public class Cue {
	
	MySurfaceView mv;//MySurfaceView���O���Ѧ�
	float size;//�y��j�p�����
	//�y���_������
	private float angdegElevation = 4;
	
	//�ϲy��¶�@�ɮy�Шt��y�b����ɡA�������y�쪺����y�Шt���b
	private float axisX = (float) Math.cos(Math.toRadians(angdegElevation));
	private float axisY = (float) Math.sin(Math.toRadians(angdegElevation));
	
	//�y�쪺���ਤ��
	float angle;
	
	//��o�y����ਤ�ת���k
	public float getAngle() {
		//�Y���Ĥ@�H�٨��סA�Ǧ^��v�������ਤ��
		if(mv.currSight == Sight.first){
			return mv.angdegAzimuth;
		}
		//�Y���ۥѨ��סA�Ǧ^�y�쪺���ਤ��
		else{
			return angle;
		}
	}
	//�]�w�y����ਤ�ת���k
	public void setAngle(float angle) {
		//�Y���Ĥ@�H�٨��סA�N�y����ਤ�סA�ഫ����v������쨤
		if(mv.currSight == Sight.first){			
			mv.angdegAzimuth = angle;
		}
		//�Y���ۥѨ��סA�]�w�y�쪺���ਤ
		else{
			this.angle = angle;
		}
	}
	
	CueSide cueSide;//��������W
	Circle circleBig;//�y��W����y
	Circle circleSmall;//�y��W����y
	//��v����쨤����w��
	private float angdegAzimuthLock;
	//��w�ثe����v����쨤����k
	public void lockAngdegAzimuth(float angdegAzimuth){
		angdegAzimuthLock = angdegAzimuth;
	}
	public Cue(MySurfaceView mv,float size)
	{
		this.mv=mv;
		this.size=size;
		cueSide=new CueSide(mv, 0.04f*size,0.08f*size,5f*size,3*size,0);//�إ߲y�쪫��
		circleBig=new Circle(mv,0.24f*size*size/Constant.TABLE_UNIT_SIZE, 0.24f*size*size/Constant.TABLE_UNIT_SIZE);
		circleSmall = new Circle(mv,0.12f*size*size/Constant.TABLE_UNIT_SIZE, 0.12f*size*size/Constant.TABLE_UNIT_SIZE);
	}
	public void drawSelf(BallKongZhi mainBall, int texId1,int texId2, int texId3)
	{
		MatrixState.pushMatrix(); 
		/*
		 * �N�y��h����ղy�B
		 * 
		 * �b�]�p���ɭԡA�y���_�l��m�M�ୱ�t90��
		 * �Gø��y�ɾ���uy�b����F-90��
		 * �y�Ф]�n�����ഫ������᪺
		 */
		MatrixState.translate(-mainBall.zOffset, BALL_Y, mainBall.xOffset);
		MatrixState.rotate(90-angdegElevation, 0, 0, 1);
		//�Y���Ĥ@�H�٨��סA����v�������ױ���
		if(mv.currSight == Sight.first){
			MatrixState.rotate(angdegAzimuthLock, axisX, axisY, 0);
		}
		//�Y���ۥѨ��סA���y�쪺���ױ���
		else{
			MatrixState.rotate(angle, axisX, axisY, 0);
		}
		MatrixState.translate(0, CUE_Y_ADJUST, 0);
		
        MatrixState.pushMatrix();     
        MatrixState.rotate(180, 0, 1, 0);
		cueSide.drawSelf(texId1);//ø��y��
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, 15*size, 0);
		circleBig.drawSelf(texId2);//ø����
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, 2*size, 0);
		MatrixState.rotate(180, 1, 0, 0);
		circleSmall.drawSelf(texId3);//ø����
		MatrixState.popMatrix();
		
		MatrixState.popMatrix(); 
	}

}

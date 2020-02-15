package com.cw.util;
public class IntersectantUtil {	
	/*
	 * 1�B�z�L�b�ù��W��Ĳ����m�A�p��������񥭭��W�y�СA
	 * �H�K�D�XAB���I�b��v���y�Шt�����y��
	 * 2�B�NAB���I�b��v�����y�Шt�����y�Э��H��v���x�}���f�x�}�A
	 * �H�K�D�oAB���I�b�@�ɮy�Шt�����y��
	 */
	public static float[] calculateABPosition
	(
		float x,//Ĳ�N�ù�X�y��
		float y,//Ĳ�N�ù�Y�y��
		float w,// �ù��e��
		float h,// �ù�����
		float left,//����left��
		float top,//����top��
		float near,//����near��
		float far//����far��
	)
	{
		//�D���f���y�Ф��ߦb���I�ɡAĲ���I���y��
		float x0=x-w/2;
		float y0=h/2-y;		
		//�p�������near���W��x�By�y��
		float xNear=2*x0*left/w;
		float yNear=2*y0*top/h;
		//�p�������far���W��x�By�y��
		float ratio=far/near;
		float xFar=ratio*xNear;
		float yFar=ratio*yNear;
		//��v���y�Шt��A���y��
        float ax=xNear;
        float ay=yNear;
        float az=-near;
        //��v���y�Шt��B���y��
        float bx=xFar;
        float by=yFar;
        float bz=-far; 
        //�z�L��v���y�Шt��A�BB���I���y�СA�D�@�ɮy�Шt��A�BB���I���y��
		float[] A = MatrixState.fromPtoPreP(new float[] { ax, ay, az });
		float[] B = MatrixState.fromPtoPreP(new float[] { bx, by, bz });
		return new float[] {//�Ǧ^�̲ת�AB���I�y��
			A[0],A[1],A[2],
			B[0],B[1],B[2]
		};
	}
}
package com.bn.gjxq.manager;
import static com.bn.gjxq.Constant.*;
public class IntersectantUtil 
{	
	/*
	 * 1�B�z�L�b�ù��W��Ĳ����m�A�p��������񥭭��W�y�СA
	 * �H�K�D�XAB���I�b��v���y�Шt�����y��
	 * 2�B�NAB���I�b��v�����y�Шt�����y�Э��H��v���x�}���f�x�}�A
	 * �H�K�D�oAB���I�b�@�ɮy�Шt�����y��
	 */
	public static Vector3f[] calculateABPosition
	(
		float x, 			//Ĳ�N�ù�X�y��
		float y, 			//Ĳ�N�ù�Y�y��
		float[] cData		//��v��9�Ѽ�
	)
	{
		//�D���f���y�Ф��ߦb���I�ɡAĲ���I���y��
		float x0=x-screenWidth/2;
		float y0=screenHeight/2-y;		
		//�p�������near���W��x�By�y��
		float xNear=2*x0*ratio/screenWidth;
		float yNear=2*y0*top/screenHeight;
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
        
        MatrixUtil.setCamera//��v��9�Ѽ�
        (
        		cData[0],cData[1],cData[2],
        		cData[3],cData[4],cData[5],
        		cData[6],cData[7],cData[8]
        );
        
        //�z�L��v���y�Шt��A�BB���I���y�СA�D�@�ɮy�Шt��A�BB���I���y��
		Vector3f start = MatrixUtil.fromGToO
		(
			new Vector3f( ax, ay, az ),
			MatrixUtil.mVMatrix
		);
		Vector3f end = MatrixUtil.fromGToO
		(
			new Vector3f( bx, by, bz ),
			MatrixUtil.mVMatrix
		);
		return new Vector3f[] {//�Ǧ^�̲ת�AB���I�y��
			start,
			end
		};
	}

	//�D�B���g�u�q�P���w����]�򲰤��ӭ����I���̪��I�Z��
	public static float calObjectDisMin
	(
			Vector3f cameraPosition,	//��v���b�@�ɮy�Шt����m
			Vector3f start,				//A�I�b�@�ɮy�Шt������m�]��OAB�u�q���_�l�I�^
			Vector3f end,				//B�I�b�@�ɮy�Ф�����m�]��OAB�u�q�����I�^
			float[] mo,					//������ܴ��x�}
			AABB3 aabb					//�]��
	)
	{
		float disMin=Float.MAX_VALUE;
		
		//�N��v���b�@�ɮy�Шt�����I�ܴ��쪫��y�Шt
		Vector3f cameraObject=MatrixUtil.fromGToO(cameraPosition, mo);
		
		//�N�B���u�qA�I�ܴ��쪫��y�Шt
		Vector3f startObject=MatrixUtil.fromGToO(start, mo);
		
		//�N�B���u�qB�I�ܨ쪫��y�Шt
		Vector3f endObject=MatrixUtil.fromGToO(end, mo);
		
		//�p��X�g�u�ѼƤ�{������d�A�Yend-start
	   	Vector3f dv=endObject.minus(startObject);
	   	
	   	//�D�P�]��xMin�������I
	   	float t=(aabb.minX-startObject.x)/dv.x;//�D�X�ײvt
		if(t>=0&&t<=1)//�Y�G�ײv���b�o�ӽd��O�S���N�q���A�]��AB�O�u�q
		{
			float y=t*dv.y+startObject.y;//�D�Xxy�y��
			float z=t*dv.z+startObject.z;
			
			//�Y�Gxy�y�Цb�ʦ�Mxy�̤j�ȻP�̤p�Ȥ���
			if(y<=aabb.maxY&&y>=aabb.minY&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float x=aabb.minX;//x�y��
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//�D�X��v���PAB�M�]�򲰥��I���̤p�Z��
				if(disTemp<disMin)//�Y�GdisTemp�p��disMin�A�h��sdisMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//�D�P�]��xMax�������I
	   	t=(aabb.maxX-startObject.x)/dv.x;//�ײvt
		if(t>=0&&t<=1)
		{
			float y=t*dv.y+startObject.y;//yz�y��
			float z=t*dv.z+startObject.z;
			
			//�Y�Gyz�y�Цb�]�򲰳̤j�ȻP�̤p�Ƚd��
			if(y<=aabb.maxY&&y>=aabb.minY&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float x=aabb.maxX;//x�y��
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//�D�X��v���PAB�M�]�򲰥��I���̤p�Z��
				if(disTemp<disMin)//�Y�GdisTemp�p��disMin�A�h��sdisMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//�D�P�]��yMin�������I
	   	t=(aabb.minY-startObject.y)/dv.y;//�ײvt
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xz�y��
			float z=t*dv.z+startObject.z;
			
			//�Y�Gxz�y�Цb�]�򲰪��̤j�ȻP�̤p�Ȥ���
			if(x<=aabb.maxX&&x>=aabb.minX&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float y=aabb.minY;//y�y��
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//�D�X��v���PAB�M�]�򲰥��I���̤p�Z��
				if(disTemp<disMin)//�Y�GdisTemp�p��disMin�A�h��sdisMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//�D�P�]��yMax�������I
	   	t=(aabb.maxY-startObject.y)/dv.y;//�ײvt
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xz�y��
			float z=t*dv.z+startObject.z;
			
			//�Y�Gxz�b�]�򲰪��̤j�ȻP�̤p�Ȥ���
			if(x<=aabb.maxX&&x>=aabb.minX&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float y=aabb.maxY;//y�y��
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//�D�X��v���PAB�M�]�򲰥��I���̤p�Z��
				if(disTemp<disMin)//�Y�GdisTemp�p��disMin�A�h��sdisMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//�D�P�]��zMin�������I
	   	t=(aabb.minZ-startObject.z)/dv.z;//�ײvt
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xy�y��
			float y=t*dv.y+startObject.y;
			
			//�Y�Gxy�b�]�򲰳̤j�ȻP�̤p�Ȥ���
			if(x<=aabb.maxX&&x>=aabb.minX&&y<=aabb.maxY&&y>=aabb.minY)
			{
				float z=aabb.minZ;//z�y��
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//�D�X��v���PAB�M�]�򲰥��I���̤p�Z��
				if(disTemp<disMin)//�Y�GdisTemp�p��disMin�A�h��sdisMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//�D�P�]��zMax�������I
	   	t=(aabb.maxZ-startObject.z)/dv.z;//�ײvt
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xy�y��
			float y=t*dv.y+startObject.y;
			
			//�Y�Gxy�b�]�򲰳̤j�ȻP�̤p�Ȥ���
			if(x<=aabb.maxX&&x>=aabb.minX&&y<=aabb.maxY&&y>=aabb.minY)
			{
				float z=aabb.maxZ;//z�y��
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//�D�X��v���PAB�M�]�򲰥��I���̤p�Z��
				if(disTemp<disMin)//�Y�GdisTemp�p��disMin�A�h��sdisMin
				{
					disMin=disTemp;
				}
			}
		}
		
		return disMin;
	}
}
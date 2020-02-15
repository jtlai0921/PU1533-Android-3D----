package com.bn.txz.game;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;

import android.opengl.Matrix;

public class BodyPart 
{
	//ø���
	VertexTextureNormal3DObjectForDraw lovnt;	
	//�������
	int index;
	TXZGameSurfaceView msv;
	
    //�l���Y�M��
    ArrayList<BodyPart> childs=new ArrayList<BodyPart>();
    //���V�����f���Ѧ�
    BodyPart father;
    
    //�غc�����ޥΰѼƬ��l���f�����I�b���y�Шt�����y��
    public BodyPart(float fx,float fy,float fz,VertexTextureNormal3DObjectForDraw lovnt,int index,TXZGameSurfaceView msv)
    {    	
    	this.index=index;
    	this.msv=msv;
    	msv.gdMain.dataArray[index].bdd=new float[]{fx, fy, fz};//���l���f�b�_�l�y�Шt���������I������
    	this.lovnt=lovnt;
    }
    
    public void drawSelf(GL10 gl)
    {
    	gl.glPushMatrix();
    	gl.glTranslatef
    	(
    		msv.gdTemp.dataArray[index].py[0], //�l���f�b�����f�y�Шt��������
    		msv.gdTemp.dataArray[index].py[1], 
    		msv.gdTemp.dataArray[index].py[2]
    	);
    	gl.glTranslatef
    	(
			msv.gdTemp.dataArray[index].pyfz[0], //�l���f�b�����f�y�Шt�������઺���U����
			msv.gdTemp.dataArray[index].pyfz[1], 
			msv.gdTemp.dataArray[index].pyfz[2]
    	);
    	gl.glRotatef
    	(
    		msv.gdTemp.dataArray[index].xz[0],//�l���f�b�����f�y�Шt��������
    		msv.gdTemp.dataArray[index].xz[1], 
    		msv.gdTemp.dataArray[index].xz[2], 
    		msv.gdTemp.dataArray[index].xz[3]
    	);
    	
    	if(this.lovnt!=null)
    	{
    		this.lovnt.drawSelf(gl);
    	}
    	
    	
    	//�M���s�ۤv���Ҧ��Ĥl
    	for(BodyPart bc:childs)
    	{
    		bc.drawSelf(gl);
    	}
    	gl.glPopMatrix();    	
    }
    
    //�b���y�Шt�������ۤv
    public void transtate(float x,float y,float z)//�]�w�uxyz�b�h��
    {
    	msv.gdMain.dataArray[index].py[0]=x;
    	msv.gdMain.dataArray[index].py[1]=y;
    	msv.gdMain.dataArray[index].py[2]=z;
    }
    
    //�b���y�Шt������ۤv
    public void rotate(float angle,float x,float y,float z)//�]�w¶xyz�b���
    {
    	 msv.gdMain.dataArray[index].xz[0]=angle;
    	 msv.gdMain.dataArray[index].xz[1]=x;
    	 msv.gdMain.dataArray[index].xz[2]=y;
    	 msv.gdMain.dataArray[index].xz[3]=z;
    	
    	 float[] dot=
    	 {
    		msv.gdMain.dataArray[index].bdd[0],
    		msv.gdMain.dataArray[index].bdd[1],
    		msv.gdMain.dataArray[index].bdd[2],
    		1
    	 };
	   	 float[] dotr=new float[4];
	   	 float[] mtemp=new float[16];
	   	 //�p�⤣���I��m����
	   	 Matrix.setIdentityM(mtemp, 0);
	   	 Matrix.rotateM(mtemp, 0, angle, x, y, z);
	   	 Matrix.multiplyMV(dotr, 0, mtemp, 0, dot, 0);
	   	 msv.gdMain.dataArray[index].pyfz[0]=-dotr[0]+dot[0];
	   	 msv.gdMain.dataArray[index].pyfz[1]=-dotr[1]+dot[1];
	   	 msv.gdMain.dataArray[index].pyfz[2]=-dotr[2]+dot[2];	   	 
    }
    
    public void setFather(BodyPart f)
    {
    	this.father=f;
    }
    
    public BodyPart getFather()
    {
    	return father;
    }
    
    public void addChild(BodyPart child)
    {
    	childs.add(child);
    }
    
    public BodyPart getChild(int index)
    {
    	return childs.get(index);
    }
}

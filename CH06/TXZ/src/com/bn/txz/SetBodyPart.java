package com.bn.txz;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;

import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;

public class SetBodyPart 
{
	//ø���
	VertexTextureNormal3DObjectForDraw lovnt;	
	//�������
	int index;
	TXZSetView set;
	
    //�l���Y�M��
    ArrayList<SetBodyPart> childs=new ArrayList<SetBodyPart>();
    //���V�����f���Ѧ�
    SetBodyPart father;
    
    //�غc�����ޥΰѼƬ��l���f�����I�b���y�Шt�����y��
    public SetBodyPart(float fx,float fy,float fz,	VertexTextureNormal3DObjectForDraw lovnt,int index,TXZSetView set)
    {    	
    	this.index=index;
    	this.set=set;
    	set.gdMain.dataArray[index].bdd=new float[]{fx, fy, fz};//���l���f�b�_�l�y�Шt���������I������
    	this.lovnt=lovnt;
    }
    
    public void drawSelf(GL10 gl)
    {
    	gl.glPushMatrix();
    	gl.glTranslatef
    	(
    		set.gdTemp.dataArray[index].py[0], //�l���f�b�����f�y�Шt��������
    		set.gdTemp.dataArray[index].py[1], 
    		set.gdTemp.dataArray[index].py[2]
    	);
    	gl.glTranslatef
    	(
    			set.gdTemp.dataArray[index].pyfz[0], //�l���f�b�����f�y�Шt�������઺���U����
    			set.gdTemp.dataArray[index].pyfz[1], 
    			set.gdTemp.dataArray[index].pyfz[2]
    	);
    	gl.glRotatef
    	(
    			set.gdTemp.dataArray[index].xz[0],//�l���f�b�����f�y�Шt��������
    			set.gdTemp.dataArray[index].xz[1], 
    			set.gdTemp.dataArray[index].xz[2], 
    			set.gdTemp.dataArray[index].xz[3]
    	);
    	
    	if(this.lovnt!=null)
    	{
    		this.lovnt.drawSelf(gl);
    	}
    	
    	
    	//�M���s�ۤv���Ҧ��Ĥl
    	for(SetBodyPart bc:childs)
    	{
    		bc.drawSelf(gl);
    	}
    	gl.glPopMatrix();    	
    }
    
    //�b���y�Шt�������ۤv
    public void transtate(float x,float y,float z)//�]�w�uxyz�b�h��
    {
    	set.gdMain.dataArray[index].py[0]=x;
    	set.gdMain.dataArray[index].py[1]=y;
    	set.gdMain.dataArray[index].py[2]=z;
    }
    
    //�b���y�Шt������ۤv
    public void rotate(float angle,float x,float y,float z)//�]�w¶xyz�b���
    {
    	set.gdMain.dataArray[index].xz[0]=angle;
    	set.gdMain.dataArray[index].xz[1]=x;
    	set.gdMain.dataArray[index].xz[2]=y;
    	set.gdMain.dataArray[index].xz[3]=z;
    	
    	 float[] dot=
    	 {
			 set.gdMain.dataArray[index].bdd[0],
			 set.gdMain.dataArray[index].bdd[1],
			 set.gdMain.dataArray[index].bdd[2],
    		1
    	 };
	   	 float[] dotr=new float[4];
	   	 float[] mtemp=new float[16];
	   	 //�p�⤣���I��m����
	   	 Matrix.setIdentityM(mtemp, 0);
	   	 Matrix.rotateM(mtemp, 0, angle, x, y, z);
	   	 Matrix.multiplyMV(dotr, 0, mtemp, 0, dot, 0);
	   	set.gdMain.dataArray[index].pyfz[0]=-dotr[0]+dot[0];
	   	set.gdMain.dataArray[index].pyfz[1]=-dotr[1]+dot[1];
	   	set.gdMain.dataArray[index].pyfz[2]=-dotr[2]+dot[2];	   	 
    }
    
    public void setFather(SetBodyPart f)
    {
    	this.father=f;
    }
    
    public SetBodyPart getFather()
    {
    	return father;
    }
    
    public void addChild(SetBodyPart child)
    {
    	childs.add(child);
    }
    
    public SetBodyPart getChild(int index)
    {
    	return childs.get(index);
    }
}

package com.bn.txz;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;

import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;

public class SelectBodyPart 
{
	//ø���
	VertexTextureNormal3DObjectForDraw lovnt;	
	//�������
	int index;
	TXZSelectView select;
	
    //�l���Y�M��
    ArrayList<SelectBodyPart> childs=new ArrayList<SelectBodyPart>();
    //���V�����f���Ѧ�
    SelectBodyPart father;
    
    //�غc�����ޥΰѼƬ��l���f�����I�b���y�Шt�����y��
    public SelectBodyPart(float fx,float fy,float fz,VertexTextureNormal3DObjectForDraw lovnt,int index,TXZSelectView select)
    {    	
    	this.index=index;
    	this.select=select;
    	select.gdMain.dataArray[index].bdd=new float[]{fx, fy, fz};//���l���f�b�_�l�y�Шt���������I������
    	this.lovnt=lovnt;
    }
    
    public void drawSelf(GL10 gl)
    {
    	gl.glPushMatrix();
    	gl.glTranslatef
    	(
    			select.gdTemp.dataArray[index].py[0], //�l���f�b�����f�y�Шt��������
    			select.gdTemp.dataArray[index].py[1], 
    			select.gdTemp.dataArray[index].py[2]
    	);
    	gl.glTranslatef
    	(
    			select.gdTemp.dataArray[index].pyfz[0], //�l���f�b�����f�y�Шt�������઺���U����
    			select.gdTemp.dataArray[index].pyfz[1], 
    			select.gdTemp.dataArray[index].pyfz[2]
    	);
    	gl.glRotatef
    	(
    			select.gdTemp.dataArray[index].xz[0],//�l���f�b�����f�y�Шt��������
    			select.gdTemp.dataArray[index].xz[1], 
    			select.gdTemp.dataArray[index].xz[2], 
    			select.gdTemp.dataArray[index].xz[3]
    	);
    	
    	if(this.lovnt!=null)
    	{
    		this.lovnt.drawSelf(gl);
    	}
    	
    	
    	//�M���s�ۤv���Ҧ��Ĥl
    	for(SelectBodyPart bc:childs)
    	{
    		bc.drawSelf(gl);
    	}
    	gl.glPopMatrix();    	
    }
    
    //�b���y�Шt�������ۤv
    public void transtate(float x,float y,float z)//�]�w�uxyz�b�h��
    {
    	select.gdMain.dataArray[index].py[0]=x;
    	select.gdMain.dataArray[index].py[1]=y;
    	select.gdMain.dataArray[index].py[2]=z;
    }
    
    //�b���y�Шt������ۤv
    public void rotate(float angle,float x,float y,float z)//�]�w¶xyz�b���
    {
    	select.gdMain.dataArray[index].xz[0]=angle;
    	select.gdMain.dataArray[index].xz[1]=x;
    	select.gdMain.dataArray[index].xz[2]=y;
    	select.gdMain.dataArray[index].xz[3]=z;
    	
    	 float[] dot=
    	 {
    			 select.gdMain.dataArray[index].bdd[0],
    			 select.gdMain.dataArray[index].bdd[1],
    			 select.gdMain.dataArray[index].bdd[2],
    		1
    	 };
	   	 float[] dotr=new float[4];
	   	 float[] mtemp=new float[16];
	   	 //�p�⤣���I��m����
	   	 Matrix.setIdentityM(mtemp, 0);
	   	 Matrix.rotateM(mtemp, 0, angle, x, y, z);
	   	 Matrix.multiplyMV(dotr, 0, mtemp, 0, dot, 0);
	   	select.gdMain.dataArray[index].pyfz[0]=-dotr[0]+dot[0];
	   	select.gdMain.dataArray[index].pyfz[1]=-dotr[1]+dot[1];
	   	select.gdMain.dataArray[index].pyfz[2]=-dotr[2]+dot[2];	   	 
    }
    
    public void setFather(SelectBodyPart f)
    {
    	this.father=f;
    }
    
    public SelectBodyPart getFather()
    {
    	return father;
    }
    
    public void addChild(SelectBodyPart child)
    {
    	childs.add(child);
    }
    
    public SelectBodyPart getChild(int index)
    {
    	return childs.get(index);
    }
}

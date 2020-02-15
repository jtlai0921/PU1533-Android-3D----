package com.bn.txz;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;

import android.opengl.Matrix;

public class MenuBodyPart 
{
	//ø���
	VertexTextureNormal3DObjectForDraw lovnt;	
	//�������
	int index;
	TXZMenuView menu;
	
    //�l���Y�M��
    ArrayList<MenuBodyPart> childs=new ArrayList<MenuBodyPart>();
    //���V�����f���Ѧ�
    MenuBodyPart father;
    
    //�غc�����ޥΰѼƬ��l���f�����I�b���y�Шt�����y��
    public MenuBodyPart(float fx,float fy,float fz,	VertexTextureNormal3DObjectForDraw lovnt,int index,TXZMenuView menu)
    {    	
    	this.index=index;
    	this.menu=menu;
    	menu.gdMain.dataArray[index].bdd=new float[]{fx, fy, fz};//���l���f�b�_�l�y�Шt���������I������
    	this.lovnt=lovnt;
    }
    
    public void drawSelf(GL10 gl)
    {
    	gl.glPushMatrix();
    	gl.glTranslatef
    	(
    		menu.gdTemp.dataArray[index].py[0], //�l���f�b�����f�y�Шt��������
    		menu.gdTemp.dataArray[index].py[1], 
    		menu.gdTemp.dataArray[index].py[2]
    	);
    	gl.glTranslatef
    	(
    		menu.gdTemp.dataArray[index].pyfz[0], //�l���f�b�����f�y�Шt�������઺���U����
    		menu.gdTemp.dataArray[index].pyfz[1], 
    		menu.gdTemp.dataArray[index].pyfz[2]
    	);
    	gl.glRotatef
    	(
    		menu.gdTemp.dataArray[index].xz[0],//�l���f�b�����f�y�Шt��������
    		menu.gdTemp.dataArray[index].xz[1], 
    		menu.gdTemp.dataArray[index].xz[2], 
    		menu.gdTemp.dataArray[index].xz[3]
    	);
    	
    	if(this.lovnt!=null)
    	{
    		this.lovnt.drawSelf(gl);
    	}
    	
    	
    	//�M���s�ۤv���Ҧ��Ĥl
    	for(MenuBodyPart bc:childs)
    	{
    		bc.drawSelf(gl);
    	}
    	gl.glPopMatrix();    	
    }
    
    //�b���y�Шt�������ۤv
    public void transtate(float x,float y,float z)//�]�w�uxyz�b�h��
    {
    	menu.gdMain.dataArray[index].py[0]=x;
    	menu.gdMain.dataArray[index].py[1]=y;
    	menu.gdMain.dataArray[index].py[2]=z;
    }
    
    //�b���y�Шt������ۤv
    public void rotate(float angle,float x,float y,float z)//�]�w¶xyz�b���
    {
    	menu.gdMain.dataArray[index].xz[0]=angle;
    	menu.gdMain.dataArray[index].xz[1]=x;
    	menu.gdMain.dataArray[index].xz[2]=y;
    	menu.gdMain.dataArray[index].xz[3]=z;
    	
    	 float[] dot=
    	 {
    		menu.gdMain.dataArray[index].bdd[0],
    		menu.gdMain.dataArray[index].bdd[1],
    		menu.gdMain.dataArray[index].bdd[2],
    		1
    	 };
	   	 float[] dotr=new float[4];
	   	 float[] mtemp=new float[16];
	   	 //�p�⤣���I��m����
	   	 Matrix.setIdentityM(mtemp, 0);
	   	 Matrix.rotateM(mtemp, 0, angle, x, y, z);
	   	 Matrix.multiplyMV(dotr, 0, mtemp, 0, dot, 0);
	   	menu.gdMain.dataArray[index].pyfz[0]=-dotr[0]+dot[0];
	   	menu.gdMain.dataArray[index].pyfz[1]=-dotr[1]+dot[1];
	   	menu.gdMain.dataArray[index].pyfz[2]=-dotr[2]+dot[2];	   	 
    }
    
    public void setFather(MenuBodyPart f)
    {
    	this.father=f;
    }
    
    public MenuBodyPart getFather()
    {
    	return father;
    }
    
    public void addChild(MenuBodyPart child)
    {
    	childs.add(child);
    }
    
    public MenuBodyPart getChild(int index)
    {
    	return childs.get(index);
    }
}

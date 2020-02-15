package com.bn.txz;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;

import android.opengl.Matrix;

public class MenuBodyPart 
{
	//繪制者
	VertexTextureNormal3DObjectForDraw lovnt;	
	//元件索引
	int index;
	TXZMenuView menu;
	
    //子骨頭清單
    ArrayList<MenuBodyPart> childs=new ArrayList<MenuBodyPart>();
    //指向父骨骼的參考
    MenuBodyPart father;
    
    //建構器的引用參數為子骨骼不動點在父座標系中的座標
    public MenuBodyPart(float fx,float fy,float fz,	VertexTextureNormal3DObjectForDraw lovnt,int index,TXZMenuView menu)
    {    	
    	this.index=index;
    	this.menu=menu;
    	menu.gdMain.dataArray[index].bdd=new float[]{fx, fy, fz};//為子骨骼在起始座標系中的不動點給予值
    	this.lovnt=lovnt;
    }
    
    public void drawSelf(GL10 gl)
    {
    	gl.glPushMatrix();
    	gl.glTranslatef
    	(
    		menu.gdTemp.dataArray[index].py[0], //子骨骼在父骨骼座標系中的平移
    		menu.gdTemp.dataArray[index].py[1], 
    		menu.gdTemp.dataArray[index].py[2]
    	);
    	gl.glTranslatef
    	(
    		menu.gdTemp.dataArray[index].pyfz[0], //子骨骼在父骨骼座標系中的旋轉的輔助平移
    		menu.gdTemp.dataArray[index].pyfz[1], 
    		menu.gdTemp.dataArray[index].pyfz[2]
    	);
    	gl.glRotatef
    	(
    		menu.gdTemp.dataArray[index].xz[0],//子骨骼在父骨骼座標系中的旋轉
    		menu.gdTemp.dataArray[index].xz[1], 
    		menu.gdTemp.dataArray[index].xz[2], 
    		menu.gdTemp.dataArray[index].xz[3]
    	);
    	
    	if(this.lovnt!=null)
    	{
    		this.lovnt.drawSelf(gl);
    	}
    	
    	
    	//然後更新自己的所有孩子
    	for(MenuBodyPart bc:childs)
    	{
    		bc.drawSelf(gl);
    	}
    	gl.glPopMatrix();    	
    }
    
    //在父座標系中平移自己
    public void transtate(float x,float y,float z)//設定沿xyz軸搬移
    {
    	menu.gdMain.dataArray[index].py[0]=x;
    	menu.gdMain.dataArray[index].py[1]=y;
    	menu.gdMain.dataArray[index].py[2]=z;
    }
    
    //在父座標系中旋轉自己
    public void rotate(float angle,float x,float y,float z)//設定繞xyz軸轉動
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
	   	 //計算不動點位置後折返
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

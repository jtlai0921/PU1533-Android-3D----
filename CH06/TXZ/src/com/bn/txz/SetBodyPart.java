package com.bn.txz;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;

import com.bn.txz.manager.VertexTextureNormal3DObjectForDraw;

public class SetBodyPart 
{
	//繪制者
	VertexTextureNormal3DObjectForDraw lovnt;	
	//元件索引
	int index;
	TXZSetView set;
	
    //子骨頭清單
    ArrayList<SetBodyPart> childs=new ArrayList<SetBodyPart>();
    //指向父骨骼的參考
    SetBodyPart father;
    
    //建構器的引用參數為子骨骼不動點在父座標系中的座標
    public SetBodyPart(float fx,float fy,float fz,	VertexTextureNormal3DObjectForDraw lovnt,int index,TXZSetView set)
    {    	
    	this.index=index;
    	this.set=set;
    	set.gdMain.dataArray[index].bdd=new float[]{fx, fy, fz};//為子骨骼在起始座標系中的不動點給予值
    	this.lovnt=lovnt;
    }
    
    public void drawSelf(GL10 gl)
    {
    	gl.glPushMatrix();
    	gl.glTranslatef
    	(
    		set.gdTemp.dataArray[index].py[0], //子骨骼在父骨骼座標系中的平移
    		set.gdTemp.dataArray[index].py[1], 
    		set.gdTemp.dataArray[index].py[2]
    	);
    	gl.glTranslatef
    	(
    			set.gdTemp.dataArray[index].pyfz[0], //子骨骼在父骨骼座標系中的旋轉的輔助平移
    			set.gdTemp.dataArray[index].pyfz[1], 
    			set.gdTemp.dataArray[index].pyfz[2]
    	);
    	gl.glRotatef
    	(
    			set.gdTemp.dataArray[index].xz[0],//子骨骼在父骨骼座標系中的旋轉
    			set.gdTemp.dataArray[index].xz[1], 
    			set.gdTemp.dataArray[index].xz[2], 
    			set.gdTemp.dataArray[index].xz[3]
    	);
    	
    	if(this.lovnt!=null)
    	{
    		this.lovnt.drawSelf(gl);
    	}
    	
    	
    	//然後更新自己的所有孩子
    	for(SetBodyPart bc:childs)
    	{
    		bc.drawSelf(gl);
    	}
    	gl.glPopMatrix();    	
    }
    
    //在父座標系中平移自己
    public void transtate(float x,float y,float z)//設定沿xyz軸搬移
    {
    	set.gdMain.dataArray[index].py[0]=x;
    	set.gdMain.dataArray[index].py[1]=y;
    	set.gdMain.dataArray[index].py[2]=z;
    }
    
    //在父座標系中旋轉自己
    public void rotate(float angle,float x,float y,float z)//設定繞xyz軸轉動
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
	   	 //計算不動點位置後折返
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

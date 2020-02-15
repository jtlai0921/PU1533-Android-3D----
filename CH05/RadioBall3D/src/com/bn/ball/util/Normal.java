package com.bn.ball.util;

import java.util.Set;

//表示法向量的類別，此類別的一個物件表示一個法向量
public class Normal 
{
   public static final float DIFF=0.0000001f;//判斷兩個法向量是否相同的設定值
   //法向量在XYZ軸上的分量
   float nx;
   float ny;
   float nz;
   
   public Normal(float nx,float ny,float nz)
   {
	   this.nx=nx;
	   this.ny=ny;
	   this.nz=nz;
   }
   
   @Override 
   public boolean equals(Object o)
   {
	   if(o instanceof  Normal)
	   {//若兩個法向量XYZ三個分量的差都小於特殊的設定值則認為這兩個法向量相等
		   Normal tn=(Normal)o;
		   if(Math.abs(nx-tn.nx)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF
             )
		   {
			   return true;
		   }
		   else
		   {
			   return false;
		   }
	   }
	   else
	   {
		   return false;
	   }
   }
   
   //由於要用到HashSet，因此一定要重新定義hashCode方法
   @Override
   public int hashCode()
   {
	   return 1;
   }
   
   //求法向量平均值的工具方法
   public static float[] getAverage(Set<Normal> sn)
   {
	   //存放法向量和的陣列
	   float[] result=new float[3];
	   //把集合中所有的法向量求和
	   for(Normal n:sn)
	   {
		   result[0]+=n.nx;
		   result[1]+=n.ny;
		   result[2]+=n.nz;
	   }	   
	   //將求和後的法向量規格化
	   return LoadUtil.vectorNormal(result);
   }
}

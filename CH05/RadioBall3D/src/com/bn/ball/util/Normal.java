package com.bn.ball.util;

import java.util.Set;

//��ܪk�V�q�����O�A�����O���@�Ӫ����ܤ@�Ӫk�V�q
public class Normal 
{
   public static final float DIFF=0.0000001f;//�P�_��Ӫk�V�q�O�_�ۦP���]�w��
   //�k�V�q�bXYZ�b�W�����q
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
	   {//�Y��Ӫk�V�qXYZ�T�Ӥ��q���t���p��S���]�w�ȫh�{���o��Ӫk�V�q�۵�
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
   
   //�ѩ�n�Ψ�HashSet�A�]���@�w�n���s�w�qhashCode��k
   @Override
   public int hashCode()
   {
	   return 1;
   }
   
   //�D�k�V�q�����Ȫ��u���k
   public static float[] getAverage(Set<Normal> sn)
   {
	   //�s��k�V�q�M���}�C
	   float[] result=new float[3];
	   //�ⶰ�X���Ҧ����k�V�q�D�M
	   for(Normal n:sn)
	   {
		   result[0]+=n.nx;
		   result[1]+=n.ny;
		   result[2]+=n.nz;
	   }	   
	   //�N�D�M�᪺�k�V�q�W���
	   return LoadUtil.vectorNormal(result);
   }
}

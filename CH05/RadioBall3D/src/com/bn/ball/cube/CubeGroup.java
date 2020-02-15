package com.bn.ball.cube;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.MySurfaceView;
import com.bn.ball.WuTiDrawer;

public class CubeGroup extends WuTiDrawer{
   Cube cube;
   MySurfaceView mv;
   int index;
   float UNIT_SIZE=0.3f;
   public CubeGroup(MySurfaceView mv,int wallId){
	   this.mv=mv;
	   cube=new Cube(mv,wallId);
   }
	public void drawSelf(GL10 gl) {
		
		gl.glPushMatrix();
		 index=0;
         for(int i=-1;i<2;i++) 
         {
         	for(int j=-1;j<2;j++)
         	{
         		for(int k=-1;k<2;k++)
         		{
         			gl.glPushMatrix(); 
         			gl.glTranslatef(2*k*UNIT_SIZE, 2*j*UNIT_SIZE, 2*i*UNIT_SIZE);
//                     MatrixState.translate(2*k*UNIT_SIZE+mv.loction_offset[index][0], 2*j*UNIT_SIZE+mv.loction_offset[index][1], 2*i*UNIT_SIZE+mv.loction_offset[index][2]);
//                     if(mv.rotate_flag)
//                     {
//                     	MatrixState.rotate(mv.angle_offset[index][0], mv.angle_offset[index][1], mv.angle_offset[index][2], mv.angle_offset[index][3]);
//                     }
                     cube.drawSelf(gl);
                     gl.glPopMatrix();
                     index++;
         		}
         	}
         }
         index=0;
		gl.glPopMatrix();	
	}
	public void go(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(0f, -1.0559f, 0);		//-1.25f+UNIT_SIZE*1.5f
		 int index=0;
         for(int i=-1;i<2;i++) 
         {
         	for(int j=-1;j<2;j++)
         	{
         		for(int k=-1;k<2;k++)
         		{
         			gl.glPushMatrix(); 
//         			MatrixState.translate(2*k*UNIT_SIZE, 2*j*UNIT_SIZE, 2*i*UNIT_SIZE);
                     gl.glTranslatef(2*k*UNIT_SIZE+mv.loction_offset[index][0], 2*j*UNIT_SIZE+mv.loction_offset[index][1], 2*i*UNIT_SIZE+mv.loction_offset[index][2]);
                     if(mv.rotate_flag)
                     {
                     	gl.glRotatef(mv.angle_offset[index][0], mv.angle_offset[index][1], mv.angle_offset[index][2], mv.angle_offset[index][3]);
                     }
                     cube.drawSelf(gl);
                     gl.glPopMatrix();
                     index++;
         		}
         	}
         }
         index=0;
		gl.glPopMatrix();
	}
}

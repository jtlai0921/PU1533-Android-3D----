package tstc.fxq.parts;
import static tstc.fxq.constants.Constant.*;

public class NearPoint {

	static float  height=TABLE_AREA_WIDTH*MiniMapScale;
	static float width=TABLE_AREA_LENGTH*MiniMapScale;
	public static float[] GetJiaoDian(float x1,float y1,float x2,float y2)//獲得交點
	{
		float x3;//交點的座標
		float y3;
		
		float[] jiaodian=new float[2];//用於存放交點的xy座標
		
		x3=width/2;
		y3=(float)((y1-y2)*(x3-x1)/(x1-x2)+y1);
		if
		(
			(x1-x2)*(x3-x1)>=0&&
			(y1-y2)*(y3-y1)>=0&&
			x3>=-(width/2)&&
			x3<=width/2&&
			y3>=-(height/2)&&
			y3<=height/2
		)
		{
			jiaodian[0]=x3;
			jiaodian[1]=y3;
			return jiaodian;
		}
		
		x3=-(width/2);
		y3=(float)((y1-y2)*(x3-x1)/(x1-x2)+y1);
		if
		(
			(x1-x2)*(x3-x1)>=0&&
			(y1-y2)*(y3-y1)>=0&&
			x3>=-(width/2)&&
			x3<=width/2&&
			y3>=-(height/2)&&
			y3<=height/2
		)
		{
			jiaodian[0]=x3;
			jiaodian[1]=y3;
			return jiaodian;
		}
		
		y3=height/2;
		x3=(float)((x1-x2)*(y3-y1)/(y1-y2)+x1);
		if
		(
			(x1-x2)*(x3-x1)>=0&&
			(y1-y2)*(y3-y1)>=0&&
			x3>=-(width/2)&&
			x3<=width/2&&
			y3>=-(height/2)&&
			y3<=height/2
		)
		{
			jiaodian[0]=x3;
			jiaodian[1]=y3;
			return jiaodian;
		}
		
		y3=-(height/2);
		x3=(float)((x1-x2)*(y3-y1)/(y1-y2)+x1);
		jiaodian[0]=x3;
		jiaodian[1]=y3;
		return jiaodian;
	}
	
	
	
	public static float[] getCuePoint(float ballX,float ballY,float Alpha)
	{
		
		float tempX=(float) (ballX-Math.cos(Math.toRadians(-Alpha))*0.15f);
		float tempY=(float)(ballY-Math.sin(Math.toRadians(-Alpha))*0.15f);
		
		return new float[]
         {
			tempX,tempY
         };
	}
}

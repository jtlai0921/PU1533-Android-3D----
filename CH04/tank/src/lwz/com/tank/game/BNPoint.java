package lwz.com.tank.game;
import static lwz.com.tank.activity.Constant.*;

public class BNPoint {
	   //觸控點X、Y座標
	   float x;
	   float y;
	   int countl;
       //坦克的偏轉角度
       float vAngle=0;
       //炮管的偏移角度
       float pAngle=0;
       //坦克每框搬移的步徑
       float hd_x=0;
       float hd_y=0;
       //搖桿中心圓點的偏移量
       float scale_x=0;
       float scale_y=0;
 		//搖桿中心點像素座標及半徑
 		public float INIT_x=(115+ssr.lucX)*ssr.ratio;
 		public float INIT_y=(425+ssr.lucY)*ssr.ratio;
 		public float INIT_R=100*(SCREEN_WIDTH-ssr.lucX*ssr.ratio*2)/SCREEN_WIDTH_STANDARD;
       //觸控點的標志位
       public  int touchflag=0;
       //坦克起始的像素座標
  	   float tkposition_x=480f;
  	   float tkposition_y=438.75f;
  	   
	   public BNPoint(float x,float y,int countl)
	   {
			//判斷觸控點是否在搖桿區域內
		   if(Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))<INIT_R)
		   {
			   this.x=x;
			   this.y=y;
			   this.countl=countl;
				//觸控標志位置為1,視為搖桿觸控點
			   touchflag=1;
		   }
		   else
		   { 
			   this.x=x;
			   this.y=y;
			   this.countl=countl;
			   //觸控標志位置為1,視為發炮觸控點
			   touchflag=2;
		   }
	   }
	   public void setLocation(float x,float y)//move事件下所修改的座標
	   {
		   //若搖桿觸控點搬移
		   if(touchflag==1)
		   {
			   //判斷目前點座標是否在搖桿觸控區域內
			   if(Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))<INIT_R)
			   {
				   this.x=x;
				   this.y=y;
				   //觸控標志位置為1,視為搖桿觸控點
				   touchflag=1;
			   }
			   else
			   {
				   //目前點座標到搖桿中心點的距離
				   float tempR=(float) Math.sqrt((x-INIT_x)*(x-INIT_x)+(INIT_y-y)*(INIT_y-y));
				   //將目前點座標強制轉為搖桿觸控範圍的邊緣座標
				   this.x=INIT_x+(x-INIT_x)*INIT_R/tempR;
				   this.y=INIT_y+(y-INIT_y)*INIT_R/tempR;
				   //觸控標志位置為1,視為搖桿觸控點
				   touchflag=1;
			   }
		   }
		   if(touchflag==2)
		   {
			   this.x=x;
			   this.y=y;
			 //觸控標志位置為2,視為發炮觸控點
			   touchflag=2;
		   }
	   }
	//取得觸控的標志位
	public int gettouchflag()
	{
		return touchflag;
	}
	//取得目前觸控點的X座標
	public float getx()
	{
		return x;
	}
	//取得目前觸控點的X座標
	public float gety()
	{
		return y;
	}
}

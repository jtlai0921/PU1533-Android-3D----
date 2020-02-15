package lwz.com.tank.game;

import lwz.com.tank.activity.BulletTextureByVertex;
import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.TextureRect;
import static lwz.com.tank.activity.Constant.*;

public class Bullet {
	//用於繪制子彈的模型參考
		BulletTextureByVertex bullet;
		//子彈的出膛位置
		float startX;
		float startY;
		float startZ;
		//子彈的實時位置
		public float cuerrentX;
		public float cuerrentY;	
		public float cuerrentZ;
		//子彈的出膛速度
		float vx;
		float vy;
		float vz;	
		//子出現膛方向
		float  startAngle;
		////子彈的實時方向
		float cuerrentAngle=0;
		//子彈發射後的累計時間
		float timeLive=0;
		//用於繪制爆炸動畫效果的紋理矩形群組
		TextureRect[] trExplo;
		//爆炸動畫是否開始標志
		 public static boolean anmiStart=false;
		//爆炸動畫框索引
		 public static int anmiIndex=0;
		//爆炸位置
		 public static float anmiX;
		 public static float anmiY;
		 public static float anmiZ;
		//爆炸動畫紋理矩形的標志板角度
		float anmiYAngle;
		//MySurfaceView的參考
		//坦克生命計數器
		public static int tankeflag1=0;
		public static int tankeflag2=0;
		//子彈標志位
		public int bulletflag=0;
		MySurfaceView mv;
		//判斷子彈撞牆的標志位
		public int flag=0;

		//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) 陳偉加入尾巴的地方
		public int  startMove=-1;
		public float [][]tempData=new float[9][3];
		public float [][]Data=new float[9][3];
		//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) 陳偉加入尾巴的地方
		
		public Bullet(BulletTextureByVertex bullet,float startX,float startY,float startZ,float vx,float vy,float vz,TextureRect[] trExplo,float startAngle,MySurfaceView mv,int bulletflag)
		{
			this.trExplo=trExplo;
			this.bullet=bullet;
			this.startX=startX;
			this.startY=startY;
			this.startZ=startZ;
			this.vx=vx;
			this.vy=vy;
			this.vz=vz;
			this.startAngle=startAngle;
			this.mv=mv;	
			this.bulletflag=bulletflag;
			cuerrentAngle=startAngle;
			cuerrentX=startX;
			cuerrentY=startY;
			cuerrentZ=startZ;
		}
		public void go()
		{
			timeLive=timeLive+TIME_SPAN;
			cuerrentX=startX+vx*timeLive;
			cuerrentY=startY+vy*timeLive;
			cuerrentZ=startZ;
			
			int rows=lowWallmapdata.length;
			for(int i=0;i<rows;i++)
			{
				if(cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))>lowWallmapdata[i][0]*Wall_UNIT_SIZE&&cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))<lowWallmapdata[i][1]*Wall_UNIT_SIZE&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))>(lowWallmapdata[i][1]-lowWallmapdata[i][4])*Wall_UNIT_SIZE)
				{
					flag++;
					if(cuerrentY-vy*TIME_SPAN+(18*Math.cos(Math.toRadians(cuerrentAngle)))<lowWallmapdata[i][1]*Wall_UNIT_SIZE&&cuerrentY-vy*TIME_SPAN+(18*Math.cos(Math.toRadians(cuerrentAngle)))>(lowWallmapdata[i][1]-lowWallmapdata[i][4])*Wall_UNIT_SIZE)
					{
						cuerrentAngle=-startAngle;
						timeLive=0;
						vx=-vx;
						startX=cuerrentX;
						startY=cuerrentY;
						cuerrentZ=startZ;
					}	
					else
					{
						cuerrentAngle=180-startAngle;
						timeLive=0;	
						vy=-vy;
						startX=cuerrentX;
						startY=cuerrentY;
						cuerrentZ=startZ;
					}
				}		
			}
			
			if(bulletflag==1)
			{
				if(cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))>mv.activity.gdFollowDraw.followtkwz[0]-30&&cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))<mv.activity.gdFollowDraw.followtkwz[0]+30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))>mv.activity.gdFollowDraw.followtkwz[1]-30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))<mv.activity.gdFollowDraw.followtkwz[1]+30)
				{
					mv.bulletCount--;
					anmiStart=true;	
					mv.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震動
					mv.activity.soundutil.playEffectsSound(2,0);
					anmiIndex=0;
					anmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
					anmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
					anmiZ=cuerrentZ;
					mv.bulletAl.remove(this);
					tankeflag2++;
					mv.activity.sendMessage(anmiStart+"<#>"+anmiIndex+"<#>"+anmiX+"<#>"+anmiY+"<#>"+anmiZ+"<#>"+tankeflag2+"<#>");
				}
			}
			if(flag==2)
			{
				if(bulletflag==1)
				{
					mv.bulletCount--;
				}
				anmiStart=true;	
				mv.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震動
				mv.activity.soundutil.playEffectsSound(2, 0);
				anmiIndex=0;
				anmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
				anmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
				anmiZ=cuerrentZ;
				mv.bulletAl.remove(this);
				mv.activity.sendMessage(anmiStart+"<#>"+anmiIndex+"<#>"+anmiX+"<#>"+anmiY+"<#>"+anmiZ+"<#>"+tankeflag2+"<#>");
			}
		}
}

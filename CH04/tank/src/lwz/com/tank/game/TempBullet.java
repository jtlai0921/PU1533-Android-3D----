package lwz.com.tank.game;

import static lwz.com.tank.activity.Constant.*;
import lwz.com.tank.activity.BulletTextureByVertex;
import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.TextureRect;

public class TempBullet {
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
	 public static boolean tempanmiStart=false;
	//爆炸動畫框索引
	 public static int tempanmiIndex=0;
	//爆炸位置
	 public static float tempanmiX;
	 public static float tempanmiY;
	 public static float tempanmiZ;
	//爆炸動畫紋理矩形的標志板角度
	float anmiYAngle;
	//MySurfaceView的參考
	public static int tankeflag1=0;
	public static int tankeflag2=0;
	//子彈標志位
	public int bulletflag=0;
	OtherSurfaceView ov;
	//判斷子彈撞牆的標志位
	public int flag=0;
	//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) 陳偉加入尾巴的地方
	public int  startMove=-1;
	public float [][]tempData=new float[9][3];
	public float [][]Data=new float[9][3];
	//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) 陳偉加入尾巴的地方
	public TempBullet(BulletTextureByVertex bullet,float startX,float startY,float startZ,float vx,float vy,float vz,TextureRect[] trExplo,float startAngle,OtherSurfaceView ov,int bulletflag)
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
		this.ov=ov;	
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
			//18為子彈的長度          18*Math.cos(cuerrentAngle)為目前角度下子彈在Y軸上的長度     18*Math.sin(cuerrentAngle)為目前角度下子彈在X軸上的長度
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
			if(cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))>ov.activity.gdFollowDraw.maintkwz[0]-30&&cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))<ov.activity.gdFollowDraw.maintkwz[0]+30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))>ov.activity.gdFollowDraw.maintkwz[1]-30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))<ov.activity.gdFollowDraw.maintkwz[1]+30)
			{
				ov.bulletCount--;
				tempanmiStart=true;	
				ov.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震動
				ov.activity.soundutil.playEffectsSound(2, 0);
				tempanmiIndex=0;
				tempanmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
				tempanmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
				tempanmiZ=cuerrentZ;
				ov.tempbulletAl.remove(this);
				tankeflag1++;
				ov.activity.sendMessage(tempanmiStart+"<#>"+tempanmiIndex+"<#>"+tempanmiX+"<#>"+tempanmiY+"<#>"+tempanmiZ+"<#>"+tankeflag1+"<#>");
			}
		}
		if(flag==2)
		{
			if(bulletflag==1)
			{
				ov.bulletCount--;
			}
			tempanmiStart=true;	
			ov.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震動
			ov.activity.soundutil.playEffectsSound(2, 0);
			tempanmiIndex=0;
			tempanmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
			tempanmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
			tempanmiZ=cuerrentZ;
			ov.tempbulletAl.remove(this);
			ov.activity.sendMessage(tempanmiStart+"<#>"+tempanmiIndex+"<#>"+tempanmiX+"<#>"+tempanmiY+"<#>"+tempanmiZ+"<#>"+tankeflag1+"<#>");
		}
	}
}

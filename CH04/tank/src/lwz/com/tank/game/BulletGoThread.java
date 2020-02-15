package lwz.com.tank.game;

import java.util.ArrayList;
//定時搬移子彈的執行緒
public class BulletGoThread extends Thread
{
	ArrayList<Bullet> bulletAl;//子彈清單
	boolean flag=true;//循環標志位
	MySurfaceView mv;
	String s1,s2,s3,s4,s,ss,sss,ssss;
	public BulletGoThread(ArrayList<Bullet> bulletAl,MySurfaceView mv)
	{
		this.bulletAl=bulletAl;
		this.mv= mv;
	}
	public void run()
	{
		while(flag)
		{//循環定時搬移炮彈
			try
			{
				synchronized(mv.gdMain.dataLock) 	//將主資料鎖上
				{
					mv.gdMain.mainbulletAl.clear();
					mv.gdMain.mainTailAl.clear();
					synchronized(mv.gdMainDraw.dataLock) //將繪制資料鎖上
					{
						mv.gdMainDraw.mainbulletAl.clear();
						mv.gdMainDraw.mainTailAl.clear();
					}
					for(int i=0;i<bulletAl.size();i++)
					{
						Bullet b=bulletAl.get(i);
						b.go();
						b.startMove++;
						if(b.startMove<=8)
						{
							b.Data[b.startMove]=new float[]
									{
									b.cuerrentX,
									b.cuerrentY,
									0
									};
							switch(b.startMove)
							{
								case 0:
									b.tempData[0]=b.tempData[1]=b.tempData[2]=b.tempData[3]=b.tempData[4]=b.tempData[5]=b.tempData[6]=b.tempData[7]=b.tempData[8]=b.Data[0];
								break;
								case 1:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.tempData[2]=b.tempData[3]=b.tempData[4]=b.tempData[5]=b.tempData[6]=b.tempData[7]=b.tempData[8]=b.Data[1];
								break;
								case 2:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.Data[1];
									b.tempData[2]=b.tempData[3]=b.tempData[4]=b.tempData[5]=b.tempData[6]=b.tempData[7]=b.tempData[8]=b.Data[2];
								break;
								case 3:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.Data[1];
									b.tempData[2]=b.Data[2];
									b.tempData[3]=b.tempData[4]=b.tempData[5]=b.tempData[6]=b.tempData[7]=b.tempData[8]=b.Data[3];
								break;
								case 4:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.Data[1];
									b.tempData[2]=b.Data[2];
									b.tempData[3]=b.Data[3];
									b.tempData[4]=b.tempData[5]=b.tempData[6]=b.tempData[7]=b.tempData[8]=b.Data[4];
								
								break;
								case 5:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.Data[1];
									b.tempData[2]=b.Data[2];
									b.tempData[3]=b.Data[3];
									b.tempData[4]=b.Data[4];
									b.tempData[5]=b.tempData[6]=b.tempData[7]=b.tempData[8]=b.Data[5];
								break;
								case 6:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.Data[1];
									b.tempData[2]=b.Data[2];
									b.tempData[3]=b.Data[3];
									b.tempData[4]=b.Data[4];
									b.tempData[5]=b.Data[5];
									b.tempData[6]=b.tempData[7]=b.tempData[8]=b.Data[6];
								break;
								case 7:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.Data[1];
									b.tempData[2]=b.Data[2];
									b.tempData[3]=b.Data[3];
									b.tempData[4]=b.Data[4];
									b.tempData[5]=b.Data[5];
									b.tempData[6]=b.Data[6];
									b.tempData[7]=b.tempData[8]=b.Data[7];
								break;
								case 8:
									b.tempData[0]=b.Data[0];
									b.tempData[1]=b.Data[1];
									b.tempData[2]=b.Data[2];
									b.tempData[3]=b.Data[3];
									b.tempData[4]=b.Data[4];
									b.tempData[5]=b.Data[5];
									b.tempData[6]=b.Data[6];
									b.tempData[7]=b.Data[7];
									b.tempData[8]=b.Data[8];
								break;
							}
						}
						else
						{
							b.Data[0]=b.Data[1];
							b.Data[1]=b.Data[2];
							b.Data[2]=b.Data[3];
							b.Data[3]=b.Data[4]; 
							b.Data[4]=b.Data[5];
							b.Data[5]=b.Data[6];
							b.Data[6]=b.Data[7]; 
							b.Data[7]=b.Data[8];
							b.Data[8]=new float[]
							{
								b.cuerrentX,
								b.cuerrentY,
								0
					         };
							b.tempData[0]=b.Data[0];
							b.tempData[1]=b.Data[1];
							b.tempData[2]=b.Data[2];
							b.tempData[3]=b.Data[3]; 
							b.tempData[4]=b.Data[4];
							b.tempData[5]=b.Data[5];
							b.tempData[6]=b.Data[6]; 
							b.tempData[7]=b.Data[7];
							b.tempData[8]=b.Data[8];
						}
						mv.gdMain.mainTailAl.add(new float[][]{b.tempData[0],b.tempData[1],b.tempData[2],b.tempData[3],b.tempData[4],b.tempData[5],b.tempData[6],b.tempData[7],b.tempData[8]});
						
						switch(mv.gdMain.mainTailAl.size())
						{
						case 0:
							break;
						case 1:
							s=mv.gdMain.mainTailAl.get(0)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][2]+"<#>";
							mv.activity.sendMessage(s);
							break;
						case 2:
							s=mv.gdMain.mainTailAl.get(0)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][2]+"<#>"
							 +mv.gdMain.mainTailAl.get(1)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][2]+"<#>";
							mv.activity.sendMessage(s);
							break;
						case 3:
							s=mv.gdMain.mainTailAl.get(0)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][2]+"<#>"
							 +mv.gdMain.mainTailAl.get(1)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][2]+"<#>"
							 +mv.gdMain.mainTailAl.get(2)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[8][2]+"<#>";
							mv.activity.sendMessage(s);
							break;
						case 4:
							s=mv.gdMain.mainTailAl.get(0)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(0)[8][2]+"<#>"
							 +mv.gdMain.mainTailAl.get(1)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(1)[8][2]+"<#>"
							 +mv.gdMain.mainTailAl.get(2)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(2)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(2)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(2)[8][2]+"<#>"
							 +mv.gdMain.mainTailAl.get(3)[0][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[0][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[0][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[1][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[1][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[1][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[2][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[2][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[2][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[3][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[3][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[3][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[4][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[4][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[4][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[5][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[5][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[5][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[6][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[6][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[6][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[7][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[7][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[7][2]+"<#>"+mv.gdMain.mainTailAl.get(3)[8][0]+"<#>"+mv.gdMain.mainTailAl.get(3)[8][1]+"<#>"+mv.gdMain.mainTailAl.get(3)[8][2]+"<#>";
							mv.activity.sendMessage(s);
						}
						
						synchronized(mv.gdMainDraw.dataLock) //將繪制資料鎖上
						{
							mv.gdMainDraw.mainTailAl.add(new float[][]{b.tempData[0],b.tempData[1],b.tempData[2],b.tempData[3],b.tempData[4],b.tempData[5],b.tempData[6],b.tempData[7],b.tempData[8]});
						}
						
						mv.gdMain.mainbullet[0]=b.cuerrentX;
						mv.gdMain.mainbullet[1]=b.cuerrentY;
						mv.gdMain.mainbullet[2]=b.cuerrentZ;
						mv.gdMain.mainbullet[3]=b.cuerrentAngle;
						
						mv.gdMain.mainbulletAl.add(new float[]{b.cuerrentX,b.cuerrentY,b.cuerrentZ,b.cuerrentAngle});
						switch (mv.gdMain.mainbulletAl.size())
						{
							case 0:
							break;
							case 1:
							s1=mv.gdMain.mainbulletAl.get(0)[0]+"<#>"+mv.gdMain.mainbulletAl.get(0)[1]+"<#>"+mv.gdMain.mainbulletAl.get(0)[2]+"<#>"+mv.gdMain.mainbulletAl.get(0)[3]+"<#>";
							mv.activity.sendMessage(s1);
							break;
							case 2:
								
							s1=mv.gdMain.mainbulletAl.get(0)[0]+"<#>"+mv.gdMain.mainbulletAl.get(0)[1]+"<#>"+mv.gdMain.mainbulletAl.get(0)[2]+"<#>"+mv.gdMain.mainbulletAl.get(0)[3]+"<#>"
							+mv.gdMain.mainbulletAl.get(1)[0]+"<#>"+mv.gdMain.mainbulletAl.get(1)[1]+"<#>"+mv.gdMain.mainbulletAl.get(1)[2]+"<#>"+mv.gdMain.mainbulletAl.get(1)[3]+"<#>";
							mv.activity.sendMessage(s1);
							break;
							case 3:
							s1=mv.gdMain.mainbulletAl.get(0)[0]+"<#>"+mv.gdMain.mainbulletAl.get(0)[1]+"<#>"+mv.gdMain.mainbulletAl.get(0)[2]+"<#>"+mv.gdMain.mainbulletAl.get(0)[3]+"<#>"
							+mv.gdMain.mainbulletAl.get(1)[0]+"<#>"+mv.gdMain.mainbulletAl.get(1)[1]+"<#>"+mv.gdMain.mainbulletAl.get(1)[2]+"<#>"+mv.gdMain.mainbulletAl.get(1)[3]+"<#>"
							+mv.gdMain.mainbulletAl.get(2)[0]+"<#>"+mv.gdMain.mainbulletAl.get(2)[1]+"<#>"+mv.gdMain.mainbulletAl.get(2)[2]+"<#>"+mv.gdMain.mainbulletAl.get(2)[3]+"<#>";
							mv.activity.sendMessage(s1);
							break;
							case 4:
							s1=mv.gdMain.mainbulletAl.get(0)[0]+"<#>"+mv.gdMain.mainbulletAl.get(0)[1]+"<#>"+mv.gdMain.mainbulletAl.get(0)[2]+"<#>"+mv.gdMain.mainbulletAl.get(0)[3]+"<#>"
							+mv.gdMain.mainbulletAl.get(1)[0]+"<#>"+mv.gdMain.mainbulletAl.get(1)[1]+"<#>"+mv.gdMain.mainbulletAl.get(1)[2]+"<#>"+mv.gdMain.mainbulletAl.get(1)[3]+"<#>"
							+mv.gdMain.mainbulletAl.get(2)[0]+"<#>"+mv.gdMain.mainbulletAl.get(2)[1]+"<#>"+mv.gdMain.mainbulletAl.get(2)[2]+"<#>"+mv.gdMain.mainbulletAl.get(2)[3]+"<#>"
							+mv.gdMain.mainbulletAl.get(3)[0]+"<#>"+mv.gdMain.mainbulletAl.get(3)[1]+"<#>"+mv.gdMain.mainbulletAl.get(3)[2]+"<#>"+mv.gdMain.mainbulletAl.get(3)[3]+"<#>";
							mv.activity.sendMessage(s1);
							break;
						}
						synchronized(mv.gdMainDraw.dataLock) //將繪制資料鎖上
						{
							mv.gdMainDraw.mainbulletAl.add(new float[]{b.cuerrentX,b.cuerrentY,b.cuerrentZ,b.cuerrentAngle});
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				Thread.sleep(15);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
			
		}
	}
}

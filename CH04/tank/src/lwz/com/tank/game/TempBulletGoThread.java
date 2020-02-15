package lwz.com.tank.game;

import java.util.ArrayList;

//定時搬移子彈的執行緒
public class TempBulletGoThread extends Thread
{
	ArrayList<TempBullet> tempbulletAl;//子彈清單
	boolean flag=true;//循環標志位
	OtherSurfaceView ov;
	String s1,s2,s3,s4,s,ss,sss,ssss;
	
	public TempBulletGoThread(ArrayList<TempBullet> tempbulletAl,OtherSurfaceView ov)
	{
		this.tempbulletAl=tempbulletAl;
		this.ov=ov;
	}
	
	public void run()
	{
		while(flag)
		{//循環定時搬移炮彈
			try
			{
				synchronized(ov.gdMain.dataLock) 	//將主資料鎖上
				{
					ov.gdMain.followbulletAl.clear();
					ov.gdMain.followTailAl.clear();
					synchronized(ov.gdMainDraw.dataLock) //將繪制資料鎖上
					{
						ov.gdMainDraw.followbulletAl.clear();
						ov.gdMainDraw.followTailAl.clear();
					}
					for(int i=0;i<tempbulletAl.size();i++)
					{
						TempBullet b=tempbulletAl.get(i);
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
						ov.gdMain.followTailAl.add(new float[][]{b.tempData[0],b.tempData[1],b.tempData[2],b.tempData[3],b.tempData[4],b.tempData[5],b.tempData[6],b.tempData[7],b.tempData[8]});
						
						switch(ov.gdMain.followTailAl.size())
						{
						case 0:
							break;
						case 1:
							s=ov.gdMain.followTailAl.get(0)[0][0]+"<#>"+ov.gdMain.followTailAl.get(0)[0][1]+"<#>"+ov.gdMain.followTailAl.get(0)[0][2]+"<#>"+ov.gdMain.followTailAl.get(0)[1][0]+"<#>"+ov.gdMain.followTailAl.get(0)[1][1]+"<#>"+ov.gdMain.followTailAl.get(0)[1][2]+"<#>"+ov.gdMain.followTailAl.get(0)[2][0]+"<#>"+ov.gdMain.followTailAl.get(0)[2][1]+"<#>"+ov.gdMain.followTailAl.get(0)[2][2]+"<#>"+ov.gdMain.followTailAl.get(0)[3][0]+"<#>"+ov.gdMain.followTailAl.get(0)[3][1]+"<#>"+ov.gdMain.followTailAl.get(0)[3][2]+"<#>"+ov.gdMain.followTailAl.get(0)[4][0]+"<#>"+ov.gdMain.followTailAl.get(0)[4][1]+"<#>"+ov.gdMain.followTailAl.get(0)[4][2]+"<#>"+ov.gdMain.followTailAl.get(0)[5][0]+"<#>"+ov.gdMain.followTailAl.get(0)[5][1]+"<#>"+ov.gdMain.followTailAl.get(0)[5][2]+"<#>"+ov.gdMain.followTailAl.get(0)[6][0]+"<#>"+ov.gdMain.followTailAl.get(0)[6][1]+"<#>"+ov.gdMain.followTailAl.get(0)[6][2]+"<#>"+ov.gdMain.followTailAl.get(0)[7][0]+"<#>"+ov.gdMain.followTailAl.get(0)[7][1]+"<#>"+ov.gdMain.followTailAl.get(0)[7][2]+"<#>"+ov.gdMain.followTailAl.get(0)[8][0]+"<#>"+ov.gdMain.followTailAl.get(0)[8][1]+"<#>"+ov.gdMain.followTailAl.get(0)[8][2]+"<#>";
							ov.activity.sendMessage(s);
							break;
						case 2:
							s=ov.gdMain.followTailAl.get(0)[0][0]+"<#>"+ov.gdMain.followTailAl.get(0)[0][1]+"<#>"+ov.gdMain.followTailAl.get(0)[0][2]+"<#>"+ov.gdMain.followTailAl.get(0)[1][0]+"<#>"+ov.gdMain.followTailAl.get(0)[1][1]+"<#>"+ov.gdMain.followTailAl.get(0)[1][2]+"<#>"+ov.gdMain.followTailAl.get(0)[2][0]+"<#>"+ov.gdMain.followTailAl.get(0)[2][1]+"<#>"+ov.gdMain.followTailAl.get(0)[2][2]+"<#>"+ov.gdMain.followTailAl.get(0)[3][0]+"<#>"+ov.gdMain.followTailAl.get(0)[3][1]+"<#>"+ov.gdMain.followTailAl.get(0)[3][2]+"<#>"+ov.gdMain.followTailAl.get(0)[4][0]+"<#>"+ov.gdMain.followTailAl.get(0)[4][1]+"<#>"+ov.gdMain.followTailAl.get(0)[4][2]+"<#>"+ov.gdMain.followTailAl.get(0)[5][0]+"<#>"+ov.gdMain.followTailAl.get(0)[5][1]+"<#>"+ov.gdMain.followTailAl.get(0)[5][2]+"<#>"+ov.gdMain.followTailAl.get(0)[6][0]+"<#>"+ov.gdMain.followTailAl.get(0)[6][1]+"<#>"+ov.gdMain.followTailAl.get(0)[6][2]+"<#>"+ov.gdMain.followTailAl.get(0)[7][0]+"<#>"+ov.gdMain.followTailAl.get(0)[7][1]+"<#>"+ov.gdMain.followTailAl.get(0)[7][2]+"<#>"+ov.gdMain.followTailAl.get(0)[8][0]+"<#>"+ov.gdMain.followTailAl.get(0)[8][1]+"<#>"+ov.gdMain.followTailAl.get(0)[8][2]+"<#>"
							 +ov.gdMain.followTailAl.get(1)[0][0]+"<#>"+ov.gdMain.followTailAl.get(1)[0][1]+"<#>"+ov.gdMain.followTailAl.get(1)[0][2]+"<#>"+ov.gdMain.followTailAl.get(1)[1][0]+"<#>"+ov.gdMain.followTailAl.get(1)[1][1]+"<#>"+ov.gdMain.followTailAl.get(1)[1][2]+"<#>"+ov.gdMain.followTailAl.get(1)[2][0]+"<#>"+ov.gdMain.followTailAl.get(1)[2][1]+"<#>"+ov.gdMain.followTailAl.get(1)[2][2]+"<#>"+ov.gdMain.followTailAl.get(1)[3][0]+"<#>"+ov.gdMain.followTailAl.get(1)[3][1]+"<#>"+ov.gdMain.followTailAl.get(1)[3][2]+"<#>"+ov.gdMain.followTailAl.get(1)[4][0]+"<#>"+ov.gdMain.followTailAl.get(1)[4][1]+"<#>"+ov.gdMain.followTailAl.get(1)[4][2]+"<#>"+ov.gdMain.followTailAl.get(1)[5][0]+"<#>"+ov.gdMain.followTailAl.get(1)[5][1]+"<#>"+ov.gdMain.followTailAl.get(1)[5][2]+"<#>"+ov.gdMain.followTailAl.get(1)[6][0]+"<#>"+ov.gdMain.followTailAl.get(1)[6][1]+"<#>"+ov.gdMain.followTailAl.get(1)[6][2]+"<#>"+ov.gdMain.followTailAl.get(1)[7][0]+"<#>"+ov.gdMain.followTailAl.get(1)[7][1]+"<#>"+ov.gdMain.followTailAl.get(1)[7][2]+"<#>"+ov.gdMain.followTailAl.get(1)[8][0]+"<#>"+ov.gdMain.followTailAl.get(1)[8][1]+"<#>"+ov.gdMain.followTailAl.get(1)[8][2]+"<#>";
							ov.activity.sendMessage(s);
							break;
						case 3:
							s=ov.gdMain.followTailAl.get(0)[0][0]+"<#>"+ov.gdMain.followTailAl.get(0)[0][1]+"<#>"+ov.gdMain.followTailAl.get(0)[0][2]+"<#>"+ov.gdMain.followTailAl.get(0)[1][0]+"<#>"+ov.gdMain.followTailAl.get(0)[1][1]+"<#>"+ov.gdMain.followTailAl.get(0)[1][2]+"<#>"+ov.gdMain.followTailAl.get(0)[2][0]+"<#>"+ov.gdMain.followTailAl.get(0)[2][1]+"<#>"+ov.gdMain.followTailAl.get(0)[2][2]+"<#>"+ov.gdMain.followTailAl.get(0)[3][0]+"<#>"+ov.gdMain.followTailAl.get(0)[3][1]+"<#>"+ov.gdMain.followTailAl.get(0)[3][2]+"<#>"+ov.gdMain.followTailAl.get(0)[4][0]+"<#>"+ov.gdMain.followTailAl.get(0)[4][1]+"<#>"+ov.gdMain.followTailAl.get(0)[4][2]+"<#>"+ov.gdMain.followTailAl.get(0)[5][0]+"<#>"+ov.gdMain.followTailAl.get(0)[5][1]+"<#>"+ov.gdMain.followTailAl.get(0)[5][2]+"<#>"+ov.gdMain.followTailAl.get(0)[6][0]+"<#>"+ov.gdMain.followTailAl.get(0)[6][1]+"<#>"+ov.gdMain.followTailAl.get(0)[6][2]+"<#>"+ov.gdMain.followTailAl.get(0)[7][0]+"<#>"+ov.gdMain.followTailAl.get(0)[7][1]+"<#>"+ov.gdMain.followTailAl.get(0)[7][2]+"<#>"+ov.gdMain.followTailAl.get(0)[8][0]+"<#>"+ov.gdMain.followTailAl.get(0)[8][1]+"<#>"+ov.gdMain.followTailAl.get(0)[8][2]+"<#>"
							 +ov.gdMain.followTailAl.get(1)[0][0]+"<#>"+ov.gdMain.followTailAl.get(1)[0][1]+"<#>"+ov.gdMain.followTailAl.get(1)[0][2]+"<#>"+ov.gdMain.followTailAl.get(1)[1][0]+"<#>"+ov.gdMain.followTailAl.get(1)[1][1]+"<#>"+ov.gdMain.followTailAl.get(1)[1][2]+"<#>"+ov.gdMain.followTailAl.get(1)[2][0]+"<#>"+ov.gdMain.followTailAl.get(1)[2][1]+"<#>"+ov.gdMain.followTailAl.get(1)[2][2]+"<#>"+ov.gdMain.followTailAl.get(1)[3][0]+"<#>"+ov.gdMain.followTailAl.get(1)[3][1]+"<#>"+ov.gdMain.followTailAl.get(1)[3][2]+"<#>"+ov.gdMain.followTailAl.get(1)[4][0]+"<#>"+ov.gdMain.followTailAl.get(1)[4][1]+"<#>"+ov.gdMain.followTailAl.get(1)[4][2]+"<#>"+ov.gdMain.followTailAl.get(1)[5][0]+"<#>"+ov.gdMain.followTailAl.get(1)[5][1]+"<#>"+ov.gdMain.followTailAl.get(1)[5][2]+"<#>"+ov.gdMain.followTailAl.get(1)[6][0]+"<#>"+ov.gdMain.followTailAl.get(1)[6][1]+"<#>"+ov.gdMain.followTailAl.get(1)[6][2]+"<#>"+ov.gdMain.followTailAl.get(1)[7][0]+"<#>"+ov.gdMain.followTailAl.get(1)[7][1]+"<#>"+ov.gdMain.followTailAl.get(1)[7][2]+"<#>"+ov.gdMain.followTailAl.get(1)[8][0]+"<#>"+ov.gdMain.followTailAl.get(1)[8][1]+"<#>"+ov.gdMain.followTailAl.get(1)[8][2]+"<#>"
							 +ov.gdMain.followTailAl.get(2)[0][0]+"<#>"+ov.gdMain.followTailAl.get(2)[0][1]+"<#>"+ov.gdMain.followTailAl.get(2)[0][2]+"<#>"+ov.gdMain.followTailAl.get(2)[1][0]+"<#>"+ov.gdMain.followTailAl.get(2)[1][1]+"<#>"+ov.gdMain.followTailAl.get(2)[1][2]+"<#>"+ov.gdMain.followTailAl.get(2)[2][0]+"<#>"+ov.gdMain.followTailAl.get(2)[2][1]+"<#>"+ov.gdMain.followTailAl.get(2)[2][2]+"<#>"+ov.gdMain.followTailAl.get(2)[3][0]+"<#>"+ov.gdMain.followTailAl.get(2)[3][1]+"<#>"+ov.gdMain.followTailAl.get(2)[3][2]+"<#>"+ov.gdMain.followTailAl.get(2)[4][0]+"<#>"+ov.gdMain.followTailAl.get(2)[4][1]+"<#>"+ov.gdMain.followTailAl.get(2)[4][2]+"<#>"+ov.gdMain.followTailAl.get(2)[5][0]+"<#>"+ov.gdMain.followTailAl.get(2)[5][1]+"<#>"+ov.gdMain.followTailAl.get(2)[5][2]+"<#>"+ov.gdMain.followTailAl.get(2)[6][0]+"<#>"+ov.gdMain.followTailAl.get(2)[6][1]+"<#>"+ov.gdMain.followTailAl.get(2)[6][2]+"<#>"+ov.gdMain.followTailAl.get(2)[7][0]+"<#>"+ov.gdMain.followTailAl.get(2)[7][1]+"<#>"+ov.gdMain.followTailAl.get(2)[7][2]+"<#>"+ov.gdMain.followTailAl.get(2)[8][0]+"<#>"+ov.gdMain.followTailAl.get(2)[8][1]+"<#>"+ov.gdMain.followTailAl.get(2)[8][2]+"<#>";
							ov.activity.sendMessage(s);
							break;
						case 4:
							s=ov.gdMain.followTailAl.get(0)[0][0]+"<#>"+ov.gdMain.followTailAl.get(0)[0][1]+"<#>"+ov.gdMain.followTailAl.get(0)[0][2]+"<#>"+ov.gdMain.followTailAl.get(0)[1][0]+"<#>"+ov.gdMain.followTailAl.get(0)[1][1]+"<#>"+ov.gdMain.followTailAl.get(0)[1][2]+"<#>"+ov.gdMain.followTailAl.get(0)[2][0]+"<#>"+ov.gdMain.followTailAl.get(0)[2][1]+"<#>"+ov.gdMain.followTailAl.get(0)[2][2]+"<#>"+ov.gdMain.followTailAl.get(0)[3][0]+"<#>"+ov.gdMain.followTailAl.get(0)[3][1]+"<#>"+ov.gdMain.followTailAl.get(0)[3][2]+"<#>"+ov.gdMain.followTailAl.get(0)[4][0]+"<#>"+ov.gdMain.followTailAl.get(0)[4][1]+"<#>"+ov.gdMain.followTailAl.get(0)[4][2]+"<#>"+ov.gdMain.followTailAl.get(0)[5][0]+"<#>"+ov.gdMain.followTailAl.get(0)[5][1]+"<#>"+ov.gdMain.followTailAl.get(0)[5][2]+"<#>"+ov.gdMain.followTailAl.get(0)[6][0]+"<#>"+ov.gdMain.followTailAl.get(0)[6][1]+"<#>"+ov.gdMain.followTailAl.get(0)[6][2]+"<#>"+ov.gdMain.followTailAl.get(0)[7][0]+"<#>"+ov.gdMain.followTailAl.get(0)[7][1]+"<#>"+ov.gdMain.followTailAl.get(0)[7][2]+"<#>"+ov.gdMain.followTailAl.get(0)[8][0]+"<#>"+ov.gdMain.followTailAl.get(0)[8][1]+"<#>"+ov.gdMain.followTailAl.get(0)[8][2]+"<#>"
							 +ov.gdMain.followTailAl.get(1)[0][0]+"<#>"+ov.gdMain.followTailAl.get(1)[0][1]+"<#>"+ov.gdMain.followTailAl.get(1)[0][2]+"<#>"+ov.gdMain.followTailAl.get(1)[1][0]+"<#>"+ov.gdMain.followTailAl.get(1)[1][1]+"<#>"+ov.gdMain.followTailAl.get(1)[1][2]+"<#>"+ov.gdMain.followTailAl.get(1)[2][0]+"<#>"+ov.gdMain.followTailAl.get(1)[2][1]+"<#>"+ov.gdMain.followTailAl.get(1)[2][2]+"<#>"+ov.gdMain.followTailAl.get(1)[3][0]+"<#>"+ov.gdMain.followTailAl.get(1)[3][1]+"<#>"+ov.gdMain.followTailAl.get(1)[3][2]+"<#>"+ov.gdMain.followTailAl.get(1)[4][0]+"<#>"+ov.gdMain.followTailAl.get(1)[4][1]+"<#>"+ov.gdMain.followTailAl.get(1)[4][2]+"<#>"+ov.gdMain.followTailAl.get(1)[5][0]+"<#>"+ov.gdMain.followTailAl.get(1)[5][1]+"<#>"+ov.gdMain.followTailAl.get(1)[5][2]+"<#>"+ov.gdMain.followTailAl.get(1)[6][0]+"<#>"+ov.gdMain.followTailAl.get(1)[6][1]+"<#>"+ov.gdMain.followTailAl.get(1)[6][2]+"<#>"+ov.gdMain.followTailAl.get(1)[7][0]+"<#>"+ov.gdMain.followTailAl.get(1)[7][1]+"<#>"+ov.gdMain.followTailAl.get(1)[7][2]+"<#>"+ov.gdMain.followTailAl.get(1)[8][0]+"<#>"+ov.gdMain.followTailAl.get(1)[8][1]+"<#>"+ov.gdMain.followTailAl.get(1)[8][2]+"<#>"
							 +ov.gdMain.followTailAl.get(2)[0][0]+"<#>"+ov.gdMain.followTailAl.get(2)[0][1]+"<#>"+ov.gdMain.followTailAl.get(2)[0][2]+"<#>"+ov.gdMain.followTailAl.get(2)[1][0]+"<#>"+ov.gdMain.followTailAl.get(2)[1][1]+"<#>"+ov.gdMain.followTailAl.get(2)[1][2]+"<#>"+ov.gdMain.followTailAl.get(2)[2][0]+"<#>"+ov.gdMain.followTailAl.get(2)[2][1]+"<#>"+ov.gdMain.followTailAl.get(2)[2][2]+"<#>"+ov.gdMain.followTailAl.get(2)[3][0]+"<#>"+ov.gdMain.followTailAl.get(2)[3][1]+"<#>"+ov.gdMain.followTailAl.get(2)[3][2]+"<#>"+ov.gdMain.followTailAl.get(2)[4][0]+"<#>"+ov.gdMain.followTailAl.get(2)[4][1]+"<#>"+ov.gdMain.followTailAl.get(2)[4][2]+"<#>"+ov.gdMain.followTailAl.get(2)[5][0]+"<#>"+ov.gdMain.followTailAl.get(2)[5][1]+"<#>"+ov.gdMain.followTailAl.get(2)[5][2]+"<#>"+ov.gdMain.followTailAl.get(2)[6][0]+"<#>"+ov.gdMain.followTailAl.get(2)[6][1]+"<#>"+ov.gdMain.followTailAl.get(2)[6][2]+"<#>"+ov.gdMain.followTailAl.get(2)[7][0]+"<#>"+ov.gdMain.followTailAl.get(2)[7][1]+"<#>"+ov.gdMain.followTailAl.get(2)[7][2]+"<#>"+ov.gdMain.followTailAl.get(2)[8][0]+"<#>"+ov.gdMain.followTailAl.get(2)[8][1]+"<#>"+ov.gdMain.followTailAl.get(2)[8][2]+"<#>"
							 +ov.gdMain.followTailAl.get(3)[0][0]+"<#>"+ov.gdMain.followTailAl.get(3)[0][1]+"<#>"+ov.gdMain.followTailAl.get(3)[0][2]+"<#>"+ov.gdMain.followTailAl.get(3)[1][0]+"<#>"+ov.gdMain.followTailAl.get(3)[1][1]+"<#>"+ov.gdMain.followTailAl.get(3)[1][2]+"<#>"+ov.gdMain.followTailAl.get(3)[2][0]+"<#>"+ov.gdMain.followTailAl.get(3)[2][1]+"<#>"+ov.gdMain.followTailAl.get(3)[2][2]+"<#>"+ov.gdMain.followTailAl.get(3)[3][0]+"<#>"+ov.gdMain.followTailAl.get(3)[3][1]+"<#>"+ov.gdMain.followTailAl.get(3)[3][2]+"<#>"+ov.gdMain.followTailAl.get(3)[4][0]+"<#>"+ov.gdMain.followTailAl.get(3)[4][1]+"<#>"+ov.gdMain.followTailAl.get(3)[4][2]+"<#>"+ov.gdMain.followTailAl.get(3)[5][0]+"<#>"+ov.gdMain.followTailAl.get(3)[5][1]+"<#>"+ov.gdMain.followTailAl.get(3)[5][2]+"<#>"+ov.gdMain.followTailAl.get(3)[6][0]+"<#>"+ov.gdMain.followTailAl.get(3)[6][1]+"<#>"+ov.gdMain.followTailAl.get(3)[6][2]+"<#>"+ov.gdMain.followTailAl.get(3)[7][0]+"<#>"+ov.gdMain.followTailAl.get(3)[7][1]+"<#>"+ov.gdMain.followTailAl.get(3)[7][2]+"<#>"+ov.gdMain.followTailAl.get(3)[8][0]+"<#>"+ov.gdMain.followTailAl.get(3)[8][1]+"<#>"+ov.gdMain.followTailAl.get(3)[8][2]+"<#>";
							ov.activity.sendMessage(s);
						}
						
						synchronized(ov.gdMainDraw.dataLock) //將繪制資料鎖上
						{
							ov.gdMainDraw.followTailAl.add(new float[][]{b.tempData[0],b.tempData[1],b.tempData[2],b.tempData[3],b.tempData[4],b.tempData[5],b.tempData[6],b.tempData[7],b.tempData[8]});
						}
						
						ov.gdMain.followbullet[0]=b.cuerrentX;
						ov.gdMain.followbullet[1]=b.cuerrentY;
						ov.gdMain.followbullet[2]=b.cuerrentZ;
						ov.gdMain.followbullet[3]=b.cuerrentAngle;
						
						ov.gdMain.followbulletAl.add(new float[]{b.cuerrentX,b.cuerrentY,b.cuerrentZ,b.cuerrentAngle});
						switch (ov.gdMain.followbulletAl.size())
						{
							case 0:
							break;
							case 1:
							s1=ov.gdMain.followbulletAl.get(0)[0]+"<#>"+ov.gdMain.followbulletAl.get(0)[1]+"<#>"+ov.gdMain.followbulletAl.get(0)[2]+"<#>"+ov.gdMain.followbulletAl.get(0)[3]+"<#>";
							ov.activity.sendMessage(s1);
							break;
							case 2:
							s1=ov.gdMain.followbulletAl.get(0)[0]+"<#>"+ov.gdMain.followbulletAl.get(0)[1]+"<#>"+ov.gdMain.followbulletAl.get(0)[2]+"<#>"+ov.gdMain.followbulletAl.get(0)[3]+"<#>"
							+ov.gdMain.followbulletAl.get(1)[0]+"<#>"+ov.gdMain.followbulletAl.get(1)[1]+"<#>"+ov.gdMain.followbulletAl.get(1)[2]+"<#>"+ov.gdMain.followbulletAl.get(1)[3]+"<#>";
							ov.activity.sendMessage(s1);
							break;
							case 3:
							s1=ov.gdMain.followbulletAl.get(0)[0]+"<#>"+ov.gdMain.followbulletAl.get(0)[1]+"<#>"+ov.gdMain.followbulletAl.get(0)[2]+"<#>"+ov.gdMain.followbulletAl.get(0)[3]+"<#>"
							+ov.gdMain.followbulletAl.get(1)[0]+"<#>"+ov.gdMain.followbulletAl.get(1)[1]+"<#>"+ov.gdMain.followbulletAl.get(1)[2]+"<#>"+ov.gdMain.followbulletAl.get(1)[3]+"<#>"
							+ov.gdMain.followbulletAl.get(2)[0]+"<#>"+ov.gdMain.followbulletAl.get(2)[1]+"<#>"+ov.gdMain.followbulletAl.get(2)[2]+"<#>"+ov.gdMain.followbulletAl.get(2)[3]+"<#>";
						
							ov.activity.sendMessage(s1);
							break;
							case 4:
							s1=ov.gdMain.followbulletAl.get(0)[0]+"<#>"+ov.gdMain.followbulletAl.get(0)[1]+"<#>"+ov.gdMain.followbulletAl.get(0)[2]+"<#>"+ov.gdMain.followbulletAl.get(0)[3]+"<#>"
							+ov.gdMain.followbulletAl.get(1)[0]+"<#>"+ov.gdMain.followbulletAl.get(1)[1]+"<#>"+ov.gdMain.followbulletAl.get(1)[2]+"<#>"+ov.gdMain.followbulletAl.get(1)[3]+"<#>"
							+ov.gdMain.followbulletAl.get(2)[0]+"<#>"+ov.gdMain.followbulletAl.get(2)[1]+"<#>"+ov.gdMain.followbulletAl.get(2)[2]+"<#>"+ov.gdMain.followbulletAl.get(2)[3]+"<#>"
							+ov.gdMain.followbulletAl.get(3)[0]+"<#>"+ov.gdMain.followbulletAl.get(3)[1]+"<#>"+ov.gdMain.followbulletAl.get(3)[2]+"<#>"+ov.gdMain.followbulletAl.get(3)[3]+"<#>";
						
							ov.activity.sendMessage(s1);
							break;
						}
						synchronized(ov.gdMainDraw.dataLock) //將繪制資料鎖上
						{
							ov.gdMainDraw.followbulletAl.add(new float[]{b.cuerrentX,b.cuerrentY,b.cuerrentZ,b.cuerrentAngle});
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

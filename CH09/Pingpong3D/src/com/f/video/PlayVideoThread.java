package com.f.video;

import static com.f.pingpong.Constant.*;

import java.util.List;
//影片播放執行緒
public class PlayVideoThread extends Thread
{
	public boolean wFlag=true;//播放完畢標志位
	public PlayVideoThread(){}//無參建構器
	public long startTime,nowTime,pauseTime;//開始時間，目前時間，暫停時間
	
	@Override
	public void run()
	{
		startTime = System.nanoTime();//獲得開始時間
		FrameData.currIndex = 0;//播放索引置為0
		List<FrameData> tempFrameDataList; //臨時播放框清單
		if(IS_HELP)//若果是播放幫助影片
		{
			tempFrameDataList = FrameData.helpFrameDataList;
		}
		else//若果是播放標準比賽影片
		{
			tempFrameDataList = FrameData.playFrameDataList;
		}
		while(wFlag)
		{
			if(!HELP_PAUSE)//若果沒有在播放幫助影片時按暫停
			{
				if(!(FrameData.currIndex >= tempFrameDataList.size()-1))//若果沒有播放到最後一框
				{
					synchronized(videoLock)//同步鎖
					{
						nowTime = System.nanoTime() - startTime;//獲得目前時間戳
						//將目前時間戳之前的框都播放掉
						if(tempFrameDataList.get(FrameData.currIndex).timestamp < nowTime)
						{
							FrameData.currIndex++;
						}
						//若果是在播放幫助影片，則在播放完畢後循環播放
						if(IS_HELP && FrameData.currIndex >= tempFrameDataList.size()-1)
						{
							startTime = System.nanoTime();//從新取得開始時間
							FrameData.currIndex = 0;//播放索引重設
						}
					}
				}
				else
				{
					wFlag=false;//影片播放完畢
				}
			}
			try
			{
				Thread.sleep(10);//執行緒睡眠10毫秒
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();//列印例外訊息
			}
		}
		IS_PLAY_VIDEO = false;//影片播放完畢
	}
}
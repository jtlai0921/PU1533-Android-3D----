package com.bn.zxl;



import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import static com.bn.util.Constant.*;
public class SelectView extends RootView 
{

	MyActivity activity;
	
	HashMap<Integer, Bitmap> bitmapOpenList;//�C���Ϥ��M��
	HashMap<Integer, Bitmap> bitmapCloseList;//�C����Ϥ��M��
	
	Bitmap selectViewBackground;//�I����
	Bitmap backButton;//�Ǧ^���s��
	float firstBitmap_x;
	float firstBitmap_y;
	float back_x=49;//�Ǧ^���sø��y��
	float back_y=463;
	float bitMapWidth;//�C���Ϫ��e
	float xOffset;//�ư�x�첾
	float previousX=0;//�W��x�y��,��xoffset��
	float mpreviousX=0;//�W��x�y�СA��move_flag��
	
	boolean move_flag=false;
	public SelectView(MyActivity activity) 
	{
		this.activity=activity;
		bitmapOpenList=new HashMap<Integer, Bitmap>();
		bitmapCloseList=new HashMap<Integer, Bitmap>();
		initBitmap();
	}
	
	protected void initBitmap()
	{
		selectViewBackground=pic2DHashMap.get("mainmenu.png");
		backButton=pic2DHashMap.get("back.png");
		for(int i=0;i<6;i++)
		{
			bitmapOpenList.put(i, pic2DHashMap.get("room_"+(i+1)+"_open.png"));
			bitmapCloseList.put(i, pic2DHashMap.get("room_"+(i+1)+"_close.png"));
		}
		firstBitmap_x=Width/2-bitmapOpenList.get(0).getWidth()/2;
		firstBitmap_y=Height/2-bitmapOpenList.get(0).getHeight()/2;
		bitMapWidth=bitmapOpenList.get(0).getHeight();
	}
	@Override
	public void onDraw(Canvas canvas) 
	{
		
		canvas.drawColor(Color.argb(255, 0, 0, 0));//�M���ù����¦�
		canvas.drawBitmap(selectViewBackground,0,0, null);//�e�I��
		canvas.drawBitmap(backButton, back_x, back_y, null);
		for(int i=0;i<bitmapOpenList.size();i++)
		{
			if(i<=getPassNum())
			{
				canvas.drawBitmap(bitmapOpenList.get(i), firstBitmap_x+i*(bitMapWidth+30)+xOffset, firstBitmap_y, null);
			}else 
			{
				canvas.drawBitmap(bitmapCloseList.get(i), firstBitmap_x+i*(bitMapWidth+30)+xOffset, firstBitmap_y, null);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		
		float dx=(float)(event.getX()-previousX);
		
		float curX;
		float curY;
		if (event.getAction()==MotionEvent.ACTION_DOWN) 
		{
			previousX=event.getX();
			mpreviousX=event.getX();
		}else if (event.getAction()==MotionEvent.ACTION_MOVE) 
		{
			
			if (xOffset+dx>=-(bitMapWidth+30)*bitmapOpenList.size()&&xOffset+dx<=0) 
			{
				xOffset+=dx;
			}
			previousX=event.getX();
			
		}else if (event.getAction()==MotionEvent.ACTION_UP&&bitmapHitTest(event.getX(), event.getY(), back_x, back_y, backButton)) 
		{
		
			activity.hd.sendEmptyMessage(1);
		}else if (event.getAction()==MotionEvent.ACTION_UP) 
		{
			if(Math.abs(event.getX()-mpreviousX)>5)
			{
				move_flag=true;
			}
			else
			{
				move_flag=false;
			}
		}
		for (int i = 0; i < getPassNum()+1; i++) 
		{
			curX=firstBitmap_x+i*(bitMapWidth+30)+xOffset;
			curY=firstBitmap_y;
			if (event.getAction()==MotionEvent.ACTION_UP&&bitmapHitTest(event.getX(), event.getY(), curX, curY, bitmapOpenList.get(i))) 
			{	
				if(!move_flag)
				{
					CurrentLevel=i;
					activity.hd.sendEmptyMessage(3);
				}
			}
		}
		
		return true;
	}

}

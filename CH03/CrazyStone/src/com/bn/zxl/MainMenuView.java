package com.bn.zxl;

import static com.bn.util.Constant.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

public class MainMenuView extends RootView 
{
	MyActivity activity;
	
	Bitmap background;
	Bitmap button_play_up;
	Bitmap button_play_down;
	Bitmap button_bg_music_open;
	Bitmap button_bg_music_close;
	Bitmap button_e_music_open;
	Bitmap button_e_music_close;
	Bitmap help_button;
	
	private float button_play_x;//按鈕圖片的左上角X座標
    private float button_play_y;//按鈕圖片的左上角X座標
    private float button_bg_music_x;
    private float button_bg_music_y;
    private float button_e_music_x;
    private float button_e_music_y;
    private float button_help_x;
    private float button_help_y;
    
    boolean play_flag=false;
    
    public MainMenuView(MyActivity activity)
    {
    	this.activity=activity;
		initBitmap();
		initXY();
		if (getBackgroundMusicStatus()) {
			activity.soundutil.playBackgroungMusic();
		}
    }
    
    public void initXY()
    {
    	button_play_x=Width/2-button_play_up.getWidth()/2;
		button_play_y=Height/2-button_play_up.getHeight()/2;
		button_e_music_x=49;
		button_e_music_y=340;
		button_bg_music_x=49;
		button_bg_music_y=440;
		button_help_x=49;
		button_help_y=240;
    }
    
    public void initBitmap()
	{
    	
		background=pic2DHashMap.get("mainmenu.png");
		button_play_up=pic2DHashMap.get("play_up.png");
		button_play_down=pic2DHashMap.get("play_down.png");
		button_bg_music_open=pic2DHashMap.get("bg_music_open.png");
		button_bg_music_close=pic2DHashMap.get("bg_music_close.png");
		button_e_music_open=pic2DHashMap.get("e_music_open.png");
		button_e_music_close=pic2DHashMap.get("e_music_close.png");
		help_button=pic2DHashMap.get("help_button.png");
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		//play按鈕的按下抬起效果
		if(
				event.getAction()!=MotionEvent.ACTION_UP&&
				bitmapHitTest(event.getX(), event.getY(), button_play_x, button_play_y, button_play_up)
		  )
		{
			play_flag=true;
			activity.soundutil.playEffectSound(BUTTON_SOUND, 0);
			
		}else {
			play_flag=false;
			
		}
		//在play上抬起切到其他界面
		if(
				event.getAction()==MotionEvent.ACTION_UP&&
				bitmapHitTest(event.getX(), event.getY(), button_play_x, button_play_y, button_play_up)
		  )
		{
			activity.hd.sendEmptyMessage(2);
		}
		//開關音效
		if(
				event.getAction()==MotionEvent.ACTION_UP&&
				bitmapHitTest(event.getX(), event.getY(), button_e_music_x, button_e_music_y, button_e_music_open)
		  )
		{
			setEffectSoundStatus(activity.sharedUtil, !getEffectSoundStatus());
		}
		//開關背景音樂
		if(
				event.getAction()==MotionEvent.ACTION_UP&&
				bitmapHitTest(event.getX(), event.getY(), button_bg_music_x, button_bg_music_y, button_bg_music_open)
		  )
		{
			setBackgroundMusicStatu(activity.sharedUtil, !getBackgroundMusicStatus());
			if (getBackgroundMusicStatus()) 
			{
				activity.soundutil.playBackgroungMusic();
			} else {
				activity.soundutil.stopBackgroungMusic();
			}
			
		}
		if (event.getAction()==MotionEvent.ACTION_UP&&bitmapHitTest(event.getX(), event.getY(), button_help_x, button_help_y,help_button)) 
		{
			activity.hd.sendEmptyMessage(4);
		}
		return true;
	}
	@Override
	public void onDraw(Canvas canvas) 
	{
		
		canvas.drawColor(Color.argb(255, 0, 0, 0));//清除螢幕為黑色
		canvas.drawBitmap(background,0,0, null);//畫背景
		//畫play按鈕
		if(play_flag){
			canvas.drawBitmap(button_play_down, button_play_x, button_play_y, null);
		}else {
			canvas.drawBitmap(button_play_up, button_play_x, button_play_y, null);
		}
		//畫音效按鈕
		if (getEffectSoundStatus()) {
			canvas.drawBitmap(button_e_music_open, button_e_music_x, button_e_music_y, null);
		} else {
			canvas.drawBitmap(button_e_music_close, button_e_music_x, button_e_music_y, null);
		}
		//畫背景音樂按鈕
		if (getBackgroundMusicStatus()) {
			canvas.drawBitmap(button_bg_music_open, button_bg_music_x, button_bg_music_y, null);
		} else {
			canvas.drawBitmap(button_bg_music_close, button_bg_music_x, button_bg_music_y, null);
		}
		canvas.drawBitmap(help_button, button_help_x, button_help_y, null);
		
	}
}

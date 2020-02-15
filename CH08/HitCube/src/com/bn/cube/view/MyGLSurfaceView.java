package com.bn.cube.view;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView 
{
   
	 public MyGLSurfaceView(Context context) {
		super(context);
		
	}
	@Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        return false;
    }
}

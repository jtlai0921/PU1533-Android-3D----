package tstc.fxq.utils;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class PicLoadUtil {
	// 縮放旋轉圖片的方法
	public static Bitmap scaleToFit(Bitmap bm, float ratio)// 縮放圖片的方法
	{
		float width = bm.getWidth(); // 圖片寬度
		float height = bm.getHeight();// 圖片高度

		Matrix m1 = new Matrix();
		m1.postScale(ratio, ratio);
		// 宣告點陣圖
		Bitmap bmResult = Bitmap.createBitmap
		(
				bm, 
				0, 0, 
				(int) width, (int) height, 
				m1, 
				true
		);
		return bmResult;
	}

	// 縮放旋轉圖片的方法,使圖片全螢幕,不等比縮放
	public static Bitmap scaleToFitFullScreen(Bitmap bm, float wRatio,
			float hRatio)// 縮放圖片的方法
	{
		float width = bm.getWidth(); // 圖片寬度
		float height = bm.getHeight();// 圖片高度

		Matrix m1 = new Matrix();
		m1.postScale(wRatio, hRatio);
		// 宣告點陣圖
		Bitmap bmResult = Bitmap.createBitmap
		(
				bm, 
				0, 0, 
				(int) width, (int) height, 
				m1, 
				true
		);
		return bmResult;
	}
}

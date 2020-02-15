package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;

public class ZhuoZiDi {
	int mProgram;// 自訂著色管執行緒序id
	int muMVPMatrixHandle;// 總變換矩陣參考id
	int muMMatrixHandle;// 位置、旋轉變換矩陣
	int maCameraHandle; // 攝影機位置屬性參考id
	int maPositionHandle; // 頂點位置屬性參考id
	int maNormalHandle; // 頂點法向量屬性參考id
	int maTexCoorHandle; // 頂點紋理座標屬性參考id
	int maSunLightLocationHandle;// 光源位置屬性參考id

	private FloatBuffer mVertexBuffer;// 頂點座標資料緩沖
	private FloatBuffer mNormalBuffer;// 頂點法向量資料緩沖
	private FloatBuffer mTexCoorBuffer; // 頂點紋理資料緩沖

	public float mOffsetX;
	public float mOffsetY;
	float scale;
	// int textureId;
	int vCount;

	public ZhuoZiDi(MySurfaceView mv, float scale, float length, float width,
			float[] textureCoors) {
		this.scale = scale;
		vCount = 36;
		float[] verteices = {

				// 頂面
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 1
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 2
				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 4

				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 4
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 2
				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 3

				// 後面
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 5
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 1
				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 8

				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 8
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 1
				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 4

				// 前面
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 2
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 6
				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 3

				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 3
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 6
				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 7

				// 下面
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 6
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 5
				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 7

				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 7
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 5
				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 8

				// 左面
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 5
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 6
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 1

				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 1
				-TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 6
				-TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 2

				// 右面
				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 4
				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 3
				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 8

				TABLE_UNIT_SIZE * length,
				-TABLE_UNIT_HIGHT * scale,
				-TABLE_UNIT_SIZE * width,// 8
				TABLE_UNIT_SIZE * length,
				TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width,// 3
				TABLE_UNIT_SIZE * length, -TABLE_UNIT_HIGHT * scale,
				TABLE_UNIT_SIZE * width// 7
		};

		ByteBuffer vbb = ByteBuffer.allocateDirect(verteices.length * 4);// 建立頂點座標資料緩沖
		vbb.order(ByteOrder.nativeOrder());// 設定位元組順序
		mVertexBuffer = vbb.asFloatBuffer();// 轉為float型緩沖
		mVertexBuffer.put(verteices);// 向緩沖區中放入頂點座標資料
		mVertexBuffer.position(0);// 設定緩沖區起始位置

		// 頂點法向量資料
		float normals[] = new float[] {
				// 頂面
				0, 1, 0, 0, 1, 0, 0, 1, 0,

				0, 1, 0, 0, 1, 0, 0, 1, 0,

				// 後面
				0, 0, -1, 0, 0, -1, 0, 0, -1,

				0, 0, -1, 0, 0, -1, 0, 0, -1,

				// 前面
				0, 0, 1, 0, 0, 1, 0, 0, 1,

				0, 0, 1, 0, 0, 1, 0, 0, 1,

				// 下面
				0, -1, 0, 0, -1, 0, 0, -1, 0,

				0, -1, 0, 0, -1, 0, 0, -1, 0,

				// 左面
				-1, 0, 0, -1, 0, 0, -1, 0, 0,

				-1, 0, 0, -1, 0, 0, -1, 0, 0,

				// 右面
				1, 0, 0, 1, 0, 0, 1, 0, 0,

				1, 0, 0, 1, 0, 0, 1, 0, 0, };
		// 建立繪制頂點法向量緩沖
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());// 設定位元組順序
		mNormalBuffer = nbb.asFloatBuffer();// 轉為float型緩沖
		mNormalBuffer.put(normals);// 向緩沖區中放入頂點座標資料
		mNormalBuffer.position(0);// 設定緩沖區起始位置

		// 建立紋理座標緩沖
		ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length * 4);// 建立頂點座標資料緩沖
		tbb.order(ByteOrder.nativeOrder());// 設定位元組順序
		mTexCoorBuffer = tbb.asFloatBuffer();// 轉為float型緩沖
		mTexCoorBuffer.put(textureCoors);// 向緩沖區中放入頂點座標資料
		mTexCoorBuffer.position(0);// 設定緩沖區起始位置

		// 起始化shader
		intShader(mv);
	}

	// 起始化shader
	public void intShader(MySurfaceView mv) {
		// 取得自訂著色管執行緒序id
		mProgram = ShaderManager.getTexLightShader();
		// 取得程式中頂點位置屬性參考id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// 取得程式中頂點經緯度屬性參考id
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
		// 取得程式中頂點法向量屬性參考id
		maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		// 取得程式中總變換矩陣參考id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		// 取得程式中攝影機位置參考id
		maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		// 取得程式中光源位置參考id
		maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
				"uLightLocationSun");
		// 取得位置、旋轉變換矩陣參考id
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
	}

	public void drawSelf(int texId) {
		MatrixState.rotate(mOffsetX, 1, 0, 0);
		MatrixState.rotate(mOffsetY, 0, 1, 0);

		// 制定使用某套shader程式
		GLES20.glUseProgram(mProgram);
		// 將最終變換矩陣傳入shader程式
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);
		// 將位置、旋轉變換矩陣傳入shader程式
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
				MatrixState.getMMatrix(), 0);
		// 將攝影機位置傳入shader程式
		GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
		// 將光源位置傳入shader程式
		GLES20.glUniform3fv(maSunLightLocationHandle, 1,
				MatrixState.lightPositionFB);

		// 為畫筆指定頂點位置資料
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 3 * 4, mVertexBuffer);
		// 為畫筆指定頂點經緯度資料
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,
				false, 2 * 4, mTexCoorBuffer);
		// 為畫筆指定頂點法向量資料
		GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, mNormalBuffer);
		// 容許頂點位置資料陣列
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		GLES20.glEnableVertexAttribArray(maNormalHandle);
		// 綁定紋理
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		// 繪制三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}
}

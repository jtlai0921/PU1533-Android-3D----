package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import tstc.fxq.utils.Vector3Util;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;

public class ZhuoBianTwo {
	float sqrt2 = (float) Math.sqrt(2);
	float l = EDGE;
	float k = EDGE_BIG;
	float height = UP_DOWN_HIGHT;
	float p = k * sqrt2 * 0.4f;

	float R = (l - k) / sqrt2;// 圓半徑
	float zOffset = k * sqrt2 / 2 - p;// 圓心點的z軸偏移量
	float os = (l - k + p * sqrt2) / 2;// 圓中心點的水平座標
	float ot = (l + k - p * sqrt2) / 2;// 圓中心點的垂直座標
	float linePointOffset = k - p / sqrt2;// 截角直線上點的計算偏移量

	float scale;

	UpRect one;
	TextureRect two;
	Cylinder three;
	TextureRect four;
	TextureRect five;

	float sqrt = (float) Math.sqrt(2);

	float mAngleX;
	float mAngleY;
	float mAngleZ;

	public ZhuoBianTwo(MySurfaceView mv, float scale) {
		this.scale = scale;

		one = new UpRect(mv, scale, l, k, p, 8);
		two = new TextureRect(mv, scale, k, height);
		three = new Cylinder(mv, scale, height, R, 8, 1);
		four = new TextureRect(mv, scale, p, height);
		five = new TextureRect(mv, scale, R * 2, height);
	}

	public void drawSelf(int textureId1, int textureId2, int textureId3) {
		MatrixState.rotate(mAngleX, 1, 0, 0);
		MatrixState.rotate(mAngleY, 0, 1, 0);
		MatrixState.rotate(mAngleZ, 0, 0, 1);

		MatrixState.pushMatrix();
		MatrixState.translate(0, scale * height / 2, 0);
		MatrixState.rotate(-45, 0, 1, 0);
		one.drawSelf(textureId3);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(-(l - k) / 2, 0, -scale * l / 2);
		MatrixState.rotate(180, 0, 1, 0);
		two.drawSelf(textureId2);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(scale * l / 2, 0, (l - k) / 2);
		MatrixState.rotate(90, 0, 1, 0);
		two.drawSelf(textureId2);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(-scale * (l / 2 - (p + (l - k) / sqrt) / sqrt),
				0, scale * (l / 2 - (p + (l - k) / sqrt) / sqrt));
		MatrixState.rotate(90, 0, 0, 1);
		three.drawSelf(textureId1);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState
				.translate(l / 2 - k + p / 2 / sqrt, 0, l / 2 - p / 2 / sqrt);
		MatrixState.rotate(225, 0, 1, 0);
		four.drawSelf(textureId1);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(-l / 2 + p / 2 / sqrt, 0, -l / 2 + k - p / 2
				/ sqrt);
		MatrixState.rotate(45, 0, 1, 0);
		four.drawSelf(textureId1);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(k / 2, 0, -k / 2);
		MatrixState.rotate(-45 + 180, 0, 1, 0);
		five.drawSelf(textureId2);
		MatrixState.popMatrix();
	}

	// 繪制上面鏤空矩形
	private class UpRect {
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

		int vCount;// 頂點數

		public UpRect(MySurfaceView mv, float scale, float r1, float r2,
				float l, int umber) {
			float angleScale = 180.0f / umber;// 切分一半度數變化量

			// 頂點座標
			ArrayList<Float> val = new ArrayList<Float>();// 用於存放頂點座標的清單
			ArrayList<Float> tal = new ArrayList<Float>();// 用於存放紋理座標的清單

			// 留邊部分頂點，紋理座標
			float xa1 = scale * (-r1 * sqrt2 / 2);
			float ya1 = 0;
			float za1 = 0;

			float sa1 = 0;
			float ta1 = 0;

			float xa2 = scale * (-(r1 - r2) * sqrt2 / 2);
			float ya2 = 0;
			float za2 = scale * (r2 * sqrt2 / 2);

			float sa2 = 0;
			float ta2 = r2 / r1;

			float xa3 = scale * (-(r1 - r2) * sqrt2 / 2);
			float ya3 = 0;
			float za3 = scale * (-r2 * sqrt2 / 2);

			float sa3 = r2 / r1;
			float ta3 = 0;

			val.add(xa1);
			val.add(ya1);
			val.add(za1);
			val.add(xa2);
			val.add(ya2);
			val.add(za2);
			val.add(xa3);
			val.add(ya3);
			val.add(za3);

			tal.add(sa1);
			tal.add(ta1);
			tal.add(sa2);
			tal.add(ta2);
			tal.add(sa3);
			tal.add(ta3);

			// 中間部分頂點，紋理座標
			for (int i = 0; i < umber; i++) {
				float x1 = (float) (-scale * R * Math.cos(Math.toRadians(i
						* angleScale)));
				float y1 = 0;
				float z1 = -scale * (r2 * sqrt2 / 2);

				float s1 = (r2 + ((R + x1) / sqrt2)) / r1;
				float t1 = ((R + x1) / sqrt2) / r1;

				float x2 = (float) (-scale * R * Math.cos(Math
						.toRadians((i + 1) * angleScale)));
				float y2 = 0;
				float z2 = -scale * (r2 * sqrt2 / 2);

				float s2 = (r2 + ((R + x2) / sqrt2)) / r1;
				float t2 = ((R + x2) / sqrt2) / r1;

				float x3 = (float) (-scale * R * Math.cos(Math
						.toRadians((i + 1) * angleScale)));
				float y3 = 0;
				float z3 = (float) (-scale * (R
						* Math.sin(Math.toRadians((i + 1) * angleScale)) - zOffset));

				float s3 = (float) ((os + R
						* Math.cos(Math.toRadians(180 - 45 - (i + 1)
								* angleScale))) / r1);
				float t3 = (float) ((ot - R
						* Math.sin(Math.toRadians(180 - 45 - (i + 1)
								* angleScale))) / r1);

				float x4 = (float) (-scale * R * Math.cos(Math.toRadians(i
						* angleScale)));
				float y4 = 0;
				float z4 = (float) (-scale * (R
						* Math.sin(Math.toRadians(i * angleScale)) - zOffset));

				float s4 = (float) ((os + R
						* Math.cos(Math.toRadians(180 - 45 - i * angleScale))) / r1);
				float t4 = (float) ((ot - R
						* Math.sin(Math.toRadians(180 - 45 - i * angleScale))) / r1);

				val.add(x1);
				val.add(y1);
				val.add(z1);
				val.add(x4);
				val.add(y4);
				val.add(z4);
				val.add(x2);
				val.add(y2);
				val.add(z2);

				val.add(x2);
				val.add(y2);
				val.add(z2);
				val.add(x4);
				val.add(y4);
				val.add(z4);
				val.add(x3);
				val.add(y3);
				val.add(z3);

				tal.add(s1);
				tal.add(t1);
				tal.add(s4);
				tal.add(t4);
				tal.add(s2);
				tal.add(t2);

				tal.add(s2);
				tal.add(t2);
				tal.add(s4);
				tal.add(t4);
				tal.add(s3);
				tal.add(t3);
			}

			// 留邊部分頂點，紋理座標
			float xb1 = scale * (r1 * sqrt2 / 2);
			float yb1 = 0;
			float zb1 = 0;

			float sb1 = 1;
			float tb1 = 1;

			float xb2 = scale * ((r1 - r2) * sqrt2 / 2);
			float yb2 = 0;
			float zb2 = scale * (r2 * sqrt2 / 2);

			float sb2 = (r1 - r2) / r1;
			float tb2 = 1;

			float xb3 = scale * ((r1 - r2) * sqrt2 / 2);
			float yb3 = 0;
			float zb3 = scale * (-r2 * sqrt2 / 2);

			float sb3 = 1;
			float tb3 = (r1 - r2) / r1;

			val.add(xb1);
			val.add(yb1);
			val.add(zb1);
			val.add(xb3);
			val.add(yb3);
			val.add(zb3);
			val.add(xb2);
			val.add(yb2);
			val.add(zb2);

			tal.add(sb1);
			tal.add(tb1);
			tal.add(sb3);
			tal.add(tb3);
			tal.add(sb2);
			tal.add(tb2);

			vCount = val.size() / 3;

			float[] vertices = new float[vCount * 3];
			for (int i = 0; i < vCount * 3; i++) {
				vertices[i] = val.get(i);
			}
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer = vbb.asFloatBuffer();
			mVertexBuffer.put(vertices);
			mVertexBuffer.position(0);

			// 頂點法向量資料
			float normals[] = new float[vertices.length];
			for (int i = 0; i < vertices.length; i += 9) {
				// 根據每個三角形的頂點，球出該三角形的平均法向量
				float[] result = Vector3Util.calTriangleAverageNormal(
						vertices[i + 0], vertices[i + 1], vertices[i + 2],
						vertices[i + 3], vertices[i + 4], vertices[i + 5],
						vertices[i + 6], vertices[i + 7], vertices[i + 8]);
				for (int j = i; j < i + 9; j += 3) {
					normals[j] = result[0];
					normals[j + 1] = result[0];
					normals[j + 2] = result[0];
				}
			}
			// 建立繪制頂點法向量緩沖
			ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
			nbb.order(ByteOrder.nativeOrder());// 設定位元組順序
			mNormalBuffer = nbb.asFloatBuffer();// 轉為float型緩沖
			mNormalBuffer.put(normals);// 向緩沖區中放入頂點座標資料
			mNormalBuffer.position(0);// 設定緩沖區起始位置

			// 建立紋理座標緩沖
			float[] textures = new float[vCount * 2];
			for (int i = 0; i < vCount * 2; i++) {
				textures[i] = tal.get(i);
			}
			ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			mTexCoorBuffer = tbb.asFloatBuffer();
			mTexCoorBuffer.put(textures);
			mTexCoorBuffer.position(0);

			// 起始化shader
			intShader(mv);
		}

		// 起始化shader
		public void intShader(MySurfaceView mv) {
			// 取得自訂著色管執行緒序id
			mProgram = ShaderManager.getTexLightShader();
			// 取得程式中頂點位置屬性參考id
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			// 取得程式中頂點經緯度屬性參考id
			maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
			// 取得程式中頂點法向量屬性參考id
			maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// 取得程式中總變換矩陣參考id
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			// 取得程式中攝影機位置參考id
			maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
			// 取得程式中光源位置參考id
			maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
					"uLightLocationSun");
			// 取得位置、旋轉變換矩陣參考id
			muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		}

		public void drawSelf(int texId) {
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
			GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mNormalBuffer);
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

	// 繪制側邊矩形
	private class TextureRect {
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
		private int vCount = 0;// 頂點數

		public TextureRect(MySurfaceView mv, float scale, float width,
				float height) {
			float[] vertices = new float[] {// 存放頂點座標的陣列
			-width / 2 * scale, height / 2 * scale, 0, -width / 2 * scale,
					-height / 2 * scale, 0, width / 2 * scale,
					height / 2 * scale, 0,

					width / 2 * scale, height / 2 * scale, 0,
					-width / 2 * scale, -height / 2 * scale, 0,
					width / 2 * scale, -height / 2 * scale, 0 };
			vCount = vertices.length / 3;// 頂點數量

			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer = vbb.asFloatBuffer();
			mVertexBuffer.put(vertices);
			mVertexBuffer.position(0);

			// 頂點法向量資料
			float normals[] = new float[vertices.length];
			for (int i = 0; i < vertices.length; i += 9) {
				// 根據每個三角形的頂點，球出該三角形的平均法向量
				float[] result = Vector3Util.calTriangleAverageNormal(
						vertices[i + 0], vertices[i + 1], vertices[i + 2],
						vertices[i + 3], vertices[i + 4], vertices[i + 5],
						vertices[i + 6], vertices[i + 7], vertices[i + 8]);
				for (int j = i; j < i + 9; j += 3) {
					normals[j] = result[0];
					normals[j + 1] = result[0];
					normals[j + 2] = result[0];
				}
			}
			// 建立繪制頂點法向量緩沖
			ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
			nbb.order(ByteOrder.nativeOrder());// 設定位元組順序
			mNormalBuffer = nbb.asFloatBuffer();// 轉為float型緩沖
			mNormalBuffer.put(normals);// 向緩沖區中放入頂點座標資料
			mNormalBuffer.position(0);// 設定緩沖區起始位置

			// 建立紋理座標緩沖
			float[] textures = new float[] { 0.242f, 0, 0.242f, 0.75f, 0.453f,
					0, 0.453f, 0, 0.242f, 0.75f, 0.453f, 0.75f };
			ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			mTexCoorBuffer = tbb.asFloatBuffer();
			mTexCoorBuffer.put(textures);
			mTexCoorBuffer.position(0);

			// 起始化shader
			intShader(mv);
		}

		// 起始化shader
		public void intShader(MySurfaceView mv) {
			// 取得自訂著色管執行緒序id
			mProgram = ShaderManager.getTexLightShader();
			// 取得程式中頂點位置屬性參考id
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			// 取得程式中頂點經緯度屬性參考id
			maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
			// 取得程式中頂點法向量屬性參考id
			maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// 取得程式中總變換矩陣參考id
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			// 取得程式中攝影機位置參考id
			maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
			// 取得程式中光源位置參考id
			maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
					"uLightLocationSun");
			// 取得位置、旋轉變換矩陣參考id
			muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		}

		public void drawSelf(int texId) {
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
			GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mNormalBuffer);
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

	// 繪制圓柱的內定類別
	private class Cylinder {
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

		int vCount;// 頂點數量

		public Cylinder(MySurfaceView mv, float scale, float length,
				float circle_radius, float degreespan, int col) {
			float collength = (float) length * scale / col;// 圓柱每塊所占的長度
			int spannum = (int) (180.0f / degreespan);

			ArrayList<Float> val = new ArrayList<Float>();// 頂點存放清單
			ArrayList<Float> alNomals = new ArrayList<Float>();// 法向量存放清單
			for (float circle_degree = 315.0f; circle_degree > 135.0f; circle_degree -= degreespan)// 循環行
			{
				for (int j = 0; j < col; j++)// 循環列
				{
					float x1 = (float) (j * collength - length / 2 * scale);
					float y1 = (float) (circle_radius * scale * Math.sin(Math
							.toRadians(circle_degree)));
					float z1 = (float) (circle_radius * scale * Math.cos(Math
							.toRadians(circle_degree)));

					float x2 = (float) (j * collength - length / 2 * scale);
					float y2 = (float) (circle_radius * scale * Math.sin(Math
							.toRadians(circle_degree - degreespan)));
					float z2 = (float) (circle_radius * scale * Math.cos(Math
							.toRadians(circle_degree - degreespan)));

					float x3 = (float) ((j + 1) * collength - length / 2
							* scale);
					float y3 = (float) (circle_radius * scale * Math.sin(Math
							.toRadians(circle_degree - degreespan)));
					float z3 = (float) (circle_radius * scale * Math.cos(Math
							.toRadians(circle_degree - degreespan)));

					float x4 = (float) ((j + 1) * collength - length / 2
							* scale);
					float y4 = (float) (circle_radius * scale * Math.sin(Math
							.toRadians(circle_degree)));
					float z4 = (float) (circle_radius * scale * Math.cos(Math
							.toRadians(circle_degree)));

					val.add(x1);
					val.add(y1);
					val.add(z1); // 兩個三角形，共6個頂點的座標
					val.add(x4);
					val.add(y4);
					val.add(z4);
					val.add(x2);
					val.add(y2);
					val.add(z2);

					val.add(x2);
					val.add(y2);
					val.add(z2);
					val.add(x4);
					val.add(y4);
					val.add(z4);
					val.add(x3);
					val.add(y3);
					val.add(z3);
					// 加入法向量資料
					alNomals.add(0f);
					alNomals.add(y1);
					alNomals.add(z1);
					alNomals.add(0f);
					alNomals.add(y4);
					alNomals.add(z4);
					alNomals.add(0f);
					alNomals.add(y2);
					alNomals.add(z2);

					alNomals.add(0f);
					alNomals.add(y2);
					alNomals.add(z2);
					alNomals.add(0f);
					alNomals.add(y4);
					alNomals.add(z4);
					alNomals.add(0f);
					alNomals.add(y3);
					alNomals.add(z3);
				}
			}

			vCount = val.size() / 3;// 確定頂點數量

			// 頂點
			float[] vertices = new float[vCount * 3];
			for (int i = 0; i < vCount * 3; i++) {
				vertices[i] = val.get(i);
			}
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer = vbb.asFloatBuffer();
			mVertexBuffer.put(vertices);
			mVertexBuffer.position(0);

			// 頂點法向量資料
			float normals[] = new float[alNomals.size()];
			for (int i = 0; i < alNomals.size(); i++) {
				normals[i] = alNomals.get(i);
			}
			// 將向量規格化
			Vector3Util.normalizeAllVectors(normals);
			// 建立繪制頂點法向量緩沖
			ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
			nbb.order(ByteOrder.nativeOrder());// 設定位元組順序
			mNormalBuffer = nbb.asFloatBuffer();// 轉為float型緩沖
			mNormalBuffer.put(normals);// 向緩沖區中放入頂點座標資料
			mNormalBuffer.position(0);// 設定緩沖區起始位置

			// 建立紋理座標緩沖
			float[] textures = generateTexCoor(col, spannum);
			ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			mTexCoorBuffer = tbb.asFloatBuffer();
			mTexCoorBuffer.put(textures);
			mTexCoorBuffer.position(0);

			// 起始化shader
			intShader(mv);
		}

		// 起始化shader
		public void intShader(MySurfaceView mv) {
			// 取得自訂著色管執行緒序id
			mProgram = ShaderManager.getTexLightShader();
			// 取得程式中頂點位置屬性參考id
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			// 取得程式中頂點經緯度屬性參考id
			maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
			// 取得程式中頂點法向量屬性參考id
			maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// 取得程式中總變換矩陣參考id
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			// 取得程式中攝影機位置參考id
			maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
			// 取得程式中光源位置參考id
			maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
					"uLightLocationSun");
			// 取得位置、旋轉變換矩陣參考id
			muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		}

		public void drawSelf(int texId) {
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
			GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mNormalBuffer);
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

		// 自動切分紋理產生紋理陣列的方法
		public float[] generateTexCoor(int bw, int bh) {
			float[] result = new float[bw * bh * 6 * 2];
			float sizew = 1f / bw;// 列數
			float sizeh = 1f / bh;// 行數
			int c = 0;
			for (int i = 0; i < bh; i++) {
				for (int j = 0; j < bw; j++) {
					// 每行列一個矩形，由兩個三角形構成，共六個點，12個紋理座標
					float s = j * sizew;
					float t = i * sizeh;

					result[c++] = s;
					result[c++] = t;

					result[c++] = s + sizew;
					result[c++] = t;

					result[c++] = s;
					result[c++] = t + sizeh;

					result[c++] = s;
					result[c++] = t + sizeh;

					result[c++] = s + sizew;
					result[c++] = t;

					result[c++] = s + sizew;
					result[c++] = t + sizeh;
				}
			}
			return result;
		}
	}
}
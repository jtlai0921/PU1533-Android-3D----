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

public class ZhuoBianOne {
	float length = EDGE_BIG;
	float width = MIDDLE * 0.8f;
	float height = UP_DOWN_HIGHT;
	float l = EDGE_BIG * 0.4f;
	float w = MIDDLE * 0.11f;

	float scale;

	UpRect one;
	TextureRect two;
	Cylinder three;
	TextureRect four;
	TextureRect five;

	float mAngleX;
	float mAngleY;
	float mAngleZ;

	public ZhuoBianOne(MySurfaceView mv, float scale) {
		this.scale = scale;

		one = new UpRect(mv, scale, length, width, w, 9);
		two = new TextureRect(mv, scale, width + w * 2, height, false);
		three = new Cylinder(mv, scale, height, width / 2, 20, 1);
		four = new TextureRect(mv, scale, w, height, true);
		five = new TextureRect(mv, scale, l, height, false);
	}

	public void drawSelf(int textureId1, int textureId2, int textureId3,
			int textureId4) {
		MatrixState.rotate(mAngleX, 1, 0, 0);
		MatrixState.rotate(mAngleY, 0, 1, 0);
		MatrixState.rotate(mAngleZ, 0, 0, 1);

		MatrixState.pushMatrix();
		MatrixState.translate(0, scale * height / 2, scale * length / 2);
		one.drawSelf(textureId3);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, -scale * length / 2);
		MatrixState.rotate(180, 0, 1, 0);
		two.drawSelf(textureId2);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, scale * (length / 2 - l));
		MatrixState.rotate(90, 0, 0, 1);
		three.drawSelf(textureId1);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(width / 2 + w / 2, 0, length / 2);
		four.drawSelf(textureId4);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(-width / 2 - w / 2, 0, length / 2);
		four.drawSelf(textureId4);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(width / 2, 0, -l / 2 + length / 2);
		MatrixState.rotate(270, 0, 1, 0);
		five.drawSelf(textureId1);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(-width / 2, 0, -l / 2 + length / 2);
		MatrixState.rotate(90, 0, 1, 0);
		five.drawSelf(textureId1);
		MatrixState.popMatrix();
	}

	// ø��W����ůx��
	private class UpRect {
		int mProgram;// �ۭq�ۦ�ް������id
		int muMVPMatrixHandle;// �`�ܴ��x�}�Ѧ�id
		int muMMatrixHandle;// ��m�B�����ܴ��x�}
		int maCameraHandle; // ��v����m�ݩʰѦ�id
		int maPositionHandle; // ���I��m�ݩʰѦ�id
		int maNormalHandle; // ���I�k�V�q�ݩʰѦ�id
		int maTexCoorHandle; // ���I���z�y���ݩʰѦ�id
		int maSunLightLocationHandle;// ������m�ݩʰѦ�id

		private FloatBuffer mVertexBuffer;// ���I�y�и�ƽw�R
		private FloatBuffer mNormalBuffer;// ���I�k�V�q��ƽw�R
		private FloatBuffer mTexCoorBuffer; // ���I���z��ƽw�R

		int vCount;// ���I��

		public UpRect(MySurfaceView mv, float scale, float length, float width,
				float w, int umber)// ���A�e�A���Υ���
		{
			float angleScale = 180.0f / umber;// �����׼��ܤƶq

			// ���I�y��
			ArrayList<Float> val = new ArrayList<Float>();// �Ω�s���I�y�Ъ��M��
			ArrayList<Float> tal = new ArrayList<Float>();// �Ω�s�񯾲z�y�Ъ��M��

			// �d�䳡�����I�A���z�y��
			float xa1 = -scale * (w + width / 2);
			float ya1 = 0;
			float za1 = scale * (-length);

			float sa1 = 0;
			float ta1 = 0;

			float xa2 = -scale * (width / 2);
			float ya2 = 0;
			float za2 = scale * (-length);

			float sa2 = w / (w * 2 + width);
			float ta2 = 0;

			float xa3 = -scale * (width / 2);
			float ya3 = 0;
			float za3 = 0;

			float sa3 = w / (w * 2 + width);
			float ta3 = 1;

			float xa4 = -scale * (w + width / 2);
			float ya4 = 0;
			float za4 = 0;

			float sa4 = 0;
			float ta4 = 1;

			val.add(xa1);
			val.add(ya1);
			val.add(za1);
			val.add(xa4);
			val.add(ya4);
			val.add(za4);
			val.add(xa2);
			val.add(ya2);
			val.add(za2);

			val.add(xa2);
			val.add(ya2);
			val.add(za2);
			val.add(xa4);
			val.add(ya4);
			val.add(za4);
			val.add(xa3);
			val.add(ya3);
			val.add(za3);

			tal.add(sa1);
			tal.add(ta1);
			tal.add(sa4);
			tal.add(ta4);
			tal.add(sa2);
			tal.add(ta2);

			tal.add(sa2);
			tal.add(ta2);
			tal.add(sa4);
			tal.add(ta4);
			tal.add(sa3);
			tal.add(ta3);

			// �����������I�A���z�y��
			for (int i = 0; i < umber; i++) {
				float x1 = (float) (scale * (-width / 2 * Math.cos(Math
						.toRadians(i * angleScale))));
				float y1 = 0;
				float z1 = scale * (-length);

				float s1 = (w + width / 2 + x1) / (w * 2 + width);
				float t1 = 0;

				float x2 = (float) (scale * (-width / 2 * Math.cos(Math
						.toRadians((i + 1) * angleScale))));
				float y2 = 0;
				float z2 = scale * (-length);

				float s2 = (w + width / 2 + x2) / (w * 2 + width);
				float t2 = 0;

				float x3 = (float) (scale * (-width / 2 * Math.cos(Math
						.toRadians((i + 1) * angleScale))));
				float y3 = 0;
				float z3 = (float) (scale * (-l - (width / 2 * Math.sin(Math
						.toRadians((i + 1) * angleScale)))));

				float s3 = (w + width / 2 + x3) / (w * 2 + width);
				float t3 = (length + z3) / length;

				float x4 = (float) (scale * (-width / 2 * Math.cos(Math
						.toRadians(i * angleScale))));
				float y4 = 0;
				float z4 = (float) (scale * (-l - (width / 2 * Math.sin(Math
						.toRadians(i * angleScale)))));

				float s4 = (w + width / 2 + x4) / (w * 2 + width);
				float t4 = (length + z4) / length;

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

			// �d�䳡�����I�A���z�y��
			float xb1 = scale * (width / 2);
			float yb1 = 0;
			float zb1 = scale * (-length);

			float sb1 = (w + width) / (w * 2 + width);
			float tb1 = 0;

			float xb2 = scale * (width / 2 + w);
			float yb2 = 0;
			float zb2 = scale * (-length);

			float sb2 = 1;
			float tb2 = 0;

			float xb3 = scale * (width / 2 + w);
			float yb3 = 0;
			float zb3 = 0;

			float sb3 = 1;
			float tb3 = 1;

			float xb4 = scale * (width / 2);
			float yb4 = 0;
			float zb4 = 0;

			float sb4 = (w + width) / (w * 2 + width);
			float tb4 = 1;

			val.add(xb1);
			val.add(yb1);
			val.add(zb1);
			val.add(xb4);
			val.add(yb4);
			val.add(zb4);
			val.add(xb2);
			val.add(yb2);
			val.add(zb2);

			val.add(xb2);
			val.add(yb2);
			val.add(zb2);
			val.add(xb4);
			val.add(yb4);
			val.add(zb4);
			val.add(xb3);
			val.add(yb3);
			val.add(zb3);

			tal.add(sb1);
			tal.add(tb1);
			tal.add(sb4);
			tal.add(tb4);
			tal.add(sb2);
			tal.add(tb2);

			tal.add(sb2);
			tal.add(tb2);
			tal.add(sb4);
			tal.add(tb4);
			tal.add(sb3);
			tal.add(tb3);

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

			// ���I�k�V�q���
			float normals[] = new float[vertices.length];
			for (int i = 0; i < vertices.length; i += 9) {
				// �ھڨC�ӤT���Ϊ����I�A�y�X�ӤT���Ϊ������k�V�q
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
			// �إ�ø��I�k�V�q�w�R
			ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
			nbb.order(ByteOrder.nativeOrder());// �]�w�줸�ն���
			mNormalBuffer = nbb.asFloatBuffer();// �ରfloat���w�R
			mNormalBuffer.put(normals);// �V�w�R�Ϥ���J���I�y�и��
			mNormalBuffer.position(0);// �]�w�w�R�ϰ_�l��m

			// ���z�y�и��
			float[] textures = new float[vCount * 2];
			for (int i = 0; i < vCount * 2; i++) {
				textures[i] = tal.get(i);
			}
			ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			mTexCoorBuffer = tbb.asFloatBuffer();
			mTexCoorBuffer.put(textures);
			mTexCoorBuffer.position(0);

			// �_�l��shader
			intShader(mv);
		}

		// �_�l��shader
		public void intShader(MySurfaceView mv) {
			// ���o�ۭq�ۦ�ް������id
			mProgram = ShaderManager.getTexLightShader();
			// ���o�{�������I��m�ݩʰѦ�id
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			// ���o�{�������I�g�n���ݩʰѦ�id
			maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
			// ���o�{�������I�k�V�q�ݩʰѦ�id
			maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// ���o�{�����`�ܴ��x�}�Ѧ�id
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			// ���o�{������v����m�Ѧ�id
			maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
			// ���o�{����������m�Ѧ�id
			maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
					"uLightLocationSun");
			// ���o��m�B�����ܴ��x�}�Ѧ�id
			muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		}

		public void drawSelf(int texId) {
			// ��w�ϥάY�Mshader�{��
			GLES20.glUseProgram(mProgram);
			// �N�̲��ܴ��x�}�ǤJshader�{��
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
					MatrixState.getFinalMatrix(), 0);
			// �N��m�B�����ܴ��x�}�ǤJshader�{��
			GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
					MatrixState.getMMatrix(), 0);
			// �N��v����m�ǤJshader�{��
			GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
			// �N������m�ǤJshader�{��
			GLES20.glUniform3fv(maSunLightLocationHandle, 1,
					MatrixState.lightPositionFB);

			// ���e�����w���I��m���
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mVertexBuffer);
			// ���e�����w���I�g�n�׸��
			GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,
					false, 2 * 4, mTexCoorBuffer);
			// ���e�����w���I�k�V�q���
			GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mNormalBuffer);
			// �e�\���I��m��ư}�C
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
			GLES20.glEnableVertexAttribArray(maNormalHandle);
			// �j�w���z
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
			// ø��T����
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		}
	}

	// ø���x��
	private class TextureRect {
		int mProgram;// �ۭq�ۦ�ް������id
		int muMVPMatrixHandle;// �`�ܴ��x�}�Ѧ�id
		int muMMatrixHandle;// ��m�B�����ܴ��x�}
		int maCameraHandle; // ��v����m�ݩʰѦ�id
		int maPositionHandle; // ���I��m�ݩʰѦ�id
		int maNormalHandle; // ���I�k�V�q�ݩʰѦ�id
		int maTexCoorHandle; // ���I���z�y���ݩʰѦ�id
		int maSunLightLocationHandle;// ������m�ݩʰѦ�id

		private FloatBuffer mVertexBuffer;// ���I�y�и�ƽw�R
		private FloatBuffer mNormalBuffer;// ���I�k�V�q��ƽw�R
		private FloatBuffer mTexCoorBuffer; // ���I���z��ƽw�R
		private int vCount = 0;// ���I��

		public TextureRect(MySurfaceView mv, float scale, float width,
				float height, boolean isFullST) {
			float[] vertices = new float[] {// �s���I�y�Ъ��}�C
			-width / 2 * scale, height / 2 * scale, 0, -width / 2 * scale,
					-height / 2 * scale, 0, width / 2 * scale,
					height / 2 * scale, 0,

					width / 2 * scale, height / 2 * scale, 0,
					-width / 2 * scale, -height / 2 * scale, 0,
					width / 2 * scale, -height / 2 * scale, 0 };
			vCount = vertices.length / 3;// ���I�ƶq

			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer = vbb.asFloatBuffer();
			mVertexBuffer.put(vertices);
			mVertexBuffer.position(0);

			// ���I�k�V�q���
			float normals[] = new float[vertices.length];
			for (int i = 0; i < vertices.length; i += 9) {
				// �ھڨC�ӤT���Ϊ����I�A�y�X�ӤT���Ϊ������k�V�q
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
			// �إ�ø��I�k�V�q�w�R
			ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
			nbb.order(ByteOrder.nativeOrder());// �]�w�줸�ն���
			mNormalBuffer = nbb.asFloatBuffer();// �ରfloat���w�R
			mNormalBuffer.put(normals);// �V�w�R�Ϥ���J���I�y�и��
			mNormalBuffer.position(0);// �]�w�w�R�ϰ_�l��m

			// �إ߯��z�y�нw�R
			float[] textures;
			if (isFullST) {
				textures = new float[] { 0f, 0, 0f, 1f, 1f, 0, 1f, 0, 0f, 1f,
						1f, 1f };
			} else {
				textures = new float[] { 0.242f, 0, 0.242f, 0.75f, 0.453f, 0,
						0.453f, 0, 0.242f, 0.75f, 0.453f, 0.75f };
			}
			ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			mTexCoorBuffer = tbb.asFloatBuffer();
			mTexCoorBuffer.put(textures);
			mTexCoorBuffer.position(0);

			// �_�l��shader
			intShader(mv);
		}

		// �_�l��shader
		public void intShader(MySurfaceView mv) {
			// ���o�ۭq�ۦ�ް������id
			mProgram = ShaderManager.getTexLightShader();
			// ���o�{�������I��m�ݩʰѦ�id
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			// ���o�{�������I�g�n���ݩʰѦ�id
			maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
			// ���o�{�������I�k�V�q�ݩʰѦ�id
			maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// ���o�{�����`�ܴ��x�}�Ѧ�id
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			// ���o�{������v����m�Ѧ�id
			maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
			// ���o�{����������m�Ѧ�id
			maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
					"uLightLocationSun");
			// ���o��m�B�����ܴ��x�}�Ѧ�id
			muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		}

		public void drawSelf(int texId) {
			// ��w�ϥάY�Mshader�{��
			GLES20.glUseProgram(mProgram);
			// �N�̲��ܴ��x�}�ǤJshader�{��
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
					MatrixState.getFinalMatrix(), 0);
			// �N��m�B�����ܴ��x�}�ǤJshader�{��
			GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
					MatrixState.getMMatrix(), 0);
			// �N��v����m�ǤJshader�{��
			GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
			// �N������m�ǤJshader�{��
			GLES20.glUniform3fv(maSunLightLocationHandle, 1,
					MatrixState.lightPositionFB);

			// ���e�����w���I��m���
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mVertexBuffer);
			// ���e�����w���I�g�n�׸��
			GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,
					false, 2 * 4, mTexCoorBuffer);
			// ���e�����w���I�k�V�q���
			GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mNormalBuffer);
			// �e�\���I��m��ư}�C
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
			GLES20.glEnableVertexAttribArray(maNormalHandle);
			// �j�w���z
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
			// ø��T����
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		}
	}

	// ø���W�����w���O
	private class Cylinder {
		int mProgram;// �ۭq�ۦ�ް������id
		int muMVPMatrixHandle;// �`�ܴ��x�}�Ѧ�id
		int muMMatrixHandle;// ��m�B�����ܴ��x�}
		int maCameraHandle; // ��v����m�ݩʰѦ�id
		int maPositionHandle; // ���I��m�ݩʰѦ�id
		int maNormalHandle; // ���I�k�V�q�ݩʰѦ�id
		int maTexCoorHandle; // ���I���z�y���ݩʰѦ�id
		int maSunLightLocationHandle;// ������m�ݩʰѦ�id

		private FloatBuffer mVertexBuffer;// ���I�y�и�ƽw�R
		private FloatBuffer mNormalBuffer;// ���I�k�V�q��ƽw�R
		private FloatBuffer mTexCoorBuffer; // ���I���z��ƽw�R

		int vCount;// ���I�ƶq

		public Cylinder(MySurfaceView mv, float scale, float length,
				float circle_radius, float degreespan, int col) {
			float collength = (float) length * scale / col;// ��W�C���ҥe������
			int spannum = (int) (180.0f / degreespan);

			ArrayList<Float> val = new ArrayList<Float>();// ���I�s��M��
			ArrayList<Float> alNomals = new ArrayList<Float>();// �k�V�q�s��M��

			for (float circle_degree = 270.0f; circle_degree > 90.0f; circle_degree -= degreespan)// �`����
			{
				for (int j = 0; j < col; j++)// �`���C
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
					val.add(z1);// ��ӤT���ΡA�@6�ӳ��I���y��
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

					// �[�J�k�V�q���
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

			vCount = val.size() / 3;// �T�w���I�ƶq

			// ���I
			float[] vertices = new float[vCount * 3];
			for (int i = 0; i < vCount * 3; i++) {
				vertices[i] = val.get(i);
			}
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			mVertexBuffer = vbb.asFloatBuffer();
			mVertexBuffer.put(vertices);
			mVertexBuffer.position(0);

			// ���I�k�V�q���
			float normals[] = new float[alNomals.size()];
			for (int i = 0; i < alNomals.size(); i++) {
				normals[i] = alNomals.get(i);
			}
			// �N�V�q�W���
			Vector3Util.normalizeAllVectors(normals);
			// �إ�ø��I�k�V�q�w�R
			ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
			nbb.order(ByteOrder.nativeOrder());// �]�w�줸�ն���
			mNormalBuffer = nbb.asFloatBuffer();// �ରfloat���w�R
			mNormalBuffer.put(normals);// �V�w�R�Ϥ���J���I�y�и��
			mNormalBuffer.position(0);// �]�w�w�R�ϰ_�l��m

			// ���z
			float[] textures = generateTexCoor(col, spannum);
			// �إ߯��z�y�нw�R
			ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			mTexCoorBuffer = tbb.asFloatBuffer();
			mTexCoorBuffer.put(textures);
			mTexCoorBuffer.position(0);

			// �_�l��shader
			intShader(mv);
		}

		// �_�l��shader
		public void intShader(MySurfaceView mv) {
			// ���o�ۭq�ۦ�ް������id
			mProgram = ShaderManager.getTexLightShader();
			// ���o�{�������I��m�ݩʰѦ�id
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			// ���o�{�������I�g�n���ݩʰѦ�id
			maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
			// ���o�{�������I�k�V�q�ݩʰѦ�id
			maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// ���o�{�����`�ܴ��x�}�Ѧ�id
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			// ���o�{������v����m�Ѧ�id
			maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
			// ���o�{����������m�Ѧ�id
			maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
					"uLightLocationSun");
			// ���o��m�B�����ܴ��x�}�Ѧ�id
			muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		}

		public void drawSelf(int texId) {
			// ��w�ϥάY�Mshader�{��
			GLES20.glUseProgram(mProgram);
			// �N�̲��ܴ��x�}�ǤJshader�{��
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
					MatrixState.getFinalMatrix(), 0);
			// �N��m�B�����ܴ��x�}�ǤJshader�{��
			GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
					MatrixState.getMMatrix(), 0);
			// �N��v����m�ǤJshader�{��
			GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
			// �N������m�ǤJshader�{��
			GLES20.glUniform3fv(maSunLightLocationHandle, 1,
					MatrixState.lightPositionFB);

			// ���e�����w���I��m���
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mVertexBuffer);
			// ���e�����w���I�g�n�׸��
			GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,
					false, 2 * 4, mTexCoorBuffer);
			// ���e�����w���I�k�V�q���
			GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mNormalBuffer);
			// �e�\���I��m��ư}�C
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
			GLES20.glEnableVertexAttribArray(maNormalHandle);
			// �j�w���z
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
			// ø��T����
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		}

		// �۰ʤ������z���ͯ��z�}�C����k
		public float[] generateTexCoor(int bw, int bh) {
			float[] result = new float[bw * bh * 6 * 2];
			float sizew = 1f / bw;// �C��
			float sizeh = 1f / bh;// ���
			int c = 0;
			for (int i = 0; i < bh; i++) {
				for (int j = 0; j < bw; j++) {
					// �C��C�@�ӯx�ΡA�Ѩ�ӤT���κc���A�@�����I�A12�ӯ��z�y��
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
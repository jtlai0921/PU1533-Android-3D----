package tstc.fxq.utils;

import static tstc.fxq.constants.Constant.BALL_R;
import static tstc.fxq.constants.Constant.V_THRESHOLD;
import tstc.fxq.constants.Constant;
import tstc.fxq.parts.BallKongZhi;

//進行碰撞檢驗與實體計算的工具類別
public class PZJCUtil {
	// 求兩個向量的點積
	public static float dotProduct(float[] vec1, float[] vec2) {
		return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];

	}

	// 求向量的模
	public static float mould(float[] vec) {
		return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2]
				* vec[2]);
	}

	// 求兩個向量的夾角
	public static float angle(float[] vec1, float[] vec2) {
		// 先求點積
		float dp = dotProduct(vec1, vec2);
		// 再求兩個向量的模
		float m1 = mould(vec1);
		float m2 = mould(vec2);

		float acos = dp / (m1 * m2);

		// 為了避免計算誤差帶來的問題
		if (acos > 1) {
			acos = 1;
		} else if (acos < -1) {
			acos = -1;
		}

		return (float) Math.acos(acos);
	}

	// 兩個球進行碰撞實體計算的方法
	public static boolean jiSuanPengZhuangSimple(BallKongZhi balla,
			BallKongZhi ballb) {
		boolean result = false;

		// 求碰撞直線向量B->A （也就是兩個參與碰撞的桌球球心連線的向量）
		float BAx = balla.xOffset - ballb.xOffset;
		float BAz = balla.zOffset - ballb.zOffset;

		// 求上述向量的模
		float mvBA = (float) Math.sqrt(BAx * BAx + BAz * BAz);

		// 若兩球距離大於球半徑的兩倍則沒有碰撞
		// 若小於半徑的兩倍則碰撞了
		if (mvBA < BALL_R * 2) {
			result = true;
		}

		return result;
	}

	// 兩個球進行碰撞實體計算的方法
	public static boolean jiSuanPengZhuang(BallKongZhi balla,
			BallKongZhi ballb) {
		// 求碰撞直線向量B->A （也就是兩個參與碰撞的桌球球心連線的向量）
		float BAx = balla.xOffset - ballb.xOffset;
		float BAz = balla.zOffset - ballb.zOffset;

		// 求上述向量的模
		float mvBA = mould(new float[] { BAx, 0, BAz });

		// 若兩球距離大於等於球半徑的兩倍則沒有碰撞
		if (mvBA >= BALL_R * 2) {
			return false;
		}

		// 解除粘滯問題的程式碼======= begin ========

		// 若果上次碰撞和本次碰撞的兩球一樣，連續碰撞次數加一，否則連續碰撞次數歸零
		if (Constant.preCollisionIdA == balla.id
				&& Constant.preCollisionIdB == ballb.id
				|| Constant.preCollisionIdB == balla.id
				&& Constant.preCollisionIdA == ballb.id) {
			Constant.collisionCount++;
		} else {
			Constant.collisionCount = 0;
		}
		// 若果兩球的連續碰撞次數過多，認為沒有碰撞，便可以解除粘滯
		if (Constant.collisionCount >= Constant.MAX_COLLISION_NUM) {
			return false;
		}

		// 記錄碰撞兩球的id
		Constant.preCollisionIdA = balla.id;
		Constant.preCollisionIdB = ballb.id;

		// 解除粘滯問題的程式碼======= end ========

		// 分解B球速度

		// 求b球的速度大小
		float vB = (float) Math.sqrt(ballb.vx * ballb.vx + ballb.vz * ballb.vz);
		// 平行方向的XZ分速度
		float vbCollx = 0;
		float vbCollz = 0;
		// 垂直方向的XZ分速度
		float vbVerticalX = 0;
		float vbVerticalZ = 0;

		// 若球速度小於設定值則認為速度為零，不用進行分解計算了
		if (V_THRESHOLD < vB) {
			// 求B球的速度方向向量與碰撞直線向量B->A的夾角(弧度)
			float bAngle = angle(new float[] { ballb.vx, 0, ballb.vz },
					new float[] { BAx, 0, BAz });

			// 求B球在碰撞方向的速度大小
			float vbColl = vB * (float) Math.cos(bAngle);

			// 求B球在碰撞方向的速度向量
			vbCollx = (vbColl / mvBA) * BAx;
			vbCollz = (vbColl / mvBA) * BAz;

			// 求B球在碰撞垂直方向的速度向量
			vbVerticalX = ballb.vx - vbCollx;
			vbVerticalZ = ballb.vz - vbCollz;
		}

		// 分解A球速度

		// 求a球的速度大小
		float vA = (float) Math.sqrt(balla.vx * balla.vx + balla.vz * balla.vz);
		// 平行方向的XZ分速度
		float vaCollx = 0;
		float vaCollz = 0;
		// 垂直方向的XZ分速度
		float vaVerticalX = 0;
		float vaVerticalZ = 0;

		// 若球速度小於設定值則認為速度為零，不用進行分解計算了
		if (V_THRESHOLD < vA) {
			// 求A球的速度方向向量與碰撞直線向量B->A的夾角(弧度)
			float aAngle = angle(new float[] { balla.vx, 0, balla.vz },
					new float[] { BAx, 0, BAz });

			// 求A球在碰撞方向的速度大小
			float vaColl = vA * (float) Math.cos(aAngle);

			// 求A球在碰撞方向的速度向量
			vaCollx = (vaColl / mvBA) * BAx;
			vaCollz = (vaColl / mvBA) * BAz;

			// 求A球在碰撞垂直方向的速度向量
			vaVerticalX = balla.vx - vaCollx;
			vaVerticalZ = balla.vz - vaCollz;
		}

		// 求碰撞後AB球的速度
		balla.vx = vaVerticalX + vbCollx;
		balla.vz = vaVerticalZ + vbCollz;

		ballb.vx = vbVerticalX + vaCollx;
		ballb.vz = vbVerticalZ + vaCollz;

		return true;
	}
}

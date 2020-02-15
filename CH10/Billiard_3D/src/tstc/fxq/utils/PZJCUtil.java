package tstc.fxq.utils;

import static tstc.fxq.constants.Constant.BALL_R;
import static tstc.fxq.constants.Constant.V_THRESHOLD;
import tstc.fxq.constants.Constant;
import tstc.fxq.parts.BallKongZhi;

//�i��I������P����p�⪺�u�����O
public class PZJCUtil {
	// �D��ӦV�q���I�n
	public static float dotProduct(float[] vec1, float[] vec2) {
		return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];

	}

	// �D�V�q����
	public static float mould(float[] vec) {
		return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2]
				* vec[2]);
	}

	// �D��ӦV�q������
	public static float angle(float[] vec1, float[] vec2) {
		// ���D�I�n
		float dp = dotProduct(vec1, vec2);
		// �A�D��ӦV�q����
		float m1 = mould(vec1);
		float m2 = mould(vec2);

		float acos = dp / (m1 * m2);

		// ���F�קK�p��~�t�a�Ӫ����D
		if (acos > 1) {
			acos = 1;
		} else if (acos < -1) {
			acos = -1;
		}

		return (float) Math.acos(acos);
	}

	// ��Ӳy�i��I������p�⪺��k
	public static boolean jiSuanPengZhuangSimple(BallKongZhi balla,
			BallKongZhi ballb) {
		boolean result = false;

		// �D�I�����u�V�qB->A �]�]�N�O��ӰѻP�I������y�y�߳s�u���V�q�^
		float BAx = balla.xOffset - ballb.xOffset;
		float BAz = balla.zOffset - ballb.zOffset;

		// �D�W�z�V�q����
		float mvBA = (float) Math.sqrt(BAx * BAx + BAz * BAz);

		// �Y��y�Z���j��y�b�|���⭿�h�S���I��
		// �Y�p��b�|���⭿�h�I���F
		if (mvBA < BALL_R * 2) {
			result = true;
		}

		return result;
	}

	// ��Ӳy�i��I������p�⪺��k
	public static boolean jiSuanPengZhuang(BallKongZhi balla,
			BallKongZhi ballb) {
		// �D�I�����u�V�qB->A �]�]�N�O��ӰѻP�I������y�y�߳s�u���V�q�^
		float BAx = balla.xOffset - ballb.xOffset;
		float BAz = balla.zOffset - ballb.zOffset;

		// �D�W�z�V�q����
		float mvBA = mould(new float[] { BAx, 0, BAz });

		// �Y��y�Z���j�󵥩�y�b�|���⭿�h�S���I��
		if (mvBA >= BALL_R * 2) {
			return false;
		}

		// �Ѱ��ߺ����D���{���X======= begin ========

		// �Y�G�W���I���M�����I������y�@�ˡA�s��I�����ƥ[�@�A�_�h�s��I�������k�s
		if (Constant.preCollisionIdA == balla.id
				&& Constant.preCollisionIdB == ballb.id
				|| Constant.preCollisionIdB == balla.id
				&& Constant.preCollisionIdA == ballb.id) {
			Constant.collisionCount++;
		} else {
			Constant.collisionCount = 0;
		}
		// �Y�G��y���s��I�����ƹL�h�A�{���S���I���A�K�i�H�Ѱ��ߺ�
		if (Constant.collisionCount >= Constant.MAX_COLLISION_NUM) {
			return false;
		}

		// �O���I����y��id
		Constant.preCollisionIdA = balla.id;
		Constant.preCollisionIdB = ballb.id;

		// �Ѱ��ߺ����D���{���X======= end ========

		// ����B�y�t��

		// �Db�y���t�פj�p
		float vB = (float) Math.sqrt(ballb.vx * ballb.vx + ballb.vz * ballb.vz);
		// �����V��XZ���t��
		float vbCollx = 0;
		float vbCollz = 0;
		// ������V��XZ���t��
		float vbVerticalX = 0;
		float vbVerticalZ = 0;

		// �Y�y�t�פp��]�w�ȫh�{���t�׬��s�A���ζi����ѭp��F
		if (V_THRESHOLD < vB) {
			// �DB�y���t�פ�V�V�q�P�I�����u�V�qB->A������(����)
			float bAngle = angle(new float[] { ballb.vx, 0, ballb.vz },
					new float[] { BAx, 0, BAz });

			// �DB�y�b�I����V���t�פj�p
			float vbColl = vB * (float) Math.cos(bAngle);

			// �DB�y�b�I����V���t�צV�q
			vbCollx = (vbColl / mvBA) * BAx;
			vbCollz = (vbColl / mvBA) * BAz;

			// �DB�y�b�I��������V���t�צV�q
			vbVerticalX = ballb.vx - vbCollx;
			vbVerticalZ = ballb.vz - vbCollz;
		}

		// ����A�y�t��

		// �Da�y���t�פj�p
		float vA = (float) Math.sqrt(balla.vx * balla.vx + balla.vz * balla.vz);
		// �����V��XZ���t��
		float vaCollx = 0;
		float vaCollz = 0;
		// ������V��XZ���t��
		float vaVerticalX = 0;
		float vaVerticalZ = 0;

		// �Y�y�t�פp��]�w�ȫh�{���t�׬��s�A���ζi����ѭp��F
		if (V_THRESHOLD < vA) {
			// �DA�y���t�פ�V�V�q�P�I�����u�V�qB->A������(����)
			float aAngle = angle(new float[] { balla.vx, 0, balla.vz },
					new float[] { BAx, 0, BAz });

			// �DA�y�b�I����V���t�פj�p
			float vaColl = vA * (float) Math.cos(aAngle);

			// �DA�y�b�I����V���t�צV�q
			vaCollx = (vaColl / mvBA) * BAx;
			vaCollz = (vaColl / mvBA) * BAz;

			// �DA�y�b�I��������V���t�צV�q
			vaVerticalX = balla.vx - vaCollx;
			vaVerticalZ = balla.vz - vaCollz;
		}

		// �D�I����AB�y���t��
		balla.vx = vaVerticalX + vbCollx;
		balla.vz = vaVerticalZ + vbCollz;

		ballb.vx = vbVerticalX + vaCollx;
		ballb.vz = vbVerticalZ + vaCollz;

		return true;
	}
}

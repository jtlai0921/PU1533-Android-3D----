package lwz.com.tank.game;

//�ޱ��ʧ@
public class Action {
	ActionType at;				//�ޱ����A
	float[] data;				//�ޱ����
	
	public Action(ActionType at,float[] data)
	{
		this.at=at;
		this.data=data;
	}
}

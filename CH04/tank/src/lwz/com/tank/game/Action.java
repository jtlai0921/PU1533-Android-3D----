package lwz.com.tank.game;

//操控動作
public class Action {
	ActionType at;				//操控型態
	float[] data;				//操控資料
	
	public Action(ActionType at,float[] data)
	{
		this.at=at;
		this.data=data;
	}
}

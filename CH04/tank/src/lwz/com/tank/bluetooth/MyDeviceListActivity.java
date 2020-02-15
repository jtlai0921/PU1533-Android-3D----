package lwz.com.tank.bluetooth;

import java.util.Set;

import lwz.com.tank.activity.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 裝置清單的Activity
 */
public class MyDeviceListActivity extends Activity {
	// extra訊息名稱
	public static String EXTRA_DEVICE_ADDR = "device_address";
	// 成員變數
	private BluetoothAdapter myBtAdapter;
	private ArrayAdapter<String> myAdapterPaired;
	private ArrayAdapter<String> myAdapterNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 設定視窗
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		// 設定為當結果是Activity.RESULT_CANCELED時，傳回到該Activity的呼叫者
		setResult(Activity.RESULT_CANCELED);
		// 起始化搜尋按鈕
		Button scanBtn = (Button) findViewById(R.id.button_scan);
		scanBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);// 使按鈕不可見
			}
		});
		// 起始化介面卡
		myAdapterPaired = new ArrayAdapter<String>(this,
				R.layout.device_name);// 已配對的
		myAdapterNew = new ArrayAdapter<String>(this,
				R.layout.device_name);// 新發現的
		// 將已配對的裝置放入清單中
		ListView lvPaired = (ListView) findViewById(R.id.paired_devices);
		lvPaired.setAdapter(myAdapterPaired);
		lvPaired.setOnItemClickListener(mDeviceClickListener);
		// 將新發現的裝置放入清單中
		ListView lvNewDevices = (ListView) findViewById(R.id.new_devices);
		lvNewDevices.setAdapter(myAdapterNew);
		lvNewDevices.setOnItemClickListener(mDeviceClickListener);
		// 登錄發現裝置時的廣播
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);
		// 登錄搜尋完成時的廣播
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);
		// 取得本機藍芽介面卡
		myBtAdapter = BluetoothAdapter.getDefaultAdapter();
		// 取得已配對的裝置
		Set<BluetoothDevice> pairedDevices = myBtAdapter.getBondedDevices();
		// 將所有已配對裝置訊息放入清單中
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				myAdapterPaired.add(device.getName() + "\n"
						+ device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired)
					.toString();
			myAdapterPaired.add(noDevices);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myBtAdapter != null) {// 確保不再搜尋裝置
			myBtAdapter.cancelDiscovery();
		}
		// 取消廣播監聽器
		this.unregisterReceiver(mReceiver);
	}
	// 使用藍芽介面卡搜尋裝置的方法
	private void doDiscovery() {
		// 在標題上顯示正在搜尋的標志
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);
		// 顯示搜尋到的新裝置的副標題
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
		if (myBtAdapter.isDiscovering()) {// 若果正在搜尋，取消本次搜尋
			myBtAdapter.cancelDiscovery();
		}
		myBtAdapter.startDiscovery();// 開始搜尋
	}

	// 清單中裝置按下時的監聽器
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			myBtAdapter.cancelDiscovery();// 取消搜尋
			// 取得裝置的MAC位址
			String msg = ((TextView) v).getText().toString();
			String address = msg.substring(msg.length() - 17);
			// 建立帶有MAC位址的Intent
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDR, address);
			// 裝置結果並離開Activity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};
	// 監聽搜尋到的裝置的BroadcastReceiver
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 若果找到裝置
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 從Intent中取得BluetoothDevice物件
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 若果沒有配對，將裝置加入新裝置清單
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					myAdapterNew.add(device.getName() + "\n"
							+ device.getAddress());
				}
				// 當搜尋完成後，改變Activity的標題
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (myAdapterNew.getCount() == 0) {
					String noDevices = getResources().getText(
							R.string.none_found).toString();
					myAdapterNew.add(noDevices);
				}
			}
		}
	};
}

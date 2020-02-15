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
 * �˸m�M�檺Activity
 */
public class MyDeviceListActivity extends Activity {
	// extra�T���W��
	public static String EXTRA_DEVICE_ADDR = "device_address";
	// �����ܼ�
	private BluetoothAdapter myBtAdapter;
	private ArrayAdapter<String> myAdapterPaired;
	private ArrayAdapter<String> myAdapterNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �]�w����
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		// �]�w�����G�OActivity.RESULT_CANCELED�ɡA�Ǧ^���Activity���I�s��
		setResult(Activity.RESULT_CANCELED);
		// �_�l�Ʒj�M���s
		Button scanBtn = (Button) findViewById(R.id.button_scan);
		scanBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);// �ϫ��s���i��
			}
		});
		// �_�l�Ƥ����d
		myAdapterPaired = new ArrayAdapter<String>(this,
				R.layout.device_name);// �w�t�諸
		myAdapterNew = new ArrayAdapter<String>(this,
				R.layout.device_name);// �s�o�{��
		// �N�w�t�諸�˸m��J�M�椤
		ListView lvPaired = (ListView) findViewById(R.id.paired_devices);
		lvPaired.setAdapter(myAdapterPaired);
		lvPaired.setOnItemClickListener(mDeviceClickListener);
		// �N�s�o�{���˸m��J�M�椤
		ListView lvNewDevices = (ListView) findViewById(R.id.new_devices);
		lvNewDevices.setAdapter(myAdapterNew);
		lvNewDevices.setOnItemClickListener(mDeviceClickListener);
		// �n���o�{�˸m�ɪ��s��
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);
		// �n���j�M�����ɪ��s��
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);
		// ���o�����Ūޤ����d
		myBtAdapter = BluetoothAdapter.getDefaultAdapter();
		// ���o�w�t�諸�˸m
		Set<BluetoothDevice> pairedDevices = myBtAdapter.getBondedDevices();
		// �N�Ҧ��w�t��˸m�T����J�M�椤
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
		if (myBtAdapter != null) {// �T�O���A�j�M�˸m
			myBtAdapter.cancelDiscovery();
		}
		// �����s����ť��
		this.unregisterReceiver(mReceiver);
	}
	// �ϥ��Ūޤ����d�j�M�˸m����k
	private void doDiscovery() {
		// �b���D�W��ܥ��b�j�M���Ч�
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);
		// ��ܷj�M�쪺�s�˸m���Ƽ��D
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
		if (myBtAdapter.isDiscovering()) {// �Y�G���b�j�M�A���������j�M
			myBtAdapter.cancelDiscovery();
		}
		myBtAdapter.startDiscovery();// �}�l�j�M
	}

	// �M�椤�˸m���U�ɪ���ť��
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			myBtAdapter.cancelDiscovery();// �����j�M
			// ���o�˸m��MAC��}
			String msg = ((TextView) v).getText().toString();
			String address = msg.substring(msg.length() - 17);
			// �إ߱a��MAC��}��Intent
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDR, address);
			// �˸m���G�����}Activity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};
	// ��ť�j�M�쪺�˸m��BroadcastReceiver
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// �Y�G���˸m
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// �qIntent�����oBluetoothDevice����
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// �Y�G�S���t��A�N�˸m�[�J�s�˸m�M��
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					myAdapterNew.add(device.getName() + "\n"
							+ device.getAddress());
				}
				// ��j�M������A����Activity�����D
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

package lwz.com.tank.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import lwz.com.tank.activity.Constant;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//用於管理連線的Service
public class MyService {
    // 本套用的唯一 UUID
	private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    // 成員變數
    private final BluetoothAdapter btAdapter;
    private final Handler myHandler;
    private AcceptThread myAcceptThread;
    private ConnectThread myConnectThread;
    private ConnectedThread myConnectedThread;
    private int myState;
    // 表示目前連線狀態的常數
    public static final int STATE_NONE = 0;       // 什麼也沒做
    public static final int STATE_LISTEN = 1;     // 正在監聽連線
    public static final int STATE_CONNECTING = 2; // 正在連線
    public static final int STATE_CONNECTED = 3;  // 已連線到裝置
    // 建構器
    public MyService(Context context, Handler handler) {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myState = STATE_NONE;
        myHandler = handler;
    }
    //設定目前連線狀態的方法
    private synchronized void setState(int state) {
        myState = state;
    }
    //取得目前連線狀態的方法
    public synchronized int getState() {
        return myState;
    }
    //開啟service的方法
    public synchronized void start() {
        // 關閉不必要的執行緒
        if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread == null) {// 開啟執行緒監聽連線
            myAcceptThread = new AcceptThread();
            myAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }
    //連線裝置的方法
    public synchronized void connect(BluetoothDevice device) {
    	// 關閉不必要的執行緒
        if (myState == STATE_CONNECTING) {
            if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        }
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        // 開啟執行緒連線裝置
        myConnectThread = new ConnectThread(device);
        myConnectThread.start();
        setState(STATE_CONNECTING);
    }
    //開啟管理和已連線的裝置間通話的執行緒的方法
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // 關閉不必要的執行緒
        if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread != null) {myAcceptThread.cancel(); myAcceptThread = null;}
        // 建立並啟動ConnectedThread
        myConnectedThread = new ConnectedThread(socket);
        myConnectedThread.start();
        // 傳送已連線的裝置名稱到主界面Activity
        Message msg = myHandler.obtainMessage(Constant.MSG_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        myHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }
    public synchronized void stop() {//停止所有執行緒的方法
        if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread != null) {myAcceptThread.cancel(); myAcceptThread = null;}
        setState(STATE_NONE);
    }
//    public void write(byte[] out) {//向ConnectedThread寫入資料的方法
//        ConnectedThread tmpCt;// 建立臨時物件參考
//        synchronized (this) {// 鎖定ConnectedThread
//            if (myState != STATE_CONNECTED) return;
//            tmpCt = myConnectedThread;
//        }
//        tmpCt.write(out);// 寫入資料
//    }
    public void write(String msg) {//向ConnectedThread寫入資料的方法
    	ConnectedThread tmpCt;// 建立臨時物件參考
    	synchronized (this) {// 鎖定ConnectedThread
    		if (myState != STATE_CONNECTED) return;
    		tmpCt = myConnectedThread;
    	}
    	tmpCt.write(msg);// 寫入資料
    }
    private class AcceptThread extends Thread {//用於監聽連線的執行緒
        // 本機伺服器端ServerSocket
        private final BluetoothServerSocket mmServerSocket;
        public AcceptThread() {
            BluetoothServerSocket tmpSS = null;
            try {// 建立用於監聽的伺服器端ServerSocket
                tmpSS = btAdapter.listenUsingRfcommWithServiceRecord("BluetoothChat", MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmServerSocket = tmpSS;
        }
        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            while (myState != STATE_CONNECTED) {//若果沒有連線到裝置
                try {
                    socket = mmServerSocket.accept();//取得連線的Sock
                } catch (IOException e) {
                	e.printStackTrace();
                    break;
                }
                if (socket != null) {// 若果連線成功
                    synchronized (MyService.this) {
                        switch (myState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // 開啟管理連線後資料交流的執行緒
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            try {// 關閉新Socket
                                socket.close();
                            } catch (IOException e) {
                            	e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
    //用於嘗試連線其他裝置的執行緒
    private class ConnectThread extends Thread {
        private final BluetoothSocket myBtSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // 透過正在連線的裝置取得BluetoothSocket
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
               e.printStackTrace();
            }
            myBtSocket = tmp;
        }
        public void run() {
            setName("ConnectThread");
            btAdapter.cancelDiscovery();// 取消搜尋裝置
            try {// 連線到BluetoothSocket
                myBtSocket.connect();//嘗試連線
            } catch (IOException e) {
            	setState(STATE_LISTEN);//連線中斷後設定狀態為正在監聽
                try {// 關閉socket
                    myBtSocket.close();
                } catch (IOException e2) {
                    e.printStackTrace();
                }
                MyService.this.start();//若果連線不成功，重新開啟service
                return;
            }
            synchronized (MyService.this) {// 將ConnectThread執行緒清空
                myConnectThread = null;
            }
            connected(myBtSocket, mmDevice);// 開啟管理連線後資料交流的執行緒
        }
        public void cancel() {
            try {
                myBtSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //用於管理連線後資料交流的執行緒
    private class ConnectedThread extends Thread {
        private final BluetoothSocket myBtSocket;
//        private final InputStream mmInStream;
//        private final OutputStream myOs;
        DataInputStream din = null;
        DataOutputStream dout = null;
        public ConnectedThread(BluetoothSocket socket) {
            myBtSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
           
            // 取得BluetoothSocket的輸入輸出流
            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
            	din = new DataInputStream(socket.getInputStream());
            	dout = new DataOutputStream(socket.getOutputStream());
            	
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run() {
//            byte[] buffer = new byte[2048];
            int bytes = 0;
            while (true) {// 一直監聽輸入流
                try {
//                    bytes = din.read(buffer);// 從輸入流中讀入資料
//                    //將讀入的資料傳送到主Activity
//                    myHandler.obtainMessage(Constant.MSG_READ, bytes, -1, buffer).sendToTarget();
                	String msgRev = din.readUTF();
                	myHandler.obtainMessage(Constant.MSG_READ, bytes, -1, msgRev).sendToTarget();
                } catch (IOException e) {
                	e.printStackTrace();
                	setState(STATE_LISTEN);//連線中斷後設定狀態為正在監聽
                    break;
                }
            }
        }
//        //向輸出流中寫入資料的方法
//        public void write(byte[] buffer) {
//            try {
//                dout.write(buffer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        //向輸出流中寫入資料的方法
        @SuppressWarnings("unused")
		public void write(String msg) {
        	try {
        		dout.writeUTF(msg);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        public void cancel() {
            try {
                myBtSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

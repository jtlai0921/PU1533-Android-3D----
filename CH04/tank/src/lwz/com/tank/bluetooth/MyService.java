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
//�Ω�޲z�s�u��Service
public class MyService {
    // ���M�Ϊ��ߤ@ UUID
	private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    // �����ܼ�
    private final BluetoothAdapter btAdapter;
    private final Handler myHandler;
    private AcceptThread myAcceptThread;
    private ConnectThread myConnectThread;
    private ConnectedThread myConnectedThread;
    private int myState;
    // ��ܥثe�s�u���A���`��
    public static final int STATE_NONE = 0;       // ����]�S��
    public static final int STATE_LISTEN = 1;     // ���b��ť�s�u
    public static final int STATE_CONNECTING = 2; // ���b�s�u
    public static final int STATE_CONNECTED = 3;  // �w�s�u��˸m
    // �غc��
    public MyService(Context context, Handler handler) {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        myState = STATE_NONE;
        myHandler = handler;
    }
    //�]�w�ثe�s�u���A����k
    private synchronized void setState(int state) {
        myState = state;
    }
    //���o�ثe�s�u���A����k
    public synchronized int getState() {
        return myState;
    }
    //�}��service����k
    public synchronized void start() {
        // ���������n�������
        if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread == null) {// �}�Ұ������ť�s�u
            myAcceptThread = new AcceptThread();
            myAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }
    //�s�u�˸m����k
    public synchronized void connect(BluetoothDevice device) {
    	// ���������n�������
        if (myState == STATE_CONNECTING) {
            if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        }
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        // �}�Ұ�����s�u�˸m
        myConnectThread = new ConnectThread(device);
        myConnectThread.start();
        setState(STATE_CONNECTING);
    }
    //�}�Һ޲z�M�w�s�u���˸m���q�ܪ����������k
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // ���������n�������
        if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread != null) {myAcceptThread.cancel(); myAcceptThread = null;}
        // �إߨñҰ�ConnectedThread
        myConnectedThread = new ConnectedThread(socket);
        myConnectedThread.start();
        // �ǰe�w�s�u���˸m�W�٨�D�ɭ�Activity
        Message msg = myHandler.obtainMessage(Constant.MSG_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        myHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }
    public synchronized void stop() {//����Ҧ����������k
        if (myConnectThread != null) {myConnectThread.cancel(); myConnectThread = null;}
        if (myConnectedThread != null) {myConnectedThread.cancel(); myConnectedThread = null;}
        if (myAcceptThread != null) {myAcceptThread.cancel(); myAcceptThread = null;}
        setState(STATE_NONE);
    }
//    public void write(byte[] out) {//�VConnectedThread�g�J��ƪ���k
//        ConnectedThread tmpCt;// �إ��{�ɪ���Ѧ�
//        synchronized (this) {// ��wConnectedThread
//            if (myState != STATE_CONNECTED) return;
//            tmpCt = myConnectedThread;
//        }
//        tmpCt.write(out);// �g�J���
//    }
    public void write(String msg) {//�VConnectedThread�g�J��ƪ���k
    	ConnectedThread tmpCt;// �إ��{�ɪ���Ѧ�
    	synchronized (this) {// ��wConnectedThread
    		if (myState != STATE_CONNECTED) return;
    		tmpCt = myConnectedThread;
    	}
    	tmpCt.write(msg);// �g�J���
    }
    private class AcceptThread extends Thread {//�Ω��ť�s�u�������
        // �������A����ServerSocket
        private final BluetoothServerSocket mmServerSocket;
        public AcceptThread() {
            BluetoothServerSocket tmpSS = null;
            try {// �إߥΩ��ť�����A����ServerSocket
                tmpSS = btAdapter.listenUsingRfcommWithServiceRecord("BluetoothChat", MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmServerSocket = tmpSS;
        }
        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            while (myState != STATE_CONNECTED) {//�Y�G�S���s�u��˸m
                try {
                    socket = mmServerSocket.accept();//���o�s�u��Sock
                } catch (IOException e) {
                	e.printStackTrace();
                    break;
                }
                if (socket != null) {// �Y�G�s�u���\
                    synchronized (MyService.this) {
                        switch (myState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // �}�Һ޲z�s�u���ƥ�y�������
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            try {// �����sSocket
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
    //�Ω���ճs�u��L�˸m�������
    private class ConnectThread extends Thread {
        private final BluetoothSocket myBtSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            // �z�L���b�s�u���˸m���oBluetoothSocket
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
               e.printStackTrace();
            }
            myBtSocket = tmp;
        }
        public void run() {
            setName("ConnectThread");
            btAdapter.cancelDiscovery();// �����j�M�˸m
            try {// �s�u��BluetoothSocket
                myBtSocket.connect();//���ճs�u
            } catch (IOException e) {
            	setState(STATE_LISTEN);//�s�u���_��]�w���A�����b��ť
                try {// ����socket
                    myBtSocket.close();
                } catch (IOException e2) {
                    e.printStackTrace();
                }
                MyService.this.start();//�Y�G�s�u�����\�A���s�}��service
                return;
            }
            synchronized (MyService.this) {// �NConnectThread������M��
                myConnectThread = null;
            }
            connected(myBtSocket, mmDevice);// �}�Һ޲z�s�u���ƥ�y�������
        }
        public void cancel() {
            try {
                myBtSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //�Ω�޲z�s�u���ƥ�y�������
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
           
            // ���oBluetoothSocket����J��X�y
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
            while (true) {// �@����ť��J�y
                try {
//                    bytes = din.read(buffer);// �q��J�y��Ū�J���
//                    //�NŪ�J����ƶǰe��DActivity
//                    myHandler.obtainMessage(Constant.MSG_READ, bytes, -1, buffer).sendToTarget();
                	String msgRev = din.readUTF();
                	myHandler.obtainMessage(Constant.MSG_READ, bytes, -1, msgRev).sendToTarget();
                } catch (IOException e) {
                	e.printStackTrace();
                	setState(STATE_LISTEN);//�s�u���_��]�w���A�����b��ť
                    break;
                }
            }
        }
//        //�V��X�y���g�J��ƪ���k
//        public void write(byte[] buffer) {
//            try {
//                dout.write(buffer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        //�V��X�y���g�J��ƪ���k
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

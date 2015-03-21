
package com.example.android.wifidirect.discovery;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocketHandler extends Thread {

    private static final String TAG = "ClientSocketHandler";
    private Handler handler;
    private ChatManager chat;
    private InetAddress mAddress;
    private Context context;

    public ClientSocketHandler(Handler handler, InetAddress groupOwnerAddress, Context context) {
        this.context = context;
        this.handler = handler;
        this.mAddress = groupOwnerAddress;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {

            socket.setReuseAddress(true);
            socket.bind(null);
            if(WiFiServiceDiscoveryActivity.no_of_nodes == 0) {
                Log.i("Connection info " , "Conntected to 4545");
                socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
                        WiFiServiceDiscoveryActivity.SERVER_PORT), 50000);
            }
            else {
                Log.i("Connection info " , "Conntected to 4546");
                socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
                        WiFiServiceDiscoveryActivity.SERVER_PORT_2), 50000);
            }
            Log.d(TAG, "Launching the I/O handler");
            chat = new ChatManager(socket, handler,false,context);
            new Thread(chat).start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
    }

    public ChatManager getChat() {
        return chat;
    }

}

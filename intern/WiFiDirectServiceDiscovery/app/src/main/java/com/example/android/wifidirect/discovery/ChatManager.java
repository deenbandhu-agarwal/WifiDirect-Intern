
package com.example.android.wifidirect.discovery;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles reading and writing of messages with socket buffers. Uses a Handler
 * to post messages to UI thread for UI updates.
 */
public class ChatManager implements Runnable {
    private Context context;
    private Socket socket = null;
    private Handler handler;
    private  static ArrayList<Socket> socketList = new ArrayList<Socket>();
    private boolean isGroupOwner;
    public ChatManager(Socket socket, Handler handler,boolean isGroupOwner , Context context) {
        this.context = context;
        this.isGroupOwner = isGroupOwner;
        if(isGroupOwner)
        {
            this.socketList.add(socket);
            Log.i("Length of socketlist","" + socketList.size());
        }

            this.socket = socket;

        this.handler = handler;
    }
    private InputStream iStream;
    private OutputStream oStream;
    private static final String TAG = "ChatHandler";

    @Override
    public void run() {
        try {

            byte[] buffer = new byte[1024];

            if(isGroupOwner)
            {
                /////
                Log.i("i AM IN GROUP OWNER","......");
                Socket socket = this.socket;

                if(socket!= null)
                Log.i("i AM IN GROUP OWNER","......");

                try {
                    socket = this.socket;
                    //iStream = socket.getInputStream();
                    //oStream = socket.getOutputStream();

                    int bytes;
                    handler.obtainMessage(WiFiServiceDiscoveryActivity.MY_HANDLE, this)
                            .sendToTarget();
                    String filename  = receiveFile(socket,this.context);

                    for(int i = 0 ; i < socketList.size();i++)
                    {
                        Log.i("Length of socketlist","......" + i);

                        if(socketList.get(i)==socket)
                            continue;

                        oStream = socketList.get(i).getOutputStream();

                        sendFile(socketList.get(i),filename);
                        //   oStream.write(buffer);
                    }

                    /*while (true) {
                        try {
                            // Read from the InputStream
                            //bytes = iStream.read(buffer);
                            //if (bytes == -1) {
                              //  Log.i("i got breaked",":)");
                               // break;

                           // }
                            String filename  = receiveFile(socket,this.context);

                            for(int i = 0 ; i < socketList.size();i++)
                            {
                                Log.i("Length of socketlist","......" + i);

                                if(socketList.get(i)==socket)
                                    continue;

                                oStream = socketList.get(i).getOutputStream();

                                sendFile(socketList.get(i),filename,oStream);
                             //   oStream.write(buffer);

                            }
                            

                            // Send the obtained bytes to the UI Activity
                            Log.d(TAG, "Rec:" + String.valueOf(buffer));
                            //handler.obtainMessage(WiFiServiceDiscoveryActivity.MESSAGE_READ,
                              //      bytes, -1, buffer).sendToTarget();
                        }catch (IOException e) {
                            Log.e(TAG, "disconnected", e);
                        }
                    } */
                }
                catch(IOException e) {
                }
                ///-------
                }
            else
            {
                //iStream = socket.getInputStream();
                oStream = socket.getOutputStream();
                buffer = new byte[1024];
                int bytes;
                handler.obtainMessage(WiFiServiceDiscoveryActivity.MY_HANDLE, this)
                        .sendToTarget();
                receiveFile(socket,this.context);
                /* while (true) {
                    try {
                        // Read from the InputStream
                        bytes = iStream.read(buffer);
                        if (bytes == -1) {
                            break;
                        }

                        // Send the obtained bytes to the UI Activity
                        Log.d(TAG, "Rec:" + String.valueOf(buffer));
                        handler.obtainMessage(WiFiServiceDiscoveryActivity.MESSAGE_READ,
                                bytes, -1, buffer).sendToTarget();
                    } catch (IOException e) {
                        Log.e(TAG, "disconnected", e);
                    }
                }*/

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if(socket== null)
                    Log.i("Socket is null","......");
                if(socket != null)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] buffer)  {

        String FileURL = new String(buffer);
        if(isGroupOwner) {

            for (int i = 0; i < socketList.size(); i++)
                try {

                    Socket socket = socketList.get(i);
                    //OutputStream oStream = socket.getOutputStream();
                    //oStream.flush();
                    //Thread.sleep(2000);
                    Log.i("In Write", "Socket " + i + "started");
                    if(socket != null)
                    sendFile(socket, FileURL);
                    else
                        Log.i("Sockt ", "socket is null ");
                    //oStream.write(buffer);
                    //oStream.flush();
                    Log.i("In Write", "Socket " + i + "completed");

                } catch (Exception e) {
                    Log.e(TAG, "disconnected", e);
                }
        }
        else
        {
            /*
            try {
                //oStream.write(buffer);
                sendFile(this.socket,FileURL,oStream);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }*/
            sendFile(this.socket,FileURL);
        }
    }
    public String receiveFile(Socket socket, Context context)
    {
        String filename = "";
        try {

            InputStream inputstream = socket.getInputStream();

            byte buf[] = new byte[1024];
            int len = inputstream.read(buf);
            String s = new String(buf);
            filename = s.substring(0, (s.indexOf('|')));

            String toWrite = s.substring((s.indexOf('|') + 1));
            int fileSize = Integer.parseInt(toWrite.substring(0,toWrite.indexOf('|')));
             int length = (filename.getBytes()).length;

            final File f = new File("" +Environment.getExternalStorageDirectory() + "/"
                    + "Pictures/AayushiJain.pdf"
            );

            File dirs = new File(f.getParent());
            if (!dirs.exists())
                dirs.mkdirs();

            if(f.exists())
                f.delete();

            f.createNewFile();
            FileOutputStream fis = new FileOutputStream(f);
            try {
                int i = 0;
                while (fileSize > 0 && (len = inputstream.read(buf, 0, (int)Math.min(buf.length, fileSize))) != -1) {
                    fis.write(buf, 0, len);
                    fileSize -= len;
                }

                fis.close();
                inputstream.close();
            } catch (IOException e) {

            }
        }
        catch(IOException e)
        {
        }

        return ("" +Environment.getExternalStorageDirectory() + "/"
                + "Pictures/AayushiJain.pdf"
        );
    }

       void sendFile(Socket socket, String FileURL) {
        FileURL = "" +Environment.getExternalStorageDirectory() + "/"
                + "Pictures/AayushiJain.pdf";
        try {
            OutputStream stream = socket.getOutputStream();
                FileInputStream is = new FileInputStream(FileURL);
                File f = new File("" + FileURL);

                stream.write(( f.getName() + "|" + f.length() + "|").getBytes(),0,(f.getName() + "|" + f.length() + "|").getBytes().length);
                int len;
                byte[] buf = new byte[1024];
                try {
                    while ((len = is.read(buf)) != -1) {

                        stream.write(buf, 0, len);
                    }
                    stream.close();
                    is.close();
                } catch (IOException e) {

                }


        } catch (IOException e){
        }

      }

}

package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    private volatile boolean running = true;
    private ServerSocket listener;

    public ServerThread() {
        try {
            listener = new ServerSocket(6969, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket botSocket = listener.accept();
                new Thread(new WorkerThread(botSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerSocket getListener() {
        return listener;
    }

    public void stop() {
        try {
            running = false;
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

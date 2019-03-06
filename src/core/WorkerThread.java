package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkerThread implements Runnable {
    private volatile boolean running = true;
    private final Socket workerSocket;
    BufferedReader in = null;

    public WorkerThread(Socket botSocket) throws IOException {
        this.workerSocket = botSocket;
        in = new BufferedReader(new InputStreamReader(workerSocket.getInputStream()));
        API.workers.add(this);
    }

    @Override
    public void run() {
        while (running) {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.contains("mule:")) {
                        API.muleRequests.add(line.split("mule:")[1]);
                    }

                    if (line.contains("status:")) {
                        API.addWorkerStatus(line);
                    }
                }

                if (in.readLine() == null) {
                    stop();
                }
            } catch (IOException e) {
                e.printStackTrace();
                stop();
            }
        }
    }

    public void stop() {
        try {
            running = false;
            workerSocket.close();
            API.workers.remove(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

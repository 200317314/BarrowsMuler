package core;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Timer;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class API {
    public static List<WorkerThread> workers = new ArrayList<>();
    public static List<String> muleRequests = new ArrayList<>();
    public static String status = "Waiting for request...";
    public static int goldAmount = 0;
    public static Timer banTimer = null;

    public static HashMap<String, String> workerStatus = new HashMap<>();

    public static final Area area = new Area(3360, 3177, 3374, 3165);

    public static void drawBox(int a, int b, int c, int d,  Graphics g) {
        g.setColor(new Color(25,25,25,150));
        g.fillRect(a, b, c, d);
        g.setColor(Color.BLACK);
        g.draw3DRect(a +2, b +2, c -4, d -4, false);
        g.setColor(Color.DARK_GRAY);
        g.draw3DRect(a -0, b -0, c +0, d +0, false);
    }

    public static String getGold(int gold) {
        DecimalFormat df = new DecimalFormat("##.##");
        if (gold >= 1000000) {
            return df.format(((double)gold/1000000.00)) + "m";
        } else if (gold < 1000000) {
            return df.format((gold/1000)) + "k";
        }
        return "0k";
    }

    public static void setLoggerDisabled() {
        Utils.getScript().getRandomManager().disableSolver(RandomEvent.LOGIN);
    }

    public static void setLoggerEnabled() {
        Utils.getScript().getRandomManager().enableSolver(RandomEvent.LOGIN);
    }

    public static String getWorkerUsername() {
        if (muleRequests.size() != 0) {
            return muleRequests.get(0).split(":")[0];
        } else {
            return null;
        }
    }

    public static int getWorkerWorld() {
        if (muleRequests.size() != 0) {
            return Integer.parseInt(muleRequests.get(0).split(":")[1]);
        } else {
            return -1;
        }
    }

    //status:Test1:317:600000:Attacking...:true

    public static String getWorkerStatusName(String status) {
        return status.split(":")[1];
    }

    public static int getWorkerStatusWorld(String status) {
        return Integer.parseInt(status.split(":")[2]);
    }

    public static int getWorkerStatusGold(String status) {
        return Integer.parseInt(status.split(":")[3]);
    }

    public static String getWorkerStatusChests(String status) {
        return status.split(":")[4];
    }

    public static String getWorkerStatusStatus(String status) {
        return status.split(":")[6];
    }

    public static boolean getWorkerStatusOnline(String status) {
        return Boolean.parseBoolean(status.split(":")[5]);
    }

    public static void addWorkerStatus(String status) {
        if (workerStatus.containsKey(getWorkerStatusName(status))) {
            workerStatus.replace(getWorkerStatusName(status), status);
        } else {
            if (!status.contains("-")) {
                workerStatus.put(getWorkerStatusName(status), status);
            }
        }
    }
}

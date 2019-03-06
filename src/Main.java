import core.API;
import core.ServerThread;
import core.Utils;
import core.WorkerThread;
import core.nodes.LoginNode;
import core.nodes.TradeNode;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.script.listener.AdvancedMessageListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.awt.*;

@ScriptManifest(name = "DynaMuler", author = "7804364", version = 1.80D, description = "makes mad money.", category = Category.MISC)
public class Main extends TaskScript implements AdvancedMessageListener {
    private Utils utils = new Utils(this);
    private Timer runTime = new Timer();
    private ServerThread serverThread;

    @Override
    public void onStart() {
        serverThread = new ServerThread();
        new Thread(serverThread).start();

        API.setLoggerDisabled();

        addNodes(new LoginNode());
        addNodes(new TradeNode());
    }

    @Override
    public void onPaint(Graphics2D g) {
        //Status
        API.drawBox(5, 20, 140, 46, g);
        g.setColor(Color.WHITE);
        g.drawString(getManifest().name() + " v" + getVersion(), 10, 32);
        g.setColor(Color.GRAY);
        g.drawString("RunTime: " + Timer.formatTime(runTime.elapsed()), 10, 46);
        g.drawString(API.status, 10, 60);

        API.drawBox(5, 70, 140, 44, g);
        if (API.workers.size() == 0) {
            g.setColor(Color.ORANGE);
            g.drawString("Workers: " + API.workerStatus.size(), 10, 82);
        } else {
            g.setColor(Color.GREEN);
            g.drawString("Workers: " + API.workerStatus.size(), 10, 82);
        }

        if (!serverThread.getListener().isClosed()) {
            g.setColor(Color.GREEN);
            g.drawString("Server: Online (" + API.muleRequests.size() + ")", 10, 96);
        } else {
            g.setColor(Color.RED);
            g.drawString("Server: Offline (" + API.muleRequests.size() + ")", 10, 96);
        }
        g.setColor(Color.YELLOW);
        g.drawString("Gold: " + API.getGold(API.goldAmount) + " C: " + getTotalChestCount(), 10, 110);


        //Mule Status
        if (API.muleRequests.size() != 0) {
            API.drawBox(150, 20, 140, 46, g);
            g.setColor(Color.WHITE);
            g.drawString("User:" + API.getWorkerUsername(), 155, 32);
            g.setColor(Color.gray);
            g.drawString("World: " + API.getWorkerWorld(), 155, 46);
            g.drawString("Ignore: " + Timer.formatTime(API.banTimer.elapsed()), 155, 60);
        }

        //Worker statuses
        int multiBox = API.workerStatus.size()*12;
        int multiItem = 12;
        int index = 1;
        if (API.workerStatus.size() > 0) {
            API.drawBox(5, 120, longestWidth(g), 4 + multiBox, g);
            g.setColor(Color.GRAY);

            for (String worker : API.workerStatus.keySet()) {
                if (API.getWorkerStatusOnline(API.workerStatus.get(worker))) {
                    g.setColor(Color.GREEN);
                    g.drawString(worker + ": " + API.getWorkerStatusWorld(API.workerStatus.get(worker)) + " | " +
                            API.getGold(API.getWorkerStatusGold(API.workerStatus.get(worker))) + " | " +
                            API.getWorkerStatusChests(API.workerStatus.get(worker)) + " | " +
                            "Online" + " | " + API.getWorkerStatusStatus(API.workerStatus.get(worker)), 10, 120 + (multiItem*index));
                } else {
                    g.setColor(Color.RED);
                    g.drawString(worker + ": " + API.getWorkerStatusWorld(API.workerStatus.get(worker)) + " | " +
                            API.getGold(API.getWorkerStatusGold(API.workerStatus.get(worker))) + " | " +
                            API.getWorkerStatusChests(API.workerStatus.get(worker)) + " | " +
                            "Offline" + " | " + API.getWorkerStatusStatus(API.workerStatus.get(worker)), 10, 120 + (multiItem*index));
                }
                index++;
            }
        }
    }

    private int getTotalChestCount() {
        int chestCount = 0;

        for (String worker : API.workerStatus.keySet()) {
            chestCount += Integer.parseInt(API.getWorkerStatusChests(API.workerStatus.get(worker)));
        }

        return chestCount;
    }

    private int longestWidth(Graphics2D g) {
        int longest = 0;
        for (String worker : API.workerStatus.keySet()) {
            String statusFull = worker + ": " + API.getWorkerStatusWorld(API.workerStatus.get(worker)) + " | " +
                    API.getGold(API.getWorkerStatusGold(API.workerStatus.get(worker))) + " | " +
                    API.getWorkerStatusChests(API.workerStatus.get(worker)) + " | " +
                    "Online" + " | " + API.getWorkerStatusStatus(API.workerStatus.get(worker));

            if (g.getFontMetrics().stringWidth(statusFull) > longest) {
                longest = g.getFontMetrics().stringWidth(statusFull);
            }
        }
        return longest;
    }

    @Override
    public void onExit() {
        serverThread.stop();

        for (WorkerThread worker : API.workers) {
            worker.stop();
        }
    }

    @Override
    public void onAutoMessage(Message message) {

    }

    @Override
    public void onPrivateInfoMessage(Message message) {

    }

    @Override
    public void onClanMessage(Message message) {

    }

    @Override
    public void onGameMessage(Message message) {
        if (message != null) {
            if (message.getMessage().equals("Accepted trade.")) {
                API.muleRequests.remove(0);
                API.goldAmount = getInventory().count("Coins");
            }
        }
    }

    @Override
    public void onPlayerMessage(Message message) {

    }

    @Override
    public void onTradeMessage(Message message) {

    }

    @Override
    public void onPrivateInMessage(Message message) {

    }

    @Override
    public void onPrivateOutMessage(Message message) {

    }
}

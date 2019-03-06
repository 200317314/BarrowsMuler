package core.nodes;

import core.API;
import org.dreambot.api.data.GameState;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;;

public class TradeNode extends TaskNode {
    @Override
    public boolean accept() {
        return canTrade();
    }

    @Override
    public int execute() {
        API.status = "Muling gold...";
        if (getClient().getCurrentWorld() == API.getWorkerWorld()) {
            if (API.area.contains(getLocalPlayer())) {
                Player mule = getPlayers().closest(p -> p != null && p.getName().equals(API.getWorkerUsername()) && API.area.contains(p));

                if (getTrade().isOpen()) {
                    if (containsGold(getTrade().getTheirItems())) {
                        getTrade().acceptTrade();
                    }
                } else if (mule != null) {
                    getTrade().tradeWithPlayer(mule.getName());
                    sleepUntil(() -> getTrade().isOpen(), 6000);
                }
            } else if (getWalking().shouldWalk(5)) {
                getWalking().walk(API.area.getCenter().getRandomizedTile(3));
            }
        } else {
            getWorldHopper().hopWorld(API.getWorkerWorld());
            sleepUntil(() -> getClient().getCurrentWorld() == API.getWorkerWorld(), 8000);
        }

        if (API.banTimer != null && API.banTimer.elapsed() >= 240000) {
            API.muleRequests.remove(0);

            if (API.muleRequests.size() != 0) {
                API.banTimer = new Timer();
            }
        }
        return 450;
    }

    private boolean canTrade() {
        return getClient().getGameState() == GameState.LOGGED_IN && API.muleRequests.size() != 0;
    }

    private boolean containsGold(Item[] items) {
        if (items != null) {
            for (Item i : items) {
                if (i.getName().equals("Coins")) {
                    return true;
                }
            }
        }
        return false;
    }
}

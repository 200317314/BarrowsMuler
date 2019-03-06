package core.nodes;

import core.API;
import org.dreambot.api.data.GameState;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.Timer;

public class LoginNode extends TaskNode {
    @Override
    public boolean accept() {
        return canLogin() || canLogout();
    }

    @Override
    public int execute() {
        if (canLogin()) {
            API.setLoggerEnabled();
            API.banTimer = new Timer();
        }

        if (canLogout()) {
            API.setLoggerDisabled();
            API.status = "Waiting for request...";
            API.banTimer = null;
            getTabs().logout();
        }
        return 0;
    }

    private boolean canLogin() {
        return getClient().getGameState() != GameState.LOGGED_IN && API.muleRequests.size() != 0;
    }

    private boolean canLogout() {
        return getClient().getGameState() == GameState.LOGGED_IN && API.muleRequests.size() == 0;
    }
}

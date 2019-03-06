package core;

import org.dreambot.api.script.AbstractScript;

public class Utils {
    private static AbstractScript abstractScript;

    public Utils(AbstractScript abstractScript) {
        this.abstractScript = abstractScript;
    }

    public static AbstractScript getScript() {
        return abstractScript;
    }
}

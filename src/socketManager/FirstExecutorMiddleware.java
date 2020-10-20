package socketManager;

import socketManager.interfaces.NetEvent;
import packetManager.Request;
import packetManager.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andrea Marco Farioli
 * @author Davide Melillo
 * @version 1.0.0
 * Manages the generic events in SocketManager
 */
public class FirstExecutorMiddleware {
    private final ArrayList<NetEvent> netEvents = new ArrayList<>();

    public void on(NetEvent listener){
        netEvents.add(listener);
    }

    public void fire(Request request, Response response) throws IOException {
        for (NetEvent netEvent : netEvents) {
            netEvent.eventEmitted(request, response);
        }
    }

}

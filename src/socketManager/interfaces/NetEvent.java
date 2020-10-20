package socketManager.interfaces;

import packetManager.Request;
import packetManager.Response;

import java.io.IOException;

/**
 * @author Andrea Marco Farioli
 * @author Davide Melillo
 * @version 1.0.0
 * A FunctionalInterface that represents a generic request income
 */
@FunctionalInterface
public interface NetEvent {
    void eventEmitted(Request request, Response response) throws IOException;
}
package socketManager;

import socketManager.interfaces.NetEvent;
import packetManager.Parser;
import packetManager.Request;
import packetManager.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Marco Farioli
 * @author Davide Melillo
 * @version 1.0.0
 * This class manage the streams of the socket, into another thread.<br>
 * It makes a loop to wait for a packet request or a emit ask.<br>
 * Manages the requests with a EventManager.
 */
public class SocketManager extends Thread {
    private final Socket socket;
    private final FirstExecutorMiddleware eventManager;
    private final Parser parser;
    private final Response response;
    private final List<byte[]> emitQueue = new ArrayList<>();
    private final Object syncQueue = new Object();
    private boolean closing = false;

    /**
     * Constructor
     * @param socket - the socket managed.
     * @throws IOException
     */
    public SocketManager(Socket socket) throws IOException {
        this(socket, new FirstExecutorMiddleware());
    }

    public SocketManager(Socket socket, FirstExecutorMiddleware eventManager) throws IOException{
        this.socket = socket;
        this.eventManager = eventManager;
        response = new Response(this.socket.getOutputStream());
        parser = new Parser(this.socket.getInputStream());
    }

    /**
     * Adds a NetEvent listener at eventManager
     * @param listener - a callback
     */
    public void on(NetEvent listener){
        eventManager.on(listener);
    }

    /**
     * Emits a packet trough the OutputStream
     * @param packet - byte array of the packet, .send() method of the PacketBuilder
     */
    public void emit(byte[] packet) {
            synchronized (syncQueue) {
                emitQueue.add(packet);
            }
    }

    @Override
    public void run() {
            while (!socket.isClosed()) {
                checkQueueEmit();
                if (closing){
                    try {
                        socket.close();
                        interrupt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getPacket();
            }
    }

    private void checkQueueEmit(){
        while (emitQueue.size() > 0){
            try {
                response.emit(emitQueue.get(0));
                emitQueue.remove(0);
            } catch (IOException ignored) {}
        }
    }

    private void getPacket(){
        try {
            if (socket.getInputStream().available() != 0) {
                parser.beginning();
                Request request = parser.parse();
                parser.closing();
                eventManager.fire(request, response);
            }
        } catch (Exception ignored) {}
    }

    public Response getResponse() {
        return response;
    }

    public void close() throws IOException {
        closing = true;
    }
}

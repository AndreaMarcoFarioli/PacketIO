package socketManager;

import org.jetbrains.annotations.NotNull;
import packetManager.ContentTypes;
import packetManager.interfaces.ParamModifier;
import socketManager.interfaces.NetEvent;
import socketManager.utilities.Tuple;
import packetManager.PacketBuilder;
import packetManager.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Andrea Marco Farioli
 * @author Davide Melillo
 * @version 1.0.0
 * This class is an high level representation of a SocketManager object<br>
 * That makes a nominal event oriented management of the socket traffic.
 */
public class EventSocketOriented {
    private final SocketManager socketManager;
    private final HashMap<String, List<NetEvent>> eventsMap = new HashMap<>();
    private String contentType = ContentTypes.TEXT;
    private boolean emitted = false;

    public EventSocketOriented(SocketManager socketManager){
        this.socketManager = socketManager;
    }

    public void start() throws Exception {
        setEvents();
        socketManager.start();
    }

    private void setEvents() throws Exception {
        socketManager.on((request, response) -> {
            String eventName = request.getHeader("event_name");
            if(eventName != null)
                if (eventsMap.containsKey(eventName))
                    fire(eventName, request);
        });

        on("closing", (request, response) -> {
            System.out.println("closing");
            socketManager.close();
        });
    }

    private void fire(String eventName, Request request){
        List<NetEvent> netEvents = eventsMap.get(eventName);
        if (netEvents != null){
            for (NetEvent netEvent : netEvents) {
                try {
                    netEvent.eventEmitted(request, socketManager.getResponse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Adds the listener into a List of the HashMap's result that matches the eventTrigger, or makes it.
     * @param eventTrigger - the name of the event
     * @param listener - the listener associated with the name
     */
    public void on(String eventTrigger, NetEvent listener) throws Exception {
        if(eventNameChecker(eventTrigger))
            throw new Exception("");
        List<NetEvent> netEvents = eventsMap.get(eventTrigger);
        if(netEvents != null){
            netEvents.add(listener);
        }else{
            netEvents = new ArrayList<>();
            netEvents.add(listener);
            eventsMap.put(eventTrigger, netEvents);
        }
    }

    /**
     * Dispatch a packet trough the OutputStream and makes the PacketBuilder automatically
     * @param eventName - The eventName of the packet
     * @param args - Arguments of the packet(body), uses the Tuple standard.
     */
    @SafeVarargs
    public final void emit(String eventName, Tuple<String, String>... args) throws Exception {
        emit(eventName, null, args);
    }


    @SafeVarargs
    public final void emit64(String eventName, Tuple<String, String>... args) throws Exception {
        emit(eventName, s -> new String(Base64.getEncoder().encode(s.getBytes())), args);
    }

    @SafeVarargs
    public final void emit(String eventName, ParamModifier<String> modifier, Tuple<String, String>... args) throws Exception {
        if(eventNameChecker(eventName))
            throw new Exception("");
        byte[] packet = createEventPacket(eventName, modifier,args).send();
        for (byte b : packet) {
            System.out.print((char)b);
        }
        socketManager.emit(packet);
    }

    private boolean eventNameChecker(String eventName){
        return !Pattern.compile("[a-zA-Z0-9_-]*").matcher(eventName).matches();
    }

    @SafeVarargs
    private PacketBuilder createEventPacket(String eventName, Tuple<String, String>... args){
        return createEventPacket(eventName, null, args);
    }

    @SafeVarargs
    private PacketBuilder createEventPacket(String eventName, ParamModifier<String> modifier, Tuple<String, String>... args) {
        PacketBuilder builder = new PacketBuilder();
        builder.setHeader("event_name", eventName);
        builder.setHeader("content_type", contentType);
        for (Tuple<String, String> arg : args)
            builder.setBody(arg.getFirstValue(), arg.getSecondValue(), modifier);
        return builder;
    }

    public void setContentType(@NotNull String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void close() throws Exception {
        emit("closing");
        socketManager.close();
    }

    public SocketManager getSocketManager() {
        return socketManager;
    }
}
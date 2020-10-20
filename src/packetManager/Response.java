package packetManager;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private final OutputStream outputStream;
    public Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }
    public void emit(byte[] packet) throws IOException {
        outputStream.write(packet);
    }


}

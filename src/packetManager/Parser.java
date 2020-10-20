package packetManager;


import java.io.IOException;
import java.io.InputStream;
/**
 * @author Andrea Marco Farioli
 * @author Davide Melillo
 * @version 1.0.0
 * This class parse the Raw Packet and puts it into a Request object
 */
public class Parser {
    private final int
        START_HEADER_LENGTH,
        END_HEADER_LENGTH;

    final String
            START_HEADER = "---START HEADER---\n",
            END_HEADER = "---END HEADER---\n";

    private final InputStream socketStream;

    /**
     * Constructor
     * @param socketStream - the InputStream of the socket, by reference
     */
    public Parser(InputStream socketStream){
        START_HEADER_LENGTH = START_HEADER.length();
        END_HEADER_LENGTH = END_HEADER.length();
        this.socketStream = socketStream;
    }

    /**
     * Calls the parse header, and parse body, and puts the results into a Request object
     * @return a Request object built on Raw Packet
     * @throws IOException
     */
    public Request parse() throws IOException {
        Request request = new Request();
        parseHeader(request);
        parseBody(request);
        return request;
    }

    /**
     * Monitor the stream to open a Packet Parse
     * @throws Exception - Not opened
     */
    public void beginning() throws Exception{
        byte[] bytes = new byte[START_HEADER_LENGTH];
        try {
            socketStream.read(bytes, 0, START_HEADER_LENGTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!new String(bytes).equals(START_HEADER))
            throw new IOException("Not opened");
    }

    /**
     * Monitor the stream to close the Packet Parse
     * @throws Exception - Not closed
     */
    public void closing() throws Exception{
        byte[] bytes = new byte[END_HEADER_LENGTH];
            try {
                socketStream.read(bytes, 0, END_HEADER_LENGTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(!new String(bytes).equals(END_HEADER))
            throw new IOException("Not closed");
    }

    private void parseHeader(Request request) throws IOException {
        char letter;
        StringBuilder builder;
        do{
            builder = new StringBuilder();

            do{
                letter = (char)socketStream.read();
                builder.append(letter);
            }while (letter != '\n');

            if(builder.length() > 1){
                String[] data = builder.toString().split(":\t");
                if(data.length == 2)
                    request.setHeader(data[0], data[1].substring(0, data[1].length() - 1));
            }
        }while (builder.length() != 1);
    }

    private void parseBody(Request request) throws IOException {
        int bodyLength = Integer.parseInt(request.getHeader("content-length"));
        byte[] body = new byte[bodyLength];
        socketStream.read(body, 0, bodyLength);

        String bodyString = new String(body);
        String[] allFieldsValue = bodyString.split("\n");
        for (String fieldValue : allFieldsValue){
            if(fieldValue.length() > 0){
                String[] data = fieldValue.split(":\t");
                request.setBody(data[0], data[1]);
            }
        }
    }
}

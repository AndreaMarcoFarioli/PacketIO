package packetManager;

import packetManager.interfaces.ParamModifier;

/**
 * @author Andrea Marco Farioli
 * @author Davide Melillo
 * @version 1.0.0
 * This class builds and manage a byte array with a generic protocol type<br>
 * ---START HEADER---\n<br>
 * header-field:\tvalue\n<br>
 * \n<br>
 * body-field:\tvalue\n<br>
 * ---END HEADER---\n
 */
public class PacketBuilder {
    private final StringBuilder
        stringBuilderHeader = new StringBuilder(),
        stringBuilderBody = new StringBuilder(),
        stringBuilderResult = new StringBuilder();

    private final String
        START_HEADER =  "---START HEADER---\n",
        END_HEADER =    "---END HEADER---\n";

    /**
     * This method sets an header line with a key and value
     * @param headerField - this String sets the key of the field
     * @param headerContent - this String sets the content associated with key of the headerField
     */
    public void setHeader(String headerField, String headerContent){
        stringBuilderHeader.append(headerField).append(":\t").append(headerContent).append("\n");
    }

    /**
     * This method sets a body line with a key and value
     * @param bodyField - this String sets the key of the field
     * @param bodyContent - this String sets the content associated with key of the bodyField
     */
    public void setBody(String bodyField, String bodyContent){
        stringBuilderBody.append(bodyField).append(":\t").append(bodyContent).append("\n");
    }

    public void setBody(String bodyField, String bodyContent, ParamModifier<String> modifier){
        if (modifier != null)
            bodyContent = modifier.modify(bodyContent);
        setBody(bodyField, bodyContent);
    }

    /**
     * Encapsulates the packet into delimiters, join the header and body and calls createBody method
     * @return the byte array of the packet
     */
    public byte[] send(){
        createBody();
        stringBuilderResult.append(START_HEADER).append(stringBuilderHeader).append(stringBuilderBody).append(END_HEADER);
        return String.valueOf(stringBuilderResult).getBytes();
    }

    /**
     * Adds the content-length of the body, it will be 0 only if the body is empty
     */
    public void createBody(){
        stringBuilderHeader.append("content-length:\t").append(stringBuilderBody.length()).append("\n\n");
    }

}

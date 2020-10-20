package packetManager;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Andrea Marco Farioli
 * @author Davide Melillo
 * @version 1.0.0
 * This class represents a packet parsed
 */
public class Request {
    private final HashMap<String, String>
            header = new HashMap<>(),
            body = new HashMap<>();

    /**
     * Set a headerField with a content into an HashMap&lt;String, String&gt;
     * @param field - headerField name
     * @param value - content of the field
     */
    public void setHeader(String field, String value){
        header.put(field, value);
    }

    /**
     * Set a bodyField with a content into an HashMap&lt;String, String&gt;
     * @param field - bodyField name
     * @param value - content of the field
     */
    public void setBody(String field, String value){
        body.put(field, value);
    }

    /**
     * Get the content of a headerField from an HashMap&lt;String, String&gt;
     * @param field - the name of the headerField
     * @return the content of the field
     */
    public String getHeader(String field) {
        return header.get(field);
    }

    /**
     * Get the content of a bodyField from an HashMap&lt;String, String&gt;
     * @param field - the name of the bodyField
     * @return the content of the field
     */
    public String getBody(String field) {
        return body.get(field);
    }

    // public void printKeys(){
    //     System.out.println(header.keySet());
    // }

    public Set<String> getBodyKeys(){
        return body.keySet();
    }

    public Set<String> getHeaderKeys(){
        return header.keySet();
    }

    @Override
    public String toString() {
        return "Request{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
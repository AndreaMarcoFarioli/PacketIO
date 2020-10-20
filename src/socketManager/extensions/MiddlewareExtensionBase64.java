package socketManager.extensions;

import packetManager.Request;
import packetManager.Response;
import socketManager.FirstExecutorMiddleware;

import java.io.IOException;
import java.util.Base64;

public class MiddlewareExtensionBase64 extends FirstExecutorMiddleware {
    @Override
    public void fire(Request request, Response response) throws IOException {
        String contentType = request.getHeader("content_type");
        if(contentType != null)
            if(contentType.equals("base-64"))
                for (String bodyKey : request.getBodyKeys())
                    request.setBody(bodyKey, new String(Base64.getDecoder().decode(request.getBody(bodyKey))));
        super.fire(request, response);
    }
}

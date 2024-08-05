package Server;

import java.io.InputStream;
import java.io.OutputStream;

public class Endpoint {
    public StatusCode GET(InputStream input, OutputStream output) {
        return StatusCode.METHOD_NOT_ALLOWED;
    }
    
    public StatusCode POST(InputStream input, OutputStream output) {
        return StatusCode.METHOD_NOT_ALLOWED;
    }

    public StatusCode DELETE(InputStream input, OutputStream output) {
        return StatusCode.METHOD_NOT_ALLOWED;
    }

    public StatusCode PATCH(InputStream input, OutputStream output) {
        return StatusCode.METHOD_NOT_ALLOWED;
    }
}

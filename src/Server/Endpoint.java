package Server;

public class Endpoint {
    public StatusCode GET() {
        return StatusCode.METHOD_NOT_ALLOWED;
    }
    
    public StatusCode POST() {
        return StatusCode.METHOD_NOT_ALLOWED;
    }

    public StatusCode DELETE() {
        return StatusCode.METHOD_NOT_ALLOWED;
    }

    public StatusCode PATCH() {
        return StatusCode.METHOD_NOT_ALLOWED;
    }
}

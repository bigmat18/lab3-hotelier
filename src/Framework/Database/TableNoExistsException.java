package Framework.Database;

public class TableNoExistsException extends Exception {
    TableNoExistsException(String msg) {
        super(msg);
    }
}

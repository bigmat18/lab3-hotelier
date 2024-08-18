package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Command {
    protected String name = "";
    protected String description = "";

    public String getName() { return this.name; }

    public String getDescption() { return this.description; }

    public abstract void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException;
}

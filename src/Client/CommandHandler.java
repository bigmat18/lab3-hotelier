package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandHandler {
    private AppStatus status;
    private Map<String, Command> commands;

    public CommandHandler(AppStatus status) {
        this.status = status;
        this.commands = new LinkedHashMap<String, Command>();
    }

    public void addCommand(Command cmd) {
        this.commands.put(cmd.getName(), cmd);
    }

    public void execute(DataInputStream input, DataOutputStream output) throws IOException {
        String choose = Keyboard.StringReader(">");
        if(choose.equals("help")) {
            System.out.println(getCommandsList());
            return;
        }

        if(choose.equals("quit")) {
            this.status.setRunning(false);
            System.out.println("Quitting hotelier");
            return;
        }

        Command cmd = this.commands.get(choose);
        if(cmd == null)
            System.out.println("Invalid option");
        
        cmd.execution(input, output, this.status);
    }

    public String getCommandsList() {
        String str = "\nDigit one of following command or 'quit' to exit.\n\n";
        for(Map.Entry<String, Command> entry : this.commands.entrySet()){
            str += "   " + entry.getKey() + entry.getValue().getDescption() + "\n";
        }
        str += "   help\t\t\tView list of commands\n";
        return str;
    }
}

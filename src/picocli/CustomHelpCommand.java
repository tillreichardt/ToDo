package picocli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

import java.util.List;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

@Command(name = "help", description = "Show this help message", mixinStandardHelpOptions = true)
public class CustomHelpCommand implements Runnable {

    @CommandLine.Spec
    CommandSpec spec; // injected by picocli
    
    DatabaseConnector db = cliNavigation.getDatabaseConnector();

    @Override
    public void run() {
        
        // help page for every command 
        CommandLine cmd = new CommandLine(spec.parent());
        cmd.usage(System.out);

        // help for sorting options from show command
        printShowCommandOptions();
    }
    
    public void printShowCommandOptions() {
        CommandLine showCmd = new CommandLine(new ShowCommand());
        CommandLine createCmd = new CommandLine(new CreateCommand());
        CommandLine deleteCmd = new CommandLine(new DeleteCommand());        
        CommandLine updateCmd = new CommandLine(new DeleteCommand());     

        System.out.println("\nSort options:");
        printOptions(showCmd);
        System.out.println("\nCreateCommand options:");
        printOptions(createCmd);
        System.out.println("\nDeleteCommand options:");
        printOptions(deleteCmd);
        System.out.println("\nUpdateCommand options:");
        printOptions(updateCmd);
    }
    
    private void printOptions(CommandLine commandLine, String... optionNames) {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        List<OptionSpec> options = commandSpec.options();
    
        for (OptionSpec option : options) {
            String[] description = option.description();
            String desc = description.length > 0 ? description[0] : "";  // Falls keine Beschreibung vorhanden ist
    
            // Typ der Option (Klassenname des Parameters)
            String paramType = option.type().getSimpleName();
    
            // Falls optionNames angegeben wurden, nur diese Optionen zeigen
            if (optionNames.length == 0 || arrayContains(optionNames, option.longestName())) {
                System.out.printf("  %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
            }
        }
    }
    
    
    private boolean arrayContains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
}

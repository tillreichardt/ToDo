package picocli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;


import com.List;
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
        printCommandOptions();
    }
    
    public void printCommandOptions() {
        CommandLine showCmd = new CommandLine(new ShowCommand());
        CommandLine createCmd = new CommandLine(new CreateCommand());

        printOptions(showCmd);
        System.out.println("\nCreateCommand options:");
        printOptions(createCmd);
    }
    
    private void printOptions(CommandLine commandLine, String... optionNames) {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        List<OptionSpec> options = new List<>();
    
        // add options
        for (OptionSpec option : commandSpec.options()) {
            options.append(option);
        }

        List<OptionSpec> userOptions = new List<>();
        List<OptionSpec> todoOptions = new List<>();
        List<OptionSpec> categoryOptions = new List<>();
        List<OptionSpec> sortOptions = new List<>();
        
        // sort options into these 4 lists
        options.toFirst(); 
        while (options.hasAccess()) {
            OptionSpec option = options.getContent();
            // options for ShowCommand
            if (commandLine.getCommandName().equals("show") && (option.longestName().contains("sort-by") || option.longestName().contains("order"))) {
                sortOptions.append(option);
            } 
            // user options (username, password)
            else if (option.longestName().contains("username") || option.longestName().contains("password")) {
                userOptions.append(option);
            } 
            // ToDo options (title, description, priority, category)
            else if (option.longestName().contains("title") || option.longestName().contains("description") || option.longestName().contains("priority") || option.longestName().contains("category")) {
                todoOptions.append(option); 

                // add to category if it is the title
                if (option.longestName().contains("title")) {
                    categoryOptions.append(option); 
                }
            }
            options.next();
        }
        
        // show sort options
        if (!sortOptions.isEmpty()) {
            System.out.println("\nSort options:");
            sortOptions.toFirst();
            while (sortOptions.hasAccess()) {
                OptionSpec option = sortOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : ""; // use desc or "" if there is non
                System.out.printf("  %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                sortOptions.next();
            }
        }
    
        // show user options
        if (!userOptions.isEmpty()) {
            System.out.println("  user:");
            userOptions.toFirst(); 
            while (userOptions.hasAccess()) {
                OptionSpec option = userOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : ""; // use desc or "" if there is non
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc); 
                userOptions.next();
            }
        }
    
        // show todo options
        if (!todoOptions.isEmpty()) {
            System.out.println("  todo:");
            todoOptions.toFirst(); 
            while (todoOptions.hasAccess()) {
                OptionSpec option = todoOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : ""; // use desc or "" if there is non
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                todoOptions.next();
            }
        }
    
        // show category options
        if (!categoryOptions.isEmpty()) {
            System.out.println("  category:");
            categoryOptions.toFirst();
            while (categoryOptions.hasAccess()) {
                OptionSpec option = categoryOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : ""; // use desc or "" if there is non
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                categoryOptions.next(); 
            }
        }
    }
}

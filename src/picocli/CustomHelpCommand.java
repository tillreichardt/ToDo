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
        CommandLine updateCmd = new CommandLine(new UpdateCommand());
        CommandLine deleteCmd= new CommandLine(new DeleteCommand());

        printOptions(showCmd);
        printOptions(deleteCmd);
        System.out.println("\nCreateCommand options:");
        printOptions(createCmd);
        printOptions(updateCmd);
    }
    
    private void printOptions(CommandLine commandLine, String... optionNames) {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        List<OptionSpec> options = new List<>();
    
        // Füge die Optionen in deine selbst erstellte Liste ein
        for (OptionSpec option : commandSpec.options()) {
            options.append(option);
        }
    
        // Erstelle separate Listen für die Optionen der verschiedenen Typen
        List<OptionSpec> userOptions = new List<>();
        List<OptionSpec> todoOptions = new List<>();
        List<OptionSpec> categoryOptions = new List<>();
        List<OptionSpec> sortOptions = new List<>();
        List<OptionSpec> deleteOptions = new List<>();
        List<OptionSpec> updateOptions = new List<>();
    
        // Sortiere die Optionen in die entsprechenden Kategorien
        options.toFirst();
        while (options.hasAccess()) {
            OptionSpec option = options.getContent();
            
            // Optionen für ShowCommand (sort-by und order)
            if (commandLine.getCommandName().equals("show") && 
                (option.longestName().contains("sort-by") || option.longestName().contains("order"))) {
                sortOptions.append(option);
            }
            // Optionen für Benutzer (username, password)
            else if (option.longestName().contains("username") || option.longestName().contains("password")) {
                userOptions.append(option);
            }
            // Optionen für ToDo (title, description, priority, category)
            else if (option.longestName().contains("title") || option.longestName().contains("description") ||
                       option.longestName().contains("priority") || option.longestName().contains("category")) {
                todoOptions.append(option); 
    
                // Füge title zur category-Liste hinzu
                if (option.longestName().contains("title")) {
                    categoryOptions.append(option);
                }
            }
            // Optionen für DeleteCommand (id)
            else if (option.longestName().contains("id") && commandLine.getCommandName().equals("delete")) {
                deleteOptions.append(option);
            }
            // Optionen für UpdateCommand (id, username, password, title, description, priority, category)
            else if (commandLine.getCommandName().equals("update") && 
                     (option.longestName().contains("id") || option.longestName().contains("username") || 
                      option.longestName().contains("password") || option.longestName().contains("title") || 
                      option.longestName().contains("description") || option.longestName().contains("priority") || 
                      option.longestName().contains("category"))) {
                updateOptions.append(option);
            }
    
            options.next();
        }
    
        // Sort options für ShowCommand anzeigen
        if (!sortOptions.isEmpty()) {
            System.out.println("\nShowCommand options:");
            sortOptions.toFirst();
            while (sortOptions.hasAccess()) {
                OptionSpec option = sortOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : "";
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                sortOptions.next();
            }
        }
    
        // DeleteCommand options anzeigen
        if (!deleteOptions.isEmpty()) {
            System.out.println("\nDeleteCommand options:");
            deleteOptions.toFirst();
            while (deleteOptions.hasAccess()) {
                OptionSpec option = deleteOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : "";
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                deleteOptions.next();
            }
        }
    
        // UpdateCommand options anzeigen
        if (!updateOptions.isEmpty()) {
            System.out.println("\nUpdateCommand options:");
            updateOptions.toFirst();
            while (updateOptions.hasAccess()) {
                OptionSpec option = updateOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : "";
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                updateOptions.next();
            }
        }
    
        // Optionen für User anzeigen
        if (!userOptions.isEmpty()) {
            
            System.out.println("  user:");
            userOptions.toFirst();
            while (userOptions.hasAccess()) {
                OptionSpec option = userOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : "";
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                userOptions.next();
            }
        }
    
        // Optionen für ToDo anzeigen
        if (!todoOptions.isEmpty()) {
            System.out.println("  todo:");
            todoOptions.toFirst();
            while (todoOptions.hasAccess()) {
                OptionSpec option = todoOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : "";
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                todoOptions.next();
            }
        }
    
        // Optionen für Category anzeigen
        if (!categoryOptions.isEmpty()) {
            System.out.println("  category:");
            categoryOptions.toFirst();
            while (categoryOptions.hasAccess()) {
                OptionSpec option = categoryOptions.getContent();
                String paramType = option.type().getSimpleName();
                String desc = option.description().length > 0 ? option.description()[0] : "";
                System.out.printf("    %-15s %-15s %s%n", option.longestName(), "<" + paramType + ">", desc);
                categoryOptions.next();
            }
        }
    }
    
}

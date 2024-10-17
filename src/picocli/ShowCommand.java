package picocli;

import com.cliNavigation;
import com.Connector.DatabaseConnector;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "show", description = "Show an entity base on type")
public class ShowCommand implements Runnable {
   @Option(names = {"--sort-by", "-s"}, description = "Sort by field: [priority, dueDate, status]")
    String sortBy;

    @Option(names = {"--order", "-o"}, description = "Sort order: [asc, desc]", defaultValue = "asc")
    String order;

    @Override
    public void run() {
        DatabaseConnector dbConnector = cliNavigation.getDatabaseConnector(); 
        System.out.printf("Showing todo items sorted by %s in %s order.%n", sortBy, order);
    }
}

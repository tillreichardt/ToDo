package com.picocli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

import java.util.List;

@Command(name = "help", description = "Show this help message", mixinStandardHelpOptions = true)
public class CustomHelpCommand implements Runnable {

    @CommandLine.Spec
    CommandSpec spec; // injected by picocli
    
    @Override
    public void run() {
        System.out.println("");
        // help page for every command 
        CommandLine cmd = new CommandLine(spec.parent());
        cmd.usage(System.out);

        // help for sorting options from show command
        printShowCommandOptions();
    }

    public void printShowCommandOptions() {
        System.out.println();
        System.out.println("Sort options:");
    
        CommandSpec showSpec = new CommandLine(new ShowCommand()).getCommandSpec();
        List<OptionSpec> options = showSpec.options();
    
        for (OptionSpec option : options) {
            if (option.longestName().equals("--sort-by") || option.longestName().equals("--order")) {
                String[] description = option.description();
                String desc = description.length > 0 ? description[0] : ""; // check if array contains elements
                System.out.printf("  %-10s %s%n", option.longestName(), desc);
            }
        }
    }
    
    
    
}

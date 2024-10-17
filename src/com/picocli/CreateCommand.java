package com.picocli;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(name = "create", description = "Create a ToDo")
public class CreateCommand implements Runnable{
    @Parameters(index = "0", description = "The title of the new todo item")
        String title;

    @Override
    public void run() {
        System.out.printf("Creating a new todo item: %s%n", title);
    }
}

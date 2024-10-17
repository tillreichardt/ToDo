package com.picocli;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(name = "delete", description = "Delete a ToDo")
public class DeleteCommand implements Runnable{
    @Parameters(index = "0", description = "The ID of the todo item to delete")
    int id;

    @Override
    public void run() {
        System.out.printf("Deleting todo item with ID: %d%n", id);
    }
}

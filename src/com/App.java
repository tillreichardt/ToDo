package com;

import com.Connector.DatabaseConnector;

public class App {

    public static void main(String[] args) throws Exception {
        Navigation nv = new Navigation();
        nv.navigationLoop();
    }
}

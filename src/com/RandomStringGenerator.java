package com;

import java.util.Random;

public class RandomStringGenerator {

    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int STRING_LENGTH = 7;
    
    private Random random;

    public RandomStringGenerator() {
        this.random = new Random();
    }
    
    public String generate() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);    
        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    
    // Main method to test the generator.
    public static void main(String[] args) {
        RandomStringGenerator generator = new RandomStringGenerator();
        String randomString = generator.generate();
        System.out.println("Generated Random String: " + randomString);
    }
}

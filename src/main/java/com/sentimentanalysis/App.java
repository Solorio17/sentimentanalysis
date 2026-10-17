
package com.sentimentanalysis;

import java.io.InputStream;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Create our custom hash table
        HashTable<String, Integer> sentiments = new HashTable<>(16);

        // Load the sentiment ratings from the resource file
        InputStream file = App.class.getClassLoader()
                .getResourceAsStream("sentiments.txt");

        if (file == null) {
            System.out.println("Could not find sentiments.txt");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                // Separate the word or phrase from its rating
                String[] parts = line.split(",", 2);

                if (parts.length == 2) {
                    String word = parts[0].trim().toLowerCase();
                    int rating = Integer.parseInt(parts[1].trim());

                    sentiments.put(word, rating);
                }
            }
        }

        // Keep track of all words and sentiment scores
        int wordCount = 0;
        int totalSentiment = 0;
        String previousWord = "";

        Scanner input = new Scanner(System.in);

        System.out.println("Enter text to analyze (END to finish):");

        while (input.hasNextLine()) {
            String line = input.nextLine();

            // Stop reading when END is entered
            if (line.trim().equalsIgnoreCase("END")) {
                break;
            }

            // Remove punctuation and convert to lowercase
            String[] words = line.replaceAll("[^a-zA-Z ]", "")
                    .toLowerCase()
                    .trim()
                    .split("\\s+");

            for (String word : words) {
                if (word.isEmpty()) {
                    continue;
                }

                wordCount++;

                // Check individual word sentiment
                Integer score = sentiments.get(word);

                if (score != null) {
                    totalSentiment += score;
                }

                // Check two-word phrases using the previous word
                if (!previousWord.isEmpty()) {
                    String phrase = previousWord + " " + word;
                    Integer phraseScore = sentiments.get(phrase);

                    if (phraseScore != null) {
                        totalSentiment += phraseScore;
                    }
                }

                previousWord = word;
            }
        }

        // Calculate and display the final results
        double average = wordCount == 0
                ? 0.0
                : (double) totalSentiment / wordCount;

        System.out.println("Word count: " + wordCount);
        System.out.println("Total sentiment: " + totalSentiment);
        System.out.printf("Average sentiment: %.2f%n", average);

        input.close();
    }
}

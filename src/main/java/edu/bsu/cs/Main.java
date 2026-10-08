package edu.bsu.cs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a Wikipedia article title: ");
        String articleTitle = scanner.nextLine().trim();

        if (articleTitle.isEmpty()) {
            System.out.println("No page requested.");
            return;
        }

        try {
            WikipediaClient client = new WikipediaClient();

            InputStream response =
                    client.getArticleRevisions(articleTitle);

            WikipediaResult result =
                    new RevisionParser().parseResult(response);

            RevisionFormatter formatter = new RevisionFormatter();

            System.out.print(formatter.format(result));

        } catch (IOException | InterruptedException exception) {
            System.out.println("Network error.");
        }
    }
}
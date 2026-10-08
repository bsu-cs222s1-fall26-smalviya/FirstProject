package edu.bsu.cs;

public class RevisionFormatter {

    public String format(WikipediaResult result) {

        if (result.isMissing()) {
            return "No page found.";
        }

        StringBuilder output = new StringBuilder();

        if (result.isRedirect()) {
            output.append("Redirected.\n");
        }

        output.append("Recent changes:\n");

        int number = 1;

        for (Revision revision : result.getRevisions()) {
            output.append(number)
                    .append(". Username: ")
                    .append(revision.getUsername())
                    .append(" | Timestamp: ")
                    .append(revision.getTimestamp())
                    .append("\n");

            number++;
        }

        return output.toString();
    }
}
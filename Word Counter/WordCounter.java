import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class WordCounter {

    public static void main(String[] args) {
        String[] options = {"Enter text", "Provide a file path"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome to Word Counter!",
                "Word Counter",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        String text = "";
        if (choice == 0) {
            text = JOptionPane.showInputDialog("Enter text:");
        } else if (choice == 1) {
            String filePath = JOptionPane.showInputDialog("Enter file path:");
            text = getTextFromFile(filePath);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid option. Exiting.");
            System.exit(1);
        }

    
        WordCounterResult result = processText(text);
        displayResults(result);
    }

    private static String getTextFromFile(String filePath) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            if (java.nio.file.Files.isRegularFile(path)) {
                return new String(java.nio.file.Files.readAllBytes(path));
            } else {
                JOptionPane.showMessageDialog(null, "Invalid file path. Exiting.");
                System.exit(1);
            }
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return "";
    }

    private static WordCounterResult processText(String text) {
        
        String[] words = splitWords(text);
   
        int wordCount = countWords(words);

        text = removeStopWords(text);

        Map<String, Integer> wordFrequency = countWordFrequency(text);

        return new WordCounterResult(wordCount, wordFrequency);
    }

    private static String[] splitWords(String text) {
        return text.split("[\\s.,;?!]+");
    }

    private static int countWords(String[] words) {
        int count = 0;
        for (String word : words) {
            if (!word.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private static String removeStopWords(String text) {
        String[] stopWords = {"the", "and", "is", "in", "it", "to", "of", "for", "with"};

        for (String stopWord : stopWords) {
            text = text.replaceAll("\\b" + stopWord + "\\b", "");
        }

        return text;
    }

    private static Map<String, Integer> countWordFrequency(String text) {
        String[] words = text.split("\\s+");
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }
        return wordFrequency;
    }

    private static void displayResults(WordCounterResult result) {
        JOptionPane.showMessageDialog(
                null,
                "Results:\n" +
                        "Total words: " + result.getWordCount() + "\n" +
                        "Unique words: " + result.getWordFrequency().size() + "\n" +
                        "Word frequency:\n" + result.getWordFrequencyAsString());
    }

    private static class WordCounterResult {
        private final int wordCount;
        private final Map<String, Integer> wordFrequency;

        public WordCounterResult(int wordCount, Map<String, Integer> wordFrequency) {
            this.wordCount = wordCount;
            this.wordFrequency = wordFrequency;
        }

        public int getWordCount() {
            return wordCount;
        }

        public Map<String, Integer> getWordFrequency() {
            return wordFrequency;
        }

        public String getWordFrequencyAsString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            return sb.toString();
        }
    }
}

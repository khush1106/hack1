import java.util.*;
import java.io.*;
    public class TextSummarizer {
    
        public static void main(String[] args) throws Exception {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter file name:");
            String fileName = sc.nextLine();
            File inputFile = new File(fileName);
    
            if (!inputFile.exists()) {
                try (PrintWriter writer = new PrintWriter(fileName)) {
                    System.out.println("Enter text:");
                    while (true)
                     {
                        String line = sc.nextLine();
                        if (line.isEmpty()) {
                            break;
                        }
                        writer.println(line);
                    }
                }
            }
    
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String fileContent = sb.toString();
    
                System.out.println("Enter summary length:");
                int summaryLength = sc.nextInt();
                String summary = summarizeText(fileContent, summaryLength);
                try (PrintWriter writer = new PrintWriter("output.txt")) {
                    writer.println(summary);
                }
    
                System.out.println("Summary written");
            }
        }
        public static String summarizeText(String inputText, int summaryLength) {
            List<String> sentences = parseSentences(inputText);
            List<String> filteredWords = filterWords(sentences);
            Map<String, Integer> wordFrequencies = computeWordFrequencies(filteredWords);
            Map<String, Integer> sentenceScores = computeSentenceScores(sentences,wordFrequencies);
            List<String> topSentences = selectTopSentences(sentenceScores,summaryLength);
            String summary = generateSummary(topSentences);
            return summary;
        }
    private static List<String> parseSentences(String inputText) {
        List<String> sentences = new ArrayList<>();
    
        // Split the input text into sentences using regular expressions
        String[] sentenceArray = inputText.split("[.?!]\\s+");
        
        // Add each sentence to the sentences list
        for (String sentence : sentenceArray) {
            sentences.add(sentence);
        }
        
        return sentences;
    }

    private static List<String> filterWords(List<String> sentences) {
        List<String> filteredWords = new ArrayList<>();
    
        // Define a list of stop words to remove
        List<String> stopWords = Arrays.asList("a", "an", "the", "and", "or", "but", "is", "are", "am", "was", "were", "be", "been", "have", "has", "had", "do", "does", "did", "in", "on", "at", "to", "from", "by", "with", "as", "for", "of", "that", "this", "these", "those");
    
        // Iterate over each sentence
        for (String sentence : sentences) {
            // Remove punctuation and convert to lowercase
            String sanitized = sentence.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
            // Split the sentence into individual words
            String[] words = sanitized.split("\\s+");
            // Iterate over each word in the sentence
            for (String word : words) {
                // Check if the word is not a stop word
                if (!stopWords.contains(word)) {
                    // Add the word to the list of filtered words
                    filteredWords.add(word);
                }
            }
        }
        //System.out.println(filteredWords);
        return filteredWords;
    }
    private static Map<String, Integer> computeWordFrequencies(List<String> filteredWords) {
        Map<String, Integer> wordFrequencies = new HashMap<>();
        for (String word : filteredWords) {
            wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
        }
        return wordFrequencies;
    }
    private static Map<String, Integer> computeSentenceScores(List<String> sentences, Map<String, Integer> wordFrequencies) {
        Map<String, Integer> sentenceScores = new HashMap<>();
        int maxFrequency = getMaxFrequency(wordFrequencies);
    
        for (String sentence : sentences) {
            int score = 0;
            List<String> words = Arrays.asList(sentence.split(" "));
            for (String word : words) {
                if (wordFrequencies.containsKey(word)) {
                    score += wordFrequencies.get(word);
                }
            }
            // Give a boost to longer sentences
            score += Math.round(10 * ((float) words.size() / (float) maxFrequency));
            sentenceScores.put(sentence, score);
        }
    
        return sentenceScores;
    }
    
    private static int getMaxFrequency(Map<String, Integer> wordFrequencies) {
        int maxFrequency = 1;
        for (int frequency : wordFrequencies.values()) {
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
            }
        }
        return maxFrequency;
    }
    private static List<String> selectTopSentences(Map<String, Integer> sentenceScores, int numTopSentences)
     {
        List<String> topSentences = new ArrayList<>();
        // Sort the sentences by score in descending order
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(sentenceScores.entrySet());
        Collections.sort(sortedScores, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });
    
        // Add the top n sentences to the list
        for (int i = 0; i < numTopSentences && i < sortedScores.size(); i++) {
            topSentences.add(sortedScores.get(i).getKey());
        }
    
        return topSentences;
    }    
    private static String generateSummary(List<String> topSentences) 
    {
        StringBuilder summaryBuilder = new StringBuilder();
        // Combine the top sentences into a summary string
        for (String sentence : topSentences) {
            summaryBuilder.append(sentence);
            summaryBuilder.append(". ");
        }
        String summary = summaryBuilder.toString().trim();
        // Capitalize the first letter of the summary
        summary = summary.substring(0, 1).toUpperCase() + summary.substring(1);
        // Add a period to the end of the summary if it doesn't already have one
        if (!summary.endsWith(".")) {
            summary += ".";
        }
        return summary;
    }
}

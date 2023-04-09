# hack1
electrothon project

It can handle large amounts of text and summarize it quickly and accurately.
The program can be customized to produce summaries of different lengths, depending on your needs.
The summarized text can be easily copied and pasted into other documents or applications.
The program is easy to use and requires no special technical knowledge.

the java program was a basic first attempt of the project 

it involves a main function and few other methodslike

summarizeText-the method which takes in the input and use call other methods for the functioning

parseSentences-takes in the input and filter out sentences basically on the basis of puntuation like "." "?"

 filterWords-it takes sentences returned and return words removing the filtered words callled as stop words

computeWordFrequencies-it takes filterword as a parameter and compute the frequencies of the word

getMaxFrequency-using hashmap technique we arrange the sentences in form of ascending order of frequencies

selectTopSentences-based on the frequency of maximum filter words it  returns the sentences as asked by user 

generateSummary-based on the generated top sentences it arranges the sentences in order of paragraph sentences

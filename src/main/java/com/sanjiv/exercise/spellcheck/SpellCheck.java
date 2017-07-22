package com.sanjiv.exercise.spellcheck;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sanjiv on 7/22/17.
 */
public class SpellCheck {
    private static final Logger logger = LoggerFactory.getLogger(SpellCheck.class);

    private static final String RESOURCES_PATH = "resources";
    private static final String DICTIONARY_FILENAME = "US.dic";
    private SpellChecker spellChecker;

    public enum SpellingStatus {
        CORRECT,
        INCORRECT;
    }

    public SpellCheck() throws IOException, URISyntaxException {
        init();
    }

    private void init() throws IOException, URISyntaxException {
        initSpellChecker();
    }

    /**
     * This method checks the spelling for the specified word.  If the spelling is found to be correct then it will
     * return SpellingStatus.CORRECT else it will return SpellingStatus.INCORRECT.
     * @param word
     * @return SpellingStatus.CORRECT if spelling is correct else SpellingStatus.INCORRECT
     * @throws IOException when error occurs trying to access dictionary.
     */
    public SpellingStatus checkSpelling(String word) throws IOException {
        return (spellChecker.exist(word) ? SpellingStatus.CORRECT : SpellingStatus.INCORRECT);
    }

    /**
     * The suggest method finds suggestions for the specified word for the specified numberOfSuggestions and returns an
     * array of suggestion strings.
     *
     * @param word
     * @param numberOfSuggestions
     * @return String[] of suggestions.
     * @throws IOException
     */
    public String[] suggest(String word, int numberOfSuggestions) throws IOException {
        String[] suggestions = spellChecker.suggestSimilar(word, numberOfSuggestions);

        return suggestions;
    }

    private void initSpellChecker() throws IOException, URISyntaxException {
        Directory directory = FSDirectory.open(Paths.get(RESOURCES_PATH));
        spellChecker = new SpellChecker(directory);

        IndexWriterConfig config = new IndexWriterConfig();

        URL resource = SpellCheck.class.getClassLoader().getResource(DICTIONARY_FILENAME);
        Path path = Paths.get(resource.toURI());
        spellChecker.indexDictionary(new PlainTextDictionary(path), config, false);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        // NOTE: See Unit tests under test folder.
        SpellCheck spellCheck = new SpellCheck();

        logger.info("Check spelling for word [aaa]: " + spellCheck.checkSpelling("aaa"));
        logger.info("Check spelling for word [sanjiv]: " + spellCheck.checkSpelling("sanjiv"));
        logger.info("Check spelling for word [god]: " + spellCheck.checkSpelling("god"));

        logger.info("Suggestions: " + Arrays.toString(spellCheck.suggest("misspelt", 5)));
        logger.info("Suggestions: " + Arrays.toString(spellCheck.suggest("apropos", 5)));
        logger.info("Suggestions: " + Arrays.toString(spellCheck.suggest("aaa", 5)));
        logger.info("Suggestions: " + Arrays.toString(spellCheck.suggest("sound", 5)));
    }
}

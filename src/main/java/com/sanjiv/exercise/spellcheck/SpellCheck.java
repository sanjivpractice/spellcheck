package com.sanjiv.exercise.spellcheck;

import java.io.File;
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

/**
 * @author Sanjiv on 7/22/17.
 */
public class SpellCheck {

    private static final String RESOURCES_PATH = "resources";
    private static final String DICTIONARY_FILENAME = "US.dic";
    private File dictionary;
    private SpellChecker spellChecker;

    public SpellCheck() throws IOException, URISyntaxException {
        init();
    }

    private void init() throws IOException, URISyntaxException {
        loadDictionary();

        initSpellChecker();
    }

    public void suggest(String word, int numberOfSuggestions) throws IOException {
        String[] suggestions = spellChecker.suggestSimilar("misspelt", numberOfSuggestions);
        System.out.println("Suggestions: " + Arrays.toString(suggestions));
    }

    private void initSpellChecker() throws IOException, URISyntaxException {
        Directory directory = FSDirectory.open(Paths.get(RESOURCES_PATH));
        spellChecker = new SpellChecker(directory);

        IndexWriterConfig config = new IndexWriterConfig();//Version.LUCENE_6_6_0, null);
//        spellChecker.indexDictionary(new PlainTextDictionary(Paths.get(     "resources/US.dic")), config, false);

        URL resource = SpellCheck.class.getClassLoader().getResource("US.dic");
        Path path = Paths.get(resource.toURI());
        spellChecker.indexDictionary(new PlainTextDictionary(path), config, false);
    }

    private void loadDictionary() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        dictionary = new File(classLoader.getResource(DICTIONARY_FILENAME).getFile());
    }



    public static void main(String[] args) throws IOException, URISyntaxException {

        SpellCheck spellCheck = new SpellCheck();
        spellCheck.suggest("misspelt", 5);
        spellCheck.suggest("apropos", 5);
    }
}

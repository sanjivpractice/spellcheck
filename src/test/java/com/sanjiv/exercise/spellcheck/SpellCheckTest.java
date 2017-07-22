package com.sanjiv.exercise.spellcheck;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Sanjiv on 7/22/17.
 */
public class SpellCheckTest {

    private SpellCheck spellCheck;

    @Before
    public void setup() throws IOException, URISyntaxException {
        spellCheck = new SpellCheck();
    }

    @Test
    public void testSpellingForWord_sound_ShouldReturnCORRECT() throws IOException {
        assertEquals(SpellCheck.SpellingStatus.CORRECT, spellCheck.checkSpelling("sound"));
    }

    @Test
    public void testSpellingForWord_sanjiv_ShouldReturnINCORRECT() throws IOException {
        assertEquals(SpellCheck.SpellingStatus.INCORRECT, spellCheck.checkSpelling("sanjiv"));
    }

    @Test
    public void testRequestTwoSuggestionsForWord_misspelt_ShouldReturnTwoSuggestions() throws IOException {
        String[] suggestions = spellCheck.suggest("misspelt", 2);
        assertEquals(2, suggestions.length);
    }
}

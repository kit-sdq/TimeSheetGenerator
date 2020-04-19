package etc;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import etc.ContextStringReplacer.ContextStringReplacerIterator;
import etc.ContextStringReplacer.ContextStringReplacerIterator.ContextStringReplacement;

public class ContextStringReplacerTest {
    
    // NOTE:
    // the static replace method is a convenience method and uses the foreach iterator internally,
    // which is why the tests are performed for the foreach iterator only
    
    @Test
    public void testEmptyString() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("", Arrays.asList("abc"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("xyz");
        }
        String result = replacer.getString();
        // assert
        assertEquals("", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptySubstring() {
        // execute
        new ContextStringReplacer("Hello World", Arrays.asList(""));
    }
    
    @Test
    public void testStringNotContainsSubstring() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("abc"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("xyz");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testStringContainsSubstringNoIterator() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        String result = replacer.getString();
        // assert
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testStringContainsSubstringNoReplace() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        for (@SuppressWarnings("unused") ContextStringReplacement replacement : replacer) {
            // do nothing
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testStringContainsSubstringReplace() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("ooo+W");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hellooo+World", result);
    }
    
    @Test
    public void testStringContainsSubstringReplaceEmpty() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hellorld", result);
    }
    
    @Test
    public void testStringContainsSubstringReplaceContainsSubstring() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("ooo W");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hellooo World", result);
    }
    
    @Test
    public void testStringContainsSubstringReplaceContainsSubstringMultipleTimes() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("ooo W");
        }
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("ooo W");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hellooooo World", result);
    }
    
    @Test
    public void testStringContainsSubstringMultipleReplace() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("l"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("_");
        }
        String result = replacer.getString();
        // assert
        assertEquals("He__o Wor_d", result);
    }
    
    @Test
    public void testStringContainsSubstringMultipleReplaceContainsSubstring() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("l"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("<l>");
        }
        String result = replacer.getString();
        // assert
        assertEquals("He<l><l>o Wor<l>d", result);
    }
    
    @Test
    public void testStringContainsSubstringMultipleReplaceFirst() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("l"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("_");
            break;
        }
        String result = replacer.getString();
        // assert
        assertEquals("He_lo World", result);
    }
    
    @Test
    public void testStringContainsOneOfMultipleSubstrings() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("e", "x"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("_");
        }
        String result = replacer.getString();
        // assert
        assertEquals("H_llo World", result);
    }
    
    @Test
    public void testStringContainsMultipleSubstrings() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("e", "r"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("_");
        }
        String result = replacer.getString();
        // assert
        assertEquals("H_llo Wo_ld", result);
    }
    
    @Test
    public void testStringContainsMultipleSubstringsReplaceDifferent() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("e", "r"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            if (replacement.getSubstring().equals("e"))
                replacement.replace("_");
            else
                replacement.replace("-");
        }
        String result = replacer.getString();
        // assert
        assertEquals("H_llo Wo-ld", result);
    }
    
    @Test
    public void testStringContainsMultipleSubstringsReplaceEdgeCases() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("H", "d"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("_");
        }
        String result = replacer.getString();
        // assert
        assertEquals("_ello Worl_", result);
    }
    
    @Test
    public void testStringContainsMultipleSubstringsEqualPrefixShorterSubstringFirst() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("ll", "llo"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            assertEquals("ll", replacement.getSubstring());
        }
    }
    
    @Test
    public void testStringContainsMultipleSubstringsEqualPrefixLongerSubstringFirst() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("llo", "ll"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            assertEquals("llo", replacement.getSubstring());
        }
    }
    
    @Test
    public void testStringContainsOverlappingSubstringsReplaceDifferent() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello Wooorld", Arrays.asList("oo"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("__");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hello W__orld", result);
    }
    
    @Test
    public void testStringContainsOverlappingSubstringsReplaceMatch() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello Wooorld", Arrays.asList("oo"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.replace("_o");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hello W_oorld", result);
    }
    
    @Test
    public void testStringContainsOverlappingSubstringsIgnoreFirst() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello Wooorld", Arrays.asList("oo"));
        // execute
        int c = 0;
        for (ContextStringReplacement replacement : replacer) {
            if (c++ > 0)
                replacement.replace("o_");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hello Woo_rld", result);
    }
    
    @Test
    public void testStringContainsSubstringSkip() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.skip();
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testStringContainsMultipleSubstringsSkipAll() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("l"));
        // execute
        for (ContextStringReplacement replacement : replacer) {
            replacement.skip();
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testStringContainsMultipleSubstringsSkipFirst() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("l"));
        // execute
        int c = 0;
        for (ContextStringReplacement replacement : replacer) {
            if (c++ == 0)
                replacement.skip();
            else
                replacement.replace("_");
        }
        String result = replacer.getString();
        // assert
        assertEquals("Hel_o Wor_d", result);
    }
    
    @Test
    public void testStringContainsOverlappingSubstringsNoSkip() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello Wooorld", Arrays.asList("oo"));
        // execute
        int c = 0;
        for (@SuppressWarnings("unused") ContextStringReplacement replacement : replacer) {
            c++;
        }
        // assert
        assertEquals(2, c);
    }
    
    @Test
    public void testStringContainsOverlappingSubstringsSkip() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello Wooorld", Arrays.asList("oo"));
        // execute
        int c = 0;
        for (ContextStringReplacement replacement : replacer) {
            replacement.skip();
            c++;
        }
        // assert
        assertEquals(1, c);
    }
    
    @Test
    public void testStringContainsMultipleSubstringsGetIndex() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("l"));
        // assert
        int c = 0;
        for (ContextStringReplacement replacement : replacer) {
            if (c == 0)
                assertEquals(2, replacement.getIndex());
            else if (c == 1)
                assertEquals(3, replacement.getIndex());
            else
                assertEquals(9, replacement.getIndex());
            
            c++;    
        }
    }
    
    @Test
    public void testStringContainsMultipleSubstringsReplaceGetIndex() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("l"));
        // assert
        int c = 0;
        for (ContextStringReplacement replacement : replacer) {
            if (c == 0)
                assertEquals(2, replacement.getIndex());
            else if (c == 1)
                assertEquals(5, replacement.getIndex());
            else
                assertEquals(13, replacement.getIndex());
            
            replacement.replace("<l>");
            c++;
        }
    }
    
    @Test
    public void testLookbehindLookaheadSize0() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            assertEquals("", replacement.getLookbehind(0));
            assertEquals("", replacement.getLookahead(0));
        }
    }
    
    @Test
    public void testLookbehindLookaheadSize1() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            assertEquals("l", replacement.getLookbehind(1));
            assertEquals("o", replacement.getLookahead(1));
        }
    }
    
    @Test
    public void testLookbehindLookaheadSizeGreater1() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            assertEquals("ell", replacement.getLookbehind(3));
            assertEquals("orl", replacement.getLookahead(3));
        }
    }
    
    @Test
    public void testLookbehindLookaheadMultiple() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o"));
        // assert
        int c = 0;
        for (ContextStringReplacement replacement : replacer) {
            if (c++ == 0) {
                assertEquals("l", replacement.getLookbehind(1));
                assertEquals(" ", replacement.getLookahead(1));
            } else {
                assertEquals("W", replacement.getLookbehind(1));
                assertEquals("r", replacement.getLookahead(1));
            }
        }
    }
    
    @Test
    public void testLookbehindLookaheadEdgeCases() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("ello", "Worl"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            if (replacement.getSubstring().equals("ello"))
                assertEquals("H", replacement.getLookbehind(1));
            else
                assertEquals("d", replacement.getLookahead(1));
        }
    }
    
    @Test
    public void testLookbehindLookaheadOverflow() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("llo", "Wor"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            if (replacement.getSubstring().equals("llo"))
                assertEquals("He", replacement.getLookbehind(3));
            else
                assertEquals("ld", replacement.getLookahead(3));
        }
    }
    
    @Test
    public void testLookbehindLookaheadOverflowEdgeCases() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("Hello", "World"));
        // assert
        for (ContextStringReplacement replacement : replacer) {
            if (replacement.getSubstring().equals("Hello"))
                assertEquals("", replacement.getLookbehind(3));
            else
                assertEquals("", replacement.getLookahead(3));
        }
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAccessStoredReplacementAfterNewReplacement() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        // execute
        ContextStringReplacement storedReplacement = null;
        for (ContextStringReplacement replacement : replacer) {
            storedReplacement = replacement;
        }
        storedReplacement.replace("ooo W");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAccessStoredIteratorAfterNewIterator() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        
        ContextStringReplacerIterator storedIterator = replacer.iterator();
        replacer.iterator();
        // execute
        storedIterator.next();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAccessStoredReplacementAfterNewIterator() {
        // data
        ContextStringReplacer replacer = new ContextStringReplacer("Hello World", Arrays.asList("o W"));
        
        ContextStringReplacerIterator storedIterator = replacer.iterator();
        ContextStringReplacement storedReplacement = storedIterator.next();
        replacer.iterator();
        // execute
        storedReplacement.replace("ooo W");
    }
    
    @Test
    public void testStaticReplaceFunction() {
        // execute
        String result = ContextStringReplacer.replace("Hello World", Arrays.asList("l", "o"), (replacement) -> {
            if (replacement.getSubstring().equals("l"))
                replacement.replace("_");
            else if (replacement.getLookahead(1).equals(" "))
                replacement.replace("ooo");
        });
        // assert
        assertEquals("He__ooo Wor_d", result);
    }
    
}

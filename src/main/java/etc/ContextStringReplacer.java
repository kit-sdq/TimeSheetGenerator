package etc;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import i18n.ResourceHandler;

/**
 * Provides functionality to replace substrings in a string.
 * The substrings are found by matching against a given collection of substrings.
 * The substrings are used in the order of the collection to find a match.
 * <br><br>
 * When iterating over a <code>ContextStringReplacer</code> object,
 * a <code>ContextStringReplacement</code> is provided for each found substring.
 * The <code>ContextStringReplacement</code> contains information about the found substring
 * as well as the characters before (lookbehind) and after (lookahead) the found substring.
 * <br><br>
 * The replacement for the found substring is then specified by calling the <code>replace</code> method
 * of the <code>ContextStringReplacement</code> object.
 * The replacement as part of the edited string is not searched for matching substrings again.
 * <br><br>
 * If <code>replace</code> is not called on the <code>ContextStringReplacement</code> object,
 * then the next substring is searched beginning at the next character of the string.
 * If the next substring should be searched beginning after the found substring, then <code>skip</code> can be called,
 * which behaves like <code>replace(getSubstring())</code>.
 * <br><br>
 * The static <code>replace</code> method of the <code>ContextStringReplacer</code> is added for convenience
 * and allows to express a replacement in one line of code.
 * <br><br>
 * Example using a for each loop:
 * <pre>
 * String s = "Hello World";
 * ContextStringReplacer replacer = new ContextStringReplacer(s, Arrays.asList("o"));
 * 
 * for (ContextStringReplacement replacement : replacer) {
 *   if (replacement.getLookahead(1).equals(" ")) {
 *     replacement.replace("ooo");
 *   } else if (replacement.getLookbehind(1).equals("W")) {
 *     replacement.replace("0");
 *   }
 * }
 * 
 * s = replacer.getString();
 * assert(s.equals("Hellooo W0rld"));
 * </pre>
 * Example using the static replace method:
 * <pre>
 * String s = "Hello World";
 * s = ContextStringReplacer.replace(s, Arrays.asList("o"), (replacement) -> {
 *   if (replacement.getLookahead(1).equals(" ")) {
 *     replacement.replace("ooo");
 *   } else if (replacement.getLookbehind(1).equals("W")) {
 *     replacement.replace("0");
 *   }
 * });
 * 
 * assert(s.equals("Hellooo W0rld"));
 * </pre>
 * Warning: <code>ContextStringReplacerIterator</code> and <code>ContextStringReplacement</code> objects are single-use only and should not be stored.
 * <br><br>
 * Warning: This class is not thread safe.
 */
public class ContextStringReplacer implements Iterable<ContextStringReplacer.ContextStringReplacerIterator.ContextStringReplacement> {

    /**
     * Create a new <code>ContextStringReplacer</code> for a string and a collection of strings to replace.
     * The collection of strings to replace may not contain empty strings.
     * 
     * @param string String that will be edited
     * @param replace Collection of substrings to replace
     */
    public ContextStringReplacer(String string, Collection<String> replace) {
        for (String substring : replace) {
            if (substring.isEmpty())
                throw new IllegalArgumentException(ResourceHandler.getMessage("error.contextstringreplacer.emptyReplaceString"));
        }
        
        this.string = string;
        this.replace = replace;
        
        currentIterator = null;
    }

    /**
     * Edited string; this variable is changed when replacing substrings
     */
    private String string;
    /**
     * Collection of substrings to replace
     */
    private final Collection<String> replace;
    
    /**
     * Iterator issued to the caller last
     */
    private ContextStringReplacerIterator currentIterator;

    /**
     * Get the edited string
     * 
     * @return Edited string
     */
    public String getString() {
        return string;
    }

    @Override
    public ContextStringReplacerIterator iterator() {
        if (currentIterator != null)
            currentIterator.invalidate();
        
        currentIterator = new ContextStringReplacerIterator();
        return currentIterator;
    }

    /**
     * Call <code>action</code> for every occurence of every substring in <code>replace</code>
     * and return the result after all replacements
     * 
     * @param string String to edit
     * @param replace Collection of substrings to replace
     * @param action Action to be called with the found substrings, specifies the replacement
     * 
     * @return Edited string
     */
    public static String replace(
        String string,
        Collection<String> replace,
        Consumer<? super ContextStringReplacer.ContextStringReplacerIterator.ContextStringReplacement> action
    ) {
        ContextStringReplacer replacer = new ContextStringReplacer(string, replace);

        replacer.forEach(action);

        return replacer.getString();
    }

    /**
     * Iterator for the <code>ContextStringReplacer</code> class.
     * Provides the <code>ContextStringReplacement</code> objects for each found occurence of a substring.
     */
    public class ContextStringReplacerIterator implements Iterator<ContextStringReplacerIterator.ContextStringReplacement> {

        /**
         * Create a new <code>ContextStringReplacerIterator</code> to iterate over all found substrings
         */
        public ContextStringReplacerIterator() {
            currentIndex = 0;

            currentReplacement = null;
            nextReplacement = null;
            
            invalid = false;
        }

        /**
         * Current index during the replacement.
         * Everything before this index is already edited.
         */
        private int currentIndex;

        /**
         * Replacement object issued to the caller last
         */
        private ContextStringReplacement currentReplacement;
        /**
         * Next replacement object to issue
         */
        private ContextStringReplacement nextReplacement;
        
        /**
         * If this iterator is invalidated.
         * The iterator is invalidated when a new iterator is created.
         */
        private boolean invalid;
        
        /**
         * Invalidates this <code>ContextStringReplacerIterator</code> object
         */
        private void invalidate() {
            invalid = true;
            
            if (currentReplacement != null)
                currentReplacement.invalidate();
        }

        /**
         * Find the next substring to replace and return the corresponding <code>ContextStringReplacement</code> object.
         * Caches information in <code>nextReplacement</code>.
         * 
         * @return <code>ContextStringReplacement</code> object for the next substring to replace
         */
        private ContextStringReplacement findNext() {
            // use the cached replacement if possible
            if (nextReplacement != null)
                return nextReplacement;

            // invalidate the current replacement (necessary to guarantee that the index found here will be valid)
            if (currentReplacement != null)
                currentReplacement.invalidate();

            int nextIndex;
            String nextSubstring;
                
            search : {
                // search for the next index going from currentIndex
                for (int i = currentIndex; i < string.length(); i++) {
                    // for each index, check if any replace substring matches
                    for (String substring : replace) {
                        // but only if we have enough characters left to match
                        if (string.length() - i >= substring.length()) {
                            // check if the substring matches with substring
                            String subString = string.substring(i, i + substring.length());
                            if (subString.equals(substring)) {
                                // found next match
                                nextIndex = i;
                                nextSubstring = substring;

                                // continue after the replacement next time
                                currentIndex = nextIndex + 1;

                                break search;
                            }
                        }
                    }
                }

                // unnecessary to search the string again
                currentIndex = string.length();
                
                return null;
            }

            nextReplacement =  new ContextStringReplacement(nextIndex, nextSubstring);
            return nextReplacement;
        }

        /**
         * Reset the cache for the next substring to replace.
         * This causes <code>findNext</code> to actually search for the next substring instead of using the cached value.
         */
        private void resetNext() {
            nextReplacement = null;
        }

        @Override
        public boolean hasNext() {
            if (invalid)
                throw new IllegalStateException();
            
            return findNext() != null;
        }

        @Override
        public ContextStringReplacement next() {
            if (invalid)
                throw new IllegalStateException();

            // find the next replacement (possibly cached)
            currentReplacement = findNext();
            if (currentReplacement == null)
                throw new NoSuchElementException();

            // search for a new replacement next time
            resetNext();

            // return the current replacement
            return currentReplacement;
        }

        /**
         * Provides information about a found substring including its context
         * and functionality to replace the found substring.
         * <br><br>
         * Replacing or skipping the substring invalidates the information in the object,
         * causing an IllegalStateException when calling any method after <code>replace</code>.
         */
        public class ContextStringReplacement {

            /**
             * Create a new ContextStringReplacement for a found substring
             * 
             * @param index Index of the found substring in the string
             * @param substring Found substring
             */
            public ContextStringReplacement(int index, String substring) {
                this.index = index;
                this.substring = substring;

                invalid = false;
            }
    
            /**
             * Index of the found substring
             */
            private final int index;
            /**
             * Found substring; this is an element of the <code>replace</code> collection
             */
            private final String substring;

            /**
             * If this replacement is invalidated.
             * The replacement is invalidated when replacing or skipping the found substring
             * or moving on to a new replacement or iterator.
             */
            private boolean invalid;
            
            /**
             * Invalidates this <code>ContextStringReplacement</code> object
             */
            private void invalidate() {
                invalid = true;
            }
            
            /**
             * Get the index of the found substring
             * 
             * @return Index of found substring
             */
            public int getIndex() {
                if (invalid)
                    throw new IllegalStateException();
                
                return index;
            }

            /**
             * Get the found substring
             * 
             * @return Found substring
             */
            public String getSubstring() {
                if (invalid)
                    throw new IllegalStateException();

                return substring;
            }

            /**
             * Get the lookahead of the found substring.
             * If the part of the string after the found substring is too short for the requested size,
             * then the lookahead contains all characters to the end of the string.
             * 
             * @param size Size (in characters) of the lookahead
             */
            public String getLookahead(int size) {
                if (invalid)
                    throw new IllegalStateException();

                int start = index + substring.length();
                int end = Math.min(start + size, string.length());

                return string.substring(start, end);
            }

            /**
             * Get the lookbehind of the found substring.
             * If the part of the string before the found substring is too short for the requested size,
             * then the lookbehind contains all characters from the beginning of the string.
             */
            public String getLookbehind(int size) {
                if (invalid)
                    throw new IllegalStateException();

                int end = index;
                int start = Math.max(end - size, 0);

                return string.substring(start, end);
            }
            
            /**
             * Skip the replacement and continue after the found substring.
             * This is different from not calling any method,
             * which would continue the search at the next character.
             * Calling this method invalidates the <code>ContextStringReplacement</code> object.
             */
            public void skip() {
                if (invalid)
                    throw new IllegalStateException();
                    
                currentIndex = index + substring.length();
                
                invalidate();
            }

            /**
             * Replace the found substring with the given replacement string.
             * Calling this method invalidates the <code>ContextStringReplacement</code> object.
             * 
             * @param replacement Replacement for the found substring
             */
            public void replace(String replacement) {
                if (invalid)
                    throw new IllegalStateException();

                String before = string.substring(0, index);
                String after = string.substring(index + substring.length(), string.length());

                string = before + replacement + after;
                currentIndex = index + replacement.length();

                invalidate();
            }
    
        }

    }

}

package net.daifo.decorator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Decorator Pattern Example
 *
 * Scenario: A tiny text-processing pipeline for outbound messages. The core
 * operation is "turn an input string into an output string" — defined by the
 * {@link TextTransformer} interface. We want to stack behaviors at runtime
 * without exploding the class hierarchy:
 *
 *     - Trim surrounding whitespace
 *     - Collapse internal runs of whitespace
 *     - Censor a forbidden word
 *     - Wrap lines at a fixed column width
 *     - Prefix a timestamp-style tag
 *     - Base64-encode the final payload
 *
 * Each decorator wraps another {@code TextTransformer} and adds one concern,
 * so callers can compose any subset in any order:
 *
 *     new Base64Encoder(new TagPrefix("INFO",
 *         new WordCensor("secret", new WhitespaceNormalizer(new Identity()))));
 *
 * Compare this to subclassing: N behaviors would require up to 2^N subclasses.
 */
public class DecoratorExample {

    // ---------- Component ----------
    public interface TextTransformer {
        String transform(String input);
    }

    // ---------- Concrete Component ----------
    /** Identity transformer — the "base" of any decorator stack. */
    public static class Identity implements TextTransformer {
        @Override
        public String transform(String input) {
            return input == null ? "" : input;
        }
    }

    // ---------- Base Decorator ----------
    public abstract static class TextDecorator implements TextTransformer {
        protected final TextTransformer inner;

        protected TextDecorator(TextTransformer inner) {
            if (inner == null) {
                throw new IllegalArgumentException("inner transformer required");
            }
            this.inner = inner;
        }
    }

    // ---------- Concrete Decorators ----------
    public static class WhitespaceNormalizer extends TextDecorator {
        public WhitespaceNormalizer(TextTransformer inner) { super(inner); }

        @Override
        public String transform(String input) {
            String s = inner.transform(input).trim();
            // collapse runs of spaces/tabs/newlines into a single space
            return s.replaceAll("\\s+", " ");
        }
    }

    public static class WordCensor extends TextDecorator {
        private final String forbidden;

        public WordCensor(String forbidden, TextTransformer inner) {
            super(inner);
            if (forbidden == null || forbidden.isEmpty()) {
                throw new IllegalArgumentException("forbidden word required");
            }
            this.forbidden = forbidden;
        }

        @Override
        public String transform(String input) {
            String s = inner.transform(input);
            StringBuilder mask = new StringBuilder(forbidden.length());
            for (int i = 0; i < forbidden.length(); i++) mask.append('*');
            // case-insensitive, word-boundary replace
            return s.replaceAll("(?i)\\b" + java.util.regex.Pattern.quote(forbidden) + "\\b",
                                mask.toString());
        }
    }

    public static class LineWrapper extends TextDecorator {
        private final int width;

        public LineWrapper(int width, TextTransformer inner) {
            super(inner);
            if (width <= 0) throw new IllegalArgumentException("width must be > 0");
            this.width = width;
        }

        @Override
        public String transform(String input) {
            String s = inner.transform(input);
            StringBuilder out = new StringBuilder(s.length() + s.length() / width);
            int col = 0;
            for (String word : s.split(" ")) {
                if (word.isEmpty()) continue;
                if (col == 0) {
                    out.append(word);
                    col = word.length();
                } else if (col + 1 + word.length() <= width) {
                    out.append(' ').append(word);
                    col += 1 + word.length();
                } else {
                    out.append('\n').append(word);
                    col = word.length();
                }
            }
            return out.toString();
        }
    }

    public static class TagPrefix extends TextDecorator {
        private final String tag;

        public TagPrefix(String tag, TextTransformer inner) {
            super(inner);
            this.tag = tag;
        }

        @Override
        public String transform(String input) {
            return "[" + tag + "] " + inner.transform(input);
        }
    }

    public static class Base64Encoder extends TextDecorator {
        public Base64Encoder(TextTransformer inner) { super(inner); }

        @Override
        public String transform(String input) {
            String s = inner.transform(input);
            return Base64.getEncoder()
                    .encodeToString(s.getBytes(StandardCharsets.UTF_8));
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        String raw = "   Hello    world, this is a    SECRET   memo   about   "
                   + "the new quarterly plan and roadmap items.   ";

        // Stack #1: clean + censor + wrap + tag
        TextTransformer human = new TagPrefix("MEMO",
                new LineWrapper(24,
                        new WordCensor("secret",
                                new WhitespaceNormalizer(
                                        new Identity()))));

        System.out.println("-- Human-readable --");
        System.out.println(human.transform(raw));

        // Stack #2: same cleanup, but encoded for transport (no wrap, no tag)
        TextTransformer wire = new Base64Encoder(
                new WordCensor("secret",
                        new WhitespaceNormalizer(
                                new Identity())));

        System.out.println();
        System.out.println("-- Wire format --");
        System.out.println(wire.transform(raw));

        // Stack #3: fully loaded — notice the order matters.
        // Here we encode AFTER tagging, so the tag is inside the payload.
        TextTransformer both = new Base64Encoder(
                new TagPrefix("MEMO",
                        new WordCensor("secret",
                                new WhitespaceNormalizer(
                                        new Identity()))));

        System.out.println();
        System.out.println("-- Tagged + encoded --");
        System.out.println(both.transform(raw));
    }
}

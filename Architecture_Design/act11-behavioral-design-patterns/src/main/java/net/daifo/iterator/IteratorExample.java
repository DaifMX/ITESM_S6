package net.daifo.iterator;

import java.util.NoSuchElementException;

/**
 * Iterator Pattern Example
 *
 * Scenario: A media playlist holds {@link Track} objects stored internally as
 * a fixed-size circular array (ring buffer). We want clients to traverse the
 * playlist sequentially without exposing the ring-buffer internals.
 *
 * Two iterator flavors are provided:
 *   - {@link LinearIterator}  : traverses the playlist once from start to end.
 *   - {@link LoopingIterator} : cycles through indefinitely (simulates repeat).
 *
 * The {@link Playlist} aggregate exposes both via factory methods, so the
 * client never depends on the concrete iterator class.
 */
public class IteratorExample {

    // ---------- Element ----------
    public static class Track {
        private final String title;
        private final int durationSec;

        public Track(String title, int durationSec) {
            this.title = title;
            this.durationSec = durationSec;
        }

        @Override
        public String toString() {
            return "\"" + title + "\" (" + durationSec + "s)";
        }
    }

    // ---------- Iterator interface ----------
    public interface TrackIterator {
        boolean hasNext();
        Track next();
    }

    // ---------- Aggregate ----------
    public static class Playlist {
        private final Track[] tracks;
        private int size = 0;

        public Playlist(int capacity) {
            this.tracks = new Track[capacity];
        }

        public void add(Track t) {
            if (size == tracks.length) throw new IllegalStateException("Playlist full");
            tracks[size++] = t;
        }

        public TrackIterator linearIterator() {
            return new LinearIterator(tracks, size);
        }

        public TrackIterator loopingIterator(int maxPlays) {
            return new LoopingIterator(tracks, size, maxPlays);
        }
    }

    // ---------- Concrete iterators ----------
    public static class LinearIterator implements TrackIterator {
        private final Track[] tracks;
        private final int size;
        private int cursor = 0;

        public LinearIterator(Track[] tracks, int size) {
            this.tracks = tracks;
            this.size = size;
        }

        @Override
        public boolean hasNext() { return cursor < size; }

        @Override
        public Track next() {
            if (!hasNext()) throw new NoSuchElementException();
            return tracks[cursor++];
        }
    }

    public static class LoopingIterator implements TrackIterator {
        private final Track[] tracks;
        private final int size;
        private final int maxPlays;
        private int cursor = 0;
        private int plays = 0;

        public LoopingIterator(Track[] tracks, int size, int maxPlays) {
            this.tracks = tracks;
            this.size = size;
            this.maxPlays = maxPlays;
        }

        @Override
        public boolean hasNext() {
            return size > 0 && plays < maxPlays * size;
        }

        @Override
        public Track next() {
            if (!hasNext()) throw new NoSuchElementException();
            Track t = tracks[cursor % size];
            cursor++;
            plays++;
            return t;
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        Playlist playlist = new Playlist(5);
        playlist.add(new Track("Bohemian Rhapsody", 354));
        playlist.add(new Track("Hotel California", 391));
        playlist.add(new Track("Stairway to Heaven", 482));

        System.out.println("-- Linear playback --");
        TrackIterator linear = playlist.linearIterator();
        while (linear.hasNext()) {
            System.out.println("  Playing: " + linear.next());
        }

        System.out.println("\n-- Looping (2 repeats) --");
        TrackIterator looping = playlist.loopingIterator(2);
        while (looping.hasNext()) {
            System.out.println("  Playing: " + looping.next());
        }
    }
}

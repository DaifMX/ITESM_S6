package net.daifo.proxy;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Proxy Pattern Example
 *
 * Scenario: A document archive exposes the {@link DocumentStore} interface.
 * The real backing store ({@link RemoteDocumentStore}) simulates a slow,
 * expensive remote service — every read pretends to take a round-trip and
 * every write "uploads" the payload.
 *
 * We wrap it with three proxies, each adding one cross-cutting concern
 * without changing the real subject or the client:
 *
 *     1) {@link ProtectionProxy}  — access control (who can read/write what)
 *     2) {@link CachingProxy}     — memoizes reads to avoid repeated round-trips
 *     3) {@link LoggingProxy}     — records every call for audit
 *
 * Because every proxy implements the same {@code DocumentStore} interface,
 * they can be stacked in any order, just like decorators. The distinction
 * from Decorator is one of intent: Proxy controls *access* to the real
 * subject (security, remoting, caching, lazy init), rather than adding new
 * behavior on top of it.
 */
public class ProxyExample {

    // ---------- Subject ----------
    public interface DocumentStore {
        String read(String user, String docId);

        void write(String user, String docId, String content);
    }

    // ---------- Real Subject (slow / expensive) ----------
    public static class RemoteDocumentStore implements DocumentStore {
        private final Map<String, String> storage = new HashMap<>();

        public RemoteDocumentStore() {
            // Pretend these were already on the remote server.
            storage.put("doc-1", "Quarterly report");
            storage.put("doc-2", "Engineering roadmap");
            storage.put("doc-3", "Board meeting minutes");
        }

        @Override
        public String read(String user, String docId) {
            simulateLatency(80);
            String content = storage.get(docId);
            if (content == null) {
                throw new IllegalArgumentException("no such doc: " + docId);
            }
            return content;
        }

        @Override
        public void write(String user, String docId, String content) {
            simulateLatency(120);
            storage.put(docId, content);
        }

        private static void simulateLatency(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ---------- Protection Proxy ----------
    public static class ProtectionProxy implements DocumentStore {
        private final DocumentStore inner;
        private final Set<String> readers;
        private final Set<String> writers;

        public ProtectionProxy(DocumentStore inner,
                               Set<String> readers, Set<String> writers) {
            this.inner = inner;
            this.readers = readers;
            this.writers = writers;
        }

        @Override
        public String read(String user, String docId) {
            if (!readers.contains(user)) {
                throw new SecurityException("read denied for " + user);
            }
            return inner.read(user, docId);
        }

        @Override
        public void write(String user, String docId, String content) {
            if (!writers.contains(user)) {
                throw new SecurityException("write denied for " + user);
            }
            inner.write(user, docId, content);
        }
    }

    // ---------- Caching Proxy ----------
    public static class CachingProxy implements DocumentStore {
        private final DocumentStore inner;
        private final Map<String, String> cache = new HashMap<>();
        private int hits = 0;
        private int misses = 0;

        public CachingProxy(DocumentStore inner) { this.inner = inner; }

        @Override
        public String read(String user, String docId) {
            String cached = cache.get(docId);
            if (cached != null) {
                hits++;
                return cached;
            }
            misses++;
            String fresh = inner.read(user, docId);
            cache.put(docId, fresh);
            return fresh;
        }

        @Override
        public void write(String user, String docId, String content) {
            // Writes invalidate the cache entry so stale reads can't leak.
            inner.write(user, docId, content);
            cache.remove(docId);
        }

        public int hits()   { return hits; }
        public int misses() { return misses; }
    }

    // ---------- Logging Proxy ----------
    public static class LoggingProxy implements DocumentStore {
        private final DocumentStore inner;

        public LoggingProxy(DocumentStore inner) { this.inner = inner; }

        @Override
        public String read(String user, String docId) {
            long t0 = System.nanoTime();
            try {
                String out = inner.read(user, docId);
                log("READ", user, docId, t0, "ok");
                return out;
            } catch (RuntimeException ex) {
                log("READ", user, docId, t0, "FAIL:" + ex.getClass().getSimpleName());
                throw ex;
            }
        }

        @Override
        public void write(String user, String docId, String content) {
            long t0 = System.nanoTime();
            try {
                inner.write(user, docId, content);
                log("WRITE", user, docId, t0, "ok");
            } catch (RuntimeException ex) {
                log("WRITE", user, docId, t0, "FAIL:" + ex.getClass().getSimpleName());
                throw ex;
            }
        }

        private static void log(String op, String user, String docId,
                                long startNanos, String outcome) {
            long ms = (System.nanoTime() - startNanos) / 1_000_000;
            System.out.printf("[audit] %-5s user=%s doc=%s %dms -> %s%n",
                    op, user, docId, ms, outcome);
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        Set<String> readers = new LinkedHashSet<>();
        readers.add("ada");
        readers.add("grace");
        readers.add("linus");

        Set<String> writers = new LinkedHashSet<>();
        writers.add("ada");
        writers.add("grace");

        // Stack order: client -> logging -> caching -> protection -> real store.
        // Logging wraps the outermost call so cache hits are also audited.
        DocumentStore store = new LoggingProxy(
                new CachingProxy(
                        new ProtectionProxy(
                                new RemoteDocumentStore(),
                                readers, writers)));

        System.out.println("=== First reads (cache misses, real round-trips) ===");
        System.out.println(store.read("ada",   "doc-1"));
        System.out.println(store.read("grace", "doc-2"));

        System.out.println();
        System.out.println("=== Repeated reads (cache hits, near-zero latency) ===");
        System.out.println(store.read("ada",   "doc-1"));
        System.out.println(store.read("linus", "doc-2"));

        System.out.println();
        System.out.println("=== Write invalidates cache ===");
        store.write("grace", "doc-1", "Quarterly report (revised)");
        System.out.println(store.read("ada", "doc-1"));  // miss again

        System.out.println();
        System.out.println("=== Access control rejects unauthorized calls ===");
        try {
            store.write("linus", "doc-1", "tampered");
        } catch (SecurityException se) {
            System.out.println("  -> rejected as expected: " + se.getMessage());
        }
        try {
            store.read("eve", "doc-1");
        } catch (SecurityException se) {
            System.out.println("  -> rejected as expected: " + se.getMessage());
        }
    }
}

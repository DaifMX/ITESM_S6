package net.daifo.adapter;

/**
 * Adapter Pattern Example
 *
 * Scenario: A music studio app only knows how to play tracks through a modern
 * {@link DigitalMixer} interface (stereo float samples in the range [-1.0, 1.0]).
 * However, the studio owns a legacy tape machine ({@link VintageTapeDeck}) that
 * exposes a completely different API (mono 8-bit unsigned samples, played by
 * spinning a reel at a given RPM).
 *
 * We introduce {@link TapeDeckAdapter} so the legacy device can be driven
 * through the same {@code DigitalMixer} contract the rest of the app uses,
 * without modifying either the client or the legacy class.
 */
public class AdapterExample {

    // ---------- Target interface (what the client expects) ----------
    public interface DigitalMixer {
        /** Feed a block of stereo float samples in [-1.0, 1.0] at 44.1 kHz. */
        void pushSamples(float[] left, float[] right);

        String deviceName();
    }

    // ---------- Adaptee (legacy, incompatible API) ----------
    public static class VintageTapeDeck {
        private int reelRpm = 0;

        public void loadReel(String reelId) {
            System.out.println("[Tape] Loaded reel: " + reelId);
        }

        public void spinReel(int rpm) {
            this.reelRpm = rpm;
            System.out.println("[Tape] Reel spinning at " + rpm + " rpm");
        }

        /** Mono playback: samples are unsigned bytes (0..255), 22.05 kHz. */
        public void playMonoBlock(byte[] monoSamples) {
            if (reelRpm <= 0) {
                System.out.println("[Tape] (reel not spinning, ignoring block)");
                return;
            }
            System.out.println("[Tape] Playing " + monoSamples.length
                    + " mono bytes (first=" + (monoSamples[0] & 0xFF) + ")");
        }

        public void stop() {
            this.reelRpm = 0;
            System.out.println("[Tape] Stopped");
        }
    }

    // ---------- Adapter (object adapter via composition) ----------
    public static class TapeDeckAdapter implements DigitalMixer {
        private final VintageTapeDeck deck;
        private boolean started = false;

        public TapeDeckAdapter(VintageTapeDeck deck, String reelId) {
            this.deck = deck;
            this.deck.loadReel(reelId);
        }

        @Override
        public void pushSamples(float[] left, float[] right) {
            if (left.length != right.length) {
                throw new IllegalArgumentException("L/R length mismatch");
            }
            if (!started) {
                deck.spinReel(33); // typical LP-ish speed; arbitrary for the demo
                started = true;
            }

            // 1) stereo -> mono by averaging channels
            // 2) downsample 44.1k -> 22.05k by taking every other sample
            // 3) float [-1, 1] -> unsigned byte [0, 255]
            int outLen = left.length / 2;
            byte[] mono = new byte[outLen];
            for (int i = 0, j = 0; j < outLen; i += 2, j++) {
                float mixed = (left[i] + right[i]) * 0.5f;
                if (mixed > 1f) mixed = 1f;
                if (mixed < -1f) mixed = -1f;
                int unsigned = Math.round((mixed + 1f) * 127.5f); // 0..255
                mono[j] = (byte) unsigned;
            }
            deck.playMonoBlock(mono);
        }

        @Override
        public String deviceName() {
            return "VintageTapeDeck (via adapter)";
        }

        public void shutdown() {
            deck.stop();
        }
    }

    // ---------- A native target implementation, for contrast ----------
    public static class StudioMixer implements DigitalMixer {
        @Override
        public void pushSamples(float[] left, float[] right) {
            System.out.println("[Mixer] Pushed " + left.length + " stereo float samples");
        }

        @Override
        public String deviceName() {
            return "StudioMixer";
        }
    }

    // ---------- Client code: depends only on DigitalMixer ----------
    public static void playTestTone(DigitalMixer mixer) {
        System.out.println("-- Sending tone to: " + mixer.deviceName() + " --");
        float[] l = new float[8];
        float[] r = new float[8];
        for (int i = 0; i < l.length; i++) {
            double t = i / 8.0;
            l[i] = (float) Math.sin(2 * Math.PI * t);
            r[i] = (float) Math.sin(2 * Math.PI * t + Math.PI / 4);
        }
        mixer.pushSamples(l, r);
    }

    public static void main(String[] args) {
        DigitalMixer modern = new StudioMixer();
        playTestTone(modern);

        TapeDeckAdapter legacy = new TapeDeckAdapter(new VintageTapeDeck(), "REEL-A1");
        playTestTone(legacy);
        legacy.shutdown();
    }
}

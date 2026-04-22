package net.daifo.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite Pattern Example
 *
 * Scenario: A manufacturing shop needs to price and describe products that are
 * built from sub-assemblies, which are themselves built from sub-assemblies or
 * raw parts (a "bill of materials" tree). The shop wants to ask any node —
 * whether it's a single screw or an entire bicycle — the same questions:
 *
 *     - What does it cost?
 *     - How heavy is it (grams)?
 *     - Print a human-readable breakdown.
 *
 * The Composite pattern lets clients treat individual {@link Part}s (leaves)
 * and {@link Assembly}s (composites) through the same {@link Component}
 * interface, so traversal, pricing, and display work uniformly at every depth.
 */
public class CompositeExample {

    // ---------- Component ----------
    public interface Component {
        String name();

        /** Price in cents, to avoid float rounding in money math. */
        long priceCents();

        int weightGrams();

        /** Pretty-print the subtree rooted at this component. */
        void print(String indent);
    }

    // ---------- Leaf ----------
    public static class Part implements Component {
        private final String name;
        private final long priceCents;
        private final int weightGrams;

        public Part(String name, long priceCents, int weightGrams) {
            this.name = name;
            this.priceCents = priceCents;
            this.weightGrams = weightGrams;
        }

        @Override public String name() { return name; }
        @Override public long priceCents() { return priceCents; }
        @Override public int weightGrams() { return weightGrams; }

        @Override
        public void print(String indent) {
            System.out.printf("%s- %s  [$%.2f, %dg]%n",
                    indent, name, priceCents / 100.0, weightGrams);
        }
    }

    // ---------- Composite ----------
    public static class Assembly implements Component {
        private final String name;
        /** Labor surcharge added on top of the children's prices. */
        private final long laborCents;
        private final List<Component> children = new ArrayList<>();

        public Assembly(String name, long laborCents) {
            this.name = name;
            this.laborCents = laborCents;
        }

        public Assembly add(Component child) {
            children.add(child);
            return this; // fluent building
        }

        public Assembly addN(int count, Component child) {
            for (int i = 0; i < count; i++) children.add(child);
            return this;
        }

        public boolean remove(Component child) {
            return children.remove(child);
        }

        @Override public String name() { return name; }

        @Override
        public long priceCents() {
            long total = laborCents;
            for (Component c : children) total += c.priceCents();
            return total;
        }

        @Override
        public int weightGrams() {
            int total = 0;
            for (Component c : children) total += c.weightGrams();
            return total;
        }

        @Override
        public void print(String indent) {
            System.out.printf("%s+ %s  [subtotal $%.2f, %dg, labor $%.2f]%n",
                    indent, name, priceCents() / 100.0, weightGrams(),
                    laborCents / 100.0);
            for (Component c : children) {
                c.print(indent + "    ");
            }
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        // Leaves: raw parts
        Part rim          = new Part("Aluminum rim",      4200, 520);
        Part spoke        = new Part("Stainless spoke",     80,  12);
        Part hub          = new Part("Sealed hub",        3100, 250);
        Part tire         = new Part("Road tire 700c",    2800, 300);
        Part tube         = new Part("Inner tube",         600,  95);
        Part frameTube    = new Part("Cr-Mo frame tube",  9500, 1800);
        Part saddle       = new Part("Leather saddle",    4500, 380);
        Part handlebar    = new Part("Drop handlebar",    3300, 290);
        Part chain        = new Part("11-speed chain",    1800, 260);
        Part crankset     = new Part("Aluminum crankset", 7800, 720);

        // Composite: wheel = rim + 32 spokes + hub + tire + tube
        Assembly wheel = new Assembly("Wheel assembly", 1500)
                .add(rim)
                .addN(32, spoke)
                .add(hub)
                .add(tire)
                .add(tube);

        // Composite: drivetrain
        Assembly drivetrain = new Assembly("Drivetrain", 900)
                .add(chain)
                .add(crankset);

        // Composite of composites: the full bicycle uses two wheels.
        Assembly bicycle = new Assembly("Road bicycle", 5000)
                .add(frameTube)
                .add(saddle)
                .add(handlebar)
                .add(drivetrain)
                .addN(2, wheel);

        // Uniform interface: the client treats leaves and composites the same.
        bicycle.print("");
        System.out.println();
        System.out.printf("TOTAL: $%.2f, %dg%n",
                bicycle.priceCents() / 100.0, bicycle.weightGrams());

        // And a leaf answers the same questions just fine:
        System.out.println();
        saddle.print("");
    }
}

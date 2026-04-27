package net.daifo.strategy;

import java.util.Arrays;

/**
 * Strategy Pattern Example
 *
 * Scenario: An e-commerce checkout calculates shipping cost using different
 * algorithms depending on the carrier chosen at runtime:
 *
 *   - Standard shipping : flat rate per kg.
 *   - Express shipping  : base fee + per-kg surcharge.
 *   - Drone delivery    : free under 1 kg, otherwise unavailable.
 *
 * The {@link ShoppingCart} (context) holds a {@link ShippingStrategy} reference
 * that can be swapped at any time without modifying the cart. Adding a new
 * carrier means adding one new strategy class, not touching the cart.
 */
public class StrategyExample {

    // ---------- Strategy interface ----------
    public interface ShippingStrategy {
        /** Returns cost in USD cents, or -1 if the option is unavailable. */
        int calculateCost(double weightKg, String destination);
        String name();
    }

    // ---------- Concrete strategies ----------
    public static class StandardShipping implements ShippingStrategy {
        private static final int RATE_PER_KG = 500; // $5.00 / kg

        @Override
        public int calculateCost(double weightKg, String destination) {
            return (int) (weightKg * RATE_PER_KG);
        }

        @Override
        public String name() { return "Standard"; }
    }

    public static class ExpressShipping implements ShippingStrategy {
        private static final int BASE_FEE = 1200;   // $12.00
        private static final int RATE_PER_KG = 800; // $8.00 / kg

        @Override
        public int calculateCost(double weightKg, String destination) {
            return BASE_FEE + (int) (weightKg * RATE_PER_KG);
        }

        @Override
        public String name() { return "Express"; }
    }

    public static class DroneDelivery implements ShippingStrategy {
        private static final double MAX_WEIGHT_KG = 1.0;

        @Override
        public int calculateCost(double weightKg, String destination) {
            if (weightKg > MAX_WEIGHT_KG) return -1; // unavailable
            return 0; // free
        }

        @Override
        public String name() { return "Drone"; }
    }

    // ---------- Context ----------
    public static class ShoppingCart {
        private ShippingStrategy strategy;
        private final double weightKg;
        private final String destination;

        public ShoppingCart(double weightKg, String destination, ShippingStrategy strategy) {
            this.weightKg = weightKg;
            this.destination = destination;
            this.strategy = strategy;
        }

        public void setStrategy(ShippingStrategy strategy) {
            this.strategy = strategy;
        }

        public void printShippingCost() {
            int cents = strategy.calculateCost(weightKg, destination);
            if (cents < 0) {
                System.out.printf("[Cart] %s: unavailable for %.1f kg to %s%n",
                        strategy.name(), weightKg, destination);
            } else {
                System.out.printf("[Cart] %s: $%d.%02d for %.1f kg to %s%n",
                        strategy.name(), cents / 100, cents % 100, weightKg, destination);
            }
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart(2.5, "Austin, TX", new StandardShipping());
        cart.printShippingCost();

        cart.setStrategy(new ExpressShipping());
        cart.printShippingCost();

        cart.setStrategy(new DroneDelivery());
        cart.printShippingCost(); // unavailable > 1 kg

        System.out.println("\n-- Light item (0.4 kg) --");
        ShoppingCart lightCart = new ShoppingCart(0.4, "Austin, TX", new DroneDelivery());
        for (ShippingStrategy s : Arrays.asList(
                new StandardShipping(), new ExpressShipping(), new DroneDelivery())) {
            lightCart.setStrategy(s);
            lightCart.printShippingCost();
        }
    }
}

package net.daifo.builder;

// BUILDER — constructs a complex object step by step using a fluent API.

class Pizza {
    private final String size;
    private final String crust;
    private final boolean pepperoni;
    private final boolean mushrooms;

    private Pizza(Builder b) {
        this.size      = b.size;
        this.crust     = b.crust;
        this.pepperoni = b.pepperoni;
        this.mushrooms = b.mushrooms;
    }

    public String toString() {
        return size + " pizza, " + crust + " crust"
            + (pepperoni ? ", pepperoni" : "")
            + (mushrooms ? ", mushrooms" : "");
    }

    // Builder collects optional params via chained setters, then creates the object
    static class Builder {
        private final String size;
        private final String crust;
        private boolean pepperoni;
        private boolean mushrooms;

        Builder(String size, String crust) { this.size = size; this.crust = crust; }

        Builder pepperoni() { this.pepperoni = true; return this; }
        Builder mushrooms() { this.mushrooms = true; return this; }

        Pizza build() { return new Pizza(this); }
    }
}

public class Builder {
    public static void demo() {
        System.out.println("\n=== BUILDER ===");
        Pizza pizza = new Pizza.Builder("Large", "thin")
                .pepperoni()
                .mushrooms()
                .build();
        System.out.println(pizza);
    }
}

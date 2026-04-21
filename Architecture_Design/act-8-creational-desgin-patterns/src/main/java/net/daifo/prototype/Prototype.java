package net.daifo.prototype;

// PROTOTYPE — creates new objects by cloning an existing one instead of building from scratch.

class Shape {
    private String type;
    private String color;

    Shape(String type, String color) { this.type = type; this.color = color; }

    // Copy constructor — used by clone()
    private Shape(Shape source) { this.type = source.type; this.color = source.color; }

    public Shape clone() { return new Shape(this); }

    void setColor(String color) { this.color = color; }

    public String toString() { return type + " [" + color + "]"; }
}

public class Prototype {
    public static void demo() {
        System.out.println("\n=== PROTOTYPE ===");
        Shape original = new Shape("Circle", "red");
        Shape copy     = original.clone();

        copy.setColor("blue"); // does not affect the original

        System.out.println("Original: " + original);
        System.out.println("Clone:    " + copy);
    }
}

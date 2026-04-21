package net.daifo.abstractfactory;

// ABSTRACT FACTORY — creates families of related objects.
// Swap the factory to change all products at once; they're always compatible.

interface UIFactory {
    Runnable createButton();
    Runnable createCheckbox();
}

class MacFactory implements UIFactory {
    public Runnable createButton()   { return () -> System.out.println("macOS Button");   }
    public Runnable createCheckbox() { return () -> System.out.println("macOS Checkbox"); }
}

class WinFactory implements UIFactory {
    public Runnable createButton()   { return () -> System.out.println("Windows Button");   }
    public Runnable createCheckbox() { return () -> System.out.println("Windows Checkbox"); }
}

public class AbstractFactory {
    public static void demo() {
        System.out.println("\n=== ABSTRACT FACTORY ===");
        UIFactory factory = new MacFactory(); // swap to WinFactory to change the whole family
        factory.createButton().run();
        factory.createCheckbox().run();
    }
}

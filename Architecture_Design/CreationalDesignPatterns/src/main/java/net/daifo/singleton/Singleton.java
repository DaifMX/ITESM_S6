package net.daifo.singleton;

// SINGLETON — ensures only one instance exists.

public class Singleton {

    private static Singleton instance;

    // Private constructor prevents external instantiation
    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public String query(String sql) {
        return "Result of: " + sql;
    }

    public static void demo() {
        System.out.println("\n=== SINGLETON ===");
        Singleton a = Singleton.getInstance();
        Singleton b = Singleton.getInstance();
        System.out.println(a.query("SELECT * FROM users"));
        System.out.println("Same instance? " + (a == b)); // true
    }
}

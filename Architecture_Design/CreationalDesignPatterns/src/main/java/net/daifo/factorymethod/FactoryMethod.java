package net.daifo.factorymethod;

// FACTORY METHOD — subclasses decide which object to instantiate.
// The factory method is createMessage(); subclasses override it to return different behavior.

abstract class Sender {
    protected abstract Runnable createMessage(String text); // factory method

    public void send(String text) {
        createMessage(text).run(); // uses the object without knowing its concrete type
    }
}

class EmailSender extends Sender {
    protected Runnable createMessage(String text) {
        return () -> System.out.println("Email: " + text);
    }
}

class SmsSender extends Sender {
    protected Runnable createMessage(String text) {
        return () -> System.out.println("SMS: " + text);
    }
}

public class FactoryMethod {
    public static void demo() {
        System.out.println("\n=== FACTORY METHOD ===");
        Sender sender = new EmailSender();
        sender.send("Hello!");
        sender = new SmsSender();
        sender.send("Hello!");
    }
}

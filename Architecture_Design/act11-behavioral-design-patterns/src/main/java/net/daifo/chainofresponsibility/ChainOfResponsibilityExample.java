package net.daifo.chainofresponsibility;

/**
 * Chain of Responsibility Pattern Example
 *
 * Scenario: A customer-support ticketing system routes an incoming support
 * ticket through a chain of handlers, each responsible for a different
 * severity level:
 *
 *   Level 1 – FAQ Bot    : handles trivial questions (severity <= 1)
 *   Level 2 – Junior Rep : handles common issues  (severity <= 3)
 *   Level 3 – Senior Rep : handles complex cases  (severity <= 7)
 *   Level 4 – Engineering: handles critical bugs  (severity <= 10)
 *
 * Each handler either processes the ticket or passes it to the next handler.
 * The sender does not know which handler will ultimately handle the request.
 */
public class ChainOfResponsibilityExample {

    // ---------- Request ----------
    public static class Ticket {
        private final String description;
        private final int severity; // 1 (trivial) to 10 (critical)

        public Ticket(String description, int severity) {
            this.description = description;
            this.severity = severity;
        }

        public int getSeverity() { return severity; }
        public String getDescription() { return description; }
    }

    // ---------- Handler interface ----------
    public interface SupportHandler {
        void setNext(SupportHandler next);
        void handle(Ticket ticket);
    }

    // ---------- Base handler ----------
    public abstract static class BaseHandler implements SupportHandler {
        private SupportHandler next;

        @Override
        public void setNext(SupportHandler next) {
            this.next = next;
        }

        protected void passToNext(Ticket ticket) {
            if (next != null) {
                next.handle(ticket);
            } else {
                System.out.println("[Chain] No handler found for severity " + ticket.getSeverity()
                        + " — ticket escalated to management.");
            }
        }
    }

    // ---------- Concrete handlers ----------
    public static class FaqBot extends BaseHandler {
        @Override
        public void handle(Ticket ticket) {
            if (ticket.getSeverity() <= 1) {
                System.out.println("[FAQ Bot] Auto-resolved: \"" + ticket.getDescription() + "\"");
            } else {
                passToNext(ticket);
            }
        }
    }

    public static class JuniorRep extends BaseHandler {
        @Override
        public void handle(Ticket ticket) {
            if (ticket.getSeverity() <= 3) {
                System.out.println("[Junior Rep] Handled: \"" + ticket.getDescription() + "\"");
            } else {
                passToNext(ticket);
            }
        }
    }

    public static class SeniorRep extends BaseHandler {
        @Override
        public void handle(Ticket ticket) {
            if (ticket.getSeverity() <= 7) {
                System.out.println("[Senior Rep] Resolved: \"" + ticket.getDescription() + "\"");
            } else {
                passToNext(ticket);
            }
        }
    }

    public static class Engineering extends BaseHandler {
        @Override
        public void handle(Ticket ticket) {
            if (ticket.getSeverity() <= 10) {
                System.out.println("[Engineering] Critical fix applied: \"" + ticket.getDescription() + "\"");
            } else {
                passToNext(ticket);
            }
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        // Build the chain: FAQ -> Junior -> Senior -> Engineering
        FaqBot bot = new FaqBot();
        JuniorRep junior = new JuniorRep();
        SeniorRep senior = new SeniorRep();
        Engineering engineering = new Engineering();

        bot.setNext(junior);
        junior.setNext(senior);
        senior.setNext(engineering);

        Ticket[] tickets = {
            new Ticket("How do I reset my password?", 1),
            new Ticket("Cannot update billing info", 3),
            new Ticket("App crashes on specific input", 6),
            new Ticket("Data loss after latest deploy", 10),
        };

        for (Ticket t : tickets) {
            System.out.print("Ticket (sev=" + t.getSeverity() + "): ");
            bot.handle(t);
        }
    }
}

package net.daifo.bridge;

/**
 * Bridge Pattern Example
 *
 * Scenario: A notification system has two independent axes of variation:
 *
 *   1) WHAT kind of notification is being sent (abstraction):
 *         - AlertNotification (high priority, prefixed with !!)
 *         - DigestNotification (bundles many lines into one message)
 *
 *   2) HOW it is actually delivered (implementor):
 *         - EmailChannel
 *         - SmsChannel
 *         - SlackChannel
 *
 * Without the Bridge pattern, supporting N notification kinds over M channels
 * would require N*M concrete classes (AlertEmail, AlertSms, DigestSlack, ...).
 *
 * The Bridge pattern decouples the two hierarchies: {@link Notification}
 * (abstraction) holds a reference to a {@link DeliveryChannel} (implementor),
 * and each hierarchy can evolve on its own. Adding a new channel or a new
 * notification type is additive — no combinatorial explosion.
 */
public class BridgeExample {

    // ---------- Implementor ----------
    public interface DeliveryChannel {
        void deliver(String recipient, String subject, String body);

        String channelName();
    }

    // ---------- Concrete Implementors ----------
    public static class EmailChannel implements DeliveryChannel {
        private final String smtpHost;

        public EmailChannel(String smtpHost) {
            this.smtpHost = smtpHost;
        }

        @Override
        public void deliver(String recipient, String subject, String body) {
            System.out.println("[Email via " + smtpHost + "] to=" + recipient
                    + " | subject=\"" + subject + "\"");
            System.out.println("    " + body);
        }

        @Override
        public String channelName() {
            return "email";
        }
    }

    public static class SmsChannel implements DeliveryChannel {
        private static final int MAX_LEN = 140;

        @Override
        public void deliver(String recipient, String subject, String body) {
            // SMS has no subject line; fold subject into body and truncate.
            String merged = subject + ": " + body;
            if (merged.length() > MAX_LEN) {
                merged = merged.substring(0, MAX_LEN - 1) + "…";
            }
            System.out.println("[SMS] to=" + recipient + " | " + merged);
        }

        @Override
        public String channelName() {
            return "sms";
        }
    }

    public static class SlackChannel implements DeliveryChannel {
        private final String workspace;

        public SlackChannel(String workspace) {
            this.workspace = workspace;
        }

        @Override
        public void deliver(String recipient, String subject, String body) {
            // Slack style: recipient is a channel like "#ops", subject becomes bold.
            System.out.println("[Slack:" + workspace + "] " + recipient
                    + " *" + subject + "*");
            System.out.println("    > " + body);
        }

        @Override
        public String channelName() {
            return "slack";
        }
    }

    // ---------- Abstraction ----------
    public abstract static class Notification {
        protected final DeliveryChannel channel;

        protected Notification(DeliveryChannel channel) {
            this.channel = channel;
        }

        /** Template method: subclasses decide how to shape the message. */
        public abstract void send(String recipient);
    }

    // ---------- Refined Abstractions ----------
    public static class AlertNotification extends Notification {
        private final String message;

        public AlertNotification(DeliveryChannel channel, String message) {
            super(channel);
            this.message = message;
        }

        @Override
        public void send(String recipient) {
            String subject = "!! ALERT !!";
            String body = "[" + channel.channelName().toUpperCase() + "] " + message;
            channel.deliver(recipient, subject, body);
        }
    }

    public static class DigestNotification extends Notification {
        private final String[] items;

        public DigestNotification(DeliveryChannel channel, String... items) {
            super(channel);
            this.items = items;
        }

        @Override
        public void send(String recipient) {
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < items.length; i++) {
                if (i > 0) body.append(" | ");
                body.append(i + 1).append(") ").append(items[i]);
            }
            String subject = "Daily digest (" + items.length + " items)";
            channel.deliver(recipient, subject, body.toString());
        }
    }

    // ---------- Client: depends only on Notification ----------
    public static void main(String[] args) {
        DeliveryChannel email = new EmailChannel("smtp.daifo.net");
        DeliveryChannel sms = new SmsChannel();
        DeliveryChannel slack = new SlackChannel("daifo-studio");

        // Same abstraction, different implementors — mixed and matched freely.
        new AlertNotification(email, "Disk usage at 95% on db-01")
                .send("oncall@daifo.net");

        new AlertNotification(sms, "Disk usage at 95% on db-01, immediate action required, please ack within 5 minutes otherwise the pager escalates to the backup rotation")
                .send("+52-555-0100");

        new DigestNotification(slack,
                "3 PRs merged",
                "2 deployments succeeded",
                "1 flaky test quarantined")
                .send("#eng-updates");

        // Swapping the channel at runtime costs nothing:
        Notification n = new DigestNotification(email, "A", "B", "C");
        n.send("team@daifo.net");
    }
}

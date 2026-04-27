package net.daifo.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern Example
 *
 * Scenario: A stock market feed publishes real-time price updates for a
 * ticker symbol. Multiple subscribers — a trading dashboard, a price-alert
 * service, and an audit logger — react to each update independently.
 *
 * The {@link StockFeed} (subject) maintains a list of {@link MarketObserver}
 * instances and notifies all of them whenever the price changes. Observers
 * subscribe and unsubscribe at runtime; the feed never knows their concrete
 * types.
 */
public class ObserverExample {

    // ---------- Observer interface ----------
    public interface MarketObserver {
        void onPriceUpdate(String ticker, double oldPrice, double newPrice);
    }

    // ---------- Subject ----------
    public static class StockFeed {
        private final String ticker;
        private double price;
        private final List<MarketObserver> observers = new ArrayList<>();

        public StockFeed(String ticker, double initialPrice) {
            this.ticker = ticker;
            this.price = initialPrice;
        }

        public void subscribe(MarketObserver o) {
            observers.add(o);
        }

        public void unsubscribe(MarketObserver o) {
            observers.remove(o);
        }

        public void setPrice(double newPrice) {
            double old = price;
            price = newPrice;
            System.out.printf("[Feed] %s price: %.2f -> %.2f%n", ticker, old, newPrice);
            for (MarketObserver o : observers) {
                o.onPriceUpdate(ticker, old, newPrice);
            }
        }

        public double getPrice() { return price; }
    }

    // ---------- Concrete observers ----------
    public static class TradingDashboard implements MarketObserver {
        @Override
        public void onPriceUpdate(String ticker, double oldPrice, double newPrice) {
            String direction = newPrice >= oldPrice ? "UP" : "DOWN";
            System.out.printf("  [Dashboard] %s is %s | new price: $%.2f%n",
                    ticker, direction, newPrice);
        }
    }

    public static class PriceAlertService implements MarketObserver {
        private final double threshold;

        public PriceAlertService(double threshold) {
            this.threshold = threshold;
        }

        @Override
        public void onPriceUpdate(String ticker, double oldPrice, double newPrice) {
            if (newPrice >= threshold) {
                System.out.printf("  [Alert] THRESHOLD BREACHED for %s: $%.2f >= $%.2f%n",
                        ticker, newPrice, threshold);
            }
        }
    }

    public static class AuditLogger implements MarketObserver {
        @Override
        public void onPriceUpdate(String ticker, double oldPrice, double newPrice) {
            System.out.printf("  [Audit] LOG | ticker=%s old=%.2f new=%.2f delta=%.2f%n",
                    ticker, oldPrice, newPrice, newPrice - oldPrice);
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        StockFeed feed = new StockFeed("ACME", 100.00);

        TradingDashboard dashboard = new TradingDashboard();
        PriceAlertService alert = new PriceAlertService(115.00);
        AuditLogger logger = new AuditLogger();

        feed.subscribe(dashboard);
        feed.subscribe(alert);
        feed.subscribe(logger);

        feed.setPrice(105.50);
        feed.setPrice(112.00);
        feed.setPrice(117.75); // breaches alert threshold

        System.out.println("\n-- Dashboard unsubscribes --");
        feed.unsubscribe(dashboard);
        feed.setPrice(95.00); // only alert + logger react
    }
}

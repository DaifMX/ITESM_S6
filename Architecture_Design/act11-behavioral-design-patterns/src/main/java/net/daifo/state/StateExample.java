package net.daifo.state;

/**
 * State Pattern Example
 *
 * Scenario: A vending machine changes its behaviour depending on its current
 * operational state:
 *
 *   Idle        — waiting for a coin; ejects nothing, dispenses nothing.
 *   HasCoin     — coin inserted; can be cancelled or product selected.
 *   Dispensing  — product being dispensed; no new coin accepted mid-cycle.
 *   SoldOut     — no stock; returns coins immediately.
 *
 * Instead of a massive if/switch in {@link VendingMachine}, each state is a
 * separate class that handles the three user actions (insertCoin, selectProduct,
 * cancel). State transitions happen inside the state objects themselves.
 */
public class StateExample {

    // ---------- State interface ----------
    public interface VendingState {
        void insertCoin(VendingMachine machine);
        void selectProduct(VendingMachine machine);
        void cancel(VendingMachine machine);
    }

    // ---------- Context ----------
    public static class VendingMachine {
        private VendingState state;
        private int stock;

        public VendingMachine(int stock) {
            this.stock = stock;
            this.state = stock > 0 ? new IdleState() : new SoldOutState();
        }

        public void setState(VendingState state) { this.state = state; }
        public int getStock() { return stock; }
        public void decrementStock() { stock--; }

        public void insertCoin()    { state.insertCoin(this); }
        public void selectProduct() { state.selectProduct(this); }
        public void cancel()        { state.cancel(this); }
    }

    // ---------- Concrete states ----------
    public static class IdleState implements VendingState {
        @Override
        public void insertCoin(VendingMachine m) {
            System.out.println("[Idle] Coin accepted.");
            m.setState(new HasCoinState());
        }

        @Override
        public void selectProduct(VendingMachine m) {
            System.out.println("[Idle] Insert a coin first.");
        }

        @Override
        public void cancel(VendingMachine m) {
            System.out.println("[Idle] Nothing to cancel.");
        }
    }

    public static class HasCoinState implements VendingState {
        @Override
        public void insertCoin(VendingMachine m) {
            System.out.println("[HasCoin] Already has a coin. Returning extra coin.");
        }

        @Override
        public void selectProduct(VendingMachine m) {
            System.out.println("[HasCoin] Dispensing product...");
            m.setState(new DispensingState());
            m.selectProduct(); // trigger dispensing
        }

        @Override
        public void cancel(VendingMachine m) {
            System.out.println("[HasCoin] Coin returned.");
            m.setState(new IdleState());
        }
    }

    public static class DispensingState implements VendingState {
        @Override
        public void insertCoin(VendingMachine m) {
            System.out.println("[Dispensing] Please wait, dispensing in progress.");
        }

        @Override
        public void selectProduct(VendingMachine m) {
            m.decrementStock();
            System.out.println("[Dispensing] Product dispensed! Stock left: " + m.getStock());
            if (m.getStock() > 0) {
                m.setState(new IdleState());
            } else {
                System.out.println("[Dispensing] Out of stock!");
                m.setState(new SoldOutState());
            }
        }

        @Override
        public void cancel(VendingMachine m) {
            System.out.println("[Dispensing] Cannot cancel mid-dispense.");
        }
    }

    public static class SoldOutState implements VendingState {
        @Override
        public void insertCoin(VendingMachine m) {
            System.out.println("[SoldOut] Machine sold out. Returning coin.");
        }

        @Override
        public void selectProduct(VendingMachine m) {
            System.out.println("[SoldOut] No products available.");
        }

        @Override
        public void cancel(VendingMachine m) {
            System.out.println("[SoldOut] Nothing to cancel.");
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine(2);

        System.out.println("-- First purchase --");
        machine.selectProduct();  // no coin
        machine.insertCoin();
        machine.selectProduct();  // dispenses, stock=1

        System.out.println("\n-- Second purchase --");
        machine.insertCoin();
        machine.cancel();         // changed mind
        machine.insertCoin();
        machine.selectProduct();  // dispenses, stock=0 → SoldOut

        System.out.println("\n-- Sold out --");
        machine.insertCoin();     // coin returned
        machine.selectProduct();
    }
}

package net.daifo.visitor;

import java.util.Arrays;
import java.util.List;

/**
 * Visitor Pattern Example
 *
 * Scenario: A financial portfolio contains different types of assets —
 * {@link Stock}, {@link Bond}, and {@link RealEstate}. We need to perform
 * several independent operations over the collection:
 *
 *   - Calculate total portfolio value.
 *   - Compute annual tax liability (each asset type has a different rate).
 *   - Generate a plain-text audit report.
 *
 * Adding each operation directly to the asset classes would bloat them and
 * violate the Open/Closed Principle. Instead, each operation is a separate
 * {@link AssetVisitor} implementation. The asset hierarchy stays closed;
 * new operations are open for extension via new visitors.
 */
public class VisitorExample {

    // ---------- Visitor interface ----------
    public interface AssetVisitor {
        void visit(Stock stock);
        void visit(Bond bond);
        void visit(RealEstate property);
    }

    // ---------- Element interface ----------
    public interface Asset {
        void accept(AssetVisitor visitor);
        String getName();
    }

    // ---------- Concrete elements ----------
    public static class Stock implements Asset {
        private final String name;
        private final int shares;
        private final double pricePerShare;

        public Stock(String name, int shares, double pricePerShare) {
            this.name = name;
            this.shares = shares;
            this.pricePerShare = pricePerShare;
        }

        public int getShares() { return shares; }
        public double getPricePerShare() { return pricePerShare; }
        public double getValue() { return shares * pricePerShare; }

        @Override
        public String getName() { return name; }

        @Override
        public void accept(AssetVisitor visitor) { visitor.visit(this); }
    }

    public static class Bond implements Asset {
        private final String name;
        private final double faceValue;
        private final double couponRate;

        public Bond(String name, double faceValue, double couponRate) {
            this.name = name;
            this.faceValue = faceValue;
            this.couponRate = couponRate;
        }

        public double getFaceValue() { return faceValue; }
        public double getCouponRate() { return couponRate; }
        public double getAnnualIncome() { return faceValue * couponRate; }

        @Override
        public String getName() { return name; }

        @Override
        public void accept(AssetVisitor visitor) { visitor.visit(this); }
    }

    public static class RealEstate implements Asset {
        private final String name;
        private final double marketValue;
        private final double annualRent;

        public RealEstate(String name, double marketValue, double annualRent) {
            this.name = name;
            this.marketValue = marketValue;
            this.annualRent = annualRent;
        }

        public double getMarketValue() { return marketValue; }
        public double getAnnualRent() { return annualRent; }

        @Override
        public String getName() { return name; }

        @Override
        public void accept(AssetVisitor visitor) { visitor.visit(this); }
    }

    // ---------- Concrete visitors ----------
    public static class ValueCalculator implements AssetVisitor {
        private double total = 0;

        @Override
        public void visit(Stock stock) { total += stock.getValue(); }

        @Override
        public void visit(Bond bond) { total += bond.getFaceValue(); }

        @Override
        public void visit(RealEstate property) { total += property.getMarketValue(); }

        public double getTotal() { return total; }
    }

    public static class TaxCalculator implements AssetVisitor {
        private static final double STOCK_TAX = 0.15;
        private static final double BOND_TAX  = 0.20;
        private static final double RE_TAX    = 0.12;

        private double totalTax = 0;

        @Override
        public void visit(Stock stock) {
            double tax = stock.getValue() * STOCK_TAX;
            totalTax += tax;
            System.out.printf("[Tax] %s (stock): $%.2f%n", stock.getName(), tax);
        }

        @Override
        public void visit(Bond bond) {
            double tax = bond.getAnnualIncome() * BOND_TAX;
            totalTax += tax;
            System.out.printf("[Tax] %s (bond): $%.2f%n", bond.getName(), tax);
        }

        @Override
        public void visit(RealEstate property) {
            double tax = property.getMarketValue() * RE_TAX;
            totalTax += tax;
            System.out.printf("[Tax] %s (real estate): $%.2f%n", property.getName(), tax);
        }

        public double getTotalTax() { return totalTax; }
    }

    public static class AuditReporter implements AssetVisitor {
        @Override
        public void visit(Stock stock) {
            System.out.printf("[Audit] STOCK  | %-20s | %4d shares @ $%6.2f | value $%,.2f%n",
                    stock.getName(), stock.getShares(), stock.getPricePerShare(), stock.getValue());
        }

        @Override
        public void visit(Bond bond) {
            System.out.printf("[Audit] BOND   | %-20s | face $%,8.2f | coupon %.1f%%%n",
                    bond.getName(), bond.getFaceValue(), bond.getCouponRate() * 100);
        }

        @Override
        public void visit(RealEstate property) {
            System.out.printf("[Audit] REALTY | %-20s | market $%,10.2f | rent $%,.2f/yr%n",
                    property.getName(), property.getMarketValue(), property.getAnnualRent());
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        List<Asset> portfolio = Arrays.asList(
            new Stock("ACME Corp", 200, 145.50),
            new Stock("TechCo", 50, 820.00),
            new Bond("US Treasury 10Y", 50_000, 0.042),
            new RealEstate("Downtown Apt", 320_000, 18_000)
        );

        System.out.println("======= Audit Report =======");
        AuditReporter reporter = new AuditReporter();
        portfolio.forEach(a -> a.accept(reporter));

        System.out.println("\n======= Tax Calculation =======");
        TaxCalculator taxCalc = new TaxCalculator();
        portfolio.forEach(a -> a.accept(taxCalc));
        System.out.printf("[Tax] Total annual tax liability: $%,.2f%n", taxCalc.getTotalTax());

        System.out.println("\n======= Portfolio Value =======");
        ValueCalculator valCalc = new ValueCalculator();
        portfolio.forEach(a -> a.accept(valCalc));
        System.out.printf("[Value] Total portfolio value: $%,.2f%n", valCalc.getTotal());
    }
}

package net.daifo.templatemethod;

/**
 * Template Method Pattern Example
 *
 * Scenario: A data pipeline framework processes reports from different sources
 * (CSV file, REST API, database). The overall algorithm is always the same:
 *
 *   1. connect()      — open the data source
 *   2. fetchData()    — retrieve raw records
 *   3. parseData()    — transform raw strings into domain objects
 *   4. generateReport() — format and print the report
 *   5. disconnect()   — close the data source
 *
 * Steps 1, 2, 3, and 5 are source-specific (abstract). Step 4 is generic and
 * shared by all subclasses (concrete). {@link DataPipeline#run()} is the
 * template method that defines the algorithm skeleton — subclasses fill in the
 * blanks without changing the sequence.
 */
public class TemplateMethodExample {

    // ---------- Abstract class with template method ----------
    public abstract static class DataPipeline {

        /** Template method — defines the algorithm; subclasses must not override. */
        public final void run() {
            connect();
            String[] raw = fetchData();
            String[] parsed = parseData(raw);
            generateReport(parsed);
            disconnect();
        }

        protected abstract void connect();
        protected abstract String[] fetchData();
        protected abstract String[] parseData(String[] raw);

        /** Hook — subclasses may override for custom formatting. */
        protected void generateReport(String[] records) {
            System.out.println("[Report] " + records.length + " record(s):");
            for (String r : records) {
                System.out.println("  - " + r);
            }
        }

        protected abstract void disconnect();
    }

    // ---------- Concrete pipelines ----------
    public static class CsvPipeline extends DataPipeline {
        private final String filePath;

        public CsvPipeline(String filePath) { this.filePath = filePath; }

        @Override
        protected void connect() {
            System.out.println("[CSV] Opening file: " + filePath);
        }

        @Override
        protected String[] fetchData() {
            // simulated CSV rows
            System.out.println("[CSV] Reading rows...");
            return new String[]{"Alice,30,Engineer", "Bob,25,Designer", "Carol,35,Manager"};
        }

        @Override
        protected String[] parseData(String[] raw) {
            String[] out = new String[raw.length];
            for (int i = 0; i < raw.length; i++) {
                String[] cols = raw[i].split(",");
                out[i] = cols[0] + " (age " + cols[1] + ", " + cols[2] + ")";
            }
            return out;
        }

        @Override
        protected void disconnect() {
            System.out.println("[CSV] File closed.");
        }
    }

    public static class ApiPipeline extends DataPipeline {
        private final String endpoint;

        public ApiPipeline(String endpoint) { this.endpoint = endpoint; }

        @Override
        protected void connect() {
            System.out.println("[API] Connecting to: " + endpoint);
        }

        @Override
        protected String[] fetchData() {
            System.out.println("[API] GET /users ...");
            return new String[]{
                "{\"id\":1,\"name\":\"Dave\",\"role\":\"Admin\"}",
                "{\"id\":2,\"name\":\"Eve\",\"role\":\"User\"}"
            };
        }

        @Override
        protected String[] parseData(String[] raw) {
            String[] out = new String[raw.length];
            for (int i = 0; i < raw.length; i++) {
                // naive parse: extract name and role values
                String name = raw[i].replaceAll(".*\"name\":\"([^\"]+)\".*", "$1");
                String role = raw[i].replaceAll(".*\"role\":\"([^\"]+)\".*", "$1");
                out[i] = name + " [" + role + "]";
            }
            return out;
        }

        @Override
        protected void generateReport(String[] records) {
            System.out.println("[API Report] === User Summary ===");
            super.generateReport(records);
            System.out.println("[API Report] === End ===");
        }

        @Override
        protected void disconnect() {
            System.out.println("[API] Connection closed.");
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        System.out.println("======= CSV Pipeline =======");
        new CsvPipeline("employees.csv").run();

        System.out.println("\n======= API Pipeline =======");
        new ApiPipeline("https://api.example.com").run();
    }
}

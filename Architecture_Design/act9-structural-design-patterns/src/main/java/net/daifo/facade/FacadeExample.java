package net.daifo.facade;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Facade Pattern Example
 *
 * Scenario: Onboarding a new employee at a company touches many internal
 * subsystems that each have their own quirks:
 *
 *     - HR registry        (assign employee id, store profile)
 *     - Payroll system     (enroll salary + tax bracket)
 *     - IT provisioning    (create account, email, VPN cert)
 *     - Access control     (grant building + system permissions by role)
 *     - Notification bus   (tell the team the new hire is starting)
 *
 * Any caller that wants to "hire someone" would otherwise need to know the
 * order of calls, which subsystem depends on which, and how to roll back on
 * partial failure. That knowledge leaks into every client.
 *
 * The {@link OnboardingFacade} hides all of this behind a single entry point:
 *
 *     facade.hire(new NewHireRequest(...));
 *
 * Clients get a simple, high-level API; the subsystems stay independent and
 * can still be used directly by power users who need finer control.
 */
public class FacadeExample {

    // ==========================================================
    // Subsystems (each is usable on its own — the facade doesn't
    // own them, it just orchestrates them)
    // ==========================================================

    public static class HrRegistry {
        private int nextId = 1000;
        private final Map<Integer, String> employees = new LinkedHashMap<>();

        public int register(String fullName, String role) {
            int id = nextId++;
            employees.put(id, fullName + " (" + role + ")");
            System.out.println("[HR] Registered #" + id + " -> " + employees.get(id));
            return id;
        }
    }

    public static class PayrollSystem {
        public void enroll(int employeeId, long annualSalaryCents, String taxBracket) {
            System.out.printf("[Payroll] id=%d salary=$%.2f bracket=%s%n",
                    employeeId, annualSalaryCents / 100.0, taxBracket);
        }
    }

    public static class ItProvisioning {
        public String createAccount(int employeeId, String fullName) {
            String handle = fullName.toLowerCase().replaceAll("[^a-z]+", ".")
                    .replaceAll("^\\.|\\.$", "");
            String email = handle + "@daifo.net";
            System.out.println("[IT] Account created for #" + employeeId
                    + " email=" + email);
            return email;
        }

        public void issueVpnCert(String email) {
            System.out.println("[IT] VPN cert issued to " + email);
        }
    }

    public static class AccessControl {
        public void grantRoleDefaults(int employeeId, String role) {
            List<String> perms = new ArrayList<>();
            perms.add("building:HQ");
            switch (role.toLowerCase()) {
                case "engineer":
                    perms.add("repo:read");
                    perms.add("repo:write");
                    perms.add("ci:trigger");
                    break;
                case "manager":
                    perms.add("repo:read");
                    perms.add("reports:view");
                    perms.add("approvals:sign");
                    break;
                default:
                    perms.add("repo:read");
            }
            System.out.println("[Access] #" + employeeId + " granted " + perms);
        }
    }

    public static class NotificationBus {
        public void announce(String message) {
            System.out.println("[Notify] " + message);
        }
    }

    // ==========================================================
    // Facade input DTO
    // ==========================================================
    public static class NewHireRequest {
        public final String fullName;
        public final String role;
        public final long annualSalaryCents;
        public final String taxBracket;
        public final boolean remote;

        public NewHireRequest(String fullName, String role,
                              long annualSalaryCents, String taxBracket,
                              boolean remote) {
            this.fullName = fullName;
            this.role = role;
            this.annualSalaryCents = annualSalaryCents;
            this.taxBracket = taxBracket;
            this.remote = remote;
        }
    }

    public static class NewHireResult {
        public final int employeeId;
        public final String email;

        public NewHireResult(int employeeId, String email) {
            this.employeeId = employeeId;
            this.email = email;
        }

        @Override
        public String toString() {
            return "NewHire{id=" + employeeId + ", email=" + email + "}";
        }
    }

    // ==========================================================
    // Facade
    // ==========================================================
    public static class OnboardingFacade {
        private final HrRegistry hr;
        private final PayrollSystem payroll;
        private final ItProvisioning it;
        private final AccessControl access;
        private final NotificationBus notify;

        public OnboardingFacade(HrRegistry hr, PayrollSystem payroll,
                                ItProvisioning it, AccessControl access,
                                NotificationBus notify) {
            this.hr = hr;
            this.payroll = payroll;
            this.it = it;
            this.access = access;
            this.notify = notify;
        }

        /** One call encapsulates the entire multi-subsystem workflow. */
        public NewHireResult hire(NewHireRequest req) {
            if (req == null || req.fullName == null || req.fullName.isBlank()) {
                throw new IllegalArgumentException("fullName required");
            }
            if (req.annualSalaryCents <= 0) {
                throw new IllegalArgumentException("salary must be positive");
            }

            int id = hr.register(req.fullName, req.role);
            payroll.enroll(id, req.annualSalaryCents, req.taxBracket);

            String email = it.createAccount(id, req.fullName);
            if (req.remote) {
                it.issueVpnCert(email);
            }

            access.grantRoleDefaults(id, req.role);
            notify.announce("Welcome " + req.fullName + " (" + req.role
                    + ") — reachable at " + email);

            return new NewHireResult(id, email);
        }
    }

    // ==========================================================
    // Client
    // ==========================================================
    public static void main(String[] args) {
        OnboardingFacade onboarding = new OnboardingFacade(
                new HrRegistry(),
                new PayrollSystem(),
                new ItProvisioning(),
                new AccessControl(),
                new NotificationBus());

        System.out.println("=== Hiring engineer (remote) ===");
        NewHireResult r1 = onboarding.hire(new NewHireRequest(
                "Ada Lovelace", "engineer", 14_500_000L, "B2", true));
        System.out.println(" -> " + r1);

        System.out.println();
        System.out.println("=== Hiring manager (on-site) ===");
        NewHireResult r2 = onboarding.hire(new NewHireRequest(
                "Grace Hopper", "manager", 18_000_000L, "A1", false));
        System.out.println(" -> " + r2);
    }
}

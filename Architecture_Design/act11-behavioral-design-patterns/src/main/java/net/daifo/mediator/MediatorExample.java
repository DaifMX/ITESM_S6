package net.daifo.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Mediator Pattern Example
 *
 * Scenario: An air-traffic control system where multiple aircraft communicate
 * only through a central {@link ControlTower} (mediator) rather than
 * coordinating directly with each other.
 *
 * Without the mediator each aircraft would need to know about every other
 * aircraft to coordinate landings. With the mediator, each {@link Aircraft}
 * only references the tower, and the tower encapsulates all routing logic:
 * which runway is free, who has permission to land, etc.
 *
 * This reduces the N*(N-1) direct communication links to N indirect ones,
 * and the coordination logic lives in one place.
 */
public class MediatorExample {

    // ---------- Mediator interface ----------
    public interface AirTrafficControl {
        void registerAircraft(Aircraft aircraft);
        void requestLanding(Aircraft aircraft);
        void notifyRunwayClear(Aircraft aircraft);
    }

    // ---------- Colleague ----------
    public static class Aircraft {
        private final String callSign;
        private final AirTrafficControl tower;

        public Aircraft(String callSign, AirTrafficControl tower) {
            this.callSign = callSign;
            this.tower = tower;
            tower.registerAircraft(this);
        }

        public String getCallSign() { return callSign; }

        public void requestLanding() {
            System.out.println("[" + callSign + "] Requesting landing clearance.");
            tower.requestLanding(this);
        }

        public void land() {
            System.out.println("[" + callSign + "] Landing... touchdown!");
            tower.notifyRunwayClear(this);
        }

        public void receiveHoldInstruction() {
            System.out.println("[" + callSign + "] Tower: hold position, runway occupied.");
        }

        public void receiveLandingClearance() {
            System.out.println("[" + callSign + "] Tower: cleared to land.");
            land();
        }
    }

    // ---------- Concrete Mediator ----------
    public static class ControlTower implements AirTrafficControl {
        private final List<Aircraft> registered = new ArrayList<>();
        private final List<Aircraft> waitingQueue = new ArrayList<>();
        private boolean runwayOccupied = false;

        @Override
        public void registerAircraft(Aircraft aircraft) {
            registered.add(aircraft);
            System.out.println("[Tower] Registered " + aircraft.getCallSign());
        }

        @Override
        public void requestLanding(Aircraft aircraft) {
            if (!runwayOccupied) {
                runwayOccupied = true;
                aircraft.receiveLandingClearance();
            } else {
                waitingQueue.add(aircraft);
                aircraft.receiveHoldInstruction();
            }
        }

        @Override
        public void notifyRunwayClear(Aircraft aircraft) {
            System.out.println("[Tower] Runway clear after " + aircraft.getCallSign());
            if (!waitingQueue.isEmpty()) {
                Aircraft next = waitingQueue.remove(0);
                System.out.println("[Tower] Clearing " + next.getCallSign() + " to land.");
                next.receiveLandingClearance();
            } else {
                runwayOccupied = false;
            }
        }
    }

    // ---------- Client ----------
    public static void main(String[] args) {
        ControlTower tower = new ControlTower();

        Aircraft ua101 = new Aircraft("UA101", tower);
        Aircraft ba202 = new Aircraft("BA202", tower);
        Aircraft af303 = new Aircraft("AF303", tower);

        System.out.println("\n-- Landing sequence --");
        ua101.requestLanding();
        ba202.requestLanding(); // runway busy — hold
        af303.requestLanding(); // runway busy — hold
        // UA101 lands, tower auto-clears BA202, then AF303
    }
}

package flight.system.FlightManager;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private Map map;
    private List<Flight> flights;

    public Trip() {
        this.map = new Map();
        this.flights = new ArrayList<Flight>();
    }

    protected boolean addFlight(Flight flight) {
        if (flights.contains(flight)) {
            return false;
        }
        flights.add(flight);
        return true;
    }

}

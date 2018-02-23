package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import ca.ubc.cs.cpsc210.translink.util.SphericalGeometry;


import java.util.*;

/**
 * Manages all bus stops.
 * <p>
 * Singleton pattern applied to ensure only a single instance of this class that
 * is globally accessible throughout application.
 */


public class StopManager implements Iterable<Stop> {
    public static final int RADIUS = 10000;
    private static StopManager instance;
    // Use this field to hold all of the stops.
    // Do not change this field or its type, as the iterator method depends on it
    private Map<Integer, Stop> stopMap;
    private Stop selectedstop;

    private Stop newStop;
    private LatLon anyloc;


    /**
     * Constructs stop manager with empty collection of stops and null as the selected stop
     */
    private StopManager() {

        stopMap = new HashMap<>();
        selectedstop = null;

    }

    /**
     * Gets one and only instance of this class
     *
     * @return instance of class
     */
    public static StopManager getInstance() {
        // Do not modify the implementation of this method!
        if (instance == null) {
            instance = new StopManager();
        }

        return instance;
    }

    public Stop getSelected() {
        return selectedstop;
    }

    /**
     * Get stop with given id, creating it if necessary. If it is necessary to create a new stop,
     * then provide it with an empty string as its name, and a default location somewhere in the
     * lower mainland as its location.
     * <p>
     * In this case, the correct name and location of the stop will be provided later
     *
     * @param id the id of this stop
     * @return stop with given id
     */
    public Stop getStopWithId(int id) {
        if (stopMap.containsKey(id)) {
            return stopMap.get(id);
        } else
            anyloc = new LatLon(12.1,22.1);
        newStop = new Stop(id, "", anyloc);
        stopMap.put(id, newStop);
        return newStop;
    }

    /**
     * Get stop with given id, creating it if necessary, using the given name and latlon
     *
     * @param id the id of this stop
     * @return stop with given id
     */
    public Stop getStopWithId(int id, String name, LatLon locn) {
        if (stopMap.containsKey(id)) {
            return stopMap.get(id);
        } else
            newStop = new Stop(id, name, locn);
        stopMap.put(id,newStop);
        return newStop;
    }


    /**
     * Set the stop selected by user
     *
     * @param selected stop selected by user
     * @throws StopException when stop manager doesn't contain selected stop
     */
    public void setSelected(Stop selected) throws StopException {
        if (stopMap.containsKey(selected.getNumber())) {
            selectedstop = selected;
        } else
            throw new StopException("No such stop: " + selected.getNumber() + " " + selected.getName());
    }

    /**
     * Clear selected stop (selected stop is null)
     */
    public void clearSelectedStop() {
        selectedstop = null;
    }

    /**
     * Get number of stops managed
     *
     * @return number of stops added to manager
     */
    public int getNumStops() {
        return stopMap.size();
    }

    /**
     * Remove all stops from stop manager
     */
    public void clearStops() {
        stopMap.clear();

    }

    /**
     * Find nearest stop to given point.  Returns null if no stop is closer than RADIUS metres.
     *
     * @param pt point to which nearest stop is sought
     * @return stop closest to pt but less than 10,000m away; null if no stop is within RADIUS metres of pt
     */
    public Stop findNearestTo(LatLon pt) {
        Stop closestStopSoFar = null;
        double r = RADIUS;
        Double ClosestDistance = r;
        for(Stop value : stopMap.values()) {
            Double Distance = SphericalGeometry.distanceBetween(value.getLocn(), pt);
            if (Distance < ClosestDistance) {
                ClosestDistance = Distance;
                closestStopSoFar = value;
            }}
        return closestStopSoFar;
    }

    @Override
    public Iterator<Stop> iterator() {
        // Do not modify the implementation of this method!
        return stopMap.values().iterator();
    }
}

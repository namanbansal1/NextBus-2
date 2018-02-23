package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.util.LatLon;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A description of one pattern of a route
 * Each pattern has a name, destination, direction, list of points (of class LatLon), and Route
 */



public class RoutePattern {
    private Route route1;
    private List<LatLon> listofpoints;
    private String name1;
    private String destination1;
    private String direction1;
    private final static int HASH_MULTIPLER = 11;


    /**
     * Construct a new RoutePattern with the given information
     *
     * @param name        the name of the pattern
     * @param destination the destination
     * @param direction   the direction
     * @param route       the Route of which this is a pattern
     */
    public RoutePattern(String name, String destination, String direction, Route route) {
        name1 = name;
        destination1=destination;
        direction1=direction;
        route1=route;
        listofpoints = new ArrayList<>();
    }

    /**
     * Get the pattern name
     *
     * @return the name
     */
    public String getName() {
        return name1;
    }

    /**
     * Get the pattern destination
     *
     * @return the destination
     */
    public String getDestination() {
        return destination1;
    }

    /**
     * Get the pattern direction
     *
     * @return the direction
     */
    public String getDirection() {
        return direction1;
    }

    /**
     * Decide if two RoutePatterns are equal. Two route patterns are equal if their names are equal.
     *
     * @param o the other route pattern to compare to
     * @return true if this is equal to o
     */
    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if( getClass() != o.getClass()){
            return false;
        }
        RoutePattern r1 = (RoutePattern) o;
        return this.name1.equals(r1.name1);
    }

    @Override
    public int hashCode() {
        return  name1.length() *HASH_MULTIPLER;
    }

    /**
     * Set the pattern path: list of coordinates
     *
     * @param path the path
     */
    public void setPath(List<LatLon> path) {
        for(LatLon next: path){
            listofpoints.add(next);
        }
    }

    /**
     * Return the list of coordinates making up this pattern
     *
     * @return the list of coordinates
     */
    public List<LatLon> getPath() {
        return  Collections.unmodifiableList(listofpoints);
    }

    /**
     * Set the direction
     *
     * @param direction the direction
     */
    public void setDirection(String direction) {
        direction1=direction;
    }

    /**
     * Set the destination
     *
     * @param destination the destination
     */
    public void setDestination(String destination) {
        destination1=destination;
    }
}

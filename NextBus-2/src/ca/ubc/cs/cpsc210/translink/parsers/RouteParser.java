package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;


/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }

    /**
     * Parse route data from the file and add all route to the route manager.
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }

    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param jsonResponse string encoding JSON data to be parsed
     * @throws JSONException             when JSON data does not have expected format
     * @throws RouteDataMissingException when
     *                                   <ul>
     *                                   <li> JSON data is not an array </li>
     *                                   <li> JSON data is missing RouteNo, Name, or Patterns elements for any route</li>
     *                                   <li> The value of the Patterns element is not an array for any route</li>
     *                                   <li> JSON data is missing PatternNo, Destination, or Direction elements for any route pattern</li>
     *                                   </ul>
     */


    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {

        JSONArray allroutes = new JSONArray(jsonResponse);
        for (int index = 0; index < allroutes.length(); index++) {
            JSONObject route = allroutes.getJSONObject(index);
            parseRoute(route);
        }
    }


    private void parseRoute(JSONObject route) throws JSONException, RouteDataMissingException {

        if (!route.has("RouteNo") && route.isNull("RouteNo"))
            throw new RouteDataMissingException();
        if (!route.has("Name") && route.isNull("Name"))
            throw new RouteDataMissingException();
        if (!route.has("Patterns") && route.isNull("Patterns"))
            throw new RouteDataMissingException();

        String RouteNumber = route.getString("RouteNo");
        String RouteName = route.getString("Name");

        try {
            JSONArray ListOfRP = route.getJSONArray("Patterns");
        }catch (JSONException e)
        {
            throw new RouteDataMissingException();
        }
        JSONArray ListOfRP = route.getJSONArray("Patterns");
        for (int index = 0; index < ListOfRP.length(); index++) {
            JSONObject OnePattern = ListOfRP.getJSONObject(index);
            if (!OnePattern.has("PatternNo") && OnePattern.isNull("PatternNo"))
                throw new RouteDataMissingException();
            if (!OnePattern.has("Destination") && OnePattern.isNull("Destination"))
                throw new RouteDataMissingException();
            if (!OnePattern.has("Direction") && OnePattern.isNull("Direction"))
                throw new RouteDataMissingException();

            String RoutePatternNumber = OnePattern.getString("PatternNo");
            String Destination = OnePattern.getString("Destination");
            String Direction = OnePattern.getString("Direction");
            storeRoute(RouteNumber, RouteName, RoutePatternNumber, Destination, Direction);
        }

    }

    private void storeRoute(String RouteNumber1, String RouteName1, String RoutePatternNumber,
                            String Destination1, String Direction1) {
        Route r = RouteManager.getInstance().getRouteWithNumber(RouteNumber1, RouteName1);
        RoutePattern rp1 = r.getPattern(RoutePatternNumber, Destination1, Direction1);
    }

}



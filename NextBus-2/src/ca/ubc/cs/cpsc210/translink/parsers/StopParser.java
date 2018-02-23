package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }

    /**
     * Parse stop data from the file and add all stops to stop manager.
     */
    public void parse() throws IOException, StopDataMissingException, JSONException {
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }

    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param jsonResponse string encoding JSON data to be parsed
     * @throws JSONException            when JSON data does not have expected format
     * @throws StopDataMissingException when
     *                                  <ul>
     *                                  <li> JSON data is not an array </li>
     *                                  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     *                                  </ul>
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {

        JSONArray allstops = new JSONArray(jsonResponse);
        for (int index = 0; index < allstops.length(); index++) {
            JSONObject stop = allstops.getJSONObject(index);
            parseStop(stop);
        }
    }


    private void parseStop(JSONObject stop) throws JSONException, StopDataMissingException {


        if(!stop.has("Name") && stop.isNull("Name"))
            throw new StopDataMissingException();

        if(!stop.has("StopNo") && stop.isNull("StopNo"))
            throw new StopDataMissingException();

        if(!stop.has("Latitude") && stop.isNull("Latitude"))
            throw new StopDataMissingException();

        if(!stop.has("Longitude") && stop.isNull("Longitude"))
            throw new StopDataMissingException();

        if(!stop.has("Routes") && stop.isNull("Routes"))
            throw new StopDataMissingException();


        String StopName = stop.getString("Name");
        int StopNumber = stop.getInt("StopNo");
        Double Latitude = stop.getDouble("Latitude");
        Double Longitude = stop.getDouble("Longitude");
        String Routes = stop.getString("Routes");
        List<String> mylist = new ArrayList<>(Arrays.asList(Routes.split(", ")));
        for (String next : mylist) {
            storeStop(StopName, StopNumber, Latitude, Longitude, next);
        }

    }


    private void storeStop(String StopName, int StopNumber, Double Latitude,
                           Double Longitude, String Routes) {

        LatLon latLon = new LatLon(Latitude, Longitude);
        Stop s = StopManager.getInstance().getStopWithId(StopNumber, StopName, latLon);
        Route r = RouteManager.getInstance().getRouteWithNumber(Routes);
        s.addRoute(r);

    }
}

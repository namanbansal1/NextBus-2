package ca.ubc.cs.cpsc210.translink.parsers;


import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {


    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when JSON response does not have expected format
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {

        JSONArray allarrivals = new JSONArray(jsonResponse);
        for (int index = 0; index < allarrivals.length(); index++) {
            JSONObject arrival = allarrivals.getJSONObject(index);
            ArrivalsParser.parseArrival1(stop,arrival);

        }
    }


    private static void parseArrival1(Stop st, JSONObject arrival) throws JSONException,
            ArrivalsDataMissingException {

        if(ArrivalsParser.parsw1(st, arrival))
            throw new ArrivalsDataMissingException();
        else
            ArrivalsParser.parseArrival(st,arrival);
    }


    public static boolean parsw1(Stop st, JSONObject arrival) throws JSONException  {

        JSONArray Schedule = arrival.getJSONArray("Schedules");
        for (int index1 = 0; index1 < Schedule.length(); index1++) {
            JSONObject sch = Schedule.getJSONObject(index1);
            if ((sch.has("ExpectedCountdown") && !sch.isNull("ExpectedCountdown"))
                    && (sch.has("Destination") && !sch.isNull("Destination")) &&
                    (sch.has("ScheduleStatus") && !sch.isNull("ScheduleStatus"))) {
                return false;
            }
        }

        return true;
    }

    private static void parseArrival(Stop st, JSONObject arrival) throws JSONException,
            ArrivalsDataMissingException {

        if(!arrival.has("RouteNo") && arrival.isNull("RouteNo")){
            throw new ArrivalsDataMissingException();
        }

        String Route = arrival.getString("RouteNo");
        JSONArray Schedule = arrival.getJSONArray("Schedules");
        List<Arrival> test = new ArrayList<>();
        for (int index = 0; index < Schedule.length(); index++) {
            JSONObject sch = Schedule.getJSONObject(index);
            int ExpectedCountdown = sch.getInt("ExpectedCountdown");
            String Destination = sch.getString("Destination");
            String scheduleStatus = sch.getString("ScheduleStatus");
            Route a = RouteManager.getInstance().getRouteWithNumber(Route);
            Arrival c = new Arrival(ExpectedCountdown, Destination, a);
            c.setStatus(scheduleStatus);
            st.addArrival(c);
            test.add(c);
        }
    }

}






package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     *
     * @param str
     */
    private void parseOnePattern(String str) {


        String routeNumber = "";
        Pattern checkRegex = Pattern.compile("N[A-Z|0-9]{1,3}-");
        Matcher regexMatcher = checkRegex.matcher(str);

        while (regexMatcher.find()) {

            routeNumber = regexMatcher.group().substring(1, regexMatcher.group().length() - 1);

        }

        String patternName = "";
        Pattern checkRegex2 = Pattern.compile("[N|S|E|W]B[A-Z|0-9]{1,6}|[N|S|E|W][A-Z|0-9]-[A-Z|0-9]*|[N|S|E|W][A-Z|0-9]*;");
        Matcher regexMatcher2 = checkRegex2.matcher(str);

        while (regexMatcher2.find()) {

            if (regexMatcher2.group().contains(";")) {
                patternName = regexMatcher2.group().substring(0, regexMatcher2.group().length() - 1);
            } else

                patternName = regexMatcher2.group();
        }

        LatLon latLon;
        List<LatLon> listoflatlon = new ArrayList<>();
        Pattern checkRegex3 = Pattern.compile("-122.[0-9]{3,6}|-123.[0-9]{3,6}|-121.[0-9]{3,6}|-124.[0-9]{3,6}");
        Matcher regexMatcher3 = checkRegex3.matcher(str);

        Pattern checkRegex4 = Pattern.compile("49.[0-9]{3,6}|48.[0-9]{3,6}|50.[0-9]{3,6}");
        Matcher regexMatcher4 = checkRegex4.matcher(str);


        while (regexMatcher3.find() && regexMatcher4.find()) {

            Double lat ;
            Double lon;

            lat = Double.parseDouble(regexMatcher4.group());
            lon = Double.parseDouble(regexMatcher3.group());

            latLon = new LatLon(lat, lon);
            listoflatlon.add(latLon);

        }
        if(routeNumber.isEmpty()&& patternName.isEmpty() )
        {

        }else
            storeRouteMap(routeNumber, patternName, listoflatlon);
    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber the number of the route
     * @param patternName the name of the pattern
     * @param elements    the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}

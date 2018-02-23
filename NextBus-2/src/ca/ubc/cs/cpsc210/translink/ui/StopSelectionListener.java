package ca.ubc.cs.cpsc210.translink.ui;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.StopException;

/**
 * Handles user selection of stop on map
 */
public interface StopSelectionListener {

    /**
     * Called when user selects a stop
     *
     * @param stop   stop selected by user
     */
    void onStopSelected(Stop stop) throws StopException;
    /**
     * Called when user asks for more info about a stop
     *
     * @param stop   stop for which more info is being requested by user
     */
    void onStopMoreInfo(Stop stop);
}

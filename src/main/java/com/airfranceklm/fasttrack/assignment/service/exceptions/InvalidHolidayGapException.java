package com.airfranceklm.fasttrack.assignment.service.exceptions;

/**
 * Custom exception thrown when the start date of a holiday is less than 5 days from the current date.
 */
public class InvalidHolidayGapException extends InvalidHolidayException {

    public InvalidHolidayGapException() {
        super("There should be a gap of at least 3 working days between holidays");
    }
}


package com.airfranceklm.fasttrack.assignment.service.exceptions;

/**
 * Custom exception thrown when the start date of a holiday is less than 5 days from the current date.
 */
public class InvalidHolidayOverlapException extends InvalidHolidayException {

    public InvalidHolidayOverlapException() {
        super("Holidays must not overlap.");
    }
}


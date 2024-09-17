package com.airfranceklm.fasttrack.assignment.service.exceptions;

/**
 * Custom exception thrown when the start date of a holiday is less than 5 days from the current date.
 */
public class InvalidHolidayStartDateException extends InvalidHolidayException {

    public InvalidHolidayStartDateException() {
        super("Start of holiday must be at least 5 days from today.");
    }
}


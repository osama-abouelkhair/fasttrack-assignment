package com.airfranceklm.fasttrack.assignment.service.exceptions;

/**
 * Custom exception thrown when the start date of a holiday is less than 5 days from the current date.
 */
public abstract class InvalidHolidayException extends RuntimeException {

    public InvalidHolidayException(String message) {
        super(message);
    }
}


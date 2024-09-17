package com.airfranceklm.fasttrack.assignment.service.exceptions;

/**
 * Custom exception thrown when the start date of a holiday is less than 5 days from the current date.
 */
public class InvalidHolidayCancellationException extends InvalidHolidayException {

    public InvalidHolidayCancellationException() {
        super("A holiday must be cancelled at least 5 working days before the start date.");
    }
}


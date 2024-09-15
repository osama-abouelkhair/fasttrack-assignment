package com.airfranceklm.fasttrack.assignment.controller;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.airfranceklm.fasttrack.assignment.resources.Holiday;

import javax.validation.Valid;

/**
 * HolidaysApi is a REST controller that handles HTTP requests related to employee holidays.
 * It provides endpoints to retrieve, create, update, and delete holiday information.
 *
 * <p>The class is annotated with {@link RestController} and {@link RequestMapping} to handle
 * requests under the "/holidays" path. It integrates with the OpenAPI specification
 * using {@link Operation} annotations to provide API documentation for each endpoint.
 */
@RestController
@RequestMapping("/holidays")
public class HolidaysApi {

    /**
     * Retrieves the list of holidays for a given employee based on their employee ID.
     *
     * <p>This endpoint accepts a query parameter {@code employeeId} and returns a list of
     * holidays associated with that employee. The method generates mock holiday data for demonstration purposes.
     *
     * <p>HTTP Status: {@code 200 OK} on success.
     *
     * @param employeeId the ID of the employee for whom the holidays are being fetched
     * @return a list of {@link Holiday} objects representing the holidays of the employee
     */
    @Operation(summary = "Get an employee holidays",
            description = "Returns holidays as per the employee id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Holiday> getHolidays(@RequestParam String employeeId) {
        return List.of(new Holiday(UUID.randomUUID().toString(),
                "label",
                "klm-012",
                Instant.now().atOffset(ZoneOffset.UTC).toString(),
                Instant.now().atOffset(ZoneOffset.UTC).toString(),
                "DRAFT"));
    }

    /**
     * Creates a new holiday for an employee.
     *
     * <p>This endpoint accepts a holiday object in the request body and creates a new holiday record.
     * The request body is validated using {@code @Valid}, and the method returns the created holiday object.
     *
     * <p>HTTP Status: {@code 201 Created} on success.
     *
     * @param holiday the {@link Holiday} object containing the holiday details to be created
     * @return the created {@link Holiday} object with the same details provided in the request
     */
    @Operation(
            summary = "Creates a holiday",
            description = "Creates a holiday by providing the holiday details and returns the created holiday.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Holiday createHoliday(@Valid @RequestBody Holiday holiday) {
        OffsetDateTime date = OffsetDateTime.parse(holiday.getStartOfHoliday());
        return holiday;
    }

    /**
     * Updates an existing holiday with new details.
     *
     * <p>This endpoint accepts an updated holiday object in the request body and modifies an existing holiday record.
     * The method returns the updated holiday object.
     *
     * <p>HTTP Status: {@code 201 Created} on success.
     *
     * @param holiday the {@link Holiday} object containing the updated holiday details
     * @return the updated {@link Holiday} object with the modified details
     */
    @Operation(
            summary = "Updates a holiday",
            description = "Updates a holiday by providing updated holiday details and returns the updated holiday.")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Holiday updateHoliday(@RequestBody Holiday holiday) {
        return holiday;
    }


    /**
     * Deletes a holiday.
     *
     * <p>This endpoint deletes an existing holiday based on its ID or other identifier.
     * It returns no content upon successful deletion.
     *
     * <p>HTTP Status: {@code 204 No Content} on success.
     */
    @Operation(
            summary = "Delete a holiday",
            description = "Deletes a holiday without returning any content.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHoliday() {
    }
}

package com.airfranceklm.fasttrack.assignment.controller;

import com.airfranceklm.fasttrack.assignment.entity.Status;
import com.airfranceklm.fasttrack.assignment.resources.HolidayDTO;
import com.airfranceklm.fasttrack.assignment.service.HolidaysService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(HolidaysApi.class)
@ExtendWith(MockitoExtension.class)
public class HolidaysApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidaysService holidaysService;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void getHolidaysSuccessfully() throws Exception {
        // Arrange
        String holidayId = UUID.randomUUID().toString();
        String label = "label";
        String employeeId = "klm012345";
        String startOfHoliday = Instant.now().toString();
        String endOfHoliday = Instant.now().plus(3, ChronoUnit.HOURS).toString();
        String status = Status.DRAFT.toString();

        // Mock service response
        List<HolidayDTO> expectedHolidays = List.of(
                new HolidayDTO(holidayId, label, employeeId, startOfHoliday, endOfHoliday, status)
        );

        // Mock the service method call
        Mockito.when(holidaysService.geyHolidays(Mockito.anyString()))
                .thenReturn(expectedHolidays);

        // Act & Assert
        mockMvc.perform(get("/holidays")
                        .param("employeeId", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Ensure HTTP status is 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Ensure content type is JSON

                // Verify the response structure
                .andExpect(jsonPath("$").isArray())  // Ensure the response is an array
                .andExpect(jsonPath("$[0].holidayId").value(holidayId))  // Check holidayId field
                .andExpect(jsonPath("$[0].holidayLabel").value(label))  // Check holidayLabel field
                .andExpect(jsonPath("$[0].employeeId").value(employeeId))  // Check employeeId field
                .andExpect(jsonPath("$[0].startOfHoliday").value(startOfHoliday))  // Check startOfHoliday field
                .andExpect(jsonPath("$[0].endOfHoliday").value(endOfHoliday))  // Check endOfHoliday field
                .andExpect(jsonPath("$[0].status").value(status));  // Check status field

        // Verify that the service was called with the correct employeeId parameter
        Mockito.verify(holidaysService).geyHolidays(Mockito.eq(employeeId));
    }


    @Test
    public void shouldCreateHolidaySuccessfully() throws Exception {
        // Arrange
        String startOfHoliday = Instant.now().toString();
        String endOfHoliday = Instant.now().plus(3, ChronoUnit.HOURS).toString();
        String label = "label";
        String employeeId = "klm012345";
        String status = Status.DRAFT.toString();

        // Expected holidayId for the response
        String expectedHolidayId = UUID.randomUUID().toString();

        // Mock the service method behavior
        HolidayDTO expectedHolidayResponse = new HolidayDTO(
                expectedHolidayId,
                label,
                employeeId,
                startOfHoliday,
                endOfHoliday,
                status
        );

        Mockito.when(holidaysService.createHoliday(Mockito.any(HolidayDTO.class)))
                .thenReturn(expectedHolidayResponse);

        // Build the request DTO
        HolidayDTO holidayDTORequest = new HolidayDTO(
                label,
                employeeId,
                startOfHoliday,
                endOfHoliday,
                status
        );

        // Act & Assert
        mockMvc.perform(post("/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(holidayDTORequest)))
                .andExpect(status().isCreated())  // Check the HTTP status code
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Check response content type

                // Check the JSON response fields
                .andExpect(jsonPath("$.holidayId").value(expectedHolidayId))
                .andExpect(jsonPath("$.holidayLabel").value(label))
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.startOfHoliday").value(startOfHoliday))
                .andExpect(jsonPath("$.endOfHoliday").value(endOfHoliday))
                .andExpect(jsonPath("$.status").value(status));

        // Verify that the service was called with the correct argument
        Mockito.verify(holidaysService).createHoliday(Mockito.argThat(holidayDTO ->
                holidayDTO.getHolidayLabel().equals(label) &&
                        holidayDTO.getEmployeeId().equals(employeeId) &&
                        holidayDTO.getStartOfHoliday().equals(startOfHoliday) &&
                        holidayDTO.getEndOfHoliday().equals(endOfHoliday) &&
                        holidayDTO.getStatus().equals(status)
        ));
    }

    @Test
    public void updateHolidaySuccessfully() throws Exception {
        // Arrange
        String holidayId = UUID.randomUUID().toString();
        String label = "updatedLabel";
        String employeeId = "klm012345";
        String startOfHoliday = Instant.now().toString();
        String endOfHoliday = Instant.now().plus(3, ChronoUnit.HOURS).toString();
        String status = Status.SCHEDULED.toString();  // Assuming status changes to "APPROVED"

        // Mock service response
        HolidayDTO expectedUpdatedHoliday = new HolidayDTO(
                holidayId,
                label,
                employeeId,
                startOfHoliday,
                endOfHoliday,
                status
        );

        // Mock the service method call
        Mockito.when(holidaysService.updateHoliday(Mockito.any(HolidayDTO.class)))
                .thenReturn(expectedUpdatedHoliday);

        // Build the request DTO (data that the controller expects)
        HolidayDTO holidayDTORequest = new HolidayDTO(
                holidayId,
                label,
                employeeId,
                startOfHoliday,
                endOfHoliday,
                status
        );

        // Act & Assert
        mockMvc.perform(put("/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(holidayDTORequest)))
                .andExpect(status().isCreated())  // Ensure HTTP status is 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Ensure content type is JSON

                // Verify the response fields
                .andExpect(jsonPath("$.holidayId").value(holidayId))  // Check holidayId field
                .andExpect(jsonPath("$.holidayLabel").value(label))  // Check holidayLabel field
                .andExpect(jsonPath("$.employeeId").value(employeeId))  // Check employeeId field
                .andExpect(jsonPath("$.startOfHoliday").value(startOfHoliday))  // Check startOfHoliday field
                .andExpect(jsonPath("$.endOfHoliday").value(endOfHoliday))  // Check endOfHoliday field
                .andExpect(jsonPath("$.status").value(status));  // Check status field

        // Verify that the service was called with the correct HolidayDTO
        Mockito.verify(holidaysService).updateHoliday(Mockito.argThat(holidayDTO ->
                holidayDTO.getHolidayId().equals(holidayId) &&
                        holidayDTO.getHolidayLabel().equals(label) &&
                        holidayDTO.getEmployeeId().equals(employeeId) &&
                        holidayDTO.getStartOfHoliday().equals(startOfHoliday) &&
                        holidayDTO.getEndOfHoliday().equals(endOfHoliday) &&
                        holidayDTO.getStatus().equals(status)
        ));
    }

    @Test
    public void deleteHolidaySuccessfully() throws Exception {
        // Arrange
        String holidayId = UUID.randomUUID().toString();

        // Mock service behavior (no return value for void method)
        Mockito.doNothing().when(holidaysService).deleteHoliday(UUID.fromString(holidayId));

        // Act & Assert
        mockMvc.perform(delete("/holidays")
                        .param("holidayId", holidayId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());  // Ensure HTTP status is 204 No Content

        // Verify that the service method was called with the correct UUID
        Mockito.verify(holidaysService).deleteHoliday(UUID.fromString(holidayId));
    }

    @Test
    public void handleMethodArgumentNotValid_ShouldReturnBadRequest() throws Exception {
        // Arrange: Mock validation error response
        String invalidField = "startOfHoliday";
        String errorMessage = "must not be null";

        // Create a dummy HolidayDTO with invalid data to trigger the validation error
        HolidayDTO invalidHoliday = new HolidayDTO(
                null,  // Holiday ID (can be null)
                "label",
                "klm012345",
                null,  // startOfHoliday (invalid, null)
                Instant.now().plus(3, ChronoUnit.HOURS).toString(),
                Status.DRAFT.toString()
        );

        // Act & Assert: Perform POST request with invalid data
        mockMvc.perform(post("/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalidHoliday)))
                .andExpect(status().isBadRequest())  // Expect 400 Bad Request
                .andExpect(jsonPath("$.errors").isArray())  // Ensure 'errors' field is an array
                .andExpect(jsonPath("$.errors[0]").value(invalidField + " " + errorMessage));  // Check error message for specific field
    }

    @Test
    public void handleDateTimeParseException_ShouldReturnBadRequest() throws Exception {
        // Arrange: Simulate a DateTimeParseException in the service
        String invalidDate = "invalid-date-format";

        // Mock the service to throw DateTimeParseException when creating a holiday
        Mockito.when(holidaysService.createHoliday(Mockito.any(HolidayDTO.class)))
                .thenThrow(DateTimeParseException.class);

        // Create a valid HolidayDTO but with an invalid startOfHoliday date string
        HolidayDTO holidayDTO = new HolidayDTO(
                UUID.randomUUID().toString(),
                "label",
                "klm012345",
                invalidDate,  // Invalid date format
                Instant.now().plus(3, ChronoUnit.HOURS).toString(),
                Status.DRAFT.toString()
        );

        // Act & Assert: Perform POST request with invalid date format
        mockMvc.perform(post("/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(holidayDTO)))
                .andExpect(status().isBadRequest())  // Expect 400 Bad Request
                .andExpect(jsonPath("$.errors").isArray())  // Ensure 'errors' field is an array
                .andExpect(jsonPath("$.errors[0]").value("DateTime format is wrong"));  // Check for specific error message
    }

}


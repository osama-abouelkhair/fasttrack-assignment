package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.entity.Holiday;
import com.airfranceklm.fasttrack.assignment.entity.Status;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import com.airfranceklm.fasttrack.assignment.resources.HolidayDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * HolidaysService is a service class that provides business logic related to employee holidays.
 *
 * <p>This class manages CRUD (Create, Read, Update, Delete) operations for holidays in the system. It interacts with
 * holiday-related resources and handles tasks such as creating, retrieving, updating, and deleting holiday records.
 *
 * <p>The service is annotated with {@link Service}, making it a candidate for Spring's component scanning to detect
 * and register it as a Spring bean in the application context. This allows the service to be injected and used
 * in other components, such as controllers.
 */
@Service
@AllArgsConstructor
public class HolidaysService {

    private HolidayRepository holidayRepository;

    private ModelMapper modelMapper;

    /**
     * Retrieves a list of holidays for a specific employee based on their employee ID.
     *
     * <p>This method queries the {@link HolidayRepository} to find all holidays associated with the provided
     * employee ID. The list of holiday entities is then converted into {@link HolidayDTO} objects,
     * which are returned as the result.
     *
     * @param employeeId the unique identifier of the employee whose holidays are being fetched
     * @return a list of {@link HolidayDTO} objects representing the holidays of the employee
     */
    public List<HolidayDTO> geyHolidays(String employeeId) {
        return holidayRepository.findByEmployeeId(employeeId)
                .stream()
                .map(holiday -> new HolidayDTO(
                        holiday.getId().toString(),
                        holiday.getLabel(),
                        holiday.getEmployeeId(),
                        holiday.getStartOfHoliday().toString(),
                        holiday.getEndOfHoliday().toString(),
                        holiday.getStatus().toString()
                )).collect(Collectors.toList());
    }

    /**
     * Creates a new holiday based on the provided holiday data transfer object (DTO).
     *
     * <p>This method accepts a {@link HolidayDTO} object and converts it into a {@link Holiday} entity.
     * The holiday entity is then saved to the repository. After successful persistence,
     * the same DTO is returned.
     *
     * @param holidayDTO the {@link HolidayDTO} containing the details of the holiday to be created
     * @return the {@link HolidayDTO} object representing the created holiday
     */
    public HolidayDTO createHoliday(HolidayDTO holidayDTO) {
        Holiday holiday = holidayRepository.save(
                Holiday.builder()
                        .label(holidayDTO.getHolidayLabel())
                        .employeeId(holidayDTO.getEmployeeId())
                        .startOfHoliday(Instant.parse(holidayDTO.getStartOfHoliday()))
                        .endOfHoliday(Instant.parse(holidayDTO.getEndOfHoliday()))
                        .status(Status.valueOf(holidayDTO.getStatus()))
                        .build()
        );


        return new HolidayDTO(
                holiday.getId().toString(),
                holiday.getLabel(),
                holiday.getEmployeeId(),
                holiday.getStartOfHoliday().toString(),
                holiday.getEndOfHoliday().toString(),
                holiday.getStatus().toString());
    }

    /**
     * Updates an existing holiday based on the provided holiday data transfer object (DTO).
     *
     * <p>This method accepts an updated {@link HolidayDTO} object, converts it into a {@link Holiday} entity,
     * and updates the corresponding holiday record in the repository. The updated DTO is then returned as the result.
     *
     * @param holidayDTO the {@link HolidayDTO} containing the updated details of the holiday
     * @return the {@link HolidayDTO} object representing the updated holiday
     */
    public HolidayDTO updateHoliday(HolidayDTO holidayDTO) {

        holidayRepository.save(
                Holiday.builder()
                        .id(UUID.fromString(holidayDTO.getHolidayId()))
                        .label(holidayDTO.getHolidayLabel())
                        .employeeId(holidayDTO.getEmployeeId())
                        .startOfHoliday(Instant.parse(holidayDTO.getStartOfHoliday()))
                        .endOfHoliday(Instant.parse(holidayDTO.getEndOfHoliday()))
                        .status(Status.valueOf(holidayDTO.getStatus()))
                        .build()
        );
        return holidayDTO;
    }

    /**
     * Deletes an existing holiday based on its unique holiday ID.
     *
     * <p>This method removes a holiday record from the repository by its {@link UUID}.
     * If the holiday with the provided ID exists, it will be deleted from the system.
     *
     * @param holidayId the unique identifier of the holiday to be deleted
     */
    public void deleteHoliday(UUID holidayId) {
        holidayRepository.deleteById(holidayId);
    }

}

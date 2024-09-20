package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.entity.Employee;
import com.airfranceklm.fasttrack.assignment.entity.Holiday;
import com.airfranceklm.fasttrack.assignment.entity.Status;
import com.airfranceklm.fasttrack.assignment.repository.EmployeeRepository;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import com.airfranceklm.fasttrack.assignment.resources.HolidayDTO;
import com.airfranceklm.fasttrack.assignment.service.exceptions.InvalidHolidayCancellationException;
import com.airfranceklm.fasttrack.assignment.service.exceptions.InvalidHolidayGapException;
import com.airfranceklm.fasttrack.assignment.service.exceptions.InvalidHolidayOverlapException;
import com.airfranceklm.fasttrack.assignment.service.exceptions.InvalidHolidayStartDateException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
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

    private EmployeeRepository employeeRepository;

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
        return employeeRepository.findById(employeeId)
                .map(Employee::getHolidays)
                .stream()
                .flatMap(Collection::stream)
                .map(holiday -> new HolidayDTO(
                        holiday.getId().toString(),
                        holiday.getLabel(),
                        holiday.getEmployee().getId(),
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
        Instant startOfHoliday = Instant.parse(holidayDTO.getStartOfHoliday());
        Instant endOfHoliday = Instant.parse(holidayDTO.getEndOfHoliday());

        if (!Instant.now().plus(5, ChronoUnit.DAYS).isBefore(startOfHoliday)) {
            throw new InvalidHolidayStartDateException();

        }

        if (isOverlapping(endOfHoliday, startOfHoliday)) {
            throw new InvalidHolidayOverlapException();
        }

        if (isGapLessThan3Days(holidayDTO.getEmployeeId(), startOfHoliday, endOfHoliday)) {
            throw new InvalidHolidayGapException();
        }

        Holiday holiday = holidayRepository.save(
                Holiday.builder()
                        .label(holidayDTO.getHolidayLabel())
                        .employee(Employee.builder().id(holidayDTO.getEmployeeId()).build())
                        .startOfHoliday(startOfHoliday)
                        .endOfHoliday(endOfHoliday)
                        .status(Status.valueOf(holidayDTO.getStatus()))
                        .build()
        );


        return new HolidayDTO(
                holiday.getId().toString(),
                holiday.getLabel(),
                holiday.getEmployee().getId(),
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

        Instant startOfHoliday = Instant.parse(holidayDTO.getStartOfHoliday());
        Instant endOfHoliday = Instant.parse(holidayDTO.getEndOfHoliday());

        if (!Instant.now().plus(5, ChronoUnit.DAYS).isBefore(startOfHoliday)) {
            throw new InvalidHolidayStartDateException();

        }
        if (isOverlapping(endOfHoliday, startOfHoliday)) {
            throw new InvalidHolidayOverlapException();
        }

        if (isGapLessThan3Days(holidayDTO.getEmployeeId(), startOfHoliday, endOfHoliday)) {
            throw new InvalidHolidayGapException();
        }

        holidayRepository.save(
                Holiday.builder()
                        .id(UUID.fromString(holidayDTO.getHolidayId()))
                        .label(holidayDTO.getHolidayLabel())
                        .employee(Employee.builder().id(holidayDTO.getEmployeeId()).build())
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

        Holiday holiday = holidayRepository.findById(holidayId).orElseThrow();
        if (!Instant.now().plus(5, ChronoUnit.DAYS).isBefore(holiday.getStartOfHoliday())) {
            throw new InvalidHolidayCancellationException();

        }

        holidayRepository.deleteById(holidayId);
    }


    private boolean isOverlapping(Instant endOfHoliday, Instant startOfHoliday) {
        return holidayRepository
                .findAll()
                .stream()
                .anyMatch(holiday -> {  // Filter for overlapping holidays
                    Instant existingStart = holiday.getStartOfHoliday();
                    Instant existingEnd = holiday.getEndOfHoliday();

                    // Check for overlap
                    return (existingStart.isBefore(endOfHoliday) || existingStart.equals(endOfHoliday)) &&
                            (startOfHoliday.isBefore(existingEnd) || startOfHoliday.equals(existingEnd));
                });
    }

    private boolean isGapLessThan3Days(String employeeId, Instant endOfHoliday, Instant startOfHoliday) {
        return employeeRepository.findById(employeeId)
                .map(Employee::getHolidays)
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(holiday -> {
                    System.out.println("----------");
                    System.out.println(holiday.getStartOfHoliday());
                    System.out.println(holiday.getEndOfHoliday());
                    Instant existingStart = holiday.getStartOfHoliday();
                    Instant existingEnd = holiday.getEndOfHoliday();
                    // Check if there is less than 3 days gap between the new holiday and existing holidays
                    long daysBetweenStartAndExistingEnd = Duration.between(existingEnd, startOfHoliday).toDays();
                    long daysBetweenExistingStartAndEnd = Duration.between(endOfHoliday, existingStart).toDays();

                    // Check if the gap is less than 3 days
                    return (daysBetweenStartAndExistingEnd < 3 && daysBetweenStartAndExistingEnd >= 0) ||
                            (daysBetweenExistingStartAndEnd < 3 && daysBetweenExistingStartAndEnd >= 0);
                });
    }
}

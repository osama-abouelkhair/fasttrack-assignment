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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HolidaysServiceTest {


    private final String klm012345 = "klm012345";
    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private HolidaysService holidaysService;

    private HolidayDTO holidayDTO;
    private Holiday holiday;
    private Employee employeeWithHoliday;
    private Employee employeeWithoutHoliday;

    @BeforeEach
    void setUp() {
        // Use constructors or builders where appropriate
        holidayDTO = new HolidayDTO(
                UUID.randomUUID().toString(),
                "Summer Vacation",
                "klm12345",
                Instant.now().plus(10, ChronoUnit.DAYS).toString(),
                Instant.now().plus(20, ChronoUnit.DAYS).toString(),
                Status.DRAFT.toString()
        );

        holiday = Holiday.builder()
                .id(UUID.fromString(holidayDTO.getHolidayId()))
                .label(holidayDTO.getHolidayLabel())
                .employee(Employee.builder().id(holidayDTO.getEmployeeId()).build())
                .startOfHoliday(Instant.parse(holidayDTO.getStartOfHoliday()))
                .endOfHoliday(Instant.parse(holidayDTO.getEndOfHoliday()))
                .status(Status.DRAFT)
                .build();

        employeeWithHoliday = Employee.builder()
                .id(klm012345)
                .holidays(List.of(holiday))
                .build();

        employeeWithoutHoliday = Employee.builder()
                .id(klm012345)
                .holidays(List.of())
                .build();
    }

    @Test
    void createHoliday_Success() {
        when(holidayRepository.save(any(Holiday.class))).thenReturn(holiday);
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employeeWithoutHoliday));
        when(holidayRepository.findAll()).thenReturn(Collections.emptyList()); // No existing holidays to check for overlap

        HolidayDTO result = holidaysService.createHoliday(holidayDTO);

        assertNotNull(result);
        assertEquals(holidayDTO.getHolidayLabel(), result.getHolidayLabel());
        verify(holidayRepository, times(1)).save(any(Holiday.class));
    }

    @Test
    void createHoliday_InvalidStartDate_ThrowsException() {
        // Modify the holidayDTO creation instead of using a setter
        holidayDTO = new HolidayDTO(
                holidayDTO.getHolidayId(),
                holidayDTO.getHolidayLabel(),
                holidayDTO.getEmployeeId(),
                Instant.now().toString(), // Invalid start date (less than 5 days)
                holidayDTO.getEndOfHoliday(),
                holidayDTO.getStatus()
        );

        assertThrows(InvalidHolidayStartDateException.class, () -> holidaysService.createHoliday(holidayDTO));
        verify(holidayRepository, never()).save(any(Holiday.class));
    }

    @Test
    void createHoliday_HolidayOverlap_ThrowsException() {
        when(holidayRepository.findAll()).thenReturn(List.of(holiday)); // Mock existing overlapping holiday

        assertThrows(InvalidHolidayOverlapException.class, () -> holidaysService.createHoliday(holidayDTO));
        verify(holidayRepository, never()).save(any(Holiday.class));
    }

    @Test
    void createHoliday_HolidayGap_ThrowsException() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employeeWithHoliday)); // Mock employee with holidays
        when(holidayRepository.findAll()).thenReturn(Collections.emptyList()); // No overlaps

        assertThrows(InvalidHolidayGapException.class, () -> holidaysService.createHoliday(holidayDTO));
        verify(holidayRepository, never()).save(any(Holiday.class));
    }

    @Test
    void deleteHoliday_Success() {
        when(holidayRepository.findById(any(UUID.class))).thenReturn(Optional.of(holiday));

        holidaysService.deleteHoliday(UUID.fromString(holidayDTO.getHolidayId()));

        verify(holidayRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deleteHoliday_InvalidCancellation_ThrowsException() {
        // Modify the holiday start date to less than 5 days from now
        holiday = Holiday.builder()
                .id(UUID.fromString(holidayDTO.getHolidayId()))
                .label(holidayDTO.getHolidayLabel())
                .employee(Employee.builder().id(holidayDTO.getEmployeeId()).build())
                .startOfHoliday(Instant.now().plus(1, ChronoUnit.DAYS)) // Less than 5 days
                .endOfHoliday(Instant.parse(holidayDTO.getEndOfHoliday()))
                .status(Status.DRAFT)
                .build();

        when(holidayRepository.findById(any(UUID.class))).thenReturn(Optional.of(holiday));

        assertThrows(InvalidHolidayCancellationException.class, () -> holidaysService.deleteHoliday(UUID.fromString(holidayDTO.getHolidayId())));
        verify(holidayRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void geyHolidays_Success() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employeeWithHoliday));

        List<HolidayDTO> holidays = holidaysService.geyHolidays(employeeWithHoliday.getId());

        assertNotNull(holidays);
        assertEquals(1, holidays.size());
        verify(employeeRepository, times(1)).findById(anyString());
    }

    @Test
    void updateHoliday_Success() {
        when(holidayRepository.save(any(Holiday.class))).thenReturn(holiday);
        when(holidayRepository.findAll()).thenReturn(Collections.emptyList()); // No overlaps
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employeeWithoutHoliday));

        HolidayDTO result = holidaysService.updateHoliday(holidayDTO);

        assertNotNull(result);
        assertEquals(holidayDTO.getHolidayLabel(), result.getHolidayLabel());
        verify(holidayRepository, times(1)).save(any(Holiday.class));
    }
}

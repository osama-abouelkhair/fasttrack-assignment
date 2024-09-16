package com.airfranceklm.fasttrack.assignment.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class HolidayDTO {

    public HolidayDTO(String holidayLabel, String employeeId, String startOfHoliday, String endOfHoliday, String status) {
        this.holidayLabel = holidayLabel;
        this.employeeId = employeeId;
        this.startOfHoliday = startOfHoliday;
        this.endOfHoliday = endOfHoliday;
        this.status = status;
    }

    @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$")
    private String holidayId;
    @NotNull
    private final String holidayLabel;
    @NotNull
    @Pattern(regexp = "^klm[0-9]{6}$")
    private final String employeeId;
    @NotNull
    private final String startOfHoliday;
    @NotNull
    private final String endOfHoliday;
    @NotNull
    private final String status;

}

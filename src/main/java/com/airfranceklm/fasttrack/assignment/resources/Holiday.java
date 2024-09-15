package com.airfranceklm.fasttrack.assignment.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class Holiday {
    @NotNull
    @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$")
    private final String holidayId;
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

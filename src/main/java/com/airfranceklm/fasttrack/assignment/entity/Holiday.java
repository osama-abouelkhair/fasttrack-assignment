package com.airfranceklm.fasttrack.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Holiday {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String label;

    private Instant startOfHoliday;

    private Instant endOfHoliday;

    private Status status;

    // To not allow employee changes through holiday
    @ManyToOne(cascade = CascadeType.DETACH)
    // Setting the foreign key column name and to not allow null
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
}



package com.airfranceklm.fasttrack.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Employee {

    @Id
    @Pattern(regexp = "^klm[0-9]{6}$")
    private String id;

    private String name;

    @OneToMany(mappedBy="employee")
    List<Holiday> holidays;

}



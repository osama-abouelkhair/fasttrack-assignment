package com.airfranceklm.fasttrack.assignment.repository;

import com.airfranceklm.fasttrack.assignment.entity.Holiday;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday, UUID> {
    List<Holiday> findByEmployeeId(String employeeId);
}

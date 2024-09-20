package com.airfranceklm.fasttrack.assignment.repository;

import com.airfranceklm.fasttrack.assignment.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, UUID> {
}

package de.ait.listtask.repository;

import de.ait.listtask.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TaskRepository extends JpaRepository<Task, Long> {


  Page<Task> findAllByStartDate(LocalDate date, Pageable pageable);

  Page<Task> findAllByFinishDate(LocalDate date, Pageable pageable);

}

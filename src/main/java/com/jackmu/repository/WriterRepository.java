package com.jackmu.repository;

import com.jackmu.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {
    Writer findByPenName(String penName);
    List<Writer> findAllByWriterId(Long id);
    List<Writer> findAllByEmail(String email);
}

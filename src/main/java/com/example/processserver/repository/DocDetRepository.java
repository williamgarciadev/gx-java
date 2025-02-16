package com.example.processserver.repository;

import com.example.processserver.model.Docdet;
import com.example.processserver.model.DocdetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocDetRepository extends JpaRepository<Docdet, DocdetId> {

}

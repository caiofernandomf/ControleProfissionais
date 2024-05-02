package io.caiofernandomf.ControleProfissionais.repository;

import io.caiofernandomf.ControleProfissionais.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional,Long> {
}

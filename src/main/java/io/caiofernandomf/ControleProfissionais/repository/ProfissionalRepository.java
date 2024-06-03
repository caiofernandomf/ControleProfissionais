package io.caiofernandomf.ControleProfissionais.repository;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import io.caiofernandomf.ControleProfissionais.model.Profissional;
import jakarta.persistence.NamedQuery;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Filter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional,Long>, JpaSpecificationExecutor<Profissional> {

    @Override
//    @Query(nativeQuery = true,value = "SELECT * FROM Profissionais e WHERE e.id=?1")
    @Query(nativeQuery = false,value = "SELECT e FROM Profissional e WHERE e.id=?1 ")
    Optional<Profissional> findById(Long id);

    static Specification<Profissional> getLikeConditional(String q){
        return (root, query, criteriaBuilder) ->
                //criteriaBuilder.and(criteriaBuilder.isTrue(root.get("ativo")),
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get("id").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("nome").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("created_date").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("cargo").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("nascimento").as(String.class),"%"+q+"%"));

    }
}

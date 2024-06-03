package io.caiofernandomf.ControleProfissionais.repository;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository  extends JpaRepository<Contato,Long> , JpaSpecificationExecutor<Contato>  {




    interface Specs {

        static Specification<Contato> byContato(String contato) {
            return (root, query, builder) ->
                    builder.equal(root.get("contato"), contato);
        }

        static Specification<Contato> byNome(String nome) {
            return (root, query, builder) ->
                    builder.equal(root.get("nome"), nome);
        }

        static Specification<Contato> byId(String id) {
            return (root, query, builder) ->
                    builder.equal(root.get("id"), id);
        }

        static Specification<Contato> byCreatedDate(String data) {
            return (root, query, builder) ->
                    builder.equal(root.get("created_date"), data);
        }

        static Specification<Contato> byProfissional(String id) {
            return (root, query, builder) ->
                    builder.equal(root.get("profissional"), id);
        }

        static Specification<Contato> getLikeConditional(String q){
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                    criteriaBuilder.like(root.get("id").as(String.class),"%"+q+"%"),
                    criteriaBuilder.like(root.get("profissional").as(String.class),"%"+q+"%"),
                    criteriaBuilder.like(root.get("created_date").as(String.class),"%"+q+"%"),
                    criteriaBuilder.like(root.get("nome").as(String.class),"%"+q+"%"),
                    criteriaBuilder.like(root.get("contato").as(String.class),"%"+q+"%"));

        }


    }
}
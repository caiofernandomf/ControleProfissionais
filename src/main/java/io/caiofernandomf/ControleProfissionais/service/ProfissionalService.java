package io.caiofernandomf.ControleProfissionais.service;

import io.caiofernandomf.ControleProfissionais.model.Profissional;
import io.caiofernandomf.ControleProfissionais.repository.ProfissionalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<String> criarProfissional(Profissional profissional){
        try {

            profissionalRepository.save(profissional);

        }catch (Exception e){
            throw  new RuntimeException(e);
        }

        return ResponseEntity.ok("Sucesso profissional com id {"+profissional.getId()+"} cadastrado");
    }

    public ResponseEntity<Profissional> buscarProfissionalPorId(Long id){
        return
                profissionalRepository.findById(id)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.ok().build());
    }

    public ResponseEntity<String> excluirProfissionalPorId(Long id){
        return
                profissionalRepository.findById(id)
                        .map(profissional -> {

                            profissional.inativar();

                            profissionalRepository.save(profissional);

                            return ResponseEntity.ok("Sucesso profissional excluído");
                        }).orElse(ResponseEntity.ok().build());
    }

    public ResponseEntity<String> atualizarProfissionalPorId(Profissional profissional,Long id)
    {
        return  profissionalRepository.findById(id)
                .map(profissionalToUpdate ->
                {
                    BeanUtils.copyProperties(profissional,profissionalToUpdate,"id");

                    return ResponseEntity.ok().body("sucesso cadastrado alterado");
                }).orElse(ResponseEntity.ok().build());

    }

    public List buscarPorParametros(String q, List<String> campos){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Profissional> criteriaQuery= criteriaBuilder.createQuery(Profissional.class);
        Root<Profissional> root = criteriaQuery.from(Profissional.class);

        List<String> camposParaSelect=null;
        List<Selection<?>> listaSelection= null;

        Predicate predicate =
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get("id").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("nome").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("created_date").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("cargo").as(String.class),"%"+q+"%"),
                        criteriaBuilder.like(root.get("nascimento").as(String.class),"%"+q+"%"));

        predicate = criteriaBuilder.and(criteriaBuilder.isTrue(root.get("ativo")),predicate);


        if(!campos.isEmpty()) {
            camposParaSelect = campos.stream()
                    .flatMap(s -> Profissional.camposParaSelect().stream().filter(s1 -> s1.equals(s.toLowerCase().trim())))
                    .map(String::trim)
                    .toList();
        }

        if (camposParaSelect!=null){
            listaSelection=camposParaSelect.stream().map(s -> root.get(s).alias(s)).collect(Collectors.toList());
            criteriaQuery=criteriaQuery.multiselect(listaSelection);
        }



        criteriaQuery.where(predicate);



        return entityManager.createQuery(criteriaQuery).getResultList();

    }
}

package io.caiofernandomf.ControleProfissionais.service;

import io.caiofernandomf.ControleProfissionais.model.Profissional;
import io.caiofernandomf.ControleProfissionais.model.ProfissionalDto;
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
    public ResponseEntity<String> criarProfissional(ProfissionalDto profissionalDto){
        Profissional profissional = new Profissional();
        try {
            BeanUtils.copyProperties(profissionalDto,profissional,"id","contatos");
            profissionalRepository.save(profissional);

        }catch (Exception e){
            throw  new RuntimeException(e);
        }

        return ResponseEntity.ok("Sucesso profissional com id {"+profissional.getId()+"} cadastrado");
    }

    public ResponseEntity<ProfissionalDto> buscarProfissionalPorId(Long id){

        return
                profissionalRepository.findById(id)
                        .map(Profissional::toDto)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.ok().build());
    }

    public ResponseEntity<String> excluirProfissionalPorId(Long id){
        return
                profissionalRepository.findById(id)
                        .map(profissional -> {

                            //profissional.inativar();

                            profissionalRepository.delete(profissional);

                            return ResponseEntity.ok("Sucesso profissional excluído");
                        }).orElse(ResponseEntity.ok().build());
    }

    public ResponseEntity<String> atualizarProfissionalPorId(ProfissionalDto profissionalDto,Long id)
    {
        return  profissionalRepository.findById(id)
                .map(profissionalToUpdate ->
                {
                    BeanUtils.copyProperties(profissionalDto,profissionalToUpdate,"id","contatos");

                    profissionalRepository.save(profissionalToUpdate);

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

    public List<ProfissionalDto> listarPorParametros(String q, List<String> campos){

        Boolean existemCampos = campos.stream().allMatch(s -> Profissional.camposParaSelect().contains(s.toLowerCase().trim()));

        if(!campos.isEmpty() && !existemCampos ){
            throw new RuntimeException("parâmetro campos inválido");
        }

        List<String> camposParaSelect = campos.stream()
                .filter(s->Profissional.camposParaSelect().contains(s.toLowerCase().trim()))
                //.flatMap(s -> Profissional.camposParaSelect().stream().filter(s1 -> s1.equals(s.toLowerCase().trim())))
                .map(String::trim)
                .toList();

        return profissionalRepository
                .findAll(ProfissionalRepository.getLikeConditional(q)).stream()
                .map(profissional -> profissional.toDto(camposParaSelect)).toList();


    }
}

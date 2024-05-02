package io.caiofernandomf.ControleProfissionais.service;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import io.caiofernandomf.ControleProfissionais.repository.ContatoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private EntityManager entityManager;

    public ResponseEntity<String> criarContato(Contato contato){
         try {
             contatoRepository.save(contato);

         }catch (Exception e){
             throw  new RuntimeException(e);
         }

         return ResponseEntity.ok("Sucesso contato com id {"+contato.getId()+"} cadastrado");
    }

    public ResponseEntity<Contato> buscarContatoPorId(Long id){
        return
                contatoRepository.findById(id)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.ok().build());
    }

    public ResponseEntity<String> excluirContatoPorId(Long id){
        return
                contatoRepository.findById(id)
                .map(contato -> {
                    contatoRepository.delete(contato);
                    return ResponseEntity.ok("Sucesso contato excluído");
                }).orElse(ResponseEntity.ok().build());
    }

    public ResponseEntity<String> atualizarContatoPorId(Contato contato,Long id)
            {
        return  contatoRepository.findById(id)
                .map(contatoToUpdate ->
                {

                    contatoToUpdate.setContato(contato.getContato());
                    contatoToUpdate.setNome(contato.getNome());
                    contatoToUpdate.setProfissional(contato.getProfissional());
                    return ResponseEntity.ok().body("sucesso cadastrado alterado");
                }).orElse(ResponseEntity.ok().build());

    }

    public List<Contato> buscarPorParametros(String q, List<String> campos){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contato> criteriaQuery= criteriaBuilder.createQuery(Contato.class);
        Root<Contato> root = criteriaQuery.from(Contato.class);

        List<String> camposParaSelect=null;
        List<Selection<?>> listaSelection= null;

        Predicate predicate = ContatoRepository.Specs.byContato(q).
                or(ContatoRepository.Specs.byNome(q))
                .toPredicate(root,criteriaQuery,criteriaBuilder);

        Predicate predicate1 =
        criteriaBuilder.or(
                criteriaBuilder.like(root.get("id").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("profissional").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("created_date").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("nome").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("contato").as(String.class),"%"+q+"%"));


        if(!campos.isEmpty()) {
            camposParaSelect = campos.stream()
                    .flatMap(s -> Contato.camposParaSelect().stream().filter(s1 -> s1.equals(s.toLowerCase().trim())))
                    .map(String::trim)
                    .toList();
            System.out.println(camposParaSelect.toString());
        }

        if (camposParaSelect!=null){
            listaSelection=camposParaSelect.stream().map(s -> root.get(s).alias(s)).collect(Collectors.toList());
            criteriaQuery=criteriaQuery.multiselect(listaSelection);
        }

    criteriaQuery.where(criteriaBuilder.or(predicate,predicate1));

    return entityManager.createQuery(criteriaQuery).getResultList();

    }


}

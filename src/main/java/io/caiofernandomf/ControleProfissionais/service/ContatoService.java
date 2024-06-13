package io.caiofernandomf.ControleProfissionais.service;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import io.caiofernandomf.ControleProfissionais.model.ContatoDto;
import io.caiofernandomf.ControleProfissionais.model.Profissional;
import io.caiofernandomf.ControleProfissionais.model.mapper.BeanUtilsMapper;
import io.caiofernandomf.ControleProfissionais.repository.ContatoRepository;
import io.caiofernandomf.ControleProfissionais.repository.ProfissionalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private EntityManager entityManager;
    private final ProfissionalRepository profissionalRepository;

    public ResponseEntity<String> criarContato(ContatoDto contatoDto){
        Contato contato = new Contato();
         try {

             BeanUtils.copyProperties(contatoDto,contato);
             contatoRepository.save(contato);

         }catch (Exception e){
             throw  new RuntimeException(e);
         }

         return ResponseEntity.ok("Sucesso contato com id {"+contato.getId()+"} cadastrado");
    }

    public ResponseEntity<ContatoDto> buscarContatoPorId(Long id){
        return
                contatoRepository.findById(id)
                        .map(contato -> BeanUtilsMapper.contatoToDto(contato,null))
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

    public ResponseEntity<String> atualizarContatoPorId(ContatoDto contatoDto,Long id)
            {
        return  contatoRepository.findById(id)
                .map(contatoToUpdate ->
                {
                    verificaProfissionalExiste(contatoDto);
                    BeanUtilsMapper.contatoDtoToContatoUpdate(contatoDto,contatoToUpdate);

                    contatoRepository.save(contatoToUpdate);

                    return ResponseEntity.ok().body("sucesso cadastrado alterado");
                }).orElse(ResponseEntity.ok().build());

    }

    private void verificaProfissionalExiste(ContatoDto contatoDto)throws RuntimeException{
        if(Objects.nonNull(contatoDto.profissional()) && Objects.nonNull(contatoDto.profissional().id())){
            if(!profissionalRepository.existsById(contatoDto.profissional().id()))
                throw new RuntimeException("Profissional não encontrado, não será possível atualizar este contato");
        }
    }

    public List<?> buscarPorParametros(String q, List<String> campos){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Contato> criteriaQuery= criteriaBuilder.createQuery(Contato.class);
        Root<Contato> root = criteriaQuery.from(Contato.class);

        Join<Contato, Profissional> join = root.join("profissional",JoinType.LEFT);
        join = join.on(criteriaBuilder.equal(join.get("id"),root.get("profissional").get("id")));

        List<String> camposParaSelect=null;
        List<Selection<?>> listaSelection= null;

        Predicate predicate =
                criteriaBuilder.or(criteriaBuilder.isNull(join.get("id"))
                        ,criteriaBuilder.equal(join.get("id"),root.get("profissional").get("id")));

        Predicate predicate1 =

        criteriaBuilder.or(
                criteriaBuilder.like(root.get("id").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("profissional").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("created_date").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("nome").as(String.class),"%"+q+"%"),
                criteriaBuilder.like(root.get("contato").as(String.class),"%"+q+"%")
                );

        if(!campos.isEmpty()) {
            camposParaSelect = campos.stream()
                    .flatMap(s -> Contato.camposParaSelect().stream().filter(s1 -> s1.equals(s.toLowerCase().trim())))
                    .map(String::trim)
                    .toList();
            System.out.println(camposParaSelect.toString());
        }

        criteriaQuery.where(criteriaBuilder.and(predicate1));

        List<String> finalCamposParaSelect = camposParaSelect;

        return entityManager.createQuery(criteriaQuery).getResultStream().map(contato -> contato.toDto(finalCamposParaSelect)).toList();

    }

    public List<ContatoDto> listarPorParametros(String q, List<String> campos){

        Boolean existemCampos = campos.stream().allMatch(s -> Contato.camposParaSelect().contains(s.toLowerCase().trim()));

        if(!campos.isEmpty() && !existemCampos ){
            throw new RuntimeException("parâmetro campos inválido");
        }

        List<String >camposParaSelect = campos.stream()
                .flatMap(s -> Contato.camposParaSelect().stream().filter(s1 -> s1.equals(s.toLowerCase().trim())))
                .map(String::trim)
                .toList();

        return contatoRepository
                .findAll(ContatoRepository.Specs.getLikeConditional(q)).stream()
                .map(contato -> BeanUtilsMapper.contatoToDto(contato,camposParaSelect)).toList();


    }


}

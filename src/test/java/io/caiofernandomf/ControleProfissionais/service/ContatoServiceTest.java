package io.caiofernandomf.ControleProfissionais.service;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import io.caiofernandomf.ControleProfissionais.model.ContatoDto;
import io.caiofernandomf.ControleProfissionais.model.mapper.BeanUtilsMapper;
import io.caiofernandomf.ControleProfissionais.repository.ContatoRepository;
import io.caiofernandomf.ControleProfissionais.repository.ProfissionalRepository;
import io.caiofernandomf.ControleProfissionais.service.mock.MockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {

    @Mock
    ContatoRepository contatoRepository;

    @Mock
    ProfissionalRepository profissionalRepository;

    @InjectMocks
    ContatoService contatoService;

    @Captor
    ArgumentCaptor<Contato> contatoCaptor;

    @Test
    @DisplayName("Deve criar um contato com sucesso")
    void criarContato() {
        var contatoDto = MockData.createContatoDto();

        when(contatoRepository.save(any(Contato.class)))
                .thenAnswer(invocationOnMock -> {
                    var c =invocationOnMock.getArgument(0, Contato.class);
                    c.setId(1L);
                    c.setCreated_date(LocalDate.now());
                    return c;
                });
        ResponseEntity<String> responseEntity = contatoService.criarContato(contatoDto);

        verify(contatoRepository).save(contatoCaptor.capture());
        System.out.println(contatoCaptor.getValue());
        assertNull(contatoCaptor.getValue().getProfissional());
        assertEquals("Sucesso contato com id {"+contatoCaptor.getValue().getId()+"} cadastrado",responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve buscar um contato por id com sucesso")
    void buscarContatoPorId() {
        var contato =  new Contato();
        BeanUtilsMapper.copyProperties(MockData.createContatoDto(),contato,"id");
        contato.setId(2L);

        when(contatoRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        contato
                ));
        ResponseEntity<ContatoDto> responseEntity = contatoService.buscarContatoPorId(2L);
        verify(contatoRepository).findById(2L);
        var contatoDto = BeanUtilsMapper.contatoToDto(contato,null);

        assertEquals(contatoDto,responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve excluir um contato por id com sucesso")
    void excluirContatoPorId() {
        var contato = new Contato();
        BeanUtilsMapper.copyProperties(MockData.createContatoDto(),contato,"id");
        contato.setId(3L);

        when(contatoRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        contato
                ));

        ResponseEntity<String> responseEntity = contatoService.excluirContatoPorId(3L);
        verify(contatoRepository).delete(contato);
        assertEquals(responseEntity.getBody(),"Sucesso contato excluído");
    }

    @Test
    @DisplayName("Deve atualizar um contato por id com sucesso")
    void atualizarContatoPorId() {
        var contatoDto = MockData.createContatoDto();
        var contato = MockData.createContatoToUpdate();

        when(contatoRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        contato
                ));
        when(profissionalRepository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        System.out.println(contato);
        ResponseEntity<String> responseEntity =
                contatoService.atualizarContatoPorId(contatoDto,3L);

        verify(contatoRepository).findById(3L);
        verify(contatoRepository).save(contato);

        assertEquals(responseEntity.getBody(),"sucesso cadastrado alterado");
        System.out.println(contato);
        System.out.println(contatoDto);
        assertTrue(contato.getNome().equals(contatoDto.nome())
                && contato.getContato().equals(contatoDto.contato())
                && contato.getProfissional().getId().equals(contatoDto.profissional().id())
                //&& contato.getCreated_date().equals(contatoDto.created_date())
                && contato.getId().equals(3L));

    }

    @Test
    @DisplayName("Deve lançar uma RuntimeException ao atualizar um contato por id")
    void atualizarContatoPorIdComException() {
        var contatoDto = MockData.createContatoDto();
        var contato = MockData.createContatoToUpdate();

        when(profissionalRepository.existsById(any(Long.class)))
                .thenReturn(false);
        when(contatoRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        contato
                ));
        System.out.println(contato);
        AtomicReference<ResponseEntity<String>> responseEntity = new AtomicReference<>();
        assertThrows(RuntimeException.class,
                ()-> responseEntity.set(contatoService.atualizarContatoPorId(contatoDto, 3L)));

        System.out.println("ResponseEntity: "+responseEntity.get());

        verify(contatoRepository).findById(3L);
        verify(contatoRepository,never()).save(contato);
        //verify(contatoRepository).save(contato);

        assertNull(responseEntity.get());
        System.out.println(contato);
        System.out.println(contatoDto);
        assertFalse(contato.getNome().equals(contatoDto.nome())
                && contato.getContato().equals(contatoDto.contato())
                && contato.getProfissional().getId().equals(contatoDto.profissional().id())
                //&& contato.getCreated_date().equals(contatoDto.created_date())
                && contato.getId().equals(3L));

    }

    @Test
    @DisplayName("Deve listar contatos baseado nos parâmetros(q,campos) com sucesso")
    void listarPorParametros() {
        var contato1 = MockData.createContatoToUpdate();
        var contatoDto = MockData.createContatoDto();
        var contato2 = new Contato();
        BeanUtilsMapper.contatoDtoToContatoUpdate(contatoDto,contato2);
        contato2.setId(4L);
        System.out.println(contato2);
        System.out.println(contato1);
        when(contatoRepository.findAll(any(Specification.class))).thenReturn(List.of(contato2,contato1));

        String q= "21";
        List<String> campos=List.of("id","nome","contato");//,"profissional"
        List<ContatoDto> lista= contatoService.listarPorParametros(q,campos);

        verify(contatoRepository).findAll(any(Specification.class));
        assertEquals(2, lista.size());
        assertTrue(lista.stream().allMatch(p->
                Objects.isNull(p.created_date()) && Objects.nonNull(p.id()) && Objects.nonNull(p.nome())
                && Objects.nonNull(p.contato())));
    }

    @Test
    @DisplayName("Deve listar contatos baseado nos parâmetro(q) com sucesso")
    void listarSemParametroCampos() {
        var contato1 = MockData.createContatoToUpdate();
        var contatoDto = MockData.createContatoDto();
        var contato2 = new Contato();
        BeanUtilsMapper.contatoDtoToContatoUpdate(contatoDto,contato2);
        contato2.setId(4L);
        contato2.setCreated_date(LocalDate.now());

        when(contatoRepository.findAll(any(Specification.class))).thenReturn(List.of(contato2,contato1));

        String q= "21";
        List<ContatoDto> lista= contatoService.listarPorParametros(q,new ArrayList<>());

        verify(contatoRepository).findAll(any(Specification.class));
        assertEquals(2, lista.size());
        assertTrue(lista.stream().allMatch(p->
                Objects.nonNull(p.created_date()) && Objects.nonNull(p.id()) && Objects.nonNull(p.nome())
                        && Objects.nonNull(p.contato())));
        assertEquals(1,
                lista.stream()
                        .filter(c->Objects.nonNull(c.profissional().id()))
                        .count());

    }
}
package io.caiofernandomf.ControleProfissionais.service;

import io.caiofernandomf.ControleProfissionais.model.Profissional;
import io.caiofernandomf.ControleProfissionais.model.ProfissionalDto;
import io.caiofernandomf.ControleProfissionais.model.TipoCargo;
import io.caiofernandomf.ControleProfissionais.repository.ProfissionalRepository;
import io.caiofernandomf.ControleProfissionais.service.mock.MockData;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.checkerframework.framework.qual.StubFiles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.matchers.Any;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    ProfissionalRepository profissionalRepository;

    @InjectMocks
    ProfissionalService profissionalService;

    @Test
    @DisplayName("Deve criar um profissional com sucesso")
    void criarProfissional() {
        ProfissionalDto profissionalDto = MockData.createProfissionalDto();
        Profissional profissional = MockData.createProfissional(profissionalDto);
        when(profissionalRepository.save(any(Profissional.class)))
                .thenAnswer(invocationOnMock -> {
                    var p =invocationOnMock.getArgument(0, Profissional.class);
                    p.setId(profissional.getId());
                    return p;
                });
        ResponseEntity<String> responseEntity = profissionalService.criarProfissional(profissionalDto);

        verify(profissionalRepository).save(any(Profissional.class));

        assertEquals("Sucesso profissional com id {"+profissional.getId()+"} cadastrado",responseEntity.getBody());


    }

    @Test
    @DisplayName("Deve buscar um profissional por id com sucesso")
    void buscarProfissionalPorId() {

        var profissional = MockData.createProfissional(MockData.createProfissionalDto());
        var profissionalDto = profissional.toDto();
        when(profissionalRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        profissional
                ));
        ResponseEntity<ProfissionalDto> responseEntity = profissionalService.buscarProfissionalPorId(1L);
        verify(profissionalRepository).findById(1L);
        assertEquals(profissionalDto,responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve excluir um profissional por id com sucesso")
    void excluirProfissionalPorId() {
        var profissional = MockData.createProfissional(MockData.createProfissionalDto());
        when(profissionalRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        profissional
                ));

        ResponseEntity<String> responseEntity = profissionalService.excluirProfissionalPorId(1L);
        verify(profissionalRepository).delete(profissional);
        assertEquals(responseEntity.getBody(),"Sucesso profissional excluído");
    }

    @Test
    @DisplayName("Deve atualizar um profissional por id com sucesso")
    void atualizarProfissionalPorId() {
        var profissional = MockData.createProfissionalToUpdate(MockData.createProfissionalDtoToUpdate());
        var profissionalDtoToUpdate =
                new ProfissionalDto(null,"Medeiros", TipoCargo.TESTER, LocalDate.of(1995, Month.JUNE,30),LocalDate.now(),null);

        when(profissionalRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(
                        profissional
                ));
        ResponseEntity<String> responseEntity =
                profissionalService.atualizarProfissionalPorId(profissionalDtoToUpdate,profissional.getId());

        verify(profissionalRepository).findById(profissional.getId());
        verify(profissionalRepository).save(profissional);

        assertEquals(responseEntity.getBody(),"sucesso cadastrado alterado");

        assertTrue(profissional.getNome().equals(profissionalDtoToUpdate.nome())
        && profissional.getCargo().equals(profissionalDtoToUpdate.cargo())
        && profissional.getNascimento().equals(profissionalDtoToUpdate.nascimento())
        && profissional.getCreated_date().equals(profissionalDtoToUpdate.created_date())
        && profissional.getId().equals(2L));
    }

    @Test
    @DisplayName("Deve listar profissionais baseado nos parâmetros(q,campos) com sucesso")
    void listarPorParametros() {
        var profissional1 = MockData.createProfissional(MockData.createProfissionalDto());
        var profissional2 = MockData.createProfissionalToUpdate(MockData.createProfissionalDtoToUpdate());

        when(profissionalRepository.findAll(any(Specification.class))).thenReturn(List.of(profissional2,profissional1));

        String q= "Fernando";
        List<String> campos=List.of("nome","cargo","nascimento");
        List<ProfissionalDto> lista= profissionalService.listarPorParametros(q,campos);

        verify(profissionalRepository).findAll(any(Specification.class));
        assertEquals(2, lista.size());
        assertTrue(lista.stream().allMatch(p-> null==p.id() && null==p.created_date() && null==p.contatos() &&
                null!=p.nome() && null!=p.cargo() && null!=p.nascimento()));
    }
}
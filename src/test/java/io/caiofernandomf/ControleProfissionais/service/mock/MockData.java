package io.caiofernandomf.ControleProfissionais.service.mock;

import io.caiofernandomf.ControleProfissionais.model.*;
import io.caiofernandomf.ControleProfissionais.model.mapper.BeanUtilsMapper;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.Month;

public class MockData {

    public static ProfissionalDto createProfissionalDto(){
        return
                BeanUtils.instantiateClass(BeanUtils.getResolvableConstructor(ProfissionalDto.class),
                        null,
                        "Caio Fernando",
                        TipoCargo.DESENVOLVEDOR,
                        LocalDate.of(1995, Month.JUNE,30),
                        null,
                        null);
    }

    public static ProfissionalDto createProfissionalDtoToUpdate(){
        return
                BeanUtils.instantiateClass(BeanUtils.getResolvableConstructor(ProfissionalDto.class),
                        2L,
                        "Caio Fernando",
                        TipoCargo.DESENVOLVEDOR,
                        LocalDate.of(1995, Month.JUNE,30),
                        LocalDate.now(),
                        null);
    }
    public static Profissional createProfissionalToUpdate(ProfissionalDto profissionalDto){
        return
                Profissional.builder().
                        id(profissionalDto.id()).
                        nome(profissionalDto.nome())
                        .cargo(profissionalDto.cargo()).
                        nascimento(profissionalDto.nascimento()).
                        created_date(LocalDate.now())
                        .build();
    }
    public static Profissional createProfissional(ProfissionalDto profissionalDto){
        return
                Profissional.builder().
                        id(1L).
                        nome(profissionalDto.nome())
                        .cargo(profissionalDto.cargo()).
                        nascimento(profissionalDto.nascimento()).
                        created_date(LocalDate.now())
                        .build();
    }

    public static ContatoDto createContatoDto(){
        Object[] dados = new Object[]{null,"Whatsapp","21980680527",null,createProfissionalDtoToUpdate()};
        return BeanUtilsMapper.instantiateClass(BeanUtils.getResolvableConstructor(ContatoDto.class), dados);
    }

    public static Contato createContatoToUpdate(){
        return Contato.
                builder()
                .id(3L)
                .nome("wpp")
                .contato("21999999999")
                .created_date(LocalDate.now())
                .profissional(null).build();
    }
}

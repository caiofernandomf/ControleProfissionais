package io.caiofernandomf.ControleProfissionais.service.mock;

import io.caiofernandomf.ControleProfissionais.model.ContatoDto;
import io.caiofernandomf.ControleProfissionais.model.Profissional;
import io.caiofernandomf.ControleProfissionais.model.ProfissionalDto;
import io.caiofernandomf.ControleProfissionais.model.TipoCargo;
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
                        id(1l).
                        nome(profissionalDto.nome())
                        .cargo(profissionalDto.cargo()).
                        nascimento(profissionalDto.nascimento()).
                        created_date(LocalDate.now())
                        .build();
    }
}

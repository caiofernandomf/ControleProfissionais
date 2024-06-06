package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ContatoDto(
        Long id,

        String nome,

        String contato,

        LocalDate created_date,

        ProfissionalDto profissional
) {

}

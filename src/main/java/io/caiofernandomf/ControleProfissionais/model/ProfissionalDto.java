package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ProfissionalDto(
        Long id,

        String nome,

        TipoCargo cargo,

        LocalDate nascimento,

        LocalDate created_date,

        List<ContatoDto> contatos
) {
}

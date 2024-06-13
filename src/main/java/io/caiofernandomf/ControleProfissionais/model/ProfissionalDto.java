package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ProfissionalDto(
        Long id,
        @Schema(implementation = String.class,name = "nome",description = "Nome do profissional",example = "Emanuel Kauê Caldeira")
        String nome,
        @Schema(implementation = TipoCargo.class,name = "cargo",description = "Cargo do profissional",example = "TESTER")
        TipoCargo cargo,
        @Schema(implementation = LocalDate.class,name = "nascimento",description = "Data de nascimento do profissional",example = "1996-05-11")
        LocalDate nascimento,
        @Schema(implementation = LocalDate.class,name = "created_date",description = "Data de cadastro do profissional")
        LocalDate created_date,

        List<ContatoDto> contatos
) {
}

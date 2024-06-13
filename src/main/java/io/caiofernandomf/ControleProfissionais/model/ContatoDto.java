package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ContatoDto(

        Long id,
        @Schema(name = "nome",example = "Whatsapp",description = "Nome do contato")
        String nome,
        @Schema(name = "contato",example = "21-xxxxx-xxxx",description = "Nº do contato")
        String contato,
        @Schema(description = "Data de criação do contato"
                ,requiredMode = Schema.RequiredMode.NOT_REQUIRED,name = "created_date")
        LocalDate created_date,
        ProfissionalDto profissional
) {

}

package io.caiofernandomf.ControleProfissionais.model;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.caiofernandomf.ControleProfissionais.config.TipoCargoDeserializer;
import lombok.Getter;

import java.util.stream.Stream;



@Getter
@JsonDeserialize(using = TipoCargoDeserializer.class)
public enum TipoCargo {

    DESENVOLVEDOR("Desenvolvedor"),
    DESIGNER("Designer"),
    SUPOERTE("Suporte"),
    TESTER("Tester");

    private final String name;

    TipoCargo(String name) {
        this.name=name;
    }


    public static TipoCargo fromName(String name){
        return Stream.of(TipoCargo.values())
                .filter(tipoCargo ->
                        tipoCargo.name().contains(name.toUpperCase())
                                 )
                .findFirst()
                .orElseThrow(() ->new RuntimeException("Cargo não encontrado [ "+name+" ]"));
    }

}

package io.caiofernandomf.ControleProfissionais.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.caiofernandomf.ControleProfissionais.model.TipoCargo;

import java.io.IOException;

public class TipoCargoDeserializer extends StdDeserializer<TipoCargo> {
    public TipoCargoDeserializer(Class<?> vc) {
        super(vc);
    }

    public TipoCargoDeserializer() {
        this(null);
    }


    @Override
    public TipoCargo deserialize(JsonParser jsonParser
            , DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String cargo = node.asText();


        return TipoCargo.fromName(cargo);
    }
}

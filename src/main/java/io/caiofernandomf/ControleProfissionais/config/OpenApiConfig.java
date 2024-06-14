package io.caiofernandomf.ControleProfissionais.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties
@Configuration
public class OpenApiConfig {

    @Setter
    private Map<String,String> description;

    @SneakyThrows
    private Components getComponent(){
        Resource resource = new ClassPathResource("static");

        Path path = Paths.get(resource.getURI());
        List<Path> lista = new ArrayList<>();


        try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)){
            stream.forEach(lista::add);
        }
        Components components = new Components();
        lista.stream()
                .forEach(pathI -> {

                    String nome=pathI.getFileName().toString().replace(".json","");

                    String json = null;
                    try {
                        json = Files.readString(pathI);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    components.addExamples(nome
                            ,new Example()
                                    .description(description.get(nome))
                                    .value(json)
                                    //.summary(description.get(nome))
                    );

                });

        return  components;
    }

    @Bean
    public OpenAPI openAPICustom(){

        return
                new OpenAPI()
                        .addServersItem(new Server().url("http://localhost:8080"))
                        .info(new Info()
                                .title("Rest api para gerenciamento de profissionais")
                                        .version("V1")
                                        .description("Rest api para teste técnico ")
                                        .termsOfService("https://github.com/caiofernandomf/ControleProfissionais")
                                        .license(new License()
                                                .name("Apache 2.0")
                                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                                        .contact(new Contact()
                                                .name("Caio Fernando")
                                                .url("https://github.com/caiofernandomf")
                                                )

                                ).components(getComponent());

    }
}

package io.caiofernandomf.ControleProfissionais.controller;

import io.caiofernandomf.ControleProfissionais.model.ContatoDto;
import io.caiofernandomf.ControleProfissionais.service.ContatoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contatos")
@AllArgsConstructor
@Tag(name = "Contato")
public class ContatoController {

    private final ContatoService contatoService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Cria um contato",
            description = "Cria um contato",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ContatoDto",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                     ,schema = @Schema(implementation = ContatoDto.class)
                    ,examples = {@ExampleObject(name = "CriarContato",
                    ref = "#/components/examples/CriarContato"),
                    @ExampleObject(name = "CriarContatoComProfissionalExistente",
                            ref = "#/components/examples/CriarContatoComProfissionalExistente")
                    }

            )),
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(example ="Sucesso contato com id {ID} cadastrado")
                    ))
            }
    )
    public ResponseEntity<String> criarContato(@RequestBody ContatoDto contatoDto){
        return contatoService.criarContato(contatoDto);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Busca um contato por id",
            description = "Busca um contato por id",
            parameters ={@Parameter(name = "id",description = "ID do contato",example = "1",required = true)} ,
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                            ,examples = {
                            @ExampleObject(name = "BuscaContato",
                                    ref = "#/components/examples/BuscaContato"

                            )}
                            ,schema = @Schema(implementation = ContatoDto.class)


                    ))
            }
    )
    public ResponseEntity<ContatoDto> buscarContatoPorId(@PathVariable  Long id){
        return contatoService.buscarContatoPorId(id);

    }

    @PutMapping(value = "{id}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Atualiza um contato ",
            description = "Atualiza um contato",
            parameters ={@Parameter(name = "id",description = "ID do contato",example = "1",required = true)} ,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ContatoDto",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                            ,schema = @Schema(implementation = ContatoDto.class)
                            ,examples = {@ExampleObject(name = "AtualizaContato",
                            ref = "#/components/examples/AtualizaContato"),
                            @ExampleObject(name = "AtualizaContatoComProfissional",
                                    ref = "#/components/examples/AtualizaContatoComProfissional")
                    }

                    )),
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE
                            ,examples = {
                            @ExampleObject(value = "sucesso cadastrado alterado"

                            )}
                            ,schema = @Schema(implementation = ContatoDto.class)


                    ))
            }
    )
    public ResponseEntity<String> atualizarContatoPorId(@PathVariable  Long id,@RequestBody ContatoDto contatoDto ){
        return contatoService.atualizarContatoPorId(contatoDto,id);

    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deleta um contato de acordo com o id",
            description = "Deleta um contato de acordo com o id passado por parâmetro",
            parameters ={@Parameter(name = "id",description = "ID do contato",example = "1",required = true)},
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(example ="Sucesso contato excluído")
                    ))
            }

    )
    public ResponseEntity<String> excluirContatoPorId(@PathVariable  Long id){
        return contatoService.excluirContatoPorId(id);

    }

   @GetMapping
   @Operation(
           summary = "Busca contatos de acordo com o parâmetros q e exibe de acordo com o parâmetro fields",
           description = "Busca contatos de acordo com o parâmetros q e exibe de acordo com o parâmetro fields",
           parameters ={@Parameter(name = "q",description = """
                   Filtro para buscar contatos que contenham o texto em qualquer um de
                   seus atributos""",example = "21",required = true),
                   @Parameter(name = "fields - List<String>"
                           ,description = """
                           Quando presente apenas os campos listados em fields deverão ser
                           retornados
                           """,example = "id,nome,contato",required = false,allowEmptyValue = true )} ,
           responses = {
                   @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                           mediaType = MediaType.APPLICATION_JSON_VALUE
                           ,array =@ArraySchema(schema = @Schema(implementation = ContatoDto.class))
                           ,examples = {
                                   @ExampleObject(name = "ListarContatos",
                                           ref = "#/components/examples/ListaContatos"
                                   )
                   }
                   ))
           }
   )
    public List<?> listarContatos(@RequestParam(required = true)  String q,
                                    @RequestParam(required = false, defaultValue = "") List<String> fields){
        return contatoService.listarPorParametros(q,fields);

    }

}

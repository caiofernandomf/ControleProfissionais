package io.caiofernandomf.ControleProfissionais.controller;

import io.caiofernandomf.ControleProfissionais.model.ContatoDto;
import io.caiofernandomf.ControleProfissionais.model.ProfissionalDto;
import io.caiofernandomf.ControleProfissionais.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@AllArgsConstructor
@Tag(name = "Profissional")
public class ProfissionalController {

    private ProfissionalService profissionalService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Cria um profissional",
            description = "Cria um profissional",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ProfissionalDto",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                            ,schema = @Schema(implementation = ProfissionalDto.class)
                            ,examples = {@ExampleObject(name = "CriaProfissional",
                            ref = "#/components/examples/CriaProfissional")
                    }

                    )),
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(example ="Sucesso profissional com id {ID} cadastrado")
                    ))
            }
    )
    public ResponseEntity<String> criarProfissional(@RequestBody ProfissionalDto profissionalDto){
        return profissionalService.criarProfissional(profissionalDto);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Busca um profissional por id",
            description = "Busca um profissional por id",
            parameters ={@Parameter(name = "id",description = "ID do profissional",example = "1",required = true)} ,
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                            ,examples = {
                            @ExampleObject(name = "BuscaProfissional",
                                    ref = "#/components/examples/BuscaProfissional"

                            )}
                            ,schema = @Schema(implementation = ProfissionalDto.class)


                    ))
            }
    )
    public ResponseEntity<ProfissionalDto> buscarProfissionalPorId(@PathVariable  Long id){
        return profissionalService.buscarProfissionalPorId(id);

    }

    @PutMapping(value = "{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Atualiza um profissional ",
            description = "Atualiza um profissional",
            parameters ={@Parameter(name = "id",description = "ID do profissional",example = "5",required = true),
                    } ,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ProfissionalDto",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE
                            ,schema = @Schema(implementation = ProfissionalDto.class)
                            ,examples = {@ExampleObject(name = "AtualizaProfissional",
                            ref = "#/components/examples/AtualizarProfissional")
                    }

                    )),
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE
                            ,examples = {
                            @ExampleObject(value = "sucesso cadastrado alterado"

                            )}
                            ,schema = @Schema(implementation = ProfissionalDto.class)


                    ))
            }
    )
    public ResponseEntity<String> atualizarProfissionalPorId(@PathVariable  Long id,@RequestBody ProfissionalDto profissionalDto ){
        return profissionalService.atualizarProfissionalPorId(profissionalDto,id);

    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deleta um profissional de acordo com o id",
            description = "Deleta um profissional de acordo com o id passado por parâmetro",
            parameters ={@Parameter(name = "id",description = "ID do profissional",example = "1",required = true)},
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(example ="Sucesso profissional excluído")
                    ))
            }

    )
    public ResponseEntity<String> excluirProfissionalPorId(@PathVariable  Long id){
        return profissionalService.excluirProfissionalPorId(id);

    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(
            summary = "Busca profissionais de acordo com o parâmetros q e exibe de acordo com o parâmetro fields",
            description = "Busca profissionais de acordo com o parâmetros q e exibe de acordo com o parâmetro fields",
            parameters ={@Parameter(name = "q",description = """
                   Filtro para buscar profissionais que contenham o texto em qualquer um de
                   seus atributos""",example = "R",required = true),
                    @Parameter(name = "fields - List<String>"
                            ,description = """
                           Quando presente apenas os campos listados em fields deverão ser
                           retornados
                           """,example = "id,nome,cargo,nascimento",required = false,allowEmptyValue = true )} ,
            responses = {
                    @ApiResponse(description = "sucess", responseCode = "200",content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                            ,array =@ArraySchema(schema = @Schema(implementation = ContatoDto.class))
                            ,examples = {
                            @ExampleObject(name = "ListarProfissionais",
                                    ref = "#/components/examples/ListaProfissionais"
                            )
                    }
                    ))
            }
    )
    public List<?> listarProfissionais(@RequestParam(required = true)  String q,
                               @RequestParam(required = false, defaultValue = "") List<String> fields ){
        return profissionalService.listarPorParametros(q,fields);

    }
}

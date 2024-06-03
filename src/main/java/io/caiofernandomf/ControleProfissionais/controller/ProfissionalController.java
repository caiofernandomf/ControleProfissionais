package io.caiofernandomf.ControleProfissionais.controller;

import io.caiofernandomf.ControleProfissionais.model.ProfissionalDto;
import io.caiofernandomf.ControleProfissionais.service.ProfissionalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@AllArgsConstructor
public class ProfissionalController {

    private ProfissionalService profissionalService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> criarProfissional(@RequestBody ProfissionalDto profissionalDto){
        return profissionalService.criarProfissional(profissionalDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfissionalDto> buscarProfissionalPorId(@PathVariable  Long id){
        return profissionalService.buscarProfissionalPorId(id);

    }

    @PutMapping(value = "{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> atualizarProfissionalPorId(@PathVariable  Long id,@RequestBody ProfissionalDto profissionalDto ){
        return profissionalService.atualizarProfissionalPorId(profissionalDto,id);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> excluirProfissionalPorId(@PathVariable  Long id){
        return profissionalService.excluirProfissionalPorId(id);

    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<?> listarProfissionais(@RequestParam(required = true)  String q,
                               @RequestParam(required = false, defaultValue = "") List<String> campos ){
        return profissionalService.listarPorParametros(q,campos);

    }
}

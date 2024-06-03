package io.caiofernandomf.ControleProfissionais.controller;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import io.caiofernandomf.ControleProfissionais.model.ContatoDto;
import io.caiofernandomf.ControleProfissionais.service.ContatoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contatos")
@AllArgsConstructor
public class ContatoController {

    private final ContatoService contatoService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> criarContato(@RequestBody ContatoDto contatoDto){
        return contatoService.criarContato(contatoDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<ContatoDto> buscarContatoPorId(@PathVariable  Long id){
        return contatoService.buscarContatoPorId(id);

    }

    @PutMapping(value = "{id}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> atualizarContatoPorId(@PathVariable  Long id,@RequestBody ContatoDto contatoDto ){
        return contatoService.atualizarContatoPorId(contatoDto,id);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> excluirContatoPorId(@PathVariable  Long id){
        return contatoService.excluirContatoPorId(id);

    }

   @GetMapping
    public List<?> listarContatos(@RequestParam(required = true)  String q,
                                    @RequestParam(required = false, defaultValue = "") List<String> campos){
        return contatoService.listarPorParametros(q,campos);

    }

}

package io.caiofernandomf.ControleProfissionais.controller;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import io.caiofernandomf.ControleProfissionais.service.ContatoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contatos")
@AllArgsConstructor
public class ContatoController {

    private final ContatoService contatoService;

    @PostMapping
    public ResponseEntity<String> criarContato(@RequestBody Contato contato){
        return contatoService.criarContato(contato);
    }

    @GetMapping("{id}")
    public ResponseEntity<Contato> buscarContatoPorId(@PathVariable  Long id){
        return contatoService.buscarContatoPorId(id);

    }

    @PutMapping("{id}")
    public ResponseEntity<String> atualizarContatoPorId(@PathVariable  Long id,@RequestBody Contato contato ){
        return contatoService.atualizarContatoPorId(contato,id);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> excluirContatoPorId(@PathVariable  Long id){
        return contatoService.excluirContatoPorId(id);

    }

    @GetMapping
    public List<Contato> listarContatos(@RequestParam(required = true)  String q,
                                    @RequestParam(required = false) List<String> campos ){
        return contatoService.buscarPorParametros(q,campos);

    }
}

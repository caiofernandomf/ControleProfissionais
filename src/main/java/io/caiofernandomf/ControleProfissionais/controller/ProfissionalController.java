package io.caiofernandomf.ControleProfissionais.controller;

import io.caiofernandomf.ControleProfissionais.model.Profissional;
import io.caiofernandomf.ControleProfissionais.service.ProfissionalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@AllArgsConstructor
public class ProfissionalController {

    private ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<String> criarContato(@RequestBody Profissional profissional){
        return profissionalService.criarProfissional(profissional);
    }

    @GetMapping("{id}")
    public ResponseEntity<Profissional> buscarContatoPorId(@PathVariable  Long id){
        return profissionalService.buscarProfissionalPorId(id);

    }

    @PutMapping("{id}")
    public ResponseEntity<String> atualizarContatoPorId(@PathVariable  Long id,@RequestBody Profissional profissional ){
        return profissionalService.atualizarProfissionalPorId(profissional,id);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> excluirContatoPorId(@PathVariable  Long id){
        return profissionalService.excluirProfissionalPorId(id);

    }

    @GetMapping
    public List listarContatos(@RequestParam(required = true)  String q,
                               @RequestParam(required = false) List<String> campos ){
        return profissionalService.buscarPorParametros(q,campos);

    }
}

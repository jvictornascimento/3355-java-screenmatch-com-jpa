package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/series")
public class SerieController {
    @Autowired
    SerieService service;

    @GetMapping
    public List<SerieDTO> obterSeries() {
        return service.obterTodasAsSeries();
    }
    @GetMapping(value = "/top5")
    public List<SerieDTO> obterTop5Series(){
        return service.obterTop5Series();
    }
    @GetMapping(value = "/lancamentos")
    public List<SerieDTO> obterLancamento(){
        return service.obterLancamentos();
    }
    @GetMapping("/{id}")
    public SerieDTO obterSerie(@PathVariable(value = "id")Long id){
        return service.obterPorId(id);
    }
    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasAsTemporadas(@PathVariable(value="id")Long id){
        return service.obtertodasAsTemporadas(id);
    }
    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTodasAsTemporadas(@PathVariable(value="id")Long id, @PathVariable(value = "numero")Long numero) {
        return service.obterTemporadasPorNumero(id, numero);
    }
    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDTO> obterSeriePorCategoria(@PathVariable(value="nomeGenero")String nomeGenero){
        return service.obterSeriePorCategoria(nomeGenero);
    }


}

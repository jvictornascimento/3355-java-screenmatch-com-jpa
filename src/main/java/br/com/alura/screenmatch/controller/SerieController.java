package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/series")
public class SerieController {
    @Autowired
    private SerieRepository repository;

    @GetMapping
    public List<SerieDTO> obterSeries() {
        return repository.findAll()
                .stream().map(s-> new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getImagemDeCapa(),s.getSinopse()))
                .collect(Collectors.toList());
    }

}

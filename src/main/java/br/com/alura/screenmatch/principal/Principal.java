package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    public Principal(SerieRepository repository){
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0 ){
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries Buscadas
                4 - Buscar Série por nome
                5 - Série por ator
                6 - Top 5 Séries
                7 - Buscar Séries por Categoria  
                8 - Buscar Sérias                           
                
                0 - Sair                                 
                """;

        System.out.println(menu);
        opcao = leitura.nextInt();
        leitura.nextLine();

        switch (opcao) {
            case 1:
                buscarSerieWeb();
                break;
            case 2:
                buscarEpisodioPorSerie();
                break;
            case 3:
               ListarSeriesBuscadas();
                break;
            case 4:
                buscarSeriePorTitulo();
                break;
            case 5:
                buscarSeriePorAtor();
                break;
            case 6:
                buscarTopSeries();
                break;
            case 7:
                buscarSeriesPorCategoria();
                break;
            case 8:
                buscarSeriesAteXTemporas();
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida");
        }
        }
    }




    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        ListarSeriesBuscadas();
        System.out.println("Escolha uma serie");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = series.stream().filter(s->s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase())).findFirst();

        if(serie.isPresent()) {
            var serieEncotrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncotrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncotrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d->d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e )))
                            .collect(Collectors.toList());
            serieEncotrada.setEpisodios(episodios);
            repository.save(serieEncotrada);
        }else {
            System.out.println("Serie não encontrada!");
        }
    }

    private void  ListarSeriesBuscadas(){
        series = repository.findAll();


        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma serie");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()){
            System.out.println("Dados da serie" + serieBuscada.get());
        }else{
            System.out.println("Serie não encontrada!");
        }
    }
    private void buscarSeriePorAtor() {
        System.out.println("Qual o nome para busca?");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliação a partir de qual valor?");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacao);
        System.out.println("Série em que " + nomeAtor + " trabalhou!");
        seriesEncontradas.forEach(s-> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarTopSeries() {
        List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s-> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));

    }
    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seiriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Series da categoria: " + nomeGenero);
        seiriesPorCategoria.forEach(System.out::println);


    }
    private void buscarSeriesAteXTemporas() {
        System.out.println("Digite a quantidade maxima de temporadas");
        var maxTemporadas = leitura.nextInt();
        System.out.println("Avaliação a partir de qual valor?");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesAteXTemporadas = repository.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(maxTemporadas,avaliacao);
        System.out.println("***** Séries Filtradas *****");
        seriesAteXTemporadas.forEach(s-> System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));


    }




}
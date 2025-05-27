package ar.edu.utn.frba.dds.fuenteEstatica.seeder.impl;

@Component
public class HechoSeeder implements CommandLineRunner{
    private final HechoService hechoService;
    private final IFuenteRepository fuenteRepository;

    public HechoSeeder(APICatedra apiCatedra, HechoService hechoService, IFuenteRepository fuenteRepository) {
        this.apiCatedra = apiCatedra;
        this.hechoService = hechoService;
        this.fuenteRepository = fuenteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        fuenteRepository.getAll().forEach(f -> {
            f.getAllHechos()
                    .doOnNext(hechoService::guardarHecho)
                    .blockLast(); // Para que espere a que termine
        });
    }
}

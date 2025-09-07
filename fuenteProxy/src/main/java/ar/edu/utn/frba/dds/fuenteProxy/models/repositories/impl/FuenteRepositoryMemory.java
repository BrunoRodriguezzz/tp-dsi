package ar.edu.utn.frba.dds.fuenteProxy.models.repositories.impl;

//@Repository
//public class FuenteRepositoryMemory implements IFuenteRepository {
//    private final Map<Long, Fuente> fuentes = new HashMap<>();
//    private final AtomicLong idGenerator = new AtomicLong(1);
//
//    @Override
//    public List<Fuente> getAll() {
//        return new ArrayList<>(fuentes.values());
//    }
//
//    @Override
//    public Fuente getById(Long id) {
//        return this.fuentes.get(id);
//    }
//
//    @Override
//    public void save(Fuente fuente) {
//        if(fuente.getId() == null) { // Es nuevo
//            Long id = idGenerator.getAndIncrement();
//            fuente.setId(id);
//            fuentes.put(id, fuente);
//        } else { // Lo actualizo
//            fuentes.put(fuente.getId(), fuente);
//        }
//    }
//
//    @Override
//    public void delete(Fuente fuente) {
//        if (fuente.getId() != null) {
//            this.fuentes.remove(fuente.getId());
//        }
//    }
//
//    @Override
//    public List<Long> devolverIDs() {
//        return new ArrayList<>(fuentes.keySet());
//    }
//}

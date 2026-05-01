package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.StockRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StockRepuestoRepository extends JpaRepository<StockRepuesto, Integer> {

    // Busca el stock de una pieza concreta en un concesionario concreto
    Optional<StockRepuesto> findByIdConcesionarioAndIdPieza(Integer idConcesionario, Integer idPieza);
}

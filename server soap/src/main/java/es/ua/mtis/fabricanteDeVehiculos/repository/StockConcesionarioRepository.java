package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.StockConcesionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockConcesionarioRepository extends JpaRepository<StockConcesionario, Integer> {

    // Busca el stock de un modelo concreto en un concesionario concreto
    Optional<StockConcesionario> findByIdModeloAndIdConcesionario(Integer idModelo, Integer idConcesionario);

    // Busca el stock de un modelo en TODOS los concesionarios (para consulta de red)
    List<StockConcesionario> findAllByIdModelo(Integer idModelo);
}
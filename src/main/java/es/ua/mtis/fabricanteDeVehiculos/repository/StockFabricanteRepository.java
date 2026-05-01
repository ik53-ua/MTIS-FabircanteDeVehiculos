package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.StockFabricante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StockFabricanteRepository extends JpaRepository<StockFabricante, Integer> {

    Optional<StockFabricante> findByIdModelo(Integer idModelo);
}

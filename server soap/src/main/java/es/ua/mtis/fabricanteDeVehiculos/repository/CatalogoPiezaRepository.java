package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.CatalogoPieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoPiezaRepository extends JpaRepository<CatalogoPieza, Integer> {
    // findById(Integer idPieza) heredado de JpaRepository es suficiente
}

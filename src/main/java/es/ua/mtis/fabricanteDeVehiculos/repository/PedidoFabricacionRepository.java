package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoFabricacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoFabricacionRepository extends JpaRepository<PedidoFabricacion, Integer> {
    // JpaRepository ya provee findById(Integer idPedido) que es suficiente para los casos de uso del proceso 2.2
}

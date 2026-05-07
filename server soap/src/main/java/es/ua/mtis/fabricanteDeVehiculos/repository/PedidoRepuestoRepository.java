package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.PedidoRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepuestoRepository extends JpaRepository<PedidoRepuesto, Integer> {
    // findById(Integer idPedido) heredado de JpaRepository es suficiente
}

package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.EnvioRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepuestoRepository extends JpaRepository<EnvioRepuesto, Integer> {
    // findById(Integer idEnvio) heredado de JpaRepository es suficiente
}

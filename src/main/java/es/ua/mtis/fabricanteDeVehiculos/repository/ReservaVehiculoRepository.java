package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.ReservaVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; 

@Repository
public interface ReservaVehiculoRepository extends JpaRepository<ReservaVehiculo, Integer> {
    
    Optional<ReservaVehiculo> findByCodigoBloqueo(String codigoBloqueo);
}
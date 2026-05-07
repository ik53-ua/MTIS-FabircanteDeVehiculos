package es.ua.mtis.fabricanteDeVehiculos.repository;

import es.ua.mtis.fabricanteDeVehiculos.entity.ConfiguracionModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConfiguracionModeloRepository extends JpaRepository<ConfiguracionModelo, Integer> {

    // Busca si existe una configuración concreta para un modelo
    Optional<ConfiguracionModelo> findByIdModeloAndConfiguracion(Integer idModelo, String configuracion);
}

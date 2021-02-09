package co.cristian.springboot.quileia.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.cristian.springboot.quileia.models.entity.Restaurante;

@Repository
public interface RestauranteDAO extends JpaRepository<Restaurante, Integer> {

}

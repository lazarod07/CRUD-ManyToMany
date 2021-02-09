package co.cristian.springboot.quileia.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.cristian.springboot.quileia.models.entity.Menu;

@Repository
public interface MenuDAO extends JpaRepository<Menu, Integer> {
	
	List<Menu> findBytipo(Integer tipo);

}

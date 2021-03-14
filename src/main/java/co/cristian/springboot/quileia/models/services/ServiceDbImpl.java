package co.cristian.springboot.quileia.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.cristian.springboot.quileia.models.dao.IngredienteDAO;
import co.cristian.springboot.quileia.models.dao.MenuDAO;
import co.cristian.springboot.quileia.models.dao.RestauranteDAO;
import co.cristian.springboot.quileia.models.entity.Ingrediente;
import co.cristian.springboot.quileia.models.entity.Menu;
import co.cristian.springboot.quileia.models.entity.Restaurante;

@Service
public class ServiceDbImpl implements IServiceDb {

	@Autowired
	private RestauranteDAO restauranteDAO;

	@Autowired
	private MenuDAO menuDAO;

	@Autowired
	private IngredienteDAO ingredienteDAO;

	@Override
	public List<Restaurante> findAllRestaurantes() {

		return restauranteDAO.findAll();

	}

	@Override
	public void saveRestaurante(Restaurante restaurante) {

		restauranteDAO.save(restaurante);

	}

	@Override
	public Restaurante findByIdRestaurante(Integer id) {

		return restauranteDAO.findById(id).get();

	}

	@Override
	public void deleteRestaurante(Restaurante restaurante) {

		restauranteDAO.delete(restaurante);

	}

	@Override
	public List<Menu> findAllMenu() {
		
		return menuDAO.findAll();
		
	}

	@Override
	public Menu findByIdMenu(Integer id) {
		
		return menuDAO.findById(id).get();
		
	}

	@Override
	public void saveMenu(Menu menu) {
		
		menuDAO.save(menu);
		
	}

	@Override
	public List<Menu> findBytipoMenu(Integer tipo) {
		
		return menuDAO.findBytipo(tipo);
		
	}

	@Override
	public void deleteMenu(Menu menu) {
		
		menuDAO.delete(menu);
		
	}

	@Override
	public Ingrediente findByIdIngrediente(Integer id) {
		
		return ingredienteDAO.findById(id).get();
		
	}

	@Override
	public void saveIngrediente(Ingrediente ingrediente) {
		
		ingredienteDAO.save(ingrediente);
		
	}

}

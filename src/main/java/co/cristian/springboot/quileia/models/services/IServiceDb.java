package co.cristian.springboot.quileia.models.services;

import java.util.List;

import co.cristian.springboot.quileia.models.entity.Ingrediente;
import co.cristian.springboot.quileia.models.entity.Menu;
import co.cristian.springboot.quileia.models.entity.Restaurante;

public interface IServiceDb {

	public List<Restaurante> findAllRestaurantes();

	public void saveRestaurante(Restaurante restaurante);

	public Restaurante findByIdRestaurante(Integer id);

	public void deleteRestaurante(Restaurante restaurante);
	
	public List<Menu> findAllMenu();
	
	public Menu findByIdMenu(Integer id);
	
	public void saveMenu(Menu menu);
	
	public List<Menu> findBytipoMenu(Integer tipo);
	
	public void deleteMenu(Menu menu);
	
	public Ingrediente findByIdIngrediente(Integer id);
	
	public void saveIngrediente(Ingrediente ingrediente);

}

package co.cristian.springboot.quileia.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import co.cristian.springboot.quileia.models.dao.MenuDAO;
import co.cristian.springboot.quileia.models.dao.RestauranteDAO;
import co.cristian.springboot.quileia.models.entity.Menu;
import co.cristian.springboot.quileia.models.entity.Restaurante;

@Controller
@SessionAttributes({"restaurante", "menu"})
@RequestMapping("/restaurante")
public class RestauranteController {
	
	@Autowired
	private RestauranteDAO restauranteDAO;
	
	@Autowired
	private MenuDAO menuDAO;
	
	@GetMapping("/lista")
	public String lista(Model model) {
		
		List<Restaurante> restaurantes = restauranteDAO.findAll();
		
		model.addAttribute("restaurantes", restaurantes);
		
		model.addAttribute("titulo","Lista de restaurantes");
		
		return "/restaurante/lista";
		
	}
	
	@GetMapping("/agregar")
	public String agregar(Model model) {
		
		Restaurante restaurante = new Restaurante();
		
		model.addAttribute("restaurante", restaurante);
		
		model.addAttribute("titulo","Agregar restaurante");
		
		return "/restaurante/agregar";
		
	}
	
	@PostMapping("/agregar")
	public String agregar(@Valid Restaurante restaurante, BindingResult result ,Model model) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo","Agregar restaurante (Campos incorrectos)");
			
			return "/restaurante/agregar";
			
		}
		
		restauranteDAO.save(restaurante);
		
		model.addAttribute("titulo","Resultado");
		
		return "redirect:/restaurante/lista";
		
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {
		
		Restaurante restaurante = restauranteDAO.findById(id).get();
		
		model.addAttribute("restaurante", restaurante) ;
		
		model.addAttribute("titulo","Editar restaurante");
		
		return "/restaurante/editar";
		
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Restaurante restaurante, BindingResult result ,Model model, SessionStatus status) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo","Editar restaurante (Campos incorrectos)");
			
			return "/restaurante/editar";
			
		}
		
		restauranteDAO.save(restaurante);
		
		status.setComplete();
		
		return "redirect:/restaurante/lista";
		
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id ,Model model) {
		
		Restaurante restaurante = restauranteDAO.findById(id).get();
		
		restauranteDAO.delete(restaurante);
		
		return "redirect:/restaurante/lista";
		
	}
	
	@GetMapping("/{id}/listamenus")
	public String menus(@PathVariable Integer id ,Model model) {
		
		Restaurante restaurante = restauranteDAO.findById(id).get();
		
		model.addAttribute("titulo","Lista de menús");
		
		model.addAttribute("restauranteId",id);
		
		model.addAttribute("menus", restaurante.getMenus());
		
		return "/restaurante/listaMenus";
		
	}
	
	@GetMapping("/{id}/agregarmenu")
	public String agregarmenu(@PathVariable Integer id, Model model) {
		
		Menu menu = new Menu();
		
		model.addAttribute("menu", menu);
		
		System.out.println(id);
		
		model.addAttribute("restauranteId", id);
		
		model.addAttribute("titulo","Agregar menú");
		
		return "/restaurante/agregarMenu";
		
	}
	
	@PostMapping("/{id}/agregarmenu")
	public String agregarmenu(@PathVariable Integer id, @Valid Menu menu, BindingResult result ,Model model) {
		
		model.addAttribute("restauranteId", id);
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo","Agregar menú (Campos incorrectos)");
			
			return "/restaurante/agregarMenu";
			
		}
		
		Restaurante restaurante = restauranteDAO.findById(id).get();
		
		menu.setId(null);
		
		restaurante.addMenu(menu);
		
		restauranteDAO.save(restaurante);
		
		return "redirect:/restaurante/"+restaurante.getId()+"/listamenus";
		
	}
	
	@GetMapping("/{id}/listaagregarmenuexistente")
	public String agregarMenuExistente(@PathVariable Integer id, Model model) {
		
		Restaurante restaurante = restauranteDAO.findById(id).get();
		
		List<Menu> menus = menuDAO.findAll();
		
		menus.removeAll(restaurante.getMenus());

		model.addAttribute("titulo","Lista de menú agregables");
		
		model.addAttribute("restauranteId",id);
		
		model.addAttribute("menus", menus);
		
		return "/restaurante/listaAgregarMenuExistente";
		
	}
	
	@GetMapping("/{idRes}/agregarmenuexistente/{idMen}")
	public String agregarMenuExistente(@PathVariable Integer idRes, @PathVariable Integer idMen) {
		
		Restaurante restaurante = restauranteDAO.findById(idRes).get();
		
		Menu menu = menuDAO.findById(idMen).get();
		
		restaurante.addMenu(menu);
		
		restauranteDAO.save(restaurante);
		
		return "redirect:/restaurante/"+restaurante.getId()+"/listamenus";
		
	}
	
	@GetMapping("/{idRes}/editarmenu/{idMen}")
	public String editarMenu(@PathVariable Integer idRes, @PathVariable Integer idMen,  Model model) {
		
		Menu menu = menuDAO.findById(idMen).get();
		
		model.addAttribute("restauranteId", idRes);
		
		model.addAttribute("menu", menu) ;
		
		model.addAttribute("titulo","Editar menú");
		
		return "/restaurante/editarMenu";
		
	}
	
	@PostMapping("/{idRes}/editarmenu")
	public String editarMenu(@PathVariable Integer idRes,@Valid Menu menu, BindingResult result ,Model model, SessionStatus status) {
		
		model.addAttribute("restauranteId", idRes);
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo","Editar menú (Campos incorrectos)");
			
			return "/restaurante/editarMenu";
			
		}
		
		model.addAttribute("restauranteId", idRes);
		
		menuDAO.save(menu);
		
		status.setComplete();
		
		return "redirect:/restaurante/"+idRes+"/listamenus";
		
	}
	
	
	@GetMapping("/{idRes}/eliminarmenu/{idMen}")
	public String eliminarMenu(@PathVariable Integer idRes , @PathVariable Integer idMen ,Model model) {
		
		Restaurante restaurante = restauranteDAO.findById(idRes).get();
		
		Menu menu = menuDAO.findById(idMen).get();
		
		restaurante.deleteMenu(menu);
		
		restauranteDAO.save(restaurante);
		
		return "redirect:/restaurante/"+idRes+"/listamenus";
		
	}

}

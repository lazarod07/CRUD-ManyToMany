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

import co.cristian.springboot.quileia.models.entity.Menu;
import co.cristian.springboot.quileia.models.entity.Restaurante;
import co.cristian.springboot.quileia.models.services.IServiceDb;

@Controller
@SessionAttributes({"restaurante", "menu"})
@RequestMapping("/restaurante")
public class RestauranteController {
	
	@Autowired
	private IServiceDb serviceDb;
	
	@GetMapping("/lista")
	public String lista(Model model) {
		
		model.addAttribute("restaurantes", serviceDb.findAllRestaurantes());
		
		model.addAttribute("titulo","Lista de restaurantes");
		
		return "/restaurante/lista";
		
	}
	
	@GetMapping("/agregar")
	public String agregar(Model model) {
		
		model.addAttribute("restaurante", new Restaurante());
		
		model.addAttribute("titulo","Agregar restaurante");
		
		return "/restaurante/agregar";
		
	}
	
	@PostMapping("/agregar")
	public String agregar(@Valid Restaurante restaurante, BindingResult result ,Model model) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo","Agregar restaurante (Campos incorrectos)");
			
			return "/restaurante/agregar";
			
		}
		
		serviceDb.saveRestaurante(restaurante);
		
		model.addAttribute("titulo","Resultado");
		
		return "redirect:/restaurante/lista";
		
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {

		model.addAttribute("restaurante", serviceDb.findByIdRestaurante(id)) ;
		
		model.addAttribute("titulo","Editar restaurante");
		
		return "/restaurante/editar";
		
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Restaurante restaurante, BindingResult result ,Model model, SessionStatus status) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("titulo","Editar restaurante (Campos incorrectos)");
			
			return "/restaurante/editar";
			
		}
		
		serviceDb.saveRestaurante(restaurante);
		
		status.setComplete();
		
		return "redirect:/restaurante/lista";
		
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id ,Model model) {
		
		serviceDb.deleteRestaurante(serviceDb.findByIdRestaurante(id));
		
		return "redirect:/restaurante/lista";
		
	}
	
	@GetMapping("/{id}/listamenus")
	public String menus(@PathVariable Integer id ,Model model) {
		
		model.addAttribute("titulo","Lista de menús");
		
		model.addAttribute("restauranteId",id);
		
		model.addAttribute("menus", serviceDb.findByIdRestaurante(id).getMenus());
		
		return "/restaurante/listaMenus";
		
	}
	
	@GetMapping("/{id}/agregarmenu")
	public String agregarmenu(@PathVariable Integer id, Model model) {
		
		Menu menu = new Menu();
		
		model.addAttribute("menu", menu);
		
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
		
		Restaurante restaurante = serviceDb.findByIdRestaurante(id);
		
		menu.setId(null);
		
		restaurante.addMenu(menu);
		
		serviceDb.saveRestaurante(restaurante);
		
		return "redirect:/restaurante/"+restaurante.getId()+"/listamenus";
		
	}
	
	@GetMapping("/{id}/listaagregarmenuexistente")
	public String agregarMenuExistente(@PathVariable Integer id, Model model) {
		
		List<Menu> menus = serviceDb.findAllMenu();
		
		menus.removeAll(serviceDb.findByIdRestaurante(id).getMenus());

		model.addAttribute("titulo","Lista de menú agregables");
		
		model.addAttribute("restauranteId",id);
		
		model.addAttribute("menus", menus);
		
		return "/restaurante/listaAgregarMenuExistente";
		
	}
	
	@GetMapping("/{idRes}/agregarmenuexistente/{idMen}")
	public String agregarMenuExistente(@PathVariable Integer idRes, @PathVariable Integer idMen) {
		
		Restaurante restaurante = serviceDb.findByIdRestaurante(idRes);
		
		restaurante.addMenu(serviceDb.findByIdMenu(idMen));
		
		serviceDb.saveRestaurante(restaurante);
		
		return "redirect:/restaurante/"+restaurante.getId()+"/listamenus";
		
	}
	
	@GetMapping("/{idRes}/editarmenu/{idMen}")
	public String editarMenu(@PathVariable Integer idRes, @PathVariable Integer idMen,  Model model) {
		
		model.addAttribute("restauranteId", idRes);
		
		model.addAttribute("menu", serviceDb.findByIdMenu(idMen)) ;
		
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
		
		serviceDb.saveMenu(menu);
		
		status.setComplete();
		
		return "redirect:/restaurante/"+idRes+"/listamenus";
		
	}
	
	
	@GetMapping("/{idRes}/eliminarmenu/{idMen}")
	public String eliminarMenu(@PathVariable Integer idRes , @PathVariable Integer idMen ,Model model) {
		
		Restaurante restaurante = serviceDb.findByIdRestaurante(idRes);
		
		restaurante.deleteMenu(serviceDb.findByIdMenu(idMen));
		
		serviceDb.saveRestaurante(restaurante);
		
		return "redirect:/restaurante/"+idRes+"/listamenus";
		
	}

}

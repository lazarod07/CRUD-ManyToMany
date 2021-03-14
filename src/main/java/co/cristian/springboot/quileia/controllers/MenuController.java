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

import co.cristian.springboot.quileia.models.entity.Ingrediente;
import co.cristian.springboot.quileia.models.entity.Menu;
import co.cristian.springboot.quileia.models.services.IServiceDb;

@Controller
@SessionAttributes({ "menu", "ingrediente" })
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	private IServiceDb serviceDb;

	@GetMapping("/lista")
	public String lista(Model model) {

		List<Menu> munusTipo1 = serviceDb.findBytipoMenu(1);

		List<Menu> munusTipo2 = serviceDb.findBytipoMenu(2);

		List<Menu> munusTipo3 = serviceDb.findBytipoMenu(3);

		List<Menu> munusTipo4 = serviceDb.findBytipoMenu(4);

		model.addAttribute("tipo1", munusTipo1);

		model.addAttribute("tipo2", munusTipo2);

		model.addAttribute("tipo3", munusTipo3);

		model.addAttribute("tipo4", munusTipo4);

		model.addAttribute("titulo", "Lista de menús");

		return "/menu/lista";

	}

	@GetMapping("/agregar")
	public String agregar(Model model) {

		Menu menu = new Menu();

		model.addAttribute("menu", menu);

		model.addAttribute("titulo", "Agregar menú");

		return "/menu/agregar";

	}

	@PostMapping("/agregar")
	public String agregarmenu(@Valid Menu menu, BindingResult result, Model model) {

		if (result.hasErrors()) {

			model.addAttribute("titulo", "Agregar menú (Campos incorrectos)");

			return "/menu/agregar";

		}

		serviceDb.saveMenu(menu);

		return "redirect:/menu/lista";

	}

	@GetMapping("/editar/{id}")
	public String editarMenu(@PathVariable Integer id, Model model) {

		model.addAttribute("menu", serviceDb.findByIdMenu(id));

		model.addAttribute("titulo", "Editar menú");

		return "/menu/editar";

	}

	@PostMapping("/editar")
	public String editarMenu(@Valid Menu menu, BindingResult result, Model model, SessionStatus status) {

		if (result.hasErrors()) {

			model.addAttribute("titulo", "Editar menú (Campos incorrectos)");

			return "/menu/editar";

		}

		serviceDb.saveMenu(menu);

		status.setComplete();

		return "redirect:/menu/lista";

	}

	@GetMapping("/eliminar/{id}")
	public String eliminarMenu(@PathVariable Integer id, Model model) {

		Menu menu = serviceDb.findByIdMenu(id);

		menu.getRestaurantes().forEach(r -> {

			r.deleteMenu(menu);

		});

		serviceDb.deleteMenu(menu);

		return "redirect:/menu/lista";

	}

	@GetMapping("/{id}/listaingredientes")
	public String listaIngredientes(@PathVariable Integer id, Model model) {

		model.addAttribute("titulo", "Lista de ingredientes");

		model.addAttribute("menuId", id);

		model.addAttribute("ingredientes", serviceDb.findByIdMenu(id).getIngredientes());

		return "/menu/listaIngredientes";

	}

	@GetMapping("/{id}/agregaringrediente")
	public String agregarIngrediente(@PathVariable Integer id, Model model) {

		Ingrediente ingrediente = new Ingrediente();

		model.addAttribute("ingrediente", ingrediente);

		model.addAttribute("menuId", id);

		model.addAttribute("titulo", "Agregar Ingrediente");

		return "/menu/agregarIngrediente";

	}

	@PostMapping("/{id}/agregaringrediente")
	public String agregarmenu(@PathVariable Integer id, @Valid Ingrediente ingrediente, BindingResult result,
			Model model) {

		Menu menu = serviceDb.findByIdMenu(id);

		model.addAttribute("menuId", id);

		if (result.hasErrors()) {

			model.addAttribute("titulo", "Agregar Ingrediente (Campos incorectos)");

			return "/menu/agregarIngrediente";

		}

		if (!menu.aceptable(menu, ingrediente)) {

			model.addAttribute("titulo", "Agregar Ingrediente (El limite de calorias fue superado)");

			return "/menu/agregarIngrediente";

		}

		ingrediente.setId(null);

		menu.addIngrediente(ingrediente);

		serviceDb.saveMenu(menu);

		return "redirect:/menu/" + menu.getId() + "/listaingredientes";

	}

	@GetMapping("/{idMen}/editaringrediente/{idIng}")
	public String editarIngrediente(@PathVariable Integer idMen, @PathVariable Integer idIng, Model model) {

		model.addAttribute("ingrediente", serviceDb.findByIdIngrediente(idIng));

		model.addAttribute("idMenu", idMen);

		model.addAttribute("titulo", "Editar ingrediente");

		return "/menu/editarIngrediente";

	}

	@PostMapping("/{idMen}/editaringrediente")
	public String editarMenu(@PathVariable Integer idMen, @Valid Ingrediente ingrediente, BindingResult result,
			Model model, SessionStatus status) {

		model.addAttribute("idMenu", idMen);

		Menu menu = serviceDb.findByIdMenu(idMen);

		if (result.hasErrors()) {

			model.addAttribute("titulo", "Editar Ingrediente (Campos incorectos)");

			return "/menu/editarIngrediente";

		}

		if (!menu.aceptable(menu, ingrediente)) {

			model.addAttribute("titulo", "Editar Ingrediente (El limite de calorías fue superado)");

			return "/menu/editarIngrediente";

		}

		serviceDb.saveIngrediente(ingrediente);

		status.setComplete();

		return "redirect:/menu/" + idMen + "/listaingredientes";

	}

	@GetMapping("/{idMen}/eliminaringrediente/{idIng}")
	public String eliminarMenu(@PathVariable Integer idMen, @PathVariable Integer idIng, Model model) {

		Menu menu = serviceDb.findByIdMenu(idMen);

		menu.deleteIngrediente(serviceDb.findByIdIngrediente(idIng));

		serviceDb.saveMenu(menu);

		return "redirect:/menu/" + idMen + "/listaingredientes";

	}

}

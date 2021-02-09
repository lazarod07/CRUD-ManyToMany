package co.cristian.springboot.quileia.models.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "menu")
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Column(name = "tipo")
	private Integer tipo;

	@NotBlank
	@Column(name = "nombre")
	private String nombre;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "menu_ingrediente", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "ingrediente_id"))
	private Set<Ingrediente> ingredientes;

	@NotNull
	@Column(name = "precio")
	private Double precio;

	@ManyToMany(mappedBy = "menus")
	private Set<Restaurante> restaurantes = new HashSet<>();;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Set<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(Set<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	public Set<Restaurante> getRestaurantes() {
		return restaurantes;
	}

	public void setRestaurantes(Set<Restaurante> restaurantes) {
		this.restaurantes = restaurantes;
	}

	public Menu() {

	}

	public Menu(@NotNull Integer tipo, @NotBlank String nombre, @NotNull Double precio) {
		this.tipo = tipo;
		this.nombre = nombre;
		this.precio = precio;
	}

	public void addIngrediente(Ingrediente ingrediente) {

		ingredientes.add(ingrediente);

		ingrediente.getMenus().add(this);

	}

	public void deleteIngrediente(Ingrediente ingrediente) {

		ingredientes.remove(ingrediente);

		ingrediente.getMenus().remove(this);

	}

	public boolean aceptable(Menu menu, Ingrediente ingrediente) {

		Integer suma = 0;

		for (Ingrediente i : menu.getIngredientes()) {
			
			if(i.getId() != ingrediente.getId()) {
				
				suma += i.getCalorias();
				
			}			

		}

		suma += ingrediente.getCalorias();

		if (suma > 2000) {

			return false;

		} else {

			return true;

		}

	}

}

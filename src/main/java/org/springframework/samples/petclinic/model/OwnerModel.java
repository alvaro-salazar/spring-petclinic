package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerModel extends RepresentationModel<OwnerModel> {

	private Integer id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String telephone;
	private List<PetModel> pets; // Lista de mascotas del propietario

	// Getters and Setters

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("address")
	public String getAddress() {
		return address;
	}

	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}

	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("telephone")
	public String getTelephone() {
		return telephone;
	}

	@JsonProperty("telephone")
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<PetModel> getPets() {
		return pets;
	}

	public void setPets(List<PetModel> pets) {
		this.pets = pets;
	}

	// Método de conversión
	public Owner toOwner() {
		Owner owner = new Owner();
		owner.setId(this.id);
		owner.setFirstName(this.firstName);
		owner.setLastName(this.lastName);
		owner.setAddress(this.address);
		owner.setCity(this.city);
		owner.setTelephone(this.telephone);
//		owner.setPets(this.pets.stream().map(PetModel::toPet).collect(Collectors.toList())); // Map pets
		return owner;
	}


}

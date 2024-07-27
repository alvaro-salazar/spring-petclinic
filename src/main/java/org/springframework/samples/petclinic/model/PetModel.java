package org.springframework.samples.petclinic.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;

import java.time.LocalDate;

public class PetModel extends RepresentationModel<PetModel> {
	private int id;
	private String name;
	private String birthDate;
	private String type;

	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// Method to convert PetModel to Pet entity
	public Pet toPet() {
		Pet pet = new Pet();
		pet.setId(this.id);
		pet.setName(this.name);
		pet.setBirthDate(LocalDate.parse(this.birthDate));
		PetType petType=new PetType();
		petType.setName(this.type);
		pet.setType(petType);
		return pet;
	}

	public static PetModel toPetModel(Pet pet){
		PetModel petModel = new PetModel();
		petModel.setId(pet.getId());
		petModel.setName(pet.getName());
		petModel.setType(pet.getType().toString());
		petModel.setBirthDate(pet.getBirthDate().toString());
		return petModel;
	}
}


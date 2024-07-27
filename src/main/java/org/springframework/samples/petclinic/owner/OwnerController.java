/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.samples.petclinic.model.OwnerModel;
import org.springframework.samples.petclinic.model.OwnerModelPage;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);
	private final OwnerRepository owners;

	@Value("${ownerservice.url}")
	private String ownerServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public OwnerController(OwnerRepository clinicService) {
		this.owners = clinicService;
	}

	@Autowired
	private ObjectMapper objectMapper;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		if (ownerId == null) {
			return new Owner();
		} else {
			ResponseEntity<Owner> response = restTemplate.getForEntity(ownerServiceUrl + "/owners/" + ownerId, Owner.class);
			return response.getBody();
		}
	}

	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		restTemplate.postForEntity(ownerServiceUrl + "/owners", owner, Owner.class);
		redirectAttributes.addFlashAttribute("message", "New Owner Created");
		return "redirect:/owners";
	}

	@GetMapping("/owners/find")
	public String initFindForm() {
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
			Model model) {
		// allow parameterless GET request for /owners to return all records
//		logger.debug("Processing find form for Owner with last name: {}", owner.getLastName());
		if (owner.getLastName() == null) {
			owner.setLastName("");
		}

//		logger.debug("Page number: {}", page);
		Pageable pageable = PageRequest.of(page - 1, 5); // Asegúrate de que pageSize es al menos 1

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(ownerServiceUrl + "/owners/search")
			.queryParam("lastName", owner.getLastName())
			.queryParam("page", pageable.getPageNumber())
			.queryParam("size", pageable.getPageSize());

//		logger.debug("uriBuilder: {}",uriBuilder.toUriString());

		// Capturar la respuesta como una cadena de texto
		try {
			RestTemplate simpleRestTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity = simpleRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, String.class);
			String responseString = responseEntity.getBody();
//			logger.debug("Response from microservice: {}", responseString);

			// Deserializar la respuesta capturada
			OwnerModelPage ownersResults = objectMapper.readValue(responseString, OwnerModelPage.class);

			if (ownersResults == null || ownersResults.getEmbeddedOwnerModelList().getOwnerModels().isEmpty()) {
				logger.debug("No Owners found for last name: {}", owner.getLastName());
				result.rejectValue("lastName", "notFound", "not found");
				return "owners/findOwners";
			}

			if (ownersResults.getEmbeddedOwnerModelList().getOwnerModels().size() == 1) {
				owner = ownersResults.getEmbeddedOwnerModelList().getOwnerModels().get(0).toOwner();
				logger.debug("One Owner found: redirecting to Owner details");
				return "redirect:/owners/" + owner.getId();
			}

			logger.debug("Multiple Owners found: adding pagination model");
			return addPaginationModel(page, model, ownersResults);
		} catch (Exception e) {
			logger.error("Error processing find form: ", e);
			result.rejectValue("lastName", "error", "Error occurred while fetching owners");
			return "owners/findOwners";
		}
	}

	private String addPaginationModel(int page, Model model, OwnerModelPage paginated) {
		List<OwnerModel> listOwners = paginated.getEmbeddedOwnerModelList().getOwnerModels();

		for (OwnerModel ownerModel : listOwners) {
			// Consulta a la base de datos del sistema heredado para obtener los PetModel
			String sql = "SELECT id, name, birth_date, type_id FROM pets WHERE owner_id = ?";
//			logger.debug("SQL: {}", sql);
			List<Pet> pets = jdbcTemplate.query(
				sql,
				new Object[]{ownerModel.getId()},
				(rs, rowNum) -> {
					Pet pet = new Pet();
					pet.setId(rs.getInt("id"));
					pet.setName(rs.getString("name"));
//					logger.debug("Pet name: {}",pet.getName());
					pet.setBirthDate(LocalDate.parse(rs.getString("birth_date")));
					PetType petType=new PetType();
					petType.setId(rs.getInt("type_id"));
					pet.setType(petType);

					return pet;
				}
			);
			ownerModel.setPets(pets);
		}

		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getMetadata().getTotalPages());
		model.addAttribute("totalItems", paginated.getMetadata().getTotalElements());
		model.addAttribute("listOwners", listOwners);
//		logger.debug("Pagination model added: {} Owners on page {}", listOwners.size(), page);
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastName(lastname, pageable);
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		ResponseEntity<Owner> response = restTemplate.getForEntity(ownerServiceUrl + "/owners/" + ownerId, Owner.class);
		model.addAttribute(Objects.requireNonNull(response.getBody()));
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in updating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		owner.setId(ownerId);
		this.owners.save(owner);
		redirectAttributes.addFlashAttribute("message", "Owner Values Updated");
		return "redirect:/owners/{ownerId}";
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		ResponseEntity<Owner> response = restTemplate.getForEntity(ownerServiceUrl + "/owners/" + ownerId, Owner.class);
		mav.addObject(response.getBody());
		return mav;
	}

}

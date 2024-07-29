package co.edu.uniandes.ownerservice.model;

import co.edu.uniandes.ownerservice.controller.OwnerController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class OwnerModelAssembler extends RepresentationModelAssemblerSupport<Owner, OwnerModel> {

    public OwnerModelAssembler() {
        super(OwnerController.class, OwnerModel.class);
    }

    @Override
    public OwnerModel toModel(Owner owner) {
        OwnerModel model = instantiateModel(owner);
        model.add(linkTo(methodOn(OwnerController.class).getOwnerById(owner.getId())).withSelfRel());
        model.setId(owner.getId());
        model.setFirstName(owner.getFirstName());
        model.setLastName(owner.getLastName());
        model.setAddress(owner.getAddress());
        model.setCity(owner.getCity());
        model.setTelephone(owner.getTelephone());
        return model;
    }
}

package co.edu.uniandes.ownerservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class OwnerModel extends RepresentationModel<OwnerModel> {

    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;

}

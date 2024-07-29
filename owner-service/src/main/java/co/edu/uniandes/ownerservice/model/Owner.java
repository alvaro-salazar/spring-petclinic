package co.edu.uniandes.ownerservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;

@Getter
@Setter
@Entity
@Table(name = "owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "address")
    @NotBlank
    private String address;

    @Column(name = "city")
    @NotBlank
    private String city;

    @Column(name = "telephone")
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Telephone must be a 10-digit number")
    private String telephone;

    @Override
    public String toString() {
        return new ToStringCreator(this).append("id", this.getId())
                .append("new", this.isNew())
                .append("lastName", this.getLastName())
                .append("firstName", this.getFirstName())
                .append("address", this.address)
                .append("city", this.city)
                .append("telephone", this.telephone)
                .toString();
    }

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    public boolean isNew() {
        return this.id == null;
    }

}
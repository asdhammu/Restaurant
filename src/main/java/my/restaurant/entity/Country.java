package my.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
@Getter
@Setter
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countrySeq")
    @SequenceGenerator(name = "countrySeq", sequenceName = "country_country_id_seq")
    @Column(name = "country_id")
    private Long countryId;
    @Column(name = "name", nullable = false)
    @NotNull
    private String name;
    @Column(name = "iso_code_2")
    private String isoCode2;
    @Column(name = "iso_code_3")
    private String isoCode3;
    @OneToMany(mappedBy = "country")
    private List<State> states = new ArrayList<>();
}

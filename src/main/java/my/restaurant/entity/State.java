package my.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Table(name = "state")
@Entity
@Getter
@Setter
public class State extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stateSeq")
    @SequenceGenerator(name = "stateSeq", sequenceName = "state_state_id_seq")
    @Column(name = "state_id")
    private Long stateId;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "abbreviation")
    private String abbreviation;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @NotNull
    private Country country;

}

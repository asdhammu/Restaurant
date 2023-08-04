package my.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.modal.AddressType;

@Table(name = "user_address")
@Entity
@Getter
@Setter
public class UserAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userAddressSeq")
    @SequenceGenerator(name = "userAddressSeq", sequenceName = "user_address_address_id_seq")
    @Column(name = "address_id")
    public Long addressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Column(name = "email_id")
    @NotNull
    public String emailId;

    @Column(name = "first_name")
    @NotNull
    public String firstName;

    @Column(name = "last_name")
    @NotNull
    public String lastName;

    @Column(name = "street_address_1")
    @NotNull
    public String streetAddress1;

    @Column(name = "street_address_2")
    public String streetAddress2;

    @ManyToOne
    @JoinColumn(name = "state_id")
    public State state;

    @NotNull
    @Column(name = "city")
    public String city;

    @Column(name = "postal_code")
    @NotNull
    public String postalCode;

    @ManyToOne
    @JoinColumn(name = "country_id")
    public Country country;

    @Column(name = "is_default")
    public boolean myDefault;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    @NotNull
    public AddressType addressType;

}

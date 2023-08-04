package my.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.modal.PaymentMethodType;

@Table(name = "user_payment_method")
@Entity
@Getter
@Setter
public class UserPaymentMethod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userPaymentSeq")
    @SequenceGenerator(name = "userPaymentSeq", sequenceName = "user_payment_method_payment_method_id_seq")
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "credit_card_number")
    @NotNull
    private String creditCardNumber;

    @Column(name = "cvv")
    @NotNull
    private String cvv;

    @Column(name = "expiry_month")
    private int expiryMonth;

    @Column(name = "expiry_year")
    private int expiryYear;

    @Column(name = "is_default")
    private boolean myDefault;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    @NotNull
    private PaymentMethodType paymentMethodType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

package my.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.modal.PaymentMethodType;

import java.util.ArrayList;
import java.util.List;

@Table(name = "payment_method")
@Entity
@Getter
@Setter
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentMethodSeq")
    @SequenceGenerator(name = "paymentMethodSeq", sequenceName = "payment_method_payment_method_id_seq")
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

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    @NotNull
    private PaymentMethodType paymentMethodType;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Order> orders = new ArrayList<>();

    public PaymentMethod(long id) {
        this.paymentMethodId = id;
    }

    public PaymentMethod() {

    }
}

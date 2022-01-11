package konkuk.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private String address;
    private String phone; //010123455678
    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(name="delivery_state")
    private DeliveryState deliveryState;
}

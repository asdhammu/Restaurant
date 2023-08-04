package my.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "carousel")
@Entity
@Getter
@Setter
public class Carousel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carouselSeq")
    @SequenceGenerator(name = "carouselSeq", sequenceName = "carousel_id_seq")
    private Long id;

    @Column(name = "priority")
    private int priority;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "height")
    private int height;

    @Column(name = "width")
    private int width;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}

package my.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorySeq")
    @SequenceGenerator(name = "categorySeq", sequenceName = "category_category_id_seq")
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name")
    private String categoryName;

    @Column(name = "img_url")
    private String imgUrl;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category(long categoryId){
        this.categoryId = categoryId;
    }

    public Category(){

    }
}

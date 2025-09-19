package br.com.cultive_api.entites;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_products_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    private String name;

    public Long getCategoryId() {return categoryId;}
    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}



    public enum Values {
        HORTALICAS(1L),
        FRUTAS(2L),
        LEGUMES(3L),
        GRAOS_CEREAIS(4L),
        TUBERCULOS(5L),
        ERVAS_TEMPEROS(6L),
        DERIVADOS(7L),
        OVOS_LATICINIOS(8L),
        PLANTAS_MUDAS(9L);

        long categoryId;

        Values(long categoryId) {
            this.categoryId = categoryId;
        }

        public long getCategoryId() {return categoryId;}
    }

}

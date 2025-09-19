package br.com.cultive_api.entites;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_harvests")
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "harvest_id")
    private Long harvestId;

    private LocalDate harvestDate;
    private Double quantHarvestedKg;
    private Double amountLodtKg;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getHarvestId() {return harvestId;}
    public void setHarvestId(Long harvestId) {this.harvestId = harvestId;}

    public LocalDate getHarvestDate() {return harvestDate;}
    public void setHarvestDate(LocalDate harvestDate) {this.harvestDate = harvestDate;}

    public Double getQuantHarvestedKg() {return quantHarvestedKg;}
    public void setQuantHarvestedKg(Double quantHarvestedKg) {this.quantHarvestedKg = quantHarvestedKg;}

    public Double getAmountLodtKg() {return amountLodtKg;}
    public void setAmountLodtKg(Double amountLodtKg) {this.amountLodtKg = amountLodtKg;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

}

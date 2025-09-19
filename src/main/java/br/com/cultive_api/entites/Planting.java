package br.com.cultive_api.entites;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "tb_plantings")
public class Planting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planting_id")
    private Long plantingId;

    private String culture;
    private LocalTime datePlanting;
    private double areaHectares;
    private boolean harvested;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getPlantingId() {return plantingId;}
    public void setPlantingId(Long plantingId) {this.plantingId = plantingId;}

    public String getCulture() {return culture;}
    public void setCulture(String culture) {this.culture = culture;}

    public LocalTime getDatePlanting() {return datePlanting;}
    public void setDatePlanting(LocalTime datePlanting) {this.datePlanting = datePlanting;}

    public double getAreaHectares() {return areaHectares;}
    public void setAreaHectares(double areaHectares) {this.areaHectares = areaHectares;}

    public boolean isHarvested() {return harvested;}
    public void setHarvested(boolean harvested) {this.harvested = harvested;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
}

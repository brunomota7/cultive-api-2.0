package br.com.cultive_api.entites;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    private String street;
    private String number;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Long getAddressId() {return addressId;}
    public void setAddressId(Long addressId) {this.addressId = addressId;}

    public String getStreet() {return street;}
    public void setStreet(String street) {this.street = street;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getZipCode() {return zipCode;}
    public void setZipCode(String zipCode) {this.zipCode = zipCode;}

    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
}

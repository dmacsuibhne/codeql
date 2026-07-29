package com.example.vulnapp.jpa;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/** JPA entity used by the JPQL-injection endpoint (CWE-089 SqlTaintedPersistence). */
@Entity
@Table(name = "Product")
public class Product {
    @Id
    public String item;
    public String category;
    public int price;
}

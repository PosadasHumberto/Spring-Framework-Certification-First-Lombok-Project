package org.hposadas.projectlombok.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Builder        //to create instances easy
@Getter
@Setter
@NoArgsConstructor
@Entity
public class BeerOrder {

    /**
     * Atributos
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Version
    private Long version;

    private String customerRef;

    /**
     * Constructor
     */
    public BeerOrder(UUID id,
                     Timestamp createdDate,
                     Timestamp lastModifiedDate,
                     Long version, String customerRef,
                     Customer customer,
                     Set<BeerOrderLine> beerOrderLines,
                     BeerOrderShipment beerOrderShipment) {
        this.id = id;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.version = version;
        this.customerRef = customerRef;
        this.setCustomer(customer);     //Asignando el customer al BeerOrder
        this.beerOrderLines = beerOrderLines;
        this.setBeerOrderShipment(beerOrderShipment);
    }

    /**
     * Relaciones
     */

    //BeerOrder y Customer N-1
    @ManyToOne
    private Customer customer;

    //BeerOrder y BeerOrderLine 1-N
    @OneToMany(mappedBy = "beerOrder")
    private Set<BeerOrderLine> beerOrderLines;

    //BeerOrder y BeerOrderShipment 1-1
    @OneToOne(cascade = CascadeType.PERSIST)
    private BeerOrderShipment beerOrderShipment;

    /**
     * Sobreescribiendo setters
     */

    /**
     * Sobreescribiendo setter de Customer para crear la relacion inversa
     * entre BeerOrder y Customer en el momento en el que a BeerOrder se le
     * asigne un Customer al atributo Set<BeerOrders> beerOrders se le agregara
     * la instancia actual de BeerOrder.
     * Para lograr esto, el atributo Set<BeerOrders> beerOrders debe estar
     * inicializado previamente en la clase Customer.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders().add(this);
    }

    //sobreescribiendo el setter de beerOrderShipment
    public void setBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
        this.beerOrderShipment = beerOrderShipment;
        beerOrderShipment.setBeerOrder(this);
    }

    /**
     * Métodos
     */
    public boolean isNew() {    //para saber si esta orden es nueva o ya existia.
        return this.id == null;
    }

}

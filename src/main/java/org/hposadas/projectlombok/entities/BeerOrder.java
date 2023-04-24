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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BeerOrder {

    //atributos
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
     * Relaciones
     */

    //BeerOrder y Customer N-1
    @ManyToOne
    private Customer customer;

    //BeerOrder y BeerOrderLine 1-N
    @OneToMany(mappedBy = "beerOrder")
    private Set<BeerOrderLine> beerOrderLines;

    //métodos
    public boolean isNew() {    //para saber si esta orden es nueva o ya existia.
        return this.id == null;
    }

}

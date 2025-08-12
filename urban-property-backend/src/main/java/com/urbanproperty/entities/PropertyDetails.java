package com.urbanproperty.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "property_details")
@NoArgsConstructor
@Getter
@Setter
public class PropertyDetails extends BaseEntity {

    @Column(name = "num_bedrooms")
    private Integer numBedrooms;

    @Column(name = "num_bathrooms")
    private Integer numBathrooms;

    @Column(name = "size_sqft")
    private Integer sizeSqft;

    @Column(name = "build_year")
    private Integer buildYear;

    @Column(columnDefinition = "json")
    private String extension;

    // This creates the foreign key link back to the Property
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // This shares the primary key with the Property entity
    @JoinColumn(name = "property_id")
    private Property property;
}
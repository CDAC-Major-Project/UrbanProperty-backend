package com.urbanproperty.entities;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "amenities", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "properties_types_id" }))

public class Amenity extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    private String iconUrl;

    // This defines the many-to-many relationship with Property
    // 'mappedBy' indicates that the 'properties' field in the Property entity
    // is the owner of this relationship (it defines the @JoinTable)

    // This is the inverse side of the ManyToMany relationship, mapped by
    // "amenities" in Property
//    @ManyToMany(mappedBy = "amenities", cascade = CascadeType.ALL)
//    private Set<Property> properties = new HashSet<>();

}
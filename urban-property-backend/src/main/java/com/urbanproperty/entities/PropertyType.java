package com.urbanproperty.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "property_types")

public class PropertyType extends BaseEntity {


    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @OneToMany(
    	    mappedBy = "propertyType", 
    	    cascade = CascadeType.ALL, 
    	    orphanRemoval = true,
    	    fetch = FetchType.LAZY
    	)
    	private Set<Property> properties = new HashSet<>();

}

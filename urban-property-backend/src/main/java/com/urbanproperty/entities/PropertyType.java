package com.urbanproperty.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

//	public PropertyType(Long id, String name, String description) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.description = description;
//	}

//	@OneToMany(mappedBy = "property_types", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Properties> properties = new ArrayList<>();
//	
//		//add helper method to add properties
//	public void addProperties(Properties property) {
//	  this.Properties.add(property);
//		// add a link property_types -> properties
//		property.setMyRestaurant(this);
//		}
//
//		// add helper method to remove properties 
//	public void removeProperties(Properties property) {
//		this.Properties.remove(property);
//		// remove a linkproperty_types -> properties
//		property.setMyRestaurant(null);
//	}

    
    
}

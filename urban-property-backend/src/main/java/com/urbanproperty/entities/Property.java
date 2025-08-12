package com.urbanproperty.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "properties")
@NoArgsConstructor
@Getter
@Setter
public class Property extends BaseEntity {

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 100, nullable = false)
    private String state;

    @Column(name = "zip_code", length = 20, nullable = false)
    private String zipCode;

    @Column(name = "starting_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal startingPrice;

    @Column(name = "current_price", precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "auction_start_time")
    private LocalDateTime auctionStartTime;

    @Column(name = "auction_end_time")
    private LocalDateTime auctionEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyStatus status = PropertyStatus.PENDING;

    // --- Relationships ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_type_id", nullable = false)
    private PropertyType propertyType;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PropertyImage> images = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "property_amenities",
        joinColumns = @JoinColumn(name = "property_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    // STEP 5: Add relationship for User Favorites
    @ManyToMany(mappedBy = "favoriteProperties")
    private Set<UserEntity> favoritedByUsers = new HashSet<>();

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PropertyDetails details;
    // --- Helper methods for images ---
    public void addImage(PropertyImage image) {
        images.add(image);
        image.setProperty(this);
    }

    public void removeImage(PropertyImage image) {
        images.remove(image);
        image.setProperty(null);
    }
    //--- Helper methods for property details---
    public void setDetails(PropertyDetails details) {
        if (details != null) {
            this.details = details;
            details.setProperty(this);
        }
    }
}




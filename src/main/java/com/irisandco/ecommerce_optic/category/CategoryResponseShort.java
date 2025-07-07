package com.irisandco.ecommerce_optic.category;

public record CategoryResponseShort(
        Long id,
        String name
) {

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CategoryResponseShort)) { // Replace YourClass with your class name
            return false;
        }
        CategoryResponseShort other = (CategoryResponseShort) o;
        return  this.id.equals(other.id) && // Replace with your attributes
                this.name == other.name && // Replace with your attributes
                // Add comparisons for all other attributes
                true;
    }
}

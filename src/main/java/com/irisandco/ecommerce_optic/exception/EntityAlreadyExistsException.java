package com.irisandco.ecommerce_optic.exception;

public class EntityAlreadyExistsException extends RuntimeException{
    public EntityAlreadyExistsException(String entityClass, String attributeName, String attributeValue) {
        super(String.format("%s with %s %s already exists", entityClass, attributeName, attributeValue));
    }
}

package org.acme.model;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "Todos.findAll", query = "SELECT f FROM Fruit f ORDER BY f.name", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    User owner;
    String name;
    String description;

    public Todo() {
    }

    public Todo(User owner){
        this.owner = owner;
    }
    
    public Todo(String name, User owner){
        this.owner = owner;
        this.name = name;
    }

    public Todo(String name, User owner,String description){
        this.owner = owner;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

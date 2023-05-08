package org.acme.service;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

public class TodoServiceImp implements TodoService {

    @Inject
    EntityManager entityManager;
}

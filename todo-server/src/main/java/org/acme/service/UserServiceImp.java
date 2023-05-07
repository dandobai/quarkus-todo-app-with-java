package org.acme.service;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

public class UserServiceImp implements UserService {

    @Inject
    EntityManager entityManager;
}

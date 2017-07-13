package com.devopsbuddy.backend.persistence.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jayden on 7/13/2017.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}

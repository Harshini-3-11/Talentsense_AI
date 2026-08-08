package com.talentsense.organization.repository;

import com.talentsense.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

    Optional<Organization> findByNameIgnoreCase(String name);
}

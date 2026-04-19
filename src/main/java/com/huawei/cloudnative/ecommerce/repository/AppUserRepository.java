package com.huawei.cloudnative.ecommerce.repository;

import com.huawei.cloudnative.ecommerce.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}

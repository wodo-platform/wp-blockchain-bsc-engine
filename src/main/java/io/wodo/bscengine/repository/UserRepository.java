package io.wodo.bscengine.repository;

import io.wodo.bscengine.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

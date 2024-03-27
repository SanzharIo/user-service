package kz.flurent.repository;


import kz.flurent.model.entity.OtpRequest;
import kz.flurent.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepository extends JpaRepository<OtpRequest, UUID> {

    Optional<OtpRequest> findByDestinationAndCodeAndUserAndValidIsTrue(String destination, String code, User user);

}

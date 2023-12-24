package com.abbas.securityservice.repository;

import com.abbas.securityservice.dao.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory,Integer> {

    @Query("""
            select uh from UserHistory uh inner join User u on uh.user.id = u.id
            where u.email = :userEmail and uh.revoked = false
            """)
    List<UserHistory> findAllValidTokensByUser(String userEmail);

    Optional<UserHistory> findByTokenId(String tokenId);

}

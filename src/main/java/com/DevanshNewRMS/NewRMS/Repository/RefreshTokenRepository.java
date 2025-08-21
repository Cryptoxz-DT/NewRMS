package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.Model.RefreshToken;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    List<RefreshToken> findByStaffAndRevokedFalse(Staff staff);
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.staff = :staff")
    void revokeAllByStaff(@Param("staff") Staff staff);
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.expiresAt < :now")
    void revokeExpiredTokens(@Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :cutoff")
    void deleteExpiredTokens(@Param("cutoff") LocalDateTime cutoff);
    
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.staff = :staff AND rt.revoked = false")
    long countActiveTokensByStaff(@Param("staff") Staff staff);
}
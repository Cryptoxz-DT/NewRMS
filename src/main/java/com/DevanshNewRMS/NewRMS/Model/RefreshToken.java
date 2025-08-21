package com.DevanshNewRMS.NewRMS.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens", indexes = {
    @Index(name = "idx_refresh_token", columnList = "token"),
    @Index(name = "idx_refresh_token_staff", columnList = "staff_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 500)
    private String token;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "revoked", nullable = false)
    @Builder.Default
    private Boolean revoked = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;
    
    @Column(name = "client_ip")
    private String clientIp;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
    
    public boolean isValid() {
        return !this.revoked && !isExpired();
    }
}
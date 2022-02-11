package com.ing.brokage.persistance.entity;

import com.ing.brokage.persistance.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Audited
@MappedSuperclass
public class BaseEntity {

    private static final String SYSTEM = "SYSTEM";

    @Id
    @Column(nullable = false, name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    private Long id;

    @Column(nullable = false, name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, name = "CREATED_BY")
    private String createdBy;

    @Column(nullable = false, name = "UPDATED_BY")
    private String updatedBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof BaseEntity) {
            if (((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail() != null) {
                createdBy = ((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
                updatedBy = ((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
            } else {
                createdBy = ((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                updatedBy = ((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            }
        } else {
            createdBy = SYSTEM;
            updatedBy = SYSTEM;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof BaseEntity) {
            if (((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail() != null) {
                updatedBy = ((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
            } else {
                updatedBy = ((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            }
        } else {
            updatedBy = SYSTEM;
        }
    }

}

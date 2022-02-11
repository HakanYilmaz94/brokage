package com.ing.brokage.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Audited
@AuditTable(value = "AU_ASSET")
@SequenceGenerator(name = "idgen", sequenceName = "ASSET_SEQ", allocationSize = 1)
public class Asset extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;

    @Column(nullable = false, length = 50)
    private String assetName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private Long usableSize;
}

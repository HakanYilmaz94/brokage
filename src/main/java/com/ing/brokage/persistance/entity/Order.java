package com.ing.brokage.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ing.brokage.persistance.enums.OrderStatus;
import com.ing.brokage.persistance.enums.SideEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDER_")
@Audited
@AuditTable(value = "AU_ORDER")
@SequenceGenerator(name = "idgen", sequenceName = "ORDER_SEQ", allocationSize = 1)
public class Order extends BaseEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(nullable = false)
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ASSET_ID", nullable = false)
	private Asset asset;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private SideEnum side;

	@Column(precision = 19, scale = 4, nullable = false)
	private BigDecimal price;

	@Column(nullable = false)
	private Long size;

	@Column(nullable = false, length = 50)
	private String assetName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private OrderStatus orderStatus;
}

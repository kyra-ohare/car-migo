package com.unosquare.carmigo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

/**
 * Data Transfer Object representing the <b>driver</b> table of the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RedisHash
@Table(name = "driver")
public class Driver implements Serializable {

  @Serial
  private static final long serialVersionUID = 7388394799795788640L;

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(name = "license_number")
  private String licenseNumber;

  @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "platform_user_id", nullable = false)
  private PlatformUser platformUser;
}

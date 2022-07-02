package com.unosquare.carmigo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "driver")
public class Driver {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(name = "license_number")
  private String licenseNumber;

  @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "platform_user_id", nullable = false)
  private PlatformUser platformUser;
}

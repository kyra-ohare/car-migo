package com.unosquare.carmigo.entity;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location")
public class Location implements Serializable {

  @Serial
  private static final long serialVersionUID = 3523461982735207852L;

  @Id
  @SequenceGenerator(name = "location_id_seq", sequenceName = "location_id_seq", allocationSize = 1)
  @GeneratedValue(generator = "location_id_seq", strategy = GenerationType.SEQUENCE)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(name = "description", nullable = false)
  private String description;
}

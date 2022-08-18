package com.unosquare.carmigo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "passenger_journey")
public class PassengerJourney {

  @Id
  @SequenceGenerator(name = "passenger_journey_id_seq", sequenceName = "passenger_journey_id_seq", allocationSize = 1)
  @GeneratedValue(generator = "passenger_journey_id_seq", strategy = GenerationType.SEQUENCE)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "passenger_id", nullable = false)
  private Passenger passenger;

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "journey_id", nullable = false)
  private Journey journey;
}

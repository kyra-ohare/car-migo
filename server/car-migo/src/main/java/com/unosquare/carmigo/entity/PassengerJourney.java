package com.unosquare.carmigo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

/**
 * Data Transfer Object representing the <b>passenger_journey</b> table of the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RedisHash
@Table(name = "passenger_journey")
public class PassengerJourney implements Serializable {

  @Serial
  private static final long serialVersionUID = -3648405935579459368L;

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

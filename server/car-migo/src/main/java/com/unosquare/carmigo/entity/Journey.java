package com.unosquare.carmigo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

/**
 * Data Transfer Object representing the <b>journey</b> table of the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RedisHash
@Table(name = "journey")
public class Journey implements Serializable {

  @Serial
  private static final long serialVersionUID = -7773299486390690039L;

  @Id
  @SequenceGenerator(name = "journey_id_seq", sequenceName = "journey_id_seq", allocationSize = 1)
  @GeneratedValue(generator = "journey_id_seq", strategy = GenerationType.SEQUENCE)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(name = "created_date", nullable = false)
  private Instant createdDate;

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id_from", nullable = false)
  private Location locationFrom;

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id_to", nullable = false)
  private Location locationTo;

  @Column(name = "max_passengers", nullable = false, unique = true)
  private int maxPassengers;

  @Column(name = "date_time", nullable = false)
  private Instant dateTime;

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "driver_id", nullable = false)
  private Driver driver;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  @JoinTable(name = "passenger_journey", joinColumns = @JoinColumn(name = "journey_id"),
      inverseJoinColumns = @JoinColumn(name = "passenger_id"))
  private List<Passenger> passengers;
}

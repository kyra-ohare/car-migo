package com.unosquare.carmigo.entity;

import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "journey")
public class Journey {

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

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
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.InstantAsTimestampJdbcType;
import org.springframework.data.redis.core.RedisHash;

/**
 * Data Transfer Object representing the <b>platform_user</b> table of the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RedisHash
@Table(name = "platform_user")
public class PlatformUser implements Serializable {

  @Serial
  private static final long serialVersionUID = 467893117206132307L;

  @Id
  @SequenceGenerator(name = "platform_user_id_seq", sequenceName = "platform_user_id_seq", allocationSize = 1)
  @GeneratedValue(generator = "platform_user_id_seq", strategy = GenerationType.SEQUENCE)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(name = "created_date", nullable = false)
  @JdbcType(InstantAsTimestampJdbcType.class)
  private Instant createdDate;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "dob", nullable = false)
  @JdbcType(InstantAsTimestampJdbcType.class)
  private Instant dob;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_access_status_id", nullable = false)
  private UserAccessStatus userAccessStatus;
}

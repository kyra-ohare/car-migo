package com.unosquare.carmigo.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
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
  private Instant createdDate;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "dob", nullable = false)
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

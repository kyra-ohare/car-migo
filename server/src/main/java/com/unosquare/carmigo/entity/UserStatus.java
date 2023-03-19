package com.unosquare.carmigo.entity;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "user_access_status")
public class UserStatus implements Serializable {

  @Serial
  private static final long serialVersionUID = -7849734962728357293L;

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(name = "status")
  private String status;
}

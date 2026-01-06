package com.antonio.MediHouse.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "IdUser")
  private Long idUser;

  // LLaves foráneas
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference("idUser-usage")
  private List<UsageHistory> usageHistoryList;

  @Column(name = "Name")
  private String name;
  @Column(name = "Lastname")
  private String lastname;
  @Column(name = "Email")
  private String email;
  @Column(name = "Password")
  private String password;
  @Column(name = "Age")
  private int age;

}

package com.example.server.model;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.example.server.validator.Unique;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "username", unique = true)
  @NotBlank(message = "Name must not be empty")
  @Size(min = 3, max = 10, message = "Name must between 3 and 10")
  @Unique(message = "Username already taken")
  private String username;

  @Column(name = "password" ,nullable = false )
  @NotBlank(message = "Password must not be empty")
  @Size(min = 6, max = 100, message = "Password must be more than 6")
  private String password;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date created_at;

  @Column(name = "image")
  private String image;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Post> posts;

  @OneToMany(mappedBy = "from", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Follower> uFollow;

  @OneToMany(mappedBy = "to",fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Follower> followU;

  @Transient
  private Integer followers; 

  @Transient 
  private Integer following;

  @Transient
  private boolean isFollow;

  @Transient
  public void setIsFollow(boolean follow){
    this.isFollow = follow;
  }

  @Transient
  public void setSizeOfFollowing(){
    this.following  = this.uFollow.size();
  }

  @Transient
  public void setSizeOfFollowers(){
    this.followers= this.followU.size();
  }
}

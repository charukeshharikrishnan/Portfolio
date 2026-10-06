package com.charukesh.portfolio;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="contact_messages")
public class ContactMessage {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=80) private String name;
  @Column(nullable=false,length=120) private String email;
  @Column(nullable=false,length=120) private String subject;
  @Column(nullable=false,length=2000) private String message;
  @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt = Instant.now();
  protected ContactMessage(){}
  public ContactMessage(String n,String e,String s,String m){name=n;email=e;subject=s;message=m;}
}

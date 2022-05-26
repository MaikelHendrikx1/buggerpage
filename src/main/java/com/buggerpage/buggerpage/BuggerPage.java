package com.buggerpage.buggerpage;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BuggerPage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(length = 50)
    public String name;

    private byte[] publicKey;

    @Column(length = 5000)
    public String description;

    public Integer ownerId;

    @ElementCollection
    public Set<Integer> maintainers;

    public BuggerPage() {
    }
    
    public BuggerPage(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String generateKey() {
        this.publicKey = generateSalt16Byte();
        return Base64.getEncoder().encodeToString(publicKey);
    }

    public Boolean KeyCorrect(String keyString){
        return (Base64.getEncoder().encodeToString(publicKey).equals(keyString));
    }

    @Override
    public String toString(){
        return id + " | " + name + " | " + description;
    } 

    private byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }
}

package com.koanz.test.springboot.app.models;

import jakarta.persistence.*;

@Entity
@Table(name = "banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "total_transfer")
    private int totalTransfer;

    public Bank() {
    }

    public Bank(Long id, String name, int totalTransfer) {
        this.id = id;
        this.name = name;
        this.totalTransfer = totalTransfer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(int totalTransfer) {
        this.totalTransfer = totalTransfer;
    }
}

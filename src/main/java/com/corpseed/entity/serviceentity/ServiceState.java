package com.corpseed.entity.serviceentity;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "service_state")
public class ServiceState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "state_name")
    private String stateName;

    @OneToOne(targetEntity = Services.class)
    @JoinColumn(name = "service_id",nullable = false)
    private Services services;

    @OneToOne(mappedBy = "serviceState",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private StateCityService stateCityService;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    public ServiceState(Long id, String stateName, Services services,
                        StateCityService stateCityService, Date createdAt, Date updatedAt) {
        this.id = id;
        this.stateName = stateName;
        this.services = services;
        this.stateCityService = stateCityService;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ServiceState() {}

    public Long getId() {
        return id;
    }

    public String getStateName() {
        return stateName;
    }

    public Services getServices() {
        return services;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StateCityService getStateCityService() {
        return stateCityService;
    }

    public void setStateCityService(StateCityService stateCityService) {
        this.stateCityService = stateCityService;
    }
}

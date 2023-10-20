package com.corpseed.entity.serviceentity;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "state_city_service")
public class StateCityService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = ServiceState.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "service_state_id")
    private ServiceState serviceState;

    @OneToOne(targetEntity = ServiceCity.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "service_city_id")
    private ServiceCity serviceCity;

    @ManyToOne(targetEntity = Services.class)
    @JoinColumn(name = "service_id")
    private Services services;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    public StateCityService(Long id, ServiceState serviceState, ServiceCity serviceCity, Services services, Date createdAt, Date updatedAt) {
        this.id = id;
        this.serviceState = serviceState;
        this.serviceCity = serviceCity;
        this.services = services;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public StateCityService() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceState getServiceState() {
        return serviceState;
    }

    public void setServiceState(ServiceState serviceState) {
        this.serviceState = serviceState;
    }

    public ServiceCity getServiceCity() {
        return serviceCity;
    }

    public void setServiceCity(ServiceCity serviceCity) {
        this.serviceCity = serviceCity;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}

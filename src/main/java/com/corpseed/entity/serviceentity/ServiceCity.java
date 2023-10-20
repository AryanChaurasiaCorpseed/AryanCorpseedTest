package com.corpseed.entity.serviceentity;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "service_city")
public class ServiceCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "city_name")
    private String cityName;

    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "service_state_name")
    private String serviceStateName;

    @OneToOne(targetEntity = Services.class)
    @JoinColumn(name = "service_id",nullable = false)
    private Services services;

    @Column(name = "state_service_id")
    private Long stateServiceId;

    @OneToOne(mappedBy = "serviceCity",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private StateCityService stateCityService;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    public ServiceCity(Long id, String cityName, String serviceStateName, Services services,
                       StateCityService stateCityService, Date createdAt, Date updatedAt,Long stateServiceId) {
        this.id = id;
        this.cityName = cityName;
        this.serviceStateName = serviceStateName;
        this.services = services;
        this.stateCityService = stateCityService;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stateServiceId=stateServiceId;
    }

    public ServiceCity() {
    }

    public Long getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
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

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getServiceStateName() {
        return serviceStateName;
    }

    public void setServiceStateName(String serviceStateName) {
        this.serviceStateName = serviceStateName;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public StateCityService getStateCityService() {
        return stateCityService;
    }

    public void setStateCityService(StateCityService stateCityService) {
        this.stateCityService = stateCityService;
    }

    public Long getStateServiceId() {
        return stateServiceId;
    }

    public void setStateServiceId(Long stateServiceId) {
        this.stateServiceId = stateServiceId;
    }
}

package com.pasteleria.cotizaciones.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.common.persistence.AuditableEntity;
import com.pasteleria.cotizaciones.domain.model.QuotationOrigin;
import com.pasteleria.cotizaciones.domain.model.QuotationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cotizacion")
public class QuotationEntity extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cotizacion_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cliente_id", nullable = false)
  private ClientEntity client;

  @Column(name = "codigo", nullable = false, unique = true, length = 50)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_cotizacion", nullable = false, length = 40)
  private QuotationStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "origen", nullable = false, length = 20)
  private QuotationOrigin origin;

  @Column(name = "observaciones")
  private String notes;

  @Column(name = "total_estimado", precision = 10, scale = 2)
  private BigDecimal estimatedTotal;

  @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<QuotationDetailEntity> details = new LinkedHashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ClientEntity getClient() {
    return client;
  }

  public void setClient(ClientEntity client) {
    this.client = client;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public QuotationStatus getStatus() {
    return status;
  }

  public void setStatus(QuotationStatus status) {
    this.status = status;
  }

  public QuotationOrigin getOrigin() {
    return origin;
  }

  public void setOrigin(QuotationOrigin origin) {
    this.origin = origin;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public BigDecimal getEstimatedTotal() {
    return estimatedTotal;
  }

  public void setEstimatedTotal(BigDecimal estimatedTotal) {
    this.estimatedTotal = estimatedTotal;
  }

  public Set<QuotationDetailEntity> getDetails() {
    return details;
  }

  public void setDetails(Set<QuotationDetailEntity> details) {
    this.details = details;
  }
}




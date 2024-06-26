package com.coral.backend.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table(name = "investments")
public class Investment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long investmentId;

  @ManyToOne
  @JoinColumn(name = "investor_id")
  private InvestorUser investor;

  @ManyToOne
  @JoinColumn(name = "enterprise_id")
  private EnterpriseUser enterprise;

  @Column(nullable = true)
  private int amountInvested;

  // Setters

  public void setEnterprise(EnterpriseUser enterprise) {
    this.enterprise = enterprise;
  }

  public void setInvestor(InvestorUser investor) {
    this.investor = investor;
  }

  public void setAmountInvested(int amountInvested) {
    this.amountInvested = amountInvested;
  }

  // Getters

  public EnterpriseUser getEnterprise() {
    return enterprise;
  }

  public InvestorUser getInvestor() {
    return investor;
  }

  public int getAmountInvested() {
    return amountInvested;
  }

  public long getInvestmentId() {
    return investmentId;
  }
}

package com.coral.backend.entities;

import com.coral.backend.dtos.EnterpriseDTO;
import com.coral.backend.dtos.InvestorDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class InvestorUser extends User {

    private int investorType;
    private String investmentCriteria;
    private int rangeMin;
    private int rangeMax;
    @Column(insertable = false, updatable = false)
    private String userType = "investor";
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name = "investments",
        joinColumns = @JoinColumn(name = "investor_id"),
        inverseJoinColumns = @JoinColumn(name = "enterprise_id")
    )
    private List<EnterpriseUser> enterprises;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name = "chat_rooms",
        joinColumns = @JoinColumn(name = "investor_id"),
        inverseJoinColumns = @JoinColumn(name = "enterprise_id")
    )
    private List<EnterpriseUser> chatsWithEnterprises;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "followings",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private List<InvestorUser> followers;
    @ManyToMany(mappedBy = "followers")
    private List<InvestorUser> follows;

    //Setters
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setEnterprises(List<EnterpriseUser> enterprises) {
        this.enterprises = enterprises;
    }
    public void setInvestorType(int investor_type) {
        this.investorType = investor_type;
    }

    public void setInvestmentCriteria(String investment_criteria) {
        this.investmentCriteria = investment_criteria;
    }

    public void setRangeMin(int range_min) {
        this.rangeMin = range_min;
    }

    public void setRangeMax(int range_max) {
        this.rangeMax = range_max;
    }

    //Getters

    public String getUserType() {
        return userType;
    }

    public List<EnterpriseUser> getEnterprises() {
        return enterprises;
    }
    public int getInvestorType() {
        return investorType;
    }

    public String getInvestmentCriteria() {
        return investmentCriteria;
    }

    public int getRangeMin() {
        return rangeMin;
    }

    public int getRangeMax() {
        return rangeMax;
    }

    public InvestorDTO toDTO(){
        InvestorDTO investorDTO = new InvestorDTO();
        investorDTO.setInvestorType(getInvestorType());
        investorDTO.setUserId(getUserId());
        List<String> areaNames = new ArrayList<>();
        for (Area area : getAreas()){
            areaNames.add(area.getName());
        }
        investorDTO.setAreas(areaNames);
        investorDTO.setDescription(getDescription());
        investorDTO.setInvestmentCriteria(getInvestmentCriteria());
        investorDTO.setLocation(getLocation());
        investorDTO.setName(getName());
        investorDTO.setProfilePicture(getProfileImageString());
        investorDTO.setRangeMax(getRangeMax());
        investorDTO.setRangeMin(getRangeMin());
        investorDTO.setFirstLogin(getFirstLogin());
        investorDTO.setUserType(getUserType());
        List<EnterpriseDTO> enterprisesDTO = new ArrayList<>();
        for (EnterpriseUser enterprise : getEnterprises()){
            enterprisesDTO.add(enterprise.toDTOWithoutInvestors());
        }
        investorDTO.setEnterprises(enterprisesDTO);
        return investorDTO;
    }

    public InvestorDTO toDTOWithoutEnterprises(){
        InvestorDTO investorDTO = new InvestorDTO();
        investorDTO.setInvestorType(getInvestorType());
        investorDTO.setUserId(getUserId());
        List<String> areaNames = new ArrayList<>();
        for (Area area : getAreas()){
            areaNames.add(area.getName());
        }
        investorDTO.setAreas(areaNames);
        investorDTO.setDescription(getDescription());
        investorDTO.setInvestmentCriteria(getInvestmentCriteria());
        investorDTO.setLocation(getLocation());
        investorDTO.setName(getName());
        investorDTO.setProfilePicture(getProfileImageString());
        investorDTO.setRangeMax(getRangeMax());
        investorDTO.setRangeMin(getRangeMin());
        investorDTO.setFirstLogin(getFirstLogin());
        investorDTO.setUserType(getUserType());

        return investorDTO;
    }
}

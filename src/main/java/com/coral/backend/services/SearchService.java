package com.coral.backend.services;

import com.coral.backend.dtos.EnterpriseDTO;
import com.coral.backend.dtos.InvestorDTO;
import com.coral.backend.dtos.SearchDTO;
import com.coral.backend.entities.Area;
import com.coral.backend.entities.EnterpriseUser;
import com.coral.backend.entities.InvestorUser;
import com.coral.backend.entities.User;
import com.coral.backend.repositories.AreaRepository;
import com.coral.backend.repositories.EnterpriseUserRepository;
import com.coral.backend.repositories.InvestorUserRepository;
import com.coral.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    @Autowired
    private InvestorUserRepository investorUserRepository;

    @Autowired
    private EnterpriseUserRepository enterpriseUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AreaRepository areaRepository;

    public ResponseEntity<Object> searchInvestors(SearchDTO searchDTO) {
        List<Area> areas = new ArrayList<>();
        Set<User> matchingInvestors = new HashSet<>();

        //Transform List<String> to List<Area>
        if (searchDTO.getAreas() != null){
            for (String area : searchDTO.getAreas()) {
                Optional<Area> areaOptional = areaRepository.findAreaByName(area);
                if (areaOptional.isEmpty()) {
                    return new ResponseEntity<>("Area not found", HttpStatus.NOT_FOUND);
                }
                areas.add(areaOptional.get());
            }
        }

        if (searchDTO.getAreas().isEmpty() && searchDTO.getLocations().isEmpty() && searchDTO.getInvestorType() >= 0){
            matchingInvestors.addAll(investorUserRepository.findAllByInvestorType(searchDTO.getInvestorType()));
        }
        else if(searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && searchDTO.getInvestorType() >= 0){
            for (String location : searchDTO.getLocations()){
                matchingInvestors.addAll(investorUserRepository.findAllByInvestorTypeAndLocation(searchDTO.getInvestorType(), location));
            }
        } else if (!searchDTO.getAreas().isEmpty() && searchDTO.getLocations().isEmpty() && searchDTO.getInvestorType() >= 0){
            for (Area area : areas){
                matchingInvestors.addAll(investorUserRepository.findAllByInvestorTypeAndAreas(searchDTO.getInvestorType(), area));
            }
        } else if(!searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && searchDTO.getInvestorType() >= 0){
            for (Area area : areas){
                for (String location : searchDTO.getLocations()){
                    matchingInvestors.addAll(investorUserRepository.findAllByInvestorTypeAndAreasAndLocation(searchDTO.getInvestorType(), area, location));
                }
            }
        } else if(searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && searchDTO.getInvestorType() < 0){
            for (String location : searchDTO.getLocations()){
                matchingInvestors.addAll(investorUserRepository.findAllByLocation(location));
            }
        } else if (!searchDTO.getAreas().isEmpty() && searchDTO.getLocations().isEmpty() && searchDTO.getInvestorType() < 0){
            for (Area area : areas){
                matchingInvestors.addAll(investorUserRepository.findAllByAreas(area));
            }
        } else if(!searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && searchDTO.getInvestorType() < 0){
            for (Area area : areas){
                for (String location : searchDTO.getLocations()){
                    matchingInvestors.addAll(investorUserRepository.findAllByAreasAndLocation(area, location));
                }
            }
        }

        if(!Objects.equals(searchDTO.getUserName(), "") && !matchingInvestors.isEmpty()){
            //Copy matchingInvestors to a new list to avoid ConcurrentModificationException
            List<User> matchingInvestorsCopy = new ArrayList<>(matchingInvestors);
            for (User user: matchingInvestorsCopy){
                boolean containsSubString = user.getName().toLowerCase().contains(searchDTO.getUserName().toLowerCase());
                if (!containsSubString){
                    matchingInvestors.remove(user);
                }
            }
        }
        else if(!Objects.equals(searchDTO.getUserName(), "") && matchingInvestors.isEmpty()){
            matchingInvestors.addAll(investorUserRepository.findAll());
            //Copy matchingInvestors to a new list to avoid ConcurrentModificationException
            List<User> matchingInvestorsCopy = new ArrayList<>(matchingInvestors);
            for (User user: matchingInvestorsCopy){
                if(!user.getFirstLogin()) {
                    boolean containsSubString = user.getName().toLowerCase().contains(searchDTO.getUserName().toLowerCase());
                    if (!containsSubString) {
                        matchingInvestors.remove(user);
                    }
                }
            }
        }

        List<InvestorDTO> FrontDataPackage = new ArrayList<>();
        if (matchingInvestors.isEmpty()){
            return new ResponseEntity<>("No matching investors found", HttpStatus.NOT_FOUND);
        }
        for (User user: matchingInvestors){
            if(!user.getFirstLogin()) {
                InvestorUser investor = (InvestorUser) user;
                FrontDataPackage.add(investor.toDTO());
            }
        }
        return new ResponseEntity<>(FrontDataPackage, HttpStatus.OK);
    }

    public ResponseEntity<Object> searchEnterprises(SearchDTO searchDTO) {
        List<Area> areas = new ArrayList<>();
        Set<User> matchingEnterprises = new HashSet<>();

        //Transform List<String> to List<Area>
        if (searchDTO.getAreas() != null){
            for (String area : searchDTO.getAreas()) {
                Optional<Area> areaOptional = areaRepository.findAreaByName(area);
                if (areaOptional.isEmpty()) {
                    return new ResponseEntity<>("Area not found", HttpStatus.NOT_FOUND);
                }
                areas.add(areaOptional.get());
            }
        }

        if (searchDTO.getAreas().isEmpty() && searchDTO.getLocations().isEmpty() && !Objects.equals(searchDTO.getEnterpriseType(), "")){
            matchingEnterprises.addAll(enterpriseUserRepository.findAllByEnterpriseType(searchDTO.getEnterpriseType()));
        }
        else if(searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && !Objects.equals(searchDTO.getEnterpriseType(), "")){
            for (String location : searchDTO.getLocations()){
                matchingEnterprises.addAll(enterpriseUserRepository.findAllByEnterpriseTypeAndLocation(searchDTO.getEnterpriseType(), location));
            }
        } else if (!searchDTO.getAreas().isEmpty() && searchDTO.getLocations().isEmpty() && !Objects.equals(searchDTO.getEnterpriseType(), "")){
            for (Area area : areas){
                matchingEnterprises.addAll(enterpriseUserRepository.findAllByEnterpriseTypeAndAreas(searchDTO.getEnterpriseType(), area));
            }
        } else if(!searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && !Objects.equals(searchDTO.getEnterpriseType(), "")){
            for (Area area : areas){
                for (String location : searchDTO.getLocations()){
                    matchingEnterprises.addAll(enterpriseUserRepository.findAllByEnterpriseTypeAndAreasAndLocation(searchDTO.getEnterpriseType(), area, location));
                }
            }
        } else if(searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && Objects.equals(searchDTO.getEnterpriseType(), "")){
            for (String location : searchDTO.getLocations()){
                matchingEnterprises.addAll(enterpriseUserRepository.findAllByLocation(location));
            }
        } else if (!searchDTO.getAreas().isEmpty() && searchDTO.getLocations().isEmpty() && Objects.equals(searchDTO.getEnterpriseType(), "")){
            for (Area area : areas){
                matchingEnterprises.addAll(enterpriseUserRepository.findAllByAreas(area));
            }
        } else if(!searchDTO.getAreas().isEmpty() && !searchDTO.getLocations().isEmpty() && Objects.equals(searchDTO.getEnterpriseType(), "")){
            for (Area area : areas){
                for (String location : searchDTO.getLocations()){
                    matchingEnterprises.addAll(enterpriseUserRepository.findAllByAreasAndLocation(area, location));
                }
            }
        }

        if(!Objects.equals(searchDTO.getUserName(), "") && !matchingEnterprises.isEmpty()){
            //Copy matchingInvestors to a new list to avoid ConcurrentModificationException
            List<User> matchingInvestorsCopy = new ArrayList<>(matchingEnterprises);
            for (User user: matchingInvestorsCopy){
                if(!user.getFirstLogin()){
                    boolean containsSubString = user.getName().toLowerCase().contains(searchDTO.getUserName().toLowerCase());
                    if (!containsSubString){
                        matchingEnterprises.remove(user);
                    }
                }
            }
        }
        else if(!Objects.equals(searchDTO.getUserName(), "") && matchingEnterprises.isEmpty()){
            matchingEnterprises.addAll(enterpriseUserRepository.findAll());
            //Copy matchingInvestors to a new list to avoid ConcurrentModificationException
            List<User> matchingEnterprisesCopy = new ArrayList<>(matchingEnterprises);
            for (User user: matchingEnterprisesCopy){
                if(!user.getFirstLogin()) {
                    boolean containsSubString = user.getName().toLowerCase().contains(searchDTO.getUserName().toLowerCase());
                    if (!containsSubString) {
                        matchingEnterprises.remove(user);
                    }
                }
            }
        }

        List<EnterpriseDTO> FrontDataPackage = new ArrayList<>();
        if (matchingEnterprises.isEmpty()){
            return new ResponseEntity<>("No matching enterprises found", HttpStatus.NOT_FOUND);
        }
        for (User user: matchingEnterprises){
            if(!user.getFirstLogin()) {
                EnterpriseUser enterprise = (EnterpriseUser) user;
                FrontDataPackage.add(enterprise.toDTO());
            }
        }
        return new ResponseEntity<>(FrontDataPackage, HttpStatus.OK);
    }
}

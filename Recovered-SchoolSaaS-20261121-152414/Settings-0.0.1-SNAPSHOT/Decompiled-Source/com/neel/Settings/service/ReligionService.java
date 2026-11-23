/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.model.Religion
 *  com.neel.Settings.repo.ReligionRepository
 *  com.neel.Settings.service.ReligionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.Settings.service;

import com.neel.Settings.model.Religion;
import com.neel.Settings.repo.ReligionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReligionService {
    @Autowired
    private ReligionRepository religionRepository;

    public Optional<Religion> findReligionByName(String religionName) {
        return this.religionRepository.findByReligionName(religionName.toLowerCase());
    }

    public Optional<Religion> findReligionByNameAndSchool(String religionName, String schoolId) {
        return this.religionRepository.findByReligionNameAndSchoolId(religionName.toLowerCase(), schoolId);
    }

    public List<Religion> getReligionsBySchoolId(String schoolId) {
        List schoolSpecificReligions = this.religionRepository.findBySchoolId(schoolId);
        List universalReligions = this.religionRepository.findByUniversalTrue();
        ArrayList<Religion> allReligions = new ArrayList<Religion>(schoolSpecificReligions);
        allReligions.addAll(universalReligions);
        return allReligions;
    }

    public boolean religionExistsAnywhere(String religionName) {
        Optional universalReligion = this.religionRepository.findByReligionNameAndUniversalTrue(religionName.toLowerCase());
        if (universalReligion.isPresent()) {
            return true;
        }
        List allReligions = this.religionRepository.findAll();
        return allReligions.stream().anyMatch(religion -> religion.getReligionName().equalsIgnoreCase(religionName));
    }

    public Religion saveReligion(Religion religion) {
        religion.setReligionName(religion.getReligionName().toLowerCase());
        Optional universalReligion = this.religionRepository.findByReligionNameAndUniversalTrue(religion.getReligionName());
        if (universalReligion.isPresent()) {
            throw new IllegalArgumentException("This religion already exists as a universal religion");
        }
        religion.setUniversal(false);
        return (Religion)this.religionRepository.save((Object)religion);
    }

    public Religion saveUniversalReligion(Religion religion) {
        religion.setSchoolId(null);
        religion.setUniversal(true);
        religion.setReligionName(religion.getReligionName().toLowerCase());
        if (this.religionExistsAnywhere(religion.getReligionName())) {
            throw new IllegalArgumentException("Religion with this name already exists");
        }
        return (Religion)this.religionRepository.save((Object)religion);
    }

    public List<Religion> getAllReligions() {
        return this.religionRepository.findAll();
    }

    public List<Religion> getAllUniversalReligions() {
        return this.religionRepository.findByUniversalTrue();
    }

    public List<Religion> createDefaultUniversalReligions() {
        String[] religionNames = new String[]{"christianity", "islam", "hinduism", "buddhism", "judaism", "sikhism", "other"};
        ArrayList<Religion> createdReligions = new ArrayList<Religion>();
        for (String name : religionNames) {
            Optional existingReligion = this.religionRepository.findByReligionNameAndUniversalTrue(name);
            if (!existingReligion.isPresent()) {
                Religion religion = new Religion();
                religion.setReligionName(name);
                religion.setUniversal(true);
                religion.setSchoolId(null);
                createdReligions.add((Religion)this.religionRepository.save((Object)religion));
                continue;
            }
            createdReligions.add((Religion)existingReligion.get());
        }
        return createdReligions;
    }

    public String getDefaultReligionsJsonData() {
        String[] religionNames = new String[]{"christianity", "islam", "hinduism", "buddhism", "judaism", "sikhism", "other"};
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        boolean firstReligion = true;
        for (String name : religionNames) {
            if (!firstReligion) {
                jsonBuilder.append(",\n");
            }
            firstReligion = false;
            jsonBuilder.append("  {\n");
            jsonBuilder.append("    \"religionName\": \"").append(name).append("\",\n");
            jsonBuilder.append("    \"universal\": true,\n");
            jsonBuilder.append("    \"schoolId\": null\n");
            jsonBuilder.append("  }");
        }
        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }

    public Optional<Religion> updateReligion(String schoolId, String id, Religion updatedReligion) {
        updatedReligion.setReligionName(updatedReligion.getReligionName().toLowerCase());
        return this.religionRepository.findById((Object)id).map(existingReligion -> {
            if (existingReligion.isUniversal()) {
                throw new IllegalArgumentException("Universal religions cannot be updated through school-specific endpoint");
            }
            if (!existingReligion.getSchoolId().equals(schoolId)) {
                throw new IllegalArgumentException("Religion does not belong to the specified school");
            }
            existingReligion.setReligionName(updatedReligion.getReligionName());
            existingReligion.setSchoolId(schoolId);
            return (Religion)this.religionRepository.save(existingReligion);
        });
    }

    public Optional<Religion> updateUniversalReligion(String id, Religion updatedReligion) {
        updatedReligion.setReligionName(updatedReligion.getReligionName().toLowerCase());
        return this.religionRepository.findById((Object)id).map(existingReligion -> {
            if (!existingReligion.isUniversal()) {
                throw new IllegalArgumentException("This is not a universal religion");
            }
            Optional duplicateReligion = this.religionRepository.findByReligionNameAndUniversalTrue(updatedReligion.getReligionName());
            if (duplicateReligion.isPresent() && !((Religion)duplicateReligion.get()).getId().equals(id)) {
                throw new IllegalArgumentException("Universal religion with this name already exists");
            }
            existingReligion.setReligionName(updatedReligion.getReligionName());
            existingReligion.setUniversal(true);
            existingReligion.setSchoolId(null);
            return (Religion)this.religionRepository.save(existingReligion);
        });
    }

    public boolean deleteReligion(String schoolId, String id) {
        Optional religion = this.religionRepository.findById((Object)id);
        if (religion.isPresent()) {
            if (((Religion)religion.get()).isUniversal()) {
                throw new IllegalArgumentException("Universal religions cannot be deleted through school-specific endpoint");
            }
            if (!((Religion)religion.get()).getSchoolId().equals(schoolId)) {
                throw new IllegalArgumentException("Religion does not belong to the specified school");
            }
            this.religionRepository.deleteById((Object)id);
            return true;
        }
        return false;
    }

    public boolean deleteUniversalReligion(String id) {
        Optional religion = this.religionRepository.findById((Object)id);
        if (religion.isPresent()) {
            if (!((Religion)religion.get()).isUniversal()) {
                throw new IllegalArgumentException("This is not a universal religion");
            }
            this.religionRepository.deleteById((Object)id);
            return true;
        }
        return false;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.exception.AlreadyExist
 *  com.neel.Settings.exception.UniversalNotFound
 *  com.neel.Settings.model.BloodGroup
 *  com.neel.Settings.repo.BloodGroupRepository
 *  com.neel.Settings.service.BloodGroupService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.Settings.service;

import com.neel.Settings.exception.AlreadyExist;
import com.neel.Settings.exception.UniversalNotFound;
import com.neel.Settings.model.BloodGroup;
import com.neel.Settings.repo.BloodGroupRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodGroupService {
    @Autowired
    private BloodGroupRepository bloodGroupRepository;

    public Optional<BloodGroup> findBloodGroupByNameAndTypeAndSchool(String bloodGroupName, String bloodGroupType, String schoolId) {
        return this.bloodGroupRepository.findByBloodGroupNameAndBloodGroupTypeAndSchoolId(bloodGroupName, bloodGroupType, schoolId);
    }

    public List<BloodGroup> getBloodGroupsBySchoolId(String schoolId) {
        List schoolSpecificGroups = this.bloodGroupRepository.findBySchoolId(schoolId);
        List universalGroups = this.bloodGroupRepository.findByUniversalTrue();
        ArrayList<BloodGroup> allGroups = new ArrayList<BloodGroup>(schoolSpecificGroups);
        allGroups.addAll(universalGroups);
        return allGroups;
    }

    public List<BloodGroup> getAllUniversalBloodGroups() {
        return this.bloodGroupRepository.findByUniversalTrue();
    }

    public List<BloodGroup> createDefaultUniversalBloodGroups() {
        String[] bloodGroupNames = new String[]{"A", "B", "AB", "O"};
        String[] bloodGroupTypes = new String[]{"Positive", "Negative"};
        ArrayList<BloodGroup> createdGroups = new ArrayList<BloodGroup>();
        for (String name : bloodGroupNames) {
            for (String type : bloodGroupTypes) {
                Optional existingGroup = this.bloodGroupRepository.findByBloodGroupNameAndBloodGroupTypeAndUniversalTrue(name, type);
                if (!existingGroup.isPresent()) {
                    BloodGroup bloodGroup = new BloodGroup();
                    bloodGroup.setBloodGroupName(name);
                    bloodGroup.setBloodGroupType(type);
                    bloodGroup.setUniversal(true);
                    bloodGroup.setSchoolId(null);
                    bloodGroup.setBloodGroup(this.generateBloodGroup(name, type));
                    createdGroups.add((BloodGroup)this.bloodGroupRepository.save((Object)bloodGroup));
                    continue;
                }
                createdGroups.add((BloodGroup)existingGroup.get());
            }
        }
        return createdGroups;
    }

    public String getDefaultBloodGroupsJsonData() {
        String[] bloodGroupNames = new String[]{"A", "B", "AB", "O"};
        String[] bloodGroupTypes = new String[]{"Positive", "Negative"};
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        boolean firstGroup = true;
        for (String name : bloodGroupNames) {
            for (String type : bloodGroupTypes) {
                if (!firstGroup) {
                    jsonBuilder.append(",\n");
                }
                firstGroup = false;
                String bloodGroupValue = this.generateBloodGroup(name, type);
                jsonBuilder.append("  {\n");
                jsonBuilder.append("    \"bloodGroupName\": \"").append(name).append("\",\n");
                jsonBuilder.append("    \"bloodGroupType\": \"").append(type).append("\",\n");
                jsonBuilder.append("    \"bloodGroup\": \"").append(bloodGroupValue).append("\",\n");
                jsonBuilder.append("    \"universal\": true,\n");
                jsonBuilder.append("    \"schoolId\": null\n");
                jsonBuilder.append("  }");
            }
        }
        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }

    public boolean bloodGroupExistsAnywhere(String bloodGroupName, String bloodGroupType) {
        Optional universalGroup = this.bloodGroupRepository.findByBloodGroupNameAndBloodGroupTypeAndUniversalTrue(bloodGroupName, bloodGroupType);
        if (universalGroup.isPresent()) {
            return true;
        }
        List allGroups = this.bloodGroupRepository.findAll();
        return allGroups.stream().anyMatch(group -> group.getBloodGroupName().equalsIgnoreCase(bloodGroupName) && group.getBloodGroupType().equalsIgnoreCase(bloodGroupType));
    }

    public BloodGroup saveBloodGroup(BloodGroup bloodGroup) {
        Optional universalGroup = this.bloodGroupRepository.findByBloodGroupNameAndBloodGroupTypeAndUniversalTrue(bloodGroup.getBloodGroupName(), bloodGroup.getBloodGroupType());
        if (universalGroup.isPresent()) {
            throw new AlreadyExist("This blood group already exists as a universal blood group");
        }
        bloodGroup.setBloodGroup(this.generateBloodGroup(bloodGroup.getBloodGroupName(), bloodGroup.getBloodGroupType()));
        return (BloodGroup)this.bloodGroupRepository.save((Object)bloodGroup);
    }

    public BloodGroup saveUniversalBloodGroup(BloodGroup bloodGroup) {
        bloodGroup.setSchoolId(null);
        bloodGroup.setUniversal(true);
        if (this.bloodGroupExistsAnywhere(bloodGroup.getBloodGroupName(), bloodGroup.getBloodGroupType())) {
            throw new AlreadyExist("Blood group with this name and type already exists");
        }
        bloodGroup.setBloodGroup(this.generateBloodGroup(bloodGroup.getBloodGroupName(), bloodGroup.getBloodGroupType()));
        return (BloodGroup)this.bloodGroupRepository.save((Object)bloodGroup);
    }

    public List<BloodGroup> getAllBloodGroups() {
        return this.bloodGroupRepository.findAll();
    }

    public Optional<BloodGroup> updateBloodGroup(String schoolId, String id, BloodGroup updatedBloodGroup) {
        return this.bloodGroupRepository.findById((Object)id).map(existingGroup -> {
            if (existingGroup.isUniversal()) {
                throw new UniversalNotFound("Universal blood groups cannot be updated through school-specific endpoint");
            }
            if (!existingGroup.getSchoolId().equals(schoolId)) {
                throw new UniversalNotFound("Blood group does not belong to the specified school");
            }
            existingGroup.setBloodGroupName(updatedBloodGroup.getBloodGroupName());
            existingGroup.setBloodGroupType(updatedBloodGroup.getBloodGroupType());
            existingGroup.setBloodGroup(this.generateBloodGroup(updatedBloodGroup.getBloodGroupName(), updatedBloodGroup.getBloodGroupType()));
            existingGroup.setSchoolId(schoolId);
            return (BloodGroup)this.bloodGroupRepository.save(existingGroup);
        });
    }

    public Optional<BloodGroup> updateUniversalBloodGroup(String id, BloodGroup updatedBloodGroup) {
        return this.bloodGroupRepository.findById((Object)id).map(existingGroup -> {
            if (!existingGroup.isUniversal()) {
                throw new UniversalNotFound("This is not a universal blood group");
            }
            Optional duplicateGroup = this.bloodGroupRepository.findByBloodGroupNameAndBloodGroupTypeAndUniversalTrue(updatedBloodGroup.getBloodGroupName(), updatedBloodGroup.getBloodGroupType());
            if (duplicateGroup.isPresent() && !((BloodGroup)duplicateGroup.get()).getId().equals(id)) {
                throw new AlreadyExist("Universal blood group with these details already exists");
            }
            existingGroup.setBloodGroupName(updatedBloodGroup.getBloodGroupName());
            existingGroup.setBloodGroupType(updatedBloodGroup.getBloodGroupType());
            existingGroup.setBloodGroup(this.generateBloodGroup(updatedBloodGroup.getBloodGroupName(), updatedBloodGroup.getBloodGroupType()));
            existingGroup.setUniversal(true);
            existingGroup.setSchoolId(null);
            return (BloodGroup)this.bloodGroupRepository.save(existingGroup);
        });
    }

    public boolean deleteBloodGroup(String schoolId, String id) {
        Optional bloodGroup = this.bloodGroupRepository.findById((Object)id);
        if (bloodGroup.isPresent()) {
            if (((BloodGroup)bloodGroup.get()).isUniversal()) {
                throw new UniversalNotFound("Universal blood groups cannot be deleted through school-specific endpoint");
            }
            if (!((BloodGroup)bloodGroup.get()).getSchoolId().equals(schoolId)) {
                throw new UniversalNotFound("Blood group does not belong to the specified school");
            }
            this.bloodGroupRepository.deleteById((Object)id);
            return true;
        }
        return false;
    }

    public boolean deleteUniversalBloodGroup(String id) {
        Optional bloodGroup = this.bloodGroupRepository.findById((Object)id);
        if (bloodGroup.isPresent()) {
            if (!((BloodGroup)bloodGroup.get()).isUniversal()) {
                throw new UniversalNotFound("This is not a universal blood group");
            }
            this.bloodGroupRepository.deleteById((Object)id);
            return true;
        }
        return false;
    }

    private String generateBloodGroup(String bloodGroupName, String bloodGroupType) {
        String suffix = bloodGroupType.equalsIgnoreCase("Positive") ? "+" : (bloodGroupType.equalsIgnoreCase("Negative") ? "-" : "");
        return bloodGroupName + suffix;
    }
}


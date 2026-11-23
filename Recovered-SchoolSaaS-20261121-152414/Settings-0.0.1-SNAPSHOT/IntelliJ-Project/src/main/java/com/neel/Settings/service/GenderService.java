/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.exception.AlreadyExist
 *  com.neel.Settings.exception.UniversalNotFound
 *  com.neel.Settings.model.Gender
 *  com.neel.Settings.repo.GenderRepository
 *  com.neel.Settings.service.GenderService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.Settings.service;

import com.neel.Settings.exception.AlreadyExist;
import com.neel.Settings.exception.UniversalNotFound;
import com.neel.Settings.model.Gender;
import com.neel.Settings.repo.GenderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService {
    @Autowired
    private GenderRepository genderRepository;

    public Optional<Gender> findGenderByName(String genderName) {
        return this.genderRepository.findByGenderName(genderName);
    }

    public Optional<Gender> findGenderByNameAndSchool(String genderName, String schoolId) {
        return this.genderRepository.findByGenderNameAndSchoolId(genderName, schoolId);
    }

    public List<Gender> getGendersBySchoolId(String schoolId) {
        List schoolSpecificGenders = this.genderRepository.findBySchoolId(schoolId);
        List universalGenders = this.genderRepository.findByUniversalTrue();
        ArrayList<Gender> allGenders = new ArrayList<Gender>(schoolSpecificGenders);
        allGenders.addAll(universalGenders);
        return allGenders;
    }

    public boolean genderExistsAnywhere(String genderName) {
        Optional universalGender = this.genderRepository.findByGenderNameAndUniversalTrue(genderName.toLowerCase());
        if (universalGender.isPresent()) {
            return true;
        }
        List<Gender> allGenders = this.genderRepository.findAll();
        return allGenders.stream().anyMatch(gender -> gender.getGenderName().equalsIgnoreCase(genderName));
    }

    public Gender saveGender(Gender gender) {
        gender.setGenderName(gender.getGenderName().toLowerCase());
        Optional universalGender = this.genderRepository.findByGenderNameAndUniversalTrue(gender.getGenderName());
        if (universalGender.isPresent()) {
            throw new AlreadyExist("This gender already exists as a universal gender");
        }
        gender.setUniversal(false);
        return (Gender)this.genderRepository.save(gender);
    }

    public Gender saveUniversalGender(Gender gender) {
        gender.setSchoolId(null);
        gender.setUniversal(true);
        gender.setGenderName(gender.getGenderName().toLowerCase());
        if (this.genderExistsAnywhere(gender.getGenderName())) {
            throw new AlreadyExist("Gender with this name already exists");
        }
        return (Gender)this.genderRepository.save(gender);
    }

    public List<Gender> getAllGenders() {
        return this.genderRepository.findAll();
    }

    public List<Gender> getAllUniversalGenders() {
        return this.genderRepository.findByUniversalTrue();
    }

    public List<Gender> createDefaultUniversalGenders() {
        String[] genderNames = new String[]{"male", "female", "other"};
        ArrayList<Gender> createdGenders = new ArrayList<Gender>();
        for (String name : genderNames) {
            Optional existingGender = this.genderRepository.findByGenderNameAndUniversalTrue(name);
            if (!existingGender.isPresent()) {
                Gender gender = new Gender();
                gender.setGenderName(name);
                gender.setUniversal(true);
                gender.setSchoolId(null);
                createdGenders.add((Gender)this.genderRepository.save(gender));
                continue;
            }
            createdGenders.add((Gender)existingGender.get());
        }
        return createdGenders;
    }

    public String getDefaultGendersJsonData() {
        String[] genderNames = new String[]{"male", "female", "other"};
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        boolean firstGender = true;
        for (String name : genderNames) {
            if (!firstGender) {
                jsonBuilder.append(",\n");
            }
            firstGender = false;
            jsonBuilder.append("  {\n");
            jsonBuilder.append("    \"genderName\": \"").append(name).append("\",\n");
            jsonBuilder.append("    \"universal\": true,\n");
            jsonBuilder.append("    \"schoolId\": null\n");
            jsonBuilder.append("  }");
        }
        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }

    public Optional<Gender> updateGender(String schoolId, String id, Gender updatedGender) {
        return this.genderRepository.findById(id).map(existingGender -> {
            if (existingGender.isUniversal()) {
                throw new UniversalNotFound("Universal genders cannot be updated through school-specific endpoint");
            }
            if (!existingGender.getSchoolId().equals(schoolId)) {
                throw new UniversalNotFound("Gender does not belong to the specified school");
            }
            existingGender.setGenderName(updatedGender.getGenderName().toLowerCase());
            existingGender.setSchoolId(schoolId);
            return (Gender)this.genderRepository.save(existingGender);
        });
    }

    public Optional<Gender> updateUniversalGender(String id, Gender updatedGender) {
        return this.genderRepository.findById(id).map(existingGender -> {
            if (!existingGender.isUniversal()) {
                throw new UniversalNotFound("This is not a universal gender");
            }
            Optional duplicateGender = this.genderRepository.findByGenderNameAndUniversalTrue(updatedGender.getGenderName().toLowerCase());
            if (duplicateGender.isPresent() && !((Gender)duplicateGender.get()).getId().equals(id)) {
                throw new AlreadyExist("Universal gender with this name already exists");
            }
            existingGender.setGenderName(updatedGender.getGenderName().toLowerCase());
            existingGender.setUniversal(true);
            existingGender.setSchoolId(null);
            return (Gender)this.genderRepository.save(existingGender);
        });
    }

    public boolean deleteGender(String schoolId, String id) {
        Optional gender = this.genderRepository.findById(id);
        if (gender.isPresent()) {
            if (((Gender)gender.get()).isUniversal()) {
                throw new UniversalNotFound("Universal genders cannot be deleted through school-specific endpoint");
            }
            if (!((Gender)gender.get()).getSchoolId().equals(schoolId)) {
                throw new UniversalNotFound("Gender does not belong to the specified school");
            }
            this.genderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteUniversalGender(String id) {
        Optional gender = this.genderRepository.findById(id);
        if (gender.isPresent()) {
            if (!((Gender)gender.get()).isUniversal()) {
                throw new UniversalNotFound("This is not a universal gender");
            }
            this.genderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


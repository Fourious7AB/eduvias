/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.dto.FeesStructureDTO
 *  com.neel.StudentAdmission.dto.FeesStructureDTO$AdditionalCharge
 *  lombok.Generated
 */
package com.neel.StudentAdmission.dto;

import com.neel.StudentAdmission.dto.FeesStructureDTO;
import java.util.List;
import lombok.Generated;

public class FeesStructureDTO {
    private String id;
    private String schoolId;
    private String className;
    private String section;
    private double admission_Fees;
    private double books;
    private double donation;
    private double fine;
    private double id_Card_Charges;
    private double late_Fees;
    private double miscellaneous;
    private double transportationalFees;
    private double tuitionFees;
    private double uniform_Charges;
    private double other;
    private double totalFees;
    private List<AdditionalCharge> additionalCharges;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getSection() {
        return this.section;
    }

    @Generated
    public double getAdmission_Fees() {
        return this.admission_Fees;
    }

    @Generated
    public double getBooks() {
        return this.books;
    }

    @Generated
    public double getDonation() {
        return this.donation;
    }

    @Generated
    public double getFine() {
        return this.fine;
    }

    @Generated
    public double getId_Card_Charges() {
        return this.id_Card_Charges;
    }

    @Generated
    public double getLate_Fees() {
        return this.late_Fees;
    }

    @Generated
    public double getMiscellaneous() {
        return this.miscellaneous;
    }

    @Generated
    public double getTransportationalFees() {
        return this.transportationalFees;
    }

    @Generated
    public double getTuitionFees() {
        return this.tuitionFees;
    }

    @Generated
    public double getUniform_Charges() {
        return this.uniform_Charges;
    }

    @Generated
    public double getOther() {
        return this.other;
    }

    @Generated
    public double getTotalFees() {
        return this.totalFees;
    }

    @Generated
    public List<AdditionalCharge> getAdditionalCharges() {
        return this.additionalCharges;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setAdmission_Fees(double admission_Fees) {
        this.admission_Fees = admission_Fees;
    }

    @Generated
    public void setBooks(double books) {
        this.books = books;
    }

    @Generated
    public void setDonation(double donation) {
        this.donation = donation;
    }

    @Generated
    public void setFine(double fine) {
        this.fine = fine;
    }

    @Generated
    public void setId_Card_Charges(double id_Card_Charges) {
        this.id_Card_Charges = id_Card_Charges;
    }

    @Generated
    public void setLate_Fees(double late_Fees) {
        this.late_Fees = late_Fees;
    }

    @Generated
    public void setMiscellaneous(double miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    @Generated
    public void setTransportationalFees(double transportationalFees) {
        this.transportationalFees = transportationalFees;
    }

    @Generated
    public void setTuitionFees(double tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

    @Generated
    public void setUniform_Charges(double uniform_Charges) {
        this.uniform_Charges = uniform_Charges;
    }

    @Generated
    public void setOther(double other) {
        this.other = other;
    }

    @Generated
    public void setTotalFees(double totalFees) {
        this.totalFees = totalFees;
    }

    @Generated
    public void setAdditionalCharges(List<AdditionalCharge> additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FeesStructureDTO)) {
            return false;
        }
        FeesStructureDTO other = (FeesStructureDTO)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getAdmission_Fees(), other.getAdmission_Fees()) != 0) {
            return false;
        }
        if (Double.compare(this.getBooks(), other.getBooks()) != 0) {
            return false;
        }
        if (Double.compare(this.getDonation(), other.getDonation()) != 0) {
            return false;
        }
        if (Double.compare(this.getFine(), other.getFine()) != 0) {
            return false;
        }
        if (Double.compare(this.getId_Card_Charges(), other.getId_Card_Charges()) != 0) {
            return false;
        }
        if (Double.compare(this.getLate_Fees(), other.getLate_Fees()) != 0) {
            return false;
        }
        if (Double.compare(this.getMiscellaneous(), other.getMiscellaneous()) != 0) {
            return false;
        }
        if (Double.compare(this.getTransportationalFees(), other.getTransportationalFees()) != 0) {
            return false;
        }
        if (Double.compare(this.getTuitionFees(), other.getTuitionFees()) != 0) {
            return false;
        }
        if (Double.compare(this.getUniform_Charges(), other.getUniform_Charges()) != 0) {
            return false;
        }
        if (Double.compare(this.getOther(), other.getOther()) != 0) {
            return false;
        }
        if (Double.compare(this.getTotalFees(), other.getTotalFees()) != 0) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$section = this.getSection();
        String other$section = other.getSection();
        if (this$section == null ? other$section != null : !this$section.equals(other$section)) {
            return false;
        }
        List this$additionalCharges = this.getAdditionalCharges();
        List other$additionalCharges = other.getAdditionalCharges();
        return !(this$additionalCharges == null ? other$additionalCharges != null : !((Object)this$additionalCharges).equals(other$additionalCharges));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof FeesStructureDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $admission_Fees = Double.doubleToLongBits(this.getAdmission_Fees());
        result = result * 59 + (int)($admission_Fees >>> 32 ^ $admission_Fees);
        long $books = Double.doubleToLongBits(this.getBooks());
        result = result * 59 + (int)($books >>> 32 ^ $books);
        long $donation = Double.doubleToLongBits(this.getDonation());
        result = result * 59 + (int)($donation >>> 32 ^ $donation);
        long $fine = Double.doubleToLongBits(this.getFine());
        result = result * 59 + (int)($fine >>> 32 ^ $fine);
        long $id_Card_Charges = Double.doubleToLongBits(this.getId_Card_Charges());
        result = result * 59 + (int)($id_Card_Charges >>> 32 ^ $id_Card_Charges);
        long $late_Fees = Double.doubleToLongBits(this.getLate_Fees());
        result = result * 59 + (int)($late_Fees >>> 32 ^ $late_Fees);
        long $miscellaneous = Double.doubleToLongBits(this.getMiscellaneous());
        result = result * 59 + (int)($miscellaneous >>> 32 ^ $miscellaneous);
        long $transportationalFees = Double.doubleToLongBits(this.getTransportationalFees());
        result = result * 59 + (int)($transportationalFees >>> 32 ^ $transportationalFees);
        long $tuitionFees = Double.doubleToLongBits(this.getTuitionFees());
        result = result * 59 + (int)($tuitionFees >>> 32 ^ $tuitionFees);
        long $uniform_Charges = Double.doubleToLongBits(this.getUniform_Charges());
        result = result * 59 + (int)($uniform_Charges >>> 32 ^ $uniform_Charges);
        long $other = Double.doubleToLongBits(this.getOther());
        result = result * 59 + (int)($other >>> 32 ^ $other);
        long $totalFees = Double.doubleToLongBits(this.getTotalFees());
        result = result * 59 + (int)($totalFees >>> 32 ^ $totalFees);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        List $additionalCharges = this.getAdditionalCharges();
        result = result * 59 + ($additionalCharges == null ? 43 : ((Object)$additionalCharges).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "FeesStructureDTO(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", admission_Fees=" + this.getAdmission_Fees() + ", books=" + this.getBooks() + ", donation=" + this.getDonation() + ", fine=" + this.getFine() + ", id_Card_Charges=" + this.getId_Card_Charges() + ", late_Fees=" + this.getLate_Fees() + ", miscellaneous=" + this.getMiscellaneous() + ", transportationalFees=" + this.getTransportationalFees() + ", tuitionFees=" + this.getTuitionFees() + ", uniform_Charges=" + this.getUniform_Charges() + ", other=" + this.getOther() + ", totalFees=" + this.getTotalFees() + ", additionalCharges=" + this.getAdditionalCharges() + ")";
    }

    @Generated
    public FeesStructureDTO(String id, String schoolId, String className, String section, double admission_Fees, double books, double donation, double fine, double id_Card_Charges, double late_Fees, double miscellaneous, double transportationalFees, double tuitionFees, double uniform_Charges, double other, double totalFees, List<AdditionalCharge> additionalCharges) {
        this.id = id;
        this.schoolId = schoolId;
        this.className = className;
        this.section = section;
        this.admission_Fees = admission_Fees;
        this.books = books;
        this.donation = donation;
        this.fine = fine;
        this.id_Card_Charges = id_Card_Charges;
        this.late_Fees = late_Fees;
        this.miscellaneous = miscellaneous;
        this.transportationalFees = transportationalFees;
        this.tuitionFees = tuitionFees;
        this.uniform_Charges = uniform_Charges;
        this.other = other;
        this.totalFees = totalFees;
        this.additionalCharges = additionalCharges;
    }

    @Generated
    public FeesStructureDTO() {
    }

    public static class AdditionalCharge {
        private String description;
        private double charges;

        public AdditionalCharge() {
        }

        public AdditionalCharge(String description, double charges) {
            this.description = description;
            this.charges = charges;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getCharges() {
            return this.charges;
        }

        public void setCharges(double charges) {
            this.charges = charges;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AdditionalCharge)) return false;
            AdditionalCharge other = (AdditionalCharge) o;
            return Double.compare(this.charges, other.charges) == 0 && java.util.Objects.equals(this.description, other.description);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(this.description, this.charges);
        }

        @Override
        public String toString() {
            return "FeesStructureDTO.AdditionalCharge(description=" + this.description + ", charges=" + this.charges + ")";
        }
    }
}


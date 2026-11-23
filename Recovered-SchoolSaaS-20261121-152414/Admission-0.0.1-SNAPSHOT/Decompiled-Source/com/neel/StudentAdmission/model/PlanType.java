/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.PlanType
 */
package com.neel.StudentAdmission.model;

public enum PlanType {
    FREE,
    ENTERPRISE,
    PRO,
    PREMIUM;


    public static PlanType fromString(String planName) {
        switch (planName.toLowerCase()) {
            case "free": {
                return FREE;
            }
            case "enterprise": {
                return ENTERPRISE;
            }
            case "pro": {
                return PRO;
            }
            case "premium": {
                return PREMIUM;
            }
        }
        throw new IllegalArgumentException("Invalid plan name. Allowed values: free, enterprise, premium");
    }
}


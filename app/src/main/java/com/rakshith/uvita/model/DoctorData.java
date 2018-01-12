package com.rakshith.uvita.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorData {

    private List<Doctor> doctors = null;
    private String lastKey;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
package com.example.softcomputing.fuzzy.utils;

import java.util.Map;

public class FuzzySet {

    Map<String, Double> membershipValues;

    public Map<String, Double> getMembershipValues() {
        return membershipValues;
    }

    public void setMembershipValues(Map<String, Double> membershipValues) {
        this.membershipValues = membershipValues;
    }

    /*
        Input: Fuzzified features from the Fuzzifier
        • Interest: Low=0, Medium=0.6, High=0.4
        • Budget: Low=0, Medium=0.8, High=0.2
    */
}

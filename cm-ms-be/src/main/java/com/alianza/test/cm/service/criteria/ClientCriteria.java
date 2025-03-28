package com.alianza.test.cm.service.criteria;

import java.time.ZonedDateTime;

/**
 * Filter criteria class for {@link com.alianza.test.cm.model.Client} entities.
 * This class is used to specify filter conditions for client search operations.
 */
public class ClientCriteria {

    private String sharedKey;
    private String name;
    private String email;
    private String phone;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    /**
     * @return the sharedKey
     */
    public String getSharedKey() {
        return sharedKey;
    }

    /**
     * @param sharedKey the sharedKey to set
     */
    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the startDate
     */
    public ZonedDateTime getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public ZonedDateTime getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }
}

package com.alianza.test.cm.model;

import com.alianza.test.cm.configuration.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a client entity with personal and business information.
 */
@Entity
@Table(name = "cmt_client")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "shared_key", nullable = false, length = 50)
    @Size(max = 50)
    private String sharedKey;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;


    @NotNull
    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotNull
    @Size(max = 10)
    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

    @NotNull
    @Column(name = "start_date", nullable = false)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date" ,nullable = false)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime endDate;

    @CreationTimestamp
    @Column(name = "data_added")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime dataAdded;

    /**
     * Default constructor for Client.
     */
    public Client() {
    }

    /**
     * Creates a new Client with all attributes specified.
     *
     * @param id The unique identifier for this client
     * @param sharedKey The shared key used for client identification
     * @param name The full name of the client
     * @param email The email address of the client
     * @param phone The phone number of the client
     * @param startDate The date when the client relationship started
     * @param endDate The date when the client relationship ended, if applicable
     * @param dataAdded The date when the client record was added to the system
     */
    public Client(UUID id, String sharedKey, String name, String email, String phone, ZonedDateTime startDate, ZonedDateTime endDate, ZonedDateTime dataAdded) {
        this.id = id;
        this.sharedKey = sharedKey;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dataAdded = dataAdded;
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

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

    /**
     * @return the dataAdded
     */
    public ZonedDateTime getDataAdded() {
        return dataAdded;
    }

    /**
     * @param dataAdded the dataAdded to set
     */
    public void setDataAdded(ZonedDateTime dataAdded) {
        this.dataAdded = dataAdded;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}

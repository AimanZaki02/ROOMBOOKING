package com.library.roombookings;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
@Entity
@Table(name = "room_bookings")
public class RoomBookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_code", nullable = false, length = 50) // Column name as per the table
    private String roomCode;

    @Column(name = "room_name", nullable = false, length = 100) // Column name as per the table
    private String roomName;

    @Column(name = "customer_name", nullable = false, length = 100) // Column name as per the table
    private String customerName;

    @Column(name = "phone_number", nullable = false, length = 15) // Column name as per the table
    private String phoneNumber;

    @Column(name = "booking_date", nullable = false) // Column name as per the table
    private Date bookingDate;

    @Column(name = "booking_time", nullable = false) // Column name as per the table
    private Time bookingTime;

    // Getters and setters
    // ...

    // Implement getters and setters for all fields
    // For example:
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Time getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Time bookingTime) {
        this.bookingTime = bookingTime;
    }

}

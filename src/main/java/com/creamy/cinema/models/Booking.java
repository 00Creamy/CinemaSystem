package com.creamy.cinema.models;

import java.time.LocalDateTime;

public class Booking {
    public enum BookingStatus {
        RESERVED(0, "Reserved"),
        PAYED(1, "Payed"),
        CANCELLED(2, "Cancelled");

        private final int number;
        private final String name;

        BookingStatus(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public static BookingStatus getBookingStatusByNumber(int number) {
            return switch (number) {
                case 0 -> RESERVED;
                case 1 -> PAYED;
                case 2 -> CANCELLED;
                default -> null;
            };
        }
    }

    private int bookingId;
    private int scheduleId;
    private String seat;
    private LocalDateTime addedDatetime;
    private BookingStatus status;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public LocalDateTime getAddedDatetime() {
        return addedDatetime;
    }

    public void setAddedDatetime(LocalDateTime addedDatetime) {
        this.addedDatetime = addedDatetime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}

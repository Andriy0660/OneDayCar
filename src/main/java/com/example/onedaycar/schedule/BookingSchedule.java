package com.example.onedaycar.schedule;

import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.service.BookingService;
import com.example.onedaycar.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingSchedule {
    private final BookingService bookingService;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void editBookingStatus() {
        List<Booking> allCREATEDBookings = bookingService.getAllByStatus(Status.CREATED.toString());
        List<Booking> allACCEPTEDBookings = bookingService.getAllByStatus(Status.ACCEPTED.toString());
        LocalDate now = LocalDate.now();
        for (Booking booking : allCREATEDBookings) {
            if (now.isAfter(booking.getStartDate())) {
                booking.setStatus(Status.REJECTED.toString());
                bookingService.save(booking);

            }
        }

        for (Booking booking : allACCEPTEDBookings) {
            if (now.isAfter(booking.getEndDate())) {
                booking.setStatus(Status.DONE.toString());
                bookingService.save(booking);

            }
        }
    }
}

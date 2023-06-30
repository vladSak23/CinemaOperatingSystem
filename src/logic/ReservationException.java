package logic;

import java.io.IOException;

public class ReservationException extends IOException {
    public ReservationException() {
    }

    public ReservationException(String message) {
        super(message);
    }
}

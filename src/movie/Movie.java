package movie;

import java.io.Serializable;

public abstract class Movie {
    private String title;
    private String date;
    private double price;
    private int hall;

    public Movie(String title, String date, double price, int hall) {
        this.title = title;
        this.date = date;
        this.price = price;
        this.hall = hall;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public int getHall() {
        return hall;
    }
}
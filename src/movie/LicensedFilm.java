package movie;

public class LicensedFilm extends Movie {

    public LicensedFilm(String title, String date, double price, int hall) {
        super(title, date, price, hall);
    }

    @Override
    public String toString() {
        return super.getTitle() + "," + super.getDate() + "," + super.getPrice() + "," + super.getHall();
    }
}

package logic;

import UI.CinemaSystemApp;
import movie.LicensedFilm;
import movie.Movie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class CinemaSystemManager {
    CinemaSystemApp cinemaSystemApp;
    private String filePathToConfig = "config.txt";
    private final int rows = 5;
    private final int cols = 7;

    public CinemaSystemManager(CinemaSystemApp cinemaSystemApp) {
        this.cinemaSystemApp = cinemaSystemApp;
    }

    public void loadMoviesFromConfigFile(DefaultListModel<Movie> moviesModel, Map<Movie, boolean[][]> seatsMap, Map<Movie, boolean[][]> reservationsMap) {
        try (Scanner scanner = new Scanner(new File(filePathToConfig))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] movieData = line.split(",");
                if (movieData.length == 4) {
                    String title = movieData[0];
                    String date = movieData[1];
                    double price = Double.parseDouble(movieData[2]);
                    int hall = Integer.parseInt(movieData[3]);

                    Movie movie = new LicensedFilm(title, date, price, hall);
                    moviesModel.addElement(movie);
                    seatsMap.put(movie, createEmptySeatsArray());
                    reservationsMap.put(movie, createEmptySeatsArray());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean[][] createEmptySeatsArray() {
        boolean[][] seats = new boolean[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                seats[row][col] = false;
            }
        }
        return seats;
    }

    public void showMovieDetails(Movie movie) {
        JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "movie.Movie Details: " + movie.getTitle() +
                "\nDate: " + movie.getDate() +
                "\nPrice: $" + cinemaSystemApp.getDecimalFormat().format(movie.getPrice()) +
                "\nHall: " + movie.getHall());
    }

    public void buyTicket(Movie movie) {
        String name = cinemaSystemApp.getNameLabel2().getText();
        String moneyText = cinemaSystemApp.getMoneyTextField().getText();

        if (name.isEmpty() || moneyText.isEmpty()) {
            JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "Please enter your name and money amount.");
            return;
        }

        double money;
        try {
            money = Double.parseDouble(moneyText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "Invalid money amount. Please enter a valid number.");
            return;
        }

        if (money < movie.getPrice()) {
            JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "Insufficient funds. Please add more money.");
            return;
        }

        boolean[][] seats = cinemaSystemApp.getMovieSeatsMap().get(movie);

        if (cinemaSystemApp.isReservation()) {
            seats[cinemaSystemApp.getReservedSeat()[0]][cinemaSystemApp.getReservedSeat()[1]] = true;
            cinemaSystemApp.setReservation(false);
            cinemaSystemApp.getReserveButton().setText("Reserve");
            cinemaSystemApp.getNameLabel2().setEnabled(true);
            payForTicket(movie);
        } else {

            JFrame seatFrame = new JFrame("Seat Selection");
            seatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            seatFrame.setLayout(new GridLayout(rows, cols));

            JButton[][] seatButtons = new JButton[rows][cols];

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    JButton seatButton = new JButton((row + 1) + "-" + (col + 1));
                    final int buttonRow = row;
                    final int buttonCol = col;


                    if (seats[buttonRow][buttonCol] || cinemaSystemApp.getMovieReservationsMap().get(movie)[buttonRow][buttonCol]) {
                        seatButton.setEnabled(false);
                    } else {
                        seatButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                seatButton.setEnabled(false);
                                seats[buttonRow][buttonCol] = true;
                                buyTicketConfirmed(name, movie);
                                seatFrame.dispose();
                            }
                        });
                    }

                    seatButtons[row][col] = seatButton;
                    seatFrame.add(seatButton);
                }
            }

            seatFrame.pack();
            seatFrame.setVisible(true);
        }
    }

    private void payForTicket(Movie movie) {
       cinemaSystemApp.getMoneyTextField().setText(String.valueOf(Double.parseDouble(cinemaSystemApp.getMoneyTextField().getText()) - movie.getPrice()));
    }

    public void reserveTicket(Movie movie) throws ReservationException {
        String name = cinemaSystemApp.getNameLabel2().getText();
        String moneyText = cinemaSystemApp.getMoneyTextField().getText();

        if (name.isEmpty() || moneyText.isEmpty()) {
            JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "Please enter your name and money amount.");
            throw new ReservationException("reservation error");
        }
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "Please enter your name.");
            throw new ReservationException("reservation error");
        }

        if (cinemaSystemApp.isReservation()) {

           cinemaSystemApp.getMovieReservationsMap().get(movie)[cinemaSystemApp.getReservedSeat()[0]][cinemaSystemApp.getReservedSeat()[1]] = false;

            cinemaSystemApp.setReservation(false);

            cinemaSystemApp.getReserveButton().setText("Reserve");

            cinemaSystemApp.getMoneyTextField().setEnabled(true);
        } else {
            boolean[][] seats = cinemaSystemApp.getMovieSeatsMap().get(movie);
            boolean[][] reservations = cinemaSystemApp.getMovieReservationsMap().get(movie);

            JFrame seatFrame = new JFrame("Seat Selection");
            seatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            seatFrame.setLayout(new GridLayout(rows, cols));

            JButton[][] seatButtons = new JButton[rows][cols];

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    JButton seatButton = new JButton((row + 1) + "-" + (col + 1));
                    final int buttonRow = row;
                    final int buttonCol = col;


                    if (seats[buttonRow][buttonCol] || reservations[buttonRow][buttonCol]) {
                        seatButton.setEnabled(false);
                    } else {
                        seatButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                seatButton.setEnabled(false);

                                cinemaSystemApp.getReservedSeat()[0] = buttonRow;
                                cinemaSystemApp.getReservedSeat()[1] = buttonCol;

                                reservations[buttonRow][buttonCol] = true;

                                reserveTicketConfirmed(name, movie);

                               cinemaSystemApp.getReserveButton().setText("cancel reserve");

                                cinemaSystemApp.setReservation(true);

                                cinemaSystemApp.getNameLabel2().setEnabled(false);

                                seatFrame.dispose();
                            }
                        });
                    }

                    seatButtons[row][col] = seatButton;
                    seatFrame.add(seatButton);
                }
            }

            seatFrame.pack();
            seatFrame.setVisible(true);
        }
    }

    public void createTicketFile(String name, Movie movie, double price) {
        try {
            String fileName = name + "_ticket.txt";
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("Name: " + name + "\n");
            fileWriter.write("Movie: " + movie.getTitle() + "\n");
            fileWriter.write("Price: $" + cinemaSystemApp.getDecimalFormat().format(price) + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buyTicketConfirmed(String name, Movie movie) {
        double price = movie.getPrice();
        JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "Ticket purchased successfully!\n" +
                "Name: " + name +
                "\nMovie: " + movie.getTitle() +
                "\nPrice: $" + cinemaSystemApp.getDecimalFormat().format(price));
        payForTicket(movie);

        createTicketFile(name, movie, price);
    }

    private void reserveTicketConfirmed(String name, Movie movie) {
        double price = movie.getPrice();
        JOptionPane.showMessageDialog(cinemaSystemApp.getMainFrame(), "Ticket reserved successfully!\n" +
                "Name: " + name +
                "\nMovie: " + movie.getTitle() +
                "\nPrice: $" + cinemaSystemApp.getDecimalFormat().format(price));
    }
}

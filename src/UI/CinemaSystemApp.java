package UI;

import account.Account;
import logic.CinemaSystemManager;
import logic.Main;
import logic.MovieListCellRenderer;
import logic.ReservationException;
import movie.Movie;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CinemaSystemApp {
    private JFrame mainFrame;
    private JTextField moneyTextField;
    private JList<Movie> moviesList;
    private int[] reservedSeat = new int[2];
    private JLabel nameLabel;
    private JLabel nameLabel2;
    private JLabel moneyLabel;
    private JButton buyButton;
    private JButton reserveButton;
    private JButton logoutButton;
    private boolean isReservation;

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public static Map<Movie, boolean[][]> movieSeatsMap;
    public static Map<Movie, boolean[][]> movieReservationsMap;

    public void initialize(Account account) {
        CinemaSystemManager cinemaSystemManager = new CinemaSystemManager(this);

        mainFrame = new JFrame("Cinema System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        nameLabel = new JLabel("Name: ");
        moneyLabel = new JLabel("Money: ");

        nameLabel2 = new JLabel(account.getName());
        moneyTextField = new JTextField();

        inputPanel.add(nameLabel);
        inputPanel.add(nameLabel2);
        inputPanel.add(moneyLabel);
        inputPanel.add(moneyTextField);

        DefaultListModel<Movie> moviesModel = new DefaultListModel<>();
        movieSeatsMap = new HashMap<>();
        movieReservationsMap = new HashMap<>();

        cinemaSystemManager.loadMoviesFromConfigFile(moviesModel, movieSeatsMap, movieReservationsMap);

        moviesList = new JList<>(moviesModel);
        moviesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moviesList.setCellRenderer(new MovieListCellRenderer());

        moviesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Movie selectedMovie = moviesList.getSelectedValue();
                    if (selectedMovie != null) {
                        cinemaSystemManager.showMovieDetails(selectedMovie);
                    }
                }
            }
        });

        buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Movie selectedMovie = moviesList.getSelectedValue();
                if (selectedMovie != null) {
                    cinemaSystemManager.buyTicket(selectedMovie);
                }
            }
        });

        reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Movie selectedMovie = moviesList.getSelectedValue();
                if (selectedMovie != null) {
                    try {
                        cinemaSystemManager.reserveTicket(selectedMovie);
                    } catch (ReservationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
                LoginUI.isFirstProgramStart = false;
                LoginUI loginUI = new LoginUI(Main.filePathToAccounts, this);
                mainFrame.setVisible(false);
            });

        JPanel moviePanel = new JPanel(new BorderLayout());
        moviePanel.add(new JScrollPane(moviesList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(buyButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(logoutButton);
        moviePanel.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.add(inputPanel, BorderLayout.NORTH);
        mainFrame.add(moviePanel, BorderLayout.CENTER);

        mainFrame.setSize(400, 300);
        mainFrame.setVisible(true);
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public void setLogoutButton(JButton logoutButton) {
        this.logoutButton = logoutButton;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(JLabel nameLabel) {
        this.nameLabel = nameLabel;
    }

    public JLabel getMoneyLabel() {
        return moneyLabel;
    }

    public void setMoneyLabel(JLabel moneyLabel) {
        this.moneyLabel = moneyLabel;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public JLabel getNameLabel2() {
        return nameLabel2;
    }

    public JTextField getMoneyTextField() {
        return moneyTextField;
    }

    public void setMoneyTextField(JTextField moneyTextField) {
        this.moneyTextField = moneyTextField;
    }

    public JList<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(JList<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public int[] getReservedSeat() {
        return reservedSeat;
    }

    public void setReservedSeat(int[] reservedSeat) {
        this.reservedSeat = reservedSeat;
    }

    public JButton getBuyButton() {
        return buyButton;
    }

    public void setBuyButton(JButton buyButton) {
        this.buyButton = buyButton;
    }

    public JButton getReserveButton() {
        return reserveButton;
    }

    public void setReserveButton(JButton reserveButton) {
        this.reserveButton = reserveButton;
    }

    public boolean isReservation() {
        return isReservation;
    }

    public void setReservation(boolean reservation) {
        isReservation = reservation;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public Map<Movie, boolean[][]> getMovieSeatsMap() {
        return movieSeatsMap;
    }

    public void setMovieSeatsMap(Map<Movie, boolean[][]> movieSeatsMap) {
        this.movieSeatsMap = movieSeatsMap;
    }

    public Map<Movie, boolean[][]> getMovieReservationsMap() {
        return movieReservationsMap;
    }

    public void setMovieReservationsMap(Map<Movie, boolean[][]> movieReservationsMap) {
        this.movieReservationsMap = movieReservationsMap;
    }
}

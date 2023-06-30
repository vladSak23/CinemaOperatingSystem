package UI;

import movie.LicensedFilm;
import movie.Movie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigEditorUI extends JFrame {

    private List<Movie> movies;
    private DefaultListModel<Movie> moviesListModel;
    private JList<Movie> moviesList;
    private JTextField titleTextField;
    private JTextField yearTextField;
    private JTextField priceTextField;
    private JTextField hallTextField;

    public ConfigEditorUI() {
        movies = new ArrayList<>();
        moviesListModel = new DefaultListModel<>();

        loadMoviesFromFile();

        setTitle("Config Editor");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        setLayout(new BorderLayout());

        moviesList = new JList<>(moviesListModel);
        JScrollPane moviesScrollPane = new JScrollPane(moviesList);
        add(moviesScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));

        JLabel titleLabel = new JLabel("Title:");
        titleTextField = new JTextField();
        JLabel yearLabel = new JLabel("Date:");
        yearTextField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        priceTextField = new JTextField();
        JLabel hallLabel = new JLabel("Hall:");
        hallTextField = new JTextField();

        inputPanel.add(titleLabel);
        inputPanel.add(titleTextField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearTextField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceTextField);
        inputPanel.add(hallLabel);
        inputPanel.add(hallTextField);

        add(inputPanel, BorderLayout.NORTH);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleTextField.getText();
                String date = yearTextField.getText();
                double price = Double.parseDouble(priceTextField.getText());
                int hall = Integer.parseInt(hallTextField.getText());

                Movie movie = new LicensedFilm(title, date, price, hall);
                movies.add(movie);
                moviesListModel.addElement(movie);

                clearInputFields();
                saveMoviesToFile();
            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Movie selectedMovie = moviesList.getSelectedValue();
                if (selectedMovie != null) {
                    movies.remove(selectedMovie);
                    moviesListModel.removeElement(selectedMovie);
                    saveMoviesToFile();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void clearInputFields() {
        titleTextField.setText("");
        yearTextField.setText("");
        priceTextField.setText("");
        hallTextField.setText("");
    }

    private void loadMoviesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String title = parts[0].trim();
                    String date = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int hall = Integer.parseInt(parts[3].trim());

                    Movie movie = new LicensedFilm(title, date, price, hall);
                    movies.add(movie);
                    moviesListModel.addElement(movie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMoviesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"))) {
            for (Movie movie : movies) {
                writer.write(movie.getTitle() + "," + movie.getDate() + "," + movie.getPrice() + "," + movie.getHall());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        saveMoviesToFile();
        super.dispose();
    }
}

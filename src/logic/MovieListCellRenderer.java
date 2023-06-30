package logic;

import movie.Movie;

import javax.swing.*;
import java.awt.*;

public class MovieListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Movie) {
            Movie movie = (Movie) value;
            setText(movie.getTitle() + " (" + movie.getDate() + ")");
        }
        return this;
    }
}
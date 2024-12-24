package G9_06;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Record {
    private final StringProperty date;
    private final StringProperty amount;
    private final StringProperty category;
    private final StringProperty note;

    public Record(String date, String amount, String category, String note) {
        this.date = new SimpleStringProperty(date);
        this.amount = new SimpleStringProperty(amount);
        this.category = new SimpleStringProperty(category);
        this.note = new SimpleStringProperty(note);
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty noteProperty() {
        return note;
    }

    public String getDate() {
        return date.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getNote() {
        return note.get();
    }

    @Override
    public String toString() {
        return "Record{" +
                "date=" + getDate() +
                ", amount=" + getAmount() +
                ", category=" + getCategory() +
                ", note=" + getNote() +
                '}';
    }
}

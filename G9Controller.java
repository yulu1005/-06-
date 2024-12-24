package G9_06;

import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class G9Controller {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> categoryCombo;

    @FXML
    private TextField noteField;

    @FXML
    private TableView<Record> recordsTable;

    @FXML
    private TableColumn<Record, String> dateColumn;

    @FXML
    private TableColumn<Record, String> amountColumn;

    @FXML
    private TableColumn<Record, String> categoryColumn;

    @FXML
    private TableColumn<Record, String> noteColumn;

    @FXML
    private TextField limitField;  // 用於設置上限的 TextField

    @FXML
    private Label balanceLabel;  // 顯示餘額的 Label

    private double limit = 0.0;  // 記錄上限金額
    private double balance = 0.0;  // 記錄當前餘額

    @FXML
    public void initialize() {
        // 初始化表格欄位綁定
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());

        // 初始化類別選項
        categoryCombo.getItems().addAll("飲食", "交通", "娛樂", "其他", "收入");

        // 初始化顯示餘額
        updateBalanceLabel();
    }

    @FXML
    private void setLimit() {
        try {
            // 設定用戶輸入的上限
            limit = Double.parseDouble(limitField.getText());
            balance = limit;  // 初始餘額為上限
            showAlert("成功", "上限已設置為: " + limit);
            updateBalanceLabel();  // 更新顯示餘額
        } catch (NumberFormatException e) {
            showAlert("錯誤", "請輸入有效的上限金額！");
        }
    }

    @FXML
private void addRecord() {
    // 獲取用戶輸入
    String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
    String amountText = amountField.getText();
    String category = categoryCombo.getValue();
    String note = noteField.getText();

    // 驗證輸入
    if (date.isEmpty() || amountText.isEmpty() || category == null) {
        showAlert("錯誤", "請填寫所有必要欄位！");
        return;
    }

    // 確保金額為數字
    double amount = 0.0;
    try {
        amount = Math.abs(Double.parseDouble(amountText));
    } catch (NumberFormatException e) {
        showAlert("錯誤", "金額必須是數字！");
        return;
    }

    // 提醒餘額不足但繼續執行
    if (balance - amount < 0 && limit > 0) {
        showAlert("警告", "剩餘金額不足，但記錄仍將被新增。");
    }

    // 根據類別調整餘額
    if (category.equals("飲食") || category.equals("交通") || category.equals("娛樂") || category.equals("其他")) {
        balance -= amount;  // 支出類別扣除餘額
    } else if (category.equals("收入")) {
        balance += amount;  // 收入類別增加餘額
    }

    // 更新表格顯示
    Record newRecord = new Record(date, amountText, category, note);
    recordsTable.getItems().add(newRecord);

    // 更新餘額顯示
    updateBalanceLabel();

    // 清空欄位
    clearFields();
}


    @FXML
    private void clearFields() {
        datePicker.setValue(null);
        amountField.clear();
        categoryCombo.setValue(null);
        noteField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

   private void updateBalanceLabel() {
    // 更新顯示剩餘餘額的 Label
    if (limit > 0) {
        balanceLabel.setText("剩餘金額: " + balance + " / 設定上限: " + limit);
    } else {
        balanceLabel.setText("剩餘金額: " + balance);
    }

    // 設定顏色：紅色代表正餘額，綠色代表負餘額
    if (balance >= 0) {
        balanceLabel.setStyle("-fx-text-fill: red;"); // 正餘額紅色
    } else {
        balanceLabel.setStyle("-fx-text-fill: green;"); // 負餘額綠色
    }
}
@FXML
private void deleteRecord() {
    Record selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
    if (selectedRecord != null) {
        recordsTable.getItems().remove(selectedRecord);

        // 更新餘額
        double amount = Double.parseDouble(selectedRecord.getAmount());
        String category = selectedRecord.getCategory();

        if (category.equals("飲食") || category.equals("交通") || category.equals("娛樂") || category.equals("其他")){
            balance += amount; // 支出類別加回餘額
        } else if (category.equals("收入")) {
            balance -= amount; // 收入類別減回餘額
        }

        updateBalanceLabel();
    } else {
        showAlert("錯誤", "請選擇一筆記錄進行刪除！");
    }
}

@FXML
private void editRecord() {
    Record selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
    if (selectedRecord != null) {
        try {
            TextInputDialog dialog = new TextInputDialog(selectedRecord.getAmount());
            dialog.setTitle("修改記錄");
            dialog.setHeaderText("修改金額");
            dialog.setContentText("請輸入新的金額：");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String newAmountText = result.get();
                double newAmount = Double.parseDouble(newAmountText);
                double oldAmount = Double.parseDouble(selectedRecord.getAmount());
                String category = selectedRecord.getCategory();

                // 還原舊金額
                if (category.equals("飲食") || category.equals("交通") || category.equals("娛樂") || category.equals("其他")) {
                    balance += oldAmount;
                } else if (category.equals("收入")) {
                    balance -= oldAmount;
                }

                // 更新新金額
                if (category.equals("飲食") || category.equals("交通") || category.equals("娛樂") || category.equals("其他")) {
                    balance -= newAmount;
                } else if (category.equals("收入")) {
                    balance += newAmount;
                }
 
                // 更新表格中的記錄
                selectedRecord.amountProperty().set(newAmountText);

                // 更新餘額顯示
                updateBalanceLabel();
            }
        } catch (NumberFormatException e) {
            showAlert("錯誤", "請輸入有效的數字金額！");
        }
    } else {
        showAlert("錯誤", "請選擇一筆記錄進行修改！");
    }
}

}

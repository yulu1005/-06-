/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package G9_06;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class G9_06 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 加載 FXML 文件
        Parent root = FXMLLoader.load(getClass().getResource("G9.fxml"));

        // 設置主視窗
        primaryStage.setTitle("個人記帳系統");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // 啟動 JavaFX 應用程式
        launch(args);
    }
}

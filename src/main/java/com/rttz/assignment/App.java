package com.rttz.assignment;

import boundary.MainSharedState;
import dao.MainControlClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author rttz159
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        Application.setUserAgentStylesheet(App.class.getResource("css/theme.css").toString());
        scene = new Scene(loadFXML("dashboard"), 800, 600);
        MainSharedState.getInstance().addSelectedIdxListener((obs, oldValue, newValue) -> {
                if(MainSharedState.getInstance().isStudent() && oldValue != newValue){
                    if(newValue.equals(0)){
                        stage.setTitle("Intern Post Listing");
                    }else if(newValue.equals(1)){
                        stage.setTitle("Application History");
                    }else if(newValue.equals(2)){
                        stage.setTitle("Profile Management");
                    }
                }
            });
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setTitle("Log In Page");
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        MainControlClass.setCurrentUser(MainControlClass.getStudentsMap().get("bobo1231231"));//testing purpose
        MainSharedState.getInstance().setIsStudent(true);//testing purpose
        MainSharedState.getInstance().setIsLogined(true);//testing purpose
        launch();
    }

}

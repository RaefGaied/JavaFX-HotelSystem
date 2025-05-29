package org.example.demo;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            String imagePath = "/org/example/demo/res/loading.png";
            Image backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
            if (backgroundImage.isError()) {
                System.out.println("Erreur : Impossible de charger l'image de fond.");
                return;
            }

            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                            true,
                            true, true,
                            true)
            );

            Pane root = new Pane();
            root.setBackground(new Background(bgImage));

            ProgressIndicator progressIndicator = new ProgressIndicator();
            Label loadingLabel = new Label("Chargement en cours...");
            loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");


            StackPane centerPane = new StackPane();
            centerPane.getChildren().addAll(progressIndicator, loadingLabel);
            centerPane.setLayoutX(0);
            centerPane.setLayoutY(0);
            centerPane.setPrefSize(600, 400);

            StackPane.setAlignment(progressIndicator, javafx.geometry.Pos.CENTER);
            StackPane.setAlignment(loadingLabel, javafx.geometry.Pos.BOTTOM_CENTER);
            loadingLabel.setTranslateY(30);

            root.getChildren().add(centerPane);

            Scene loadingScene = new Scene(root, 600, 400);
            stage.setScene(loadingScene);
            stage.setTitle("Chargement...");
            stage.show();

            Task<Parent> loadMainSceneTask = new Task<>() {
                @Override
                protected Parent call() throws Exception {
                    Thread.sleep(3000);
                    return FXMLLoader.load(getClass().getResource("/org/example/demo/login.fxml"));
                }
            };
            loadMainSceneTask.setOnSucceeded(event -> {
                Scene mainScene = new Scene(loadMainSceneTask.getValue());
                stage.setScene(mainScene);
            });

            loadMainSceneTask.setOnFailed(event -> loadMainSceneTask.getException().printStackTrace());

            new Thread(loadMainSceneTask).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

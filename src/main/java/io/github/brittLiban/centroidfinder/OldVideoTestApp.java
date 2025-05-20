package io.github.brittLiban.centroidfinder;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.File;

public class OldVideoTestApp extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    // 1. load video from project root
   String videoPath = new File("realVideo.mp4").toURI().toString();

    Media media = new Media(videoPath);

    // 2. print metadata
    media.getMetadata().addListener(new MapChangeListener<String, Object>() {
      @Override
      public void onChanged(Change<? extends String, ? extends Object> change) {
        if (change.wasAdded()) {
          System.out.println(change.getKey() + " = " + change.getValueAdded());
        }
      }
    });

    // 3. setup player and view
    MediaPlayer player = new MediaPlayer(media);
    MediaView view = new MediaView(player);

    // 4. scene graph
    Group root = new Group(view);
    Scene scene = new Scene(root, 800, 450);
    stage.setScene(scene);
    stage.setTitle("JavaFX Media Test");
    stage.show();

    // 5. play
    player.play();

    // 6. after 1.5 seconds, snapshot then exit
    PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
    delay.setOnFinished(evt -> {
      try {
        WritableImage snap = view.snapshot(null, null);
        File out = new File("frame1.png");
        ImageIO.write(SwingFXUtils.fromFXImage(snap, null), "png", out);
        System.out.println("Saved snapshot to " + out.getAbsolutePath());
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        Platform.exit();
      }
    });
    delay.play();
  }

  public static void main(String[] args) {
    launch();
  }
}

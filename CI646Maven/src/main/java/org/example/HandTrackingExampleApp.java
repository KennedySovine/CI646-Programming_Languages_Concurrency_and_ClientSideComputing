package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.intelligence.gesturerecog.HandTrackingService;
import com.almasb.fxgl.net.ws.VideoInputDeviceInfo;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.List;

public class HandTrackingExampleApp extends GameApplication {

    private Canvas canvas;
    private GraphicsContext g;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.addEngineService(HandTrackingService.class);
    }

    @Override
    protected void initInput() {
        FXGL.onKeyDown(KeyCode.F, "Border Set", () -> {
            //System.out.println(FXGL.getService(HandTrackingService.class).getVideoDevices());
        });
    }

    @Override
    protected void initUI() {
        canvas = new Canvas(800, 600);
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLUE);

        FXGL.addUINode(canvas);

        //g.fillOval(100, 100, 15, 15);

        FXGL.getService(HandTrackingService.class)
                .addInputHandler(hand -> {
                    // code
                    System.out.println(hand);

                    Platform.runLater(() -> {
                        //g.clearRect(0, 0, 800, 600);

                        Point3D p = hand.getPoints().get(0);

                        g.fillOval((1 - p.getX()) * 1800 - 400, p.getY() * 1600 - 400, 10, 10);

//                        Stage stage = FXGL.getPrimaryStage();
//                        stage.setX((1 - point.getX()) * 1800 - 400);

//                        hand.getPoints().forEach(p -> {
//
//                            g.fillOval((1 - p.getX()) * 1800 - 400, p.getY() * 1600 - 400, 10, 10);
//                        });
                    });

                 });
        
        FXGL.getService(HandTrackingService.class)
                .readyProperty()
                .subscribe(() -> {
                   // code
                    System.out.println(FXGL.getService(HandTrackingService.class).getVideoDevices());
                    System.out.println("Ready");
                });

        FXGL.getService(HandTrackingService.class).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

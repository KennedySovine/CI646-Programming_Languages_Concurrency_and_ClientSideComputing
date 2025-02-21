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
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.List;

public class HandTrackingExampleApp extends GameApplication {

    private Canvas canvas;
    private GraphicsContext g;
    private int distanceMax = 0;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.addEngineService(HandTrackingService.class);
    }

    @Override
    protected void initInput() {
        FXGL.onKeyDown(KeyCode.F, "Border Set", () -> {
            //System.out.println(FXGL.getService(HandTrackingService.class).getVideoDevices());
            List<VideoInputDeviceInfo> devices = FXGL.getService(HandTrackingService.class).getVideoDevices();
            System.out.println(devices);
            FXGL.getService(HandTrackingService.class).setVideoDevice(devices.get(1));

        });
    }

    @Override
    protected void initUI() {
        canvas = new Canvas(800, 600);
        g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLUE);
        g.setFill(Color.BLUE);
        g.setStroke(Color.RED); // Set the color for the lines

        FXGL.addUINode(canvas);

        //g.fillOval(100, 100, 15, 15);

        FXGL.getService(HandTrackingService.class)
                .addInputHandler(hand -> {
                    // code
                    //System.out.println(hand);

                    Platform.runLater(() -> {
                        g.clearRect(0, 0, 800, 600);

                        List<Point3D> points = hand.getPoints();
                        for (int i = 0; i < points.size() - 1; i++) {

                            Point3D p = hand.getPoints().get(i);

                            //System.out.println(p);

                            g.fillOval((1 - p.getX()) * 900 - 300, p.getY() * 800 - 300, 10, 10);

                            Stage stage = FXGL.getPrimaryStage();
                            //stage.setX((1 - point.getX()) * 1800 - 400);

                            //2 - 5 is thumb
                            //if (i > 0){
                            //Thumb, index, middle, ring, pinky
                            if ((i > 0 &&  i < 5) || (i > 5 && i < 9) || (i > 9 && i < 13) || (i > 13 && i < 17) || (i > 17 && i < 20)) {
                                Point3D prev = hand.getPoints().get(i - 1);
                                System.out.println((p.getX() - prev.getX()) / (p.getY() - prev.getY()));
                                g.strokeLine((1 - prev.getX()) * 900 - 300, prev.getY() * 800 - 300,
                                        (1 - p.getX()) * 900 - 300, p.getY() * 800 - 300);
                                //hand.getPoints().forEach(o -> {

                                //g.fillOval((1 - o.getX()) * 900 - 300, o.getY() * 800 - 300, 20, 20);
                            }
                            //Other lines
                            if (i == 1){
                                Point3D prev = hand.getPoints().get(0);
                                g.strokeLine((1 - prev.getX()) * 900 - 300, prev.getY() * 800 - 300,
                                        (1 - p.getX()) * 900 - 300, p.getY() * 800 - 300);
                            }

                            /*int[] Knuckles = {5, 9, 13, 17};
                            for (int j = 0; j < Knuckles.length - 1; j++) {
                                if (i == Knuckles[j]){
                                    Point3D next = hand.getPoints().get(j+1);
                                    g.strokeLine((1 - next.getX()) * 900 - 300, next.getY() * 800 - 300,
                                            (1 - p.getX()) * 900 - 300, p.getY() * 800 - 300);
                                }
                            }*/
                        }
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

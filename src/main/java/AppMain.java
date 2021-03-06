import com.jfoenix.controls.JFXDecorator;
import data.AirQualityData;
import data.WeatherData;
import event.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import network.DataSource;
import network.OpenWeatherDataSource;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class AppMain extends Application {

    private static final String FXML_MAIN = "/fxml/main.fxml";

    private static final EventStream eventStream = EventStream.getInstance();
    private static final Logger logger = Logger.getLogger(AppMain.class);

    private Stage mainWindow;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;

        setupMainWindow();
        setupLogger();

        mainWindow.show();
    }

    private void setupMainWindow() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FXML_MAIN));

        Scene scene = new Scene(root, 320, 440);
        scene.setFill(null);

        mainWindow.setScene(scene);
        mainWindow.setWidth(320);
        mainWindow.setHeight(440);
        mainWindow.setResizable(false);
    }

    private void setupLogger() {
        logger.info("Setting logger up...");

        eventStream.getEvents().ofType(WeatherDataEvent.class)
            .map(weatherDataEvent ->
                weatherDataEvent + "\n\t" + weatherDataEvent.getWeatherData())
            .subscribe(logger::info);

        eventStream.getEvents().ofType(AirQualityDataEvent.class)
            .map(airQualityDataEvent ->
                airQualityDataEvent + "\n\t" + airQualityDataEvent.getAirQualityData())
            .subscribe(logger::info);

        eventStream.getEvents().ofType(WeatherDataSourceChangeEvent.class)
            .subscribe(logger::info);

        eventStream.getEvents().ofType(ErrorEvent.class)
            .subscribe(logger::error);
    }
}

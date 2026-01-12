import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;

import javax.swing.SwingUtilities;
import java.io.File;

/**
 * Simple JavaFX GIS Map Viewer using GeoTools
 *
 * Single-file demo that loads and displays a vector shapefile (e.g., world countries)
 * in a JavaFX window using GeoTools library.
 *
 * Features:
 * - Loads shapefile data (vector polygons)
 * - Applies basic styling (fill, outline)
 * - Displays interactive map with pan/zoom (mouse drag + zoom buttons)
 *
 * Requirements:
 * - JDK 17 or 21
 * - JavaFX SDK 21 → https://gluonhq.com/products/javafx/
 * - GeoTools JARs (gt-shapefile, gt-swing) → https://geotools.org/
 *
 * Shapefile Setup (do NOT upload to GitHub):
 * Download free countries shapefile from:
 * https://www.naturalearthdata.com/downloads/110m-cultural-vectors/
 * Unzip to project root/data/ne_110m_admin_0_countries/
 *
 * How to run in VS Code:
 * 1. Add GeoTools JARs to classpath in .vscode/settings.json
 * 2. Use same JavaFX VM args as previous projects
 * 3. Run with F5 or "Run Java"
 */
public class SimpleGeoToolsMap extends Application {

    private static final String SHAPEFILE_PATH = "data/ne_110m_admin_0_countries/ne_110m_admin_0_countries.shp";

    @Override
    public void start(Stage primaryStage) {
        SwingNode swingNode = new SwingNode();

        SwingUtilities.invokeLater(() -> {
            try {
                File shpFile = new File(SHAPEFILE_PATH);
                if (!shpFile.exists()) {
                    System.err.println("Shapefile not found! Please download from:");
                    System.err.println("https://www.naturalearthdata.com/downloads/110m-cultural-vectors/");
                    System.err.println("Unzip to: " + SHAPEFILE_PATH);
                    return;
                }

                FileDataStore store = FileDataStoreFinder.getDataStore(shpFile);
                SimpleFeatureSource featureSource = store.getFeatureSource();

                // Create basic style (tomato red fill, black outline for visibility)
                Style style = SLD.createSimpleStyle(featureSource.getSchema());
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0)
                    .setFill(SLD.fill(SLD.color("#FF6347"))); // Tomato red
                style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0)
                    .setStroke(SLD.stroke(SLD.color("#000000"), 1.0f));

                FeatureLayer layer = new FeatureLayer(featureSource, style);

                MapContent map = new MapContent();
                map.addLayer(layer);
                map.setTitle("World Countries – GeoTools + JavaFX Demo");

                JMapPane mapPane = new JMapPane(map);
                mapPane.setRenderer(new StreamingRenderer());
                mapPane.setDisplayArea(map.getMaxBounds());

                swingNode.setContent(mapPane);
                System.out.println("Map loaded successfully!");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error loading map: " + e.getMessage());
            }
        });

        // Simple zoom controls
        Button zoomIn = new Button("Zoom In");
        zoomIn.setOnAction(e -> {
            // Approximate zoom in (scale bounds)
            if (swingNode.getContent() instanceof JMapPane) {
                JMapPane pane = (JMapPane) swingNode.getContent();
                pane.setDisplayArea(pane.getDisplayArea().scale(0.8));
            }
        });

        Button zoomOut = new Button("Zoom Out");
        zoomOut.setOnAction(e -> {
            if (swingNode.getContent() instanceof JMapPane) {
                JMapPane pane = (JMapPane) swingNode.getContent();
                pane.setDisplayArea(pane.getDisplayArea().scale(1.25));
            }
        });

        HBox controls = new HBox(10, zoomIn, zoomOut);
        controls.setStyle("-fx-padding: 10; -fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(swingNode);
        root.setBottom(controls);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Simple JavaFX GIS Map Viewer – GeoTools Demo");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
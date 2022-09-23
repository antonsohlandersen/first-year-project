package bfst22.vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Desktop;

import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Enum.ThemeType;
import bfst22.vector.Parsing.RegexParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
    private Point2D lastMouse;
    private bfst22.vector.Data.Point2D currentRightClick;
    private bfst22.vector.Data.Point2D currentMousePos;
    private boolean routeStarted = false;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MapCanvas canvas;

    @FXML
    private StackPane stackpane;

    @FXML
    private AutoCompleteTextField addressTextField;

    @FXML
    private DialogPane dialogpane;

    @FXML
    private AutoCompleteTextField firstAddressTextField;

    @FXML
    private AutoCompleteTextField secondAddressTextField;

    @FXML
    private Button endRouteButton;

    @FXML
    private Button zoomPlus;

    @FXML
    private Button zoomMinus;

    @FXML
    private ProgressBar zoomLevel;

    @FXML
    private Text zoomText;

    private Model model;
    @FXML
    private Scene scene;


    @FXML
    private ContextMenu contextMenu;
    private MenuItem menuItem1;
    private MenuItem menuItem2;
    private MenuItem menuItem3;
    private MenuItem menuItem4;
    private MenuItem menuItem5;
    private MenuItem menuItem6;

    @FXML
    private Label showNearestHighwayLabel;

    @FXML
    private MenuItem carButton;

    @FXML
    private Button routeCarButton;

    @FXML
    private MenuItem bikeButton;

    @FXML
    private Button routeBikeButton;

    @FXML
    private MenuItem walkButton;

    @FXML
    private Button routeWalkButton;

    public void init(Model model, double width, double height, Scene scene) {
        canvas.init(model, width, height);
        this.model = model;
        this.scene = scene;
        carButton.setDisable(true);
        routeCarButton.setDisable(true);
        dialogpane.setVisible(false);
        setContextMenu();
        DebugMode();
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        var factor = 0;
        if (e.getDeltaY() > 0)
            factor = 40;
        else if (e.getDeltaY() < 0)
            factor = -40;
        if (zoomLevel.getProgress() >= 1.0 && factor > 0)
            return;
        else if (zoomLevel.getProgress() <= 0.01 && factor < 0)
            return;
        canvas.zoom(Math.pow(1.01, factor), e.getX(), e.getY());
        if (factor > 0)
            zoomLevel.setProgress(zoomLevel.getProgress() + 0.05);
        else if (factor < 0)
            zoomLevel.setProgress(zoomLevel.getProgress() - 0.05);
    }

    @FXML
    private void zoomPositive() {
        if (zoomLevel.getProgress() >= 1.0)
            return;
        canvas.zoom(Math.pow(1.01, 40.0), canvas.getWidth() / 2, canvas.getHeight() / 2);
        zoomLevel.setProgress(zoomLevel.getProgress() + 0.05);
    }

    @FXML
    private void zoomNegative() {
        if (zoomLevel.getProgress() <= 0.01)
            return;
        else
            canvas.zoom(Math.pow(1.01, -40.0), canvas.getWidth() / 2, canvas.getHeight() / 2);
        zoomLevel.setProgress(zoomLevel.getProgress() - 0.05);
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        boolean panned = false;
        if (e.getButton() == MouseButton.PRIMARY) {
            var dx = e.getX() - lastMouse.getX();
            var dy = e.getY() - lastMouse.getY();
            if(canvas.getScreen().getBottomLeft()[0] <= model.getMinlon() - 3 && dx > 0) {
                canvas.pan(-dx, dy);
            }
            else if(canvas.getScreen().getTopRight()[0] >= model.getMaxlon() + 3 && dx < 0) {
                canvas.pan(-dx, dy);
            }
            else {
                canvas.pan(dx, dy);
                panned = true;
            }
            if(canvas.getScreen().getBottomLeft()[1] <= model.getMinlat() && dy > 0) {
                canvas.pan(dx, -dy);
            }
            else if(canvas.getScreen().getTopRight()[1] >= model.getMaxlat() && dy < 0) {
                canvas.pan(dx, -dy);
            } else {
                if(!panned) canvas.pan(dx, dy);
            }
            lastMouse = new Point2D(e.getX(), e.getY());
        }
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            lastMouse = new Point2D(e.getX(), e.getY());
            contextMenu.hide();
        } else if (e.getButton() == MouseButton.SECONDARY) {
            if (!routeStarted) {
                menuItem4.setDisable(true);
                menuItem5.setDisable(true);
            } else {
                menuItem4.setDisable(false);
                menuItem5.setDisable(false);
            }
            if(addressTextField.getLength() < 1 || routeStarted) {
                menuItem6.setDisable(true);
            } else {
                menuItem6.setDisable(false);
            }
            try {
                javafx.geometry.Point2D temp = canvas.getTrans().inverseTransform(e.getX(), e.getY());
                currentRightClick = new bfst22.vector.Data.Point2D(temp.getX(), temp.getY());
            } catch (NonInvertibleTransformException e2) {
                e2.printStackTrace();
            }
            contextMenu.show(canvas, e.getX(), e.getY());
        }
    }

    @FXML
    private void onMouseMoved(MouseEvent e) {
        try {
            javafx.geometry.Point2D temp = canvas.getTrans().inverseTransform(e.getX(), e.getY());
            currentMousePos = new bfst22.vector.Data.Point2D(temp.getX(), temp.getY());
        } catch (NonInvertibleTransformException e1) {
            e1.printStackTrace();
        }
        String text = model.getPointTree().findResult(currentMousePos).getStreetName();
        if (text == null) {
            text = "Unknown road";
        }
        showNearestHighwayLabel.setText(text);
    }

    // Making the pop up menu for when you right click on the screen
    @FXML
    private void setContextMenu() {
        contextMenu = new ContextMenu();
        menuItem1 = new MenuItem("Mark as point of interest");
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                model.addPointOfInterest(new Icon(new Image(getClass().getResourceAsStream("location-2.png")),
                        (float) currentRightClick.getX(),
                        (float) currentRightClick.getY(), canvas.getTrans()));
                canvas.repaint();
            }
        });
        menuItem2 = new MenuItem("Delete nearest point of interest");
        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                float currentShortestDistance = Float.MAX_VALUE;
                int currentClosestPoiIndex = 0;
                List<Icon> currentPointsOfInterest = model.getPointsOfInterest();
                for (int i = 0; i < currentPointsOfInterest.size(); i++) {
                    bfst22.vector.Data.Point2D currentPoint = currentPointsOfInterest.get(i).getPoint();
                    float distanceToCurrentPoint = (float) Math
                            .sqrt(Math.pow(Math.abs(currentPoint.getX() - currentRightClick.getX()), 2)
                                    + Math.pow(Math.abs(currentPoint.getY() - currentRightClick.getY()), 2));
                    if (distanceToCurrentPoint < currentShortestDistance) {
                        currentShortestDistance = distanceToCurrentPoint;
                        currentClosestPoiIndex = i;
                    }
                }
                model.removePointOfInterest(currentClosestPoiIndex);
                canvas.repaint();
            }
        });
        menuItem3 = new MenuItem("Start route from this point");
        menuItem3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                OSMNode nearestPoint = model.getPointTree().findResult(currentRightClick);
                model.setRouteStartNode(new bfst22.vector.Data.Point2D(nearestPoint.getLon(), nearestPoint.getLat()),
                        nearestPoint);
                routeStarted = true;
                model.getIcons().add(new Icon(new Image(getClass().getResourceAsStream(model.getTransportation() + ".png")),
                        nearestPoint.getLon(), nearestPoint.getLat(), canvas.getTrans()));
                canvas.repaint();
            }
        });
        menuItem4 = new MenuItem("End route at this point");
        menuItem4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                model.getIcons().clear();
                OSMNode nearestPoint = model.getPointTree().findResult(currentRightClick);
                model.setRouteEndNode(nearestPoint);
                Point2D endResult = new Point2D(nearestPoint.getLon(), nearestPoint.getLat());
                model.getIcons().add(
                        new Icon(new Image(getClass().getResourceAsStream("location.png")), (float) endResult.getX(),
                                (float) endResult.getY(), canvas.getTrans()));
                model.getIcons().add(new Icon(new Image(getClass().getResourceAsStream(model.getTransportation() + ".png")),
                        (float) model.getRouteStartPoint().getX(),
                        (float) model.getRouteStartPoint().getY(), canvas.getTrans()));
                canvas.repaint();
            }
        });
        menuItem5 = new MenuItem("Clear route");
        menuItem5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                model.clearRoute();
                addressTextField.clear();
                firstAddressTextField.clear();
                secondAddressTextField.clear();
                routeStarted = false;
                canvas.repaint();
            }
        });
        menuItem6 = new MenuItem("Clear location");
        menuItem6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                model.getIcons().clear();
                addressTextField.clear();
                canvas.repaint();
            }
        });
        contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4, menuItem5, menuItem6);
    }

    @FXML
    private void onNormalTheme() {
        canvas.setTheme(ThemeType.NORMAL);
    }

    @FXML
    private void onBlackAndWhiteTheme() {
        canvas.setTheme(ThemeType.NOIR);
    }

    @FXML
    private void onColorblindTheme() {
        canvas.setTheme(ThemeType.INVERT);
    }

    @FXML
    private void onSickoMode() {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(new Stage());
            VBox dialogVbox = new VBox(10);
            dialogVbox.getChildren().add(new Text("WARNING: This theme may potentially \n trigger seizures for people with photosensitive epilepsy. \n Viewer discretion is advised."));
            dialogVbox.setAlignment(Pos.CENTER);
            Button acceptButton = new Button("Accept");
            acceptButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    canvas.setTheme(ThemeType.RANDOM);
                    dialog.close();
                }
            });
            Button declineButton = new Button("Decline");
            declineButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    dialog.close();
                }
            });
            dialogVbox.getChildren().add(acceptButton);
            dialogVbox.getChildren().add(declineButton);
            Scene dialogScene = new Scene(dialogVbox, 400, 150);
            dialog.setScene(dialogScene);
            dialog.show();
    }

    @FXML
    private void suggestAddress(KeyEvent e) {
        if (addressTextField.getText().isBlank()) {
            return;
        }
        getSuggestedAddresses(addressTextField);
    }

    @FXML
    private void suggestStartAddress(KeyEvent e) {
        checkTextFields(firstAddressTextField);
        getSuggestedAddresses(firstAddressTextField);
    }

    @FXML
    private void suggestEndAddress(KeyEvent e) {
        checkTextFields(secondAddressTextField);
        getSuggestedAddresses(secondAddressTextField);
    }

    private void checkTextFields(AutoCompleteTextField field) {
        if (field.getText().isBlank()) {
            return;
        }
        if (!addressTextField.getText().isBlank()) {
            addressTextField.clear();
        }
    }

    private void getSuggestedAddresses(AutoCompleteTextField field) {
        String[] parsedAddress = RegexParser.getAll(field.getText());
        model.getTernarySearchTree().searchSuggestions(parsedAddress);
        if(field.getText().length()>2 && model.getTernarySearchTree().getSuggestions().size() == 0){
            ArrayList<String[]> spellCheckSuggestions = new ArrayList<>();
            for(String s : spellCheck(field.getText())){
                spellCheckSuggestions.add(RegexParser.getAll(s));
            }
            for(String[] s : spellCheckSuggestions){
                model.getTernarySearchTree().searchSuggestions(s);
                if(model.getTernarySearchTree().getSuggestions().size() > 0){
                    break;
                }
            }
            
        }
        field.addSuggestions(model.getTernarySearchTree().getSuggestions(), this);
        model.getTernarySearchTree().clearSuggestions();
    }

    @FXML
    private void searchedAddress() {
        searchAddress();
    }

    // The searchAddress method searches for a single address
    // and pans to its location
    public void searchAddress() {
        model.getIcons().clear();
        routeStarted = false;
        if (addressTextField.getText().isBlank()) {
            return;
        }
        String[] parsedAddress = RegexParser.getAll(addressTextField.getText());
        float[] result = model.getTernarySearchTree().getAddressCoordinates(parsedAddress);
        if (result == null) {
            return;
        }
        Point2D temp = canvas.getTrans().transform(result[0], result[1]);
        bfst22.vector.Data.Point2D target = new bfst22.vector.Data.Point2D(temp.getX(), temp.getY());
        Point2D current = new Point2D(canvas.getWidth() / 2, canvas.getHeight() / 2);
        var dx = -(target.getX() - current.getX());
        var dy = -(target.getY() - current.getY());
        model.clearRoute();
        model.getIcons().add(new Icon(new Image(getClass().getResourceAsStream("location.png")), (float) result[0],
                (float) result[1], canvas.getTrans()));
        canvas.pan(dx, dy);
    }

    // The searchedAddresses method is used to compute DijkstraSP between two
    // addresses (through the model) and draw it on the canvas
    @FXML
    private void searchedAddresses(ActionEvent e) {
        routeStarted = true;
        model.getIcons().clear();
        if (firstAddressTextField.getText().isBlank() || secondAddressTextField.getText().isBlank())
            return;
        String[] startAddress = RegexParser.getAll(firstAddressTextField.getText());
        float[] tempResult = model.getTernarySearchTree().getAddressCoordinates(startAddress);
        bfst22.vector.Data.Point2D startResult = new bfst22.vector.Data.Point2D(tempResult[0], tempResult[1]);
        model.setRouteStartNode(startResult, model.getPointTree().findResult(startResult));
        String[] endAddress = RegexParser.getAll(secondAddressTextField.getText());
        tempResult = model.getTernarySearchTree().getAddressCoordinates(endAddress);
        bfst22.vector.Data.Point2D endResult = new bfst22.vector.Data.Point2D(tempResult[0], tempResult[1]);
        model.setRouteEndNode(model.getPointTree().findResult(endResult));
        model.getIcons().add(new Icon(new Image(getClass().getResourceAsStream(model.getTransportation() + ".png")),
                (float) model.getRouteStartPoint().getX(),
                (float) model.getRouteStartPoint().getY(), canvas.getTrans()));
        model.getIcons().add(new Icon(new Image(getClass().getResourceAsStream("location.png")), (float) endResult.getX(),
                (float) endResult.getY(), canvas.getTrans()));
        canvas.repaint();
    }

    @FXML
    private void onPlanRoute() {
        if (dialogpane.isVisible())
            dialogpane.setVisible(false);
        else
            dialogpane.setVisible(true);
    }

    @FXML
    private void onCar() {
        setDijkstraTransportation("car");
    }

    public boolean getRouteStarted(){
        return routeStarted;
    }

    @FXML
    private void onBike() {
        setDijkstraTransportation("bike");
    }

    @FXML
    private void onWalk() {
        setDijkstraTransportation("walk");

    }

    @FXML
    private void loadFile() throws Exception {
        FileChooser FC = new FileChooser();
        File selectedFile = FC.showOpenDialog(null);
        if (selectedFile != null) {
            new View(new Model(selectedFile.toString()), new Stage());
        } else {
            System.out.println("file is not valid");
        }
    }

    @FXML
    private void loadDirections() {
        if(!model.hasRouteEndNode()) {
            final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(new Stage());
                VBox dialogVbox = new VBox(10);
                dialogVbox.getChildren().add(new Text("Please start a route"));
                dialogVbox.setAlignment(Pos.CENTER);
                Button okButton = new Button("Ok");
                okButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        dialog.close();
                    }
                });
                dialogVbox.getChildren().add(okButton);
                Scene dialogScene = new Scene(dialogVbox, 300, 100);
                dialog.setScene(dialogScene);
                dialog.show();
                return;
        }
        File txtFile = new File("data/directions.txt");
        if(txtFile.exists()) {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(txtFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } else {            
            System.out.println("AWT Desktop not supported");
            }
        } else {
            System.out.println("File does not exist");
        }
    }

    private void DebugMode() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.R) {
                if (!canvas.getRTreeDebugMode()) {
                    canvas.setRTreeDebugMode(true);
                } else {
                    canvas.setRTreeDebugMode(false);
                }
            } else if (e.getCode() == KeyCode.K) {
                if (!canvas.getKdTreeDebugMode()) {
                    canvas.setKdTreeDebugMode(true);
                } else {
                    canvas.setKdTreeDebugMode(false);
                }
            } else if (e.getCode() == KeyCode.A) {
                if (!canvas.getAStarDebugMode()) {
                    model.setAStarActivated(true);
                    if(model.hasRouteEndNode()) {
                        model.setRouteEndNode(model.getRouteEndNode());
                    }
                    canvas.setAStarDebugMode(true);
                } else {
                    model.setAStarActivated(false);
                    model.getAllVisitedEdges().clear();
                    if(model.hasRouteEndNode()) {
                        model.setRouteEndNode(model.getRouteEndNode());
                    }
                    canvas.setAStarDebugMode(false);
                }
            } else if (e.getCode() == KeyCode.V) {
                if(!canvas.getVisitedEdgesMode()) {
                    model.setAllVisitedEdges(true);
                    if(model.hasRouteEndNode()) {
                        model.setRouteEndNode(model.getRouteEndNode());
                    }
                    canvas.setVisitedEdgesMode(true);
                }
                else {
                    model.setAllVisitedEdges(false);
                    if(model.hasRouteEndNode()) {
                        model.setRouteEndNode(model.getRouteEndNode());
                    }
                    canvas.setVisitedEdgesMode(false);
                }
            }
        });
    }

    public void setDijkstraTransportation(String transportation) {
        model.setTransportation(transportation);
        if (transportation.equals("car")) {
            carButton.setDisable(true);
            routeCarButton.setDisable(true);
            bikeButton.setDisable(false);
            routeBikeButton.setDisable(false);
            walkButton.setDisable(false);
            routeWalkButton.setDisable(false);
        } else if (transportation.equals("bike")) {
            bikeButton.setDisable(true);
            routeBikeButton.setDisable(true);
            carButton.setDisable(false);
            routeCarButton.setDisable(false);
            walkButton.setDisable(false);
            routeWalkButton.setDisable(false);
        } else if (transportation.equals("walk")) {
            walkButton.setDisable(true);
            routeWalkButton.setDisable(true);
            carButton.setDisable(false);
            routeCarButton.setDisable(false);
            bikeButton.setDisable(false);
            routeBikeButton.setDisable(false);
        }
        if (model.hasRouteStartNode() && model.hasRouteEndNode()) {
            model.getIcons().clear();
            model.setTransportation(transportation);
            model.setRouteEndNode(model.getRouteEndNode());
            Point2D endResult = new Point2D(model.getRouteEndNode().getLon(), model.getRouteEndNode().getLat());
            model.getIcons().add(new Icon(new Image(getClass().getResourceAsStream(model.getTransportation() + ".png")),
                    (float) model.getRouteStartPoint().getX(),
                    (float) model.getRouteStartPoint().getY(), canvas.getTrans()));
            model.getIcons()
                    .add(new Icon(new Image(getClass().getResourceAsStream("location.png")), (float) endResult.getX(),
                            (float) endResult.getY(), canvas.getTrans()));
            canvas.repaint();
        }
    }

    public Point2D getLastMouse(){
        return lastMouse;
    }

    // This method is for testing exclusively
    public Point2D getCurrentRightClick() {
        Point2D currentRightClickPoint = new Point2D(currentRightClick.getX(), currentRightClick.getY());
        return currentRightClickPoint;
    }

    // This method is for testing exclusively
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    // This method is for testing exclusively
    public AutoCompleteTextField getTextField() {
        return addressTextField;
    }

    // This method is for testing exclusively
    public MapCanvas getCanvas() {
        return canvas;
    }

    // This method is for testing exclusively
    public void writeToTextField(String text) {
        addressTextField.appendText(text);
    }
    
    public ArrayList<String> spellCheck(String s) {
        ArrayList<String> searchResults = new ArrayList<>();
        for (int i = 1; i < s.length() + 1; i++) {
            for (char character = 'a'; character <= 'z'; character++) {
                StringBuilder sb = new StringBuilder(s);
                sb.insert(i, character);
                searchResults.add(sb.toString());
            }
        }

        for (int i = 1; i < s.length(); i++) {
            for (char character = 'a'; character <= 'z'; character++) {
                StringBuilder sb = new StringBuilder(s);
                sb.setCharAt(i, character);
                searchResults.add(sb.toString());
                sb = new StringBuilder(s);
                sb.deleteCharAt(i);
                searchResults.add(sb.toString());
            }
        }
        return searchResults;
    }
}
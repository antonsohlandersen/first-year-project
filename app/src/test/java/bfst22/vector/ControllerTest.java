package bfst22.vector;

import bfst22.vector.Data.OSMData.OSMNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.geometry.Point2D;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.SortedSet;

import org.testfx.framework.junit5.*;

public class ControllerTest extends ApplicationTest{
    Model model;
    Controller controller;
    MapCanvas canvas;

    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Here, we initialize the model, controller and mapcanvas that we use for the tests
        //Initialization is done in the same way as we do when we run the program normally
        model = new Model("data/Femoo.osm");

        var loader = new FXMLLoader(View.class.getResource("View.fxml"));
        primaryStage.setScene(loader.load());
        primaryStage.getScene().getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        controller = loader.getController();
        controller.init(model, primaryStage.getWidth() + 300, primaryStage.getHeight() + 35, primaryStage.getScene());

        canvas = new MapCanvas();
        canvas.init(model, primaryStage.getWidth() + 300, primaryStage.getHeight() + 35);
        stage = primaryStage;
    }

    @Test
    public void onLeftMouseButtonPressedTest() throws Exception{
        //Here, we get the onMousePressed() method from Controller. We have to do it this way because it is private
        Method method = Controller.class.getDeclaredMethod("onMousePressed", MouseEvent.class);
        method.setAccessible(true);

        //Here, we call the onMousePressed() method with the point: (0,0).
        //Then we check that the click is stored correctly in the controller.
        method.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        Point2D lastMouse = controller.getLastMouse();
        assertEquals(0, lastMouse.getX());
        assertEquals(0, lastMouse.getY());
        
        //Here, we do the same thing, but with negative values to check that this also works as it should
        method.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, -100, -100, -100, -100, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        lastMouse = controller.getLastMouse();
        assertEquals(-100, lastMouse.getX());
        assertEquals(-100, lastMouse.getY());
    }

    @Test
    public void onRightMouseButtonPressedTest() throws Exception{
        Method method = Controller.class.getDeclaredMethod("onMousePressed", MouseEvent.class);
        method.setAccessible(true);

        //Here, we simulate a right-click, and then check that the coordinates for the click is saved correctly
        method.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 100, 100, 100, 100, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        Point2D currentRightClick = controller.getCurrentRightClick();
        Point2D expectedCurrentRightClick = canvas.getTrans().inverseTransform(new Point2D(100, 100));
        assertEquals(expectedCurrentRightClick.getX(), currentRightClick.getX());
        assertEquals(expectedCurrentRightClick.getY(), currentRightClick.getY());

        //We do the same again, but with negative coordinates
        method.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, -50, -50, -50, -50, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        currentRightClick = controller.getCurrentRightClick();
        expectedCurrentRightClick = canvas.getTrans().inverseTransform(new Point2D(-50, -50));
        assertEquals(expectedCurrentRightClick.getX(), currentRightClick.getX());
        assertEquals(expectedCurrentRightClick.getY(), currentRightClick.getY());
    }

    @Test
    public void contextMenuItem1Test() throws Exception{
        Method mouseClickMethod = Controller.class.getDeclaredMethod("onMousePressed", MouseEvent.class);
        mouseClickMethod.setAccessible(true);

        //We check that there are currently no point of interest in the list in Model
        assertEquals(0, model.getPointsOfInterest().size());

        //We simulate a right-click and press MenuItem1
        ContextMenu menu = controller.getContextMenu();
        mouseClickMethod.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 100, 100, 100, 100, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        menu.getItems().get(0).fire();

        //Then we check that the added point of interest has the correct coordinates
        Point2D expectedCurrentRightClick = canvas.getTrans().inverseTransform(new Point2D(100, 100));
        assertEquals((float) expectedCurrentRightClick.getX(), (float) model.getPointsOfInterest().get(0).getPoint().getX());
        assertEquals((float) expectedCurrentRightClick.getY(), (float) model.getPointsOfInterest().get(0).getPoint().getY());

        //Then we check that the size of the list in model has changed to 1
        assertEquals(1, model.getPointsOfInterest().size());
    }

    @Test
    public void contextMenuItem2Test() throws Exception{
        Method mouseClickMethod = Controller.class.getDeclaredMethod("onMousePressed", MouseEvent.class);
        mouseClickMethod.setAccessible(true);

        ContextMenu menu = controller.getContextMenu();

        //We check that the points of interest list in Model is empty
        assertEquals(model.getPointsOfInterest().size(), 0);

        //We simulate a right-click and then add a point of interest
        mouseClickMethod.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 100, 100, 100, 100, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        menu.getItems().get(0).fire();

        Point2D currentRightClick = controller.getCurrentRightClick();
        assertEquals((float) model.getPointsOfInterest().get(0).getPoint().getX(), (float) currentRightClick.getX());
        assertEquals((float) model.getPointsOfInterest().get(0).getPoint().getY(), (float) currentRightClick.getY());

        //We add another point of interest
        mouseClickMethod.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 200, -200, 200, -200, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        menu.getItems().get(0).fire();

        currentRightClick = controller.getCurrentRightClick();
        assertEquals((float) model.getPointsOfInterest().get(1).getPoint().getX(), (float) currentRightClick.getX());
        assertEquals((float) model.getPointsOfInterest().get(1).getPoint().getY(), (float) currentRightClick.getY());

        assertEquals(model.getPointsOfInterest().size(), 2);

        //We simulate a right-click and remove the closest point of interest with MenuItem2
        mouseClickMethod.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 100, 0, 100, 0, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        menu.getItems().get(1).fire();

        assertEquals(model.getPointsOfInterest().size(), 1);

        //We check that the point left in the list of points of interests is the correct one
        assertEquals((float) model.getPointsOfInterest().get(0).getPoint().getX(), (float) currentRightClick.getX());
        assertEquals((float) model.getPointsOfInterest().get(0).getPoint().getY(), (float) currentRightClick.getY());
    }

    @Test
    public void contextMenuItem3And4Test() throws Exception{
        //ContextMenuItem3 really only finds the closest point to where the user clicks.
        //This functionality should already be covered in our KdTreeTest, so the test for this item
        //will not be very thorough.
        Method mouseClickMethod = Controller.class.getDeclaredMethod("onMousePressed", MouseEvent.class);
        mouseClickMethod.setAccessible(true);

        //We simulate a right-click and save it in currentRightClick.
        ContextMenu menu = controller.getContextMenu();
        mouseClickMethod.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 8, -55, 8, -55, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        Point2D currentRightClick = canvas.getTrans().inverseTransform(new Point2D(8, -55));

        //We check that we do not have a start-point, and then check that we find the correct one afterwards
        assertEquals(null, model.getRouteStartPoint());
        menu.getItems().get(2).fire();
        assertEquals(model.getPointTree().findResult(new bfst22.vector.Data.Point2D(currentRightClick.getX(), currentRightClick.getY())), model.getRouteStartNode());

        //We simulate another right-click and then end our route with menuItem4.
        mouseClickMethod.invoke(controller, new MouseEvent(MouseEvent.MOUSE_CLICKED, 8.5, -55.5, 8.5, -55.5, MouseButton.SECONDARY, 1, true, true, true, true, true, true, true, true, true, true, null));
        currentRightClick = canvas.getTrans().inverseTransform(new Point2D(8.5, -55.5));
        OSMNode currentClosestNode = model.getPointTree().findResult(new bfst22.vector.Data.Point2D(currentRightClick.getX(), currentRightClick.getY()));
    
        //We check that endResult is null in model, and that it is then initialized when we press menuItem4
        assertEquals(null, model.getRouteEndNode());
        menu.getItems().get(3).fire();
        assertEquals((float) currentClosestNode.getLon(), (float) model.getRouteEndNode().getLon());
        assertEquals((float) currentClosestNode.getLat(), (float) model.getRouteEndNode().getLat());
    }

    @Test
    public void suggestAddressTest() throws Exception{
        //Getting the textField for the address from Model
        AutoCompleteTextField textField = controller.getTextField();
        try{
            //We call the event attached to the textField, which is suggestAddress(KeyEvent e). Then we write "E" in the textField and make another call to the attached function.
            //When we do this, an exception is thrown because the program tries to show the suggestions in our window, but no such window exists when we are testing.
            textField.fireEvent(new KeyEvent(textField, textField, KeyEvent.KEY_TYPED, "", "", KeyCode.UNDEFINED, false, false, false, false));
            controller.writeToTextField("E");
            textField.getOnKeyTyped().handle(new KeyEvent(textField, textField, KeyEvent.KEY_TYPED, "", "", KeyCode.UNDEFINED, false, false, false, false));
        } catch(Exception e){
            System.out.println("Expected error occurred: This happens because the textfield is unable to show its suggestions, because we do not have a view open in our tests");
        }
        SortedSet<String> suggestionsFromController = textField.getSuggestions();

        model.getTernarySearchTree().clearSuggestions();
        //Now we get try to get all suggestions when we type "e" in the textField for adresses. These should match the ones we got from the controller
        String[] searchValue = new String[]{"e", "", "", ""};
        ArrayList<String> searchResults = new ArrayList<>();
        model.getTernarySearchTree().searchSuggestions(searchValue);
        searchResults.addAll(model.getTernarySearchTree().getSuggestions());

        //Now we check that the suggestions we got directly from the tree and the ones we got from our controller are the same
        int count = 0;
        for(String s : suggestionsFromController){
            assertEquals(searchResults.get(count), s);
            count++;
        }

        //Now we do all of this again, but with an input where we expect the addresses to not match.
        model.getTernarySearchTree().clearSuggestions();
        searchValue = new String[]{"m", "", "", ""};
        model.getTernarySearchTree().searchSuggestions(searchValue);
        searchResults.clear();
        searchResults.addAll(model.getTernarySearchTree().getSuggestions());

        //Now we check that the suggestions we got directly from the tree and the ones we got from our controller are the same
        count = 0;
        for(String s : suggestionsFromController){
            if(count < searchResults.size()){
                assertNotEquals(searchResults.get(count), s);
            }
            count++;
        }
    }

    @Test
    public void searchedAddressTest() throws Exception{
        //I get the method: searchedAddress
        Method searchedAddressesMethod = Controller.class.getDeclaredMethod("searchedAddress");
        searchedAddressesMethod.setAccessible(true);

        //I call the retrieved method three times
        //First time with an empty textField, second time with text in the textField that should not have any matching address for the ternary tree to find
        //Third time with an address the tree should be able to find an address for
        searchedAddressesMethod.invoke(controller);
        controller.writeToTextField("NoSuggestionsForThis");
        searchedAddressesMethod.invoke(controller);
        controller.getTextField().clear();
        controller.writeToTextField("Engmosevej 1 4945");
        searchedAddressesMethod.invoke(controller);

        //I check that the points made by the tree based on the address typed into the textField matches the actual points of the address
        float actualX = (float)model.getIcons().get(0).getPoint().getX();
        float actualY = (float)model.getIcons().get(0).getPoint().getY();
        assertEquals((float)6.471991, actualX);
        assertEquals((float)-54.968685, actualY);
    }
}

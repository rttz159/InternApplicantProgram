package boundary;

import control.joblistingstudent.ApplicationSharedState;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.rttz.assignment.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Raymond
 */
public class MainStudentDashboardBoundary {
    
    @FXML
    private JFXDrawersStack drawerStack;
    
    @FXML
    private JFXHamburger hamburger;
    
    @FXML
    private VBox mainContent;
    
    @FXML
    private VBox overlayVbox;
    
    private JFXDrawer drawer;
    
    private boolean opened;
    
    @FXML
    public void initialize() {
        try {
            opened = false;
            HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
            
            transition.setInterpolator(Interpolator.EASE_BOTH);
            for (Node node : hamburger.getChildren()) {
                if (node instanceof StackPane) {
                    ((StackPane) node).setBackground(new Background(
                            new BackgroundFill(Color.rgb(244, 184, 24), new CornerRadii(5), Insets.EMPTY)
                    ));
                }
            }
            overlayVbox.setOpacity(0);
            overlayVbox.setMouseTransparent(true);
            
            hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (drawer.isClosed()) {
                    drawerStack.toggle(drawer);
                    drawerStack.setMouseTransparent(false);
                    overlayVbox.setOpacity(.15);
                    overlayVbox.setMouseTransparent(false);
                    transition.setRate(2);
                    opened = true;
                } else {
                    drawer.close();
                    drawerStack.setMouseTransparent(true);
                    overlayVbox.setOpacity(0);
                    overlayVbox.setMouseTransparent(true);
                    transition.setRate(-2);
                    opened = false;
                }
                transition.play();
            });
            
            overlayVbox.addEventHandler(MouseEvent.MOUSE_CLICKED, eh -> {
                if (opened) {
                    drawer.close();
                    drawerStack.setMouseTransparent(true);
                    overlayVbox.setOpacity(0);
                    overlayVbox.setMouseTransparent(true);
                    transition.setRate(-2);
                    transition.play();
                }
            });
            
            drawer = new JFXDrawer();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("drawerMenuStudent.fxml"));
            VBox drawerContent = loader.load();
            drawer.setSidePane(drawerContent);
            drawer.setDefaultDrawerSize(275);
            drawer.setOverLayVisible(true);
            drawer.setResizableOnDrag(false);
            drawer.close();
            drawerStack.setMouseTransparent(true);
            
            mainContent.getChildren().add(FXMLLoader.load(App.class.getResource("JobListingStudent/InternJobSearch.fxml")));
            
            drawer.setOnDrawerClosed(event -> drawer.setOverLayVisible(false));
            
            ApplicationSharedState.getInstance().addAppliedListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    MainSharedState.getInstance().setSelectedIdx(1);
                }
            });
            
            MainSharedState.getInstance().addSelectedIdxListener((obs, oldValue, newValue) -> {
                if(MainSharedState.getInstance().isStudent() && oldValue != newValue){
                    if(newValue.equals(0)){
                        changeMainContent("JobListingStudent/InternJobSearch");
                    }else if(newValue.equals(1)){
                        changeMainContent("studentapplication/StudentApplicationHistory");
                    }else if(newValue.equals(2)){
                        changeMainContent("studentprofile/StudentProfileManagement");
                    }
                }
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void changeMainContent(String fxmlFileName) {
        mainContent.getChildren().clear();
        try {
            mainContent.getChildren().add(FXMLLoader.load(App.class.getResource(fxmlFileName + ".fxml")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}

package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.MainViewController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

// This class contains all the other views in the app
// and handles switching between them
public class MainView extends BorderPane {

  private MainViewController aMainViewController;
  private BorderPane aContentArea;

  public MainView() {
    buildView();
  }

  public void setController(MainViewController pMainViewController) {
    aMainViewController = pMainViewController;
  }

  public void setContent(Node content) {
    aContentArea.setCenter(content);
  }

  protected void buildView() {
    VBox topArea = new VBox(5);
    topArea.getStyleClass().add("header");

    Label titleLabel = new Label("SmartHome App by Dana and Keanu");
    titleLabel.getStyleClass().add("title");
    topArea.getChildren().add(titleLabel);

    Label tagLine = new Label("Welcome to the Buckley's of IOT Apps. It looks bad, but it works.");
    tagLine.getStyleClass().add("tag-line");
    topArea.getChildren().add(tagLine);

    setTop(topArea);

    // we use this contentPane just so we can add style (i.e. padding)
    aContentArea = new BorderPane();
    aContentArea.getStyleClass().add("body");
    setCenter(aContentArea);
  }
}

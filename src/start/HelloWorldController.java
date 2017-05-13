package start;

import com.yworks.yfiles.view.GraphControl;
import com.yworks.yfiles.view.input.GraphEditorInputMode;

/**
 * Created by pierrelouislacorte on 13/05/2017.
 */
public class HelloWorldController {

    public GraphControl graphControl;

    public void initialize() {
        // Called by the JavaFX framework on loading.
        graphControl.setInputMode(new GraphEditorInputMode());
    }

    public void onLoaded() {
        // Called by our application right after stage is loaded.
    }
}

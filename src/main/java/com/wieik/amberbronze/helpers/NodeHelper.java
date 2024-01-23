package com.wieik.amberbronze.helpers;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;

import java.util.HashMap;
import java.util.Map;

/**
 * The NodeHelper class provides utility methods for managing JavaFX nodes.
 */
public class NodeHelper {
    /**
     * Hides the specified node by setting its managed and visible properties to false.
     *
     * @param node the node to be hidden
     */
    public static void unmount(Node node) {
        node.setManaged(false);
        node.setVisible(false);
    }

    /**
     * Shows the specified node by setting its managed and visible properties to true.
     *
     * @param node the node to be shown
     */
    public static void mount(Node node) {
        node.setManaged(true);
        node.setVisible(true);
    }
}

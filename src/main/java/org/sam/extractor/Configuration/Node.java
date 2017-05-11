package org.sam.extractor.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by RShastri on 5/8/2017.
 */
public class Node {
    private final String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return open == node.open &&
                Objects.equals(name, node.name) &&
                Objects.equals(action, node.action) &&
                Objects.equals(icon, node.icon) &&
                Objects.equals(children, node.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, action, icon, children, open);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Node{");
        sb.append("name='").append(name).append('\'');
        sb.append(", action='").append(action).append('\'');
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", children=").append(children);
        sb.append(", open=").append(open);
        sb.append('}');
        return sb.toString();
    }

    public String getAction() {
        return action;
    }

    private final String action = "extract";

    public String getIcon() {
        return icon;
    }

    public boolean isOpen() {
        return open;
    }

    private final String icon;
    private final List<Node> children = new ArrayList<>();
    private final boolean open;


    public Node(String name, List<Node> children, String icon) {
        this.name = name;
        this.icon = icon;
        this.children.addAll(children);
        this.open = false;
    }

    public Node(String name) {
        this.name = name;
        this.open = true;
        this.icon = null;
    }

    public String getName() {
        return name;
    }

    public List<Node> getChildren() {
        return children;
    }

}

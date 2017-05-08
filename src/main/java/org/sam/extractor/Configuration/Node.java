package org.sam.extractor.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RShastri on 5/8/2017.
 */
public class Node {
    private final String name;
    private final List<Node> children = new ArrayList<>();


    public Node(String name, List<Node> children) {
        this.name = name;
        this.children.addAll(children);
    }

    public String getName() {
        return name;
    }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        return children != null ? children.equals(node.children) : node.children == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", children=" + children +
                '}';
    }
}

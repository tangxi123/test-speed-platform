package org.tangxi.testplatform.model;

import java.util.List;

public class ModuleTree {
    private int id;

    private String name;

    private int parentId;

    private List<ModuleTree> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModuleTree> getChildren() {
        return children;
    }

    public void setChildren(List<ModuleTree> children) {
        this.children = children;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}

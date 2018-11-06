package io.sdb.common.entity;

import io.sdb.model.Area;
import lombok.Data;

@Data
public class AreaNode extends Node<Area> {
    String name;
    String fullName;
    Integer grade;
    String treePath;

    public AreaNode(Area area) {
        super(area.getId(), area.getParentId());
        this.name = area.getName();
        this.fullName = area.getFullName();
        this.grade = area.getGrade();
        this.treePath = area.getTreePath();
    }
}

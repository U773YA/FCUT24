package org.example.model;

import org.example.enums.Position;
import org.example.enums.Role;

import java.util.Objects;

public class PositionRole {
    private final Position position;
    private final Role role;

    public PositionRole(Position position, Role role) {
        this.position = position;
        this.role = role;
    }

    public Position getPosition() {
        return position;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionRole that)) return false;
        return getPosition() == that.getPosition() && getRole() == that.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getRole());
    }
}

package hack;

import hack.GridLock.Paintable;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import javax.lang.model.type.IntersectionType;
import javax.naming.spi.DirStateFactory.Result;

public class Intersection implements Paintable {
    public static final Intersection[][] INTERSECTIONS = new Intersection[GridLock.GRID_WIDTH][GridLock.GRID_HEIGHT];

    private static int intersection_index_x = 0, intersection_index_y = 0;

    private final LinkedList<Car> waiting = new LinkedList<>();
    private final IntersectionType type;
    private final Point location, index;  // NOTE: this is the location of the CENTER of the intersection
    private Road north_road = null, south_road = null, east_road = null, west_road = null;

    public Intersection(IntersectionType type) {
        this.type = type;

        index = new Point(intersection_index_x, intersection_index_y);
        location =
                new Point((intersection_index_x + 1) * GridLock.WINDOW_WIDTH / (GridLock.GRID_WIDTH + 1), (intersection_index_y + 1) * GridLock.WINDOW_HEIGHT
                        / (GridLock.GRID_HEIGHT + 1));

        // add the new Intersection to the array of Intersections
        INTERSECTIONS[intersection_index_x][intersection_index_y] = this;

        // increment intersection indices
        if (intersection_index_x == GridLock.GRID_WIDTH - 1) {
            intersection_index_y++;
            intersection_index_x = 0;
        } else
            intersection_index_x++;
    }

    public enum IntersectionType {
        STOP_SIGN, STOP_LIGHT, STOPLIGHT_W_LEFT_ARROW/* , STOPLIGHT_W_LEFT_ARROW_REQUIRED */;
    }

    public enum TrafficFlow {
        NORTH_SOUTH, EAST_WEST, NORTH_SOUTH_LEFT, EAST_WEST_LEFT;
    }

    public enum RoadDirection {
        NORTH, SOUTH, EAST, WEST;
    }

    boolean addRoad(Road road, RoadDirection direction) {
        switch (direction) {
            case NORTH:
                if (north_road == null) {
                    north_road = road;
                    return true;
                } else
                    return false;
            case SOUTH:
                if (south_road == null) {
                    south_road = road;
                    return true;
                } else
                    return false;
            case EAST:
                if (east_road == null) {
                    east_road = road;
                    return true;
                } else
                    return false;
            case WEST:
                if (west_road == null) {
                    west_road = road;
                    return true;
                } else
                    return false;
            default:
                throw new RuntimeException("What the hell kind of RoadDirection is " + direction + "?");
        }
    }

    public LinkedList<Car> getWaitingCars() {
        return waiting;
    }

    public IntersectionType getType() {
        return type;
    }

    public Point getLocation() {
        return location;
    }

    public Road getRoad(RoadDirection direction) {
        switch (direction) {
            case NORTH:
                return north_road;
            case SOUTH:
                return south_road;
            case EAST:
                return east_road;
            case WEST:
                return west_road;
            default:
                throw new RuntimeException("What the hell kind of RoadDirection is " + direction + "?");
        }
    }

    public Road getNorthRoad() {
        return north_road;
    }

    public Road getSouthRoad() {
        return south_road;
    }

    public Road getEastRoad() {
        return east_road;
    }

    public Road getWestRoad() {
        return west_road;
    }

    @Override
    public void paint(Graphics g) {
        // intersections are represented by dark gray squares
        g.setColor(Color.DARK_GRAY);

        // find the max values of the widths of the adjoining roads
        int N_width = north_road.getSELanes() * Road.LANE_WIDTH, S_width = south_road.getNWLanes() * Road.LANE_WIDTH, E_width = east_road.getNWLanes() * Road.LANE_WIDTH, W_width =
                west_road.getSELanes() * Road.LANE_WIDTH;
        int width = N_width > S_width ? N_width : S_width, height = E_width > W_width ? E_width : W_width;

        // draw the intersection
        g.fillRect(location.x - width / 2, location.y - height / 2, width, height);
    }

    @Override
    public String toString() {
        return index.toString() + ":\n" + "N -> " + north_road.getIndex() + "\nS -> " + south_road.getIndex() + "\nE -> " + east_road.getIndex() + "\nW -> "
                + west_road.getIndex();
    }
}
package org.jetbrains.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

@RestController
class ApplicationController {

    @PostMapping("/locations")
    public ResponseEntity<List<Coordinate>> getCoordinates(@RequestBody List<Move> moves) {
        List<Coordinate> coordinates = new ArrayList<>();
        int x = 0, y = 0;
        for (Move move : moves) {
            switch (move.direction) {
                case EAST:
                    x += move.steps;
                    break;
                case WEST:
                    x -= move.steps;
                    break;
                case NORTH:
                    y += move.steps;
                    break;
                case SOUTH:
                    y -= move.steps;
                    break;
            }
            coordinates.add(new Coordinate(x, y));
        }
        return ResponseEntity.ok(coordinates);
    }

    @PostMapping("/moves")
    public ResponseEntity<List<Move>> getMoves(@RequestBody List<Coordinate> locations) {
        List<Move> moves = new ArrayList<>();
        Coordinate current = locations.get(0);
        for (int i = 1; i < locations.size(); i++) {
            Coordinate destination = locations.get(i);
            int dx = destination.x - current.x;
            int dy = destination.y - current.y;
            if (dx != 0) {
                moves.add(new Move(dx > 0 ? Direction.EAST : Direction.WEST, Math.abs(dx)));
            }
            if (dy != 0) {
                moves.add(new Move(dy > 0 ? Direction.NORTH : Direction.SOUTH, Math.abs(dy)));
            }
            current = destination;
        }
        return ResponseEntity.ok(moves);
    }
}

class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Move {
    Direction direction;
    int steps;

    public Move(Direction direction, int steps) {
        this.direction = direction;
        this.steps = steps;
    }
}

enum Direction {
    NORTH, EAST, SOUTH, WEST
}

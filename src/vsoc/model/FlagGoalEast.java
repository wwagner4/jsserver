package vsoc.model;

/**
 * A Flag
 */

class FlagGoalEast extends SimObject {

    private ServerFlag type;

    FlagGoalEast(ServerFlag type, double x, double y) {
        super(x, y);
        this.type = type;
    }

    Vision createVisionForWestPlayer() {
        return new VisionGoalOther(FlagConverter.current().forWest(this.type));
    }

    Vision createVisionForEastPlayer() {
        return new VisionGoalOwn(FlagConverter.current().forEast(this.type));
    }
}
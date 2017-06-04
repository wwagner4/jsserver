package vsoc.server;

/**
 * A Flag
 */
class FlagSouth extends SimObject {

	private static final long serialVersionUID = 1L;
	
    private ServerFlag type;
    
    FlagSouth(ServerFlag type, double x, double y) {
        super(x, y);
        this.type = type;
    }

    Vision createVisionForWestPlayer() {
        return new VisionFlagRight(FlagConverter.current().forWest(this.type));
    }

    Vision createVisionForEastPlayer() {
        return new VisionFlagLeft(FlagConverter.current().forEast(this.type));
    }
}
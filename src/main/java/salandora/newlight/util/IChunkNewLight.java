package salandora.newlight.util;

public interface IChunkNewLight
{
    int[] getNeighborLightChecks();
    void setNeighborLightChecks(int[] in);

    boolean getPendingBoundaryChecks();
    void setPendingBoundaryChecks(boolean value);

    int[] getLightTrackings();
    void setLightTrackings(int[] in);

    int[] getNeighborLightTrackings();
    void setNeighborLightTrackings(int[] in);

    int getNeighborsLoaded();
    void setNeighborsLoaded(int in);

    short getPendingNeighborLightInits();
    void setPendingNeighborLightInits(short in);

    boolean getIsLightPopulated();
    void setIsLightPopulated(boolean value);
}

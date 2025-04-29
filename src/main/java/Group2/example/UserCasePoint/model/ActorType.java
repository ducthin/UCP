package Group2.example.UserCasePoint.model;

public enum ActorType {
    SIMPLE(1),    // External system, API
    AVERAGE(2),   // External system, protocols
    COMPLEX(3);   // Human using GUI
    
    private final int weight;
    
    ActorType(int weight) {
        this.weight = weight;
    }
    
    public int getWeight() {
        return weight;
    }
}

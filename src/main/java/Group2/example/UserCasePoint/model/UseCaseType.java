package Group2.example.UserCasePoint.model;

public enum UseCaseType {
    SIMPLE(5),     // 1-3 transactions
    AVERAGE(10),   // 4-7 transactions
    COMPLEX(15);   // 8+ transactions
    
    private final int weight;
    
    UseCaseType(int weight) {
        this.weight = weight;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public static UseCaseType fromTransactionCount(int count) {
        if (count <= 3) {
            return SIMPLE;
        } else if (count <= 7) {
            return AVERAGE;
        } else {
            return COMPLEX;
        }
    }
}

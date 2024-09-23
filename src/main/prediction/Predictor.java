package main.prediction;

public class Predictor {

    public String predictFromImage(double similarity) {
        if (similarity > 90) {
            return "High similarity, conditions are favorable!";
        } else if (similarity > 50) {
            return "Moderate similarity, conditions are uncertain.";
        } else {
            return "Low similarity, conditions are unfavorable.";
        }
    }
}

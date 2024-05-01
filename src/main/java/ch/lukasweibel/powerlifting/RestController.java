package ch.lukasweibel.powerlifting;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    PowerlifterAnalyse powerlifterAnalyse = new PowerlifterAnalyse();

    @GetMapping("/trainmodel")
    public String getMethodName() {
        // CSVAccessor csvAccessor = new CSVAccessor();
        // ArrayList<Powerlifter> powerlifters = csvAccessor.readEntities();
        powerlifterAnalyse.trainModel();
        return "hello world";
    }

    @PostMapping(path = "/predict", consumes = "application/json")
    public ResponseEntity<Double> predict(@RequestBody Map<String, Object> requestBody) {
        int age = Integer.parseInt((String) requestBody.get("age"));
        float weight = Float.parseFloat((String) requestBody.get("weight"));
        float sex = "F".equalsIgnoreCase((String) requestBody.get("sex")) ? 1f : 0f;
        int equipment = Integer.parseInt((String) requestBody.get("equipment"));
        float[] featuresForPrediction = new float[] { sex, equipment, age, weight };
        double prediction = powerlifterAnalyse.predict(featuresForPrediction);
        return ResponseEntity.ok(prediction);
    }
}

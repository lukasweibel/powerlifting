package ch.lukasweibel.powerlifting;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @GetMapping("/trainmodel")
    public String getMethodName() {
        PowerlifterAnalyse.getInstance().trainModel();
        return "hello world";
    }

    @PostMapping(path = "/predict", consumes = "application/json")
    public ResponseEntity<Double> predict(@RequestBody Map<String, Object> requestBody) {
        int age = Integer.parseInt((String) requestBody.get("age"));
        float weight = Float.parseFloat((String) requestBody.get("weight"));
        float sex = "F".equalsIgnoreCase((String) requestBody.get("sex")) ? 1f : 0f;
        int equipment = Integer.parseInt((String) requestBody.get("equipment"));
        float[] featuresForPrediction = new float[] { sex, equipment, age, weight };
        double prediction = PowerlifterAnalyse.getInstance().predict(featuresForPrediction);
        return ResponseEntity.ok(prediction);
    }
}

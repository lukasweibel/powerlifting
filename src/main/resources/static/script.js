document.getElementById('predictionForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    const age = document.getElementById('age').value;
    const weight = document.getElementById('weight').value;
    const sex = document.getElementById('sex').value;
    const equipment = document.getElementById('equipment').value;

    const data = {
      age: parseInt(age),
      weight: parseFloat(weight),
      sex: sex,
      equipment: parseInt(equipment)
    };

    fetch('/predict', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(data => {
      document.getElementById('predictionResult').innerHTML = 'Prediction: ' + data + ' kg';
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  });
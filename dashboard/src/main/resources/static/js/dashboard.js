var chartOptions = {
  responsive: true,
  legend: {
    position: "top"
  },
  title: {
    display: true,
    text: "COVID-19 Tweet Data"
  },
  scales: {
    yAxes: [{
      ticks: {
        max: 100,
        min: 0,
        stepSize: 10,
        callback: function(value, index, values) {
          return value + '%';
        }
      }
    }]
  }
};

var barChartData = {
  labels: [
    "SARS-CoV-2",
    "Wuhan Virus",
    "Chinese Virus",
    "Coronavirus",
    "COVID-19"
  ],

  datasets: [
    {
      label: "Verified Users",
      backgroundColor: " #166a9c",
      borderColor: "red",
      borderWidth: 0,
      data: [0, 0, 0, 0, 0]
    },
    {
      label: "Unverified Users",
      backgroundColor: "#912e49",
      borderColor: "blue",
      borderWidth: 0,
      data: [0, 0, 0, 0, 0]
    }
  ]
};

var ctx = document.getElementById("chart").getContext("2d");

var chart = new Chart(ctx, {
  type: "bar",
  data: barChartData,
  options: chartOptions
});

var apiUrl = document.getElementById("api_url").value;

var evtSource = new EventSource(apiUrl);

evtSource.onerror = function() {
  console.error("Failed to connect to " + apiUrl);
};

evtSource.addEventListener("new-packet", function(event) { 

  console.log("New server-sent event received")

  let dataPacketJson = event.data;
  let dataPacket = dataPacketHelper(JSON.parse(dataPacketJson));

  chart.data.datasets[0].data = dataPacket.verified_dataset;
  chart.data.datasets[1].data = dataPacket.unverified_dataset;

  document.getElementById("verified_total").innerHTML = dataPacket.verified_total;
  document.getElementById("unverified_total").innerHTML = dataPacket.unverified_total;
  document.getElementById("last_update").innerHTML = dataPacket.build_time;

  chart.update();

});

window.addEventListener("unload", function(event) { 
    evtSource.close();
});

var dataPacketHelper = function(dataPacket) {

    let verifiedUsersData = [];
    let unverifiedUsersData = [];

    let dataArray = dataPacket.data;

    for (let item of dataArray) {

        // Array position is determined by position of same terms in `barChartData.labels`
        switch (item.term) {
          case 'coronavirus':
            verifiedUsersData[3] = item.verified_users_percentage;
            unverifiedUsersData[3] = item.unverified_users_percentage;
            break;

          case 'COVID-19':
            verifiedUsersData[4] = item.verified_users_percentage;
            unverifiedUsersData[4] = item.unverified_users_percentage;
            break;

          case 'SARS-CoV-2':
            verifiedUsersData[0] = item.verified_users_percentage;
            unverifiedUsersData[0] = item.unverified_users_percentage;
            break;

          case 'Chinese Virus':
            verifiedUsersData[2] = item.verified_users_percentage;
            unverifiedUsersData[2] = item.unverified_users_percentage;
            break;

          case 'Wuhan Virus':
            verifiedUsersData[1] = item.verified_users_percentage;
            unverifiedUsersData[1] = item.unverified_users_percentage;
            break;
        }
    }

    return {
      "verified_dataset": verifiedUsersData,
      "unverified_dataset": unverifiedUsersData,
      "verified_total": dataPacket.verified_total,
      "unverified_total": dataPacket.unverified_total,
      "build_time": dataPacket.build_time
    };
};

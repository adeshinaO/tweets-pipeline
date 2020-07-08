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
        beginAtZero: true
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
      backgroundColor: "pink",
      borderColor: "red",
      borderWidth: 0,
      data: [0, 0, 0, 0, 0]
    },
    {
      label: "Unverified Users",
      backgroundColor: "lightblue",
      borderColor: "blue",
      borderWidth: 0,
      data: [0, 0, 0, 0, 0]
    }
  ]
};

var ctx = document.getElementById("chart").getContext("2d");

chart = new Chart(ctx, {
  type: "bar",
  data: barChartData,
  options: chartOptions
});

const evtSource = new EventSource('/data');

evtSource.onerror = function() {
  console.log("Error!"); // todo: better message
};

evtSource.addEventListener("data-update", function(dataPacket) { 

  let data = dataPacketHelper(dataPacket);

  // Pass the two built arrays into chart.data.**

  // Call chart.update()

});

var dataPacketHelper = function(dataPacket) {

    // See: https://www.chartjs.org/docs/latest/developers/updates.html

    // See: https://developer.mozilla.org/en-US/docs/Web/API/EventSource

    // return an object holding two arrays.

    let verifiedUsersData = [];
    let unverifiedUsersData = [];

    let dataArray = dataPacket.data;

    for (const item of dataArray) {
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
      "verified": verifiedUsersData,
      "unverified": unverifiedUsersData
    };
};

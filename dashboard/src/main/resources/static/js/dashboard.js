var chartOptions = {
  responsive: true,
  legend: {
    position: "top"
  },
  title: {
    display: true,
    text: "Cities Tweet Data"
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
    "Amsterdam",
    "Paris",
    "New York",
    "Berlin",
    "London"
  ],

  datasets: [
    {
      label: "Users with >= 1k followers",
      backgroundColor: " #166a9c",
      borderColor: "red",
      borderWidth: 0,
      data: [0, 0, 0, 0, 0]
    },
    {
      label: "Users with < 1k followers",
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

  chart.data.datasets[0].data = dataPacket.one_thousand_followers_dataset;
  chart.data.datasets[1].data = dataPacket.less_thousand_dataset;

  document.getElementById("1k_total").innerHTML = dataPacket.thousand_followers_total;
  document.getElementById("less_1k_total").innerHTML = dataPacket.less_than_thousand_followers_total;
  document.getElementById("last_update").innerHTML = dataPacket.build_time;

  chart.update();

});

window.addEventListener("unload", function(event) { 
    evtSource.close();
});

var dataPacketHelper = function(dataPacket) {

    let thousandFollowersUsers = [];
    let lessThanThousandFollowersUsers = [];

    let dataArray = dataPacket.data;

    for (let item of dataArray) {

        // Array position is determined by position of same terms in `barChartData.labels`
        switch (item.term) {
          case 'Berlin':
            thousandFollowersUsers[3] = item.thousand_followers_percentage;
            lessThanThousandFollowersUsers[3] = item.less_than_thousand_followers_percentage;
            break;
            
          case 'London':
            thousandFollowersUsers[4] = item.thousand_followers_percentage;
            lessThanThousandFollowersUsers[4] = item.less_than_thousand_followers_percentage;
            break;

          case 'Amsterdam':
            thousandFollowersUsers[0] = item.thousand_followers_percentage;
            lessThanThousandFollowersUsers[0] = item.less_than_thousand_followers_percentage;
            break;

          case 'New York':
            thousandFollowersUsers[2] = item.thousand_followers_percentage;
            lessThanThousandFollowersUsers[2] = item.less_than_thousand_followers_percentage;
            break;

          case 'Paris':
            thousandFollowersUsers[1] = item.thousand_followers_percentage;
            lessThanThousandFollowersUsers[1] = item.less_than_thousand_followers_percentage;
            break;
        }
    }

    return {
      "one_thousand_followers_dataset": thousandFollowersUsers,
      "less_thousand_dataset": lessThanThousandFollowersUsers,
      "thousand_followers_total": dataPacket.thousand_followers_total,
      "less_than_thousand_followers_total": dataPacket.less_than_thousand_followers_total,
      "build_time": dataPacket.build_time
    };
};

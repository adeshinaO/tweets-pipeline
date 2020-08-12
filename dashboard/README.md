# About

This is a Spring Boot web application which is essentially the frontend of the pipeline. It renders a simple
grouped bar chart which is created using [Chart.js](https://www.chartjs.org/). The dataset used for the bar chart is
updated continuously from data packages received from the data API through an event stream (Server-Sent Events or SSEs).
    
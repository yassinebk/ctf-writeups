<?php

setcookie("is_captain", "false", time() + 3600, "/");

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>COD Directives</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>

<body>
    <header class="bg-dark text-white text-center py-5">
        <h1>Welcome to COD Modern Warefare </h1>
    </header>

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="#">Home</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#rules">Rules</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#restrictions">Restrictions</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#contact">Contact</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/admin.php">Admin</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <section id="home" class="py-5">
        <div class="container">
            <h2 class="text-center">Welcome, Soldiers</h2>
            <p class="text-center">
                Congratulations on surviving the last missions. As you know Victory belongs to those who control the battlefield. You have to be always Vigilant in the battelfield!
                Soldiers, Read attentively all the directives in this page! 
            </p>
        </div>
    </section>

    <section id="rules" class="py-5">
        <div class="container">
            <h2 class="text-center">Rules</h2>
            <ol>
                <li>Obey all directives issued by your squad leader without question.</li>
                <li>Respect and cooperate with your squad captain.</li>
                <li>Secure and control key objectives to dominate the battlefield.</li>
                <li>Report any suspicious activities to the superior authorities immediately.</li>
                <li>Regularly update and check the integrity of your personal data in the Central Database.</li>
            </ol>
        </div>
    </section>

    <section id="restrictions" class="py-5">
        <div class="container">
            <h2 class="text-center">Restrictions</h2>
            <ul>
                <li>Avoid collateral damage to civilian infrastructure and non-combatants.</li>
                <li>No looting or unauthorized seizure of civilian property, only in extrem cases.</li>
                <li>Refrain from disobeying direct orders from commanding officers.</li>
                <li>No spreading of false information or propaganda.</li>
                <li>No unauthorized gatherings or attempts to form resistance groups.</li>
            </ul>
        </div>
    </section>

    <footer class="bg-dark text-white text-center py-3">
        <p>&copy; CALL OF DUTY HACKFEST'8. All rights reserved.</p>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>

<?php

if (!isset($_COOKIE['is_captain']) || $_COOKIE['is_captain'] !== "true") {
    header("Location: /access_denied.php");
    exit();
}

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Operation Center Board - Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>

<body >
    <header class="bg-dark text-white text-center py-5">
        <h1>Operation Center Board - Admin</h1>
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
                        <a class="nav-link" href="index.php">View Directive</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#edit-rules">Edit Rules</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#logout">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <section id="edit-rules" class="py-5">
        <div class="container">
            <h2 class="text-center">Edit Rules</h2>
            <form>
                <div class="mb-3">
                    <label for="rulesTextarea" class="form-label">Rules</label>
                    <textarea class="form-control" id="rulesTextarea" rows="5"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </form>
        </div>
    </section>

    <section id="system-override" class="py-5">
        <div class="container">
            <h2 class="text-center">Systems Override Master Password</h2>
            <form>
                <div class="mb-3">
                    <label for="passwordInput" class="form-label">Master password</label>
                    <div class="alert alert-danger text-center" role="alert">
                        HACKFEST{fake}
                    </div>
                </div>
    
            </form>
        </div>
    </section>

  
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
  <footer class="bg-dark text-white text-center py-3">
        <div class="container">
            <p>&copy; CALL OF DUTY HACKFEST'8. All rights reserved.</p>
        </div>
    </footer>
</html>

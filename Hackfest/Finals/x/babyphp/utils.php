<?php

class Debugger {
    private $variable;
    private $argument;

    public function __construct($variable, $argument) {
        $this->variable = $variable;
        $this->argument = $argument;
    }

    public function __debugInfo() {
        if ($this->argument === "file") {
            return [
                'file' => (string)$this->argument,
                'content' => file_get_contents($this->variable),
            ];
        }
    
        if ($this->argument === "func") {
            $function = $this->variable;
            return [
                'function' => (string)$this->argument,
                'content' => $function(),
            ];
        }
    }
}



?>
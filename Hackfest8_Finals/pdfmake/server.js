var http = require('http');
var express = require('express');
var path = require('path');
var bodyParser = require('body-parser');

var pdfmake = require('../pdfmake/js/index');

var app = express();

app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.json({ limit: '50mb' }));
app.use(bodyParser.urlencoded({ extended: false }));

function createPdfBinary(docDefinition) {
        var fonts = {
                Roboto: {
                        normal: path.join(__dirname, '../pdfmake', 'examples', '/fonts/Roboto-Regular.ttf'),
                        bold: path.join(__dirname, '../pdfmake', 'examples', '/fonts/Roboto-Medium.ttf'),
                        italics: path.join(__dirname, '../pdfmake', 'examples', '/fonts/Roboto-Italic.ttf'),
                        bolditalics: path.join(__dirname, '../pdfmake', 'examples', '/fonts/Roboto-MediumItalic.ttf')
                }
        };

        pdfmake.setFonts(fonts);

        var pdf = pdfmake.createPdf(docDefinition);
        return pdf.getDataUrl();
}

app.post('/pdf', function (req, res) {

    const dd = req.body;

    if (!dd.content) {
        return res.status(400).send('Invalid document definition.');
    }
        createPdfBinary(dd).then(function (binary) {
                res.contentType('application/pdf');
                res.send(binary);
        }, function (error) {
                res.send('ERROR:' + error);
        });

});



var server = http.createServer(app);
var port = 8080;
server.listen(port);

console.log('http server listening on port %d', port);

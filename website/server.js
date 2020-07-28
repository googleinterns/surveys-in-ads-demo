var http = require('http');
var fs  = require('fs');
var path = require('path');
const express = require('express');
const app = express();

app.get('/', function(req, res){
  fs.readFile('index.html', function(err, data){
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.write(data);
    res.end();
  })
  // fs.readFile('random.js', function(err, data){
  //   res.writeHead(200, {'Content-Type': 'application/javascript'});
  //   res.write(data);
  //   res.end();
  // })
});

app.listen(8000);

app.use(express.static(__dirname + '/public'));

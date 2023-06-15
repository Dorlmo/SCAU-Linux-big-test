var http = require('http');
var url = require('url');
var mysql = require('mysql')

var pool = mysql.createPool({
  host: '8.139.5.39',
  user: 'user',
  password: '123456',
  port: '3306',
  database: 'test'
});

function getStudents() {
  return new Promise(function(resolve, reject) {
    pool.getConnection(function(err, connection) {
      if (err) {
        console.log(err);
        reject(err);
        return;
      }
      var sql = "SELECT * FROM students";
      connection.query(sql, [], function(err, results) {
        connection.release();
        if (err) {
          console.log(err);
          reject(err);
          return;
        }
        resolve(results);
      });
    });
  });
}

server = http.createServer(async function(request, response) {
  response.writeHead(200, { 'Content-Type': 'text/html' });

  response.write(
    '<!DOCTYPE html>' +
    '<html lang="en">' +
    '<head>' +
    '<meta charset="utf-8"/>' +
    '</head>' +
    '<div>Here is Node' +
    '</div>');
  response.write("<table border='1'><tr>");
  try {
    var result = await getStudents();
    result.forEach(r => {
      response.write("<tr>");
      response.write("<td>" + r.stuId + "</td>");
      response.write("<td>" + r.stuName + "</td>");
      response.write("</tr>");
    });
    response.end();
  } catch (error) {
    response.write("An error occurred.");
    response.end();
  }
});

server.listen(8001);
